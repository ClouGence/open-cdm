package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;
import com.clougence.clouddm.init.constant.InitSeedConstants;

public class V202605070031__init_data extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                     INSERT INTO `dm_worker` (id, gmt_create, gmt_modified, cluster_id, worker_ip, cloud_or_idc_name, region,
                                                 worker_state,
                                                 physic_mem_mb, physic_core_num, physic_disk_gb, cpu_use_ratio,
                                                 mem_use_ratio, free_mem_mb, free_disk_gb, worker_load, schedule_ip, worker_name,
                                                 worker_seq_number,
                                                 worker_desc,
                                                 install_console_job_id, uninstall_console_job_id, upgrade_all_console_job_id,
                                                 deploy_status, external_ip, uid, console_job_id, life_cycle_state, install_or_upgrade_date,
                                                 install_or_upgrade_version, session_pool_use, session_pool_max)
                        VALUES (1, now(), now(), 1, '172.31.239.4', 'SELF_MAINTENANCE', 'customer', 'ONLINE', 0, 0, 0, 0, 0, 0,
                                0,
                                0,
                                '172.31.239.3', 'workers8c4qs80l26', 'wsn582nm54ca045p014288w6e919ec6294m430h427619v64g0pyqzcjb5040q3f',
                                'workers8c4qs80l26',
                                null, null, null, null, '183.134.161.226', '%s',
                                null, 'CREATED', null, null, 0, 100)\
                """.formatted(InitSeedConstants.ADMIN_UID), """
                    INSERT INTO `dm_worker_status` (`worker_conn_status`,`uid`,`worker_seq_number`,`console_ip`,`worker_ip`,`cluster_id`)
                        VALUES ('NEW','%s','wsn582nm54ca045p014288w6e919ec6294m430h427619v64g0pyqzcjb5040q3f','172.31.239.3','172.31.239.4','1')\
                """.formatted(InitSeedConstants.ADMIN_UID), """
                    INSERT INTO `dm_cluster` (id, gmt_create, gmt_modified, cluster_name, region, cloud_or_idc_name,
                                                  cluster_desc, uid)
                        VALUES (1, now(), now(), 'cluster1aw2byj490', 'customer', 'SELF_MAINTENANCE', 'Default Cluster',
                                '%s')\
                """.formatted(InitSeedConstants.ADMIN_UID));
    }
}
