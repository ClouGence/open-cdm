package com.clougence.rdp.controller.model.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020/12/30 14:26
 */
@Getter
@Setter
public class DsSecurityDetailVO {

    private List<DsSecurityOption> securityOptions = new ArrayList<>();
}
