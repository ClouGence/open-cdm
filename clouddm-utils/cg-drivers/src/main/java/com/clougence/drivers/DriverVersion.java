package com.clougence.drivers;

import java.io.File;
import java.util.List;

import com.clougence.drivers.def.ResDef;

public interface DriverVersion {

    String getFamilyName();

    String getVersion();

    File getAbsoluteDir();

    File getRelativeDir();

    long getTimestamp();

    String getDsFactory();

    boolean isPrepared();

    void setPrepared(boolean prepared);

    List<ResDef> getResources();

    void addResource(ResDef resource);

    List<DriverFile> getFiles();

    void deleteFiles();
}
