package com.clougence.clouddm.console.web.controller.system;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.DmBuildInfo;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.menus.UiMenuDef;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsService;
import com.clougence.clouddm.console.web.component.dsconfig.impl.DsMenuUtils;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfig;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsMenu;
import com.clougence.clouddm.console.web.component.file.mode.FormatConvertDef;
import com.clougence.clouddm.console.web.component.whitelist.WhiteListService;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.constants.DmMode;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.model.vo.ConsoleSettingsVO;
import com.clougence.clouddm.console.web.model.vo.GlobalSettingsVO;
import com.clougence.clouddm.console.web.service.system.impl.DsVersionsServiceImpl;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.dsfamily.definition.ui.browser.RdbUiMenuDef;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.resultset.file.FileFormatConvert;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.service.RdpAuthServiceForBiz;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.utils.StringUtils;

@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/")
public class DmHomeController {

    @Resource
    private DmConsoleConfig       dmConfig;
    @Resource
    private DsVersionsServiceImpl dsVersionsService;
    @Resource
    private RdpAuthServiceForBiz  rdpAuthServiceForBiz;
    @Resource
    private DmDsService           dmDsService;
    @Resource
    private WhiteListService      whiteListService;

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/dm_global_settings", method = { RequestMethod.POST })
    public ResWebData<?> dmGlobalSettings(HttpServletRequest request) {
        GlobalSettingsVO settings = new GlobalSettingsVO();
        settings.setBuildVersion(DmBuildInfo.BUILD_VERSION);
        settings.setBuildId(DmBuildInfo.BUILD_ID);

        if (this.dmConfig.getDmMode() == DmMode.desktop && this.dmConfig.getPersonalConfig() != null) {
            settings.setPersonal(this.dmConfig.getPersonalConfig());
        }

        if (this.dmConfig.getDmMode() == DmMode.output) {
            settings.setProductVersions(this.dsVersionsService.fetchDsVersions());
        }

        return ResWebDataUtils.buildSuccess(settings);
    }

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/dm_console_settings", method = { RequestMethod.POST })
    public ResWebData<?> dmConsoleSettings(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        boolean isSubAccount = !StringUtils.equalsIgnoreCase(puid, uid);

        ConsoleSettingsVO settings = null;
        if (this.rdpAuthServiceForBiz.checkRoleAuth(puid, uid, SecRoleAuthLabel.DM_DS_MAINTENANCE)) {
            List<DsMenu> envAllMenus = DsMenuUtils.generationDsMenus(UiMenuDef.DEFAULT_ENV);
            List<DsMenu> envMenus = envAllMenus.stream().filter(m -> {
                return whiteListService.checkMenuMaintenance(m.getMenuId());
            }).filter(m -> {
                return isSubAccount || !RdbUiMenuDef.MENU_BROWSE_PERMISSIONS.equalsIgnoreCase(m.getMenuId());
            }).collect(Collectors.toList());

            settings = new ConsoleSettingsVO();
            settings.setMenus(new HashMap<>());
            settings.getMenus().put(DsMenuType.Env.getTypeName(), envMenus);
            settings.setDsSettingDef(filterMenuBy(m -> whiteListService.checkMenuMaintenance(m.getMenuId()), isSubAccount));
        }

        if (settings == null && this.rdpAuthServiceForBiz.checkRoleAuth(puid, uid, SecRoleAuthLabel.DM_OBJECT_MANAGER)) {
            List<DsMenu> envAllMenus = DsMenuUtils.generationDsMenus(UiMenuDef.DEFAULT_ENV);
            List<DsMenu> envMenus = envAllMenus.stream().filter(m -> whiteListService.checkMenuManager(m.getMenuId())).collect(Collectors.toList());

            settings = new ConsoleSettingsVO();
            settings.setMenus(new HashMap<>());
            settings.getMenus().put(DsMenuType.Env.getTypeName(), envMenus);
            settings.setDsSettingDef(filterMenuBy(m -> whiteListService.checkMenuManager(m.getMenuId()), isSubAccount));
        }

        if (settings == null) {
            List<DsMenu> envAllMenus = DsMenuUtils.generationDsMenus(UiMenuDef.DEFAULT_ENV);
            List<DsMenu> envMenus = envAllMenus.stream().filter(m -> whiteListService.checkMenuQuery(m.getMenuId())).collect(Collectors.toList());

            settings = new ConsoleSettingsVO();
            settings.setMenus(new HashMap<>());
            settings.getMenus().put(DsMenuType.Env.getTypeName(), envMenus);
            settings.setDsSettingDef(filterMenuBy(m -> whiteListService.checkMenuQuery(m.getMenuId()), isSubAccount));
        }

        settings.setFmtConvertDef(new ArrayList<>());
        List<String> convert = PluginManager.getSpiNamesByType(FileFormatConvert.class);
        for (String name : convert) {
            FileFormatConvert fmtConvert = PluginManager.findSpi(FileFormatConvert.class, name);

            FormatConvertDef def = new FormatConvertDef();
            def.setName(fmtConvert.name());
            def.setDescription(DmI18nUtils.getMessage(fmtConvert.descriptionI18n()));
            def.setIcon(fmtConvert.iconName());
            def.setOption(fmtConvert.getOption());
            settings.getFmtConvertDef().add(def);
        }
        settings.getFmtConvertDef().sort(Comparator.comparing(FormatConvertDef::getName));

        return ResWebDataUtils.buildSuccess(settings);
    }

    private Map<DataSourceType, DsConfig> filterMenuBy(Predicate<DsMenu> predicate, boolean isSubAccount) {
        Map<DataSourceType, DsConfig> dsConfigMap = this.dmDsService.dsConstantSettings();
        dsConfigMap.forEach((dsType, dsConfig) -> {
            if (dsConfig == null) {
                return;
            }
            Map<String, List<DsMenu>> copy = new HashMap<>();
            dsConfig.getMenus().forEach((k, v) -> {
                List<DsMenu> dsMenus = v.stream().filter(predicate).filter(m -> {
                    return isSubAccount || !RdbUiMenuDef.MENU_BROWSE_PERMISSIONS.equalsIgnoreCase(m.getMenuId());
                }).collect(Collectors.toList());

                // last menu can not be separator
                while (true) {
                    int size = dsMenus.size();
                    if (size > 0 && DsMenuUtils.isSeparator(dsMenus.get(size - 1))) {
                        dsMenus.remove(size - 1);
                    } else {
                        break;
                    }
                }

                copy.put(k, dsMenus);
            });
            dsConfig.setMenus(copy);
        });
        return dsConfigMap;
    }
}
