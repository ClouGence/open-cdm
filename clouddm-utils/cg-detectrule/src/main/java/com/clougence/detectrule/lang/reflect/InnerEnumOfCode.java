package com.clougence.detectrule.lang.reflect;

import com.clougence.detectrule.lang.EnumAccess;

class InnerEnumOfCode<T> implements EnumAccess<T> {

    private final T[]           elements;
    private final EnumAccess<?> direct;

    public InnerEnumOfCode(Class<?> enumType){
        this.elements = (T[]) enumType.getEnumConstants();

        if (EnumAccess.class.isAssignableFrom(enumType) && this.elements.length > 0) {
            direct = (EnumAccess<?>) this.elements[0];
        } else {
            direct = null;
        }
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public T valueOfCode(String codeString) {
        return null;
    }
}
