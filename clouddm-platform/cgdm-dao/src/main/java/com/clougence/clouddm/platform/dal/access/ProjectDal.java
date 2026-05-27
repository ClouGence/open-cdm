package com.clougence.clouddm.platform.dal.access;

import com.clougence.clouddm.platform.dal.mapper.project.*;

public interface ProjectDal {

    DmProjectMapper projectMapper();

    DmProjectChangeMapper changeMapper();

    DmProjectChangeItemMapper changeItemMapper();

    DmProjectDevopsMapper devopsMapper();

    DmProjectDevopsItemMapper devopsItemMapper();

    DmProjectMsgMapper msgMapper();

    DmProjectScmMapper scmMapper();

    DmProjectVersionMapper versionMapper();
}
