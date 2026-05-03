import { Token } from 'antlr4ts';
import { CandidatesCollection } from 'antlr4-c3';
import { HiveSqlLexer } from '../lib/hive/HiveSqlLexer';
import { HiveSqlParser, ProgramContext, StatementContext } from '../lib/hive/HiveSqlParser';
import BasicParser from './common/basicParser';
import { HiveSqlParserListener } from '../lib/hive/HiveSqlParserListener';
import { Suggestions } from './common/basic-parser-types';
export default class HiveSQL extends BasicParser<HiveSqlLexer, ProgramContext, HiveSqlParser> {
    protected createLexerFormCharStream(charStreams: any): HiveSqlLexer;
    protected createParserFromTokenStream(tokenStream: any): HiveSqlParser;
    protected preferredRules: Set<number>;
    protected get splitListener(): HiveSqlSplitListener;
    protected processCandidates(candidates: CandidatesCollection, allTokens: Token[], caretTokenIndex: number, tokenIndexOffset: number): Suggestions<Token>;
}
export declare class HiveSqlSplitListener implements HiveSqlParserListener {
    private _statementContext;
    exitStatement: (ctx: StatementContext) => void;
    enterStatement: (ctx: StatementContext) => void;
    get statementsContext(): StatementContext[];
}
