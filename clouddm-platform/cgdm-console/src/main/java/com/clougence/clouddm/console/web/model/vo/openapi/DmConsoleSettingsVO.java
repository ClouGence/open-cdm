package com.clougence.clouddm.console.web.model.vo.openapi;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.DsClassify;
import com.clougence.clouddm.console.web.constants.DmMcpI18nKey;
import com.clougence.clouddm.sdk.ui.browser.DsCaseType;
import com.clougence.rdp.component.mcp.model.McpField;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2020/4/13
 **/
@Getter
@Setter
public class DmConsoleSettingsVO {

    @McpField(value = DmMcpI18nKey.F_DATA_SOURCE_TYPE_DESC)
    private DataSourceType dsType;

    private DsClassify     classify;

    private DsCaseType     caseType;
    private String         leftQualifier;
    private String         rightQualifier;

    @McpField(value = DmMcpI18nKey.F_LEVELS_DESC)
    private List<String>   levels;

    @McpField(value = DmMcpI18nKey.F_LEAF_EXPAND_DESC)
    private List<String>   leafExpand;

}
