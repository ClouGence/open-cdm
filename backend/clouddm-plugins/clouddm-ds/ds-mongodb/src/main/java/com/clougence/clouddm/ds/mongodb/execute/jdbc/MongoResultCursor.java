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
package com.clougence.clouddm.ds.mongodb.execute.jdbc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import org.bson.Document;
import org.bson.types.Binary;

import com.clougence.drivers.adapter.AdapterCursor;
import com.clougence.drivers.adapter.AdapterRequest;
import com.clougence.drivers.adapter.JdbcColumn;

public class MongoResultCursor implements AdapterCursor {

    private final AdapterRequest         request;
    private final List<JdbcColumn>       columns;
    private final Map<Integer, String>   columnIdMap;
    //
    private final List<String>           warnings;
    private volatile Map<String, Object> currentRow;
    private final Iterator<Document> iterator;
    private final MongoResultBuffer  buffer;

    private volatile boolean             closed;

    public MongoResultCursor(AdapterRequest request, List<JdbcColumn> columns, MongoResultBuffer buffer){
        this.request = request;
        this.columns = columns;
        this.columnIdMap = new HashMap<>();
        this.warnings = new ArrayList<>();
        this.closed = false;
        this.buffer = buffer;
        this.iterator = buffer.iterator();

        for (int i = 0; i < columns.size(); i++) {
            JdbcColumn column = columns.get(i);
            this.columnIdMap.put(i + 1, column.name);
        }
    }

    @Override
    public List<JdbcColumn> columns() {
        return this.columns;
    }

    @Override
    public boolean next() throws SQLException {
        if (this.closed) {
            throw new SQLException("cursor is closed.");
        }

        if (iterator.hasNext()) {
            Document next = this.iterator.next();
            HashMap<String, Object> map = new HashMap<>();
            next.forEach((k, v) -> {
                if (v instanceof Document) {
                    map.put(k, ((Document) v).toJson());
                } else if (v instanceof Binary) {
                    map.put(k, ((Binary) v).getData());
                } else {
                    map.put(k, v);
                }
            });
            this.currentRow = map;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int batchSize() {
        return this.request.getFetchSize();
    }

    @Override
    public void close() throws IOException {
        this.buffer.close();
        this.closed = true;
    }

    @Override
    public Object column(int column) throws SQLException {
        if (this.currentRow == null) {
            throw new SQLException("empty ResultSet or After end of ResultSet");
        }
        if (!this.columnIdMap.containsKey(column)) {
            throw new SQLException("Before start of ResultSet.");
        }

        return this.currentRow.get(this.columnIdMap.get(column));
    }

    @Override
    public List<String> warnings() {
        return this.warnings;
    }

    @Override
    public void clearWarnings() {
        this.warnings.clear();
    }

    @Override
    public boolean isPending() { return true; }

    @Override
    public boolean isClose() { return this.closed; }

}
