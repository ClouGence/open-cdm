// Generated from RedisParser.g4 by ANTLR 4.9.3
package com.clougence.clouddm.ds.redis.parser.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link RedisParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface RedisParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link RedisParser#intValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntValue(RedisParser.IntValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#bitValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitValue(RedisParser.BitValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#floatValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatValue(RedisParser.FloatValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#keyName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyName(RedisParser.KeyNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPattern(RedisParser.PatternContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#stringValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringValue(RedisParser.StringValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#multiString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiString(RedisParser.MultiStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#offsetNum}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOffsetNum(RedisParser.OffsetNumContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#aclCmdRule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAclCmdRule(RedisParser.AclCmdRuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#idStr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdStr(RedisParser.IdStrContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#keyWordValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyWordValue(RedisParser.KeyWordValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#valueAsStr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueAsStr(RedisParser.ValueAsStrContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArg(RedisParser.ArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#rangeInt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRangeInt(RedisParser.RangeIntContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#min}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMin(RedisParser.MinContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#max}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMax(RedisParser.MaxContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#port}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPort(RedisParser.PortContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#weight}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWeight(RedisParser.WeightContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#sha1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSha1(RedisParser.Sha1Context ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#key}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKey(RedisParser.KeyContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cursor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursor(RedisParser.CursorContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#count}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCount(RedisParser.CountContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#fieldName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldName(RedisParser.FieldNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElement(RedisParser.ElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#member}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMember(RedisParser.MemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#srcKey}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSrcKey(RedisParser.SrcKeyContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#channel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChannel(RedisParser.ChannelContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#slot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSlot(RedisParser.SlotContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#keyAndString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyAndString(RedisParser.KeyAndStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#patternOpt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPatternOpt(RedisParser.PatternOptContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#keyOpt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyOpt(RedisParser.KeyOptContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#limitOpt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLimitOpt(RedisParser.LimitOptContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#sortOpt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSortOpt(RedisParser.SortOptContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#ttlOpt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTtlOpt(RedisParser.TtlOptContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#directionOpt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirectionOpt(RedisParser.DirectionOptContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdCopy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdCopy(RedisParser.CmdCopyContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdDel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdDel(RedisParser.CmdDelContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdDump}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdDump(RedisParser.CmdDumpContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdExists}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdExists(RedisParser.CmdExistsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdExpire}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdExpire(RedisParser.CmdExpireContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdExpireat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdExpireat(RedisParser.CmdExpireatContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdExpireTime}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdExpireTime(RedisParser.CmdExpireTimeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdKeys}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdKeys(RedisParser.CmdKeysContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdMove}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdMove(RedisParser.CmdMoveContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdObject}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdObject(RedisParser.CmdObjectContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdPersist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdPersist(RedisParser.CmdPersistContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdPexpire}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdPexpire(RedisParser.CmdPexpireContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdPexpireat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdPexpireat(RedisParser.CmdPexpireatContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdPExpireTime}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdPExpireTime(RedisParser.CmdPExpireTimeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdTtl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdTtl(RedisParser.CmdTtlContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdPttl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdPttl(RedisParser.CmdPttlContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdRandomkey}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdRandomkey(RedisParser.CmdRandomkeyContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdRename}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdRename(RedisParser.CmdRenameContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdRenamenx}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdRenamenx(RedisParser.CmdRenamenxContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdRestore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdRestore(RedisParser.CmdRestoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdScan}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdScan(RedisParser.CmdScanContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSort}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSort(RedisParser.CmdSortContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSortro}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSortro(RedisParser.CmdSortroContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdTouch}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdTouch(RedisParser.CmdTouchContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdType(RedisParser.CmdTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdUnlink}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdUnlink(RedisParser.CmdUnlinkContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdWait}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdWait(RedisParser.CmdWaitContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdWaitAOF}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdWaitAOF(RedisParser.CmdWaitAOFContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdAppend}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdAppend(RedisParser.CmdAppendContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdDecr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdDecr(RedisParser.CmdDecrContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdDecrby}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdDecrby(RedisParser.CmdDecrbyContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdGet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdGet(RedisParser.CmdGetContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdGetdel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdGetdel(RedisParser.CmdGetdelContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdGetex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdGetex(RedisParser.CmdGetexContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdGetrange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdGetrange(RedisParser.CmdGetrangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdGetset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdGetset(RedisParser.CmdGetsetContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdIncr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdIncr(RedisParser.CmdIncrContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdIncrby}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdIncrby(RedisParser.CmdIncrbyContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdIncrbyFloat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdIncrbyFloat(RedisParser.CmdIncrbyFloatContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdLcs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdLcs(RedisParser.CmdLcsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdMget}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdMget(RedisParser.CmdMgetContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdMset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdMset(RedisParser.CmdMsetContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdMsetnx}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdMsetnx(RedisParser.CmdMsetnxContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSetex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSetex(RedisParser.CmdSetexContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdPSetex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdPSetex(RedisParser.CmdPSetexContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSet(RedisParser.CmdSetContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSetnx}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSetnx(RedisParser.CmdSetnxContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSetrange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSetrange(RedisParser.CmdSetrangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdStrlen}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdStrlen(RedisParser.CmdStrlenContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSubstr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSubstr(RedisParser.CmdSubstrContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdBitCount}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdBitCount(RedisParser.CmdBitCountContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdBitField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdBitField(RedisParser.CmdBitFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#bitFieldItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitFieldItem(RedisParser.BitFieldItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#encodOffOpt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEncodOffOpt(RedisParser.EncodOffOptContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdBitFieldRO}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdBitFieldRO(RedisParser.CmdBitFieldROContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdBitOP}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdBitOP(RedisParser.CmdBitOPContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdBitPos}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdBitPos(RedisParser.CmdBitPosContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdGetbit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdGetbit(RedisParser.CmdGetbitContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSetbit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSetbit(RedisParser.CmdSetbitContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHdel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHdel(RedisParser.CmdHdelContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHexists}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHexists(RedisParser.CmdHexistsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHexpire}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHexpire(RedisParser.CmdHexpireContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHexpireat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHexpireat(RedisParser.CmdHexpireatContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHexpiretime}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHexpiretime(RedisParser.CmdHexpiretimeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHGet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHGet(RedisParser.CmdHGetContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHGetAll}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHGetAll(RedisParser.CmdHGetAllContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHgetDel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHgetDel(RedisParser.CmdHgetDelContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHgetex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHgetex(RedisParser.CmdHgetexContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHincrBy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHincrBy(RedisParser.CmdHincrByContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHincrByFloat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHincrByFloat(RedisParser.CmdHincrByFloatContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHKeys}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHKeys(RedisParser.CmdHKeysContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHLen}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHLen(RedisParser.CmdHLenContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHMget}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHMget(RedisParser.CmdHMgetContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHMset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHMset(RedisParser.CmdHMsetContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHPersist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHPersist(RedisParser.CmdHPersistContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHPexpire}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHPexpire(RedisParser.CmdHPexpireContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHPexpireAt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHPexpireAt(RedisParser.CmdHPexpireAtContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHPexpireTime}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHPexpireTime(RedisParser.CmdHPexpireTimeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHPTTL}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHPTTL(RedisParser.CmdHPTTLContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHTTL}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHTTL(RedisParser.CmdHTTLContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHrandfield}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHrandfield(RedisParser.CmdHrandfieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHscan}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHscan(RedisParser.CmdHscanContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHSet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHSet(RedisParser.CmdHSetContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHSetex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHSetex(RedisParser.CmdHSetexContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHsetnx}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHsetnx(RedisParser.CmdHsetnxContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHStrLen}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHStrLen(RedisParser.CmdHStrLenContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHVals}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHVals(RedisParser.CmdHValsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdBlmove}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdBlmove(RedisParser.CmdBlmoveContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdBLmpop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdBLmpop(RedisParser.CmdBLmpopContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdBLPop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdBLPop(RedisParser.CmdBLPopContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdBRPop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdBRPop(RedisParser.CmdBRPopContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdBrpoplpush}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdBrpoplpush(RedisParser.CmdBrpoplpushContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdLindex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdLindex(RedisParser.CmdLindexContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdLinsert}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdLinsert(RedisParser.CmdLinsertContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdLlen}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdLlen(RedisParser.CmdLlenContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdLmove}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdLmove(RedisParser.CmdLmoveContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdLmpop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdLmpop(RedisParser.CmdLmpopContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdLPop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdLPop(RedisParser.CmdLPopContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdLpos}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdLpos(RedisParser.CmdLposContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdLPush}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdLPush(RedisParser.CmdLPushContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdLPushx}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdLPushx(RedisParser.CmdLPushxContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdLRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdLRange(RedisParser.CmdLRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdLRem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdLRem(RedisParser.CmdLRemContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdLSet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdLSet(RedisParser.CmdLSetContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdLTrim}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdLTrim(RedisParser.CmdLTrimContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdRPop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdRPop(RedisParser.CmdRPopContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdRpoplpush}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdRpoplpush(RedisParser.CmdRpoplpushContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdRPush}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdRPush(RedisParser.CmdRPushContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdRPushx}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdRPushx(RedisParser.CmdRPushxContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSadd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSadd(RedisParser.CmdSaddContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdScard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdScard(RedisParser.CmdScardContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSdiff}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSdiff(RedisParser.CmdSdiffContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSdiffstore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSdiffstore(RedisParser.CmdSdiffstoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSinter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSinter(RedisParser.CmdSinterContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSinterCard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSinterCard(RedisParser.CmdSinterCardContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSinterStore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSinterStore(RedisParser.CmdSinterStoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSismember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSismember(RedisParser.CmdSismemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSmembers}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSmembers(RedisParser.CmdSmembersContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSmismember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSmismember(RedisParser.CmdSmismemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSmove}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSmove(RedisParser.CmdSmoveContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSpop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSpop(RedisParser.CmdSpopContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSrandmember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSrandmember(RedisParser.CmdSrandmemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSrem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSrem(RedisParser.CmdSremContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSscan}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSscan(RedisParser.CmdSscanContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSunion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSunion(RedisParser.CmdSunionContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSunionstore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSunionstore(RedisParser.CmdSunionstoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdBzmpop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdBzmpop(RedisParser.CmdBzmpopContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdBzpopmax}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdBzpopmax(RedisParser.CmdBzpopmaxContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdBzpopmin}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdBzpopmin(RedisParser.CmdBzpopminContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZadd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZadd(RedisParser.CmdZaddContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#scoreAndMemberOpt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScoreAndMemberOpt(RedisParser.ScoreAndMemberOptContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZcard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZcard(RedisParser.CmdZcardContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZcount}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZcount(RedisParser.CmdZcountContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZdiff}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZdiff(RedisParser.CmdZdiffContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZdiffStore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZdiffStore(RedisParser.CmdZdiffStoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZincrby}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZincrby(RedisParser.CmdZincrbyContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZinter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZinter(RedisParser.CmdZinterContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZintercard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZintercard(RedisParser.CmdZintercardContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZinterstore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZinterstore(RedisParser.CmdZinterstoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZLexCount}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZLexCount(RedisParser.CmdZLexCountContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZmpop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZmpop(RedisParser.CmdZmpopContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZmscore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZmscore(RedisParser.CmdZmscoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZpopmax}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZpopmax(RedisParser.CmdZpopmaxContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZpopmin}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZpopmin(RedisParser.CmdZpopminContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZrandmember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZrandmember(RedisParser.CmdZrandmemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZrange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZrange(RedisParser.CmdZrangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZrangebylex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZrangebylex(RedisParser.CmdZrangebylexContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZrangebyscore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZrangebyscore(RedisParser.CmdZrangebyscoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZrangestore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZrangestore(RedisParser.CmdZrangestoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZrank}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZrank(RedisParser.CmdZrankContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZRem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZRem(RedisParser.CmdZRemContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZRemRangeByLex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZRemRangeByLex(RedisParser.CmdZRemRangeByLexContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZremrangebyrank}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZremrangebyrank(RedisParser.CmdZremrangebyrankContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZRemRangeByScore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZRemRangeByScore(RedisParser.CmdZRemRangeByScoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZrevrange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZrevrange(RedisParser.CmdZrevrangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZrevrangebylex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZrevrangebylex(RedisParser.CmdZrevrangebylexContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZrevrangebyscore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZrevrangebyscore(RedisParser.CmdZrevrangebyscoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZrevrank}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZrevrank(RedisParser.CmdZrevrankContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZscan}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZscan(RedisParser.CmdZscanContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZscore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZscore(RedisParser.CmdZscoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZunion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZunion(RedisParser.CmdZunionContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdZunionstore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdZunionstore(RedisParser.CmdZunionstoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdScriptDebug}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdScriptDebug(RedisParser.CmdScriptDebugContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdScriptExists}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdScriptExists(RedisParser.CmdScriptExistsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdScriptFlush}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdScriptFlush(RedisParser.CmdScriptFlushContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdScriptKill}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdScriptKill(RedisParser.CmdScriptKillContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdScriptLoad}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdScriptLoad(RedisParser.CmdScriptLoadContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdEval}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdEval(RedisParser.CmdEvalContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdEvalRO}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdEvalRO(RedisParser.CmdEvalROContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdEvalsha}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdEvalsha(RedisParser.CmdEvalshaContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdEvalshaRO}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdEvalshaRO(RedisParser.CmdEvalshaROContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdFCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdFCall(RedisParser.CmdFCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdFCallRO}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdFCallRO(RedisParser.CmdFCallROContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdFunctionDel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdFunctionDel(RedisParser.CmdFunctionDelContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdFunctionDump}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdFunctionDump(RedisParser.CmdFunctionDumpContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdFunctionFlush}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdFunctionFlush(RedisParser.CmdFunctionFlushContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdFunctionKill}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdFunctionKill(RedisParser.CmdFunctionKillContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdFunctionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdFunctionList(RedisParser.CmdFunctionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdFunctionLoad}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdFunctionLoad(RedisParser.CmdFunctionLoadContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdFunctionRestore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdFunctionRestore(RedisParser.CmdFunctionRestoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdFunctionStats}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdFunctionStats(RedisParser.CmdFunctionStatsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdDiscard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdDiscard(RedisParser.CmdDiscardContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdExec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdExec(RedisParser.CmdExecContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdMulti}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdMulti(RedisParser.CmdMultiContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdUnwatch}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdUnwatch(RedisParser.CmdUnwatchContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdWatch}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdWatch(RedisParser.CmdWatchContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdPFAdd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdPFAdd(RedisParser.CmdPFAddContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdPFCount}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdPFCount(RedisParser.CmdPFCountContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdPFMerge}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdPFMerge(RedisParser.CmdPFMergeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdPSubscribe}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdPSubscribe(RedisParser.CmdPSubscribeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdPublish}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdPublish(RedisParser.CmdPublishContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdPubSubChannels}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdPubSubChannels(RedisParser.CmdPubSubChannelsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdPubSubNumPat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdPubSubNumPat(RedisParser.CmdPubSubNumPatContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdPubSubNumSub}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdPubSubNumSub(RedisParser.CmdPubSubNumSubContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdPubSubShardChannels}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdPubSubShardChannels(RedisParser.CmdPubSubShardChannelsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdPubSubShardNumSub}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdPubSubShardNumSub(RedisParser.CmdPubSubShardNumSubContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdPunSubscribe}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdPunSubscribe(RedisParser.CmdPunSubscribeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSpublish}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSpublish(RedisParser.CmdSpublishContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSSubscribe}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSSubscribe(RedisParser.CmdSSubscribeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSubscribe}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSubscribe(RedisParser.CmdSubscribeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSunSubscribe}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSunSubscribe(RedisParser.CmdSunSubscribeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdUnSubScribe}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdUnSubScribe(RedisParser.CmdUnSubScribeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdAsking}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdAsking(RedisParser.CmdAskingContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdReadonly}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdReadonly(RedisParser.CmdReadonlyContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdReadWrite}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdReadWrite(RedisParser.CmdReadWriteContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterAddSlots}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterAddSlots(RedisParser.CmdClusterAddSlotsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterDelSlots}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterDelSlots(RedisParser.CmdClusterDelSlotsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterAddSlotsRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterAddSlotsRange(RedisParser.CmdClusterAddSlotsRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterDelSlotsRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterDelSlotsRange(RedisParser.CmdClusterDelSlotsRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#clusterStartAndEnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClusterStartAndEnd(RedisParser.ClusterStartAndEndContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterBumpEpoch}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterBumpEpoch(RedisParser.CmdClusterBumpEpochContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterCountFailureReports}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterCountFailureReports(RedisParser.CmdClusterCountFailureReportsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterCountKeysInSlot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterCountKeysInSlot(RedisParser.CmdClusterCountKeysInSlotContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterFailOver}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterFailOver(RedisParser.CmdClusterFailOverContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterFlushSlots}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterFlushSlots(RedisParser.CmdClusterFlushSlotsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterForget}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterForget(RedisParser.CmdClusterForgetContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterGetKeysInSlot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterGetKeysInSlot(RedisParser.CmdClusterGetKeysInSlotContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterInfo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterInfo(RedisParser.CmdClusterInfoContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterKeySlot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterKeySlot(RedisParser.CmdClusterKeySlotContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterLinks}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterLinks(RedisParser.CmdClusterLinksContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterMeet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterMeet(RedisParser.CmdClusterMeetContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterMyId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterMyId(RedisParser.CmdClusterMyIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterMyShardId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterMyShardId(RedisParser.CmdClusterMyShardIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterNodes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterNodes(RedisParser.CmdClusterNodesContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterReplicas}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterReplicas(RedisParser.CmdClusterReplicasContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterReplicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterReplicate(RedisParser.CmdClusterReplicateContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterReset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterReset(RedisParser.CmdClusterResetContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterSaveConfig}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterSaveConfig(RedisParser.CmdClusterSaveConfigContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterSetConfigEpoch}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterSetConfigEpoch(RedisParser.CmdClusterSetConfigEpochContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterSetSlot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterSetSlot(RedisParser.CmdClusterSetSlotContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterShards}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterShards(RedisParser.CmdClusterShardsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterSlaves}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterSlaves(RedisParser.CmdClusterSlavesContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterSlotStats}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterSlotStats(RedisParser.CmdClusterSlotStatsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClusterSlots}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClusterSlots(RedisParser.CmdClusterSlotsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdInfo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdInfo(RedisParser.CmdInfoContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#infoOpt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInfoOpt(RedisParser.InfoOptContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#username}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsername(RedisParser.UsernameContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#command}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommand(RedisParser.CommandContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdAclCat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdAclCat(RedisParser.CmdAclCatContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdAclDelUser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdAclDelUser(RedisParser.CmdAclDelUserContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdAclDryRun}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdAclDryRun(RedisParser.CmdAclDryRunContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdAclGenPass}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdAclGenPass(RedisParser.CmdAclGenPassContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdAclGetUser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdAclGetUser(RedisParser.CmdAclGetUserContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdAclList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdAclList(RedisParser.CmdAclListContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdAclLoad}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdAclLoad(RedisParser.CmdAclLoadContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdAclLog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdAclLog(RedisParser.CmdAclLogContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdAclSave}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdAclSave(RedisParser.CmdAclSaveContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdAclSetUser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdAclSetUser(RedisParser.CmdAclSetUserContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdAclUsers}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdAclUsers(RedisParser.CmdAclUsersContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdAclWhoami}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdAclWhoami(RedisParser.CmdAclWhoamiContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdCommand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdCommand(RedisParser.CmdCommandContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdCommandCount}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdCommandCount(RedisParser.CmdCommandCountContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdCommandDocs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdCommandDocs(RedisParser.CmdCommandDocsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdCommandGetKeys}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdCommandGetKeys(RedisParser.CmdCommandGetKeysContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdCommandGetKeysAndFlags}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdCommandGetKeysAndFlags(RedisParser.CmdCommandGetKeysAndFlagsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdCommandInfo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdCommandInfo(RedisParser.CmdCommandInfoContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdCommandList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdCommandList(RedisParser.CmdCommandListContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdConfigGet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdConfigGet(RedisParser.CmdConfigGetContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdConfigSet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdConfigSet(RedisParser.CmdConfigSetContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdConfigResetStat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdConfigResetStat(RedisParser.CmdConfigResetStatContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdConfigRewrite}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdConfigRewrite(RedisParser.CmdConfigRewriteContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#event}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvent(RedisParser.EventContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdLatencyDoctor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdLatencyDoctor(RedisParser.CmdLatencyDoctorContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdLatencyGraph}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdLatencyGraph(RedisParser.CmdLatencyGraphContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdLatencyHistogram}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdLatencyHistogram(RedisParser.CmdLatencyHistogramContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdLatencyHistory}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdLatencyHistory(RedisParser.CmdLatencyHistoryContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdLatencyLatest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdLatencyLatest(RedisParser.CmdLatencyLatestContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdLatencyReset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdLatencyReset(RedisParser.CmdLatencyResetContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdMemoryDoctor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdMemoryDoctor(RedisParser.CmdMemoryDoctorContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdMemoryMallocStats}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdMemoryMallocStats(RedisParser.CmdMemoryMallocStatsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdMemoryPurge}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdMemoryPurge(RedisParser.CmdMemoryPurgeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdMemoryStats}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdMemoryStats(RedisParser.CmdMemoryStatsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdMemoryUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdMemoryUsage(RedisParser.CmdMemoryUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#configKV}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConfigKV(RedisParser.ConfigKVContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdModuleList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdModuleList(RedisParser.CmdModuleListContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdModuleLoad}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdModuleLoad(RedisParser.CmdModuleLoadContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdModuleLoadEx}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdModuleLoadEx(RedisParser.CmdModuleLoadExContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdModuleUnload}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdModuleUnload(RedisParser.CmdModuleUnloadContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdBgrewriteaof}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdBgrewriteaof(RedisParser.CmdBgrewriteaofContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdBgsave}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdBgsave(RedisParser.CmdBgsaveContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdDbsize}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdDbsize(RedisParser.CmdDbsizeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdFailover}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdFailover(RedisParser.CmdFailoverContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdFlushAll}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdFlushAll(RedisParser.CmdFlushAllContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdFlushDB}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdFlushDB(RedisParser.CmdFlushDBContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdLastsave}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdLastsave(RedisParser.CmdLastsaveContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdLolwut}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdLolwut(RedisParser.CmdLolwutContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdMonitor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdMonitor(RedisParser.CmdMonitorContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdPSync}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdPSync(RedisParser.CmdPSyncContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdReplicaOf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdReplicaOf(RedisParser.CmdReplicaOfContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdRole}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdRole(RedisParser.CmdRoleContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSave}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSave(RedisParser.CmdSaveContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdShutdown}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdShutdown(RedisParser.CmdShutdownContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSlaveOf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSlaveOf(RedisParser.CmdSlaveOfContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSlowlogGet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSlowlogGet(RedisParser.CmdSlowlogGetContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSlowlogLen}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSlowlogLen(RedisParser.CmdSlowlogLenContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSlowlogReset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSlowlogReset(RedisParser.CmdSlowlogResetContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSwapDB}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSwapDB(RedisParser.CmdSwapDBContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSync}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSync(RedisParser.CmdSyncContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdTime}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdTime(RedisParser.CmdTimeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#clientId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClientId(RedisParser.ClientIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClientCaching}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClientCaching(RedisParser.CmdClientCachingContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClientGetname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClientGetname(RedisParser.CmdClientGetnameContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClientGetredir}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClientGetredir(RedisParser.CmdClientGetredirContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClientID}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClientID(RedisParser.CmdClientIDContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClientInfo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClientInfo(RedisParser.CmdClientInfoContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClientKill}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClientKill(RedisParser.CmdClientKillContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#killBy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKillBy(RedisParser.KillByContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#killById}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKillById(RedisParser.KillByIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#killByType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKillByType(RedisParser.KillByTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#killByUser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKillByUser(RedisParser.KillByUserContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#killByAddr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKillByAddr(RedisParser.KillByAddrContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#killByLAddr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKillByLAddr(RedisParser.KillByLAddrContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#killBySkipme}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKillBySkipme(RedisParser.KillBySkipmeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#killMaxage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKillMaxage(RedisParser.KillMaxageContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClientList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClientList(RedisParser.CmdClientListContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClientNoEvict}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClientNoEvict(RedisParser.CmdClientNoEvictContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClientNoTouch}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClientNoTouch(RedisParser.CmdClientNoTouchContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClientPause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClientPause(RedisParser.CmdClientPauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClientReply}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClientReply(RedisParser.CmdClientReplyContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClientSetInfo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClientSetInfo(RedisParser.CmdClientSetInfoContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClientSetname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClientSetname(RedisParser.CmdClientSetnameContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClientTracking}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClientTracking(RedisParser.CmdClientTrackingContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#prefixOpt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefixOpt(RedisParser.PrefixOptContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClientTrackingInfo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClientTrackingInfo(RedisParser.CmdClientTrackingInfoContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClientUnBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClientUnBlock(RedisParser.CmdClientUnBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdClientUnPause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdClientUnPause(RedisParser.CmdClientUnPauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdAuth}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdAuth(RedisParser.CmdAuthContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdEcho}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdEcho(RedisParser.CmdEchoContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdHello}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdHello(RedisParser.CmdHelloContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdPing}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdPing(RedisParser.CmdPingContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdQuit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdQuit(RedisParser.CmdQuitContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdReset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdReset(RedisParser.CmdResetContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdSelect}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSelect(RedisParser.CmdSelectContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#cmdInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdInst(RedisParser.CmdInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#rootInstSet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRootInstSet(RedisParser.RootInstSetContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#commands}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommands(RedisParser.CommandsContext ctx);
}