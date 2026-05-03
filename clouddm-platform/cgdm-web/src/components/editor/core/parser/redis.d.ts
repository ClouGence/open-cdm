import { Token } from 'antlr4ts';
import { CandidatesCollection } from 'antlr4-c3';
import BasicParser from './common/basicParser';
import { Suggestions } from './common/basic-parser-types';
import { RedisSqlParserListener } from '../lib/redis/RedisSqlParserListener';
import { RedisSqlLexer } from "../lib/redis/RedisSqlLexer";
import { RedisSqlParser, ProgramContext, SqlStatementContext, KeyNameContext } from '../lib/redis/RedisSqlParser';
export default class Redis extends BasicParser<RedisSqlLexer, ProgramContext, RedisSqlParser> {
    protected createLexerFormCharStream(charStreams: any): RedisSqlLexer;
    protected createParserFromTokenStream(tokenStream: any): RedisSqlParser;
    protected preferredRules: Set<number>;
    protected get splitListener(): redisSqlSplitListener;
    protected processCandidates(candidates: CandidatesCollection, allTokens: Token[], caretTokenIndex: number, tokenIndexOffset: number): Suggestions<Token>;
}
export declare class redisSqlSplitListener implements RedisSqlParserListener {
    private _statementsContext;
    enterSqlStatement: (ctx: SqlStatementContext) => void;
    get statementsContext(): SqlStatementContext[];
    private _tableNamesContext;
    enterKeyName: (ctx: KeyNameContext) => void;
    get tableNamesContext(): KeyNameContext[];
}
