package com.clougence.rdp.controller.model.dto.saas;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PendingUpdateDetailDTO {

    private Long expiresAt;

    public PendingUpdateDetailDTO(Long expiresAt){
        this.expiresAt = expiresAt;
    }

    public PendingUpdateDetailDTO(){
    }
}
