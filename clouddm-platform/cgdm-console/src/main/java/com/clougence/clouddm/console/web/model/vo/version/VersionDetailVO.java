package com.clougence.clouddm.console.web.model.vo.version;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VersionDetailVO {

    private String       lastVersion;

    @JsonInclude()
    private List<String> detail;
}
