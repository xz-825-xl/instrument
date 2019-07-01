import com.sun.tools.attach.VirtualMachine;

import java.io.File;
import java.util.Optional;


/**
 * @author zy
 * To be a happy coder!
 */
public class AgentMain {
    public static void main(String[] args) {
        //指定jar路径
        String agentFilePath = "/Users/luoyanjie/IdeaProjects/instrument/targetApp/target/targetApp-1.0-SNAPSHOT.jar";

        //需要attach的进程标识
        String applicationName = "targetApp";

        //查到需要监控的进程
        Optional<String> jvmProcessOpt = Optional.ofNullable(VirtualMachine.list()
                .stream()
                .filter(jvm -> {
                    System.out.println(String.format("jvm:%s" , jvm.displayName()));
                    return jvm.displayName().contains(applicationName);
                })
                .findFirst().get().id());

        if (!jvmProcessOpt.isPresent()) {
            System.out.println("Target Application not found");
            return;
        }
        File agentFile = new File(agentFilePath);
        try {
            String jvmPid = jvmProcessOpt.get();
            System.out.println("Attaching to target JVM with PID: " + jvmPid);
            VirtualMachine jvm = VirtualMachine.attach(jvmPid);
            jvm.loadAgent(agentFile.getAbsolutePath());
            jvm.detach();
            System.out.println("Attached to target JVM and loaded Java agent successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}