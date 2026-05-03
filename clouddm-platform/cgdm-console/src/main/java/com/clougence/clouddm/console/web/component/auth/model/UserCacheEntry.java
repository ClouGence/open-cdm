package com.clougence.clouddm.console.web.component.auth.model;

import java.time.LocalDateTime;

import com.clougence.rdp.dal.enumeration.AccountBindType;
import com.clougence.rdp.dal.enumeration.AccountType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2024/2/27 13:20:36
 */
@Getter
@Setter
public class UserCacheEntry {

    private Long            userNumId;
    private String          uid;
    private String          parentUid;
    private AccountType     userType;
    private AccountBindType bindType;
    private String          ak;
    private String          userName;
    private String          roleName;
    private LocalDateTime   addTime = LocalDateTime.now();
}
