package com.clougence.clouddm.console.web.service.sdk;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.dal.enumeration.QueryConstraintType;
import com.clougence.clouddm.console.web.dal.mapper.DmQueryConstraintsMapper;
import com.clougence.clouddm.console.web.dal.model.DmQueryConstraintsDO;
import com.clougence.clouddm.sdk.analysis.column.QueryConstraintsDTO;
import com.clougence.clouddm.sdk.analysis.column.QueryConstraintService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConsoleQueryConstraintServiceImpl implements QueryConstraintService {

    @Resource
    private DmQueryConstraintsMapper constraintsMapper;

    @Override
    public List<QueryConstraintsDTO> fetchQueryConstraints(String primaryUid, long dsId, List<String> path) {
        List<DmQueryConstraintsDO> dmConstraintsDOS = constraintsMapper.selectAllByUid(primaryUid, dsId, path);
        List<QueryConstraintsDTO> result = new ArrayList<>();
        for (DmQueryConstraintsDO dmConstraintsDO : dmConstraintsDOS) {
            QueryConstraintsDTO dto = new QueryConstraintsDTO();
            dto.setDsId(dmConstraintsDO.getDsId());
            dto.setPath(dmConstraintsDO.getPath());
            dto.setConstraints(new ArrayList<>());
            for (DmQueryConstraintsDO.Constraint constraint : dmConstraintsDO.getConstraints()) {
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
