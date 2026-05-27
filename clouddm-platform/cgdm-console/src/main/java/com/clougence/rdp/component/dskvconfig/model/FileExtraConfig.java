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
package com.clougence.rdp.component.dskvconfig.model;

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.constant.DsConfigDef;
import com.clougence.clouddm.console.web.global.i18n.I18nDsConfigMsgKeys;
import com.clougence.clouddm.platform.dal.model.system.KvConfValType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
public class FileExtraConfig extends DsExtraConfig {

    @DsConfigDef(name = "dbsJson", defaultValue = "[{\"db\":\"cc_virtual_fs\",\"schemas\":[{\"schema\":\"your bucket, base dir, or folderId\",\"tables\":[]}]}]", descKey = I18nDsConfigMsgKeys.DS_CONFIG_VIRTUAL_DBS, readOnly = false, kvConfWebOp = KvConfValType.JSON)
    private String  dbsJson;

    @DsConfigDef(name = "defaultLineSchemaJson", defaultValue = "[{\"column\":\"line\",\"jdbcType\":12,\"typeName\":\"TEXT\"}]", descKey = I18nDsConfigMsgKeys.DS_CONFIG_DEFAULT_SCHEMA, readOnly = false, kvConfWebOp = KvConfValType.JSON)
    private String  defaultLineSchemaJson;

    @DsConfigDef(name = "fileSuffixArray", descKey = I18nDsConfigMsgKeys.DS_CONFIG_FILE_SUFFIXES, valueAdvance = ".md,.txt,.c,.js,.gdoc,.gsheet...", readOnly = false)
    private String  fileSuffixArray;

    @DsConfigDef(name = "withMetaFields", defaultValue = "true", descKey = I18nDsConfigMsgKeys.DS_CONFIG_WITH_METAFIELDS, readOnly = false, kvConfWebOp = KvConfValType.BOOLEAN)
    private Boolean withMetaFields;

    @DsConfigDef(name = "enableLLMExtraction", defaultValue = "false", descKey = I18nDsConfigMsgKeys.DS_CONFIG_ENABLE_LLM_EXTRACTION, readOnly = false, kvConfWebOp = KvConfValType.BOOLEAN)
    private Boolean enableLLMExtraction;

    @DsConfigDef(name = "llmExtractionPrompt", descKey = I18nDsConfigMsgKeys.DS_CONFIG_LLM_EXTRACTION_PROMPT, readOnly = false, kvConfWebOp = KvConfValType.TEXT)
    private String  llmExtractionPrompt;
}
