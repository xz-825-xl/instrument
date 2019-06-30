import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Mmn on 2019/6/30.
 * To be a happy coder!
 */
public class MyApplication {

    private static Logger logger = LogManager.getLogger(MyApplication.class);

    public static void run() throws Exception {
        logger.info("[Application] Starting My application");
        Runner runner = new Runner();
        for(;;){
            runner.run();
        }
    }

}
