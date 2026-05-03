package com.clougence.clouddm.console.web.model.vo.cluster;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020/9/17 10:17
 */
@Getter
@Setter
public class WorkerDeployConfigVO {

    private String userAkLabel      = "clouddm.auth.ak";
    private String userAkValue;

    private String userSkLabel      = "clouddm.auth.sk";
    private String userSkValue;

    private String wsnLabel         = "clouddm.worker.wsn";
    private String wsnValue;

    private String consoleHostLabel = "clouddm.console.host";
    private String consoleHostValue;

    private String consolePortLabel = "clouddm.console.port";
    private String consolePortValue;
}
