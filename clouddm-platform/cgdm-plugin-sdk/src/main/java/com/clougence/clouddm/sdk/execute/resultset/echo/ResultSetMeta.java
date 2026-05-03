package com.clougence.clouddm.sdk.execute.resultset.echo;

import java.util.List;

import com.clougence.clouddm.sdk.execute.resultset.file.DmFileType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultSetMeta extends Result {

    private int          fetchCount;
    private String       receiveCost;

    private List<String> columnList;
    private List<String> columnType;
    private String       cacheFilePath;
    private String       cacheFileUri;
    private DmFileType   cacheFileFormat;
    private ReceiveMode  receiveMode;
}
