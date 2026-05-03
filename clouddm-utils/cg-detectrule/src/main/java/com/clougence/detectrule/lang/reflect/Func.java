package com.clougence.detectrule.lang.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Func implements Accessible {

    private final Accessible parent;
    private String           name;
    private List<FuncArg>    funcArgs;
    private Type             funcReturn;

    private Method           localMethod;
    private boolean          staticMethod;

    Func(Accessible parent, String name, Method localMethod){
        this.parent = parent;
        this.name = name;
        this.localMethod = localMethod;
        this.staticMethod = Modifier.isStatic(localMethod.getModifiers());
    }

    @Override
    public TypeType getTypeType() { return TypeType.Func; }

    @Override
    public String toString() {
        return "Func{" + //
               "name='" + name + "', " +//
               "funcArgs=(" + StringUtils.join(funcArgs.toArray(), ", ") + "), " +//
               "funcReturn=" + this.funcReturn + ", " +//
               "typeType=" + getTypeType() + '}';
    }
}
