package xyz.ldqc.tightcall.provider.scanner;

import xyz.ldqc.tightcall.provider.service.ServiceDefinition;

import java.util.List;

/**
 * @author Fetters
 */
public interface ServiceScanner {

    /**
     * 启动扫描并获取结果
     * @return 扫描结果
     */
    List<ServiceDefinition> doScan();

    void setPackagePath(String packagePath);
}
