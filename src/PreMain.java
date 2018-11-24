package src;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.Loader;
import javassist.NotFoundException;
import src.tracker.TrackerManager;
import src.transformer.CustomTransformer;

import java.lang.instrument.Instrumentation;

public class PreMain {
  public static void premain(String agentArguments, Instrumentation instrumentation) {

      String target = agentArguments.substring(0, agentArguments.lastIndexOf('.'));
      System.out.println("Entered premain of analysis agent");
//      poc();
      instrumentation.addTransformer(new CustomTransformer(target));
      System.out.println("exiting premain of analysis agent");
  }

  private static void poc() {
      ClassPool pool = ClassPool.getDefault();
      Loader l = new Loader(pool);

      try {
          System.out.println(pool.get("src.tracker.ClassTracker"));
      } catch (NotFoundException e) {
          e.printStackTrace();
      }

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
      tr.summarizeLog();
  }
}
