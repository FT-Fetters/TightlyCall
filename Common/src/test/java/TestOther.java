import org.junit.Test;

public class TestOther {

    @Test
    public void testStr(){
        String m = "{\n" +
                "  \"test\": \"test json\",\n" +
                "  \"pwd\": \"12345\"\n" +
                "}";
        byte[] bytes = m.getBytes();
    }
}
