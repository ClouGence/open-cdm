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
package com.clougence.clouddm.console.web.service.faker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FakerColumnDTO {

    private String  seedType;
    private String  allowNullable;
    private String  nullableRatio;
    //array
    private String  minSize;
    private String  maxSize;
    //bytes & string
    private String  minLength;
    private String  maxLength;
    //string
    private String  allowEmpty;
    //number
    private String  minMax;
    private String  scale;
    private String  abs;
    private String  numberType;
    //date
    private String  genType;
    private String  rangeForm;
    private String  rangeTo;
    private String  zoneForm;
    private String  zoneTo;
    private String  startTime;
    private String  minInterval;
    private String  maxInterval;
    private String  intervalScope;
    //geometry
    private String  geometryType;
    private String  formatType;
    private String  minPointSize;
    private String  maxPointSize;
    //guid
    private String  dateType;
    //    number & date & geometry
    private String  precision;

    // ignore
    private Boolean ignoreColsUpdate;
    private Boolean ignoreColsUpdateWhere;
    private Boolean ignoreColsDeleteWhere;
    private Boolean ignoreColsInsert;
}
