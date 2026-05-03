package com.clougence.clouddm.console.web.model.fo.datasource;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ConnectType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityType;
import com.clougence.rdp.controller.model.fo.InitDsKvBaseConfigFO;
import com.clougence.rdp.dal.enumeration.DeployEnvType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectDsFO {

    private Long                       bindClusterId;

    private DataSourceType             dataSourceType;

    private DeployEnvType              deployEnvType;

    private String                     privateHost;

    private String                     publicHost;

    private String                     defaultHost;

    private String                     region;

    private String                     instanceDesc;

    private SecurityType               securityType;

    private ConnectType                connectType;

    private Long                       envId;

    private String                     driver;

    private String                     dsPropsJson;

    private List<InitDsKvBaseConfigFO> dsKvConfigs;
}
