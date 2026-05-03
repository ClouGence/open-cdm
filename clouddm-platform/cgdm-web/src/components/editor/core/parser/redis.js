import BasicParser from './common/basicParser';
import { SyntaxContextType } from './common/basic-parser-types';
import { REDIS_KEYWORDS } from './keywords/redis';
import { RedisSqlLexer } from "../lib/redis/RedisSqlLexer";
import { RedisSqlParser, } from '../lib/redis/RedisSqlParser';
export default class Redis extends BasicParser {
    constructor() {
        super(...arguments);
        this.preferredRules = new Set([RedisSqlParser.RULE_keyName]);
    }
    createLexerFormCharStream(charStreams) {
        const lexer = new RedisSqlLexer(charStreams);
        return lexer;
    }
    createParserFromTokenStream(tokenStream) {
        return new RedisSqlParser(tokenStream);
    }
    get splitListener() {
        return new redisSqlSplitListener();
    }
    processCandidates(candidates, allTokens, caretTokenIndex, tokenIndexOffset) {
        const originalSyntaxSuggestions = [];
        const keywords = [];
        for (const candidate of candidates.rules) {
            const [ruleType, candidateRule] = candidate;
            const startTokenIndex = candidateRule.startTokenIndex + tokenIndexOffset;
            const tokenRanges = allTokens.slice(startTokenIndex, caretTokenIndex + tokenIndexOffset + 1);
            let syntaxContextType;
            switch (ruleType) {
                case RedisSqlParser.RULE_keyName: {
                    syntaxContextType = SyntaxContextType.Key;
                    break;
                }
                default:
                    break;
            }
            if (syntaxContextType) {
                originalSyntaxSuggestions.push({
                    syntaxContextType,
                    wordRanges: tokenRanges,
                });
            }
        }
        for (const candidate of candidates.tokens) {
            const symbolicName = this._parser.vocabulary.getSymbolicName(candidate[0]);
            const displayName = this._parser.vocabulary.getDisplayName(candidate[0]);
            if (symbolicName && REDIS_KEYWORDS.includes(symbolicName)) {
                const keyword = displayName.startsWith("'") && displayName.endsWith("'")
                    ? displayName.slice(1, -1)
                    : displayName;
                keywords.push(keyword);
            }
        }
        return {
            syntax: originalSyntaxSuggestions,
            keywords,
        };
    }
}
export class redisSqlSplitListener {
    constructor() {
        this._statementsContext = [];
        this.enterSqlStatement = (ctx) => {
            this._statementsContext.push(ctx);
        };
        this._tableNamesContext = [];
        this.enterKeyName = (ctx) => {
            this._tableNamesContext.push(ctx);
        };
    }
    get statementsContext() {
        return this._statementsContext;
    }
    get tableNamesContext() {
        return this._tableNamesContext;
    }
}
