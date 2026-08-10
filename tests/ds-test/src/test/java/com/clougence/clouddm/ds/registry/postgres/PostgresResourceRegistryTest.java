/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.registry.postgres;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.clougence.sql.postgres.analysis.sysobj.PgResourceRegistry;
import com.clougence.sql.postgres.parser.PostgresVersion;

public class PostgresResourceRegistryTest {

    private static final List<PostgresVersion> SUPPORTED_VERSIONS = List.of(
        PostgresVersion.POSTGRES_12,
        PostgresVersion.POSTGRES_13,
        PostgresVersion.POSTGRES_14,
        PostgresVersion.POSTGRES_15,
        PostgresVersion.POSTGRES_16,
        PostgresVersion.POSTGRES_17,
        PostgresVersion.POSTGRES_18);

    private final PgResourceRegistry resources = PgResourceRegistry.instance();

    @Test
    public void coreResourcesCoverEverySupportedMajorVersion() {
        for (PostgresVersion version : SUPPORTED_VERSIONS) {
            Assertions.assertTrue(resources.isSystemFunction("pg_catalog", "abs", version), version.name());
            Assertions.assertTrue(resources.isSystemRelation("pg_catalog", "pg_class", version), version.name());
            Assertions.assertTrue(resources.isSystemType("pg_catalog", "int4", version), version.name());
        }

        Assertions.assertFalse(resources.isSystemFunction("array_append_support", PostgresVersion.POSTGRES_17));
        Assertions.assertTrue(resources.isSystemFunction("array_append_support", PostgresVersion.POSTGRES_18));
    }

    @Test
    public void contribResourcesRetainTheirVersionBoundaries() {
        Assertions.assertTrue(resources.isSystemRelation("installed_schema", "pg_buffercache", PostgresVersion.POSTGRES_12));
        Assertions.assertFalse(resources.isSystemFunction("installed_schema", "pg_buffercache_evict", PostgresVersion.POSTGRES_16));
        Assertions.assertTrue(resources.isSystemFunction("installed_schema", "pg_buffercache_evict", PostgresVersion.POSTGRES_17));
        Assertions.assertTrue(resources.isSystemFunction("installed_schema", "pg_buffercache_evict", PostgresVersion.POSTGRES_18));
    }

    @Test
    public void vectorResourcesCoverEverySupportedMajorVersion() {
        for (PostgresVersion version : SUPPORTED_VERSIONS) {
            Assertions.assertTrue(resources.isSystemFunction("installed_schema", "array_to_halfvec", version), version.name());
            Assertions.assertTrue(resources.isSystemFunction("another_schema", "array_to_halfvec", version), version.name());
            Assertions.assertTrue(resources.isSystemType("installed_schema", "vector", version), version.name());
        }
    }

    @Test
    public void postgisResourcesCoverEverySupportedMajorVersion() {
        for (PostgresVersion version : SUPPORTED_VERSIONS) {
            Assertions.assertTrue(resources.isSystemFunction("installed_schema", "st_point", version), version.name());
            Assertions.assertTrue(resources.isSystemFunction("another_schema", "st_point", version), version.name());
            Assertions.assertTrue(resources.isSystemRelation("installed_schema", "spatial_ref_sys", version), version.name());
            Assertions.assertTrue(resources.isSystemRelation("another_schema", "spatial_ref_sys", version), version.name());
            Assertions.assertTrue(resources.isSystemType("installed_schema", "geometry", version), version.name());
        }
    }
}
