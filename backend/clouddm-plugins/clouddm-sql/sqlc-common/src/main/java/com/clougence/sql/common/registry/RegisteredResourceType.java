/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.common.registry;

/**
 * Resource kinds whose identity or semantics may require a dialect-owned registry.
 *
 * <p>Table includes readable relations such as views. Explicit view DDL remains a parser concern.</p>
 */
public enum RegisteredResourceType {
    TABLE,
    FUNCTION,
    PROCEDURE,
    TYPE
}
