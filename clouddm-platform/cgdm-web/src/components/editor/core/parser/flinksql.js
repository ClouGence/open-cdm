import { FlinkSqlLexer } from '../lib/flinksql/FlinkSqlLexer';
import { FlinkSqlParser, } from '../lib/flinksql/FlinkSqlParser';
import { SyntaxContextType } from './common/basic-parser-types';
import BasicParser from './common/basicParser';
export default class FlinkSQL extends BasicParser {
    constructor() {
        super(...arguments);
        this.preferredRules = new Set([
            FlinkSqlParser.RULE_catalogPath,
            FlinkSqlParser.RULE_databasePath,
            FlinkSqlParser.RULE_databasePathCreate,
            FlinkSqlParser.RULE_tablePath,
            FlinkSqlParser.RULE_tablePathCreate,
            FlinkSqlParser.RULE_viewPath,
            FlinkSqlParser.RULE_viewPathCreate,
            FlinkSqlParser.RULE_functionName,
            FlinkSqlParser.RULE_functionNameCreate,
            FlinkSqlParser.RULE_columnName,
            FlinkSqlParser.RULE_columnNameCreate,
        ]);
    }
    createLexerFormCharStream(charStreams) {
        const lexer = new FlinkSqlLexer(charStreams);
        return lexer;
    }
    createParserFromTokenStream(tokenStream) {
        const parser = new FlinkSqlParser(tokenStream);
        return parser;
    }
    get splitListener() {
        return new FlinkSqlSplitListener();
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
                case FlinkSqlParser.RULE_catalogPath: {
                    syntaxContextType = SyntaxContextType.CATALOG;
                    break;
                }
                case FlinkSqlParser.RULE_databasePath: {
                    syntaxContextType = SyntaxContextType.DATABASE;
                    break;
                }
                case FlinkSqlParser.RULE_databasePathCreate: {
                    syntaxContextType = SyntaxContextType.DATABASE_CREATE;
                    break;
                }
                case FlinkSqlParser.RULE_tablePath: {
                    syntaxContextType = SyntaxContextType.TABLE;
                    break;
                }
                case FlinkSqlParser.RULE_tablePathCreate: {
                    syntaxContextType = SyntaxContextType.TABLE_CREATE;
                    break;
                }
                case FlinkSqlParser.RULE_viewPath: {
                    syntaxContextType = SyntaxContextType.VIEW;
                    break;
                }
                case FlinkSqlParser.RULE_viewPathCreate: {
                    syntaxContextType = SyntaxContextType.VIEW_CREATE;
                    break;
                }
                case FlinkSqlParser.RULE_functionName: {
                    syntaxContextType = SyntaxContextType.FUNCTION;
                    break;
                }
                case FlinkSqlParser.RULE_functionNameCreate: {
                    syntaxContextType = SyntaxContextType.FUNCTION_CREATE;
                    break;
                }
                case FlinkSqlParser.RULE_columnName: {
                    syntaxContextType = SyntaxContextType.COLUMN;
                    break;
                }
                case FlinkSqlParser.RULE_columnNameCreate: {
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
export class FlinkSqlSplitListener {
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
