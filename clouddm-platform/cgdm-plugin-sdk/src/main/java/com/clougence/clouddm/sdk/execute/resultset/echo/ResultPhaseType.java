package com.clougence.clouddm.sdk.execute.resultset.echo;

public enum ResultPhaseType {
    // before query
    Before,
    // after query
    After,
    // cancel query
    Cancel,
    // begin receive ResultSet
    BeginReceive,
    // finish receive ResultSet
    FinishReceive,
}
