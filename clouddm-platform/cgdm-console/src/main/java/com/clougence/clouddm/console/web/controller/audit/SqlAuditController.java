package com.clougence.clouddm.console.web.controller.audit;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_SQL_AUDIT;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.model.fo.audit.SqlAuditFO;
import com.clougence.clouddm.console.web.model.fo.project.GuideUsersFO;
import com.clougence.clouddm.console.web.model.vo.audit.OperateUserVO;
import com.clougence.clouddm.console.web.model.vo.audit.SqlAuditVO;
import com.clougence.clouddm.console.web.model.vo.browse.BrowseLevelsVO;
import com.clougence.clouddm.console.web.service.audit.SqlAuditService;
import com.clougence.clouddm.console.web.service.browse.BrowseService;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.dal.mapper.RdpUserMapper;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.dal.model.RdpUserInfoDO;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/audit/sqlAudit")
@Slf4j
public class SqlAuditController {

    @Resource
    private SqlAuditService sqlAuditService;
    @Resource
    private BrowseService   browseService;
    @Resource
    private RdpUserMapper   rdpUserMapper;

    @RequestAuth(DM_SQL_AUDIT)
    @RequestMapping(value = "/queryAll", method = RequestMethod.POST)
    public ResWebData<?> queryAll(@Valid @RequestBody SqlAuditFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        List<SqlAuditVO> sqlAuditVOS = sqlAuditService.queryUserAllAudit(puid, fo.getUserUid(), fo.getSqlKind(), fo.getResourcePath(), fo.getDsId(), fo.getRequester(), fo
            .getStatus(), fo.getOpStart(), fo.getOpEnd(), fo.getPageData().getStartId(), fo.getPageData().getPageSize());

        return ResWebDataUtils.buildSuccess(sqlAuditVOS);
    }

    @RequestAuth(DM_SQL_AUDIT)
    @RequestMapping(value = "/listDs", method = RequestMethod.POST)
    public ResWebData<?> listDs(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        // ds list
        List<BrowseLevelsVO> levels = this.browseService.listDsIncludeAllEnv(puid, uid);
        return ResWebDataUtils.buildSuccess(levels);
    }

    @RequestAuth(DM_SQL_AUDIT)
    @RequestMapping(value = "/operateUser", method = RequestMethod.POST)
    public ResWebData<?> operateUser(HttpServletRequest request, @Valid @RequestBody GuideUsersFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        RdpUserDO mainUser = this.rdpUserMapper.queryByUid(puid);
        String search = StringUtils.isBlank(fo.getSearch()) ? null : fo.getSearch();
        List<RdpUserInfoDO> result = this.rdpUserMapper.searchUserByKeywords(mainUser.getUserDomain(), search);
        List<OperateUserVO> vos = result.stream().map(DmConvertUtils::convertToOperateUserVO).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(vos);
    }
}
