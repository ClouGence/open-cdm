package com.clougence.detectrule.lang.reflect;

import java.lang.reflect.Method;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Field implements Accessible {

    private final Accessible parent;
    private final String     name;
    private final Type       type;
    private final TypeType   typeType;

    private final AccessMode accessMode;
    private final Method     readMethod;
    private final Method     writeMethod;

    Field(Accessible parent, String name, Type type, AccessMode accessMode, Method writeMethod, Method readMethod){
        this.parent = parent;
        this.name = name;
        this.type = type;
        this.typeType = TypeType.Field;

        this.accessMode = accessMode;
        this.writeMethod = writeMethod;
        this.readMethod = readMethod;
    }

    @Override
    public String toString() {
        return "Field{" + //
               "name='" + name + "', " +//
               "type=" + type + ", " +//
               "typeType=" + typeType + ", " +//
               "accessMode=" + accessMode + '}';
    }
}
