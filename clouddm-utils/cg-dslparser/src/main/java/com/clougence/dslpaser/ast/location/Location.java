package com.clougence.dslpaser.ast.location;

/**
 * ast and line number location
 * @author zyc@hasor.net
 * @version : 2020-06-11
 */
public interface Location {

    CodeLocation getStartPosition();

    CodeLocation getEndPosition();

    void setStartPosition(CodeLocation codeLocation);

    void setEndPosition(CodeLocation codeLocation);
}
