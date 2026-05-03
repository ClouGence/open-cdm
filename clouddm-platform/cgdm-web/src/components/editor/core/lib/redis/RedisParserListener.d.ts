import { ParseTreeListener } from "antlr4ts/tree/ParseTreeListener";
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
 * This interface defines a complete listener for a parse tree produced by
 * `RedisParser`.
 */
export interface RedisParserListener extends ParseTreeListener {
    /**
     * Enter a parse tree produced by the `cmdClientPause`
     * labeled alternative in `RedisParser.cmdClient`.
     * @param ctx the parse tree
     */
    enterCmdClientPause?: (ctx: CmdClientPauseContext) => void;
    /**
     * Exit a parse tree produced by the `cmdClientPause`
     * labeled alternative in `RedisParser.cmdClient`.
     * @param ctx the parse tree
     */
    exitCmdClientPause?: (ctx: CmdClientPauseContext) => void;
    /**
     * Enter a parse tree produced by the `cmdClientKill`
     * labeled alternative in `RedisParser.cmdClient`.
     * @param ctx the parse tree
     */
    enterCmdClientKill?: (ctx: CmdClientKillContext) => void;
    /**
     * Exit a parse tree produced by the `cmdClientKill`
     * labeled alternative in `RedisParser.cmdClient`.
     * @param ctx the parse tree
     */
    exitCmdClientKill?: (ctx: CmdClientKillContext) => void;
    /**
     * Enter a parse tree produced by the `cmdClientGetname`
     * labeled alternative in `RedisParser.cmdClient`.
     * @param ctx the parse tree
     */
    enterCmdClientGetname?: (ctx: CmdClientGetnameContext) => void;
    /**
     * Exit a parse tree produced by the `cmdClientGetname`
     * labeled alternative in `RedisParser.cmdClient`.
     * @param ctx the parse tree
     */
    exitCmdClientGetname?: (ctx: CmdClientGetnameContext) => void;
    /**
     * Enter a parse tree produced by the `cmdClientSetname`
     * labeled alternative in `RedisParser.cmdClient`.
     * @param ctx the parse tree
     */
    enterCmdClientSetname?: (ctx: CmdClientSetnameContext) => void;
    /**
     * Exit a parse tree produced by the `cmdClientSetname`
     * labeled alternative in `RedisParser.cmdClient`.
     * @param ctx the parse tree
     */
    exitCmdClientSetname?: (ctx: CmdClientSetnameContext) => void;
    /**
     * Enter a parse tree produced by the `cmdClientList`
     * labeled alternative in `RedisParser.cmdClient`.
     * @param ctx the parse tree
     */
    enterCmdClientList?: (ctx: CmdClientListContext) => void;
    /**
     * Exit a parse tree produced by the `cmdClientList`
     * labeled alternative in `RedisParser.cmdClient`.
     * @param ctx the parse tree
     */
    exitCmdClientList?: (ctx: CmdClientListContext) => void;
    /**
     * Enter a parse tree produced by the `cmdCmdInfo`
     * labeled alternative in `RedisParser.cmdCmdx`.
     * @param ctx the parse tree
     */
    enterCmdCmdInfo?: (ctx: CmdCmdInfoContext) => void;
    /**
     * Exit a parse tree produced by the `cmdCmdInfo`
     * labeled alternative in `RedisParser.cmdCmdx`.
     * @param ctx the parse tree
     */
    exitCmdCmdInfo?: (ctx: CmdCmdInfoContext) => void;
    /**
     * Enter a parse tree produced by the `cmdCmdGetkeys`
     * labeled alternative in `RedisParser.cmdCmdx`.
     * @param ctx the parse tree
     */
    enterCmdCmdGetkeys?: (ctx: CmdCmdGetkeysContext) => void;
    /**
     * Exit a parse tree produced by the `cmdCmdGetkeys`
     * labeled alternative in `RedisParser.cmdCmdx`.
     * @param ctx the parse tree
     */
    exitCmdCmdGetkeys?: (ctx: CmdCmdGetkeysContext) => void;
    /**
     * Enter a parse tree produced by the `cmdCmdCount`
     * labeled alternative in `RedisParser.cmdCmdx`.
     * @param ctx the parse tree
     */
    enterCmdCmdCount?: (ctx: CmdCmdCountContext) => void;
    /**
     * Exit a parse tree produced by the `cmdCmdCount`
     * labeled alternative in `RedisParser.cmdCmdx`.
     * @param ctx the parse tree
     */
    exitCmdCmdCount?: (ctx: CmdCmdCountContext) => void;
    /**
     * Enter a parse tree produced by the `cmdConfigGet`
     * labeled alternative in `RedisParser.cmdConfig`.
     * @param ctx the parse tree
     */
    enterCmdConfigGet?: (ctx: CmdConfigGetContext) => void;
    /**
     * Exit a parse tree produced by the `cmdConfigGet`
     * labeled alternative in `RedisParser.cmdConfig`.
     * @param ctx the parse tree
     */
    exitCmdConfigGet?: (ctx: CmdConfigGetContext) => void;
    /**
     * Enter a parse tree produced by the `cmdConfigSet`
     * labeled alternative in `RedisParser.cmdConfig`.
     * @param ctx the parse tree
     */
    enterCmdConfigSet?: (ctx: CmdConfigSetContext) => void;
    /**
     * Exit a parse tree produced by the `cmdConfigSet`
     * labeled alternative in `RedisParser.cmdConfig`.
     * @param ctx the parse tree
     */
    exitCmdConfigSet?: (ctx: CmdConfigSetContext) => void;
    /**
     * Enter a parse tree produced by the `cmdConfigReset`
     * labeled alternative in `RedisParser.cmdConfig`.
     * @param ctx the parse tree
     */
    enterCmdConfigReset?: (ctx: CmdConfigResetContext) => void;
    /**
     * Exit a parse tree produced by the `cmdConfigReset`
     * labeled alternative in `RedisParser.cmdConfig`.
     * @param ctx the parse tree
     */
    exitCmdConfigReset?: (ctx: CmdConfigResetContext) => void;
    /**
     * Enter a parse tree produced by the `cmdConfigRewrite`
     * labeled alternative in `RedisParser.cmdConfig`.
     * @param ctx the parse tree
     */
    enterCmdConfigRewrite?: (ctx: CmdConfigRewriteContext) => void;
    /**
     * Exit a parse tree produced by the `cmdConfigRewrite`
     * labeled alternative in `RedisParser.cmdConfig`.
     * @param ctx the parse tree
     */
    exitCmdConfigRewrite?: (ctx: CmdConfigRewriteContext) => void;
    /**
     * Enter a parse tree produced by the `cmdIncr`
     * labeled alternative in `RedisParser.cmdInc`.
     * @param ctx the parse tree
     */
    enterCmdIncr?: (ctx: CmdIncrContext) => void;
    /**
     * Exit a parse tree produced by the `cmdIncr`
     * labeled alternative in `RedisParser.cmdInc`.
     * @param ctx the parse tree
     */
    exitCmdIncr?: (ctx: CmdIncrContext) => void;
    /**
     * Enter a parse tree produced by the `cmdIncrby`
     * labeled alternative in `RedisParser.cmdInc`.
     * @param ctx the parse tree
     */
    enterCmdIncrby?: (ctx: CmdIncrbyContext) => void;
    /**
     * Exit a parse tree produced by the `cmdIncrby`
     * labeled alternative in `RedisParser.cmdInc`.
     * @param ctx the parse tree
     */
    exitCmdIncrby?: (ctx: CmdIncrbyContext) => void;
    /**
     * Enter a parse tree produced by the `cmdIncrbyFloat`
     * labeled alternative in `RedisParser.cmdInc`.
     * @param ctx the parse tree
     */
    enterCmdIncrbyFloat?: (ctx: CmdIncrbyFloatContext) => void;
    /**
     * Exit a parse tree produced by the `cmdIncrbyFloat`
     * labeled alternative in `RedisParser.cmdInc`.
     * @param ctx the parse tree
     */
    exitCmdIncrbyFloat?: (ctx: CmdIncrbyFloatContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.program`.
     * @param ctx the parse tree
     */
    enterProgram?: (ctx: ProgramContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.program`.
     * @param ctx the parse tree
     */
    exitProgram?: (ctx: ProgramContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.sqlStatements`.
     * @param ctx the parse tree
     */
    enterSqlStatements?: (ctx: SqlStatementsContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.sqlStatements`.
     * @param ctx the parse tree
     */
    exitSqlStatements?: (ctx: SqlStatementsContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.emptyStatement_`.
     * @param ctx the parse tree
     */
    enterEmptyStatement_?: (ctx: EmptyStatement_Context) => void;
    /**
     * Exit a parse tree produced by `RedisParser.emptyStatement_`.
     * @param ctx the parse tree
     */
    exitEmptyStatement_?: (ctx: EmptyStatement_Context) => void;
    /**
     * Enter a parse tree produced by `RedisParser.keyName`.
     * @param ctx the parse tree
     */
    enterKeyName?: (ctx: KeyNameContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.keyName`.
     * @param ctx the parse tree
     */
    exitKeyName?: (ctx: KeyNameContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.fieldName`.
     * @param ctx the parse tree
     */
    enterFieldName?: (ctx: FieldNameContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.fieldName`.
     * @param ctx the parse tree
     */
    exitFieldName?: (ctx: FieldNameContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.stringValue`.
     * @param ctx the parse tree
     */
    enterStringValue?: (ctx: StringValueContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.stringValue`.
     * @param ctx the parse tree
     */
    exitStringValue?: (ctx: StringValueContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.intValue`.
     * @param ctx the parse tree
     */
    enterIntValue?: (ctx: IntValueContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.intValue`.
     * @param ctx the parse tree
     */
    exitIntValue?: (ctx: IntValueContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.floatValue`.
     * @param ctx the parse tree
     */
    enterFloatValue?: (ctx: FloatValueContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.floatValue`.
     * @param ctx the parse tree
     */
    exitFloatValue?: (ctx: FloatValueContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.pattern`.
     * @param ctx the parse tree
     */
    enterPattern?: (ctx: PatternContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.pattern`.
     * @param ctx the parse tree
     */
    exitPattern?: (ctx: PatternContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cursor`.
     * @param ctx the parse tree
     */
    enterCursor?: (ctx: CursorContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cursor`.
     * @param ctx the parse tree
     */
    exitCursor?: (ctx: CursorContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.min`.
     * @param ctx the parse tree
     */
    enterMin?: (ctx: MinContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.min`.
     * @param ctx the parse tree
     */
    exitMin?: (ctx: MinContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.max`.
     * @param ctx the parse tree
     */
    enterMax?: (ctx: MaxContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.max`.
     * @param ctx the parse tree
     */
    exitMax?: (ctx: MaxContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.multiString`.
     * @param ctx the parse tree
     */
    enterMultiString?: (ctx: MultiStringContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.multiString`.
     * @param ctx the parse tree
     */
    exitMultiString?: (ctx: MultiStringContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cout`.
     * @param ctx the parse tree
     */
    enterCout?: (ctx: CoutContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cout`.
     * @param ctx the parse tree
     */
    exitCout?: (ctx: CoutContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.start`.
     * @param ctx the parse tree
     */
    enterStart?: (ctx: StartContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.start`.
     * @param ctx the parse tree
     */
    exitStart?: (ctx: StartContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.end`.
     * @param ctx the parse tree
     */
    enterEnd?: (ctx: EndContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.end`.
     * @param ctx the parse tree
     */
    exitEnd?: (ctx: EndContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.port`.
     * @param ctx the parse tree
     */
    enterPort?: (ctx: PortContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.port`.
     * @param ctx the parse tree
     */
    exitPort?: (ctx: PortContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.keyAndString`.
     * @param ctx the parse tree
     */
    enterKeyAndString?: (ctx: KeyAndStringContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.keyAndString`.
     * @param ctx the parse tree
     */
    exitKeyAndString?: (ctx: KeyAndStringContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.intAndString`.
     * @param ctx the parse tree
     */
    enterIntAndString?: (ctx: IntAndStringContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.intAndString`.
     * @param ctx the parse tree
     */
    exitIntAndString?: (ctx: IntAndStringContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.infoOpt`.
     * @param ctx the parse tree
     */
    enterInfoOpt?: (ctx: InfoOptContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.infoOpt`.
     * @param ctx the parse tree
     */
    exitInfoOpt?: (ctx: InfoOptContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.patternOpt`.
     * @param ctx the parse tree
     */
    enterPatternOpt?: (ctx: PatternOptContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.patternOpt`.
     * @param ctx the parse tree
     */
    exitPatternOpt?: (ctx: PatternOptContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.keyOpt`.
     * @param ctx the parse tree
     */
    enterKeyOpt?: (ctx: KeyOptContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.keyOpt`.
     * @param ctx the parse tree
     */
    exitKeyOpt?: (ctx: KeyOptContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.idletimeOpt`.
     * @param ctx the parse tree
     */
    enterIdletimeOpt?: (ctx: IdletimeOptContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.idletimeOpt`.
     * @param ctx the parse tree
     */
    exitIdletimeOpt?: (ctx: IdletimeOptContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.freqOpt`.
     * @param ctx the parse tree
     */
    enterFreqOpt?: (ctx: FreqOptContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.freqOpt`.
     * @param ctx the parse tree
     */
    exitFreqOpt?: (ctx: FreqOptContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.limitOpt`.
     * @param ctx the parse tree
     */
    enterLimitOpt?: (ctx: LimitOptContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.limitOpt`.
     * @param ctx the parse tree
     */
    exitLimitOpt?: (ctx: LimitOptContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.sortOpt`.
     * @param ctx the parse tree
     */
    enterSortOpt?: (ctx: SortOptContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.sortOpt`.
     * @param ctx the parse tree
     */
    exitSortOpt?: (ctx: SortOptContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.getsetOpt`.
     * @param ctx the parse tree
     */
    enterGetsetOpt?: (ctx: GetsetOptContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.getsetOpt`.
     * @param ctx the parse tree
     */
    exitGetsetOpt?: (ctx: GetsetOptContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.lrOpt`.
     * @param ctx the parse tree
     */
    enterLrOpt?: (ctx: LrOptContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.lrOpt`.
     * @param ctx the parse tree
     */
    exitLrOpt?: (ctx: LrOptContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.rankOpt`.
     * @param ctx the parse tree
     */
    enterRankOpt?: (ctx: RankOptContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.rankOpt`.
     * @param ctx the parse tree
     */
    exitRankOpt?: (ctx: RankOptContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.countOpt`.
     * @param ctx the parse tree
     */
    enterCountOpt?: (ctx: CountOptContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.countOpt`.
     * @param ctx the parse tree
     */
    exitCountOpt?: (ctx: CountOptContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.maxlenOpt`.
     * @param ctx the parse tree
     */
    enterMaxlenOpt?: (ctx: MaxlenOptContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.maxlenOpt`.
     * @param ctx the parse tree
     */
    exitMaxlenOpt?: (ctx: MaxlenOptContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdCopy`.
     * @param ctx the parse tree
     */
    enterCmdCopy?: (ctx: CmdCopyContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdCopy`.
     * @param ctx the parse tree
     */
    exitCmdCopy?: (ctx: CmdCopyContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdDel`.
     * @param ctx the parse tree
     */
    enterCmdDel?: (ctx: CmdDelContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdDel`.
     * @param ctx the parse tree
     */
    exitCmdDel?: (ctx: CmdDelContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdDump`.
     * @param ctx the parse tree
     */
    enterCmdDump?: (ctx: CmdDumpContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdDump`.
     * @param ctx the parse tree
     */
    exitCmdDump?: (ctx: CmdDumpContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdExists`.
     * @param ctx the parse tree
     */
    enterCmdExists?: (ctx: CmdExistsContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdExists`.
     * @param ctx the parse tree
     */
    exitCmdExists?: (ctx: CmdExistsContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdExpire`.
     * @param ctx the parse tree
     */
    enterCmdExpire?: (ctx: CmdExpireContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdExpire`.
     * @param ctx the parse tree
     */
    exitCmdExpire?: (ctx: CmdExpireContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdExpireat`.
     * @param ctx the parse tree
     */
    enterCmdExpireat?: (ctx: CmdExpireatContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdExpireat`.
     * @param ctx the parse tree
     */
    exitCmdExpireat?: (ctx: CmdExpireatContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdExpireTime`.
     * @param ctx the parse tree
     */
    enterCmdExpireTime?: (ctx: CmdExpireTimeContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdExpireTime`.
     * @param ctx the parse tree
     */
    exitCmdExpireTime?: (ctx: CmdExpireTimeContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdKeys`.
     * @param ctx the parse tree
     */
    enterCmdKeys?: (ctx: CmdKeysContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdKeys`.
     * @param ctx the parse tree
     */
    exitCmdKeys?: (ctx: CmdKeysContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdMove`.
     * @param ctx the parse tree
     */
    enterCmdMove?: (ctx: CmdMoveContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdMove`.
     * @param ctx the parse tree
     */
    exitCmdMove?: (ctx: CmdMoveContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdObject`.
     * @param ctx the parse tree
     */
    enterCmdObject?: (ctx: CmdObjectContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdObject`.
     * @param ctx the parse tree
     */
    exitCmdObject?: (ctx: CmdObjectContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdPersist`.
     * @param ctx the parse tree
     */
    enterCmdPersist?: (ctx: CmdPersistContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdPersist`.
     * @param ctx the parse tree
     */
    exitCmdPersist?: (ctx: CmdPersistContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdPexpire`.
     * @param ctx the parse tree
     */
    enterCmdPexpire?: (ctx: CmdPexpireContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdPexpire`.
     * @param ctx the parse tree
     */
    exitCmdPexpire?: (ctx: CmdPexpireContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdPexpireat`.
     * @param ctx the parse tree
     */
    enterCmdPexpireat?: (ctx: CmdPexpireatContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdPexpireat`.
     * @param ctx the parse tree
     */
    exitCmdPexpireat?: (ctx: CmdPexpireatContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdPExpireTime`.
     * @param ctx the parse tree
     */
    enterCmdPExpireTime?: (ctx: CmdPExpireTimeContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdPExpireTime`.
     * @param ctx the parse tree
     */
    exitCmdPExpireTime?: (ctx: CmdPExpireTimeContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdTtl`.
     * @param ctx the parse tree
     */
    enterCmdTtl?: (ctx: CmdTtlContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdTtl`.
     * @param ctx the parse tree
     */
    exitCmdTtl?: (ctx: CmdTtlContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdPttl`.
     * @param ctx the parse tree
     */
    enterCmdPttl?: (ctx: CmdPttlContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdPttl`.
     * @param ctx the parse tree
     */
    exitCmdPttl?: (ctx: CmdPttlContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdRandomkey`.
     * @param ctx the parse tree
     */
    enterCmdRandomkey?: (ctx: CmdRandomkeyContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdRandomkey`.
     * @param ctx the parse tree
     */
    exitCmdRandomkey?: (ctx: CmdRandomkeyContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdRename`.
     * @param ctx the parse tree
     */
    enterCmdRename?: (ctx: CmdRenameContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdRename`.
     * @param ctx the parse tree
     */
    exitCmdRename?: (ctx: CmdRenameContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdRenamenx`.
     * @param ctx the parse tree
     */
    enterCmdRenamenx?: (ctx: CmdRenamenxContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdRenamenx`.
     * @param ctx the parse tree
     */
    exitCmdRenamenx?: (ctx: CmdRenamenxContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdRestore`.
     * @param ctx the parse tree
     */
    enterCmdRestore?: (ctx: CmdRestoreContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdRestore`.
     * @param ctx the parse tree
     */
    exitCmdRestore?: (ctx: CmdRestoreContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdScan`.
     * @param ctx the parse tree
     */
    enterCmdScan?: (ctx: CmdScanContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdScan`.
     * @param ctx the parse tree
     */
    exitCmdScan?: (ctx: CmdScanContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdSort`.
     * @param ctx the parse tree
     */
    enterCmdSort?: (ctx: CmdSortContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdSort`.
     * @param ctx the parse tree
     */
    exitCmdSort?: (ctx: CmdSortContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdSortro`.
     * @param ctx the parse tree
     */
    enterCmdSortro?: (ctx: CmdSortroContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdSortro`.
     * @param ctx the parse tree
     */
    exitCmdSortro?: (ctx: CmdSortroContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdTouch`.
     * @param ctx the parse tree
     */
    enterCmdTouch?: (ctx: CmdTouchContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdTouch`.
     * @param ctx the parse tree
     */
    exitCmdTouch?: (ctx: CmdTouchContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdType`.
     * @param ctx the parse tree
     */
    enterCmdType?: (ctx: CmdTypeContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdType`.
     * @param ctx the parse tree
     */
    exitCmdType?: (ctx: CmdTypeContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdUnlink`.
     * @param ctx the parse tree
     */
    enterCmdUnlink?: (ctx: CmdUnlinkContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdUnlink`.
     * @param ctx the parse tree
     */
    exitCmdUnlink?: (ctx: CmdUnlinkContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdWait`.
     * @param ctx the parse tree
     */
    enterCmdWait?: (ctx: CmdWaitContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdWait`.
     * @param ctx the parse tree
     */
    exitCmdWait?: (ctx: CmdWaitContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdAppend`.
     * @param ctx the parse tree
     */
    enterCmdAppend?: (ctx: CmdAppendContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdAppend`.
     * @param ctx the parse tree
     */
    exitCmdAppend?: (ctx: CmdAppendContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdDecr`.
     * @param ctx the parse tree
     */
    enterCmdDecr?: (ctx: CmdDecrContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdDecr`.
     * @param ctx the parse tree
     */
    exitCmdDecr?: (ctx: CmdDecrContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdDecrby`.
     * @param ctx the parse tree
     */
    enterCmdDecrby?: (ctx: CmdDecrbyContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdDecrby`.
     * @param ctx the parse tree
     */
    exitCmdDecrby?: (ctx: CmdDecrbyContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdGet`.
     * @param ctx the parse tree
     */
    enterCmdGet?: (ctx: CmdGetContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdGet`.
     * @param ctx the parse tree
     */
    exitCmdGet?: (ctx: CmdGetContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdGetdel`.
     * @param ctx the parse tree
     */
    enterCmdGetdel?: (ctx: CmdGetdelContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdGetdel`.
     * @param ctx the parse tree
     */
    exitCmdGetdel?: (ctx: CmdGetdelContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdGetex`.
     * @param ctx the parse tree
     */
    enterCmdGetex?: (ctx: CmdGetexContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdGetex`.
     * @param ctx the parse tree
     */
    exitCmdGetex?: (ctx: CmdGetexContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdGetrange`.
     * @param ctx the parse tree
     */
    enterCmdGetrange?: (ctx: CmdGetrangeContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdGetrange`.
     * @param ctx the parse tree
     */
    exitCmdGetrange?: (ctx: CmdGetrangeContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdGetset`.
     * @param ctx the parse tree
     */
    enterCmdGetset?: (ctx: CmdGetsetContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdGetset`.
     * @param ctx the parse tree
     */
    exitCmdGetset?: (ctx: CmdGetsetContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdGetbit`.
     * @param ctx the parse tree
     */
    enterCmdGetbit?: (ctx: CmdGetbitContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdGetbit`.
     * @param ctx the parse tree
     */
    exitCmdGetbit?: (ctx: CmdGetbitContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdInc`.
     * @param ctx the parse tree
     */
    enterCmdInc?: (ctx: CmdIncContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdInc`.
     * @param ctx the parse tree
     */
    exitCmdInc?: (ctx: CmdIncContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdLcs`.
     * @param ctx the parse tree
     */
    enterCmdLcs?: (ctx: CmdLcsContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdLcs`.
     * @param ctx the parse tree
     */
    exitCmdLcs?: (ctx: CmdLcsContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdMget`.
     * @param ctx the parse tree
     */
    enterCmdMget?: (ctx: CmdMgetContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdMget`.
     * @param ctx the parse tree
     */
    exitCmdMget?: (ctx: CmdMgetContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdMset`.
     * @param ctx the parse tree
     */
    enterCmdMset?: (ctx: CmdMsetContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdMset`.
     * @param ctx the parse tree
     */
    exitCmdMset?: (ctx: CmdMsetContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdMsetnx`.
     * @param ctx the parse tree
     */
    enterCmdMsetnx?: (ctx: CmdMsetnxContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdMsetnx`.
     * @param ctx the parse tree
     */
    exitCmdMsetnx?: (ctx: CmdMsetnxContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdSetex`.
     * @param ctx the parse tree
     */
    enterCmdSetex?: (ctx: CmdSetexContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdSetex`.
     * @param ctx the parse tree
     */
    exitCmdSetex?: (ctx: CmdSetexContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdSet`.
     * @param ctx the parse tree
     */
    enterCmdSet?: (ctx: CmdSetContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdSet`.
     * @param ctx the parse tree
     */
    exitCmdSet?: (ctx: CmdSetContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdSetnx`.
     * @param ctx the parse tree
     */
    enterCmdSetnx?: (ctx: CmdSetnxContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdSetnx`.
     * @param ctx the parse tree
     */
    exitCmdSetnx?: (ctx: CmdSetnxContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdSetrange`.
     * @param ctx the parse tree
     */
    enterCmdSetrange?: (ctx: CmdSetrangeContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdSetrange`.
     * @param ctx the parse tree
     */
    exitCmdSetrange?: (ctx: CmdSetrangeContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdSetbit`.
     * @param ctx the parse tree
     */
    enterCmdSetbit?: (ctx: CmdSetbitContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdSetbit`.
     * @param ctx the parse tree
     */
    exitCmdSetbit?: (ctx: CmdSetbitContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdStrlen`.
     * @param ctx the parse tree
     */
    enterCmdStrlen?: (ctx: CmdStrlenContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdStrlen`.
     * @param ctx the parse tree
     */
    exitCmdStrlen?: (ctx: CmdStrlenContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdSubstr`.
     * @param ctx the parse tree
     */
    enterCmdSubstr?: (ctx: CmdSubstrContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdSubstr`.
     * @param ctx the parse tree
     */
    exitCmdSubstr?: (ctx: CmdSubstrContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdHdelMget`.
     * @param ctx the parse tree
     */
    enterCmdHdelMget?: (ctx: CmdHdelMgetContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdHdelMget`.
     * @param ctx the parse tree
     */
    exitCmdHdelMget?: (ctx: CmdHdelMgetContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdHexistsGetKeysStrlen`.
     * @param ctx the parse tree
     */
    enterCmdHexistsGetKeysStrlen?: (ctx: CmdHexistsGetKeysStrlenContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdHexistsGetKeysStrlen`.
     * @param ctx the parse tree
     */
    exitCmdHexistsGetKeysStrlen?: (ctx: CmdHexistsGetKeysStrlenContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdHgetallLenVals`.
     * @param ctx the parse tree
     */
    enterCmdHgetallLenVals?: (ctx: CmdHgetallLenValsContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdHgetallLenVals`.
     * @param ctx the parse tree
     */
    exitCmdHgetallLenVals?: (ctx: CmdHgetallLenValsContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdHmsetHset`.
     * @param ctx the parse tree
     */
    enterCmdHmsetHset?: (ctx: CmdHmsetHsetContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdHmsetHset`.
     * @param ctx the parse tree
     */
    exitCmdHmsetHset?: (ctx: CmdHmsetHsetContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdHincrby`.
     * @param ctx the parse tree
     */
    enterCmdHincrby?: (ctx: CmdHincrbyContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdHincrby`.
     * @param ctx the parse tree
     */
    exitCmdHincrby?: (ctx: CmdHincrbyContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdHrandfield`.
     * @param ctx the parse tree
     */
    enterCmdHrandfield?: (ctx: CmdHrandfieldContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdHrandfield`.
     * @param ctx the parse tree
     */
    exitCmdHrandfield?: (ctx: CmdHrandfieldContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdHscan`.
     * @param ctx the parse tree
     */
    enterCmdHscan?: (ctx: CmdHscanContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdHscan`.
     * @param ctx the parse tree
     */
    exitCmdHscan?: (ctx: CmdHscanContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdHsetnx`.
     * @param ctx the parse tree
     */
    enterCmdHsetnx?: (ctx: CmdHsetnxContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdHsetnx`.
     * @param ctx the parse tree
     */
    exitCmdHsetnx?: (ctx: CmdHsetnxContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdBlmove`.
     * @param ctx the parse tree
     */
    enterCmdBlmove?: (ctx: CmdBlmoveContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdBlmove`.
     * @param ctx the parse tree
     */
    exitCmdBlmove?: (ctx: CmdBlmoveContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdLmpop`.
     * @param ctx the parse tree
     */
    enterCmdLmpop?: (ctx: CmdLmpopContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdLmpop`.
     * @param ctx the parse tree
     */
    exitCmdLmpop?: (ctx: CmdLmpopContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdBpop`.
     * @param ctx the parse tree
     */
    enterCmdBpop?: (ctx: CmdBpopContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdBpop`.
     * @param ctx the parse tree
     */
    exitCmdBpop?: (ctx: CmdBpopContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdBrpoplpush`.
     * @param ctx the parse tree
     */
    enterCmdBrpoplpush?: (ctx: CmdBrpoplpushContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdBrpoplpush`.
     * @param ctx the parse tree
     */
    exitCmdBrpoplpush?: (ctx: CmdBrpoplpushContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdLindex`.
     * @param ctx the parse tree
     */
    enterCmdLindex?: (ctx: CmdLindexContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdLindex`.
     * @param ctx the parse tree
     */
    exitCmdLindex?: (ctx: CmdLindexContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdLinsert`.
     * @param ctx the parse tree
     */
    enterCmdLinsert?: (ctx: CmdLinsertContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdLinsert`.
     * @param ctx the parse tree
     */
    exitCmdLinsert?: (ctx: CmdLinsertContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdLlen`.
     * @param ctx the parse tree
     */
    enterCmdLlen?: (ctx: CmdLlenContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdLlen`.
     * @param ctx the parse tree
     */
    exitCmdLlen?: (ctx: CmdLlenContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdLmove`.
     * @param ctx the parse tree
     */
    enterCmdLmove?: (ctx: CmdLmoveContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdLmove`.
     * @param ctx the parse tree
     */
    exitCmdLmove?: (ctx: CmdLmoveContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdPop`.
     * @param ctx the parse tree
     */
    enterCmdPop?: (ctx: CmdPopContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdPop`.
     * @param ctx the parse tree
     */
    exitCmdPop?: (ctx: CmdPopContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdLpos`.
     * @param ctx the parse tree
     */
    enterCmdLpos?: (ctx: CmdLposContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdLpos`.
     * @param ctx the parse tree
     */
    exitCmdLpos?: (ctx: CmdLposContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdPush`.
     * @param ctx the parse tree
     */
    enterCmdPush?: (ctx: CmdPushContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdPush`.
     * @param ctx the parse tree
     */
    exitCmdPush?: (ctx: CmdPushContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdLrangeTrim`.
     * @param ctx the parse tree
     */
    enterCmdLrangeTrim?: (ctx: CmdLrangeTrimContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdLrangeTrim`.
     * @param ctx the parse tree
     */
    exitCmdLrangeTrim?: (ctx: CmdLrangeTrimContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdLremSet`.
     * @param ctx the parse tree
     */
    enterCmdLremSet?: (ctx: CmdLremSetContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdLremSet`.
     * @param ctx the parse tree
     */
    exitCmdLremSet?: (ctx: CmdLremSetContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdRpoplpush`.
     * @param ctx the parse tree
     */
    enterCmdRpoplpush?: (ctx: CmdRpoplpushContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdRpoplpush`.
     * @param ctx the parse tree
     */
    exitCmdRpoplpush?: (ctx: CmdRpoplpushContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdSadd`.
     * @param ctx the parse tree
     */
    enterCmdSadd?: (ctx: CmdSaddContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdSadd`.
     * @param ctx the parse tree
     */
    exitCmdSadd?: (ctx: CmdSaddContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdScard`.
     * @param ctx the parse tree
     */
    enterCmdScard?: (ctx: CmdScardContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdScard`.
     * @param ctx the parse tree
     */
    exitCmdScard?: (ctx: CmdScardContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdSdiff`.
     * @param ctx the parse tree
     */
    enterCmdSdiff?: (ctx: CmdSdiffContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdSdiff`.
     * @param ctx the parse tree
     */
    exitCmdSdiff?: (ctx: CmdSdiffContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdSdiffstore`.
     * @param ctx the parse tree
     */
    enterCmdSdiffstore?: (ctx: CmdSdiffstoreContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdSdiffstore`.
     * @param ctx the parse tree
     */
    exitCmdSdiffstore?: (ctx: CmdSdiffstoreContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdSintercard`.
     * @param ctx the parse tree
     */
    enterCmdSintercard?: (ctx: CmdSintercardContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdSintercard`.
     * @param ctx the parse tree
     */
    exitCmdSintercard?: (ctx: CmdSintercardContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdSismember`.
     * @param ctx the parse tree
     */
    enterCmdSismember?: (ctx: CmdSismemberContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdSismember`.
     * @param ctx the parse tree
     */
    exitCmdSismember?: (ctx: CmdSismemberContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdSmove`.
     * @param ctx the parse tree
     */
    enterCmdSmove?: (ctx: CmdSmoveContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdSmove`.
     * @param ctx the parse tree
     */
    exitCmdSmove?: (ctx: CmdSmoveContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdSpop`.
     * @param ctx the parse tree
     */
    enterCmdSpop?: (ctx: CmdSpopContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdSpop`.
     * @param ctx the parse tree
     */
    exitCmdSpop?: (ctx: CmdSpopContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdSscan`.
     * @param ctx the parse tree
     */
    enterCmdSscan?: (ctx: CmdSscanContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdSscan`.
     * @param ctx the parse tree
     */
    exitCmdSscan?: (ctx: CmdSscanContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdBzmpop`.
     * @param ctx the parse tree
     */
    enterCmdBzmpop?: (ctx: CmdBzmpopContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdBzmpop`.
     * @param ctx the parse tree
     */
    exitCmdBzmpop?: (ctx: CmdBzmpopContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdBzpopmax`.
     * @param ctx the parse tree
     */
    enterCmdBzpopmax?: (ctx: CmdBzpopmaxContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdBzpopmax`.
     * @param ctx the parse tree
     */
    exitCmdBzpopmax?: (ctx: CmdBzpopmaxContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdZadd`.
     * @param ctx the parse tree
     */
    enterCmdZadd?: (ctx: CmdZaddContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdZadd`.
     * @param ctx the parse tree
     */
    exitCmdZadd?: (ctx: CmdZaddContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdZcard`.
     * @param ctx the parse tree
     */
    enterCmdZcard?: (ctx: CmdZcardContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdZcard`.
     * @param ctx the parse tree
     */
    exitCmdZcard?: (ctx: CmdZcardContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdZcount`.
     * @param ctx the parse tree
     */
    enterCmdZcount?: (ctx: CmdZcountContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdZcount`.
     * @param ctx the parse tree
     */
    exitCmdZcount?: (ctx: CmdZcountContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdZdiff`.
     * @param ctx the parse tree
     */
    enterCmdZdiff?: (ctx: CmdZdiffContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdZdiff`.
     * @param ctx the parse tree
     */
    exitCmdZdiff?: (ctx: CmdZdiffContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdZdiffstore`.
     * @param ctx the parse tree
     */
    enterCmdZdiffstore?: (ctx: CmdZdiffstoreContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdZdiffstore`.
     * @param ctx the parse tree
     */
    exitCmdZdiffstore?: (ctx: CmdZdiffstoreContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdZincrby`.
     * @param ctx the parse tree
     */
    enterCmdZincrby?: (ctx: CmdZincrbyContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdZincrby`.
     * @param ctx the parse tree
     */
    exitCmdZincrby?: (ctx: CmdZincrbyContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdZinter`.
     * @param ctx the parse tree
     */
    enterCmdZinter?: (ctx: CmdZinterContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdZinter`.
     * @param ctx the parse tree
     */
    exitCmdZinter?: (ctx: CmdZinterContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdZintercard`.
     * @param ctx the parse tree
     */
    enterCmdZintercard?: (ctx: CmdZintercardContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdZintercard`.
     * @param ctx the parse tree
     */
    exitCmdZintercard?: (ctx: CmdZintercardContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdZinterstore`.
     * @param ctx the parse tree
     */
    enterCmdZinterstore?: (ctx: CmdZinterstoreContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdZinterstore`.
     * @param ctx the parse tree
     */
    exitCmdZinterstore?: (ctx: CmdZinterstoreContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdZmpop`.
     * @param ctx the parse tree
     */
    enterCmdZmpop?: (ctx: CmdZmpopContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdZmpop`.
     * @param ctx the parse tree
     */
    exitCmdZmpop?: (ctx: CmdZmpopContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdZmscore`.
     * @param ctx the parse tree
     */
    enterCmdZmscore?: (ctx: CmdZmscoreContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdZmscore`.
     * @param ctx the parse tree
     */
    exitCmdZmscore?: (ctx: CmdZmscoreContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdZpopmax`.
     * @param ctx the parse tree
     */
    enterCmdZpopmax?: (ctx: CmdZpopmaxContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdZpopmax`.
     * @param ctx the parse tree
     */
    exitCmdZpopmax?: (ctx: CmdZpopmaxContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdZrandmember`.
     * @param ctx the parse tree
     */
    enterCmdZrandmember?: (ctx: CmdZrandmemberContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdZrandmember`.
     * @param ctx the parse tree
     */
    exitCmdZrandmember?: (ctx: CmdZrandmemberContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdZrange`.
     * @param ctx the parse tree
     */
    enterCmdZrange?: (ctx: CmdZrangeContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdZrange`.
     * @param ctx the parse tree
     */
    exitCmdZrange?: (ctx: CmdZrangeContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdZrangebylex`.
     * @param ctx the parse tree
     */
    enterCmdZrangebylex?: (ctx: CmdZrangebylexContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdZrangebylex`.
     * @param ctx the parse tree
     */
    exitCmdZrangebylex?: (ctx: CmdZrangebylexContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdZrangebyscore`.
     * @param ctx the parse tree
     */
    enterCmdZrangebyscore?: (ctx: CmdZrangebyscoreContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdZrangebyscore`.
     * @param ctx the parse tree
     */
    exitCmdZrangebyscore?: (ctx: CmdZrangebyscoreContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdZrangestore`.
     * @param ctx the parse tree
     */
    enterCmdZrangestore?: (ctx: CmdZrangestoreContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdZrangestore`.
     * @param ctx the parse tree
     */
    exitCmdZrangestore?: (ctx: CmdZrangestoreContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdZrank`.
     * @param ctx the parse tree
     */
    enterCmdZrank?: (ctx: CmdZrankContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdZrank`.
     * @param ctx the parse tree
     */
    exitCmdZrank?: (ctx: CmdZrankContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdZremrangebyrank`.
     * @param ctx the parse tree
     */
    enterCmdZremrangebyrank?: (ctx: CmdZremrangebyrankContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdZremrangebyrank`.
     * @param ctx the parse tree
     */
    exitCmdZremrangebyrank?: (ctx: CmdZremrangebyrankContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdZrevrange`.
     * @param ctx the parse tree
     */
    enterCmdZrevrange?: (ctx: CmdZrevrangeContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdZrevrange`.
     * @param ctx the parse tree
     */
    exitCmdZrevrange?: (ctx: CmdZrevrangeContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdZrevrangebylex`.
     * @param ctx the parse tree
     */
    enterCmdZrevrangebylex?: (ctx: CmdZrevrangebylexContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdZrevrangebylex`.
     * @param ctx the parse tree
     */
    exitCmdZrevrangebylex?: (ctx: CmdZrevrangebylexContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdZscan`.
     * @param ctx the parse tree
     */
    enterCmdZscan?: (ctx: CmdZscanContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdZscan`.
     * @param ctx the parse tree
     */
    exitCmdZscan?: (ctx: CmdZscanContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdZunion`.
     * @param ctx the parse tree
     */
    enterCmdZunion?: (ctx: CmdZunionContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdZunion`.
     * @param ctx the parse tree
     */
    exitCmdZunion?: (ctx: CmdZunionContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdZunionstore`.
     * @param ctx the parse tree
     */
    enterCmdZunionstore?: (ctx: CmdZunionstoreContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdZunionstore`.
     * @param ctx the parse tree
     */
    exitCmdZunionstore?: (ctx: CmdZunionstoreContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdScriptKill`.
     * @param ctx the parse tree
     */
    enterCmdScriptKill?: (ctx: CmdScriptKillContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdScriptKill`.
     * @param ctx the parse tree
     */
    exitCmdScriptKill?: (ctx: CmdScriptKillContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdScriptLoad`.
     * @param ctx the parse tree
     */
    enterCmdScriptLoad?: (ctx: CmdScriptLoadContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdScriptLoad`.
     * @param ctx the parse tree
     */
    exitCmdScriptLoad?: (ctx: CmdScriptLoadContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdEval`.
     * @param ctx the parse tree
     */
    enterCmdEval?: (ctx: CmdEvalContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdEval`.
     * @param ctx the parse tree
     */
    exitCmdEval?: (ctx: CmdEvalContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdEvalsha`.
     * @param ctx the parse tree
     */
    enterCmdEvalsha?: (ctx: CmdEvalshaContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdEvalsha`.
     * @param ctx the parse tree
     */
    exitCmdEvalsha?: (ctx: CmdEvalshaContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdScriptExists`.
     * @param ctx the parse tree
     */
    enterCmdScriptExists?: (ctx: CmdScriptExistsContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdScriptExists`.
     * @param ctx the parse tree
     */
    exitCmdScriptExists?: (ctx: CmdScriptExistsContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdScriptFlush`.
     * @param ctx the parse tree
     */
    enterCmdScriptFlush?: (ctx: CmdScriptFlushContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdScriptFlush`.
     * @param ctx the parse tree
     */
    exitCmdScriptFlush?: (ctx: CmdScriptFlushContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdExec`.
     * @param ctx the parse tree
     */
    enterCmdExec?: (ctx: CmdExecContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdExec`.
     * @param ctx the parse tree
     */
    exitCmdExec?: (ctx: CmdExecContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdWatch`.
     * @param ctx the parse tree
     */
    enterCmdWatch?: (ctx: CmdWatchContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdWatch`.
     * @param ctx the parse tree
     */
    exitCmdWatch?: (ctx: CmdWatchContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdDiscard`.
     * @param ctx the parse tree
     */
    enterCmdDiscard?: (ctx: CmdDiscardContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdDiscard`.
     * @param ctx the parse tree
     */
    exitCmdDiscard?: (ctx: CmdDiscardContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdUnwatch`.
     * @param ctx the parse tree
     */
    enterCmdUnwatch?: (ctx: CmdUnwatchContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdUnwatch`.
     * @param ctx the parse tree
     */
    exitCmdUnwatch?: (ctx: CmdUnwatchContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdMulti`.
     * @param ctx the parse tree
     */
    enterCmdMulti?: (ctx: CmdMultiContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdMulti`.
     * @param ctx the parse tree
     */
    exitCmdMulti?: (ctx: CmdMultiContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdPfmerge`.
     * @param ctx the parse tree
     */
    enterCmdPfmerge?: (ctx: CmdPfmergeContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdPfmerge`.
     * @param ctx the parse tree
     */
    exitCmdPfmerge?: (ctx: CmdPfmergeContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdPfadd`.
     * @param ctx the parse tree
     */
    enterCmdPfadd?: (ctx: CmdPfaddContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdPfadd`.
     * @param ctx the parse tree
     */
    exitCmdPfadd?: (ctx: CmdPfaddContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdPfcount`.
     * @param ctx the parse tree
     */
    enterCmdPfcount?: (ctx: CmdPfcountContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdPfcount`.
     * @param ctx the parse tree
     */
    exitCmdPfcount?: (ctx: CmdPfcountContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdSsubscribe`.
     * @param ctx the parse tree
     */
    enterCmdSsubscribe?: (ctx: CmdSsubscribeContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdSsubscribe`.
     * @param ctx the parse tree
     */
    exitCmdSsubscribe?: (ctx: CmdSsubscribeContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdUnsubscribe`.
     * @param ctx the parse tree
     */
    enterCmdUnsubscribe?: (ctx: CmdUnsubscribeContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdUnsubscribe`.
     * @param ctx the parse tree
     */
    exitCmdUnsubscribe?: (ctx: CmdUnsubscribeContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdPusubnumpat`.
     * @param ctx the parse tree
     */
    enterCmdPusubnumpat?: (ctx: CmdPusubnumpatContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdPusubnumpat`.
     * @param ctx the parse tree
     */
    exitCmdPusubnumpat?: (ctx: CmdPusubnumpatContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdPusubnumsub`.
     * @param ctx the parse tree
     */
    enterCmdPusubnumsub?: (ctx: CmdPusubnumsubContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdPusubnumsub`.
     * @param ctx the parse tree
     */
    exitCmdPusubnumsub?: (ctx: CmdPusubnumsubContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdPusubchannels`.
     * @param ctx the parse tree
     */
    enterCmdPusubchannels?: (ctx: CmdPusubchannelsContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdPusubchannels`.
     * @param ctx the parse tree
     */
    exitCmdPusubchannels?: (ctx: CmdPusubchannelsContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdPublish`.
     * @param ctx the parse tree
     */
    enterCmdPublish?: (ctx: CmdPublishContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdPublish`.
     * @param ctx the parse tree
     */
    exitCmdPublish?: (ctx: CmdPublishContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdAsking`.
     * @param ctx the parse tree
     */
    enterCmdAsking?: (ctx: CmdAskingContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdAsking`.
     * @param ctx the parse tree
     */
    exitCmdAsking?: (ctx: CmdAskingContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdReadonly`.
     * @param ctx the parse tree
     */
    enterCmdReadonly?: (ctx: CmdReadonlyContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdReadonly`.
     * @param ctx the parse tree
     */
    exitCmdReadonly?: (ctx: CmdReadonlyContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdReadwrite`.
     * @param ctx the parse tree
     */
    enterCmdReadwrite?: (ctx: CmdReadwriteContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdReadwrite`.
     * @param ctx the parse tree
     */
    exitCmdReadwrite?: (ctx: CmdReadwriteContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdAddDelSlots`.
     * @param ctx the parse tree
     */
    enterCmdAddDelSlots?: (ctx: CmdAddDelSlotsContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdAddDelSlots`.
     * @param ctx the parse tree
     */
    exitCmdAddDelSlots?: (ctx: CmdAddDelSlotsContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdCountKeysInSlot`.
     * @param ctx the parse tree
     */
    enterCmdCountKeysInSlot?: (ctx: CmdCountKeysInSlotContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdCountKeysInSlot`.
     * @param ctx the parse tree
     */
    exitCmdCountKeysInSlot?: (ctx: CmdCountKeysInSlotContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdFailOver`.
     * @param ctx the parse tree
     */
    enterCmdFailOver?: (ctx: CmdFailOverContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdFailOver`.
     * @param ctx the parse tree
     */
    exitCmdFailOver?: (ctx: CmdFailOverContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdForget`.
     * @param ctx the parse tree
     */
    enterCmdForget?: (ctx: CmdForgetContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdForget`.
     * @param ctx the parse tree
     */
    exitCmdForget?: (ctx: CmdForgetContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdGetKeysInSlot`.
     * @param ctx the parse tree
     */
    enterCmdGetKeysInSlot?: (ctx: CmdGetKeysInSlotContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdGetKeysInSlot`.
     * @param ctx the parse tree
     */
    exitCmdGetKeysInSlot?: (ctx: CmdGetKeysInSlotContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdClusterOrder`.
     * @param ctx the parse tree
     */
    enterCmdClusterOrder?: (ctx: CmdClusterOrderContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdClusterOrder`.
     * @param ctx the parse tree
     */
    exitCmdClusterOrder?: (ctx: CmdClusterOrderContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdKeySlot`.
     * @param ctx the parse tree
     */
    enterCmdKeySlot?: (ctx: CmdKeySlotContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdKeySlot`.
     * @param ctx the parse tree
     */
    exitCmdKeySlot?: (ctx: CmdKeySlotContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdMeet`.
     * @param ctx the parse tree
     */
    enterCmdMeet?: (ctx: CmdMeetContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdMeet`.
     * @param ctx the parse tree
     */
    exitCmdMeet?: (ctx: CmdMeetContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdReplicaesSlave`.
     * @param ctx the parse tree
     */
    enterCmdReplicaesSlave?: (ctx: CmdReplicaesSlaveContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdReplicaesSlave`.
     * @param ctx the parse tree
     */
    exitCmdReplicaesSlave?: (ctx: CmdReplicaesSlaveContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdReset`.
     * @param ctx the parse tree
     */
    enterCmdReset?: (ctx: CmdResetContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdReset`.
     * @param ctx the parse tree
     */
    exitCmdReset?: (ctx: CmdResetContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdSetSlot`.
     * @param ctx the parse tree
     */
    enterCmdSetSlot?: (ctx: CmdSetSlotContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdSetSlot`.
     * @param ctx the parse tree
     */
    exitCmdSetSlot?: (ctx: CmdSetSlotContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdFlushdb`.
     * @param ctx the parse tree
     */
    enterCmdFlushdb?: (ctx: CmdFlushdbContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdFlushdb`.
     * @param ctx the parse tree
     */
    exitCmdFlushdb?: (ctx: CmdFlushdbContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdEcho`.
     * @param ctx the parse tree
     */
    enterCmdEcho?: (ctx: CmdEchoContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdEcho`.
     * @param ctx the parse tree
     */
    exitCmdEcho?: (ctx: CmdEchoContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdSave`.
     * @param ctx the parse tree
     */
    enterCmdSave?: (ctx: CmdSaveContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdSave`.
     * @param ctx the parse tree
     */
    exitCmdSave?: (ctx: CmdSaveContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdSlowlog`.
     * @param ctx the parse tree
     */
    enterCmdSlowlog?: (ctx: CmdSlowlogContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdSlowlog`.
     * @param ctx the parse tree
     */
    exitCmdSlowlog?: (ctx: CmdSlowlogContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdLastsave`.
     * @param ctx the parse tree
     */
    enterCmdLastsave?: (ctx: CmdLastsaveContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdLastsave`.
     * @param ctx the parse tree
     */
    exitCmdLastsave?: (ctx: CmdLastsaveContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdConfig`.
     * @param ctx the parse tree
     */
    enterCmdConfig?: (ctx: CmdConfigContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdConfig`.
     * @param ctx the parse tree
     */
    exitCmdConfig?: (ctx: CmdConfigContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdClient`.
     * @param ctx the parse tree
     */
    enterCmdClient?: (ctx: CmdClientContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdClient`.
     * @param ctx the parse tree
     */
    exitCmdClient?: (ctx: CmdClientContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdShutdown`.
     * @param ctx the parse tree
     */
    enterCmdShutdown?: (ctx: CmdShutdownContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdShutdown`.
     * @param ctx the parse tree
     */
    exitCmdShutdown?: (ctx: CmdShutdownContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdSync`.
     * @param ctx the parse tree
     */
    enterCmdSync?: (ctx: CmdSyncContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdSync`.
     * @param ctx the parse tree
     */
    exitCmdSync?: (ctx: CmdSyncContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdRole`.
     * @param ctx the parse tree
     */
    enterCmdRole?: (ctx: CmdRoleContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdRole`.
     * @param ctx the parse tree
     */
    exitCmdRole?: (ctx: CmdRoleContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdMonitor`.
     * @param ctx the parse tree
     */
    enterCmdMonitor?: (ctx: CmdMonitorContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdMonitor`.
     * @param ctx the parse tree
     */
    exitCmdMonitor?: (ctx: CmdMonitorContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdSlaveof`.
     * @param ctx the parse tree
     */
    enterCmdSlaveof?: (ctx: CmdSlaveofContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdSlaveof`.
     * @param ctx the parse tree
     */
    exitCmdSlaveof?: (ctx: CmdSlaveofContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdFlushall`.
     * @param ctx the parse tree
     */
    enterCmdFlushall?: (ctx: CmdFlushallContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdFlushall`.
     * @param ctx the parse tree
     */
    exitCmdFlushall?: (ctx: CmdFlushallContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdSelect`.
     * @param ctx the parse tree
     */
    enterCmdSelect?: (ctx: CmdSelectContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdSelect`.
     * @param ctx the parse tree
     */
    exitCmdSelect?: (ctx: CmdSelectContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdPing`.
     * @param ctx the parse tree
     */
    enterCmdPing?: (ctx: CmdPingContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdPing`.
     * @param ctx the parse tree
     */
    exitCmdPing?: (ctx: CmdPingContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdQuit`.
     * @param ctx the parse tree
     */
    enterCmdQuit?: (ctx: CmdQuitContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdQuit`.
     * @param ctx the parse tree
     */
    exitCmdQuit?: (ctx: CmdQuitContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdAuth`.
     * @param ctx the parse tree
     */
    enterCmdAuth?: (ctx: CmdAuthContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdAuth`.
     * @param ctx the parse tree
     */
    exitCmdAuth?: (ctx: CmdAuthContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdDbsize`.
     * @param ctx the parse tree
     */
    enterCmdDbsize?: (ctx: CmdDbsizeContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdDbsize`.
     * @param ctx the parse tree
     */
    exitCmdDbsize?: (ctx: CmdDbsizeContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdBgrewriteaof`.
     * @param ctx the parse tree
     */
    enterCmdBgrewriteaof?: (ctx: CmdBgrewriteaofContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdBgrewriteaof`.
     * @param ctx the parse tree
     */
    exitCmdBgrewriteaof?: (ctx: CmdBgrewriteaofContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdTime`.
     * @param ctx the parse tree
     */
    enterCmdTime?: (ctx: CmdTimeContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdTime`.
     * @param ctx the parse tree
     */
    exitCmdTime?: (ctx: CmdTimeContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdInfo`.
     * @param ctx the parse tree
     */
    enterCmdInfo?: (ctx: CmdInfoContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdInfo`.
     * @param ctx the parse tree
     */
    exitCmdInfo?: (ctx: CmdInfoContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdBgsave`.
     * @param ctx the parse tree
     */
    enterCmdBgsave?: (ctx: CmdBgsaveContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdBgsave`.
     * @param ctx the parse tree
     */
    exitCmdBgsave?: (ctx: CmdBgsaveContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdCmd`.
     * @param ctx the parse tree
     */
    enterCmdCmd?: (ctx: CmdCmdContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdCmd`.
     * @param ctx the parse tree
     */
    exitCmdCmd?: (ctx: CmdCmdContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdCmdx`.
     * @param ctx the parse tree
     */
    enterCmdCmdx?: (ctx: CmdCmdxContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdCmdx`.
     * @param ctx the parse tree
     */
    exitCmdCmdx?: (ctx: CmdCmdxContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.cmdDebug`.
     * @param ctx the parse tree
     */
    enterCmdDebug?: (ctx: CmdDebugContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.cmdDebug`.
     * @param ctx the parse tree
     */
    exitCmdDebug?: (ctx: CmdDebugContext) => void;
    /**
     * Enter a parse tree produced by `RedisParser.sqlStatement`.
     * @param ctx the parse tree
     */
    enterSqlStatement?: (ctx: SqlStatementContext) => void;
    /**
     * Exit a parse tree produced by `RedisParser.sqlStatement`.
     * @param ctx the parse tree
     */
    exitSqlStatement?: (ctx: SqlStatementContext) => void;
}
