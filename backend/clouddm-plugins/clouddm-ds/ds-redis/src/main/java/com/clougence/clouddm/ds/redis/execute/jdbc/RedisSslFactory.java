/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.ds.redis.execute.jdbc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.net.ssl.*;

import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.utils.StringUtils;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.SslOptions;
import redis.clients.jedis.SslVerifyMode;

final class RedisSslFactory {

    private static final byte[] RSA_ALGORITHM_IDENTIFIER = new byte[] { 0x30, 0x0d, 0x06, 0x09, 0x2a, (byte) 0x86, 0x48, (byte) 0x86, (byte) 0xf7, 0x0d, 0x01, 0x01, 0x01, 0x05,
                                                                        0x00 };

    private RedisSslFactory(){
    }

    static void apply(DefaultJedisClientConfig.Builder builder, Map<String, String> dsConfig) {
        SslMode mode = sslMode(dsConfig.get(JedisKeys.SSL_MODE));
        if (mode == SslMode.DISABLED) {
            builder.ssl(false);
            return;
        }

        builder.ssl(true);
        try {
            switch (mode) {
                case TRUST -> builder.sslOptions(SslOptions.builder().sslVerifyMode(SslVerifyMode.INSECURE).build());
                case CA -> applyCertificateAuthority(builder, dsConfig);
                case TRUSTSTORE -> builder.sslOptions(trustStoreOptions(dsConfig));
                case KEYSTORE_TRUSTSTORE -> builder.sslOptions(keyStoreTrustStoreOptions(dsConfig));
                case CLIENT_CERT -> applyClientCertificate(builder, dsConfig);
                default -> throw new IllegalArgumentException("unsupported Redis SSL mode: " + mode);
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalArgumentException("invalid Redis SSL " + mode + " configuration: " + e.getMessage(), e);
        }
    }

    private static void applyCertificateAuthority(DefaultJedisClientConfig.Builder builder, Map<String, String> dsConfig) throws GeneralSecurityException, IOException {
        File caFile = requiredFile(dsConfig, JedisKeys.SSL_CA_FILE, "CA certificate");
        SSLContext sslContext = sslContext(null, trustManagers(readCertificates(caFile)));
        builder.sslSocketFactory(sslContext.getSocketFactory());
        builder.sslParameters(sslParameters(false));
    }

    private static void applyClientCertificate(DefaultJedisClientConfig.Builder builder, Map<String, String> dsConfig) throws GeneralSecurityException, IOException {
        File caFile = requiredFile(dsConfig, JedisKeys.SSL_CA_FILE, "CA certificate");
        File clientCertFile = requiredFile(dsConfig, JedisKeys.SSL_CLIENT_CERT_FILE, "client certificate");
        File clientKeyFile = requiredFile(dsConfig, JedisKeys.SSL_CLIENT_KEY_FILE, "client private key");
        char[] keyPassword = password(dsConfig.get(JedisKeys.SSL_CLIENT_KEY_PASSWORD));

        X509Certificate[] clientCertificates = readCertificates(clientCertFile);
        PrivateKey privateKey = readPrivateKey(clientKeyFile, keyPassword, clientCertificates[0]);
        verifyKeyPair(privateKey, clientCertificates[0]);

        KeyManager[] keyManagers = keyManagers(privateKey, clientCertificates, keyPassword);
        TrustManager[] trustManagers = trustManagers(readCertificates(caFile));
        SSLContext sslContext = sslContext(keyManagers, trustManagers);
        builder.sslSocketFactory(sslContext.getSocketFactory());
        builder.sslParameters(sslParameters(true));
    }

    private static SslOptions trustStoreOptions(Map<String, String> dsConfig) {
        File trustStore = requiredFile(dsConfig, JedisKeys.SSL_CA_FILE, "TrustStore");
        return SslOptions.builder()
            .trustStoreType(keyStoreType(dsConfig.get(JedisKeys.SSL_CA_FORMAT), trustStore, "TrustStore"))
            .truststore(trustStore, password(dsConfig.get(JedisKeys.SSL_CA_PASSWORD)))
            .sslVerifyMode(SslVerifyMode.CA)
            .build();
    }

    private static SslOptions keyStoreTrustStoreOptions(Map<String, String> dsConfig) {
        File trustStore = requiredFile(dsConfig, JedisKeys.SSL_CA_FILE, "TrustStore");
        File keyStore = requiredFile(dsConfig, JedisKeys.SSL_CLIENT_CERT_FILE, "KeyStore");
        return SslOptions.builder()
            .trustStoreType(keyStoreType(dsConfig.get(JedisKeys.SSL_CA_FORMAT), trustStore, "TrustStore"))
            .truststore(trustStore, password(dsConfig.get(JedisKeys.SSL_CA_PASSWORD)))
            .keyStoreType(keyStoreType(dsConfig.get(JedisKeys.SSL_CLIENT_CERT_FORMAT), keyStore, "KeyStore"))
            .keystore(keyStore, password(dsConfig.get(JedisKeys.SSL_CLIENT_KEY_PASSWORD)))
            .sslVerifyMode(SslVerifyMode.FULL)
            .build();
    }

    private static SSLContext sslContext(KeyManager[] keyManagers, TrustManager[] trustManagers) throws GeneralSecurityException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagers, trustManagers, null);
        return sslContext;
    }

    private static SSLParameters sslParameters(boolean verifyHostname) {
        SSLParameters sslParameters = new SSLParameters();
        if (verifyHostname) {
            sslParameters.setEndpointIdentificationAlgorithm("HTTPS");
        }
        return sslParameters;
    }

    private static TrustManager[] trustManagers(X509Certificate[] certificates) throws GeneralSecurityException, IOException {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);
        for (int i = 0; i < certificates.length; i++) {
            trustStore.setCertificateEntry("redis-ca-" + i, certificates[i]);
        }

        TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        factory.init(trustStore);
        return factory.getTrustManagers();
    }

    private static KeyManager[] keyManagers(PrivateKey privateKey, X509Certificate[] certificates, char[] password) throws GeneralSecurityException, IOException {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(null, null);
        keyStore.setKeyEntry("redis-client", privateKey, password, certificates);

        KeyManagerFactory factory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        factory.init(keyStore, password);
        return factory.getKeyManagers();
    }

    private static X509Certificate[] readCertificates(File file) throws GeneralSecurityException, IOException {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates;
        try (ByteArrayInputStream input = new ByteArrayInputStream(Files.readAllBytes(file.toPath()))) {
            certificates = factory.generateCertificates(input);
        }
        if (certificates.isEmpty()) {
            throw new GeneralSecurityException("certificate file is empty: " + file.getName());
        }

        List<X509Certificate> result = new ArrayList<>();
        for (Certificate certificate : certificates) {
            if (!(certificate instanceof X509Certificate x509Certificate)) {
                throw new GeneralSecurityException("certificate is not X.509: " + file.getName());
            }
            result.add(x509Certificate);
        }
        return result.toArray(new X509Certificate[0]);
    }

    private static PrivateKey readPrivateKey(File file, char[] password, X509Certificate certificate) throws GeneralSecurityException, IOException {
        byte[] fileData = Files.readAllBytes(file.toPath());
        String pem = new String(fileData, StandardCharsets.US_ASCII);
        if (pem.contains("Proc-Type: 4,ENCRYPTED")) {
            throw new GeneralSecurityException("traditional encrypted PEM keys are not supported; use encrypted PKCS#8");
        }
        if (pem.contains("-----BEGIN ENCRYPTED PRIVATE KEY-----")) {
            return parsePkcs8(decryptPkcs8(pemBlock(pem, "ENCRYPTED PRIVATE KEY"), password), certificate);
        }
        if (pem.contains("-----BEGIN PRIVATE KEY-----")) {
            return parsePkcs8(pemBlock(pem, "PRIVATE KEY"), certificate);
        }
        if (pem.contains("-----BEGIN RSA PRIVATE KEY-----")) {
            return parsePkcs8(wrapPkcs8(pemBlock(pem, "RSA PRIVATE KEY"), RSA_ALGORITHM_IDENTIFIER), certificate);
        }
        if (pem.contains("-----BEGIN EC PRIVATE KEY-----")) {
            byte[] algorithmIdentifier = publicKeyAlgorithmIdentifier(certificate.getPublicKey().getEncoded());
            return parsePkcs8(wrapPkcs8(pemBlock(pem, "EC PRIVATE KEY"), algorithmIdentifier), certificate);
        }
        if (pem.contains("-----BEGIN")) {
            throw new GeneralSecurityException("unsupported private key PEM format: " + file.getName());
        }
        if (password.length > 0) {
            try {
                return parsePkcs8(decryptPkcs8(fileData, password), certificate);
            } catch (GeneralSecurityException ignored) {
                // The binary key may be an unencrypted PKCS#8 key despite a configured password.
            }
        }
        return parsePkcs8(fileData, certificate);
    }

    private static byte[] decryptPkcs8(byte[] encryptedData, char[] password) throws GeneralSecurityException, IOException {
        if (password.length == 0) {
            throw new GeneralSecurityException("client private key password is required");
        }

        EncryptedPrivateKeyInfo encryptedKey = new EncryptedPrivateKeyInfo(encryptedData);
        String algorithm = encryptedKey.getAlgName();
        AlgorithmParameters parameters = encryptedKey.getAlgParameters();
        if ("PBES2".equals(algorithm) && parameters != null) {
            algorithm = parameters.toString();
        }
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
        SecretKey secretKey = keyFactory.generateSecret(new PBEKeySpec(password));
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameters);
        return encryptedKey.getKeySpec(cipher).getEncoded();
    }

    private static PrivateKey parsePkcs8(byte[] encoded, X509Certificate certificate) throws GeneralSecurityException {
        String algorithm = certificate.getPublicKey().getAlgorithm();
        return KeyFactory.getInstance(algorithm).generatePrivate(new PKCS8EncodedKeySpec(encoded));
    }

    private static void verifyKeyPair(PrivateKey privateKey, X509Certificate certificate) throws GeneralSecurityException {
        String signatureAlgorithm = switch (privateKey.getAlgorithm()) {
            case "RSA" -> "SHA256withRSA";
            case "EC" -> "SHA256withECDSA";
            case "DSA" -> "SHA256withDSA";
            default -> throw new GeneralSecurityException("unsupported client private key algorithm: " + privateKey.getAlgorithm());
        };
        byte[] challenge = "CloudDM Redis SSL key verification".getBytes(StandardCharsets.UTF_8);
        Signature signature = Signature.getInstance(signatureAlgorithm);
        signature.initSign(privateKey);
        signature.update(challenge);
        byte[] signed = signature.sign();
        signature.initVerify(certificate);
        signature.update(challenge);
        if (!signature.verify(signed)) {
            throw new GeneralSecurityException("client certificate does not match the private key");
        }
    }

    private static byte[] pemBlock(String pem, String label) throws GeneralSecurityException {
        String begin = "-----BEGIN " + label + "-----";
        String end = "-----END " + label + "-----";
        int beginIndex = pem.indexOf(begin);
        int endIndex = pem.indexOf(end, beginIndex + begin.length());
        if (beginIndex < 0 || endIndex < 0) {
            throw new GeneralSecurityException("invalid " + label + " PEM data");
        }
        String base64 = pem.substring(beginIndex + begin.length(), endIndex).replaceAll("\\s", "");
        try {
            return Base64.getDecoder().decode(base64);
        } catch (IllegalArgumentException e) {
            throw new GeneralSecurityException("invalid " + label + " PEM data", e);
        }
    }

    private static byte[] wrapPkcs8(byte[] privateKey, byte[] algorithmIdentifier) {
        return der(0x30, concat(new byte[] { 0x02, 0x01, 0x00 }, algorithmIdentifier, der(0x04, privateKey)));
    }

    private static byte[] publicKeyAlgorithmIdentifier(byte[] publicKey) throws GeneralSecurityException {
        int[] outer = derElement(publicKey, 0, 0x30);
        int algorithmOffset = outer[0];
        int[] algorithm = derElement(publicKey, algorithmOffset, 0x30);
        return Arrays.copyOfRange(publicKey, algorithmOffset, algorithm[2]);
    }

    private static int[] derElement(byte[] data, int offset, int expectedTag) throws GeneralSecurityException {
        if (offset < 0 || offset + 2 > data.length || (data[offset] & 0xff) != expectedTag) {
            throw new GeneralSecurityException("invalid DER private key data");
        }
        int lengthByte = data[offset + 1] & 0xff;
        int contentOffset = offset + 2;
        int length;
        if ((lengthByte & 0x80) == 0) {
            length = lengthByte;
        } else {
            int lengthBytes = lengthByte & 0x7f;
            if (lengthBytes == 0 || lengthBytes > 4 || contentOffset + lengthBytes > data.length) {
                throw new GeneralSecurityException("invalid DER private key length");
            }
            length = 0;
            for (int i = 0; i < lengthBytes; i++) {
                length = (length << 8) | (data[contentOffset + i] & 0xff);
            }
            contentOffset += lengthBytes;
        }
        int endOffset = contentOffset + length;
        if (endOffset < contentOffset || endOffset > data.length) {
            throw new GeneralSecurityException("invalid DER private key length");
        }
        return new int[] { contentOffset, length, endOffset };
    }

    private static byte[] der(int tag, byte[] value) {
        return concat(new byte[] { (byte) tag }, derLength(value.length), value);
    }

    private static byte[] derLength(int length) {
        if (length < 0x80) {
            return new byte[] { (byte) length };
        }
        int bytes = 0;
        int remaining = length;
        while (remaining > 0) {
            bytes++;
            remaining >>>= 8;
        }
        byte[] result = new byte[bytes + 1];
        result[0] = (byte) (0x80 | bytes);
        for (int i = bytes; i > 0; i--) {
            result[i] = (byte) length;
            length >>>= 8;
        }
        return result;
    }

    private static byte[] concat(byte[]... values) {
        int length = 0;
        for (byte[] value : values) {
            length += value.length;
        }
        byte[] result = new byte[length];
        int offset = 0;
        for (byte[] value : values) {
            System.arraycopy(value, 0, result, offset, value.length);
            offset += value.length;
        }
        return result;
    }

    private static File requiredFile(Map<String, String> dsConfig, String configKey, String usage) {
        String path = dsConfig.get(configKey);
        if (StringUtils.isBlank(path)) {
            throw new IllegalArgumentException("Redis SSL " + usage + " is required");
        }
        File file = new File(path);
        SslOptions.assertFile(usage, file);
        return file;
    }

    private static String keyStoreType(String format, File file, String usage) {
        String actualFormat = format;
        if (StringUtils.isBlank(actualFormat)) {
            String name = file.getName();
            int dot = name.lastIndexOf('.');
            actualFormat = dot < 0 ? "" : name.substring(dot + 1);
        }
        return switch (actualFormat.toLowerCase()) {
            case "p12", "pfx", "pkcs12" -> "PKCS12";
            case "jks" -> "JKS";
            default -> throw new IllegalArgumentException("Redis SSL " + usage + " must be PKCS12 or JKS");
        };
    }

    private static char[] password(String value) {
        return value == null ? new char[0] : value.toCharArray();
    }

    private static SslMode sslMode(String value) {
        if (StringUtils.isBlank(value)) {
            return SslMode.DISABLED;
        }
        return SslMode.valueOf(value);
    }
}
