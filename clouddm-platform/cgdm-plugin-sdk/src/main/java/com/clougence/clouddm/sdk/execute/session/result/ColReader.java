package com.clougence.clouddm.sdk.execute.session.result;

import java.sql.SQLException;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;

public interface ColReader {

    ValueFetcher readColumn(String col, ColMetaData colMetaData) throws SQLException;
}
