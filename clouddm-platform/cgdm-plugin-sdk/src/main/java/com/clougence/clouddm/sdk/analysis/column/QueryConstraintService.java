package com.clougence.clouddm.sdk.analysis.column;

import java.util.List;

import com.clougence.clouddm.sdk.service.Service;

public interface QueryConstraintService extends Service {

    List<QueryConstraintsDTO> fetchQueryConstraints(String pri, long dsId, List<String> path);
}
