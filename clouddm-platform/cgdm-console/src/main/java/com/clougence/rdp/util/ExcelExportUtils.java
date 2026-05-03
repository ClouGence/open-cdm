//package com.clougence.rdp.util;
//
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletResponse;
//
//import com.alibaba.excel.EasyExcel;
//import com.alibaba.excel.ExcelWriter;
//import com.alibaba.excel.metadata.FieldCache;
//import com.alibaba.excel.metadata.FieldWrapper;
//import com.alibaba.excel.util.ClassUtils;
//import com.alibaba.excel.write.metadata.WriteSheet;
//import com.clougence.utils.CollectionUtils;
//import com.clougence.utils.ExceptionUtils;
//
//import lombok.extern.slf4j.Slf4j;
//
///**
// * @author chunlin create time is 2025/9/29
// */
//@Slf4j
//public class ExcelExportUtils {
//
//    private static final int MAX_EXPORT_SHEET_SIZE = 100000;
//
//    private static final int MAX_DB_PAGE_SIZE      = 50000;
//
//    public static <F, M, R> void exportExcel(F exportFO, M exportModel, String fileName, String sheetName, Integer maxExportSize, HttpServletResponse response,
//                                             Function<F, R> dataFetcher) {
//        int dbPageSize = MAX_DB_PAGE_SIZE;
//        int sheetSize = MAX_EXPORT_SHEET_SIZE;
//        int currentSheet = 1;
//        int totalLine = 0;
//        // header line = 1
//        int currentLine = 1;
//        int currentDbBatch = 0;
//        ExcelUtils.resetCellMaxTextLength();
//
//        long startTime = System.currentTimeMillis();
//        try {
//            response.setCharacterEncoding("utf-8");
//            response.setHeader("content-Type", "application/vnd.ms-excel");
//            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
//
//            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), exportModel.getClass()).build();
//            FieldCache fieldCache = ClassUtils.declaredFields(exportModel.getClass(), excelWriter.writeContext().currentWriteHolder());
//            processHead(fieldCache);
//            WriteSheet sheet = EasyExcel.writerSheet(sheetName).build();
//            List<R> auditDTOs = null;
//            int listIndex = 0;
//            while (true) {
//                if (totalLine >= maxExportSize) {
//                    break;
//                }
//
//                if (auditDTOs == null) {
//                    auditDTOs = dataFetcher.apply(exportFO, dbPageSize, currentDbBatch++);
//                } else if (listIndex == auditDTOs.size()) {
//                    auditDTOs = dataFetcher.apply(exportFO, dbPageSize, currentDbBatch++);
//                    listIndex = 0;
//                }
//
//                if (CollectionUtils.isNotEmpty(auditDTOs)) {
//                    int remainSize = auditDTOs.size() - listIndex;
//                    if (currentLine + remainSize <= sheetSize) {
//                        if (totalLine + remainSize > maxExportSize) {
//                            remainSize = maxExportSize - totalLine;
//                        }
//                        excelWriter.write(auditDTOs.subList(listIndex, listIndex + remainSize), sheet);
//                        currentLine += remainSize;
//                        totalLine += remainSize;
//                        listIndex = auditDTOs.size();
//                    } else {
//                        int writeSize = sheetSize - currentLine;
//                        if (totalLine + writeSize > maxExportSize) {
//                            writeSize = maxExportSize - totalLine;
//                        }
//                        // from listIndex to listIndex + writeSize
//                        excelWriter.write(auditDTOs.subList(listIndex, listIndex + writeSize), sheet);
//                        totalLine += writeSize;
//                        // header line = 1
//                        currentLine = 1;
//                        sheet = EasyExcel.writerSheet(sheetName + "_" + (currentSheet++)).build();
//                        listIndex += writeSize;
//                    }
//                } else {
//                    break;
//                }
//            }
//            excelWriter.finish();
//            log.info("Export excel file success, total line: {}, cost time: {} ms", totalLine, System.currentTimeMillis() - startTime);
//        } catch (Exception e) {
//            log.error("Export excel file failed, msg: {}", ExceptionUtils.getRootCauseMessage(e));
//        }
//        log.info("Export excel file finished, cost time: {} ms", System.currentTimeMillis() - startTime);
//    }
//
//    private static void processHead(FieldCache fieldCache) {
//        Map<Integer, FieldWrapper> headMap = fieldCache.getSortedFieldMap();
//        for (Map.Entry<Integer, FieldWrapper> integerHeadEntry : headMap.entrySet()) {
//            FieldWrapper value = integerHeadEntry.getValue();
//            String[] heads = value.getHeads();
//            if (heads != null && heads.length >= 1) {
//                String headStr = heads[0];
//                value.setHeads(new String[] { RdpI18nUtils.getMessage(headStr) });
//            }
//        }
//    }
//
//    @FunctionalInterface
//    public interface Function<F, R> {
//
//        List<R> apply(F f, int dbPageSize, int currentDbBatch);
//    }
//}
