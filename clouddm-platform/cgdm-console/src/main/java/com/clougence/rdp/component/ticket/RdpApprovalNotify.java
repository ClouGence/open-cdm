package com.clougence.rdp.component.ticket;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.clougence.clouddm.sdk.approval.ApprovalProvider;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.approval.ApprovalProviderSpi;
import com.clougence.rdp.constant.UserConfigTagType;
import com.clougence.clouddm.sdk.LifeSpiRequest;
import com.clougence.rdp.service.RdpNotifyService;
import com.clougence.rdp.service.model.UserConfigMO;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RdpApprovalNotify implements RdpNotifyService {

    @Resource
    private RdpApprovalService approvalService;

    @SneakyThrows
    @Override
    public void notifyUserConfig(String ownerUid, List<UserConfigMO> configList) {

        List<UserConfigTagType> tagTypes = configList.stream().map(UserConfigMO::getTagType).distinct().collect(Collectors.toList());
        for (UserConfigTagType tagType : tagTypes) {
            ApprovalProvider providerType = convertToProviderType(tagType);
            if (providerType == null) {
                continue;
            }

            ApprovalProviderSpi service = PluginManager.findSpi(ApprovalProviderSpi.class, providerType.name());
            if (service == null) {
                continue;
            }

            service.stop(ownerUid, new LifeSpiRequest());
            if (this.approvalService.checkEnableApproval(ownerUid, providerType)) {
                service.start(ownerUid, new LifeSpiRequest());
            }
        }
    }

    private static ApprovalProvider convertToProviderType(UserConfigTagType type) {
        switch (type) {
            case FEISHU:
                return ApprovalProvider.Feishu;
            case WECHAT:
                return ApprovalProvider.Wechat;
            case DINGTALK:
                return ApprovalProvider.DingTalk;
            default:
                return null;
        }
    }
}
