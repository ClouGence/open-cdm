package com.clougence.clouddm.console.web.model.vo;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfig;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsMenu;
import com.clougence.clouddm.console.web.component.file.mode.FormatConvertDef;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2020/4/13
 **/
@Getter
@Setter
public class ConsoleSettingsVO {

    private Map<String, List<DsMenu>>     menus;
    private Map<DataSourceType, DsConfig> dsSettingDef;
    private List<FormatConvertDef>        fmtConvertDef;

}
