import { HiveSqlLexer } from '../lib/hive/HiveSqlLexer';
import { HiveSqlParser } from '../lib/hive/HiveSqlParser';
import BasicParser from './common/basicParser';
import { SyntaxContextType } from './common/basic-parser-types';
export default class HiveSQL extends BasicParser {
    constructor() {
        super(...arguments);
        this.preferredRules = new Set([
            HiveSqlParser.RULE_dbSchemaName,
            HiveSqlParser.RULE_dbSchemaNameCreate,
            HiveSqlParser.RULE_tableName,
            HiveSqlParser.RULE_tableNameCreate,
            HiveSqlParser.RULE_viewName,
            HiveSqlParser.RULE_viewNameCreate,
            HiveSqlParser.RULE_functionNameForDDL,
            HiveSqlParser.RULE_functionNameForInvoke,
            HiveSqlParser.RULE_functionNameCreate,
            HiveSqlParser.RULE_columnName,
            HiveSqlParser.RULE_columnNameCreate,
        ]);
    }
    createLexerFormCharStream(charStreams) {
        const lexer = new HiveSqlLexer(charStreams);
        return lexer;
    }
    createParserFromTokenStream(tokenStream) {
        return new HiveSqlParser(tokenStream);
    }
    get splitListener() {
        return new HiveSqlSplitListener();
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
                case HiveSqlParser.RULE_dbSchemaName: {
                    syntaxContextType = SyntaxContextType.DATABASE;
                    break;
                }
                case HiveSqlParser.RULE_dbSchemaNameCreate: {
                    syntaxContextType = SyntaxContextType.DATABASE_CREATE;
                    break;
                }
                case HiveSqlParser.RULE_tableName: {
                    syntaxContextType = SyntaxContextType.TABLE;
                    break;
                }
                case HiveSqlParser.RULE_tableNameCreate: {
                    syntaxContextType = SyntaxContextType.TABLE_CREATE;
                    break;
                }
                case HiveSqlParser.RULE_viewName: {
                    syntaxContextType = SyntaxContextType.VIEW;
                    break;
                }
                case HiveSqlParser.RULE_viewNameCreate: {
                    syntaxContextType = SyntaxContextType.VIEW_CREATE;
                    break;
                }
                case HiveSqlParser.RULE_functionNameForDDL:
                case HiveSqlParser.RULE_functionNameForInvoke: {
                    syntaxContextType = SyntaxContextType.FUNCTION;
                    break;
                }
                case HiveSqlParser.RULE_functionNameCreate: {
                    syntaxContextType = SyntaxContextType.FUNCTION_CREATE;
                    break;
                }
                case HiveSqlParser.RULE_columnName: {
                    syntaxContextType = SyntaxContextType.COLUMN;
                    break;
                }
                case HiveSqlParser.RULE_columnNameCreate: {
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
export class HiveSqlSplitListener {
    constructor() {
        this._statementContext = [];
        this.exitStatement = (ctx) => {
            this._statementContext.push(ctx);
        };
        this.enterStatement = (ctx) => { };
    }
    get statementsContext() {
        return this._statementContext;
    }
}
