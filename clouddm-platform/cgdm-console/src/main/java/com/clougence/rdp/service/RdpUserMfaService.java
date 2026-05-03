package com.clougence.rdp.service;

public interface RdpUserMfaService {

    String MFA_PRE_ACTION_TYPE = "MFA_PRE_ACTION_TYPE";

    String MFA_LOGIN_JWT_TOKEN = "MFA_LOGIN_JWT_TOKEN";

    byte[] initUserMfaSetting(String uid);

    byte[] resetMfaSetting(String uid, int mfaCode);

    void confirmUserMfaSetting(String uid, boolean reset, int mfaCode);

    boolean validMfaCode(String uid, int mfaCode);

    void closeUserMfa(String uid, int mfaCode);
}
