# TightlyCall

轻量级、易于使用的远程调用框架  
[Readme en](README.md)

## 描述

基于 NIO 实现服务通信。TCP 通信的客户端和服务器开发模仿了 Netty 的形式。使用方法受到 OpenFeign 的 HTTP 客户端的启发。尽管尚未集成到
Spring Boot 的 SPI 中，但仍可在 Spring Boot 中使用。

## 入门指南

由于尚未上传到 Maven 仓库，需要在本地安装。

### 步骤 1

从 GitHub 上拉取项目  
当前最新分支为 dev-1.0.1

```shell
mvn clean -f pom.xml
mvn install -f pom.xml
```

运行这些命令后，JAR 包将安装在本地 Maven 仓库中。

### 步骤 2

导入 POM 文件

Common

```xml

<dependency>
    <groupId>xyz.ldqc</groupId>
    <artifactId>tightly-call-common</artifactId>
    <version>1.0.1</version>
</dependency>
```

Registry

```xml

<dependency>
    <groupId>xyz.ldqc</groupId>
    <artifactId>tightly-call-registry</artifactId>
    <version>1.0.1</version>
</dependency>
```

RemoteCall

```xml

<dependency>
  <groupId>xyz.ldqc</groupId>
  <artifactId>tightly-call-remote-call</artifactId>
  <version>1.0.1</version>
</dependency>
```

### 步骤 3

完成上述步骤后，您可以开始正式的代码开发😀  
这里的示例将集成到 Spring Boot 中。如果您不想使用 Spring Boot，则步骤类似。  
创建一个注册中心模块非常简单

```java
public class Main {
    public static void main(String[] args) {
        // 注册中心
        RegistryServerApplication.builder()
                .bind(1234) // 端口
                .registerServer(DefaultRegisterServer.class)
                .indexRoom(null)
                .boot();
    }
}
```

启动它，现在您就拥有了一个用于TightlyCall的注册中心，看，非常简单。

现在创建一个服务提供者模块，比上一步骤稍微复杂一些，但仍然非常简单😁。

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

使用 @Component 和 @Bean 让 Spring 管理它。  
@OpenRegClient 用于配置连接的注册中心和服务名称。  
@OpenScan 用于设置服务提供包扫描的路径和其他一些配置。  
接下来，您可以完成要提供的服务的类和代码的编写。请注意，您需要在定义的包扫描路径下创建类。这是一个简单的服务提供者类的代码示例：

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

它定义了一个访问路径为 /service/test 的服务，提供者就完成了。  
最后一步是如何远程调用这个服务，这也很简单。  
首先，我们还需要创建一个 bean，以便 Spring Boot 可以管理 Tightly call 的客户端。

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

然后创建一个接口类来定义访问服务的方法信息，它必须在我们设置的包中。

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

接下来，我们可以调用这个服务。为了调用这个服务，我们需要创建一个 Controller 来访问它。

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

最后，启动这三个服务。请求我们定义的接口：
http://localhost:8081/test/test，我们就可以看到结果了。

## LICENSE
MIT ©[Fetters](LICENSE)
