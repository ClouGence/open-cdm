package com.clougence.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/7
 **/
@Slf4j
public class HostUtil {

    /** Need ignore other useless network's ip */
    private static final List<String> blackPrefixs = Arrays.asList("docker0", "br-", "veth");
    private static String             hostIpCache  = null;

    public static String getHostIp() {
        if (StringUtils.isNotBlank(hostIpCache)) {
            return hostIpCache;
        } else {
            try {
                String bindAddress = SystemUtils.getSystemProperty("cg.ipv4bind");
                if (StringUtils.isNotBlank(bindAddress)) {
                    List<String> inetAddress = localAddrForIPv4();
                    if (inetAddress.contains(bindAddress)) {
                        hostIpCache = bindAddress;
                    }
                }

            } catch (SocketException e) {
                log.error(e.getMessage(), e);
            }

            if (StringUtils.isBlank(hostIpCache)) {
                hostIpCache = getHostIpInner();
            }

            return hostIpCache;
        }
    }

    /** 获取本机地址 */
    public static List<String> localAddrForIPv4() throws SocketException {
        List<String> ipList = new ArrayList<>();
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();
            Enumeration<InetAddress> ipAddrEnum = ni.getInetAddresses();
            while (ipAddrEnum.hasMoreElements()) {
                InetAddress addr = ipAddrEnum.nextElement();
                if (addr.isLoopbackAddress()) {
                    continue;
                }
                String ip = addr.getHostAddress();
                if (ip.contains(":")) {
                    //skip the IPv6 addr
                    continue;
                }
                if (!ipList.contains(ip)) {
                    ipList.add(ip);
                }
            }
        }
        return ipList;
    }

    private static String getHostIpInner() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            String lastMatchIP = null;
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                boolean skip = false;
                for (String prefix : blackPrefixs) {
                    if (networkInterface.getName().startsWith(prefix)) {
                        log.info("Network " + networkInterface.getName() + " will be ignored because it start with prefix " + prefix);
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
                    if (!lastMatchIP.contains(":")) {
                        return lastMatchIP;// return IPv4 addr
                    }
                }
            }
            if (StringUtils.isBlank(lastMatchIP)) {
                return InetAddress.getLocalHost().getHostAddress();
            } else {
                return lastMatchIP;
            }
        } catch (Exception e) {
            String errMsg = "get host ip failed,msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(errMsg, e);
            throw new RuntimeException(errMsg, e);
        }
    }
}
