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
package com.clougence.rdp.service.impl;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.util.RandomStrUtils;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.rdp.service.RdpNamingService;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020/7/10 21:18
 */
@Slf4j
@Service
public class RdpNamingServiceImpl implements RdpNamingService {
    @Resource
    private AuthDal authDal;

    @Override
    public String genAccessKey() {
        String namePattern = "ak%s";

        while (true) {
            String ak = String.format(namePattern, RandomStrUtils.fixedLenRandomStr(61));
            DmAuthUserDO userDO = authDal.userMapper().queryByAccessKey(ak);
            if (userDO == null) {
                return ak;
            }
        }
    }

    @Override
    public String genUid() {
        while (true) {
            String uid = RandomStrUtils.fixedLenRandomNumberStr(16);
            DmAuthUserDO user = authDal.userMapper().queryByUid(uid);
            if (user == null) {
                return uid;
            }
        }
    }

    @Override
    public String genSecretKey() {
        String namePattern = "sk%s";
        return String.format(namePattern, RandomStrUtils.fixedLenRandomStr(61));
    }

    @Override
    public String genInnerUserPwd() {
        String namePattern = "inner%s";
        return String.format(namePattern, RandomStrUtils.fixedLenRandomStr(61));
    }

    @Override
    public String genProductClusterName() {
        String namePattern = "pc_name_%s";
        return String.format(namePattern, RandomStrUtils.fixedLenRandomStr(16));
    }

    @Override
    public String genProductClusterCode() {
        String namePattern = "pc_code_%s";
        return String.format(namePattern, RandomStrUtils.fixedLenRandomStr(16));
    }

    @Override
    public String genConsoleJobToken() {
        String namePattern = "cjtoken_" + System.currentTimeMillis() + "_%s";
        return String.format(namePattern, RandomStrUtils.fixedLenRandomStr(16));
    }
}
