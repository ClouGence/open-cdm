import { Token } from 'antlr4ts';
import { CandidatesCollection } from 'antlr4-c3';
import { PlSqlLexer } from '../lib/plsql/PlSqlLexer';
import { PlSqlParser, ProgramContext } from '../lib/plsql/PlSqlParser';
import BasicParser from './common/basicParser';
import { Suggestions } from './common/basic-parser-types';
import { PlSqlParserListener } from '../lib/plsql/PlSqlParserListener';
import { Sql_statementContext, Tableview_nameContext } from '../lib/plsql/PlSqlParser';
export default class PLSQL extends BasicParser<PlSqlLexer, ProgramContext, PlSqlParser> {
    protected createLexerFormCharStream(charStreams: any): PlSqlLexer;
    protected createParserFromTokenStream(tokenStream: any): PlSqlParser;
    protected preferredRules: Set<number>;
    protected get splitListener(): plSqlSplitListener;
    protected processCandidates(candidates: CandidatesCollection, allTokens: Token[], caretTokenIndex: number, tokenIndexOffset: number): Suggestions<Token>;
}
export declare class plSqlSplitListener implements PlSqlParserListener {
    private _statementsContext;
    enterSql_statement: (ctx: Sql_statementContext) => void;
    get statementsContext(): Sql_statementContext[];
    private _tableNamesContext;
    enterTableview_name: (ctx: Tableview_nameContext) => void;
    get tableNamesContext(): Tableview_nameContext[];
}
