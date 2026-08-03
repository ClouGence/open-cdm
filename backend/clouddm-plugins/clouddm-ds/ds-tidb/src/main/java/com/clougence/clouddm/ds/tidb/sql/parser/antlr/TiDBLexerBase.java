package com.clougence.clouddm.ds.tidb.sql.parser.antlr;

import org.antlr.v4.runtime.*;

import com.clougence.clouddm.ds.tidb.sql.parser.TiDBParserConfig;
import com.clougence.clouddm.ds.tidb.sql.parser.TiDBParserFeature;
import com.clougence.clouddm.ds.tidb.sql.parser.TiDBVersion;

public abstract class TiDBLexerBase extends Lexer {

    private TiDBParserConfig config                    = TiDBParserConfig.unknownSqlMode(null);
    private boolean           insideExecutableComment;
    private int               lastDefaultTokenType      = Token.INVALID_TYPE;
    private int               lastDefaultTokenStopIndex = -2;

    protected TiDBLexerBase(CharStream input){
        super(input);
    }

    @Override
    public Token nextToken() {
        Token token;
        if (exactVersion() >= 80100 && _input.LA(1) == '$' && !isImmediatelyAfterDot(_input.index())) {
            int delimiterLength = dollarQuoteDelimiterLength();
            int tokenLength = delimiterLength == 0 ? 0 : dollarQuoteTokenLength(delimiterLength);
            if (delimiterLength > 0) {
                token = tokenLength > 0 ? emitDollarQuotedString(tokenLength) : emitUnterminatedDollarQuote();
                rememberDefaultToken(token);
                return token;
            }
        }
        token = super.nextToken();
        downgradeVersionedToken(token);
        classifySpecialFunctionToken(token);
        rememberDefaultToken(token);
        return token;
    }

    private void classifySpecialFunctionToken(Token token) {
        if (!(token instanceof WritableToken writableToken) || token.getChannel() != DEFAULT_TOKEN_CHANNEL || !config.sqlModeKnown() || !isSpecialFunctionName(token.getText())) {
            return;
        }
        if (isImmediatelyAfterDot(token.getStartIndex())) {
            writableToken.setType(TiDBLexer.ID);
            return;
        }
        if (!isIgnoreSpace() && !isImmediatelyFollowedByLeftParen(token)) {
            writableToken.setType(TiDBLexer.ID);
        }
    }

    private boolean isImmediatelyFollowedByLeftParen(Token token) {
        int nextIndex = token.getStopIndex() + 1;
        return nextIndex >= 0 && nextIndex < _input.size() && _input.getText(org.antlr.v4.runtime.misc.Interval.of(nextIndex, nextIndex)).charAt(0) == '(';
    }

    private static boolean isSpecialFunctionName(String text) {
        if (text == null) {
            return false;
        }
        return switch (text.toUpperCase(java.util.Locale.ROOT)) {
            case "ADDDATE", "BIT_AND", "BIT_OR", "BIT_XOR", "CAST", "COUNT", "CURDATE", "CURTIME", "DATE_ADD", "DATE_SUB", "EXTRACT", "GROUP_CONCAT", "JSON_ARRAYAGG",
                    "JSON_DUALITY_OBJECT", "JSON_OBJECTAGG", "MAX", "MID", "MIN", "NOW", "POSITION", "PI", "SESSION_USER", "STD", "STDDEV", "STDDEV_POP", "STDDEV_SAMP",
                    "ST_COLLECT", "SUBDATE", "SUBSTR", "SUBSTRING", "SUM", "SYSDATE", "SYSTEM_USER", "TRIM", "VARIANCE", "VAR_POP", "VAR_SAMP" ->
                true;
            default -> false;
        };
    }

    private void downgradeVersionedToken(Token token) {
        if (!(token instanceof WritableToken writableToken) || isTokenAllowed(token.getType())) {
            return;
        }
        writableToken.setType(TiDBLexer.ID);
    }

    private boolean isTokenAllowed(int tokenType) {
        // TiDB introduced VECTOR in major 8, earlier than the MySQL grammar
        // ceiling (9.7) from which this lexer was cloned.
        if (tokenType == TiDBLexer.VECTOR) {
            return tidbAtLeast(8);
        }
        // TABLESAMPLE and BERNOULLI are TiDB keywords in every supported
        // major even though the cloned MySQL lexer gates them at 8.4.
        if (tokenType == TiDBLexer.TABLESAMPLE || tokenType == TiDBLexer.BERNOULLI || tokenType == TiDBLexer.SQL_CACHE) {
            return true;
        }
        if (tokenType == TiDBLexer.POLICY) {
            return true;
        }
        return switch (tokenType) {
            case TiDBLexer.ANALYSE, TiDBLexer.REDOFILE, TiDBLexer.SQL_CACHE -> atMost(5, 7);
            case TiDBLexer.OLD_PASSWORD -> atMost(5, 6);
            case TiDBLexer.MASTER_BIND, TiDBLexer.MASTER_SSL_VERIFY_SERVER_CERT -> atMost(8, 0);
            case TiDBLexer.COMPONENT, TiDBLexer.CLONE, TiDBLexer.EXCEPT, TiDBLexer.EXCLUDE, TiDBLexer.GROUPS, TiDBLexer.GROUPING,
                    TiDBLexer.INTERSECT, TiDBLexer.LATERAL, TiDBLexer.NULLS, TiDBLexer.OTHERS, TiDBLexer.TIES, TiDBLexer.RESTART,
                    TiDBLexer.RESPECT, TiDBLexer.URL, TiDBLexer.BULK, TiDBLexer.ZONE, TiDBLexer.GEOMCOLLECTION ->
                atLeast(8, 0);
            case TiDBLexer.PARSE_TREE, TiDBLexer.QUALIFY, TiDBLexer.S3, TiDBLexer.PARALLEL -> atLeast(8, 4);
            case TiDBLexer.ABSENT, TiDBLexer.DUALITY, TiDBLexer.EXTERNAL, TiDBLexer.EXTERNAL_FORMAT, TiDBLexer.LIBRARY, TiDBLexer.MASKING,
                    TiDBLexer.GUIDED, TiDBLexer.VALIDATE, TiDBLexer.POLICY, TiDBLexer.RELATIONAL, TiDBLexer.VECTOR, TiDBLexer.URI,
                    TiDBLexer.HEADER, TiDBLexer.PARAMETERS, TiDBLexer.MATERIALIZED, TiDBLexer.SETS, TiDBLexer.ALLOW_MISSING_FILES,
                    TiDBLexer.AUTO_REFRESH, TiDBLexer.AUTO_REFRESH_SOURCE, TiDBLexer.FILES, TiDBLexer.FILE_FORMAT, TiDBLexer.FILE_NAME,
                    TiDBLexer.FILE_PATTERN, TiDBLexer.FILE_PREFIX, TiDBLexer.STRICT_LOAD, TiDBLexer.VERIFY_KEY_CONSTRAINTS ->
                atLeast(9, 7);
            case TiDBLexer.SECONDARY_LOAD, TiDBLexer.SECONDARY_UNLOAD -> atLeast(8, 0);
            case TiDBLexer.GB18030 -> atLeast(5, 7);
            case TiDBLexer.JSON_DUALITY_OBJECT -> isFunctionTokenAllowed(90700);
            case TiDBLexer.JSON_ARRAYAGG, TiDBLexer.JSON_OBJECTAGG -> isFunctionTokenAllowed(50722);
            case TiDBLexer.ST_COLLECT -> isFunctionTokenAllowed(80024);
            case TiDBLexer.STRING_CHARSET_NAME -> {
                String token = tokenText();
                yield (!"_gb18030".equalsIgnoreCase(token) || atLeast(5, 7)) && (!"_filename".equalsIgnoreCase(token) || exactVersion() < 50710);
            }
            case TiDBLexer.DOLLAR_QUOTED_STRING -> false;
            default -> true;
        };
    }

    private String tokenText() {
        return _text == null ? getText() : _text;
    }

    private void rememberDefaultToken(Token token) {
        if (token.getChannel() != DEFAULT_TOKEN_CHANNEL) {
            return;
        }
        lastDefaultTokenType = token.getType();
        lastDefaultTokenStopIndex = token.getStopIndex();
    }

    private boolean isImmediatelyAfterDot(int tokenStartIndex) {
        return lastDefaultTokenType == TiDBLexer.DOT && lastDefaultTokenStopIndex + 1 == tokenStartIndex;
    }

    private int dollarQuoteDelimiterLength() {
        if (_input.LA(2) == '$') {
            return 2;
        }
        if (!isDollarTagPart(_input.LA(2))) {
            return 0;
        }
        int offset = 3;
        while (isDollarTagPart(_input.LA(offset))) {
            offset++;
        }
        return _input.LA(offset) == '$' ? offset : 0;
    }

    private int dollarQuoteTokenLength(int delimiterLength) {
        for (int offset = delimiterLength + 1; _input.LA(offset) != IntStream.EOF; offset++) {
            if (_input.LA(offset) == '$' && dollarQuoteDelimiterMatches(offset, delimiterLength)) {
                return offset + delimiterLength - 1;
            }
        }
        return 0;
    }

    private boolean dollarQuoteDelimiterMatches(int offset, int delimiterLength) {
        for (int i = 0; i < delimiterLength; i++) {
            if (_input.LA(offset + i) != _input.LA(1 + i)) {
                return false;
            }
        }
        return true;
    }

    private Token emitDollarQuotedString(int tokenLength) {
        _token = null;
        _channel = DEFAULT_TOKEN_CHANNEL;
        _tokenStartCharIndex = _input.index();
        _tokenStartLine = getLine();
        _tokenStartCharPositionInLine = getCharPositionInLine();
        _text = null;
        for (int i = 0; i < tokenLength; i++) {
            getInterpreter().consume(_input);
        }
        _type = TiDBLexer.DOLLAR_QUOTED_STRING;
        if (_input.LA(1) == IntStream.EOF) {
            _hitEOF = true;
        }
        return emit();
    }

    private Token emitUnterminatedDollarQuote() {
        int tokenLength = 0;
        while (_input.LA(tokenLength + 1) != IntStream.EOF) {
            tokenLength++;
        }
        _token = null;
        _channel = DEFAULT_TOKEN_CHANNEL;
        _tokenStartCharIndex = _input.index();
        _tokenStartLine = getLine();
        _tokenStartCharPositionInLine = getCharPositionInLine();
        _text = null;
        for (int i = 0; i < tokenLength; i++) {
            getInterpreter().consume(_input);
        }
        _type = TiDBLexer.ERROR_RECONGNIGION;
        _hitEOF = true;
        return emit();
    }

    private static boolean isDollarTagPart(int value) {
        return value == '_' || value >= 'A' && value <= 'Z' || value >= 'a' && value <= 'z' || value >= '0' && value <= '9' || value >= 0x80;
    }

    public final void setConfig(TiDBParserConfig config) { this.config = config == null ? TiDBParserConfig.unknownSqlMode(null) : config; }

    public final TiDBVersion tiDBVersion() {
        return config.version();
    }

    protected final boolean tidbAtLeast(TiDBVersion minimum) {
        return config.version().atLeast(minimum);
    }

    protected final boolean tidbAtLeast(int minimumMajor) {
        return config.version().major() >= minimumMajor;
    }

    public final TiDBParserConfig config() {
        return config;
    }

    protected final int exactVersion() {
        return config.exactVersion();
    }

    protected final boolean isAnsiQuotes() { return config.isEnabled(TiDBParserFeature.ANSI_QUOTES); }

    protected final boolean isSqlModeUnknown() { return !config.sqlModeKnown(); }

    protected final boolean isNoBackslashEscapes() { return config.isEnabled(TiDBParserFeature.NO_BACKSLASH_ESCAPES); }

    protected final boolean isPipesAsConcat() { return config.isEnabled(TiDBParserFeature.PIPES_AS_CONCAT); }

    protected final boolean isIgnoreSpace() { return config.isEnabled(TiDBParserFeature.IGNORE_SPACE); }

    protected final boolean isFunctionTokenAllowed(int introducedExactVersion) {
        return exactVersion() >= introducedExactVersion && isFunctionLeftParenAhead() && !isImmediatelyAfterDot(_tokenStartCharIndex);
    }

    private boolean isFunctionLeftParenAhead() {
        if (_input.LA(1) == '(') {
            return true;
        }
        if (!isIgnoreSpace()) {
            return false;
        }
        int offset = 1;
        while (Character.isWhitespace(_input.LA(offset))) {
            offset++;
        }
        return _input.LA(offset) == '(';
    }

    protected final boolean isWhitespaceAhead() { return Character.isWhitespace(_input.LA(1)); }

    protected final boolean notIdentifierPartAhead() {
        int next = _input.LA(1);
        return next == IntStream.EOF || !(next == '$' || next == '_' || Character.isLetterOrDigit(next) || next >= 0x80);
    }

    protected final boolean notIdentifierPartExceptDollarAhead() {
        int next = _input.LA(1);
        return next == '$' || next == IntStream.EOF || !(next == '_' || Character.isLetterOrDigit(next) || next >= 0x80);
    }

    protected final boolean isLeadingDotRealAllowed() {
        int previous = _input.LA(-1);
        return previous == IntStream.EOF || !(previous == '$' || previous == '_' || previous == '`' || previous == '"' || Character.isLetterOrDigit(previous) || previous >= 0x80);
    }

    protected final void normalizeExecutableCommentPrefix() {
        String text = getText();
        if (text.length() != 9) {
            return;
        }
        if (atLeast(8, 4) && isWhitespaceAhead()) {
            return;
        }
        _input.seek(_input.index() - 1);
        setCharPositionInLine(Math.max(0, getCharPositionInLine() - 1));
    }

    protected final boolean hasExecutableCommentEndAhead() {
        for (int offset = 1; _input.LA(offset) != IntStream.EOF; offset++) {
            if (_input.LA(offset) == '*' && _input.LA(offset + 1) == '/') {
                return true;
            }
        }
        return false;
    }

    protected final boolean isTiDBExecutableCommentAhead() { return _input.LA(1) == '/' && _input.LA(2) == '*' && _input.LA(3) == 'T' && _input.LA(4) == '!'; }

    protected final boolean isExecutableCommentBackslashStringAhead() {
        return this.insideExecutableComment && _input.LA(1) == '\'' && _input.LA(2) == '\\' && _input.LA(3) == '\'';
    }

    protected final boolean isTiDBExecutableCommentActive() {
        // Feature comments keep DDL readable by older TiDB parsers. TTL became
        // executable syntax in TiDB 6; a TiDB 5 parser preserves the whole
        // comment but deliberately does not parse its body.
        if (config.version().major() < 6 && "/*T![ttl]".equalsIgnoreCase(getText())) {
            skipInactiveExecutableComment();
            return false;
        }
        return true;
    }

    protected final void enterExecutableComment() {
        this.insideExecutableComment = true;
    }

    protected final void leaveExecutableComment() {
        this.insideExecutableComment = false;
    }

    protected final boolean isInsideExecutableComment() { return this.insideExecutableComment; }

    protected final boolean isExecutableCommentActive() {
        String digits = getText().substring(3);
        if (digits.isEmpty()) {
            return true;
        }
        int threshold;
        if (digits.length() == 6) {
            threshold = Integer.parseInt(digits);
        } else if (digits.length() >= 5) {
            threshold = Integer.parseInt(digits.substring(0, 5));
        } else {
            return true;
        }
        if (exactVersion() >= threshold) {
            return true;
        }
        skipInactiveExecutableComment();
        return false;
    }

    private void skipInactiveExecutableComment() {
        int depth = 1;
        while (_input.LA(1) != IntStream.EOF && depth > 0) {
            if (_input.LA(1) == '/' && _input.LA(2) == '*') {
                depth++;
                _input.consume();
                _input.consume();
            } else if (_input.LA(1) == '*' && _input.LA(2) == '/') {
                depth--;
                _input.consume();
                _input.consume();
            } else {
                _input.consume();
            }
        }
    }

    protected final boolean atLeast(int major, int minor) {
        return config.atLeast(major, minor);
    }

    protected final boolean atMost(int major, int minor) {
        return config.atMost(major, minor);
    }

    protected final boolean between(int minMajor, int minMinor, int maxMajor, int maxMinor) {
        return config.between(minMajor, minMinor, maxMajor, maxMinor);
    }
}
