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
package com.clougence.clouddm.console.web.model.fo.security;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.AuthKind;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2024/2/21 09:57:43
 */
@Getter
@Setter
public class ListMyAuthOfResFO {

    @NotNull(message = "authKind can not be null.")
    private AuthKind                   authKind;

    @NotNull(message = "groups can not be null.")
    private List<ListAuthOfResGroupFO> groups;
}
