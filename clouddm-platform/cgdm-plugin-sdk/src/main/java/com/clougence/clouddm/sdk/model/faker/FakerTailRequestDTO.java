package com.clougence.clouddm.sdk.model.faker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author olddream
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FakerTailRequestDTO {

    private int startLine;
}
