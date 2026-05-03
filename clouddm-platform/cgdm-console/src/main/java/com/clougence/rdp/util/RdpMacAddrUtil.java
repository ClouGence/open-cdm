package com.clougence.rdp.util;

import java.net.InetAddress;
import java.net.NetworkInterface;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2022/11/7
 **/
@Slf4j
public class RdpMacAddrUtil {

    private static final String DELIMITER = ":";

    public RdpMacAddrUtil(){
    }

    @SneakyThrows
    public static String getLocalMacAddress(String localIp) {
        try {
            InetAddress localIP = InetAddress.getByName(localIp);
            NetworkInterface ni = NetworkInterface.getByInetAddress(localIP);
            if (ni == null) {
                log.warn("Local ip " + localIp + ", not have valid network interface....");
                return null;
            }
            byte[] hardwareAddress = ni.getHardwareAddress();
            if (hardwareAddress == null) {
                log.warn("Local ip " + localIp + ", not have mac address...");
                return null;
            }
            String[] hexadecimal = new String[hardwareAddress.length];

            for (int i = 0; i < hardwareAddress.length; ++i) {
                hexadecimal[i] = String.format("%02X", hardwareAddress[i]);
            }

            return String.join(":", hexadecimal);
        } catch (Throwable var6) {
            throw var6;
        }
    }

    @SneakyThrows
    public static boolean haveMac(String localIp) {
        try {
            InetAddress localIP = InetAddress.getByName(localIp);
            NetworkInterface ni = NetworkInterface.getByInetAddress(localIP);
            byte[] hardwareAddress = ni.getHardwareAddress();
            return hardwareAddress != null;
        } catch (Throwable var6) {
            throw var6;
        }
    }
}
