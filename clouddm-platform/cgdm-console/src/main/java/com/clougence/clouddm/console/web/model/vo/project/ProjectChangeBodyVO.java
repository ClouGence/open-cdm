package com.clougence.clouddm.console.web.model.vo.project;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectChangeBodyVO {

    private String                        changeBody;
    private List<ProjectChangeBodyItemVO> itemList;
}
