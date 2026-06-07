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

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.platform.dal.model.CanBeReplaced;
import com.clougence.clouddm.platform.dal.model.auth.*;

/**
 * @author wanshao create time is 2021/1/5
 **/
public interface DmAuthUserMapper extends BaseMapper<DmAuthUserDO> {

    DmAuthUserDO queryById(long id);

    List<RsAuthPersonObj> searchUserByKeywords(@Param("primaryId") Long primaryId, @Param("keywords") String keywords);

    DmAuthUserDO queryPrimaryByPhone(String phone);

    DmAuthUserDO queryPrimaryByEmail(String email);

    List<DmAuthUserDO> querySubByPrimaryId(long primaryId);

    List<RsAuthPersonObj> queryApproPerson(@Param("accountType") AccountType accountType, @Param("parentId") long parentId, @Param("resId") Long resId,
                                           @Param("resPath") String resPath);

    List<RsAuthPersonObj> queryAuthApproPerson(@Param("accountType") AccountType accountType, @Param("parentId") long parentId);

    DmAuthUserDO querySubByPhone(String phone);

    DmAuthUserDO querySubByEmail(String email);

    DmAuthUserDO queryLocalLoginUserByAccount(@Param("account") String account);

    DmAuthUserDO queryLocalLoginUserByEmail(@Param("email") String email);

    DmAuthUserDO queryLocalLoginUserByPhone(@Param("phone") String phone);

    DmAuthUserDO querySubAccountByPhoneAndAccount(String phone, String subAccount);

    DmAuthUserDO querySubAccountByEmailAndAccount(String email, String subAccount);

    DmAuthUserDO queryByPhoneAndParentId(String phone, Long parentId);

    DmAuthUserDO queryByEmailAndParentId(String email, Long parentId);

    DmAuthUserDO queryBySubAccount(String subAccount);

    DmAuthUserDO queryBySubAccountExcludeUid(@Param("subAccount") String subAccount, @Param("excludeUid") String excludeUid);

    DmAuthUserDO queryByPhoneAndParentIdExcludeUid(@Param("phone") String phone, @Param("parentId") Long parentId, @Param("excludeUid") String excludeUid);

    DmAuthUserDO queryByEmailAndParentIdExcludeUid(@Param("email") String email, @Param("parentId") Long parentId, @Param("excludeUid") String excludeUid);

    DmAuthUserDO queryBySubAccountAndBind(String parentId, String subAccount, String bindType);

    DmAuthUserDO queryBySubAccountByBindInfo(Long parentId, String bindAccount, AccountBindType bindType);

    DmAuthUserDO queryByBindInfo(String bindAccount, AccountBindType bindType);

    DmAuthUserDO queryByUid(@CanBeReplaced String uid);

    DmAuthUserDO queryByAccessKey(String accessKey);

    long queryCountByParentId(Long parentId);

    List<DmAuthUserDO> listByUids(@Param("uids") List<String> uids);

    List<DmAuthUserDO> listByUserName(@Param("userName") String userName);

    List<DmAuthUserDO> listByRoleId(@Param("roleId") Long roleId);

    List<DmAuthUserDO> listByParentId(@Param("parentId") Long parentId);

    List<DmAuthUserDO> listByCondition(Long parentId, Long roleId, String prefix);

    List<DmAuthUserDO> listPrimaryAccount();

    List<DmAuthUserDO> listByIds(@Param("ids") List<Long> ids);

    Long queryPrimaryAccountCount();

    Long querySubAccountCount();

    Long queryUsableSubAccountCountByPrimary(Long parentId);

    List<DmAuthUserDO> listMaintainers();

    void updateRoleById(Long id, Long roleId);

    void updateLoginLimitInfo(Date lastTryLoginTime, int loginFailCount, boolean loginLocked, Long id);

    void updatePasswdById(Long id, String password);

    void updateOpPasswdById(Long id, String opPassword);

    void updateAbilityByUid(String uid, boolean disable);

    void updateUserStatus(String uid, UserStatus userStatus);

    void updateAccessTokenByUid(String uid, String accessToken);

    void updateSubAccountAndName(String uid, String subAccount, String userName);

    void updateUserName(String uid, String userName);

    void updateUserContactInfo(String uid, String phone, String email);

    void updateUserAkSk(String uid, String accessKey, String secretKey);

    void updateLastUpdatePwdTimeById(Long id);

    List<DmAuthUserDO> listSubAccountByPidAndAuth(Long pid, String authLabel);

    String queryUsernameByUid(String uid);

    void updateMfaStatus(@Param("uid") String uid, @Param("useMfa") boolean useMfa);
}
