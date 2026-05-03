package com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection;

import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.AbstractMongoFunc;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class CollectionFunc extends AbstractMongoFunc {

    protected String collectionName;

    public CollectionFunc(MongoFuncType type){
        super(type);
    }

    public CollectionFunc(){
    }

    protected String getQuoteName() { return "\"" + collectionName + "\""; }

}
