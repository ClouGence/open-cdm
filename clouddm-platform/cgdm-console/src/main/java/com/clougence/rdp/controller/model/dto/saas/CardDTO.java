package com.clougence.rdp.controller.model.dto.saas;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CardDTO {

    private String id;

    private String name;

    private String last4;

    private String funding;

    private Long   expireMonth;

    private Long   expireYear;

    private String cvcCheck;

    private String brand;

    private String addressCity;

    private String addressCountry;

    private String addressState;

    private String addressZip;

    private String addressLine1;

    private String addressLine2;

    private String subscriptionId;

    public CardDTO(){
    }

    public CardDTO(String id, String name, String last4, String funding, Long expireMonth, Long expireYear, String cvcCheck, String brand, String addressCity,
                   String addressCountry, String addressState, String addressZip, String addressLine1, String addressLine2, String subscriptionId){
        this.id = id;
        this.name = name;
        this.last4 = last4;
        this.funding = funding;
        this.expireMonth = expireMonth;
        this.expireYear = expireYear;
        this.cvcCheck = cvcCheck;
        this.brand = brand;
        this.addressCity = addressCity;
        this.addressCountry = addressCountry;
        this.addressState = addressState;
        this.addressZip = addressZip;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.subscriptionId = subscriptionId;
    }
}
