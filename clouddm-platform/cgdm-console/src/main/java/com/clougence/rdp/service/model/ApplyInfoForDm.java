package com.clougence.rdp.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class ApplyInfoForDm {

    @JsonIgnore
    private Long   applyId;

    private String consoleIp;

    private String consoleMacAddress;

    private String hardwareUuid;

}
