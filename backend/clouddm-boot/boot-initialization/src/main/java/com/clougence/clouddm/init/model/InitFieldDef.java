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
package com.clougence.clouddm.init.model;

import lombok.Data;

/**
 * Definition of a single initialization configuration field.
 * The field schema comes from init-fields.json, and the field value is resolved from runtime classpath property files.
 */
@Data
public class InitFieldDef {

    /** Property key matching the corresponding entry in a .properties file. */
    private String  propertyKey;

    /** Default value resolved from the active runtime configuration. */
    private String  defaultValue;

    /** UI category such as database, security, or connectivity. */
    private String  category;

    /** Form input type such as text, password, or number. */
    private String  inputType;

    /** Whether the field is required. */
    private boolean required;

    /** Localized label shown in the initialization form. */
    private String  label;

    /** Localized description shown beside the field. */
    private String  description;
}
