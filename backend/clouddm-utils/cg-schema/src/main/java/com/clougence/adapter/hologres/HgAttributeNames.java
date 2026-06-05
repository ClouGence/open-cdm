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
package com.clougence.adapter.hologres;

import com.clougence.schema.DsType;
import com.clougence.schema.umi.special.rdb.RdbAttributeNames;
import com.clougence.schema.umi.struts.UmiAttributeNames;

/**
 * @version : 2021-04-01
 * @author 赵永春 (zyc@hasor.net)
 */
public class HgAttributeNames extends RdbAttributeNames {

    private HgAttributeNames(String name){
        super(DsType.Hologres.getShortName(), name);
    }

    // Common
    public static final UmiAttributeNames OWNER                  = check(new HgAttributeNames("cowner"));
    public static final UmiAttributeNames LAST_META_MODIFIED     = check(new HgAttributeNames("clmm"));
    public static final UmiAttributeNames LAST_DATA_MODIFIED     = check(new HgAttributeNames("cldm"));
    public static final UmiAttributeNames LAST_ACCESS_MODIFIED   = check(new HgAttributeNames("clam"));
    public static final UmiAttributeNames DATA_BYTES             = check(new HgAttributeNames("cdsize"));
    public static final UmiAttributeNames PHYSICAL_BYTES         = check(new HgAttributeNames("cpsize"));
    public static final UmiAttributeNames FILE_NUMBER            = check(new HgAttributeNames("cfsnum"));
    public static final UmiAttributeNames RECORD_NUM             = check(new HgAttributeNames("ccnt"));
    public static final UmiAttributeNames LIFE_DAY               = check(new HgAttributeNames("clife"));
    public static final UmiAttributeNames ARCHIVED               = check(new HgAttributeNames("carc"));
    public static final UmiAttributeNames CREATED_TIME           = check(new HgAttributeNames("ccre"));

    // table
    public static final UmiAttributeNames TABLE_ID               = check(new HgAttributeNames("tid"));
    public static final UmiAttributeNames TABLE_CRYPTO_ALGO_NAME = check(new HgAttributeNames("tcry"));
    public static final UmiAttributeNames TABLE_MAX_LABEL        = check(new HgAttributeNames("tlabel"));
    public static final UmiAttributeNames TABLE_LABEL            = check(new HgAttributeNames("tlabel"));

    // partition

    // column
    public static final UmiAttributeNames COLUMN_TYPE            = check(new HgAttributeNames("ct"));

    // func
    public static final UmiAttributeNames CLASS_PATH             = check(new HgAttributeNames("fclass"));

    // role
    public static final UmiAttributeNames ROLE_POLICY            = check(new HgAttributeNames("rpol"));
    public static final UmiAttributeNames ROLE_TYPE              = check(new HgAttributeNames("rtype"));
    public static final UmiAttributeNames ROLE_ACL               = check(new HgAttributeNames("racl"));

}
