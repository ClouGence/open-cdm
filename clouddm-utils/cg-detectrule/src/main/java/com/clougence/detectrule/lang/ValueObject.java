package com.clougence.detectrule.lang;

import com.clougence.detectrule.lang.reflect.TypeType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValueObject implements LangObject {

    private final Object   value;
    private final TypeType type;

    public ValueObject(Object value, TypeType type){
        this.value = value;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    @Override
    public Object unwrap() {
        switch (this.type) {
            case Float:
                return ((Number) this.value).doubleValue();
            case Integer:
                return ((Number) this.value).longValue();
            default:
                return this.value;
        }
    }
}
