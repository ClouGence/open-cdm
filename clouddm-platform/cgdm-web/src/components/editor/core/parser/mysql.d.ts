import { Token } from 'antlr4ts';
import { CandidatesCollection } from 'antlr4-c3';
import { MySqlLexer } from '../lib/mysql/MySqlLexer';
import { MySqlParser, ProgramContext, SingleStatementContext } from '../lib/mysql/MySqlParser';
import BasicParser from './common/basicParser';
import { Suggestions } from './common/basic-parser-types';
import { MySqlParserListener } from '../lib/mysql/MySqlParserListener';
export default class MySQL extends BasicParser<MySqlLexer, ProgramContext, MySqlParser> {
    protected createLexerFormCharStream(charStreams: any): MySqlLexer;
    protected createParserFromTokenStream(tokenStream: any): MySqlParser;
    protected preferredRules: Set<number>;
    protected get splitListener(): MysqlSplitListener;
    protected processCandidates(candidates: CandidatesCollection, allTokens: Token[], caretTokenIndex: number, tokenIndexOffset: number): Suggestions<Token>;
}
export declare class MysqlSplitListener implements MySqlParserListener {
    private _statementsContext;
    exitSingleStatement: (ctx: SingleStatementContext) => void;
    enterSingleStatement: (ctx: SingleStatementContext) => void;
    get statementsContext(): SingleStatementContext[];
}
