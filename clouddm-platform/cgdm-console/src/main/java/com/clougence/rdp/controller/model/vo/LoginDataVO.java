package com.clougence.rdp.controller.model.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wanshao create time is 2020/9/12
 **/
@Getter
@Setter
public class LoginDataVO {

    private long    lastTryLoginTimeMs = 0;

    private int     loginFailCount     = 0;

    private boolean locked             = false;

    private String  loginFailMsg;
}
