package com.clougence.clouddm.worker.services;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.sdk.execute.resultset.file.ResultReader;
import com.clougence.clouddm.sdk.execute.resultset.file.ResultReaderService;

@Service
public class SidecarResultReaderServiceImpl implements ResultReaderService {

    @Override
    public ResultReader openReader(File resultFile) throws IOException {
        if (resultFile == null) {
            throw new NullPointerException("resultFile is null");
        }
        if (!resultFile.exists() || !resultFile.isFile()) {
            throw new IOException("resultFile not exists or not file:" + resultFile.getAbsolutePath());
        }

        return new SidecarResultReaderImpl(resultFile.getAbsoluteFile());
    }

}
