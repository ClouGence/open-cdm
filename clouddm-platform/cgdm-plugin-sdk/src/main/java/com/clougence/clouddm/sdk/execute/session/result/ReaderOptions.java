package com.clougence.clouddm.sdk.execute.session.result;

import com.clougence.utils.format.WellKnowFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReaderOptions {

    private long           columnBytesLimit;
    private long           elementBytesLimit;
    private String         tempPath;
    private WellKnowFormat dataFormat;
    private WellKnowFormat timeFormat;
    private WellKnowFormat dataTimeFormat;
    private WellKnowFormat timeWithZoneFormat;
    private WellKnowFormat dataTimeWithZoneFormat;
}
