package com.clougence.rdp.component.openapi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.clougence.rdp.component.openapi.exception.ClientException;
import com.clougence.rdp.component.openapi.exception.ServerException;
import com.clougence.utils.ExceptionUtils;
import com.fasterxml.uuid.Generators;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

/**
 * @author bucketli 2021/11/10 11:59:18
 */
@Slf4j
public class OpenApiHttpClient {

    public static final MediaType JSON             = MediaType.get("application/json; charset=utf-8");

    public static final MediaType FILE             = MediaType.get("application/x-www-form-urlencoded");

    private final String          host;

    private final Integer         openApiTimeout;

    private final String          accessKey;

    private final String          secretKey;

    private static final String   SIGNATURE_METHOD = "HmacSHA1";

    public OpenApiHttpClient(String host, Integer openApiTimeout, String accessKey, String secretKey){
        this.host = host;
        this.openApiTimeout = openApiTimeout;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String doJsonPost(String uri, String content) throws Exception {
        Response response = null;
        try {
            Map<String, String> commonParams = genCommonParams();
            String url = genFullUrl(uri, commonParams);

            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, content);
            Request request = new Request.Builder().url(url).post(body).build();
            response = client.newCall(request).execute();
            if (response.code() >= 200 && response.code() < 300) {
                return Objects.requireNonNull(response.body()).string();
            } else {
                throw new ServerException(String.valueOf(response.code()), Objects.requireNonNull(response.body()).string());
            }
        } catch (IOException e) {
            String msg = "failed to request to open api endpoint(" + host + "),msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new ClientException(e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    protected String genFullUrl(String uri, Map<String, String> commonParams) {
        String paramStr = OpenApiSigner.genSortedParamsStr(commonParams);
        return "http://" + host + uri + "?" + paramStr;
    }

    protected Map<String, String> genCommonParams() {
        String nonce = Generators.timeBasedGenerator().generate().toString();
        Map<String, String> commonParams = new HashMap<>();
        commonParams.put("SignatureMethod", SIGNATURE_METHOD);
        commonParams.put("SignatureNonce", nonce);
        commonParams.put("AccessKeyId", accessKey);

        String paramStr = OpenApiSigner.composeStringToSign(commonParams);
        String signature = OpenApiSigner.signString(paramStr, secretKey);

        commonParams.put("Signature", signature);

        return commonParams;
    }
}
