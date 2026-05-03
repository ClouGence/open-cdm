package com.clougence.clouddm.dsfamily.mysql.execute.function;

import java.util.List;

import com.clougence.schema.umi.special.rdb.RdbFunction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuiltInFunctionCollector {

    private List<RdbFunction> functions;
}
