package com.clougence.rdp.component.sso;

import com.clougence.clouddm.sdk.security.login.LoginProvider;

public interface RdpSubLoginService {

    boolean checkLoginEnable(String ownerUid, LoginProvider type);
}
