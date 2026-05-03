package com.clougence.clouddm.console.web.model.fo.cluster;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/16 15:38
 */
@Getter
@Setter
public class UpdateWorkerDescFO {

    @Min(value = 1, message = "worker id must be large than 0.")
    private long   workerId;

    @NotBlank
    @Size(min = 1, max = 64, message = "worker desc must between 1~64 character.")
    private String desc;
}
