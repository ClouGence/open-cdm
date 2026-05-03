package com.clougence.clouddm.console.web.util;

import static com.clougence.rdp.util.RandomStrUtils.fixedLenRandomStr;

import org.springframework.context.ApplicationContext;

import com.clougence.clouddm.console.web.dal.enumeration.SQLJobBizType;
import com.clougence.clouddm.console.web.dal.mapper.DmAutoExecJobMapper;
import com.clougence.clouddm.console.web.dal.model.exec.DmAutoExecJobDO;

/**
 * @author mode create time is 2021/1/30
 **/
public class DmTeamUtils {

    private static DmAutoExecJobMapper dmAutoExecJobMapper;

    public static void initUtils(ApplicationContext spring) {
        dmAutoExecJobMapper = spring.getBean(DmAutoExecJobMapper.class);
    }

    public static String nextExecJobBizId(SQLJobBizType bizType) {
        String namePattern = "auto" + bizType + "-Job-%s";
        while (true) {
            String bizId = String.format(namePattern, fixedLenRandomStr(20));
            DmAutoExecJobDO jobDO = dmAutoExecJobMapper.queryByBizId(bizId);
            if (jobDO == null) {
                return bizId;
            }
        }
    }

    public static String nextExecTaskBizId(SQLJobBizType bizType) {
        String namePattern = "auto" + bizType + "-Task-%s";
        while (true) {
            String bizId = String.format(namePattern, fixedLenRandomStr(20));
            DmAutoExecJobDO jobDO = dmAutoExecJobMapper.queryByBizId(bizId);
            if (jobDO == null) {
                return bizId;
            }
        }
    }
}
