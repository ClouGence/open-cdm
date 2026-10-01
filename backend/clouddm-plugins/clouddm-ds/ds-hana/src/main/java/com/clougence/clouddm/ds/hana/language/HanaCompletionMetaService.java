/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.ds.hana.language;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.language.completion.CompletionRequest;
import com.clougence.clouddm.sdk.service.execute.MetaCol;
import com.clougence.clouddm.sdk.service.execute.MetaObj;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.umi.struts.UmiTypes;

final class HanaCompletionMetaService implements MetaService {

    private final MetaService delegate;
    private final List<MetaObj> objects;
    private final Set<String> tables;

    HanaCompletionMetaService(MetaService delegate, CompletionRequest request) {
        this.delegate = delegate;
        this.objects = delegate.cachedObjectNames(request.getPrimaryUserId(), request.getCurrentUserId(), request.getDataSourceId(), request.getLevels(), request.getLevelsParam());
        this.tables = objects.stream().filter(object -> object.getType() == UmiTypes.Table || object.getType() == UmiTypes.View)
            .map(MetaObj::getName).collect(Collectors.toSet());
    }

    @Override
    public List<MetaCol> fetchTableColumns(String uid, long dsId, Map<UmiTypes, Object> levelsParam, String tableName) {
        // The shared column lookup does not check object authorization itself.
        if (!tables.contains(tableName)) {
            return List.of();
        }
        return delegate.fetchTableColumns(uid, dsId, levelsParam, tableName);
    }

    @Override
    public List<MetaObj> cachedObjectNames(String puid, String uid, long dsId, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) {
        return objects;
    }
}
