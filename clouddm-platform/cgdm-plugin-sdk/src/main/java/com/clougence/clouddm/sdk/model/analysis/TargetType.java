package com.clougence.clouddm.sdk.model.analysis;

import com.clougence.schema.umi.struts.UmiTypes;

import lombok.Getter;

@Getter
public enum TargetType {

    // common
    Unknown(null),
    Environment(null),
    Instance(UmiTypes.Instance),
    Machine(null),
    UserOrRole(null),
    ConfigKey(null),

    Query(null),
    Update(null),
    Delete(null),
    Insert(null),
    Call(null),

    // rdb
    Catalog(UmiTypes.Catalog),
    Schema(UmiTypes.Schema),
    Table(UmiTypes.Table),
    View(UmiTypes.View),
    Materialized(UmiTypes.Materialized),
    Column(UmiTypes.Column),
    Index(UmiTypes.Index),
    Constraint(null),
    Sequence(UmiTypes.Sequence),
    Function(UmiTypes.Function),
    Procedure(UmiTypes.Procedure),
    Trigger(UmiTypes.Trigger),
    Event(null),
    Synonym(UmiTypes.Synonym),
    Object(null),
    PrepareStatement(null),

    // cache
    Key(UmiTypes.Key),

    ;

    private final UmiTypes umiType;

    TargetType(UmiTypes umiType){
        this.umiType = umiType;
    }
}
