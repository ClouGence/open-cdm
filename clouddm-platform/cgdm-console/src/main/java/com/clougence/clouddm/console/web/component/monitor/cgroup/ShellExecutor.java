package com.clougence.clouddm.console.web.component.monitor.cgroup;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShellExecutor {

    public static List<String> exec(String shell, int timeoutSeconds) throws Exception {
        String[] cmd = new String[] { "/bin/sh", "-c", shell };
        Process p = Runtime.getRuntime().exec(cmd);
        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            Future<List<String>> future = executor.submit(() -> {
                ArrayList<String> sa = new ArrayList<>();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.defaultCharset()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sa.add(line);
                    }
                }
                p.waitFor();
                return sa;
            });

            return future.get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            String errMsg = "exec shell fail,msg: " + com.clougence.utils.ExceptionUtils.getRootCauseMessage(e);
            log.error(errMsg);
            throw e;
        } finally {
            p.destroy();
            executor.shutdownNow();
        }
    }

    public static Map<String, String> exec(String shell, int timeoutSeconds, String separator) throws Exception {
        Map<String, String> map = new HashMap<>();
        List<String> lines = ShellExecutor.exec(shell, timeoutSeconds);
        for (String line : lines) {
            String[] parts = line.split(separator);
            if (parts.length == 2) {
                map.put(parts[0], parts[1].trim());
            }
        }
        return map;
    }
}
