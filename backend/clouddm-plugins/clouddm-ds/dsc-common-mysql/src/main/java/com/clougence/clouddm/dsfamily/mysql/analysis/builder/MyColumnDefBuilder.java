/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
