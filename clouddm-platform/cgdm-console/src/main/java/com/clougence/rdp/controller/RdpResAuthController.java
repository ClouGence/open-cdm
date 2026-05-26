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
package com.clougence.rdp.controller;

import static com.clougence.clouddm.console.web.global.jwtsession.RequestAuth.AuthStrategy.Ignore;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.RDP_AUTH_MANAGE;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.RDP_AUTH_READ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.clouddm.console.web.dal.model.DmResAuthDO;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.global.jwtsession.SecurityLevel;
import com.clougence.clouddm.console.web.model.fo.BrowseAuthTreeFO;
import com.clougence.clouddm.console.web.model.fo.ListUserAuthResFO;
import com.clougence.clouddm.console.web.model.fo.security.*;
import com.clougence.clouddm.console.web.model.fo.ticket.RdpTicketBasicVO;
import com.clougence.clouddm.console.web.model.vo.RdpAuthObjectVO;
import com.clougence.clouddm.console.web.model.vo.ResAuthVO;
import com.clougence.clouddm.console.web.model.vo.role.RoleAuthTreeVO;
import com.clougence.clouddm.console.web.service.approval.ApprovalControlService;
import com.clougence.clouddm.console.web.util.RdpAuthUtils;
import com.clougence.clouddm.console.web.util.RdpConvertUtils;
import com.clougence.clouddm.sdk.security.auth.AuthElementType;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.rdp.constant.RdpControllerUrlPrefix;
import com.clougence.rdp.constant.operation.AuditType;
import com.clougence.rdp.service.RdpAuthServiceForBiz;
import com.clougence.rdp.service.RdpAuthServiceForManage;
import com.clougence.rdp.service.RdpOpAuditService;
import com.clougence.rdp.service.RdpUserService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/5
 **/
@RestController
@RequestMapping(value = RdpControllerUrlPrefix.CONSOLE_PREFIX + "/auth")
@Slf4j
public class RdpResAuthController {

    @Resource
    private RdpAuthServiceForManage authServiceForManage;
    @Resource
    private RdpAuthServiceForBiz    authServiceForBiz;
    @Resource
    private RdpOpAuditService       opAuditService;
    @Resource
    private ApprovalControlService  approvalControlService;

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/listElementsOfLevel", method = RequestMethod.POST)
    public ResWebData<?> listElementsOfLevel(@Valid @RequestBody ListElementsOfLevelFO levelsFO, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        List<RdpAuthObjectVO> result = this.authServiceForManage.listElements(puid, levelsFO.getResPaths(), levelsFO.getAuthKind());
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
        List<AuthInfo> allCategory = this.authServiceForManage.getAllCategory();
        List<AuthInfo> allLabels = this.authServiceForManage.getAllAuthLabelForAuthTreeDef(authKind, elementType, dsType);
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

        this.authServiceForBiz.checkOperateOtherUserAuth(uid, fo.getTargetUid());

        Map<Long, List<ListAuthOfResGroupFO>> sameResId = new HashMap<>();
        for (ListAuthOfResGroupFO authFO : fo.getGroups()) {
            long resId = authFO.getResId();
            if (!sameResId.containsKey(resId)) {
                sameResId.put(resId, new ArrayList<>());
            }

            sameResId.get(resId).add(authFO);
        }

        List<DmResAuthDO> authList = new ArrayList<>();
        for (Long resId : sameResId.keySet()) {
            List<ListAuthOfResGroupFO> batch = sameResId.get(resId);
            List<String> authPrefixList = batch.stream().map(authFO -> RdpAuthUtils.genResPathByList(authFO.getResPaths()).getResPath()).collect(Collectors.toList());
            List<DmResAuthDO> data = this.authServiceForManage.listUserAuthByRes(fo.getTargetUid(), resId, authPrefixList, fo.getAuthKind());
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

        authServiceForBiz.checkOperateOtherUserAuth(uid, fo.getTargetUid());

        List<DmResAuthDO> data = this.authServiceForManage.listUserAuthWithoutLabels(fo.getTargetUid(), fo.getAuthKind());

        List<ResAuthVO> collect = data.stream().map(RdpConvertUtils::convertToResAuthVO).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(collect);
    }

    // --------------------------------
    //      for Auth Manage
    // --------------------------------
    @RequestAuth(checkOpPassword = true, value = RDP_AUTH_MANAGE)
    @RequestMapping(value = "/modifyUserAuth", method = RequestMethod.POST)
    public ResWebData<?> modifyUserAuth(@Valid @RequestBody ModifyUserAuthFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        authServiceForBiz.checkOperateOtherUserAuth(uid, fo.getTargetUid());

        authServiceForManage.modifyUserAuth(puid, fo);

        opAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), fo
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

        List<DmResAuthDO> authList = new ArrayList<>();
        for (Long resId : sameResId.keySet()) {
            List<ListAuthOfResGroupFO> batch = sameResId.get(resId);
            List<String> authPrefixList = batch.stream().map(authFO -> RdpAuthUtils.genResPathByList(authFO.getResPaths()).getResPath()).collect(Collectors.toList());
            List<DmResAuthDO> data = this.authServiceForManage.listUserAuthByRes(uid, resId, authPrefixList, fo.getAuthKind());
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

        List<DmResAuthDO> data = this.authServiceForManage.listUserAuthWithoutLabels(uid, fo.getAuthKind());
        List<ResAuthVO> collect = data.stream().map(RdpConvertUtils::convertToResAuthVO).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(collect);
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/listMyAuthTicket", method = RequestMethod.POST)
    public ResWebData<?> listMyAuthTicket(@Valid @RequestBody ListMyAuthTicketFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        fo.setUid(uid);
        IPage<RdpTicketBasicVO> data = this.approvalControlService.queryAuthTicketListByPage(uid, fo);
        return ResWebDataUtils.buildSuccess(data);
    }

    @RequestAuth(checkOpPassword = true, value = RDP_AUTH_MANAGE)
    @RequestMapping(value = "/checkResourceManger", method = RequestMethod.POST)
    public ResWebData<Boolean> checkResourceManger(@RequestBody CheckResourceMangerFO fo) {
        return ResWebDataUtils.buildSuccess(authServiceForManage.isResourceMangerEnable(fo.getTargetUid()));
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/checkMyResourceManger", method = RequestMethod.POST)
    public ResWebData<Boolean> checkMyResourceManger(HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        return ResWebDataUtils.buildSuccess(authServiceForManage.isResourceMangerEnable(uid));
    }
}
