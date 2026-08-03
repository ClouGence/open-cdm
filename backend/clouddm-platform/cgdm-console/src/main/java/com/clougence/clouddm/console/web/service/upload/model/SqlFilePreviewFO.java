/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.console.web.service.upload.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SqlFilePreviewFO {

    @NotNull
    private Long attachmentId;
    @Min(1)
    private int  startLine = 1;
    @Min(1)
    @Max(1000)
    private int  lineCount = 30;
}
