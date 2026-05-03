package migration.dm.output;

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

public class V202503201711__sql_auto_exec extends BaseJavaMigration {

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
        sqls.add("   create table if not exists dm_auto_exec_job\n" +
                "    (\n" +
                "        id               bigint auto_increment primary key,\n" +
                "        gmt_create       datetime default CURRENT_TIMESTAMP not null,\n" +
                "        gmt_modified     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,\n" +
                "        data_source_id   bigint                             not null,\n" +
                "        levels           varchar(512)                       not null,\n" +
                "        uid              varchar(127)                       not null,\n" +
                "        primary_uid      varchar(127)                       not null,\n" +
                "        biz_id           varchar(128)                       not null,\n" +
                "        depend_on_biz_type         varchar(32)                        not null,\n" +
                "        depend_on_biz_id           varchar(127)                       null,\n" +
                "        query_id         varchar(128)                       null,\n" +
                "        status           varchar(32)                        not null,\n" +
                "        end_time         datetime                           null,\n" +
                "        last_report_time datetime                           null,\n" +
                "        worker_seq_number              varchar(255)                       null,\n" +
                "        config           text                               null,\n" +
                "        exec_type        varchar(32)                        not null,\n" +
                "        schedule_time    datetime                           not null,\n" +
                "        normal           tinyint(1)                         not null default 1\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("create unique index idx_depend_biz on dm_auto_exec_job (depend_on_biz_id,depend_on_biz_type)");

        sqls.add("create unique index idx_biz on dm_auto_exec_job (biz_id)");

        sqls.add("create table if not exists dm_auto_exec_task\n" +
                "    (\n" +
                "        id                bigint auto_increment primary key,\n" +
                "        gmt_create        datetime default CURRENT_TIMESTAMP not null,\n" +
                "        gmt_modified      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,\n" +
                "        biz_id            varchar(128)                       not null,\n" +
                "        auto_exec_job_id  bigint                             not null,\n" +
                "        exec_order        int                                not null,\n" +
                "        exec_sql          text                               not null,\n" +
                "        gmt_last_start    datetime                           null,\n" +
                "        gmt_last_end      datetime                           null,\n" +
                "        status            varchar(32)                        not null,\n" +
                "        affect_row        bigint                             null,\n" +
                "        sql_type          varchar(32)                        not null,\n" +
                "        transactional_group int               default 0      not null,\n" +
                "        exec_count        int                 default 0      not null\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("create index idx_sql_job_id\n" +
                "        on dm_auto_exec_task (auto_exec_job_id)");

        sqls.add("create unique index idx_biz on dm_auto_exec_task (biz_id)");

        sqls.add("create table if not exists dm_biz_log\n" +
                "    (\n" +
                "        id             bigint auto_increment primary key,\n" +
                "        gmt_create     datetime default CURRENT_TIMESTAMP not null,\n" +
                "        gmt_modified   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,\n" +
                "        depend_on_biz_id  varchar(128)                             not null,\n" +
                "        depend_on_biz_type  varchar(128)                             not null,\n" +
                "        content        text                               not null,\n" +
                "        log_level      varchar(32)                      not null\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("create index idx_depend_biz on dm_biz_log (depend_on_biz_id,depend_on_biz_type)");

        sqls.add("drop table data_export_job");

        sqls.add("drop table data_export_task");
    }
}