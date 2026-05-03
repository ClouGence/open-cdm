package com.clougence.rdp.controller.model.fo.ticket;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplyAuth {

    private long         resId;

    private List<String> resPaths;

    private List<String> authLabels;

    private String       startTime;

    private String       endTime;

    private String       resInstId;

    private String       resDesc;

}
