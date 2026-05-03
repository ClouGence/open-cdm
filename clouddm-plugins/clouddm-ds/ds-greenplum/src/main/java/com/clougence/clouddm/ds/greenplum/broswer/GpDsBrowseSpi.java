package com.clougence.clouddm.ds.greenplum.broswer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.postgres.definition.ui.browser.PgDsBrowseSpi;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

/**
 * @author CloudConceal
 */
public class GpDsBrowseSpi extends PgDsBrowseSpi {

    @Override
    public Map<UmiTypes, List<UmiTypes>> getLeafGroupMap() {
        List<UmiTypes> schemaList = Arrays.asList(UmiTypes.Table, UmiTypes.View, UmiTypes.Materialized, UmiTypes.Function, UmiTypes.Trigger, UmiTypes.Sequence);
        return CollectionUtils.asMap(UmiTypes.Schema, schemaList);
    }

    @Override
    public List<UmiTypes> getLeafExpand() { return Arrays.asList(UmiTypes.Table, UmiTypes.View, UmiTypes.Materialized); }
}
