package src.util;

import src.tracker.ClassTracker;
import src.tracker.MethodTracker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Random;

public class JsonLogger {

    public static void logJson(Map<String, ClassTracker> summary) {
        try {
            Writer fw = new FileWriter("/Users/ro.stradamus/Desktop/rostradamus/CPSC410/codes/CodeAnalysisAgent/out.json");

            fw.write("[\n");
            summary.forEach(((k,v) -> {
//                try {
//                    fw.write(k + "\n");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                v.getMethodTrackerMap().forEach(((k1, v1) -> {
                    try {
                        fw.write("\n    {");
                        fw.write("\n        \"name\": \"" + k + "." + k1 + "\",");
                        fw.write("\n        \"size\": " + new Random().nextInt(10000) +",");
                        fw.write("\n        \"imports\": [");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String s = "";
                    Map<MethodTracker, Integer> callers = v1.getCallerMap();
                    for (MethodTracker caller : callers.keySet()) {
                        s = s.concat("\n            \"" + caller.getClassTracker().getClassName() + "." + caller.getMethodName() + "\",");
                    }
//                    v1.getCallerMap().forEach(((k2,v2) ->
//                    {
//                        try {
//                            s = s.concat("            \"" + k2.getClassTracker().getClassName() + "." + k2.getMethodName() + "\",\n");
//                            fw.write("            \"" + k2.getClassTracker().getClassName() + "." + k2.getMethodName() + "\",\n");
////                            fw.write("        " + k2.getClassTracker().getClassName() + "." + k2.getMethodName() + ": " + v2 + "\n");
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }));
                    try {
                        fw.write(s.length() > 0 ? s.substring(0, s.length() - 1) : s);
                        fw.write("\n        ]");
                        fw.write("\n    },");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));
            }));
            fw.write("\n]");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
