package com.clougence.clouddm.console.web.controller.devops;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_IM_MANAGE;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_IM_READ;

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

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.enumeration.ImType;
import com.clougence.clouddm.console.web.dal.model.DmMessengerDO;
import com.clougence.clouddm.console.web.dal.model.DmProjectMsgDO;
import com.clougence.clouddm.console.web.model.fo.project.DevopsImAddFO;
import com.clougence.clouddm.console.web.model.fo.project.DevopsImDeleteFO;
import com.clougence.clouddm.console.web.model.fo.project.DevopsImProviderListFO;
import com.clougence.clouddm.console.web.model.fo.project.DevopsImUpdateFO;
import com.clougence.clouddm.console.web.model.vo.project.DevopsImVO;
import com.clougence.clouddm.console.web.service.project.DmImService;
import com.clougence.clouddm.console.web.service.project.DmProjectService;
import com.clougence.clouddm.console.web.service.project.domain.DmImDef;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.constant.auth.RequestAuth.AuthStrategy;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode create time is 2021/1/5
 **/
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/devops/im")
@Slf4j
public class DmImController {

    @Resource
    private DmImService      dmImService;
    @Resource
    private DmProjectService dmProjectService;

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/defList", method = RequestMethod.POST)
    public ResWebData<?> defList(HttpServletRequest request) {
        List<Map<String, String>> services = new ArrayList<>();
        for (DmImDef scmDef : this.dmImService.getImDefList()) {
            Map<String, String> item = new HashMap<>();
            item.put("imType", scmDef.getImType().name());
            item.put("imTypeI18n", DmI18nUtils.getMessage(scmDef.getImType().getI18nKey()));
            item.put("helpUrl", scmDef.getHelpUrl());
            services.add(item);
        }
        return ResWebDataUtils.buildSuccess(services);
    }

    @RequestAuth(value = DM_IM_READ)
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResWebData<?> list(HttpServletRequest request, @Valid @RequestBody DevopsImProviderListFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        List<DmImDef> defList = this.dmImService.getImDefList();
        Map<ImType, DmImDef> defMap = defList.stream().collect(Collectors.toMap(DmImDef::getImType, d -> d));

        List<DmMessengerDO> imList = this.dmImService.queryMessengerByOwnerAndType(puid, fo.getImType());
        List<DevopsImVO> vos = imList.stream().map(scmDO -> {
            return DmConvertUtils.convertToDevopsImVO(scmDO, defMap);
        }).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(value = DM_IM_MANAGE)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResWebData<?> add(HttpServletRequest request, @Valid @RequestBody DevopsImAddFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        this.dmImService.addIm(puid, fo);
        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(value = DM_IM_MANAGE)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResWebData<?> delete(HttpServletRequest request, @Valid @RequestBody DevopsImDeleteFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmMessengerDO imDO = this.dmImService.queryImById(puid, fo.getImId());
        if (imDO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IM_NOT_EXIST_ERROR.name()));
        }

        if (!fo.isForce()) {
            List<DmProjectMsgDO> useList = this.dmProjectService.queryEnableDevopsByImId(puid, fo.getImId());
            if (!useList.isEmpty()) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IM_INUSE_ERROR.name(), imDO.getImDisplay()));
            }
        }

        this.dmImService.deleteImById(puid, fo.getImId());
        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(value = DM_IM_MANAGE)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResWebData<?> update(HttpServletRequest request, @Valid @RequestBody DevopsImUpdateFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmMessengerDO imDO = this.dmImService.queryImById(puid, fo.getImId());
        if (imDO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IM_NOT_EXIST_ERROR.name()));
        }

        // key config change
        if (StringUtils.isNotBlank(fo.getNewWebhookUrl())) {
            if (!fo.isForce()) {
                List<DmProjectMsgDO> useList = this.dmProjectService.queryEnableDevopsByImId(puid, fo.getImId());
                if (!useList.isEmpty()) {
                    throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IM_INUSE_ERROR.name(), imDO.getImDisplay()));
                }
            }
        }

        this.dmImService.updateImById(puid, fo);
        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(value = DM_IM_MANAGE)
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ResWebData<?> test(HttpServletRequest request, @Valid @RequestBody DevopsImAddFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        if (fo.getImId() != null) {
            DmMessengerDO messengerDO = this.dmImService.queryImById(puid, fo.getImId());
            fo.setImType(messengerDO.getImType());
            fo.setDisplay(messengerDO.getImDisplay());
            fo.setWebhookUrl(messengerDO.getWebhook());
            fo.setSecret(messengerDO.getSecret());
        }

        this.dmImService.testImByConfig(puid, fo);
        return ResWebDataUtils.buildSuccess(true);
    }
}
