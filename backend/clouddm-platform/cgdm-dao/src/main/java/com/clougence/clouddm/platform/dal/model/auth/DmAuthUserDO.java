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
package com.clougence.clouddm.platform.dal.model.auth;

import java.util.Date;
import java.util.Objects;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.platform.dal.handler.AccountBindTypeTypeHandler;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wanshao create time is 2021/1/5
 **/
@Getter
@Setter
@TableName(value = "dm_auth_user")
public class DmAuthUserDO {

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

    private String          account;

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

    private boolean         allowLocal;

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

    private Date            lastDateUpdatePwd;

    private String          unionId;

    private String          accessToken;

    @Deprecated
    private String          ssoType;

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

        DmAuthUserDO userDO = (DmAuthUserDO) o;

        if (!Objects.equals(uid, userDO.uid)) {
            return false;
        }

        if (!Objects.equals(email, userDO.email)) {
            return false;
        }

        if (!Objects.equals(username, userDO.username)) {
            return false;
        }

        return Objects.equals(phone, userDO.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, email, username, phone);
    }
}
