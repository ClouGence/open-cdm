package com.clougence.clouddm.console.web.component.auth;

import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.model.analysis.resource.DsResPath;

/**
 * @author bucketli 2024/2/21 09:40:00
 */
public interface DmAuthServiceForBiz {

    void checkResPath(String puid, String uid, long resID, AuthKind authKind, DsResPath resPath, String dataAuthLabel);

    void checkBrowseAuth(String puid, String uid, long resId, AuthKind authKind, DsResPath dsResource, String dataAuthLabel);

    boolean checkResPathWithoutError(String puid, String uid, long resID, AuthKind authKind, DsResPath resPath, String dataAuthLabel);

    boolean checkResPathChildrenWithoutError(String puid, String uid, long resID, AuthKind authKind, DsResPath resPath, String dataAuthLabel);

    boolean checkRoleAuthWithoutError(String puid, String uid, String roleAuthLabel);

    void checkResultFile(String puid, String uid, String fileUniqueId);
}
