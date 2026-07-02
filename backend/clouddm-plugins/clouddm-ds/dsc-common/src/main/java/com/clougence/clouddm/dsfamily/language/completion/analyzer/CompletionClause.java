/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.dsfamily.language.completion.analyzer;

public enum CompletionClause {
    SELECT_LIST,
    FROM_TABLE,
    JOIN_TABLE,
    JOIN_CONDITION,
    WHERE_CONDITION,
    GROUP_BY,
    ORDER_BY,
    INSERT_TARGET,
    INSERT_COLUMNS,
    UPDATE_TARGET,
    UPDATE_SET,
    UNKNOWN
}
