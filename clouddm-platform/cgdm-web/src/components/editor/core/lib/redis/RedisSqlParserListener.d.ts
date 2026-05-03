import { ParseTreeListener } from "antlr4ts/tree/ParseTreeListener";
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
 * This interface defines a complete listener for a parse tree produced by
 * `RedisSqlParser`.
 */
export interface RedisSqlParserListener extends ParseTreeListener {
    /**
     * Enter a parse tree produced by the `cmdClientPause`
     * labeled alternative in `RedisSqlParser.cmdClient`.
     * @param ctx the parse tree
     */
    enterCmdClientPause?: (ctx: CmdClientPauseContext) => void;
    /**
     * Exit a parse tree produced by the `cmdClientPause`
     * labeled alternative in `RedisSqlParser.cmdClient`.
     * @param ctx the parse tree
     */
    exitCmdClientPause?: (ctx: CmdClientPauseContext) => void;
    /**
     * Enter a parse tree produced by the `cmdClientKill`
     * labeled alternative in `RedisSqlParser.cmdClient`.
     * @param ctx the parse tree
     */
    enterCmdClientKill?: (ctx: CmdClientKillContext) => void;
    /**
     * Exit a parse tree produced by the `cmdClientKill`
     * labeled alternative in `RedisSqlParser.cmdClient`.
     * @param ctx the parse tree
     */
    exitCmdClientKill?: (ctx: CmdClientKillContext) => void;
    /**
     * Enter a parse tree produced by the `cmdClientGetname`
     * labeled alternative in `RedisSqlParser.cmdClient`.
     * @param ctx the parse tree
     */
    enterCmdClientGetname?: (ctx: CmdClientGetnameContext) => void;
    /**
     * Exit a parse tree produced by the `cmdClientGetname`
     * labeled alternative in `RedisSqlParser.cmdClient`.
     * @param ctx the parse tree
     */
    exitCmdClientGetname?: (ctx: CmdClientGetnameContext) => void;
    /**
     * Enter a parse tree produced by the `cmdClientSetname`
     * labeled alternative in `RedisSqlParser.cmdClient`.
     * @param ctx the parse tree
     */
    enterCmdClientSetname?: (ctx: CmdClientSetnameContext) => void;
    /**
     * Exit a parse tree produced by the `cmdClientSetname`
     * labeled alternative in `RedisSqlParser.cmdClient`.
     * @param ctx the parse tree
     */
    exitCmdClientSetname?: (ctx: CmdClientSetnameContext) => void;
    /**
     * Enter a parse tree produced by the `cmdClientList`
     * labeled alternative in `RedisSqlParser.cmdClient`.
     * @param ctx the parse tree
     */
    enterCmdClientList?: (ctx: CmdClientListContext) => void;
    /**
     * Exit a parse tree produced by the `cmdClientList`
     * labeled alternative in `RedisSqlParser.cmdClient`.
     * @param ctx the parse tree
     */
    exitCmdClientList?: (ctx: CmdClientListContext) => void;
    /**
     * Enter a parse tree produced by the `cmdCmdInfo`
     * labeled alternative in `RedisSqlParser.cmdCmdx`.
     * @param ctx the parse tree
     */
    enterCmdCmdInfo?: (ctx: CmdCmdInfoContext) => void;
    /**
     * Exit a parse tree produced by the `cmdCmdInfo`
     * labeled alternative in `RedisSqlParser.cmdCmdx`.
     * @param ctx the parse tree
     */
    exitCmdCmdInfo?: (ctx: CmdCmdInfoContext) => void;
    /**
     * Enter a parse tree produced by the `cmdCmdGetkeys`
     * labeled alternative in `RedisSqlParser.cmdCmdx`.
     * @param ctx the parse tree
     */
    enterCmdCmdGetkeys?: (ctx: CmdCmdGetkeysContext) => void;
    /**
     * Exit a parse tree produced by the `cmdCmdGetkeys`
     * labeled alternative in `RedisSqlParser.cmdCmdx`.
     * @param ctx the parse tree
     */
    exitCmdCmdGetkeys?: (ctx: CmdCmdGetkeysContext) => void;
    /**
     * Enter a parse tree produced by the `cmdCmdCount`
     * labeled alternative in `RedisSqlParser.cmdCmdx`.
     * @param ctx the parse tree
     */
    enterCmdCmdCount?: (ctx: CmdCmdCountContext) => void;
    /**
     * Exit a parse tree produced by the `cmdCmdCount`
     * labeled alternative in `RedisSqlParser.cmdCmdx`.
     * @param ctx the parse tree
     */
    exitCmdCmdCount?: (ctx: CmdCmdCountContext) => void;
    /**
     * Enter a parse tree produced by the `cmdConfigGet`
     * labeled alternative in `RedisSqlParser.cmdConfig`.
     * @param ctx the parse tree
     */
    enterCmdConfigGet?: (ctx: CmdConfigGetContext) => void;
    /**
     * Exit a parse tree produced by the `cmdConfigGet`
     * labeled alternative in `RedisSqlParser.cmdConfig`.
     * @param ctx the parse tree
     */
    exitCmdConfigGet?: (ctx: CmdConfigGetContext) => void;
    /**
     * Enter a parse tree produced by the `cmdConfigSet`
     * labeled alternative in `RedisSqlParser.cmdConfig`.
     * @param ctx the parse tree
     */
    enterCmdConfigSet?: (ctx: CmdConfigSetContext) => void;
    /**
     * Exit a parse tree produced by the `cmdConfigSet`
     * labeled alternative in `RedisSqlParser.cmdConfig`.
     * @param ctx the parse tree
     */
    exitCmdConfigSet?: (ctx: CmdConfigSetContext) => void;
    /**
     * Enter a parse tree produced by the `cmdConfigReset`
     * labeled alternative in `RedisSqlParser.cmdConfig`.
     * @param ctx the parse tree
     */
    enterCmdConfigReset?: (ctx: CmdConfigResetContext) => void;
    /**
     * Exit a parse tree produced by the `cmdConfigReset`
     * labeled alternative in `RedisSqlParser.cmdConfig`.
     * @param ctx the parse tree
     */
    exitCmdConfigReset?: (ctx: CmdConfigResetContext) => void;
    /**
     * Enter a parse tree produced by the `cmdConfigRewrite`
     * labeled alternative in `RedisSqlParser.cmdConfig`.
     * @param ctx the parse tree
     */
    enterCmdConfigRewrite?: (ctx: CmdConfigRewriteContext) => void;
    /**
     * Exit a parse tree produced by the `cmdConfigRewrite`
     * labeled alternative in `RedisSqlParser.cmdConfig`.
     * @param ctx the parse tree
     */
    exitCmdConfigRewrite?: (ctx: CmdConfigRewriteContext) => void;
    /**
     * Enter a parse tree produced by the `cmdIncr`
     * labeled alternative in `RedisSqlParser.cmdInc`.
     * @param ctx the parse tree
     */
    enterCmdIncr?: (ctx: CmdIncrContext) => void;
    /**
     * Exit a parse tree produced by the `cmdIncr`
     * labeled alternative in `RedisSqlParser.cmdInc`.
     * @param ctx the parse tree
     */
    exitCmdIncr?: (ctx: CmdIncrContext) => void;
    /**
     * Enter a parse tree produced by the `cmdIncrby`
     * labeled alternative in `RedisSqlParser.cmdInc`.
     * @param ctx the parse tree
     */
    enterCmdIncrby?: (ctx: CmdIncrbyContext) => void;
    /**
     * Exit a parse tree produced by the `cmdIncrby`
     * labeled alternative in `RedisSqlParser.cmdInc`.
     * @param ctx the parse tree
     */
    exitCmdIncrby?: (ctx: CmdIncrbyContext) => void;
    /**
     * Enter a parse tree produced by the `cmdIncrbyFloat`
     * labeled alternative in `RedisSqlParser.cmdInc`.
     * @param ctx the parse tree
     */
    enterCmdIncrbyFloat?: (ctx: CmdIncrbyFloatContext) => void;
    /**
     * Exit a parse tree produced by the `cmdIncrbyFloat`
     * labeled alternative in `RedisSqlParser.cmdInc`.
     * @param ctx the parse tree
     */
    exitCmdIncrbyFloat?: (ctx: CmdIncrbyFloatContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.program`.
     * @param ctx the parse tree
     */
    enterProgram?: (ctx: ProgramContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.program`.
     * @param ctx the parse tree
     */
    exitProgram?: (ctx: ProgramContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.sqlStatements`.
     * @param ctx the parse tree
     */
    enterSqlStatements?: (ctx: SqlStatementsContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.sqlStatements`.
     * @param ctx the parse tree
     */
    exitSqlStatements?: (ctx: SqlStatementsContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.emptyStatement_`.
     * @param ctx the parse tree
     */
    enterEmptyStatement_?: (ctx: EmptyStatement_Context) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.emptyStatement_`.
     * @param ctx the parse tree
     */
    exitEmptyStatement_?: (ctx: EmptyStatement_Context) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.keyName`.
     * @param ctx the parse tree
     */
    enterKeyName?: (ctx: KeyNameContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.keyName`.
     * @param ctx the parse tree
     */
    exitKeyName?: (ctx: KeyNameContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.fieldName`.
     * @param ctx the parse tree
     */
    enterFieldName?: (ctx: FieldNameContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.fieldName`.
     * @param ctx the parse tree
     */
    exitFieldName?: (ctx: FieldNameContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.stringValue`.
     * @param ctx the parse tree
     */
    enterStringValue?: (ctx: StringValueContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.stringValue`.
     * @param ctx the parse tree
     */
    exitStringValue?: (ctx: StringValueContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.intValue`.
     * @param ctx the parse tree
     */
    enterIntValue?: (ctx: IntValueContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.intValue`.
     * @param ctx the parse tree
     */
    exitIntValue?: (ctx: IntValueContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.floatValue`.
     * @param ctx the parse tree
     */
    enterFloatValue?: (ctx: FloatValueContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.floatValue`.
     * @param ctx the parse tree
     */
    exitFloatValue?: (ctx: FloatValueContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.pattern`.
     * @param ctx the parse tree
     */
    enterPattern?: (ctx: PatternContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.pattern`.
     * @param ctx the parse tree
     */
    exitPattern?: (ctx: PatternContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cursor`.
     * @param ctx the parse tree
     */
    enterCursor?: (ctx: CursorContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cursor`.
     * @param ctx the parse tree
     */
    exitCursor?: (ctx: CursorContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.min`.
     * @param ctx the parse tree
     */
    enterMin?: (ctx: MinContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.min`.
     * @param ctx the parse tree
     */
    exitMin?: (ctx: MinContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.max`.
     * @param ctx the parse tree
     */
    enterMax?: (ctx: MaxContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.max`.
     * @param ctx the parse tree
     */
    exitMax?: (ctx: MaxContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.multiString`.
     * @param ctx the parse tree
     */
    enterMultiString?: (ctx: MultiStringContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.multiString`.
     * @param ctx the parse tree
     */
    exitMultiString?: (ctx: MultiStringContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cout`.
     * @param ctx the parse tree
     */
    enterCout?: (ctx: CoutContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cout`.
     * @param ctx the parse tree
     */
    exitCout?: (ctx: CoutContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.start`.
     * @param ctx the parse tree
     */
    enterStart?: (ctx: StartContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.start`.
     * @param ctx the parse tree
     */
    exitStart?: (ctx: StartContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.end`.
     * @param ctx the parse tree
     */
    enterEnd?: (ctx: EndContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.end`.
     * @param ctx the parse tree
     */
    exitEnd?: (ctx: EndContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.port`.
     * @param ctx the parse tree
     */
    enterPort?: (ctx: PortContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.port`.
     * @param ctx the parse tree
     */
    exitPort?: (ctx: PortContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.keyAndString`.
     * @param ctx the parse tree
     */
    enterKeyAndString?: (ctx: KeyAndStringContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.keyAndString`.
     * @param ctx the parse tree
     */
    exitKeyAndString?: (ctx: KeyAndStringContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.intAndString`.
     * @param ctx the parse tree
     */
    enterIntAndString?: (ctx: IntAndStringContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.intAndString`.
     * @param ctx the parse tree
     */
    exitIntAndString?: (ctx: IntAndStringContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.infoOpt`.
     * @param ctx the parse tree
     */
    enterInfoOpt?: (ctx: InfoOptContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.infoOpt`.
     * @param ctx the parse tree
     */
    exitInfoOpt?: (ctx: InfoOptContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.patternOpt`.
     * @param ctx the parse tree
     */
    enterPatternOpt?: (ctx: PatternOptContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.patternOpt`.
     * @param ctx the parse tree
     */
    exitPatternOpt?: (ctx: PatternOptContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.keyOpt`.
     * @param ctx the parse tree
     */
    enterKeyOpt?: (ctx: KeyOptContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.keyOpt`.
     * @param ctx the parse tree
     */
    exitKeyOpt?: (ctx: KeyOptContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.idletimeOpt`.
     * @param ctx the parse tree
     */
    enterIdletimeOpt?: (ctx: IdletimeOptContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.idletimeOpt`.
     * @param ctx the parse tree
     */
    exitIdletimeOpt?: (ctx: IdletimeOptContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.freqOpt`.
     * @param ctx the parse tree
     */
    enterFreqOpt?: (ctx: FreqOptContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.freqOpt`.
     * @param ctx the parse tree
     */
    exitFreqOpt?: (ctx: FreqOptContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.limitOpt`.
     * @param ctx the parse tree
     */
    enterLimitOpt?: (ctx: LimitOptContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.limitOpt`.
     * @param ctx the parse tree
     */
    exitLimitOpt?: (ctx: LimitOptContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.sortOpt`.
     * @param ctx the parse tree
     */
    enterSortOpt?: (ctx: SortOptContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.sortOpt`.
     * @param ctx the parse tree
     */
    exitSortOpt?: (ctx: SortOptContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.getsetOpt`.
     * @param ctx the parse tree
     */
    enterGetsetOpt?: (ctx: GetsetOptContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.getsetOpt`.
     * @param ctx the parse tree
     */
    exitGetsetOpt?: (ctx: GetsetOptContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.lrOpt`.
     * @param ctx the parse tree
     */
    enterLrOpt?: (ctx: LrOptContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.lrOpt`.
     * @param ctx the parse tree
     */
    exitLrOpt?: (ctx: LrOptContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.rankOpt`.
     * @param ctx the parse tree
     */
    enterRankOpt?: (ctx: RankOptContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.rankOpt`.
     * @param ctx the parse tree
     */
    exitRankOpt?: (ctx: RankOptContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.countOpt`.
     * @param ctx the parse tree
     */
    enterCountOpt?: (ctx: CountOptContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.countOpt`.
     * @param ctx the parse tree
     */
    exitCountOpt?: (ctx: CountOptContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.maxlenOpt`.
     * @param ctx the parse tree
     */
    enterMaxlenOpt?: (ctx: MaxlenOptContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.maxlenOpt`.
     * @param ctx the parse tree
     */
    exitMaxlenOpt?: (ctx: MaxlenOptContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdCopy`.
     * @param ctx the parse tree
     */
    enterCmdCopy?: (ctx: CmdCopyContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdCopy`.
     * @param ctx the parse tree
     */
    exitCmdCopy?: (ctx: CmdCopyContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdDel`.
     * @param ctx the parse tree
     */
    enterCmdDel?: (ctx: CmdDelContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdDel`.
     * @param ctx the parse tree
     */
    exitCmdDel?: (ctx: CmdDelContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdDump`.
     * @param ctx the parse tree
     */
    enterCmdDump?: (ctx: CmdDumpContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdDump`.
     * @param ctx the parse tree
     */
    exitCmdDump?: (ctx: CmdDumpContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdExists`.
     * @param ctx the parse tree
     */
    enterCmdExists?: (ctx: CmdExistsContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdExists`.
     * @param ctx the parse tree
     */
    exitCmdExists?: (ctx: CmdExistsContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdExpire`.
     * @param ctx the parse tree
     */
    enterCmdExpire?: (ctx: CmdExpireContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdExpire`.
     * @param ctx the parse tree
     */
    exitCmdExpire?: (ctx: CmdExpireContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdExpireat`.
     * @param ctx the parse tree
     */
    enterCmdExpireat?: (ctx: CmdExpireatContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdExpireat`.
     * @param ctx the parse tree
     */
    exitCmdExpireat?: (ctx: CmdExpireatContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdExpireTime`.
     * @param ctx the parse tree
     */
    enterCmdExpireTime?: (ctx: CmdExpireTimeContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdExpireTime`.
     * @param ctx the parse tree
     */
    exitCmdExpireTime?: (ctx: CmdExpireTimeContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdKeys`.
     * @param ctx the parse tree
     */
    enterCmdKeys?: (ctx: CmdKeysContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdKeys`.
     * @param ctx the parse tree
     */
    exitCmdKeys?: (ctx: CmdKeysContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdMove`.
     * @param ctx the parse tree
     */
    enterCmdMove?: (ctx: CmdMoveContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdMove`.
     * @param ctx the parse tree
     */
    exitCmdMove?: (ctx: CmdMoveContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdObject`.
     * @param ctx the parse tree
     */
    enterCmdObject?: (ctx: CmdObjectContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdObject`.
     * @param ctx the parse tree
     */
    exitCmdObject?: (ctx: CmdObjectContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdPersist`.
     * @param ctx the parse tree
     */
    enterCmdPersist?: (ctx: CmdPersistContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdPersist`.
     * @param ctx the parse tree
     */
    exitCmdPersist?: (ctx: CmdPersistContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdPexpire`.
     * @param ctx the parse tree
     */
    enterCmdPexpire?: (ctx: CmdPexpireContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdPexpire`.
     * @param ctx the parse tree
     */
    exitCmdPexpire?: (ctx: CmdPexpireContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdPexpireat`.
     * @param ctx the parse tree
     */
    enterCmdPexpireat?: (ctx: CmdPexpireatContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdPexpireat`.
     * @param ctx the parse tree
     */
    exitCmdPexpireat?: (ctx: CmdPexpireatContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdPExpireTime`.
     * @param ctx the parse tree
     */
    enterCmdPExpireTime?: (ctx: CmdPExpireTimeContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdPExpireTime`.
     * @param ctx the parse tree
     */
    exitCmdPExpireTime?: (ctx: CmdPExpireTimeContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdTtl`.
     * @param ctx the parse tree
     */
    enterCmdTtl?: (ctx: CmdTtlContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdTtl`.
     * @param ctx the parse tree
     */
    exitCmdTtl?: (ctx: CmdTtlContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdPttl`.
     * @param ctx the parse tree
     */
    enterCmdPttl?: (ctx: CmdPttlContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdPttl`.
     * @param ctx the parse tree
     */
    exitCmdPttl?: (ctx: CmdPttlContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdRandomkey`.
     * @param ctx the parse tree
     */
    enterCmdRandomkey?: (ctx: CmdRandomkeyContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdRandomkey`.
     * @param ctx the parse tree
     */
    exitCmdRandomkey?: (ctx: CmdRandomkeyContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdRename`.
     * @param ctx the parse tree
     */
    enterCmdRename?: (ctx: CmdRenameContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdRename`.
     * @param ctx the parse tree
     */
    exitCmdRename?: (ctx: CmdRenameContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdRenamenx`.
     * @param ctx the parse tree
     */
    enterCmdRenamenx?: (ctx: CmdRenamenxContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdRenamenx`.
     * @param ctx the parse tree
     */
    exitCmdRenamenx?: (ctx: CmdRenamenxContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdRestore`.
     * @param ctx the parse tree
     */
    enterCmdRestore?: (ctx: CmdRestoreContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdRestore`.
     * @param ctx the parse tree
     */
    exitCmdRestore?: (ctx: CmdRestoreContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdScan`.
     * @param ctx the parse tree
     */
    enterCmdScan?: (ctx: CmdScanContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdScan`.
     * @param ctx the parse tree
     */
    exitCmdScan?: (ctx: CmdScanContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdSort`.
     * @param ctx the parse tree
     */
    enterCmdSort?: (ctx: CmdSortContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdSort`.
     * @param ctx the parse tree
     */
    exitCmdSort?: (ctx: CmdSortContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdSortro`.
     * @param ctx the parse tree
     */
    enterCmdSortro?: (ctx: CmdSortroContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdSortro`.
     * @param ctx the parse tree
     */
    exitCmdSortro?: (ctx: CmdSortroContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdTouch`.
     * @param ctx the parse tree
     */
    enterCmdTouch?: (ctx: CmdTouchContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdTouch`.
     * @param ctx the parse tree
     */
    exitCmdTouch?: (ctx: CmdTouchContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdType`.
     * @param ctx the parse tree
     */
    enterCmdType?: (ctx: CmdTypeContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdType`.
     * @param ctx the parse tree
     */
    exitCmdType?: (ctx: CmdTypeContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdUnlink`.
     * @param ctx the parse tree
     */
    enterCmdUnlink?: (ctx: CmdUnlinkContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdUnlink`.
     * @param ctx the parse tree
     */
    exitCmdUnlink?: (ctx: CmdUnlinkContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdWait`.
     * @param ctx the parse tree
     */
    enterCmdWait?: (ctx: CmdWaitContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdWait`.
     * @param ctx the parse tree
     */
    exitCmdWait?: (ctx: CmdWaitContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdAppend`.
     * @param ctx the parse tree
     */
    enterCmdAppend?: (ctx: CmdAppendContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdAppend`.
     * @param ctx the parse tree
     */
    exitCmdAppend?: (ctx: CmdAppendContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdDecr`.
     * @param ctx the parse tree
     */
    enterCmdDecr?: (ctx: CmdDecrContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdDecr`.
     * @param ctx the parse tree
     */
    exitCmdDecr?: (ctx: CmdDecrContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdDecrby`.
     * @param ctx the parse tree
     */
    enterCmdDecrby?: (ctx: CmdDecrbyContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdDecrby`.
     * @param ctx the parse tree
     */
    exitCmdDecrby?: (ctx: CmdDecrbyContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdGet`.
     * @param ctx the parse tree
     */
    enterCmdGet?: (ctx: CmdGetContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdGet`.
     * @param ctx the parse tree
     */
    exitCmdGet?: (ctx: CmdGetContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdGetdel`.
     * @param ctx the parse tree
     */
    enterCmdGetdel?: (ctx: CmdGetdelContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdGetdel`.
     * @param ctx the parse tree
     */
    exitCmdGetdel?: (ctx: CmdGetdelContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdGetex`.
     * @param ctx the parse tree
     */
    enterCmdGetex?: (ctx: CmdGetexContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdGetex`.
     * @param ctx the parse tree
     */
    exitCmdGetex?: (ctx: CmdGetexContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdGetrange`.
     * @param ctx the parse tree
     */
    enterCmdGetrange?: (ctx: CmdGetrangeContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdGetrange`.
     * @param ctx the parse tree
     */
    exitCmdGetrange?: (ctx: CmdGetrangeContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdGetset`.
     * @param ctx the parse tree
     */
    enterCmdGetset?: (ctx: CmdGetsetContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdGetset`.
     * @param ctx the parse tree
     */
    exitCmdGetset?: (ctx: CmdGetsetContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdGetbit`.
     * @param ctx the parse tree
     */
    enterCmdGetbit?: (ctx: CmdGetbitContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdGetbit`.
     * @param ctx the parse tree
     */
    exitCmdGetbit?: (ctx: CmdGetbitContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdInc`.
     * @param ctx the parse tree
     */
    enterCmdInc?: (ctx: CmdIncContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdInc`.
     * @param ctx the parse tree
     */
    exitCmdInc?: (ctx: CmdIncContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdLcs`.
     * @param ctx the parse tree
     */
    enterCmdLcs?: (ctx: CmdLcsContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdLcs`.
     * @param ctx the parse tree
     */
    exitCmdLcs?: (ctx: CmdLcsContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdMget`.
     * @param ctx the parse tree
     */
    enterCmdMget?: (ctx: CmdMgetContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdMget`.
     * @param ctx the parse tree
     */
    exitCmdMget?: (ctx: CmdMgetContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdMset`.
     * @param ctx the parse tree
     */
    enterCmdMset?: (ctx: CmdMsetContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdMset`.
     * @param ctx the parse tree
     */
    exitCmdMset?: (ctx: CmdMsetContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdMsetnx`.
     * @param ctx the parse tree
     */
    enterCmdMsetnx?: (ctx: CmdMsetnxContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdMsetnx`.
     * @param ctx the parse tree
     */
    exitCmdMsetnx?: (ctx: CmdMsetnxContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdSetex`.
     * @param ctx the parse tree
     */
    enterCmdSetex?: (ctx: CmdSetexContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdSetex`.
     * @param ctx the parse tree
     */
    exitCmdSetex?: (ctx: CmdSetexContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdSet`.
     * @param ctx the parse tree
     */
    enterCmdSet?: (ctx: CmdSetContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdSet`.
     * @param ctx the parse tree
     */
    exitCmdSet?: (ctx: CmdSetContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdSetnx`.
     * @param ctx the parse tree
     */
    enterCmdSetnx?: (ctx: CmdSetnxContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdSetnx`.
     * @param ctx the parse tree
     */
    exitCmdSetnx?: (ctx: CmdSetnxContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdSetrange`.
     * @param ctx the parse tree
     */
    enterCmdSetrange?: (ctx: CmdSetrangeContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdSetrange`.
     * @param ctx the parse tree
     */
    exitCmdSetrange?: (ctx: CmdSetrangeContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdSetbit`.
     * @param ctx the parse tree
     */
    enterCmdSetbit?: (ctx: CmdSetbitContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdSetbit`.
     * @param ctx the parse tree
     */
    exitCmdSetbit?: (ctx: CmdSetbitContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdStrlen`.
     * @param ctx the parse tree
     */
    enterCmdStrlen?: (ctx: CmdStrlenContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdStrlen`.
     * @param ctx the parse tree
     */
    exitCmdStrlen?: (ctx: CmdStrlenContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdSubstr`.
     * @param ctx the parse tree
     */
    enterCmdSubstr?: (ctx: CmdSubstrContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdSubstr`.
     * @param ctx the parse tree
     */
    exitCmdSubstr?: (ctx: CmdSubstrContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdHdelMget`.
     * @param ctx the parse tree
     */
    enterCmdHdelMget?: (ctx: CmdHdelMgetContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdHdelMget`.
     * @param ctx the parse tree
     */
    exitCmdHdelMget?: (ctx: CmdHdelMgetContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdHexistsGetKeysStrlen`.
     * @param ctx the parse tree
     */
    enterCmdHexistsGetKeysStrlen?: (ctx: CmdHexistsGetKeysStrlenContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdHexistsGetKeysStrlen`.
     * @param ctx the parse tree
     */
    exitCmdHexistsGetKeysStrlen?: (ctx: CmdHexistsGetKeysStrlenContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdHgetallLenVals`.
     * @param ctx the parse tree
     */
    enterCmdHgetallLenVals?: (ctx: CmdHgetallLenValsContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdHgetallLenVals`.
     * @param ctx the parse tree
     */
    exitCmdHgetallLenVals?: (ctx: CmdHgetallLenValsContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdHmsetHset`.
     * @param ctx the parse tree
     */
    enterCmdHmsetHset?: (ctx: CmdHmsetHsetContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdHmsetHset`.
     * @param ctx the parse tree
     */
    exitCmdHmsetHset?: (ctx: CmdHmsetHsetContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdHincrby`.
     * @param ctx the parse tree
     */
    enterCmdHincrby?: (ctx: CmdHincrbyContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdHincrby`.
     * @param ctx the parse tree
     */
    exitCmdHincrby?: (ctx: CmdHincrbyContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdHrandfield`.
     * @param ctx the parse tree
     */
    enterCmdHrandfield?: (ctx: CmdHrandfieldContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdHrandfield`.
     * @param ctx the parse tree
     */
    exitCmdHrandfield?: (ctx: CmdHrandfieldContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdHscan`.
     * @param ctx the parse tree
     */
    enterCmdHscan?: (ctx: CmdHscanContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdHscan`.
     * @param ctx the parse tree
     */
    exitCmdHscan?: (ctx: CmdHscanContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdHsetnx`.
     * @param ctx the parse tree
     */
    enterCmdHsetnx?: (ctx: CmdHsetnxContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdHsetnx`.
     * @param ctx the parse tree
     */
    exitCmdHsetnx?: (ctx: CmdHsetnxContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdBlmove`.
     * @param ctx the parse tree
     */
    enterCmdBlmove?: (ctx: CmdBlmoveContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdBlmove`.
     * @param ctx the parse tree
     */
    exitCmdBlmove?: (ctx: CmdBlmoveContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdLmpop`.
     * @param ctx the parse tree
     */
    enterCmdLmpop?: (ctx: CmdLmpopContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdLmpop`.
     * @param ctx the parse tree
     */
    exitCmdLmpop?: (ctx: CmdLmpopContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdBpop`.
     * @param ctx the parse tree
     */
    enterCmdBpop?: (ctx: CmdBpopContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdBpop`.
     * @param ctx the parse tree
     */
    exitCmdBpop?: (ctx: CmdBpopContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdBrpoplpush`.
     * @param ctx the parse tree
     */
    enterCmdBrpoplpush?: (ctx: CmdBrpoplpushContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdBrpoplpush`.
     * @param ctx the parse tree
     */
    exitCmdBrpoplpush?: (ctx: CmdBrpoplpushContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdLindex`.
     * @param ctx the parse tree
     */
    enterCmdLindex?: (ctx: CmdLindexContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdLindex`.
     * @param ctx the parse tree
     */
    exitCmdLindex?: (ctx: CmdLindexContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdLinsert`.
     * @param ctx the parse tree
     */
    enterCmdLinsert?: (ctx: CmdLinsertContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdLinsert`.
     * @param ctx the parse tree
     */
    exitCmdLinsert?: (ctx: CmdLinsertContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdLlen`.
     * @param ctx the parse tree
     */
    enterCmdLlen?: (ctx: CmdLlenContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdLlen`.
     * @param ctx the parse tree
     */
    exitCmdLlen?: (ctx: CmdLlenContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdLmove`.
     * @param ctx the parse tree
     */
    enterCmdLmove?: (ctx: CmdLmoveContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdLmove`.
     * @param ctx the parse tree
     */
    exitCmdLmove?: (ctx: CmdLmoveContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdPop`.
     * @param ctx the parse tree
     */
    enterCmdPop?: (ctx: CmdPopContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdPop`.
     * @param ctx the parse tree
     */
    exitCmdPop?: (ctx: CmdPopContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdLpos`.
     * @param ctx the parse tree
     */
    enterCmdLpos?: (ctx: CmdLposContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdLpos`.
     * @param ctx the parse tree
     */
    exitCmdLpos?: (ctx: CmdLposContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdPush`.
     * @param ctx the parse tree
     */
    enterCmdPush?: (ctx: CmdPushContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdPush`.
     * @param ctx the parse tree
     */
    exitCmdPush?: (ctx: CmdPushContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdLrangeTrim`.
     * @param ctx the parse tree
     */
    enterCmdLrangeTrim?: (ctx: CmdLrangeTrimContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdLrangeTrim`.
     * @param ctx the parse tree
     */
    exitCmdLrangeTrim?: (ctx: CmdLrangeTrimContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdLremSet`.
     * @param ctx the parse tree
     */
    enterCmdLremSet?: (ctx: CmdLremSetContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdLremSet`.
     * @param ctx the parse tree
     */
    exitCmdLremSet?: (ctx: CmdLremSetContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdRpoplpush`.
     * @param ctx the parse tree
     */
    enterCmdRpoplpush?: (ctx: CmdRpoplpushContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdRpoplpush`.
     * @param ctx the parse tree
     */
    exitCmdRpoplpush?: (ctx: CmdRpoplpushContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdSadd`.
     * @param ctx the parse tree
     */
    enterCmdSadd?: (ctx: CmdSaddContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdSadd`.
     * @param ctx the parse tree
     */
    exitCmdSadd?: (ctx: CmdSaddContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdScard`.
     * @param ctx the parse tree
     */
    enterCmdScard?: (ctx: CmdScardContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdScard`.
     * @param ctx the parse tree
     */
    exitCmdScard?: (ctx: CmdScardContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdSdiff`.
     * @param ctx the parse tree
     */
    enterCmdSdiff?: (ctx: CmdSdiffContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdSdiff`.
     * @param ctx the parse tree
     */
    exitCmdSdiff?: (ctx: CmdSdiffContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdSdiffstore`.
     * @param ctx the parse tree
     */
    enterCmdSdiffstore?: (ctx: CmdSdiffstoreContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdSdiffstore`.
     * @param ctx the parse tree
     */
    exitCmdSdiffstore?: (ctx: CmdSdiffstoreContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdSintercard`.
     * @param ctx the parse tree
     */
    enterCmdSintercard?: (ctx: CmdSintercardContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdSintercard`.
     * @param ctx the parse tree
     */
    exitCmdSintercard?: (ctx: CmdSintercardContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdSismember`.
     * @param ctx the parse tree
     */
    enterCmdSismember?: (ctx: CmdSismemberContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdSismember`.
     * @param ctx the parse tree
     */
    exitCmdSismember?: (ctx: CmdSismemberContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdSmove`.
     * @param ctx the parse tree
     */
    enterCmdSmove?: (ctx: CmdSmoveContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdSmove`.
     * @param ctx the parse tree
     */
    exitCmdSmove?: (ctx: CmdSmoveContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdSpop`.
     * @param ctx the parse tree
     */
    enterCmdSpop?: (ctx: CmdSpopContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdSpop`.
     * @param ctx the parse tree
     */
    exitCmdSpop?: (ctx: CmdSpopContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdSscan`.
     * @param ctx the parse tree
     */
    enterCmdSscan?: (ctx: CmdSscanContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdSscan`.
     * @param ctx the parse tree
     */
    exitCmdSscan?: (ctx: CmdSscanContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdBzmpop`.
     * @param ctx the parse tree
     */
    enterCmdBzmpop?: (ctx: CmdBzmpopContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdBzmpop`.
     * @param ctx the parse tree
     */
    exitCmdBzmpop?: (ctx: CmdBzmpopContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdBzpopmax`.
     * @param ctx the parse tree
     */
    enterCmdBzpopmax?: (ctx: CmdBzpopmaxContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdBzpopmax`.
     * @param ctx the parse tree
     */
    exitCmdBzpopmax?: (ctx: CmdBzpopmaxContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdZadd`.
     * @param ctx the parse tree
     */
    enterCmdZadd?: (ctx: CmdZaddContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdZadd`.
     * @param ctx the parse tree
     */
    exitCmdZadd?: (ctx: CmdZaddContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdZcard`.
     * @param ctx the parse tree
     */
    enterCmdZcard?: (ctx: CmdZcardContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdZcard`.
     * @param ctx the parse tree
     */
    exitCmdZcard?: (ctx: CmdZcardContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdZcount`.
     * @param ctx the parse tree
     */
    enterCmdZcount?: (ctx: CmdZcountContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdZcount`.
     * @param ctx the parse tree
     */
    exitCmdZcount?: (ctx: CmdZcountContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdZdiff`.
     * @param ctx the parse tree
     */
    enterCmdZdiff?: (ctx: CmdZdiffContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdZdiff`.
     * @param ctx the parse tree
     */
    exitCmdZdiff?: (ctx: CmdZdiffContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdZdiffstore`.
     * @param ctx the parse tree
     */
    enterCmdZdiffstore?: (ctx: CmdZdiffstoreContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdZdiffstore`.
     * @param ctx the parse tree
     */
    exitCmdZdiffstore?: (ctx: CmdZdiffstoreContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdZincrby`.
     * @param ctx the parse tree
     */
    enterCmdZincrby?: (ctx: CmdZincrbyContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdZincrby`.
     * @param ctx the parse tree
     */
    exitCmdZincrby?: (ctx: CmdZincrbyContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdZinter`.
     * @param ctx the parse tree
     */
    enterCmdZinter?: (ctx: CmdZinterContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdZinter`.
     * @param ctx the parse tree
     */
    exitCmdZinter?: (ctx: CmdZinterContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdZintercard`.
     * @param ctx the parse tree
     */
    enterCmdZintercard?: (ctx: CmdZintercardContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdZintercard`.
     * @param ctx the parse tree
     */
    exitCmdZintercard?: (ctx: CmdZintercardContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdZinterstore`.
     * @param ctx the parse tree
     */
    enterCmdZinterstore?: (ctx: CmdZinterstoreContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdZinterstore`.
     * @param ctx the parse tree
     */
    exitCmdZinterstore?: (ctx: CmdZinterstoreContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdZmpop`.
     * @param ctx the parse tree
     */
    enterCmdZmpop?: (ctx: CmdZmpopContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdZmpop`.
     * @param ctx the parse tree
     */
    exitCmdZmpop?: (ctx: CmdZmpopContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdZmscore`.
     * @param ctx the parse tree
     */
    enterCmdZmscore?: (ctx: CmdZmscoreContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdZmscore`.
     * @param ctx the parse tree
     */
    exitCmdZmscore?: (ctx: CmdZmscoreContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdZpopmax`.
     * @param ctx the parse tree
     */
    enterCmdZpopmax?: (ctx: CmdZpopmaxContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdZpopmax`.
     * @param ctx the parse tree
     */
    exitCmdZpopmax?: (ctx: CmdZpopmaxContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdZrandmember`.
     * @param ctx the parse tree
     */
    enterCmdZrandmember?: (ctx: CmdZrandmemberContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdZrandmember`.
     * @param ctx the parse tree
     */
    exitCmdZrandmember?: (ctx: CmdZrandmemberContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdZrange`.
     * @param ctx the parse tree
     */
    enterCmdZrange?: (ctx: CmdZrangeContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdZrange`.
     * @param ctx the parse tree
     */
    exitCmdZrange?: (ctx: CmdZrangeContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdZrangebylex`.
     * @param ctx the parse tree
     */
    enterCmdZrangebylex?: (ctx: CmdZrangebylexContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdZrangebylex`.
     * @param ctx the parse tree
     */
    exitCmdZrangebylex?: (ctx: CmdZrangebylexContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdZrangebyscore`.
     * @param ctx the parse tree
     */
    enterCmdZrangebyscore?: (ctx: CmdZrangebyscoreContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdZrangebyscore`.
     * @param ctx the parse tree
     */
    exitCmdZrangebyscore?: (ctx: CmdZrangebyscoreContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdZrangestore`.
     * @param ctx the parse tree
     */
    enterCmdZrangestore?: (ctx: CmdZrangestoreContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdZrangestore`.
     * @param ctx the parse tree
     */
    exitCmdZrangestore?: (ctx: CmdZrangestoreContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdZrank`.
     * @param ctx the parse tree
     */
    enterCmdZrank?: (ctx: CmdZrankContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdZrank`.
     * @param ctx the parse tree
     */
    exitCmdZrank?: (ctx: CmdZrankContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdZremrangebyrank`.
     * @param ctx the parse tree
     */
    enterCmdZremrangebyrank?: (ctx: CmdZremrangebyrankContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdZremrangebyrank`.
     * @param ctx the parse tree
     */
    exitCmdZremrangebyrank?: (ctx: CmdZremrangebyrankContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdZrevrange`.
     * @param ctx the parse tree
     */
    enterCmdZrevrange?: (ctx: CmdZrevrangeContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdZrevrange`.
     * @param ctx the parse tree
     */
    exitCmdZrevrange?: (ctx: CmdZrevrangeContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdZrevrangebylex`.
     * @param ctx the parse tree
     */
    enterCmdZrevrangebylex?: (ctx: CmdZrevrangebylexContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdZrevrangebylex`.
     * @param ctx the parse tree
     */
    exitCmdZrevrangebylex?: (ctx: CmdZrevrangebylexContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdZscan`.
     * @param ctx the parse tree
     */
    enterCmdZscan?: (ctx: CmdZscanContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdZscan`.
     * @param ctx the parse tree
     */
    exitCmdZscan?: (ctx: CmdZscanContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdZunion`.
     * @param ctx the parse tree
     */
    enterCmdZunion?: (ctx: CmdZunionContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdZunion`.
     * @param ctx the parse tree
     */
    exitCmdZunion?: (ctx: CmdZunionContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdZunionstore`.
     * @param ctx the parse tree
     */
    enterCmdZunionstore?: (ctx: CmdZunionstoreContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdZunionstore`.
     * @param ctx the parse tree
     */
    exitCmdZunionstore?: (ctx: CmdZunionstoreContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdScriptKill`.
     * @param ctx the parse tree
     */
    enterCmdScriptKill?: (ctx: CmdScriptKillContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdScriptKill`.
     * @param ctx the parse tree
     */
    exitCmdScriptKill?: (ctx: CmdScriptKillContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdScriptLoad`.
     * @param ctx the parse tree
     */
    enterCmdScriptLoad?: (ctx: CmdScriptLoadContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdScriptLoad`.
     * @param ctx the parse tree
     */
    exitCmdScriptLoad?: (ctx: CmdScriptLoadContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdEval`.
     * @param ctx the parse tree
     */
    enterCmdEval?: (ctx: CmdEvalContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdEval`.
     * @param ctx the parse tree
     */
    exitCmdEval?: (ctx: CmdEvalContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdEvalsha`.
     * @param ctx the parse tree
     */
    enterCmdEvalsha?: (ctx: CmdEvalshaContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdEvalsha`.
     * @param ctx the parse tree
     */
    exitCmdEvalsha?: (ctx: CmdEvalshaContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdScriptExists`.
     * @param ctx the parse tree
     */
    enterCmdScriptExists?: (ctx: CmdScriptExistsContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdScriptExists`.
     * @param ctx the parse tree
     */
    exitCmdScriptExists?: (ctx: CmdScriptExistsContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdScriptFlush`.
     * @param ctx the parse tree
     */
    enterCmdScriptFlush?: (ctx: CmdScriptFlushContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdScriptFlush`.
     * @param ctx the parse tree
     */
    exitCmdScriptFlush?: (ctx: CmdScriptFlushContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdExec`.
     * @param ctx the parse tree
     */
    enterCmdExec?: (ctx: CmdExecContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdExec`.
     * @param ctx the parse tree
     */
    exitCmdExec?: (ctx: CmdExecContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdWatch`.
     * @param ctx the parse tree
     */
    enterCmdWatch?: (ctx: CmdWatchContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdWatch`.
     * @param ctx the parse tree
     */
    exitCmdWatch?: (ctx: CmdWatchContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdDiscard`.
     * @param ctx the parse tree
     */
    enterCmdDiscard?: (ctx: CmdDiscardContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdDiscard`.
     * @param ctx the parse tree
     */
    exitCmdDiscard?: (ctx: CmdDiscardContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdUnwatch`.
     * @param ctx the parse tree
     */
    enterCmdUnwatch?: (ctx: CmdUnwatchContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdUnwatch`.
     * @param ctx the parse tree
     */
    exitCmdUnwatch?: (ctx: CmdUnwatchContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdMulti`.
     * @param ctx the parse tree
     */
    enterCmdMulti?: (ctx: CmdMultiContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdMulti`.
     * @param ctx the parse tree
     */
    exitCmdMulti?: (ctx: CmdMultiContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdPfmerge`.
     * @param ctx the parse tree
     */
    enterCmdPfmerge?: (ctx: CmdPfmergeContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdPfmerge`.
     * @param ctx the parse tree
     */
    exitCmdPfmerge?: (ctx: CmdPfmergeContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdPfadd`.
     * @param ctx the parse tree
     */
    enterCmdPfadd?: (ctx: CmdPfaddContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdPfadd`.
     * @param ctx the parse tree
     */
    exitCmdPfadd?: (ctx: CmdPfaddContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdPfcount`.
     * @param ctx the parse tree
     */
    enterCmdPfcount?: (ctx: CmdPfcountContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdPfcount`.
     * @param ctx the parse tree
     */
    exitCmdPfcount?: (ctx: CmdPfcountContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdSsubscribe`.
     * @param ctx the parse tree
     */
    enterCmdSsubscribe?: (ctx: CmdSsubscribeContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdSsubscribe`.
     * @param ctx the parse tree
     */
    exitCmdSsubscribe?: (ctx: CmdSsubscribeContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdUnsubscribe`.
     * @param ctx the parse tree
     */
    enterCmdUnsubscribe?: (ctx: CmdUnsubscribeContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdUnsubscribe`.
     * @param ctx the parse tree
     */
    exitCmdUnsubscribe?: (ctx: CmdUnsubscribeContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdPusubnumpat`.
     * @param ctx the parse tree
     */
    enterCmdPusubnumpat?: (ctx: CmdPusubnumpatContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdPusubnumpat`.
     * @param ctx the parse tree
     */
    exitCmdPusubnumpat?: (ctx: CmdPusubnumpatContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdPusubnumsub`.
     * @param ctx the parse tree
     */
    enterCmdPusubnumsub?: (ctx: CmdPusubnumsubContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdPusubnumsub`.
     * @param ctx the parse tree
     */
    exitCmdPusubnumsub?: (ctx: CmdPusubnumsubContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdPusubchannels`.
     * @param ctx the parse tree
     */
    enterCmdPusubchannels?: (ctx: CmdPusubchannelsContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdPusubchannels`.
     * @param ctx the parse tree
     */
    exitCmdPusubchannels?: (ctx: CmdPusubchannelsContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdPublish`.
     * @param ctx the parse tree
     */
    enterCmdPublish?: (ctx: CmdPublishContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdPublish`.
     * @param ctx the parse tree
     */
    exitCmdPublish?: (ctx: CmdPublishContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdAsking`.
     * @param ctx the parse tree
     */
    enterCmdAsking?: (ctx: CmdAskingContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdAsking`.
     * @param ctx the parse tree
     */
    exitCmdAsking?: (ctx: CmdAskingContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdReadonly`.
     * @param ctx the parse tree
     */
    enterCmdReadonly?: (ctx: CmdReadonlyContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdReadonly`.
     * @param ctx the parse tree
     */
    exitCmdReadonly?: (ctx: CmdReadonlyContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdReadwrite`.
     * @param ctx the parse tree
     */
    enterCmdReadwrite?: (ctx: CmdReadwriteContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdReadwrite`.
     * @param ctx the parse tree
     */
    exitCmdReadwrite?: (ctx: CmdReadwriteContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdAddDelSlots`.
     * @param ctx the parse tree
     */
    enterCmdAddDelSlots?: (ctx: CmdAddDelSlotsContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdAddDelSlots`.
     * @param ctx the parse tree
     */
    exitCmdAddDelSlots?: (ctx: CmdAddDelSlotsContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdCountKeysInSlot`.
     * @param ctx the parse tree
     */
    enterCmdCountKeysInSlot?: (ctx: CmdCountKeysInSlotContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdCountKeysInSlot`.
     * @param ctx the parse tree
     */
    exitCmdCountKeysInSlot?: (ctx: CmdCountKeysInSlotContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdFailOver`.
     * @param ctx the parse tree
     */
    enterCmdFailOver?: (ctx: CmdFailOverContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdFailOver`.
     * @param ctx the parse tree
     */
    exitCmdFailOver?: (ctx: CmdFailOverContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdForget`.
     * @param ctx the parse tree
     */
    enterCmdForget?: (ctx: CmdForgetContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdForget`.
     * @param ctx the parse tree
     */
    exitCmdForget?: (ctx: CmdForgetContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdGetKeysInSlot`.
     * @param ctx the parse tree
     */
    enterCmdGetKeysInSlot?: (ctx: CmdGetKeysInSlotContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdGetKeysInSlot`.
     * @param ctx the parse tree
     */
    exitCmdGetKeysInSlot?: (ctx: CmdGetKeysInSlotContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdClusterOrder`.
     * @param ctx the parse tree
     */
    enterCmdClusterOrder?: (ctx: CmdClusterOrderContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdClusterOrder`.
     * @param ctx the parse tree
     */
    exitCmdClusterOrder?: (ctx: CmdClusterOrderContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdKeySlot`.
     * @param ctx the parse tree
     */
    enterCmdKeySlot?: (ctx: CmdKeySlotContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdKeySlot`.
     * @param ctx the parse tree
     */
    exitCmdKeySlot?: (ctx: CmdKeySlotContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdMeet`.
     * @param ctx the parse tree
     */
    enterCmdMeet?: (ctx: CmdMeetContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdMeet`.
     * @param ctx the parse tree
     */
    exitCmdMeet?: (ctx: CmdMeetContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdReplicaesSlave`.
     * @param ctx the parse tree
     */
    enterCmdReplicaesSlave?: (ctx: CmdReplicaesSlaveContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdReplicaesSlave`.
     * @param ctx the parse tree
     */
    exitCmdReplicaesSlave?: (ctx: CmdReplicaesSlaveContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdReset`.
     * @param ctx the parse tree
     */
    enterCmdReset?: (ctx: CmdResetContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdReset`.
     * @param ctx the parse tree
     */
    exitCmdReset?: (ctx: CmdResetContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdSetSlot`.
     * @param ctx the parse tree
     */
    enterCmdSetSlot?: (ctx: CmdSetSlotContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdSetSlot`.
     * @param ctx the parse tree
     */
    exitCmdSetSlot?: (ctx: CmdSetSlotContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdFlushdb`.
     * @param ctx the parse tree
     */
    enterCmdFlushdb?: (ctx: CmdFlushdbContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdFlushdb`.
     * @param ctx the parse tree
     */
    exitCmdFlushdb?: (ctx: CmdFlushdbContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdEcho`.
     * @param ctx the parse tree
     */
    enterCmdEcho?: (ctx: CmdEchoContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdEcho`.
     * @param ctx the parse tree
     */
    exitCmdEcho?: (ctx: CmdEchoContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdSave`.
     * @param ctx the parse tree
     */
    enterCmdSave?: (ctx: CmdSaveContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdSave`.
     * @param ctx the parse tree
     */
    exitCmdSave?: (ctx: CmdSaveContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdSlowlog`.
     * @param ctx the parse tree
     */
    enterCmdSlowlog?: (ctx: CmdSlowlogContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdSlowlog`.
     * @param ctx the parse tree
     */
    exitCmdSlowlog?: (ctx: CmdSlowlogContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdLastsave`.
     * @param ctx the parse tree
     */
    enterCmdLastsave?: (ctx: CmdLastsaveContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdLastsave`.
     * @param ctx the parse tree
     */
    exitCmdLastsave?: (ctx: CmdLastsaveContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdConfig`.
     * @param ctx the parse tree
     */
    enterCmdConfig?: (ctx: CmdConfigContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdConfig`.
     * @param ctx the parse tree
     */
    exitCmdConfig?: (ctx: CmdConfigContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdClient`.
     * @param ctx the parse tree
     */
    enterCmdClient?: (ctx: CmdClientContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdClient`.
     * @param ctx the parse tree
     */
    exitCmdClient?: (ctx: CmdClientContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdShutdown`.
     * @param ctx the parse tree
     */
    enterCmdShutdown?: (ctx: CmdShutdownContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdShutdown`.
     * @param ctx the parse tree
     */
    exitCmdShutdown?: (ctx: CmdShutdownContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdSync`.
     * @param ctx the parse tree
     */
    enterCmdSync?: (ctx: CmdSyncContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdSync`.
     * @param ctx the parse tree
     */
    exitCmdSync?: (ctx: CmdSyncContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdRole`.
     * @param ctx the parse tree
     */
    enterCmdRole?: (ctx: CmdRoleContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdRole`.
     * @param ctx the parse tree
     */
    exitCmdRole?: (ctx: CmdRoleContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdMonitor`.
     * @param ctx the parse tree
     */
    enterCmdMonitor?: (ctx: CmdMonitorContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdMonitor`.
     * @param ctx the parse tree
     */
    exitCmdMonitor?: (ctx: CmdMonitorContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdSlaveof`.
     * @param ctx the parse tree
     */
    enterCmdSlaveof?: (ctx: CmdSlaveofContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdSlaveof`.
     * @param ctx the parse tree
     */
    exitCmdSlaveof?: (ctx: CmdSlaveofContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdFlushall`.
     * @param ctx the parse tree
     */
    enterCmdFlushall?: (ctx: CmdFlushallContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdFlushall`.
     * @param ctx the parse tree
     */
    exitCmdFlushall?: (ctx: CmdFlushallContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdSelect`.
     * @param ctx the parse tree
     */
    enterCmdSelect?: (ctx: CmdSelectContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdSelect`.
     * @param ctx the parse tree
     */
    exitCmdSelect?: (ctx: CmdSelectContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdPing`.
     * @param ctx the parse tree
     */
    enterCmdPing?: (ctx: CmdPingContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdPing`.
     * @param ctx the parse tree
     */
    exitCmdPing?: (ctx: CmdPingContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdQuit`.
     * @param ctx the parse tree
     */
    enterCmdQuit?: (ctx: CmdQuitContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdQuit`.
     * @param ctx the parse tree
     */
    exitCmdQuit?: (ctx: CmdQuitContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdAuth`.
     * @param ctx the parse tree
     */
    enterCmdAuth?: (ctx: CmdAuthContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdAuth`.
     * @param ctx the parse tree
     */
    exitCmdAuth?: (ctx: CmdAuthContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdDbsize`.
     * @param ctx the parse tree
     */
    enterCmdDbsize?: (ctx: CmdDbsizeContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdDbsize`.
     * @param ctx the parse tree
     */
    exitCmdDbsize?: (ctx: CmdDbsizeContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdBgrewriteaof`.
     * @param ctx the parse tree
     */
    enterCmdBgrewriteaof?: (ctx: CmdBgrewriteaofContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdBgrewriteaof`.
     * @param ctx the parse tree
     */
    exitCmdBgrewriteaof?: (ctx: CmdBgrewriteaofContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdTime`.
     * @param ctx the parse tree
     */
    enterCmdTime?: (ctx: CmdTimeContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdTime`.
     * @param ctx the parse tree
     */
    exitCmdTime?: (ctx: CmdTimeContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdInfo`.
     * @param ctx the parse tree
     */
    enterCmdInfo?: (ctx: CmdInfoContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdInfo`.
     * @param ctx the parse tree
     */
    exitCmdInfo?: (ctx: CmdInfoContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdBgsave`.
     * @param ctx the parse tree
     */
    enterCmdBgsave?: (ctx: CmdBgsaveContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdBgsave`.
     * @param ctx the parse tree
     */
    exitCmdBgsave?: (ctx: CmdBgsaveContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdCmd`.
     * @param ctx the parse tree
     */
    enterCmdCmd?: (ctx: CmdCmdContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdCmd`.
     * @param ctx the parse tree
     */
    exitCmdCmd?: (ctx: CmdCmdContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdCmdx`.
     * @param ctx the parse tree
     */
    enterCmdCmdx?: (ctx: CmdCmdxContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdCmdx`.
     * @param ctx the parse tree
     */
    exitCmdCmdx?: (ctx: CmdCmdxContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.cmdDebug`.
     * @param ctx the parse tree
     */
    enterCmdDebug?: (ctx: CmdDebugContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.cmdDebug`.
     * @param ctx the parse tree
     */
    exitCmdDebug?: (ctx: CmdDebugContext) => void;
    /**
     * Enter a parse tree produced by `RedisSqlParser.sqlStatement`.
     * @param ctx the parse tree
     */
    enterSqlStatement?: (ctx: SqlStatementContext) => void;
    /**
     * Exit a parse tree produced by `RedisSqlParser.sqlStatement`.
     * @param ctx the parse tree
     */
    exitSqlStatement?: (ctx: SqlStatementContext) => void;
}
