package com.clougence.rdp.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.rdp.dal.enumeration.AccountBindType;
import com.clougence.rdp.dal.enumeration.AccountType;
import com.clougence.rdp.dal.enumeration.AreaCode;
import com.clougence.rdp.dal.enumeration.UserStatus;
import com.clougence.rdp.dal.handler.AccountBindTypeTypeHandler;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wanshao create time is 2021/1/5
 **/
@Getter
@Setter
@TableName(value = "rdp_user")
public class RdpUserDO {

    @TableId(type = IdType.AUTO)
    private Long            id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date            gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date            gmtModified;

    private String          uid;

    private String          username;

    /**
     * for primary acount login
     */
    private String          email;

    /**
     * for primary acount login
     */
    private String          phone;

    private AreaCode        phoneAreaCode;

    /**
     * for sub account login,account@primary userDomain
     */
    private String          subAccount;

    private String          company;

    private String          password;

    private String          opPassword;

    private Long            roleId;

    private String          accessKey;

    private String          secretKey;

    private Date            lastTryLoginTime;

    private int             loginFailCount;

    private boolean         loginLocked;

    private Date            lastTryOpVerifyTime;

    private int             opVerifyFailCount;

    private boolean         opLocked;

    private AccountType     accountType;

    @TableField(typeHandler = AccountBindTypeTypeHandler.class)
    private AccountBindType bindType;

    private String          bindAccount;

    /**
     * "primary account uid".clouddm.com
     */
    private String          userDomain;

    private boolean         disable;

    private Long            parentId;

    private UserStatus      userStatus;

    private boolean         contactMe;

    private String          src;

    private String          keyword;

    private String          clientId;

    @Deprecated
    private String          country;

    /**
     * for maintainer (e.g., multi product cluster), can not be change on web and primary user (for saas manager)
     */
    private boolean         maintainer;

    @Deprecated
    private String          aliyunAk;

    @Deprecated
    private String          aliyunSk;

    @Deprecated
    private Date            lastDateUpdateAliyunAk;

    private Date            lastDateUpdatePwd;

    private String          unionId;

    private String          accessToken;

    @Deprecated
    private String          ssoType;

    private boolean         resourceManageEnable;

    @Deprecated
    private String          customerId;

    @Deprecated
    private String          saasUserStatus;

    @Deprecated
    private String          marketplaceType;

    private boolean         useMfa;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        RdpUserDO userDO = (RdpUserDO) o;

        if (!uid.equals(userDO.uid))
            return false;
        if (!email.equals(userDO.email))
            return false;
        if (!username.equals(userDO.username))
            return false;
        if (!company.equals(userDO.company))
            return false;
        if (phoneAreaCode != userDO.phoneAreaCode)
            return false;
        return phone.equals(userDO.phone);
    }

    @Override
    public int hashCode() {
        int result = uid.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + company.hashCode();
        result = 31 * result + phone.hashCode();
        result = 31 * result + phoneAreaCode.hashCode();
        return result;
    }
}
