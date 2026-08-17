package com.clougence.sql.mysql.parser.antlr;

import org.antlr.v4.runtime.*;

import com.clougence.sql.mysql.analysis.sysobj.MySqlResourceRegistry;
import com.clougence.sql.mysql.parser.MySqlParserConfig;
import com.clougence.sql.mysql.parser.MySqlParserConfig.Feature;
import com.clougence.sql.mysql.parser.MySqlVersion;
import com.clougence.sql.common.parser.SplitLexerFastPath;
import com.clougence.sql.common.parser.SplitLexerFastPath.CommentSyntax;

public abstract class MySqlLexerBase extends Lexer {

    private MySqlParserConfig config                    = MySqlParserConfig.unknownSqlMode(null);
    private boolean           insideExecutableComment;
    private int               lastDefaultTokenType      = Token.INVALID_TYPE;
    private int               lastDefaultTokenStopIndex = -2;
    private int               lastDefaultTokenLine      = -1;
    private String            statementDelimiter       = ";";
    private Token             pendingStatementDelimiter;

    protected MySqlLexerBase(CharStream input){
        super(input);
    }

    private static volatile boolean fastPathEnabled = !"false".equalsIgnoreCase(System.getProperty("com.clougence.sql.mysql.lexer.fastpath", "true"));

    /** Diagnostic switch for tests; production toggles via the system property above. */
    public static void setFastPathEnabled(boolean enabled) { fastPathEnabled = enabled; }

    @Override
    public Token nextToken() {
        if (this.pendingStatementDelimiter != null) {
            Token pending = this.pendingStatementDelimiter;
            this.pendingStatementDelimiter = null;
            rememberDefaultToken(pending);
            return pending;
        }
        Token customDelimiter = customDelimiterToken();
        if (customDelimiter != null) {
            rememberDefaultToken(customDelimiter);
            return customDelimiter;
        }
        if (fastPathEnabled) {
            Token fast = fastPathToken();
            if (fast != null) {
                rememberDefaultToken(fast);
                return fast;
            }
        }
        Token token;
        if (exactVersion() >= 80100 && _input.LA(1) == '$' && !isImmediatelyAfterDot(_input.index())) {
            int delimiterLength = dollarQuoteDelimiterLength();
            int tokenLength = delimiterLength == 0 ? 0 : dollarQuoteTokenLength(delimiterLength);
            if (delimiterLength > 0) {
                int marker = _input.mark();
                try {
                    token = tokenLength > 0 ? emitDollarQuotedString(tokenLength) : emitUnterminatedDollarQuote();
                    rememberDefaultToken(token);
                    return token;
                } finally {
                    _input.release(marker);
                }
            }
        }
        token = super.nextToken();
        splitTrailingStatementDelimiter(token);
        downgradeVersionedToken(token);
        classifySpecialFunctionToken(token);
        rememberDefaultToken(token);
        return token;
    }

    /**
     * Hand-written fast path for high-frequency simple tokens (whitespace, comma, parens,
     * plain decimal digits). These dominate bulk INSERT/IN-list statements and the ANTLR
     * ATN simulation costs ~100-200us per token on this 900+ rule grammar, while a direct
     * scan is nanoseconds. Returns null to fall back to {@link #super.nextToken()}.
     */
    private Token fastPathToken() {
        if (splitFastPathEnabled()) {
            Token token = SplitLexerFastPath.nextToken(this, MySqlLexer.ID, MySqlLexer.SPACE, CommentSyntax.MYSQL);
            if (token != null) {
                return token;
            }
        }
        int startIndex = _input.index();
        int marker = _input.mark();
        try {
            int c = _input.LA(1);
            if (c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == 0x0B || c == 0x0C) {
                int startLine = getLine();
                int startColumn = getCharPositionInLine();
                while (isSpace(_input.LA(1))) {
                    getInterpreter().consume(_input);
                }
                return emitFast(MySqlLexer.SPACE, Token.HIDDEN_CHANNEL, startIndex, startLine, startColumn);
            }
            if (c == ',') {
                return emitSingle(MySqlLexer.COMMA);
            }
            if (c == '(') {
                return emitSingle(MySqlLexer.LR_BRACKET);
            }
            if (c == ')') {
                return emitSingle(MySqlLexer.RR_BRACKET);
            }
            if (c >= '0' && c <= '9') {
                int length = 0;
                while (isDigit(_input.LA(1 + length))) {
                    length++;
                }
                int next = _input.LA(1 + length);
                // Ambiguous continuations (REAL_LITERAL, HEXADECIMAL_LITERAL, FILESIZE_LITERAL, ID, ...)
                // must be decided by the real lexer. '$' is allowed after digits.
                if (next == '.' || next == 'e' || next == 'E' || next == 'x' || next == 'X' || isIdentifierPart(next)) {
                    return null;
                }
                int startLine = getLine();
                int startColumn = getCharPositionInLine();
                for (int i = 0; i < length; i++) {
                    getInterpreter().consume(_input);
                }
                if (length == 1) {
                    if (c == '0') {
                        return emitFast(MySqlLexer.ZERO_DECIMAL, Token.DEFAULT_CHANNEL, startIndex, startLine, startColumn);
                    }
                    if (c == '1') {
                        return emitFast(MySqlLexer.ONE_DECIMAL, Token.DEFAULT_CHANNEL, startIndex, startLine, startColumn);
                    }
                    if (c == '2') {
                        return emitFast(MySqlLexer.TWO_DECIMAL, Token.DEFAULT_CHANNEL, startIndex, startLine, startColumn);
                    }
                }
                return emitFast(MySqlLexer.DECIMAL_LITERAL, Token.DEFAULT_CHANNEL, startIndex, startLine, startColumn);
            }
            return null;
        } finally {
            // Mirror Lexer.nextToken(): release the token-start marker so unbuffered char
            // streams slide their window and keep the emitted token text readable.
            _input.release(marker);
        }
    }

    /** Split-only lexers may bypass the full grammar for unambiguous tokens. */
    protected boolean splitFastPathEnabled() {
        return false;
    }

    private static boolean isSpace(int value) {
        return value == ' ' || value == '\t' || value == '\n' || value == '\r' || value == 0x0B || value == 0x0C;
    }

    private static boolean isDigit(int value) {
        return value >= '0' && value <= '9';
    }

    /** Identifier part except '$' (matches notIdentifierPartExceptDollarAhead semantics). */
    private static boolean isIdentifierPart(int value) {
        return value >= 'a' && value <= 'z' || value >= 'A' && value <= 'Z' || value >= '0' && value <= '9' || value == '_' || value >= 0x80;
    }

    private Token emitSingle(int type) {
        int startIndex = _input.index();
        int startLine = getLine();
        int startColumn = getCharPositionInLine();
        getInterpreter().consume(_input);
        return emitFast(type, Token.DEFAULT_CHANNEL, startIndex, startLine, startColumn);
    }

    private Token emitFast(int type, int channel, int startIndex, int startLine, int startColumn) {
        _token = null;
        _channel = channel;
        _tokenStartCharIndex = startIndex;
        _tokenStartLine = startLine;
        _tokenStartCharPositionInLine = startColumn;
        _text = null;
        _type = type;
        if (_input.LA(1) == IntStream.EOF) {
            _hitEOF = true;
        }
        Token token = emit();
        rememberDefaultToken(token);
        return token;
    }

    private void classifySpecialFunctionToken(Token token) {
        if (!(token instanceof WritableToken writableToken) || token.getChannel() != DEFAULT_TOKEN_CHANNEL || !config.isSqlModeKnown()
                || !MySqlResourceRegistry.instance().isLexerSpecialFunction(token.getText())) {
            return;
        }
        if (isImmediatelyAfterDot(token.getStartIndex())) {
            writableToken.setType(MySqlLexer.ID);
            return;
        }
        if (!isIgnoreSpace() && !isImmediatelyFollowedByLeftParen(token)) {
            writableToken.setType(MySqlLexer.ID);
        }
    }

    private boolean isImmediatelyFollowedByLeftParen(Token token) {
        int nextIndex = token.getStopIndex() + 1;
        return nextIndex == _input.index() && _input.LA(1) == '(';
    }

    private void downgradeVersionedToken(Token token) {
        if (!(token instanceof WritableToken writableToken) || isTokenAllowed(token)) {
            return;
        }
        writableToken.setType(MySqlLexer.ID);
    }

    private boolean isTokenAllowed(Token token) {
        return switch (token.getType()) {
            case MySqlLexer.ANALYSE, MySqlLexer.REDOFILE, MySqlLexer.SQL_CACHE -> atMost(5, 7);
            case MySqlLexer.OLD_PASSWORD -> atMost(5, 6);
            case MySqlLexer.MASTER_BIND, MySqlLexer.MASTER_SSL_VERIFY_SERVER_CERT -> atMost(8, 0);
            case MySqlLexer.COMPONENT, MySqlLexer.CLONE, MySqlLexer.EXCEPT, MySqlLexer.EXCLUDE, MySqlLexer.GROUPS, MySqlLexer.GROUPING, MySqlLexer.INTERSECT, MySqlLexer.LATERAL,
                    MySqlLexer.NULLS, MySqlLexer.OTHERS, MySqlLexer.TIES, MySqlLexer.RESTART, MySqlLexer.RESPECT, MySqlLexer.URL, MySqlLexer.BULK, MySqlLexer.ZONE,
                    MySqlLexer.GEOMCOLLECTION ->
                atLeast(8, 0);
            case MySqlLexer.TABLESAMPLE, MySqlLexer.BERNOULLI, MySqlLexer.PARSE_TREE, MySqlLexer.QUALIFY, MySqlLexer.S3, MySqlLexer.PARALLEL -> atLeast(8, 4);
            case MySqlLexer.ABSENT, MySqlLexer.DUALITY, MySqlLexer.EXTERNAL, MySqlLexer.EXTERNAL_FORMAT, MySqlLexer.LIBRARY, MySqlLexer.MASKING, MySqlLexer.GUIDED,
                    MySqlLexer.VALIDATE, MySqlLexer.POLICY, MySqlLexer.RELATIONAL, MySqlLexer.VECTOR, MySqlLexer.URI, MySqlLexer.HEADER, MySqlLexer.PARAMETERS,
                    MySqlLexer.MATERIALIZED, MySqlLexer.SETS, MySqlLexer.ALLOW_MISSING_FILES, MySqlLexer.AUTO_REFRESH, MySqlLexer.AUTO_REFRESH_SOURCE, MySqlLexer.FILES,
                    MySqlLexer.FILE_FORMAT, MySqlLexer.FILE_NAME, MySqlLexer.FILE_PATTERN, MySqlLexer.FILE_PREFIX, MySqlLexer.STRICT_LOAD, MySqlLexer.VERIFY_KEY_CONSTRAINTS ->
                atLeast(9, 7);
            case MySqlLexer.SECONDARY_LOAD, MySqlLexer.SECONDARY_UNLOAD -> atLeast(8, 0);
            case MySqlLexer.GB18030 -> atLeast(5, 7);
            case MySqlLexer.JSON_DUALITY_OBJECT -> isFunctionTokenAllowed(90700);
            case MySqlLexer.JSON_ARRAYAGG, MySqlLexer.JSON_OBJECTAGG -> isFunctionTokenAllowed(50722);
            case MySqlLexer.ST_COLLECT -> isFunctionTokenAllowed(80024);
            case MySqlLexer.STRING_CHARSET_NAME -> {
                String tokenText = token.getText();
                yield (!"_gb18030".equalsIgnoreCase(tokenText) || atLeast(5, 7)) && (!"_filename".equalsIgnoreCase(tokenText) || exactVersion() < 50710);
            }
            case MySqlLexer.DOLLAR_QUOTED_STRING -> false;
            default -> true;
        };
    }

    private void rememberDefaultToken(Token token) {
        if (token.getChannel() != DEFAULT_TOKEN_CHANNEL) {
            return;
        }
        lastDefaultTokenType = token.getType();
        lastDefaultTokenStopIndex = token.getStopIndex();
        lastDefaultTokenLine = token.getLine();
    }

    /** A DELIMITER directive is a mysql client command and must start a physical line. */
    protected final boolean isClientDelimiterDirective() {
        return lastDefaultTokenLine < getLine();
    }

    protected final void setStatementDelimiterDirective(String directive) {
        int index = "DELIMITER".length();
        while (index < directive.length() && Character.isWhitespace(directive.charAt(index))) {
            index++;
        }
        String delimiter = directive.substring(index).trim();
        this.statementDelimiter = delimiter.isEmpty() ? ";" : delimiter;
    }

    /** Inner executable-comment semicolons and inactive client delimiters are not boundaries. */
    protected final void hideInactiveSemicolon() {
        if (this.insideExecutableComment || !";".equals(this.statementDelimiter)) {
            setChannel(HIDDEN);
        }
    }

    private Token customDelimiterToken() {
        if (";".equals(this.statementDelimiter) || !delimiterAhead()) {
            return null;
        }
        int startIndex = _input.index();
        int startLine = getLine();
        int startColumn = getCharPositionInLine();
        for (int i = 0; i < this.statementDelimiter.length(); i++) {
            getInterpreter().consume(_input);
        }
        if (_input.LA(1) == IntStream.EOF) {
            _hitEOF = true;
        }
        return getTokenFactory().create(_tokenFactorySourcePair,
            MySqlLexer.SEMI,
            this.statementDelimiter,
            MySqlLexer.MYSQLCOMMENT,
            startIndex,
            startIndex + this.statementDelimiter.length() - 1,
            startLine,
            startColumn);
    }

    private boolean delimiterAhead() {
        for (int i = 0; i < this.statementDelimiter.length(); i++) {
            if (_input.LA(i + 1) != this.statementDelimiter.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * MySQL identifiers may contain '$', so {@code END$$} can arrive as one ID token. Split
     * the active client terminator back out without reparsing or altering the source text.
     */
    private void splitTrailingStatementDelimiter(Token token) {
        if (";".equals(this.statementDelimiter) || token.getType() == MySqlLexer.DELIMITER_DIRECTIVE || token.getChannel() != DEFAULT_TOKEN_CHANNEL
            || !(token instanceof CommonToken common)) {
            return;
        }
        String text = token.getText();
        if (text == null || text.length() <= this.statementDelimiter.length() || !text.endsWith(this.statementDelimiter)) {
            return;
        }
        int delimiterStart = token.getStopIndex() - this.statementDelimiter.length() + 1;
        int prefixLength = text.length() - this.statementDelimiter.length();
        common.setText(text.substring(0, prefixLength));
        common.setStopIndex(delimiterStart - 1);
        this.pendingStatementDelimiter = getTokenFactory().create(_tokenFactorySourcePair,
            MySqlLexer.SEMI,
            this.statementDelimiter,
            MySqlLexer.MYSQLCOMMENT,
            delimiterStart,
            token.getStopIndex() + this.statementDelimiter.length(),
            token.getLine(),
            token.getCharPositionInLine() + prefixLength);
    }

    private boolean isImmediatelyAfterDot(int tokenStartIndex) {
        return lastDefaultTokenType == MySqlLexer.DOT && lastDefaultTokenStopIndex + 1 == tokenStartIndex;
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
        _type = MySqlLexer.DOLLAR_QUOTED_STRING;
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
        _type = MySqlLexer.ERROR_RECONGNIGION;
        _hitEOF = true;
        return emit();
    }

    private static boolean isDollarTagPart(int value) {
        return value == '_' || value >= 'A' && value <= 'Z' || value >= 'a' && value <= 'z' || value >= '0' && value <= '9' || value >= 0x80;
    }

    public final void setVersion(MySqlVersion version) {
        MySqlVersion resolved = version == null ? MySqlVersion.LATEST : version;
        setVersion(resolved, resolved.exactVersion());
    }

    public final void setVersion(MySqlVersion version, int exactVersion) {
        MySqlVersion resolved = version == null ? MySqlVersion.LATEST : version;
        this.config = MySqlParserConfig.unknownSqlMode(versionString(resolved, exactVersion));
    }

    public final void setConfig(MySqlParserConfig config) { this.config = config; }

    public final MySqlParserConfig config() {
        return config;
    }

    protected final int exactVersion() {
        return config.exactVersion();
    }

    protected final boolean isAnsiQuotes() { return config.isEnabled(Feature.ANSI_QUOTES); }

    protected final boolean isSqlModeUnknown() { return !config.isSqlModeKnown(); }

    protected final boolean isNoBackslashEscapes() { return config.isEnabled(Feature.NO_BACKSLASH_ESCAPES); }

    protected final boolean isPipesAsConcat() { return config.isEnabled(Feature.PIPES_AS_CONCAT); }

    protected final boolean isIgnoreSpace() { return config.isEnabled(Feature.IGNORE_SPACE); }

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

    protected final boolean between(int minMajor, int minMinor, int maxMajor, int maxMinor) {
        return MySqlVersion.ge(config.grammarVersion(), minMajor * 100 + minMinor) && MySqlVersion.le(config.grammarVersion(), maxMajor * 100 + maxMinor);
    }

    private static String versionString(MySqlVersion version, int exactVersion) {
        int major = exactVersion / 10000;
        int minor = exactVersion / 100 % 100;
        int release = exactVersion % 100;
        if (major <= 0) {
            return version == null ? null : version.name();
        }
        return major + "." + minor + "." + release;
    }
}
