package com.clougence.rdp.component.csrf;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.rdp.dal.mapper.RdpCsrfTokenMapper;
import com.clougence.rdp.dal.model.RdpCsrfTokenDO;
import com.clougence.rdp.util.RandomStrUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ThreadUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RdpCsrfTokenServiceImpl implements RdpCsrfTokenService, UnifiedPostConstruct {

    @Resource
    private RdpCsrfTokenMapper tokenMapper;
    private Thread             cleanerTokenThread;

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
                int updated = tokenMapper.deleteBeforeTime(new Date(System.currentTimeMillis() - (dataTimeout * 1000)));
                log.info("[RdpCsrfTokenService] CsrfTokenCleaner " + updated + " expired tokens were deleted.");
            } catch (Exception e) {
                log.error("[RdpCsrfTokenService] CsrfTokenCleaner failed " + e.getMessage(), e);
            } finally {
                ThreadUtils.safeSleep(interval * 1000);
            }
        }
    }

    @Override
    public RdpCsrfTokenDO pullToken(String state) {
        RdpCsrfTokenDO tokenDO = this.tokenMapper.queryByToken(state);
        this.tokenMapper.deleteByToken(state);
        return tokenDO;
    }

    @Override
    public String pushToken(String secretToken) {
        RdpCsrfTokenDO tokenDO = new RdpCsrfTokenDO();
        tokenDO.setToken(RandomStrUtils.fixedLenRandomStr(32));
        tokenDO.setJumpUrl(null);
        tokenDO.setSecretToken(secretToken);
        this.tokenMapper.insert(tokenDO);
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

        RdpCsrfTokenDO dbToken = this.tokenMapper.queryByToken(token);
        if (dbToken == null) {
            RdpCsrfTokenDO tokenDO = new RdpCsrfTokenDO();
            tokenDO.setToken(token);
            tokenDO.setJumpUrl(jumpUrl);
            tokenDO.setSecretToken("");
            this.tokenMapper.insert(tokenDO);
        } else {
            dbToken.setJumpUrl(jumpUrl);
            this.tokenMapper.updateToken(token, dbToken);
        }
    }

    @Override
    public void storeSecretToken(String token, String secretToken) {
        if (StringUtils.isBlank(token)) {
            return;
        }

        RdpCsrfTokenDO dbToken = this.tokenMapper.queryByToken(token);
        if (dbToken == null) {
            RdpCsrfTokenDO tokenDO = new RdpCsrfTokenDO();
            tokenDO.setToken(token);
            tokenDO.setJumpUrl(null);
            tokenDO.setSecretToken(secretToken);
            this.tokenMapper.insert(tokenDO);
        } else {
            dbToken.setSecretToken(secretToken);
            this.tokenMapper.updateToken(token, dbToken);
        }
    }

    @Override
    public String randomToken() {
        String random = RandomStrUtils.fixedLenRandomStr(32);

        RdpCsrfTokenDO tokenDO = new RdpCsrfTokenDO();
        tokenDO.setToken(random);
        tokenDO.setJumpUrl(null);
        tokenDO.setSecretToken(random);
        this.tokenMapper.insert(tokenDO);
        return random;
    }
}
