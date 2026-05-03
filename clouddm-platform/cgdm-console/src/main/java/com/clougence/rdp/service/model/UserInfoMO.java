package com.clougence.rdp.service.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserInfoMO {

    private String username;

    public UserInfoMO(){
    }

    public UserInfoMO(String username){
        this.username = username;
    }
}
