package com.clougence.adapter.selectdb;

import com.clougence.adapter.doris.DorisAttributeNames;
import com.clougence.schema.DsType;
import com.clougence.schema.umi.struts.UmiAttributeNames;

public class SelDorisAttributeNames extends DorisAttributeNames {

    public SelDorisAttributeNames(String name){
        super(DsType.SelectDB.getShortName(), name);
    }

    public static final UmiAttributeNames JUST_TABLE = check(new SelDorisAttributeNames("juta"));
}
