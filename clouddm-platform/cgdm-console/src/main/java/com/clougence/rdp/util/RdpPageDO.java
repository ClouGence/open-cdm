package com.clougence.rdp.util;

import lombok.Getter;
import lombok.Setter;

/**
* @author Ekko
* @date 2022/11/24 11:49
* page limit
*/
@Getter
@Setter
public class RdpPageDO {

    private int pageNum;

    private int pageSize;

    public RdpPageDO(int pageNum, int pageSize){
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public RdpPageDO(){
    }
}
