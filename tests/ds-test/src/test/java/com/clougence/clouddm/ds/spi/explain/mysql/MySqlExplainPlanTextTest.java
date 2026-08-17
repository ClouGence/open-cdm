/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.spi.explain.mysql;

import com.clougence.clouddm.ds.spi.explain.ExplainPlanTextTest;
import com.clougence.clouddm.sdk.execute.explain.ExplainPlanSpi;
import com.clougence.sql.mysql.execute.explain.MyExplainPlanSpi;

public abstract class MySqlExplainPlanTextTest extends ExplainPlanTextTest {

    protected MySqlExplainPlanTextTest(String version){
        super("spi/explain/mysql/" + version);
    }

    @Override
    protected ExplainPlanSpi explainPlanSpi() {
        return new MyExplainPlanSpi();
    }
}
