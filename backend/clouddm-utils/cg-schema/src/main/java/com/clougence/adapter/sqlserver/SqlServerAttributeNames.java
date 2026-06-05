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
package com.clougence.adapter.sqlserver;

import com.clougence.schema.DsType;
import com.clougence.schema.umi.special.rdb.RdbAttributeNames;
import com.clougence.schema.umi.struts.UmiAttributeNames;

/**
 * @version : 2021-04-01
 * @author 赵永春 (zyc@hasor.net)
 */
public class SqlServerAttributeNames extends RdbAttributeNames {

    private SqlServerAttributeNames(String name){
        super(DsType.SqlServer.getShortName(), name);
    }

    // catalog
    public static final UmiAttributeNames READ_ONLY                     = check(new SqlServerAttributeNames("ro"));
    public static final UmiAttributeNames FULLTEXT_ENABLED              = check(new SqlServerAttributeNames("fe"));
    public static final UmiAttributeNames DEFAULT_LANGUAGE_NAME         = check(new SqlServerAttributeNames("dln"));
    public static final UmiAttributeNames DEFAULT_FULLTEXT_LANGUAGE     = check(new SqlServerAttributeNames("dfl"));
    public static final UmiAttributeNames DEFAULT_COLLATION_NAME        = check(new SqlServerAttributeNames("dcn"));

    // schema
    public static final UmiAttributeNames DEFAULT_CHARACTER_SET_CATALOG = check(new SqlServerAttributeNames("dcsc"));
    public static final UmiAttributeNames DEFAULT_CHARACTER_SET_SCHEMA  = check(new SqlServerAttributeNames("dcss"));
    public static final UmiAttributeNames DEFAULT_CHARACTER_SET_NAME    = check(new SqlServerAttributeNames("dcsn"));

    // table
    public static final UmiAttributeNames CREATE_DATE                   = check(new SqlServerAttributeNames("ctime"));
    public static final UmiAttributeNames MODIFY_DATE                   = check(new SqlServerAttributeNames("utime"));
    public static final UmiAttributeNames MAX_COLUMN_ID_USED            = check(new SqlServerAttributeNames("mciu"));
    public static final UmiAttributeNames TEXT_IN_ROW_LIMIT             = check(new SqlServerAttributeNames("tirl"));
    public static final UmiAttributeNames EXTERNAL                      = check(new SqlServerAttributeNames("ext"));

    // column
    public static final UmiAttributeNames DATA_TYPE                     = check(new SqlServerAttributeNames("dt"));
    public static final UmiAttributeNames MAX_LENGTH                    = check(new SqlServerAttributeNames("mlen"));
    public static final UmiAttributeNames COLLATION_NAME                = check(new SqlServerAttributeNames("cn"));
    public static final UmiAttributeNames ROW_GUID_COL                  = check(new SqlServerAttributeNames("guid"));
    public static final UmiAttributeNames IDENTITY                      = check(new SqlServerAttributeNames("ide"));
    public static final UmiAttributeNames COMPUTED_COL                  = check(new SqlServerAttributeNames("cc"));

    // PK\UK\FK\INDEX
    public static final UmiAttributeNames INDEX_TYPE                    = check(new SqlServerAttributeNames("it"));
    public static final UmiAttributeNames CONSTRAINT_TYPE               = check(new SqlServerAttributeNames("cont"));
}
