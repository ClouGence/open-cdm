/*
 * Copyright 2015-2022 the original author or authors.
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
package com.clougence.drivers.adapter;

import java.util.Objects;

public class JdbcColumn {

    public final String catalog;
    public final String schema;
    public final String table;
    public final String name;
    public final String type;

    public JdbcColumn(String name, String type, String table, String catalog, String schema){
        if (name == null) {
            throw new IllegalArgumentException("[name] must not be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("[type] must not be null");
        }
        if (table == null) {
            throw new IllegalArgumentException("[table] must not be null");
        }
        if (catalog == null) {
            throw new IllegalArgumentException("[catalog] must not be null");
        }
        if (schema == null) {
            throw new IllegalArgumentException("[schema] must not be null");
        }
        this.name = name;
        this.type = type;
        this.table = table;
        this.catalog = catalog;
        this.schema = schema;
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        if (!this.table.isEmpty()) {
            b.append(this.table).append('.');
        }
        b.append(this.name).append("<type=[").append(this.type).append(']');
        if (!this.catalog.isEmpty()) {
            b.append(" catalog=[").append(this.catalog).append(']');
        }
        if (!this.schema.isEmpty()) {
            b.append(" schema=[").append(this.schema).append(']');
        }
        return b.append('>').toString();
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        JdbcColumn other = (JdbcColumn) obj;
        return (this.name.equals(other.name) &&      //
                this.type.equals(other.type) &&      //
                this.table.equals(other.table) &&    //
                this.catalog.equals(other.catalog) &&//
                this.schema.equals(other.schema));
    }

    public int hashCode() {
        return Objects.hash(this.name, this.type, this.table, this.catalog, this.schema);
    }
}
