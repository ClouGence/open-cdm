package com.clougence.rdp.controller.model.fo;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/13 09:42
 */
@Getter
@Setter
public class RequestUserDsAuthFO {

    private String                            description;

    @NotBlank(message = "authed uid can not be empty.")
    private String                            authedUid;

    private List<ModifyAuthWithDsForAppendFO> dsOpsAuthList;

    private List<ModifyAuthWithDsForUpdateFO> updates;

    private Long                              startTime;

    private Long                              endTime;
}
