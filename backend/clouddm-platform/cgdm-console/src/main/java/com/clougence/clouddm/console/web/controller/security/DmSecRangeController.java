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
package com.clougence.clouddm.console.web.controller.security;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_SECRULES_MANAGE;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_SECRULES_READ;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.detectrule.domain.SecRange;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.fo.browse.BrowseDetailFO;
import com.clougence.clouddm.console.web.model.fo.browse.BrowseLeafFO;
import com.clougence.clouddm.console.web.model.fo.browse.BrowseLevelsFO;
import com.clougence.clouddm.console.web.model.fo.checkrules.RangeDeleteFO;
import com.clougence.clouddm.console.web.model.fo.checkrules.RangeListInsFo;
import com.clougence.clouddm.console.web.model.fo.checkrules.SpecFetchRangeFO;
import com.clougence.clouddm.console.web.model.fo.checkrules.SpecSaveRangeFO;
import com.clougence.clouddm.console.web.model.vo.browse.BrowseLevelsVO;
import com.clougence.clouddm.console.web.model.vo.checkrules.RangeObjectVO;
import com.clougence.clouddm.console.web.model.vo.checkrules.RangeVO;
import com.clougence.clouddm.console.web.model.vo.checkrules.SpecUpdateVO;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.browse.BrowseService;
import com.clougence.clouddm.console.web.service.browse.model.rdb.BrowseColumnMO;
import com.clougence.clouddm.console.web.service.envparam.DmEnvParamService;
import com.clougence.clouddm.console.web.service.security.CheckRulesService;
import com.clougence.clouddm.console.web.service.security.mode.DmSecRuleMO;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.console.web.util.RdpConvertUtils;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.model.secrule.DmSecRefererDO;
import com.clougence.clouddm.platform.dal.model.secrule.DmSecSpecDO;
import com.clougence.clouddm.platform.dal.model.secrule.RuleKind;
import com.clougence.clouddm.platform.dal.model.secrule.SecRangeType;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvDO;
import com.clougence.clouddm.sdk.model.env.EnvParamKeys;
import com.clougence.rdp.service.RdpDsEnvService;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode create time is 2021/2/25
 **/
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/security/range")
@Slf4j
public class DmSecRangeController {

    @Resource
    private CheckRulesService checkRulesService;
    @Resource
    private DmEnvParamService dmEnvParamService;
    @Resource
    private RdpDsEnvService   rdpDsEnvService;
    @Resource
    private BrowseService     browseService;
    @Resource
    private DmDsConfigService dmDsConfigService;
    @Resource
    private ObjectCacheDao    objectCacheDao;

    @RequestAuth(value = DM_SECRULES_READ)
    @RequestMapping(value = "/specFetchRange", method = RequestMethod.POST)
    public ResWebData<?> specFetchRange(@RequestBody @Valid SpecFetchRangeFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmSecRuleMO ruleMO = this.checkRulesService.queryRuleById(puid, fo.getRuleId(), fo.getRuleKind());
        if (ruleMO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_NOT_EXIST_ERROR.name()));
        }

        DmSecRefererDO refererDO = this.checkRulesService.querySpecRefererById(puid, fo.getSpecId(), fo.getRuleId(), fo.getRuleKind());
        if (refererDO == null) {
            return ResWebDataUtils.buildSuccess(Collections.emptyList());
        }

        List<SecRange> infos = this.checkRulesService.fetchRangeByRef(puid, refererDO.getId());
        List<RangeVO> collect = infos.stream().map(DmConvertUtils::convertToRangeVO).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(collect);
    }

    @RequestAuth(value = DM_SECRULES_MANAGE)
    @RequestMapping(value = "/specSaveRange", method = RequestMethod.POST)
    public ResWebData<?> specSaveRange(@RequestBody @Valid SpecSaveRangeFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmSecSpecDO specDO = this.checkRulesService.querySpecById(puid, fo.getSpecId());
        if (specDO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_SPEC_NOT_EXIST_ERROR.name()));
        }

        SpecUpdateVO vo = new SpecUpdateVO();
        List<DmSysEnvDO> envs = this.dmEnvParamService.queryListByParamKeyValue(puid, EnvParamKeys.DM_BIND_CHECK_SPEC, String.valueOf(fo.getSpecId()));
        if (!envs.isEmpty()) {
            vo.setMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_SPEC_INUSE_MESSAGE.name(), specDO.getName()));
            vo.setReferer(envs.stream().map(DmConvertUtils::convertToRefEnvVO).collect(Collectors.toList()));
        }

        if (!envs.isEmpty() && !fo.isForce()) {
            vo.setSuccess(false);
            return ResWebDataUtils.buildSuccess(vo);
        } else {
            vo.setSuccess(true);
            vo.setMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_SPEC_UPDATE_FINISH_MESSAGE.name(), specDO.getName()));
            this.checkRulesService.saveRange(puid, fo);
            return ResWebDataUtils.buildSuccess(vo);
        }
    }

    @RequestAuth(value = DM_SECRULES_MANAGE)
    @RequestMapping(value = "/specDeleteRange", method = RequestMethod.POST)
    public ResWebData<?> specDeleteRange(@RequestBody @Valid RangeDeleteFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmSecSpecDO specDO = this.checkRulesService.querySpecById(puid, fo.getSpecId());
        if (specDO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_SPEC_NOT_EXIST_ERROR.name()));
        }

        SpecUpdateVO vo = new SpecUpdateVO();
        List<DmSysEnvDO> envs = this.dmEnvParamService.queryListByParamKeyValue(puid, EnvParamKeys.DM_BIND_CHECK_SPEC, String.valueOf(fo.getSpecId()));
        if (!envs.isEmpty()) {
            vo.setMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_SPEC_INUSE_MESSAGE.name(), specDO.getName()));
            vo.setReferer(envs.stream().map(DmConvertUtils::convertToRefEnvVO).collect(Collectors.toList()));
        }

        if (!envs.isEmpty() && !fo.isForce()) {
            vo.setSuccess(false);
            return ResWebDataUtils.buildSuccess(vo);
        } else {
            vo.setSuccess(true);
            vo.setMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_SPEC_UPDATE_FINISH_MESSAGE.name(), specDO.getName()));
            this.checkRulesService.deleteRange(puid, fo);
            return ResWebDataUtils.buildSuccess(vo);
        }
    }

    @RequestAuth(value = DM_SECRULES_READ)
    @RequestMapping(value = "/listEnv", method = RequestMethod.POST)
    public ResWebData<?> listEnv(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        List<DmSysEnvDO> dsEnvDOList = this.rdpDsEnvService.listDsEnv(puid, uid, null);
        List<RangeObjectVO> vos = dsEnvDOList.stream().map(envDO -> {
            return DmConvertUtils.buildRangeObjectVO(String.valueOf(envDO.getId()), envDO.getEnvName(), null, SecRangeType.Environment, null);
        }).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(value = DM_SECRULES_READ)
    @RequestMapping(value = "/listIns", method = RequestMethod.POST)
    public ResWebData<?> listIns(@Valid @RequestBody RangeListInsFo fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        List<BrowseLevelsVO> dsList = this.browseService.listDs(puid, uid, fo.getEnvId());
        if (fo.getRefId() == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_NOT_EXIST_ERROR.name()));
        }
        DmSecRefererDO refDO = this.checkRulesService.querySpecRefererById(puid, fo.getRefId());
        if (refDO == null || !refDO.isEnable()) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_IS_DISABLED_MESSAGE.name()));
        }
        DmSecRuleMO ruleMO = this.checkRulesService.queryRuleById(puid, refDO.getRefRule(), refDO.getRefRuleKind());
        List<DataSourceType> supportDsList;
        if (refDO.getRefRuleKind() == RuleKind.QUERY) {
            supportDsList = ruleMO.getRuleDO().getRuleDsRange();
        } else {
            supportDsList = Arrays.asList(DataSourceType.values());
        }

        List<RangeObjectVO> vos = dsList.stream().map(dsDO -> {
            DataSourceType dsType = DataSourceType.valueOf((String) dsDO.getObjAttr().get("dsType"));
            boolean enable = supportDsList.contains(dsType);
            if (enable) {
                enable = this.checkRulesService.isSupportRangeType(fo.getRangeType(), fo.getMatchType(), fo.getRuleKind(), dsType);
            }

            Map<String, Object> attr = CollectionUtils.asMap(//
                    "dsType", dsDO.getObjAttr().get("dsType"),//
                    "disabled", !enable//
            );

            String insId = (String) dsDO.getObjAttr().get("dsInstance");
            String insDesc = RdpConvertUtils.removeNoDescription((String) dsDO.getObjAttr().get("dsInstanceDesc"));
            return DmConvertUtils.buildRangeObjectVO(String.valueOf(dsDO.getObjId()), insId, insDesc, SecRangeType.Instance, attr);
        }).collect(Collectors.toList());

        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(value = DM_SECRULES_READ)
    @RequestMapping(value = "/listLevels", method = RequestMethod.POST)
    public ResWebData<?> listLevels(@Valid @RequestBody BrowseLevelsFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        // ds object list
        DsLevels dbLevels = this.dmDsConfigService.parseLevels(fo.getLevels());
        this.objectCacheDao.ownDataSource(puid, dbLevels.dsDO().getId());

        List<BrowseLevelsVO> levels = this.browseService.listLevels(puid, uid, dbLevels, fo.isRefreshCache());

        List<RangeObjectVO> result = new ArrayList<>();
        levels.forEach(vo -> {
            switch (UmiTypes.valueOfCode(vo.getObjType())) {
                case Catalog:
                case ExternalCatalog: {
                    result.add(DmConvertUtils.buildRangeObjectVO(String.valueOf(vo.getObjId()), vo.getObjName(), null, SecRangeType.Catalog, null));
                    break;
                }
                case Schema:
                case ExternalSchema: {
                    result.add(DmConvertUtils.buildRangeObjectVO(String.valueOf(vo.getObjId()), vo.getObjName(), null, SecRangeType.Schema, null));
                    break;
                }
            }
            vo.setObjAttr(Collections.emptyMap());
        });
        return ResWebDataUtils.buildSuccess(result);
    }

    @RequestAuth(value = DM_SECRULES_READ)
    @RequestMapping(value = "/listLeaf", method = RequestMethod.POST)
    public ResWebData<?> listLeaf(@Valid @RequestBody BrowseLeafFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        // ds object list
        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        this.objectCacheDao.ownDataSource(puid, levels.dsDO().getId());

        UmiTypes leafType = UmiTypes.valueOfCode(fo.getLeafType());
        List<BrowseLevelsVO> objects = this.browseService.listLeaf(puid, uid, levels, leafType, fo.getPattern(), false);

        List<RangeObjectVO> result = new ArrayList<>();
        objects.forEach(vo -> {
            switch (UmiTypes.valueOfCode(vo.getObjType())) {
                case Table:
                case View:
                case Materialized:
                    result.add(DmConvertUtils.buildRangeObjectVO(String.valueOf(vo.getObjId()), vo.getObjName(), null, SecRangeType.TableOrView, null));
                    break;
            }
            vo.setObjAttr(Collections.emptyMap());
        });
        return ResWebDataUtils.buildSuccess(result);
    }

    @RequestAuth(value = DM_SECRULES_READ)
    @RequestMapping(value = "/listColumn", method = RequestMethod.POST)
    public ResWebData<?> listColumn(@Valid @RequestBody BrowseDetailFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        // ds object list
        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        this.objectCacheDao.ownDataSource(puid, levels.dsDO().getId());

        UmiTypes leafType = UmiTypes.valueOfCode(fo.getTargetType());
        String leafName = fo.getTargetName();
        List<BrowseColumnMO> columns = this.browseService.rdbColumns(puid, uid, levels, leafType, leafName);

        List<RangeObjectVO> result = new ArrayList<>();
        columns.forEach(c -> {
            //Map<String, Object> attr = CollectionUtils.asMap("dsType", c.getDataType().getObjAttr().get("dsType"));
            result.add(DmConvertUtils.buildRangeObjectVO("-1", c.getName(), null, SecRangeType.Column, null));
        });

        return ResWebDataUtils.buildSuccess(result);
    }
}
