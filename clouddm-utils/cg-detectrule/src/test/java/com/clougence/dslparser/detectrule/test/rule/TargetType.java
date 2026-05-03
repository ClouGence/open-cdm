package com.clougence.dslparser.detectrule.test.rule;

import lombok.Getter;

@Getter
public enum TargetType {

    // common
    Unknown(null),
    Environment(null),
    Machine(null),

    // rdb
    Catalog(UmiTypes.Catalog),
    Schema(UmiTypes.Schema),
    Table(UmiTypes.Table),
    View(UmiTypes.View),
    Column(UmiTypes.Column),
    Index(UmiTypes.Index),
    Constraint(null),
    Sequence(UmiTypes.Sequence),
    Function(UmiTypes.Function),
    Procedure(UmiTypes.Procedure),
    Trigger(UmiTypes.Trigger),
    Event(null),
    Synonym(UmiTypes.Synonym),

    // cache
    Key(UmiTypes.Key),

    ;

    private final UmiTypes umiType;

    TargetType(UmiTypes umiType){
        this.umiType = umiType;
    }
}
