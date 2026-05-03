package com.clougence.clouddm.console.web.model.vo.export;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2020/4/13
 **/
@Getter
@Setter
public class DmExportVO {

    private String         uid;
    private String         trackId;
    private DmExportStatus status;
    private String         message;
    private int            percent;

    private long           current;
    private long           total;
}
