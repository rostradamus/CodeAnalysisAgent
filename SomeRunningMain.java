public class SomeRunningMain {
  public static void main(String args[]) {
    SomeRunningMain s = new SomeRunningMain();
    s.methodA();
    s.methodB();
    s.methodB();
    s.methodA();
  }

  void methodA() {
    try {
      System.out.println("Hello from inside methodA!");
      Thread.sleep(1000);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  void methodB() {
    try {
      System.out.println("Hello from inside methodB!");
      Thread.sleep(800);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
