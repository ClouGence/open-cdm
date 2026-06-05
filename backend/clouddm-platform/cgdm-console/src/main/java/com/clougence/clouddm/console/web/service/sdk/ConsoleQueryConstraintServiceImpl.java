/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.console.web.service.sdk;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.model.execution.DmExecQueryConstraintsDO;
import com.clougence.clouddm.platform.dal.model.execution.QueryConstraintType;
import com.clougence.clouddm.sdk.analysis.column.QueryConstraintService;
import com.clougence.clouddm.sdk.analysis.column.QueryConstraintsDTO;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConsoleQueryConstraintServiceImpl implements QueryConstraintService {
    @Resource
    private ExecutionDal executionDal;

    @Override
    public List<QueryConstraintsDTO> fetchQueryConstraints(String primaryUid, long dsId, List<String> path) {
        List<DmExecQueryConstraintsDO> dmConstraintsDOS = executionDal.queryConstraintsMapper().selectAllByUid(primaryUid, dsId, path);
        List<QueryConstraintsDTO> result = new ArrayList<>();
        for (DmExecQueryConstraintsDO dmConstraintsDO : dmConstraintsDOS) {
            QueryConstraintsDTO dto = new QueryConstraintsDTO();
            dto.setDsId(dmConstraintsDO.getDsId());
            dto.setPath(dmConstraintsDO.getPath());
            dto.setConstraints(new ArrayList<>());
            for (DmExecQueryConstraintsDO.Constraint constraint : dmConstraintsDO.getConstraints()) {
                if (constraint.getType() == QueryConstraintType.SELECT_COLUMN) {
                    QueryConstraintsDTO.Constraint dtoConstraint = new QueryConstraintsDTO.Constraint();
                    dtoConstraint.setColumn(constraint.getColumn());
                    dtoConstraint.setConfig(constraint.getConfig());
                    dto.getConstraints().add(dtoConstraint);
                }
            }
            result.add(dto);
        }
        return result;
    }
}
