package com.clougence.clouddm.sec.rules.execute.sensitive;

import java.util.Arrays;
import java.util.List;

/**
 * @author mode create time is 2023/05/21 13:27
 **/
public class SenAlgorithm {

    public static final String        SEN_ORIGINAL  = "algorithm::ORIGINAL";
    public static final String        SEN_FULL_MASK = "algorithm::FULL_MASK";

    //the bigger the index, the better,
    private static final List<String> algorithmPool = Arrays.asList(SEN_ORIGINAL, SEN_FULL_MASK);

    public static String chooseAlgorithm(String current, String newone) {
        int a = algorithmPool.indexOf(current);
        int b = algorithmPool.indexOf(newone);
        if (a < 0 || b < 0) {
            throw new IllegalStateException(current + " or " + newone + " not in algorithmPool.");
        }

        if (a > b) {
            return current;
        } else if (a < b) {
            return newone;
        } else {
            return current;
        }
    }

    // algorithm::original
    public static String doOriginalAlgorithm(String command, String cv) {
        return cv;
    }

    // algorithm::full_mask
    public static String doFullMaskAlgorithm(String command, String cv) {
        return "******";
    }

    // algorithm::half_mask(3,7,11)
    public static String doHalfMaskAlgorithm(String command, String cv) {
        int lbtIndex = command.indexOf("(");
        int rbtIndex = command.indexOf(")");
        if (lbtIndex < 0 || rbtIndex < 0) {
            return "algorithm::half_mask ERROR: Bad args.";
        }

        String args = command.substring(lbtIndex + 1, rbtIndex);
        String[] splitArg = args.split(",");
        if (splitArg.length < 3) {
            return "algorithm::half_mask ERROR: Bad args.";
        }

        return "***";
    }
}
