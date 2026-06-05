package com.clougence.clouddm.platform.dal.access;

import com.clougence.clouddm.platform.dal.mapper.execution.*;

public interface ExecutionDal {

    DmExecAsyncTaskMapper asyncTaskMapper();

    DmExecAutoJobMapper autoJobMapper();

    DmExecAutoTaskMapper autoTaskMapper();

    DmExecFileMapper fileMapper();

    DmExecQueryConstraintsMapper queryConstraintsMapper();

    DmExecSessionMapper sessionMapper();

    DmExecSqlAuditMapper sqlAuditMapper();
}
