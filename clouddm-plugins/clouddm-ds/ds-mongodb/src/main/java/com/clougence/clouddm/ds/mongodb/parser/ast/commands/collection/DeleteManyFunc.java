package com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection;

import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;

import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.CollectionFunc;
import lombok.Setter;

@Setter
public class DeleteManyFunc extends CollectionFunc {

    private String filter;

    //option
    private String writeConcern;
    private String collation;
    private String hint;
    private String maxTimeMS;
    private String let;

    @Override
    public MongoFuncType getFuncType() { return MongoFuncType.DELETE_MANY; }

    @Override
    public String toString() {
        return "";
    }

    public String toBson() {
        StringBuilder sb = new StringBuilder();

        sb.append("{");
        sb.append(" \"delete\":").append("\"").append(this.getCollectionName()).append("\"").append(",");
        sb.append("\"deletes\":[");
        sb.append("{");
        sb.append("\"q\":");
        if (this.filter != null) {
            sb.append(this.filter);
        } else {
            sb.append("{}");
        }
        sb.append(",\"limit\":0");
        if (this.collation != null) {
            sb.append(",\"collation\":").append(this.collation);
        }
        if (this.hint != null) {
            sb.append(",\"hint\":").append(this.hint);
        }
        sb.append("}");
        sb.append("]");

        if (this.let != null) {
            sb.append(",\"let\":").append(this.let);
        }

        if (this.maxTimeMS != null) {
            sb.append(",\"maxTimeMS\":").append(this.maxTimeMS);
        }
        if (this.writeConcern != null) {
            sb.append(",\"writeConcern\":").append(this.writeConcern);
        }
        sb.append("}");
        return sb.toString();
    }

}
