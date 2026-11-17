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
package com.clougence.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class StringUtilsTest {

    @Test
    public void trimStart_01() {
        assert StringUtils.trimStart(null, ' ') == null;
        assert StringUtils.trimStart("", ' ').equals("");
        assert StringUtils.trimStart("     ", ' ').equals("");
        assert StringUtils.trimStart("abc", ' ').equals("abc");
        assert StringUtils.trimStart("    abc    ", ' ').equals("abc    ");
        assert StringUtils.trimStart("1111abc1111", '1').equals("abc1111");
    }

    @Test
    public void trimEnd_01() {
        assert StringUtils.trimEnd(null, ' ') == null;
        assert StringUtils.trimEnd("", ' ').equals("");
        assert StringUtils.trimEnd("     ", ' ').equals("");
        assert StringUtils.trimEnd("abc", ' ').equals("abc");
        assert StringUtils.trimEnd("    abc    ", ' ').equals("    abc");
        assert StringUtils.trimEnd("1111abc1111", '1').equals("1111abc");
    }

    @Test
    public void trim_01() {
        assert StringUtils.trim(null, ' ') == null;
        assert StringUtils.trim("", ' ').equals("");
        assert StringUtils.trim("     ", ' ').equals("");
        assert StringUtils.trim("abc", ' ').equals("abc");
        assert StringUtils.trim("1111abc1111", '1').equals("abc");
        assert !StringUtils.trim("2222abc2222", '3').equals("abc");
        assert StringUtils.trim("1111abc1111", '1').equals("abc");
    }

    @Test
    public void join_01() {
        assert StringUtils.join((List<?>) null, ' ') == null;
        assert StringUtils.join(Arrays.asList("a", "b"), ' ').equals("a b");
    }

    @Test
    public void trimBlankStart_01() {
        assert StringUtils.trimBlankStart(null) == null;
        assert StringUtils.trimBlankStart("").equals("");
        assert StringUtils.trimBlankStart("     ").equals("");
        assert StringUtils.trimBlankStart("abc").equals("abc");
        assert StringUtils.trimBlankStart("    abc    ").equals("abc    ");
        assert StringUtils.trimBlankStart("\n\n\nabc\n\n\n").equals("abc\n\n\n");
    }

    @Test
    public void trimBlankEnd_01() {
        assert StringUtils.trimBlankEnd(null) == null;
        assert StringUtils.trimBlankEnd("").equals("");
        assert StringUtils.trimBlankEnd("     ").equals("");
        assert StringUtils.trimBlankEnd("abc").equals("abc");
        assert StringUtils.trimBlankEnd("    abc    ").equals("    abc");
        assert StringUtils.trimBlankEnd("\n\n\nabc\n\n\n").equals("\n\n\nabc");
    }

    @Test
    public void trimBlank_01() {
        assert StringUtils.trimBlank(null) == null;
        assert StringUtils.trimBlank("").equals("");
        assert StringUtils.trimBlank("  \n   ").equals("");
        assert StringUtils.trimBlank("abc").equals("abc");
        assert StringUtils.trimBlank(" \n abc \n ").equals("abc");
        assert StringUtils.trimBlank("1 \n abc \n ").equals("1 \n abc");
    }

    @Test
    public void startsWithIgnoreCaseIgnoringLeadingWhitespace_01() {
        assert !StringUtils.startsWithIgnoreCaseIgnoringLeadingWhitespace(null, "EXPLAIN ");
        assert StringUtils.startsWithIgnoreCaseIgnoringLeadingWhitespace(null, null);
        assert !StringUtils.startsWithIgnoreCaseIgnoringLeadingWhitespace("", "EXPLAIN ");
        assert StringUtils.startsWithIgnoreCaseIgnoringLeadingWhitespace("EXPLAIN SELECT 1", "EXPLAIN ");
        assert StringUtils.startsWithIgnoreCaseIgnoringLeadingWhitespace("  explain SELECT 1", "EXPLAIN ");
        assert StringUtils.startsWithIgnoreCaseIgnoringLeadingWhitespace("\n\tExPlAiN SELECT 1", "EXPLAIN ");
        assert !StringUtils.startsWithIgnoreCaseIgnoringLeadingWhitespace("SELECT 1", "EXPLAIN ");
    }

    @Test
    public void hostAndPortFormatsIpv4DomainsAndIpv6() {
        Map<String, String> endpoints = Map
            .ofEntries(Map.entry("localhost", "localhost:5432"), Map.entry("localhost:15432", "localhost:15432"), Map.entry("127.0.0.1", "127.0.0.1:5432"), Map
                .entry("127.0.0.1:15432", "127.0.0.1:15432"), Map.entry("::1", "[::1]:5432"), Map.entry("[::1]", "[::1]:5432"), Map.entry("[::1]:15432", "[::1]:15432"), Map
                    .entry("2001:db8::10", "[2001:db8::10]:5432"), Map
                        .entry("[2001:db8::10]:65535", "[2001:db8::10]:65535"), Map.entry("::ffff:192.0.2.1", "[::ffff:192.0.2.1]:5432"));
        for (Map.Entry<String, String> endpoint : endpoints.entrySet()) {
            assertEquals(endpoint.getValue(), HostAndPort.parse(endpoint.getKey(), 5432).toString());
        }
    }

    @Test
    public void hostAndPortKeepsBareIpv6AsAWholeHost() {
        HostAndPort endpoint = HostAndPort.parse("2001:db8::5432");
        assertEquals("2001:db8::5432", endpoint.getHost());
        assertEquals(-1, endpoint.getPort());
        assertEquals("[2001:db8::5432]", endpoint.toString());
        assertEquals("[::1]:3306", HostAndPort.fromParts("::1", 3306).toString());
        assertEquals("[::1]:3306", HostAndPort.fromParts("[::1]", 3306).toString());
        assertThrows(IllegalArgumentException.class, () -> HostAndPort.fromParts("localhost:5432", 3306));
    }

    @Test
    public void hostAndPortRejectsMalformedEndpoints() {
        for (String address : List
            .of("", "[::1", "[::1]extra", "[localhost]:5432", "[::1]:", "[::1]:abc", "[::1]:0", "[::1]:65536", "host:-1", ":5432", "2001:db8::xyz", "[::1]:5432:extra")) {
            assertThrows(IllegalArgumentException.class, () -> HostAndPort.parse(address), address);
        }
    }

}
