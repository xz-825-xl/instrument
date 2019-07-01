import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

/**
 * @author zy
 * To be a happy coder!
 */
public class Transformer implements ClassFileTransformer {

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer){
        byte[] transformed = null;
        System.out.println("Transforming " + className);
        ClassPool pool;
        CtClass cl = null;
        try {
            pool = ClassPool.getDefault();

            cl = pool.makeClass(new java.io.ByteArrayInputStream(
                    classfileBuffer));

            if (!cl.isInterface()) {
                CtMethod[] methods = cl.getDeclaredMethods();
                for (CtMethod method : methods) {
                    if (!method.isEmpty()) {
                        aopInsertMethod(method);
                    }
                }
                transformed = cl.toBytecode();
            }
        } catch (Exception e) {
            System.err.println("Could not instrument  " + className
                    + ",  exception : " + e.getMessage());
        } finally {
            if (cl != null) {
                cl.detach();
            }
        }
        return transformed;
    }

    private void aopInsertMethod(CtMethod method) throws CannotCompileException {
        //situation 1:添加监控时间
        method.instrument(new ExprEditor() {
            @Override
            public void edit(MethodCall m) throws CannotCompileException {
                m.replace("{ long stime = System.currentTimeMillis(); $_ = $proceed($$);System.out.println(\""
                        + m.getClassName() + "." + m.getMethodName()
                        + " cost:\" + (System.currentTimeMillis() - stime) + \" ms\");}");
            }
        });
        //situation 2:在方法体前后语句
//      method.insertBefore("System.out.println(\"enter method\");");
//      method.insertAfter("System.out.println(\"leave method\");");
    }

}
