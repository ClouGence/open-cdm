package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;
import com.clougence.clouddm.init.constant.InitSeedConstants;

public class V202605070029__saas_managed extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List
            .of("""
                        INSERT IGNORE INTO `rdp_role` (`id`, `gmt_create`, `gmt_modified`, `owner_uid`, `role_name`, `role_auth_labels`, `alias_name`, `inner_tag`)
                           VALUES (4, now(), now(), '%s', 'CC_SaaS_Developer', '["CC_DATAJOB_MANAGE","CC_DATAJOB_READ","CC_WORKER_READ","RDP_DS_READ","RDP_DS_MANAGE","RDP_ROLE_READ"]', 'SaaS 开发者角色', '1' )\
                    """
                .formatted(InitSeedConstants.ADMIN_UID));
    }
}
