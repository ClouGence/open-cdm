// Generated from RedisParser.g4 by ANTLR 4.9.3
package com.clougence.clouddm.ds.redis.parser.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link RedisParser}.
 */
public interface RedisParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link RedisParser#intValue}.
	 * @param ctx the parse tree
	 */
	void enterIntValue(RedisParser.IntValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#intValue}.
	 * @param ctx the parse tree
	 */
	void exitIntValue(RedisParser.IntValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#bitValue}.
	 * @param ctx the parse tree
	 */
	void enterBitValue(RedisParser.BitValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#bitValue}.
	 * @param ctx the parse tree
	 */
	void exitBitValue(RedisParser.BitValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#floatValue}.
	 * @param ctx the parse tree
	 */
	void enterFloatValue(RedisParser.FloatValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#floatValue}.
	 * @param ctx the parse tree
	 */
	void exitFloatValue(RedisParser.FloatValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#keyName}.
	 * @param ctx the parse tree
	 */
	void enterKeyName(RedisParser.KeyNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#keyName}.
	 * @param ctx the parse tree
	 */
	void exitKeyName(RedisParser.KeyNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#pattern}.
	 * @param ctx the parse tree
	 */
	void enterPattern(RedisParser.PatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#pattern}.
	 * @param ctx the parse tree
	 */
	void exitPattern(RedisParser.PatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#stringValue}.
	 * @param ctx the parse tree
	 */
	void enterStringValue(RedisParser.StringValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#stringValue}.
	 * @param ctx the parse tree
	 */
	void exitStringValue(RedisParser.StringValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#multiString}.
	 * @param ctx the parse tree
	 */
	void enterMultiString(RedisParser.MultiStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#multiString}.
	 * @param ctx the parse tree
	 */
	void exitMultiString(RedisParser.MultiStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#offsetNum}.
	 * @param ctx the parse tree
	 */
	void enterOffsetNum(RedisParser.OffsetNumContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#offsetNum}.
	 * @param ctx the parse tree
	 */
	void exitOffsetNum(RedisParser.OffsetNumContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#aclCmdRule}.
	 * @param ctx the parse tree
	 */
	void enterAclCmdRule(RedisParser.AclCmdRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#aclCmdRule}.
	 * @param ctx the parse tree
	 */
	void exitAclCmdRule(RedisParser.AclCmdRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#idStr}.
	 * @param ctx the parse tree
	 */
	void enterIdStr(RedisParser.IdStrContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#idStr}.
	 * @param ctx the parse tree
	 */
	void exitIdStr(RedisParser.IdStrContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#keyWordValue}.
	 * @param ctx the parse tree
	 */
	void enterKeyWordValue(RedisParser.KeyWordValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#keyWordValue}.
	 * @param ctx the parse tree
	 */
	void exitKeyWordValue(RedisParser.KeyWordValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#valueAsStr}.
	 * @param ctx the parse tree
	 */
	void enterValueAsStr(RedisParser.ValueAsStrContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#valueAsStr}.
	 * @param ctx the parse tree
	 */
	void exitValueAsStr(RedisParser.ValueAsStrContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#arg}.
	 * @param ctx the parse tree
	 */
	void enterArg(RedisParser.ArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#arg}.
	 * @param ctx the parse tree
	 */
	void exitArg(RedisParser.ArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#rangeInt}.
	 * @param ctx the parse tree
	 */
	void enterRangeInt(RedisParser.RangeIntContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#rangeInt}.
	 * @param ctx the parse tree
	 */
	void exitRangeInt(RedisParser.RangeIntContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#min}.
	 * @param ctx the parse tree
	 */
	void enterMin(RedisParser.MinContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#min}.
	 * @param ctx the parse tree
	 */
	void exitMin(RedisParser.MinContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#max}.
	 * @param ctx the parse tree
	 */
	void enterMax(RedisParser.MaxContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#max}.
	 * @param ctx the parse tree
	 */
	void exitMax(RedisParser.MaxContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#port}.
	 * @param ctx the parse tree
	 */
	void enterPort(RedisParser.PortContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#port}.
	 * @param ctx the parse tree
	 */
	void exitPort(RedisParser.PortContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#weight}.
	 * @param ctx the parse tree
	 */
	void enterWeight(RedisParser.WeightContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#weight}.
	 * @param ctx the parse tree
	 */
	void exitWeight(RedisParser.WeightContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#sha1}.
	 * @param ctx the parse tree
	 */
	void enterSha1(RedisParser.Sha1Context ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#sha1}.
	 * @param ctx the parse tree
	 */
	void exitSha1(RedisParser.Sha1Context ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#key}.
	 * @param ctx the parse tree
	 */
	void enterKey(RedisParser.KeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#key}.
	 * @param ctx the parse tree
	 */
	void exitKey(RedisParser.KeyContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cursor}.
	 * @param ctx the parse tree
	 */
	void enterCursor(RedisParser.CursorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cursor}.
	 * @param ctx the parse tree
	 */
	void exitCursor(RedisParser.CursorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#count}.
	 * @param ctx the parse tree
	 */
	void enterCount(RedisParser.CountContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#count}.
	 * @param ctx the parse tree
	 */
	void exitCount(RedisParser.CountContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#fieldName}.
	 * @param ctx the parse tree
	 */
	void enterFieldName(RedisParser.FieldNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#fieldName}.
	 * @param ctx the parse tree
	 */
	void exitFieldName(RedisParser.FieldNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#element}.
	 * @param ctx the parse tree
	 */
	void enterElement(RedisParser.ElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#element}.
	 * @param ctx the parse tree
	 */
	void exitElement(RedisParser.ElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#member}.
	 * @param ctx the parse tree
	 */
	void enterMember(RedisParser.MemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#member}.
	 * @param ctx the parse tree
	 */
	void exitMember(RedisParser.MemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#srcKey}.
	 * @param ctx the parse tree
	 */
	void enterSrcKey(RedisParser.SrcKeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#srcKey}.
	 * @param ctx the parse tree
	 */
	void exitSrcKey(RedisParser.SrcKeyContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#channel}.
	 * @param ctx the parse tree
	 */
	void enterChannel(RedisParser.ChannelContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#channel}.
	 * @param ctx the parse tree
	 */
	void exitChannel(RedisParser.ChannelContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#slot}.
	 * @param ctx the parse tree
	 */
	void enterSlot(RedisParser.SlotContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#slot}.
	 * @param ctx the parse tree
	 */
	void exitSlot(RedisParser.SlotContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#keyAndString}.
	 * @param ctx the parse tree
	 */
	void enterKeyAndString(RedisParser.KeyAndStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#keyAndString}.
	 * @param ctx the parse tree
	 */
	void exitKeyAndString(RedisParser.KeyAndStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#patternOpt}.
	 * @param ctx the parse tree
	 */
	void enterPatternOpt(RedisParser.PatternOptContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#patternOpt}.
	 * @param ctx the parse tree
	 */
	void exitPatternOpt(RedisParser.PatternOptContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#keyOpt}.
	 * @param ctx the parse tree
	 */
	void enterKeyOpt(RedisParser.KeyOptContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#keyOpt}.
	 * @param ctx the parse tree
	 */
	void exitKeyOpt(RedisParser.KeyOptContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#limitOpt}.
	 * @param ctx the parse tree
	 */
	void enterLimitOpt(RedisParser.LimitOptContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#limitOpt}.
	 * @param ctx the parse tree
	 */
	void exitLimitOpt(RedisParser.LimitOptContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#sortOpt}.
	 * @param ctx the parse tree
	 */
	void enterSortOpt(RedisParser.SortOptContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#sortOpt}.
	 * @param ctx the parse tree
	 */
	void exitSortOpt(RedisParser.SortOptContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#ttlOpt}.
	 * @param ctx the parse tree
	 */
	void enterTtlOpt(RedisParser.TtlOptContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#ttlOpt}.
	 * @param ctx the parse tree
	 */
	void exitTtlOpt(RedisParser.TtlOptContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#directionOpt}.
	 * @param ctx the parse tree
	 */
	void enterDirectionOpt(RedisParser.DirectionOptContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#directionOpt}.
	 * @param ctx the parse tree
	 */
	void exitDirectionOpt(RedisParser.DirectionOptContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdCopy}.
	 * @param ctx the parse tree
	 */
	void enterCmdCopy(RedisParser.CmdCopyContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdCopy}.
	 * @param ctx the parse tree
	 */
	void exitCmdCopy(RedisParser.CmdCopyContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdDel}.
	 * @param ctx the parse tree
	 */
	void enterCmdDel(RedisParser.CmdDelContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdDel}.
	 * @param ctx the parse tree
	 */
	void exitCmdDel(RedisParser.CmdDelContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdDump}.
	 * @param ctx the parse tree
	 */
	void enterCmdDump(RedisParser.CmdDumpContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdDump}.
	 * @param ctx the parse tree
	 */
	void exitCmdDump(RedisParser.CmdDumpContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdExists}.
	 * @param ctx the parse tree
	 */
	void enterCmdExists(RedisParser.CmdExistsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdExists}.
	 * @param ctx the parse tree
	 */
	void exitCmdExists(RedisParser.CmdExistsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdExpire}.
	 * @param ctx the parse tree
	 */
	void enterCmdExpire(RedisParser.CmdExpireContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdExpire}.
	 * @param ctx the parse tree
	 */
	void exitCmdExpire(RedisParser.CmdExpireContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdExpireat}.
	 * @param ctx the parse tree
	 */
	void enterCmdExpireat(RedisParser.CmdExpireatContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdExpireat}.
	 * @param ctx the parse tree
	 */
	void exitCmdExpireat(RedisParser.CmdExpireatContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdExpireTime}.
	 * @param ctx the parse tree
	 */
	void enterCmdExpireTime(RedisParser.CmdExpireTimeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdExpireTime}.
	 * @param ctx the parse tree
	 */
	void exitCmdExpireTime(RedisParser.CmdExpireTimeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdKeys}.
	 * @param ctx the parse tree
	 */
	void enterCmdKeys(RedisParser.CmdKeysContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdKeys}.
	 * @param ctx the parse tree
	 */
	void exitCmdKeys(RedisParser.CmdKeysContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdMove}.
	 * @param ctx the parse tree
	 */
	void enterCmdMove(RedisParser.CmdMoveContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdMove}.
	 * @param ctx the parse tree
	 */
	void exitCmdMove(RedisParser.CmdMoveContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdObject}.
	 * @param ctx the parse tree
	 */
	void enterCmdObject(RedisParser.CmdObjectContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdObject}.
	 * @param ctx the parse tree
	 */
	void exitCmdObject(RedisParser.CmdObjectContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdPersist}.
	 * @param ctx the parse tree
	 */
	void enterCmdPersist(RedisParser.CmdPersistContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdPersist}.
	 * @param ctx the parse tree
	 */
	void exitCmdPersist(RedisParser.CmdPersistContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdPexpire}.
	 * @param ctx the parse tree
	 */
	void enterCmdPexpire(RedisParser.CmdPexpireContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdPexpire}.
	 * @param ctx the parse tree
	 */
	void exitCmdPexpire(RedisParser.CmdPexpireContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdPexpireat}.
	 * @param ctx the parse tree
	 */
	void enterCmdPexpireat(RedisParser.CmdPexpireatContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdPexpireat}.
	 * @param ctx the parse tree
	 */
	void exitCmdPexpireat(RedisParser.CmdPexpireatContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdPExpireTime}.
	 * @param ctx the parse tree
	 */
	void enterCmdPExpireTime(RedisParser.CmdPExpireTimeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdPExpireTime}.
	 * @param ctx the parse tree
	 */
	void exitCmdPExpireTime(RedisParser.CmdPExpireTimeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdTtl}.
	 * @param ctx the parse tree
	 */
	void enterCmdTtl(RedisParser.CmdTtlContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdTtl}.
	 * @param ctx the parse tree
	 */
	void exitCmdTtl(RedisParser.CmdTtlContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdPttl}.
	 * @param ctx the parse tree
	 */
	void enterCmdPttl(RedisParser.CmdPttlContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdPttl}.
	 * @param ctx the parse tree
	 */
	void exitCmdPttl(RedisParser.CmdPttlContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdRandomkey}.
	 * @param ctx the parse tree
	 */
	void enterCmdRandomkey(RedisParser.CmdRandomkeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdRandomkey}.
	 * @param ctx the parse tree
	 */
	void exitCmdRandomkey(RedisParser.CmdRandomkeyContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdRename}.
	 * @param ctx the parse tree
	 */
	void enterCmdRename(RedisParser.CmdRenameContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdRename}.
	 * @param ctx the parse tree
	 */
	void exitCmdRename(RedisParser.CmdRenameContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdRenamenx}.
	 * @param ctx the parse tree
	 */
	void enterCmdRenamenx(RedisParser.CmdRenamenxContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdRenamenx}.
	 * @param ctx the parse tree
	 */
	void exitCmdRenamenx(RedisParser.CmdRenamenxContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdRestore}.
	 * @param ctx the parse tree
	 */
	void enterCmdRestore(RedisParser.CmdRestoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdRestore}.
	 * @param ctx the parse tree
	 */
	void exitCmdRestore(RedisParser.CmdRestoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdScan}.
	 * @param ctx the parse tree
	 */
	void enterCmdScan(RedisParser.CmdScanContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdScan}.
	 * @param ctx the parse tree
	 */
	void exitCmdScan(RedisParser.CmdScanContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSort}.
	 * @param ctx the parse tree
	 */
	void enterCmdSort(RedisParser.CmdSortContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSort}.
	 * @param ctx the parse tree
	 */
	void exitCmdSort(RedisParser.CmdSortContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSortro}.
	 * @param ctx the parse tree
	 */
	void enterCmdSortro(RedisParser.CmdSortroContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSortro}.
	 * @param ctx the parse tree
	 */
	void exitCmdSortro(RedisParser.CmdSortroContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdTouch}.
	 * @param ctx the parse tree
	 */
	void enterCmdTouch(RedisParser.CmdTouchContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdTouch}.
	 * @param ctx the parse tree
	 */
	void exitCmdTouch(RedisParser.CmdTouchContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdType}.
	 * @param ctx the parse tree
	 */
	void enterCmdType(RedisParser.CmdTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdType}.
	 * @param ctx the parse tree
	 */
	void exitCmdType(RedisParser.CmdTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdUnlink}.
	 * @param ctx the parse tree
	 */
	void enterCmdUnlink(RedisParser.CmdUnlinkContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdUnlink}.
	 * @param ctx the parse tree
	 */
	void exitCmdUnlink(RedisParser.CmdUnlinkContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdWait}.
	 * @param ctx the parse tree
	 */
	void enterCmdWait(RedisParser.CmdWaitContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdWait}.
	 * @param ctx the parse tree
	 */
	void exitCmdWait(RedisParser.CmdWaitContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdWaitAOF}.
	 * @param ctx the parse tree
	 */
	void enterCmdWaitAOF(RedisParser.CmdWaitAOFContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdWaitAOF}.
	 * @param ctx the parse tree
	 */
	void exitCmdWaitAOF(RedisParser.CmdWaitAOFContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdAppend}.
	 * @param ctx the parse tree
	 */
	void enterCmdAppend(RedisParser.CmdAppendContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdAppend}.
	 * @param ctx the parse tree
	 */
	void exitCmdAppend(RedisParser.CmdAppendContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdDecr}.
	 * @param ctx the parse tree
	 */
	void enterCmdDecr(RedisParser.CmdDecrContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdDecr}.
	 * @param ctx the parse tree
	 */
	void exitCmdDecr(RedisParser.CmdDecrContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdDecrby}.
	 * @param ctx the parse tree
	 */
	void enterCmdDecrby(RedisParser.CmdDecrbyContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdDecrby}.
	 * @param ctx the parse tree
	 */
	void exitCmdDecrby(RedisParser.CmdDecrbyContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdGet}.
	 * @param ctx the parse tree
	 */
	void enterCmdGet(RedisParser.CmdGetContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdGet}.
	 * @param ctx the parse tree
	 */
	void exitCmdGet(RedisParser.CmdGetContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdGetdel}.
	 * @param ctx the parse tree
	 */
	void enterCmdGetdel(RedisParser.CmdGetdelContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdGetdel}.
	 * @param ctx the parse tree
	 */
	void exitCmdGetdel(RedisParser.CmdGetdelContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdGetex}.
	 * @param ctx the parse tree
	 */
	void enterCmdGetex(RedisParser.CmdGetexContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdGetex}.
	 * @param ctx the parse tree
	 */
	void exitCmdGetex(RedisParser.CmdGetexContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdGetrange}.
	 * @param ctx the parse tree
	 */
	void enterCmdGetrange(RedisParser.CmdGetrangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdGetrange}.
	 * @param ctx the parse tree
	 */
	void exitCmdGetrange(RedisParser.CmdGetrangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdGetset}.
	 * @param ctx the parse tree
	 */
	void enterCmdGetset(RedisParser.CmdGetsetContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdGetset}.
	 * @param ctx the parse tree
	 */
	void exitCmdGetset(RedisParser.CmdGetsetContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdIncr}.
	 * @param ctx the parse tree
	 */
	void enterCmdIncr(RedisParser.CmdIncrContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdIncr}.
	 * @param ctx the parse tree
	 */
	void exitCmdIncr(RedisParser.CmdIncrContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdIncrby}.
	 * @param ctx the parse tree
	 */
	void enterCmdIncrby(RedisParser.CmdIncrbyContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdIncrby}.
	 * @param ctx the parse tree
	 */
	void exitCmdIncrby(RedisParser.CmdIncrbyContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdIncrbyFloat}.
	 * @param ctx the parse tree
	 */
	void enterCmdIncrbyFloat(RedisParser.CmdIncrbyFloatContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdIncrbyFloat}.
	 * @param ctx the parse tree
	 */
	void exitCmdIncrbyFloat(RedisParser.CmdIncrbyFloatContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdLcs}.
	 * @param ctx the parse tree
	 */
	void enterCmdLcs(RedisParser.CmdLcsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdLcs}.
	 * @param ctx the parse tree
	 */
	void exitCmdLcs(RedisParser.CmdLcsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdMget}.
	 * @param ctx the parse tree
	 */
	void enterCmdMget(RedisParser.CmdMgetContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdMget}.
	 * @param ctx the parse tree
	 */
	void exitCmdMget(RedisParser.CmdMgetContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdMset}.
	 * @param ctx the parse tree
	 */
	void enterCmdMset(RedisParser.CmdMsetContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdMset}.
	 * @param ctx the parse tree
	 */
	void exitCmdMset(RedisParser.CmdMsetContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdMsetnx}.
	 * @param ctx the parse tree
	 */
	void enterCmdMsetnx(RedisParser.CmdMsetnxContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdMsetnx}.
	 * @param ctx the parse tree
	 */
	void exitCmdMsetnx(RedisParser.CmdMsetnxContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSetex}.
	 * @param ctx the parse tree
	 */
	void enterCmdSetex(RedisParser.CmdSetexContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSetex}.
	 * @param ctx the parse tree
	 */
	void exitCmdSetex(RedisParser.CmdSetexContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdPSetex}.
	 * @param ctx the parse tree
	 */
	void enterCmdPSetex(RedisParser.CmdPSetexContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdPSetex}.
	 * @param ctx the parse tree
	 */
	void exitCmdPSetex(RedisParser.CmdPSetexContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSet}.
	 * @param ctx the parse tree
	 */
	void enterCmdSet(RedisParser.CmdSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSet}.
	 * @param ctx the parse tree
	 */
	void exitCmdSet(RedisParser.CmdSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSetnx}.
	 * @param ctx the parse tree
	 */
	void enterCmdSetnx(RedisParser.CmdSetnxContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSetnx}.
	 * @param ctx the parse tree
	 */
	void exitCmdSetnx(RedisParser.CmdSetnxContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSetrange}.
	 * @param ctx the parse tree
	 */
	void enterCmdSetrange(RedisParser.CmdSetrangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSetrange}.
	 * @param ctx the parse tree
	 */
	void exitCmdSetrange(RedisParser.CmdSetrangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdStrlen}.
	 * @param ctx the parse tree
	 */
	void enterCmdStrlen(RedisParser.CmdStrlenContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdStrlen}.
	 * @param ctx the parse tree
	 */
	void exitCmdStrlen(RedisParser.CmdStrlenContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSubstr}.
	 * @param ctx the parse tree
	 */
	void enterCmdSubstr(RedisParser.CmdSubstrContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSubstr}.
	 * @param ctx the parse tree
	 */
	void exitCmdSubstr(RedisParser.CmdSubstrContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdBitCount}.
	 * @param ctx the parse tree
	 */
	void enterCmdBitCount(RedisParser.CmdBitCountContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdBitCount}.
	 * @param ctx the parse tree
	 */
	void exitCmdBitCount(RedisParser.CmdBitCountContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdBitField}.
	 * @param ctx the parse tree
	 */
	void enterCmdBitField(RedisParser.CmdBitFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdBitField}.
	 * @param ctx the parse tree
	 */
	void exitCmdBitField(RedisParser.CmdBitFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#bitFieldItem}.
	 * @param ctx the parse tree
	 */
	void enterBitFieldItem(RedisParser.BitFieldItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#bitFieldItem}.
	 * @param ctx the parse tree
	 */
	void exitBitFieldItem(RedisParser.BitFieldItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#encodOffOpt}.
	 * @param ctx the parse tree
	 */
	void enterEncodOffOpt(RedisParser.EncodOffOptContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#encodOffOpt}.
	 * @param ctx the parse tree
	 */
	void exitEncodOffOpt(RedisParser.EncodOffOptContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdBitFieldRO}.
	 * @param ctx the parse tree
	 */
	void enterCmdBitFieldRO(RedisParser.CmdBitFieldROContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdBitFieldRO}.
	 * @param ctx the parse tree
	 */
	void exitCmdBitFieldRO(RedisParser.CmdBitFieldROContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdBitOP}.
	 * @param ctx the parse tree
	 */
	void enterCmdBitOP(RedisParser.CmdBitOPContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdBitOP}.
	 * @param ctx the parse tree
	 */
	void exitCmdBitOP(RedisParser.CmdBitOPContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdBitPos}.
	 * @param ctx the parse tree
	 */
	void enterCmdBitPos(RedisParser.CmdBitPosContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdBitPos}.
	 * @param ctx the parse tree
	 */
	void exitCmdBitPos(RedisParser.CmdBitPosContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdGetbit}.
	 * @param ctx the parse tree
	 */
	void enterCmdGetbit(RedisParser.CmdGetbitContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdGetbit}.
	 * @param ctx the parse tree
	 */
	void exitCmdGetbit(RedisParser.CmdGetbitContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSetbit}.
	 * @param ctx the parse tree
	 */
	void enterCmdSetbit(RedisParser.CmdSetbitContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSetbit}.
	 * @param ctx the parse tree
	 */
	void exitCmdSetbit(RedisParser.CmdSetbitContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHdel}.
	 * @param ctx the parse tree
	 */
	void enterCmdHdel(RedisParser.CmdHdelContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHdel}.
	 * @param ctx the parse tree
	 */
	void exitCmdHdel(RedisParser.CmdHdelContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHexists}.
	 * @param ctx the parse tree
	 */
	void enterCmdHexists(RedisParser.CmdHexistsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHexists}.
	 * @param ctx the parse tree
	 */
	void exitCmdHexists(RedisParser.CmdHexistsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHexpire}.
	 * @param ctx the parse tree
	 */
	void enterCmdHexpire(RedisParser.CmdHexpireContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHexpire}.
	 * @param ctx the parse tree
	 */
	void exitCmdHexpire(RedisParser.CmdHexpireContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHexpireat}.
	 * @param ctx the parse tree
	 */
	void enterCmdHexpireat(RedisParser.CmdHexpireatContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHexpireat}.
	 * @param ctx the parse tree
	 */
	void exitCmdHexpireat(RedisParser.CmdHexpireatContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHexpiretime}.
	 * @param ctx the parse tree
	 */
	void enterCmdHexpiretime(RedisParser.CmdHexpiretimeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHexpiretime}.
	 * @param ctx the parse tree
	 */
	void exitCmdHexpiretime(RedisParser.CmdHexpiretimeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHGet}.
	 * @param ctx the parse tree
	 */
	void enterCmdHGet(RedisParser.CmdHGetContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHGet}.
	 * @param ctx the parse tree
	 */
	void exitCmdHGet(RedisParser.CmdHGetContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHGetAll}.
	 * @param ctx the parse tree
	 */
	void enterCmdHGetAll(RedisParser.CmdHGetAllContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHGetAll}.
	 * @param ctx the parse tree
	 */
	void exitCmdHGetAll(RedisParser.CmdHGetAllContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHgetDel}.
	 * @param ctx the parse tree
	 */
	void enterCmdHgetDel(RedisParser.CmdHgetDelContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHgetDel}.
	 * @param ctx the parse tree
	 */
	void exitCmdHgetDel(RedisParser.CmdHgetDelContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHgetex}.
	 * @param ctx the parse tree
	 */
	void enterCmdHgetex(RedisParser.CmdHgetexContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHgetex}.
	 * @param ctx the parse tree
	 */
	void exitCmdHgetex(RedisParser.CmdHgetexContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHincrBy}.
	 * @param ctx the parse tree
	 */
	void enterCmdHincrBy(RedisParser.CmdHincrByContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHincrBy}.
	 * @param ctx the parse tree
	 */
	void exitCmdHincrBy(RedisParser.CmdHincrByContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHincrByFloat}.
	 * @param ctx the parse tree
	 */
	void enterCmdHincrByFloat(RedisParser.CmdHincrByFloatContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHincrByFloat}.
	 * @param ctx the parse tree
	 */
	void exitCmdHincrByFloat(RedisParser.CmdHincrByFloatContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHKeys}.
	 * @param ctx the parse tree
	 */
	void enterCmdHKeys(RedisParser.CmdHKeysContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHKeys}.
	 * @param ctx the parse tree
	 */
	void exitCmdHKeys(RedisParser.CmdHKeysContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHLen}.
	 * @param ctx the parse tree
	 */
	void enterCmdHLen(RedisParser.CmdHLenContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHLen}.
	 * @param ctx the parse tree
	 */
	void exitCmdHLen(RedisParser.CmdHLenContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHMget}.
	 * @param ctx the parse tree
	 */
	void enterCmdHMget(RedisParser.CmdHMgetContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHMget}.
	 * @param ctx the parse tree
	 */
	void exitCmdHMget(RedisParser.CmdHMgetContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHMset}.
	 * @param ctx the parse tree
	 */
	void enterCmdHMset(RedisParser.CmdHMsetContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHMset}.
	 * @param ctx the parse tree
	 */
	void exitCmdHMset(RedisParser.CmdHMsetContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHPersist}.
	 * @param ctx the parse tree
	 */
	void enterCmdHPersist(RedisParser.CmdHPersistContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHPersist}.
	 * @param ctx the parse tree
	 */
	void exitCmdHPersist(RedisParser.CmdHPersistContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHPexpire}.
	 * @param ctx the parse tree
	 */
	void enterCmdHPexpire(RedisParser.CmdHPexpireContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHPexpire}.
	 * @param ctx the parse tree
	 */
	void exitCmdHPexpire(RedisParser.CmdHPexpireContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHPexpireAt}.
	 * @param ctx the parse tree
	 */
	void enterCmdHPexpireAt(RedisParser.CmdHPexpireAtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHPexpireAt}.
	 * @param ctx the parse tree
	 */
	void exitCmdHPexpireAt(RedisParser.CmdHPexpireAtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHPexpireTime}.
	 * @param ctx the parse tree
	 */
	void enterCmdHPexpireTime(RedisParser.CmdHPexpireTimeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHPexpireTime}.
	 * @param ctx the parse tree
	 */
	void exitCmdHPexpireTime(RedisParser.CmdHPexpireTimeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHPTTL}.
	 * @param ctx the parse tree
	 */
	void enterCmdHPTTL(RedisParser.CmdHPTTLContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHPTTL}.
	 * @param ctx the parse tree
	 */
	void exitCmdHPTTL(RedisParser.CmdHPTTLContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHTTL}.
	 * @param ctx the parse tree
	 */
	void enterCmdHTTL(RedisParser.CmdHTTLContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHTTL}.
	 * @param ctx the parse tree
	 */
	void exitCmdHTTL(RedisParser.CmdHTTLContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHrandfield}.
	 * @param ctx the parse tree
	 */
	void enterCmdHrandfield(RedisParser.CmdHrandfieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHrandfield}.
	 * @param ctx the parse tree
	 */
	void exitCmdHrandfield(RedisParser.CmdHrandfieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHscan}.
	 * @param ctx the parse tree
	 */
	void enterCmdHscan(RedisParser.CmdHscanContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHscan}.
	 * @param ctx the parse tree
	 */
	void exitCmdHscan(RedisParser.CmdHscanContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHSet}.
	 * @param ctx the parse tree
	 */
	void enterCmdHSet(RedisParser.CmdHSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHSet}.
	 * @param ctx the parse tree
	 */
	void exitCmdHSet(RedisParser.CmdHSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHSetex}.
	 * @param ctx the parse tree
	 */
	void enterCmdHSetex(RedisParser.CmdHSetexContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHSetex}.
	 * @param ctx the parse tree
	 */
	void exitCmdHSetex(RedisParser.CmdHSetexContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHsetnx}.
	 * @param ctx the parse tree
	 */
	void enterCmdHsetnx(RedisParser.CmdHsetnxContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHsetnx}.
	 * @param ctx the parse tree
	 */
	void exitCmdHsetnx(RedisParser.CmdHsetnxContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHStrLen}.
	 * @param ctx the parse tree
	 */
	void enterCmdHStrLen(RedisParser.CmdHStrLenContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHStrLen}.
	 * @param ctx the parse tree
	 */
	void exitCmdHStrLen(RedisParser.CmdHStrLenContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHVals}.
	 * @param ctx the parse tree
	 */
	void enterCmdHVals(RedisParser.CmdHValsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHVals}.
	 * @param ctx the parse tree
	 */
	void exitCmdHVals(RedisParser.CmdHValsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdBlmove}.
	 * @param ctx the parse tree
	 */
	void enterCmdBlmove(RedisParser.CmdBlmoveContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdBlmove}.
	 * @param ctx the parse tree
	 */
	void exitCmdBlmove(RedisParser.CmdBlmoveContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdBLmpop}.
	 * @param ctx the parse tree
	 */
	void enterCmdBLmpop(RedisParser.CmdBLmpopContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdBLmpop}.
	 * @param ctx the parse tree
	 */
	void exitCmdBLmpop(RedisParser.CmdBLmpopContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdBLPop}.
	 * @param ctx the parse tree
	 */
	void enterCmdBLPop(RedisParser.CmdBLPopContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdBLPop}.
	 * @param ctx the parse tree
	 */
	void exitCmdBLPop(RedisParser.CmdBLPopContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdBRPop}.
	 * @param ctx the parse tree
	 */
	void enterCmdBRPop(RedisParser.CmdBRPopContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdBRPop}.
	 * @param ctx the parse tree
	 */
	void exitCmdBRPop(RedisParser.CmdBRPopContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdBrpoplpush}.
	 * @param ctx the parse tree
	 */
	void enterCmdBrpoplpush(RedisParser.CmdBrpoplpushContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdBrpoplpush}.
	 * @param ctx the parse tree
	 */
	void exitCmdBrpoplpush(RedisParser.CmdBrpoplpushContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdLindex}.
	 * @param ctx the parse tree
	 */
	void enterCmdLindex(RedisParser.CmdLindexContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdLindex}.
	 * @param ctx the parse tree
	 */
	void exitCmdLindex(RedisParser.CmdLindexContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdLinsert}.
	 * @param ctx the parse tree
	 */
	void enterCmdLinsert(RedisParser.CmdLinsertContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdLinsert}.
	 * @param ctx the parse tree
	 */
	void exitCmdLinsert(RedisParser.CmdLinsertContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdLlen}.
	 * @param ctx the parse tree
	 */
	void enterCmdLlen(RedisParser.CmdLlenContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdLlen}.
	 * @param ctx the parse tree
	 */
	void exitCmdLlen(RedisParser.CmdLlenContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdLmove}.
	 * @param ctx the parse tree
	 */
	void enterCmdLmove(RedisParser.CmdLmoveContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdLmove}.
	 * @param ctx the parse tree
	 */
	void exitCmdLmove(RedisParser.CmdLmoveContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdLmpop}.
	 * @param ctx the parse tree
	 */
	void enterCmdLmpop(RedisParser.CmdLmpopContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdLmpop}.
	 * @param ctx the parse tree
	 */
	void exitCmdLmpop(RedisParser.CmdLmpopContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdLPop}.
	 * @param ctx the parse tree
	 */
	void enterCmdLPop(RedisParser.CmdLPopContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdLPop}.
	 * @param ctx the parse tree
	 */
	void exitCmdLPop(RedisParser.CmdLPopContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdLpos}.
	 * @param ctx the parse tree
	 */
	void enterCmdLpos(RedisParser.CmdLposContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdLpos}.
	 * @param ctx the parse tree
	 */
	void exitCmdLpos(RedisParser.CmdLposContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdLPush}.
	 * @param ctx the parse tree
	 */
	void enterCmdLPush(RedisParser.CmdLPushContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdLPush}.
	 * @param ctx the parse tree
	 */
	void exitCmdLPush(RedisParser.CmdLPushContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdLPushx}.
	 * @param ctx the parse tree
	 */
	void enterCmdLPushx(RedisParser.CmdLPushxContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdLPushx}.
	 * @param ctx the parse tree
	 */
	void exitCmdLPushx(RedisParser.CmdLPushxContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdLRange}.
	 * @param ctx the parse tree
	 */
	void enterCmdLRange(RedisParser.CmdLRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdLRange}.
	 * @param ctx the parse tree
	 */
	void exitCmdLRange(RedisParser.CmdLRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdLRem}.
	 * @param ctx the parse tree
	 */
	void enterCmdLRem(RedisParser.CmdLRemContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdLRem}.
	 * @param ctx the parse tree
	 */
	void exitCmdLRem(RedisParser.CmdLRemContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdLSet}.
	 * @param ctx the parse tree
	 */
	void enterCmdLSet(RedisParser.CmdLSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdLSet}.
	 * @param ctx the parse tree
	 */
	void exitCmdLSet(RedisParser.CmdLSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdLTrim}.
	 * @param ctx the parse tree
	 */
	void enterCmdLTrim(RedisParser.CmdLTrimContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdLTrim}.
	 * @param ctx the parse tree
	 */
	void exitCmdLTrim(RedisParser.CmdLTrimContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdRPop}.
	 * @param ctx the parse tree
	 */
	void enterCmdRPop(RedisParser.CmdRPopContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdRPop}.
	 * @param ctx the parse tree
	 */
	void exitCmdRPop(RedisParser.CmdRPopContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdRpoplpush}.
	 * @param ctx the parse tree
	 */
	void enterCmdRpoplpush(RedisParser.CmdRpoplpushContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdRpoplpush}.
	 * @param ctx the parse tree
	 */
	void exitCmdRpoplpush(RedisParser.CmdRpoplpushContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdRPush}.
	 * @param ctx the parse tree
	 */
	void enterCmdRPush(RedisParser.CmdRPushContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdRPush}.
	 * @param ctx the parse tree
	 */
	void exitCmdRPush(RedisParser.CmdRPushContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdRPushx}.
	 * @param ctx the parse tree
	 */
	void enterCmdRPushx(RedisParser.CmdRPushxContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdRPushx}.
	 * @param ctx the parse tree
	 */
	void exitCmdRPushx(RedisParser.CmdRPushxContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSadd}.
	 * @param ctx the parse tree
	 */
	void enterCmdSadd(RedisParser.CmdSaddContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSadd}.
	 * @param ctx the parse tree
	 */
	void exitCmdSadd(RedisParser.CmdSaddContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdScard}.
	 * @param ctx the parse tree
	 */
	void enterCmdScard(RedisParser.CmdScardContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdScard}.
	 * @param ctx the parse tree
	 */
	void exitCmdScard(RedisParser.CmdScardContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSdiff}.
	 * @param ctx the parse tree
	 */
	void enterCmdSdiff(RedisParser.CmdSdiffContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSdiff}.
	 * @param ctx the parse tree
	 */
	void exitCmdSdiff(RedisParser.CmdSdiffContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSdiffstore}.
	 * @param ctx the parse tree
	 */
	void enterCmdSdiffstore(RedisParser.CmdSdiffstoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSdiffstore}.
	 * @param ctx the parse tree
	 */
	void exitCmdSdiffstore(RedisParser.CmdSdiffstoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSinter}.
	 * @param ctx the parse tree
	 */
	void enterCmdSinter(RedisParser.CmdSinterContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSinter}.
	 * @param ctx the parse tree
	 */
	void exitCmdSinter(RedisParser.CmdSinterContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSinterCard}.
	 * @param ctx the parse tree
	 */
	void enterCmdSinterCard(RedisParser.CmdSinterCardContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSinterCard}.
	 * @param ctx the parse tree
	 */
	void exitCmdSinterCard(RedisParser.CmdSinterCardContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSinterStore}.
	 * @param ctx the parse tree
	 */
	void enterCmdSinterStore(RedisParser.CmdSinterStoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSinterStore}.
	 * @param ctx the parse tree
	 */
	void exitCmdSinterStore(RedisParser.CmdSinterStoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSismember}.
	 * @param ctx the parse tree
	 */
	void enterCmdSismember(RedisParser.CmdSismemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSismember}.
	 * @param ctx the parse tree
	 */
	void exitCmdSismember(RedisParser.CmdSismemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSmembers}.
	 * @param ctx the parse tree
	 */
	void enterCmdSmembers(RedisParser.CmdSmembersContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSmembers}.
	 * @param ctx the parse tree
	 */
	void exitCmdSmembers(RedisParser.CmdSmembersContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSmismember}.
	 * @param ctx the parse tree
	 */
	void enterCmdSmismember(RedisParser.CmdSmismemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSmismember}.
	 * @param ctx the parse tree
	 */
	void exitCmdSmismember(RedisParser.CmdSmismemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSmove}.
	 * @param ctx the parse tree
	 */
	void enterCmdSmove(RedisParser.CmdSmoveContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSmove}.
	 * @param ctx the parse tree
	 */
	void exitCmdSmove(RedisParser.CmdSmoveContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSpop}.
	 * @param ctx the parse tree
	 */
	void enterCmdSpop(RedisParser.CmdSpopContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSpop}.
	 * @param ctx the parse tree
	 */
	void exitCmdSpop(RedisParser.CmdSpopContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSrandmember}.
	 * @param ctx the parse tree
	 */
	void enterCmdSrandmember(RedisParser.CmdSrandmemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSrandmember}.
	 * @param ctx the parse tree
	 */
	void exitCmdSrandmember(RedisParser.CmdSrandmemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSrem}.
	 * @param ctx the parse tree
	 */
	void enterCmdSrem(RedisParser.CmdSremContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSrem}.
	 * @param ctx the parse tree
	 */
	void exitCmdSrem(RedisParser.CmdSremContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSscan}.
	 * @param ctx the parse tree
	 */
	void enterCmdSscan(RedisParser.CmdSscanContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSscan}.
	 * @param ctx the parse tree
	 */
	void exitCmdSscan(RedisParser.CmdSscanContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSunion}.
	 * @param ctx the parse tree
	 */
	void enterCmdSunion(RedisParser.CmdSunionContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSunion}.
	 * @param ctx the parse tree
	 */
	void exitCmdSunion(RedisParser.CmdSunionContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSunionstore}.
	 * @param ctx the parse tree
	 */
	void enterCmdSunionstore(RedisParser.CmdSunionstoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSunionstore}.
	 * @param ctx the parse tree
	 */
	void exitCmdSunionstore(RedisParser.CmdSunionstoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdBzmpop}.
	 * @param ctx the parse tree
	 */
	void enterCmdBzmpop(RedisParser.CmdBzmpopContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdBzmpop}.
	 * @param ctx the parse tree
	 */
	void exitCmdBzmpop(RedisParser.CmdBzmpopContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdBzpopmax}.
	 * @param ctx the parse tree
	 */
	void enterCmdBzpopmax(RedisParser.CmdBzpopmaxContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdBzpopmax}.
	 * @param ctx the parse tree
	 */
	void exitCmdBzpopmax(RedisParser.CmdBzpopmaxContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdBzpopmin}.
	 * @param ctx the parse tree
	 */
	void enterCmdBzpopmin(RedisParser.CmdBzpopminContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdBzpopmin}.
	 * @param ctx the parse tree
	 */
	void exitCmdBzpopmin(RedisParser.CmdBzpopminContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZadd}.
	 * @param ctx the parse tree
	 */
	void enterCmdZadd(RedisParser.CmdZaddContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZadd}.
	 * @param ctx the parse tree
	 */
	void exitCmdZadd(RedisParser.CmdZaddContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#scoreAndMemberOpt}.
	 * @param ctx the parse tree
	 */
	void enterScoreAndMemberOpt(RedisParser.ScoreAndMemberOptContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#scoreAndMemberOpt}.
	 * @param ctx the parse tree
	 */
	void exitScoreAndMemberOpt(RedisParser.ScoreAndMemberOptContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZcard}.
	 * @param ctx the parse tree
	 */
	void enterCmdZcard(RedisParser.CmdZcardContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZcard}.
	 * @param ctx the parse tree
	 */
	void exitCmdZcard(RedisParser.CmdZcardContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZcount}.
	 * @param ctx the parse tree
	 */
	void enterCmdZcount(RedisParser.CmdZcountContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZcount}.
	 * @param ctx the parse tree
	 */
	void exitCmdZcount(RedisParser.CmdZcountContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZdiff}.
	 * @param ctx the parse tree
	 */
	void enterCmdZdiff(RedisParser.CmdZdiffContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZdiff}.
	 * @param ctx the parse tree
	 */
	void exitCmdZdiff(RedisParser.CmdZdiffContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZdiffStore}.
	 * @param ctx the parse tree
	 */
	void enterCmdZdiffStore(RedisParser.CmdZdiffStoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZdiffStore}.
	 * @param ctx the parse tree
	 */
	void exitCmdZdiffStore(RedisParser.CmdZdiffStoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZincrby}.
	 * @param ctx the parse tree
	 */
	void enterCmdZincrby(RedisParser.CmdZincrbyContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZincrby}.
	 * @param ctx the parse tree
	 */
	void exitCmdZincrby(RedisParser.CmdZincrbyContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZinter}.
	 * @param ctx the parse tree
	 */
	void enterCmdZinter(RedisParser.CmdZinterContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZinter}.
	 * @param ctx the parse tree
	 */
	void exitCmdZinter(RedisParser.CmdZinterContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZintercard}.
	 * @param ctx the parse tree
	 */
	void enterCmdZintercard(RedisParser.CmdZintercardContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZintercard}.
	 * @param ctx the parse tree
	 */
	void exitCmdZintercard(RedisParser.CmdZintercardContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZinterstore}.
	 * @param ctx the parse tree
	 */
	void enterCmdZinterstore(RedisParser.CmdZinterstoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZinterstore}.
	 * @param ctx the parse tree
	 */
	void exitCmdZinterstore(RedisParser.CmdZinterstoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZLexCount}.
	 * @param ctx the parse tree
	 */
	void enterCmdZLexCount(RedisParser.CmdZLexCountContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZLexCount}.
	 * @param ctx the parse tree
	 */
	void exitCmdZLexCount(RedisParser.CmdZLexCountContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZmpop}.
	 * @param ctx the parse tree
	 */
	void enterCmdZmpop(RedisParser.CmdZmpopContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZmpop}.
	 * @param ctx the parse tree
	 */
	void exitCmdZmpop(RedisParser.CmdZmpopContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZmscore}.
	 * @param ctx the parse tree
	 */
	void enterCmdZmscore(RedisParser.CmdZmscoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZmscore}.
	 * @param ctx the parse tree
	 */
	void exitCmdZmscore(RedisParser.CmdZmscoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZpopmax}.
	 * @param ctx the parse tree
	 */
	void enterCmdZpopmax(RedisParser.CmdZpopmaxContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZpopmax}.
	 * @param ctx the parse tree
	 */
	void exitCmdZpopmax(RedisParser.CmdZpopmaxContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZpopmin}.
	 * @param ctx the parse tree
	 */
	void enterCmdZpopmin(RedisParser.CmdZpopminContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZpopmin}.
	 * @param ctx the parse tree
	 */
	void exitCmdZpopmin(RedisParser.CmdZpopminContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZrandmember}.
	 * @param ctx the parse tree
	 */
	void enterCmdZrandmember(RedisParser.CmdZrandmemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZrandmember}.
	 * @param ctx the parse tree
	 */
	void exitCmdZrandmember(RedisParser.CmdZrandmemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZrange}.
	 * @param ctx the parse tree
	 */
	void enterCmdZrange(RedisParser.CmdZrangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZrange}.
	 * @param ctx the parse tree
	 */
	void exitCmdZrange(RedisParser.CmdZrangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZrangebylex}.
	 * @param ctx the parse tree
	 */
	void enterCmdZrangebylex(RedisParser.CmdZrangebylexContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZrangebylex}.
	 * @param ctx the parse tree
	 */
	void exitCmdZrangebylex(RedisParser.CmdZrangebylexContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZrangebyscore}.
	 * @param ctx the parse tree
	 */
	void enterCmdZrangebyscore(RedisParser.CmdZrangebyscoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZrangebyscore}.
	 * @param ctx the parse tree
	 */
	void exitCmdZrangebyscore(RedisParser.CmdZrangebyscoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZrangestore}.
	 * @param ctx the parse tree
	 */
	void enterCmdZrangestore(RedisParser.CmdZrangestoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZrangestore}.
	 * @param ctx the parse tree
	 */
	void exitCmdZrangestore(RedisParser.CmdZrangestoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZrank}.
	 * @param ctx the parse tree
	 */
	void enterCmdZrank(RedisParser.CmdZrankContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZrank}.
	 * @param ctx the parse tree
	 */
	void exitCmdZrank(RedisParser.CmdZrankContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZRem}.
	 * @param ctx the parse tree
	 */
	void enterCmdZRem(RedisParser.CmdZRemContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZRem}.
	 * @param ctx the parse tree
	 */
	void exitCmdZRem(RedisParser.CmdZRemContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZRemRangeByLex}.
	 * @param ctx the parse tree
	 */
	void enterCmdZRemRangeByLex(RedisParser.CmdZRemRangeByLexContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZRemRangeByLex}.
	 * @param ctx the parse tree
	 */
	void exitCmdZRemRangeByLex(RedisParser.CmdZRemRangeByLexContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZremrangebyrank}.
	 * @param ctx the parse tree
	 */
	void enterCmdZremrangebyrank(RedisParser.CmdZremrangebyrankContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZremrangebyrank}.
	 * @param ctx the parse tree
	 */
	void exitCmdZremrangebyrank(RedisParser.CmdZremrangebyrankContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZRemRangeByScore}.
	 * @param ctx the parse tree
	 */
	void enterCmdZRemRangeByScore(RedisParser.CmdZRemRangeByScoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZRemRangeByScore}.
	 * @param ctx the parse tree
	 */
	void exitCmdZRemRangeByScore(RedisParser.CmdZRemRangeByScoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZrevrange}.
	 * @param ctx the parse tree
	 */
	void enterCmdZrevrange(RedisParser.CmdZrevrangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZrevrange}.
	 * @param ctx the parse tree
	 */
	void exitCmdZrevrange(RedisParser.CmdZrevrangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZrevrangebylex}.
	 * @param ctx the parse tree
	 */
	void enterCmdZrevrangebylex(RedisParser.CmdZrevrangebylexContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZrevrangebylex}.
	 * @param ctx the parse tree
	 */
	void exitCmdZrevrangebylex(RedisParser.CmdZrevrangebylexContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZrevrangebyscore}.
	 * @param ctx the parse tree
	 */
	void enterCmdZrevrangebyscore(RedisParser.CmdZrevrangebyscoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZrevrangebyscore}.
	 * @param ctx the parse tree
	 */
	void exitCmdZrevrangebyscore(RedisParser.CmdZrevrangebyscoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZrevrank}.
	 * @param ctx the parse tree
	 */
	void enterCmdZrevrank(RedisParser.CmdZrevrankContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZrevrank}.
	 * @param ctx the parse tree
	 */
	void exitCmdZrevrank(RedisParser.CmdZrevrankContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZscan}.
	 * @param ctx the parse tree
	 */
	void enterCmdZscan(RedisParser.CmdZscanContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZscan}.
	 * @param ctx the parse tree
	 */
	void exitCmdZscan(RedisParser.CmdZscanContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZscore}.
	 * @param ctx the parse tree
	 */
	void enterCmdZscore(RedisParser.CmdZscoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZscore}.
	 * @param ctx the parse tree
	 */
	void exitCmdZscore(RedisParser.CmdZscoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZunion}.
	 * @param ctx the parse tree
	 */
	void enterCmdZunion(RedisParser.CmdZunionContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZunion}.
	 * @param ctx the parse tree
	 */
	void exitCmdZunion(RedisParser.CmdZunionContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdZunionstore}.
	 * @param ctx the parse tree
	 */
	void enterCmdZunionstore(RedisParser.CmdZunionstoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdZunionstore}.
	 * @param ctx the parse tree
	 */
	void exitCmdZunionstore(RedisParser.CmdZunionstoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdScriptDebug}.
	 * @param ctx the parse tree
	 */
	void enterCmdScriptDebug(RedisParser.CmdScriptDebugContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdScriptDebug}.
	 * @param ctx the parse tree
	 */
	void exitCmdScriptDebug(RedisParser.CmdScriptDebugContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdScriptExists}.
	 * @param ctx the parse tree
	 */
	void enterCmdScriptExists(RedisParser.CmdScriptExistsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdScriptExists}.
	 * @param ctx the parse tree
	 */
	void exitCmdScriptExists(RedisParser.CmdScriptExistsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdScriptFlush}.
	 * @param ctx the parse tree
	 */
	void enterCmdScriptFlush(RedisParser.CmdScriptFlushContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdScriptFlush}.
	 * @param ctx the parse tree
	 */
	void exitCmdScriptFlush(RedisParser.CmdScriptFlushContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdScriptKill}.
	 * @param ctx the parse tree
	 */
	void enterCmdScriptKill(RedisParser.CmdScriptKillContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdScriptKill}.
	 * @param ctx the parse tree
	 */
	void exitCmdScriptKill(RedisParser.CmdScriptKillContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdScriptLoad}.
	 * @param ctx the parse tree
	 */
	void enterCmdScriptLoad(RedisParser.CmdScriptLoadContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdScriptLoad}.
	 * @param ctx the parse tree
	 */
	void exitCmdScriptLoad(RedisParser.CmdScriptLoadContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdEval}.
	 * @param ctx the parse tree
	 */
	void enterCmdEval(RedisParser.CmdEvalContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdEval}.
	 * @param ctx the parse tree
	 */
	void exitCmdEval(RedisParser.CmdEvalContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdEvalRO}.
	 * @param ctx the parse tree
	 */
	void enterCmdEvalRO(RedisParser.CmdEvalROContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdEvalRO}.
	 * @param ctx the parse tree
	 */
	void exitCmdEvalRO(RedisParser.CmdEvalROContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdEvalsha}.
	 * @param ctx the parse tree
	 */
	void enterCmdEvalsha(RedisParser.CmdEvalshaContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdEvalsha}.
	 * @param ctx the parse tree
	 */
	void exitCmdEvalsha(RedisParser.CmdEvalshaContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdEvalshaRO}.
	 * @param ctx the parse tree
	 */
	void enterCmdEvalshaRO(RedisParser.CmdEvalshaROContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdEvalshaRO}.
	 * @param ctx the parse tree
	 */
	void exitCmdEvalshaRO(RedisParser.CmdEvalshaROContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdFCall}.
	 * @param ctx the parse tree
	 */
	void enterCmdFCall(RedisParser.CmdFCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdFCall}.
	 * @param ctx the parse tree
	 */
	void exitCmdFCall(RedisParser.CmdFCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdFCallRO}.
	 * @param ctx the parse tree
	 */
	void enterCmdFCallRO(RedisParser.CmdFCallROContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdFCallRO}.
	 * @param ctx the parse tree
	 */
	void exitCmdFCallRO(RedisParser.CmdFCallROContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdFunctionDel}.
	 * @param ctx the parse tree
	 */
	void enterCmdFunctionDel(RedisParser.CmdFunctionDelContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdFunctionDel}.
	 * @param ctx the parse tree
	 */
	void exitCmdFunctionDel(RedisParser.CmdFunctionDelContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdFunctionDump}.
	 * @param ctx the parse tree
	 */
	void enterCmdFunctionDump(RedisParser.CmdFunctionDumpContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdFunctionDump}.
	 * @param ctx the parse tree
	 */
	void exitCmdFunctionDump(RedisParser.CmdFunctionDumpContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdFunctionFlush}.
	 * @param ctx the parse tree
	 */
	void enterCmdFunctionFlush(RedisParser.CmdFunctionFlushContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdFunctionFlush}.
	 * @param ctx the parse tree
	 */
	void exitCmdFunctionFlush(RedisParser.CmdFunctionFlushContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdFunctionKill}.
	 * @param ctx the parse tree
	 */
	void enterCmdFunctionKill(RedisParser.CmdFunctionKillContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdFunctionKill}.
	 * @param ctx the parse tree
	 */
	void exitCmdFunctionKill(RedisParser.CmdFunctionKillContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdFunctionList}.
	 * @param ctx the parse tree
	 */
	void enterCmdFunctionList(RedisParser.CmdFunctionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdFunctionList}.
	 * @param ctx the parse tree
	 */
	void exitCmdFunctionList(RedisParser.CmdFunctionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdFunctionLoad}.
	 * @param ctx the parse tree
	 */
	void enterCmdFunctionLoad(RedisParser.CmdFunctionLoadContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdFunctionLoad}.
	 * @param ctx the parse tree
	 */
	void exitCmdFunctionLoad(RedisParser.CmdFunctionLoadContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdFunctionRestore}.
	 * @param ctx the parse tree
	 */
	void enterCmdFunctionRestore(RedisParser.CmdFunctionRestoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdFunctionRestore}.
	 * @param ctx the parse tree
	 */
	void exitCmdFunctionRestore(RedisParser.CmdFunctionRestoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdFunctionStats}.
	 * @param ctx the parse tree
	 */
	void enterCmdFunctionStats(RedisParser.CmdFunctionStatsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdFunctionStats}.
	 * @param ctx the parse tree
	 */
	void exitCmdFunctionStats(RedisParser.CmdFunctionStatsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdDiscard}.
	 * @param ctx the parse tree
	 */
	void enterCmdDiscard(RedisParser.CmdDiscardContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdDiscard}.
	 * @param ctx the parse tree
	 */
	void exitCmdDiscard(RedisParser.CmdDiscardContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdExec}.
	 * @param ctx the parse tree
	 */
	void enterCmdExec(RedisParser.CmdExecContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdExec}.
	 * @param ctx the parse tree
	 */
	void exitCmdExec(RedisParser.CmdExecContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdMulti}.
	 * @param ctx the parse tree
	 */
	void enterCmdMulti(RedisParser.CmdMultiContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdMulti}.
	 * @param ctx the parse tree
	 */
	void exitCmdMulti(RedisParser.CmdMultiContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdUnwatch}.
	 * @param ctx the parse tree
	 */
	void enterCmdUnwatch(RedisParser.CmdUnwatchContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdUnwatch}.
	 * @param ctx the parse tree
	 */
	void exitCmdUnwatch(RedisParser.CmdUnwatchContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdWatch}.
	 * @param ctx the parse tree
	 */
	void enterCmdWatch(RedisParser.CmdWatchContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdWatch}.
	 * @param ctx the parse tree
	 */
	void exitCmdWatch(RedisParser.CmdWatchContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdPFAdd}.
	 * @param ctx the parse tree
	 */
	void enterCmdPFAdd(RedisParser.CmdPFAddContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdPFAdd}.
	 * @param ctx the parse tree
	 */
	void exitCmdPFAdd(RedisParser.CmdPFAddContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdPFCount}.
	 * @param ctx the parse tree
	 */
	void enterCmdPFCount(RedisParser.CmdPFCountContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdPFCount}.
	 * @param ctx the parse tree
	 */
	void exitCmdPFCount(RedisParser.CmdPFCountContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdPFMerge}.
	 * @param ctx the parse tree
	 */
	void enterCmdPFMerge(RedisParser.CmdPFMergeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdPFMerge}.
	 * @param ctx the parse tree
	 */
	void exitCmdPFMerge(RedisParser.CmdPFMergeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdPSubscribe}.
	 * @param ctx the parse tree
	 */
	void enterCmdPSubscribe(RedisParser.CmdPSubscribeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdPSubscribe}.
	 * @param ctx the parse tree
	 */
	void exitCmdPSubscribe(RedisParser.CmdPSubscribeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdPublish}.
	 * @param ctx the parse tree
	 */
	void enterCmdPublish(RedisParser.CmdPublishContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdPublish}.
	 * @param ctx the parse tree
	 */
	void exitCmdPublish(RedisParser.CmdPublishContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdPubSubChannels}.
	 * @param ctx the parse tree
	 */
	void enterCmdPubSubChannels(RedisParser.CmdPubSubChannelsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdPubSubChannels}.
	 * @param ctx the parse tree
	 */
	void exitCmdPubSubChannels(RedisParser.CmdPubSubChannelsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdPubSubNumPat}.
	 * @param ctx the parse tree
	 */
	void enterCmdPubSubNumPat(RedisParser.CmdPubSubNumPatContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdPubSubNumPat}.
	 * @param ctx the parse tree
	 */
	void exitCmdPubSubNumPat(RedisParser.CmdPubSubNumPatContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdPubSubNumSub}.
	 * @param ctx the parse tree
	 */
	void enterCmdPubSubNumSub(RedisParser.CmdPubSubNumSubContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdPubSubNumSub}.
	 * @param ctx the parse tree
	 */
	void exitCmdPubSubNumSub(RedisParser.CmdPubSubNumSubContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdPubSubShardChannels}.
	 * @param ctx the parse tree
	 */
	void enterCmdPubSubShardChannels(RedisParser.CmdPubSubShardChannelsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdPubSubShardChannels}.
	 * @param ctx the parse tree
	 */
	void exitCmdPubSubShardChannels(RedisParser.CmdPubSubShardChannelsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdPubSubShardNumSub}.
	 * @param ctx the parse tree
	 */
	void enterCmdPubSubShardNumSub(RedisParser.CmdPubSubShardNumSubContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdPubSubShardNumSub}.
	 * @param ctx the parse tree
	 */
	void exitCmdPubSubShardNumSub(RedisParser.CmdPubSubShardNumSubContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdPunSubscribe}.
	 * @param ctx the parse tree
	 */
	void enterCmdPunSubscribe(RedisParser.CmdPunSubscribeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdPunSubscribe}.
	 * @param ctx the parse tree
	 */
	void exitCmdPunSubscribe(RedisParser.CmdPunSubscribeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSpublish}.
	 * @param ctx the parse tree
	 */
	void enterCmdSpublish(RedisParser.CmdSpublishContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSpublish}.
	 * @param ctx the parse tree
	 */
	void exitCmdSpublish(RedisParser.CmdSpublishContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSSubscribe}.
	 * @param ctx the parse tree
	 */
	void enterCmdSSubscribe(RedisParser.CmdSSubscribeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSSubscribe}.
	 * @param ctx the parse tree
	 */
	void exitCmdSSubscribe(RedisParser.CmdSSubscribeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSubscribe}.
	 * @param ctx the parse tree
	 */
	void enterCmdSubscribe(RedisParser.CmdSubscribeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSubscribe}.
	 * @param ctx the parse tree
	 */
	void exitCmdSubscribe(RedisParser.CmdSubscribeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSunSubscribe}.
	 * @param ctx the parse tree
	 */
	void enterCmdSunSubscribe(RedisParser.CmdSunSubscribeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSunSubscribe}.
	 * @param ctx the parse tree
	 */
	void exitCmdSunSubscribe(RedisParser.CmdSunSubscribeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdUnSubScribe}.
	 * @param ctx the parse tree
	 */
	void enterCmdUnSubScribe(RedisParser.CmdUnSubScribeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdUnSubScribe}.
	 * @param ctx the parse tree
	 */
	void exitCmdUnSubScribe(RedisParser.CmdUnSubScribeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdAsking}.
	 * @param ctx the parse tree
	 */
	void enterCmdAsking(RedisParser.CmdAskingContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdAsking}.
	 * @param ctx the parse tree
	 */
	void exitCmdAsking(RedisParser.CmdAskingContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdReadonly}.
	 * @param ctx the parse tree
	 */
	void enterCmdReadonly(RedisParser.CmdReadonlyContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdReadonly}.
	 * @param ctx the parse tree
	 */
	void exitCmdReadonly(RedisParser.CmdReadonlyContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdReadWrite}.
	 * @param ctx the parse tree
	 */
	void enterCmdReadWrite(RedisParser.CmdReadWriteContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdReadWrite}.
	 * @param ctx the parse tree
	 */
	void exitCmdReadWrite(RedisParser.CmdReadWriteContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterAddSlots}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterAddSlots(RedisParser.CmdClusterAddSlotsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterAddSlots}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterAddSlots(RedisParser.CmdClusterAddSlotsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterDelSlots}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterDelSlots(RedisParser.CmdClusterDelSlotsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterDelSlots}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterDelSlots(RedisParser.CmdClusterDelSlotsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterAddSlotsRange}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterAddSlotsRange(RedisParser.CmdClusterAddSlotsRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterAddSlotsRange}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterAddSlotsRange(RedisParser.CmdClusterAddSlotsRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterDelSlotsRange}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterDelSlotsRange(RedisParser.CmdClusterDelSlotsRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterDelSlotsRange}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterDelSlotsRange(RedisParser.CmdClusterDelSlotsRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#clusterStartAndEnd}.
	 * @param ctx the parse tree
	 */
	void enterClusterStartAndEnd(RedisParser.ClusterStartAndEndContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#clusterStartAndEnd}.
	 * @param ctx the parse tree
	 */
	void exitClusterStartAndEnd(RedisParser.ClusterStartAndEndContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterBumpEpoch}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterBumpEpoch(RedisParser.CmdClusterBumpEpochContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterBumpEpoch}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterBumpEpoch(RedisParser.CmdClusterBumpEpochContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterCountFailureReports}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterCountFailureReports(RedisParser.CmdClusterCountFailureReportsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterCountFailureReports}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterCountFailureReports(RedisParser.CmdClusterCountFailureReportsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterCountKeysInSlot}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterCountKeysInSlot(RedisParser.CmdClusterCountKeysInSlotContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterCountKeysInSlot}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterCountKeysInSlot(RedisParser.CmdClusterCountKeysInSlotContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterFailOver}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterFailOver(RedisParser.CmdClusterFailOverContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterFailOver}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterFailOver(RedisParser.CmdClusterFailOverContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterFlushSlots}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterFlushSlots(RedisParser.CmdClusterFlushSlotsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterFlushSlots}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterFlushSlots(RedisParser.CmdClusterFlushSlotsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterForget}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterForget(RedisParser.CmdClusterForgetContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterForget}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterForget(RedisParser.CmdClusterForgetContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterGetKeysInSlot}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterGetKeysInSlot(RedisParser.CmdClusterGetKeysInSlotContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterGetKeysInSlot}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterGetKeysInSlot(RedisParser.CmdClusterGetKeysInSlotContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterInfo}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterInfo(RedisParser.CmdClusterInfoContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterInfo}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterInfo(RedisParser.CmdClusterInfoContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterKeySlot}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterKeySlot(RedisParser.CmdClusterKeySlotContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterKeySlot}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterKeySlot(RedisParser.CmdClusterKeySlotContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterLinks}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterLinks(RedisParser.CmdClusterLinksContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterLinks}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterLinks(RedisParser.CmdClusterLinksContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterMeet}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterMeet(RedisParser.CmdClusterMeetContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterMeet}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterMeet(RedisParser.CmdClusterMeetContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterMyId}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterMyId(RedisParser.CmdClusterMyIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterMyId}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterMyId(RedisParser.CmdClusterMyIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterMyShardId}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterMyShardId(RedisParser.CmdClusterMyShardIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterMyShardId}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterMyShardId(RedisParser.CmdClusterMyShardIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterNodes}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterNodes(RedisParser.CmdClusterNodesContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterNodes}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterNodes(RedisParser.CmdClusterNodesContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterReplicas}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterReplicas(RedisParser.CmdClusterReplicasContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterReplicas}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterReplicas(RedisParser.CmdClusterReplicasContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterReplicate}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterReplicate(RedisParser.CmdClusterReplicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterReplicate}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterReplicate(RedisParser.CmdClusterReplicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterReset}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterReset(RedisParser.CmdClusterResetContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterReset}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterReset(RedisParser.CmdClusterResetContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterSaveConfig}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterSaveConfig(RedisParser.CmdClusterSaveConfigContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterSaveConfig}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterSaveConfig(RedisParser.CmdClusterSaveConfigContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterSetConfigEpoch}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterSetConfigEpoch(RedisParser.CmdClusterSetConfigEpochContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterSetConfigEpoch}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterSetConfigEpoch(RedisParser.CmdClusterSetConfigEpochContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterSetSlot}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterSetSlot(RedisParser.CmdClusterSetSlotContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterSetSlot}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterSetSlot(RedisParser.CmdClusterSetSlotContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterShards}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterShards(RedisParser.CmdClusterShardsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterShards}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterShards(RedisParser.CmdClusterShardsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterSlaves}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterSlaves(RedisParser.CmdClusterSlavesContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterSlaves}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterSlaves(RedisParser.CmdClusterSlavesContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterSlotStats}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterSlotStats(RedisParser.CmdClusterSlotStatsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterSlotStats}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterSlotStats(RedisParser.CmdClusterSlotStatsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClusterSlots}.
	 * @param ctx the parse tree
	 */
	void enterCmdClusterSlots(RedisParser.CmdClusterSlotsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClusterSlots}.
	 * @param ctx the parse tree
	 */
	void exitCmdClusterSlots(RedisParser.CmdClusterSlotsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdInfo}.
	 * @param ctx the parse tree
	 */
	void enterCmdInfo(RedisParser.CmdInfoContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdInfo}.
	 * @param ctx the parse tree
	 */
	void exitCmdInfo(RedisParser.CmdInfoContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#infoOpt}.
	 * @param ctx the parse tree
	 */
	void enterInfoOpt(RedisParser.InfoOptContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#infoOpt}.
	 * @param ctx the parse tree
	 */
	void exitInfoOpt(RedisParser.InfoOptContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#username}.
	 * @param ctx the parse tree
	 */
	void enterUsername(RedisParser.UsernameContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#username}.
	 * @param ctx the parse tree
	 */
	void exitUsername(RedisParser.UsernameContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#command}.
	 * @param ctx the parse tree
	 */
	void enterCommand(RedisParser.CommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#command}.
	 * @param ctx the parse tree
	 */
	void exitCommand(RedisParser.CommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdAclCat}.
	 * @param ctx the parse tree
	 */
	void enterCmdAclCat(RedisParser.CmdAclCatContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdAclCat}.
	 * @param ctx the parse tree
	 */
	void exitCmdAclCat(RedisParser.CmdAclCatContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdAclDelUser}.
	 * @param ctx the parse tree
	 */
	void enterCmdAclDelUser(RedisParser.CmdAclDelUserContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdAclDelUser}.
	 * @param ctx the parse tree
	 */
	void exitCmdAclDelUser(RedisParser.CmdAclDelUserContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdAclDryRun}.
	 * @param ctx the parse tree
	 */
	void enterCmdAclDryRun(RedisParser.CmdAclDryRunContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdAclDryRun}.
	 * @param ctx the parse tree
	 */
	void exitCmdAclDryRun(RedisParser.CmdAclDryRunContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdAclGenPass}.
	 * @param ctx the parse tree
	 */
	void enterCmdAclGenPass(RedisParser.CmdAclGenPassContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdAclGenPass}.
	 * @param ctx the parse tree
	 */
	void exitCmdAclGenPass(RedisParser.CmdAclGenPassContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdAclGetUser}.
	 * @param ctx the parse tree
	 */
	void enterCmdAclGetUser(RedisParser.CmdAclGetUserContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdAclGetUser}.
	 * @param ctx the parse tree
	 */
	void exitCmdAclGetUser(RedisParser.CmdAclGetUserContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdAclList}.
	 * @param ctx the parse tree
	 */
	void enterCmdAclList(RedisParser.CmdAclListContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdAclList}.
	 * @param ctx the parse tree
	 */
	void exitCmdAclList(RedisParser.CmdAclListContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdAclLoad}.
	 * @param ctx the parse tree
	 */
	void enterCmdAclLoad(RedisParser.CmdAclLoadContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdAclLoad}.
	 * @param ctx the parse tree
	 */
	void exitCmdAclLoad(RedisParser.CmdAclLoadContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdAclLog}.
	 * @param ctx the parse tree
	 */
	void enterCmdAclLog(RedisParser.CmdAclLogContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdAclLog}.
	 * @param ctx the parse tree
	 */
	void exitCmdAclLog(RedisParser.CmdAclLogContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdAclSave}.
	 * @param ctx the parse tree
	 */
	void enterCmdAclSave(RedisParser.CmdAclSaveContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdAclSave}.
	 * @param ctx the parse tree
	 */
	void exitCmdAclSave(RedisParser.CmdAclSaveContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdAclSetUser}.
	 * @param ctx the parse tree
	 */
	void enterCmdAclSetUser(RedisParser.CmdAclSetUserContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdAclSetUser}.
	 * @param ctx the parse tree
	 */
	void exitCmdAclSetUser(RedisParser.CmdAclSetUserContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdAclUsers}.
	 * @param ctx the parse tree
	 */
	void enterCmdAclUsers(RedisParser.CmdAclUsersContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdAclUsers}.
	 * @param ctx the parse tree
	 */
	void exitCmdAclUsers(RedisParser.CmdAclUsersContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdAclWhoami}.
	 * @param ctx the parse tree
	 */
	void enterCmdAclWhoami(RedisParser.CmdAclWhoamiContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdAclWhoami}.
	 * @param ctx the parse tree
	 */
	void exitCmdAclWhoami(RedisParser.CmdAclWhoamiContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdCommand}.
	 * @param ctx the parse tree
	 */
	void enterCmdCommand(RedisParser.CmdCommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdCommand}.
	 * @param ctx the parse tree
	 */
	void exitCmdCommand(RedisParser.CmdCommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdCommandCount}.
	 * @param ctx the parse tree
	 */
	void enterCmdCommandCount(RedisParser.CmdCommandCountContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdCommandCount}.
	 * @param ctx the parse tree
	 */
	void exitCmdCommandCount(RedisParser.CmdCommandCountContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdCommandDocs}.
	 * @param ctx the parse tree
	 */
	void enterCmdCommandDocs(RedisParser.CmdCommandDocsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdCommandDocs}.
	 * @param ctx the parse tree
	 */
	void exitCmdCommandDocs(RedisParser.CmdCommandDocsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdCommandGetKeys}.
	 * @param ctx the parse tree
	 */
	void enterCmdCommandGetKeys(RedisParser.CmdCommandGetKeysContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdCommandGetKeys}.
	 * @param ctx the parse tree
	 */
	void exitCmdCommandGetKeys(RedisParser.CmdCommandGetKeysContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdCommandGetKeysAndFlags}.
	 * @param ctx the parse tree
	 */
	void enterCmdCommandGetKeysAndFlags(RedisParser.CmdCommandGetKeysAndFlagsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdCommandGetKeysAndFlags}.
	 * @param ctx the parse tree
	 */
	void exitCmdCommandGetKeysAndFlags(RedisParser.CmdCommandGetKeysAndFlagsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdCommandInfo}.
	 * @param ctx the parse tree
	 */
	void enterCmdCommandInfo(RedisParser.CmdCommandInfoContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdCommandInfo}.
	 * @param ctx the parse tree
	 */
	void exitCmdCommandInfo(RedisParser.CmdCommandInfoContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdCommandList}.
	 * @param ctx the parse tree
	 */
	void enterCmdCommandList(RedisParser.CmdCommandListContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdCommandList}.
	 * @param ctx the parse tree
	 */
	void exitCmdCommandList(RedisParser.CmdCommandListContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdConfigGet}.
	 * @param ctx the parse tree
	 */
	void enterCmdConfigGet(RedisParser.CmdConfigGetContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdConfigGet}.
	 * @param ctx the parse tree
	 */
	void exitCmdConfigGet(RedisParser.CmdConfigGetContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdConfigSet}.
	 * @param ctx the parse tree
	 */
	void enterCmdConfigSet(RedisParser.CmdConfigSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdConfigSet}.
	 * @param ctx the parse tree
	 */
	void exitCmdConfigSet(RedisParser.CmdConfigSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdConfigResetStat}.
	 * @param ctx the parse tree
	 */
	void enterCmdConfigResetStat(RedisParser.CmdConfigResetStatContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdConfigResetStat}.
	 * @param ctx the parse tree
	 */
	void exitCmdConfigResetStat(RedisParser.CmdConfigResetStatContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdConfigRewrite}.
	 * @param ctx the parse tree
	 */
	void enterCmdConfigRewrite(RedisParser.CmdConfigRewriteContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdConfigRewrite}.
	 * @param ctx the parse tree
	 */
	void exitCmdConfigRewrite(RedisParser.CmdConfigRewriteContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#event}.
	 * @param ctx the parse tree
	 */
	void enterEvent(RedisParser.EventContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#event}.
	 * @param ctx the parse tree
	 */
	void exitEvent(RedisParser.EventContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdLatencyDoctor}.
	 * @param ctx the parse tree
	 */
	void enterCmdLatencyDoctor(RedisParser.CmdLatencyDoctorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdLatencyDoctor}.
	 * @param ctx the parse tree
	 */
	void exitCmdLatencyDoctor(RedisParser.CmdLatencyDoctorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdLatencyGraph}.
	 * @param ctx the parse tree
	 */
	void enterCmdLatencyGraph(RedisParser.CmdLatencyGraphContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdLatencyGraph}.
	 * @param ctx the parse tree
	 */
	void exitCmdLatencyGraph(RedisParser.CmdLatencyGraphContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdLatencyHistogram}.
	 * @param ctx the parse tree
	 */
	void enterCmdLatencyHistogram(RedisParser.CmdLatencyHistogramContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdLatencyHistogram}.
	 * @param ctx the parse tree
	 */
	void exitCmdLatencyHistogram(RedisParser.CmdLatencyHistogramContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdLatencyHistory}.
	 * @param ctx the parse tree
	 */
	void enterCmdLatencyHistory(RedisParser.CmdLatencyHistoryContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdLatencyHistory}.
	 * @param ctx the parse tree
	 */
	void exitCmdLatencyHistory(RedisParser.CmdLatencyHistoryContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdLatencyLatest}.
	 * @param ctx the parse tree
	 */
	void enterCmdLatencyLatest(RedisParser.CmdLatencyLatestContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdLatencyLatest}.
	 * @param ctx the parse tree
	 */
	void exitCmdLatencyLatest(RedisParser.CmdLatencyLatestContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdLatencyReset}.
	 * @param ctx the parse tree
	 */
	void enterCmdLatencyReset(RedisParser.CmdLatencyResetContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdLatencyReset}.
	 * @param ctx the parse tree
	 */
	void exitCmdLatencyReset(RedisParser.CmdLatencyResetContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdMemoryDoctor}.
	 * @param ctx the parse tree
	 */
	void enterCmdMemoryDoctor(RedisParser.CmdMemoryDoctorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdMemoryDoctor}.
	 * @param ctx the parse tree
	 */
	void exitCmdMemoryDoctor(RedisParser.CmdMemoryDoctorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdMemoryMallocStats}.
	 * @param ctx the parse tree
	 */
	void enterCmdMemoryMallocStats(RedisParser.CmdMemoryMallocStatsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdMemoryMallocStats}.
	 * @param ctx the parse tree
	 */
	void exitCmdMemoryMallocStats(RedisParser.CmdMemoryMallocStatsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdMemoryPurge}.
	 * @param ctx the parse tree
	 */
	void enterCmdMemoryPurge(RedisParser.CmdMemoryPurgeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdMemoryPurge}.
	 * @param ctx the parse tree
	 */
	void exitCmdMemoryPurge(RedisParser.CmdMemoryPurgeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdMemoryStats}.
	 * @param ctx the parse tree
	 */
	void enterCmdMemoryStats(RedisParser.CmdMemoryStatsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdMemoryStats}.
	 * @param ctx the parse tree
	 */
	void exitCmdMemoryStats(RedisParser.CmdMemoryStatsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdMemoryUsage}.
	 * @param ctx the parse tree
	 */
	void enterCmdMemoryUsage(RedisParser.CmdMemoryUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdMemoryUsage}.
	 * @param ctx the parse tree
	 */
	void exitCmdMemoryUsage(RedisParser.CmdMemoryUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#configKV}.
	 * @param ctx the parse tree
	 */
	void enterConfigKV(RedisParser.ConfigKVContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#configKV}.
	 * @param ctx the parse tree
	 */
	void exitConfigKV(RedisParser.ConfigKVContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdModuleList}.
	 * @param ctx the parse tree
	 */
	void enterCmdModuleList(RedisParser.CmdModuleListContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdModuleList}.
	 * @param ctx the parse tree
	 */
	void exitCmdModuleList(RedisParser.CmdModuleListContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdModuleLoad}.
	 * @param ctx the parse tree
	 */
	void enterCmdModuleLoad(RedisParser.CmdModuleLoadContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdModuleLoad}.
	 * @param ctx the parse tree
	 */
	void exitCmdModuleLoad(RedisParser.CmdModuleLoadContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdModuleLoadEx}.
	 * @param ctx the parse tree
	 */
	void enterCmdModuleLoadEx(RedisParser.CmdModuleLoadExContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdModuleLoadEx}.
	 * @param ctx the parse tree
	 */
	void exitCmdModuleLoadEx(RedisParser.CmdModuleLoadExContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdModuleUnload}.
	 * @param ctx the parse tree
	 */
	void enterCmdModuleUnload(RedisParser.CmdModuleUnloadContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdModuleUnload}.
	 * @param ctx the parse tree
	 */
	void exitCmdModuleUnload(RedisParser.CmdModuleUnloadContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdBgrewriteaof}.
	 * @param ctx the parse tree
	 */
	void enterCmdBgrewriteaof(RedisParser.CmdBgrewriteaofContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdBgrewriteaof}.
	 * @param ctx the parse tree
	 */
	void exitCmdBgrewriteaof(RedisParser.CmdBgrewriteaofContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdBgsave}.
	 * @param ctx the parse tree
	 */
	void enterCmdBgsave(RedisParser.CmdBgsaveContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdBgsave}.
	 * @param ctx the parse tree
	 */
	void exitCmdBgsave(RedisParser.CmdBgsaveContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdDbsize}.
	 * @param ctx the parse tree
	 */
	void enterCmdDbsize(RedisParser.CmdDbsizeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdDbsize}.
	 * @param ctx the parse tree
	 */
	void exitCmdDbsize(RedisParser.CmdDbsizeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdFailover}.
	 * @param ctx the parse tree
	 */
	void enterCmdFailover(RedisParser.CmdFailoverContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdFailover}.
	 * @param ctx the parse tree
	 */
	void exitCmdFailover(RedisParser.CmdFailoverContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdFlushAll}.
	 * @param ctx the parse tree
	 */
	void enterCmdFlushAll(RedisParser.CmdFlushAllContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdFlushAll}.
	 * @param ctx the parse tree
	 */
	void exitCmdFlushAll(RedisParser.CmdFlushAllContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdFlushDB}.
	 * @param ctx the parse tree
	 */
	void enterCmdFlushDB(RedisParser.CmdFlushDBContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdFlushDB}.
	 * @param ctx the parse tree
	 */
	void exitCmdFlushDB(RedisParser.CmdFlushDBContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdLastsave}.
	 * @param ctx the parse tree
	 */
	void enterCmdLastsave(RedisParser.CmdLastsaveContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdLastsave}.
	 * @param ctx the parse tree
	 */
	void exitCmdLastsave(RedisParser.CmdLastsaveContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdLolwut}.
	 * @param ctx the parse tree
	 */
	void enterCmdLolwut(RedisParser.CmdLolwutContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdLolwut}.
	 * @param ctx the parse tree
	 */
	void exitCmdLolwut(RedisParser.CmdLolwutContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdMonitor}.
	 * @param ctx the parse tree
	 */
	void enterCmdMonitor(RedisParser.CmdMonitorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdMonitor}.
	 * @param ctx the parse tree
	 */
	void exitCmdMonitor(RedisParser.CmdMonitorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdPSync}.
	 * @param ctx the parse tree
	 */
	void enterCmdPSync(RedisParser.CmdPSyncContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdPSync}.
	 * @param ctx the parse tree
	 */
	void exitCmdPSync(RedisParser.CmdPSyncContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdReplicaOf}.
	 * @param ctx the parse tree
	 */
	void enterCmdReplicaOf(RedisParser.CmdReplicaOfContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdReplicaOf}.
	 * @param ctx the parse tree
	 */
	void exitCmdReplicaOf(RedisParser.CmdReplicaOfContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdRole}.
	 * @param ctx the parse tree
	 */
	void enterCmdRole(RedisParser.CmdRoleContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdRole}.
	 * @param ctx the parse tree
	 */
	void exitCmdRole(RedisParser.CmdRoleContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSave}.
	 * @param ctx the parse tree
	 */
	void enterCmdSave(RedisParser.CmdSaveContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSave}.
	 * @param ctx the parse tree
	 */
	void exitCmdSave(RedisParser.CmdSaveContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdShutdown}.
	 * @param ctx the parse tree
	 */
	void enterCmdShutdown(RedisParser.CmdShutdownContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdShutdown}.
	 * @param ctx the parse tree
	 */
	void exitCmdShutdown(RedisParser.CmdShutdownContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSlaveOf}.
	 * @param ctx the parse tree
	 */
	void enterCmdSlaveOf(RedisParser.CmdSlaveOfContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSlaveOf}.
	 * @param ctx the parse tree
	 */
	void exitCmdSlaveOf(RedisParser.CmdSlaveOfContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSlowlogGet}.
	 * @param ctx the parse tree
	 */
	void enterCmdSlowlogGet(RedisParser.CmdSlowlogGetContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSlowlogGet}.
	 * @param ctx the parse tree
	 */
	void exitCmdSlowlogGet(RedisParser.CmdSlowlogGetContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSlowlogLen}.
	 * @param ctx the parse tree
	 */
	void enterCmdSlowlogLen(RedisParser.CmdSlowlogLenContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSlowlogLen}.
	 * @param ctx the parse tree
	 */
	void exitCmdSlowlogLen(RedisParser.CmdSlowlogLenContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSlowlogReset}.
	 * @param ctx the parse tree
	 */
	void enterCmdSlowlogReset(RedisParser.CmdSlowlogResetContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSlowlogReset}.
	 * @param ctx the parse tree
	 */
	void exitCmdSlowlogReset(RedisParser.CmdSlowlogResetContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSwapDB}.
	 * @param ctx the parse tree
	 */
	void enterCmdSwapDB(RedisParser.CmdSwapDBContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSwapDB}.
	 * @param ctx the parse tree
	 */
	void exitCmdSwapDB(RedisParser.CmdSwapDBContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSync}.
	 * @param ctx the parse tree
	 */
	void enterCmdSync(RedisParser.CmdSyncContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSync}.
	 * @param ctx the parse tree
	 */
	void exitCmdSync(RedisParser.CmdSyncContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdTime}.
	 * @param ctx the parse tree
	 */
	void enterCmdTime(RedisParser.CmdTimeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdTime}.
	 * @param ctx the parse tree
	 */
	void exitCmdTime(RedisParser.CmdTimeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#clientId}.
	 * @param ctx the parse tree
	 */
	void enterClientId(RedisParser.ClientIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#clientId}.
	 * @param ctx the parse tree
	 */
	void exitClientId(RedisParser.ClientIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClientCaching}.
	 * @param ctx the parse tree
	 */
	void enterCmdClientCaching(RedisParser.CmdClientCachingContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClientCaching}.
	 * @param ctx the parse tree
	 */
	void exitCmdClientCaching(RedisParser.CmdClientCachingContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClientGetname}.
	 * @param ctx the parse tree
	 */
	void enterCmdClientGetname(RedisParser.CmdClientGetnameContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClientGetname}.
	 * @param ctx the parse tree
	 */
	void exitCmdClientGetname(RedisParser.CmdClientGetnameContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClientGetredir}.
	 * @param ctx the parse tree
	 */
	void enterCmdClientGetredir(RedisParser.CmdClientGetredirContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClientGetredir}.
	 * @param ctx the parse tree
	 */
	void exitCmdClientGetredir(RedisParser.CmdClientGetredirContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClientID}.
	 * @param ctx the parse tree
	 */
	void enterCmdClientID(RedisParser.CmdClientIDContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClientID}.
	 * @param ctx the parse tree
	 */
	void exitCmdClientID(RedisParser.CmdClientIDContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClientInfo}.
	 * @param ctx the parse tree
	 */
	void enterCmdClientInfo(RedisParser.CmdClientInfoContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClientInfo}.
	 * @param ctx the parse tree
	 */
	void exitCmdClientInfo(RedisParser.CmdClientInfoContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClientKill}.
	 * @param ctx the parse tree
	 */
	void enterCmdClientKill(RedisParser.CmdClientKillContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClientKill}.
	 * @param ctx the parse tree
	 */
	void exitCmdClientKill(RedisParser.CmdClientKillContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#killBy}.
	 * @param ctx the parse tree
	 */
	void enterKillBy(RedisParser.KillByContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#killBy}.
	 * @param ctx the parse tree
	 */
	void exitKillBy(RedisParser.KillByContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#killById}.
	 * @param ctx the parse tree
	 */
	void enterKillById(RedisParser.KillByIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#killById}.
	 * @param ctx the parse tree
	 */
	void exitKillById(RedisParser.KillByIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#killByType}.
	 * @param ctx the parse tree
	 */
	void enterKillByType(RedisParser.KillByTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#killByType}.
	 * @param ctx the parse tree
	 */
	void exitKillByType(RedisParser.KillByTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#killByUser}.
	 * @param ctx the parse tree
	 */
	void enterKillByUser(RedisParser.KillByUserContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#killByUser}.
	 * @param ctx the parse tree
	 */
	void exitKillByUser(RedisParser.KillByUserContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#killByAddr}.
	 * @param ctx the parse tree
	 */
	void enterKillByAddr(RedisParser.KillByAddrContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#killByAddr}.
	 * @param ctx the parse tree
	 */
	void exitKillByAddr(RedisParser.KillByAddrContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#killByLAddr}.
	 * @param ctx the parse tree
	 */
	void enterKillByLAddr(RedisParser.KillByLAddrContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#killByLAddr}.
	 * @param ctx the parse tree
	 */
	void exitKillByLAddr(RedisParser.KillByLAddrContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#killBySkipme}.
	 * @param ctx the parse tree
	 */
	void enterKillBySkipme(RedisParser.KillBySkipmeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#killBySkipme}.
	 * @param ctx the parse tree
	 */
	void exitKillBySkipme(RedisParser.KillBySkipmeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#killMaxage}.
	 * @param ctx the parse tree
	 */
	void enterKillMaxage(RedisParser.KillMaxageContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#killMaxage}.
	 * @param ctx the parse tree
	 */
	void exitKillMaxage(RedisParser.KillMaxageContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClientList}.
	 * @param ctx the parse tree
	 */
	void enterCmdClientList(RedisParser.CmdClientListContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClientList}.
	 * @param ctx the parse tree
	 */
	void exitCmdClientList(RedisParser.CmdClientListContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClientNoEvict}.
	 * @param ctx the parse tree
	 */
	void enterCmdClientNoEvict(RedisParser.CmdClientNoEvictContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClientNoEvict}.
	 * @param ctx the parse tree
	 */
	void exitCmdClientNoEvict(RedisParser.CmdClientNoEvictContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClientNoTouch}.
	 * @param ctx the parse tree
	 */
	void enterCmdClientNoTouch(RedisParser.CmdClientNoTouchContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClientNoTouch}.
	 * @param ctx the parse tree
	 */
	void exitCmdClientNoTouch(RedisParser.CmdClientNoTouchContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClientPause}.
	 * @param ctx the parse tree
	 */
	void enterCmdClientPause(RedisParser.CmdClientPauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClientPause}.
	 * @param ctx the parse tree
	 */
	void exitCmdClientPause(RedisParser.CmdClientPauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClientReply}.
	 * @param ctx the parse tree
	 */
	void enterCmdClientReply(RedisParser.CmdClientReplyContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClientReply}.
	 * @param ctx the parse tree
	 */
	void exitCmdClientReply(RedisParser.CmdClientReplyContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClientSetInfo}.
	 * @param ctx the parse tree
	 */
	void enterCmdClientSetInfo(RedisParser.CmdClientSetInfoContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClientSetInfo}.
	 * @param ctx the parse tree
	 */
	void exitCmdClientSetInfo(RedisParser.CmdClientSetInfoContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClientSetname}.
	 * @param ctx the parse tree
	 */
	void enterCmdClientSetname(RedisParser.CmdClientSetnameContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClientSetname}.
	 * @param ctx the parse tree
	 */
	void exitCmdClientSetname(RedisParser.CmdClientSetnameContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClientTracking}.
	 * @param ctx the parse tree
	 */
	void enterCmdClientTracking(RedisParser.CmdClientTrackingContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClientTracking}.
	 * @param ctx the parse tree
	 */
	void exitCmdClientTracking(RedisParser.CmdClientTrackingContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#prefixOpt}.
	 * @param ctx the parse tree
	 */
	void enterPrefixOpt(RedisParser.PrefixOptContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#prefixOpt}.
	 * @param ctx the parse tree
	 */
	void exitPrefixOpt(RedisParser.PrefixOptContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClientTrackingInfo}.
	 * @param ctx the parse tree
	 */
	void enterCmdClientTrackingInfo(RedisParser.CmdClientTrackingInfoContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClientTrackingInfo}.
	 * @param ctx the parse tree
	 */
	void exitCmdClientTrackingInfo(RedisParser.CmdClientTrackingInfoContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClientUnBlock}.
	 * @param ctx the parse tree
	 */
	void enterCmdClientUnBlock(RedisParser.CmdClientUnBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClientUnBlock}.
	 * @param ctx the parse tree
	 */
	void exitCmdClientUnBlock(RedisParser.CmdClientUnBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdClientUnPause}.
	 * @param ctx the parse tree
	 */
	void enterCmdClientUnPause(RedisParser.CmdClientUnPauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdClientUnPause}.
	 * @param ctx the parse tree
	 */
	void exitCmdClientUnPause(RedisParser.CmdClientUnPauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdAuth}.
	 * @param ctx the parse tree
	 */
	void enterCmdAuth(RedisParser.CmdAuthContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdAuth}.
	 * @param ctx the parse tree
	 */
	void exitCmdAuth(RedisParser.CmdAuthContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdEcho}.
	 * @param ctx the parse tree
	 */
	void enterCmdEcho(RedisParser.CmdEchoContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdEcho}.
	 * @param ctx the parse tree
	 */
	void exitCmdEcho(RedisParser.CmdEchoContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdHello}.
	 * @param ctx the parse tree
	 */
	void enterCmdHello(RedisParser.CmdHelloContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdHello}.
	 * @param ctx the parse tree
	 */
	void exitCmdHello(RedisParser.CmdHelloContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdPing}.
	 * @param ctx the parse tree
	 */
	void enterCmdPing(RedisParser.CmdPingContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdPing}.
	 * @param ctx the parse tree
	 */
	void exitCmdPing(RedisParser.CmdPingContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdQuit}.
	 * @param ctx the parse tree
	 */
	void enterCmdQuit(RedisParser.CmdQuitContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdQuit}.
	 * @param ctx the parse tree
	 */
	void exitCmdQuit(RedisParser.CmdQuitContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdReset}.
	 * @param ctx the parse tree
	 */
	void enterCmdReset(RedisParser.CmdResetContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdReset}.
	 * @param ctx the parse tree
	 */
	void exitCmdReset(RedisParser.CmdResetContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdSelect}.
	 * @param ctx the parse tree
	 */
	void enterCmdSelect(RedisParser.CmdSelectContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdSelect}.
	 * @param ctx the parse tree
	 */
	void exitCmdSelect(RedisParser.CmdSelectContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#cmdInst}.
	 * @param ctx the parse tree
	 */
	void enterCmdInst(RedisParser.CmdInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#cmdInst}.
	 * @param ctx the parse tree
	 */
	void exitCmdInst(RedisParser.CmdInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#rootInstSet}.
	 * @param ctx the parse tree
	 */
	void enterRootInstSet(RedisParser.RootInstSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#rootInstSet}.
	 * @param ctx the parse tree
	 */
	void exitRootInstSet(RedisParser.RootInstSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#commands}.
	 * @param ctx the parse tree
	 */
	void enterCommands(RedisParser.CommandsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#commands}.
	 * @param ctx the parse tree
	 */
	void exitCommands(RedisParser.CommandsContext ctx);
}