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
package com.clougence.clouddm.ds.valkey.execute;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.clougence.schema.umi.struts.Value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Covers the Valkey schema-probe fallback used when managed hosts disable CONFIG:
 * the {@code buildSchemaValues} pure helper and the {@code probeDatabaseCount}/{@code refineDatabaseCount}
 * jump-then-refine algorithm, including the {@code PROBE_LIMIT} ceiling and out-of-range edges.
 *
 * <p>SELECT behaviour is simulated with JDK dynamic proxies (the same pattern used by
 * {@code AbstractMetadataProvider}), so no real Redis or Mockito is required.
 */
public final class ValkeyMetaProviderDmProbeTest {

    // ---------- buildSchemaValues (pure) ----------

    @Test
    void buildSchemaValues_zeroIsEmpty() {
        assertTrue(ValkeyMetaProviderUtils.buildSchemaValues(0).isEmpty());
    }

    @Test
    void buildSchemaValues_generatesIndexedDbNames() {
        List<Value> values = ValkeyMetaProviderUtils.buildSchemaValues(3);
        assertEquals(3, values.size());
        assertEquals("0", values.get(0).asValue());
        assertEquals("1", values.get(1).asValue());
        assertEquals("2", values.get(2).asValue());
    }

    // ---------- probeDatabaseCount (algorithm boundaries) ----------
    // threshold = number of usable dbs; SELECT db fails for db >= threshold (treated as out of range).

    @Test
    void probe_selectZeroFails_returnsZero() {
        assertEquals(0, probeDatabaseCount(newProvider(), selectProbeConnection(0)));
    }

    @Test
    void probe_singleDatabase() {
        assertEquals(1, probeDatabaseCount(newProvider(), selectProbeConnection(1)));
    }

    @Test
    void probe_landsOnJumpPoint_refinesAll() {
        // candidate sequence 0,3,7,...; db 3 fails -> refine(0,3) walks db 1,2 -> 3.
        assertEquals(3, probeDatabaseCount(newProvider(), selectProbeConnection(3)));
    }

    @Test
    void probe_refineStartsAtFailure() {
        // db 3 ok, db 7 fails -> refine(3,7) fails immediately at db 4 -> 4.
        assertEquals(4, probeDatabaseCount(newProvider(), selectProbeConnection(4)));
    }

    @Test
    void probe_multipleJumpsBeforeRefine() {
        // db 0,3,7 ok, db 12 fails -> refine(7,12) fails at db 8 -> 8.
        assertEquals(8, probeDatabaseCount(newProvider(), selectProbeConnection(8)));
    }

    @Test
    void probe_allSucceedUpToLimit_capsAtLastGood() {
        // Every SELECT succeeds, so the probe stops at PROBE_LIMIT. The largest successful candidate
        // below 256 is 250 (sequence 0,3,7,12,18,25,...,250), so the reported count is 251.
        assertEquals(251, probeDatabaseCount(newProvider(), selectProbeConnection(1000)));
    }

    @Test
    void probe_boundaryJustBelowLimit_refinesToEdge() {
        // db 250 fails -> refine(228,250) walks 229..249 successfully -> 250.
        assertEquals(250, probeDatabaseCount(newProvider(), selectProbeConnection(250)));
    }

    // ---------- refineDatabaseCount (direct) ----------

    @Test
    void refine_walksToEnd() {
        // threshold 9: db 6,7 ok (count 7,8), loop ends before failPoint 8 -> 8.
        assertEquals(8, refineDatabaseCount(newProvider(), selectProbeConnection(9), 5, 8));
    }

    @Test
    void refine_failsMidway() {
        // threshold 7: db 6 ok (count 7), db 7 fails -> break -> 7.
        assertEquals(7, refineDatabaseCount(newProvider(), selectProbeConnection(7), 5, 8));
    }

    @Test
    void refine_failsImmediately() {
        // threshold 6: db 6 fails -> break -> lastGood+1 = 6.
        assertEquals(6, refineDatabaseCount(newProvider(), selectProbeConnection(6), 5, 8));
    }

    // ---------- helpers ----------

    private static ValkeyMetaProviderDm newProvider() {
        // probeDatabaseCount/refineDatabaseCount only use the Connection passed to them, never the
        // provider's own connection, so a null construction-time connection is safe.
        return new ValkeyMetaProviderDm(null);
    }

    private static int probeDatabaseCount(ValkeyMetaProviderDm provider, Connection conn) {
        return provider.probeDatabaseCount(conn);
    }

    private static int refineDatabaseCount(ValkeyMetaProviderDm provider, Connection conn, int lastGood, int failPoint) {
        return provider.refineDatabaseCount(conn, lastGood, failPoint);
    }

    /**
     * A Connection whose {@code prepareStatement("select ?")} yields a stateful PreparedStatement
     * that records the db set via {@code setString(1, db)} and fails {@code execute()} when db >= threshold,
     * simulating an out-of-range SELECT on a host with CONFIG disabled.
     */
    @SuppressWarnings("unchecked")
    private static Connection selectProbeConnection(int threshold) {
        ClassLoader loader = ValkeyMetaProviderDmProbeTest.class.getClassLoader();
        InvocationHandler connectionHandler = (proxy, method, args) -> {
            if ("prepareStatement".equals(method.getName()) && args != null && args.length > 0 && "select ?".equals(args[0])) {
                int[] currentDb = { -1 };
                return Proxy.newProxyInstance(loader, new Class<?>[]{PreparedStatement.class}, (p, m, a) -> {
                    String name = m.getName();
                    if ("setString".equals(name) && a != null && a.length >= 2) {
                        currentDb[0] = Integer.parseInt((String) a[1]);
                        return null;
                    }
                    if ("execute".equals(name) && (a == null || a.length == 0)) {
                        if (currentDb[0] >= threshold) {
                            throw new SQLException("ERR invalid DB index: " + currentDb[0]);
                        }
                        return Boolean.TRUE;
                    }
                    return defaultReturn(m.getReturnType());
                });
            }
            return defaultReturn(method.getReturnType());
        };
        return (Connection) Proxy.newProxyInstance(loader, new Class<?>[]{Connection.class}, connectionHandler);
    }

    private static Object defaultReturn(Class<?> returnType) {
        if (returnType == void.class) return null;
        if (returnType == boolean.class) return false;
        if (returnType == int.class) return 0;
        if (returnType == long.class) return 0L;
        if (returnType == short.class) return (short) 0;
        if (returnType == byte.class) return (byte) 0;
        if (returnType == char.class) return '\0';
        if (returnType == double.class) return 0.0d;
        if (returnType == float.class) return 0.0f;
        return null;
    }
}
