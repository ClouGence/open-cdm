package com.clougence.clouddm.comm.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Send back async response will use this class
 *
 * @author wanshao create time is 2021/1/11
 **/
@Getter
@Setter
public class RSocketRespDTO<T> extends RSocketRequestWrapperDTO {

    private String code;

    private String msg;

    private T      data;

    public RSocketRespDTO(){
    }

    public RSocketRespDTO(String code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public RSocketRespDTO(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public RSocketRespDTO(RSocketRespCode resultEnum){
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
    }

    public RSocketRespDTO(RSocketRespCode resultEnum, T data){
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
        this.data = data;
    }

}
