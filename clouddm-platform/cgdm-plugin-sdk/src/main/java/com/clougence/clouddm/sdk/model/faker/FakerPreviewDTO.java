package com.clougence.clouddm.sdk.model.faker;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author olddream
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FakerPreviewDTO {

    private Map<String, List<FakerLineDTO>> preview;
}
