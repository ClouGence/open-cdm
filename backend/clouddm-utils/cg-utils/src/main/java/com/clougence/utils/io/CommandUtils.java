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
package com.clougence.utils.io;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.SystemUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: baili
 * @Date: 2023/04/06/20:28
 * @Description: execute command for Linux or Windows
 */
@Slf4j
public class CommandUtils {

    private static List<String> getLinuxPrefix() {
        List<String> res = new ArrayList<>();
        res.add("/bin/sh");
        res.add("-c");
        return res;
    }

    private static List<String> getWinPrefix() {
        List<String> res = new ArrayList<>();
        res.add("cmd");
        res.add("/c");
        return res;
    }

    /**
     * for windows
     * you must install git, and not change default URL. if you change the default URL, you need to modify the program url
     * @author baili
     */
    private static List<String> getGitPrefix() throws IOException {

        if (isWin()) {
            String gitPath = "C:\\Program Files\\Git\\bin\\bash.exe";
            File file = new File(gitPath);
            if (file.exists()) {
                List<String> res = new ArrayList<>();
                res.add(gitPath);
                res.add("-c");
                return res;
            } else {
                throw new IOException("not found git bash program, please check if the default path is correct : " + gitPath);
            }
        } else {
            return getLinuxPrefix();
        }
    }

    private static String exec(String... args) throws Exception {

        if (args.length == 0) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        List<String> cmd = null;

        if (isWin()) {
            cmd = getWinPrefix();
        } else {
            cmd = getLinuxPrefix();
        }

        cmd.addAll(Arrays.asList(args));

        ProcessBuilder pb = new ProcessBuilder(cmd);
        Process process = pb.start();

        try (InputStream stream = process.getInputStream(); BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }

        try (InputStream stream = process.getErrorStream(); BufferedReader errorReader = new BufferedReader(new InputStreamReader(stream))) {
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                sb.append(errorLine).append("\n");
            }
        }

        if (process.waitFor() != 0) {
            throw new IllegalArgumentException(sb.toString());
        }

        return sb.toString();
    }

    private static String execForGit(String... args) throws Exception {

        if (args.length == 0) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        List<String> cmd = getGitPrefix();

        cmd.addAll(Arrays.asList(args));

        ProcessBuilder pb = new ProcessBuilder(cmd);
        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }

        try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                sb.append(errorLine).append("\n");
            }
        }

        return sb.toString();
    }

    private static boolean isWin() { return SystemUtils.isWindows(); }

    private static String transformWinPathForGit(String path) {
        return "/" + path.replaceAll(":", "").replaceAll("\\\\", "/");
    }

    public static String tailLog(String logFilePath, String endRow) {
        if (isWin()) {
            logFilePath = transformWinPathForGit(logFilePath);
        }
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isBlank(endRow)) {
            sb.append("tail -n 200 " + logFilePath);
        } else {
            if (Long.parseLong(endRow) < 200L) {
                endRow = "200";
            }
            sb.append("sed -n '" + String.valueOf(Long.parseLong(endRow) - 200 > 0 ? Long.parseLong(endRow) - 200 : 1) + "," + endRow + "p' " + logFilePath);
        }
        try {
            return execForGit(sb.toString());
        } catch (Exception e) {
            String errMsg = "Execute tail log path = " + logFilePath + "endRow = " + endRow + "is error. Root cause is " + ExceptionUtils.getRootCauseMessage(e);
            log.error(errMsg, e);
        }
        return null;
    }

    public static String endRow(String logFilePath) {
        if (isWin()) {
            logFilePath = transformWinPathForGit(logFilePath);
        }
        try {
            String res = execForGit("wc -l " + logFilePath).trim().split(" ")[0];
            return String.valueOf(Long.parseLong(res) == 0L ? 0L : Long.parseLong(res) + 1);
        } catch (Exception e) {
            String errMsg = "Execute wc -l log path = " + logFilePath + "is error. Root cause is " + ExceptionUtils.getRootCauseMessage(e);
            log.error(errMsg, e);
        }
        return null;
    }

    public static boolean checkInstallClientFile(String installClientPath) {
        if (isWin()) {
            throw new IllegalArgumentException("unsupport windows deploy mode");
        } else {
            String commandStr = "if [ -e " + installClientPath + " ]; then \n echo \"" + installClientPath + " exists\" \n else echo \"" + installClientPath
                                + " not exists\" \n exit 1 \n fi";
            try {
                exec(commandStr);
            } catch (Exception e) {
                String errMsg = "Execute check script error.Root cause is " + ExceptionUtils.getRootCauseMessage(e);
                log.error(errMsg, e);
                return false;
            }
        }
        return true;
    }

    public static String grepOperationLog(String logFilePath, String grepContent) {
        if (isWin()) {
            throw new IllegalArgumentException("unsupport windows deploy mode");
        } else {
            String commandStr = "cat " + logFilePath + " | grep " + grepContent;
            try {
                return exec(commandStr);
            } catch (Exception e) {
                String errMsg = "Execute check script error.Root cause is " + ExceptionUtils.getRootCauseMessage(e);
                log.error(errMsg, e);
                return null;
            }
        }
    }

}
