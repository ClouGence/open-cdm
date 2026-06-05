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
package com.clougence.adapter.clickhouse;

import com.clougence.schema.DsType;
import com.clougence.schema.umi.special.rdb.RdbAttributeNames;
import com.clougence.schema.umi.struts.UmiAttributeNames;

/**
 * @version : 2021-04-01
 * @author 赵永春 (zyc@hasor.net)
 */
public class ClickHouseAttributeNames extends RdbAttributeNames {

    private ClickHouseAttributeNames(String name){
        super(DsType.ClickHouse.getShortName(), name);
    }

    protected ClickHouseAttributeNames(String space, String name){
        super(space, name);
    }

    // schema

    // table
    public static final UmiAttributeNames DATA_PATH       = check(new ClickHouseAttributeNames("dpath"));
    public static final UmiAttributeNames METADATA_PATH   = check(new ClickHouseAttributeNames("mpath"));
    public static final UmiAttributeNames ENGINE          = check(new ClickHouseAttributeNames("eng"));
    public static final UmiAttributeNames ENGINE_PARAMS   = check(new ClickHouseAttributeNames("engp"));
    public static final UmiAttributeNames CK_CLUSTER      = check(new ClickHouseAttributeNames("ckcluster"));
    public static final UmiAttributeNames MULTI_REPLICA   = check(new ClickHouseAttributeNames("mreplica"));
    public static final UmiAttributeNames SHARD_NAME      = check(new ClickHouseAttributeNames("shard"));
    public static final UmiAttributeNames REPLICA_NAME    = check(new ClickHouseAttributeNames("replica"));
    public static final UmiAttributeNames ENGINE_FULL     = check(new ClickHouseAttributeNames("engf"));
    public static final UmiAttributeNames TEMPORARY       = check(new ClickHouseAttributeNames("temp"));

    // column
    public static final UmiAttributeNames DATA_TYPE       = check(new ClickHouseAttributeNames("dt"));
    public static final UmiAttributeNames COLUMN_TYPE     = check(new ClickHouseAttributeNames("ct"));
    public static final UmiAttributeNames DATETIME_ZONE   = check(new ClickHouseAttributeNames("zone"));
    public static final UmiAttributeNames PARTITION_KEY   = check(new ClickHouseAttributeNames("ptk"));
    public static final UmiAttributeNames SORTING_KEY     = check(new ClickHouseAttributeNames("stk"));
    public static final UmiAttributeNames SORTING_COLUMNS = check(new ClickHouseAttributeNames("stkcs"));
    public static final UmiAttributeNames SAMPLING_KEY    = check(new ClickHouseAttributeNames("spk"));
    public static final UmiAttributeNames MATERIALIZED    = check(new ClickHouseAttributeNames("mat"));
    public static final UmiAttributeNames EPHEMERAL       = check(new ClickHouseAttributeNames("eph"));
    public static final UmiAttributeNames ALIAS           = check(new ClickHouseAttributeNames("alias"));
    public static final UmiAttributeNames COMPRESSION     = check(new ClickHouseAttributeNames("codec"));
    public static final UmiAttributeNames TTL             = check(new ClickHouseAttributeNames("ttl"));

    // PK\UK\FK\INDEX
    // public static final UmiSchemaAttributeNames STORAGE_TYPE = new ClickHouseAttributeNames("storageType");
    // public static final UmiSchemaAttributeNames CONSTRAINT_TYPE = new ClickHouseAttributeNames("constraintType");
    // public static final UmiSchemaAttributeNames SUB_PART = new ClickHouseAttributeNames("subPart");
}
