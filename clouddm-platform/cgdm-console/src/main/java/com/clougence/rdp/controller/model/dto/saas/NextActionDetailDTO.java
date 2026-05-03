package com.clougence.rdp.controller.model.dto.saas;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NextActionDetailDTO {

    private String              type;

    private Map<String, Object> useStripeSdk;

    public NextActionDetailDTO(String type, Map<String, Object> useStripeSdk){
        this.type = type;
        this.useStripeSdk = useStripeSdk;
    }

    public NextActionDetailDTO(){
    }
}
