package com.clougence.rdp.service.openapi.model;

import java.util.Map;

import com.clougence.rdp.controller.model.fo.UpsertDsKvConfigFO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiUpsertDsKvConfigFO {

    long                dataSourceId;

    Map<String, String> updateConfigs;

    Map<String, String> needCreateConfigs;

    public UpsertDsKvConfigFO genUpsertDsKvConfigFo() {
        UpsertDsKvConfigFO fo = new UpsertDsKvConfigFO();
        fo.setDataSourceId(this.dataSourceId);
        fo.setUpdateConfigs(this.updateConfigs);
        fo.setNeedCreateConfigs(this.needCreateConfigs);
        return fo;
    }
}
