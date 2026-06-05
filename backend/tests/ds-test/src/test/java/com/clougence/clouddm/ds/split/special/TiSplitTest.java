package com.clougence.clouddm.ds.split.special;

import com.clougence.clouddm.ds.split.family.mysql.MySplitTest;
import com.clougence.clouddm.ds.tidb.analysis.TiSplitAnalysisSpi;

public class TiSplitTest extends MySplitTest {

    public TiSplitTest(){
        this.splitAnalysisSpi = new TiSplitAnalysisSpi();
    }
}
