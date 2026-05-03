//package com.clougence.rdp.component.alert.impl;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//
//import org.springframework.stereotype.Service;
//
//import com.aliyun.dyvmsapi20170525.Client;
//import com.aliyun.dyvmsapi20170525.models.SingleCallByTtsRequest;
//import com.aliyun.dyvmsapi20170525.models.SingleCallByTtsResponse;
//import com.aliyun.teaopenapi.models.Config;
//import com.aliyun.teautil.models.RuntimeOptions;
//import com.clougence.rdp.cloud.aliyun.AliyunProfileService;
//import com.clougence.rdp.component.alert.RdpVoiceAlertService;
//import com.clougence.rdp.component.alert.model.SendMsgResult;
//import com.clougence.rdp.dal.model.RdpUserKvBaseConfigDO;
//import com.clougence.rdp.global.config.user.UserDefinedConfig;
//import com.clougence.rdp.service.RdpUserConfigService;
//import com.clougence.rdp.service.enumeration.AlertMediaType;
//import com.clougence.utils.CollectionUtils;
//import com.clougence.utils.ExceptionUtils;
//import com.clougence.utils.StringUtils;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import lombok.Setter;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * @author bucketli 2024/7/22 18:55:10
// */
//@Service(value = "RdpAliyunVoiceAlertService")
//@Slf4j
//public class RdpAliyunVoiceAlertService implements RdpVoiceAlertService {
//
//    @Resource
//    @Setter
//    private AliyunProfileService   profileService;
//
//    @Resource
//    @Setter
//    protected RdpUserConfigService rdpUserConfigService;
//
//    protected Client newVoiceTeaClient(String aliyunAk, String aliyunSk, String proxyAddr) {
//        try {
//            Config c = profileService.genTeaConfig(null, aliyunAk, aliyunSk, proxyAddr);
//            return new Client(c);
//        } catch (Exception e) {
//            String msg = "Error create voice tea client,msg:" + ExceptionUtils.getRootCauseMessage(e);
//            throw new RuntimeException(msg, e);
//        }
//    }
//
//    protected Map<String, RdpUserKvBaseConfigDO> fetchUserVoiceConfig(String ownerUid) {
//        List<RdpUserKvBaseConfigDO> configs = rdpUserConfigService.getSpecifiedConfigs(ownerUid, //
//                Arrays.asList(UserDefinedConfig.Fields.mobileAccessKey, //
//                        UserDefinedConfig.Fields.mobileSecretKey, //
//                        UserDefinedConfig.Fields.taskErrorMobileTc, //
//                        UserDefinedConfig.Fields.taskRecoverMobileTc, //
//                        UserDefinedConfig.Fields.mobileProxyAddr));
//
//        if (configs == null || configs.isEmpty()) {
//            return new HashMap<>();
//        }
//
//        Map<String, RdpUserKvBaseConfigDO> re = new HashMap<>();
//        for (RdpUserKvBaseConfigDO configDO : configs) {
//            re.put(configDO.getConfigName(), configDO);
//        }
//
//        return re;
//    }
//
//    @Override
//    public SendMsgResult singleCallByTts(String phone, String ttsCode, Map<String, String> params, String ownerUid) {
//        Map<String, RdpUserKvBaseConfigDO> configs = fetchUserVoiceConfig(ownerUid);
//        if (configs == null) {
//            return new SendMsgResult(false, "User's voice config is empty,uid:" + ownerUid, null, AlertMediaType.VOICE, CollectionUtils.asList(phone));
//        }
//
//        String accessKey = configs.get(UserDefinedConfig.Fields.mobileAccessKey).getConfigValue();
//        String secretKey = configs.get(UserDefinedConfig.Fields.mobileSecretKey).getConfigValue();
//        if (StringUtils.isBlank(accessKey) || StringUtils.isBlank(secretKey) || StringUtils.isBlank(ttsCode)) {
//            return new SendMsgResult(false, "Miss accessKey or secretKey or ttsCode", null, AlertMediaType.VOICE, CollectionUtils.asList(phone));
//        }
//
//        String proxyAddr = configs.get(UserDefinedConfig.Fields.mobileProxyAddr).getConfigValue();
//
//        return singleCallByTtsInner(phone, ttsCode, params, accessKey, secretKey, proxyAddr);
//    }
//
//    protected SendMsgResult singleCallByTtsInner(String phone, String ttsCode, Map<String, String> params, String accessKey, String secretKey, String proxyAddr) {
//        String paramJsonStr = "{}";
//        try {
//            if (params != null) {
//                paramJsonStr = new ObjectMapper().writeValueAsString(params);
//            }
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Gen voice message parameter json failed.msg:" + ExceptionUtils.getRootCauseMessage(e), e);
//        }
//
//        SingleCallByTtsRequest request = new SingleCallByTtsRequest();
//        request.setCalledNumber(phone);
//        request.setTtsCode(ttsCode);
//        request.setTtsParam(paramJsonStr);
//
//        try {
//            RuntimeOptions runtime = new RuntimeOptions();
//            Client c = newVoiceTeaClient(accessKey, secretKey, proxyAddr);
//            SingleCallByTtsResponse response = c.singleCallByTtsWithOptions(request, runtime);
//            if (response == null || response.getBody() == null) {
//                return new SendMsgResult(false, "Response is empty.", null, AlertMediaType.VOICE, CollectionUtils.asList(phone));
//            }
//
//            if (response.getBody().getCode().equals("OK")) {
//                return new SendMsgResult(true, response.getBody().getMessage(), null, AlertMediaType.VOICE, CollectionUtils.asList(phone));
//            } else {
//                return new SendMsgResult(false, response.getBody().getMessage(), null, AlertMediaType.VOICE, CollectionUtils.asList(phone));
//            }
//        } catch (Exception e) {
//            String msg = "Voice alert failed,msg:" + ExceptionUtils.getRootCauseMessage(e);
//            log.error(msg, e);
//            throw new RuntimeException(msg, e);
//        }
//    }
//}
