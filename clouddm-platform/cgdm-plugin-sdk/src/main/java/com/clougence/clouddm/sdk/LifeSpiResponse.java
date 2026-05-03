package com.clougence.clouddm.sdk;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LifeSpiResponse {

    public LifeSpiResponse(String body){
        this.body = body;
    }

    public LifeSpiResponse(){

    }

    private String body;
}
