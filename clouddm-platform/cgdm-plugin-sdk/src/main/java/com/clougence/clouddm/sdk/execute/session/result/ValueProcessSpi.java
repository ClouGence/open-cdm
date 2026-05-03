package com.clougence.clouddm.sdk.execute.session.result;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.service.Service;

public interface ValueProcessSpi extends Service {

    void begin(QueryRequest query, Map<String, ColMetaData> rowMeta, Map<String, Object> flash);

    List<String> processRow(QueryRequest query, Map<String, ColMetaData> meta, List<String> rowData, Map<String, Object> flash);

    void finish(QueryRequest query, Map<String, Object> flash);
}
