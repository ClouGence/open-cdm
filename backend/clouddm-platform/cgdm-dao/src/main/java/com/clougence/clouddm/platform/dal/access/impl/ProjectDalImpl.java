package com.clougence.clouddm.platform.dal.access.impl;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.platform.dal.access.ProjectDal;
import com.clougence.clouddm.platform.dal.mapper.project.*;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProjectDalImpl implements ProjectDal {

    @Resource
    private DmProjectMapper           projectMapper;
    @Resource
    private DmProjectChangeMapper     changeMapper;
    @Resource
    private DmProjectChangeItemMapper changeItemMapper;
    @Resource
    private DmProjectDevopsMapper     devopsMapper;
    @Resource
    private DmProjectDevopsItemMapper devopsItemMapper;
    @Resource
    private DmProjectMsgMapper        msgMapper;
    @Resource
    private DmProjectScmMapper        scmMapper;
    @Resource
    private DmProjectVersionMapper    versionMapper;

    @Override
    public DmProjectMapper projectMapper() {
        return projectMapper;
    }

    @Override
    public DmProjectChangeMapper changeMapper() {
        return changeMapper;
    }

    @Override
    public DmProjectChangeItemMapper changeItemMapper() {
        return changeItemMapper;
    }

    @Override
    public DmProjectDevopsMapper devopsMapper() {
        return devopsMapper;
    }

    @Override
    public DmProjectDevopsItemMapper devopsItemMapper() {
        return devopsItemMapper;
    }

    @Override
    public DmProjectMsgMapper msgMapper() {
        return msgMapper;
    }

    @Override
    public DmProjectScmMapper scmMapper() {
        return scmMapper;
    }

    @Override
    public DmProjectVersionMapper versionMapper() {
        return versionMapper;
    }

    // ---------- dal service methods ----------
}
