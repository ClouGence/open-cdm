package com.clougence.detectrule.lang;

import com.clougence.detectrule.lang.reflect.TypeType;

public interface LangObject {

    TypeType getType();

    Object unwrap();
}
