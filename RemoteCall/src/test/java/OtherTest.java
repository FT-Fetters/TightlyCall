import org.junit.Test;
import xyz.ldqc.tightcall.util.Path;

import java.net.InetSocketAddress;

public class OtherTest {


    @Test
    public void pathTest(){
        String path = "";

        Path p = new Path(path);

        path = "/te";
        p.append(path);
        System.out.println("p.getPath() = " + p.getPath());
        p.append("/");
        System.out.println("p.getPath() = " + p.getPath());

    }

    @Test
    public void testInetSocketAddress(){
        InetSocketAddress unresolved = InetSocketAddress.createUnresolved("127.0.0.1", 123);
        InetSocketAddress unresolved1 = InetSocketAddress.createUnresolved("127.0.0.1", 123);
        System.out.println(unresolved1.equals(unresolved));

    }

}
