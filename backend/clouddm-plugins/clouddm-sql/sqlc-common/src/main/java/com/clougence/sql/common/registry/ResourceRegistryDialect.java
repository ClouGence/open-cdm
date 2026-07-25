/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.common.registry;

/**
 * Identifier normalization used only by registered SQL resources.
 *
 * <p>This is intentionally separate from schema and completion dialect interfaces. A concrete
 * data source may bridge this method to its real dialect implementation.</p>
 */
public interface ResourceRegistryDialect {

    String normalizeIdentifier(String identifier);

    boolean isQuotedIdentifier(String identifier);
}
