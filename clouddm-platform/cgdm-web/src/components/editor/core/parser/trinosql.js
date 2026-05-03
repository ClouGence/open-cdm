import { TrinoSqlLexer } from '../lib/trinosql/TrinoSqlLexer';
import { TrinoSqlParser, } from '../lib/trinosql/TrinoSqlParser';
import BasicParser from './common/basicParser';
import { SyntaxContextType } from './common/basic-parser-types';
export default class TrinoSQL extends BasicParser {
    constructor() {
        super(...arguments);
        this.preferredRules = new Set([
            TrinoSqlParser.RULE_catalogName,
            TrinoSqlParser.RULE_catalogNameCreate,
            TrinoSqlParser.RULE_schemaName,
            TrinoSqlParser.RULE_schemaNameCreate,
            TrinoSqlParser.RULE_tableName,
            TrinoSqlParser.RULE_tableNameCreate,
            TrinoSqlParser.RULE_viewName,
            TrinoSqlParser.RULE_viewNameCreate,
            TrinoSqlParser.RULE_functionName,
            TrinoSqlParser.RULE_columnName,
            TrinoSqlParser.RULE_columnNameCreate,
        ]);
    }
    createLexerFormCharStream(charStreams) {
        const lexer = new TrinoSqlLexer(charStreams);
        return lexer;
    }
    createParserFromTokenStream(tokenStream) {
        const parser = new TrinoSqlParser(tokenStream);
        return parser;
    }
    get splitListener() {
        return new TrinoSqlSplitListener();
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
                case TrinoSqlParser.RULE_catalogName: {
                    syntaxContextType = SyntaxContextType.CATALOG;
                    break;
                }
                case TrinoSqlParser.RULE_schemaName: {
                    syntaxContextType = SyntaxContextType.DATABASE;
                    break;
                }
                case TrinoSqlParser.RULE_schemaNameCreate: {
                    syntaxContextType = SyntaxContextType.DATABASE_CREATE;
                    break;
                }
                case TrinoSqlParser.RULE_tableName: {
                    syntaxContextType = SyntaxContextType.TABLE;
                    break;
                }
                case TrinoSqlParser.RULE_tableNameCreate: {
                    syntaxContextType = SyntaxContextType.TABLE_CREATE;
                    break;
                }
                case TrinoSqlParser.RULE_viewName: {
                    syntaxContextType = SyntaxContextType.VIEW;
                    break;
                }
                case TrinoSqlParser.RULE_viewNameCreate: {
                    syntaxContextType = SyntaxContextType.VIEW_CREATE;
                    break;
                }
                case TrinoSqlParser.RULE_functionName: {
                    syntaxContextType = SyntaxContextType.FUNCTION;
                    break;
                }
                case TrinoSqlParser.RULE_columnNameCreate: {
                    syntaxContextType = SyntaxContextType.COLUMN_CREATE;
                    break;
                }
                case TrinoSqlParser.RULE_columnName: {
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
            if (symbolicName && symbolicName.startsWith('KW_')) {
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
export class TrinoSqlSplitListener {
    constructor() {
        this._statementsContext = [];
        this.exitSingleStatement = (ctx) => {
            this._statementsContext.push(ctx);
        };
    }
    get statementsContext() {
        return this._statementsContext;
    }
}
