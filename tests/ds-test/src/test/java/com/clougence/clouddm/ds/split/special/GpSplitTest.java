package com.clougence.clouddm.ds.split.special;

import com.clougence.clouddm.ds.split.family.postgres.PgSplitTest;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgSplitAnalysisSpi;

public class GpSplitTest extends PgSplitTest {

    public GpSplitTest(){
        this.splitAnalysisSpi = new PgSplitAnalysisSpi();
    }
}
