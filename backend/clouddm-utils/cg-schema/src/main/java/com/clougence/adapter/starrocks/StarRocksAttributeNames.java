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
package com.clougence.adapter.starrocks;

import com.clougence.adapter.mysql.MyUmiAttributeNames;
import com.clougence.schema.DsType;
import com.clougence.schema.umi.struts.UmiAttributeNames;

/**
 * @version : 2021-04-01
 * @author 赵永春 (zyc@hasor.net)
 */
public class StarRocksAttributeNames extends MyUmiAttributeNames {

    private StarRocksAttributeNames(String name){
        super(DsType.StarRocks.getShortName(), name);
    }

    // Table
    public static final UmiAttributeNames EXTERNAL               = new StarRocksAttributeNames("eal");
    public static final UmiAttributeNames BUCKET_NUMBER          = new StarRocksAttributeNames("bn");
    public static final UmiAttributeNames PROPERTIES_CONFIG      = new StarRocksAttributeNames("conf");
    public static final UmiAttributeNames KEY_TYPE               = new StarRocksAttributeNames("kt");
    public static final UmiAttributeNames ORDER_BY               = new StarRocksAttributeNames("ob");
    public static final UmiAttributeNames DISTRIBUTED_BY_TYPE    = new StarRocksAttributeNames("dbt");
    public static final UmiAttributeNames DISTRIBUTED_BY_COLUMNS = new StarRocksAttributeNames("dbc");
    public static final UmiAttributeNames PARTITION_EXPR         = new StarRocksAttributeNames("part");

    // column
    public static final UmiAttributeNames AGG_TYPE               = new StarRocksAttributeNames("at");
    public static final UmiAttributeNames AUTO_INCREMENT         = new StarRocksAttributeNames("ai");
    public static final UmiAttributeNames GEN_EXPR               = new StarRocksAttributeNames("gexpr");
    public static final UmiAttributeNames ARRAY_DEPTH            = new StarRocksAttributeNames("adep");

}
