/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.hana.sql.parser;

import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.sql.iso.sql2003.parser.antlr.Sql2003ParserBaseVisitor;

final class HanaSplitVisitor extends Sql2003ParserBaseVisitor<SecQueryType> {

    static final HanaSplitVisitor INSTANCE = new HanaSplitVisitor();

    private HanaSplitVisitor(){
    }

    @Override
    protected SecQueryType defaultResult() {
        return SecQueryType.UNKNOWN;
    }
}
