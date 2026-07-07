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

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.RDP_OP_AUDIT_EXPORT;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.RDP_OP_AUDIT_READ;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.fo.ExportOpAuditFO;
import com.clougence.clouddm.console.web.model.fo.QueryOpAuditByNameFO;
import com.clougence.clouddm.console.web.model.fo.QueryOpAuditFO;
import com.clougence.clouddm.console.web.model.vo.RdpOpAuditVO;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.rdp.service.RdpOpAuditService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020/4/13 14:49
 */
@RestController
@Slf4j
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/audit")
public class RdpOpAuditController {

    @Resource
    private RdpOpAuditService auditService;
    @Resource
    private RdpUserService    rdpUserService;

    @RequestAuth(RDP_OP_AUDIT_READ)
    @RequestMapping(value = "/queryAll", method = RequestMethod.POST)
    public ResWebData<?> queryAll(@RequestBody @Valid QueryOpAuditFO auditFO, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        List<RdpOpAuditVO> auditVos = auditService.queryUserAllAudit(puid, auditFO.getUid(), auditFO.getSecurityLevel(), auditFO.getUserNameLike(), auditFO.getAuditType(), auditFO
            .getResourceType(), auditFO.getOpStart(), auditFO.getOpEnd(), auditFO.getPageData().getStartId(), auditFO.getPageData().getPageSize());

        return ResWebDataUtils.buildSuccess(auditVos);
    }

    @RequestAuth(RDP_OP_AUDIT_READ)
    @RequestMapping(value = "/queryByUser", method = RequestMethod.POST)
    public ResWebData<?> queryByUser(@RequestBody @Valid QueryOpAuditFO auditFO, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        List<RdpOpAuditVO> auditVos = auditService.queryUserAllAudit(puid, auditFO.getUid(), auditFO.getSecurityLevel(), auditFO.getUserNameLike(), auditFO.getAuditType(), auditFO
            .getResourceType(), auditFO.getOpStart(), auditFO.getOpEnd(), auditFO.getPageData().getStartId(), auditFO.getPageData().getPageSize());
        return ResWebDataUtils.buildSuccess(auditVos);
    }

    @RequestAuth(RDP_OP_AUDIT_READ)
    @RequestMapping(value = "/queryByUserName", method = RequestMethod.POST)
    public ResWebData<?> queryByUserName(@RequestBody @Valid QueryOpAuditByNameFO auditByNameFO, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        List<DmAuthUserDO> subAccounts = rdpUserService.listSubAccounts(puid);
        if (subAccounts == null) {
            subAccounts = new ArrayList<>();
        }

        List<String> uids = subAccounts.stream().map(DmAuthUserDO::getUid).collect(Collectors.toList());
        uids.add(puid);

        List<RdpOpAuditVO> auditVos = auditService
            .findAuditByUserName(puid, auditByNameFO.getUserName(), auditByNameFO.getSecurityLevel(), auditByNameFO.getAuditType(), auditByNameFO.getResourceType(), auditByNameFO
                .getOpStart(), auditByNameFO.getOpEnd(), auditByNameFO.getPageData().getStartId(), auditByNameFO.getPageData().getPageSize());
        return ResWebDataUtils.buildSuccess(auditVos);
    }

    @RequestAuth({ RDP_OP_AUDIT_READ })
    @RequestMapping(value = "/queryListCondition", method = RequestMethod.POST)
    public ResWebData<?> queryListCondition() {
        return ResWebDataUtils.buildSuccess(auditService.queryListCondition());
    }

    @RequestAuth({ RDP_OP_AUDIT_EXPORT })
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public void export(@RequestBody() @Valid ExportOpAuditFO fo, HttpServletRequest request, HttpServletResponse response) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        fo.setPuid(puid);
        fo.setRequesterUid(uid);
        auditService.exportAuditLog(fo, response);
    }
}
