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
package com.clougence.clouddm.api.common.crypt;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.clougence.utils.StringUtils;

class CryptSupport implements CryptService {

    private static final CryptService     INSTANCE              = new CryptSupport();
    private static final String           DEFAULT_KEY           = "UDq_RnLcgfsWygv2TtXGcPGmAGee9dZE";
    private static final String           DEFAULT_SALT          = "FFLPmbhZXckU9qd2qiH";

    private static final SecureRandom     SECURE_RANDOM         = new SecureRandom();
    private static final String           AES_TRANSFORMATION    = "AES/CBC/PKCS5Padding";
    private static final String           AES_KEY_FACTORY       = "PBKDF2WithHmacSHA1";
    private static final int              AES_ITERATIONS        = 1024;
    private static final int              AES_KEY_LENGTH        = 256;
    private static final int              AES_BLOCK_SIZE        = 16;

    private static final String           PBKDF2_PREFIX         = "pbkdf2";
    private static final String           PBKDF2_SHA256_TOKEN   = "sha256";
    private static final String           PBKDF2_SHA1_TOKEN     = "sha1";
    private static final String           PBKDF2_SHA256_FACTORY = "PBKDF2WithHmacSHA256";
    private static final String           PBKDF2_SHA1_FACTORY   = "PBKDF2WithHmacSHA1";
    private static final int              PASSWORD_ITERATIONS   = 310000;
    private static final int              PASSWORD_SALT_BYTES   = 16;
    private static final int              PASSWORD_HASH_BYTES   = 32;

    private static final Pattern          HEX_PATTERN           = Pattern.compile("^[0-9a-fA-F]+$");

    private static final PasswordHashSpec DEFAULT_PASSWORD_HASH = resolveDefaultPasswordHash();

    private CryptSupport(){
    }

    public static CryptService getInstance() { return INSTANCE; }

    @Override
    public String encryptUseDefaultKeyAndSalt(String plainPassword) {
        PasswordInfo passInfo = new PasswordInfo();
        passInfo.setPlainPassword(plainPassword);
        encrypt(passInfo);
        return passInfo.getEncryptPassword();
    }

    @Override
    public String decryptUseDefaultKeyAndSalt(String encryptPassword) {
        PasswordInfo passInfo = new PasswordInfo();
        passInfo.setEncryptPassword(encryptPassword);
        decrypt(passInfo);
        return passInfo.getPlainPassword();
    }

    @Override
    public void encrypt(PasswordInfo passwordInfo) {
        if (StringUtils.isNotBlank(passwordInfo.getKey()) && StringUtils.isNotBlank(passwordInfo.getSalt())) {
            passwordInfo.setEncryptPassword(encryptAes(passwordInfo.getPlainPassword(), passwordInfo.getKey(), passwordInfo.getSalt()));
        } else {
            passwordInfo.setEncryptPassword(encryptAes(passwordInfo.getPlainPassword(), DEFAULT_KEY, DEFAULT_SALT));
        }
    }

    @Override
    public void decrypt(PasswordInfo passwordInfo) {
        if (StringUtils.isNotBlank(passwordInfo.getEncryptPassword())) {
            if (StringUtils.isNotBlank(passwordInfo.getKey()) && StringUtils.isNotBlank(passwordInfo.getSalt())) {
                passwordInfo.setPlainPassword(decryptAes(passwordInfo.getEncryptPassword(), passwordInfo.getKey(), passwordInfo.getSalt()));
            } else {
                passwordInfo.setPlainPassword(decryptAes(passwordInfo.getEncryptPassword(), DEFAULT_KEY, DEFAULT_SALT));
            }
        } else {
            throw new RuntimeException("encrypted password can not be empty when use symmetric decrypt");
        }
    }

    @Override
    public PasswordInfo encryptForOneWay(String plainPassword) {
        if (StringUtils.isNotBlank(plainPassword)) {
            PasswordInfo re = new PasswordInfo();
            re.setEncryptPassword(encodePassword(plainPassword));
            return re;
        } else {
            throw new RuntimeException("plain password can not be empty when use one-way encrypt");
        }
    }

    @Override
    public boolean isMatchForOneWay(PasswordInfo passwordInfo) {
        if (StringUtils.isNotBlank(passwordInfo.getEncryptPassword()) && StringUtils.isNotBlank(passwordInfo.getPlainPassword())) {
            return matchesPassword(passwordInfo.getPlainPassword(), passwordInfo.getEncryptPassword());
        } else {
            throw new RuntimeException("encrypted password and plain password can not be empty when test whether two password is match");
        }
    }

    public static String encryptAes(String plainText, String password, String salt) {
        byte[] iv = randomBytes(AES_BLOCK_SIZE);
        byte[] encrypted = doCipher(Cipher.ENCRYPT_MODE, plainText.getBytes(StandardCharsets.UTF_8), password, salt, iv);
        byte[] output = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, output, 0, iv.length);
        System.arraycopy(encrypted, 0, output, iv.length, encrypted.length);
        return toHex(output);
    }

    public static String decryptAes(String encryptedHex, String password, String salt) {
        byte[] encrypted = fromHex(encryptedHex);
        if (encrypted.length < AES_BLOCK_SIZE) {
            throw new IllegalArgumentException("encrypted data is invalid");
        }

        byte[] iv = new byte[AES_BLOCK_SIZE];
        byte[] cipherBytes = new byte[encrypted.length - AES_BLOCK_SIZE];
        System.arraycopy(encrypted, 0, iv, 0, AES_BLOCK_SIZE);
        System.arraycopy(encrypted, AES_BLOCK_SIZE, cipherBytes, 0, cipherBytes.length);

        byte[] plainBytes = doCipher(Cipher.DECRYPT_MODE, cipherBytes, password, salt, iv);
        return new String(plainBytes, StandardCharsets.UTF_8);
    }

    private static String encodePassword(String plainPassword) {
        byte[] salt = randomBytes(PASSWORD_SALT_BYTES);
        byte[] hash = pbkdf2(plainPassword.toCharArray(), salt, PASSWORD_ITERATIONS, PASSWORD_HASH_BYTES * 8, DEFAULT_PASSWORD_HASH.factory);
        return PBKDF2_PREFIX + '$' + DEFAULT_PASSWORD_HASH.token + '$' + PASSWORD_ITERATIONS + '$' + toHex(salt) + '$' + toHex(hash);
    }

    private static boolean matchesPassword(String plainPassword, String encodedPassword) {
        if (!encodedPassword.startsWith(PBKDF2_PREFIX + "$")) {
            return false;
        }

        String[] parts = encodedPassword.split("\\$", 5);
        if (parts.length != 5) {
            return false;
        }

        PasswordHashSpec hashSpec = resolveHashSpec(parts[1]);
        int iterations = Integer.parseInt(parts[2]);
        byte[] salt = fromHex(parts[3]);
        byte[] expected = fromHex(parts[4]);
        byte[] actual = pbkdf2(plainPassword.toCharArray(), salt, iterations, expected.length * 8, hashSpec.factory);
        return MessageDigest.isEqual(expected, actual);
    }

    private static byte[] doCipher(int mode, byte[] input, String password, String salt, byte[] iv) {
        try {
            byte[] saltBytes = normalizeSalt(salt);
            byte[] keyBytes = pbkdf2(password.toCharArray(), saltBytes, AES_ITERATIONS, AES_KEY_LENGTH, AES_KEY_FACTORY);
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(mode, secretKey, new IvParameterSpec(iv));
            return cipher.doFinal(input);
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("crypt operation failed", ex);
        }
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int keyLength, String factory) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
            return SecretKeyFactory.getInstance(factory).generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("pbkdf2 operation failed", ex);
        }
    }

    private static byte[] normalizeSalt(String salt) {
        if (salt != null && salt.length() % 2 == 0 && HEX_PATTERN.matcher(salt).matches()) {
            return fromHex(salt);
        }
        return salt.getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] randomBytes(int length) {
        byte[] bytes = new byte[length];
        SECURE_RANDOM.nextBytes(bytes);
        return bytes;
    }

    private static String toHex(byte[] bytes) {
        char[] chars = new char[bytes.length * 2];
        for (int index = 0; index < bytes.length; index++) {
            int value = bytes[index] & 0xFF;
            chars[index * 2] = Character.forDigit(value >>> 4, 16);
            chars[index * 2 + 1] = Character.forDigit(value & 0x0F, 16);
        }
        return new String(chars);
    }

    private static byte[] fromHex(String value) {
        if (value == null || value.length() % 2 != 0 || !HEX_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("hex value is invalid");
        }

        byte[] bytes = new byte[value.length() / 2];
        for (int index = 0; index < value.length(); index += 2) {
            int high = Character.digit(value.charAt(index), 16);
            int low = Character.digit(value.charAt(index + 1), 16);
            bytes[index / 2] = (byte) ((high << 4) + low);
        }
        return bytes;
    }

    private static PasswordHashSpec resolveDefaultPasswordHash() {
        if (isSecretKeyFactoryAvailable(PBKDF2_SHA256_FACTORY)) {
            return new PasswordHashSpec(PBKDF2_SHA256_TOKEN, PBKDF2_SHA256_FACTORY);
        }
        return new PasswordHashSpec(PBKDF2_SHA1_TOKEN, PBKDF2_SHA1_FACTORY);
    }

    private static PasswordHashSpec resolveHashSpec(String token) {
        if (PBKDF2_SHA256_TOKEN.equals(token)) {
            return new PasswordHashSpec(PBKDF2_SHA256_TOKEN, PBKDF2_SHA256_FACTORY);
        }
        if (PBKDF2_SHA1_TOKEN.equals(token)) {
            return new PasswordHashSpec(PBKDF2_SHA1_TOKEN, PBKDF2_SHA1_FACTORY);
        }
        throw new IllegalArgumentException("unsupported password hash algorithm: " + token);
    }

    private static boolean isSecretKeyFactoryAvailable(String algorithm) {
        try {
            SecretKeyFactory.getInstance(algorithm);
            return true;
        } catch (GeneralSecurityException ex) {
            return false;
        }
    }

    private static final class PasswordHashSpec {

        private final String token;
        private final String factory;

        private PasswordHashSpec(String token, String factory){
            this.token = token;
            this.factory = factory;
        }
    }
}
