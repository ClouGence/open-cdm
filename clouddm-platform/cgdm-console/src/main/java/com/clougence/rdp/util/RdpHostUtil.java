package com.clougence.rdp.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2019/12/16 8:46 下午
 **/
@Slf4j
public class RdpHostUtil {

    /**
     * Need ignore other useless network's ip
     */
    private static final List<String> blackPrefixs    = Arrays.asList("docker0", "br-", "veth", "bridge", "virbr", "utun");

    private static String             hostIpCache     = null;

    private static String             hostNameCache   = null;

    public static final String        LOCAL_IP        = "local_ip";

    public static final String        LOCAL_HOST_NAME = "local_host_name";

    public static String getHostIpOrHostName(String type) {
        try {
            return LOCAL_IP.equals(type) ? getHostIp() : getHostName();
        } catch (Exception e) {
            String msg = "[RdpHostUtil] Get host ip or host name failed,msg:" + com.clougence.utils.ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    public static String getHostIp() {
        if (com.clougence.utils.StringUtils.isNotBlank(hostIpCache)) {
            return hostIpCache;
        } else {
            hostIpCache = getHostIpOrHostNameInner(false);
            log.info("[RdpHostUtil] fetched host ip:" + hostIpCache);
            return hostIpCache;
        }
    }

    private static String getHostName() {
        if (com.clougence.utils.StringUtils.isNotBlank(hostNameCache)) {
            return hostNameCache;
        } else {
            hostNameCache = getHostIpOrHostNameInner(true);
            log.info("[RdpHostUtil] Fetched host name:" + hostNameCache);
            return hostNameCache;
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
                        log.info("[RdpHostUtil] Network " + networkInterface.getName() + " will be ignored because it start with prefix " + prefix);
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
                    if (!lastMatchIP.contains(":") && RdpMacAddrUtil.haveMac(lastMatchIP)) {
                        return forceHostName ? addr.getCanonicalHostName() : lastMatchIP; // return IPv4 addr
                    }
                }
            }

            if (StringUtils.isBlank(lastMatchIP)) {
                InetAddress noMacAddr = InetAddress.getLocalHost();
                if (forceHostName) {
                    String noMacHostName = noMacAddr.getCanonicalHostName();
                    log.warn("[RdpHostUtil] Can't find valid local host name, use the host that not have mac address. Host name is " + noMacHostName);
                    return noMacHostName;
                } else {
                    String noMacHostIp = noMacAddr.getHostAddress();
                    log.warn("[RdpHostUtil] Can't find valid local host ip, use the host that not have mac address. Host ip is " + noMacHostIp);
                    return noMacHostIp;
                }
            } else {
                return lastMatchIP;
            }
        } catch (Exception e) {
            String msg = "[RdpHostUtil] Get host ip or host name failed,msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}
