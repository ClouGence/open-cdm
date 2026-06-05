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
package com.clougence.clouddm.sdk.ui.editor.data;

import lombok.Getter;
import lombok.Setter;

/**
 * @author caishan
 * @date 2023-10-25 11:04:51
 * @version: 1.0
 */

@Getter
@Setter
public class DataEditorHeader {

    private String type;
    private String expr;
    private String message;

    public DataEditorHeader(){
    }

    public DataEditorHeader(String type, String expr, String message){
        this.type = type;
        this.expr = expr;
        this.message = message;
    }
}
