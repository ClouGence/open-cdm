package com.clougence.rdp.controller;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.RDP_ENV_MANAGE;
import static com.clougence.rdp.constant.auth.RequestAuth.AuthStrategy.Ignore;
import static com.clougence.rdp.constant.auth.SecurityLevel.HIGH;

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
import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.constant.auth.SecurityLevel;
import com.clougence.rdp.constant.operation.AuditType;
import com.clougence.rdp.controller.model.fo.AddDsEnvFO;
import com.clougence.rdp.controller.model.fo.DeleteDsEnvFO;
import com.clougence.rdp.controller.model.fo.ListAllDsEnvFO;
import com.clougence.rdp.controller.model.fo.UpdateDsEnvFO;
import com.clougence.rdp.controller.model.http.RdpControllerUrlPrefix;
import com.clougence.rdp.controller.model.lo.UpdateDsEnvLO;
import com.clougence.rdp.controller.model.vo.DsEnvVO;
import com.clougence.rdp.dal.model.RdpDsEnvDO;
import com.clougence.rdp.service.RdpDsEnvService;
import com.clougence.rdp.service.RdpOpAuditService;
import com.clougence.rdp.service.RdpUserService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/18
 **/
@RestController
@RequestMapping(value = RdpControllerUrlPrefix.CONSOLE_PREFIX + "/dsenv")
@Slf4j
public class RdpDsEnvController {

    @Resource
    private RdpDsEnvService   rdpDsEnvService;
    @Resource
    private RdpOpAuditService rdpOpAuditService;

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResWebData<?> list(@RequestBody @Valid ListAllDsEnvFO listAllDsEnvFO, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        List<RdpDsEnvDO> dsEnvDOs = this.rdpDsEnvService.listDsEnv(puid, uid, listAllDsEnvFO.getEnvName());
        return ResWebDataUtils.buildSuccess(DsEnvVO.generateVO(dsEnvDOs));
    }

    @RequestAuth(level = HIGH, value = RDP_ENV_MANAGE)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResWebData<?> addDsEnv(@RequestBody @Valid AddDsEnvFO addDsEnvFO, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        RdpDsEnvDO dsEnvDO = new RdpDsEnvDO();
        dsEnvDO.setOwnerUid(puid);
        dsEnvDO.setEnvName(addDsEnvFO.getEnvName());
        dsEnvDO.setDescription(addDsEnvFO.getDescription());

        int affectRows = rdpDsEnvService.addEnvDs(puid, uid, dsEnvDO);

        this.rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), dsEnvDO
            .getId(), addDsEnvFO, SecurityLevel.HIGH, AuditType.ADD_DS_ENV, ResourceType.DS_ENV);

        return ResWebDataUtils.buildSuccess(affectRows);
    }

    @RequestAuth(level = HIGH, value = RDP_ENV_MANAGE)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResWebData<?> update(@RequestBody @Valid UpdateDsEnvFO updateDsEnvFO, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        UpdateDsEnvLO updateDsEnvLO = rdpDsEnvService.updateDsEnv(puid, uid, updateDsEnvFO);

        this.rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), updateDsEnvFO
            .getDsEnvId(), updateDsEnvLO, SecurityLevel.HIGH, AuditType.UPDATE_DS_ENV, ResourceType.DS_ENV);

        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = RDP_ENV_MANAGE)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResWebData<?> delete(@RequestBody @Valid DeleteDsEnvFO deleteDsEnvFO, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        Long dsEnvId = deleteDsEnvFO.getDsEnvId();
        RdpDsEnvDO rdpDsEnvDO = rdpDsEnvService.queryByUserAndId(puid, uid, dsEnvId);
        int affectRows = this.rdpDsEnvService.deleteDsEnv(puid, uid, dsEnvId);

        this.rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request
            .getRemoteAddr(), dsEnvId, deleteDsEnvFO, SecurityLevel.HIGH, AuditType.DELETE_DS_ENV, ResourceType.DS_ENV, rdpDsEnvDO.getEnvName());

        return ResWebDataUtils.buildSuccess("Success delete " + affectRows + " rows");
    }
}
