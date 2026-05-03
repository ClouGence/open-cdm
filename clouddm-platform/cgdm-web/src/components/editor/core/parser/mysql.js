import { MySqlLexer } from '../lib/mysql/MySqlLexer';
import { MySqlParser } from '../lib/mysql/MySqlParser';
import BasicParser from './common/basicParser';
import { SyntaxContextType } from './common/basic-parser-types';
export default class MySQL extends BasicParser {
    constructor() {
        super(...arguments);
        this.preferredRules = new Set([
            MySqlParser.RULE_databaseName,
            MySqlParser.RULE_databaseNameCreate,
            MySqlParser.RULE_tableName,
            MySqlParser.RULE_tableNameCreate,
            MySqlParser.RULE_viewName,
            MySqlParser.RULE_viewNameCreate,
            MySqlParser.RULE_functionName,
            MySqlParser.RULE_functionNameCreate,
            MySqlParser.RULE_columnName,
            MySqlParser.RULE_columnNameCreate,
        ]);
    }
    createLexerFormCharStream(charStreams) {
        const lexer = new MySqlLexer(charStreams);
        return lexer;
    }
    createParserFromTokenStream(tokenStream) {
        return new MySqlParser(tokenStream);
    }
    get splitListener() {
        return new MysqlSplitListener();
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
                case MySqlParser.RULE_databaseName: {
                    syntaxContextType = SyntaxContextType.DATABASE;
                    break;
                }
                case MySqlParser.RULE_databaseNameCreate: {
                    syntaxContextType = SyntaxContextType.DATABASE_CREATE;
                    break;
                }
                case MySqlParser.RULE_tableName: {
                    syntaxContextType = SyntaxContextType.TABLE;
                    break;
                }
                case MySqlParser.RULE_tableNameCreate: {
                    syntaxContextType = SyntaxContextType.TABLE_CREATE;
                    break;
                }
                case MySqlParser.RULE_viewName: {
                    syntaxContextType = SyntaxContextType.VIEW;
                    break;
                }
                case MySqlParser.RULE_viewNameCreate: {
                    syntaxContextType = SyntaxContextType.VIEW_CREATE;
                    break;
                }
                case MySqlParser.RULE_functionName: {
                    syntaxContextType = SyntaxContextType.FUNCTION;
                    break;
                }
                case MySqlParser.RULE_functionNameCreate: {
                    syntaxContextType = SyntaxContextType.FUNCTION_CREATE;
                    break;
                }
                case MySqlParser.RULE_columnName: {
                    syntaxContextType = SyntaxContextType.COLUMN;
                    break;
                }
                case MySqlParser.RULE_columnNameCreate: {
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
export class MysqlSplitListener {
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
