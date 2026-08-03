/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.ds.language.completion;

public record CompletionScriptCase(String path, String name, String languageClass, String meta, long dataSourceId, String catalog, String schema, String sqlText,
                                   int cursorLineNumber, int cursorColNumber, String expectJson) {
}
