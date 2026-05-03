package com.clougence.rdp.component.mcp.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.clougence.utils.CollectionUtils;
import com.clougence.utils.HexadecimalUtils;
import com.clougence.utils.StringUtils;

import okhttp3.Headers;

public class RagUtils {

    private RagUtils(){
    }

    public static Headers buildHeaders(Consumer<Headers.Builder> other) {
        Headers.Builder b = new Headers.Builder();
        b.add("Accept", "application/json,text/event-stream");
        b.add("Content-Type", "application/json; charset=utf-8");
        b.add("Cache-Control", "no-cache");
        other.accept(b);
        return b.build();
    }

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (org.apache.commons.lang3.StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0].trim();
        }

        ip = request.getHeader("X-Real-IP");
        if (org.apache.commons.lang3.StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        return request.getRemoteAddr();
    }

    public static <T extends Collection<?>> T ensureNotEmpty(T collection, String name) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be null or empty");
        }

        return collection;
    }

    public static <T> T getOrDefault(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static <T> List<T> getOrDefault(List<T> list, List<T> defaultList) {
        return isNullOrEmpty(list) ? defaultList : list;
    }

    public static <T> T ensureNotNull(T object, String name) {
        return ensureNotNull(object, "%s cannot be null", name);
    }

    public static <T> T ensureNotNull(T object, String format, Object... args) {
        if (object == null) {
            throw new IllegalArgumentException(String.format(format, args));
        }
        return object;
    }

    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNullOrEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static int ensureGreaterThanZero(Integer i, String name) {
        if (i == null || i <= 0) {
            throw new IllegalArgumentException(String.format("%s must be greater than zero, but is: %s", name, i));
        }

        return i;
    }

    public static String ensureNotBlank(String string, String name) {
        if (string == null || string.trim().isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be null or blank");
        }

        return string;
    }

    public static <T> T[] ensureNotEmpty(T[] array, String name) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(name + " cannot be null or empty");
        }

        return array;
    }

    public static String generateUUIDFrom(String input) {
        byte[] hashBytes = getSha256Instance().digest(input.getBytes(UTF_8));
        String hexFormat = HexadecimalUtils.bytes2hex(hashBytes);
        return UUID.nameUUIDFromBytes(hexFormat.getBytes(UTF_8)).toString();
    }

    private static MessageDigest getSha256Instance() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <T> List<T> copy(List<T> list) {
        if (list == null) {
            return new ArrayList<>();
        }

        return unmodifiableList(list);
    }

    public static <K, V> Map<K, V> copy(Map<K, V> map) {
        if (map == null) {
            return new HashMap<>();
        }

        return unmodifiableMap(map);
    }

    public static String quoted(Object object) {
        if (object == null) {
            return "null";
        }
        return "\"" + object + "\"";
    }

    public static String firstNotEmpty(String[] value, String[] path) {
        String p = firstOrEmpty(value);
        if (StringUtils.isNotBlank(p)) {
            return p;
        }
        return firstOrEmpty(path);
    }

    public static String firstOrEmpty(String[] arr) {
        return (arr != null && arr.length > 0) ? StringUtils.defaultString(arr[0]) : "";
    }

    public static void requireNonNull(Object v, String key) {
        if (v == null) {
            throw new IllegalStateException("[Rag Config] missing: " + key);
        }
    }

    public static String genListStr(List<String> list) {
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }

        list = list.stream().sorted().collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        for (String mac : list) {
            sb.append(mac).append(",");
        }
        String macStr = sb.toString();
        return macStr.substring(0, macStr.length() - 1);
    }

    public static List<String> genListByStr(String listStr) {
        if (StringUtils.isBlank(listStr)) {
            return new ArrayList<>();
        }
        List<String> result = Arrays.asList(listStr.split(","));
        return result.stream().sorted().collect(Collectors.toList());
    }
}
