package com.clougence.clouddm.ds.redis.parser.ast.commands.bit.bitfield;

import com.clougence.clouddm.ds.redis.parser.ast.commands.bit.bitfield.BitFieldItem;
import com.clougence.clouddm.ds.redis.parser.ast.commands.bit.bitfield.OverflowItem;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncrItem extends BitFieldItem {

    private OverflowItem overflow;
    private StrToken     encoding;
    private StrToken     offset;
    private IntToken     incr;
}
