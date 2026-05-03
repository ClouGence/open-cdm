package com.clougence.clouddm.sdk.model.analysis;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.schema.umi.struts.UmiTypes;

import lombok.Builder;
import lombok.Getter;

/** @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
@Builder
@Getter
public class ContextInfo {

    private String                puid;
    private String                cuid;
    private long                  dsId;
    private Map<UmiTypes, Object> levelsParam;

    // query meta info
    private boolean               deepParser;
    private DataSourceConfig      dataSourceConfig;
}
