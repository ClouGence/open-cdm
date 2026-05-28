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
package com.clougence.clouddm.console.web.global.csrf;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.console.web.util.RandomStrUtils;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthCsrfTokenDO;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ThreadUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CsrfTokenServiceImpl implements CsrfTokenService, UnifiedPostConstruct {
    @Resource
    private AuthDal authDal;
    private Thread  cleanerTokenThread;

    @Override
    public void init() throws Exception {
        if (this.cleanerTokenThread == null) {
            this.cleanerTokenThread = ThreadUtils.runDaemonThread(this::cleanerToken);
        }
    }

    @Override
    public void stop() {

    }

    private void cleanerToken() {
        int dataTimeout = 5 * 60;
        int interval = 60;
        Thread.currentThread().setName("csrfToken-cleaner-interval-" + interval + "-timeout-" + dataTimeout);

        log.info("[RdpCsrfTokenService] Init csrfTokenCleaner worker intervalSec " + interval + ", tokenTimeoutSec " + dataTimeout);
        while (true) {
            try {
                int updated = authDal.csrfTokenMapper().deleteBeforeTime(new Date(System.currentTimeMillis() - (dataTimeout * 1000)));
                log.info("[RdpCsrfTokenService] CsrfTokenCleaner " + updated + " expired tokens were deleted.");
            } catch (Exception e) {
                log.error("[RdpCsrfTokenService] CsrfTokenCleaner failed " + e.getMessage(), e);
            } finally {
                ThreadUtils.safeSleep(interval * 1000);
            }
        }
    }

    @Override
    public DmAuthCsrfTokenDO pullToken(String state) {
        DmAuthCsrfTokenDO tokenDO = this.authDal.csrfTokenMapper().queryByToken(state);
        this.authDal.csrfTokenMapper().deleteByToken(state);
        return tokenDO;
    }

    @Override
    public String pushToken(String secretToken) {
        DmAuthCsrfTokenDO tokenDO = new DmAuthCsrfTokenDO();
        tokenDO.setToken(RandomStrUtils.fixedLenRandomStr(32));
        tokenDO.setJumpUrl(null);
        tokenDO.setSecretToken(secretToken);
        this.authDal.csrfTokenMapper().insert(tokenDO);
        return tokenDO.getToken();
    }

    @Override
    public String randomTokenWithoutSave() {
        return RandomStrUtils.fixedLenRandomStr(32);
    }

    @Override
    public void storeJumpUrl(String token, String jumpUrl) {
        if (StringUtils.isBlank(token)) {
            return;
        }

        DmAuthCsrfTokenDO dbToken = this.authDal.csrfTokenMapper().queryByToken(token);
        if (dbToken == null) {
            DmAuthCsrfTokenDO tokenDO = new DmAuthCsrfTokenDO();
            tokenDO.setToken(token);
            tokenDO.setJumpUrl(jumpUrl);
            tokenDO.setSecretToken("");
            this.authDal.csrfTokenMapper().insert(tokenDO);
        } else {
            dbToken.setJumpUrl(jumpUrl);
            this.authDal.csrfTokenMapper().updateToken(token, dbToken);
        }
    }

    @Override
    public void storeSecretToken(String token, String secretToken) {
        if (StringUtils.isBlank(token)) {
            return;
        }

        DmAuthCsrfTokenDO dbToken = this.authDal.csrfTokenMapper().queryByToken(token);
        if (dbToken == null) {
            DmAuthCsrfTokenDO tokenDO = new DmAuthCsrfTokenDO();
            tokenDO.setToken(token);
            tokenDO.setJumpUrl(null);
            tokenDO.setSecretToken(secretToken);
            this.authDal.csrfTokenMapper().insert(tokenDO);
        } else {
            dbToken.setSecretToken(secretToken);
            this.authDal.csrfTokenMapper().updateToken(token, dbToken);
        }
    }

    @Override
    public String randomToken() {
        String random = RandomStrUtils.fixedLenRandomStr(32);

        DmAuthCsrfTokenDO tokenDO = new DmAuthCsrfTokenDO();
        tokenDO.setToken(random);
        tokenDO.setJumpUrl(null);
        tokenDO.setSecretToken(random);
        this.authDal.csrfTokenMapper().insert(tokenDO);
        return random;
    }
}
