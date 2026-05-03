package com.clougence.detectrule.lang;

import java.util.Iterator;

import com.clougence.detectrule.lang.reflect.Type;

public interface CollectionAccess extends LangObject {

    Type getElementType();

    void putElement(LangObject value);

    LangObject getElement(int index);

    Iterator<LangObject> toIterator();

    boolean contains(LangObject value);

    int getSize();
}
