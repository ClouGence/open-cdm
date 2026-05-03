package com.clougence.clouddm.comm.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RSocketParam {

    private String data;
    private String serializer;

    public RSocketParam(){
    }

    public RSocketParam(String serializer, String data){
        this.serializer = serializer;
        this.data = data;
    }
}
