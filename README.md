# TightlyCall

Lightweight, easy-to-use remote call framework

## Description

Based on NIO to implement service communication.  
The client and server of TCP communication are developed in imitation of the form of netty.  
The usage method is inspired by OpenFeign 's Http client.  
Although it has not been integrated into Springboot 's SIP, it can still be used in Springboot.

## Getting Started

Since it has not been uploaded to the Maven repository, it needs to be installed locally.

### Step 1

pull project from GitHub  
current newest branch iss dev-1.0.1

```shell
mvn clean -f pom.xml
mvn install -f pom.xml
```

after run these command, the jar will install in local maven repository.

### Step 2

import pom

Common

```xml

<dependency>
    <groupId>xyz.ldqc</groupId>
    <artifactId>Common</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

Registry

```xml

<dependency>
    <groupId>xyz.ldqc</groupId>
    <artifactId>Registry</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

RemoteCall

```xml

<dependency>
    <groupId>xyz.ldqc</groupId>
    <artifactId>RemoteCall</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### Step 3

After completing the above steps, you can enter the formal code development😀  
The example here will integrate Springboot. If you don't want to use Springboot, the steps are similar.  
Create a module for registry, is very simply

```java
public class Main {
    public static void main(String[] args) {
        // registry
        RegistryServerApplication.builder()
                .bind(1234) // port
                .registerServer(DefaultRegisterServer.class)
                .indexRoom(null)
                .boot();
    }
}
```

and boot it, now you get a registry for tightly call, see, it is very easy.

Now create a module for the service provider, the steps are a little more complicated than the previous step, but still
very simple😁.

```java

@OpenScan(packageName = "org.example.provider.service.tc", scanner = DefaultServiceScanner.class, type = ServiceRegisterFactory.Type.DEFAULT)
@OpenRegClient(host = "127.0.0.1", port = 1234, name = "test")
@ProviderConfig
@Component
public class ProviderClient {
    @Bean
    public ProviderApplication getProviderApplication() {
        return ProviderApplication.run(ProviderClient.class);
    }
}
```

Using @Component and @Bean to make spring can manage it.  
@OpenRegClient is used to configure the connected registry, and service name.  
@OpenScan is used to set the path of the service-providing package scan and some other configurations.  
Next, you can complete writing the classes and codes for the services to be provided. Note that you need to create
classes under the defined package scan path. This is the code for a simple service provider class:

```java
package org.example.provider.service.tc;

import xyz.ldqc.tightcall.provider.annotation.OpenMapping;
import xyz.ldqc.tightcall.provider.annotation.OpenService;

@OpenService
@OpenMapping("/service")
public class ServiceTest {
    @OpenMapping("/test")
    public String test(int a) {
        return a + "111";
    }
}
```

It defines a service with an access path of /service/test, and provider is done.  
The last step is how to remote call this service, it's simple too.  
First we also need to create a bean so that Springboot can manage the client of Tightly call.

```java
package org.example.consumer.client;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import xyz.ldqc.tightcall.consumer.ConsumerApplication;
import xyz.ldqc.tightcall.consumer.annotation.TightCallConfig;
import xyz.ldqc.tightcall.consumer.annotation.TightlyCallScan;

@TightCallConfig(registerHost = "127.0.0.1", registerPort = 1234)
@TightlyCallScan(packageName = "org.example.consumer.open")
@Component
public class ConsumerClient {

    @Bean("cons")
    public ConsumerApplication getConsumerApplication() {
        return ConsumerApplication.run(ConsumerClient.class);
    }
}
```

Then create an interface class to define the method information of the accessed service, it must in the package where we
set up.

```java
package org.example.consumer.open;

import xyz.ldqc.tightcall.common.annotation.OpenMapping;
import xyz.ldqc.tightcall.consumer.annotation.TightlyCallClient;

@TightlyCallClient(serviceName = "test")
public interface ServiceTest {

    @OpenMapping("/service/test")
    String test(int a);
}
```

Next, we can call this service. In order to call this service, we need to create a Controller to access it.

```java
package org.example.consumer.rest;

import org.example.consumer.open.ServiceTest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.ldqc.tightcall.consumer.ConsumerApplication;

@RestController
@RequestMapping("/test")
public class TestController {

    private final ConsumerApplication consumerApplication;

    public TestController(@Qualifier(value = "cons") ConsumerApplication consumerApplication) {
        this.consumerApplication = consumerApplication;
    }

    @RequestMapping("/test")
    public String test() {
        ServiceTest callClient = consumerApplication.getCallClient(ServiceTest.class);
        return callClient.test(123);
    }

}
```

In the end, boot the three service.
Request the interface we defined: http://localhost:8081/test/test
and we can see the result.
