package src.transformer;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.lang.instrument.*;
import java.util.Objects;

import javassist.*;
import javassist.bytecode.ClassFile;
import src.tracker.ClassTracker;

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
        try {
            pool.getClassLoader().loadClass("src.tracker.TrackerManager");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        CtClass cl = null;
        try {
            cl = pool.makeClass(new java.io.ByteArrayInputStream(b));
//            System.out.println("Transforming: " + cl.getSimpleName());
            if (cl.getName().contains("ClassTracker") || cl.getName().contains("MethodTracker")
                || cl.getName().contains("TrackerManager")){
                cl.detach();
                return b;
            }
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
        String longClassName = method.getDeclaringClass().getName();
        String className = method.getDeclaringClass().getSimpleName();
        String customBlockV2 =
                "if (Thread.currentThread().getStackTrace().length > 2 " +
                "&& !Thread.currentThread().getStackTrace()[2].getClassName().equals(\"" + longClassName + "\")) {" +
                "src.tracker.TrackerManager.getInstance().logMethodCall(" +
                "\""+ className +"\", \"" + method.getName() + "\", " +
                "Thread.currentThread().getStackTrace()[2].getClassName()," +
                "Thread.currentThread().getStackTrace()[2].getMethodName());}";
        method.insertBefore(customBlockV2);

        if (method.getName().equals("main")) {
            method.insertAfter("src.tracker.TrackerManager.getInstance().logAsJSON();");
        }
    }
}
