import BasicParser from './common/basicParser';
import { SyntaxContextType } from './common/basic-parser-types';
import { StarRocksSqlLexer } from '../lib/starrocks/StarRocksSqlLexer';
import { StarRocksSqlParser, } from '../lib/starrocks/StarRocksSqlParser';
import { STARROCKS_KEYWORDS } from './keywords/starrocks';
export default class StarRocks extends BasicParser {
    constructor() {
        super(...arguments);
        this.preferredRules = new Set([
            StarRocksSqlParser.RULE_qualifiedName,
            StarRocksSqlParser.RULE_partitionNames,
            StarRocksSqlParser.RULE_columnReference,
        ]);
    }
    createLexerFormCharStream(charStreams) {
        const lexer = new StarRocksSqlLexer(charStreams);
        return lexer;
    }
    createParserFromTokenStream(tokenStream) {
        return new StarRocksSqlParser(tokenStream);
    }
    get splitListener() {
        return new starRocksSqlSplitListener();
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
                case StarRocksSqlParser.RULE_qualifiedName: {
                    syntaxContextType = SyntaxContextType.TABLE;
                    break;
                }
                case StarRocksSqlParser.RULE_partitionNames: {
                    syntaxContextType = SyntaxContextType.PARTITION;
                    break;
                }
                case StarRocksSqlParser.RULE_columnReference: {
                    syntaxContextType = SyntaxContextType.COLUMN;
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
            if (symbolicName && STARROCKS_KEYWORDS.includes(symbolicName)) {
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
export class starRocksSqlSplitListener {
    constructor() {
        this._statementsContext = [];
        this.enterStatement = (ctx) => {
            this._statementsContext.push(ctx);
        };
        this._tableNamesContext = [];
        this.enterQualifiedName = (ctx) => {
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
