package com.clougence.dslpaser.ast.fragment;

import java.io.IOException;
import java.util.Objects;

import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Comment extends AstFragment {

    private String      beginWrap;
    private String      endWrap;
    private String      content;
    private CommentType type;

    public Comment(String beginWrap, String content){
        this.beginWrap = Objects.requireNonNull(beginWrap, "wrap is null.");
        this.endWrap = "";
        this.content = Objects.requireNonNull(content, "content is null.");
        this.type = CommentType.Line;
    }

    public Comment(String beginWrap, String endWrap, String content){
        this.beginWrap = Objects.requireNonNull(beginWrap, "beginWrap is null.");
        this.endWrap = Objects.requireNonNull(endWrap, "endWrap is null.");
        this.content = Objects.requireNonNull(content, "content is null.");
        this.type = CommentType.Block;
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        if (this.type == CommentType.Block) {
            writer.write(this.beginWrap);
            writer.write(this.content);
            writer.write(this.endWrap);
        } else if (this.type == CommentType.Line) {
            writer.write(this.beginWrap);
            writer.write(this.content);
        } else {
            throw new IllegalStateException("can't happen, CommentType is null.");
        }
    }

}
