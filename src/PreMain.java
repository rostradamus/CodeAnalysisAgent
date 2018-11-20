package src;

import src.transformer.CustomTransformer;

import java.lang.instrument.Instrumentation;

public class PreMain {
  public static void premain(String agentArguments, Instrumentation instrumentation) {
    String target = agentArguments.substring(0, agentArguments.lastIndexOf('.'));
    System.out.println("Entered premain of analysis agent");
    instrumentation.addTransformer(new CustomTransformer(target));
    System.out.println("exiting premain of analysis agent");
  }
}
