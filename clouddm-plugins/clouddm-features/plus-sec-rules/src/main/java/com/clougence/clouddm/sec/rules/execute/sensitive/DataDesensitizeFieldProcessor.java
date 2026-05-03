//package com.clougence.clouddm.worker.component.session.dataproc;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.tuple.MutablePair;
//import org.apache.commons.lang3.tuple.Pair;
//
//import com.clougence.clouddm.base.api.console.ConsolePkgRService;
//import com.clougence.clouddm.base.metadata.process.ColUnit;
//import com.clougence.clouddm.base.metadata.process.desentization.DesensitizeConfigWrapper;
//import com.clougence.clouddm.base.metadata.process.desentization.model.ExprWrapper;
//import com.clougence.clouddm.base.metadata.process.desentization.model.HalfMaskExpr;
//import com.clougence.clouddm.sdk.api.ColMetaDataInSdk;
//import com.clougence.clouddm.sdk.api.ColProcess;
//import com.clougence.clouddm.sdk.execute.session.ProcessConfigDTO;
//import com.clougence.utils.JsonUtils;
//
///**
// * @author wanshao create time is 2021/5/21
// **/
//public class DataDesensitizeFieldProcessor implements ColProcess {
//
//    private final ConsolePkgRService       consoleRService;
//
//    private final Map<String, ExprWrapper> columnProcessConfigMap;
//
//    private static final char              MASK_CHAR = '*';
//
//    public DataDesensitizeFieldProcessor(ProcessConfigDTO dto, ConsolePkgRService consoleRService){
//        this.columnProcessConfigMap = new HashMap<>();
//        this.consoleRService = consoleRService;
//
//        DesensitizeConfigWrapper config = JsonUtils.toObj(dto.getConfig(), DesensitizeConfigWrapper.class);
//        for (Map.Entry<ColUnit, ExprWrapper> entry : config.getColDesensitizeExprMap().entrySet()) {
//            columnProcessConfigMap.put(entry.getKey().toPath(), entry.getValue());
//        }
//    }
//
//    @Override
//    public Object doProcess(Object colValue, ColMetaDataInSdk colMeta, String processConfig) {
//        String currentPath = colMeta.toPath();
//        ExprWrapper exprWrapper = columnProcessConfigMap.get(currentPath);
//        return doMaskByExpr(colValue, exprWrapper);
//    }
//
//    private static Object doMaskByExpr(Object value, ExprWrapper exprWrapper) {
//        if (exprWrapper == null) {
//            return value;
//        }
//
//        switch (exprWrapper.getInnerDesensitizeRuleType()) {
//            case FULL_MASK:
//                return StringUtils.repeat("*", String.valueOf(value).length());
//            case PART_MASK:
//                HalfMaskExpr halfMaskExpr = JsonUtils.toObj(exprWrapper.getExprConfigJson(), HalfMaskExpr.class);
//                Map<MutablePair<Integer, Integer>, Boolean> halfMaskExprRangePairMap = halfMaskExpr.getRangePairMap();
//                String maskedResult = String.valueOf(value);
//                for (Pair<Integer, Integer> rangePair : halfMaskExprRangePairMap.keySet()) {
//                    boolean reversed = halfMaskExprRangePairMap.get(rangePair);
//                    Integer leftVal = rangePair.getLeft();
//                    Integer rightVal = rangePair.getRight();
//                    if (leftVal == null) {
//                        maskedResult = singleValModeHalfMask(maskedResult, rightVal, reversed);
//                    } else {
//                        maskedResult = rangeModeHalfMask(maskedResult, leftVal, rightVal, reversed);
//                    }
//                }
//                if (maskedResult == null) {
//                    return value;
//                } else {
//                    return maskedResult;
//                }
//            default:
//                throw new IllegalArgumentException("Unsupported.");
//        }
//    }
//
//    public static Object processForTest(Object value, ExprWrapper exprWrapper) {
//        return doMaskByExpr(value, exprWrapper);
//    }
//
//    protected static String rangeModeHalfMask(String rawStr, Integer leftValue, Integer rightValue, boolean reversed) {
//        char[] charArray = rawStr.toCharArray();
//        if (reversed) {
//            if (leftValue > charArray.length) {
//                return StringUtils.repeat(String.valueOf(MASK_CHAR), charArray.length);
//            }
//
//            // first range is [0,firstRangeRightBound)
//            int firstRangeRightBound = leftValue;
//            // second range is [secondRangeRightBound-1,charArray.length)
//            int secondRangeRightBound = rightValue > charArray.length ? charArray.length : rightValue;
//            for (int i = 0; i < firstRangeRightBound - 1; i++) {
//                charArray[i] = MASK_CHAR;
//            }
//            for (int i = secondRangeRightBound; i < charArray.length; i++) {
//                charArray[i] = MASK_CHAR;
//            }
//            return String.valueOf(charArray);
//
//        } else {
//            if (leftValue > charArray.length) {
//                return rawStr;
//            }
//            int realRightVal = rightValue > charArray.length ? charArray.length : rightValue;
//            for (int i = leftValue - 1; i < realRightVal; i++) {
//                charArray[i] = MASK_CHAR;
//            }
//            return String.valueOf(charArray);
//
//        }
//
//    }
//
//    protected static String singleValModeHalfMask(String rawStr, Integer rightVal, boolean reversed) {
//        char[] charArray = rawStr.toCharArray();
//        if (rightVal < 0) {
//            if (reversed) {
//                return maskAllBeforeLastSpecifiedCountChar(Math.abs(rightVal), charArray);
//            } else {
//                return maskLastSpecifiedCountChar(Math.abs(rightVal), charArray);
//            }
//        } else {
//            if (reversed) {
//                maskBeforeIndex(rightVal - 1, charArray);
//            } else {
//                maskAfterIndex(rightVal - 1, charArray);
//            }
//        }
//
//        return String.valueOf(charArray);
//    }
//
//    /** Last specified count char will be masked */
//    private static String maskLastSpecifiedCountChar(Integer maskCount, char[] charArray) {
//        int count = 0;
//        for (int i = charArray.length - 1; i >= 0; i--) {
//            charArray[i] = MASK_CHAR;
//            count++;
//            if (count == maskCount) {
//                break;
//            }
//        }
//        return String.valueOf(charArray);
//    }
//
//    /** Mask all char before last specified count char */
//    private static String maskAllBeforeLastSpecifiedCountChar(Integer notMaskCount, char[] charArray) {
//        if (notMaskCount >= charArray.length) {
//            return String.valueOf(charArray);
//        } else {
//            int needMaskCount = charArray.length - notMaskCount;
//            int count = 0;
//
//            for (int i = 0; i < charArray.length; i++) {
//                charArray[i] = MASK_CHAR;
//                count++;
//                if (count == needMaskCount) {
//                    break;
//                }
//            }
//            return String.valueOf(charArray);
//        }
//    }
//
//    /**
//     * All char after index is masked
//     *
//     * @param index start from 0
//     */
//    private static String maskAfterIndex(Integer index, char[] charArray) {
//        for (int i = index; i < charArray.length; i++) {
//            charArray[i] = MASK_CHAR;
//        }
//        return String.valueOf(charArray);
//    }
//
//    private static String maskBeforeIndex(Integer index, char[] charArray) {
//        for (int i = 0; i < index; i++) {
//            charArray[i] = MASK_CHAR;
//        }
//        return String.valueOf(charArray);
//    }
//}
