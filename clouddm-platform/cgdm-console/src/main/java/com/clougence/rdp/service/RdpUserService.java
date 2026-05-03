package com.clougence.rdp.service;

import java.util.Collection;
import java.util.List;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.rdp.controller.model.fo.*;
import com.clougence.rdp.controller.model.lo.UpdateUserRoleLO;
import com.clougence.rdp.controller.model.vo.ListUserVO;
import com.clougence.rdp.controller.model.vo.PwdValidateExprVO;
import com.clougence.rdp.controller.model.vo.RdpUserAkSkVO;
import com.clougence.rdp.dal.enumeration.AccountBindType;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.rdp.service.model.*;

/**
 * @author wanshao create time is 2019/12/12 9:36 下午
 **/
public interface RdpUserService {

    String PUID                       = "puid";
    String UID                        = "uid";

    //
    String INNER_USER_EMAIL           = "inner@clougence.com";
    String INNER_USER_NAME            = "clougence_inner";
    String INNER_USER_COMPANY_NAME    = "clougence";
    String INNER_USER_PHONE           = "15661234567";

    //
    String USER_ROLE                  = "user_role";
    String IS_MAINTAINER              = "is_maintainer";
    String EMAIL                      = "email";
    String PHONE                      = "phone";
    String USERNAME                   = "username";
    String ACCESSKEY                  = "accesskey";

    long   OP_PASSWD_TOEKN_EXPIRE_MS  = 30 * 60 * 1000;

    long   MFA_TOKEN_EXPIRE_SEC       = 120;

    String DEFAULT_USER_DOMAIN_SUFFIX = "clougence.com";

    //
    String DEFAULT_PWD_REGEX          = "^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[^\\da-zA-Z\\s]).{8,32}$";
    String EMAIL_VALIDATE_REGEX       = "^[A-Za-z0-9+_.-]+@(.+)$";
    String CHINA_PHONE_VALIDATE_REGEX = "^\\d{1,20}$";

    Collection<AuthInfo> allAuthLabelByUser(String puid, String uid);

    Collection<AuthInfo> allAuthMenuCategoryByUser(String puid, String uid);

    RdpUserDO getUserByUid(String uid);

    RdpUserDO getUserById(long id);

    RdpUserDO getInnerUser();

    boolean isPrimaryUid(String uid);

    boolean isMaintainer(String uid);

    boolean isCloudManagedUser(String puid, String uid);

    /**
     * if uid is not cloud managed uid, return null, this method for high performance
     */
    RdpUserDO cloudManagedBindUser(String puid, String uid);

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

    List<RdpUserDO> listSubAccounts(String puid);

    List<ListUserVO> listSubAccounts(String puid, ListSubAccountsFO fo);

    AddSubAccountMO addSubAccountForBind(String puid, AccountBindType bindType, RdpUserDO bindUser);

    AddSubAccountMO addSubAccountForInternal(String puid, AddSubAccountFO fo);

    UpdateUserInfoMO updateSubAccount(UpdateSubAccountFO fo, String puid);

    CheckSubAccountMO checkSubAccount(String puid, CheckSubAccountFO fo);

    ResWebData<Boolean> deleteSubAccount(String puid, DeleteSubAccountFO fo);

    UpdateUserRoleLO updateUserRole(UpdateUserRoleFO fo);

    ResWebData<Boolean> updateAccountAbility(String puid, AccountAbilityFO fo);

    //
    //
    //

    RdpUserDO getUserByAk(String ak);

    String getPrimaryUid(String uid);

    RdpUserDO getPrimaryUser(String uid);

    List<RdpUserDO> listPrimaryUser();

    UpdateUserInfoMO updateResourceManage(UpdateResourceManageFO fo, String puid);
}
