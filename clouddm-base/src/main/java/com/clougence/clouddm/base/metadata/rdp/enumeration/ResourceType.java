package com.clougence.clouddm.base.metadata.rdp.enumeration;

/**
 * @author bucketli 2020/2/29 11:49
 */
public enum ResourceType {
    /**
     * dm,cc
     */
    CATALOG,
    PURE_URL,
    DATASOURCE,
    CLUSTER,
    WORKER,
    CONSOLE_JOB,
    DS_ENV,
    TICKET,
    PACKAGE,

    /**
     * dm
     */
    QUERY,
    DATA_EXPORT,

    /**
     * cc
     */
    DATA_JOB,
    DATA_TASK,
    FSM,
    ACCOUNT,
    ROLE,

    /**
     * ?
     */
    CONSOLE_USER,
    CONSOLE;
}
