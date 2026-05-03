package com.clougence.clouddm.console.web.component.auth;

import java.util.List;

import com.clougence.clouddm.console.web.component.auth.model.ResourceAccessInfo;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.rdp.dal.model.RdpResAuthDO;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.model.analysis.resource.DsResPath;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;

/**
 * @author mode 2020/12/8 15:21
 */
public interface DmResAuthService {

    List<RdpResAuthDO> listAuthByUser(String targetUid, AuthKind authKind);

    List<RdpResAuthDO> listAuthByUser(long dsId, String targetUid, AuthKind authKind, List<String> path);

    List<Long> listResByUser(String targetUid, AuthKind authKind);

    List<Long> listResByUserContainAnyAuth(String targetUid, AuthKind authKind);

    boolean checkResAuth(String puid, String uid, long dsId, DsResPath dsObj, String resAuth);

    /** The permissions of resources and functions are verified at the same time */
    <T extends DsResPath> List<T> filterAuthByUser(String puid, String uid, long dsId, List<T> dsResource, String resAuth);

    AuthInfo getAuthInfo(String authKey);

    ResourceAccessInfo getAllowBrowseInfo(DsLevels levels, String uid);

}
