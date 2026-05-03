package com.clougence.schema.editor.domain;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author mode 2021/5/21 19:56
 */
public class EditorUtils {

    public static boolean testAttribute(Map<String, String> o1, Map<String, String> o2) {
        if (o1.size() != o2.size()) {
            return true;
        }
        Set<String> datO1 = new HashSet<>(o1.keySet());
        Set<String> datO2 = new HashSet<>(o2.keySet());
        datO1.removeAll(datO2);
        if (!datO1.isEmpty()) {
            return true;
        }
        //
        AtomicBoolean testChanged = new AtomicBoolean(false);
        o1.forEach((key, value) -> {
            if (testChanged.get()) {
                return;
            }
            String initAttrData = o2.get(key);
            if (!Objects.equals(value, initAttrData)) {
                testChanged.set(true);
                return;
            }
        });
        return testChanged.get();
    }

    public static <T extends EChange<T>> boolean testList(List<T> o1List, List<T> o2List) {
        if (o1List.size() != o2List.size()) {
            return true;
        } else {
            for (int i = 0; i < o1List.size(); i++) {
                T o1 = o1List.get(i);
                T o2 = o2List.get(i);
                if (o1.testChanged(o2)) {
                    return true;
                }
            }
        }
        return false;
    }
}
