package com.clougence.rdp.controller.model.dto.saas;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemDTO {

    private String   username;

    private String   email;

    private PriceDTO price;

    private String   description;

    private Long     quantity;

    private Long     amount;

    private boolean  proration;

    public ItemDTO(){
    }

    public ItemDTO(String username, String email, PriceDTO price, String description, Long quantity, Long amount, boolean proration){
        this.username = username;
        this.email = email;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.amount = amount;
        this.proration = proration;
    }
}
