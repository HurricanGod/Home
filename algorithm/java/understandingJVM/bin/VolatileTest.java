public class VolatileTest {

    private VolatileTest(){

    }

    private static volatile VolatileTest instance;

    public static VolatileTest getInstance(){
        if(instance == null){
            synchronized (VolatileTest.class){
                if(instance == null){
                    instance = new VolatileTest();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        VolatileTest instance = getInstance();
        System.out.println("end...");
    }
}
