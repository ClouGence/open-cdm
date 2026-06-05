/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.StringDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTableDomain;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

public abstract class CommentBuilder implements DomainBuilder {

    private TargetType   targetType;

    private List<String> nameList = new ArrayList<>();

    private String       comment;

    public CommentBuilder(TargetType targetType){
        this.targetType = targetType;
    }

    protected RdbColumnDomain getColumnDomain() { return new RdbColumnDomain(); }

    protected RdbTableDomain getTableDomain() { return new RdbTableDomain(); }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource type) {
        if (type == DomainSource.OBJ_NAME) {
            Domain domain = list.get(0);
            ObjNameDomain objNameDomain = (ObjNameDomain) domain;
            this.nameList = objNameDomain.getNameList();
        } else if (type == DomainSource.COMMENT) {
            StringDomain constraintDomain = (StringDomain) list.get(0);
            String comment = constraintDomain.getVal();
            this.comment = comment.substring(1, comment.length() - 1);
        } else {
            throw new UnsupportedOperationException(type.name());
        }

    }

    @Override
    public List<Domain> build() {
        if (targetType == TargetType.Column) {
            RdbColumnDomain rdbColumnDomain = getColumnDomain();
            rdbColumnDomain.setSqlType(SecQueryType.COMMENT_COLUMN);
            rdbColumnDomain.setAuditKind(SecQueryKind.ALTER);

            Map<UmiTypes, String> map = BuilderUtil.parseColumnName(nameList);
            rdbColumnDomain.setCatalog(map.get(UmiTypes.Catalog));
            rdbColumnDomain.setSchema(map.get(UmiTypes.Schema));
            rdbColumnDomain.setTable(map.get(UmiTypes.Table));
            rdbColumnDomain.setColumn(map.get(UmiTypes.Column));

            rdbColumnDomain.setComment(this.comment);
            return Collections.singletonList(rdbColumnDomain);
        } else if (targetType == TargetType.Table) {
            RdbTableDomain rdbTableDomain = getTableDomain();
            rdbTableDomain.setSqlType(SecQueryType.COMMENT_TABLE);
            rdbTableDomain.setAuditKind(SecQueryKind.ALTER);

            Map<UmiTypes, String> map = BuilderUtil.parseTableName(nameList);
            rdbTableDomain.setCatalog(map.get(UmiTypes.Catalog));
            rdbTableDomain.setSchema(map.get(UmiTypes.Schema));
            rdbTableDomain.setTable(map.get(UmiTypes.Table));
            rdbTableDomain.setComment(comment);

            return Collections.singletonList(rdbTableDomain);
        }

        throw new UnsupportedOperationException(targetType.name());
    }
}
