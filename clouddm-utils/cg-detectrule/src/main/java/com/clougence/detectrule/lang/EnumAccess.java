package com.clougence.detectrule.lang;

public interface EnumAccess<T> {

    default String codeName() {
        return name();
    }

    String name();

    T valueOfCode(String codeString);
}
