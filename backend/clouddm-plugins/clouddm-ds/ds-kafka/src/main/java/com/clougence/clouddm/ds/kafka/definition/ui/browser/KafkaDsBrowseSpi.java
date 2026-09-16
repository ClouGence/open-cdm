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
package com.clougence.clouddm.ds.kafka.definition.ui.browser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.kafka.dialect.KafkaDialect;
import com.clougence.clouddm.dsfamily.definition.ui.browser.AbstractDsBrowseSpi;
import com.clougence.clouddm.dsfamily.definition.ui.browser.RdbUiMenuDef;
import com.clougence.clouddm.sdk.ui.browser.DsCaseType;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

public class KafkaDsBrowseSpi extends AbstractDsBrowseSpi {

    @Override
    public List<UmiTypes> getLevels() { return Collections.singletonList(UmiTypes.Schema); }

    @Override
    public Map<UmiTypes, List<UmiTypes>> getLeafGroupMap() {
        List<UmiTypes> schemaLeaves = Arrays.asList(UmiTypes.Topic, UmiTypes.ConsumerGroup, UmiTypes.Endpoint);
        return CollectionUtils.asMap(UmiTypes.Schema, schemaLeaves);
    }

    @Override
    protected Dialect dialect() {
        return KafkaDialect.INSTANCE;
    }

    @Override
    public DsCaseType getCaseType() { return DsCaseType.Sensitive; }

    @Override
    public List<String> getMenus(DsMenuType targetType) {
        switch (targetType) {
            case Instance: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_INSTANCE_TWO_LAYERS;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_SCHEMA_CREATE, MENU_BROWSE_INSTANCE_DROP));
            }
            case RdbCatalog:
            case RdbSchema: {
                List<String> menus = RdbUiMenuDef.DEFAULT_RDB_SCHEMA;
                return filterMenus(menus, Arrays.asList(MENU_BROWSE_SCHEMA_CREATE, MENU_BROWSE_TABLE_CREATE, MENU_BROWSE_SCHEMA_RENAME, MENU_BROWSE_SCHEMA_DROP));
            }
            case Topic:
            case ConsumerGroup:
            case Endpoint: {
                return Collections.emptyList();
            }
            default:
                return super.getMenus(targetType);
        }
    }
}
