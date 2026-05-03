package migration.rdp.output;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import com.clougence.utils.ExceptionUtils;

public class V202412131642__add_activity extends BaseJavaMigration {

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
            // MySQL / OceanBase / TiDB
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
        sqls.add("create table `rdp_ticket_process_activity`\n" +
                "(\n" +
                "    id              bigint auto_increment primary key,\n" +
                "    gmt_create      datetime     default CURRENT_TIMESTAMP not null,\n" +
                "    gmt_modified    datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,\n" +
                "    activity_id     varchar(64)                            not null,\n" +
                "    process_id      bigint                                 not null,\n" +
                "    ticket_id       bigint                                 not null,\n" +
                "    activity_title  varchar(128)                           not null,\n" +
                "    context         text,\n" +
                "    deleted         tinyint(1)   default 0                 not null,\n" +
                "    order_number    int                                    not null\n" +
                ") ENGINE = InnoDB\n" +
                "  DEFAULT CHARSET = utf8mb4");

        sqls.add("create index idx_process_activity\n" +
                "    on `rdp_ticket_process_activity` (process_id, activity_id)");

        sqls.add("create index idx_ticket_id\n" +
                "    on `rdp_ticket_process_activity` (ticket_id)");

        sqls.add("ALTER TABLE `rdp_ticket_inst` add COLUMN `approval_url` text");

        sqls.add("delete from rdp_async_task where handler_name = 'rdpTicketAsyncTask'");
    }
}