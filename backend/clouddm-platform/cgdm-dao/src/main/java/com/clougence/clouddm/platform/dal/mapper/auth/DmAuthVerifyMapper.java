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
package com.clougence.clouddm.platform.dal.mapper.auth;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.platform.dal.model.auth.VerifyCodeType;
import com.clougence.clouddm.platform.dal.model.auth.VerifyType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthVerifyDO;

/**
 * @author bucketli 2020/2/27 19:57
 */
public interface DmAuthVerifyMapper extends BaseMapper<DmAuthVerifyDO> {

    /** query by uid ,get all uid's {@link DmAuthVerifyDO}s */
    List<DmAuthVerifyDO> queryByUid(String uid);

    DmAuthVerifyDO queryByUidAndType(VerifyType verifyType, VerifyCodeType verifyCodeType, String uid);

    /** query by email（sql condition contain {@link VerifyType#EMAIL_VERIFY_CODE},{@link VerifyCodeType},email） */
    DmAuthVerifyDO queryByPrimaryEmail(VerifyType verifyType, VerifyCodeType verifyCodeType, String email);

    /** query by phone (sql condition contain {@link VerifyType#SMS_VERIFY_CODE},{@link VerifyCodeType},phone) */
    DmAuthVerifyDO queryByPrimaryPhone(VerifyType verifyType, VerifyCodeType verifyCodeType, String phone);

    /** update failTimes by {@link DmAuthVerifyDO id} */
    void updateFailTimesAndDateById(Integer failTimes, Date failDate, Long id);

    /** update sent verify code and time by {@link DmAuthVerifyDO id} */
    int updateVerifyCodeAndSendTime(String verifyCode, Date sendTime, Long id);

    void deleteByUid(String uid);

    /** query by uid ,get all uid's {@link DmAuthVerifyDO}s */
    void deleteOldData(Date beforeThisTime);

    void updatePhoneOrEmailByUid(String uid, String phone, String email, VerifyCodeType verifyCodeType, VerifyType verifyType);
}
