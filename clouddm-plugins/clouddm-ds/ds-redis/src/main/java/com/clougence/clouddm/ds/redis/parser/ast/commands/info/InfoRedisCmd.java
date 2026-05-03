package com.clougence.clouddm.ds.redis.parser.ast.commands.info;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

/**
 * @Author: Ekko
 * @Date: 2023-06-05 11:19
 */
@Getter
public class InfoRedisCmd extends AbstractRedisCmd {

    private final List<InfoType> types = new ArrayList<>();

    public void addType(InfoType type) {
        if (type != null) {
            this.types.add(type);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.INFO; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("INFO");
        if (!this.types.isEmpty()) {
            for (InfoType type : types) {
                writer.append(" ");
                writer.append(type.name().toUpperCase());
            }
        }
    }

    public enum InfoType {
        SERVER,
        THREADS,
        CLUSTER,
        CLIENTS,
        MEMORY,
        PERSISTENCE,
        STATS,
        REPLICATION,
        CPU,
        COMMANDSTATS,
        LATENCYSTATS,
        SENTINEL,
        MODULES,
        KEYSPACE,
        ERRORSTATS,
        ALL,
        DEFAULT,
        EVERYTHING
    }
}
