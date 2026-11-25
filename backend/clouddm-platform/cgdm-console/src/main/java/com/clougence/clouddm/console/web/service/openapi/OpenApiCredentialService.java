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

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.model.vo.openapi.OpenApiCredentialVO;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.NamingDao;
import com.clougence.clouddm.platform.dal.model.auth.AccountType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;

import jakarta.annotation.Resource;

@Service
public class OpenApiCredentialService {

    @Resource
    private AuthDal authDal;
    @Resource
    private NamingDao namingDao;

    @Transactional
    public OpenApiCredentialVO writeCredential(String primaryUid, String targetUid, boolean reset) {
        DmAuthUserDO primary = authDal.userMapper().queryByUid(primaryUid);
        DmAuthUserDO target = authDal.userMapper().lockByUid(targetUid);
        if (primary == null || primary.getAccountType() != AccountType.PRIMARY_ACCOUNT || target == null || target.getAccountType() != AccountType.SUB_ACCOUNT
            || !Objects.equals(primary.getId(), target.getParentId())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_NOT_EXIST_ERROR.name()));
        }

        if (!reset) {
            String secretKey = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(target.getSecretKey());
            return new OpenApiCredentialVO(target.getAccessKey(), secretKey);
        }

        String accessKeyId = namingDao.genAccessKey();
        String secretKey = namingDao.genSecretKey();
        String storedSecretKey = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(secretKey);
        authDal.userMapper().updateUserAkSk(target.getUid(), accessKeyId, storedSecretKey);
        return new OpenApiCredentialVO(accessKeyId, secretKey);
    }
}
