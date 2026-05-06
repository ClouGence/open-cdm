package migration.dm.output;

import com.clougence.clouddm.init.constant.InitSeedConstants;
import com.clougence.utils.ExceptionUtils;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class V202403131547__init_data extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        Connection c = context.getConnection();
        for (String sql : sqls) {
            safeExecute(c, sql);
        }
    }

    private static final Set<Integer> errorCodes = new HashSet<>();
    static {
        errorCodes.add(1060);
        errorCodes.add(1061);
        errorCodes.add(1062);
        errorCodes.add(1050);
        errorCodes.add(1072);
        errorCodes.add(1091);
    }

    private void safeExecute(Connection conn, String sql) {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            // MySQL / OceanBase 错误码：
            // 1060 = Duplicate column name
            // 1050 = Table exists (for CREATE)
            // 1061 = Duplicate key name
            // 1091 = Can't DROP ... ; check that column/key exists
            if (errorCodes.contains(e.getErrorCode())) {
                System.out.println("Flyway java exec error but skip, msg:" + ExceptionUtils.getRootCauseMessage(e) + ", sql: " + sql);
                return;
            }

            throw new RuntimeException("Failed to execute: " + sql, e);
        }
    }

    private static final List<String> sqls = new ArrayList<>();
    static {
        sqls.add(" INSERT INTO `dm_worker` (id, gmt_create, gmt_modified, cluster_id, worker_ip, cloud_or_idc_name, region,\n" +
                "                             worker_state,\n" +
                "                             physic_mem_mb, physic_core_num, physic_disk_gb, cpu_use_ratio,\n" +
                "                             mem_use_ratio, free_mem_mb, free_disk_gb, worker_load, schedule_ip, worker_name,\n" +
                "                             worker_seq_number,\n" +
                "                             worker_desc,\n" +
                "                             install_console_job_id, uninstall_console_job_id, upgrade_all_console_job_id,\n" +
                "                             deploy_status, external_ip, uid, console_job_id, life_cycle_state, install_or_upgrade_date,\n" +
                "                             install_or_upgrade_version, session_pool_use, session_pool_max)\n" +
                "    VALUES (1, now(), now(), 1, '172.31.239.4', 'SELF_MAINTENANCE', 'customer', 'ONLINE', 0, 0, 0, 0, 0, 0,\n" +
                "            0,\n" +
                "            0,\n" +
                "            '172.31.239.3', 'workers8c4qs80l26', 'wsn582nm54ca045p014288w6e919ec6294m430h427619v64g0pyqzcjb5040q3f',\n" +
                "            'workers8c4qs80l26',\n" +
                "            null, null, null, null, '183.134.161.226', '" + InitSeedConstants.ADMIN_UID + "',\n" +
                "            null, 'CREATED', null, null, 0, 100)");

        sqls.add("INSERT INTO `dm_worker_status` (`worker_conn_status`,`uid`,`worker_seq_number`,`console_ip`,`worker_ip`,`cluster_id`)\n" +
                "    VALUES ('NEW','" + InitSeedConstants.ADMIN_UID + "','wsn582nm54ca045p014288w6e919ec6294m430h427619v64g0pyqzcjb5040q3f','172.31.239.3','172.31.239.4','1')");

        sqls.add("INSERT INTO `dm_cluster` (id, gmt_create, gmt_modified, cluster_name, region, cloud_or_idc_name,\n" +
                "                              cluster_desc, uid)\n" +
                "    VALUES (1, now(), now(), 'cluster1aw2byj490', 'customer', 'SELF_MAINTENANCE', 'Default Cluster',\n" +
                "            '" + InitSeedConstants.ADMIN_UID + "')");
    }
}