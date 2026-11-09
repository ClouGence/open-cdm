package com.clougence.clouddm.console.web.global.session;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.config.RootUserConfig;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;

import jakarta.annotation.Resource;

@Service
public class SessionAccountPolicy {

    @Resource
    private SystemDal systemDal;

    public Long passwordValidDays(DmAuthUserDO user) {
        if (user.getParentId() == null || user.getLastDateUpdatePwd() == null) {
            return null;
        }
        Integer days = systemDal.fetchSystemConf(RootUserConfig.Fields.accountPwdExpireDays, Integer.class);
        if (days == null || days <= 0) {
            return null;
        }
        LocalDateTime updated = LocalDateTime.ofInstant(user.getLastDateUpdatePwd().toInstant(), ZoneId.systemDefault());
        long diffMinutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), updated.plusDays(days));
        return (long) Math.floor(((double) diffMinutes) / 60 / 24);
    }

    public boolean isPasswordExpired(DmAuthUserDO user) {
        if (user.getParentId() == null || user.getLastDateUpdatePwd() == null) {
            return false;
        }
        Integer days = systemDal.fetchSystemConf(RootUserConfig.Fields.accountPwdExpireDays, Integer.class);
        if (days == null || days <= 0) {
            return false;
        }
        LocalDateTime updated = LocalDateTime.ofInstant(user.getLastDateUpdatePwd().toInstant(), ZoneId.systemDefault());
        return !updated.plusDays(days).isAfter(LocalDateTime.now());
    }
}
