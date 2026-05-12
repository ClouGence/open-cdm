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
package com.clougence.rdp.service.impl;

import static com.clougence.rdp.constant.I18nRdpMsgKeys.MFA_CODE_IS_INVALID;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.base.metadata.rdp.enumeration.GlobalDeploySite;
import com.clougence.clouddm.console.web.dal.enumeration.MfaStatus;
import com.clougence.clouddm.console.web.dal.mapper.RdpUserMapper;
import com.clougence.clouddm.console.web.dal.mapper.RdpUserMfaMapper;
import com.clougence.clouddm.console.web.dal.model.RdpUserDO;
import com.clougence.clouddm.console.web.dal.model.RdpUserMfaDO;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.rdp.service.RdpUserMfaService;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
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
public class RdpUserMfaServiceImpl implements RdpUserMfaService {

    @Resource
    private RdpUserMfaMapper rdpUserMfaMapper;

    @Resource
    private RdpUserMapper    rdpUserMapper;

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public byte[] initUserMfaSetting(String uid) {
        RdpUserDO userDO = rdpUserMapper.queryByUid(uid);
        if (userDO == null) {
            throw new IllegalArgumentException("User (" + uid + ") is not exist.");
        }

        if (userDO.isUseMfa()) {
            throw new IllegalArgumentException("User (" + uid + ") is already use mfa, if need change,go reset logic.");
        }

        RdpUserMfaDO userMfaDO = rdpUserMfaMapper.queryByUid(uid);
        if (userMfaDO != null && userMfaDO.getMfaStatus() == MfaStatus.ACTIVE) {
            throw new IllegalArgumentException("User (" + uid + ")'s mfa setting is already active,broke data.");
        }

        String mfaKey = genMfaKey();
        String encodeKey = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(mfaKey);
        if (userMfaDO != null) {
            rdpUserMfaMapper.updateById(userMfaDO.getId(), encodeKey, MfaStatus.INACTIVE);
        } else {
            userMfaDO = new RdpUserMfaDO();
            userMfaDO.setMfaKey(encodeKey);
            userMfaDO.setMfaStatus(MfaStatus.INACTIVE);
            userMfaDO.setUserId(userDO.getId());
            userMfaDO.setUid(userDO.getUid());
            rdpUserMfaMapper.insert(userMfaDO);
        }

        if (GlobalDeploySite.outChina()) {
            return genCcTotpUriQrCodePicture(mfaKey, userDO.getEmail());
        } else {
            return genCcTotpUriQrCodePicture(mfaKey, userDO.getPhone());
        }
    }

    @Override
    public byte[] resetMfaSetting(String uid, int mfaCode) {
        RdpUserDO userDO = rdpUserMapper.queryByUid(uid);
        if (userDO == null) {
            throw new IllegalArgumentException("User (" + uid + ") is not exist.");
        }

        if (!userDO.isUseMfa()) {
            throw new IllegalArgumentException("User (" + uid + ") is not use mfa.");
        }

        RdpUserMfaDO userMfaDO = rdpUserMfaMapper.queryByUid(uid);
        if (userMfaDO == null || userMfaDO.getMfaStatus() == MfaStatus.INACTIVE || StringUtils.isBlank(userMfaDO.getMfaKey())) {
            throw new IllegalArgumentException("User (" + uid + ")'s mfa setting is not exist or illegal, broken data.");
        }

        String decryptKey = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(userMfaDO.getMfaKey());
        if (!mfaValid(decryptKey, mfaCode)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_CODE_IS_INVALID.name()));
        }

        String newMfaKey = genMfaKey();
        String newEncodeKey = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(newMfaKey);
        rdpUserMfaMapper.updateResetMfaKeyById(userMfaDO.getId(), newEncodeKey);

        if (GlobalDeploySite.outChina()) {
            return genCcTotpUriQrCodePicture(newMfaKey, userDO.getEmail());
        } else {
            return genCcTotpUriQrCodePicture(newMfaKey, userDO.getPhone());
        }
    }

    @Override
    public boolean validMfaCode(String uid, int mfaCode) {
        RdpUserDO userDO = rdpUserMapper.queryByUid(uid);
        if (userDO == null) {
            throw new IllegalArgumentException("User (" + uid + ") is not exist.");
        }

        if (!userDO.isUseMfa()) {
            throw new IllegalArgumentException("User (" + uid + ") is not use mfa.");
        }

        RdpUserMfaDO userMfaDO = rdpUserMfaMapper.queryByUid(uid);
        if (userMfaDO == null || userMfaDO.getMfaStatus() == MfaStatus.INACTIVE || StringUtils.isBlank(userMfaDO.getMfaKey())) {
            throw new IllegalArgumentException("User (" + uid + ")'s mfa setting is not exist or illegal, broken data.");
        }

        String decryptKey = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(userMfaDO.getMfaKey());
        return mfaValid(decryptKey, mfaCode);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void confirmUserMfaSetting(String uid, boolean reset, int mfaCode) {
        RdpUserDO userDO = rdpUserMapper.queryByUid(uid);
        if (userDO == null) {
            throw new IllegalArgumentException("User (" + uid + ") is not exist.");
        }

        RdpUserMfaDO userMfaDO = rdpUserMfaMapper.queryByUid(uid);
        if (userMfaDO == null) {
            throw new IllegalArgumentException("User (" + uid + ")'s mfa setting is not exist,broke data.");
        }

        if (reset) {
            if (StringUtils.isBlank(userMfaDO.getResetMfaKey())) {
                throw new IllegalArgumentException("User (" + uid + ")'s reset mfa key is empty,broke data.");
            }

            String decryptKey = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(userMfaDO.getResetMfaKey());
            if (!mfaValid(decryptKey, mfaCode)) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_CODE_IS_INVALID.name()));
            }

            rdpUserMfaMapper.updateById(userMfaDO.getId(), userMfaDO.getResetMfaKey(), MfaStatus.ACTIVE);
            rdpUserMfaMapper.emptyResetMfaKeyById(userMfaDO.getId());
        } else {
            if (StringUtils.isBlank(userMfaDO.getMfaKey())) {
                throw new IllegalArgumentException("User (" + uid + ")'s mfa key is empty,broke data.");
            }

            String decryptKey = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(userMfaDO.getMfaKey());
            if (!mfaValid(decryptKey, mfaCode)) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_CODE_IS_INVALID.name()));
            }

            rdpUserMfaMapper.updateStatusById(userMfaDO.getId(), MfaStatus.ACTIVE);
        }

        rdpUserMapper.updateMfaStatus(userDO.getUid(), true);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void closeUserMfa(String uid, int mfaCode) {
        RdpUserDO userDO = rdpUserMapper.queryByUid(uid);
        if (userDO == null) {
            throw new IllegalArgumentException("User (" + uid + ") is not exist.");
        }

        RdpUserMfaDO userMfaDO = rdpUserMfaMapper.queryByUid(uid);
        if (userMfaDO == null || userMfaDO.getMfaStatus() == MfaStatus.INACTIVE || StringUtils.isBlank(userMfaDO.getMfaKey())) {
            throw new IllegalArgumentException("User (" + uid + ")'s mfa setting is not exist or illegal,broke data.");
        }

        String decryptKey = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(userMfaDO.getMfaKey());
        if (!mfaValid(decryptKey, mfaCode)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_CODE_IS_INVALID.name()));
        }

        rdpUserMfaMapper.deleteById(userMfaDO.getId());
        rdpUserMapper.updateMfaStatus(uid, false);
    }

    private String genMfaKey() {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        return gAuth.createCredentials().getKey();
    }

    private boolean mfaValid(String privateKey, int code) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        return gAuth.authorize(privateKey, code);
    }

    private static final String ccTotpUriFormat = "otpauth://totp/{0}?secret={1}&issuer={2}";

    private byte[] genCcTotpUriQrCodePicture(String code, String account) {
        try {
            String totpUri = MessageFormat.format(ccTotpUriFormat, account, code, GlobalDeploySite.ccProductName());
            QRCodeWriter qrw = new QRCodeWriter();
            BitMatrix matrix = qrw.encode(totpUri, BarcodeFormat.QR_CODE, 200, 200);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", pngOutputStream);

            return pngOutputStream.toByteArray();
        } catch (Exception e) {
            String msg = "Generate QR code failed,msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}
