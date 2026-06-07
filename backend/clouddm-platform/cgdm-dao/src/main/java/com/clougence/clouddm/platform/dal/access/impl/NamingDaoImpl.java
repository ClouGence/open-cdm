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
package com.clougence.clouddm.platform.dal.access.impl;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.NamingDao;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.secrule.RuleKind;
import com.clougence.clouddm.platform.dal.model.system.DmSysClusterDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysWorkerDO;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020/7/10 21:18
 */
@Slf4j
@Service
public class NamingDaoImpl implements NamingDao {
    @Resource
    private SystemDal   systemDal;
    @Resource
    private AuthDal     authDal;
    @Resource
    private ApprovalDal approvalDal;

    @Override
    public String genClusterName() {
        String namePattern = "cluster%s";
        while (true) {
            String name = String.format(namePattern, fixedLenRandomStr(10));
            DmSysClusterDO clusterDO = systemDal.clusterMapper().getClusterByName(name);
            if (clusterDO == null) {
                return name;
            }
        }
    }

    @Override
    public String genApprovalBizId() {
        String namePattern = "ticket%s";
        while (true) {
            String bizId = String.format(namePattern, fixedLenRandomStr(10));
            DmApprovalDO ticketDO = approvalDal.approvalMapper().queryByBizId(bizId);
            if (ticketDO == null) {
                return bizId;
            }
        }
    }

    @Override
    public String genWorkerSequenceNumber() {
        String namePattern = "wsn%s";
        while (true) {
            String seq = String.format(namePattern, fixedLenRandomStr(61));
            DmSysWorkerDO workerDO = systemDal.workerMapper().getByWsn(seq);
            if (workerDO == null) {
                return seq;
            }
        }
    }

    @Override
    public String genWorkerName() {
        String namePattern = "worker%s";
        while (true) {
            String name = String.format(namePattern, fixedLenRandomStr(11));
            DmSysWorkerDO workerDO = systemDal.workerMapper().getByName(name);
            if (workerDO == null) {
                return name;
            }
        }
    }

    @Override
    public String genAccessKey() {
        String namePattern = "ak%s";

        while (true) {
            String ak = String.format(namePattern, fixedLenRandomStr(61));
            DmAuthUserDO userDO = authDal.userMapper().queryByAccessKey(ak);
            if (userDO == null) {
                return ak;
            }
        }
    }

    @Override
    public String genUID() {
        while (true) {
            String uid = fixedLenRandomNumberStr(16);
            DmAuthUserDO user = authDal.userMapper().queryByUid(uid);
            if (user == null) {
                return uid;
            }
        }
    }

    @Override
    public String genSecretKey() {
        String namePattern = "sk%s";
        return String.format(namePattern, fixedLenRandomStr(61));
    }

    @Override
    public String genLoginAccount() {
        while (true) {
            String account = fixedLenRandomStr(8);
            DmAuthUserDO user = authDal.userMapper().queryLocalLoginUserByAccount(account);
            if (user == null) {
                return account;
            }
        }
    }

    @Override
    public String genSecRuleName(RuleKind ruleKind) {
        String namePattern = "rule%s";
        return String.format(namePattern, fixedLenRandomStr(10));
    }

    /**
     * generator random string with number or character
     */
    private static String fixedLenRandomStr(int length) {
        if (length == 0) {
            return "";
        }

        char[] fixedLenRandomCharArr = new char[length];
        int flag = 0;
        for (int i = 0; i < length; i++) {
            flag = (int) (Math.random() * 2);
            if (flag == 0) {
                // 产生数字
                int charVal = (int) (Math.random() * 10 + 48);
                fixedLenRandomCharArr[i] = (char) charVal;
            } else {
                // 产生小写字母
                int charVal = (int) ((Math.random() * 26) + 97);
                fixedLenRandomCharArr[i] = (char) charVal;
            }

        }
        return new String(fixedLenRandomCharArr);
    }

    /**
     * generator random string with number
     */
    private static String fixedLenRandomNumberStr(int length) {
        if (length == 0) {
            return "";
        }

        char[] fixedLenRandomCharArr = new char[length];
        boolean first = true;
        for (int i = 0; i < length; i++) {
            int charVal = (int) (Math.random() * 10 + 48);
            // first number can not be zero
            if (charVal == 0 && first) {
                i--;
                continue;
            }

            if (first) {
                first = false;
            }

            fixedLenRandomCharArr[i] = (char) charVal;
        }

        return new String(fixedLenRandomCharArr);
    }
}
