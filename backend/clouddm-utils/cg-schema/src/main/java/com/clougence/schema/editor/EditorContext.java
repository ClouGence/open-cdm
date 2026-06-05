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
package com.clougence.schema.editor;

import java.util.ArrayList;
import java.util.List;

import com.clougence.schema.editor.builder.DsInfo;
import com.clougence.schema.editor.builder.actions.Action;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.metadata.MetaDataService;
import com.clougence.schema.metadata.Version;
import com.clougence.schema.umi.struts.AttributeUmiData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditorContext extends AttributeUmiData {

    // result
    private final List<Action> actions      = new ArrayList<>();

    private boolean            useDelimited;

    private boolean            cascade;

    private boolean            skipHandlers = false;

    // db info
    private final DsInfo       srcDsInfo;

    private DsInfo             tarDsInfo;

    public EditorContext(SqlBuilder builderProvider){
        this(builderProvider, "");
    }

    public EditorContext(SqlBuilder builderProvider, String version){
        MainVersion mainVersion = MetaDataService.getMainVersion(version, builderProvider.getDataSourceType());
        if (mainVersion == null) {
            mainVersion = new Version(builderProvider.getDataSourceType(), version);
        }
        this.srcDsInfo = new DsInfo(builderProvider, builderProvider.getDataSourceType(), mainVersion);
        this.tarDsInfo = new DsInfo(builderProvider, builderProvider.getDataSourceType(), mainVersion);
    }

    public EditorContext(SqlBuilder builderProvider, MainVersion mainVersion){
        this.srcDsInfo = new DsInfo(builderProvider, builderProvider.getDataSourceType(), mainVersion);
        this.tarDsInfo = new DsInfo(builderProvider, builderProvider.getDataSourceType(), mainVersion);
    }

    public EditorContext(DsInfo srcDsInfo, DsInfo tarDsInfo){
        this.srcDsInfo = srcDsInfo;
        this.tarDsInfo = tarDsInfo;
    }

    public EditorContext clone() {
        EditorContext context = new EditorContext(this.srcDsInfo, this.tarDsInfo);
        context.merge(this);
        context.setUseDelimited(this.useDelimited);
        context.setCascade(this.cascade);
        return context;
    }

}
