package com.clougence.clouddm.console.web.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DmConsoleHostUtil {

    /**
     * Need ignore other useless network's ip
     */
    private static final List<String> blackPrefixs = Arrays.asList("docker0", "br-", "veth", "bridge", "virbr", "utun");

    private static String             hostIpCache  = null;

    public static String getHostIp(boolean refreshCache) {
        if (StringUtils.isNotBlank(hostIpCache) && !refreshCache) {
            return hostIpCache;
        } else {
            hostIpCache = getHostIpOrHostNameInner(false);
            log.info("[DmConsoleHostUtil] Fetched host ip:" + hostIpCache);
            return hostIpCache;
        }
    }

    private static String getHostIpOrHostNameInner(boolean forceHostName) {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            String lastMatchIP = null;
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                boolean skip = false;
                for (String prefix : blackPrefixs) {
                    if (networkInterface.getName().startsWith(prefix)) {
                        log.info("[DmConsoleHostUtil] Network " + networkInterface.getName() + " will be ignored because it start with prefix " + prefix);
                        skip = true;
                        break;
                    }
                }

                if (skip) {
                    continue;
                }

                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr.isLoopbackAddress()) {
                        continue;
                    }

                    lastMatchIP = addr.getHostAddress();
                    if (!lastMatchIP.contains(":") && DmConsoleMacAddressUtil.haveMac(lastMatchIP)) {
                        return forceHostName ? addr.getCanonicalHostName() : lastMatchIP; // return IPv4 addr
                    }
                }
            }

            if (StringUtils.isBlank(lastMatchIP)) {
                InetAddress noMacAddr = InetAddress.getLocalHost();
                if (forceHostName) {
                    String noMacHostName = noMacAddr.getCanonicalHostName();
                    log.warn("[DmConsoleHostUtil] Can't find valid local host name, use the host that not have mac address. Host name is " + noMacHostName);
                    return noMacHostName;
                } else {
                    String noMacHostIp = noMacAddr.getHostAddress();
                    log.warn("[DmConsoleHostUtil] Can't find valid local host ip, use the host that not have mac address. Host ip is " + noMacHostIp);
                    return noMacHostIp;
                }
            } else {
                return lastMatchIP;
            }
        } catch (Exception e) {
            String msg = "[DmConsoleHostUtil] Get host ip or host name failed, msg: " + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}
