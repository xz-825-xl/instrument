import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.instrument.Instrumentation;

/**
 * @author zy
 * To be a happy coder!
 */
public class MyInstrumentationAgent {

    private static Logger logger = LogManager.getLogger(MyInstrumentationAgent.class);

    public static void agentmain(String agentArgs, Instrumentation inst) {
        logger.info("[Agent] In agentmain method");

        //需要监控的类
        String className = "Runner";
        transformClass(className, inst);
    }

    private static void transformClass(String className, Instrumentation instrumentation) {
        Class<?> targetCls = null;
        ClassLoader targetClassLoader = null;
        // see if we can get the class using forName
        try {
            targetCls = Class.forName(className);
            targetClassLoader = targetCls.getClassLoader();
            transform(targetCls, targetClassLoader, instrumentation);
            return;
        } catch (Exception ex) {
            logger.error("Class [{}] not found with Class.forName");
        }
        // otherwise iterate all loaded classes and find what we want
        for(Class<?> clazz: instrumentation.getAllLoadedClasses()) {
            if(clazz.getName().equals(className)) {
                targetCls = clazz;
                targetClassLoader = targetCls.getClassLoader();
                transform(targetCls, targetClassLoader, instrumentation);
                return;
            }
        }
        throw new RuntimeException("Failed to find class [" + className + "]");
    }

    private static void transform(Class<?> clazz, ClassLoader classLoader, Instrumentation instrumentation) {
        MyTransformer dt = new MyTransformer(clazz.getName(), classLoader);
        instrumentation.addTransformer(dt, true);
        try {
            System.out.println("transform: " + clazz.getName());
            instrumentation.retransformClasses(clazz);
        } catch (Exception ex) {
            throw new RuntimeException("Transform failed for class: [" + clazz.getName() + "]", ex);
        }
    }
}
