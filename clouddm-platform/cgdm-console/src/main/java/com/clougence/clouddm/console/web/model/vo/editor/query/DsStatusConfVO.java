package com.clougence.clouddm.console.web.model.vo.editor.query;

import com.clougence.clouddm.console.web.dal.enumeration.DataSourceStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2020-01-20 12:28
 * @since 1.1.3
 */
@Getter
@Setter
public class DsStatusConfVO {

    private DataSourceStatus      dsStatus;
    private String                dsStatusMessage;

    private DsStatusSupportConfVO catalog;
    private DsStatusSupportConfVO schema;
    private DsStatusSupportConfVO isolation;
    private DsStatusSupportConfVO autoCommit;
    private DsStatusSupportConfVO readOnly;
    private DsStatusSupportConfVO cancel;
    private DsStatusSupportConfVO explain;
    private DsStatusSupportConfVO format;
}
