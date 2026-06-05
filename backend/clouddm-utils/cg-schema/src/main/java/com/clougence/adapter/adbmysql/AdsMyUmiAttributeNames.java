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
package com.clougence.adapter.adbmysql;

import com.clougence.adapter.mysql.MyUmiAttributeNames;
import com.clougence.schema.DsType;
import com.clougence.schema.umi.struts.UmiAttributeNames;

/**
 * @version : 2021-04-01
 * @author 赵永春 (zyc@hasor.net)
 */
public class AdsMyUmiAttributeNames extends MyUmiAttributeNames {

    private AdsMyUmiAttributeNames(String name){
        super(DsType.AdbForMySQL.getShortName(), name);
    }

    // table
    public static final UmiAttributeNames STORAGE_POLICY            = check(new AdsMyUmiAttributeNames("sp"));
    public static final UmiAttributeNames STORAGE_POLICY_HOT_PT_CNT = check(new AdsMyUmiAttributeNames("ptcnt"));
    public static final UmiAttributeNames BLOCK_SIZE                = check(new AdsMyUmiAttributeNames("bsize"));
    public static final UmiAttributeNames RT_ENGINE                 = check(new AdsMyUmiAttributeNames("reng"));
    public static final UmiAttributeNames TAB_PROPERTIES            = check(new AdsMyUmiAttributeNames("tabp"));

    // column
    public static final UmiAttributeNames TYPE_STRUCTURE            = check(new AdsMyUmiAttributeNames("st"));

    // PK\UK\FK\INDEX
    public static final UmiAttributeNames ANN_ALGORITHM             = check(new AdsMyUmiAttributeNames("anna"));
    public static final UmiAttributeNames ANN_DISFUNCTION           = check(new AdsMyUmiAttributeNames("annd"));

}
