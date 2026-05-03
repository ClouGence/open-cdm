import { Token } from 'antlr4ts';
import { CandidatesCollection } from 'antlr4-c3';
import { PostgreSQLLexer } from '../lib/pgsql/PostgreSQLLexer';
import { PostgreSQLParser, ProgramContext, SingleStmtContext } from '../lib/pgsql/PostgreSQLParser';
import BasicParser from './common/basicParser';
import { PostgreSQLParserListener } from '../lib/pgsql/PostgreSQLParserListener';
import { Suggestions } from './common/basic-parser-types';
export default class PostgresSQL extends BasicParser<PostgreSQLLexer, ProgramContext, PostgreSQLParser> {
    protected createLexerFormCharStream(charStreams: any): PostgreSQLLexer;
    protected createParserFromTokenStream(tokenStream: any): PostgreSQLParser;
    protected preferredRules: Set<number>;
    protected get splitListener(): PgSqlSplitListener;
    protected processCandidates(candidates: CandidatesCollection, allTokens: Token[], caretTokenIndex: number, tokenIndexOffset: number): Suggestions<Token>;
}
export declare class PgSqlSplitListener implements PostgreSQLParserListener {
    private _statementsContext;
    exitSingleStmt: (ctx: SingleStmtContext) => void;
    enterSingleStmt: (ctx: SingleStmtContext) => void;
    get statementsContext(): SingleStmtContext[];
}
