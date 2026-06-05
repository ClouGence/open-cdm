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
package com.clougence.adapter.redis;

import com.clougence.schema.DsType;
import com.clougence.schema.umi.special.rdb.RdbAttributeNames;
import com.clougence.schema.umi.struts.UmiAttributeNames;

/**
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2021-04-01
 */
public class RedisAttributeNames extends RdbAttributeNames {

    private RedisAttributeNames(String name){
        super(DsType.Redis.getShortName(), name);
    }

    protected RedisAttributeNames(String space, String name){
        super(space, name);
    }

    // key
    public static final UmiAttributeNames KEY_TYPE = check(new RedisAttributeNames("type"));
}
