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
package com.clougence.clouddm.ds.mongodb.definition.ui.template;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.sdk.ui.template.AbstractCmdTemplateSpi;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.clouddm.dsfamily.schema.dialect.DefaultDialect;
import com.clougence.schema.dialect.Dialect;

public class MongoCmdTemplateSpi extends AbstractCmdTemplateSpi {

    @Override
    protected Dialect getDialect() { return DefaultDialect.DEFAULT; }

    @Override
    public List<String> getDropTable(CmdTemplateOption option) {
        String result = "db.getCollection(\"" + option.getTargetName() + "\").drop();";
        return Collections.singletonList(result);
    }

    @Override
    public List<String> getDropView(CmdTemplateOption option) {
        return this.getDropTable(option);
    }

    @Override
    public List<String> getCreateSchema(CmdTemplateOption option) {
        String useDatabase = "use " + option.getSchema() + ";";
        String defaultCollection = "db.createCollection(\"test\");";
        return Arrays.asList(useDatabase, defaultCollection);
    }

    @Override
    public String getQuickQuery(CmdTemplateOption option) {
        return "db.getCollection(\"" + TABLE_PLACEHOLDER + "\").find();";
    }

    @Override
    public List<String> getRenameTable(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("db.getCollection(\"").append(option.getTargetName()).append("\").renameCollection(\"").append(option.getTargetNewName()).append("\");");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public String getQuickQueryByTable(CmdTemplateOption option) {
        return this.getQuickQuery(option);
    }

    @Override
    public String getQuickQueryByView(CmdTemplateOption option) {
        return this.getQuickQuery(option);
    }
}
