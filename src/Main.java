package src;

import java.lang.instrument.Instrumentation;

// javac -cp "lib/*" -d bin/ src/*.java
// jar cvfm myAgent.jar MANIFEST.MF -C bin .
public class Main {
  public static void premain(String agentArguments, Instrumentation instrumentation) {
    Class[] classes = instrumentation.getInitiatedClasses(ClassLoader.getSystemClassLoader());
    for (Class c : classes) {
      // if (c.getName().equals(agentArguments))
        // System.out.println(c.getName());
    }
    instrumentation.addTransformer(new CustomTransformer(agentArguments));
  }
}
