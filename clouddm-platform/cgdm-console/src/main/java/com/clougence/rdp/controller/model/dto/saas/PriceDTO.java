package com.clougence.rdp.controller.model.dto.saas;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriceDTO {

    private String              priceId;

    private boolean             isActive;

    private String              currency;

    private List<TiersDTO>      tiersDTO;

    private TierMode            tierMode;

    private Long                unitAmount;

    private String              lookupKey;

    private String              productId;

    private Map<String, String> metaData;

    public PriceDTO(){
    }

    public PriceDTO(String priceId, boolean isActive, String currency, List<TiersDTO> tiersDTO, TierMode tierMode, Long unitAmount, String lookupKey, String productId,
                    Map<String, String> metaData){
        this.priceId = priceId;
        this.isActive = isActive;
        this.currency = currency;
        this.tiersDTO = tiersDTO;
        this.tierMode = tierMode;
        this.unitAmount = unitAmount;
        this.lookupKey = lookupKey;
        this.productId = productId;
        this.metaData = metaData;
    }
}
