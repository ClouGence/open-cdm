package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePhoneFO {

    @NotBlank(message = "{notblank.oldphone}")
    private String oldPhone;

    @NotBlank(message = "{notblank.newphone}")
    private String newPhone;
}
