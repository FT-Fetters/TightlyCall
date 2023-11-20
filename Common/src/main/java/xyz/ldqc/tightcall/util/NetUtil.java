package xyz.ldqc.tightcall.util;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author Fetters
 */
public class NetUtil {

    /**
     * 判断是否端口是否可用
     */
    public static boolean portAvailable(int port){
        try (ServerSocket server = new ServerSocket(port)){
            return false;
        } catch (IOException ignored) {}
        return true;
    }


}
