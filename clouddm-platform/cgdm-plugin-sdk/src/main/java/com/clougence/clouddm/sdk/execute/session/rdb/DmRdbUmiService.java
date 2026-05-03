package com.clougence.clouddm.sdk.execute.session.rdb;

import java.sql.SQLException;
import java.util.Map;

import com.clougence.schema.umi.service.RdbUmiServiceDm;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;

public interface DmRdbUmiService extends RdbUmiServiceDm {

    Value fetchSelectObject(Map<UmiTypes, Object> levelsParam, String leafName) throws SQLException;
}
