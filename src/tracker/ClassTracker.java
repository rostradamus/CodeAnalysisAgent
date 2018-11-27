package src.tracker;

import java.util.*;

public class ClassTracker {
    private String className;
    private int numObject;
    private Map<String, MethodTracker> methodTrackerMap;

    public ClassTracker(String className) {
        this.className = className;
        this.numObject = 0;
        this.methodTrackerMap = new HashMap<>();
    }

    public String getClassName() {
        return className;
    }



    public int getNumObject() {
        return numObject;
    }

    public Map<String, MethodTracker> getMethodTrackerMap() {
        return methodTrackerMap;
    }

    public int incrementNumObject() {
        return ++numObject;
    }

    public MethodTracker getMethodTracker(String mname) {
        if (methodTrackerMap.get(mname) != null)
            return methodTrackerMap.get(mname);
        MethodTracker mt = new MethodTracker(mname);
        methodTrackerMap.put(mname, mt);
        return mt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassTracker that = (ClassTracker) o;
        return Objects.equals(className, that.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className);
    }
}
