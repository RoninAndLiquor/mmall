import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CyclicBarrierTest {

    public static void main(String[] args) {
        Long l1 = 2324234234234l;
        Long l2 = 2324234234234l;
        System.out.println(l1.equals(l2));
        int count = 3;
        final CyclicBarrier cb = new CyclicBarrier(count,new Runnable(){
            public void run(){
                System.out.println("Personal is come on all");
            }
        });
        ExecutorService es = Executors.newCachedThreadPool();
        for(int i=1;i<=count;i++){
            final int temp = i;
            es.execute(new Runnable() {
                @Override
                public void run() {
                    try{
                        System.out.println("in NO."+temp);
                        cb.await();
                        System.out.println("out NO."+temp);
                    }catch (Exception e){

                    }
                }
            });
        }
        es.shutdown();
    }

}
