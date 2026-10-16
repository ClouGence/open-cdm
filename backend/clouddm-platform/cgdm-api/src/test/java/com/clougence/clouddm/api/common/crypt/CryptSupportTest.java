package com.clougence.clouddm.api.common.crypt;

import org.junit.Test;

public class CryptSupportTest {

    @Test
    public void encryptOneWay() {
        String ePwd = CryptSupport.getInstance().encryptForOneWay("xxxx").getEncryptPassword();
        System.out.println(ePwd);
    }
}
