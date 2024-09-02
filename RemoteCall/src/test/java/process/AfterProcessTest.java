package process;

import java.lang.reflect.Method;
import xyz.ldqc.tightcall.provider.service.process.AfterServiceProcess;

public class AfterProcessTest implements AfterServiceProcess {

    @Override
    public Object process(Method method, Object[] objects, Object o) {
        return ((String) o) + "after";
    }
}
