package com.clougence.rdp.controller;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.RDP_AUTH_MANAGE;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.RDP_AUTH_READ;
import static com.clougence.rdp.constant.auth.RequestAuth.AuthStrategy.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.GlobalDeployMode;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.rdp.component.ticket.RdpTicketService;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.constant.auth.SecurityLevel;
import com.clougence.rdp.constant.operation.AuditType;
import com.clougence.rdp.controller.model.fo.BrowseAuthTreeFO;
import com.clougence.rdp.controller.model.fo.ListUserAuthResFO;
import com.clougence.rdp.controller.model.fo.security.*;
import com.clougence.rdp.controller.model.fo.ticket.RdpTicketBasicVO;
import com.clougence.rdp.controller.model.http.RdpControllerUrlPrefix;
import com.clougence.rdp.controller.model.vo.RdpAuthObjectVO;
import com.clougence.rdp.controller.model.vo.ResAuthVO;
import com.clougence.rdp.controller.model.vo.RoleAuthTreeVO;
import com.clougence.rdp.dal.model.RdpResAuthDO;
import com.clougence.clouddm.sdk.security.auth.AuthElementType;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.rdp.service.RdpAuthServiceForBiz;
import com.clougence.rdp.service.RdpAuthServiceForManage;
import com.clougence.rdp.service.RdpOpAuditService;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.rdp.util.RdpAuthUtils;
import com.clougence.rdp.util.RdpConvertUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/5
 **/
@RestController
@RequestMapping(value = RdpControllerUrlPrefix.CONSOLE_PREFIX + "/auth")
@Slf4j
public class RdpResAuthController {

    @Resource
    private RdpAuthServiceForManage rdpAuthServiceForManage;
    @Resource
    private RdpAuthServiceForBiz    rdpAuthServiceForBiz;
    @Resource
    private RdpOpAuditService       rdpOpAuditService;
    @Resource
    private RdpTicketService        rdpTicketService;

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/listElementsOfLevel", method = RequestMethod.POST)
    public ResWebData<?> listElementsOfLevel(@Valid @RequestBody ListElementsOfLevelFO levelsFO, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        List<RdpAuthObjectVO> result = this.rdpAuthServiceForManage.listElements(puid, levelsFO.getResPaths(), levelsFO.getAuthKind());
        return ResWebDataUtils.buildSuccess(result);
    }

    // --------------------------------
    //      for Auth Manage
    // --------------------------------
    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/fetchAuthTreeDef", method = RequestMethod.POST)
    public ResWebData<?> fetchAuthTreeDef(@Valid @RequestBody BrowseAuthTreeFO fo) {
        AuthKind authKind = fo.getKind();
        AuthElementType elementType = fo.getElementType();
        DataSourceType dsType = fo.getDsType();
        List<AuthInfo> allCategory = this.rdpAuthServiceForManage.getAllCategory();
        List<AuthInfo> allLabels = this.rdpAuthServiceForManage.getAllAuthLabelForAuthTreeDef(authKind, elementType, dsType);
        List<RoleAuthTreeVO> treeList = RdpConvertUtils.convertToResourceAuthTree(allCategory, allLabels);
        return ResWebDataUtils.buildSuccess(treeList);
    }

    // --------------------------------
    //      for Auth Manage
    // --------------------------------
    @RequestAuth(RDP_AUTH_READ)
    @RequestMapping(value = "/listUserAuthOfRes", method = RequestMethod.POST)
    public ResWebData<List<ResAuthVO>> listUserAuthOfRes(@Valid @RequestBody ListUserAuthOfResFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.rdpAuthServiceForBiz.checkOperateOtherUserAuth(uid, fo.getTargetUid());

        Map<Long, List<ListAuthOfResGroupFO>> sameResId = new HashMap<>();
        for (ListAuthOfResGroupFO authFO : fo.getGroups()) {
            long resId = authFO.getResId();
            if (!sameResId.containsKey(resId)) {
                sameResId.put(resId, new ArrayList<>());
            }

            sameResId.get(resId).add(authFO);
        }

        List<RdpResAuthDO> authList = new ArrayList<>();
        for (Long resId : sameResId.keySet()) {
            List<ListAuthOfResGroupFO> batch = sameResId.get(resId);
            List<String> authPrefixList = batch.stream().map(authFO -> RdpAuthUtils.genResPathByList(authFO.getResPaths()).getResPath()).collect(Collectors.toList());
            List<RdpResAuthDO> data = this.rdpAuthServiceForManage.listUserAuthByRes(fo.getTargetUid(), resId, authPrefixList, fo.getAuthKind());
            authList.addAll(data);
        }

        List<ResAuthVO> collect = authList.stream().map(RdpConvertUtils::convertToResAuthVO).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(collect);
    }

    // --------------------------------
    //      for Auth Manage
    // --------------------------------
    @RequestAuth(RDP_AUTH_READ)
    @RequestMapping(value = "/listUserAuthRes", method = RequestMethod.POST)
    public ResWebData<List<ResAuthVO>> listUserAuthRes(@Valid @RequestBody ListUserAuthResFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);

        rdpAuthServiceForBiz.checkOperateOtherUserAuth(uid, fo.getTargetUid());

        List<RdpResAuthDO> data = this.rdpAuthServiceForManage.listUserAuthWithoutLabels(fo.getTargetUid(), fo.getAuthKind());

        List<ResAuthVO> collect = data.stream().map(RdpConvertUtils::convertToResAuthVO).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(collect);
    }

    // --------------------------------
    //      for Auth Manage
    // --------------------------------
    @RequestAuth(checkOpPassword = true, value = RDP_AUTH_MANAGE)
    @RequestMapping(value = "/modifyUserAuth", method = RequestMethod.POST)
    public ResWebData<?> modifyUserAuth(@Valid @RequestBody ModifyUserAuthFO fo, HttpServletRequest request) {
        if (GlobalDeployMode.inCloud()) {
            throw new UnsupportedOperationException("This operation not supported in CLOUD mode.");
        }

        String uid = (String) request.getAttribute(RdpUserService.UID);
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        rdpAuthServiceForBiz.checkOperateOtherUserAuth(uid, fo.getTargetUid());

        rdpAuthServiceForManage.modifyUserAuth(puid, fo);

        rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), fo
            .getTargetUid(), fo, SecurityLevel.HIGH, AuditType.MODIFY_SUB_ACCOUNT_AUTH, ResourceType.ACCOUNT);

        return ResWebDataUtils.buildSuccess(true);
    }

    // --------------------------------
    //      For login user self query
    // --------------------------------
    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/listMyAuthOfRes", method = RequestMethod.POST)
    public ResWebData<List<ResAuthVO>> listMyAuthOfRes(@Valid @RequestBody ListMyAuthOfResFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);

        Map<Long, List<ListAuthOfResGroupFO>> sameResId = new HashMap<>();
        for (ListAuthOfResGroupFO authFO : fo.getGroups()) {
            long resId = authFO.getResId();
            if (!sameResId.containsKey(resId)) {
                sameResId.put(resId, new ArrayList<>());
            }

            sameResId.get(resId).add(authFO);
        }

        List<RdpResAuthDO> authList = new ArrayList<>();
        for (Long resId : sameResId.keySet()) {
            List<ListAuthOfResGroupFO> batch = sameResId.get(resId);
            List<String> authPrefixList = batch.stream().map(authFO -> RdpAuthUtils.genResPathByList(authFO.getResPaths()).getResPath()).collect(Collectors.toList());
            List<RdpResAuthDO> data = this.rdpAuthServiceForManage.listUserAuthByRes(uid, resId, authPrefixList, fo.getAuthKind());
            authList.addAll(data);
        }

        List<ResAuthVO> collect = authList.stream().map(RdpConvertUtils::convertToResAuthVO).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(collect);
    }

    // --------------------------------
    //      For login user self query
    // --------------------------------
    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/listMyAuthRes", method = RequestMethod.POST)
    public ResWebData<List<ResAuthVO>> listMyAuthRes(@Valid @RequestBody ListMyAuthResFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);

        List<RdpResAuthDO> data = this.rdpAuthServiceForManage.listUserAuthWithoutLabels(uid, fo.getAuthKind());
        List<ResAuthVO> collect = data.stream().map(RdpConvertUtils::convertToResAuthVO).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(collect);
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/listMyAuthTicket", method = RequestMethod.POST)
    public ResWebData<?> listMyAuthTicket(@Valid @RequestBody ListMyAuthTicketFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        fo.setUid(uid);
        IPage<RdpTicketBasicVO> data = this.rdpTicketService.queryAuthTicketListByPage(uid, fo);
        return ResWebDataUtils.buildSuccess(data);
    }

    @RequestAuth(checkOpPassword = true, value = RDP_AUTH_MANAGE)
    @RequestMapping(value = "/checkResourceManger", method = RequestMethod.POST)
    public ResWebData<Boolean> checkResourceManger(@RequestBody CheckResourceMangerFO fo) {
        return ResWebDataUtils.buildSuccess(rdpAuthServiceForManage.isResourceMangerEnable(fo.getTargetUid()));
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/checkMyResourceManger", method = RequestMethod.POST)
    public ResWebData<Boolean> checkMyResourceManger(HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        return ResWebDataUtils.buildSuccess(rdpAuthServiceForManage.isResourceMangerEnable(uid));
    }
}
