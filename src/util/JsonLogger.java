package src.util;

import src.tracker.ClassTracker;
import src.tracker.MethodTracker;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class JsonLogger {

    public static void logJson(Map<String, ClassTracker> summary) {
        String path = System.getProperty("user.dir");
        System.out.println(path);
        try {
            Writer fw = new FileWriter(path + "/out.json");

            fw.write("[\n");
            fw.write("    ");
            List<String> listOfEntries = new ArrayList<>();
            summary.forEach(((className, classTracker) -> classTracker.getMethodTrackerMap().forEach(((methodName, methodTracker) -> {
                String jsonObjEntry = "";
                jsonObjEntry = jsonObjEntry
                        .concat("{")
                        .concat("\n        \"name\": \"" + className + "." + methodName + "\",")
                        .concat("\n        \"size\": " + new Random().nextInt(10000) +",")
                        .concat("\n        \"imports\": [");
                String s = "";
                for (MethodTracker caller : methodTracker.getCallerMap().keySet()) {
                    s = s.concat("\n            \"" + caller.getClassTracker().getClassName() + "." + caller.getMethodName() + "\",");
                }
                jsonObjEntry = jsonObjEntry.concat(s.length() > 0 ? s.substring(0, s.length() - 1) : s)
                        .concat("\n        ]")
                        .concat("\n    }");

                listOfEntries.add(jsonObjEntry);
            }))));

            fw.write(String.join(", ", listOfEntries));
            fw.write("\n]");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
