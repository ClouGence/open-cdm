package com.clougence.clouddm.sdk.execute.session.result.fetcher;

import java.io.IOException;

public interface ValueFetcherContextCharStreamInfo {

    boolean isComplete();

    long fullSize();

    long readSize();

    String sampleData(int maxChars) throws IOException;
}
