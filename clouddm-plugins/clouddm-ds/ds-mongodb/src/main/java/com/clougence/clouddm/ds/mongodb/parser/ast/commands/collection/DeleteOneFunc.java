package com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection;

import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;

import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.CollectionFunc;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteOneFunc extends CollectionFunc {

    private String filter;

    //option
    private String writeConcern;
    private String collation;
    private String hint;
    private String maxTimeMS;
    private String let;

    @Override
    public MongoFuncType getFuncType() { return MongoFuncType.DELETE_ONE; }

    public String toBson() {
        StringBuilder sb = new StringBuilder();

        sb.append("{");
        sb.append(" \"delete\":").append("\"").append(this.getCollectionName()).append("\"").append(",");
        sb.append("\"deletes\":[");
        sb.append("{");
        sb.append("\"q\":");
        if (this.getFilter() != null) {
            sb.append(this.getFilter());
        } else {
            sb.append("{}");
        }
        sb.append(",\"limit\":1");
        if (this.collation != null) {
            sb.append(",\"collation\":").append(this.collation);
        }
        if (this.getHint() != null) {
            sb.append(",\"hint\":").append(this.getHint());
        }
        sb.append("}");
        sb.append("]");

        if (this.let != null) {
            sb.append(",\"let\":").append(this.getLet());
        }

        if (this.maxTimeMS != null) {
            sb.append(",\"maxTimeMS\":").append(this.getMaxTimeMS());
        }
        if (this.getWriteConcern() != null) {
            sb.append(",\"writeConcern\":").append(this.getWriteConcern());
        }
        sb.append("}");

        return sb.toString();
    }

}
