package com.clougence.detectrule.runtime.v1;

import com.clougence.detectrule.lang.LangObject;
import com.clougence.detectrule.lang.reflect.Accessible;
import com.clougence.dslpaser.ast.location.Location;

import lombok.Getter;

@Getter
class TypedExpr {

    private final Location   location;
    private final LangObject data;
    private final Accessible type;

    public TypedExpr(Location location, LangObject data, Accessible type){
        this.location = location;
        this.data = data;
        this.type = type;
    }
}
