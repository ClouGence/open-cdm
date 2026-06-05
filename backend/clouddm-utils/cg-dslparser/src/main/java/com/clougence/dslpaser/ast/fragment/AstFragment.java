/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
