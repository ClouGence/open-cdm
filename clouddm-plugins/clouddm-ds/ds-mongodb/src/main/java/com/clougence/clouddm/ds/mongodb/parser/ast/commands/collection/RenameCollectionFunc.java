package com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection;

import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;

import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.CollectionFunc;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RenameCollectionFunc extends CollectionFunc {

    private String to;
    private Boolean dropTarget;

    @Override
    public MongoFuncType getFuncType() { return MongoFuncType.RENAME_COLLECTION; }

}
