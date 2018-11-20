package src.tracker;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ClassTracker {
    private String className;
    private int numObject;
    private Set<MethodTracker> methods;

    public ClassTracker(String className) {
        this.className = className;
        this.numObject = 0;
        this.methods = new HashSet<>();
    }

    public String getClassName() {
        return className;
    }

    public int getNumObject() {
        return numObject;
    }

    public Set<MethodTracker> getMethods() {
        return methods;
    }

    public int incrementNumObject() {
        return ++numObject;
    }

    public void addMethod(MethodTracker m) {
        if (this.methods.add(m)) {
            m.setClassTracker(this);
        }
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
