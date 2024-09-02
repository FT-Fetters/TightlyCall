package ldqc;

import java.util.Map;
import ldqc.open.ServiceTest;
import xyz.ldqc.tightcall.consumer.ConsumerApplication;
import xyz.ldqc.tightcall.consumer.annotation.TightlyCallScan;
import xyz.ldqc.tightcall.consumer.proxy.interceptor.extra.CallBody;
import xyz.ldqc.tightcall.consumer.proxy.interceptor.extra.CallResult;

//@TightCallConfig(registerHost = "127.0.0.1", registerPort = 1234)
@TightlyCallScan(packageName = "ldqc.open")
public class ConsumerClient {


    public static void main(String[] args) {
        ConsumerApplication consumerApplication = ConsumerApplication.run(ConsumerClient.class);
        ServiceTest callClient = consumerApplication.getCallClient(ServiceTest.class);
        CallBody callBody = new CallBody(String.class)
            .set("127.0.0.1:6770", "123");
//            .set("127.0.0.1:6771", "456");
        CallResult<String> callResult = callClient.test(callBody);
        Map<String, String> result = callResult.result();
        result.forEach((k, v) -> System.out.println(k + ":" + v));
    }
}
