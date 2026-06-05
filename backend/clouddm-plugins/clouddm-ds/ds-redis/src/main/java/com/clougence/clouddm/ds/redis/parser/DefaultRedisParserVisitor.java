/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.ds.redis.parser;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicLong;

import org.antlr.v4.runtime.tree.TerminalNode;

import com.clougence.clouddm.ds.redis.parser.antlr.RedisParser;
import com.clougence.clouddm.ds.redis.parser.antlr.RedisParserVisitor;
import com.clougence.clouddm.ds.redis.parser.antlr.RedisParser.*;
import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdSet;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.commands.acl.*;
import com.clougence.clouddm.ds.redis.parser.ast.commands.bit.*;
import com.clougence.clouddm.ds.redis.parser.ast.commands.bit.bitfield.GetItem;
import com.clougence.clouddm.ds.redis.parser.ast.commands.bit.bitfield.IncrItem;
import com.clougence.clouddm.ds.redis.parser.ast.commands.bit.bitfield.OverflowItem;
import com.clougence.clouddm.ds.redis.parser.ast.commands.bit.bitfield.SetItem;
import com.clougence.clouddm.ds.redis.parser.ast.commands.client.*;
import com.clougence.clouddm.ds.redis.parser.ast.commands.cluster.*;
import com.clougence.clouddm.ds.redis.parser.ast.commands.command.*;
import com.clougence.clouddm.ds.redis.parser.ast.commands.config.ConfigGetCmd;
import com.clougence.clouddm.ds.redis.parser.ast.commands.config.ConfigResetStatCmd;
import com.clougence.clouddm.ds.redis.parser.ast.commands.config.ConfigRewriteCmd;
import com.clougence.clouddm.ds.redis.parser.ast.commands.config.ConfigSetCmd;
import com.clougence.clouddm.ds.redis.parser.ast.commands.control.*;
import com.clougence.clouddm.ds.redis.parser.ast.commands.hash.*;
import com.clougence.clouddm.ds.redis.parser.ast.commands.info.InfoRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.commands.keys.*;
import com.clougence.clouddm.ds.redis.parser.ast.commands.list.*;
import com.clougence.clouddm.ds.redis.parser.ast.commands.pf.PFAddRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.commands.pf.PFCountRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.commands.pf.PFMergeRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.commands.pubsub.*;
import com.clougence.clouddm.ds.redis.parser.ast.commands.set.*;
import com.clougence.clouddm.ds.redis.parser.ast.commands.sortedset.*;
import com.clougence.clouddm.ds.redis.parser.ast.commands.string.*;
import com.clougence.clouddm.ds.redis.parser.ast.commands.tx.*;
import com.clougence.clouddm.ds.redis.parser.ast.token.*;
import com.clougence.dslpaser.antlr.AbstractLocationParseTreeVisitor;
import com.clougence.utils.CollectionUtils;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link RedisParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public class DefaultRedisParserVisitor<T> extends AbstractLocationParseTreeVisitor<T> implements RedisParserVisitor<T> {

    private final Stack<Object> instStack = new Stack<>();
    private final AtomicLong    counter   = new AtomicLong(0);

    @Override
    public T visitRootInstSet(RootInstSetContext ctx) {
        this.instStack.push(code(new RedisCmdSet(), ctx));
        this.visitChildren(ctx);
        return (T) this.instStack.pop();
    }

    @Override
    public T visitCommands(CommandsContext ctx) {
        ctx.cmdInst().accept(this);
        AbstractRedisCmd instSet = (AbstractRedisCmd) this.instStack.pop();
        RedisCmdSet rootBlockSet = (RedisCmdSet) this.instStack.peek();
        rootBlockSet.add(instSet);

        if (ctx.commands() != null) {
            ctx.commands().accept(this);
        }
        return null;
    }

    @Override
    public T visitCmdInst(CmdInstContext ctx) {
        visitChildren(ctx);
        return null;
    }

    /* ----------------------------------------------------------------------------------- basic type */

    private String fixString(TerminalNode stringNode) {
        String nodeText = stringNode.getText();
        return nodeText.substring(1, nodeText.length() - 1);
    }

    @Override
    public T visitIntValue(IntValueContext ctx) {
        TerminalNode argTerm = ctx.ARG();
        TerminalNode bitTerm = ctx.BIT_VALUE();
        TerminalNode intTerm = ctx.INTEGER_NUM();

        if (bitTerm != null) {
            BigInteger intInt = new BigInteger(bitTerm.getText());
            this.instStack.push(code(IntToken.of(intInt), ctx));
        } else if (intTerm != null) {
            BigInteger bigInt = new BigInteger(intTerm.getText());
            this.instStack.push(code(IntToken.of(bigInt), ctx));
        } else if (argTerm != null) {
            this.instStack.push(code(IntToken.ofArg(counter.getAndIncrement()), argTerm));
        } else {
            throw new UnsupportedOperationException("impossible");
        }

        return null;
    }

    @Override
    public T visitBitValue(BitValueContext ctx) {
        TerminalNode argTerm = ctx.ARG();
        TerminalNode bitTerm = ctx.BIT_VALUE();

        if (bitTerm != null) {
            boolean bool = "1".equals(bitTerm.getText());
            this.instStack.push(code(BoolToken.of(bool), ctx));
        } else if (argTerm != null) {
            this.instStack.push(code(BoolToken.ofArg(counter.getAndIncrement()), argTerm));
        } else {
            throw new UnsupportedOperationException("impossible");
        }

        return null;
    }

    @Override
    public T visitFloatValue(FloatValueContext ctx) {
        TerminalNode argTerm = ctx.ARG();
        TerminalNode bitTerm = ctx.BIT_VALUE();
        TerminalNode intTerm = ctx.INTEGER_NUM();
        TerminalNode nInfTerm = ctx.N_INF();
        TerminalNode sInfTerm = ctx.S_INF();
        TerminalNode decTerm = ctx.DECIMAL_NUM();

        if (bitTerm != null) {
            String decString = bitTerm.getText();
            double bigDec = Double.parseDouble(decString);
            this.instStack.push(code(FloatToken.of(bigDec, decString), ctx));
        } else if (decTerm != null) {
            String decString = decTerm.getText();
            double bigDec = Double.parseDouble(decString);
            this.instStack.push(code(FloatToken.of(bigDec, decString), ctx));
        } else if (nInfTerm != null) {
            this.instStack.push(code(FloatToken.of(Double.NEGATIVE_INFINITY, "-INF"), ctx));
        } else if (sInfTerm != null) {
            this.instStack.push(code(FloatToken.of(Double.POSITIVE_INFINITY, "+INF"), ctx));
        } else if (intTerm != null) {
            String intString = intTerm.getText();
            double bigDec = Double.parseDouble(intString);
            this.instStack.push(code(FloatToken.of(bigDec, intString), ctx));
        } else if (argTerm != null) {
            this.instStack.push(code(FloatToken.ofArg(counter.getAndIncrement()), argTerm));
        } else {
            throw new UnsupportedOperationException("impossible");
        }

        return null;
    }

    @Override
    public T visitKeyName(KeyNameContext ctx) {
        TerminalNode argTerm = ctx.ARG();
        TerminalNode nameTerm = ctx.NAME_TOKEN();
        IdStrContext idTerm = ctx.idStr();
        TerminalNode stringTerm = ctx.STRING();

        if (idTerm != null) {
            String text = idTerm.getText();
            this.instStack.push(code(StrToken.of(text, false), idTerm));
        } else if (stringTerm != null) {
            String text = fixString(stringTerm);
            this.instStack.push(code(StrToken.of(text, true), stringTerm));
        } else if (nameTerm != null) {
            String text = nameTerm.getText();
            this.instStack.push(code(StrToken.of(text, true), nameTerm));
        } else if (argTerm != null) {
            this.instStack.push(code(StrToken.ofArg(counter.getAndIncrement()), argTerm));
        } else {
            throw new UnsupportedOperationException("impossible");
        }
        return null;
    }

    @Override
    public T visitPattern(PatternContext ctx) {
        TerminalNode argTerm = ctx.ARG();
        IdStrContext idTerm = ctx.idStr();
        TerminalNode stringTerm = ctx.STRING();
        TerminalNode rangeNumTerm = ctx.RANGE_NUM();
        TerminalNode patternTerm = ctx.PATTERN_STRING();

        if (idTerm != null) {
            String text = idTerm.getText();
            this.instStack.push(code(StrToken.of(text, false), idTerm));
        } else if (stringTerm != null) {
            String text = fixString(stringTerm);
            this.instStack.push(code(StrToken.of(text, true), stringTerm));
        } else if (argTerm != null) {
            this.instStack.push(code(StrToken.ofArg(counter.getAndIncrement()), argTerm));
        } else if (rangeNumTerm != null) {
            String text = rangeNumTerm.getText();
            this.instStack.push(code(StrToken.of(text, false), rangeNumTerm));
        } else if (patternTerm != null) {
            String text = patternTerm.getText();
            this.instStack.push(code(StrToken.of(text, false), patternTerm));
        } else {
            throw new UnsupportedOperationException("impossible");
        }

        return null;
    }

    @Override
    public T visitStringValue(StringValueContext ctx) {
        TerminalNode argTerm = ctx.ARG();
        TerminalNode stringTerm = ctx.STRING();

        if (stringTerm != null) {
            String text = fixString(stringTerm);
            this.instStack.push(code(StrToken.of(text, true), stringTerm));
        } else if (argTerm != null) {
            this.instStack.push(code(StrToken.ofArg(counter.getAndIncrement()), argTerm));
        } else {
            throw new UnsupportedOperationException("impossible");
        }

        return null;
    }

    @Override
    public T visitMultiString(MultiStringContext ctx) {
        TerminalNode argTerm = ctx.ARG();
        TerminalNode stringTerm = ctx.STRING();

        if (stringTerm != null) {
            String text = fixString(stringTerm);
            this.instStack.push(code(StrToken.of(text, true), stringTerm));
        } else if (argTerm != null) {
            this.instStack.push(code(StrToken.ofArg(counter.getAndIncrement()), argTerm));
        } else {
            throw new UnsupportedOperationException("impossible");
        }
        return null;
    }

    @Override
    public T visitOffsetNum(OffsetNumContext ctx) {
        TerminalNode argTerm = ctx.ARG();
        IntValueContext numValue = ctx.intValue();
        TerminalNode offset = ctx.OFFSET();

        if (offset != null) {
            String text = offset.getText();
            this.instStack.push(code(StrToken.of(text, false), offset));
        } else if (numValue != null) {
            numValue.accept(this);
            IntToken numToken = (IntToken) this.instStack.pop();
            StrToken result;
            if (numToken.isArg()) {
                result = StrToken.ofArg(numToken.getArgIndex());
            } else {
                result = StrToken.of(numValue.getText(), false);
            }
            this.instStack.push(code(result, numValue));
        } else if (argTerm != null) {
            this.instStack.push(code(StrToken.ofArg(counter.getAndIncrement()), argTerm));
        } else {
            throw new UnsupportedOperationException("impossible");
        }
        return null;
    }

    @Override
    public T visitAclCmdRule(AclCmdRuleContext ctx) {
        TerminalNode argTerm = ctx.ARG();

        if (argTerm != null) {
            this.instStack.push(code(StrToken.ofArg(counter.getAndIncrement()), argTerm));
        } else {
            String text = ctx.getText();
            this.instStack.push(code(StrToken.of(text, false), ctx));
        }
        return null;
    }

    @Override
    public T visitValueAsStr(ValueAsStrContext ctx) {
        StringValueContext str1Value = ctx.stringValue();
        MultiStringContext str2Value = ctx.multiString();
        PatternContext key1Value = ctx.pattern();
        KeyNameContext key2Value = ctx.keyName();
        IdStrContext key3Value = ctx.idStr();
        IntValueContext num1Value = ctx.intValue();
        FloatValueContext num2Value = ctx.floatValue();

        if (str1Value != null) {
            str1Value.accept(this);
        } else if (str2Value != null) {
            str2Value.accept(this);
        } else if (key1Value != null) {
            key1Value.accept(this);
        } else if (key2Value != null) {
            key2Value.accept(this);
        } else if (key3Value != null) {
            key3Value.accept(this);
        } else if (num1Value != null) {
            num1Value.accept(this);
            IntToken numToken = (IntToken) this.instStack.pop();
            StrToken result;
            if (numToken.isArg()) {
                result = StrToken.ofArg(numToken.getArgIndex());
            } else {
                result = StrToken.of(num2Value.getText(), false);
            }
            this.instStack.push(code(result, num1Value));
        } else if (num2Value != null) {
            num2Value.accept(this);
            FloatToken numToken = (FloatToken) this.instStack.pop();
            StrToken result;
            if (numToken.isArg()) {
                result = StrToken.ofArg(numToken.getArgIndex());
            } else {
                result = StrToken.of(num2Value.getText(), false);
            }
            this.instStack.push(code(result, num2Value));
        } else {
            throw new UnsupportedOperationException("impossible");
        }
        return null;
    }

    @Override
    public T visitIdStr(IdStrContext ctx) {
        String text = ctx.getText();
        this.instStack.push(code(StrToken.of(text, false), ctx));
        return null;
    }

    @Override
    public T visitKeyWordValue(KeyWordValueContext ctx) {
        throw new UnsupportedOperationException("impossible");
    }

    /* ----------------------------------------------------------------------------------- alias */

    @Override
    public T visitSha1(Sha1Context ctx) {
        ctx.valueAsStr().accept(this);
        return null;
    }

    @Override
    public T visitArg(ArgContext ctx) {
        return this.visitChildren(ctx);
    }

    @Override
    public T visitRangeInt(RangeIntContext ctx) {
        PatternContext patternCtx = ctx.pattern();
        IntValueContext intCtx = ctx.intValue();

        if (patternCtx != null) {
            patternCtx.accept(this);
            StrToken strToken = (StrToken) this.instStack.pop();
            StrIntToken result;
            if (strToken.isArg()) {
                result = StrIntToken.ofArg(strToken.getArgIndex());
            } else {
                result = StrIntToken.of(patternCtx.getText());
            }
            this.instStack.push(code(result, ctx));
        } else if (intCtx != null) {
            intCtx.accept(this);
            IntToken intToken = (IntToken) this.instStack.pop();
            StrIntToken result;
            if (intToken.isArg()) {
                result = StrIntToken.ofArg(intToken.getArgIndex());
            } else {
                result = StrIntToken.of(intCtx.getText());
            }
            this.instStack.push(code(result, ctx));
        } else {
            throw new UnsupportedOperationException("impossible");
        }
        return null;
    }

    @Override
    public T visitMin(MinContext ctx) {
        PatternContext patternCtx = ctx.pattern();
        FloatValueContext floatCtx = ctx.floatValue();

        if (patternCtx != null) {
            patternCtx.accept(this);
            StrToken strToken = (StrToken) this.instStack.pop();
            StrIntToken result;
            if (strToken.isArg()) {
                result = StrIntToken.ofArg(strToken.getArgIndex());
            } else {
                result = StrIntToken.of(patternCtx.getText());
            }
            this.instStack.push(code(result, ctx));
        } else if (floatCtx != null) {
            floatCtx.accept(this);
            FloatToken floatToken = (FloatToken) this.instStack.pop();
            StrIntToken result;
            if (floatToken.isArg()) {
                result = StrIntToken.ofArg(floatToken.getArgIndex());
            } else {
                result = StrIntToken.of(floatCtx.getText());
            }
            this.instStack.push(code(result, ctx));
        } else {
            throw new UnsupportedOperationException("impossible");
        }
        return null;
    }

    @Override
    public T visitMax(MaxContext ctx) {
        PatternContext patternCtx = ctx.pattern();
        FloatValueContext floatCtx = ctx.floatValue();

        if (patternCtx != null) {
            patternCtx.accept(this);
            StrToken strToken = (StrToken) this.instStack.pop();
            StrIntToken result;
            if (strToken.isArg()) {
                result = StrIntToken.ofArg(strToken.getArgIndex());
            } else {
                result = StrIntToken.of(patternCtx.getText());
            }
            this.instStack.push(code(result, ctx));
        } else if (floatCtx != null) {
            floatCtx.accept(this);
            FloatToken floatToken = (FloatToken) this.instStack.pop();
            StrIntToken result;
            if (floatToken.isArg()) {
                result = StrIntToken.ofArg(floatToken.getArgIndex());
            } else {
                result = StrIntToken.of(floatCtx.getText());
            }
            this.instStack.push(code(result, ctx));
        } else {
            throw new UnsupportedOperationException("impossible");
        }
        return null;
    }

    @Override
    public T visitPort(PortContext ctx) {
        ctx.intValue().accept(this);
        return null;
    }

    @Override
    public T visitWeight(WeightContext ctx) {
        ctx.intValue().accept(this);
        return null;
    }

    @Override
    public T visitKey(KeyContext ctx) {
        KeyNameContext keyNameContext = ctx.keyName();
        PatternContext patternContext = ctx.pattern();

        if (keyNameContext != null) {
            keyNameContext.accept(this);
        } else if (patternContext != null) {
            patternContext.accept(this);
        } else {
            throw new UnsupportedOperationException("never gonna happen.");
        }

        return null;
    }

    @Override
    public T visitCursor(CursorContext ctx) {
        ctx.intValue().accept(this);
        return null;
    }

    @Override
    public T visitCount(CountContext ctx) {
        ctx.intValue().accept(this);
        return null;
    }

    @Override
    public T visitFieldName(FieldNameContext ctx) {
        ctx.keyName().accept(this);
        return null;
    }

    @Override
    public T visitElement(ElementContext ctx) {
        ctx.keyName().accept(this);
        return null;
    }

    @Override
    public T visitMember(MemberContext ctx) {
        ctx.keyName().accept(this);
        return null;
    }

    @Override
    public T visitSrcKey(SrcKeyContext ctx) {
        ctx.keyName().accept(this);
        return null;
    }

    @Override
    public T visitChannel(ChannelContext ctx) {
        ctx.keyName().accept(this);
        return null;
    }

    @Override
    public T visitSlot(SlotContext ctx) {
        ctx.intValue().accept(this);
        return null;
    }

    /* ----------------------------------------------------------------------------------- part */

    @Override
    public T visitKeyAndString(KeyAndStringContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        ctx.valueAsStr().accept(this);
        StrToken value = (StrToken) instStack.pop();

        this.instStack.push(code(new KeyAndStringToken(keyName, value), ctx));
        return null;
    }

    @Override
    public T visitInfoOpt(InfoOptContext ctx) {
        throw new UnsupportedOperationException("impossible");
    }

    @Override
    public T visitUsername(UsernameContext ctx) {
        return this.visitChildren(ctx);
    }

    @Override
    public T visitCommand(CommandContext ctx) {
        return this.visitChildren(ctx);
    }

    @Override
    public T visitKeyOpt(KeyOptContext ctx) {
        KeyOptToken optToken = null;
        TerminalNode nx = ctx.NX();
        TerminalNode xx = ctx.XX();
        TerminalNode gt = ctx.GT();
        TerminalNode lt = ctx.LT();

        if (nx != null) {
            optToken = code(new KeyOptToken(KeyOptToken.KeyOptionType.NX), nx);
        } else if (xx != null) {
            optToken = code(new KeyOptToken(KeyOptToken.KeyOptionType.XX), xx);
        } else if (gt != null) {
            optToken = code(new KeyOptToken(KeyOptToken.KeyOptionType.GT), gt);
        } else if (lt != null) {
            optToken = code(new KeyOptToken(KeyOptToken.KeyOptionType.LT), lt);
        } else {
            throw new UnsupportedOperationException("never gonna happen.");
        }

        this.instStack.push(optToken);
        return null;
    }

    @Override
    public T visitLimitOpt(LimitOptContext ctx) {
        ctx.offset.accept(this);
        IntToken offsetToken = (IntToken) instStack.pop();

        ctx.count().accept(this);
        IntToken countToken = (IntToken) instStack.pop();

        this.instStack.push(code(new LimitToken(offsetToken, countToken), ctx));
        return null;
    }

    @Override
    public T visitSortOpt(SortOptContext ctx) {
        TerminalNode asc = ctx.ASC();
        TerminalNode desc = ctx.DESC();
        if (asc != null) {
            SortByToken optToken = code(new SortByToken(OrderType.ASC), asc);
            this.instStack.push(optToken);
            return null;
        } else if (desc != null) {
            SortByToken optToken = code(new SortByToken(OrderType.DESC), desc);
            this.instStack.push(optToken);
            return null;
        } else {
            throw new UnsupportedOperationException("never gonna happen.");
        }
    }

    @Override
    public T visitTtlOpt(TtlOptContext ctx) {
        ctx.expirTime.accept(this);
        IntToken valueToken = (IntToken) instStack.pop();

        TtlOptToken gsToken;
        TerminalNode ex = ctx.EX();
        TerminalNode px = ctx.PX();
        TerminalNode exat = ctx.EXAT();
        TerminalNode pxat = ctx.PXAT();
        if (ex != null) {
            gsToken = code(new TtlOptToken(valueToken, TtlOptToken.TtlType.EX), ex);
        } else if (px != null) {
            gsToken = code(new TtlOptToken(valueToken, TtlOptToken.TtlType.PX), px);
        } else if (exat != null) {
            gsToken = code(new TtlOptToken(valueToken, TtlOptToken.TtlType.EXAT), exat);
        } else if (pxat != null) {
            gsToken = code(new TtlOptToken(valueToken, TtlOptToken.TtlType.PXAT), pxat);
        } else {
            throw new UnsupportedOperationException("never gonna happen.");
        }

        this.instStack.push(gsToken);
        return null;
    }

    @Override
    public T visitDirectionOpt(DirectionOptContext ctx) {
        TerminalNode left = ctx.LEFT();
        TerminalNode right = ctx.RIGHT();

        if (left != null) {
            LrOptToken lrOptToken = code(new LrOptToken(LrOptToken.LrOptionType.Left), left);
            this.instStack.push(lrOptToken);
            return null;
        } else if (right != null) {
            LrOptToken lrOptToken = code(new LrOptToken(LrOptToken.LrOptionType.Right), right);
            this.instStack.push(lrOptToken);
            return null;
        } else {
            throw new UnsupportedOperationException("never gonna happen.");
        }
    }

    @Override
    public T visitPatternOpt(PatternOptContext ctx) {
        ctx.pattern().accept(this);
        return null;
    }

    /* ----------------------------------------------------------------------------------- keys commands */

    @Override
    public T visitCmdCopy(CmdCopyContext ctx) {
        ctx.src.accept(this);
        StrToken srcKey = (StrToken) instStack.pop();

        ctx.dst.accept(this);
        StrToken dstKey = (StrToken) instStack.pop();
        CopyRedisCmd cmd = code(new CopyRedisCmd(srcKey, dstKey), ctx);

        if (ctx.db != null) {
            ctx.db.accept(this);
            cmd.setDstDB((IntToken) instStack.pop());
        }

        TerminalNode replaceTag = ctx.REPLACE();
        if (replaceTag != null) {
            cmd.setReplace(code(new TagToken(), replaceTag));
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdDel(CmdDelContext ctx) {
        DelRedisCmd cmd = code(new DelRedisCmd(), ctx);

        List<KeyNameContext> keyList = ctx.keyName();
        if (keyList != null) {
            for (KeyNameContext key : keyList) {
                key.accept(this);
                cmd.addKey((StrToken) this.instStack.pop());
            }
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdDump(CmdDumpContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        DumpRedisCmd cmd = code(new DumpRedisCmd(keyName), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdExists(CmdExistsContext ctx) {
        ExistsRedisCmd cmd = code(new ExistsRedisCmd(), ctx);

        List<KeyNameContext> keyList = ctx.keyName();
        if (keyList != null) {
            for (KeyNameContext key : keyList) {
                key.accept(this);
                cmd.addKey((StrToken) this.instStack.pop());
            }
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdExpire(CmdExpireContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        ctx.seconds.accept(this);
        IntToken seconds = (IntToken) instStack.pop();

        ExpireRedisCmd cmd = code(new ExpireRedisCmd(keyName, seconds), ctx);
        KeyOptContext keyOpt = ctx.keyOpt();
        if (keyOpt != null) {
            keyOpt.accept(this);
            cmd.setKeyOpt((KeyOptToken) instStack.pop());
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdExpireat(CmdExpireatContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        ctx.unixTimeSeconds.accept(this);
        IntToken unixTimestampSec = (IntToken) instStack.pop();

        ExpireAtRedisCmd cmd = code(new ExpireAtRedisCmd(keyName, unixTimestampSec), ctx);
        KeyOptContext keyOpt = ctx.keyOpt();
        if (keyOpt != null) {
            keyOpt.accept(this);
            cmd.setKeyOpt((KeyOptToken) instStack.pop());
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdExpireTime(CmdExpireTimeContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        ExpireTimeRedisCmd cmd = code(new ExpireTimeRedisCmd(keyName), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdKeys(CmdKeysContext ctx) {
        ctx.pattern().accept(this);
        StrToken pattern = (StrToken) instStack.pop();

        KeysRedisCmd cmd = code(new KeysRedisCmd(pattern), ctx);
        this.instStack.push(cmd);
        return null;
    }

    //cmdMigrate      : MIGRATE host port key|"" destination-db timeout [COPY] [REPLACE] [AUTH password] [AUTH2 username password] [KEYS key [key ...]]

    @Override
    public T visitCmdMove(CmdMoveContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        ctx.db.accept(this);
        IntToken destDatabase = (IntToken) instStack.pop();

        MoveRedisCmd cmd = code(new MoveRedisCmd(keyName, destDatabase), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdObject(CmdObjectContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        TagToken optionTag = null;
        ObjectRedisCmd.OptionType optionType = null;
        if (ctx.ENCODING() != null) {
            optionTag = code(new TagToken(), ctx.ENCODING());
            optionType = ObjectRedisCmd.OptionType.ENCODING;
        } else if (ctx.FREQ() != null) {
            optionTag = code(new TagToken(), ctx.FREQ());
            optionType = ObjectRedisCmd.OptionType.FREQ;
        } else if (ctx.IDLETIME() != null) {
            optionTag = code(new TagToken(), ctx.IDLETIME());
            optionType = ObjectRedisCmd.OptionType.IDLETIME;
        } else if (ctx.REFCOUNT() != null) {
            optionTag = code(new TagToken(), ctx.REFCOUNT());
            optionType = ObjectRedisCmd.OptionType.REFCOUNT;
        }

        ObjectRedisCmd cmd = code(new ObjectRedisCmd(keyName, optionType), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdPersist(CmdPersistContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        PersistRedisCmd cmd = code(new PersistRedisCmd(keyName), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdPexpire(CmdPexpireContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        ctx.milliseconds.accept(this);
        IntToken milliseconds = (IntToken) instStack.pop();

        PExpireRedisCmd cmd = code(new PExpireRedisCmd(keyName, milliseconds), ctx);
        KeyOptContext keyOpt = ctx.keyOpt();
        if (keyOpt != null) {
            keyOpt.accept(this);

            KeyOptToken keyOptToken = (KeyOptToken) instStack.pop();
            cmd.setKeyOpt(keyOptToken);
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdPexpireat(CmdPexpireatContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        ctx.unixTimeSeconds.accept(this);
        IntToken unixTimeSeconds = (IntToken) instStack.pop();

        PExpireAtRedisCmd cmd = code(new PExpireAtRedisCmd(keyName, unixTimeSeconds), ctx);
        KeyOptContext keyOpt = ctx.keyOpt();
        if (keyOpt != null) {
            keyOpt.accept(this);

            KeyOptToken keyOptToken = (KeyOptToken) instStack.pop();
            cmd.setKeyOpt(keyOptToken);
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdPExpireTime(CmdPExpireTimeContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        PExpireTimeRedisCmd cmd = code(new PExpireTimeRedisCmd(keyName), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdTtl(CmdTtlContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        TTLRedisCmd cmd = code(new TTLRedisCmd(keyName), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdPttl(CmdPttlContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        PTTLRedisCmd cmd = code(new PTTLRedisCmd(keyName), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdRandomkey(CmdRandomkeyContext ctx) {
        RandomKeyRedisCmd cmd = code(new RandomKeyRedisCmd(), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdRename(CmdRenameContext ctx) {
        ctx.oldKey.accept(this); // old
        StrToken oldKey = (StrToken) instStack.pop();

        ctx.newName.accept(this); // new
        StrToken newName = (StrToken) instStack.pop();

        RenameRedisCmd cmd = code(new RenameRedisCmd(oldKey, newName), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdRenamenx(CmdRenamenxContext ctx) {
        ctx.oldKey.accept(this); // old
        StrToken oldKey = (StrToken) instStack.pop();

        ctx.newName.accept(this); // new
        StrToken newName = (StrToken) instStack.pop();

        RenameNXRedisCmd cmd = code(new RenameNXRedisCmd(oldKey, newName), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdRestore(CmdRestoreContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        ctx.ttl.accept(this);
        IntToken ttl = (IntToken) instStack.pop();

        ctx.serializedValue.accept(this);
        StrToken serializedValue = (StrToken) instStack.pop();

        RestoreRedisCmd cmd = code(new RestoreRedisCmd(keyName, ttl, serializedValue), ctx);

        TerminalNode replaceTag = ctx.REPLACE();
        if (replaceTag != null) {
            cmd.setReplace(code(new TagToken(), replaceTag));
        }

        TerminalNode absTtlTag = ctx.ABSTTL();
        if (absTtlTag != null) {
            cmd.setAbsTTL(code(new TagToken(), absTtlTag));
        }

        if (ctx.seconds != null) {
            ctx.seconds.accept(this);
            cmd.setIdleTime((IntToken) instStack.pop());
        }

        if (ctx.frequency != null) {
            ctx.frequency.accept(this);
            cmd.setFrequency((IntToken) instStack.pop());
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdScan(CmdScanContext ctx) {
        ctx.cursor().accept(this);
        IntToken cursor = (IntToken) instStack.pop();

        StrToken pattern = null;
        if (ctx.pattern() != null) {
            ctx.pattern().accept(this);
            pattern = (StrToken) instStack.pop();
        }

        ScanRedisCmd cmd = code(new ScanRedisCmd(cursor, pattern), ctx);

        if (ctx.COUNT() != null) {
            ctx.count().accept(this);
            cmd.setCount((IntToken) instStack.pop());
        }

        if (ctx.type != null) {
            ctx.type.accept(this);
            cmd.setType((StrToken) instStack.pop());
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSort(CmdSortContext ctx) {
        ctx.keyStr.accept(this);
        StrToken keyName = (StrToken) instStack.pop();
        SortRedisCmd cmd = code(new SortRedisCmd(keyName), ctx);

        if (ctx.byPattern != null) {
            ctx.byPattern.accept(this);
            StrToken byPattern = (StrToken) instStack.pop();
            cmd.setByPattern(byPattern);
        }

        if (ctx.limitOpt() != null) {
            ctx.limitOpt().accept(this);
            LimitToken limitToken = (LimitToken) instStack.pop();
            cmd.setLimitOffset(limitToken.getOffsetToken());
            cmd.setLimitCount(limitToken.getCountToken());
        }

        List<PatternOptContext> contexts = ctx.patternOpt();
        if (CollectionUtils.isNotEmpty(contexts)) {
            List<StrToken> getPatternList = new ArrayList<>();
            for (PatternOptContext patternOpt : contexts) {
                patternOpt.accept(this);
                getPatternList.add((StrToken) instStack.pop());
            }
            cmd.setGetPatterns(getPatternList);
        }

        if (ctx.sortOpt() != null) {
            ctx.sortOpt().accept(this);
            cmd.setSortToken((SortByToken) instStack.pop());
        }

        if (ctx.ALPHA() != null) {
            cmd.setAlphaTag(code(new TagToken(), ctx.ALPHA()));
        }

        if (ctx.destination != null) {
            ctx.destination.accept(this);
            cmd.setDestination((StrToken) instStack.pop());
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSortro(CmdSortroContext ctx) {
        ctx.keyStr.accept(this);
        StrToken keyName = (StrToken) instStack.pop();
        SortRORedisCmd cmd = code(new SortRORedisCmd(keyName), ctx);

        if (ctx.byPattern != null) {
            ctx.byPattern.accept(this);
            StrToken byPattern = (StrToken) instStack.pop();
            cmd.setByPattern(byPattern);
        }

        if (ctx.limitOpt() != null) {
            ctx.limitOpt().accept(this);
            LimitToken limitToken = (LimitToken) instStack.pop();
            cmd.setLimitOffset(limitToken.getOffsetToken());
            cmd.setLimitCount(limitToken.getCountToken());
        }

        List<PatternOptContext> contexts = ctx.patternOpt();
        if (CollectionUtils.isNotEmpty(contexts)) {
            List<StrToken> getPatternList = new ArrayList<>();
            for (PatternOptContext patternOpt : contexts) {
                patternOpt.accept(this);
                getPatternList.add((StrToken) instStack.pop());
            }
            cmd.setGetPatterns(getPatternList);
        }

        if (ctx.sortOpt() != null) {
            ctx.sortOpt().accept(this);
            cmd.setSortToken((SortByToken) instStack.pop());
        }

        if (ctx.ALPHA() != null) {
            cmd.setAlphaTag(code(new TagToken(), ctx.ALPHA()));
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdTouch(CmdTouchContext ctx) {
        TouchRedisCmd cmd = code(new TouchRedisCmd(), ctx);

        List<KeyNameContext> keyList = ctx.keyName();
        for (KeyNameContext key : keyList) {
            key.accept(this);
            cmd.addKey((StrToken) this.instStack.pop());
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdType(CmdTypeContext ctx) {
        TypeRedisCmd cmd = code(new TypeRedisCmd(), ctx);

        List<KeyNameContext> keyList = ctx.keyName();
        for (KeyNameContext key : keyList) {
            key.accept(this);
            cmd.addKeyName((StrToken) this.instStack.pop());
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdUnlink(CmdUnlinkContext ctx) {
        UnlinkRedisCmd cmd = code(new UnlinkRedisCmd(), ctx);

        List<KeyNameContext> keyList = ctx.keyName();
        for (KeyNameContext key : keyList) {
            key.accept(this);
            StrToken kas = (StrToken) this.instStack.pop();
            cmd.addKey(kas);
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdWait(CmdWaitContext ctx) {
        ctx.numreplicas.accept(this);
        IntToken numReplicas = (IntToken) instStack.pop();

        ctx.timeout.accept(this);
        IntToken timeout = (IntToken) instStack.pop();

        WaitRedisCmd cmd = code(new WaitRedisCmd(numReplicas, timeout), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdWaitAOF(CmdWaitAOFContext ctx) {
        ctx.numlocal.accept(this);
        IntToken numLocal = (IntToken) instStack.pop();

        ctx.numreplicas.accept(this);
        IntToken numReplicas = (IntToken) instStack.pop();

        ctx.timeout.accept(this);
        IntToken timeout = (IntToken) instStack.pop();

        WaitAOFRedisCmd cmd = code(new WaitAOFRedisCmd(numLocal, numReplicas, timeout), ctx);
        this.instStack.push(cmd);
        return null;
    }

    /* ----------------------------------------------------------------------------------- string commands */

    @Override
    public T visitCmdAppend(CmdAppendContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        ctx.valueAsStr().accept(this);
        StrToken newValue = (StrToken) instStack.pop();

        AppendRedisCmd cmd = code(new AppendRedisCmd(keyName, newValue), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdDecr(CmdDecrContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        DecrRedisCmd cmd = code(new DecrRedisCmd(keyName), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdDecrby(CmdDecrbyContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        ctx.decrement.accept(this);
        IntToken decrement = (IntToken) instStack.pop();

        DecrByRedisCmd cmd = code(new DecrByRedisCmd(keyName, decrement), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdGet(CmdGetContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        GetRedisCmd cmd = code(new GetRedisCmd(keyName), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdGetdel(CmdGetdelContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        GetDelRedisCmd cmd = code(new GetDelRedisCmd(keyName), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdGetex(CmdGetexContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        GetEXRedisCmd cmd = code(new GetEXRedisCmd(keyName), ctx);

        if (ctx.ttlOpt() != null) {
            ctx.ttlOpt().accept(this);
            cmd.setTtlOptToken((TtlOptToken) instStack.pop());
        }

        if (ctx.PERSIST() != null) {
            cmd.setPersistToken(code(new TagToken(), ctx.PERSIST()));
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdGetrange(CmdGetrangeContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        ctx.startNum.accept(this);
        IntToken start = (IntToken) instStack.pop();

        ctx.endNum.accept(this);
        IntToken end = (IntToken) instStack.pop();

        GetRangeRedisCmd cmd = code(new GetRangeRedisCmd(keyName, start, end), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdGetset(CmdGetsetContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        ctx.valueAsStr().accept(this);
        StrToken newValue = (StrToken) instStack.pop();

        GetSetRedisCmd cmd = code(new GetSetRedisCmd(keyName, newValue), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdIncr(CmdIncrContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        IncrRedisCmd cmd = code(new IncrRedisCmd(keyName), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdIncrby(CmdIncrbyContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        ctx.increment.accept(this);
        IntToken increment = (IntToken) instStack.pop();

        IncrByRedisCmd cmd = code(new IncrByRedisCmd(keyName, increment), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdIncrbyFloat(CmdIncrbyFloatContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        ctx.increment.accept(this);
        FloatToken increment = (FloatToken) instStack.pop();

        IncrByFloatRedisCmd cmd = code(new IncrByFloatRedisCmd(keyName, increment), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdLcs(CmdLcsContext ctx) {
        ctx.key1.accept(this);
        StrToken key1Name = (StrToken) instStack.pop();

        ctx.key2.accept(this);
        StrToken key2Name = (StrToken) instStack.pop();

        LcsRedisCmd cmd = code(new LcsRedisCmd(key1Name, key2Name), ctx);

        if (ctx.LEN() != null) {
            cmd.setLenToken(code(new TagToken(), ctx.LEN()));
        }

        if (ctx.IDX() != null) {
            cmd.setIdxToken(code(new TagToken(), ctx.IDX()));
        }

        if (ctx.MINMATCHLEN() != null) {
            ctx.minMatchLen.accept(this);
            cmd.setMinMatchLen((IntToken) instStack.pop());
        }

        if (ctx.WITHMATCHLEN() != null) {
            cmd.setWithMatchLen(code(new TagToken(), ctx.WITHMATCHLEN()));
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdMget(CmdMgetContext ctx) {
        MGetRedisCmd cmd = code(new MGetRedisCmd(), ctx);

        List<KeyNameContext> keyList = ctx.keyName();
        if (keyList != null) {
            for (KeyNameContext key : keyList) {
                key.accept(this);
                cmd.addKey((StrToken) this.instStack.pop());
            }
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdMset(CmdMsetContext ctx) {
        MSetRedisCmd cmd = code(new MSetRedisCmd(), ctx);

        List<KeyAndStringContext> keyAndStringList = ctx.keyAndString();
        if (keyAndStringList != null) {
            for (KeyAndStringContext keyAndString : keyAndStringList) {
                keyAndString.accept(this);
                cmd.addKeyValue((KeyAndStringToken) this.instStack.pop());
            }
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdMsetnx(CmdMsetnxContext ctx) {
        MSetNXRedisCmd cmd = code(new MSetNXRedisCmd(), ctx);

        List<KeyAndStringContext> keyAndStringList = ctx.keyAndString();
        if (keyAndStringList != null) {
            for (KeyAndStringContext keyAndString : keyAndStringList) {
                keyAndString.accept(this);
                cmd.addKeyValue((KeyAndStringToken) this.instStack.pop());
            }
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSetex(CmdSetexContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        ctx.seconds.accept(this);
        IntToken seconds = (IntToken) instStack.pop();

        ctx.valueAsStr().accept(this);
        StrToken value = (StrToken) instStack.pop();

        SetEXRedisCmd cmd = code(new SetEXRedisCmd(keyName, seconds, value), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdPSetex(CmdPSetexContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        ctx.milliseconds.accept(this);
        IntToken milliseconds = (IntToken) instStack.pop();

        ctx.valueAsStr().accept(this);
        StrToken value = (StrToken) instStack.pop();

        PSetEXRedisCmd cmd = code(new PSetEXRedisCmd(keyName, milliseconds, value), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSet(CmdSetContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        ctx.valueAsStr().accept(this);
        StrToken value = (StrToken) instStack.pop();

        SetRedisCmd cmd = code(new SetRedisCmd(keyName, value), ctx);

        TerminalNode nx = ctx.NX();
        TerminalNode xx = ctx.XX();
        if (nx != null) {
            KeyOptToken optToken = code(new KeyOptToken(KeyOptToken.KeyOptionType.NX), nx);
            cmd.setOptToken(optToken);
        } else if (xx != null) {
            KeyOptToken optToken = code(new KeyOptToken(KeyOptToken.KeyOptionType.XX), xx);
            cmd.setOptToken(optToken);
        }

        if (ctx.GET() != null) {
            cmd.setGetTag(code(new TagToken(), ctx.GET()));
        }

        if (ctx.ttlOpt() != null) {
            ctx.ttlOpt().accept(this);
            cmd.setTtlOptToken((TtlOptToken) instStack.pop());
        }
        if (ctx.KEEPTTL() != null) {
            cmd.setKeepTtlTag(code(new TagToken(), ctx.KEEPTTL()));
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSetnx(CmdSetnxContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        ctx.valueAsStr().accept(this);
        StrToken value = (StrToken) instStack.pop();

        SetNXRedisCmd cmd = code(new SetNXRedisCmd(keyName, value), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSetrange(CmdSetrangeContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        ctx.offset.accept(this);
        IntToken offset = (IntToken) instStack.pop();

        ctx.valueAsStr().accept(this);
        StrToken value = (StrToken) instStack.pop();

        SetRangeRedisCmd cmd = code(new SetRangeRedisCmd(keyName, offset, value), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdStrlen(CmdStrlenContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        StrLenRedisCmd cmd = code(new StrLenRedisCmd(keyName), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSubstr(CmdSubstrContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        ctx.startNum.accept(this);
        IntToken start = (IntToken) instStack.pop();

        ctx.endNum.accept(this);
        IntToken end = (IntToken) instStack.pop();

        SubstrRedisCmd cmd = code(new SubstrRedisCmd(keyName, start, end), ctx);
        this.instStack.push(cmd);
        return null;
    }

    /* ----------------------------------------------------------------------------------- bit commands */
    @Override
    public T visitEncodOffOpt(EncodOffOptContext ctx) {
        throw new UnsupportedOperationException("never gonna happen.");
    }

    @Override
    public T visitBitFieldItem(BitFieldItemContext ctx) {
        throw new UnsupportedOperationException("never gonna happen.");
    }

    private GetItem buildGetItem(EncodOffOptContext ctx) {
        ctx.encoding.accept(this);
        StrToken encodingToken = (StrToken) instStack.pop();

        ctx.offsetNum().accept(this);
        StrToken offsetToken = (StrToken) instStack.pop();

        GetItem item = new GetItem();
        item.setEncoding(encodingToken);
        item.setOffset(offsetToken);
        return item;
    }

    private SetItem buildSetItem(BitFieldItemContext ctx) {
        SetItem item = new SetItem();

        if (ctx.OVERFLOW() != null) {
            if (ctx.WRAP() != null) {
                item.setOverflow(OverflowItem.WRAP);
            } else if (ctx.SAT() != null) {
                item.setOverflow(OverflowItem.SAT);
            } else if (ctx.FAIL() != null) {
                item.setOverflow(OverflowItem.FAIL);
            } else {
                throw new UnsupportedOperationException("not implemented yet.");
            }
        }

        EncodOffOptContext valContext = ctx.setOpt;
        valContext.encoding.accept(this);
        StrToken encodingToken = (StrToken) instStack.pop();

        valContext.offsetNum().accept(this);
        StrToken offsetToken = (StrToken) instStack.pop();

        item.setEncoding(encodingToken);
        item.setOffset(offsetToken);

        ctx.valueAsStr().accept(this);
        StrToken valueToken = (StrToken) instStack.pop();
        item.setValue(valueToken);
        return item;
    }

    private IncrItem buildIncrItem(BitFieldItemContext ctx) {
        IncrItem item = new IncrItem();

        if (ctx.OVERFLOW() != null) {
            if (ctx.WRAP() != null) {
                item.setOverflow(OverflowItem.WRAP);
            } else if (ctx.SAT() != null) {
                item.setOverflow(OverflowItem.SAT);
            } else if (ctx.FAIL() != null) {
                item.setOverflow(OverflowItem.FAIL);
            } else {
                throw new UnsupportedOperationException("not implemented yet.");
            }
        }

        EncodOffOptContext valContext = ctx.incrOpt;
        valContext.encoding.accept(this);
        StrToken encodingToken = (StrToken) instStack.pop();

        valContext.offsetNum().accept(this);
        StrToken offsetToken = (StrToken) instStack.pop();

        item.setEncoding(encodingToken);
        item.setOffset(offsetToken);

        ctx.intValue().accept(this);
        IntToken valueToken = (IntToken) instStack.pop();
        item.setIncr(valueToken);
        return item;
    }

    @Override
    public T visitCmdBitField(CmdBitFieldContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();
        BitFieldRedisCmd cmd = code(new BitFieldRedisCmd(keyName), ctx);

        List<BitFieldItemContext> items = ctx.bitFieldItem();
        if (CollectionUtils.isNotEmpty(items)) {
            for (BitFieldItemContext item : items) {
                if (item.GET() != null) {
                    cmd.addItem(buildGetItem(item.encodOffOpt()));
                } else if (item.SET() != null) {
                    cmd.addItem(buildSetItem(item));
                } else if (item.INCRBY() != null) {
                    cmd.addItem(buildIncrItem(item));
                } else {
                    throw new UnsupportedOperationException("Unknown BitField item.");
                }
            }
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdBitFieldRO(CmdBitFieldROContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();
        BitFieldRORedisCmd cmd = code(new BitFieldRORedisCmd(keyName), ctx);

        List<EncodOffOptContext> items = ctx.encodOffOpt();
        if (CollectionUtils.isNotEmpty(items)) {
            for (EncodOffOptContext item : items) {
                cmd.addItem(buildGetItem(item));
            }
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdBitCount(CmdBitCountContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();
        BitCountRedisCmd cmd = code(new BitCountRedisCmd(keyName), ctx);

        if (ctx.start != null && ctx.end != null) {
            ctx.start.accept(this);
            IntToken startToken = (IntToken) instStack.pop();
            cmd.setStart(startToken);

            ctx.end.accept(this);
            IntToken endToken = (IntToken) instStack.pop();
            cmd.setEnd(endToken);
        }

        if (ctx.BYTE() != null) {
            cmd.setTypeEnum(BitCountTypeEnum.BYTE);
        } else if (ctx.BIT() != null) {
            cmd.setTypeEnum(BitCountTypeEnum.BIT);
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdBitOP(CmdBitOPContext ctx) {
        BitOPRedisCmd cmd = code(new BitOPRedisCmd(), ctx);

        ctx.destkey.accept(this);
        cmd.setDestKey((StrToken) instStack.pop());

        for (KeyContext context : ctx.key()) {
            context.accept(this);
            cmd.addKey((StrToken) this.instStack.pop());
        }

        if (ctx.AND() != null) {
            cmd.setOpType(BitOPTypeEnum.AND);
        } else if (ctx.OR() != null) {
            cmd.setOpType(BitOPTypeEnum.OR);
        } else if (ctx.XOR() != null) {
            cmd.setOpType(BitOPTypeEnum.XOR);
        } else if (ctx.NOT() != null) {
            cmd.setOpType(BitOPTypeEnum.NOT);
        } else if (ctx.DIFF() != null) {
            cmd.setOpType(BitOPTypeEnum.DIFF);
        } else if (ctx.DIFF1() != null) {
            cmd.setOpType(BitOPTypeEnum.DIFF1);
        } else if (ctx.ANDOR() != null) {
            cmd.setOpType(BitOPTypeEnum.ANDOR);
        } else if (ctx.ONE() != null) {
            cmd.setOpType(BitOPTypeEnum.ONE);
        } else {
            throw new IllegalArgumentException("Unknown BitOP type.");
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdBitPos(CmdBitPosContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();
        BitPosRedisCmd cmd = code(new BitPosRedisCmd(keyName), ctx);

        ctx.bitValue().accept(this);
        cmd.setBitValue((BoolToken) instStack.pop());

        if (ctx.start != null) {
            ctx.start.accept(this);
            IntToken startToken = (IntToken) instStack.pop();
            cmd.setStart(startToken);

            if (ctx.end != null) {
                ctx.end.accept(this);
                IntToken endToken = (IntToken) instStack.pop();
                cmd.setEnd(endToken);
            }
        }

        if (ctx.BYTE() != null) {
            cmd.setTypeEnum(BitCountTypeEnum.BYTE);
        } else if (ctx.BIT() != null) {
            cmd.setTypeEnum(BitCountTypeEnum.BIT);
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdGetbit(CmdGetbitContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        ctx.offset.accept(this);
        IntToken offset = (IntToken) instStack.pop();

        GetBitRedisCmd cmd = code(new GetBitRedisCmd(keyName, offset), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSetbit(CmdSetbitContext ctx) {
        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) instStack.pop();

        ctx.offset.accept(this);
        IntToken offset = (IntToken) instStack.pop();

        ctx.bitValue().accept(this);
        BoolToken value = (BoolToken) instStack.pop();

        SetBitRedisCmd cmd = code(new SetBitRedisCmd(keyName, offset, value), ctx);
        this.instStack.push(cmd);
        return null;
    }

    /* ----------------------------------------------------------------------------------- hash commands */

    @Override
    public T visitCmdHdel(CmdHdelContext ctx) {
        HDelRedisCmd cmd = new HDelRedisCmd();
        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        for (FieldNameContext context : ctx.fieldName()) {
            context.accept(this);
            cmd.addKey((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdHexists(CmdHexistsContext ctx) {
        HExistsRedisCmd cmd = new HExistsRedisCmd();
        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.fieldName().accept(this);
        cmd.setFieldName((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdHexpire(CmdHexpireContext ctx) {
        HExpireRedisCmd cmd = new HExpireRedisCmd();
        ctx.keyName().accept(this);
        cmd.setKey((StrToken) this.instStack.pop());

        ctx.timeout.accept(this);
        cmd.setTimeout((IntToken) this.instStack.pop());

        if (ctx.keyOpt() != null) {
            ctx.keyOpt().accept(this);
            cmd.setKeyOpt((KeyOptToken) this.instStack.pop());
        }

        ctx.numfields.accept(this);
        cmd.setNumFields((IntToken) this.instStack.pop());

        for (FieldNameContext fieldName : ctx.fieldName()) {
            fieldName.accept(this);
            cmd.addField((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdHexpireat(CmdHexpireatContext ctx) {
        HExpireAtRedisCmd cmd = new HExpireAtRedisCmd();
        ctx.keyName().accept(this);
        cmd.setKey((StrToken) this.instStack.pop());

        ctx.timeout.accept(this);
        cmd.setTimeout((IntToken) this.instStack.pop());

        if (ctx.keyOpt() != null) {
            ctx.keyOpt().accept(this);
            cmd.setKeyOpt((KeyOptToken) this.instStack.pop());
        }

        ctx.numfields.accept(this);
        cmd.setNumFields((IntToken) this.instStack.pop());

        for (FieldNameContext fieldName : ctx.fieldName()) {
            fieldName.accept(this);
            cmd.addField((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdHexpiretime(CmdHexpiretimeContext ctx) {
        HExpireTimeRedisCmd cmd = new HExpireTimeRedisCmd();
        ctx.keyName().accept(this);
        cmd.setKey((StrToken) this.instStack.pop());

        ctx.numfields.accept(this);
        cmd.setNumFields((IntToken) this.instStack.pop());

        for (FieldNameContext fieldName : ctx.fieldName()) {
            fieldName.accept(this);
            cmd.addField((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdHGet(CmdHGetContext ctx) {
        HGetRedisCmd cmd = new HGetRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.fieldName().accept(this);
        cmd.setFieldName((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdHGetAll(CmdHGetAllContext ctx) {
        HGetAllRedisCmd cmd = new HGetAllRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdHgetDel(CmdHgetDelContext ctx) {
        HGetDelRedisCmd cmd = new HGetDelRedisCmd();
        ctx.keyName().accept(this);
        cmd.setKey((StrToken) this.instStack.pop());

        ctx.numfields.accept(this);
        cmd.setNumFields((IntToken) this.instStack.pop());

        for (FieldNameContext fieldName : ctx.fieldName()) {
            fieldName.accept(this);
            cmd.addField((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdHgetex(CmdHgetexContext ctx) {
        HGetEXRedisCmd cmd = new HGetEXRedisCmd();
        ctx.keyName().accept(this);
        cmd.setKey((StrToken) this.instStack.pop());

        if (ctx.ttlOpt() != null) {
            ctx.ttlOpt().accept(this);
            cmd.setTtlOptToken((TtlOptToken) instStack.pop());
        } else if (ctx.PERSIST() != null) {
            cmd.setPersistTag(code(new TagToken(), ctx.PERSIST()));
        }

        ctx.numfields.accept(this);
        cmd.setNumFields((IntToken) this.instStack.pop());

        for (FieldNameContext fieldName : ctx.fieldName()) {
            fieldName.accept(this);
            cmd.addField((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdHincrBy(CmdHincrByContext ctx) {
        HIncrByRedisCmd cmd = new HIncrByRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.fieldName().accept(this);
        cmd.setFiledName((StrToken) this.instStack.pop());

        ctx.floatValue().accept(this);
        cmd.setFloatValue((FloatToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdHincrByFloat(CmdHincrByFloatContext ctx) {
        HIncrByFloatRedisCmd cmd = new HIncrByFloatRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.fieldName().accept(this);
        cmd.setFiledName((StrToken) this.instStack.pop());

        ctx.floatValue().accept(this);
        cmd.setFloatValue((FloatToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdHKeys(CmdHKeysContext ctx) {
        HKeysRedisCmd cmd = new HKeysRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdHLen(CmdHLenContext ctx) {
        HLenRedisCmd cmd = new HLenRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdHMget(CmdHMgetContext ctx) {
        HMGetRedisCmd cmd = new HMGetRedisCmd();
        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        for (FieldNameContext context : ctx.fieldName()) {
            context.accept(this);
            cmd.addKey((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdHMset(CmdHMsetContext ctx) {
        HMSetRedisCmd cmd = new HMSetRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        List<KeyAndStringContext> contexts = ctx.keyAndString();
        for (KeyAndStringContext context : contexts) {
            context.accept(this);
            cmd.addKeyValue((KeyAndStringToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdHPersist(CmdHPersistContext ctx) {
        HPersistRedisCmd cmd = new HPersistRedisCmd();
        ctx.keyName().accept(this);
        cmd.setKey((StrToken) this.instStack.pop());

        ctx.numfields.accept(this);
        cmd.setNumFields((IntToken) this.instStack.pop());

        for (FieldNameContext fieldName : ctx.fieldName()) {
            fieldName.accept(this);
            cmd.addField((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdHPexpire(CmdHPexpireContext ctx) {
        HPExpireRedisCmd cmd = new HPExpireRedisCmd();
        ctx.keyName().accept(this);
        cmd.setKey((StrToken) this.instStack.pop());

        ctx.timeout.accept(this);
        cmd.setTimeout((IntToken) this.instStack.pop());

        if (ctx.keyOpt() != null) {
            ctx.keyOpt().accept(this);
            cmd.setKeyOpt((KeyOptToken) this.instStack.pop());
        }

        ctx.numfields.accept(this);
        cmd.setNumFields((IntToken) this.instStack.pop());

        for (FieldNameContext fieldName : ctx.fieldName()) {
            fieldName.accept(this);
            cmd.addField((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdHPexpireAt(CmdHPexpireAtContext ctx) {
        HPExpireAtRedisCmd cmd = new HPExpireAtRedisCmd();
        ctx.keyName().accept(this);
        cmd.setKey((StrToken) this.instStack.pop());

        ctx.timeout.accept(this);
        cmd.setTimeout((IntToken) this.instStack.pop());

        if (ctx.keyOpt() != null) {
            ctx.keyOpt().accept(this);
            cmd.setKeyOpt((KeyOptToken) this.instStack.pop());
        }

        ctx.numfields.accept(this);
        cmd.setNumFields((IntToken) this.instStack.pop());

        for (FieldNameContext fieldName : ctx.fieldName()) {
            fieldName.accept(this);
            cmd.addField((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdHPexpireTime(CmdHPexpireTimeContext ctx) {
        HPExpireTimeRedisCmd cmd = new HPExpireTimeRedisCmd();
        ctx.keyName().accept(this);
        cmd.setKey((StrToken) this.instStack.pop());

        ctx.numfields.accept(this);
        cmd.setNumFields((IntToken) this.instStack.pop());

        for (FieldNameContext fieldName : ctx.fieldName()) {
            fieldName.accept(this);
            cmd.addField((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdHPTTL(CmdHPTTLContext ctx) {
        HPTtlRedisCmd cmd = new HPTtlRedisCmd();
        ctx.keyName().accept(this);
        cmd.setKey((StrToken) this.instStack.pop());

        ctx.numfields.accept(this);
        cmd.setNumFields((IntToken) this.instStack.pop());

        for (FieldNameContext fieldName : ctx.fieldName()) {
            fieldName.accept(this);
            cmd.addField((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdHTTL(CmdHTTLContext ctx) {
        HTtlRedisCmd cmd = new HTtlRedisCmd();
        ctx.keyName().accept(this);
        cmd.setKey((StrToken) this.instStack.pop());

        ctx.numfields.accept(this);
        cmd.setNumFields((IntToken) this.instStack.pop());

        for (FieldNameContext fieldName : ctx.fieldName()) {
            fieldName.accept(this);
            cmd.addField((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdHrandfield(CmdHrandfieldContext ctx) {
        HRandFieldRedisCmd cmd = new HRandFieldRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        if (ctx.count() != null) {
            ctx.count().accept(this);
            cmd.setCount((IntToken) this.instStack.pop());

            if (ctx.WITHVALUES() != null) {
                cmd.setWithValues(true);
            }
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdHscan(CmdHscanContext ctx) {
        HScanRedisCmd cmd = new HScanRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.cursor().accept(this);
        IntToken value1 = (IntToken) this.instStack.pop();
        cmd.setCursor(value1);

        if (ctx.MATCH() != null) {
            ctx.pattern().accept(this);
            StrToken pattern = (StrToken) this.instStack.pop();
            cmd.setPattern(pattern);
        }

        if (ctx.COUNT() != null) {
            ctx.count().accept(this);
            cmd.setCount((IntToken) this.instStack.pop());
        }

        if (ctx.NOVALUES() != null) {
            cmd.setNoValues(code(new TagToken(), ctx.NOVALUES()));
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdHSet(CmdHSetContext ctx) {
        HSetRedisCmd cmd = new HSetRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        List<KeyAndStringContext> contexts = ctx.keyAndString();
        for (KeyAndStringContext context : contexts) {
            context.accept(this);
            cmd.addKeyValue((KeyAndStringToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdHSetex(CmdHSetexContext ctx) {
        HSetEXRedisCmd cmd = new HSetEXRedisCmd();
        ctx.keyName().accept(this);
        cmd.setKey((StrToken) this.instStack.pop());

        if (ctx.FXX() != null) {
            cmd.setFxOpt(HSetEXRedisCmd.HSetExOpt.FXX);
        } else if (ctx.FNX() != null) {
            cmd.setFxOpt(HSetEXRedisCmd.HSetExOpt.FNX);
        }

        if (ctx.ttlOpt() != null) {
            ctx.ttlOpt().accept(this);
            cmd.setTtlOptToken((TtlOptToken) instStack.pop());
        } else if (ctx.KEEPTTL() != null) {
            cmd.setKeepTtlTag(code(new TagToken(), ctx.KEEPTTL()));
        }

        ctx.numfields.accept(this);
        cmd.setNumFields((IntToken) this.instStack.pop());

        List<KeyAndStringContext> contexts = ctx.keyAndString();
        for (KeyAndStringContext context : contexts) {
            context.accept(this);
            cmd.addKeyValue((KeyAndStringToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdHsetnx(CmdHsetnxContext ctx) {
        HSetNXRedisCmd cmd = new HSetNXRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.fieldName().accept(this);
        cmd.setFieldName((StrToken) this.instStack.pop());

        ctx.valueAsStr().accept(this);
        cmd.setValue((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdHStrLen(CmdHStrLenContext ctx) {
        HStrLenRedisCmd cmd = new HStrLenRedisCmd();
        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.fieldName().accept(this);
        cmd.setFieldName((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdHVals(CmdHValsContext ctx) {
        HValsRedisCmd cmd = new HValsRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    /* ----------------------------------------------------------------------------------- list commands */

    @Override
    public T visitCmdBlmove(CmdBlmoveContext ctx) {
        BLMoveRedisCmd cmd = new BLMoveRedisCmd();

        ctx.src.accept(this);
        cmd.setSrc((StrToken) this.instStack.pop());

        ctx.dst.accept(this);
        cmd.setDst((StrToken) this.instStack.pop());

        if (ctx.srcDirection != null) {
            if (ctx.srcDirection.LEFT() != null) {
                cmd.setSrcDirection(Direction.LEFT);
            } else if (ctx.srcDirection.RIGHT() != null) {
                cmd.setSrcDirection(Direction.RIGHT);
            } else {
                throw new IllegalArgumentException("Unknown direction for BLMOVE srcDirection.");
            }
        }
        if (ctx.dstDirection != null) {
            if (ctx.dstDirection.LEFT() != null) {
                cmd.setDstDirection(Direction.LEFT);
            } else if (ctx.dstDirection.RIGHT() != null) {
                cmd.setDstDirection(Direction.RIGHT);
            } else {
                throw new IllegalArgumentException("Unknown direction for BLMOVE dstDirection.");
            }
        }

        ctx.timeout.accept(this);
        cmd.setTimeout((IntToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdBLmpop(CmdBLmpopContext ctx) {
        BLMPopRedisCmd cmd = new BLMPopRedisCmd();

        ctx.timeout.accept(this);
        cmd.setTimeout((IntToken) this.instStack.pop());

        ctx.numkeys.accept(this);
        cmd.setNumKeys((IntToken) this.instStack.pop());

        List<KeyNameContext> keys = ctx.keyName();
        for (KeyNameContext keyNameContext : keys) {
            keyNameContext.accept(this);
            cmd.addKeyNames((StrToken) this.instStack.pop());
        }

        DirectionOptContext directionOpt = ctx.directionOpt();
        if (directionOpt.LEFT() != null) {
            cmd.setDirection(Direction.LEFT);
        } else if (directionOpt.RIGHT() != null) {
            cmd.setDirection(Direction.RIGHT);
        } else {
            throw new IllegalArgumentException("Unknown direction for BLMPOP direction.");
        }

        if (ctx.COUNT() != null) {
            ctx.count().accept(this);
            cmd.setCount((IntToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdBLPop(CmdBLPopContext ctx) {
        BLPopRedisCmd cmd = new BLPopRedisCmd();

        List<KeyNameContext> keyNameContexts = ctx.keyName();
        for (KeyNameContext keyNameContext : keyNameContexts) {
            keyNameContext.accept(this);
            cmd.addKeyNames((StrToken) this.instStack.pop());
        }

        ctx.intValue().accept(this);
        cmd.setTimeout((IntToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdBRPop(CmdBRPopContext ctx) {
        BRPopRedisCmd cmd = new BRPopRedisCmd();

        List<KeyNameContext> keyNameContexts = ctx.keyName();
        for (KeyNameContext keyNameContext : keyNameContexts) {
            keyNameContext.accept(this);
            cmd.addKeyNames((StrToken) this.instStack.pop());
        }

        ctx.intValue().accept(this);
        cmd.setTimeout((IntToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdBrpoplpush(CmdBrpoplpushContext ctx) {
        BRPopLPushRedisCmd cmd = new BRPopLPushRedisCmd();

        ctx.src.accept(this);
        cmd.setSrcKey((StrToken) this.instStack.pop());

        ctx.dst.accept(this);
        cmd.setDstKEy((StrToken) this.instStack.pop());

        ctx.timeout.accept(this);
        cmd.setTimeout((IntToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdLindex(CmdLindexContext ctx) {
        LIndexRedisCmd cmd = new LIndexRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.index.accept(this);
        cmd.setIndex((IntToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdLinsert(CmdLinsertContext ctx) {
        LInsertRedisCmd cmd = new LInsertRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        if (ctx.BEFORE() != null) {
            cmd.setPosition(Position.BEFORE);
        } else if (ctx.AFTER() != null) {
            cmd.setPosition(Position.AFTER);
        } else {
            throw new IllegalArgumentException("Unknown position for LINSERT command.");
        }

        ctx.pivot.accept(this);
        cmd.setPivot((StrToken) this.instStack.pop());

        ctx.element().accept(this);
        cmd.setElement((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdLlen(CmdLlenContext ctx) {
        LLenRedisCmd cmd = new LLenRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdLmove(CmdLmoveContext ctx) {
        LMoveRedisCmd cmd = new LMoveRedisCmd();

        ctx.src.accept(this);
        cmd.setSrc((StrToken) this.instStack.pop());

        ctx.dst.accept(this);
        cmd.setDst((StrToken) this.instStack.pop());

        if (ctx.srcDirection != null) {
            if (ctx.srcDirection.LEFT() != null) {
                cmd.setSrcDirection(Direction.LEFT);
            } else if (ctx.srcDirection.RIGHT() != null) {
                cmd.setSrcDirection(Direction.RIGHT);
            } else {
                throw new IllegalArgumentException("Unknown direction for LMOVE direction.");
            }
        }
        if (ctx.dstDirection != null) {
            if (ctx.dstDirection.LEFT() != null) {
                cmd.setDstDirection(Direction.LEFT);
            } else if (ctx.dstDirection.RIGHT() != null) {
                cmd.setDstDirection(Direction.RIGHT);
            } else {
                throw new IllegalArgumentException("Unknown direction for LMOVE direction.");
            }
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdLmpop(CmdLmpopContext ctx) {
        LMPopRedisCmd cmd = new LMPopRedisCmd();

        ctx.numkeys.accept(this);
        cmd.setNumKeys((IntToken) this.instStack.pop());

        List<KeyNameContext> keys = ctx.keyName();
        for (KeyNameContext keyNameContext : keys) {
            keyNameContext.accept(this);
            StrToken keyName = (StrToken) this.instStack.pop();
            cmd.addKeyNames(keyName);
        }

        DirectionOptContext directionOpt = ctx.directionOpt();
        if (directionOpt.LEFT() != null) {
            cmd.setDirection(Direction.LEFT);
        } else if (directionOpt.RIGHT() != null) {
            cmd.setDirection(Direction.RIGHT);
        } else {
            throw new IllegalArgumentException("Unknown direction for LMPOP direction.");
        }

        if (ctx.COUNT() != null) {
            ctx.count().accept(this);
            cmd.setCount((IntToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdLPop(CmdLPopContext ctx) {
        LPopRedisCmd cmd = new LPopRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        if (ctx.count() != null) {
            ctx.count().accept(this);
            cmd.setCount((IntToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdLpos(CmdLposContext ctx) {
        LPosRedisCmd cmd = new LPosRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.element().accept(this);
        cmd.setElement((StrToken) this.instStack.pop());

        if (ctx.rank != null) {
            ctx.rank.accept(this);
            cmd.setRank((IntToken) this.instStack.pop());
        }

        if (ctx.numMatches != null) {
            ctx.numMatches.accept(this);
            cmd.setCount((IntToken) this.instStack.pop());
        }

        if (ctx.len != null) {
            ctx.len.accept(this);
            cmd.setMaxLen((IntToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdLPush(CmdLPushContext ctx) {
        LPushRedisCmd cmd = new LPushRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        List<StringValueContext> stringValueContexts = ctx.stringValue();
        for (StringValueContext stringValueContext : stringValueContexts) {
            stringValueContext.accept(this);
            StrToken strValue = (StrToken) this.instStack.pop();
            cmd.addStrValue(strValue);
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdLPushx(CmdLPushxContext ctx) {
        LPushXRedisCmd cmd = new LPushXRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        List<StringValueContext> stringValueContexts = ctx.stringValue();
        for (StringValueContext stringValueContext : stringValueContexts) {
            stringValueContext.accept(this);
            StrToken strValue = (StrToken) this.instStack.pop();
            cmd.addStrValue(strValue);
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdLRange(CmdLRangeContext ctx) {
        LRangeRedisCmd cmd = new LRangeRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.startNum.accept(this);
        cmd.setStart((IntToken) this.instStack.pop());

        ctx.endNum.accept(this);
        cmd.setStop((IntToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdLRem(CmdLRemContext ctx) {
        LRemRedisCmd cmd = new LRemRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.count().accept(this);
        cmd.setCount((IntToken) this.instStack.pop());

        ctx.element().accept(this);
        cmd.setElement((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdLSet(CmdLSetContext ctx) {
        LSetRedisCmd cmd = new LSetRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.index.accept(this);
        cmd.setIndex((IntToken) this.instStack.pop());

        ctx.element().accept(this);
        cmd.setElement((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdLTrim(CmdLTrimContext ctx) {
        LTrimRedisCmd cmd = new LTrimRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.startNum.accept(this);
        cmd.setStartNum((IntToken) this.instStack.pop());

        ctx.endNum.accept(this);
        cmd.setStopNum((IntToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdRPop(CmdRPopContext ctx) {
        RPopRedisCmd cmd = new RPopRedisCmd();

        ctx.keyName().accept(this);
        StrToken keyName = (StrToken) this.instStack.pop();
        cmd.setKeyName(keyName);

        if (ctx.count() != null) {
            ctx.count().accept(this);
            cmd.setCount((IntToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdRpoplpush(CmdRpoplpushContext ctx) {
        RPopLPushRedisCmd cmd = new RPopLPushRedisCmd();

        ctx.src.accept(this);
        cmd.setSrcKey((StrToken) this.instStack.pop());

        ctx.dst.accept(this);
        cmd.setDstKey((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdRPush(CmdRPushContext ctx) {
        RPushRedisCmd cmd = new RPushRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        List<StringValueContext> stringValueContexts = ctx.stringValue();
        for (StringValueContext stringValueContext : stringValueContexts) {
            stringValueContext.accept(this);
            cmd.addStrValue((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdRPushx(CmdRPushxContext ctx) {
        RPushXRedisCmd cmd = new RPushXRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        List<StringValueContext> stringValueContexts = ctx.stringValue();
        for (StringValueContext stringValueContext : stringValueContexts) {
            stringValueContext.accept(this);
            cmd.addStrValue((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    /* ----------------------------------------------------------------------------------- set commands */

    @Override
    public T visitCmdSadd(CmdSaddContext ctx) {
        SAddRedisCmd cmd = new SAddRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        List<FieldNameContext> fieldNameContexts = ctx.fieldName();
        for (FieldNameContext fieldNameContext : fieldNameContexts) {
            fieldNameContext.accept(this);
            cmd.addMember((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdScard(CmdScardContext ctx) {
        SCardRedisCmd cmd = new SCardRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSdiff(CmdSdiffContext ctx) {
        SDiffRedisCmd cmd = new SDiffRedisCmd();

        List<KeyNameContext> keyNameContexts = ctx.keyName();
        for (KeyNameContext keyNameContext : keyNameContexts) {
            keyNameContext.accept(this);
            cmd.addKeyName((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSdiffstore(CmdSdiffstoreContext ctx) {
        SDiffStoreRedisCmd cmd = new SDiffStoreRedisCmd();

        ctx.dst.accept(this);
        cmd.setDstKey((StrToken) this.instStack.pop());

        List<MemberContext> memberContexts = ctx.member();
        for (MemberContext memberContext : memberContexts) {
            memberContext.accept(this);
            cmd.addKey((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSinter(CmdSinterContext ctx) {
        SInterRedisCmd cmd = new SInterRedisCmd();

        List<KeyNameContext> keyNameContexts = ctx.keyName();
        for (KeyNameContext keyNameContext : keyNameContexts) {
            keyNameContext.accept(this);
            cmd.addKeyName((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSinterCard(CmdSinterCardContext ctx) {
        SInterCardRedisCmd cmd = new SInterCardRedisCmd();

        ctx.numkeys.accept(this);
        cmd.setNumKeys((IntToken) this.instStack.pop());

        List<KeyNameContext> keyNameContexts = ctx.keyName();
        for (KeyNameContext keyNameContext : keyNameContexts) {
            keyNameContext.accept(this);
            cmd.addKey((StrToken) this.instStack.pop());
        }

        if (ctx.limit != null) {
            ctx.limit.accept(this);
            cmd.setLimit((IntToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSinterStore(CmdSinterStoreContext ctx) {
        SinterStoreRedisCmd cmd = new SinterStoreRedisCmd();

        ctx.dst.accept(this);
        cmd.setDst((StrToken) this.instStack.pop());

        List<MemberContext> memberContexts = ctx.member();
        for (MemberContext memberContext : memberContexts) {
            memberContext.accept(this);
            cmd.addKey((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSismember(CmdSismemberContext ctx) {
        SISMemberRedisCmd cmd = new SISMemberRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.member().accept(this);
        cmd.setMember((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSmembers(CmdSmembersContext ctx) {
        SMembersRedisCmd cmd = new SMembersRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSmismember(CmdSmismemberContext ctx) {
        SMISMemberRedisCmd cmd = new SMISMemberRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        List<FieldNameContext> nameContexts = ctx.fieldName();
        for (FieldNameContext nameContext : nameContexts) {
            nameContext.accept(this);
            cmd.addMember((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSmove(CmdSmoveContext ctx) {
        SMoveRedisCmd cmd = new SMoveRedisCmd();

        ctx.src.accept(this);
        cmd.setSrc((StrToken) this.instStack.pop());

        ctx.dst.accept(this);
        cmd.setDst((StrToken) this.instStack.pop());

        ctx.member().accept(this);
        cmd.setMember((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSpop(CmdSpopContext ctx) {
        SPopRedisCmd cmd = new SPopRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        if (ctx.count() != null) {
            ctx.count().accept(this);
            cmd.setCount((IntToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSrandmember(CmdSrandmemberContext ctx) {
        SRandMemberRedisCmd cmd = new SRandMemberRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        if (ctx.count() != null) {
            ctx.count().accept(this);
            cmd.setCount((IntToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSrem(CmdSremContext ctx) {
        SRemRedisCmd cmd = new SRemRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        List<FieldNameContext> nameContexts = ctx.fieldName();
        for (FieldNameContext nameContext : nameContexts) {
            nameContext.accept(this);
            cmd.addMember((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSscan(CmdSscanContext ctx) {
        SSCanRedisCmd cmd = new SSCanRedisCmd();
        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.cursor().accept(this);
        cmd.setCursor((IntToken) this.instStack.pop());

        if (ctx.MATCH() != null) {
            ctx.pattern().accept(this);
            cmd.setPattern((StrToken) this.instStack.pop());
        }

        if (ctx.COUNT() != null) {
            ctx.count().accept(this);
            cmd.setCount((IntToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSunion(CmdSunionContext ctx) {
        SUnionRedisCmd cmd = new SUnionRedisCmd();

        List<KeyNameContext> keyNameContexts = ctx.keyName();
        for (KeyNameContext keyNameContext : keyNameContexts) {
            keyNameContext.accept(this);
            cmd.addKeyName((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSunionstore(CmdSunionstoreContext ctx) {
        SUnionStoreRedisCmd cmd = new SUnionStoreRedisCmd();

        ctx.dst.accept(this);
        cmd.setDst((StrToken) this.instStack.pop());

        List<MemberContext> memberContexts = ctx.member();
        for (MemberContext memberContext : memberContexts) {
            memberContext.accept(this);
            cmd.addKey((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    /* ----------------------------------------------------------------------------------- sorted set commands */

    @Override
    public T visitScoreAndMemberOpt(ScoreAndMemberOptContext ctx) {
        throw new UnsupportedOperationException("impossible");
    }

    @Override
    public T visitCmdBzmpop(CmdBzmpopContext ctx) {
        BZMPopRedisCmd cmd = new BZMPopRedisCmd();

        ctx.timeout.accept(this);
        cmd.setTimeout((IntToken) this.instStack.pop());

        ctx.numkeys.accept(this);
        cmd.setNumKeys((IntToken) this.instStack.pop());

        List<KeyNameContext> keyNameContexts = ctx.keyName();
        for (KeyNameContext keyNameContext : keyNameContexts) {
            keyNameContext.accept(this);
            cmd.addKeyName((StrToken) this.instStack.pop());
        }

        if (ctx.MIN() != null) {
            cmd.setMinMax(MinMax.MIN);
        } else if (ctx.MAX() != null) {
            cmd.setMinMax(MinMax.MAX);
        } else {
            throw new UnsupportedOperationException("BZMPOP need min or max");
        }

        if (ctx.count() != null) {
            ctx.count().accept(this);
            cmd.setCount((IntToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdBzpopmax(CmdBzpopmaxContext ctx) {
        BZPopMaxRedisCmd cmd = new BZPopMaxRedisCmd();

        List<KeyNameContext> keyNameContexts = ctx.keyName();
        for (KeyNameContext keyNameContext : keyNameContexts) {
            keyNameContext.accept(this);
            cmd.addKeyName((StrToken) this.instStack.pop());
        }

        ctx.timeout.accept(this);
        cmd.setTimeout((IntToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdBzpopmin(CmdBzpopminContext ctx) {
        BZPopMinRedisCmd cmd = new BZPopMinRedisCmd();

        List<KeyNameContext> keyNameContexts = ctx.keyName();
        for (KeyNameContext keyNameContext : keyNameContexts) {
            keyNameContext.accept(this);
            cmd.addKeyName((StrToken) this.instStack.pop());
        }

        ctx.timeout.accept(this);
        cmd.setTimeout((IntToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZadd(CmdZaddContext ctx) {
        ZAddRedisCmd cmd = new ZAddRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        if (ctx.NX() != null) {
            cmd.setNxxx(NxXx.NX);
        } else if (ctx.XX() != null) {
            cmd.setNxxx(NxXx.XX);
        }

        if (ctx.GT() != null) {
            cmd.setGtlt(GtLt.GT);
        } else if (ctx.LT() != null) {
            cmd.setGtlt(GtLt.LT);
        }

        if (ctx.CH() != null) {
            cmd.setChTag(code(new TagToken(), ctx.CH()));
        }

        if (ctx.INCR() != null) {
            cmd.setIncrTag(code(new TagToken(), ctx.INCR()));
        }

        List<ScoreAndMemberOptContext> memberContexts = ctx.scoreAndMemberOpt();
        for (ScoreAndMemberOptContext member : memberContexts) {
            member.score.accept(this);
            IntToken scoreToken = (IntToken) this.instStack.pop();
            member.member().accept(this);
            StrToken memberToken = (StrToken) this.instStack.pop();

            cmd.addMember(new ScoreAndMemberToken(scoreToken, memberToken));
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZcard(CmdZcardContext ctx) {
        ZCardRedisCmd cmd = new ZCardRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZcount(CmdZcountContext ctx) {
        ZCountRedisCmd cmd = new ZCountRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.min().accept(this);
        cmd.setMin((StrIntToken) this.instStack.pop());

        ctx.max().accept(this);
        cmd.setMax((StrIntToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZdiff(CmdZdiffContext ctx) {
        ZDiffRedisCmd cmd = new ZDiffRedisCmd();

        ctx.numkeys.accept(this);
        cmd.setNumKeys((IntToken) this.instStack.pop());

        List<KeyNameContext> keyNameContexts = ctx.keyName();
        for (KeyNameContext keyNameContext : keyNameContexts) {
            keyNameContext.accept(this);
            cmd.addKeyName((StrToken) this.instStack.pop());
        }

        cmd.setWithScores(ctx.WITHSCORES() != null);

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZdiffStore(CmdZdiffStoreContext ctx) {
        ZDiffStoreRedisCmd cmd = new ZDiffStoreRedisCmd();

        ctx.dst.accept(this);
        cmd.setDst((StrToken) this.instStack.pop());

        ctx.numkeys.accept(this);
        cmd.setNumKeys((IntToken) this.instStack.pop());

        List<MemberContext> memberContexts = ctx.member();
        for (MemberContext memberContext : memberContexts) {
            memberContext.accept(this);
            cmd.addKeyName((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZincrby(CmdZincrbyContext ctx) {
        ZIncrByRedisCmd cmd = new ZIncrByRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.increment.accept(this);
        cmd.setIncrement((IntToken) this.instStack.pop());

        ctx.member().accept(this);
        cmd.setMember((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZinter(CmdZinterContext ctx) {
        ZInterRedisCmd cmd = new ZInterRedisCmd();

        ctx.numkeys.accept(this);
        cmd.setNumKeys((IntToken) this.instStack.pop());

        List<KeyNameContext> keyNameContexts = ctx.keyName();
        for (KeyNameContext keyNameContext : keyNameContexts) {
            keyNameContext.accept(this);
            cmd.addKeyName((StrToken) this.instStack.pop());
        }

        List<WeightContext> weightContexts = ctx.weight();
        if (weightContexts != null) {
            for (WeightContext weightContext : weightContexts) {
                weightContext.accept(this);
                cmd.addWeight((IntToken) this.instStack.pop());
            }
        }

        if (ctx.SUM() != null) {
            cmd.setAggregateType(AggregateType.SUM);
        } else if (ctx.MIN() != null) {
            cmd.setAggregateType(AggregateType.MIN);
        } else if (ctx.MAX() != null) {
            cmd.setAggregateType(AggregateType.MAX);
        }

        cmd.setWithScores(ctx.WITHSCORES() != null);

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZintercard(CmdZintercardContext ctx) {
        ZInterCardRedisCmd cmd = new ZInterCardRedisCmd();

        ctx.numkeys.accept(this);
        cmd.setNumKeys((IntToken) this.instStack.pop());

        List<KeyNameContext> keyNameContexts = ctx.keyName();
        for (KeyNameContext keyNameContext : keyNameContexts) {
            keyNameContext.accept(this);
            cmd.addKeyName((StrToken) this.instStack.pop());
        }

        if (ctx.limit != null) {
            ctx.limit.accept(this);
            cmd.setLimit((IntToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZinterstore(CmdZinterstoreContext ctx) {
        ZInterStoreRedisCmd cmd = new ZInterStoreRedisCmd();

        ctx.dst.accept(this);
        cmd.setDst((StrToken) this.instStack.pop());

        ctx.numkeys.accept(this);
        cmd.setNumKeys((IntToken) this.instStack.pop());

        List<MemberContext> memberContexts = ctx.member();
        for (MemberContext memberContext : memberContexts) {
            memberContext.accept(this);
            cmd.addKeyName((StrToken) this.instStack.pop());
        }

        List<WeightContext> weightContexts = ctx.weight();
        if (weightContexts != null) {
            for (WeightContext weightContext : weightContexts) {
                weightContext.accept(this);
                cmd.addWeight((IntToken) this.instStack.pop());
            }
        }

        if (ctx.SUM() != null) {
            cmd.setAggregateType(AggregateType.SUM);
        } else if (ctx.MIN() != null) {
            cmd.setAggregateType(AggregateType.MIN);
        } else if (ctx.MAX() != null) {
            cmd.setAggregateType(AggregateType.MAX);
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZLexCount(CmdZLexCountContext ctx) {
        ZLexCountRedisCmd cmd = new ZLexCountRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.min().accept(this);
        cmd.setMin((StrIntToken) this.instStack.pop());

        ctx.max().accept(this);
        cmd.setMax((StrIntToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZmpop(CmdZmpopContext ctx) {
        ZMPopRedisCmd cmd = new ZMPopRedisCmd();

        ctx.numkeys.accept(this);
        cmd.setNumKeys((IntToken) this.instStack.pop());

        List<KeyNameContext> keyNameContexts = ctx.keyName();
        for (KeyNameContext keyNameContext : keyNameContexts) {
            keyNameContext.accept(this);
            cmd.addKeyName((StrToken) this.instStack.pop());
        }

        if (ctx.MIN() != null) {
            cmd.setMinMax(MinMax.MIN);
        } else if (ctx.MAX() != null) {
            cmd.setMinMax(MinMax.MAX);
        } else {
            throw new UnsupportedOperationException("BZMPOP need min or max");
        }

        if (ctx.count() != null) {
            ctx.count().accept(this);
            cmd.setCount((IntToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZmscore(CmdZmscoreContext ctx) {
        ZMScoreRedisCmd cmd = new ZMScoreRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        List<FieldNameContext> fieldContexts = ctx.fieldName();
        for (FieldNameContext fieldContext : fieldContexts) {
            fieldContext.accept(this);
            cmd.addField((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZpopmax(CmdZpopmaxContext ctx) {
        ZPopMaxRedisCmd cmd = new ZPopMaxRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        if (ctx.count() != null) {
            ctx.count().accept(this);
            cmd.setCount((IntToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZpopmin(CmdZpopminContext ctx) {
        ZPopMinRedisCmd cmd = new ZPopMinRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        if (ctx.count() != null) {
            ctx.count().accept(this);
            cmd.setCount((IntToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZrandmember(CmdZrandmemberContext ctx) {
        ZRAndMemberRedisCmd cmd = new ZRAndMemberRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        if (ctx.count() != null) {
            ctx.count().accept(this);
            cmd.setCount((IntToken) this.instStack.pop());
        }
        cmd.setWithScores(ctx.WITHSCORES() != null);

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZrange(CmdZrangeContext ctx) {
        ZRangeRedisCmd cmd = new ZRangeRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.start.accept(this);
        cmd.setStart((StrIntToken) this.instStack.pop());

        ctx.end.accept(this);
        cmd.setEnd((StrIntToken) this.instStack.pop());

        if (ctx.BYSCORE() != null) {
            cmd.setScoreLex(ScoreLex.BYSCORE);
        } else if (ctx.BYLEX() != null) {
            cmd.setScoreLex(ScoreLex.BYLEX);
        }

        if (ctx.REV() != null) {
            cmd.setRev(true);
        }

        if (ctx.limitOpt() != null) {
            ctx.limitOpt().accept(this);
            LimitToken limitToken = (LimitToken) instStack.pop();
            cmd.setLimitOffset(limitToken.getOffsetToken());
            cmd.setLimitCount(limitToken.getCountToken());
        }

        if (ctx.WITHSCORES() != null) {
            cmd.setWithScores(true);
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZrangebylex(CmdZrangebylexContext ctx) {
        ZRangeByLexRedisCmd cmd = new ZRangeByLexRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.min().accept(this);
        cmd.setMin((StrIntToken) this.instStack.pop());

        ctx.max().accept(this);
        cmd.setMax((StrIntToken) this.instStack.pop());

        if (ctx.limitOpt() != null) {
            ctx.limitOpt().accept(this);
            LimitToken limitToken = (LimitToken) instStack.pop();
            cmd.setLimitOffset(limitToken.getOffsetToken());
            cmd.setLimitCount(limitToken.getCountToken());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZrangebyscore(CmdZrangebyscoreContext ctx) {
        ZRangeByScoreRedisCmd cmd = new ZRangeByScoreRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.min().accept(this);
        cmd.setMin((StrIntToken) this.instStack.pop());

        ctx.max().accept(this);
        cmd.setMax((StrIntToken) this.instStack.pop());

        if (ctx.WITHSCORES() != null) {
            cmd.setWithScores(true);
        }

        if (ctx.limitOpt() != null) {
            ctx.limitOpt().accept(this);
            LimitToken limitToken = (LimitToken) instStack.pop();
            cmd.setLimitOffset(limitToken.getOffsetToken());
            cmd.setLimitCount(limitToken.getCountToken());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZrangestore(CmdZrangestoreContext ctx) {
        ZRangeStoreRedisCmd cmd = new ZRangeStoreRedisCmd();

        ctx.dst.accept(this);
        cmd.setDstKey((StrToken) this.instStack.pop());
        ctx.src.accept(this);
        cmd.setSrcName((StrToken) this.instStack.pop());

        ctx.min().accept(this);
        cmd.setMin((StrIntToken) this.instStack.pop());

        ctx.max().accept(this);
        cmd.setMax((StrIntToken) this.instStack.pop());

        if (ctx.BYSCORE() != null) {
            cmd.setScoreLex(ScoreLex.BYSCORE);
        } else if (ctx.BYLEX() != null) {
            cmd.setScoreLex(ScoreLex.BYLEX);
        }

        if (ctx.REV() != null) {
            cmd.setRev(true);
        }

        if (ctx.limitOpt() != null) {
            ctx.limitOpt().accept(this);
            LimitToken limitToken = (LimitToken) instStack.pop();
            cmd.setLimitOffset(limitToken.getOffsetToken());
            cmd.setLimitCount(limitToken.getCountToken());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZrank(CmdZrankContext ctx) {
        ZRankRedisCmd cmd = new ZRankRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.member().accept(this);
        cmd.setMember((StrToken) this.instStack.pop());

        if (ctx.WITHSCORE() != null) {
            cmd.setWithScore(true);
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZRem(CmdZRemContext ctx) {
        ZRemRedisCmd cmd = new ZRemRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        List<FieldNameContext> fieldContexts = ctx.fieldName();
        for (FieldNameContext fieldContext : fieldContexts) {
            fieldContext.accept(this);
            cmd.addField((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZRemRangeByLex(CmdZRemRangeByLexContext ctx) {
        ZRemRangeByLexRedisCmd cmd = new ZRemRangeByLexRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.min().accept(this);
        cmd.setMin((StrIntToken) this.instStack.pop());

        ctx.max().accept(this);
        cmd.setMax((StrIntToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZremrangebyrank(CmdZremrangebyrankContext ctx) {
        ZRemRangeByRankRedisCmd cmd = new ZRemRangeByRankRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.startNum.accept(this);
        cmd.setStart((IntToken) this.instStack.pop());

        ctx.endNum.accept(this);
        cmd.setEnd((IntToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZRemRangeByScore(CmdZRemRangeByScoreContext ctx) {
        ZRemRangeByScoreRedisCmd cmd = new ZRemRangeByScoreRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.min().accept(this);
        cmd.setMin((StrIntToken) this.instStack.pop());

        ctx.max().accept(this);
        cmd.setMax((StrIntToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZrevrange(CmdZrevrangeContext ctx) {
        ZRevRangeRedisCmd cmd = new ZRevRangeRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.startNum.accept(this);
        cmd.setStart((IntToken) this.instStack.pop());

        ctx.endNum.accept(this);
        cmd.setEnd((IntToken) this.instStack.pop());

        if (ctx.WITHSCORES() != null) {
            cmd.setWithScores(true);
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZrevrangebylex(CmdZrevrangebylexContext ctx) {
        ZRevRangeByLexRedisCmd cmd = new ZRevRangeByLexRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.max().accept(this);
        cmd.setMax((StrIntToken) this.instStack.pop());

        ctx.min().accept(this);
        cmd.setMin((StrIntToken) this.instStack.pop());

        if (ctx.limitOpt() != null) {
            ctx.limitOpt().accept(this);
            LimitToken limitToken = (LimitToken) instStack.pop();
            cmd.setLimitOffset(limitToken.getOffsetToken());
            cmd.setLimitCount(limitToken.getCountToken());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZrevrangebyscore(CmdZrevrangebyscoreContext ctx) {
        ZRevRangeByScoreRedisCmd cmd = new ZRevRangeByScoreRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.max().accept(this);
        cmd.setMax((StrIntToken) this.instStack.pop());

        ctx.min().accept(this);
        cmd.setMin((StrIntToken) this.instStack.pop());

        if (ctx.WITHSCORES() != null) {
            cmd.setWithScores(true);
        }

        if (ctx.limitOpt() != null) {
            ctx.limitOpt().accept(this);
            LimitToken limitToken = (LimitToken) instStack.pop();
            cmd.setLimitOffset(limitToken.getOffsetToken());
            cmd.setLimitCount(limitToken.getCountToken());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZrevrank(CmdZrevrankContext ctx) {
        ZRevRankRedisCmd cmd = new ZRevRankRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.member().accept(this);
        cmd.setMember((StrToken) this.instStack.pop());

        if (ctx.WITHSCORE() != null) {
            cmd.setWithScore(true);
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZscan(CmdZscanContext ctx) {
        ZSCanRedisCmd cmd = new ZSCanRedisCmd();
        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.cursor().accept(this);
        cmd.setCursor((IntToken) this.instStack.pop());

        if (ctx.MATCH() != null) {
            ctx.pattern().accept(this);
            cmd.setPattern((StrToken) this.instStack.pop());
        }

        if (ctx.COUNT() != null) {
            ctx.count().accept(this);
            cmd.setCount((IntToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZscore(CmdZscoreContext ctx) {
        ZScoreRedisCmd cmd = new ZScoreRedisCmd();

        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        ctx.member().accept(this);
        cmd.setMember((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZunion(CmdZunionContext ctx) {
        ZUnionRedisCmd cmd = new ZUnionRedisCmd();

        ctx.numkeys.accept(this);
        cmd.setNumKeys((IntToken) this.instStack.pop());

        List<KeyNameContext> keyNameContexts = ctx.keyName();
        for (KeyNameContext keyNameContext : keyNameContexts) {
            keyNameContext.accept(this);
            cmd.addKeyName((StrToken) this.instStack.pop());
        }

        List<WeightContext> weightContexts = ctx.weight();
        if (weightContexts != null) {
            for (WeightContext weightContext : weightContexts) {
                weightContext.accept(this);
                cmd.addWeight((IntToken) this.instStack.pop());
            }
        }

        if (ctx.SUM() != null) {
            cmd.setAggregateType(AggregateType.SUM);
        } else if (ctx.MIN() != null) {
            cmd.setAggregateType(AggregateType.MIN);
        } else if (ctx.MAX() != null) {
            cmd.setAggregateType(AggregateType.MAX);
        }

        cmd.setWithScores(ctx.WITHSCORES() != null);

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdZunionstore(CmdZunionstoreContext ctx) {
        ZUnionStoreRedisCmd cmd = new ZUnionStoreRedisCmd();

        ctx.dst.accept(this);
        cmd.setDst((StrToken) this.instStack.pop());

        ctx.numkeys.accept(this);
        cmd.setNumKeys((IntToken) this.instStack.pop());

        List<MemberContext> memberContexts = ctx.member();
        for (MemberContext memberContext : memberContexts) {
            memberContext.accept(this);
            cmd.addKeyName((StrToken) this.instStack.pop());
        }

        List<WeightContext> weightContexts = ctx.weight();
        if (weightContexts != null) {
            for (WeightContext weightContext : weightContexts) {
                weightContext.accept(this);
                cmd.addWeight((IntToken) this.instStack.pop());
            }
        }

        if (ctx.SUM() != null) {
            cmd.setAggregateType(AggregateType.SUM);
        } else if (ctx.MIN() != null) {
            cmd.setAggregateType(AggregateType.MIN);
        } else if (ctx.MAX() != null) {
            cmd.setAggregateType(AggregateType.MAX);
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    /* ----------------------------------------------------------------------------------- script commands */

    @Override
    public T visitCmdScriptDebug(CmdScriptDebugContext ctx) {
        throw new UnsupportedOperationException("SCRIPT DEBUG Unsupported.");
    }

    @Override
    public T visitCmdScriptExists(CmdScriptExistsContext ctx) {
        throw new UnsupportedOperationException("SCRIPT EXISTS Unsupported.");
    }

    @Override
    public T visitCmdScriptFlush(CmdScriptFlushContext ctx) {
        throw new UnsupportedOperationException("SCRIPT FLUSH Unsupported.");
    }

    @Override
    public T visitCmdScriptKill(CmdScriptKillContext ctx) {
        throw new UnsupportedOperationException("SCRIPT KILL Unsupported.");
    }

    @Override
    public T visitCmdScriptLoad(CmdScriptLoadContext ctx) {
        throw new UnsupportedOperationException("SCRIPT LOAD Unsupported.");
    }

    @Override
    public T visitCmdEval(CmdEvalContext ctx) {
        throw new UnsupportedOperationException("EVAL Unsupported.");
    }

    @Override
    public T visitCmdEvalRO(CmdEvalROContext ctx) {
        throw new UnsupportedOperationException("EVAL_RO Unsupported.");
    }

    @Override
    public T visitCmdEvalsha(CmdEvalshaContext ctx) {
        throw new UnsupportedOperationException("EVALSHA Unsupported.");
    }

    @Override
    public T visitCmdEvalshaRO(CmdEvalshaROContext ctx) {
        throw new UnsupportedOperationException("EVALSHA_RO Unsupported.");
    }

    @Override
    public T visitCmdFCall(CmdFCallContext ctx) {
        throw new UnsupportedOperationException("FCALL Unsupported.");
    }

    @Override
    public T visitCmdFCallRO(CmdFCallROContext ctx) {
        throw new UnsupportedOperationException("FCALL_RO Unsupported.");
    }

    @Override
    public T visitCmdFunctionDel(CmdFunctionDelContext ctx) {
        throw new UnsupportedOperationException("FUNCTION DELETE Unsupported.");
    }

    @Override
    public T visitCmdFunctionDump(CmdFunctionDumpContext ctx) {
        throw new UnsupportedOperationException("FUNCTION DUMP Unsupported.");
    }

    @Override
    public T visitCmdFunctionFlush(CmdFunctionFlushContext ctx) {
        throw new UnsupportedOperationException("FUNCTION FLUSH Unsupported.");
    }

    @Override
    public T visitCmdFunctionKill(CmdFunctionKillContext ctx) {
        throw new UnsupportedOperationException("FUNCTION KILL Unsupported.");
    }

    @Override
    public T visitCmdFunctionList(CmdFunctionListContext ctx) {
        throw new UnsupportedOperationException("FUNCTION LIST Unsupported.");
    }

    @Override
    public T visitCmdFunctionLoad(CmdFunctionLoadContext ctx) {
        throw new UnsupportedOperationException("FUNCTION LOAD Unsupported.");
    }

    @Override
    public T visitCmdFunctionRestore(CmdFunctionRestoreContext ctx) {
        throw new UnsupportedOperationException("FUNCTION RESTORE Unsupported.");
    }

    @Override
    public T visitCmdFunctionStats(CmdFunctionStatsContext ctx) {
        throw new UnsupportedOperationException("FUNCTION STATS Unsupported.");
    }

    /* ----------------------------------------------------------------------------------- tx commands */

    @Override
    public T visitCmdDiscard(CmdDiscardContext ctx) {
        DiscardRedisCmd cmd = code(new DiscardRedisCmd(), ctx);

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdExec(CmdExecContext ctx) {
        ExecRedisCmd cmd = code(new ExecRedisCmd(), ctx);

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdMulti(CmdMultiContext ctx) {
        MultiRedisCmd cmd = code(new MultiRedisCmd(), ctx);

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdUnwatch(CmdUnwatchContext ctx) {
        UnwatchRedisCmd cmd = code(new UnwatchRedisCmd(), ctx);

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdWatch(CmdWatchContext ctx) {
        WatchRedisCmd cmd = code(new WatchRedisCmd(), ctx);

        List<KeyNameContext> keyList = ctx.keyName();
        for (KeyNameContext key : keyList) {
            key.accept(this);
            cmd.addKey((StrToken) this.instStack.pop());
        }

        this.instStack.push(cmd);
        return null;
    }

    /* ----------------------------------------------------------------------------------- HyperLog commands */

    @Override
    public T visitCmdPFAdd(CmdPFAddContext ctx) {
        PFAddRedisCmd cmd = new PFAddRedisCmd();
        ctx.keyName().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        for (ElementContext context : ctx.element()) {
            context.accept(this);
            cmd.addElement((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdPFCount(CmdPFCountContext ctx) {
        PFCountRedisCmd cmd = new PFCountRedisCmd();

        for (KeyNameContext context : ctx.keyName()) {
            context.accept(this);
            cmd.addKey((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdPFMerge(CmdPFMergeContext ctx) {
        PFMergeRedisCmd cmd = new PFMergeRedisCmd();
        ctx.keyName().accept(this);
        cmd.setDst((StrToken) this.instStack.pop());

        for (SrcKeyContext context : ctx.srcKey()) {
            context.accept(this);
            cmd.addSrcKey((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    /* ----------------------------------------------------------------------------------- publish commands */

    @Override
    public T visitCmdPSubscribe(CmdPSubscribeContext ctx) {
        PSubscribeRedisCmd cmd = new PSubscribeRedisCmd();

        List<PatternContext> patternContexts = ctx.pattern();
        for (PatternContext patternContext : patternContexts) {
            patternContext.accept(this);
            cmd.addPattern((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdPublish(CmdPublishContext ctx) {
        ctx.channel().accept(this);
        StrToken channel = (StrToken) this.instStack.pop();

        ctx.message.accept(this);
        StrToken message = (StrToken) this.instStack.pop();

        PublishRedisCmd cmd = code(new PublishRedisCmd(channel, message), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdPubSubChannels(CmdPubSubChannelsContext ctx) {
        PubSubChannelsRedisCmd cmd = new PubSubChannelsRedisCmd();

        if (ctx.pattern() != null) {
            ctx.pattern().accept(this);
            cmd.setPattern((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdPubSubNumPat(CmdPubSubNumPatContext ctx) {
        PubSubNumPatRedisCmd cmd = code(new PubSubNumPatRedisCmd(), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdPubSubNumSub(CmdPubSubNumSubContext ctx) {
        PubSubNumSubRedisCmd cmd = code(new PubSubNumSubRedisCmd(), ctx);

        List<ChannelContext> channelList = ctx.channel();
        if (channelList != null) {
            for (ChannelContext channelContext : channelList) {
                channelContext.accept(this);
                cmd.addChannel((StrToken) this.instStack.pop());
            }
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdPubSubShardChannels(CmdPubSubShardChannelsContext ctx) {
        PubSubShardChannelsRedisCmd cmd = new PubSubShardChannelsRedisCmd();

        if (ctx.pattern() != null) {
            ctx.pattern().accept(this);
            cmd.setPattern((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdPubSubShardNumSub(CmdPubSubShardNumSubContext ctx) {
        PubSubShardNumSubRedisCmd cmd = code(new PubSubShardNumSubRedisCmd(), ctx);

        List<ChannelContext> channelList = ctx.channel();
        if (channelList != null) {
            for (ChannelContext channelContext : channelList) {
                channelContext.accept(this);
                cmd.addChannel((StrToken) this.instStack.pop());
            }
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdPunSubscribe(CmdPunSubscribeContext ctx) {
        PunSubscribeRedisCmd cmd = new PunSubscribeRedisCmd();

        List<PatternContext> patternContexts = ctx.pattern();
        if (patternContexts != null) {
            for (PatternContext patternContext : patternContexts) {
                patternContext.accept(this);
                cmd.addPattern((StrToken) this.instStack.pop());
            }
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSpublish(CmdSpublishContext ctx) {
        SPublishRedisCmd cmd = new SPublishRedisCmd();

        ctx.channel().accept(this);
        cmd.setChannel((StrToken) this.instStack.pop());

        ctx.message.accept(this);
        cmd.setMessage((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSSubscribe(CmdSSubscribeContext ctx) {
        SSubscribeRedisCmd cmd = new SSubscribeRedisCmd();

        List<ChannelContext> channelContexts = ctx.channel();
        if (channelContexts != null) {
            for (ChannelContext channelContext : channelContexts) {
                channelContext.accept(this);
                cmd.addChannel((StrToken) this.instStack.pop());
            }
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSubscribe(CmdSubscribeContext ctx) {
        SubscribeRedisCmd cmd = new SubscribeRedisCmd();

        List<ChannelContext> channelContexts = ctx.channel();
        if (channelContexts != null) {
            for (ChannelContext channelContext : channelContexts) {
                channelContext.accept(this);
                cmd.addChannel((StrToken) this.instStack.pop());
            }
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSunSubscribe(CmdSunSubscribeContext ctx) {
        SunSubscribeRedisCmd cmd = new SunSubscribeRedisCmd();

        List<ChannelContext> channelContexts = ctx.channel();
        if (channelContexts != null) {
            for (ChannelContext channelContext : channelContexts) {
                channelContext.accept(this);
                cmd.addChannel((StrToken) this.instStack.pop());
            }
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdUnSubScribe(CmdUnSubScribeContext ctx) {
        UnSubScribeRedisCmd cmd = new UnSubScribeRedisCmd();

        List<ChannelContext> channelContexts = ctx.channel();
        if (channelContexts != null) {
            for (ChannelContext channelContext : channelContexts) {
                channelContext.accept(this);
                cmd.addChannel((StrToken) this.instStack.pop());
            }
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    /* ----------------------------------------------------------------------------------- cluster commands */

    @Override
    public T visitCmdAsking(CmdAskingContext ctx) {
        AskingRedisCmd cmd = code(new AskingRedisCmd(), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdReadonly(CmdReadonlyContext ctx) {
        ReadOnlyRedisCmd cmd = code(new ReadOnlyRedisCmd(), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdReadWrite(CmdReadWriteContext ctx) {
        ReadWriteRedisCmd cmd = code(new ReadWriteRedisCmd(), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdClusterAddSlots(CmdClusterAddSlotsContext ctx) {
        ClusterAddSlotsRedisCmd cmd = new ClusterAddSlotsRedisCmd();

        List<SlotContext> slotContexts = ctx.slot();
        for (SlotContext slotContext : slotContexts) {
            slotContext.accept(this);
            cmd.addSlot((IntToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.add(cmd);
        return null;
    }

    @Override
    public T visitCmdClusterDelSlots(CmdClusterDelSlotsContext ctx) {
        ClusterDelSlotsRedisCmd cmd = new ClusterDelSlotsRedisCmd();

        List<SlotContext> slotContexts = ctx.slot();
        for (SlotContext slotContext : slotContexts) {
            slotContext.accept(this);
            cmd.addSlot((IntToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.add(cmd);
        return null;
    }

    @Override
    public T visitCmdClusterAddSlotsRange(CmdClusterAddSlotsRangeContext ctx) {
        AddSlotsRangeRedisCmd cmd = new AddSlotsRangeRedisCmd();

        List<ClusterStartAndEndContext> startAndEndCtxList = ctx.clusterStartAndEnd();
        for (int i = 0; i < startAndEndCtxList.size(); i++) {
            for (int j = i; j <= i; j++) {
                ClusterStartAndEndContext startAndEndCtx = startAndEndCtxList.get(i);

                startAndEndCtx.start.accept(this);
                IntToken startToken = (IntToken) this.instStack.pop();

                startAndEndCtx.end.accept(this);
                IntToken endToken = (IntToken) this.instStack.pop();

                cmd.addRange(startToken, endToken);
            }
        }

        cmd = code(cmd, ctx);
        this.instStack.add(cmd);
        return null;
    }

    @Override
    public T visitCmdClusterDelSlotsRange(CmdClusterDelSlotsRangeContext ctx) {
        DelSlotsRangeRedisCmd cmd = new DelSlotsRangeRedisCmd();

        List<ClusterStartAndEndContext> startAndEndCtxList = ctx.clusterStartAndEnd();
        for (int i = 0; i < startAndEndCtxList.size(); i++) {
            for (int j = i; j <= i; j++) {
                ClusterStartAndEndContext startAndEndCtx = startAndEndCtxList.get(i);

                startAndEndCtx.start.accept(this);
                IntToken startToken = (IntToken) this.instStack.pop();

                startAndEndCtx.end.accept(this);
                IntToken endToken = (IntToken) this.instStack.pop();

                cmd.addRange(startToken, endToken);
            }
        }

        cmd = code(cmd, ctx);
        this.instStack.add(cmd);
        return null;
    }

    @Override
    public T visitClusterStartAndEnd(ClusterStartAndEndContext ctx) {
        throw new UnsupportedOperationException("impossible");
    }

    @Override
    public T visitCmdClusterBumpEpoch(CmdClusterBumpEpochContext ctx) {
        ClusterBumpEpochRedisCmd redisCmd = code(new ClusterBumpEpochRedisCmd(), ctx);
        this.instStack.push(redisCmd);
        return null;
    }

    @Override
    public T visitCmdClusterCountFailureReports(CmdClusterCountFailureReportsContext ctx) {
        ClusterCountFailureReportsRedisCmd cmd = new ClusterCountFailureReportsRedisCmd();

        ctx.nodeId.accept(this);
        cmd.setNodeId((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdClusterCountKeysInSlot(CmdClusterCountKeysInSlotContext ctx) {
        CountKeysInSlotRedisCmd cmd = new CountKeysInSlotRedisCmd();

        ctx.slot().accept(this);
        cmd.setSlot((IntToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdClusterFailOver(CmdClusterFailOverContext ctx) {
        ClusterFailOverRedisCmd cmd = new ClusterFailOverRedisCmd();

        if (ctx.FORCE() != null) {
            cmd.setType(ClusterFailOverRedisCmd.Type.FORCE);
        } else if (ctx.TAKEOVER() != null) {
            cmd.setType(ClusterFailOverRedisCmd.Type.TAKEOVER);
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdClusterFlushSlots(CmdClusterFlushSlotsContext ctx) {
        ClusterFlushSlotsRedisCmd cmd = code(new ClusterFlushSlotsRedisCmd(), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdClusterForget(CmdClusterForgetContext ctx) {
        ClusterForgetRedisCmd cmd = new ClusterForgetRedisCmd();

        ctx.nodeId.accept(this);
        cmd.setNodeId((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdClusterGetKeysInSlot(CmdClusterGetKeysInSlotContext ctx) {
        ClusterGetKeysInSlotRedisCmd cmd = new ClusterGetKeysInSlotRedisCmd();

        ctx.slot().accept(this);
        cmd.setSlot((IntToken) this.instStack.pop());

        ctx.count().accept(this);
        cmd.setCount((IntToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdClusterInfo(CmdClusterInfoContext ctx) {
        ClusterInfoRedisCmd cmd = code(new ClusterInfoRedisCmd(), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdClusterKeySlot(CmdClusterKeySlotContext ctx) {
        ClusterKeySlotRedisCmd cmd = new ClusterKeySlotRedisCmd();

        ctx.key().accept(this);
        cmd.setKeyName((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdClusterLinks(CmdClusterLinksContext ctx) {
        ClusterLinksRedisCmd cmd = code(new ClusterLinksRedisCmd(), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdClusterMeet(CmdClusterMeetContext ctx) {
        ClusterMeetRedisCmd cmd = new ClusterMeetRedisCmd();

        String ipText = ctx.ip.getText();
        cmd.setIp(code(StrToken.of(ipText, false), ctx.ip));

        ctx.port().accept(this);
        cmd.setPort((IntToken) this.instStack.pop());

        if (ctx.clusterBusPort != null) {
            ctx.clusterBusPort.accept(this);
            cmd.setClusterBusPort((IntToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdClusterMyId(CmdClusterMyIdContext ctx) {
        ClusterMyIdRedisCmd cmd = code(new ClusterMyIdRedisCmd(), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdClusterMyShardId(CmdClusterMyShardIdContext ctx) {
        ClusterMyShardIdRedisCmd cmd = code(new ClusterMyShardIdRedisCmd(), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdClusterNodes(CmdClusterNodesContext ctx) {
        ClusterNodesRedisCmd cmd = code(new ClusterNodesRedisCmd(), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdClusterReplicas(CmdClusterReplicasContext ctx) {
        ClusterReplicasRedisCmd cmd = new ClusterReplicasRedisCmd();

        ctx.nodeId.accept(this);
        cmd.setNodeId((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdClusterReplicate(CmdClusterReplicateContext ctx) {
        ClusterReplicateRedisCmd cmd = new ClusterReplicateRedisCmd();

        ctx.nodeId.accept(this);
        cmd.setNodeId((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdClusterReset(CmdClusterResetContext ctx) {
        ClusterResetRedisCmd cmd = new ClusterResetRedisCmd();

        if (ctx.HARD() != null) {
            cmd.setType(ClusterResetRedisCmd.Type.HARD);
        } else if (ctx.SOFT() != null) {
            cmd.setType(ClusterResetRedisCmd.Type.SOFT);
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdClusterSaveConfig(CmdClusterSaveConfigContext ctx) {
        ClusterSaveConfigRedisCmd cmd = code(new ClusterSaveConfigRedisCmd(), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdClusterSetConfigEpoch(CmdClusterSetConfigEpochContext ctx) {
        ClusterSetConfigEpochRedisCmd cmd = new ClusterSetConfigEpochRedisCmd();

        ctx.configEpoch.accept(this);
        cmd.setConfigEpoch((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdClusterSetSlot(CmdClusterSetSlotContext ctx) {
        ClusterSetSlotRedisCmd cmd = new ClusterSetSlotRedisCmd();

        ctx.slot().accept(this);
        cmd.setSlot((IntToken) this.instStack.pop());

        StrToken keyNameToken = null;
        if (ctx.IMPORTING() != null) {
            ctx.nodeId.accept(this);
            cmd.setNodeId((StrToken) this.instStack.pop());
            cmd.setType(ClusterSetSlotRedisCmd.Type.IMPORTING);
        } else if (ctx.MIGRATING() != null) {
            ctx.nodeId.accept(this);
            cmd.setNodeId((StrToken) this.instStack.pop());
            cmd.setType(ClusterSetSlotRedisCmd.Type.MIGRATING);
        } else if (ctx.NODE() != null) {
            ctx.nodeId.accept(this);
            cmd.setNodeId((StrToken) this.instStack.pop());
            cmd.setType(ClusterSetSlotRedisCmd.Type.NODE);
        } else if (ctx.STABLE() != null) {
            cmd.setType(ClusterSetSlotRedisCmd.Type.STABLE);
        } else {
            throw new UnsupportedOperationException("impossible");
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdClusterShards(CmdClusterShardsContext ctx) {
        ClusterShardsRedisCmd cmd = code(new ClusterShardsRedisCmd(), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdClusterSlaves(CmdClusterSlavesContext ctx) {
        ClusterSlavesRedisCmd cmd = new ClusterSlavesRedisCmd();

        ctx.nodeId.accept(this);
        cmd.setNodeId((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdClusterSlotStats(CmdClusterSlotStatsContext ctx) {
        ClusterSlotStatsRedisCmd cmd = new ClusterSlotStatsRedisCmd();

        if (ctx.startNum != null && ctx.endNum != null) {
            ctx.startNum.accept(this);
            cmd.setStartNum((IntToken) this.instStack.pop());
            ctx.endNum.accept(this);
            cmd.setEndNum((IntToken) this.instStack.pop());
        }

        if (ctx.metric != null) {
            String metricText = ctx.metric.getText();
            cmd.setMetric(ClusterSlotStatsRedisCmd.MetricType.valueOf(String.valueOf(metricText)));
        }

        if (ctx.limit != null) {
            ctx.limit.accept(this);
            cmd.setLimit((IntToken) instStack.pop());
        }

        if (ctx.ASC() != null) {
            cmd.setOrder(OrderType.ASC);
        } else if (ctx.DESC() != null) {
            cmd.setOrder(OrderType.DESC);
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdClusterSlots(CmdClusterSlotsContext ctx) {
        ClusterSlotsRedisCmd cmd = code(new ClusterSlotsRedisCmd(), ctx);
        this.instStack.push(cmd);
        return null;
    }

    /* ----------------------------------------------------------------------------------- info commands */

    @Override
    public T visitCmdInfo(CmdInfoContext ctx) {
        InfoRedisCmd cmd = new InfoRedisCmd();

        if (ctx.infoOpt() != null) {
            for (InfoOptContext info : ctx.infoOpt()) {
                cmd.addType(InfoRedisCmd.InfoType.valueOf(info.getText().toUpperCase()));
            }
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    /* ----------------------------------------------------------------------------------- acl commands */

    @Override
    public T visitCmdAclCat(CmdAclCatContext ctx) {
        AclCatRedisCmd cmd = new AclCatRedisCmd();

        if (ctx.category != null) {
            ctx.category.accept(this);
            cmd.setCategory((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdAclDelUser(CmdAclDelUserContext ctx) {
        AclDelUserRedisCmd cmd = new AclDelUserRedisCmd();

        List<UsernameContext> usernameContexts = ctx.username();
        if (usernameContexts != null) {
            for (UsernameContext usernameContext : usernameContexts) {
                usernameContext.accept(this);
                cmd.addUser((StrToken) this.instStack.pop());
            }
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdAclDryRun(CmdAclDryRunContext ctx) {
        AclDryRunRedisCmd cmd = new AclDryRunRedisCmd();

        List<UsernameContext> usernameContexts = ctx.username();
        if (usernameContexts != null) {
            for (UsernameContext usernameContext : usernameContexts) {
                usernameContext.accept(this);
                cmd.addUser((StrToken) this.instStack.pop());
            }
        }
        List<CommandContext> commandContexts = ctx.command();
        if (commandContexts != null) {
            for (CommandContext commandContext : commandContexts) {
                String cmdName = commandContext.keyWordValue().getText();
                cmd.addCommand(StrToken.of(cmdName, false));
            }
        }
        List<ArgContext> argContexts = ctx.arg();
        if (argContexts != null) {
            for (ArgContext argContext : argContexts) {
                argContext.accept(this);
                cmd.addArg((StrToken) this.instStack.pop());
            }
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdAclGenPass(CmdAclGenPassContext ctx) {
        AclGenPassRedisCmd cmd = new AclGenPassRedisCmd();

        if (ctx.bits != null) {
            ctx.bits.accept(this);
            cmd.setBits((IntToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdAclGetUser(CmdAclGetUserContext ctx) {
        AclGetUserRedisCmd cmd = new AclGetUserRedisCmd();

        ctx.username().accept(this);
        cmd.setUsername((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdAclList(CmdAclListContext ctx) {
        AclListRedisCmd cmd = new AclListRedisCmd();
        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdAclLoad(CmdAclLoadContext ctx) {
        AclLoadRedisCmd cmd = new AclLoadRedisCmd();
        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdAclLog(CmdAclLogContext ctx) {
        AclLogRedisCmd cmd = new AclLogRedisCmd();
        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdAclSave(CmdAclSaveContext ctx) {
        AclSaveRedisCmd cmd = new AclSaveRedisCmd();
        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdAclSetUser(CmdAclSetUserContext ctx) {
        AclSetUserRedisCmd cmd = new AclSetUserRedisCmd();

        ctx.username().accept(this);
        cmd.setUsername((StrToken) this.instStack.pop());

        List<AclCmdRuleContext> ruleContexts = ctx.aclCmdRule();
        for (AclCmdRuleContext aclCmdRuleContext : ruleContexts) {
            aclCmdRuleContext.accept(this);
            cmd.addRule((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdAclUsers(CmdAclUsersContext ctx) {
        AclUsersRedisCmd cmd = new AclUsersRedisCmd();
        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdAclWhoami(CmdAclWhoamiContext ctx) {
        AclWhoamiRedisCmd cmd = new AclWhoamiRedisCmd();
        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    /* ----------------------------------------------------------------------------------- command commands */

    @Override
    public T visitCmdCommand(CmdCommandContext ctx) {
        CommandRedisCmd cmd = new CommandRedisCmd();
        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdCommandCount(CmdCommandCountContext ctx) {
        CommandCountRedisCmd cmd = new CommandCountRedisCmd();
        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdCommandDocs(CmdCommandDocsContext ctx) {
        CommandDocsRedisCmd cmd = new CommandDocsRedisCmd();

        List<CommandContext> commands = ctx.command();
        if (commands != null) {
            for (CommandContext commandContext : commands) {
                String cmdName = commandContext.keyWordValue().getText();
                cmd.addCommand(StrToken.of(cmdName, false));
            }
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdCommandGetKeys(CmdCommandGetKeysContext ctx) {
        CommandGetKeysRedisCmd cmd = new CommandGetKeysRedisCmd();

        String cmdName = ctx.command().getText();
        cmd.setCommand(StrToken.of(cmdName, false));

        List<ArgContext> args = ctx.arg();
        if (args != null) {
            for (ArgContext argContext : args) {
                argContext.accept(this);
                cmd.addArg((StrToken) this.instStack.pop());
            }
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdCommandGetKeysAndFlags(CmdCommandGetKeysAndFlagsContext ctx) {
        CommandGetKeysAndFlagsRedisCmd cmd = new CommandGetKeysAndFlagsRedisCmd();

        String cmdName = ctx.command().getText();
        cmd.setCommand(StrToken.of(cmdName, false));

        List<ArgContext> args = ctx.arg();
        if (args != null) {
            for (ArgContext argContext : args) {
                argContext.accept(this);
                cmd.addArg((StrToken) this.instStack.pop());
            }
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdCommandInfo(CmdCommandInfoContext ctx) {
        CommandInfoRedisCmd cmd = new CommandInfoRedisCmd();

        List<CommandContext> commands = ctx.command();
        if (commands != null) {
            for (CommandContext commandContext : commands) {
                String cmdName = commandContext.keyWordValue().getText();
                cmd.addCommand(StrToken.of(cmdName, false));
            }
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdCommandList(CmdCommandListContext ctx) {
        CommandListRedisCmd cmd = new CommandListRedisCmd();

        if (ctx.MODULE() != null) {
            ctx.moduleName.accept(this);
            cmd.setModuleName((StrToken) this.instStack.pop());
        }
        if (ctx.ACLCAT() != null) {
            ctx.category.accept(this);
            cmd.setCategory((StrToken) this.instStack.pop());
        }
        if (ctx.pattern() != null) {
            ctx.pattern().accept(this);
            cmd.setPattern((StrToken) this.instStack.pop());
        }

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    /* ----------------------------------------------------------------------------------- config commands */

    @Override
    public T visitCmdConfigGet(CmdConfigGetContext ctx) {
        ConfigGetCmd cmd = code(new ConfigGetCmd(), ctx);

        List<PatternContext> patternContexts = ctx.pattern();
        for (PatternContext patternContext : patternContexts) {
            patternContext.accept(this);
            StrToken name = (StrToken) this.instStack.pop();
            cmd.addPattern(name);
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdConfigSet(CmdConfigSetContext ctx) {
        ConfigSetCmd cmd = code(new ConfigSetCmd(), ctx);

        ctx.paramName.accept(this);
        cmd.setConfigKey((StrToken) instStack.pop());

        ctx.paramValue.accept(this);
        cmd.setConfigValue((StrToken) instStack.pop());

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdConfigResetStat(CmdConfigResetStatContext ctx) {
        ConfigResetStatCmd cmd = code(new ConfigResetStatCmd(), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdConfigRewrite(CmdConfigRewriteContext ctx) {
        ConfigRewriteCmd cmd = code(new ConfigRewriteCmd(), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitEvent(EventContext ctx) {
        return this.visitChildren(ctx);
    }

    /* ----------------------------------------------------------------------------------- latency commands */

    @Override
    public T visitCmdLatencyDoctor(CmdLatencyDoctorContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdLatencyGraph(CmdLatencyGraphContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdLatencyHistogram(CmdLatencyHistogramContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdLatencyHistory(CmdLatencyHistoryContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdLatencyLatest(CmdLatencyLatestContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdLatencyReset(CmdLatencyResetContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    /* ----------------------------------------------------------------------------------- memory commands */

    @Override
    public T visitCmdMemoryDoctor(CmdMemoryDoctorContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdMemoryMallocStats(CmdMemoryMallocStatsContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdMemoryPurge(CmdMemoryPurgeContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdMemoryStats(CmdMemoryStatsContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdMemoryUsage(CmdMemoryUsageContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    /* ----------------------------------------------------------------------------------- module commands */

    @Override
    public T visitConfigKV(ConfigKVContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdModuleList(CmdModuleListContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdModuleLoad(CmdModuleLoadContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdModuleLoadEx(CmdModuleLoadExContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdModuleUnload(CmdModuleUnloadContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    /* ----------------------------------------------------------------------------------- control commands */

    @Override
    public T visitCmdBgrewriteaof(CmdBgrewriteaofContext ctx) {
        BgreWriteAOFRedisCmd cmd = code(new BgreWriteAOFRedisCmd(), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdBgsave(CmdBgsaveContext ctx) {
        BgSaveRedisCmd cmd = code(new BgSaveRedisCmd(), ctx);

        cmd.setSchedule(ctx.SCHEDULE() != null);

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdDbsize(CmdDbsizeContext ctx) {
        DbSizeRedisCmd cmd = code(new DbSizeRedisCmd(), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdFailover(CmdFailoverContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdFlushAll(CmdFlushAllContext ctx) {
        FlushAllRedisCmd cmd = new FlushAllRedisCmd();

        if (ctx.ASYNC() != null) {
            cmd.setType(FlushAllRedisCmd.Type.ASYNC);
        } else if (ctx.SYNC() != null) {
            cmd.setType(FlushAllRedisCmd.Type.SYNC);
        }

        FlushAllRedisCmd redisCmd = code(cmd, ctx);
        this.instStack.push(redisCmd);
        return null;
    }

    @Override
    public T visitCmdFlushDB(CmdFlushDBContext ctx) {
        FlushDBRedisCmd cmd = code(new FlushDBRedisCmd(), ctx);

        if (ctx.SYNC() != null) {
            cmd.setType(FlushDBRedisCmd.Type.SYNC);
        } else if (ctx.ASYNC() != null) {
            cmd.setType(FlushDBRedisCmd.Type.ASYNC);
        }

        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdLastsave(CmdLastsaveContext ctx) {
        LastSaveRedisCmd cmd = code(new LastSaveRedisCmd(), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdLolwut(CmdLolwutContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdMonitor(CmdMonitorContext ctx) {
        MonitorRedisCmd cmd = code(new MonitorRedisCmd(), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdPSync(CmdPSyncContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdReplicaOf(CmdReplicaOfContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdRole(CmdRoleContext ctx) {
        RoleRedisCmd cmd = code(new RoleRedisCmd(), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdSave(CmdSaveContext ctx) {
        SaveRedisCmd redisCmd = code(new SaveRedisCmd(), ctx);
        this.instStack.push(redisCmd);
        return null;
    }

    @Override
    public T visitCmdShutdown(CmdShutdownContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdSlaveOf(CmdSlaveOfContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdSlowlogGet(CmdSlowlogGetContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdSlowlogLen(CmdSlowlogLenContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdSlowlogReset(CmdSlowlogResetContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdSwapDB(CmdSwapDBContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdSync(CmdSyncContext ctx) {
        SyncRedisCmd cmd = code(new SyncRedisCmd(), ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdTime(CmdTimeContext ctx) {
        TimeRedisCmd redisCmd = code(new TimeRedisCmd(), ctx);
        this.instStack.push(redisCmd);
        return null;
    }

    @Override
    public T visitClientId(ClientIdContext ctx) {
        return this.visitChildren(ctx);
    }

    /* ----------------------------------------------------------------------------------- client commands */

    @Override
    public T visitCmdClientCaching(CmdClientCachingContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdClientGetname(CmdClientGetnameContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdClientGetredir(CmdClientGetredirContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdClientID(CmdClientIDContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdClientInfo(CmdClientInfoContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdClientKill(CmdClientKillContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitKillBy(KillByContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitKillById(KillByIdContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitKillByType(KillByTypeContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitKillByUser(KillByUserContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitKillByAddr(KillByAddrContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitKillByLAddr(KillByLAddrContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitKillBySkipme(KillBySkipmeContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitKillMaxage(KillMaxageContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdClientList(CmdClientListContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdClientNoEvict(CmdClientNoEvictContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdClientNoTouch(CmdClientNoTouchContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdClientPause(CmdClientPauseContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdClientReply(CmdClientReplyContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdClientSetInfo(CmdClientSetInfoContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdClientSetname(CmdClientSetnameContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdClientTracking(CmdClientTrackingContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitPrefixOpt(PrefixOptContext ctx) {
        return this.visitChildren(ctx);
    }

    @Override
    public T visitCmdClientTrackingInfo(CmdClientTrackingInfoContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdClientUnBlock(CmdClientUnBlockContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdClientUnPause(CmdClientUnPauseContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdAuth(CmdAuthContext ctx) {
        AuthRedisCmd cmd = new AuthRedisCmd();

        if (ctx.username() != null) {
            ctx.username().accept(this);
            cmd.setUsername((StrToken) this.instStack.pop());
        }

        ctx.password.accept(this);
        cmd.setPassword((StrToken) this.instStack.pop());

        cmd = code(cmd, ctx);
        this.instStack.push(cmd);
        return null;
    }

    @Override
    public T visitCmdEcho(CmdEchoContext ctx) {
        ctx.stringValue().accept(this);

        StrToken message = (StrToken) instStack.pop();
        EchoRedisCmd redisCmd = code(new EchoRedisCmd(message), ctx);
        this.instStack.push(redisCmd);
        return null;
    }

    @Override
    public T visitCmdHello(CmdHelloContext ctx) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public T visitCmdPing(CmdPingContext ctx) {
        PingRedisCmd pingRedisCmd = new PingRedisCmd();
        if (ctx.stringValue() != null) {
            ctx.stringValue().accept(this);
            StrToken message = (StrToken) instStack.pop();
            pingRedisCmd.setMessage(message);
        }
        PingRedisCmd redisCmd = code(pingRedisCmd, ctx);
        this.instStack.push(redisCmd);
        return null;
    }

    @Override
    public T visitCmdQuit(CmdQuitContext ctx) {
        QuitRedisCmd redisCmd = code(new QuitRedisCmd(), ctx);
        this.instStack.push(redisCmd);
        return null;
    }

    @Override
    public T visitCmdReset(CmdResetContext ctx) {
        ResetRedisCmd redisCmd = code(new ResetRedisCmd(), ctx);
        this.instStack.push(redisCmd);
        return null;
    }

    @Override
    public T visitCmdSelect(CmdSelectContext ctx) {
        ctx.db.accept(this);
        IntToken select = (IntToken) instStack.pop();

        SelectRedisCmd cmd = code(new SelectRedisCmd(select), ctx);
        this.instStack.push(cmd);
        return null;
    }
}
