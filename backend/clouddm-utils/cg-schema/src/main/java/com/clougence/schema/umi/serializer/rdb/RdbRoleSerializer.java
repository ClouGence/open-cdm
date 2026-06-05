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
package com.clougence.schema.umi.serializer.rdb;

import java.util.Map;

import com.clougence.schema.umi.serializer.UmiAttributeSetSerializer;
import com.clougence.schema.umi.special.rdb.RdbRole;

public class RdbRoleSerializer  extends UmiAttributeSetSerializer<RdbRole> {
    @Override
    public void readData(Map<String, Object> jsonMap, RdbRole readTo) {
        super.readData(jsonMap, readTo);
        readTo.setRoleName((String) jsonMap.get(KEY_RDB_ROLE_NAME));
    }

    @Override
    public void writeToMap(RdbRole umiAttr, Map<String, Object> toMap) {
        super.writeToMap(umiAttr, toMap);
        if (umiAttr.getRoleName() != null) {
            toMap.put(KEY_RDB_ROLE_NAME, umiAttr.getRoleName());
        }
    }
}