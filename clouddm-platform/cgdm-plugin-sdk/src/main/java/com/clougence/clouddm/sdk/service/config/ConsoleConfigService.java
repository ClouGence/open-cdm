package com.clougence.clouddm.sdk.service.config;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.service.Service;

public interface ConsoleConfigService extends Service {

    List<ConfigData> fetchSettings(String ownerUid, List<String> names);

    Map<String, String> fetchSettingsMap(String ownerUid, List<String> names);

    UserData findUserByUID(String userUid);

    List<RoleData> findRoleByName(String ownerUid, String roleName);
}
