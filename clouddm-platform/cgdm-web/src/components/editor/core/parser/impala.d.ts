import { Token } from 'antlr4ts';
import { CandidatesCollection } from 'antlr4-c3';
import { ImpalaSqlLexer } from '../lib/impala/ImpalaSqlLexer';
import { ImpalaSqlParser, ProgramContext, SingleStatementContext } from '../lib/impala/ImpalaSqlParser';
import BasicParser from './common/basicParser';
import { ImpalaSqlParserListener } from '../lib/impala/ImpalaSqlParserListener';
import { Suggestions } from './common/basic-parser-types';
export default class ImpalaSQL extends BasicParser<ImpalaSqlLexer, ProgramContext, ImpalaSqlParser> {
    protected createLexerFormCharStream(charStreams: any): ImpalaSqlLexer;
    protected createParserFromTokenStream(tokenStream: any): ImpalaSqlParser;
    protected preferredRules: Set<number>;
    protected get splitListener(): ImpalaSqlSplitListener;
    protected processCandidates(candidates: CandidatesCollection, allTokens: Token[], caretTokenIndex: number, tokenIndexOffset: number): Suggestions<Token>;
}
export declare class ImpalaSqlSplitListener implements ImpalaSqlParserListener {
    private _statementContext;
    exitSingleStatement: (ctx: SingleStatementContext) => void;
    enterSingleStatement: (ctx: SingleStatementContext) => void;
    get statementsContext(): SingleStatementContext[];
}
