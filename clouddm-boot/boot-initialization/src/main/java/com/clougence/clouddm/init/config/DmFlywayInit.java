package com.clougence.clouddm.init.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Configuration
public class DmFlywayInit {

    @Value("${clouddm.mode.personal_desktop_version:false}")
    private boolean    desktopVersion;

    @Value("${spring.flyway.enabled:true}")
    private Boolean    flywayEnabled;

    @Value("${spring.flyway.baseline-on-migrate:true}")
    private Boolean    baselineOnMigrate;

    @Value("${spring.flyway.sql-migration-prefix:V}")
    private String     sqlMigrationPrefix;

    @Value("${spring.flyway.sql-migration-separator:__}")
    private String     sqlMigrationSeparator;

    @Value("${spring.flyway.sql-migration-suffixes:.java}")
    private String     sqlMigrationSuffixes;

    @Value("${spring.flyway.dm.baseline-description:<< ClouGence DM >>}")
    private String     dmBaselineDescription;

    @Value("${spring.flyway.dm.locations}")
    private String[]   dmLocations;

    @Value("${spring.flyway.dm.table}")
    private String     dmTable;

    @Value("${spring.flyway.rdp.baseline-description:<< ClouGence RDP >>}")
    private String     rdpBaselineDescription;

    @Value("${spring.flyway.rdp.locations}")
    private String[]   rdpLocations;

    @Value("${spring.flyway.rdp.table}")
    private String     rdpTable;

    @Resource
    private DataSource dataSource;

    public void doUpgrade() {
        if (!flywayEnabled || desktopVersion) {
            return;
        }

        String currentSchema = fetchCurrentSchema();
        if (StringUtils.isBlank(currentSchema)) {
            throw new RuntimeException("Fetch currentSchema is empty.");
        }

        doUpgrade("RDP", rdpLocations, rdpTable, rdpBaselineDescription, currentSchema, buildRdpFixSqls());
        doUpgrade("DM", dmLocations, dmTable, dmBaselineDescription, currentSchema, buildDmFixSqls());
    }

    private void doUpgrade(String name, String[] locations, String table, String baselineDescription, String currentSchema, List<String> fixSqls) {
        if (StringUtils.isNotBlank(sqlMigrationSuffixes) && sqlMigrationSuffixes.equals(".java") && !isInitial(currentSchema, table)) {
            log.info("Try to fix {} Flyway migration info.", name);
            fixMigInfo(table, fixSqls);
            log.info("{} Flyway migration info fixed.", name);
        }

        log.info("Start {} Flyway DB Upgrade.", name);
        Flyway flyway = Flyway.configure()
            .dataSource(dataSource)
            .locations(locations)
            .createSchemas(false)
            .defaultSchema(currentSchema)
            .baselineOnMigrate(baselineOnMigrate)
            .baselineDescription(baselineDescription)
            .sqlMigrationPrefix(sqlMigrationPrefix)
            .sqlMigrationSeparator(sqlMigrationSeparator)
            .sqlMigrationSuffixes(sqlMigrationSuffixes)
            .table(table)
            .outOfOrder(false)
            .load();
        try {
            flyway.migrate();
        } catch (Exception e) {
            String msg = "[DmFlywayInit] " + name + " Flyway DB Upgrade failed, msg : " + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg);
            throw e;
        }
        log.info("{} Flyway DB Upgrade Done.", name);
    }

    protected boolean isInitial(String currentSchema, String table) {
        try (Connection c = dataSource.getConnection()) {
            try (Statement s = c.createStatement();
                    ResultSet rs = s.executeQuery("select table_name from information_schema.tables where table_schema='" + currentSchema + "' and table_name='" + table + "'")) {
                return !rs.next();
            }
        } catch (SQLException e) {
            String msg = "Fix flyway migration info failed.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    private String fetchCurrentSchema() {
        try (Connection c = dataSource.getConnection()) {
            try (Statement s = c.createStatement(); ResultSet rs = s.executeQuery("select database()")) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        } catch (SQLException e) {
            String msg = "Fix flyway migration info failed.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }

        return null;
    }

    protected void fixMigInfo(String table, List<String> fixSqls) {
        try (Connection c = dataSource.getConnection(); Statement s = c.createStatement()) {
            for (String fixSql : fixSqls) {
                s.execute(fixSql.replace("${table}", table));
            }
        } catch (SQLException e) {
            String msg = "Fix flyway migration info failed.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    private List<String> buildDmFixSqls() {
        List<String> fixSqls = new ArrayList<>();
        fixSqls
            .add("update ${table} set description='init sql',type='JDBC',script='migration.dm.output.V202403121434__init_sql',checksum=NULL where version='202403121434' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='init data',type='JDBC',script='migration.dm.output.V202403131547__init_data',checksum=NULL where version='202403131547' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='team sql',type='JDBC',script='migration.dm.output.V202407011219__team_sql',checksum=NULL where version='202407011219' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='desensitive sql',type='JDBC',script='migration.dm.output.V202408141504__desensitive_sql',checksum=NULL where version='202408141504' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='hosttype sql',type='JDBC',script='migration.dm.output.V202408141516__hosttype_sql',checksum=NULL where version='202408141516' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='ticket info',type='JDBC',script='migration.dm.output.V202412131644__ticket_info',checksum=NULL where version='202412131644' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='ticket info add',type='JDBC',script='migration.dm.output.V202502211749__ticket_info_add',checksum=NULL where version='202502211749' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='sql auto exec',type='JDBC',script='migration.dm.output.V202503201711__sql_auto_exec',checksum=NULL where version='202503201711' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='devops',type='JDBC',script='migration.dm.output.V202503241417__devops',checksum=NULL where version='202503241417' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='sql audit',type='JDBC',script='migration.dm.output.V202506161058__sql_audit',checksum=NULL where version='202506161058' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='information cache',type='JDBC',script='migration.dm.output.V202507091000__information_cache',checksum=NULL where version='202507091000' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='export',type='JDBC',script='migration.dm.output.V202507151120__export',checksum=NULL where version='202507151120' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='add console heartbeat',type='JDBC',script='migration.dm.output.V202508051500__add_console_heartbeat',checksum=NULL where version='202508051500' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='must select',type='JDBC',script='migration.dm.output.V202508261013__must_select',checksum=NULL where version='202508261013' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='ticket',type='JDBC',script='migration.dm.output.V202509040955__ticket',checksum=NULL where version='202509040955' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='redis',type='JDBC',script='migration.dm.output.V202511071816__redis',checksum=NULL where version='202511071816' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='sqlrewrite',type='JDBC',script='migration.dm.output.V202511241837__sqlrewrite',checksum=NULL where version='202511241837' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='stream result',type='JDBC',script='migration.dm.output.V202512031553__stream_result',checksum=NULL where version='202512031553' and checksum IS NOT NULL");
        return fixSqls;
    }

    private List<String> buildRdpFixSqls() {
        List<String> fixSqls = new ArrayList<>();
        fixSqls
            .add("update ${table} set description='init sql',type='JDBC',script='migration.rdp.output.V202310252100__init_sql',checksum=NULL where version='202310252100' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='init data',type='JDBC',script='migration.rdp.output.V202404072200__init_data',checksum=NULL where version='202404072200' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='add sso',type='JDBC',script='migration.rdp.output.V202405071600__add_sso',checksum=NULL where version='202405071600' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='user last reset pwd',type='JDBC',script='migration.rdp.output.V202406031500__user_last_reset_pwd',checksum=NULL where version='202406031500' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='add rdp license',type='JDBC',script='migration.rdp.output.V202406151600__add_rdp_license',checksum=NULL where version='202406151600' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='add operation rdp ip',type='JDBC',script='migration.rdp.output.V202406201900__add_operation_rdp_ip',checksum=NULL where version='202406201900' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='dm order',type='JDBC',script='migration.rdp.output.V202407011219__dm_order',checksum=NULL where version='202407011219' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='audit optimize',type='JDBC',script='migration.rdp.output.V202407191714__audit_optimize',checksum=NULL where version='202407191714' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='add apply collect',type='JDBC',script='migration.rdp.output.V202408061600__add_apply_collect',checksum=NULL where version='202408061600' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='audit add name',type='JDBC',script='migration.rdp.output.V202408230939__audit_add_name',checksum=NULL where version='202408230939' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='dm license support',type='JDBC',script='migration.rdp.output.V202409111700__dm_license_support',checksum=NULL where version='202409111700' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='add log path',type='JDBC',script='migration.rdp.output.V202409191700__add_log_path',checksum=NULL where version='202409191700' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='ticket add env',type='JDBC',script='migration.rdp.output.V202409261136__ticket_add_env',checksum=NULL where version='202409261136' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='add sso type',type='JDBC',script='migration.rdp.output.V202410161422__add_sso_type',checksum=NULL where version='202410161422' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='add resource manage enable',type='JDBC',script='migration.rdp.output.V202410171417__add_resource_manage_enable',checksum=NULL where version='202410171417' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='add activity',type='JDBC',script='migration.rdp.output.V202412131642__add_activity',checksum=NULL where version='202412131642' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='init saas v2',type='JDBC',script='migration.rdp.output.V202412271400__init_saas_v2',checksum=NULL where version='202412271400' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='dm query ds count',type='JDBC',script='migration.rdp.output.V202503041400__dm_query_ds_count',checksum=NULL where version='202503041400' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='file src',type='JDBC',script='migration.rdp.output.V202503061400__file_src',checksum=NULL where version='202503061400' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='sub account sso',type='JDBC',script='migration.rdp.output.V202503070052__sub_account_sso',checksum=NULL where version='202503070052' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='auth ticket',type='JDBC',script='migration.rdp.output.V202503081353__auth_ticket',checksum=NULL where version='202503081353' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='oversea private',type='JDBC',script='migration.rdp.output.V202503242200__oversea_private',checksum=NULL where version='202503242200' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='check uuid',type='JDBC',script='migration.rdp.output.V202505190830__check_uuid',checksum=NULL where version='202505190830' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='devops',type='JDBC',script='migration.rdp.output.V202505200830__devops',checksum=NULL where version='202505200830' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='aws marketplace',type='JDBC',script='migration.rdp.output.V202506031930__aws_marketplace',checksum=NULL where version='202506031930' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='bugs 20250625',type='JDBC',script='migration.rdp.output.V202506261850__bugs_20250625',checksum=NULL where version='202506261850' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='mfa',type='JDBC',script='migration.rdp.output.V202507072130__mfa',checksum=NULL where version='202507072130' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='mysql ssl',type='JDBC',script='migration.rdp.output.V202507151111__mysql_ssl',checksum=NULL where version='202507151111' and checksum IS NOT NULL");
        fixSqls
            .add("update ${table} set description='saas managed',type='JDBC',script='migration.rdp.output.V202509152013__saas_managed',checksum=NULL where version='202509152013' and checksum IS NOT NULL");
        return fixSqls;
    }
}
