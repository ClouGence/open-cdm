/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.sdk.execute.session;

import java.sql.SQLException;

public final class ExplainStatementUtils {

    private ExplainStatementUtils(){
    }

    public static void requireExplain(QueryRequest request) throws SQLException {
        String queryBody = request.getQueryBody();
        String normalized = queryBody == null ? "" : queryBody.stripLeading();
        int keywordLength = "EXPLAIN".length();
        boolean explain = normalized.regionMatches(true, 0, "EXPLAIN", 0, keywordLength);
        if (explain && normalized.length() > keywordLength) {
            explain = Character.isWhitespace(normalized.charAt(keywordLength));
        }
        if (!explain) {
            throw new SQLException("Explain request does not contain an EXPLAIN statement");
        }
    }
}
