package xyz.ldqc.tightcall.consumer.proxy.support;

import xyz.ldqc.tightcall.consumer.discovery.DiscoveryClient;
import xyz.ldqc.tightcall.consumer.proxy.ClientProxy;
import xyz.ldqc.tightcall.exception.ProxyException;
import xyz.ldqc.tightcall.util.PackageUtil;

import java.io.IOException;
import java.util.List;

/**
 * @author Fetters
 */
public class CglibClientProxy implements ClientProxy {

    private String packageName;

    private DiscoveryClient discoveryClient;

    public CglibClientProxy(){
    }

    @Override
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public void setDiscoveryClient(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    public List<Object> doProxy() {
        List<Class<?>> classes = scanAllClasses();
        return null;
    }

    public List<Class<?>> scanAllClasses(){
        try {
            return PackageUtil.getPackageClasses(packageName);
        } catch (IOException e) {
            throw new ProxyException("scan classes fail");
        }
    }
}
