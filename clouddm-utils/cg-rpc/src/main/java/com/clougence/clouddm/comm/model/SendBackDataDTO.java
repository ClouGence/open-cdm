package com.clougence.clouddm.comm.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wanshao create time is 2021/1/9
 **/
@Getter
@Setter
public class SendBackDataDTO {

    /**
     * Compressed ResponseData string and encoded with base64
     */
    private String               compressedRespStr;
    /**
     * When compress is disabled, will use this object to store send back data
     */
    private RSocketRespDTO       rSocketRespDTO;

    private RSocketDirectionType rSocketDirectionType;
}
