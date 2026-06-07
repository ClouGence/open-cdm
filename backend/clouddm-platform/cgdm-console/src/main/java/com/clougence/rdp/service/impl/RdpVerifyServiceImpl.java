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
package com.clougence.clouddm.console.web.component.config.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.console.web.util.NamedThreadFactory;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.rdp.service.RdpVerifyService;
import com.clougence.rdp.service.model.CheckVerifyMO;
import com.clougence.utils.ExceptionUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * Reserved for the next image-captcha based verification flow.
 */
@Service
@Slf4j
public class RdpVerifyServiceImpl implements RdpVerifyService, UnifiedPostConstruct {

    @Resource
    private AuthDal authDal;

    @Override
    public void init() {
        ScheduledExecutorService verifyCleanExecutor = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("clouddm-auth-integrity-check", true));
        verifyCleanExecutor.scheduleAtFixedRate(() -> {
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.MINUTE, -10);
                authDal.verifyMapper().deleteOldData(calendar.getTime());
                log.info("[Rdp Verify Service] Verify info cleaned.");
            } catch (Throwable e) {
                log.error("Clean verify info failed, but ignore. msg:" + ExceptionUtils.getRootCauseMessage(e), e);
            }
        }, 2, 10, TimeUnit.MINUTES);
    }

    @Override
    public void stop() {

    }

    @Override
    public void dropUserVerify(String uid) {
        authDal.verifyMapper().deleteByUid(uid);
    }

    @Override
    public void checkVerifyCode(CheckVerifyMO checkVerifyPO) {

    }

    @Override
    public void updateEmailOrPhoneByUid(String uid, String phone, String email) {

    }
}
