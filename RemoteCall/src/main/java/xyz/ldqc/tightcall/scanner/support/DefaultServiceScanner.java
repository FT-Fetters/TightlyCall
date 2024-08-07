package xyz.ldqc.tightcall.scanner.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ldqc.tightcall.provider.annotation.OpenMapping;
import xyz.ldqc.tightcall.provider.annotation.OpenService;
import xyz.ldqc.tightcall.scanner.ServiceScanner;
import xyz.ldqc.tightcall.registry.server.request.ServiceDefinition;
import xyz.ldqc.tightcall.util.ClassUtil;
import xyz.ldqc.tightcall.util.PackageUtil;
import xyz.ldqc.tightcall.util.Path;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fetters
 */
public class DefaultServiceScanner implements ServiceScanner {

    private final static Logger log = LoggerFactory.getLogger(DefaultServiceScanner.class);

    private String packagePath;

    private Class<?> runClass;

    public DefaultServiceScanner(String packagePath, Class<?> runClass) {
        this.packagePath = packagePath;
        this.runClass = runClass;
    }

    public DefaultServiceScanner(){

    }


    @Override
    public List<ServiceDefinition> doScan() {
        List<ServiceDefinition> definitions = new ArrayList<>();
        List<Class<?>> classList = PackageUtil.getPackageClasses(packagePath, runClass);
        log.info("scan class: {}", classList.size());
        classList.forEach(c -> doScanClass(c, definitions));
        return definitions;
    }

    @Override
    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    @Override
    public void setRunClass(Class<?> runClass) {
        this.runClass = runClass;
    }

    private void doScanClass(Class<?> clazz, List<ServiceDefinition> definitions) {
        OpenService openService = clazz.getAnnotation(OpenService.class);
        if (openService == null) {
            return;
        }
        List<ServiceDefinition> serviceDefinitions = getServiceDefinitionsByClass(clazz);
        definitions.addAll(serviceDefinitions);
    }

    private List<ServiceDefinition> getServiceDefinitionsByClass(Class<?> clazz) {
        OpenMapping openMapping = clazz.getAnnotation(OpenMapping.class);
        StringBuilder mappingBuilder = new StringBuilder();
        // 如果类上的有openMapping注解且值不为空则添加至mapping上
        if (openMapping != null && openMapping.value() != null && !openMapping.value().isEmpty()) {
            mappingBuilder.append(openMapping.value());
        }
        Method[] methods = clazz.getMethods();
        Path path = new Path(mappingBuilder.toString());
        return getServiceDefinitionsByMethods(path, clazz, methods);
    }

    private List<ServiceDefinition> getServiceDefinitionsByMethods(
            Path path, Class<?> clazz, Method... methods) {
        List<ServiceDefinition> definitions = new ArrayList<>(methods.length);
        for (Method method : methods) {
            if (method == null) {
                continue;
            }
            ServiceDefinition serviceDefinition = doGetServiceDefinition(path, method, clazz);
            if (serviceDefinition != null) {
                definitions.add(serviceDefinition);
            }
        }
        return definitions;
    }

    private ServiceDefinition doGetServiceDefinition(Path path, Method method, Class<?> clazz) {
        OpenMapping openMapping = method.getAnnotation(OpenMapping.class);
        if (openMapping == null) {
            return null;
        }
        String pathValue = openMapping.value();
        path.append(pathValue);
        ServiceDefinition serviceDefinition = new ServiceDefinition();
        serviceDefinition.setClazz(clazz.getName());
        serviceDefinition.setPath(path.getPath());
        serviceDefinition.setMethod(method.getName());
        serviceDefinition.setParamTypes(ClassUtil.getMethodParamTypes(method));
        return serviceDefinition;
    }


}
