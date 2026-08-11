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
package com.clougence.adapter.cloudberry;

import com.clougence.adapter.postgre.PostgreAttributeNames;
import com.clougence.schema.DsType;
import com.clougence.schema.umi.struts.UmiAttributeNames;

public class CloudberryAttributeNames extends PostgreAttributeNames {

    private CloudberryAttributeNames(String name){
        super(DsType.PostgreSQL.getShortName(), name);
    }

    public static final UmiAttributeNames DISTRIBUTED_TYPE   = check(new CloudberryAttributeNames("dt"));
    public static final UmiAttributeNames DISTRIBUTED_COLUMN = check(new CloudberryAttributeNames("dc"));
    public static final UmiAttributeNames APPEND_OPTIMIZED   = check(new CloudberryAttributeNames("ao"));
    public static final UmiAttributeNames BLOCK_SIZE         = check(new CloudberryAttributeNames("bs"));
    public static final UmiAttributeNames ORIENTATION        = check(new CloudberryAttributeNames("ori"));
    public static final UmiAttributeNames CHECK_SUM          = check(new CloudberryAttributeNames("cs"));
    public static final UmiAttributeNames COMPRESS_TYPE      = check(new CloudberryAttributeNames("ct"));
    public static final UmiAttributeNames COMPRESS_LEVEL     = check(new CloudberryAttributeNames("cl"));
}
