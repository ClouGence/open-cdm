package com.clougence.dslpaser.antlr;

import com.clougence.dslpaser.ast.location.CodeLocation;

import lombok.Getter;

@Getter
public class AntlerSyntaxException extends RuntimeException {

    private final int line;
    private final int column;

    public AntlerSyntaxException(int line, int column, String errorMessage){
        super(errorMessage);
        this.line = Math.max(1, line);
        this.column = Math.max(0, column);
    }

    public CodeLocation offsetLocation(int offsetLine, int offsetColumn) {
        if (this.line == 1) {
            return new CodeLocation(this.line + Math.max(1, offsetLine) - 1, this.column + offsetColumn);
        } else {
            return new CodeLocation(this.line + Math.max(1, offsetLine) - 1, this.column);
        }
    }
}
