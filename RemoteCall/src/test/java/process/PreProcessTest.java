package process;

import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ldqc.tightcall.provider.service.process.BeforeServiceProcess;

public class PreProcessTest implements BeforeServiceProcess {

    private static final Logger log = LoggerFactory.getLogger(PreProcessTest.class);


    @Override
    public void process(Method method, Object[] args) {
        log.info("method: {}, args: {}", method, args);
    }
}
