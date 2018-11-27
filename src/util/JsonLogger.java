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
//        System.out.println(path);
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
                List<String> imports = new ArrayList<>();
                String callerObj = "";
                for (MethodTracker caller : methodTracker.getCallerMap().keySet()) {
                    imports.add(callerObj.concat("{")
                            .concat("\n          \"name\": \"" + caller.getClassTracker().getClassName() + "." + caller.getMethodName() + "\",")
                            .concat("\n          \"counter\":" + methodTracker.getCallerMap().get(caller).toString())
                            .concat("\n          }"));
//                    System.out.println(methodTracker.getCallerMap().get(caller));
//                    callerObj = callerObj.concat("\n            \"" + caller.getClassTracker().getClassName() + "." + caller.getMethodName() + "\",");
                }
                jsonObjEntry = jsonObjEntry.concat(String.join(", ", imports))
                    .concat("\n      ]")
                    .concat("\n   }");
//                jsonObjEntry = jsonObjEntry.concat(callerObj.length() > 0 ? callerObj.substring(0, callerObj.length() - 1) : callerObj)
//                        .concat("\n        ]")
//                        .concat("\n    }");

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
