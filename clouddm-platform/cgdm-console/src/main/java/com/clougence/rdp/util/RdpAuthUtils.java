package com.clougence.rdp.util;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.api.common.crypt.PasswordInfo;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.dal.mapper.RdpDataSourceMapper;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.clouddm.sdk.model.analysis.resource.DsResPathObj;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.rdp.service.RdpAuthServiceForManage;
import com.clougence.utils.StringUtils;

public class RdpAuthUtils {

    private static RdpAuthServiceForManage rdpAuthServiceForManage;
    private static RdpDataSourceMapper     rdpDataSourceMapper;

    public static void initUtils(ApplicationContext spring) {
        rdpAuthServiceForManage = spring.getBean(RdpAuthServiceForManage.class);
        rdpDataSourceMapper = spring.getBean(RdpDataSourceMapper.class);
    }

    public static <T> ResWebData<T> missDataPermission(final long dsId, final String resourcePath, final String dataAuthLabel) {
        String useAuthLabel = dataAuthLabel;
        if (rdpAuthServiceForManage != null) {
            AuthInfo authLabel = rdpAuthServiceForManage.getAuthLabel(dataAuthLabel);
            if (authLabel != null) {
                useAuthLabel = authLabel.getKeyI18n();
            }
        }

        String useResourcePath = "/" + dsId + "/" + StringUtils.trimStart(resourcePath, '/');
        String useResourceLabel = useResourcePath;
        if (rdpDataSourceMapper != null) {
            RdpDataSourceDO dsDO = rdpDataSourceMapper.queryDsIdentityById(dsId);
            String dsLabel = dsDO.getInstanceDesc();
            if (RdpConvertUtils.removeNoDescription(dsLabel) == null) {
                dsLabel = dsDO.getInstanceId();
            }
            useResourceLabel = "/" + dsLabel + "/" + StringUtils.trimStart(resourcePath, '/');
        }

        String labelI18n = RdpI18nUtils.getMessage(useAuthLabel);
        String errorI18n = RdpI18nUtils.getMessage(I18nRdpMsgKeys.COMM_DATA_AUTH_PERMISSION_ERROR.name(), useResourceLabel, labelI18n);

        ResWebData<T> data = ResWebDataUtils.buildSuccess();
        data.setPermission(false);
        data.setPermissionI18n(errorI18n);
        data.setNeedResource(useResourcePath);
        data.setNeedAuthKey(useAuthLabel);
        return data;
    }

    public static String missRoleAuthMsg(String needAuthLabel) {
        if (rdpAuthServiceForManage != null) {
            AuthInfo authLabel = rdpAuthServiceForManage.getAuthLabel(needAuthLabel);
            if (authLabel != null) {
                needAuthLabel = authLabel.getKeyI18n();
            }
        }
        String authI18n = RdpI18nUtils.getMessage(needAuthLabel);
        return RdpI18nUtils.getMessage(I18nRdpMsgKeys.COMM_ROLE_AUTH_PERMISSION_ERROR.name(), authI18n);
    }

    public static String missDataAuthMsg(final long dsId, final String resourcePath, final String dataAuthLabel) {
        String useAuthLabel = dataAuthLabel;
        if (rdpAuthServiceForManage != null) {
            AuthInfo authLabel = rdpAuthServiceForManage.getAuthLabel(dataAuthLabel);
            if (authLabel != null) {
                useAuthLabel = authLabel.getKeyI18n();
            }
        }

        String useResourceLabel = "/" + dsId + "/" + StringUtils.trimStart(resourcePath, '/');
        if (rdpDataSourceMapper != null) {
            RdpDataSourceDO dsDO = rdpDataSourceMapper.queryDsIdentityById(dsId);
            String dsLabel = dsDO.getInstanceDesc();
            if (RdpConvertUtils.removeNoDescription(dsLabel) == null) {
                dsLabel = dsDO.getInstanceId();
            }
            useResourceLabel = "/" + dsLabel + "/" + StringUtils.trimStart(resourcePath, '/');
        }

        String labelI18n = RdpI18nUtils.getMessage(useAuthLabel);
        return RdpI18nUtils.getMessage(I18nRdpMsgKeys.COMM_DATA_AUTH_PERMISSION_ERROR.name(), useResourceLabel, labelI18n);
    }

    public static DsResPathObj genResPathByList(List<String> levelElements) {
        StringBuilder resPathLike = new StringBuilder();
        for (String path : levelElements) {
            if (StringUtils.isNotBlank(path)) {
                resPathLike.append("/").append(path);
            }
        }
        resPathLike.append("/");
        return new DsResPathObj(resPathLike.toString());
    }

    public static DsResPathObj genResPathByList(List<String> levelElements, String lastOne) {
        StringBuilder resPathLike = new StringBuilder();
        for (String path : levelElements) {
            if (StringUtils.isNotBlank(path)) {
                resPathLike.append("/").append(path);
            }
        }

        if (StringUtils.isNotBlank(lastOne)) {
            resPathLike.append("/").append(lastOne);
        }
        resPathLike.append("/");

        return new DsResPathObj(resPathLike.toString());
    }

    private static final DsResPathObj EMPTY = new DsResPathObj("/");

    public static DsResPathObj genEmptyResPath() {
        return EMPTY;
    }

    public static boolean isErrorPasswd(String encryptPwd, String plainPwd) {
        PasswordInfo info = new PasswordInfo();
        info.setEncryptPassword(encryptPwd);
        info.setPlainPassword(plainPwd);
        return !CryptService.INSTANCE.isMatchForOneWay(info);
    }
}
