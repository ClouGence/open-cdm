package com.clougence.clouddm.console.web.component.auth;

import java.util.List;

import com.clougence.rdp.controller.model.vo.RdpAuthObjectVO;
import com.clougence.clouddm.sdk.security.auth.AuthKind;

/**
 * @author bucketli 2020/12/8 15:21
 */
public interface DmAuthServiceForManage {

    List<RdpAuthObjectVO> listElements(String puid, String envId, AuthKind authKind);
}
