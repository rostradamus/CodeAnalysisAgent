package src.tracker;

import java.util.*;

public class TrackerManager {
    private Map<String, ClassTracker> classTrackerMap;
    private static TrackerManager instance;

    private TrackerManager() {
        classTrackerMap = new HashMap<>();
    }


    public static TrackerManager getInstance() {
        if (instance == null)
            instance = new TrackerManager();
        return instance;
    }

    public void printAllClassNames() {
        classTrackerMap.forEach(((k,v) -> System.out.println(k)));
    }

    public void summarizeLog() {
        classTrackerMap.forEach(((k,v) -> {
            System.out.println(k);
            v.getMethodTrackerMap().forEach(((k1, v1) -> {
                System.out.println("---" + k1);
                v1.getCallerMap().forEach(((k2,v2) -> {
                    System.out.println("------" + k2.getClassTracker().getClassName() + "." + k2.getMethodName() + ": " + v2);
//                    System.out.println("------" + k2.getMethodName() + ": " + v2);
//                            if (k2.getClassTracker() != null && v2 != null)
//                                System.out.println(k2.getClassTracker().getClassName() + "." + k2.getMethodName() + ": " + v2);
//                            System.out.println(k2.getClassTracker().getClassName());
//                            System.out.println(v2);

                }));
            }));
        }));
    }

    public void logMethodCall(String cname, String mname, String callerCname, String callerMethod) {
        ClassTracker calleeClass = getClassTracker(cname);
        MethodTracker calleeMethod = calleeClass.getMethodTracker(mname);
        calleeMethod.setClassTracker(calleeClass);
        ClassTracker callerClass = getClassTracker(callerCname);
        calleeMethod.logCall(callerClass, callerMethod);
    }

    private ClassTracker getClassTracker(String cname) {
        if (classTrackerMap.get(cname) != null)
            return classTrackerMap.get(cname);
        ClassTracker ct = new ClassTracker(cname);
        classTrackerMap.put(cname, ct);
        return ct;
    }
}
