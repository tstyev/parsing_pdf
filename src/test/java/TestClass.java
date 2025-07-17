import org.testng.annotations.Test;

public class TestClass {

    @Test
    void test() throws InterruptedException {
        System.out.println("test");
        Thread.sleep(10000);
        assert 1 == 1;
    }
}