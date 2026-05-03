//package com.clougence.rdp.controller;
//
//import static com.clougence.rdp.sdk.auth.dm.RdpRoleAuthLabel.RDP_OP_AUDIT_EXPORT;
//import static com.clougence.rdp.sdk.auth.dm.RdpRoleAuthLabel.RDP_OP_AUDIT_READ;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.validation.Valid;
//
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.clougence.rdp.constant.auth.RequestAuth;
//import com.clougence.rdp.controller.model.fo.ExportOpAuditFO;
//import com.clougence.rdp.controller.model.fo.QueryOpAuditByNameFO;
//import com.clougence.rdp.controller.model.fo.QueryOpAuditFO;
//import com.clougence.rdp.controller.model.fo.QueryUserOpAuditFO;
//import com.clougence.rdp.controller.model.http.RdpControllerUrlPrefix;
//import com.clougence.rdp.controller.model.vo.RdpOpAuditVO;
//import com.clougence.rdp.dal.model.RdpUserDO;
//import com.clougence.clouddm.api.common.rpc.ResWebData;
//import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
//import com.clougence.rdp.service.RdpAuthServiceForBiz;
//import com.clougence.rdp.service.RdpOpAuditService;
//import com.clougence.rdp.service.RdpUserService;
//
//import lombok.extern.slf4j.Slf4j;
//
///**
// * @author bucketli 2020/4/13 14:49
// */
//@RestController
//@Slf4j
//@RequestMapping(value = RdpControllerUrlPrefix.CONSOLE_PREFIX + "/audit")
//public class RdpOpAuditController {
//
//    @Resource
//    private RdpOpAuditService    auditService;
//
//    @Resource
//    private RdpUserService       rdpUserService;
//
//    @Resource
//    private RdpAuthServiceForBiz rdpAuthServiceForBiz;
//
//    @RequestAuth(RDP_OP_AUDIT_READ)
//    @RequestMapping(value = "/ctrl_queryAll", method = RequestMethod.POST)
//    public ResWebData<?> ctrlQueryAll() {
//        return ResWebDataUtils.buildSuccess();
//    }
//
//    @RequestAuth(RDP_OP_AUDIT_READ)
//    @RequestMapping(value = "/queryall", method = RequestMethod.POST)
//    public ResWebData<?> queryAll(@RequestBody @Valid QueryOpAuditFO auditFO, HttpServletRequest request) {
//        String puid = (String) request.getAttribute(RdpUserService.PUID);
//        List<RdpOpAuditVO> auditVos = auditService.queryUserAllAudit(puid, auditFO.getUid(), auditFO.getSecurityLevel(), auditFO.getUserNameLike(), auditFO.getAuditType(), auditFO
//            .getResourceType(), auditFO.getOpStart(), auditFO.getOpEnd(), auditFO.getPageData().getStartId(), auditFO.getPageData().getPageSize());
//
//        return ResWebDataUtils.buildSuccess(auditVos);
//    }
//
//    @RequestAuth(RDP_OP_AUDIT_READ)
//    @RequestMapping(value = "/querybyuid", method = RequestMethod.POST)
//    public ResWebData<?> getByUid(@RequestBody @Valid QueryUserOpAuditFO auditFO, HttpServletRequest request) {
//        String uid = (String) request.getAttribute(RdpUserService.UID);
//
//        rdpAuthServiceForBiz.checkOperateOtherUserAuth(uid, auditFO.getUid());
//
//        List<RdpOpAuditVO> auditVos = auditService.findAuditByUid(auditFO.getUid(), auditFO.getSecurityLevel(), auditFO.getAuditType(), auditFO.getResourceType(), auditFO
//            .getOpStart(), auditFO.getOpEnd(), auditFO.getPageData().getStartId(), auditFO.getPageData().getPageSize());
//        return ResWebDataUtils.buildSuccess(auditVos);
//    }
//
//    @RequestAuth(RDP_OP_AUDIT_READ)
//    @RequestMapping(value = "/querybyusername", method = RequestMethod.POST)
//    public ResWebData<?> getByUserName(@RequestBody @Valid QueryOpAuditByNameFO auditByNameFO, HttpServletRequest request) {
//        String puid = (String) request.getAttribute(RdpUserService.PUID);
//        List<RdpUserDO> subAccounts = rdpUserService.listSubAccounts(puid);
//        if (subAccounts == null) {
//            subAccounts = new ArrayList<>();
//        }
//
//        List<String> uids = subAccounts.stream().map(RdpUserDO::getUid).collect(Collectors.toList());
//        uids.add(puid);
//
//        List<RdpOpAuditVO> auditVos = auditService
//            .findAuditByUserName(puid, auditByNameFO.getUserName(), auditByNameFO.getSecurityLevel(), auditByNameFO.getAuditType(), auditByNameFO.getResourceType(), auditByNameFO
//                .getOpStart(), auditByNameFO.getOpEnd(), auditByNameFO.getPageData().getStartId(), auditByNameFO.getPageData().getPageSize());
//        return ResWebDataUtils.buildSuccess(auditVos);
//    }
//
//    @RequestAuth({ RDP_OP_AUDIT_READ })
//    @RequestMapping(value = "/querylistcondition", method = RequestMethod.POST)
//    public ResWebData<?> queryListCondition() {
//        return ResWebDataUtils.buildSuccess(auditService.queryListCondition(RdpOpAuditService.QUERY_CONDITION_RDP));
//    }
//
//    @RequestAuth({ RDP_OP_AUDIT_EXPORT })
//    @RequestMapping(value = "/export", method = RequestMethod.POST)
//    public void export(@RequestBody() @Valid ExportOpAuditFO exportOpAuditFO, HttpServletRequest request, HttpServletResponse response) {
//        String puid = (String) request.getAttribute(RdpUserService.PUID);
//        exportOpAuditFO.setPuid(puid);
//        auditService.exportAuditLog(exportOpAuditFO, response);
//    }
//}
