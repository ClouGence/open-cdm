package com.clougence.drivers;

import java.util.List;

public interface DriverFamily {

    String getFamilyName();

    List<String> getVersions();

    DriverVersion findVersion(String version);

    DriverVersion addVersion(String version);

    boolean hasVersion(String version);
}
