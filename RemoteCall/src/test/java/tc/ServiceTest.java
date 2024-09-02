package tc;

import inject.InjectTest;
import xyz.ldqc.tightcall.common.annotation.Inject;
import xyz.ldqc.tightcall.provider.annotation.OpenMapping;
import xyz.ldqc.tightcall.provider.annotation.OpenService;

@OpenService
@OpenMapping("/service")
public class ServiceTest {

    @Inject
    public InjectTest getInjectTest(){
        return new InjectTest("bk");
    }


    @OpenMapping("/test")
    public String test(String a){
        InjectTest injectTest = getInjectTest();
        String name = injectTest.getName();
        return a + "111 inject: " + name;
//        return a + "111";
    }
}
