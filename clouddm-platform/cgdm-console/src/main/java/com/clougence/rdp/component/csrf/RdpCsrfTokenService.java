package com.clougence.rdp.component.csrf;

import com.clougence.rdp.dal.model.RdpCsrfTokenDO;

public interface RdpCsrfTokenService {

    RdpCsrfTokenDO pullToken(String state);

    String pushToken(String secretToken);

    String randomToken();

    String randomTokenWithoutSave();

    void storeJumpUrl(String token, String jumpUrl);

    void storeSecretToken(String token, String secretToken);
}
