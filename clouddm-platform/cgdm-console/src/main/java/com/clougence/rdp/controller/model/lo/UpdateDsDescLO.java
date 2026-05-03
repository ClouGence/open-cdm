package com.clougence.rdp.controller.model.lo;

import lombok.Data;

/**
 * @author chunlin create time is 2024/11/5
 */
@Data
public class UpdateDsDescLO {

    private Long   dataSourceId;
    private String oldInstanceDesc;
    private String newInstanceDesc;
}
