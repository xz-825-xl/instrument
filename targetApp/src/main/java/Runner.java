

/**
 * Created by Mmn on 2019/6/30.
 * To be a happy coder!
 */
public class Runner {

    public void run() throws InterruptedException {
        long sleep = (long) (Math.random() * 1000 + 10000);
        Thread.sleep(sleep);
        System.out.println("run in " + sleep);
    }
}
