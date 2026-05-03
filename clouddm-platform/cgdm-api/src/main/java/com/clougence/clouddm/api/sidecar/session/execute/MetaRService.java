package com.clougence.clouddm.api.sidecar.session.execute;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.sdk.execute.meta.DsElement;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;

/**
 * @author mode 2021/1/14 14:10
 */
@RSocketApiClass
public interface MetaRService {

    // Commons

    TestConnectResultDO testConnect(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam);

    String getVersion(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam);

    List<DsElement> listLevels(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam);

    DsElement detailLevel(RSocketSendDTO sendDTO, DataSourceConfig dsConfig, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam);

    List<DsElement> listLeaf(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String pattern);

    Value detailLeaf(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName);

    Value fetchSelectObject(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, String leafName);

    // RDB

    Map<String, List<RdbColumn>> loadColumns(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, List<String> leafNames);

    String loadTableEditor(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, String table);

    List<String> requestObjectScript(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName);
}
