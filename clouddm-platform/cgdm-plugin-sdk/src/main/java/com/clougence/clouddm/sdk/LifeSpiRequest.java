package com.clougence.clouddm.sdk;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LifeSpiRequest {

    private String body;

    public LifeSpiRequest(){
    }

    public LifeSpiRequest(String body){
        this.body = body;
    }
}
