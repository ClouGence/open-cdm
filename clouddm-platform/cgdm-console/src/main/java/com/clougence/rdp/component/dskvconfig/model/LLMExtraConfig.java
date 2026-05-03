package com.clougence.rdp.component.dskvconfig.model;

import java.util.Map;

import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.LLMChatConfig;
import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.LLMEmbeddingConfig;
import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.constant.DsConfigDef;
import com.clougence.rdp.constant.DsUseActualValueAsDefault;
import com.clougence.rdp.constant.I18nDsConfigMsgKeys;
import com.clougence.rdp.constant.KvConfValType;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * @author bucketli 2022/8/10 09:33:48
 */
@Getter
@Setter
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
public class LLMExtraConfig extends DsExtraConfig {

    @DsConfigDef(name = "llmEmbedding", descKey = I18nDsConfigMsgKeys.DS_CONFIG_LLM_EMBEDDING, readOnly = false, kvConfWebOp = KvConfValType.JSON)
    @DsUseActualValueAsDefault
    private String                          llmEmbedding;

    @DsConfigDef(name = "llmChat", descKey = I18nDsConfigMsgKeys.DS_CONFIG_LLM_CHAT, readOnly = false, kvConfWebOp = KvConfValType.JSON)
    @DsUseActualValueAsDefault
    private String                          llmChat;

    @DsConfigDef(name = "httpsEnabled", descKey = I18nDsConfigMsgKeys.DS_CONFIG_LLM_URI_HTTPS_ENABLED, valueAdvance = "true / false", readOnly = false, kvConfWebOp = KvConfValType.BOOLEAN)
    @DsUseActualValueAsDefault
    private Boolean                         httpsEnabled;

    private Map<String, LLMEmbeddingConfig> llmEmbeddingConfigs;

    private Map<String, LLMChatConfig>      llmChatConfigs;

    public void deserialize() {
        if (StringUtils.isNotBlank(llmEmbedding)) {
            try {
                llmEmbeddingConfigs = new ObjectMapper().readValue(llmEmbedding, new TypeReference<Map<String, LLMEmbeddingConfig>>() {
                });
            } catch (Exception e) {
                throw new RuntimeException("Process llm embedding config error.msg:" + ExceptionUtils.getRootCauseMessage(e), e);
            }
        }

        if (StringUtils.isNotBlank(llmChat)) {
            try {
                llmChatConfigs = new ObjectMapper().readValue(llmChat, new TypeReference<Map<String, LLMChatConfig>>() {
                });
            } catch (Exception e) {
                throw new RuntimeException("Process llm chat config error.msg:" + ExceptionUtils.getRootCauseMessage(e), e);
            }
        }
    }
}
