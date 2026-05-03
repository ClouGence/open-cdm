package com.clougence.clouddm.sdk.analysis.rewrite;

import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
public interface RewriteSpi extends Spi {

    String rewriterQuery(QueryRequest request, RewriteContext context);
}
