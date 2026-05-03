package com.clougence.clouddm.console.web.model.vo.faker;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakerFieldVO {

    private String             field;
    private String             type;
    private boolean            require;
    private boolean            readOnly;
    private boolean            hide;
    private Object             defaultVal;
    private List<Object>       options;
    private String             titleI18N;
    private String             descI18N;
    private List<FakerFieldVO> children;

}
