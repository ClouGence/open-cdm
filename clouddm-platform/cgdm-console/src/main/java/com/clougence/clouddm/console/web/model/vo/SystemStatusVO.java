package com.clougence.clouddm.console.web.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.console.web.constants.SystemStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemStatusVO {

    private SystemStatus status = SystemStatus.Initial;
    private String       initReason;
    private String       dbError;
    private List<String> upgradeScripts = new ArrayList<>();
}
