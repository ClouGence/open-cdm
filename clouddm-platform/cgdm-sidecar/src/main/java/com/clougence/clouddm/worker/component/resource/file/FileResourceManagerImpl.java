package com.clougence.clouddm.worker.component.resource.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.console.configs.ConfigRService;
import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityFileType;
import com.clougence.utils.io.FileUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileResourceManagerImpl {

    @Resource
    private ConfigRService           statusRService;

    private final AtomicBoolean      inited   = new AtomicBoolean(false);

    private final Map<String, Cache> cacheMap = new ConcurrentHashMap<>();

    private final File               fileDir  = new File(GlobalConfUtils.getPluginDir("files"));

    public String getFilePath(DataSourceConfig config, String fileName, SecurityFileType securityFileType) throws IOException {
        String key = config.getInstanceId() + "-" + fileName;
        Long configVersion = config.getConfigVersion();
        if (!cacheMap.containsKey(key) || !cacheMap.get(key).getVersion().equals(configVersion)) {
            synchronized (this) {
                if (!cacheMap.containsKey(key) || !cacheMap.get(key).getVersion().equals(configVersion)) {
                    String instanceId = config.getInstanceId();
                    // instance
                    File instanceDir = new File(fileDir, instanceId);

                    File file = new File(instanceDir, fileName);
                    if (file.exists()) {
                        FileUtils.deleteQuietly(file);
                    }
                    byte[] fileBytes = statusRService.fetchDsFile(instanceId, ResourceType.DATASOURCE, securityFileType);
                    try (FileOutputStream fileOutputStream = FileUtils.openOutputStream(file)) {
                        fileOutputStream.write(fileBytes);
                    }
                    cacheMap.put(key, new Cache(configVersion, file.getAbsolutePath()));
                    log.info("cache file location: {}", file.getAbsolutePath());
                }
            }
        }
        return cacheMap.get(key).getFilePath();
    }

    @PostConstruct
    public void init() throws IOException {
        if (inited.compareAndSet(false, true) && fileDir.exists()) {
            FileUtils.deleteDirectory(fileDir);
            log.info("Deleted file directory {}", fileDir.getAbsolutePath());
        }
    }

    @Getter
    @Setter
    private static class Cache {

        private Long   version;
        private String filePath;

        public Cache(Long version, String filePath){
            this.version = version;
            this.filePath = filePath;
        }
    }
}
