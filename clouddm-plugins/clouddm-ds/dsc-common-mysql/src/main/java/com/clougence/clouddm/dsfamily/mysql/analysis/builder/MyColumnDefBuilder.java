package com.clougence.clouddm.dsfamily.mysql.analysis.builder;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.ColumnDefBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.mysql.analysis.builder.enums.MyAttribute;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyColumnDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class MyColumnDefBuilder extends ColumnDefBuilder<MyColumnDomain> {

    private boolean auto;
    private String  comment;
    private String  collate;
    private String  charset;
    private boolean unsigned;
    private boolean zerofill;

    @Override
    protected MyColumnDomain getColumnDomain() {
        MyColumnDomain myColumnDomain = new MyColumnDomain();
        myColumnDomain.setNullable(true);
        return myColumnDomain;
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == MyAttribute.AUTO_INCREMENT) {
            auto = true;
        } else if (attr == CommonAttribute.COMMENT) {
            this.comment = (String) value;
        } else if (attr == MyAttribute.COLLATE) {
            this.collate = (String) value;
        } else if (attr == MyAttribute.CHARACTER_SET) {
            this.charset = (String) value;
        } else if (attr == MyAttribute.UNSIGNED) {
            this.unsigned = true;
        } else if (attr == MyAttribute.ZEROFILL) {
            this.zerofill = true;
        } else {
            super.addAttr(attr, value);
        }
    }

    @Override
    public List<Domain> build() {
        columnDomain.setCollate(this.collate);
        columnDomain.setCharacterSet(this.charset);
        columnDomain.setUnsigned(unsigned);
        columnDomain.setZerofill(zerofill);
        columnDomain.setComment(this.comment);
        columnDomain.setAuto(auto);
        return Collections.singletonList(columnDomain);
    }
}
