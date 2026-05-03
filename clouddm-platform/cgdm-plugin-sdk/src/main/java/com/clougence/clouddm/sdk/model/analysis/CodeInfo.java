package com.clougence.clouddm.sdk.model.analysis;

import lombok.Builder;
import lombok.Getter;

/** @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
@Builder
@Getter
public class CodeInfo {

    private int    baseLine;
    private int    baseColumn;
    private String query;
}
