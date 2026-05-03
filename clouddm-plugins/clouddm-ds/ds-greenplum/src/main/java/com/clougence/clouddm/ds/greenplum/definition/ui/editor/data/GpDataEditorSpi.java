package com.clougence.clouddm.ds.greenplum.definition.ui.editor.data;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.data.PgDataEditorSpi;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorColumn;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.i18n.I18nUtils;

public class GpDataEditorSpi extends PgDataEditorSpi {

    @Override
    protected StringBuilder buildSelect(Dialect dialect, RdbTable tableMeta) {
        StringBuilder sb = super.buildSelect(dialect, tableMeta);
        if (tableMeta.getUmiType() == UmiTypes.Table) {
            sb.insert(0, "gp_segment_id, ");
        }
        return sb;
    }

    @Override
    public void configTableHeader(RdbTable rdbTable, List<DataEditorColumn> headerCols, Map<String, RdbColumn> colMap, I18nUtils i18nUtils) {
        super.configTableHeader(rdbTable, headerCols, colMap, i18nUtils);

        if (rdbTable.getUmiType() == UmiTypes.Table) {
            DataEditorColumn segment = new DataEditorColumn();
            segment.setHide(true);
            segment.setInsertReadOnly(true);
            segment.setUpdateReadOnly(true);
            segment.setWhereKey(true);
            segment.setColumn("gp_segment_id");
            headerCols.add(1, segment);
        }
    }

    @Override
    protected StringBuilder buildWhere(Dialect dialect, RdbTable tableMeta, Map<String, String> data) {
        StringBuilder sb = new StringBuilder();
        sb.append("ctid = '");
        sb.append(data.get("ctid"));
        sb.append("' and gp_segment_id = ");
        sb.append(data.get("gp_segment_id"));
        return sb;
    }
}
