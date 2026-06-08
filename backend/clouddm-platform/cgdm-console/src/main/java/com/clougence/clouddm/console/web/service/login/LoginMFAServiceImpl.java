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
package com.clougence.clouddm.console.web.service.login;

import static com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys.*;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.constants.MfaAccountType;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.model.vo.MfaCodeVO;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthMFADO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.auth.MfaStatus;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginMFAServiceImpl implements LoginMFAService {
    private static final long MFA_SETTING_EXPIRE_MS = 10 * 60 * 1000L;

    @Resource
    private AuthDal authDal;

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public MfaCodeVO initMFA(String uid, MfaAccountType mfaAccountType) {
        DmAuthUserDO userDO = authDal.userMapper().queryByUid(uid);
        if (userDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_USER_NOT_EXIST.name()));
        }

        DmAuthMFADO userMfaDO = authDal.mfaMapper().queryByUid(uid);
        if (userDO.isUseMfa() && (userMfaDO == null || userMfaDO.getMfaStatus() == MfaStatus.ACTIVE)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_ALREADY_ENABLED.name()));
        }

        if (userMfaDO != null && userMfaDO.getMfaStatus() == MfaStatus.ACTIVE) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_SETTING_INVALID.name()));
        }

        String mfaKey = genMfaKey();
        String encodeKey = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(mfaKey);
        if (userMfaDO != null) {
            authDal.mfaMapper().updateById(userMfaDO.getId(), encodeKey, MfaStatus.INACTIVE, mfaAccountType.name());
        } else {
            userMfaDO = new DmAuthMFADO();
            userMfaDO.setMfaKey(encodeKey);
            userMfaDO.setMfaStatus(MfaStatus.INACTIVE);
            userMfaDO.setMfaAccountType(mfaAccountType.name());
            userMfaDO.setUserId(userDO.getId());
            userMfaDO.setUid(userDO.getUid());
            authDal.mfaMapper().insert(userMfaDO);
        }

        MfaCodeVO vo = new MfaCodeVO();
        vo.setMfaCode(mfaKey);
        vo.setQrCode(toQrCodeDataUrl(genCcTotpUriQrCodePicture(mfaKey, getMfaAccount(userDO, mfaAccountType))));
        return vo;
    }

    @Override
    public MfaCodeVO resetMFA(String uid, MfaAccountType mfaAccountType) {
        DmAuthUserDO userDO = authDal.userMapper().queryByUid(uid);
        if (userDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_USER_NOT_EXIST.name()));
        }

        if (!userDO.isUseMfa()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_NOT_ENABLED.name()));
        }

        DmAuthMFADO userMfaDO = authDal.mfaMapper().queryByUid(uid);
        if (userMfaDO == null || userMfaDO.getMfaStatus() != MfaStatus.ACTIVE || StringUtils.isBlank(userMfaDO.getMfaKey())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_SETTING_INVALID.name()));
        }

        String newMfaKey = genMfaKey();
        String newEncodeKey = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(newMfaKey);
        authDal.mfaMapper().updateResetMfaKeyById(userMfaDO.getId(), newEncodeKey, mfaAccountType.name());

        MfaCodeVO vo = new MfaCodeVO();
        vo.setMfaCode(newMfaKey);
        vo.setQrCode(toQrCodeDataUrl(genCcTotpUriQrCodePicture(newMfaKey, getMfaAccount(userDO, mfaAccountType))));
        return vo;
    }

    @Override
    public boolean validMFA(String uid, int mfaCode) {
        DmAuthUserDO userDO = authDal.userMapper().queryByUid(uid);
        if (userDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_USER_NOT_EXIST.name()));
        }

        if (!userDO.isUseMfa()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_NOT_ENABLED.name()));
        }

        DmAuthMFADO userMfaDO = authDal.mfaMapper().queryByUid(uid);
        if (userMfaDO == null || userMfaDO.getMfaStatus() != MfaStatus.ACTIVE || StringUtils.isBlank(userMfaDO.getMfaKey())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_SETTING_INVALID.name()));
        }

        String decryptKey = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(userMfaDO.getMfaKey());
        return mfaValid(decryptKey, mfaCode);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void confirmFMA(String uid, boolean reset, int mfaCode) {
        DmAuthUserDO userDO = authDal.userMapper().queryByUid(uid);
        if (userDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_USER_NOT_EXIST.name()));
        }

        DmAuthMFADO userMfaDO = authDal.mfaMapper().queryByUid(uid);
        if (userMfaDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_SETTING_INVALID.name()));
        }

        if (reset) {
            if (StringUtils.isBlank(userMfaDO.getResetMfaKey())) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_RESET_SETTING_INVALID.name()));
            }
            assertMfaSettingNotExpired(userMfaDO);

            String decryptKey = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(userMfaDO.getResetMfaKey());
            if (!mfaValid(decryptKey, mfaCode)) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_CODE_IS_INVALID.name()));
            }

            authDal.mfaMapper().updateById(userMfaDO.getId(), userMfaDO.getResetMfaKey(), MfaStatus.ACTIVE, userMfaDO.getResetMfaAccountType());
            authDal.mfaMapper().emptyResetMfaKeyById(userMfaDO.getId());
        } else {
            if (StringUtils.isBlank(userMfaDO.getMfaKey())) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_SETTING_INVALID.name()));
            }
            assertMfaSettingNotExpired(userMfaDO);

            String decryptKey = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(userMfaDO.getMfaKey());
            if (!mfaValid(decryptKey, mfaCode)) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_CODE_IS_INVALID.name()));
            }

            authDal.mfaMapper().updateStatusById(userMfaDO.getId(), MfaStatus.ACTIVE);
        }

        authDal.userMapper().updateMfaStatus(userDO.getUid(), true);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void closeMFA(String uid) {
        DmAuthUserDO userDO = authDal.userMapper().queryByUid(uid);
        if (userDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_USER_NOT_EXIST.name()));
        }

        DmAuthMFADO userMfaDO = authDal.mfaMapper().queryByUid(uid);
        if (userMfaDO == null || userMfaDO.getMfaStatus() != MfaStatus.ACTIVE || StringUtils.isBlank(userMfaDO.getMfaKey())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_SETTING_INVALID.name()));
        }

        authDal.mfaMapper().deleteById(userMfaDO.getId());
        authDal.userMapper().updateMfaStatus(uid, false);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void closeInvalidMFA(String uid) {
        DmAuthMFADO userMfaDO = authDal.mfaMapper().queryByUid(uid);
        if (userMfaDO != null) {
            if (userMfaDO.getMfaStatus() == MfaStatus.ACTIVE) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_SETTING_INVALID.name()));
            }
            authDal.mfaMapper().deleteById(userMfaDO.getId());
        }
        authDal.userMapper().updateMfaStatus(uid, false);
    }

    @Override
    public boolean isInvalidMFA(String uid) {
        DmAuthUserDO userDO = authDal.userMapper().queryByUid(uid);
        if (userDO == null || !userDO.isUseMfa()) {
            return false;
        }
        DmAuthMFADO userMfaDO = authDal.mfaMapper().queryByUid(uid);
        return userMfaDO == null || userMfaDO.getMfaStatus() != MfaStatus.ACTIVE;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void invalidateMFAIfAccountChanged(String uid, Collection<MfaAccountType> changedTypes, boolean confirmed) {
        if (changedTypes == null || changedTypes.isEmpty()) {
            return;
        }
        DmAuthUserDO userDO = authDal.userMapper().queryByUid(uid);
        if (userDO == null || !userDO.isUseMfa()) {
            return;
        }
        DmAuthMFADO userMfaDO = authDal.mfaMapper().queryByUid(uid);
        if (userMfaDO == null || userMfaDO.getMfaStatus() != MfaStatus.ACTIVE) {
            return;
        }
        MfaAccountType mfaAccountType = parseMfaAccountType(userMfaDO.getMfaAccountType());
        if (mfaAccountType != null && !changedTypes.contains(mfaAccountType)) {
            return;
        }
        if (!confirmed) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_ACCOUNT_CHANGE_CONFIRM_REQUIRED.name()));
        }
        authDal.mfaMapper().updateStatusById(userMfaDO.getId(), MfaStatus.INVALID);
    }

    @Override
    public DmAuthMFADO queryMFA(String uid) {
        return authDal.mfaMapper().queryByUid(uid);
    }

    private String genMfaKey() {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        return gAuth.createCredentials().getKey();
    }

    private void assertMfaSettingNotExpired(DmAuthMFADO userMfaDO) {
        Date modified = userMfaDO.getGmtModified() == null ? userMfaDO.getGmtCreate() : userMfaDO.getGmtModified();
        if (modified == null) {
            return;
        }
        if (System.currentTimeMillis() - modified.getTime() > MFA_SETTING_EXPIRE_MS) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_SETTING_EXPIRED.name()));
        }
    }

    private boolean mfaValid(String privateKey, int code) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        return gAuth.authorize(privateKey, code);
    }

    private String getMfaAccount(DmAuthUserDO userDO, MfaAccountType mfaAccountType) {
        if (mfaAccountType == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_ACCOUNT_TYPE_EMPTY.name()));
        }

        String account = switch (mfaAccountType) {
            case ACCOUNT -> userDO.getAccount();
            case EMAIL -> userDO.getEmail();
            case PHONE -> userDO.getPhone();
        };
        if (StringUtils.isBlank(account)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_ACCOUNT_EMPTY.name()));
        }
        return account;
    }

    private MfaAccountType parseMfaAccountType(String type) {
        if (StringUtils.isBlank(type)) {
            return null;
        }
        try {
            return MfaAccountType.valueOf(type);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static final String ccTotpUriFormat = "otpauth://totp/{0}?secret={1}&issuer={2}";

    private String toQrCodeDataUrl(byte[] qrCode) {
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(qrCode);
    }

    private byte[] genCcTotpUriQrCodePicture(String code, String account) {
        try {
            String totpUri = MessageFormat.format(ccTotpUriFormat, account, code, "CloudDM");
            QRCodeWriter qrw = new QRCodeWriter();
            BitMatrix matrix = qrw.encode(totpUri, BarcodeFormat.QR_CODE, 200, 200);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", pngOutputStream);

            return pngOutputStream.toByteArray();
        } catch (Exception e) {
            String msg = ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_QR_CODE_GENERATE_ERROR.name(), msg));
        }
    }
}
