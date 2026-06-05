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
package com.clougence.adapter.greenplum;

import com.clougence.adapter.postgre.PostgreAttributeNames;
import com.clougence.schema.DsType;
import com.clougence.schema.umi.struts.UmiAttributeNames;

public class GreenplumAttributeNames extends PostgreAttributeNames {

    private GreenplumAttributeNames(String name){
        super(DsType.PostgreSQL.getShortName(), name);
    }

    protected GreenplumAttributeNames(String space, String name){
        super(space, name);
    }

    public static final UmiAttributeNames DISTRIBUTED_TYPE   = check(new GreenplumAttributeNames("dt"));
    public static final UmiAttributeNames DISTRIBUTED_COLUMN = check(new GreenplumAttributeNames("dc"));
    public static final UmiAttributeNames APPEND_OPTIMIZED   = check(new GreenplumAttributeNames("ao"));
    public static final UmiAttributeNames BLOCK_SIZE         = check(new GreenplumAttributeNames("bs"));
    public static final UmiAttributeNames ORIENTATION        = check(new GreenplumAttributeNames("ori"));
    public static final UmiAttributeNames CHECK_SUM          = check(new GreenplumAttributeNames("cs"));
    public static final UmiAttributeNames COMPRESS_TYPE      = check(new GreenplumAttributeNames("ct"));
    public static final UmiAttributeNames COMPRESS_LEVEL     = check(new GreenplumAttributeNames("cl"));

}
