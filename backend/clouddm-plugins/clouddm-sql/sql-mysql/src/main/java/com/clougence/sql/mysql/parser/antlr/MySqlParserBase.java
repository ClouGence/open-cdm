package com.clougence.sql.mysql.parser.antlr;

import java.math.BigInteger;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;

import com.clougence.sql.mysql.parser.MySqlParserConfig;
import com.clougence.sql.mysql.parser.MySqlParserConfig.Feature;
import com.clougence.sql.mysql.parser.MySqlVersion;

public abstract class MySqlParserBase extends Parser {

    private static final Set<String> AGGREGATE_FUNCTIONS      = Set
        .of("AVG", "BIT_AND", "BIT_OR", "BIT_XOR", "COUNT", "GROUP_CONCAT", "MAX", "MIN", "STD", "STDDEV", "STDDEV_POP", "STDDEV_SAMP", "SUM", "VAR_POP", "VAR_SAMP", "VARIANCE");

    private static final Set<String> WINDOW_FUNCTIONS         = Set
        .of("CUME_DIST", "DENSE_RANK", "FIRST_VALUE", "LAG", "LAST_VALUE", "LEAD", "NTH_VALUE", "NTILE", "PERCENT_RANK", "RANK", "ROW_NUMBER");

    private static final Set<String> SPECIAL_SYNTAX_FUNCTIONS = Set
        .of("ADDDATE", "ASCII", "CHARSET", "COALESCE", "COLLATION", "CONTAINS", "DATABASE", "DATE", "DATE_ADD", "DATE_SUB", "DAY", "FORMAT", "GEOMETRYCOLLECTION", "GET_FORMAT", "HOUR", "IF", "INSERT", "INTERVAL", "LEFT", "LINESTRING", "MICROSECOND", "MINUTE", "MOD", "MONTH", "MULTILINESTRING", "MID", "MULTIPOINT", "MULTIPOLYGON", "POINT", "POLYGON", "POSITION", "QUARTER", "REPEAT", "REPLACE", "REVERSE", "RIGHT", "ROW_COUNT", "SECOND", "SUBDATE", "SUBSTR", "SUBSTRING", "TIME", "TIMESTAMP", "TIMESTAMPADD", "TIMESTAMPDIFF", "TRIM", "TRUNCATE", "USER", "WEEK", "WEIGHT_STRING", "YEAR");

    private static final Set<String> TIMESTAMP_INTERVAL_UNITS = Set
        .of("DAY", "HOUR", "MICROSECOND", "MINUTE", "MONTH", "QUARTER", "SECOND", "WEEK", "YEAR", "SQL_TSI_DAY", "SQL_TSI_HOUR", "SQL_TSI_MINUTE", "SQL_TSI_MONTH", "SQL_TSI_QUARTER", "SQL_TSI_SECOND", "SQL_TSI_WEEK", "SQL_TSI_YEAR");

    /** All scalar function names accepted after the lexer refactor (functions previously tokenized are now plain IDs). */
    private static final Set<String> SCALAR_FUNCTION_NAMES = Set
        .of(
            "ABS", "ACOS", "ADDDATE", "ADDTIME", "AES_DECRYPT", "AES_ENCRYPT", "AREA", "ASBINARY", "ASCII", "ASIN", "ASTEXT", "ASWKB",
            "ASWKT", "ASYMMETRIC_DECRYPT", "ASYMMETRIC_DERIVE", "ASYMMETRIC_ENCRYPT", "ASYMMETRIC_SIGN", "ASYMMETRIC_VERIFY", "ATAN", "ATAN2", "BENCHMARK", "BIN", "BIT_COUNT", "BIT_LENGTH",
            "BUFFER", "CEIL", "CEILING", "CENTROID", "CHARACTER_LENGTH", "CHARSET", "CHAR_LENGTH", "COALESCE", "COERCIBILITY", "COLLATION", "COMPRESS", "CONCAT",
            "CONCAT_WS", "CONNECTION_ID", "CONTAINS", "CONV", "CONVERT_TZ", "COS", "COT", "COUNT", "CRC32", "CREATE_ASYMMETRIC_PRIV_KEY", "CREATE_ASYMMETRIC_PUB_KEY", "CREATE_DH_PARAMETERS",
            "CREATE_DIGEST", "CROSSES", "DATABASE", "DATE", "DATEDIFF", "DATE_FORMAT", "DAY", "DAYNAME", "DAYOFMONTH", "DAYOFWEEK", "DAYOFYEAR", "DECODE",
            "DEGREES", "DES_DECRYPT", "DES_ENCRYPT", "DIMENSION", "DISJOINT", "ELT", "ENCODE", "ENCRYPT", "ENDPOINT", "ENVELOPE", "EQUALS", "EXP",
            "EXPORT_SET", "EXTERIORRING", "EXTRACTVALUE", "FIELD", "FIND_IN_SET", "FLOOR", "FORMAT", "FOUND_ROWS", "FROM_BASE64", "FROM_DAYS", "FROM_UNIXTIME", "GEOMCOLLECTION",
            "GEOMCOLLFROMTEXT", "GEOMCOLLFROMWKB", "GEOMETRYCOLLECTION", "GEOMETRYCOLLECTIONFROMTEXT", "GEOMETRYCOLLECTIONFROMWKB", "GEOMETRYFROMTEXT", "GEOMETRYFROMWKB", "GEOMETRYN", "GEOMETRYTYPE", "GEOMFROMTEXT", "GEOMFROMWKB", "GET_FORMAT",
            "GET_LOCK", "GLENGTH", "GREATEST", "GROUPING", "GTID_SUBSET", "GTID_SUBTRACT", "HEX", "HOUR", "IF", "IFNULL", "INET6_ATON", "INET6_NTOA",
            "INET_ATON", "INET_NTOA", "INSTR", "INTERIORRINGN", "INTERSECTS", "INVISIBLE", "ISCLOSED", "ISEMPTY", "ISNULL", "ISSIMPLE", "IS_FREE_LOCK", "IS_IPV4",
            "IS_IPV4_COMPAT", "IS_IPV4_MAPPED", "IS_IPV6", "IS_USED_LOCK", "JSON_ARRAY", "JSON_ARRAY_APPEND", "JSON_ARRAY_INSERT", "JSON_CONTAINS", "JSON_CONTAINS_PATH", "JSON_DEPTH", "JSON_EXTRACT", "JSON_INSERT",
            "JSON_KEYS", "JSON_LENGTH", "JSON_MERGE", "JSON_MERGE_PATCH", "JSON_MERGE_PRESERVE", "JSON_OBJECT", "JSON_OVERLAPS", "JSON_PRETTY", "JSON_QUOTE", "JSON_REMOVE", "JSON_REPLACE", "JSON_SCHEMA_VALID",
            "JSON_SCHEMA_VALIDATION_REPORT", "JSON_SEARCH", "JSON_SET", "JSON_STORAGE_FREE", "JSON_STORAGE_SIZE", "JSON_TYPE", "JSON_UNQUOTE", "JSON_VALID", "JSON_VALUE", "LASTVAL", "LAST_INSERT_ID", "LCASE",
            "LEAST", "LENGTH", "LINEFROMTEXT", "LINEFROMWKB", "LINESTRING", "LINESTRINGFROMTEXT", "LINESTRINGFROMWKB", "LOAD_FILE", "LOCATE", "LOG", "LOG10", "LOG2",
            "LOWER", "LPAD", "LTRIM", "MAKEDATE", "MAKETIME", "MAKE_SET", "MASTER_POS_WAIT", "MBRCONTAINS", "MBRDISJOINT", "MBREQUAL", "MBRINTERSECTS", "MBROVERLAPS",
            "MBRTOUCHES", "MBRWITHIN", "MD5", "MICROSECOND", "MID", "MINUTE", "MLINEFROMTEXT", "MLINEFROMWKB", "MOD", "MONTH", "MONTHNAME", "MPOINTFROMTEXT",
            "MPOINTFROMWKB", "MPOLYFROMTEXT", "MPOLYFROMWKB", "MULTILINESTRING", "MULTILINESTRINGFROMTEXT", "MULTILINESTRINGFROMWKB", "MULTIPOINT", "MULTIPOINTFROMTEXT", "MULTIPOINTFROMWKB", "MULTIPOLYGON", "MULTIPOLYGONFROMTEXT", "MULTIPOLYGONFROMWKB",
            "NAME_CONST", "NEXTVAL", "NULLIF", "NUMGEOMETRIES", "NUMINTERIORRINGS", "NUMPOINTS", "OCT", "OCTET_LENGTH", "ORD", "OVERLAPS", "PERIOD_ADD", "PERIOD_DIFF",
            "POINT", "POINTFROMTEXT", "POINTFROMWKB", "POINTN", "POLYFROMTEXT", "POLYFROMWKB", "POLYGON", "POLYGONFROMTEXT", "POLYGONFROMWKB", "POSITION", "POW", "POWER",
            "QUARTER", "QUOTE", "RADIANS", "RAND", "RANDOM", "RANDOM_BYTES", "RELEASE_LOCK", "REPEAT", "REPLACE", "REVERSE", "ROUND", "ROW_COUNT",
            "RPAD", "RTRIM", "SECOND", "SEC_TO_TIME", "SESSION_USER", "SESSION_VARIABLES_ADMIN", "SETVAL", "SHA", "SHA1", "SHA2", "SIGN", "SIN",
            "SLEEP", "SOUNDEX", "SQL_THREAD_WAIT_AFTER_GTIDS", "SQRT", "STARTPOINT", "STRCMP", "STR_TO_DATE", "ST_AREA", "ST_ASBINARY", "ST_ASTEXT", "ST_ASWKB", "ST_ASWKT",
            "ST_BUFFER", "ST_CENTROID", "ST_COLLECT", "ST_CONTAINS", "ST_CROSSES", "ST_DIFFERENCE", "ST_DIMENSION", "ST_DISJOINT", "ST_DISTANCE", "ST_ENDPOINT", "ST_ENVELOPE", "ST_EQUALS",
            "ST_EXTERIORRING", "ST_GEOMCOLLFROMTEXT", "ST_GEOMCOLLFROMTXT", "ST_GEOMCOLLFROMWKB", "ST_GEOMETRYCOLLECTIONFROMTEXT", "ST_GEOMETRYCOLLECTIONFROMWKB", "ST_GEOMETRYFROMTEXT", "ST_GEOMETRYFROMWKB", "ST_GEOMETRYN", "ST_GEOMETRYTYPE", "ST_GEOMFROMTEXT", "ST_GEOMFROMWKB",
            "ST_INTERIORRINGN", "ST_INTERSECTION", "ST_INTERSECTS", "ST_ISCLOSED", "ST_ISEMPTY", "ST_ISSIMPLE", "ST_LINEFROMTEXT", "ST_LINEFROMWKB", "ST_LINESTRINGFROMTEXT", "ST_LINESTRINGFROMWKB", "ST_NUMGEOMETRIES", "ST_NUMINTERIORRING",
            "ST_NUMINTERIORRINGS", "ST_NUMPOINTS", "ST_OVERLAPS", "ST_POINTFROMTEXT", "ST_POINTFROMWKB", "ST_POINTN", "ST_POLYFROMTEXT", "ST_POLYFROMWKB", "ST_POLYGONFROMTEXT", "ST_POLYGONFROMWKB", "ST_SRID", "ST_STARTPOINT",
            "ST_SYMDIFFERENCE", "ST_TOUCHES", "ST_UNION", "ST_WITHIN", "ST_X", "ST_Y", "SUBDATE", "SUBSTR", "SUBSTRING", "SUBSTRING_INDEX", "SUBTIME", "SYSTEM_USER",
            "TAN", "TIME", "TIMEDIFF", "TIMESTAMP", "TIMESTAMPADD", "TIMESTAMPDIFF", "TIME_FORMAT", "TIME_TO_SEC", "TOUCHES", "TO_BASE64", "TO_DAYS", "TO_SECONDS",
            "TRIM", "TRUNCATE", "UCASE", "UNCOMPRESS", "UNCOMPRESSED_LENGTH", "UNHEX", "UNIX_TIMESTAMP", "UPDATEXML", "UPPER", "UUID", "UUID_SHORT", "VALIDATE_PASSWORD_STRENGTH",
            "VERSION", "VISIBLE", "WAIT_UNTIL_SQL_THREAD_AFTER_GTIDS", "WEEK", "WEEKDAY", "WEEKOFYEAR", "WEIGHT_STRING", "WITHIN", "X_FUNCTION", "YEAR", "YEARWEEK", "Y_FUNCTION"
        );
    private static final Set<String> DISALLOWED_LABELS_56     = Set
        .of("BACKUP", "CLOSE", "FORMAT", "HOST", "OPEN", "OPTIONS", "OWNER", "PARSER", "PORT", "REMOVE", "RESTORE", "SECURITY", "SERVER", "SOCKET", "SONAME", "UPGRADE", "WRAPPER");

    private static final Set<String> DISALLOWED_LABELS_57     = Set
        .of("ACCOUNT", "ALWAYS", "BACKUP", "CLOSE", "FORMAT", "GROUP_REPLICATION", "HOST", "OPEN", "OPTIONS", "OWNER", "PARSER", "PARSE_GCOL_EXPR", "PORT", "REMOVE", "RESTORE", "SECURITY", "SERVER", "SOCKET", "SONAME", "UPGRADE", "WRAPPER");

    private static final Set<String> DYNAMIC_PRIVILEGES_80    = Set
        .of("APPLICATION_PASSWORD_ADMIN", "AUDIT_ABORT_EXEMPT", "AUDIT_ADMIN", "AUTHENTICATION_POLICY_ADMIN", "BACKUP_ADMIN", "BINLOG_ADMIN", "BINLOG_ENCRYPTION_ADMIN", "CLONE_ADMIN", "CONNECTION_ADMIN", "ENCRYPTION_KEY_ADMIN", "FIREWALL_ADMIN", "FIREWALL_EXEMPT", "FIREWALL_USER", "FLUSH_OPTIMIZER_COSTS", "FLUSH_STATUS", "FLUSH_TABLES", "FLUSH_USER_RESOURCES", "GROUP_REPLICATION_ADMIN", "GROUP_REPLICATION_STREAM", "INNODB_REDO_LOG_ARCHIVE", "INNODB_REDO_LOG_ENABLE", "MASKING_DICTIONARIES_ADMIN", "NDB_STORED_USER", "PASSWORDLESS_USER_ADMIN", "PERSIST_RO_VARIABLES_ADMIN", "REPLICATION_APPLIER", "REPLICATION_SLAVE_ADMIN", "RESOURCE_GROUP_ADMIN", "RESOURCE_GROUP_USER", "ROLE_ADMIN", "SENSITIVE_VARIABLES_OBSERVER", "SERVICE_CONNECTION_ADMIN", "SESSION_VARIABLES_ADMIN", "SET_USER_ID", "SHOW_ROUTINE", "SKIP_QUERY_REWRITE", "SYSTEM_USER", "SYSTEM_VARIABLES_ADMIN", "TABLE_ENCRYPTION_ADMIN", "TELEMETRY_LOG_ADMIN", "TP_CONNECTION_ADMIN", "VERSION_TOKEN_ADMIN", "XA_RECOVER_ADMIN");

    private static final Set<String> DYNAMIC_PRIVILEGES_84    = Set
        .of("ALLOW_NONEXISTENT_DEFINER", "APPLICATION_PASSWORD_ADMIN", "AUDIT_ABORT_EXEMPT", "AUDIT_ADMIN", "AUTHENTICATION_POLICY_ADMIN", "BACKUP_ADMIN", "BINLOG_ADMIN", "BINLOG_ENCRYPTION_ADMIN", "CLONE_ADMIN", "CONNECTION_ADMIN", "ENCRYPTION_KEY_ADMIN", "FIREWALL_ADMIN", "FIREWALL_EXEMPT", "FIREWALL_USER", "FLUSH_OPTIMIZER_COSTS", "FLUSH_PRIVILEGES", "FLUSH_STATUS", "FLUSH_TABLES", "FLUSH_USER_RESOURCES", "GROUP_REPLICATION_ADMIN", "GROUP_REPLICATION_STREAM", "INNODB_REDO_LOG_ARCHIVE", "INNODB_REDO_LOG_ENABLE", "MASKING_DICTIONARIES_ADMIN", "NDB_STORED_USER", "OPTIMIZE_LOCAL_TABLE", "PASSWORDLESS_USER_ADMIN", "PERSIST_RO_VARIABLES_ADMIN", "REPLICATION_APPLIER", "REPLICATION_SLAVE_ADMIN", "RESOURCE_GROUP_ADMIN", "RESOURCE_GROUP_USER", "ROLE_ADMIN", "SENSITIVE_VARIABLES_OBSERVER", "SERVICE_CONNECTION_ADMIN", "SESSION_VARIABLES_ADMIN", "SET_ANY_DEFINER", "SHOW_ROUTINE", "SKIP_QUERY_REWRITE", "SYSTEM_USER", "SYSTEM_VARIABLES_ADMIN", "TABLE_ENCRYPTION_ADMIN", "TELEMETRY_LOG_ADMIN", "TP_CONNECTION_ADMIN", "TRANSACTION_GTID_TAG", "VERSION_TOKEN_ADMIN", "XA_RECOVER_ADMIN");

    private MySqlParserConfig        config                   = MySqlParserConfig.unknownSqlMode(null);

    protected MySqlParserBase(TokenStream input){
        super(input);
    }

    public final void setVersion(MySqlVersion version) {
        MySqlVersion resolved = version == null ? MySqlVersion.LATEST : version;
        setVersion(resolved, resolved.exactVersion());
    }

    public final void setVersion(MySqlVersion version, int exactVersion) {
        MySqlVersion resolved = version == null ? MySqlVersion.LATEST : version;
        int major = exactVersion / 10000;
        int minor = exactVersion / 100 % 100;
        int release = exactVersion % 100;
        String versionText = major > 0 ? major + "." + minor + "." + release : resolved.name();
        this.config = MySqlParserConfig.unknownSqlMode(versionText);
    }

    public final void setConfig(MySqlParserConfig config) { this.config = config; }

    public final MySqlParserConfig config() {
        return config;
    }

    protected final int exactVersion() {
        return config.exactVersion();
    }

    protected final boolean atLeastExact(int minimum) {
        return exactVersion() >= minimum;
    }

    protected final boolean isSqlModeKnown() { return config.isSqlModeKnown(); }

    protected final boolean isSetVariableAssignmentAllowed(MySqlParser.VariableClauseContext variable) {
        if (MySqlVersion.le(config.grammarVersion(), MySqlVersion.MYSQL_5_7)) {
            return true;
        }
        String text = variable.getText().toUpperCase(Locale.ROOT);
        return !Set.of("GLOBAL", "LOCAL", "PERSIST", "PERSIST_ONLY", "SESSION").contains(text);
    }

    protected final boolean isTruthPredicateAllowed(ParserRuleContext suffix) {
        ParserRuleContext comparison = suffix.getParent() instanceof ParserRuleContext ? (ParserRuleContext) suffix.getParent() : null;
        if (comparison == null || comparison.children == null) {
            return true;
        }
        return comparison.children.stream().noneMatch(child -> child != suffix && child instanceof MySqlParser.TruthPredicateContext);
    }

    protected final boolean isPipesAsConcat() { return config.isEnabled(Feature.PIPES_AS_CONCAT); }

    protected final boolean isHighNotPrecedence() { return config.isEnabled(Feature.HIGH_NOT_PRECEDENCE); }

    protected final boolean isSimpleIdentifierAllowed() {
        if (!isSqlModeKnown()) {
            return true;
        }
        Token previous = getInputStream().LT(-1);
        if (previous != null && previous.getType() == MySqlParser.DOT) {
            return true;
        }
        if (getInputStream().LT(1).getType() == MySqlParser.ID) {
            return true;
        }
        String text = getInputStream().LT(1).getText();
        return text == null || !switch (text.toUpperCase(Locale.ROOT)) {
            case "ADDDATE", "BIT_AND", "BIT_OR", "BIT_XOR", "CAST", "COUNT", "CURDATE", "CURTIME", "DATE_ADD", "DATE_SUB", "EXTRACT", "GROUP_CONCAT", "JSON_ARRAYAGG",
                    "JSON_DUALITY_OBJECT", "JSON_OBJECTAGG", "MAX", "MID", "MIN", "NOW", "POSITION", "SESSION_USER", "STD", "STDDEV", "STDDEV_POP", "STDDEV_SAMP", "ST_COLLECT",
                    "SUBDATE", "SUBSTR", "SUBSTRING", "SUM", "SYSDATE", "SYSTEM_USER", "TRIM", "VARIANCE", "VAR_POP", "VAR_SAMP" ->
                true;
            default -> false;
        };
    }

    /**
     * MySQL 8.0.17 briefly assigned OR precedence to the concat token. The
     * observable parser regression was limited to LIKE/ESCAPE conflict paths.
     */
    protected final boolean isPipesConcatLikeOperandAllowed() { return !(exactVersion() == 80017 && isPipesAsConcat() && pipesConcatAheadBeforePredicateBoundary()); }

    private boolean pipesConcatAheadBeforePredicateBoundary() {
        int depth = 0;
        for (int i = 1; !isStatementEnd(i); i++) {
            String text = getInputStream().LT(i).getText();
            if ("(".equals(text)) {
                depth++;
            } else if (")".equals(text)) {
                if (depth == 0) {
                    return false;
                }
                depth--;
            } else if (depth == 0 && ("||".equals(text))) {
                return true;
            } else if (depth == 0 && (",".equals(text) || "ESCAPE".equalsIgnoreCase(text) || "AND".equalsIgnoreCase(text) || "OR".equalsIgnoreCase(text))) {
                return false;
            }
        }
        return false;
    }

    protected final boolean isCreateViewIfNotExistsAllowed() {
        if (!atLeastExact(90700)) {
            return false;
        }
        int start = getContext().getStart().getTokenIndex();
        int current = getCurrentToken().getTokenIndex();
        for (int i = start; i + 1 < current; i++) {
            String token = getInputStream().get(i).getText();
            String nextToken = getInputStream().get(i + 1).getText();
            if ("OR".equalsIgnoreCase(token) && "REPLACE".equalsIgnoreCase(nextToken)) {
                return false;
            }
        }
        return true;
    }

    protected final boolean atLeast(MySqlVersion minimum) {
        return MySqlVersion.ge(config.grammarVersion(), minimum);
    }

    protected final boolean atMost(MySqlVersion maximum) {
        return MySqlVersion.le(config.grammarVersion(), maximum);
    }

    protected final boolean between(MySqlVersion minimum, MySqlVersion maximum) {
        return MySqlVersion.ge(config.grammarVersion(), minimum) && MySqlVersion.le(config.grammarVersion(), maximum);
    }

    protected final boolean atLeast(int major, int minor) {
        return MySqlVersion.ge(config.grammarVersion(), major * 100 + minor);
    }

    protected final boolean atMost(int major, int minor) {
        return MySqlVersion.le(config.grammarVersion(), major * 100 + minor);
    }

    protected final boolean isPositiveIntegerAhead() {
        String text = getInputStream().LT(1).getText();
        try {
            return text != null && new BigInteger(text).signum() > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    protected final boolean isLegacySetRoleAssignment() {
        return atMost(5, 6) && "SET".equalsIgnoreCase(getInputStream().LT(1).getText()) && "ROLE".equalsIgnoreCase(getInputStream().LT(2).getText())
               && "PUBLIC".equalsIgnoreCase(getInputStream().LT(3).getText()) && isStatementEnd(4);
    }

    protected final boolean isPrivilegeClauseAllowed() {
        if (!atLeast(8, 0)) {
            return true;
        }

        boolean columnListSeen = false;
        for (int i = 1; !isStatementEnd(i); i++) {
            String token = getInputStream().LT(i).getText();
            if ("ON".equalsIgnoreCase(token)) {
                return !"FUNCTION".equalsIgnoreCase(getInputStream().LT(i + 1).getText()) || !columnListSeen;
            }
            if ("(".equals(token)) {
                columnListSeen = true;
            }
        }
        return true;
    }

    protected final boolean isColumnConstraintSequenceAllowed(List<MySqlParser.ColumnConstraintContext> constraints) {
        if (atLeast(8, 0) || constraints == null || constraints.isEmpty()) {
            return true;
        }

        boolean checkSeen = false;
        for (MySqlParser.ColumnConstraintContext constraint : constraints) {
            if (constraint instanceof MySqlParser.CheckColumnConstraintContext) {
                if (checkSeen) {
                    return false;
                }
                checkSeen = true;
            } else if (checkSeen) {
                return false;
            }
        }
        return true;
    }

    protected final boolean between(int minMajor, int minMinor, int maxMajor, int maxMinor) {
        return MySqlVersion.ge(config.grammarVersion(), minMajor * 100 + minMinor) && MySqlVersion.le(config.grammarVersion(), maxMajor * 100 + maxMinor);
    }

    protected final boolean isQueryWhereAllowed(ParserRuleContext fromClause) {
        return atLeast(8, 0) || fromClause != null || (atLeast(5, 7) && isQueryTableExpressionContext());
    }

    protected final boolean isQueryGroupOrHavingAllowed(ParserRuleContext fromClause) {
        return atLeast(8, 0) || (fromClause != null && (atLeast(5, 7) || !isBareDualFromClause(fromClause))) || (atLeast(5, 7) && isQueryTableExpressionContext());
    }

    protected final boolean isQueryOrderByAllowed(ParserRuleContext fromClause) {
        if (atLeast(5, 7)) {
            return true;
        }
        if (isDirectCreateTableQueryContext()) {
            return fromClause != null && !isBareDualFromClause(fromClause);
        }
        return fromClause == null || !isBareDualFromClause(fromClause);
    }

    protected final boolean isDerivedColumnAliasListAllowed() {
        if (atLeast(8, 0)) {
            return true;
        }
        if (!between(5, 6, 5, 6)) {
            return false;
        }
        for (ParserRuleContext context = getContext(); context != null; context = context.getParent()) {
            int ruleIndex = context.getRuleIndex();
            if (ruleIndex >= 0 && ruleIndex < getRuleNames().length && "createView".equals(getRuleNames()[ruleIndex])) {
                return true;
            }
        }
        return false;
    }

    protected final boolean isQueryProcedureAllowed(ParserRuleContext fromClause) {
        return fromClause != null && (atLeast(5, 7) || !isBareDualFromClause(fromClause));
    }

    protected final boolean isTrailingSelectIntoAllowed() {
        if (atLeast(8, 0)) {
            return true;
        }
        for (ParserRuleContext context = getContext(); context != null; context = context.getParent()) {
            int ruleIndex = context.getRuleIndex();
            if (ruleIndex < 0 || ruleIndex >= getRuleNames().length) {
                continue;
            }
            String ruleName = getRuleNames()[ruleIndex];
            if ("subqueryStatement".equals(ruleName) || "tableSourceItem".equals(ruleName)) {
                return false;
            }
        }
        return true;
    }

    protected final boolean isDefaultColumnConstraintAllowed() { return !between(5, 7, 5, 7) || !hasColumnDefinitionToken("AS"); }

    protected final boolean isGeneratedColumnConstraintAllowed() { return atLeast(5, 7) && (!between(5, 7, 5, 7) || !hasColumnDefinitionToken("DEFAULT")); }

    private boolean hasColumnDefinitionToken(String expected) {
        ParserRuleContext columnDefinition = null;
        for (ParserRuleContext context = getContext(); context != null; context = context.getParent()) {
            int ruleIndex = context.getRuleIndex();
            if (ruleIndex >= 0 && ruleIndex < getRuleNames().length && "columnDefinition".equals(getRuleNames()[ruleIndex])) {
                columnDefinition = context;
                break;
            }
        }
        if (columnDefinition == null) {
            return false;
        }
        int start = columnDefinition.getStart().getTokenIndex();
        int current = getCurrentToken().getTokenIndex();
        for (int i = start; i < current; i++) {
            if (expected.equalsIgnoreCase(getInputStream().get(i).getText())) {
                return true;
            }
        }
        return false;
    }

    private boolean isBareDualFromClause(ParserRuleContext fromClause) {
        return "FROMDUAL".equalsIgnoreCase(contextText(fromClause));
    }

    private boolean isQueryTableExpressionContext() {
        for (ParserRuleContext context = getContext(); context != null; context = context.getParent()) {
            int ruleIndex = context.getRuleIndex();
            if (ruleIndex < 0 || ruleIndex >= getRuleNames().length) {
                continue;
            }
            switch (getRuleNames()[ruleIndex]) {
                case "subqueryStatement":
                case "tableSourceItem":
                case "insertQueryStatement":
                case "createTable":
                    return true;
                default:
                    break;
            }
        }
        return false;
    }

    private boolean isDirectCreateTableQueryContext() {
        for (ParserRuleContext context = getContext(); context != null; context = context.getParent()) {
            int ruleIndex = context.getRuleIndex();
            if (ruleIndex < 0 || ruleIndex >= getRuleNames().length) {
                continue;
            }
            switch (getRuleNames()[ruleIndex]) {
                case "createTable":
                    return true;
                case "subqueryStatement":
                case "tableSourceItem":
                case "withSelectExpr":
                    return false;
                default:
                    break;
            }
        }
        return false;
    }

    protected final boolean isIdentifierAfterDot() { return isIdentifierToken(1); }

    protected final boolean isIdentifierAfterDotAhead() { return isIdentifierToken(2); }

    protected final boolean isIdentifierBeforeDot() { return isIdentifierToken(1) && ".".equals(getInputStream().LT(2).getText()); }

    protected final boolean isBarePersistScopeAllowed() { return atLeast(8, 0); }

    protected final boolean isPersistOnlyToken() { return "PERSIST_ONLY".equalsIgnoreCase(getInputStream().LT(1).getText()); }

    protected final boolean isXidToken() { return "XID".equalsIgnoreCase(getInputStream().LT(1).getText()); }

    protected final boolean isStCollectToken() { return "ST_COLLECT".equalsIgnoreCase(getInputStream().LT(1).getText()); }

    protected final boolean isStartTransactionModeListAllowed() {
        boolean readOnly = false;
        boolean readWrite = false;
        for (int i = 1;; i++) {
            Token lookahead = getInputStream().LT(i);
            String token = lookahead == null ? null : lookahead.getText();
            if (lookahead == null || lookahead.getType() == Token.EOF || ";".equals(token)) {
                break;
            }
            if ("READ".equalsIgnoreCase(token)) {
                String mode = getInputStream().LT(i + 1).getText();
                readOnly |= "ONLY".equalsIgnoreCase(mode);
                readWrite |= "WRITE".equalsIgnoreCase(mode);
            }
        }
        return !(readOnly && readWrite);
    }

    protected final boolean hasJoinConditionAhead() {
        for (int i = 1; !isStatementEnd(i); i++) {
            String token = getInputStream().LT(i).getText();
            if ("ON".equalsIgnoreCase(token) || "USING".equalsIgnoreCase(token)) {
                return true;
            }
        }
        return false;
    }

    protected final boolean isCompletionTypeAllowed() {
        boolean chain = false;
        boolean release = false;
        for (int i = 1; !isStatementEnd(i); i++) {
            String token = getInputStream().LT(i).getText();
            String previous = i == 1 ? null : getInputStream().LT(i - 1).getText();
            chain |= "CHAIN".equalsIgnoreCase(token) && !"NO".equalsIgnoreCase(previous);
            release |= "RELEASE".equalsIgnoreCase(token) && !"NO".equalsIgnoreCase(previous);
        }
        return !(chain && release);
    }

    protected final boolean isSetTransactionOptionListAllowed() {
        int accessModes = 0;
        int isolationLevels = 0;
        for (int i = 1; !isStatementEnd(i); i++) {
            String token = getInputStream().LT(i).getText();
            if ("READ".equalsIgnoreCase(token)) {
                String next = getInputStream().LT(i + 1).getText();
                if ("ONLY".equalsIgnoreCase(next) || "WRITE".equalsIgnoreCase(next)) {
                    accessModes++;
                }
            } else if ("ISOLATION".equalsIgnoreCase(token) && "LEVEL".equalsIgnoreCase(getInputStream().LT(i + 1).getText())) {
                isolationLevels++;
            }
        }
        return accessModes <= 1 && isolationLevels <= 1;
    }

    protected final boolean isXaRecoverClauseAllowed() {
        if (isStatementEnd(1)) {
            return true;
        }
        return atLeast(5, 7) && "CONVERT".equalsIgnoreCase(getInputStream().LT(1).getText()) && "XID".equalsIgnoreCase(getInputStream().LT(2).getText()) && isStatementEnd(3);
    }

    private boolean isStatementEnd(int lookahead) {
        Token token = getInputStream().LT(lookahead);
        return token == null || token.getType() == Token.EOF || ";".equals(token.getText());
    }

    protected final boolean isLabelAllowed() {
        String text = getInputStream().LT(1).getText().toUpperCase(Locale.ROOT);
        if (atMost(5, 6)) {
            return !DISALLOWED_LABELS_56.contains(text);
        }
        if (between(5, 7, 5, 7)) {
            return !DISALLOWED_LABELS_57.contains(text);
        }
        return true;
    }

    private boolean isIdentifierToken(int lookahead) {
        String text = getInputStream().LT(lookahead).getText();
        if (text == null || text.isEmpty()) {
            return false;
        }
        boolean hasNonDigit = false;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (!(ch == '$' || ch == '_' || Character.isLetterOrDigit(ch) || ch >= 0x80)) {
                return false;
            }
            hasNonDigit |= !Character.isDigit(ch);
        }
        return hasNonDigit;
    }

    protected final boolean isServerNum() {
        String text = getInputStream().LT(1).getText();
        if (text == null || text.isEmpty()) {
            return false;
        }
        int firstNonZero = 0;
        while (firstNonZero < text.length() && text.charAt(firstNonZero) == '0') {
            firstNonZero++;
        }
        String normalized = firstNonZero == text.length() ? "0" : text.substring(firstNonZero);
        return normalized.length() < 10 || (normalized.length() == 10 && normalized.compareTo("2147483647") <= 0);
    }

    protected final boolean isCustomFunctionCallAllowed() {
        String functionName = getInputStream().LT(1).getText();
        String nextToken = getInputStream().LT(2).getText();
        if (functionName == null || ".".equals(nextToken)) {
            return true;
        }
        String normalized = functionName.toUpperCase(Locale.ROOT);
        if ("ST_COLLECT".equals(normalized) && !atLeastExact(80024) && isStCollectSpecialSyntaxAhead()) {
            return false;
        }
        if ("PASSWORD".equals(normalized)) {
            return false;
        }
        if ("OLD_PASSWORD".equals(normalized)) {
            return atLeast(5, 7);
        }
        if ("JSON_ARRAYAGG".equals(normalized) || "JSON_OBJECTAGG".equals(normalized)) {
            return atMost(5, 6);
        }
        if ("GROUPING".equals(normalized)) {
            return atMost(5, 7);
        }
        if ("JSON_VALUE".equals(normalized)) {
            return !atLeastExact(80021);
        }
        if (AGGREGATE_FUNCTIONS.contains(normalized)) {
            return false;
        }
        if (WINDOW_FUNCTIONS.contains(normalized)) {
            return atMost(5, 7);
        }
        if (SPECIAL_SYNTAX_FUNCTIONS.contains(normalized)) {
            return false;
        }
        if (atMost(5, 7)) {
            return true;
        }
        return !"CONTAINS".equals(normalized) && !"SRID".equals(normalized);
    }

    protected final boolean isFunctionSyntaxAllowedAhead() {
        String functionName = getInputStream().LT(1).getText();
        if (functionName == null || !"(".equals(getInputStream().LT(2).getText())) {
            return true;
        }
        String normalized = functionName.toUpperCase(Locale.ROOT);
        if ("SRID".equals(normalized) && atLeast(8, 0)) {
            return false;
        }
        int argumentCount = functionArgumentCountAhead();
        if ("GROUPING".equals(normalized) && atLeast(8, 0)) {
            return argumentCount >= 1;
        }
        if ("LOG".equals(normalized) && atLeast(8, 4)) {
            return argumentCount == 1 || argumentCount == 2;
        }
        return true;
    }

    protected final boolean isTypedTemporalLiteralAhead() {
        int tokenType = getInputStream().LA(1);
        return tokenType == MySqlParser.DATE || tokenType == MySqlParser.TIME || tokenType == MySqlParser.TIMESTAMP;
    }

    protected final boolean isBareCharsetIntroducerIdentifier() {
        int nextTokenType = getInputStream().LA(2);
        return nextTokenType != MySqlParser.STRING_LITERAL && nextTokenType != MySqlParser.DOUBLE_QUOTE_STRING_LITERAL && nextTokenType != MySqlParser.DOUBLE_QUOTE_AMBIGUOUS;
    }

    private int functionArgumentCountAhead() {
        int depth = 0;
        int commas = 0;
        boolean hasArgument = false;
        for (int i = 2; !isStatementEnd(i); i++) {
            String token = getInputStream().LT(i).getText();
            if ("(".equals(token)) {
                hasArgument |= depth >= 1;
                depth++;
            } else if (")".equals(token)) {
                if (--depth == 0) {
                    return hasArgument ? commas + 1 : 0;
                }
            } else if (depth == 1) {
                if (",".equals(token)) {
                    commas++;
                } else {
                    hasArgument = true;
                }
            }
        }
        return -1;
    }

    protected final boolean isGenericFunctionCallAllowed(String functionName) {
        if (functionName == null) {
            return false;
        }
        if (functionName.indexOf('.') >= 0) {
            return true;
        }
        String normalized = functionName.toUpperCase(Locale.ROOT);
        if ("PASSWORD".equals(normalized)) {
            return false;
        }
        if ("OLD_PASSWORD".equals(normalized)) {
            return atLeast(5, 7);
        }
        if ("JSON_ARRAYAGG".equals(normalized) || "JSON_OBJECTAGG".equals(normalized)) {
            return atMost(5, 6);
        }
        if ("JSON_VALUE".equals(normalized)) {
            return !atLeastExact(80021);
        }
        if (AGGREGATE_FUNCTIONS.contains(normalized)) {
            return false;
        }
        if (WINDOW_FUNCTIONS.contains(normalized)) {
            return atMost(5, 7);
        }
        if (SPECIAL_SYNTAX_FUNCTIONS.contains(normalized)) {
            return false;
        }
        return true;
    }

    protected final boolean isGenericFunctionCallAllowed(ParserRuleContext function) {
        if (function != null && function.getStart().getType() == MySqlParser.ID) {
            return true;
        }
        return function != null && isGenericFunctionCallAllowed(function.getText());
    }

    protected final boolean isGenericFunctionCallAllowed(ParserRuleContext function, ParserRuleContext args) {
        if (function instanceof MySqlParser.ScalarGenericFunctionNameContext) {
            return isScalarFunctionCallAllowed(function.getText(), args) || isGenericFunctionCallAllowed(function.getText());
        }
        if (function != null && function.getText().indexOf('.') >= 0 && args instanceof MySqlParser.FunctionArgsContext) {
            for (MySqlParser.FunctionArgWithAliasContext argument : ((MySqlParser.FunctionArgsContext) args).functionArgWithAlias()) {
                if (argument.functionArgAlias() != null) {
                    return false;
                }
            }
        }
        return true;
    }

    protected final boolean isScalarFunctionNameAhead() {
        if (".".equals(getInputStream().LT(2).getText())) {
            return false;
        }
        Token token = getInputStream().LT(1);
        if (token.getType() == MySqlParser.ID) {
            return SCALAR_FUNCTION_NAMES.contains(token.getText().toUpperCase(Locale.ROOT));
        }
        return getATN().nextTokens(getATN().ruleToStartState[MySqlParser.RULE_scalarFunctionName]).contains(token.getType());
    }

    protected final boolean isGenericFunctionSyntaxAhead() {
        String functionName = getInputStream().LT(1).getText();
        if (functionName == null || ".".equals(getInputStream().LT(2).getText())) {
            return true;
        }
        if (getInputStream().LT(1).getType() == MySqlParser.ID) {
            return true;
        }
        String normalized = functionName.toUpperCase(Locale.ROOT);
        if (AGGREGATE_FUNCTIONS.contains(normalized)) {
            return false;
        }
        if (atLeast(8, 0) && WINDOW_FUNCTIONS.contains(normalized)) {
            return false;
        }
        switch (normalized) {
            case "CURRENT_DATE":
            case "CURRENT_TIME":
            case "CURRENT_TIMESTAMP":
            case "CURRENT_USER":
            case "CURDATE":
            case "CURTIME":
            case "LOCALTIME":
            case "LOCALTIMESTAMP":
            case "NOW":
            case "SCHEMA":
            case "SYSDATE":
            case "UTC_DATE":
            case "UTC_TIME":
            case "UTC_TIMESTAMP":
            case "DATE":
            case "DAY":
            case "HOUR":
            case "INSERT":
            case "INTERVAL":
            case "LEFT":
            case "MINUTE":
            case "MONTH":
            case "RIGHT":
            case "SECOND":
            case "TIME":
            case "TIMESTAMP":
            case "USER":
            case "YEAR":
            case "PASSWORD":
            case "JSON_DUALITY_OBJECT":
                return false;
            default:
                return true;
        }
    }

    protected final boolean isFinalSelectIntoAllowed(ParserRuleContext leadingInto) {
        return leadingInto == null ? isTrailingSelectIntoAllowed() : atLeast(8, 0);
    }

    protected final boolean isUnionOperandTailAllowed(ParserRuleContext leadingInto) {
        return leadingInto == null;
    }

    protected final boolean isUnionAfterQuerySpecificationAllowed(MySqlParser.QuerySpecificationContext query) {
        return atMost(5, 7) || (query.orderByClause() == null && query.limitClause() == null);
    }

    protected final boolean isSelectTailIntoAllowed(MySqlParser.QuerySpecificationContext query) {
        return atLeast(8, 0) && (query == null || query.getRuleContexts(MySqlParser.SelectIntoExpressionContext.class).isEmpty());
    }

    protected final boolean isUnionAfterParenthesizedQueryAllowed(ParserRuleContext parenthesizedQuery) {
        if (atLeast(5, 7) || !"UNION".equalsIgnoreCase(getInputStream().LT(1).getText())) {
            return true;
        }
        if (!(parenthesizedQuery instanceof MySqlParser.ParenthesizedSelectContext parenthesized)) {
            return false;
        }
        MySqlParser.LegacyQueryExpressionContext legacy = parenthesized.legacyQueryExpression();
        while (legacy != null && legacy.legacyQueryExpression() != null) {
            legacy = legacy.legacyQueryExpression();
        }
        if (legacy == null || legacy.querySpecification() == null) {
            return false;
        }
        MySqlParser.QuerySpecificationContext query = legacy.querySpecification();
        boolean hasLocalTail = query.orderByClause() != null || query.limitClause() != null || legacy.lockClauses() != null;
        if (!hasLocalTail) {
            return true;
        }
        int operandLookahead = 2;
        String unionOption = getInputStream().LT(operandLookahead).getText();
        if ("ALL".equalsIgnoreCase(unionOption) || "DISTINCT".equalsIgnoreCase(unionOption)) {
            operandLookahead++;
        }
        return "(".equals(getInputStream().LT(operandLookahead).getText());
    }

    private boolean isStCollectSpecialSyntaxAhead() {
        boolean distinct = "DISTINCT".equalsIgnoreCase(getInputStream().LT(3).getText());
        int depth = 0;
        for (int i = 2; !isStatementEnd(i); i++) {
            String token = getInputStream().LT(i).getText();
            if ("(".equals(token)) {
                depth++;
            } else if (")".equals(token) && --depth == 0) {
                return distinct || "OVER".equalsIgnoreCase(getInputStream().LT(i + 1).getText());
            }
        }
        return distinct;
    }

    protected final boolean isScalarFunctionCallAllowed(String functionName, ParserRuleContext args) {
        if (functionName == null) {
            return true;
        }
        String normalized = functionName.toUpperCase(Locale.ROOT);
        if (AGGREGATE_FUNCTIONS.contains(normalized)) {
            return false;
        }
        if (WINDOW_FUNCTIONS.contains(normalized)) {
            return atMost(5, 7);
        }
        int argCount = argumentCount(args);
        switch (normalized) {
            case "DATE":
            case "DAY":
            case "HOUR":
            case "ASCII":
            case "CHARSET":
            case "COLLATION":
            case "MICROSECOND":
            case "MINUTE":
            case "MONTH":
            case "QUARTER":
            case "REVERSE":
            case "SECOND":
            case "TIME":
            case "YEAR":
                return argCount == 1;
            case "TIMESTAMP":
                return argCount == 1 || argCount == 2;
            case "LEFT":
            case "RIGHT":
            case "MOD":
            case "REPEAT":
            case "TRUNCATE":
                return argCount == 2;
            case "SUBSTR":
            case "SUBSTRING":
            case "MID":
                return argCount == 2 || argCount == 3;
            case "TRIM":
                return argCount == 1;
            case "INSERT":
                return argCount == 4;
            case "INTERVAL":
                return argCount >= 2;
            case "COALESCE":
                return argCount >= 1;
            case "GROUPING":
                return atMost(5, 7) || argCount >= 1;
            case "GEOMETRYCOLLECTION":
                return atLeast(5, 7) || argCount >= 1;
            case "LINESTRING":
            case "MULTILINESTRING":
            case "MULTIPOINT":
            case "MULTIPOLYGON":
            case "POLYGON":
                return argCount >= 1;
            case "POINT":
                return argCount == 2;
            case "CONTAINS":
                return atMost(5, 7) && argCount == 2;
            case "GET_FORMAT":
            case "POSITION":
                return false;
            case "JSON_ARRAYAGG":
            case "JSON_OBJECTAGG":
                return atMost(5, 6);
            case "JSON_VALUE":
                return !atLeastExact(80021);
            case "IF":
            case "REPLACE":
                return argCount == 3;
            case "FORMAT":
                return argCount == 2 || argCount == 3;
            case "LOG":
                return atMost(8, 0) || argCount == 1 || argCount == 2;
            case "WEEK":
                return argCount == 1 || argCount == 2;
            case "WEIGHT_STRING":
                return argCount == 1 || argCount == 4;
            case "DATABASE":
            case "ROW_COUNT":
            case "USER":
                return argCount == 0;
            case "ADDDATE":
            case "SUBDATE":
                return argCount == 2;
            case "DATE_ADD":
            case "DATE_SUB":
                return argCount == 2 && argumentText(args, 1).toUpperCase(Locale.ROOT).startsWith("INTERVAL");
            case "TIMESTAMPADD":
            case "TIMESTAMPDIFF":
                return argCount == 3 && TIMESTAMP_INTERVAL_UNITS.contains(argumentText(args, 0).toUpperCase(Locale.ROOT));
            default:
                return true;
        }
    }

    private int argumentCount(ParserRuleContext args) {
        if (args == null) {
            return 0;
        }

        int count = 1;
        int depth = 0;
        for (int i = args.getStart().getTokenIndex(); i <= args.getStop().getTokenIndex(); i++) {
            Token token = getInputStream().get(i);
            if (token.getChannel() != Token.DEFAULT_CHANNEL) {
                continue;
            }
            String text = token.getText();
            if (isOpeningDelimiter(text)) {
                depth++;
            } else if (isClosingDelimiter(text)) {
                depth--;
            } else if (depth == 0 && ",".equals(text)) {
                count++;
            }
        }
        return count;
    }

    private String argumentText(ParserRuleContext args, int index) {
        StringBuilder result = new StringBuilder();
        int argument = 0;
        int depth = 0;
        for (int i = args.getStart().getTokenIndex(); i <= args.getStop().getTokenIndex(); i++) {
            Token token = getInputStream().get(i);
            if (token.getChannel() != Token.DEFAULT_CHANNEL) {
                continue;
            }
            String text = token.getText();
            if (depth == 0 && ",".equals(text)) {
                argument++;
                continue;
            }
            if (argument == index) {
                result.append(text);
            }
            if (isOpeningDelimiter(text)) {
                depth++;
            } else if (isClosingDelimiter(text)) {
                depth--;
            }
        }
        return result.toString();
    }

    private String contextText(ParserRuleContext context) {
        StringBuilder result = new StringBuilder();
        for (int i = context.getStart().getTokenIndex(); i <= context.getStop().getTokenIndex(); i++) {
            Token token = getInputStream().get(i);
            if (token.getChannel() == Token.DEFAULT_CHANNEL) {
                result.append(token.getText());
            }
        }
        return result.toString();
    }

    private static boolean isOpeningDelimiter(String text) {
        return "(".equals(text) || "[".equals(text) || "{".equals(text);
    }

    private static boolean isClosingDelimiter(String text) {
        return ")".equals(text) || "]".equals(text) || "}".equals(text);
    }

    protected final boolean isDynamicPrivilege() {
        String privilege = getInputStream().LT(1).getText();
        if (privilege == null || !atLeast(8, 0)) {
            return false;
        }
        String normalized = privilege.toUpperCase(Locale.ROOT);
        if (atLeast(9, 7) && ("CREATE_SPATIAL_REFERENCE_SYSTEM".equals(normalized) || "EXPORT_QUERY_RESULTS".equals(normalized) || "MANAGE_DATA_MASKING_POLICY".equals(normalized)
                              || "OPTION_TRACKER_OBSERVER".equals(normalized) || "OPTION_TRACKER_UPDATER".equals(normalized))) {
            return true;
        }
        return (atLeast(8, 4) ? DYNAMIC_PRIVILEGES_84 : DYNAMIC_PRIVILEGES_80).contains(normalized);
    }

    protected final boolean isReplicationSourceOption() {
        String option = getInputStream().LT(1).getText();
        if (option == null) {
            return false;
        }
        switch (option.toUpperCase(java.util.Locale.ROOT)) {
            case "SOURCE_HOST":
            case "NETWORK_NAMESPACE":
            case "SOURCE_BIND":
            case "SOURCE_USER":
            case "SOURCE_PASSWORD":
            case "SOURCE_PORT":
            case "SOURCE_CONNECT_RETRY":
            case "SOURCE_RETRY_COUNT":
            case "SOURCE_DELAY":
            case "SOURCE_SSL":
            case "SOURCE_SSL_CA":
            case "SOURCE_SSL_CAPATH":
            case "SOURCE_TLS_VERSION":
            case "SOURCE_TLS_CIPHERSUITES":
            case "SOURCE_SSL_CERT":
            case "SOURCE_SSL_CIPHER":
            case "SOURCE_SSL_KEY":
            case "SOURCE_SSL_VERIFY_SERVER_CERT":
            case "SOURCE_SSL_CRL":
            case "SOURCE_SSL_CRLPATH":
            case "SOURCE_PUBLIC_KEY_PATH":
            case "GET_SOURCE_PUBLIC_KEY":
            case "SOURCE_HEARTBEAT_PERIOD":
            case "IGNORE_SERVER_IDS":
            case "SOURCE_COMPRESSION_ALGORITHMS":
            case "SOURCE_ZSTD_COMPRESSION_LEVEL":
            case "SOURCE_AUTO_POSITION":
            case "PRIVILEGE_CHECKS_USER":
            case "REQUIRE_ROW_FORMAT":
            case "REQUIRE_TABLE_PRIMARY_KEY_CHECK":
            case "SOURCE_CONNECTION_AUTO_FAILOVER":
            case "ASSIGN_GTIDS_TO_ANONYMOUS_TRANSACTIONS":
            case "GTID_ONLY":
            case "SOURCE_LOG_FILE":
            case "SOURCE_LOG_POS":
            case "RELAY_LOG_FILE":
            case "RELAY_LOG_POS":
                return true;
            default:
                return atMost(8, 0) && isDeprecatedMasterSourceOption(option);
        }
    }

    private boolean isDeprecatedMasterSourceOption(String option) {
        switch (option.toUpperCase(java.util.Locale.ROOT)) {
            case "MASTER_HOST":
            case "MASTER_BIND":
            case "MASTER_USER":
            case "MASTER_PASSWORD":
            case "MASTER_PORT":
            case "MASTER_CONNECT_RETRY":
            case "MASTER_RETRY_COUNT":
            case "MASTER_DELAY":
            case "MASTER_SSL":
            case "MASTER_SSL_CA":
            case "MASTER_SSL_CAPATH":
            case "MASTER_TLS_VERSION":
            case "MASTER_TLS_CIPHERSUITES":
            case "MASTER_SSL_CERT":
            case "MASTER_SSL_CIPHER":
            case "MASTER_SSL_KEY":
            case "MASTER_SSL_VERIFY_SERVER_CERT":
            case "MASTER_SSL_CRL":
            case "MASTER_SSL_CRLPATH":
            case "MASTER_PUBLIC_KEY_PATH":
            case "GET_MASTER_PUBLIC_KEY":
            case "MASTER_HEARTBEAT_PERIOD":
            case "MASTER_COMPRESSION_ALGORITHMS":
            case "MASTER_ZSTD_COMPRESSION_LEVEL":
            case "MASTER_AUTO_POSITION":
            case "MASTER_LOG_FILE":
            case "MASTER_LOG_POS":
                return true;
            default:
                return false;
        }
    }
}
