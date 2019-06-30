import com.sun.tools.attach.VirtualMachine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Optional;

/**
 * Created by Mmn on 2019/6/30.
 * To be a happy coder!
 */
public class AgentLoader {

    private static Logger logger = LogManager.getLogger(AgentLoader.class);

    public static void run() {
        //指定jar路径
        String agentFilePath = "/Users/luoyanjie/IdeaProjects/instrument/targetApp/target/targetApp-1.0-SNAPSHOT.jar";

        //需要attach的进程标识
        String applicationName = "targetApp";

        //查到需要监控的进程
        Optional<String> jvmProcessOpt = Optional.ofNullable(VirtualMachine.list()
                .stream()
                .filter(jvm -> {
                    logger.info("jvm:{}", jvm.displayName());
                    return jvm.displayName().contains(applicationName);
                })
                .findFirst().get().id());

        if (!jvmProcessOpt.isPresent()) {
            logger.error("Target Application not found");
            return;
        }
        File agentFile = new File(agentFilePath);
        try {
            String jvmPid = jvmProcessOpt.get();
            logger.info("Attaching to target JVM with PID: " + jvmPid);
            VirtualMachine jvm = VirtualMachine.attach(jvmPid);
            jvm.loadAgent(agentFile.getAbsolutePath());
            jvm.detach();
            logger.info("Attached to target JVM and loaded Java agent successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
