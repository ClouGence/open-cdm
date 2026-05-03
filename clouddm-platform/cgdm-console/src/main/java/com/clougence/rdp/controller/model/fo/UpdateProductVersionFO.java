package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2023/11/23 17:17:10
 */
@Getter
@Setter
public class UpdateProductVersionFO {

    @Min(value = 1, message = "id must be large than 0.")
    private long   id;

    @NotBlank(message = "productVersion can not be blank.")
    private String productVersion;
}
