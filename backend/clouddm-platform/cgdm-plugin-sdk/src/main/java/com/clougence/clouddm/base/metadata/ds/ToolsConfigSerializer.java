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
package com.clougence.clouddm.base.metadata.ds;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.clougence.clouddm.base.metadata.ds.tools.FakerPluginConfig;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * @author mode 2020/11/10 16:46
 */
public class ToolsConfigSerializer extends JsonDeserializer<ToolConfig> {

    @Override
    public ToolConfig deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String jsonData = p.readValueAsTree().toString();
        JSONObject objectMap = JSON.parseObject(jsonData);
        String toolName = objectMap.getString("toolName");
        if (StringUtils.equals(FakerPluginConfig.TOOL_NAME, toolName)) {
            return objectMap.toJavaObject(FakerPluginConfig.class);
        }
        throw new UnsupportedEncodingException("tool '" + toolName + "' Unsupported.");
    }
}
