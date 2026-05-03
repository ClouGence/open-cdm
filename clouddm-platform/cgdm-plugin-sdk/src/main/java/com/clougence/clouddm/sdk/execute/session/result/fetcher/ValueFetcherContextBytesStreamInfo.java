package com.clougence.clouddm.sdk.execute.session.result.fetcher;

import java.io.IOException;

public interface ValueFetcherContextBytesStreamInfo {

    boolean isComplete();

    long fullSize();

    long readSize();

    byte[] sampleData(int displayBytes) throws IOException;
}
