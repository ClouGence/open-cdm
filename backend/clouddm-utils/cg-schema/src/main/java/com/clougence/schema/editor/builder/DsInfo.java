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
package com.clougence.schema.editor.builder;

import java.util.ArrayList;
import java.util.List;

import com.clougence.schema.DsType;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.metadata.TypeInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
public class DsInfo {

    private final SqlBuilder      provider;
    private final DsType          dsType;
    private final MainVersion     mainVersion;

    @Setter
    private List<TypeInfo>        typeInfoList;

    @Setter
    private CharsetsAndCollations charsetAndCollation;

    public DsInfo(SqlBuilder provider, DsType dsType, MainVersion mainVersion){
        this.provider = provider;
        this.dsType = dsType;
        this.mainVersion = mainVersion;
        this.typeInfoList = new ArrayList<>();
        this.charsetAndCollation = new CharsetsAndCollations();
    }
}
