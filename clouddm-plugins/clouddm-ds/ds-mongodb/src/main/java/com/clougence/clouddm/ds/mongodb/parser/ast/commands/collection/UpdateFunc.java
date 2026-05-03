package com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection;

import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;

import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.CollectionFunc;
import lombok.Setter;

@Setter
public class UpdateFunc extends CollectionFunc {

    private String  filter;
    private String  update;

    //option
    private String  upsert;
    private String  writeConcern;
    private String  collation;
    private String  arrayFilters;
    private String  hint;
    private String  maxTimeMS;
    private String  let;
    private String  bypassDocumentValidation;
    private String  sort;

    private boolean multi;

    public UpdateFunc(MongoFuncType type, boolean multi){
        super(type);
        this.multi = multi;
    }

    @Override
    public MongoFuncType getFuncType() { return this.funcType; }

    public String toBson() {
        StringBuilder sb = new StringBuilder();

        sb.append("{");
        sb.append(" \"update\":").append("\"").append(this.getCollectionName()).append("\"").append(",");
        sb.append("\"updates\":[");
        sb.append("{");
        sb.append("\"q\":");
        if (this.filter != null) {
            sb.append(this.filter);
        } else {
            sb.append("{}");
        }
        if (this.update != null) {
            sb.append(",u:").append(this.update);
        } else {
            sb.append(",u:{}");
        }
        if (this.upsert != null) {
            sb.append(",upsert:").append(upsert);
        }
        if (this.collation != null) {
            sb.append(",\"collation\":").append(this.collation);
        }
        if (this.arrayFilters != null) {
            sb.append(",\"arrayFilters\":").append(this.arrayFilters);
        }
        if (this.hint != null) {
            sb.append(",\"hint\":").append(this.hint);
        }
        if (this.sort != null) {
            sb.append(",\"sort\":").append(this.sort);
        }
        sb.append(",\"multi\":").append(this.multi);

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
        if (this.bypassDocumentValidation != null) {
            sb.append(",\"bypassDocumentValidation\":").append(this.bypassDocumentValidation);
        }
        sb.append("}");
        return sb.toString();
    }

}
