package src.tracker;

import java.util.*;



public class MethodTracker {
    private String methodName;
    private ClassTracker classTracker;
    private Map<MethodTracker, Integer> callerMap;

    public MethodTracker(String methodName) {
        this.methodName = methodName;
        classTracker = null;
        this.callerMap = new HashMap<>();
    }

    public String getMethodName() {
        return methodName;
    }

    public ClassTracker getClassTracker() {
        return classTracker;
    }

    public void setClassTracker(ClassTracker classTracker) {
        if (this.classTracker == null)
            this.classTracker = classTracker;
    }

    public Map<MethodTracker, Integer> getCallerMap() {
        return callerMap;
    }

    public void logCall(ClassTracker callerClass, String calleeMethod) {
        MethodTracker mt = callerClass.getMethodTracker(calleeMethod);
        mt.setClassTracker(callerClass);
        if (callerMap.get(mt) == null) {
            callerMap.put(mt, 1);
            return;
        }
        callerMap.put(mt, callerMap.get(mt) + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodTracker that = (MethodTracker) o;
        return Objects.equals(methodName, that.methodName) &&
                Objects.equals(classTracker, that.classTracker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(methodName, classTracker);
    }
}
