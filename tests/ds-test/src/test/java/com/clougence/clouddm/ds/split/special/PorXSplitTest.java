package com.clougence.clouddm.ds.split.special;

import com.clougence.clouddm.ds.polardb.sql.porx.split.PorXSplitAnalysisSpi;
import com.clougence.clouddm.ds.split.family.mysql.MySplitTest;

public class PorXSplitTest extends MySplitTest {

    public PorXSplitTest(){
        this.splitAnalysisSpi = new PorXSplitAnalysisSpi();
    }
}
