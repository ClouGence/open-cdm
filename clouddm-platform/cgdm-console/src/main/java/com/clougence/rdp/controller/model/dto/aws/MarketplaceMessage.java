package com.clougence.rdp.controller.model.dto.aws;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketplaceMessage {

    private String action;

    @JsonProperty("customer-identifier")
    private String customerIdentifier;

    @JsonProperty("product-code")
    private String productCode;
}
