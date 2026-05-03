package com.clougence.clouddm.console.web.model.fo.asyntask;

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/7 15:01
 */
@Getter
@Setter
public class ActionAsyncTaskFO {

    @Min(value = 1, message = "taskId id must large than 0.")
    private long taskId;
}
