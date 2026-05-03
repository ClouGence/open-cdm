import { Token } from 'antlr4ts';
import { CandidatesCollection } from 'antlr4-c3';
import { TrinoSqlLexer } from '../lib/trinosql/TrinoSqlLexer';
import { TrinoSqlParser, ProgramContext, SingleStatementContext } from '../lib/trinosql/TrinoSqlParser';
import { TrinoSqlListener } from '../lib/trinosql/TrinoSqlListener';
import BasicParser from './common/basicParser';
import { Suggestions } from './common/basic-parser-types';
export default class TrinoSQL extends BasicParser<TrinoSqlLexer, ProgramContext, TrinoSqlParser> {
    protected createLexerFormCharStream(charStreams: any): TrinoSqlLexer;
    protected createParserFromTokenStream(tokenStream: any): TrinoSqlParser;
    protected get splitListener(): TrinoSqlSplitListener;
    protected preferredRules: Set<number>;
    protected processCandidates(candidates: CandidatesCollection, allTokens: Token[], caretTokenIndex: number, tokenIndexOffset: number): Suggestions<Token>;
}
export declare class TrinoSqlSplitListener implements TrinoSqlListener {
    private _statementsContext;
    exitSingleStatement: (ctx: SingleStatementContext) => void;
    get statementsContext(): SingleStatementContext[];
}
