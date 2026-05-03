package com.clougence.clouddm.init.fix;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.rdp.service.impl.RdpRoleServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RdpFixUserRole {

    @Resource
    private RdpUserService     rdpUserService;
    @Resource
    private RdpRoleServiceImpl rdpRoleService;

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void init() {
        this.rdpRoleService.init();

        List<RdpUserDO> primaryUsers = this.rdpUserService.listPrimaryUser();
        for (RdpUserDO user : primaryUsers) {
            log.info("RdpFixUserRole: repairRoleForUser " + user.getUid());
            this.rdpRoleService.repairRoleForUser(user.getUid());
        }
    }
}
