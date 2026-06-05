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
package com.clougence.clouddm.console.web.util;

import static com.clougence.clouddm.console.web.util.RandomStrUtils.fixedLenRandomStr;

import org.springframework.context.ApplicationContext;

import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAutoJobDO;
import com.clougence.clouddm.platform.dal.model.execution.SQLJobBizType;

/**
 * @author mode create time is 2021/1/30
 **/
public class DmTeamUtils {

    private static ExecutionDal executionDal;

    public static void initUtils(ApplicationContext spring) {
        executionDal = spring.getBean(ExecutionDal.class);
    }

    public static String nextExecJobBizId(SQLJobBizType bizType) {
        String namePattern = "auto" + bizType + "-Job-%s";
        while (true) {
            String bizId = String.format(namePattern, fixedLenRandomStr(20));
            DmExecAutoJobDO jobDO = executionDal.autoJobMapper().queryByBizId(bizId);
            if (jobDO == null) {
                return bizId;
            }
        }
    }

    public static String nextExecTaskBizId(SQLJobBizType bizType) {
        String namePattern = "auto" + bizType + "-Task-%s";
        while (true) {
            String bizId = String.format(namePattern, fixedLenRandomStr(20));
            DmExecAutoJobDO jobDO = executionDal.autoJobMapper().queryByBizId(bizId);
            if (jobDO == null) {
                return bizId;
            }
        }
    }
}
