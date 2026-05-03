package com.clougence.clouddm.console.web.model.vo.faker;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakerPanelVO {

    private String             titleI18N;
    private String             descI18N;
    private List<FakerFieldVO> children;
}
