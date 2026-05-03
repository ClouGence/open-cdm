package com.clougence.clouddm.console.web.model.vo.datasource;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConnectDsResultVO {

    private boolean success;
    private String  message;
    private String  version;
}
