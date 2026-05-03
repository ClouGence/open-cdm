package com.clougence.rdp.controller.model.fo.user;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/2/1 15:17
 */
@Getter
@Setter
public class GetUserSpecifiedConfsFO {

    @NotNull
    List<String> configNames;
}
