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
 */
package com.clougence.clouddm.sdk.service.file;

import java.io.File;
import java.io.IOException;

import com.clougence.clouddm.sdk.service.Service;

public interface FileService extends Service {

    File createFileObject(String fileName, boolean tempFile);

    boolean touchFile(File file);

    boolean deleteFile(File file);

    boolean existsFile(File file);

    boolean moveFile(File dst, File src);

    long fileSize(File file);

    byte[] fileRead(File file, long offset, int length) throws IOException;
}
