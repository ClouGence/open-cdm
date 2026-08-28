/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.console.web.model.fo.cicd;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeSqlPreviewFO {
    private long   changeId;
    private int    startLine;
    private int    lineCount;
    private String contentName;
}
