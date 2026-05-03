package com.clougence.rdp.service;

import java.util.List;

import com.clougence.rdp.service.enumeration.UserOperationType;
import com.clougence.rdp.service.model.UserConfigMO;

/**
 * @author mode 2023/11/24 10:24:16
 */
public interface RdpNotifyService {

    default void onDsAdd(String operatorUid, long dsId) {
    }

    default void onDsUpdate(long dsId) {
    }

    default void onDsDelete(long dsId) {
    }

    default void notifyUser(String curUID, String userUID, UserOperationType type) {
    }

    default void notifyUserConfig(String ownerUid, List<UserConfigMO> configList) {
    }
}
