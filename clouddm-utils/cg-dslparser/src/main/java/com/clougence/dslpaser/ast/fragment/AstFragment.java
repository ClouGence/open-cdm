package com.clougence.dslpaser.ast.fragment;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.clougence.dslpaser.ast.location.BlockLocation;
import com.clougence.dslpaser.foramt.FmtWriter;
import com.clougence.dslpaser.foramt.Format;

import lombok.Getter;
import lombok.SneakyThrows;

@Getter
public abstract class AstFragment extends BlockLocation implements Format {

    private final List<Comment> blockComments = new ArrayList<>();// At the beginning of the statement
    private final List<Comment> lineComments  = new ArrayList<>();// At the end of the statement

    public void addComment(Comment comment) {
        Objects.requireNonNull(comment.getType(), "comment Type is null.");

        if (comment.getType() == CommentType.Block) {
            this.blockComments.add(comment);
        } else if (comment.getType() == CommentType.Line) {
            this.lineComments.add(comment);
        } else {
            throw new IllegalStateException("can't happen, CommentType is null.");
        }
    }
    //
    //    protected void visitorEnter(Visitor visitor) {
    //        if (this.blockComments != null) {
    //            for (Comment comment : this.blockComments) {
    //                comment.accept(visitor);
    //            }
    //        }
    //    }

    //    protected void visitorLeave(Visitor visitor) {
    //        if (this.lineComments != null) {
    //            for (Comment comment : this.lineComments) {
    //                comment.accept(visitor);
    //            }
    //        }
    //    }

    @SneakyThrows
    @Override
    public String toString() {
        Writer strWriter = new StringWriter();
        FmtWriter writer = new FmtWriter(strWriter);

        this.doFormat(writer);

        return strWriter.toString();
    }
}
