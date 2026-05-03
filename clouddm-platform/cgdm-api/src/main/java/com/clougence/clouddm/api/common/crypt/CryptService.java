package com.clougence.clouddm.api.common.crypt;

public interface CryptService {

    CryptService INSTANCE = CryptSupport.getInstance();

    String encryptUseDefaultKeyAndSalt(String plainPassword);

    String decryptUseDefaultKeyAndSalt(String encryptPassword);

    void encrypt(PasswordInfo passwordInfo);

    void decrypt(PasswordInfo passwordInfo);

    PasswordInfo encryptForOneWay(String plainPassword);

    boolean isMatchForOneWay(PasswordInfo passwordInfo);
}
