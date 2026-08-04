/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.clougence.sql.db2.analysis.security.builder;

import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbDeleteDomain;
import com.clougence.sql.common.analysis.secrules.builder.DeleteDomainBuilder;
import com.clougence.sql.db2.analysis.security.domain.Db2DeleteDomain;

public class Db2DeleteBuilder extends DeleteDomainBuilder {

    @Override
    protected RdbDeleteDomain getDeleteDomain() { return new Db2DeleteDomain(); }
}
