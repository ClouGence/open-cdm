package com.clougence.clouddm.sec.rules.execute.sensitive;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2023/05/21 13:27
 **/
@Setter
@Getter
public class SenColValue {

    private final ColMetaData colMeta;
    private String            colValue;
    private String            algorithm;

    public SenColValue(ColMetaData colMeta, String colValue, String algorithm){
        this.colMeta = colMeta;
        this.colValue = colValue;
        this.algorithm = algorithm;
    }
}
