/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.hana.analysis;

import java.util.Locale;
import java.util.Set;

import com.clougence.sql.hana.parser.antlr.HanaParser;

public final class HanaSqlFunctions {
    // Only unquoted, unqualified built-ins avoid a program-object permission requirement.
    private static final Set<String> BUILT_INS = Set
        .of("COUNT", "SUM", "AVG", "MIN", "MAX", "COALESCE", "IFNULL", "NULLIF", "ABS", "ROUND", "FLOOR", "CEIL", "MOD", "POWER", "SQRT", "UPPER", "LOWER", "LENGTH", "SUBSTRING", "TRIM", "LTRIM", "RTRIM", "CONCAT", "TO_DATE", "TO_TIME", "TO_TIMESTAMP", "TO_VARCHAR", "TO_NVARCHAR", "TO_INTEGER", "HEXTOBIN", "TO_DECIMAL", "ADD_DAYS", "ADD_MONTHS", "DAYS_BETWEEN", "SECONDS_BETWEEN", "YEAR", "MONTH", "DAYOFMONTH", "ROW_NUMBER", "RANK", "DENSE_RANK", "LAG", "LEAD", "FIRST_VALUE", "LAST_VALUE", "SESSION_CONTEXT", "CURRENT_DATABASE");

    private HanaSqlFunctions(){
    }

    public static boolean isBuiltIn(HanaParser.QualifiedNameContext function) {
        return function.identifier().size() == 1 && function.identifier(0).QUOTED_IDENTIFIER() == null && BUILT_INS.contains(function.getText().toUpperCase(Locale.ROOT));
    }
}
