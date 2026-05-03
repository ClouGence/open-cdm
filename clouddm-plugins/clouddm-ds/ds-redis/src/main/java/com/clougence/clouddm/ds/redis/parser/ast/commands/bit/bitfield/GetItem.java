package com.clougence.clouddm.ds.redis.parser.ast.commands.bit.bitfield;

import com.clougence.clouddm.ds.redis.parser.ast.commands.bit.bitfield.BitFieldItem;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetItem extends BitFieldItem {

    private StrToken encoding;
    private StrToken offset;
}
