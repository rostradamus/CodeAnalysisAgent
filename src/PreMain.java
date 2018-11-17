package src;

import java.lang.instrument.Instrumentation;

public class PreMain {
  public static void premain(String agentArguments, Instrumentation instrumentation) {
    System.out.println("Entered premain of analysis agent");
    instrumentation.addTransformer(new CustomTransformer());
    System.out.println("exiting premain of analysis agent");
  }
}
