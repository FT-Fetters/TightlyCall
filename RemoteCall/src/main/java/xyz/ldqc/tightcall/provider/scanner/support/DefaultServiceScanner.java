package xyz.ldqc.tightcall.provider.scanner.support;

import xyz.ldqc.tightcall.provider.scanner.ServiceScanner;
import xyz.ldqc.tightcall.provider.service.ServiceDefinition;

import java.util.List;

/**
 * @author Fetters
 */
public class DefaultServiceScanner implements ServiceScanner {

    private final String packagePath;

    public DefaultServiceScanner(String packagePath){
        this.packagePath = packagePath;
    }


    @Override
    public List<ServiceDefinition> doScan() {

        return null;
    }
}
