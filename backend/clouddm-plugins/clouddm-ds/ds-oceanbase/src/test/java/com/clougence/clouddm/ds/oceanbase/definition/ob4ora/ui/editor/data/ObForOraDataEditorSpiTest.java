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
package com.clougence.clouddm.ds.oceanbase.definition.ob4ora.ui.editor.data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.oceanbase.definition.ob4ora.ui.browser.ObForOraDsBrowseSpi;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;

public class ObForOraDataEditorSpiTest {

    private RdbTable sampleTable() {
        RdbTable table = new RdbTable();
        table.setSchema("TEST_SCHEMA");
        table.setName("DEMO_TABLE");
        table.setUmiType(UmiTypes.Table);

        Map<String, RdbColumn> cols = new LinkedHashMap<>();
        RdbColumn id = new RdbColumn();
        id.setName("ID");
        cols.put("ID", id);

        RdbColumn name = new RdbColumn();
        name.setName("NAME");
        cols.put("NAME", name);

        table.setColumns(cols);
        return table;
    }

    @Test
    public void testBuildSelectAndTableMenuEnabled() {
        ObForOraDataEditorSpi spi = new ObForOraDataEditorSpi();
        RdbTable table = sampleTable();

        String firstPage = spi.buildSelect(table, "ID > 0", "ID DESC", 0, 50);
        Assert
            .assertEquals("select \"ROWID\", \"ID\", \"NAME\" from (select ROWID as \"ROWID\", \"ID\", \"NAME\" from \"TEST_SCHEMA\".\"DEMO_TABLE\" where ID > 0 order by ID DESC) where rownum <= 50", firstPage);

        String secondPage = spi.buildSelect(table, null, "ID ASC", 50, 20);
        Assert
            .assertEquals("select \"ROWID\", \"ID\", \"NAME\" from (select cdm_t.*, rownum as cdm_rn from (select ROWID as \"ROWID\", \"ID\", \"NAME\" from \"TEST_SCHEMA\".\"DEMO_TABLE\" order by ID ASC) cdm_t where rownum <= 70) where cdm_rn > 50", secondPage);

        ObForOraDsBrowseSpi browseSpi = new ObForOraDsBrowseSpi();
        List<String> tableMenus = browseSpi.getMenus(DsMenuType.RdbTable);
        Assert.assertTrue(tableMenus.contains(DsFeatureIDs.MENU_BROWSE_TABLE_DATA));
    }
}
