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
package com.clougence.clouddm.dsfamily.execute.ssl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.clougence.clouddm.base.metadata.ds.SslMode;

public class DsSslSupportTest {

    // sslMode ----------------------------------------------------------------
    @Test
    public void sslMode_defaultsToDisabledWhenBlank() {
        Assertions.assertSame(SslMode.DISABLED, DsSslSupport.sslMode(null));
        Assertions.assertSame(SslMode.DISABLED, DsSslSupport.sslMode(""));
        Assertions.assertSame(SslMode.DISABLED, DsSslSupport.sslMode("   "));
    }

    @Test
    public void sslMode_parsesValidValues() {
        Assertions.assertSame(SslMode.TRUST, DsSslSupport.sslMode("TRUST"));
        Assertions.assertSame(SslMode.CA, DsSslSupport.sslMode("CA"));
        Assertions.assertSame(SslMode.TRUSTSTORE, DsSslSupport.sslMode("TRUSTSTORE"));
        Assertions.assertSame(SslMode.KEYSTORE_TRUSTSTORE, DsSslSupport.sslMode("KEYSTORE_TRUSTSTORE"));
        Assertions.assertSame(SslMode.CLIENT_CERT, DsSslSupport.sslMode("CLIENT_CERT"));
    }

    @Test
    public void sslMode_throwsOnUnknownValue() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> DsSslSupport.sslMode("FOO"));
    }

    // password ---------------------------------------------------------------
    @Test
    public void password_returnsEmptyForNull() {
        Assertions.assertEquals(0, DsSslSupport.password(null).length);
    }

    @Test
    public void password_returnsEmptyForEmpty() {
        Assertions.assertEquals(0, DsSslSupport.password("").length);
    }

    @Test
    public void password_convertsValueToChars() {
        Assertions.assertArrayEquals(new char[] { 'a', 'b', 'c' }, DsSslSupport.password("abc"));
    }

    // keyStoreType -----------------------------------------------------------
    @Test
    public void keyStoreType_usesExplicitFormat() {
        // When format is given, the file extension is ignored.
        Assertions.assertEquals("PKCS12", DsSslSupport.keyStoreType("p12", new File("ca.txt"), "TrustStore"));
        Assertions.assertEquals("PKCS12", DsSslSupport.keyStoreType("pfx", new File("ca.txt"), "TrustStore"));
        Assertions.assertEquals("PKCS12", DsSslSupport.keyStoreType("pkcs12", new File("ca.txt"), "TrustStore"));
        Assertions.assertEquals("JKS", DsSslSupport.keyStoreType("jks", new File("ca.txt"), "TrustStore"));
        Assertions.assertEquals("PKCS12", DsSslSupport.keyStoreType("P12", new File("ca.txt"), "TrustStore"));
    }

    @Test
    public void keyStoreType_infersFromExtensionWhenFormatBlank() {
        Assertions.assertEquals("PKCS12", DsSslSupport.keyStoreType(null, new File("ca.p12"), "TrustStore"));
        Assertions.assertEquals("PKCS12", DsSslSupport.keyStoreType("", new File("ca.pfx"), "TrustStore"));
        Assertions.assertEquals("PKCS12", DsSslSupport.keyStoreType("  ", new File("ca.PKCS12"), "TrustStore"));
        Assertions.assertEquals("JKS", DsSslSupport.keyStoreType(null, new File("ca.jks"), "TrustStore"));
    }

    @Test
    public void keyStoreType_rejectsUnknownFormat() {
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> DsSslSupport.keyStoreType("foo", new File("ca.p12"), "TrustStore"));
    }

    @Test
    public void keyStoreType_rejectsUnknownExtension() {
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> DsSslSupport.keyStoreType(null, new File("ca.txt"), "TrustStore"));
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> DsSslSupport.keyStoreType(null, new File("ca"), "TrustStore"));
    }

    // requiredFile -----------------------------------------------------------
    @Test
    public void requiredFile_throwsWhenPathMissing() {
        Map<String, String> config = Map.of();
        IllegalArgumentException ex = Assertions.assertThrows(IllegalArgumentException.class,
            () -> DsSslSupport.requiredFile(config, "sslCaFile", "CA certificate"));
        Assertions.assertTrue(ex.getMessage().contains("CA certificate is required"));
    }

    @Test
    public void requiredFile_throwsWhenPathBlank() {
        Map<String, String> config = new HashMap<>();
        config.put("sslCaFile", "   ");
        IllegalArgumentException ex = Assertions.assertThrows(IllegalArgumentException.class,
            () -> DsSslSupport.requiredFile(config, "sslCaFile", "CA certificate"));
        Assertions.assertTrue(ex.getMessage().contains("CA certificate is required"));
    }

    @Test
    public void requiredFile_throwsWhenFileMissing() {
        Map<String, String> config = Map.of("sslCaFile", "/no/such/file/ca.pem");
        IllegalArgumentException ex = Assertions.assertThrows(IllegalArgumentException.class,
            () -> DsSslSupport.requiredFile(config, "sslCaFile", "CA certificate"));
        Assertions.assertTrue(ex.getMessage().contains("file does not exist"));
    }

    @Test
    public void requiredFile_returnsExistingFile(@TempDir Path tempDir) throws IOException {
        Path file = Files.createFile(tempDir.resolve("ca.pem"));
        Map<String, String> config = Map.of("sslCaFile", file.toString());
        File result = DsSslSupport.requiredFile(config, "sslCaFile", "CA certificate");
        Assertions.assertTrue(result.isFile());
        Assertions.assertEquals(file.toFile(), result);
    }

    // trustAllManagers -------------------------------------------------------
    @Test
    public void trustAllManagers_acceptsAnyChainWithoutValidation() {
        TrustManager[] managers = DsSslSupport.trustAllManagers();
        Assertions.assertEquals(1, managers.length);
        Assertions.assertTrue(managers[0] instanceof X509TrustManager);

        X509TrustManager trustManager = (X509TrustManager) managers[0];
        X509Certificate[] chain = new X509Certificate[0];
        // trust-all performs no validation: these must not throw.
        Assertions.assertDoesNotThrow(() -> trustManager.checkServerTrusted(chain, "RSA"));
        Assertions.assertDoesNotThrow(() -> trustManager.checkClientTrusted(chain, "RSA"));
        Assertions.assertEquals(0, trustManager.getAcceptedIssuers().length);
    }

    // verifyKeyPair ----------------------------------------------------------
    // The matching / mismatching branches need a real X509 certificate, which cannot be
    // built in-memory without an extra crypto provider; only the unsupported-algorithm
    // branch is covered here as it never touches the certificate argument.
    @Test
    public void verifyKeyPair_throwsOnUnsupportedAlgorithm() {
        PrivateKey unknownKey = new PrivateKey() {
            @Override
            public String getAlgorithm() { return "UNKNOWN"; }

            @Override
            public String getFormat() { return null; }

            @Override
            public byte[] getEncoded() { return null; }
        };
        GeneralSecurityException ex = Assertions.assertThrows(GeneralSecurityException.class,
            () -> DsSslSupport.verifyKeyPair(unknownKey, null));
        Assertions.assertTrue(ex.getMessage().contains("UNKNOWN"));
    }
}
