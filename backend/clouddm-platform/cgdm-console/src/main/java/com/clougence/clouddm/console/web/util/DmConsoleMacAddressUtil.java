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
package com.clougence.clouddm.console.web.util;

import java.net.InetAddress;
import java.net.NetworkInterface;

import com.clougence.utils.ExceptionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DmConsoleMacAddressUtil {

    private static final String DELIMITER = ":";

    public DmConsoleMacAddressUtil(){
    }

    public static String getLocalMacAddress(String localIp) {
        try {
            InetAddress localIP = InetAddress.getByName(localIp);
            NetworkInterface ni = NetworkInterface.getByInetAddress(localIP);
            if (ni == null) {
                log.warn("[DmConsoleMacAddressUtil] Local ip " + localIp + ", not have valid network interface....");
                return null;
            }
            byte[] hardwareAddress = ni.getHardwareAddress();
            if (hardwareAddress == null) {
                log.warn("[DmConsoleMacAddressUtil] Local ip " + localIp + ", not have mac address...");
                return null;
            }
            String[] hexadecimal = new String[hardwareAddress.length];

            for (int i = 0; i < hardwareAddress.length; ++i) {
                hexadecimal[i] = String.format("%02X", hardwareAddress[i]);
            }

            return String.join(":", hexadecimal);
        } catch (Throwable var6) {
            String msg = "[DmConsoleMacAddressUtil] Get Local MacAddress failed, msg: " + ExceptionUtils.getRootCauseMessage(var6);
            log.error(msg, var6);
            throw new RuntimeException(msg, var6);
        }
    }

    public static boolean haveMac(String localIp) {
        try {
            InetAddress localIP = InetAddress.getByName(localIp);
            NetworkInterface ni = NetworkInterface.getByInetAddress(localIP);
            byte[] hardwareAddress = ni.getHardwareAddress();
            return hardwareAddress != null;
        } catch (Throwable var6) {
            String msg = "[DmConsoleMacAddressUtil] Have mac failed, msg: " + ExceptionUtils.getRootCauseMessage(var6);
            log.error(msg, var6);
            throw new RuntimeException(msg, var6);
        }
    }
}
