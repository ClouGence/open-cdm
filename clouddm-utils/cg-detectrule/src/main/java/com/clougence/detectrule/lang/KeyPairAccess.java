package com.clougence.detectrule.lang;

import com.clougence.detectrule.lang.reflect.Accessible;

public interface KeyPairAccess extends LangObject {

    Accessible getPairReflect(String key);

    void putKeyPair(String key, LangObject value);

    LangObject getPair(String key);

    boolean containPair(String key);

    String[] getKeySet();
}
