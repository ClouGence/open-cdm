package com.clougence.clouddm.sdk.execute.resultset.echo;

public enum ResultType {
    // result is ResultSet
    ResultSet,
    // result is ResultSet
    ResultSetMeta,
    // result is ResultSet
    ResultSetRows,

    // result is update count
    ResultCount,
    // result is procedure Output parameters.
    ResultOut,
    // result is Message, the message from Connection
    Message,
    // result is Phase Message, means some query beginning or finish
    Phase
}
