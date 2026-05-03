import { ParseTreeVisitor } from "antlr4ts/tree/ParseTreeVisitor";
import { CmdClientPauseContext } from "./RedisSqlParser";
import { CmdClientKillContext } from "./RedisSqlParser";
import { CmdClientGetnameContext } from "./RedisSqlParser";
import { CmdClientSetnameContext } from "./RedisSqlParser";
import { CmdClientListContext } from "./RedisSqlParser";
import { CmdCmdInfoContext } from "./RedisSqlParser";
import { CmdCmdGetkeysContext } from "./RedisSqlParser";
import { CmdCmdCountContext } from "./RedisSqlParser";
import { CmdConfigGetContext } from "./RedisSqlParser";
import { CmdConfigSetContext } from "./RedisSqlParser";
import { CmdConfigResetContext } from "./RedisSqlParser";
import { CmdConfigRewriteContext } from "./RedisSqlParser";
import { CmdIncrContext } from "./RedisSqlParser";
import { CmdIncrbyContext } from "./RedisSqlParser";
import { CmdIncrbyFloatContext } from "./RedisSqlParser";
import { ProgramContext } from "./RedisSqlParser";
import { SqlStatementsContext } from "./RedisSqlParser";
import { EmptyStatement_Context } from "./RedisSqlParser";
import { KeyNameContext } from "./RedisSqlParser";
import { FieldNameContext } from "./RedisSqlParser";
import { StringValueContext } from "./RedisSqlParser";
import { IntValueContext } from "./RedisSqlParser";
import { FloatValueContext } from "./RedisSqlParser";
import { PatternContext } from "./RedisSqlParser";
import { CursorContext } from "./RedisSqlParser";
import { MinContext } from "./RedisSqlParser";
import { MaxContext } from "./RedisSqlParser";
import { MultiStringContext } from "./RedisSqlParser";
import { CoutContext } from "./RedisSqlParser";
import { StartContext } from "./RedisSqlParser";
import { EndContext } from "./RedisSqlParser";
import { PortContext } from "./RedisSqlParser";
import { KeyAndStringContext } from "./RedisSqlParser";
import { IntAndStringContext } from "./RedisSqlParser";
import { InfoOptContext } from "./RedisSqlParser";
import { PatternOptContext } from "./RedisSqlParser";
import { KeyOptContext } from "./RedisSqlParser";
import { IdletimeOptContext } from "./RedisSqlParser";
import { FreqOptContext } from "./RedisSqlParser";
import { LimitOptContext } from "./RedisSqlParser";
import { SortOptContext } from "./RedisSqlParser";
import { GetsetOptContext } from "./RedisSqlParser";
import { LrOptContext } from "./RedisSqlParser";
import { RankOptContext } from "./RedisSqlParser";
import { CountOptContext } from "./RedisSqlParser";
import { MaxlenOptContext } from "./RedisSqlParser";
import { CmdCopyContext } from "./RedisSqlParser";
import { CmdDelContext } from "./RedisSqlParser";
import { CmdDumpContext } from "./RedisSqlParser";
import { CmdExistsContext } from "./RedisSqlParser";
import { CmdExpireContext } from "./RedisSqlParser";
import { CmdExpireatContext } from "./RedisSqlParser";
import { CmdExpireTimeContext } from "./RedisSqlParser";
import { CmdKeysContext } from "./RedisSqlParser";
import { CmdMoveContext } from "./RedisSqlParser";
import { CmdObjectContext } from "./RedisSqlParser";
import { CmdPersistContext } from "./RedisSqlParser";
import { CmdPexpireContext } from "./RedisSqlParser";
import { CmdPexpireatContext } from "./RedisSqlParser";
import { CmdPExpireTimeContext } from "./RedisSqlParser";
import { CmdTtlContext } from "./RedisSqlParser";
import { CmdPttlContext } from "./RedisSqlParser";
import { CmdRandomkeyContext } from "./RedisSqlParser";
import { CmdRenameContext } from "./RedisSqlParser";
import { CmdRenamenxContext } from "./RedisSqlParser";
import { CmdRestoreContext } from "./RedisSqlParser";
import { CmdScanContext } from "./RedisSqlParser";
import { CmdSortContext } from "./RedisSqlParser";
import { CmdSortroContext } from "./RedisSqlParser";
import { CmdTouchContext } from "./RedisSqlParser";
import { CmdTypeContext } from "./RedisSqlParser";
import { CmdUnlinkContext } from "./RedisSqlParser";
import { CmdWaitContext } from "./RedisSqlParser";
import { CmdAppendContext } from "./RedisSqlParser";
import { CmdDecrContext } from "./RedisSqlParser";
import { CmdDecrbyContext } from "./RedisSqlParser";
import { CmdGetContext } from "./RedisSqlParser";
import { CmdGetdelContext } from "./RedisSqlParser";
import { CmdGetexContext } from "./RedisSqlParser";
import { CmdGetrangeContext } from "./RedisSqlParser";
import { CmdGetsetContext } from "./RedisSqlParser";
import { CmdGetbitContext } from "./RedisSqlParser";
import { CmdIncContext } from "./RedisSqlParser";
import { CmdLcsContext } from "./RedisSqlParser";
import { CmdMgetContext } from "./RedisSqlParser";
import { CmdMsetContext } from "./RedisSqlParser";
import { CmdMsetnxContext } from "./RedisSqlParser";
import { CmdSetexContext } from "./RedisSqlParser";
import { CmdSetContext } from "./RedisSqlParser";
import { CmdSetnxContext } from "./RedisSqlParser";
import { CmdSetrangeContext } from "./RedisSqlParser";
import { CmdSetbitContext } from "./RedisSqlParser";
import { CmdStrlenContext } from "./RedisSqlParser";
import { CmdSubstrContext } from "./RedisSqlParser";
import { CmdHdelMgetContext } from "./RedisSqlParser";
import { CmdHexistsGetKeysStrlenContext } from "./RedisSqlParser";
import { CmdHgetallLenValsContext } from "./RedisSqlParser";
import { CmdHmsetHsetContext } from "./RedisSqlParser";
import { CmdHincrbyContext } from "./RedisSqlParser";
import { CmdHrandfieldContext } from "./RedisSqlParser";
import { CmdHscanContext } from "./RedisSqlParser";
import { CmdHsetnxContext } from "./RedisSqlParser";
import { CmdBlmoveContext } from "./RedisSqlParser";
import { CmdLmpopContext } from "./RedisSqlParser";
import { CmdBpopContext } from "./RedisSqlParser";
import { CmdBrpoplpushContext } from "./RedisSqlParser";
import { CmdLindexContext } from "./RedisSqlParser";
import { CmdLinsertContext } from "./RedisSqlParser";
import { CmdLlenContext } from "./RedisSqlParser";
import { CmdLmoveContext } from "./RedisSqlParser";
import { CmdPopContext } from "./RedisSqlParser";
import { CmdLposContext } from "./RedisSqlParser";
import { CmdPushContext } from "./RedisSqlParser";
import { CmdLrangeTrimContext } from "./RedisSqlParser";
import { CmdLremSetContext } from "./RedisSqlParser";
import { CmdRpoplpushContext } from "./RedisSqlParser";
import { CmdSaddContext } from "./RedisSqlParser";
import { CmdScardContext } from "./RedisSqlParser";
import { CmdSdiffContext } from "./RedisSqlParser";
import { CmdSdiffstoreContext } from "./RedisSqlParser";
import { CmdSintercardContext } from "./RedisSqlParser";
import { CmdSismemberContext } from "./RedisSqlParser";
import { CmdSmoveContext } from "./RedisSqlParser";
import { CmdSpopContext } from "./RedisSqlParser";
import { CmdSscanContext } from "./RedisSqlParser";
import { CmdBzmpopContext } from "./RedisSqlParser";
import { CmdBzpopmaxContext } from "./RedisSqlParser";
import { CmdZaddContext } from "./RedisSqlParser";
import { CmdZcardContext } from "./RedisSqlParser";
import { CmdZcountContext } from "./RedisSqlParser";
import { CmdZdiffContext } from "./RedisSqlParser";
import { CmdZdiffstoreContext } from "./RedisSqlParser";
import { CmdZincrbyContext } from "./RedisSqlParser";
import { CmdZinterContext } from "./RedisSqlParser";
import { CmdZintercardContext } from "./RedisSqlParser";
import { CmdZinterstoreContext } from "./RedisSqlParser";
import { CmdZmpopContext } from "./RedisSqlParser";
import { CmdZmscoreContext } from "./RedisSqlParser";
import { CmdZpopmaxContext } from "./RedisSqlParser";
import { CmdZrandmemberContext } from "./RedisSqlParser";
import { CmdZrangeContext } from "./RedisSqlParser";
import { CmdZrangebylexContext } from "./RedisSqlParser";
import { CmdZrangebyscoreContext } from "./RedisSqlParser";
import { CmdZrangestoreContext } from "./RedisSqlParser";
import { CmdZrankContext } from "./RedisSqlParser";
import { CmdZremrangebyrankContext } from "./RedisSqlParser";
import { CmdZrevrangeContext } from "./RedisSqlParser";
import { CmdZrevrangebylexContext } from "./RedisSqlParser";
import { CmdZscanContext } from "./RedisSqlParser";
import { CmdZunionContext } from "./RedisSqlParser";
import { CmdZunionstoreContext } from "./RedisSqlParser";
import { CmdScriptKillContext } from "./RedisSqlParser";
import { CmdScriptLoadContext } from "./RedisSqlParser";
import { CmdEvalContext } from "./RedisSqlParser";
import { CmdEvalshaContext } from "./RedisSqlParser";
import { CmdScriptExistsContext } from "./RedisSqlParser";
import { CmdScriptFlushContext } from "./RedisSqlParser";
import { CmdExecContext } from "./RedisSqlParser";
import { CmdWatchContext } from "./RedisSqlParser";
import { CmdDiscardContext } from "./RedisSqlParser";
import { CmdUnwatchContext } from "./RedisSqlParser";
import { CmdMultiContext } from "./RedisSqlParser";
import { CmdPfmergeContext } from "./RedisSqlParser";
import { CmdPfaddContext } from "./RedisSqlParser";
import { CmdPfcountContext } from "./RedisSqlParser";
import { CmdSsubscribeContext } from "./RedisSqlParser";
import { CmdUnsubscribeContext } from "./RedisSqlParser";
import { CmdPusubnumpatContext } from "./RedisSqlParser";
import { CmdPusubnumsubContext } from "./RedisSqlParser";
import { CmdPusubchannelsContext } from "./RedisSqlParser";
import { CmdPublishContext } from "./RedisSqlParser";
import { CmdAskingContext } from "./RedisSqlParser";
import { CmdReadonlyContext } from "./RedisSqlParser";
import { CmdReadwriteContext } from "./RedisSqlParser";
import { CmdAddDelSlotsContext } from "./RedisSqlParser";
import { CmdCountKeysInSlotContext } from "./RedisSqlParser";
import { CmdFailOverContext } from "./RedisSqlParser";
import { CmdForgetContext } from "./RedisSqlParser";
import { CmdGetKeysInSlotContext } from "./RedisSqlParser";
import { CmdClusterOrderContext } from "./RedisSqlParser";
import { CmdKeySlotContext } from "./RedisSqlParser";
import { CmdMeetContext } from "./RedisSqlParser";
import { CmdReplicaesSlaveContext } from "./RedisSqlParser";
import { CmdResetContext } from "./RedisSqlParser";
import { CmdSetSlotContext } from "./RedisSqlParser";
import { CmdFlushdbContext } from "./RedisSqlParser";
import { CmdEchoContext } from "./RedisSqlParser";
import { CmdSaveContext } from "./RedisSqlParser";
import { CmdSlowlogContext } from "./RedisSqlParser";
import { CmdLastsaveContext } from "./RedisSqlParser";
import { CmdConfigContext } from "./RedisSqlParser";
import { CmdClientContext } from "./RedisSqlParser";
import { CmdShutdownContext } from "./RedisSqlParser";
import { CmdSyncContext } from "./RedisSqlParser";
import { CmdRoleContext } from "./RedisSqlParser";
import { CmdMonitorContext } from "./RedisSqlParser";
import { CmdSlaveofContext } from "./RedisSqlParser";
import { CmdFlushallContext } from "./RedisSqlParser";
import { CmdSelectContext } from "./RedisSqlParser";
import { CmdPingContext } from "./RedisSqlParser";
import { CmdQuitContext } from "./RedisSqlParser";
import { CmdAuthContext } from "./RedisSqlParser";
import { CmdDbsizeContext } from "./RedisSqlParser";
import { CmdBgrewriteaofContext } from "./RedisSqlParser";
import { CmdTimeContext } from "./RedisSqlParser";
import { CmdInfoContext } from "./RedisSqlParser";
import { CmdBgsaveContext } from "./RedisSqlParser";
import { CmdCmdContext } from "./RedisSqlParser";
import { CmdCmdxContext } from "./RedisSqlParser";
import { CmdDebugContext } from "./RedisSqlParser";
import { SqlStatementContext } from "./RedisSqlParser";
/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by `RedisSqlParser`.
 *
 * @param <Result> The return type of the visit operation. Use `void` for
 * operations with no return type.
 */
export interface RedisSqlParserVisitor<Result> extends ParseTreeVisitor<Result> {
    /**
     * Visit a parse tree produced by the `cmdClientPause`
     * labeled alternative in `RedisSqlParser.cmdClient`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdClientPause?: (ctx: CmdClientPauseContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdClientKill`
     * labeled alternative in `RedisSqlParser.cmdClient`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdClientKill?: (ctx: CmdClientKillContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdClientGetname`
     * labeled alternative in `RedisSqlParser.cmdClient`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdClientGetname?: (ctx: CmdClientGetnameContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdClientSetname`
     * labeled alternative in `RedisSqlParser.cmdClient`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdClientSetname?: (ctx: CmdClientSetnameContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdClientList`
     * labeled alternative in `RedisSqlParser.cmdClient`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdClientList?: (ctx: CmdClientListContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdCmdInfo`
     * labeled alternative in `RedisSqlParser.cmdCmdx`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdCmdInfo?: (ctx: CmdCmdInfoContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdCmdGetkeys`
     * labeled alternative in `RedisSqlParser.cmdCmdx`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdCmdGetkeys?: (ctx: CmdCmdGetkeysContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdCmdCount`
     * labeled alternative in `RedisSqlParser.cmdCmdx`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdCmdCount?: (ctx: CmdCmdCountContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdConfigGet`
     * labeled alternative in `RedisSqlParser.cmdConfig`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdConfigGet?: (ctx: CmdConfigGetContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdConfigSet`
     * labeled alternative in `RedisSqlParser.cmdConfig`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdConfigSet?: (ctx: CmdConfigSetContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdConfigReset`
     * labeled alternative in `RedisSqlParser.cmdConfig`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdConfigReset?: (ctx: CmdConfigResetContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdConfigRewrite`
     * labeled alternative in `RedisSqlParser.cmdConfig`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdConfigRewrite?: (ctx: CmdConfigRewriteContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdIncr`
     * labeled alternative in `RedisSqlParser.cmdInc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdIncr?: (ctx: CmdIncrContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdIncrby`
     * labeled alternative in `RedisSqlParser.cmdInc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdIncrby?: (ctx: CmdIncrbyContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdIncrbyFloat`
     * labeled alternative in `RedisSqlParser.cmdInc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdIncrbyFloat?: (ctx: CmdIncrbyFloatContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.program`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitProgram?: (ctx: ProgramContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.sqlStatements`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSqlStatements?: (ctx: SqlStatementsContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.emptyStatement_`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitEmptyStatement_?: (ctx: EmptyStatement_Context) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.keyName`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitKeyName?: (ctx: KeyNameContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.fieldName`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFieldName?: (ctx: FieldNameContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.stringValue`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStringValue?: (ctx: StringValueContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.intValue`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIntValue?: (ctx: IntValueContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.floatValue`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFloatValue?: (ctx: FloatValueContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.pattern`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPattern?: (ctx: PatternContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cursor`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCursor?: (ctx: CursorContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.min`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMin?: (ctx: MinContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.max`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMax?: (ctx: MaxContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.multiString`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMultiString?: (ctx: MultiStringContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cout`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCout?: (ctx: CoutContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.start`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStart?: (ctx: StartContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.end`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitEnd?: (ctx: EndContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.port`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPort?: (ctx: PortContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.keyAndString`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitKeyAndString?: (ctx: KeyAndStringContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.intAndString`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIntAndString?: (ctx: IntAndStringContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.infoOpt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInfoOpt?: (ctx: InfoOptContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.patternOpt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPatternOpt?: (ctx: PatternOptContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.keyOpt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitKeyOpt?: (ctx: KeyOptContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.idletimeOpt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIdletimeOpt?: (ctx: IdletimeOptContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.freqOpt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFreqOpt?: (ctx: FreqOptContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.limitOpt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLimitOpt?: (ctx: LimitOptContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.sortOpt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSortOpt?: (ctx: SortOptContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.getsetOpt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGetsetOpt?: (ctx: GetsetOptContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.lrOpt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLrOpt?: (ctx: LrOptContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.rankOpt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRankOpt?: (ctx: RankOptContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.countOpt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCountOpt?: (ctx: CountOptContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.maxlenOpt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMaxlenOpt?: (ctx: MaxlenOptContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdCopy`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdCopy?: (ctx: CmdCopyContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdDel`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdDel?: (ctx: CmdDelContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdDump`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdDump?: (ctx: CmdDumpContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdExists`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdExists?: (ctx: CmdExistsContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdExpire`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdExpire?: (ctx: CmdExpireContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdExpireat`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdExpireat?: (ctx: CmdExpireatContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdExpireTime`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdExpireTime?: (ctx: CmdExpireTimeContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdKeys`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdKeys?: (ctx: CmdKeysContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdMove`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdMove?: (ctx: CmdMoveContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdObject`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdObject?: (ctx: CmdObjectContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdPersist`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPersist?: (ctx: CmdPersistContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdPexpire`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPexpire?: (ctx: CmdPexpireContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdPexpireat`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPexpireat?: (ctx: CmdPexpireatContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdPExpireTime`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPExpireTime?: (ctx: CmdPExpireTimeContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdTtl`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdTtl?: (ctx: CmdTtlContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdPttl`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPttl?: (ctx: CmdPttlContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdRandomkey`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdRandomkey?: (ctx: CmdRandomkeyContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdRename`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdRename?: (ctx: CmdRenameContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdRenamenx`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdRenamenx?: (ctx: CmdRenamenxContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdRestore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdRestore?: (ctx: CmdRestoreContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdScan`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdScan?: (ctx: CmdScanContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdSort`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSort?: (ctx: CmdSortContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdSortro`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSortro?: (ctx: CmdSortroContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdTouch`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdTouch?: (ctx: CmdTouchContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdType`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdType?: (ctx: CmdTypeContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdUnlink`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdUnlink?: (ctx: CmdUnlinkContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdWait`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdWait?: (ctx: CmdWaitContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdAppend`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdAppend?: (ctx: CmdAppendContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdDecr`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdDecr?: (ctx: CmdDecrContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdDecrby`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdDecrby?: (ctx: CmdDecrbyContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdGet`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdGet?: (ctx: CmdGetContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdGetdel`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdGetdel?: (ctx: CmdGetdelContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdGetex`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdGetex?: (ctx: CmdGetexContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdGetrange`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdGetrange?: (ctx: CmdGetrangeContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdGetset`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdGetset?: (ctx: CmdGetsetContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdGetbit`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdGetbit?: (ctx: CmdGetbitContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdInc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdInc?: (ctx: CmdIncContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdLcs`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdLcs?: (ctx: CmdLcsContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdMget`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdMget?: (ctx: CmdMgetContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdMset`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdMset?: (ctx: CmdMsetContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdMsetnx`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdMsetnx?: (ctx: CmdMsetnxContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdSetex`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSetex?: (ctx: CmdSetexContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdSet`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSet?: (ctx: CmdSetContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdSetnx`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSetnx?: (ctx: CmdSetnxContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdSetrange`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSetrange?: (ctx: CmdSetrangeContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdSetbit`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSetbit?: (ctx: CmdSetbitContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdStrlen`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdStrlen?: (ctx: CmdStrlenContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdSubstr`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSubstr?: (ctx: CmdSubstrContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdHdelMget`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdHdelMget?: (ctx: CmdHdelMgetContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdHexistsGetKeysStrlen`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdHexistsGetKeysStrlen?: (ctx: CmdHexistsGetKeysStrlenContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdHgetallLenVals`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdHgetallLenVals?: (ctx: CmdHgetallLenValsContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdHmsetHset`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdHmsetHset?: (ctx: CmdHmsetHsetContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdHincrby`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdHincrby?: (ctx: CmdHincrbyContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdHrandfield`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdHrandfield?: (ctx: CmdHrandfieldContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdHscan`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdHscan?: (ctx: CmdHscanContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdHsetnx`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdHsetnx?: (ctx: CmdHsetnxContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdBlmove`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdBlmove?: (ctx: CmdBlmoveContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdLmpop`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdLmpop?: (ctx: CmdLmpopContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdBpop`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdBpop?: (ctx: CmdBpopContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdBrpoplpush`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdBrpoplpush?: (ctx: CmdBrpoplpushContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdLindex`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdLindex?: (ctx: CmdLindexContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdLinsert`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdLinsert?: (ctx: CmdLinsertContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdLlen`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdLlen?: (ctx: CmdLlenContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdLmove`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdLmove?: (ctx: CmdLmoveContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdPop`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPop?: (ctx: CmdPopContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdLpos`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdLpos?: (ctx: CmdLposContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdPush`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPush?: (ctx: CmdPushContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdLrangeTrim`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdLrangeTrim?: (ctx: CmdLrangeTrimContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdLremSet`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdLremSet?: (ctx: CmdLremSetContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdRpoplpush`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdRpoplpush?: (ctx: CmdRpoplpushContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdSadd`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSadd?: (ctx: CmdSaddContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdScard`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdScard?: (ctx: CmdScardContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdSdiff`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSdiff?: (ctx: CmdSdiffContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdSdiffstore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSdiffstore?: (ctx: CmdSdiffstoreContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdSintercard`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSintercard?: (ctx: CmdSintercardContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdSismember`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSismember?: (ctx: CmdSismemberContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdSmove`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSmove?: (ctx: CmdSmoveContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdSpop`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSpop?: (ctx: CmdSpopContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdSscan`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSscan?: (ctx: CmdSscanContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdBzmpop`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdBzmpop?: (ctx: CmdBzmpopContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdBzpopmax`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdBzpopmax?: (ctx: CmdBzpopmaxContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdZadd`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZadd?: (ctx: CmdZaddContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdZcard`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZcard?: (ctx: CmdZcardContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdZcount`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZcount?: (ctx: CmdZcountContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdZdiff`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZdiff?: (ctx: CmdZdiffContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdZdiffstore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZdiffstore?: (ctx: CmdZdiffstoreContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdZincrby`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZincrby?: (ctx: CmdZincrbyContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdZinter`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZinter?: (ctx: CmdZinterContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdZintercard`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZintercard?: (ctx: CmdZintercardContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdZinterstore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZinterstore?: (ctx: CmdZinterstoreContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdZmpop`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZmpop?: (ctx: CmdZmpopContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdZmscore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZmscore?: (ctx: CmdZmscoreContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdZpopmax`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZpopmax?: (ctx: CmdZpopmaxContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdZrandmember`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZrandmember?: (ctx: CmdZrandmemberContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdZrange`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZrange?: (ctx: CmdZrangeContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdZrangebylex`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZrangebylex?: (ctx: CmdZrangebylexContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdZrangebyscore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZrangebyscore?: (ctx: CmdZrangebyscoreContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdZrangestore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZrangestore?: (ctx: CmdZrangestoreContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdZrank`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZrank?: (ctx: CmdZrankContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdZremrangebyrank`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZremrangebyrank?: (ctx: CmdZremrangebyrankContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdZrevrange`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZrevrange?: (ctx: CmdZrevrangeContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdZrevrangebylex`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZrevrangebylex?: (ctx: CmdZrevrangebylexContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdZscan`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZscan?: (ctx: CmdZscanContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdZunion`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZunion?: (ctx: CmdZunionContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdZunionstore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZunionstore?: (ctx: CmdZunionstoreContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdScriptKill`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdScriptKill?: (ctx: CmdScriptKillContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdScriptLoad`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdScriptLoad?: (ctx: CmdScriptLoadContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdEval`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdEval?: (ctx: CmdEvalContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdEvalsha`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdEvalsha?: (ctx: CmdEvalshaContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdScriptExists`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdScriptExists?: (ctx: CmdScriptExistsContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdScriptFlush`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdScriptFlush?: (ctx: CmdScriptFlushContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdExec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdExec?: (ctx: CmdExecContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdWatch`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdWatch?: (ctx: CmdWatchContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdDiscard`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdDiscard?: (ctx: CmdDiscardContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdUnwatch`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdUnwatch?: (ctx: CmdUnwatchContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdMulti`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdMulti?: (ctx: CmdMultiContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdPfmerge`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPfmerge?: (ctx: CmdPfmergeContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdPfadd`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPfadd?: (ctx: CmdPfaddContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdPfcount`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPfcount?: (ctx: CmdPfcountContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdSsubscribe`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSsubscribe?: (ctx: CmdSsubscribeContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdUnsubscribe`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdUnsubscribe?: (ctx: CmdUnsubscribeContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdPusubnumpat`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPusubnumpat?: (ctx: CmdPusubnumpatContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdPusubnumsub`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPusubnumsub?: (ctx: CmdPusubnumsubContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdPusubchannels`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPusubchannels?: (ctx: CmdPusubchannelsContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdPublish`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPublish?: (ctx: CmdPublishContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdAsking`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdAsking?: (ctx: CmdAskingContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdReadonly`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdReadonly?: (ctx: CmdReadonlyContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdReadwrite`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdReadwrite?: (ctx: CmdReadwriteContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdAddDelSlots`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdAddDelSlots?: (ctx: CmdAddDelSlotsContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdCountKeysInSlot`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdCountKeysInSlot?: (ctx: CmdCountKeysInSlotContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdFailOver`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdFailOver?: (ctx: CmdFailOverContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdForget`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdForget?: (ctx: CmdForgetContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdGetKeysInSlot`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdGetKeysInSlot?: (ctx: CmdGetKeysInSlotContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdClusterOrder`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdClusterOrder?: (ctx: CmdClusterOrderContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdKeySlot`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdKeySlot?: (ctx: CmdKeySlotContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdMeet`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdMeet?: (ctx: CmdMeetContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdReplicaesSlave`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdReplicaesSlave?: (ctx: CmdReplicaesSlaveContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdReset`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdReset?: (ctx: CmdResetContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdSetSlot`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSetSlot?: (ctx: CmdSetSlotContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdFlushdb`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdFlushdb?: (ctx: CmdFlushdbContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdEcho`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdEcho?: (ctx: CmdEchoContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdSave`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSave?: (ctx: CmdSaveContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdSlowlog`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSlowlog?: (ctx: CmdSlowlogContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdLastsave`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdLastsave?: (ctx: CmdLastsaveContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdConfig`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdConfig?: (ctx: CmdConfigContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdClient`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdClient?: (ctx: CmdClientContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdShutdown`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdShutdown?: (ctx: CmdShutdownContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdSync`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSync?: (ctx: CmdSyncContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdRole`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdRole?: (ctx: CmdRoleContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdMonitor`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdMonitor?: (ctx: CmdMonitorContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdSlaveof`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSlaveof?: (ctx: CmdSlaveofContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdFlushall`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdFlushall?: (ctx: CmdFlushallContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdSelect`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSelect?: (ctx: CmdSelectContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdPing`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPing?: (ctx: CmdPingContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdQuit`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdQuit?: (ctx: CmdQuitContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdAuth`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdAuth?: (ctx: CmdAuthContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdDbsize`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdDbsize?: (ctx: CmdDbsizeContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdBgrewriteaof`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdBgrewriteaof?: (ctx: CmdBgrewriteaofContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdTime`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdTime?: (ctx: CmdTimeContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdInfo`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdInfo?: (ctx: CmdInfoContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdBgsave`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdBgsave?: (ctx: CmdBgsaveContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdCmd`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdCmd?: (ctx: CmdCmdContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdCmdx`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdCmdx?: (ctx: CmdCmdxContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.cmdDebug`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdDebug?: (ctx: CmdDebugContext) => Result;
    /**
     * Visit a parse tree produced by `RedisSqlParser.sqlStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSqlStatement?: (ctx: SqlStatementContext) => Result;
}
