package com.clougence.clouddm.ds.split.special;

import com.clougence.clouddm.ds.oceanbase.sql.ob4my.split.ObSplitAnalysisSpi;
import com.clougence.clouddm.ds.split.family.mysql.MySplitTest;

public class ObSplitTest extends MySplitTest {

    public ObSplitTest(){
        this.splitAnalysisSpi = new ObSplitAnalysisSpi();
    }
}
