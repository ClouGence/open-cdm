package com.clougence.clouddm.ds.redis.parser.ast.commands.bit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.commands.bit.bitfield.BitFieldItem;
import com.clougence.clouddm.ds.redis.parser.ast.commands.bit.bitfield.GetItem;
import com.clougence.clouddm.ds.redis.parser.ast.commands.bit.bitfield.IncrItem;
import com.clougence.clouddm.ds.redis.parser.ast.commands.bit.bitfield.SetItem;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;
import com.clougence.utils.CollectionUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BitFieldRedisCmd extends AbstractRedisCmd {

    private final StrToken     keyName;
    private List<BitFieldItem> items = new ArrayList<>();

    public BitFieldRedisCmd(StrToken keyName){
        this.keyName = keyName;
    }

    public void addItem(BitFieldItem item) {
        if (item != null) {
            this.items.add(item);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.BITFIELD; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("BITFIELD");
        writer.append(" ");
        writer.append(fmtString(keyName));
        if (CollectionUtils.isNotEmpty(items)) {
            for (BitFieldItem item : items) {
                if (item instanceof GetItem) {
                    writer.append(" GET ");
                    writer.append(fmtString(((GetItem) item).getEncoding()));
                    writer.append(" ");
                    writer.append(fmtString(((GetItem) item).getOffset()));
                } else if (item instanceof SetItem) {
                    if (((SetItem) item).getOverflow() != null) {
                        writer.append(" OVERFLOW ");
                        writer.append(((SetItem) item).getOverflow().name());
                    }

                    writer.append(" SET ");
                    writer.append(fmtString(((SetItem) item).getEncoding()));
                    writer.append(" ");
                    writer.append(fmtString(((SetItem) item).getOffset()));
                    writer.append(" ");
                    writer.append(fmtString(((SetItem) item).getValue()));
                } else if (item instanceof IncrItem) {
                    if (((IncrItem) item).getOverflow() != null) {
                        writer.append(" OVERFLOW ");
                        writer.append(((IncrItem) item).getOverflow().name());
                    }

                    writer.append(" INCRBY ");
                    writer.append(fmtString(((IncrItem) item).getEncoding()));
                    writer.append(" ");
                    writer.append(fmtString(((IncrItem) item).getOffset()));
                    writer.append(" ");
                    writer.append(fmtInt(((IncrItem) item).getIncr()));
                } else {
                    throw new UnsupportedOperationException("Unsupported BitFieldItem Type :" + item.getClass().getName());
                }
            }
        }
    }
}
