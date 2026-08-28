/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.console.web.model.vo.cicd;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeSqlPreviewVO {
    private int                    startLine;
    private int                    totalLines;
    private String                 content;
    private boolean                eof;
    private List<ChangeBodyItemVO> itemList;
}
