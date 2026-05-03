# noinspection SqlNoDataSourceInspectionForFile

DELIMITER $$

DROP PROCEDURE if EXISTS proc_init;

$$
create procedure proc_init()
begin
    declare continue handler for 1060 begin
    end;
    declare continue handler for 1061 begin
    end;
    declare continue handler for 1062 begin
    end;
    declare continue handler for 1050 begin
    end;
    declare continue handler for 1072 begin
    end;
    declare continue handler for 1091 begin
    end;

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
            null, null, null, null, '183.134.161.226', '6258151610403310',
            null, 'CREATED', null, null, 0, 100);

    INSERT INTO `dm_worker_status` (`worker_conn_status`,`uid`,`worker_seq_number`,`console_ip`,`worker_ip`,`cluster_id`)
    VALUES ('NEW','6258151610403310','wsn582nm54ca045p014288w6e919ec6294m430h427619v64g0pyqzcjb5040q3f','172.31.239.3','172.31.239.4','1');

    INSERT INTO `dm_cluster` (id, gmt_create, gmt_modified, cluster_name, region, cloud_or_idc_name,
                              cluster_desc, uid)
    VALUES (1, now(), now(), 'cluster1aw2byj490', 'customer', 'SELF_MAINTENANCE', 'Default Cluster',
            '6258151610403310');

-- sample user and worker (end) --------------------
end
$$

call proc_init();