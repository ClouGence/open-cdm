/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.ds.spi.explain.hana;

import com.clougence.clouddm.ds.hana.execute.explain.HanaExplainPlanSpi;
import com.clougence.clouddm.ds.spi.explain.ExplainPlanTextTest;
import com.clougence.clouddm.sdk.execute.explain.ExplainPlanSpi;

public final class HanaExplainPlanTextTest extends ExplainPlanTextTest {

    public HanaExplainPlanTextTest(){
        super("spi/explain/hana");
    }

    @Override
    protected ExplainPlanSpi explainPlanSpi() {
        return new HanaExplainPlanSpi();
    }
}
