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
        return transformClass(redefiningClass,bytes);
    }

    private byte[] transformClass(Class classToTransform, byte[] b) {
        ClassPool pool = ClassPool.getDefault();
        CtClass cl = null;
        try {
            cl = pool.makeClass(new java.io.ByteArrayInputStream(b));
            System.out.println("Transforming: " + cl.getSimpleName());
            CtBehavior[] methods = cl.getDeclaredBehaviors();


            for (CtBehavior method : methods) {
                if (!method.isEmpty() && !method.getName().contains("lambda")) {
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
        String className = method.getDeclaringClass().getSimpleName();
        method.insertBefore("System.out.println(\"In class: " + className + ", Entering method:" + method.getName() + "\");");
        method.insertAfter("System.out.println(\"In class: " + className + ", Exiting method:" + method.getName() + "\");");
    }
}
