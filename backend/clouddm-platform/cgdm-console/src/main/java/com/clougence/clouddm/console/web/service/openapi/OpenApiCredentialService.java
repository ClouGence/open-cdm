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
package com.clougence.clouddm.console.web.service.openapi;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.api.common.exception.DmErrorCode;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.model.vo.openapi.OpenApiCredentialVO;
import com.clougence.clouddm.console.web.util.RdpAuthUtils;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.NamingDao;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthRoleDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;

@Service
public class OpenApiCredentialService {

    @Resource
    private AuthDal   authDal;
    @Resource
    private NamingDao namingDao;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public OpenApiCredentialVO writeCredential(String primaryUid, String operatorUid, String targetUid, boolean reset) {
        DmAuthUserDO primary = authDal.userMapper().queryByUid(primaryUid);
        DmAuthUserDO target = authDal.userMapper().lockByUid(targetUid);
        if (!RdpAuthUtils.isSubAccountOf(primary, target)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_NOT_EXIST_ERROR.name()));
        }

        DmAuthUserDO operator = primaryUid.equals(operatorUid) ? primary : authDal.userMapper().queryByUid(operatorUid);
        if (!RdpAuthUtils.canManageSubAccountCredentials(primary, operator, target, this.findRole(operator), this.findRole(target))) {
            throw new ErrorMessageException(DmErrorCode.O_AUTHORITY.code(), DmI18nUtils.getMessage(I18nRdpMsgKeys.OPENAPI_CREDENTIAL_SCOPE_FORBIDDEN.name()));
        }

        if (!reset && StringUtils.isNotBlank(target.getAccessKey()) && StringUtils.isNotBlank(target.getSecretKey())) {
            String secretKey = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(target.getSecretKey());
            return new OpenApiCredentialVO(target.getAccessKey(), secretKey);
        }

        String accessKeyId = namingDao.genAccessKey();
        String secretKey = namingDao.genSecretKey();
        String storedSecretKey = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(secretKey);
        authDal.userMapper().updateUserAkSk(target.getUid(), accessKeyId, storedSecretKey);
        return new OpenApiCredentialVO(accessKeyId, secretKey);
    }

    private DmAuthRoleDO findRole(DmAuthUserDO user) {
        if (user == null || user.getRoleId() == null) {
            return null;
        }
        return authDal.roleMapper().selectById(user.getRoleId());
    }
}
