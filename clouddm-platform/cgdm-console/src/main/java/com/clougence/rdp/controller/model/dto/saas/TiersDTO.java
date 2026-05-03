package com.clougence.rdp.controller.model.dto.saas;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TiersDTO {

    private Long upTo;

    private Long unitAmount;

    public TiersDTO(){
    }

    public TiersDTO(Long upTo, Long unitAmount){
        this.upTo = upTo;
        this.unitAmount = unitAmount;
    }
}
