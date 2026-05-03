package com.clougence.clouddm.console.web.controller.editor.data;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_QUERY_CONSOLE;

import java.util.List;

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
import com.clougence.clouddm.console.web.model.fo.editor.data.ExecuteSqlFO;
import com.clougence.clouddm.console.web.model.fo.editor.data.GenerateDataFO;
import com.clougence.clouddm.console.web.model.fo.editor.data.SelectCountFO;
import com.clougence.clouddm.console.web.model.fo.editor.data.SelectDataFO;
import com.clougence.clouddm.console.web.model.vo.editor.data.DataEditorResultVO;
import com.clougence.clouddm.console.web.service.editor.DsDataEditorService;
import com.clougence.clouddm.console.web.service.editor.model.DataEditorChangeDTO;
import com.clougence.clouddm.console.web.service.editor.model.DataEditorExecuteResultDTO;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.model.analysis.resource.DsResPath;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.rdp.util.RdpAuthUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/editor/data")
@Slf4j
public class DataEditorController {

    @Resource
    private DsDataEditorService     dataEditorService;
    @Resource
    private DmDsConfigService       dmDsConfigService;
    @Resource
    private BizResOwnerCacheService ownerCacheService;
    @Resource
    private DmAuthServiceForBiz     dmAuthServiceForBiz;

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/fetchData", method = RequestMethod.POST)
    public ResWebData<?> fetchData(@Valid @RequestBody SelectDataFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        this.ownerCacheService.ownDataSource(puid, levels.getDsDO().getId());
        DsResPath dsResource = RdpAuthUtils.genResPathByList(levels.getDbLevels(), fo.getTargetName());
        this.dmAuthServiceForBiz.checkResPath(puid, uid, levels.getDsDO().getId(), AuthKind.DataSource, dsResource, SecDataAuthLabel.DM_DAUTH_QUERY);

        DataEditorResultVO vo = this.dataEditorService.fetchData(puid, uid, request.getRemoteAddr(), levels, fo);
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/fetchCount", method = RequestMethod.POST)
    public ResWebData<?> fetchCount(@Valid @RequestBody SelectCountFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        this.ownerCacheService.ownDataSource(puid, levels.getDsDO().getId());
        DsResPath dsResource = RdpAuthUtils.genResPathByList(levels.getDbLevels(), fo.getTargetName());
        this.dmAuthServiceForBiz.checkResPath(puid, uid, levels.getDsDO().getId(), AuthKind.DataSource, dsResource, SecDataAuthLabel.DM_DAUTH_QUERY);

        long res = this.dataEditorService.fetchCount(puid, uid, levels, fo, request.getRemoteAddr());
        return ResWebDataUtils.buildSuccess(res);
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/generateDml", method = RequestMethod.POST)
    public ResWebData<?> generateDml(@Valid @RequestBody GenerateDataFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        Long dsID = levels.getDsDO().getId();
        this.ownerCacheService.ownDataSource(puid, dsID);
        DsResPath dsResource = RdpAuthUtils.genResPathByList(levels.getDbLevels(), fo.getTargetName());
        String dataAuthLabel = SecDataAuthLabel.DM_DAUTH_DML;
        boolean checkAuth = this.dmAuthServiceForBiz.checkResPathWithoutError(puid, uid, dsID, AuthKind.DataSource, dsResource, dataAuthLabel);

        List<DataEditorChangeDTO> res = this.dataEditorService.generateDml(puid, uid, levels, fo);
        if (checkAuth) {
            return ResWebDataUtils.buildSuccess(res);
        } else {
            ResWebData<Object> data = RdpAuthUtils.missDataPermission(dsID, dsResource.getResPath(), dataAuthLabel);
            data.setData(res);
            return data;
        }
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/saveData", method = RequestMethod.POST)
    public ResWebData<?> saveData(@Valid @RequestBody ExecuteSqlFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        Long dsID = levels.getDsDO().getId();
        this.ownerCacheService.ownDataSource(puid, dsID);
        DsResPath dsResource = RdpAuthUtils.genResPathByList(levels.getDbLevels(), fo.getTargetName());
        String dataAuthLabel = SecDataAuthLabel.DM_DAUTH_DML;
        boolean checkAuth = this.dmAuthServiceForBiz.checkResPathWithoutError(puid, uid, dsID, AuthKind.DataSource, dsResource, dataAuthLabel);
        if (!checkAuth) {
            return RdpAuthUtils.missDataPermission(dsID, dsResource.getResPath(), dataAuthLabel);
        }

        DataEditorExecuteResultDTO result = this.dataEditorService.saveData(puid, uid, levels, fo, request.getRemoteAddr());

        if (result.getSuccess()) {
            return ResWebDataUtils.buildSuccess(result);
        } else {
            DataEditorExecuteResultDTO dto = new DataEditorExecuteResultDTO();
            dto.setMessage(result.getMessage());
            dto.setSuccess(result.getSuccess());
            return ResWebDataUtils.buildSuccess(dto);
        }
    }
}
