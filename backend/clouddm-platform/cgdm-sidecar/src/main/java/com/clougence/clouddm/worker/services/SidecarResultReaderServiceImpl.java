/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */package com.clougence.clouddm.worker.services;

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
