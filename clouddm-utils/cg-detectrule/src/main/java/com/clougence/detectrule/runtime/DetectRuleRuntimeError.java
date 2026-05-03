package com.clougence.detectrule.runtime;

import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.dslpaser.ast.location.Location;

public class DetectRuleRuntimeError extends RuntimeException {

    public DetectRuleRuntimeError(Location location, String s){
        super(toLineRange(location) + ": " + s);
    }

    public DetectRuleRuntimeError(Location location, String s, Exception e){
        super(toLineRange(location) + ": " + s, e);
    }

    private static String toLineRange(Location location) {
        CodeLocation startPosition = location.getStartPosition();
        CodeLocation endPosition = location.getEndPosition();

        String starStr = startPosition == null ? "Unknown" : startPosition.toString();
        String endStr = endPosition == null ? "Unknown" : endPosition.toString();
        if ("Unknown".equalsIgnoreCase(starStr) && "Unknown".equalsIgnoreCase(endStr)) {
            return "Unknown";
        }
        if ("Unknown".equalsIgnoreCase(endStr)) {
            return "line " + starStr;
        } else {
            return "line " + starStr + "~" + endStr;
        }
    }
}
