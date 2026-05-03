import BasicParser from './common/basicParser';
import { SyntaxContextType } from './common/basic-parser-types';
import { TSqlLexer } from '../lib/tsql/TSqlLexer';
import { TSQL_KEYWORDS } from './keywords/tsql';
import { TSqlParser } from '../lib/tsql/TSqlParser';
export default class TSql extends BasicParser {
    constructor() {
        super(...arguments);
        this.preferredRules = new Set([
            TSqlParser.RULE_full_column_name,
            TSqlParser.RULE_full_table_name,
        ]);
    }
    createLexerFormCharStream(charStreams) {
        const lexer = new TSqlLexer(charStreams);
        return lexer;
    }
    createParserFromTokenStream(tokenStream) {
        return new TSqlParser(tokenStream);
    }
    get splitListener() {
        return new TSqlSplitListener();
    }
    processCandidates(candidates, allTokens, caretTokenIndex, tokenIndexOffset) {
        const originalSyntaxSuggestions = [];
        const keywords = [];
        for (let candidate of candidates.rules) {
            const [ruleType, candidateRule] = candidate;
            const startTokenIndex = candidateRule.startTokenIndex + tokenIndexOffset;
            const tokenRanges = allTokens.slice(startTokenIndex, caretTokenIndex + tokenIndexOffset + 1);
            let syntaxContextType;
            switch (ruleType) {
                case TSqlParser.RULE_full_table_name: {
                    syntaxContextType = SyntaxContextType.TABLE_CREATE;
                    break;
                }
                case TSqlParser.RULE_full_column_name: {
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
        for (let candidate of candidates.tokens) {
            const symbolicName = this._parser.vocabulary.getSymbolicName(candidate[0]);
            const displayName = this._parser.vocabulary.getDisplayName(candidate[0]);
            if (symbolicName && TSQL_KEYWORDS.includes(symbolicName)) {
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
export class TSqlSplitListener {
    constructor() {
        this._statementsContext = [];
        this.enterSql_clauses = (ctx) => {
            this._statementsContext.push(ctx);
        };
    }
    get statementsContext() {
        return this._statementsContext;
    }
}
