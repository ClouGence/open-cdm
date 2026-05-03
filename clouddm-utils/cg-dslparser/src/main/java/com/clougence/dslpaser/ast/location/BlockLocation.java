package com.clougence.dslpaser.ast.location;

import lombok.Getter;
import lombok.Setter;

/**
 * ast and line number location
 * @author zyc@hasor.net
 * @version : 2020-06-11
 */
@Getter
@Setter
public class BlockLocation implements Location {

    private CodeLocation startPosition;
    private CodeLocation endPosition;

    public String toLineRange() {
        CodeLocation startPosition = getStartPosition();
        CodeLocation endPosition = getEndPosition();

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

    @Override
    public String toString() {
        return toLineRange();
    }
}
