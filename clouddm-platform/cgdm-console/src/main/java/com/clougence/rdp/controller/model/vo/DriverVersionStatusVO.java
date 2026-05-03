package com.clougence.rdp.controller.model.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverVersionStatusVO {

    private String       driverFamily;
    private String       driverVersion;
    private boolean      available;
    private List<String> workerWsn;
}
