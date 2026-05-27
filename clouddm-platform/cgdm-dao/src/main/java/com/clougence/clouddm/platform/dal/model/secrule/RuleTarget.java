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
package com.clougence.clouddm.platform.dal.model.secrule;

import com.clougence.clouddm.sdk.model.analysis.TargetType;

import lombok.Getter;

/**
 * @author mode create time is 2023/05/21 13:27
 **/
@Getter
public enum RuleTarget {

    Catalog("RDB_CATALOG", TargetType.Catalog),
    Schema("RDB_SCHEMA", TargetType.Schema),
    Table("RDB_TABLE", TargetType.Table),
    View("RDB_VIEW", TargetType.View),
    Materialized("RDB_MATERIALIZED", TargetType.Materialized),
    Column("RDB_COLUMN", TargetType.Column),
    Index("RDB_INDEX", TargetType.Index),
    Constraint("RDB_CONSTRAINT", TargetType.Constraint),
    Sequence("RDB_SEQUENCE", TargetType.Sequence),
    Function("RDB_FUNCTION", TargetType.Function),
    Procedure("RDB_PROCEDURE", TargetType.Procedure),
    Trigger("RDB_TRIGGER", TargetType.Trigger),
    Synonym("RDB_SYNONYM", TargetType.Synonym),
    Key("CACHE_KEY", TargetType.Key),
    //
    Query("RDB_QUERY", TargetType.Query),
    Insert("RDB_INSERT", TargetType.Insert),
    Update("RDB_UPDATE", TargetType.Update),
    Delete("RDB_DELETE", TargetType.Delete),

    ;

    private final String     i18nKey;
    private final TargetType type;

    RuleTarget(String i18nKey, TargetType type){
        this.i18nKey = i18nKey;
        this.type = type;
    }

    public static RuleTarget ofTarget(TargetType type) {
        for (RuleTarget t : RuleTarget.values()) {
            if (t.type == type) {
                return t;
            }
        }
        return null;
    }
}
