/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.console.web.component.cicd;

import java.nio.file.Path;

import com.clougence.utils.function.EFunction;

public interface ChangeSqlService {

    <T> T consumeSqlFile(long changeId, EFunction<Path, T, Exception> consumer);
}
