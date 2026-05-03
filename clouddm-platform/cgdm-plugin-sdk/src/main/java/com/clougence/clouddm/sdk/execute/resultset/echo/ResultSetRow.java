package com.clougence.clouddm.sdk.execute.resultset.echo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2022/3/23 18:42:25
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultSetRow {

    private String               rowId;
    private List<ResultSetValue> data;
}
