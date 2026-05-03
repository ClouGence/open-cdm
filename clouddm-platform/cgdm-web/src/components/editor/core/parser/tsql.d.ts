import { Token } from 'antlr4ts';
import { CandidatesCollection } from 'antlr4-c3';
import BasicParser from './common/basicParser';
import { Suggestions } from './common/basic-parser-types';
import { TSqlLexer } from "../lib/tsql/TSqlLexer";
import { TSqlParserListener } from "../lib/tsql/TSqlParserListener";
import { ProgramContext, Sql_clausesContext, TSqlParser } from "../lib/tsql/TSqlParser";
export default class TSql extends BasicParser<TSqlLexer, ProgramContext, TSqlParser> {
    protected createLexerFormCharStream(charStreams: any): TSqlLexer;
    protected createParserFromTokenStream(tokenStream: any): TSqlParser;
    protected preferredRules: Set<number>;
    protected get splitListener(): TSqlSplitListener;
    protected processCandidates(candidates: CandidatesCollection, allTokens: Token[], caretTokenIndex: number, tokenIndexOffset: number): Suggestions<Token>;
}
export declare class TSqlSplitListener implements TSqlParserListener {
    private _statementsContext;
    enterSql_clauses: (ctx: Sql_clausesContext) => void;
    get statementsContext(): Sql_clausesContext[];
}
