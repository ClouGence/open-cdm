package com.clougence.clouddm.console.web.controller.editor.property;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_QUERY_CONSOLE;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.component.auth.BizResOwnerCacheService;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.model.fo.editor.property.PropertyEditorFO;
import com.clougence.clouddm.console.web.model.fo.editor.property.PropertyInitFO;
import com.clougence.clouddm.console.web.model.vo.editor.table.TableEditorForm;
import com.clougence.clouddm.console.web.service.editor.DsObjPropertyService;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyEditorUiData;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.model.analysis.resource.DsResPath;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.rdp.util.RdpAuthUtils;
import com.clougence.schema.umi.struts.UmiTypes;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/editor/properties")
@Slf4j
public class PropertyController {

    @Resource
    private DmDsConfigService       dmDsConfigService;
    @Resource
    private BizResOwnerCacheService ownerCacheService;
    @Resource
    private DsObjPropertyService    propertyEditorService;
    @Resource
    private DmAuthServiceForBiz     dmAuthServiceForBiz;

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/propertiesDef", method = RequestMethod.POST)
    public ResWebData<?> editorDef(@Valid @RequestBody PropertyEditorFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        this.ownerCacheService.ownDataSource(puid, levels.getDsDO().getId());

        UmiTypes umiTypes = UmiTypes.valueOfCode(fo.getTypes());

        TableEditorForm form = this.propertyEditorService.loadPropertyDef(puid, uid, levels, umiTypes);
        return ResWebDataUtils.buildSuccess(form);
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/getProperties", method = RequestMethod.POST)
    public ResWebData<?> initEditor(@Valid @RequestBody PropertyInitFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        this.ownerCacheService.ownDataSource(puid, levels.getDsDO().getId());
        DsResPath dsResource = RdpAuthUtils.genResPathByList(levels.getDbLevels(), fo.getLeafName());
        this.dmAuthServiceForBiz.checkResPath(puid, uid, levels.getDsDO().getId(), AuthKind.DataSource, dsResource, SecDataAuthLabel.DM_DAUTH_QUERY);

        UmiTypes umiTypes = UmiTypes.valueOfCode(fo.getType());
        PropertyEditorUiData uiData = this.propertyEditorService.loadPropertyData(puid, uid, levels, umiTypes, fo.getLeafName());
        return ResWebDataUtils.buildSuccess(uiData);
    }
}
