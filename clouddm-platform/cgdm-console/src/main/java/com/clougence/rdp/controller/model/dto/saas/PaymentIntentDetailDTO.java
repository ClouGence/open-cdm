package com.clougence.rdp.controller.model.dto.saas;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentIntentDetailDTO {

    private String              id;

    private String              clientSecret;

    private String              status;

    private NextActionDetailDTO nextAction;

    public PaymentIntentDetailDTO(){
    }

    public PaymentIntentDetailDTO(String id, String clientSecret, String status, NextActionDetailDTO nextAction){
        this.id = id;
        this.clientSecret = clientSecret;
        this.status = status;
        this.nextAction = nextAction;
    }
}
