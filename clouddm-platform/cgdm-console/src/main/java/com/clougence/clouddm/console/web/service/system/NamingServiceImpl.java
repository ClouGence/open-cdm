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
package com.clougence.clouddm.console.web.service.system;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.dal.enumeration.RuleKind;
import com.clougence.clouddm.console.web.dal.mapper.DmApprovalMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmClusterMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmWorkerMapper;
import com.clougence.clouddm.console.web.dal.mapper.RdpUserMapper;
import com.clougence.clouddm.console.web.dal.model.DmApprovalDO;
import com.clougence.clouddm.console.web.dal.model.DmClusterDO;
import com.clougence.clouddm.console.web.dal.model.DmWorkerDO;
import com.clougence.clouddm.console.web.dal.model.RdpUserDO;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020/7/10 21:18
 */
@Slf4j
@Service
public class NamingServiceImpl implements NamingService {

    @Resource
    private DmClusterMapper  clusterMapper;
    @Resource
    private DmWorkerMapper   workerMapper;
    @Resource
    private RdpUserMapper    userMapper;
    @Resource
    private DmApprovalMapper approvalMapper;

    @Override
    public String genLocalClusterName() {
        String namePattern = "localcluster%s";
        while (true) {
            String name = String.format(namePattern, fixedLenRandomStr(10));
            DmClusterDO clusterDO = clusterMapper.getClusterByName(name);
            if (clusterDO == null) {
                return name;
            }
        }
    }

    @Override
    public String genClusterName() {
        String namePattern = "cluster%s";
        while (true) {
            String name = String.format(namePattern, fixedLenRandomStr(10));
            DmClusterDO clusterDO = clusterMapper.getClusterByName(name);
            if (clusterDO == null) {
                return name;
            }
        }
    }

    @Override
    public String genTicketBizId() {
        String namePattern = "ticket%s";
        while (true) {
            String bizId = String.format(namePattern, fixedLenRandomStr(10));
            DmApprovalDO ticketDO = approvalMapper.queryByBizId(bizId);
            if (ticketDO == null) {
                return bizId;
            }
        }
    }

    @Override
    public String genDefaultClusterName() {
        String namePattern = "defaultcluster%s";
        while (true) {
            String name = String.format(namePattern, fixedLenRandomStr(10));
            DmClusterDO clusterDO = clusterMapper.getClusterByName(name);
            if (clusterDO == null) {
                return name;
            }
        }
    }

    @Override
    public String genWorkerSequenceNumber() {
        String namePattern = "wsn%s";
        while (true) {
            String seq = String.format(namePattern, fixedLenRandomStr(61));
            DmWorkerDO workerDO = workerMapper.getByWsn(seq);
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
            DmWorkerDO workerDO = workerMapper.getByName(name);
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
            RdpUserDO userDO = userMapper.queryByAccessKey(ak);
            if (userDO == null) {
                return ak;
            }
        }
    }

    @Override
    public String genUid() {
        while (true) {
            String uid = fixedLenRandomNumberStr(16);
            RdpUserDO user = userMapper.queryByUid(uid);
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
    public String genInnerUserPwd() {
        String namePattern = "inner%s";
        return String.format(namePattern, fixedLenRandomStr(61));
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
