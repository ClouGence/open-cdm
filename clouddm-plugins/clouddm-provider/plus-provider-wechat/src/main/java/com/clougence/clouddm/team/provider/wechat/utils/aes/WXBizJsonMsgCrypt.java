package com.clougence.clouddm.team.provider.wechat.utils.aes;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.clougence.clouddm.team.provider.wechat.constants.WechatI18nKey2;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;

/**
 * Provide encryption and decryption interfaces (UTF8 encoded strings) for receiving and pushing enterprise WeChat messages
 * <ol>
 * Third party replies encrypted messages to enterprise WeChat</li>
 * The third party receives the message sent by the enterprise WeChat, verifies the security of the message, and decrypts the message</ li>
 * </ol>
 * Explanation: Exception java. security InvalidKeyException: Solution to Illegal Key Size
 * <ol>
 * Download the JCE unrestricted permission policy file from the official website (download link for JDK7:
 *       http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html </li>
 * <li>After downloading and decompressing, you can see local_policy.jar, US_dexport_policy.jar, and readme. txt</li>
 * If JRE is installed, place the two jar files in the% JRE-HOME% \ lib \ security directory and overwrite the original files</li>
 * If JDK is installed, place the two jar files in the% JDK_HOME% \ jre \ lib \ security directory to overwrite the original files</li>
 * </ol>
 */
public class WXBizJsonMsgCrypt {

    static Charset CHARSET = Charset.forName("utf-8");
    Base64         base64  = new Base64();
    byte[]         aesKey;
    String         token;
    String         receiveid;

    /**
     * Constructor function
     * @param token Enterprise WeChat backend, token set by developers
     * @param encodingAesKey Enterprise WeChat backend, EncodingAESKey set by developers
     * @param receiveid have different meanings in different scenarios, please refer to the documentation for details
     */
    public WXBizJsonMsgCrypt(String token, String encodingAesKey, String receiveid){
        if (encodingAesKey.length() != 43) {
            throw ThirdPartyApiException.asRDP().with(WechatI18nKey2.WECHAT_ILLEGAL_AES_KEY_ERROR);
        }

        this.token = token;
        this.receiveid = receiveid;
        aesKey = Base64.decodeBase64(encodingAesKey + "=");
    }

    // Generate a 4-byte network byte order
    byte[] getNetworkBytesOrder(int sourceNumber) {
        byte[] orderBytes = new byte[4];
        orderBytes[3] = (byte) (sourceNumber & 0xFF);
        orderBytes[2] = (byte) (sourceNumber >> 8 & 0xFF);
        orderBytes[1] = (byte) (sourceNumber >> 16 & 0xFF);
        orderBytes[0] = (byte) (sourceNumber >> 24 & 0xFF);
        return orderBytes;
    }

    // Restore the network byte order of 4 bytes
    int recoverNetworkBytesOrder(byte[] orderBytes) {
        int sourceNumber = 0;
        for (int i = 0; i < 4; i++) {
            sourceNumber <<= 8;
            sourceNumber |= orderBytes[i] & 0xff;
        }
        return sourceNumber;
    }

    // Randomly generate a 16 bit string
    String getRandomStr() {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 16; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * Encrypt plaintext
     *
     * @param text requires encrypted plaintext
     * @return Encrypt the base64 encoded string
     */
    String encrypt(String randomStr, String text) {
        ByteGroup byteCollector = new ByteGroup();
        byte[] randomStrBytes = randomStr.getBytes(CHARSET);
        byte[] textBytes = text.getBytes(CHARSET);
        byte[] networkBytesOrder = getNetworkBytesOrder(textBytes.length);
        byte[] receiveidBytes = receiveid.getBytes(CHARSET);

        // randomStr + networkBytesOrder + text + receiveid
        byteCollector.addBytes(randomStrBytes);
        byteCollector.addBytes(networkBytesOrder);
        byteCollector.addBytes(textBytes);
        byteCollector.addBytes(receiveidBytes);

        // ... + pad: Fill in plaintext with a custom padding method
        byte[] padBytes = PKCS7Encoder.encode(byteCollector.size());
        byteCollector.addBytes(padBytes);

        // Obtain the final byte stream, unencrypted
        byte[] unencrypted = byteCollector.toBytes();

        try {
            // Set the encryption mode to AES CBC mode
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(aesKey, 0, 16);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

            // encryption
            byte[] encrypted = cipher.doFinal(unencrypted);

            // Encoding encrypted strings using BASE64

            return base64.encodeToString(encrypted);
        } catch (Exception e) {
            throw ThirdPartyApiException.asRDP().with(e, WechatI18nKey2.WECHAT_ENCRYPT_AES_ERROR);
        }
    }

    /**
     * Decrypt the ciphertext
     *
     * The ciphertext that @ param text needs to decrypt
     * @return Decrypted plaintext
     */
    String decrypt(String text) {
        byte[] original;
        try {
            // Set decryption mode to AES CBC mode
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec key_spec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
            cipher.init(Cipher.DECRYPT_MODE, key_spec, iv);

            // Decoding ciphertext using BASE64
            byte[] encrypted = Base64.decodeBase64(text);

            // decrypt
            original = cipher.doFinal(encrypted);
        } catch (Exception e) {
            throw ThirdPartyApiException.asRDP().with(e, WechatI18nKey2.WECHAT_DECRYPT_AES_ERROR);
        }

        String jsonContent, from_receiveid;
        try {
            // Remove padding characters
            byte[] bytes = PKCS7Encoder.decode(original);

            // Separate 16 bit random string, network byte order, and receiver id
            byte[] networkOrder = Arrays.copyOfRange(bytes, 16, 20);

            int jsonLength = recoverNetworkBytesOrder(networkOrder);

            jsonContent = new String(Arrays.copyOfRange(bytes, 20, 20 + jsonLength), CHARSET);
            from_receiveid = new String(Arrays.copyOfRange(bytes, 20 + jsonLength, bytes.length), CHARSET);
        } catch (Exception e) {
            throw ThirdPartyApiException.asRDP().with(e, WechatI18nKey2.WECHAT_ILLEGAL_BUFFER_ERROR);
        }

        // The situation where the receivers have different IDs
        if (!from_receiveid.equals(receiveid)) {
            throw ThirdPartyApiException.asRDP().with(WechatI18nKey2.WECHAT_VALIDATE_CORP_ID_ERROR);
        }
        return jsonContent;

    }

    /**
     * Encrypt and package enterprise WeChat replies to users' messages
     * <ol>
     * Encrypt the message to be sent with AES-CBC encryption</li>
     *<li>Generate secure signature</li>
     *<li>Package the message ciphertext and secure signature into JSON format</li>
     * </ol>
     *
     * @paramreplyMsg Enterprise WeChat message awaiting reply from user, JSON format string
     * @paramtimeStamp timestamp, can be generated by oneself or used as a timestamp for URL parameters
     * @param nonce random string, can be generated by oneself, or can be used with URL parameter nonce
     *
     * @return encrypted ciphertext that can directly reply to the user, including msgsignature, A JSON formatted string of timestamp, nonce, and encrypt
     */
    public String EncryptMsg(String replyMsg, String timeStamp, String nonce) {
        // encrypt
        String encrypt = encrypt(getRandomStr(), replyMsg);

        if (timeStamp == "") {
            timeStamp = Long.toString(System.currentTimeMillis());
        }

        String signature = SHA1.getSHA1(token, timeStamp, nonce, encrypt);

        // json
        return JsonParse.generate(encrypt, signature, timeStamp, nonce);
    }

    /**
     * Verify the authenticity of the message and obtain the decrypted plaintext
     * <ol>
     * Generate a secure signature using the received ciphertext and perform signature verification</li>
     * If the verification is successful, extract the encrypted message from the JSON file</li>
     * Decrypt the message</li>
     * </ol>
     *
     * @parammsgSignature signature string, corresponding to the msgsignature of the URL parameter
     * @paramtimeStamp timestamp, corresponding to the timestamp of the URL parameter
     * @param nonce random string, corresponding to the nonce of the URL parameter
     * @parampostData ciphertext, corresponding to the data of POST request
     *
     * @return Decrypted Original Text
     */
    public String DecryptMsg(String msgSignature, String timeStamp, String nonce, String postData) {

        // Key, app secret for public accounts
        // Extract ciphertext
        Object[] encrypt = JsonParse.extract(postData);

        // Verify secure signature
        String signature = SHA1.getSHA1(token, timeStamp, nonce, encrypt[1].toString());

        // Compare with the signature in the URL to see if it is equal
        if (!signature.equals(msgSignature)) {
            throw ThirdPartyApiException.asRDP().with(WechatI18nKey2.WECHAT_VALIDATE_SIGNATURE_ERROR);
        }

        // decrypt
        return decrypt(encrypt[1].toString());
    }

    /**
     * Verify URL
     * @parammsgSignature signature string, corresponding to the msgsignature of the URL parameter
     * @paramtimeStamp timestamp, corresponding to the timestamp of the URL parameter
     * @param nonce random string, corresponding to the nonce of the URL parameter
     * @paramechoStr random string, corresponding to the echostr of the URL parameter
     *
     * @return Decrypted echostr
     */
    public String VerifyURL(String msgSignature, String timeStamp, String nonce, String echoStr) {
        String signature = SHA1.getSHA1(token, timeStamp, nonce, echoStr);

        if (!signature.equals(msgSignature)) {
            throw ThirdPartyApiException.asRDP().with(WechatI18nKey2.WECHAT_VALIDATE_SIGNATURE_ERROR);
        }

        return decrypt(echoStr);
    }

}
