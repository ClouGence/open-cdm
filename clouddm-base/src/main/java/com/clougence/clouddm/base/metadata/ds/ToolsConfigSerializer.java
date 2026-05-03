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
