package com.clougence.clouddm.console.web.component.asyntask;

import java.io.Serializable;
import java.util.List;

import com.clougence.clouddm.console.web.dal.enumeration.DmAsyncTaskProcessType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2019-12-30 15:53
 * @since 1.1.3
 */
@Getter
@Setter
public class AsyncTaskConfig implements Serializable {

    private static final long      serialVersionUID = -7166362823815428900L;

    private String                 title;
    private String                 description;

    private String                 bizId;
    private String                 bizType;

    private Class<?>               handlerType;
    private DmAsyncTaskProcessType processType;
    private String                 configData;

    private int                    delayActivate;
    private Boolean                parallel;
    private boolean                showInDock;
    private boolean                fastFail;
    private List<AsyncTaskConfig>  subTask;
}
