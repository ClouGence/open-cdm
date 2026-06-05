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
package com.clougence.adapter.polar.porx;

import com.clougence.adapter.mysql.MyUmiAttributeNames;
import com.clougence.schema.DsType;
import com.clougence.schema.umi.struts.UmiAttributeNames;

/**
 * @version : 2021-04-01
 * @author zylicfc
 */
public class PolarDbXAttributeNames extends MyUmiAttributeNames {

    private PolarDbXAttributeNames(String name){
        super(DsType.PolarDbX.getShortName(), name);
    }

    protected PolarDbXAttributeNames(String space, String name){
        super(space, name);
    }

    public static final UmiAttributeNames SHARD_DEF     = check(new PolarDbXAttributeNames("pxcshard"));

    public static final UmiAttributeNames PARTITION_DEF = check(new PolarDbXAttributeNames("pxcpt"));
}
