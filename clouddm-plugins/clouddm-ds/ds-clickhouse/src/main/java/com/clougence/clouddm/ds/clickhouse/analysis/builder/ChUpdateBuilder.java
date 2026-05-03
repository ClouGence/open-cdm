package com.clougence.clouddm.ds.clickhouse.analysis.builder;

import java.util.*;

import com.clougence.clouddm.ds.clickhouse.analysis.secrules.ChUpdateDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.UpdateBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WithSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbUpdateDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

public class ChUpdateBuilder extends UpdateBuilder {

    public ChUpdateBuilder(Stack<List<WithSelectDomain>> selectStack){
        super(selectStack);
    }

    @Override
    protected RdbUpdateDomain getRdbUpdateDomain() {
        ChUpdateDomain pgUpdateDomain = new ChUpdateDomain();
        pgUpdateDomain.setSetColumns(new ArrayList<>());
        return pgUpdateDomain;
    }

    @Override
    public List<Domain> build() {
        if (!this.selectStack.peek().isEmpty()) {
            this.updateDomain.setHasWith(true);
        }

        if (CollectionUtils.isNotEmpty(nameList)) {
            Map<UmiTypes, String> map = BuilderUtil.parseTableName(nameList);
            this.updateDomain.setTable(map.get(UmiTypes.Table));
            this.updateDomain.setSchema(map.get(UmiTypes.Schema));
            this.updateDomain.setCatalog(map.get(UmiTypes.Catalog));
        }
        updateDomain.setAuditKind(SecQueryKind.DML);
        updateDomain.setSqlType(SecQueryType.UPDATE);
        if (updateDomain.getWhereColumns() == null) {
            updateDomain.setWhereColumns(new ArrayList<>());
        }
        return Collections.singletonList(this.updateDomain);
    }
}
