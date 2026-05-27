package com.clougence.clouddm.platform.dal.access.impl;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.mapper.execution.*;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExecutionDalImpl implements ExecutionDal {

    @Resource
    private DmExecAsyncTaskMapper        asyncTaskMapper;
    @Resource
    private DmExecAutoJobMapper          autoJobMapper;
    @Resource
    private DmExecAutoTaskMapper         autoTaskMapper;
    @Resource
    private DmExecFileMapper             fileMapper;
    @Resource
    private DmExecQueryConstraintsMapper queryConstraintsMapper;
    @Resource
    private DmExecSessionMapper          sessionMapper;
    @Resource
    private DmExecSqlAuditMapper         sqlAuditMapper;

    @Override
    public DmExecAsyncTaskMapper asyncTaskMapper() {
        return asyncTaskMapper;
    }

    @Override
    public DmExecAutoJobMapper autoJobMapper() {
        return autoJobMapper;
    }

    @Override
    public DmExecAutoTaskMapper autoTaskMapper() {
        return autoTaskMapper;
    }

    @Override
    public DmExecFileMapper fileMapper() {
        return fileMapper;
    }

    @Override
    public DmExecQueryConstraintsMapper queryConstraintsMapper() {
        return queryConstraintsMapper;
    }

    @Override
    public DmExecSessionMapper sessionMapper() {
        return sessionMapper;
    }

    @Override
    public DmExecSqlAuditMapper sqlAuditMapper() {
        return sqlAuditMapper;
    }
}
