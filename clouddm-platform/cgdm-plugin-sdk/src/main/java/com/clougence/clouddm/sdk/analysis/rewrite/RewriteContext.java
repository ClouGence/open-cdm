package com.clougence.clouddm.sdk.analysis.rewrite;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
@Getter
@Setter
public class RewriteContext {

    private long         fetchLimit;

    private List<String> rewriterTags = new ArrayList<>();

    public void addRewriterInfo(String tag) {
        if (!this.rewriterTags.contains(tag)) {
            this.rewriterTags.add(tag);
        }
    }
}
