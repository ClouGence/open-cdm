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
package com.clougence.clouddm.console.web.service.system;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.DmBuildInfo;
import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.model.vo.version.VersionDetailVO;
import com.clougence.clouddm.console.web.model.vo.version.VersionPromptVO;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.FileUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

@Service
@Slf4j
public class VersionPromptServiceImpl implements VersionPromptService, UnifiedPostConstruct {

    private static final String FILE_SUFFIX = ".html";
    private String              cacheDir;
    private File                ignoreFile;

    private File cacheReleaseNodeFile(String version) {
        return new File(cacheDir, version + FILE_SUFFIX);
    }

    @Resource
    private DmConsoleConfig dmConfig;
    private String          checkRemoteURL;
    private String          releaseRemoteURL;
    private List<String>    versionCache;

    @Override
    public void init() {
        this.checkRemoteURL = "https://" + this.dmConfig.getUpgradeServer() + "/apis/clouddm/version/difference/%s";
        this.releaseRemoteURL = "https://" + this.dmConfig.getUpgradeServer() + "/apis/clouddm/version/releasenode";
        this.versionCache = new ArrayList<>();

        this.cacheDir = GlobalConfUtils.getAppHome() + "/data/releaseinfo";
        this.ignoreFile = new File(GlobalConfUtils.getAppHome() + "/" + "ignoreVersion.txt");
        new File(this.cacheDir).mkdirs();
    }

    @Override
    public void stop() {

    }

    @Override
    public VersionPromptVO check() {
        VersionPromptVO promptVO = new VersionPromptVO();
        promptVO.setNewVersion(false);
        promptVO.setPrompt(false);
        return promptVO;
    }

    @Override
    public VersionDetailVO detail() {
        remoteGetDifference(false);

        Map<String, String> detailMap = new HashMap<>();
        for (String ver : this.versionCache) {
            String body = loadLocalRelease(ver);
            if (body != null) {
                detailMap.put(ver, body);
            }
        }

        List<String> needRemote = this.versionCache.stream().filter(s -> !detailMap.containsKey(s)).collect(Collectors.toList());
        detailMap.putAll(loadRemoteRelease(needRemote));

        return fmtDetail(this.versionCache, detailMap);
    }

    private VersionDetailVO fmtDetail(List<String> order, Map<String, String> bodyMap) {
        if (CollectionUtils.isEmpty(order) || CollectionUtils.isEmpty(bodyMap)) {
            return null;
        }

        List<String> details = new ArrayList<>();
        for (String version : order) {
            details.add(bodyMap.getOrDefault(version, ""));
        }

        VersionDetailVO vo = new VersionDetailVO();
        vo.setDetail(details);
        vo.setLastVersion(order.get(0));
        return vo;
    }

    private void remoteGetDifference(boolean refresh) {
        if (this.versionCache.isEmpty() || refresh) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(String.format(this.checkRemoteURL, currentVersion())).build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    ResListData data = JsonUtils.toObj(response.body().string(), ResListData.class);
                    this.versionCache.clear();
                    this.versionCache.addAll(data.getData());
                }
            } catch (Exception e) {
                String msg = "Get the latest version field.msg:" + ExceptionUtils.getRootCauseMessage(e);
                log.warn(msg);
            }
        }
    }

    private String loadLocalRelease(String version) {
        File verFile = cacheReleaseNodeFile(version);

        if (!verFile.exists()) {
            return null;
        } else {
            try {
                return FileUtils.readFileToString(verFile);
            } catch (IOException e) {
                log.error("load Release " + verFile + " File Error, " + e.getMessage(), e);
                return null;
            }
        }
    }

    private Map<String, String> loadRemoteRelease(List<String> loadVersions) {
        Map<String, String> detailMap = new HashMap<>();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON, JsonUtils.toJson(loadVersions));

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(releaseRemoteURL).post(requestBody).build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                ResMapData data = JsonUtils.toObj(response.body().string(), ResMapData.class);
                data.data.forEach((ver, bodyLines) -> {
                    detailMap.put(ver, StringUtils.join(bodyLines.toArray(), ""));
                });
            } else {
                log.warn("Remote request execution failed.Error:" + response.code() + " - " + response.message());
            }
        } catch (Exception e) {
            log.warn("Get the latest version failed.msg:" + ExceptionUtils.getRootCauseMessage(e));
        }

        storeCache(detailMap);
        return detailMap;
    }

    private void storeCache(Map<String, String> releaseMap) {
        releaseMap.forEach((version, body) -> {
            try {
                FileUtils.write(cacheReleaseNodeFile(version), body);
            } catch (IOException e) {
                log.warn("storeCache releaseNode " + version + " failed. msg:" + ExceptionUtils.getRootCauseMessage(e));
            }
        });
    }

    private boolean isIgnoreVersion() {
        if (CollectionUtils.isEmpty(this.versionCache) || !ignoreFile.exists()) {
            return true;
        }

        try {
            String missVersion = FileUtils.readFileToString(ignoreFile);
            if (StringUtils.isBlank(missVersion)) {
                return true;
            } else {
                return !this.versionCache.get(0).equalsIgnoreCase(missVersion.trim());
            }
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public void ignore() {
        if (CollectionUtils.isEmpty(this.versionCache)) {
            return;
        }

        try {
            FileUtils.write(ignoreFile, this.versionCache.get(0));
        } catch (Exception e) {
            log.warn("User directory cache failed.msg:" + ExceptionUtils.getRootCauseMessage(e));
        }
    }

    private static String currentVersion() {
        String userVersion = DmBuildInfo.BUILD_VERSION;//xxx.xxx.xxx(2023-12-07)
        int index = userVersion.indexOf("(");
        if (index >= 0) {
            userVersion = userVersion.substring(0, index);
        }
        return userVersion;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResMapData implements Serializable {

        private String                    code;
        private String                    msg;
        private Map<String, List<String>> data;

        public ResMapData(){
        }
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResListData implements Serializable {

        private String       code;
        private String       msg;
        private List<String> data;

        public ResListData(){
        }
    }
}
