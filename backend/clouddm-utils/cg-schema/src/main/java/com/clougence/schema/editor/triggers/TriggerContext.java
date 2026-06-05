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
package com.clougence.schema.editor.triggers;

import java.util.ArrayList;
import java.util.List;

import com.clougence.schema.editor.EditorContext;
import com.clougence.schema.editor.builder.DsInfo;
import com.clougence.schema.editor.builder.mappings.MappingHandler;
import com.clougence.schema.umi.struts.AttributeUmiData;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/5/21 19:56
 */
@Getter
@Setter
public class TriggerContext extends AttributeUmiData {

    private MappingHandler  mappingHandler;
    private List<Exception> errorMessage = new ArrayList<>();
    private DsInfo          srcDsInfo;
    private DsInfo          tarDsInfo;

    private boolean         skipHandlers = false;
    private boolean         useDelimited;
    private boolean         cascade;
    private boolean         ifExists;

    public TriggerContext(){
    }

    public MappingHandler getMappingHandler() { return mappingHandler; }

    public boolean isSelf() { return this.srcDsInfo.getDsType() == this.tarDsInfo.getDsType(); }

    public void addErrors(Exception error) {
        if (error != null) {
            this.errorMessage.add(error);
        }
    }

    public EditorContext toEditorContext() {
        EditorContext editorContext = new EditorContext(this.srcDsInfo, this.tarDsInfo);
        editorContext.setUseDelimited(this.useDelimited);
        editorContext.setCascade(this.cascade);
        editorContext.merge(this);

        return editorContext;
    }

}
