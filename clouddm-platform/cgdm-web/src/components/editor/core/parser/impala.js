import { ImpalaSqlLexer } from '../lib/impala/ImpalaSqlLexer';
import { ImpalaSqlParser, } from '../lib/impala/ImpalaSqlParser';
import BasicParser from './common/basicParser';
import { SyntaxContextType } from './common/basic-parser-types';
export default class ImpalaSQL extends BasicParser {
    constructor() {
        super(...arguments);
        this.preferredRules = new Set([
            ImpalaSqlParser.RULE_functionNameCreate,
            ImpalaSqlParser.RULE_tableNameCreate,
            ImpalaSqlParser.RULE_viewNameCreate,
            ImpalaSqlParser.RULE_databaseNameCreate,
            ImpalaSqlParser.RULE_columnNamePathCreate,
            ImpalaSqlParser.RULE_tableNamePath,
            ImpalaSqlParser.RULE_functionNamePath,
            ImpalaSqlParser.RULE_viewNamePath,
            ImpalaSqlParser.RULE_databaseNamePath,
            ImpalaSqlParser.RULE_columnNamePath,
        ]);
    }
    createLexerFormCharStream(charStreams) {
        const lexer = new ImpalaSqlLexer(charStreams);
        return lexer;
    }
    createParserFromTokenStream(tokenStream) {
        return new ImpalaSqlParser(tokenStream);
    }
    get splitListener() {
        return new ImpalaSqlSplitListener();
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
                case ImpalaSqlParser.RULE_functionNameCreate: {
                    syntaxContextType = SyntaxContextType.FUNCTION_CREATE;
                    break;
                }
                case ImpalaSqlParser.RULE_tableNameCreate: {
                    syntaxContextType = SyntaxContextType.TABLE_CREATE;
                    break;
                }
                case ImpalaSqlParser.RULE_databaseNameCreate: {
                    syntaxContextType = SyntaxContextType.DATABASE_CREATE;
                    break;
                }
                case ImpalaSqlParser.RULE_viewNameCreate: {
                    syntaxContextType = SyntaxContextType.VIEW_CREATE;
                    break;
                }
                case ImpalaSqlParser.RULE_columnNamePathCreate: {
                    syntaxContextType = SyntaxContextType.COLUMN_CREATE;
                    break;
                }
                case ImpalaSqlParser.RULE_databaseNamePath: {
                    syntaxContextType = SyntaxContextType.DATABASE;
                    break;
                }
                case ImpalaSqlParser.RULE_tableNamePath: {
                    syntaxContextType = SyntaxContextType.TABLE;
                    break;
                }
                case ImpalaSqlParser.RULE_viewNamePath: {
                    syntaxContextType = SyntaxContextType.VIEW;
                    break;
                }
                case ImpalaSqlParser.RULE_functionNamePath: {
                    syntaxContextType = SyntaxContextType.FUNCTION;
                    break;
                }
                case ImpalaSqlParser.RULE_columnNamePath: {
                    syntaxContextType = SyntaxContextType.COLUMN;
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
export class ImpalaSqlSplitListener {
    constructor() {
        this._statementContext = [];
        this.exitSingleStatement = (ctx) => {
            this._statementContext.push(ctx);
        };
        this.enterSingleStatement = (ctx) => { };
    }
    get statementsContext() {
        return this._statementContext;
    }
}
