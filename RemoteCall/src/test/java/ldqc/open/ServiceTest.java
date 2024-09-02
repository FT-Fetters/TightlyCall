package ldqc.open;

import xyz.ldqc.tightcall.common.annotation.OpenMapping;
import xyz.ldqc.tightcall.consumer.annotation.TightlyCallClient;
import xyz.ldqc.tightcall.consumer.proxy.interceptor.extra.CallBody;
import xyz.ldqc.tightcall.consumer.proxy.interceptor.extra.CallResult;

@TightlyCallClient()
public interface ServiceTest {

    @OpenMapping("/service/test")
    CallResult<String> test(CallBody callBody);
}
