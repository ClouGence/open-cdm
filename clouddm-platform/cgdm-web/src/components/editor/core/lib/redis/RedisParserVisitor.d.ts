import { ParseTreeVisitor } from "antlr4ts/tree/ParseTreeVisitor";
import { CmdClientPauseContext } from "./RedisParser";
import { CmdClientKillContext } from "./RedisParser";
import { CmdClientGetnameContext } from "./RedisParser";
import { CmdClientSetnameContext } from "./RedisParser";
import { CmdClientListContext } from "./RedisParser";
import { CmdCmdInfoContext } from "./RedisParser";
import { CmdCmdGetkeysContext } from "./RedisParser";
import { CmdCmdCountContext } from "./RedisParser";
import { CmdConfigGetContext } from "./RedisParser";
import { CmdConfigSetContext } from "./RedisParser";
import { CmdConfigResetContext } from "./RedisParser";
import { CmdConfigRewriteContext } from "./RedisParser";
import { CmdIncrContext } from "./RedisParser";
import { CmdIncrbyContext } from "./RedisParser";
import { CmdIncrbyFloatContext } from "./RedisParser";
import { ProgramContext } from "./RedisParser";
import { SqlStatementsContext } from "./RedisParser";
import { EmptyStatement_Context } from "./RedisParser";
import { KeyNameContext } from "./RedisParser";
import { FieldNameContext } from "./RedisParser";
import { StringValueContext } from "./RedisParser";
import { IntValueContext } from "./RedisParser";
import { FloatValueContext } from "./RedisParser";
import { PatternContext } from "./RedisParser";
import { CursorContext } from "./RedisParser";
import { MinContext } from "./RedisParser";
import { MaxContext } from "./RedisParser";
import { MultiStringContext } from "./RedisParser";
import { CoutContext } from "./RedisParser";
import { StartContext } from "./RedisParser";
import { EndContext } from "./RedisParser";
import { PortContext } from "./RedisParser";
import { KeyAndStringContext } from "./RedisParser";
import { IntAndStringContext } from "./RedisParser";
import { InfoOptContext } from "./RedisParser";
import { PatternOptContext } from "./RedisParser";
import { KeyOptContext } from "./RedisParser";
import { IdletimeOptContext } from "./RedisParser";
import { FreqOptContext } from "./RedisParser";
import { LimitOptContext } from "./RedisParser";
import { SortOptContext } from "./RedisParser";
import { GetsetOptContext } from "./RedisParser";
import { LrOptContext } from "./RedisParser";
import { RankOptContext } from "./RedisParser";
import { CountOptContext } from "./RedisParser";
import { MaxlenOptContext } from "./RedisParser";
import { CmdCopyContext } from "./RedisParser";
import { CmdDelContext } from "./RedisParser";
import { CmdDumpContext } from "./RedisParser";
import { CmdExistsContext } from "./RedisParser";
import { CmdExpireContext } from "./RedisParser";
import { CmdExpireatContext } from "./RedisParser";
import { CmdExpireTimeContext } from "./RedisParser";
import { CmdKeysContext } from "./RedisParser";
import { CmdMoveContext } from "./RedisParser";
import { CmdObjectContext } from "./RedisParser";
import { CmdPersistContext } from "./RedisParser";
import { CmdPexpireContext } from "./RedisParser";
import { CmdPexpireatContext } from "./RedisParser";
import { CmdPExpireTimeContext } from "./RedisParser";
import { CmdTtlContext } from "./RedisParser";
import { CmdPttlContext } from "./RedisParser";
import { CmdRandomkeyContext } from "./RedisParser";
import { CmdRenameContext } from "./RedisParser";
import { CmdRenamenxContext } from "./RedisParser";
import { CmdRestoreContext } from "./RedisParser";
import { CmdScanContext } from "./RedisParser";
import { CmdSortContext } from "./RedisParser";
import { CmdSortroContext } from "./RedisParser";
import { CmdTouchContext } from "./RedisParser";
import { CmdTypeContext } from "./RedisParser";
import { CmdUnlinkContext } from "./RedisParser";
import { CmdWaitContext } from "./RedisParser";
import { CmdAppendContext } from "./RedisParser";
import { CmdDecrContext } from "./RedisParser";
import { CmdDecrbyContext } from "./RedisParser";
import { CmdGetContext } from "./RedisParser";
import { CmdGetdelContext } from "./RedisParser";
import { CmdGetexContext } from "./RedisParser";
import { CmdGetrangeContext } from "./RedisParser";
import { CmdGetsetContext } from "./RedisParser";
import { CmdGetbitContext } from "./RedisParser";
import { CmdIncContext } from "./RedisParser";
import { CmdLcsContext } from "./RedisParser";
import { CmdMgetContext } from "./RedisParser";
import { CmdMsetContext } from "./RedisParser";
import { CmdMsetnxContext } from "./RedisParser";
import { CmdSetexContext } from "./RedisParser";
import { CmdSetContext } from "./RedisParser";
import { CmdSetnxContext } from "./RedisParser";
import { CmdSetrangeContext } from "./RedisParser";
import { CmdSetbitContext } from "./RedisParser";
import { CmdStrlenContext } from "./RedisParser";
import { CmdSubstrContext } from "./RedisParser";
import { CmdHdelMgetContext } from "./RedisParser";
import { CmdHexistsGetKeysStrlenContext } from "./RedisParser";
import { CmdHgetallLenValsContext } from "./RedisParser";
import { CmdHmsetHsetContext } from "./RedisParser";
import { CmdHincrbyContext } from "./RedisParser";
import { CmdHrandfieldContext } from "./RedisParser";
import { CmdHscanContext } from "./RedisParser";
import { CmdHsetnxContext } from "./RedisParser";
import { CmdBlmoveContext } from "./RedisParser";
import { CmdLmpopContext } from "./RedisParser";
import { CmdBpopContext } from "./RedisParser";
import { CmdBrpoplpushContext } from "./RedisParser";
import { CmdLindexContext } from "./RedisParser";
import { CmdLinsertContext } from "./RedisParser";
import { CmdLlenContext } from "./RedisParser";
import { CmdLmoveContext } from "./RedisParser";
import { CmdPopContext } from "./RedisParser";
import { CmdLposContext } from "./RedisParser";
import { CmdPushContext } from "./RedisParser";
import { CmdLrangeTrimContext } from "./RedisParser";
import { CmdLremSetContext } from "./RedisParser";
import { CmdRpoplpushContext } from "./RedisParser";
import { CmdSaddContext } from "./RedisParser";
import { CmdScardContext } from "./RedisParser";
import { CmdSdiffContext } from "./RedisParser";
import { CmdSdiffstoreContext } from "./RedisParser";
import { CmdSintercardContext } from "./RedisParser";
import { CmdSismemberContext } from "./RedisParser";
import { CmdSmoveContext } from "./RedisParser";
import { CmdSpopContext } from "./RedisParser";
import { CmdSscanContext } from "./RedisParser";
import { CmdBzmpopContext } from "./RedisParser";
import { CmdBzpopmaxContext } from "./RedisParser";
import { CmdZaddContext } from "./RedisParser";
import { CmdZcardContext } from "./RedisParser";
import { CmdZcountContext } from "./RedisParser";
import { CmdZdiffContext } from "./RedisParser";
import { CmdZdiffstoreContext } from "./RedisParser";
import { CmdZincrbyContext } from "./RedisParser";
import { CmdZinterContext } from "./RedisParser";
import { CmdZintercardContext } from "./RedisParser";
import { CmdZinterstoreContext } from "./RedisParser";
import { CmdZmpopContext } from "./RedisParser";
import { CmdZmscoreContext } from "./RedisParser";
import { CmdZpopmaxContext } from "./RedisParser";
import { CmdZrandmemberContext } from "./RedisParser";
import { CmdZrangeContext } from "./RedisParser";
import { CmdZrangebylexContext } from "./RedisParser";
import { CmdZrangebyscoreContext } from "./RedisParser";
import { CmdZrangestoreContext } from "./RedisParser";
import { CmdZrankContext } from "./RedisParser";
import { CmdZremrangebyrankContext } from "./RedisParser";
import { CmdZrevrangeContext } from "./RedisParser";
import { CmdZrevrangebylexContext } from "./RedisParser";
import { CmdZscanContext } from "./RedisParser";
import { CmdZunionContext } from "./RedisParser";
import { CmdZunionstoreContext } from "./RedisParser";
import { CmdScriptKillContext } from "./RedisParser";
import { CmdScriptLoadContext } from "./RedisParser";
import { CmdEvalContext } from "./RedisParser";
import { CmdEvalshaContext } from "./RedisParser";
import { CmdScriptExistsContext } from "./RedisParser";
import { CmdScriptFlushContext } from "./RedisParser";
import { CmdExecContext } from "./RedisParser";
import { CmdWatchContext } from "./RedisParser";
import { CmdDiscardContext } from "./RedisParser";
import { CmdUnwatchContext } from "./RedisParser";
import { CmdMultiContext } from "./RedisParser";
import { CmdPfmergeContext } from "./RedisParser";
import { CmdPfaddContext } from "./RedisParser";
import { CmdPfcountContext } from "./RedisParser";
import { CmdSsubscribeContext } from "./RedisParser";
import { CmdUnsubscribeContext } from "./RedisParser";
import { CmdPusubnumpatContext } from "./RedisParser";
import { CmdPusubnumsubContext } from "./RedisParser";
import { CmdPusubchannelsContext } from "./RedisParser";
import { CmdPublishContext } from "./RedisParser";
import { CmdAskingContext } from "./RedisParser";
import { CmdReadonlyContext } from "./RedisParser";
import { CmdReadwriteContext } from "./RedisParser";
import { CmdAddDelSlotsContext } from "./RedisParser";
import { CmdCountKeysInSlotContext } from "./RedisParser";
import { CmdFailOverContext } from "./RedisParser";
import { CmdForgetContext } from "./RedisParser";
import { CmdGetKeysInSlotContext } from "./RedisParser";
import { CmdClusterOrderContext } from "./RedisParser";
import { CmdKeySlotContext } from "./RedisParser";
import { CmdMeetContext } from "./RedisParser";
import { CmdReplicaesSlaveContext } from "./RedisParser";
import { CmdResetContext } from "./RedisParser";
import { CmdSetSlotContext } from "./RedisParser";
import { CmdFlushdbContext } from "./RedisParser";
import { CmdEchoContext } from "./RedisParser";
import { CmdSaveContext } from "./RedisParser";
import { CmdSlowlogContext } from "./RedisParser";
import { CmdLastsaveContext } from "./RedisParser";
import { CmdConfigContext } from "./RedisParser";
import { CmdClientContext } from "./RedisParser";
import { CmdShutdownContext } from "./RedisParser";
import { CmdSyncContext } from "./RedisParser";
import { CmdRoleContext } from "./RedisParser";
import { CmdMonitorContext } from "./RedisParser";
import { CmdSlaveofContext } from "./RedisParser";
import { CmdFlushallContext } from "./RedisParser";
import { CmdSelectContext } from "./RedisParser";
import { CmdPingContext } from "./RedisParser";
import { CmdQuitContext } from "./RedisParser";
import { CmdAuthContext } from "./RedisParser";
import { CmdDbsizeContext } from "./RedisParser";
import { CmdBgrewriteaofContext } from "./RedisParser";
import { CmdTimeContext } from "./RedisParser";
import { CmdInfoContext } from "./RedisParser";
import { CmdBgsaveContext } from "./RedisParser";
import { CmdCmdContext } from "./RedisParser";
import { CmdCmdxContext } from "./RedisParser";
import { CmdDebugContext } from "./RedisParser";
import { SqlStatementContext } from "./RedisParser";
/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by `RedisParser`.
 *
 * @param <Result> The return type of the visit operation. Use `void` for
 * operations with no return type.
 */
export interface RedisParserVisitor<Result> extends ParseTreeVisitor<Result> {
    /**
     * Visit a parse tree produced by the `cmdClientPause`
     * labeled alternative in `RedisParser.cmdClient`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdClientPause?: (ctx: CmdClientPauseContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdClientKill`
     * labeled alternative in `RedisParser.cmdClient`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdClientKill?: (ctx: CmdClientKillContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdClientGetname`
     * labeled alternative in `RedisParser.cmdClient`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdClientGetname?: (ctx: CmdClientGetnameContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdClientSetname`
     * labeled alternative in `RedisParser.cmdClient`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdClientSetname?: (ctx: CmdClientSetnameContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdClientList`
     * labeled alternative in `RedisParser.cmdClient`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdClientList?: (ctx: CmdClientListContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdCmdInfo`
     * labeled alternative in `RedisParser.cmdCmdx`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdCmdInfo?: (ctx: CmdCmdInfoContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdCmdGetkeys`
     * labeled alternative in `RedisParser.cmdCmdx`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdCmdGetkeys?: (ctx: CmdCmdGetkeysContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdCmdCount`
     * labeled alternative in `RedisParser.cmdCmdx`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdCmdCount?: (ctx: CmdCmdCountContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdConfigGet`
     * labeled alternative in `RedisParser.cmdConfig`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdConfigGet?: (ctx: CmdConfigGetContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdConfigSet`
     * labeled alternative in `RedisParser.cmdConfig`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdConfigSet?: (ctx: CmdConfigSetContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdConfigReset`
     * labeled alternative in `RedisParser.cmdConfig`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdConfigReset?: (ctx: CmdConfigResetContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdConfigRewrite`
     * labeled alternative in `RedisParser.cmdConfig`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdConfigRewrite?: (ctx: CmdConfigRewriteContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdIncr`
     * labeled alternative in `RedisParser.cmdInc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdIncr?: (ctx: CmdIncrContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdIncrby`
     * labeled alternative in `RedisParser.cmdInc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdIncrby?: (ctx: CmdIncrbyContext) => Result;
    /**
     * Visit a parse tree produced by the `cmdIncrbyFloat`
     * labeled alternative in `RedisParser.cmdInc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdIncrbyFloat?: (ctx: CmdIncrbyFloatContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.program`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitProgram?: (ctx: ProgramContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.sqlStatements`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSqlStatements?: (ctx: SqlStatementsContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.emptyStatement_`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitEmptyStatement_?: (ctx: EmptyStatement_Context) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.keyName`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitKeyName?: (ctx: KeyNameContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.fieldName`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFieldName?: (ctx: FieldNameContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.stringValue`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStringValue?: (ctx: StringValueContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.intValue`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIntValue?: (ctx: IntValueContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.floatValue`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFloatValue?: (ctx: FloatValueContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.pattern`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPattern?: (ctx: PatternContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cursor`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCursor?: (ctx: CursorContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.min`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMin?: (ctx: MinContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.max`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMax?: (ctx: MaxContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.multiString`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMultiString?: (ctx: MultiStringContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cout`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCout?: (ctx: CoutContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.start`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitStart?: (ctx: StartContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.end`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitEnd?: (ctx: EndContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.port`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPort?: (ctx: PortContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.keyAndString`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitKeyAndString?: (ctx: KeyAndStringContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.intAndString`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIntAndString?: (ctx: IntAndStringContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.infoOpt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitInfoOpt?: (ctx: InfoOptContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.patternOpt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitPatternOpt?: (ctx: PatternOptContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.keyOpt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitKeyOpt?: (ctx: KeyOptContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.idletimeOpt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitIdletimeOpt?: (ctx: IdletimeOptContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.freqOpt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitFreqOpt?: (ctx: FreqOptContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.limitOpt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLimitOpt?: (ctx: LimitOptContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.sortOpt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSortOpt?: (ctx: SortOptContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.getsetOpt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitGetsetOpt?: (ctx: GetsetOptContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.lrOpt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitLrOpt?: (ctx: LrOptContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.rankOpt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitRankOpt?: (ctx: RankOptContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.countOpt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCountOpt?: (ctx: CountOptContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.maxlenOpt`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitMaxlenOpt?: (ctx: MaxlenOptContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdCopy`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdCopy?: (ctx: CmdCopyContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdDel`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdDel?: (ctx: CmdDelContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdDump`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdDump?: (ctx: CmdDumpContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdExists`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdExists?: (ctx: CmdExistsContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdExpire`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdExpire?: (ctx: CmdExpireContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdExpireat`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdExpireat?: (ctx: CmdExpireatContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdExpireTime`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdExpireTime?: (ctx: CmdExpireTimeContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdKeys`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdKeys?: (ctx: CmdKeysContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdMove`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdMove?: (ctx: CmdMoveContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdObject`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdObject?: (ctx: CmdObjectContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdPersist`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPersist?: (ctx: CmdPersistContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdPexpire`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPexpire?: (ctx: CmdPexpireContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdPexpireat`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPexpireat?: (ctx: CmdPexpireatContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdPExpireTime`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPExpireTime?: (ctx: CmdPExpireTimeContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdTtl`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdTtl?: (ctx: CmdTtlContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdPttl`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPttl?: (ctx: CmdPttlContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdRandomkey`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdRandomkey?: (ctx: CmdRandomkeyContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdRename`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdRename?: (ctx: CmdRenameContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdRenamenx`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdRenamenx?: (ctx: CmdRenamenxContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdRestore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdRestore?: (ctx: CmdRestoreContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdScan`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdScan?: (ctx: CmdScanContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdSort`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSort?: (ctx: CmdSortContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdSortro`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSortro?: (ctx: CmdSortroContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdTouch`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdTouch?: (ctx: CmdTouchContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdType`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdType?: (ctx: CmdTypeContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdUnlink`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdUnlink?: (ctx: CmdUnlinkContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdWait`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdWait?: (ctx: CmdWaitContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdAppend`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdAppend?: (ctx: CmdAppendContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdDecr`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdDecr?: (ctx: CmdDecrContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdDecrby`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdDecrby?: (ctx: CmdDecrbyContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdGet`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdGet?: (ctx: CmdGetContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdGetdel`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdGetdel?: (ctx: CmdGetdelContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdGetex`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdGetex?: (ctx: CmdGetexContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdGetrange`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdGetrange?: (ctx: CmdGetrangeContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdGetset`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdGetset?: (ctx: CmdGetsetContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdGetbit`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdGetbit?: (ctx: CmdGetbitContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdInc`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdInc?: (ctx: CmdIncContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdLcs`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdLcs?: (ctx: CmdLcsContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdMget`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdMget?: (ctx: CmdMgetContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdMset`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdMset?: (ctx: CmdMsetContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdMsetnx`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdMsetnx?: (ctx: CmdMsetnxContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdSetex`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSetex?: (ctx: CmdSetexContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdSet`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSet?: (ctx: CmdSetContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdSetnx`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSetnx?: (ctx: CmdSetnxContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdSetrange`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSetrange?: (ctx: CmdSetrangeContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdSetbit`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSetbit?: (ctx: CmdSetbitContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdStrlen`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdStrlen?: (ctx: CmdStrlenContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdSubstr`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSubstr?: (ctx: CmdSubstrContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdHdelMget`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdHdelMget?: (ctx: CmdHdelMgetContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdHexistsGetKeysStrlen`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdHexistsGetKeysStrlen?: (ctx: CmdHexistsGetKeysStrlenContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdHgetallLenVals`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdHgetallLenVals?: (ctx: CmdHgetallLenValsContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdHmsetHset`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdHmsetHset?: (ctx: CmdHmsetHsetContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdHincrby`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdHincrby?: (ctx: CmdHincrbyContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdHrandfield`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdHrandfield?: (ctx: CmdHrandfieldContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdHscan`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdHscan?: (ctx: CmdHscanContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdHsetnx`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdHsetnx?: (ctx: CmdHsetnxContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdBlmove`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdBlmove?: (ctx: CmdBlmoveContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdLmpop`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdLmpop?: (ctx: CmdLmpopContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdBpop`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdBpop?: (ctx: CmdBpopContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdBrpoplpush`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdBrpoplpush?: (ctx: CmdBrpoplpushContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdLindex`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdLindex?: (ctx: CmdLindexContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdLinsert`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdLinsert?: (ctx: CmdLinsertContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdLlen`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdLlen?: (ctx: CmdLlenContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdLmove`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdLmove?: (ctx: CmdLmoveContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdPop`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPop?: (ctx: CmdPopContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdLpos`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdLpos?: (ctx: CmdLposContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdPush`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPush?: (ctx: CmdPushContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdLrangeTrim`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdLrangeTrim?: (ctx: CmdLrangeTrimContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdLremSet`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdLremSet?: (ctx: CmdLremSetContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdRpoplpush`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdRpoplpush?: (ctx: CmdRpoplpushContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdSadd`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSadd?: (ctx: CmdSaddContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdScard`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdScard?: (ctx: CmdScardContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdSdiff`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSdiff?: (ctx: CmdSdiffContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdSdiffstore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSdiffstore?: (ctx: CmdSdiffstoreContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdSintercard`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSintercard?: (ctx: CmdSintercardContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdSismember`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSismember?: (ctx: CmdSismemberContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdSmove`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSmove?: (ctx: CmdSmoveContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdSpop`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSpop?: (ctx: CmdSpopContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdSscan`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSscan?: (ctx: CmdSscanContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdBzmpop`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdBzmpop?: (ctx: CmdBzmpopContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdBzpopmax`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdBzpopmax?: (ctx: CmdBzpopmaxContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdZadd`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZadd?: (ctx: CmdZaddContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdZcard`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZcard?: (ctx: CmdZcardContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdZcount`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZcount?: (ctx: CmdZcountContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdZdiff`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZdiff?: (ctx: CmdZdiffContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdZdiffstore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZdiffstore?: (ctx: CmdZdiffstoreContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdZincrby`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZincrby?: (ctx: CmdZincrbyContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdZinter`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZinter?: (ctx: CmdZinterContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdZintercard`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZintercard?: (ctx: CmdZintercardContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdZinterstore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZinterstore?: (ctx: CmdZinterstoreContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdZmpop`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZmpop?: (ctx: CmdZmpopContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdZmscore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZmscore?: (ctx: CmdZmscoreContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdZpopmax`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZpopmax?: (ctx: CmdZpopmaxContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdZrandmember`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZrandmember?: (ctx: CmdZrandmemberContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdZrange`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZrange?: (ctx: CmdZrangeContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdZrangebylex`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZrangebylex?: (ctx: CmdZrangebylexContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdZrangebyscore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZrangebyscore?: (ctx: CmdZrangebyscoreContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdZrangestore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZrangestore?: (ctx: CmdZrangestoreContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdZrank`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZrank?: (ctx: CmdZrankContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdZremrangebyrank`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZremrangebyrank?: (ctx: CmdZremrangebyrankContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdZrevrange`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZrevrange?: (ctx: CmdZrevrangeContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdZrevrangebylex`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZrevrangebylex?: (ctx: CmdZrevrangebylexContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdZscan`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZscan?: (ctx: CmdZscanContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdZunion`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZunion?: (ctx: CmdZunionContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdZunionstore`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdZunionstore?: (ctx: CmdZunionstoreContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdScriptKill`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdScriptKill?: (ctx: CmdScriptKillContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdScriptLoad`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdScriptLoad?: (ctx: CmdScriptLoadContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdEval`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdEval?: (ctx: CmdEvalContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdEvalsha`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdEvalsha?: (ctx: CmdEvalshaContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdScriptExists`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdScriptExists?: (ctx: CmdScriptExistsContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdScriptFlush`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdScriptFlush?: (ctx: CmdScriptFlushContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdExec`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdExec?: (ctx: CmdExecContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdWatch`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdWatch?: (ctx: CmdWatchContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdDiscard`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdDiscard?: (ctx: CmdDiscardContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdUnwatch`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdUnwatch?: (ctx: CmdUnwatchContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdMulti`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdMulti?: (ctx: CmdMultiContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdPfmerge`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPfmerge?: (ctx: CmdPfmergeContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdPfadd`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPfadd?: (ctx: CmdPfaddContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdPfcount`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPfcount?: (ctx: CmdPfcountContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdSsubscribe`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSsubscribe?: (ctx: CmdSsubscribeContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdUnsubscribe`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdUnsubscribe?: (ctx: CmdUnsubscribeContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdPusubnumpat`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPusubnumpat?: (ctx: CmdPusubnumpatContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdPusubnumsub`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPusubnumsub?: (ctx: CmdPusubnumsubContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdPusubchannels`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPusubchannels?: (ctx: CmdPusubchannelsContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdPublish`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPublish?: (ctx: CmdPublishContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdAsking`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdAsking?: (ctx: CmdAskingContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdReadonly`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdReadonly?: (ctx: CmdReadonlyContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdReadwrite`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdReadwrite?: (ctx: CmdReadwriteContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdAddDelSlots`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdAddDelSlots?: (ctx: CmdAddDelSlotsContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdCountKeysInSlot`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdCountKeysInSlot?: (ctx: CmdCountKeysInSlotContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdFailOver`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdFailOver?: (ctx: CmdFailOverContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdForget`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdForget?: (ctx: CmdForgetContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdGetKeysInSlot`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdGetKeysInSlot?: (ctx: CmdGetKeysInSlotContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdClusterOrder`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdClusterOrder?: (ctx: CmdClusterOrderContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdKeySlot`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdKeySlot?: (ctx: CmdKeySlotContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdMeet`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdMeet?: (ctx: CmdMeetContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdReplicaesSlave`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdReplicaesSlave?: (ctx: CmdReplicaesSlaveContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdReset`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdReset?: (ctx: CmdResetContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdSetSlot`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSetSlot?: (ctx: CmdSetSlotContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdFlushdb`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdFlushdb?: (ctx: CmdFlushdbContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdEcho`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdEcho?: (ctx: CmdEchoContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdSave`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSave?: (ctx: CmdSaveContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdSlowlog`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSlowlog?: (ctx: CmdSlowlogContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdLastsave`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdLastsave?: (ctx: CmdLastsaveContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdConfig`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdConfig?: (ctx: CmdConfigContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdClient`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdClient?: (ctx: CmdClientContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdShutdown`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdShutdown?: (ctx: CmdShutdownContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdSync`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSync?: (ctx: CmdSyncContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdRole`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdRole?: (ctx: CmdRoleContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdMonitor`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdMonitor?: (ctx: CmdMonitorContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdSlaveof`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSlaveof?: (ctx: CmdSlaveofContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdFlushall`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdFlushall?: (ctx: CmdFlushallContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdSelect`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdSelect?: (ctx: CmdSelectContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdPing`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdPing?: (ctx: CmdPingContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdQuit`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdQuit?: (ctx: CmdQuitContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdAuth`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdAuth?: (ctx: CmdAuthContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdDbsize`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdDbsize?: (ctx: CmdDbsizeContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdBgrewriteaof`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdBgrewriteaof?: (ctx: CmdBgrewriteaofContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdTime`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdTime?: (ctx: CmdTimeContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdInfo`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdInfo?: (ctx: CmdInfoContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdBgsave`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdBgsave?: (ctx: CmdBgsaveContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdCmd`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdCmd?: (ctx: CmdCmdContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdCmdx`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdCmdx?: (ctx: CmdCmdxContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.cmdDebug`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitCmdDebug?: (ctx: CmdDebugContext) => Result;
    /**
     * Visit a parse tree produced by `RedisParser.sqlStatement`.
     * @param ctx the parse tree
     * @return the visitor result
     */
    visitSqlStatement?: (ctx: SqlStatementContext) => Result;
}
