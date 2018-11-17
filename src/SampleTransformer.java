import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.*;
import java.lang.instrument.*;
import java.util.*;
import javassist.*;

public class SampleTransformer implements ClassFileTransformer {
  private String runningClass;

  public SampleTransformer(String runningClass) {
    super();
    this.runningClass = runningClass;
  }

  public byte[] transform(ClassLoader loader, String className, Class redefiningClass, ProtectionDomain domain, byte[] bytes) throws IllegalClassFormatException {
    if (!className.equals(runningClass)) return bytes;
    System.out.println("hello " + className);
    System.out.println(bytes);
    return transformClass(redefiningClass,bytes);
  }

  private byte[] transformClass(Class classToTransform, byte[] b) {
    ClassPool pool = ClassPool.getDefault();
    CtClass cl = null;
    try {
      cl = pool.makeClass(new java.io.ByteArrayInputStream(b));
      CtBehavior[] methods = cl.getDeclaredBehaviors();
      for (CtBehavior method : methods) {
        System.out.println(method);
        if (!method.isEmpty()) {
          // System.out.println("Hello " + method.getName());
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

  private void changeMethod(CtBehavior method) throws NotFoundException, CannotCompileException {
    method.insertBefore("System.out.println(\"started method at \" + new java.util.Date());");
    method.insertAfter("System.out.println(\"ended method at \" + new java.util.Date());");
  }
}
