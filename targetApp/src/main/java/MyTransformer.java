import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

/**
 * @author zy
 * To be a happy coder!
 */
public class MyTransformer implements ClassFileTransformer {

    private static Logger logger = LogManager.getLogger(MyTransformer.class);

    /**
     * 需要监控的方法
     */
    private static final String WITHDRAW_MONEY_METHOD = "run";

    /**
     * The internal form class name of the class to transform
     */
    private String targetClassName;
    /**
     * The class loader of the class we want to transform
     */
    private ClassLoader targetClassLoader;

    public MyTransformer(String targetClassName, ClassLoader targetClassLoader) {
        this.targetClassName = targetClassName;
        this.targetClassLoader = targetClassLoader;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        byte[] byteCode = classfileBuffer;
        //replace . with /
        String finalTargetClassName = this.targetClassName.replaceAll("\\.", "/");
        if (!className.equals(finalTargetClassName)) {
            return byteCode;
        }

        if (className.equals(finalTargetClassName) && loader.equals(targetClassLoader)) {
            logger.info("[Agent] Transforming class" + className);
            try {
                ClassPool cp = ClassPool.getDefault();
                ClassClassPath ccpath = new ClassClassPath(Runner.class);
                cp.insertClassPath(ccpath);
                CtClass cc = cp.get(targetClassName);
                CtMethod m = cc.getDeclaredMethod(WITHDRAW_MONEY_METHOD);

                // 开始时间
                m.addLocalVariable("startTime", CtClass.longType);
                m.insertBefore("startTime = System.currentTimeMillis();");

                StringBuilder endBlock = new StringBuilder();

                // 结束时间
                m.addLocalVariable("endTime", CtClass.longType);
                endBlock.append("endTime = System.currentTimeMillis();");

                // 时间差
                m.addLocalVariable("opTime", CtClass.longType);
                endBlock.append("opTime = endTime-startTime;");

                // 打印方法耗时
                endBlock.append("System.out.println(\"completed in:\" + opTime + \" millis!\");");

                m.insertAfter(endBlock.toString());

                byteCode = cc.toBytecode();
                cc.detach();
            } catch (Exception e) {
                logger.error("Exception", e);
            }
        }
        return byteCode;
    }
}
