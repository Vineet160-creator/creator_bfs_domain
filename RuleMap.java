package com.discore.d48;
import java.util.*;

public class RuleMap {
    private  static Map<String, Integer> rules = new HashMap<>();

    static {
        rules.put("48.71", 2);
        rules.put("48.75", 2);
        rules.put("48.75.1", 2);
        rules.put("48.75.2", 2);
        rules.put("48.75.3", 2);
        rules.put("48.75.4", 2);
        rules.put("48.75.5", 2);
    }
    
    public static boolean hasField(String field) {
        return rules.containsKey(field);
    }

    public static int getFieldLength(String field) {
        return rules.get(field);
    }
}