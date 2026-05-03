package com.clougence.rdp.dal.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.constant.auth.CanBeReplaced;
import com.clougence.rdp.dal.enumeration.AccountBindType;
import com.clougence.rdp.dal.enumeration.AccountType;
import com.clougence.rdp.dal.enumeration.AreaCode;
import com.clougence.rdp.dal.enumeration.UserStatus;
import com.clougence.rdp.dal.model.RdpTicketApproPersonDO;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.dal.model.RdpUserInfoDO;

/**
 * @author wanshao create time is 2021/1/5
 **/
public interface RdpUserMapper extends BaseMapper<RdpUserDO> {

    RdpUserDO queryById(long id);

    List<RdpUserInfoDO> searchUserByKeywords(String userDomain, String keywords);

    RdpUserDO queryPrimaryByPhone(String phone);

    RdpUserDO queryPrimaryByPhoneAndAreaCode(String phone, AreaCode areaCode);

    RdpUserDO queryPrimaryByEmail(String email);

    RdpUserDO queryPrimaryByCompany(String company);

    RdpUserDO queryPrimaryByDomain(String domain);

    List<RdpUserDO> querySubByPrimaryId(long primaryId);

    List<RdpTicketApproPersonDO> queryApproPerson(@Param("accountType") AccountType accountType, @Param("parentId") long parentId, @Param("resId") Long resId,
                                                  @Param("resPath") String resPath);

    List<RdpTicketApproPersonDO> queryAuthApproPerson(@Param("accountType") AccountType accountType, @Param("parentId") long parentId);

    RdpUserDO querySubByPhone(String phone);

    RdpUserDO querySubByEmail(String email);

    RdpUserDO querySubAccountByPhoneAndAccount(String phone, String subAccount);

    RdpUserDO querySubAccountByEmailAndAccount(String email, String subAccount);

    RdpUserDO queryByPhoneAndParentId(String phone, Long parentId);

    RdpUserDO queryByEmailAndParentId(String email, Long parentId);

    RdpUserDO queryBySubAccount(String subAccount);

    RdpUserDO queryBySubAccountAndBind(String parentId, String subAccount, String bindType);

    RdpUserDO queryBySubAccountByBindInfo(Long parentId, String bindAccount, AccountBindType bindType);

    RdpUserDO queryByUid(@CanBeReplaced String uid);

    RdpUserDO queryByAccessKey(String accessKey);

    long queryCountByParentId(Long parentId);

    List<RdpUserDO> listByUids(@Param("uids") List<String> uids);

    List<RdpUserDO> listByUserName(@Param("userName") String userName);

    List<RdpUserDO> listByRoleId(@Param("roleId") Long roleId);

    List<RdpUserDO> listByParentId(@Param("parentId") Long parentId);

    List<RdpUserDO> listByCondition(Long parentId, Long roleId, String prefix);

    List<RdpUserDO> listPrimaryAccount();

    List<RdpUserDO> listByIds(@Param("ids") List<Long> ids);

    Long queryPrimaryAccountCount();

    Long querySubAccountCount();

    Long queryUsableSubAccountCountByPrimary(Long parentId);

    List<RdpUserDO> listMaintainers();

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

    void updateUserAliyunAkSk(String uid, String aliyunAk, String aliyunSk);

    void updateUserAkSk(String uid, String accessKey, String secretKey);

    void updateLastUpdatePwdTimeById(Long id);

    List<RdpUserDO> listSubAccountByPidAndAuth(Long pid, String authLabel);

    void updateResourceMangeEnable(@Param("targetUid") String targetUid, @Param("resourceManage") boolean resourceManage);

    String queryUsernameByUid(String uid);

    List<RdpUserDO> listSubResourceManagersByPrimaryUId(@Param("puid") String puid);

    List<RdpUserDO> listSubResourceManagersByPrimaryId(@Param("pid") Long pid);

    Boolean isResourceManger(@Param("targetUid") String targetUid);

    void updateMfaStatus(@Param("uid") String uid, @Param("useMfa") boolean useMfa);
}
