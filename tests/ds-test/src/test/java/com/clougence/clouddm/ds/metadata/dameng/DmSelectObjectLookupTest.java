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
package com.clougence.clouddm.ds.metadata.dameng;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.dameng.execute.DmMetaProviderDm;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;

public class DmSelectObjectLookupTest extends DmMetaProviderDm {
    private final List<UmiTypes> lookups = new ArrayList<>();
    private UmiTypes             objectType;
    private int                  columnQueries;

    public DmSelectObjectLookupTest(){
        super(null);
    }

    @Test
    public void tableReturnsColumnsWithoutViewLookup() throws SQLException {
        assertObject(UmiTypes.Table, List.of(UmiTypes.Table));
    }

    @Test
    public void viewReturnsColumnsAfterTableMiss() throws SQLException {
        assertObject(UmiTypes.View, List.of(UmiTypes.Table, UmiTypes.View));
    }

    @Test
    public void missingObjectDoesNotLoadColumns() throws SQLException {
        assertNull(loadSelectObject(null, "TEST_SCHEMA", "MISSING"));
        assertEquals(List.of(UmiTypes.Table, UmiTypes.View), this.lookups);
        assertEquals(0, this.columnQueries);
    }

    private void assertObject(UmiTypes type, List<UmiTypes> expectedLookups) throws SQLException {
        this.objectType = type;
        RdbTable result = (RdbTable) loadSelectObject(null, "TEST_SCHEMA", "T1");
        assertEquals(type, result.getUmiType());
        assertEquals("T1", result.getName());
        assertEquals(1, result.getColumns().size());
        if (type == UmiTypes.View) {
            assertEquals("VIEW_ID", result.getColumns().get("VIEW_ID").getName());
        } else {
            assertEquals("ID", result.getColumns().get("ID").getName());
        }

        assertEquals(expectedLookups, this.lookups);
        assertEquals(1, this.columnQueries);
    }

    private List<RdbTable> lookup(String schema, List<String> names, UmiTypes type) {
        assertEquals("TEST_SCHEMA", schema);
        this.lookups.add(type);
        if (this.objectType != type) {
            return List.of();
        }

        RdbTable table = new RdbTable();
        table.setName(names.get(0));
        table.setUmiType(type);
        return List.of(table);
    }

    @Override
    protected List<RdbTable> fetchTableByPart(Connection conn, String catalog, String schema, List<String> names) {
        return lookup(schema, names, UmiTypes.Table);
    }

    @Override
    protected List<RdbTable> fetchViewByPart(Connection conn, String catalog, String schema, List<String> names) {
        return lookup(schema, names, UmiTypes.View);
    }

    @Override
    protected List<RdbTable> fetchMaterializedByPart(Connection conn, String catalog, String schema, List<String> names) {
        return lookup(schema, names, UmiTypes.Materialized);
    }

    @Override
    protected Map<String, List<RdbColumn>> fetchTableColumns(Connection conn, String catalog, String schema, List<String> names) {
        this.columnQueries++;
        RdbColumn column = new RdbColumn();
        assertEquals(UmiTypes.Table, this.objectType);
        column.setName("ID");
        return Map.of(names.get(0), List.of(column));
    }

    @Override
    protected Map<String, List<RdbColumn>> fetchViewColumns(Connection conn, String catalog, String schema, List<String> names) {
        this.columnQueries++;
        assertEquals(UmiTypes.View, this.objectType);
        RdbColumn column = new RdbColumn();
        column.setName("VIEW_ID");
        return Map.of(names.get(0), List.of(column));
    }
}
