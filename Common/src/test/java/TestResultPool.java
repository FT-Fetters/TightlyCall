import org.junit.Test;
import xyz.ldqc.tightcall.pool.support.BlockResultPool;

public class TestResultPool {

    @Test
    public void blockResultPool(){
        BlockResultPool<String, String> blockResultPool = new BlockResultPool<>();

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                blockResultPool.put("test", "test");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(5000);
                blockResultPool.put("test1", "test1");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        System.out.println(blockResultPool.getResult("test"));

        new Thread(() -> {
            System.out.println(blockResultPool.getResult("test1", 2));
        }).start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
