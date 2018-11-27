package src;

import src.tracker.TrackerManager;
import src.transformer.CustomTransformer;

import java.lang.instrument.Instrumentation;

public class PreMain {
  public static void premain(String agentArguments, Instrumentation instrumentation) {
      System.out.println("Entered premain of analysis agent");
      instrumentation.addTransformer(new CustomTransformer());
      System.out.println("exiting premain of analysis agent");
  }

  public static void main(String args[]) {
      System.out.println("hello from main!");
      TrackerManager tr = TrackerManager.getInstance();
      tr.logMethodCall("ClassA", "MethodA-a", "ClassB", "MethodB-a");
      tr.logMethodCall("ClassB", "MethodB-a", "ClassA", "MethodA-a");
      tr.logMethodCall("ClassB", "MethodB-b", "ClassA", "MethodA-a");
      tr.logMethodCall("ClassA", "MethodA-a", "ClassB", "MethodB-b");
      tr.logMethodCall("ClassA", "MethodA-a", "ClassB", "MethodB-b");
      tr.logMethodCall("ClassA", "MethodA-a", "ClassB", "MethodB-b");
      tr.logMethodCall("ClassA", "MethodA-a", "ClassB", "MethodB-b");
      tr.logMethodCall("ClassB", "MethodB-a", "ClassA", "MethodA-a");
      tr.logMethodCall("ClassC", "MethodC-a", "ClassA", "MethodA-a");
      tr.logAsJSON();
  }
}
