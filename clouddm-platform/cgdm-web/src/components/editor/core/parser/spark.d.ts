import { Token } from 'antlr4ts';
import { CandidatesCollection } from 'antlr4-c3';
import { SparkSqlLexer } from '../lib/spark/SparkSqlLexer';
import { SparkSqlParser, ProgramContext, SingleStatementContext } from '../lib/spark/SparkSqlParser';
import BasicParser from './common/basicParser';
import { Suggestions } from './common/basic-parser-types';
import { SparkSqlParserListener } from '../lib/spark/SparkSqlParserListener';
export default class SparkSQL extends BasicParser<SparkSqlLexer, ProgramContext, SparkSqlParser> {
    protected createLexerFormCharStream(charStreams: any): SparkSqlLexer;
    protected createParserFromTokenStream(tokenStream: any): SparkSqlParser;
    protected preferredRules: Set<number>;
    protected get splitListener(): SparkSqlSplitListener;
    protected processCandidates(candidates: CandidatesCollection, allTokens: Token[], caretTokenIndex: number, tokenIndexOffset: number): Suggestions<Token>;
}
export declare class SparkSqlSplitListener implements SparkSqlParserListener {
    private _statementsContext;
    exitSingleStatement: (ctx: SingleStatementContext) => void;
    enterSingleStatement: (ctx: SingleStatementContext) => void;
    get statementsContext(): SingleStatementContext[];
}
