import { Token } from 'antlr4ts';
import { CandidatesCollection } from 'antlr4-c3';
import { FlinkSqlLexer } from '../lib/flinksql/FlinkSqlLexer';
import { FlinkSqlParser, ProgramContext, SingleStatementContext } from '../lib/flinksql/FlinkSqlParser';
import { FlinkSqlParserListener } from '../lib/flinksql/FlinkSqlParserListener';
import { Suggestions } from './common/basic-parser-types';
import BasicParser from './common/basicParser';
export default class FlinkSQL extends BasicParser<FlinkSqlLexer, ProgramContext, FlinkSqlParser> {
    protected createLexerFormCharStream(charStreams: any): FlinkSqlLexer;
    protected createParserFromTokenStream(tokenStream: any): FlinkSqlParser;
    protected preferredRules: Set<number>;
    protected get splitListener(): FlinkSqlSplitListener;
    protected processCandidates(candidates: CandidatesCollection, allTokens: Token[], caretTokenIndex: number, tokenIndexOffset: number): Suggestions<Token>;
}
export declare class FlinkSqlSplitListener implements FlinkSqlParserListener {
    private _statementsContext;
    exitSingleStatement: (ctx: SingleStatementContext) => void;
    enterSingleStatement: (ctx: SingleStatementContext) => void;
    get statementsContext(): SingleStatementContext[];
}
