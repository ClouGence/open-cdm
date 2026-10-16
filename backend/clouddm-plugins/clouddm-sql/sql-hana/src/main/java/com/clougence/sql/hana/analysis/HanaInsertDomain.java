/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.hana.analysis;

import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbInsertDomain;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class HanaInsertDomain extends RdbInsertDomain {
    private boolean hasSpecifyColumn;
    private boolean hasUnion;
}
