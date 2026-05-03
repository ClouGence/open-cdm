package com.clougence.clouddm.sdk.analysis.column;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryConstraintsDTO {

    private Long             dsId;
    private List<String>     levels;
    private List<Constraint> constraints;
    private String           path;

    @Setter
    @Getter
    public static class Constraint {

        private String column;
        private String config;
    }
}
