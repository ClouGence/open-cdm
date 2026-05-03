package com.clougence.rdp.service;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.rdp.controller.model.fo.security.ModifyUserAuthFO;
import com.clougence.rdp.controller.model.fo.ticket.RdpAddAuthTicketFO;
import com.clougence.rdp.controller.model.vo.RdpAuthObjectVO;
import com.clougence.rdp.dal.model.RdpResAuthDO;
import com.clougence.clouddm.sdk.security.auth.AuthElementType;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;

/**
 * @author bucketli 2020/12/8 15:21
 */
public interface RdpAuthServiceForManage {

    // for Basic Info
    AuthInfo getAuthLabel(String authLabelKey);

    List<AuthInfo> getRoleAuthLabel();

    List<AuthInfo> getDataAuthLabel();

    List<AuthInfo> getAllAuthLabel(AuthKind kindType);

    List<AuthInfo> getAllAuthLabelForAuthTreeDef(AuthKind kindType, AuthElementType elementType, DataSourceType dsType);

    List<AuthInfo> getAllCategory();

    List<AuthInfo> getCascadeAuthByLabel(String authLabel);

    List<String> normalizeRoleAuthLabels(List<String> authLabels);

    // for Commons
    List<RdpAuthObjectVO> listElements(String uid, List<String> levels, AuthKind authKind);

    List<RdpResAuthDO> listUserAuthWithoutLabels(String targetUid, AuthKind kind);

    List<RdpResAuthDO> listUserAuthByRes(String targetUid, long resId, List<String> authPrefixList, AuthKind authKind);

    void modifyUserAuth(String puid, ModifyUserAuthFO fo);

    void appendUserAuth(String puid, RdpAddAuthTicketFO fo);

    void clearAuthOfRes(long resId, AuthKind authKind);

    void clearAuthOfUser(String uid);

    boolean isResourceMangerEnable(String targetUid);
}
