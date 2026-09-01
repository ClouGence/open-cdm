/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.common.registry;

import java.util.Set;

/** One datasource-owned database resource loaded from its XML catalog. */
public record DatabaseResource(RegisteredResourceType type, //
                               String name, String catalog, String schema,//
                               Set<String> versions, boolean skipPermission, boolean aggregate, String environment) {

    public boolean isNameMatched() { return "*".equals(catalog) && "*".equals(schema); }

    public boolean hasSchemaPattern() {
        return schema.indexOf('*') >= 0 && !"*".equals(schema);
    }

    public String[] registrationNameParts() {
        if (isNameMatched()) {
            return new String[] { name };
        }
        if ("*".equals(catalog)) {
            return new String[] { schema, name };
        }
        return new String[] { catalog, schema, name };
    }
}
