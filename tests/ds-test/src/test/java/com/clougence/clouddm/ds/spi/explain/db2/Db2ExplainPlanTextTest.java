/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.spi.explain.db2;

import com.clougence.clouddm.ds.db2.execute.explain.Db2ExplainPlanSpi;
import com.clougence.clouddm.ds.spi.explain.ExplainPlanTextTest;
import com.clougence.clouddm.sdk.execute.explain.ExplainPlanSpi;

public final class Db2ExplainPlanTextTest extends ExplainPlanTextTest {
    public Db2ExplainPlanTextTest(){
        super("spi/explain/db2");
    }

    @Override
    protected ExplainPlanSpi explainPlanSpi() {
        return new Db2ExplainPlanSpi();
    }
}
