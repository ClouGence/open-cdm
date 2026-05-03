package com.clougence.dslpaser.parse;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.dslpaser.ast.location.BlockLocation;
import com.clougence.dslpaser.ast.location.CodeLocation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AstSplitScript {

    // script content
    private String    script;

    // for antlr attachment
    private ParseTree astTree;
    private Lexer     lexer;
    private Parser    parser;

    // for location
    private int       startCodeLine;
    private int       startCodeColumn;
    private int       endCodeLine;
    private int       endCodeColumn;

    private int       bodyStartCodeLine;
    private int       bodyStartCodeColumn;

    public BlockLocation toLocation() {
        BlockLocation blockLocation = new BlockLocation();
        blockLocation.setStartPosition(new CodeLocation(startCodeLine, startCodeColumn));
        blockLocation.setEndPosition(new CodeLocation(endCodeLine, endCodeColumn));
        return blockLocation;
    }
}
