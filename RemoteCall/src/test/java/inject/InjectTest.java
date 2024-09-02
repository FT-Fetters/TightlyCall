package inject;

/**
 * @author Fetters
 */
public class InjectTest {

    private String name;

    public InjectTest(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
