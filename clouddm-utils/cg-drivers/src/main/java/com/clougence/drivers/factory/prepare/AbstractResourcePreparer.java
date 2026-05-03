package com.clougence.drivers.factory.prepare;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.clougence.drivers.DriverPrepareProgress;
import com.clougence.drivers.DriverVersion;
import com.clougence.drivers.def.FileDef;
import com.clougence.drivers.def.ResDef;
import com.clougence.drivers.factory.ResourcePreparer;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

public abstract class AbstractResourcePreparer implements ResourcePreparer {

    protected final File       localDir;
    protected final Properties config;

    protected AbstractResourcePreparer(File localDir, Properties config){
        this.localDir = localDir;
        this.config = config != null ? config : new Properties();
    }

    @Override
    public void refresh(DriverVersion driverVersion, ResDef resDef, ClassLoader classLoader, DriverPrepareProgress progress) throws IOException {
        if (CollectionUtils.isEmpty(resDef.getFileDefList())) {
            resDef.setPrepared(true);
            return;
        }

        boolean allPrepared = true;
        for (FileDef fileDef : resDef.getFileDefList()) {
            String absolutePath = StringUtils.trimToNull(fileDef.getAbsolutePath());
            fileDef.setPrepared(absolutePath != null && new File(absolutePath).exists());
            allPrepared = allPrepared && fileDef.isPrepared();
        }
        resDef.setPrepared(allPrepared);
    }
}
