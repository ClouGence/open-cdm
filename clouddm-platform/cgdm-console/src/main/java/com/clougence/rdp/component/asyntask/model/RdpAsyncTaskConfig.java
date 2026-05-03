package com.clougence.rdp.component.asyntask.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.clougence.rdp.component.asyntask.RdpAsyncTaskType;
import com.clougence.rdp.dal.enumeration.RdpAsyncTaskProcessType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2019-12-30 15:53
 * @since 1.1.3
 */
@Getter
@Setter
public final class RdpAsyncTaskConfig implements Serializable {

    private static final long        serialVersionUID = -7166362823815428900L;

    private String                   title;
    private String                   description;

    private String                   bizId;
    private RdpAsyncTaskType         bizType;

    private Class<?>                 handlerType;
    private RdpAsyncTaskProcessType  processType;
    private String                   configData;

    private int                      delayActivate;
    private Boolean                  parallel;
    private boolean                  showInDock;
    private boolean                  fastFail;
    private List<RdpAsyncTaskConfig> subTask;

    public RdpAsyncTaskConfig(String titleKey, String descKey, RdpAsyncTaskType bizType, Class<?> handlerType){
        this.title = titleKey;
        this.description = descKey;
        this.bizType = bizType;
        this.handlerType = handlerType;
        this.showInDock = true;
        this.bizId = UUID.randomUUID().toString().replace("-", "");
        this.processType = RdpAsyncTaskProcessType.PROGRESS;
    }

    public RdpAsyncTaskConfig(){
    }
}
