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

import com.clougence.schema.umi.serializer.UmiAttributeSetSerializer;
import com.clougence.schema.umi.special.rdb.RdbDbLink;

import java.util.Map;

public class RdbDbLinkSerializer extends UmiAttributeSetSerializer<RdbDbLink> {

    @Override
    public void readData(Map<String, Object> jsonMap, RdbDbLink readTo) {
        super.readData(jsonMap, readTo);
        readTo.setName((String) jsonMap.get(KEY_NAME));
        readTo.setUsername((String) jsonMap.get(KEY_RDB_DBLINK_USERNAME));
        readTo.setSchema((String) jsonMap.get(KEY_RDB_SCHEMA));
        readTo.setHost((String) jsonMap.get(KEY_RDB_DBLINK_HOST));
    }

    @Override
    public void writeToMap(RdbDbLink umiAttr, Map<String, Object> toMap) {
        super.writeToMap(umiAttr, toMap);
        if (umiAttr.getName() != null) {
            toMap.put(KEY_NAME, umiAttr.getName());
        }
        if (umiAttr.getUsername() != null) {
            toMap.put(KEY_RDB_DBLINK_USERNAME, umiAttr.getUsername());
        }
        if (umiAttr.getSchema() != null) {
            toMap.put(KEY_RDB_SCHEMA, umiAttr.getSchema());
        }
        if (umiAttr.getHost() != null) {
            toMap.put(KEY_RDB_DBLINK_HOST, umiAttr.getHost());
        }
    }
}
