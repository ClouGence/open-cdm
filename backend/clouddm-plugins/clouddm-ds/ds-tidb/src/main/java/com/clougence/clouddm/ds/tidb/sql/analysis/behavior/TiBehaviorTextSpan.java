/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.tidb.sql.analysis.behavior;

final class TiBehaviorTextSpan {
    private final int start;
    private final int end;

    TiBehaviorTextSpan(int start, int end){
        this.start = start;
        this.end = end;
    }

    int start() {
        return start;
    }

    int end() {
        return end;
    }

    String text(String source) {
        return source.substring(start, end);
    }
}
