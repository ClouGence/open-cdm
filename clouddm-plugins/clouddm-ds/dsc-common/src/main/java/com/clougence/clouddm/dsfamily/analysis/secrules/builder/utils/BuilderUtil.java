package com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clougence.schema.umi.struts.UmiTypes;

public class BuilderUtil {

    public static Map<UmiTypes, String> parseTableName(List<String> nameList) {
        Map<UmiTypes, String> map = new HashMap<>();
        int size = nameList.size();
        switch (size) {
            case 3: {
                map.put(UmiTypes.Catalog, nameList.get(0));
            }
            case 2: {
                map.put(UmiTypes.Schema, nameList.get(size - 2));
            }
            case 1: {
                map.put(UmiTypes.Table, nameList.get(size - 1));
            }
        }
        return map;
    }

    public static Map<UmiTypes, String> parseFunctionName(List<String> nameList) {
        Map<UmiTypes, String> map = new HashMap<>();
        int size = nameList.size();
        switch (size) {
            case 3: {
                map.put(UmiTypes.Catalog, nameList.get(0));
            }
            case 2: {
                map.put(UmiTypes.Schema, nameList.get(size - 2));
            }
            case 1: {
                map.put(UmiTypes.Function, nameList.get(size - 1));
            }
        }
        return map;
    }

    public static Map<UmiTypes, String> parseTriggerName(List<String> nameList) {
        Map<UmiTypes, String> map = new HashMap<>();
        int size = nameList.size();
        switch (size) {
            case 3: {
                map.put(UmiTypes.Catalog, nameList.get(0));
            }
            case 2: {
                map.put(UmiTypes.Schema, nameList.get(size - 2));
            }
            case 1: {
                map.put(UmiTypes.Trigger, nameList.get(size - 1));
            }
        }
        return map;
    }

    public static Map<UmiTypes, String> parseViewName(List<String> nameList) {
        Map<UmiTypes, String> map = new HashMap<>();
        int size = nameList.size();
        switch (size) {
            case 3: {
                map.put(UmiTypes.Catalog, nameList.get(0));
            }
            case 2: {
                map.put(UmiTypes.Schema, nameList.get(size - 2));
            }
            case 1: {
                map.put(UmiTypes.View, nameList.get(size - 1));
            }
        }
        return map;
    }

    public static Map<UmiTypes, String> parseProcedureName(List<String> nameList) {
        Map<UmiTypes, String> map = new HashMap<>();
        int size = nameList.size();
        switch (size) {
            case 3: {
                map.put(UmiTypes.Catalog, nameList.get(0));
            }
            case 2: {
                map.put(UmiTypes.Schema, nameList.get(size - 2));
            }
            case 1: {
                map.put(UmiTypes.Procedure, nameList.get(size - 1));
            }
        }
        return map;
    }

    public static Map<UmiTypes, String> parseColumnName(List<String> nameList) {
        Map<UmiTypes, String> map = new HashMap<>();
        int size = nameList.size();
        switch (size) {
            case 4: {
                map.put(UmiTypes.Catalog, nameList.get(0));
            }
            case 3: {
                map.put(UmiTypes.Schema, nameList.get(size - 3));
            }
            case 2: {
                map.put(UmiTypes.Table, nameList.get(size - 2));
            }
            case 1: {
                map.put(UmiTypes.Column, nameList.get(size - 1));
            }
        }
        return map;
    }

    public static Map<UmiTypes, String> parseSchemaName(List<String> nameList) {
        Map<UmiTypes, String> map = new HashMap<>();
        int size = nameList.size();
        switch (size) {
            case 2: {
                map.put(UmiTypes.Catalog, nameList.get(0));
            }
            case 1: {
                map.put(UmiTypes.Schema, nameList.get(size - 1));
            }
        }
        return map;
    }
}
