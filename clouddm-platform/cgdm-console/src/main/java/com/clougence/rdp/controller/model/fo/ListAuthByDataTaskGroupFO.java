package com.clougence.rdp.controller.model.fo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/13 10:18
 */
@Getter
@Setter
public class ListAuthByDataTaskGroupFO {

    private String     targetUid;

    private List<Long> dataJobs;
}
