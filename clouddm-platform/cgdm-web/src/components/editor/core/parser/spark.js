import { SparkSqlLexer } from '../lib/spark/SparkSqlLexer';
import { SparkSqlParser, } from '../lib/spark/SparkSqlParser';
import BasicParser from './common/basicParser';
import { SyntaxContextType } from './common/basic-parser-types';
export default class SparkSQL extends BasicParser {
    constructor() {
        super(...arguments);
        this.preferredRules = new Set([
            SparkSqlParser.RULE_dbSchemaName,
            SparkSqlParser.RULE_dbSchemaNameCreate,
            SparkSqlParser.RULE_tableName,
            SparkSqlParser.RULE_tableNameCreate,
            SparkSqlParser.RULE_viewName,
            SparkSqlParser.RULE_viewNameCreate,
            SparkSqlParser.RULE_functionName,
            SparkSqlParser.RULE_functionNameCreate,
            SparkSqlParser.RULE_columnName,
            SparkSqlParser.RULE_columnNameCreate,
        ]);
    }
    createLexerFormCharStream(charStreams) {
        const lexer = new SparkSqlLexer(charStreams);
        return lexer;
    }
    createParserFromTokenStream(tokenStream) {
        const parser = new SparkSqlParser(tokenStream);
        return parser;
    }
    get splitListener() {
        return new SparkSqlSplitListener();
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
                case SparkSqlParser.RULE_dbSchemaName: {
                    syntaxContextType = SyntaxContextType.DATABASE;
                    break;
                }
                case SparkSqlParser.RULE_dbSchemaNameCreate: {
                    syntaxContextType = SyntaxContextType.DATABASE_CREATE;
                    break;
                }
                case SparkSqlParser.RULE_tableName: {
                    syntaxContextType = SyntaxContextType.TABLE;
                    break;
                }
                case SparkSqlParser.RULE_tableNameCreate: {
                    syntaxContextType = SyntaxContextType.TABLE_CREATE;
                    break;
                }
                case SparkSqlParser.RULE_viewName: {
                    syntaxContextType = SyntaxContextType.VIEW;
                    break;
                }
                case SparkSqlParser.RULE_viewNameCreate: {
                    syntaxContextType = SyntaxContextType.VIEW_CREATE;
                    break;
                }
                case SparkSqlParser.RULE_functionName: {
                    syntaxContextType = SyntaxContextType.FUNCTION;
                    break;
                }
                case SparkSqlParser.RULE_functionNameCreate: {
                    syntaxContextType = SyntaxContextType.FUNCTION_CREATE;
                    break;
                }
                case SparkSqlParser.RULE_columnName: {
                    syntaxContextType = SyntaxContextType.COLUMN;
                    break;
                }
                case SparkSqlParser.RULE_columnNameCreate: {
                    syntaxContextType = SyntaxContextType.COLUMN_CREATE;
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
export class SparkSqlSplitListener {
    constructor() {
        this._statementsContext = [];
        this.exitSingleStatement = (ctx) => {
            this._statementsContext.push(ctx);
        };
        this.enterSingleStatement = (ctx) => { };
    }
    get statementsContext() {
        return this._statementsContext;
    }
}
