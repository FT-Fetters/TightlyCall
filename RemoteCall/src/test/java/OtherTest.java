import org.junit.Test;
import xyz.ldqc.tightcall.util.Path;

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

}
