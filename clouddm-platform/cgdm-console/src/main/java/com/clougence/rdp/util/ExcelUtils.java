//package com.clougence.rdp.util;
//
//import java.lang.reflect.Field;
//
//import org.apache.poi.ss.SpreadsheetVersion;
//
///**
// * @author chunlin create time is 2024/11/15
// */
//public class ExcelUtils {
//
//    public static void resetCellMaxTextLength() {
//        SpreadsheetVersion excel2007 = SpreadsheetVersion.EXCEL2007;
//        if (Integer.MAX_VALUE != excel2007.getMaxTextLength()) {
//            Field field;
//            try {
//                field = excel2007.getClass().getDeclaredField("_maxTextLength");
//                field.setAccessible(true);
//                field.set(excel2007, Integer.MAX_VALUE);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
