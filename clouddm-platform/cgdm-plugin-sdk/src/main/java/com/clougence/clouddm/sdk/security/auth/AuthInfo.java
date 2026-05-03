package com.clougence.clouddm.sdk.security.auth;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2021/2/23
 **/
@Getter
@Setter
public final class AuthInfo {

    // basic
    private String                               defField;
    private String                               key;
    private String                               keyI18n;
    private AuthInfoType                         authType;
    private List<AuthKind>                       kinds;
    private boolean                              usedOfRole;
    private int                                  order;
    private List<String>                         forProduct;

    // for category
    private boolean                              hidden;
    private String                               parent;

    // for label
    private String                               category;
    /** System core auth must select and it is readonly */
    private boolean                              mustSelectAndReadOnly;
    private List<String>                         include;
    private List<String>                         tag;
    private Map<AuthKind, List<AuthElementType>> condition;

    // for specific
    private AuthInfoScope                        scope;
    private DataSourceType                       scopeDs;
}
