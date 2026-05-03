package com.clougence.clouddm.worker.provider;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.sidecar.session.drivers.DriversRService;
import com.clougence.clouddm.api.sidecar.session.drivers.DsDriverRes;
import com.clougence.clouddm.api.sidecar.session.drivers.DsDriverVer;
import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.drivers.DriverVersion;
import com.clougence.drivers.def.ResDef;
import com.clougence.utils.io.FilenameUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RSocketApiClass
public class DriversRServiceProvider implements DriversRService {

    @Override
    public DsDriverVer refreshDriverVersion(RSocketSendDTO sendDTO, String familyName, String version) {
        DriverVersion driver = PluginManager.driverLoader().findDriver(familyName, version);
        if (driver != null) {
            PluginManager.driverLoader().refreshDriverVersion(driver);
            DsDriverVer ver = new DsDriverVer();
            ver.setVersion(driver.getVersion());
            ver.setDsFactory(driver.getDsFactory());

            List<ResDef> resList = driver.getResources();
            if (resList != null) {
                ver.setResources(resList.stream().map(r -> {
                    return DsDriverRes.of(r.getResourceType(), r.getCoordinate(), r.isPrepared());
                }).collect(Collectors.toList()));
            }
            return ver;
        }

        return null;
    }

    @Override
    public void deleteDriverResource(RSocketSendDTO sendDTO, String familyName, String version) {
        DriverVersion driver = PluginManager.driverLoader().findDriver(familyName, version);
        if (driver != null) {
            driver.deleteFiles();
        }
    }

    @Override
    public void transferDriverResource(RSocketSendDTO sendDTO, String familyName, String version, String fileName, long offset, byte[] content) {
        DriverVersion driver = PluginManager.driverLoader().findDriver(familyName, version);
        File driverDir = driver != null ? driver.getAbsoluteDir() : null;
        if (driver == null || driverDir == null) {
            throw new IllegalArgumentException("driver not found: " + familyName + " / " + version);
        }

        String relativePath = FilenameUtils.normalize(fileName);
        if (relativePath == null) {
            throw new IllegalArgumentException("driver resource fileName is blank.");
        }

        File file = new File(driverDir, fileName);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IllegalStateException("cannot create directory: " + parent.getAbsolutePath());
        }

        try (RandomAccessFile accessFile = new RandomAccessFile(file, "rw")) {
            accessFile.seek(offset);
            accessFile.write(content);
            accessFile.getFD().sync();
        } catch (IOException e) {
            throw new IllegalStateException("write resource failed: " + file.getAbsolutePath(), e);
        }
    }
}
