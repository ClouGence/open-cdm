package com.clougence.detectrule.runtime;

import com.clougence.detectrule.lang.LangObject;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.dslpaser.ast.location.Location;

import lombok.Getter;

@Getter
public class DetectRuleRuntimeException extends RuntimeException {

    private final LangObject throwData;

    public DetectRuleRuntimeException(Location location, LangObject s){
        super(toLineRange(location) + ": " + s);
        this.throwData = s;
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
