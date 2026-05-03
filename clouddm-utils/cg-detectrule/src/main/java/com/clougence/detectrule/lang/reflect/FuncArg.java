package com.clougence.detectrule.lang.reflect;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class FuncArg implements Accessible {

    private final Accessible parent;
    private final int        index;
    private final String     name;
    private final String     desc;
    private final Type       type;

    public FuncArg(Accessible parent, int index, String name, String desc, Type type){
        this.parent = parent;
        this.index = index;
        this.name = name;
        this.desc = desc;
        this.type = type;
    }

    @Override
    public TypeType getTypeType() { return TypeType.Arg; }

    @Override
    public String toString() {
        return "FuncArg{" + //
               "index='" + index + "', " +//
               "type=" + type + ", " +//
               "typeType=" + getTypeType() + '}';
    }
}
