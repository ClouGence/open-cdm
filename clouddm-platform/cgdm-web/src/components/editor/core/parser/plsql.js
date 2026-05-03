import { PlSqlLexer } from '../lib/plsql/PlSqlLexer';
import { PlSqlParser } from '../lib/plsql/PlSqlParser';
import BasicParser from './common/basicParser';
import { SyntaxContextType } from './common/basic-parser-types';
import { PLSQL_KEYWORDS } from './keywords/plsql';
export default class PLSQL extends BasicParser {
    constructor() {
        super(...arguments);
        this.preferredRules = new Set([
            PlSqlParser.RULE_database_name,
            PlSqlParser.RULE_schema_name,
            PlSqlParser.RULE_tableview_name,
            PlSqlParser.RULE_function_name,
            PlSqlParser.RULE_column_name,
        ]);
    }
    createLexerFormCharStream(charStreams) {
        const lexer = new PlSqlLexer(charStreams);
        return lexer;
    }
    createParserFromTokenStream(tokenStream) {
        return new PlSqlParser(tokenStream);
    }
    get splitListener() {
        return new plSqlSplitListener();
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
                case PlSqlParser.RULE_database_name: {
                    syntaxContextType = SyntaxContextType.DATABASE;
                    break;
                }
                case PlSqlParser.RULE_schema_name: {
                    syntaxContextType = SyntaxContextType.SCHEMA;
                    break;
                }
                case PlSqlParser.RULE_tableview_name: {
                    syntaxContextType = SyntaxContextType.TABLE;
                    break;
                }
                case PlSqlParser.RULE_function_name: {
                    syntaxContextType = SyntaxContextType.FUNCTION;
                    break;
                }
                case PlSqlParser.RULE_column_name: {
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
            if (symbolicName && PLSQL_KEYWORDS.includes(symbolicName)) {
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
export class plSqlSplitListener {
    constructor() {
        this._statementsContext = [];
        this.enterSql_statement = (ctx) => {
            this._statementsContext.push(ctx);
        };
        this._tableNamesContext = [];
        this.enterTableview_name = (ctx) => {
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
