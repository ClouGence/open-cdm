package com.clougence.clouddm.sdk.execute.resultset.file;

import java.io.File;
import java.io.IOException;

public interface ResultReaderService {

    ResultReader openReader(File resultFile) throws IOException;

}
