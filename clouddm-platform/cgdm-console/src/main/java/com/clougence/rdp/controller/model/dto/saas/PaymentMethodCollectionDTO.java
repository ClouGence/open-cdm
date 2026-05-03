package com.clougence.rdp.controller.model.dto.saas;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentMethodCollectionDTO {

    private List<CardDTO> paymentMethods;

    public PaymentMethodCollectionDTO(){
    }

    public PaymentMethodCollectionDTO(List<CardDTO> paymentMethods){
        this.paymentMethods = paymentMethods;
    }
}
