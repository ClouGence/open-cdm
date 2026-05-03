package com.clougence.rdp.component.openapi;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author bucketli 2021/10/11 19:47
 */
public class OpenApiSigner {

    public static final String  ENCODING       = "UTF-8";

    private static final String ALGORITHM_NAME = "HmacSHA1";

    public static String signString(String stringToSign, String accessKeySecret) {
        try {
            Mac mac = Mac.getInstance(ALGORITHM_NAME);
            mac.init(new SecretKeySpec(accessKeySecret.getBytes(ENCODING), ALGORITHM_NAME));
            byte[] signData = mac.doFinal(stringToSign.getBytes(ENCODING));
            return Base64.getEncoder().encodeToString(signData);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            throw new IllegalArgumentException(e.toString());
        }
    }

    public static String composeStringToSign(Map<String, String> queries) {
        try {
            String paramsStr = genSortedParamsStr(queries);
            return percentEncode(paramsStr);
        } catch (UnsupportedEncodingException exp) {
            throw new RuntimeException("UTF-8 encoding is not supported.");
        }
    }

    public static String genSortedParamsStr(Map<String, String> queries) {
        String[] sortedKeys = queries.keySet().toArray(new String[] {});
        Arrays.sort(sortedKeys);
        StringBuilder queryString = new StringBuilder();
        try {
            boolean first = true;
            for (String key : sortedKeys) {
                if (first) {
                    first = false;
                } else {
                    queryString.append("&");
                }

                queryString.append(percentEncode(key)).append("=").append(percentEncode(queries.get(key)));
            }

            return queryString.toString();
        } catch (UnsupportedEncodingException exp) {
            throw new RuntimeException("UTF-8 encoding is not supported.");
        }
    }

    private static String percentEncode(String value) throws UnsupportedEncodingException {
        return value != null ? URLEncoder.encode(value, ENCODING).replace("+", "%20").replace("*", "%2A").replace("%7E", "~") : null;
    }
}
