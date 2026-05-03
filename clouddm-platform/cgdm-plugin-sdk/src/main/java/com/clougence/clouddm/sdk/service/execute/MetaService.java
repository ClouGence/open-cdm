package com.clougence.clouddm.sdk.service.execute;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.service.Service;
import com.clougence.schema.umi.struts.UmiTypes;

public interface MetaService extends Service {

    List<MetaCol> fetchTableColumns(String uid, long dsId, Map<UmiTypes, Object> levelsParam, String tableName, int tableId);
}
