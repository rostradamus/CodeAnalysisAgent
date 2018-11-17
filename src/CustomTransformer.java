package src;

import java.security.*;
import java.lang.instrument.*;
import java.util.*;
import javassist.*;

public class CustomTransformer implements ClassFileTransformer {
    private String target;

    public CustomTransformer(String target) {
        super();
        this.target = target;
    }

    private boolean isLibrary(String className) {
        return className.startsWith("java") || className.startsWith("sun") || className.startsWith("jdk");
    }

    public byte[] transform(ClassLoader loader, String className, Class redefiningClass, ProtectionDomain domain, byte[] bytes) {
        if (isLibrary(className)) return bytes;
        System.out.println("Transforming: " + className);
        return transformClass(redefiningClass,bytes);
    }

    private byte[] transformClass(Class classToTransform, byte[] b) {
        ClassPool pool = ClassPool.getDefault();
        CtClass cl = null;
        try {
            cl = pool.makeClass(new java.io.ByteArrayInputStream(b));
            CtBehavior[] methods = cl.getDeclaredBehaviors();
            for (CtBehavior method : methods) {
                if (!method.isEmpty()) {
                    changeMethod(method);
                }
            }
            b = cl.toBytecode();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (cl != null) {
                cl.detach();
            }
        }
        return b;
    }

    private void changeMethod(CtBehavior method) throws CannotCompileException {
        method.insertBefore("System.out.println(\"Running method:" + method.getName() + " in " + method.getDeclaringClass().getName() + "\");");

    }
}
