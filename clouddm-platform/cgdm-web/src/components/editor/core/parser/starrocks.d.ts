import { Token } from 'antlr4ts';
import { CandidatesCollection } from 'antlr4-c3';
import BasicParser from './common/basicParser';
import { Suggestions } from './common/basic-parser-types';
import { StarRocksSqlLexer } from '../lib/starrocks/StarRocksSqlLexer';
import { QualifiedNameContext, StarRocksSqlParser, StatementContext, ProgramContext } from '../lib/starrocks/StarRocksSqlParser';
import { StarRocksSqlParserListener } from '../lib/starrocks/StarRocksSqlParserListener';
export default class StarRocks extends BasicParser<StarRocksSqlLexer, ProgramContext, StarRocksSqlParser> {
    protected createLexerFormCharStream(charStreams: any): StarRocksSqlLexer;
    protected createParserFromTokenStream(tokenStream: any): StarRocksSqlParser;
    protected preferredRules: Set<number>;
    protected get splitListener(): starRocksSqlSplitListener;
    protected processCandidates(candidates: CandidatesCollection, allTokens: Token[], caretTokenIndex: number, tokenIndexOffset: number): Suggestions<Token>;
}
export declare class starRocksSqlSplitListener implements StarRocksSqlParserListener {
    private _statementsContext;
    enterStatement: (ctx: StatementContext) => void;
    get statementsContext(): StatementContext[];
    private _tableNamesContext;
    enterQualifiedName: (ctx: QualifiedNameContext) => void;
    get tableNamesContext(): QualifiedNameContext[];
}
