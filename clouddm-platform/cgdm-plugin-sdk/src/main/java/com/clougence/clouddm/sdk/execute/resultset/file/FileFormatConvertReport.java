package com.clougence.clouddm.sdk.execute.resultset.file;

import java.io.IOException;

public interface FileFormatConvertReport {

    void reportProcess(String message, long from, long to, long current) throws IOException;
}
