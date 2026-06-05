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
package com.clougence.rdp.service.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author chunlin create time is 2024/7/18
 */
@Getter
@Setter
public class AddRoleMO {

    private boolean success;

    private Long    roleUid;

    private String  errorMsg;

    public AddRoleMO(boolean success){
        this.success = success;
    }

    public AddRoleMO(boolean success, String errorMsg){
        this.success = success;
        this.errorMsg = errorMsg;
    }

    public AddRoleMO(boolean success, Long roleUid){
        this.success = success;
        this.roleUid = roleUid;
    }
}
