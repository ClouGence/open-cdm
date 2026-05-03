package com.clougence.clouddm.team.provider.wechat.utils.aes;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Provide encryption and decryption interfaces based on PKCS7 algorithm
 */
class PKCS7Encoder {

    static Charset CHARSET    = Charset.forName("utf-8");
    static int     BLOCK_SIZE = 32;

    /**
     * Obtain bytes for padding plaintext
     * 
     * @param count The number of plaintext bytes that require padding operation
     * @return Byte array for filling in
     */
    static byte[] encode(int count) {
        // 计算需要填充的位数
        int amountToPad = BLOCK_SIZE - (count % BLOCK_SIZE);
        if (amountToPad == 0) {
            amountToPad = BLOCK_SIZE;
        }
        // 获得补位所用的字符
        char padChr = chr(amountToPad);
        String tmp = new String();
        for (int index = 0; index < amountToPad; index++) {
            tmp += padChr;
        }
        return tmp.getBytes(CHARSET);
    }

    /**
     * Delete the padding characters of decrypted plaintext
     *
     * @aram decrypted plaintext
     * @return Clear text after removing padding characters
     */
    static byte[] decode(byte[] decrypted) {
        int pad = (int) decrypted[decrypted.length - 1];
        if (pad < 1 || pad > 32) {
            pad = 0;
        }
        return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
    }

    /**
     * Convert numbers into characters corresponding to ASCII code, used to complement plaintext
     * 
     * @param a needs to convert
     * @return Converted characters
     */
    static char chr(int a) {
        byte target = (byte) (a & 0xFF);
        return (char) target;
    }

}
