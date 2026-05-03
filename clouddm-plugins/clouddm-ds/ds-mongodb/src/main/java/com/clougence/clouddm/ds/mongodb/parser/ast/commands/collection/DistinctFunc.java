package com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection;

import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;

import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.CollectionFunc;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DistinctFunc extends CollectionFunc {

    private String field;
    private String query;
    private String collation;

    @Override
    public MongoFuncType getFuncType() { return MongoFuncType.DISTINCT; }

    @Override
    public String toBson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"distinct\":").append("\"").append(this.getCollectionName()).append("\"");
        sb.append(",\"key\":").append(field);
        if (this.query != null) {
            sb.append(",\"query\":").append(query);
        }
        if (this.collation != null) {
            sb.append(",\"collation\":").append(collation);
        }
        sb.append("}");
        return sb.toString();
    }
}
