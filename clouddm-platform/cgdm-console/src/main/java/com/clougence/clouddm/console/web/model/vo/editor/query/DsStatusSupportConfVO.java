package com.clougence.clouddm.console.web.model.vo.editor.query;

import com.clougence.clouddm.sdk.execute.session.rdb.RdbSupportLevel;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2020-01-20 12:28
 * @since 1.1.3
 */
@Getter
@Setter
public class DsStatusSupportConfVO {

    private String          defaultValue;
    private RdbSupportLevel conf;
    private String          hint;
}
