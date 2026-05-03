package com.clougence.clouddm.ds.redis.parser.ast.commands.keys;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

@Getter
public class ObjectRedisCmd extends AbstractRedisCmd {

    private final StrToken   keyName;
    private final OptionType operation;

    public ObjectRedisCmd(StrToken keyName, OptionType operation){
        this.keyName = keyName;
        this.operation = operation;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.OBJECT; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // OBJECT (ENCODING | FREQ | IDLETIME | REFCOUNT) key

        writer.append("OBJECT ");
        writer.append(operation.name().toUpperCase());
        writer.append(" ");
        writer.append(fmtString(keyName));
    }

    public static enum OptionType {
        ENCODING,
        FREQ,
        IDLETIME,
        REFCOUNT
    }

}
