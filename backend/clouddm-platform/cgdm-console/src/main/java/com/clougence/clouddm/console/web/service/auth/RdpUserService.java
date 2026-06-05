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
package com.clougence.clouddm.console.web.service.auth;

import java.util.Collection;
import java.util.List;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.console.web.model.fo.*;
import com.clougence.clouddm.console.web.model.fo.role.UpdateUserRoleFO;
import com.clougence.clouddm.console.web.model.fo.user.*;
import com.clougence.clouddm.console.web.model.lo.UpdateUserRoleLO;
import com.clougence.clouddm.console.web.model.vo.ListUserVO;
import com.clougence.clouddm.console.web.model.vo.PwdValidateExprVO;
import com.clougence.clouddm.console.web.model.vo.RdpUserAkSkVO;
import com.clougence.clouddm.platform.dal.model.auth.AccountBindType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.rdp.service.model.*;

/**
 * @author wanshao create time is 2019/12/12 9:36 下午
 **/
public interface RdpUserService {

    String PUID                       = "puid";
    String UID                        = "uid";

    //
    String USER_ROLE                  = "user_role";
    String IS_MAINTAINER              = "is_maintainer";
    String ACCESSKEY                  = "accesskey";

    long   OP_PASSWD_TOEKN_EXPIRE_MS  = 30 * 60 * 1000;
    long   MFA_TOKEN_EXPIRE_SEC       = 120;
    String DEFAULT_USER_DOMAIN_SUFFIX = "cdmgr.com";

    //
    String DEFAULT_PWD_REGEX          = "^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[^\\da-zA-Z\\s]).{8,32}$";
    String EMAIL_VALIDATE_REGEX       = "^[A-Za-z0-9+_.-]+@(.+)$";
    String CHINA_PHONE_VALIDATE_REGEX = "^\\d{1,20}$";

    Collection<AuthInfo> allAuthLabelByUser(String puid, String uid);

    Collection<AuthInfo> allAuthMenuCategoryByUser(String puid, String uid);

    DmAuthUserDO getUserByUid(String uid);

    DmAuthUserDO getUserById(long id);

    boolean isPrimaryUid(String uid);

    boolean isMaintainer(String uid);

    PwdValidateExprVO getPwdValidateExprWithoutEscape(String puid);

    //
    // -- for Current User Manager
    //

    ValidateResultMO validatePrimaryAccountPwd(String pwd);

    ValidateResultMO validateSubAccountPwd(String puid, String pwd);

    ValidateResultMO validateByExpr(String expr, String errorMsg, String content);

    UpdateUserInfoMO resetOpPasswd(ResetOpPasswdFO fo, String uid);

    OpPasswdVerifyMO opPasswdVerify(String opPassword, String uid);

    UpdateUserInfoMO updateUserPhone(String uid, UpdateUserPhoneFO fo);

    UpdateUserInfoMO updateUserEmail(String uid, UpdateUserEmailFO fo);

    UpdateUserInfoMO updateUserPhoneWithPwd(String uid, UpdateUserPhoneWithPwdFO fo);

    UpdateUserInfoMO updateUserEmailWithPwd(String uid, UpdateUserEmailWithPwdFO fo);

    void updateAliyunAkSk(String puid, String ak, String sk);

    void cleanAliyunAkSk(String puid);

    ResWebData<RdpUserAkSkVO> queryAkSk(String puid, QueryUserAkSkFO fo);

    ResWebData<String> resetAkSk(String puid, ResetUserAkSkFO fo);

    //
    // -- for Other Manager

    UpdateUserInfoMO resetPassword(ResetPasswdFO fo);

    UpdateUserInfoMO resetPwdWithOriginPwd(ResetPwdWithOriginPwdFO fo, String targetUid, String puid);

    UpdateUserInfoMO resetSubAccountPwd(ResetSubAccountPwdFO fo, String operatorUid);

    List<DmAuthUserDO> listSubAccounts(String puid);

    List<ListUserVO> listSubAccounts(String puid, ListSubAccountsFO fo);

    AddSubAccountMO addSubAccountForBind(String puid, AccountBindType bindType, DmAuthUserDO bindUser);

    AddSubAccountMO addSubAccountForInternal(String puid, AddSubAccountFO fo);

    UpdateUserInfoMO updateSubAccount(UpdateSubAccountFO fo, String puid);

    CheckSubAccountMO checkSubAccount(String puid, CheckSubAccountFO fo);

    ResWebData<Boolean> deleteSubAccount(String puid, DeleteSubAccountFO fo);

    UpdateUserRoleLO updateUserRole(UpdateUserRoleFO fo);

    ResWebData<Boolean> updateAccountAbility(String puid, AccountAbilityFO fo);

    //
    //
    //

    DmAuthUserDO getUserByAk(String ak);

    String getPrimaryUid(String uid);

    DmAuthUserDO getPrimaryUser(String uid);

    List<DmAuthUserDO> listPrimaryUser();

    UpdateUserInfoMO updateResourceManage(UpdateResourceManageFO fo, String puid);
}
