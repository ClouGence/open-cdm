package com.clougence.clouddm.team.provider.wechat.utils.aes;

import java.security.MessageDigest;
import java.util.Arrays;

import com.clougence.clouddm.team.provider.wechat.constants.WechatI18nKey2;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;

/**
 * SHA1 class
 * Compute message signature interface
 */
class SHA1 {

    /**
     * Generate secure signatures using SHA1 algorithm
     * @param token ticket
     * @paramtimestamp timestamp
     * @param nonce random string
     * @param encrypt ciphertext
     * @return Secure Signature
     */
    public static String getSHA1(String token, String timestamp, String nonce, String encrypt) {
        try {
            String[] array = new String[] { token, timestamp, nonce, encrypt };
            StringBuffer sb = new StringBuffer();

            Arrays.sort(array);
            for (int i = 0; i < 4; i++) {
                sb.append(array[i]);
            }
            String str = sb.toString();

            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());
            byte[] digest = md.digest();

            StringBuffer hexstr = new StringBuffer();
            String shaHex = "";
            for (int i = 0; i < digest.length; i++) {
                shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
            return hexstr.toString();
        } catch (Exception e) {
            throw ThirdPartyApiException.asRDP().with(e, WechatI18nKey2.WECHAT_COMPUTE_SIGNATURE_ERROR);
        }
    }
}
