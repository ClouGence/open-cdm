package com.clougence.detectrule.lang;

import com.clougence.detectrule.lang.reflect.TypeType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefObject implements LangObject {

    private final Object value;

    public RefObject(Object value){
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    @Override
    public TypeType getType() { return TypeType.Ref; }

    @Override
    public Object unwrap() {
        return this.value;
    }
}
