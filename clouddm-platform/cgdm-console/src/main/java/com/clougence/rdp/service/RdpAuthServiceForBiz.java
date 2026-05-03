package com.clougence.rdp.service;

import java.util.List;

import com.clougence.rdp.dal.model.RdpResAuthDO;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.model.analysis.resource.DsResPath;

/**
 * @author mode 2020/12/8 15:21
 */
public interface RdpAuthServiceForBiz {

    boolean checkRoleAuth(String puid, String uid, String roleAuth);

    void checkOperateOtherUserAuth(String loginUid, String targetUid);

    void checkResOwnership(String puid, long resId, AuthKind authKind);

    boolean checkResAuthWithoutError(String puid, String uid, long resId, DsResPath resPath, String dataAuthLabel, AuthKind authKind);

    void checkResAuth(String puid, String uid, long resId, DsResPath resPath, String dataAuthLabel, AuthKind authKind);

    List<RdpResAuthDO> listAuthByUser(String targetUid, AuthKind authKind);

    List<Long> listResByUser(String targetUid, AuthKind authKind);

    List<RdpResAuthDO> listSpecifiedAuthOfUser(String targetUid, String dataAuthLabel, AuthKind authKind);
}
