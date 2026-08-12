/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.spi.explain.postgres;

import com.clougence.clouddm.ds.postgres.execute.explain.PgExplainPlanSpi;
import com.clougence.clouddm.ds.spi.explain.ExplainPlanTextTest;
import com.clougence.clouddm.sdk.execute.explain.ExplainPlanSpi;

public final class PgExplainPlanTextTest extends ExplainPlanTextTest {
    public PgExplainPlanTextTest(){
        super("spi/explain/postgres");
    }

    @Override
    protected ExplainPlanSpi explainPlanSpi() {
        return new PgExplainPlanSpi();
    }
}
