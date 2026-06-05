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
package com.clougence.schema.umi.special.rdb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.schema.umi.struts.AttributeUmiData;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RdbPartitionItem extends AttributeUmiData {

    private String                 name;
    private String                 description;
    private String                 tablespace;
    private String                 partitionMethod;
    private String                 comment;

    private Map<String, String>    valueMap;

    private List<RdbPartitionItem> subItems = new ArrayList<>();
}
