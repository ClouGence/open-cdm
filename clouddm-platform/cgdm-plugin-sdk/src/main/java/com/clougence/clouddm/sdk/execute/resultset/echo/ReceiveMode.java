package com.clougence.clouddm.sdk.execute.resultset.echo;

public enum ReceiveMode {
    // batch is a result set.
    PAGE_FULL,
    // batch is a result page.
    PAGINATED,
    // batch is a row.
    STREAM,
}
