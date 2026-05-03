package com.clougence.clouddm.console.web.component.auth.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForManage;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsService;
import com.clougence.clouddm.console.web.dal.model.DmDsConfigDO;
import com.clougence.rdp.controller.model.vo.RdpAuthObjectVO;
import com.clougence.rdp.dal.mapper.RdpDataSourceMapper;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.clouddm.sdk.security.auth.AuthElementType;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.model.analysis.resource.AuthBrowseObject;
import com.clougence.rdp.util.RdpConvertUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2021/1/13 10:50
 */
@Slf4j
@Service
public class DmAuthServiceForManageImpl implements DmAuthServiceForManage {

    @Resource
    private RdpDataSourceMapper rdpDsMapper;
    @Resource
    private DmDsService         dmDsService;

    @Override
    public List<RdpAuthObjectVO> listElements(String puid, String envId, AuthKind authKind) {
        List<AuthBrowseObject> objs;
        if (authKind == AuthKind.DataSource) {
            objs = listDsEles(puid, envId);
        } else {
            throw new IllegalArgumentException("Unsupported auth kind:" + authKind);
        }

        return objs.stream().map(RdpConvertUtils::convertToRdpAuthObjectVO).collect(Collectors.toList());
    }

    private List<AuthBrowseObject> listDsEles(String puid, String envId) {
        List<RdpDataSourceDO> dsDOs = this.rdpDsMapper.listByDsEnvId(Long.parseLong(envId));
        if (dsDOs == null || dsDOs.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> dsIds = dsDOs.stream().map(RdpDataSourceDO::getId).collect(Collectors.toList());

        List<DmDsConfigDO> confList = this.dmDsService.fetchDsConfigByIds(puid, dsIds);
        List<Long> enableQueryDsIds = confList.stream().map(DmDsConfigDO::getDataSourceId).collect(Collectors.toList());

        List<AuthBrowseObject> objs = new ArrayList<>();
        for (RdpDataSourceDO dsDO : dsDOs) {
            boolean enable = enableQueryDsIds.contains(dsDO.getId());
            if (!enable) {
                continue;
            }

            AuthBrowseObject obj = new AuthBrowseObject();
            obj.setObjId(dsDO.getId());
            obj.setObjName(dsDO.getInstanceId());
            obj.setObjDesc(dsDO.getInstanceDesc());
            obj.setObjType(AuthElementType.Instance);
            obj.setObjAttr(new HashMap<>());
            obj.getObjAttr().put("dsDeployType", dsDO.getDeployType().name());
            obj.getObjAttr().put("dsType", dsDO.getDataSourceType().name());
            obj.getObjAttr().put("enableQuery", enable);
            obj.setLeaf(true);
            objs.add(obj);
        }

        return objs;
    }
}
