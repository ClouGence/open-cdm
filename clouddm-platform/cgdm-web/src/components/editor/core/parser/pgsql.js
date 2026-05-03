import { PostgreSQLLexer } from '../lib/pgsql/PostgreSQLLexer';
import { PostgreSQLParser } from '../lib/pgsql/PostgreSQLParser';
import BasicParser from './common/basicParser';
import { SyntaxContextType } from './common/basic-parser-types';
export default class PostgresSQL extends BasicParser {
    constructor() {
        super(...arguments);
        this.preferredRules = new Set([
            PostgreSQLParser.RULE_table_name_create,
            PostgreSQLParser.RULE_table_name,
            PostgreSQLParser.RULE_function_name,
            PostgreSQLParser.RULE_function_name_create,
            PostgreSQLParser.RULE_schema_name_create,
            PostgreSQLParser.RULE_schema_name,
            PostgreSQLParser.RULE_view_name_create,
            PostgreSQLParser.RULE_view_name,
            PostgreSQLParser.RULE_database_name_create,
            PostgreSQLParser.RULE_database_name,
            PostgreSQLParser.RULE_procedure_name_create,
            PostgreSQLParser.RULE_procedure_name,
            PostgreSQLParser.RULE_column_name_create,
            PostgreSQLParser.RULE_column_name, // column name
        ]);
    }
    createLexerFormCharStream(charStreams) {
        const lexer = new PostgreSQLLexer(charStreams);
        return lexer;
    }
    createParserFromTokenStream(tokenStream) {
        return new PostgreSQLParser(tokenStream);
    }
    get splitListener() {
        return new PgSqlSplitListener();
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
                case PostgreSQLParser.RULE_table_name_create: {
                    syntaxContextType = SyntaxContextType.TABLE_CREATE;
                    break;
                }
                case PostgreSQLParser.RULE_table_name: {
                    syntaxContextType = SyntaxContextType.TABLE;
                    break;
                }
                case PostgreSQLParser.RULE_function_name_create: {
                    syntaxContextType = SyntaxContextType.FUNCTION_CREATE;
                    break;
                }
                case PostgreSQLParser.RULE_function_name: {
                    syntaxContextType = SyntaxContextType.FUNCTION;
                    break;
                }
                case PostgreSQLParser.RULE_schema_name_create: {
                    syntaxContextType = SyntaxContextType.DATABASE_CREATE;
                    break;
                }
                case PostgreSQLParser.RULE_schema_name: {
                    syntaxContextType = SyntaxContextType.DATABASE;
                    break;
                }
                case PostgreSQLParser.RULE_view_name_create: {
                    syntaxContextType = SyntaxContextType.VIEW_CREATE;
                    break;
                }
                case PostgreSQLParser.RULE_view_name: {
                    syntaxContextType = SyntaxContextType.VIEW;
                    break;
                }
                case PostgreSQLParser.RULE_database_name_create: {
                    syntaxContextType = SyntaxContextType.DATABASE_CREATE;
                    break;
                }
                case PostgreSQLParser.RULE_database_name: {
                    syntaxContextType = SyntaxContextType.DATABASE;
                    break;
                }
                case PostgreSQLParser.RULE_procedure_name_create: {
                    syntaxContextType = SyntaxContextType.PROCEDURE_CREATE;
                    break;
                }
                case PostgreSQLParser.RULE_procedure_name: {
                    syntaxContextType = SyntaxContextType.PROCEDURE;
                    break;
                }
                case PostgreSQLParser.RULE_column_name_create: {
                    syntaxContextType = SyntaxContextType.COLUMN_CREATE;
                    break;
                }
                case PostgreSQLParser.RULE_column_name: {
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
export class PgSqlSplitListener {
    constructor() {
        this._statementsContext = [];
        this.exitSingleStmt = (ctx) => {
            this._statementsContext.push(ctx);
        };
        this.enterSingleStmt = (ctx) => { };
    }
    get statementsContext() {
        return this._statementsContext;
    }
}
