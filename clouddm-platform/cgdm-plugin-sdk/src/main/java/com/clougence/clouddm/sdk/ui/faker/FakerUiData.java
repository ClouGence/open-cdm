package com.clougence.clouddm.sdk.ui.faker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

/**
 * @author olddream
 */
@Getter
@Setter
public class FakerUiData {

    @JsonInclude()
    private List<Map<String, Object>> columns = new ArrayList<>();

}
