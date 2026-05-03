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

public class V202407011219__team_sql extends BaseJavaMigration {

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
        sqls.add("alter table `dm_ds_session`\n" +
                "        add session_type varchar(16) null");

        sqls.add("alter table `dm_ds_session`\n" +
                "        modify datasource_id varchar(255) null");

        sqls.add("alter table `dm_ds_session`\n" +
                "        add `attach` text null");

        sqls.add("alter table `dm_ds_session`\n" +
                "        add `last_query_time` datetime null");

        sqls.add("alter table `dm_ds_session`\n" +
                "        drop `tx`");

        sqls.add("CREATE TABLE IF NOT EXISTS `dm_async_task`\n" +
                "    (\n" +
                "        `id`                 bigint(20)   NOT NULL AUTO_INCREMENT,\n" +
                "        `gmt_create`         datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `gmt_modified`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `title`              varchar(256) NOT NULL,\n" +
                "        `description`        text         NULL,\n" +
                "        `biz_id`             varchar(128) NULL,\n" +
                "        `biz_type`           varchar(64)  NULL,\n" +
                "        `console_ip`         varchar(32)  NULL,\n" +
                "        `depend_on_biz_id`   varchar(128) NULL,\n" +
                "        `depend_on_biz_type` varchar(64)  NULL,\n" +
                "        `owner_uid`          varchar(255) NOT NULL,\n" +
                "        `handler_name`       text         NOT NULL,\n" +
                "        `handler_type`       text         NOT NULL,\n" +
                "        `config_data`        text         NULL,\n" +
                "        `show_in_dock`       tinyint      NOT NULL DEFAULT 0,\n" +
                "        `process_type`       varchar(64)  NOT NULL DEFAULT 'SCROLL',\n" +
                "        `process_value`      bigint       NOT NULL DEFAULT 0,\n" +
                "        `fast_fail`          tinyint      NOT NULL DEFAULT 0,\n" +
                "        `status`             varchar(32)  NOT NULL,\n" +
                "        `status_msg`         text         NULL,\n" +
                "        `time_of_start`      datetime     NULL,\n" +
                "        `time_of_last`       datetime     NULL,\n" +
                "        `time_of_finish`     datetime     NULL,\n" +
                "        PRIMARY KEY (`id`),\n" +
                "        index dm_async_task_owner_uid (`owner_uid`),\n" +
                "        unique dm_async_task_biz_idx (`biz_id`, `biz_type`)\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("create table if not exists `dm_sec_spec`\n" +
                "    (\n" +
                "        `id`           bigint(20)   not null auto_increment,\n" +
                "        `gmt_create`   datetime     not null default current_timestamp,\n" +
                "        `gmt_modified` datetime     not null default current_timestamp,\n" +
                "        `owner_uid`    varchar(255) not null,\n" +
                "        `spec_name`    varchar(255) not null,\n" +
                "        `description`  text         null,\n" +
                "        `enable`       tinyint      not null default 0,\n" +
                "        primary key (`id`),\n" +
                "        index dm_sec_spec_owner_uid (`owner_uid`)\n" +
                "    ) engine = innodb\n" +
                "      default charset = utf8mb4");

        sqls.add("create table if not exists `dm_sec_rules`\n" +
                "    (\n" +
                "        `id`           bigint(20)   not null auto_increment,\n" +
                "        `gmt_create`   datetime     not null default current_timestamp,\n" +
                "        `gmt_modified` datetime     not null default current_timestamp,\n" +
                "        `owner_uid`    varchar(255) not null,\n" +
                "        `rule_name`    varchar(255) not null,\n" +
                "        `rule_desc`    text,\n" +
                "        `ds_range`     text         null,\n" +
                "        `rule_target`  varchar(64)  not null,\n" +
                "        `rule_type`    varchar(64)  not null,\n" +
                "        `rule_def`     text,\n" +
                "        `rule_content` text,\n" +
                "        `inner_share`  tinyint      not null default 0,\n" +
                "        primary key (`id`),\n" +
                "        index dm_sec_rules_owner_uid (`owner_uid`)\n" +
                "    ) engine = innodb\n" +
                "      default charset = utf8mb4");

        sqls.add("create table if not exists `dm_sec_referer`\n" +
                "    (\n" +
                "        `id`           bigint(20)   not null auto_increment,\n" +
                "        `gmt_create`   datetime     not null default current_timestamp,\n" +
                "        `gmt_modified` datetime     not null default current_timestamp,\n" +
                "        `owner_uid`    varchar(255) not null,\n" +
                "        `enable`       tinyint      not null,\n" +
                "        `ref_rule`     bigint       not null,\n" +
                "        `ref_spec`     bigint       not null,\n" +
                "        `warn_level`   varchar(64),\n" +
                "        `rule_param`   text,\n" +
                "        primary key (`id`),\n" +
                "        index dm_sec_referer_owner_uid (`owner_uid`),\n" +
                "        index dm_sec_referer_refs (`ref_rule`, `ref_spec`)\n" +
                "    ) engine = innodb\n" +
                "      default charset = utf8mb4");

        sqls.add("CREATE TABLE `dm_ticket_details_inst`\n" +
                "    (\n" +
                "        `id`                     bigint       NOT NULL AUTO_INCREMENT,\n" +
                "        `gmt_create`             datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `gmt_modified`           datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                "        `rdp_ticket_ins_id`      varchar(64)  NOT NULL COMMENT 'The corresponding ID in the RDP work order table',\n" +
                "        `session_id`             varchar(255)          DEFAULT NULL COMMENT 'Session will only be created after entering the automatic execution phase',\n" +
                "        `risk_sql_count`         int                   DEFAULT NULL COMMENT 'Risk SQL quantity',\n" +
                "        `raw_sql`                longtext COMMENT 'Unresolved original SQL content submitted by users',\n" +
                "        `explain_sql_data`       longtext,\n" +
                "        `total_count`            mediumtext COMMENT 'The total number of SQL statements in the execution process',\n" +
                "        `expected_affected_rows` bigint                DEFAULT NULL COMMENT 'Expected impact on the number of rows',\n" +
                "        `expected_exec_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'If executed immediately, it defaults to the time when the work order was generated. If it is found that the expected execution time is less than the current execution time after approval, execute immediately',\n" +
                "        `immediately`            tinyint(1)            DEFAULT NULL COMMENT 'Do you want to execute it immediately',\n" +
                "        `ddl_sql_exec_type`      varchar(128) NOT NULL DEFAULT 'DIRECT',\n" +
                "        `none_ddl_sql_exec_type` varchar(128) NOT NULL DEFAULT 'DIRECT',\n" +
                "        `roll_back_sql`          longtext,\n" +
                "        `deleted`                tinyint(1)   NOT NULL DEFAULT '0',\n" +
                "        PRIMARY KEY (`id`),\n" +
                "        UNIQUE KEY `idx_unique_biz_id` (`rdp_ticket_ins_id`)\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("drop table `checker_parameter`");

        sqls.add("drop table `checker_template`");

        sqls.add("drop table `console_job`");

        sqls.add("drop table `console_task`");

        sqls.add("drop table `sql_process`");

        sqls.add("drop table `ticket_process`");

        sqls.add("drop table `dm_ticket_inst`");

        sqls.add("drop table `ds_appro_template`");

        sqls.add("    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content, inner_share)\n" +
                "    VALUES (1, '', '限制库的排序规则',\n" +
                "            '对于排序规则选项建库语句必须设置为 \\'collate #{collate}\\'，对于修改库语句若出现该选项也必须设置为 \\'collate #{collate}\\'',\n" +
                "            '[\"MySQL\"]', 'Catalog', 'DetectRules',\n" +
                "            '[{\"name\":\"require\",\"type\":\"bool\",\"defaultValue\":\"true\",\"range\":[\"true\",\"false\"],\"hint\":\"强制要求设置排序规则\"},{\"name\":\"collate\",\"type\":\"string\",\"defaultValue\":\"utf8mb4_general_ci\",\"range\":[\"utf8_general_ci\",\"utf8_bin\",\"utf8mb4_general_ci\",\"utf8mb4_bin\"],\"hint\":\"允许使用的排序规则\"}]', '#define \"require\" as bool\n" +
                "        default \"true\"\n" +
                "        enum [\"true\", \"false\"]\n" +
                "        hint \"强制要求设置排序规则\"\n" +
                "#define \"collate\" as string\n" +
                "        default \"utf8mb4_unicode_ci\"\n" +
                "        enum [\"utf8_unicode_ci\", \"utf8mb4_unicode_ci\"]\n" +
                "        hint \"允许使用的排序规则\"\n" +
                "\n" +
                "// 对于 alert 语句，只有当设置了 collate 选项时候才进行校验\n" +
                "if\n" +
                "  @domain.sqlType in [\\'ALERT_CATALOG\\'] and\n" +
                "  @func.string.isNotBlank(@domain.collate)\n" +
                "then\n" +
                "\n" +
                "    if @func.string.isBlank(#{collate}) then\n" +
                "      return false //规则配置错误\n" +
                "    else\n" +
                "      return @func.string.equalsIgnoreCase(@domain.collate, #{collate})\n" +
                "    end\n" +
                "\n" +
                "end\n" +
                "\n" +
                "// 对于 create 语句，检测是否必须设置 collate 选项\n" +
                "if @domain.sqlType == \\'CREATE_CATALOG\\' then\n" +
                "\n" +
                "  if cast(#{require} as bool) then\n" +
                "\n" +
                "    if @func.string.isBlank(#{collate}) then\n" +
                "      return false //规则配置错误\n" +
                "    else\n" +
                "      return @func.string.equalsIgnoreCase(@domain.collate, #{collate})\n" +
                "    end\n" +
                "\n" +
                "  else\n" +
                "\n" +
                "    if @func.string.isBlank(@domain.collate) then\n" +
                "      return true\n" +
                "    else\n" +
                "      return @func.string.equalsIgnoreCase(@domain.collate, #{collate})\n" +
                "    end\n" +
                "\n" +
                "  end\n" +
                "\n" +
                "end', 1)");

        sqls.add("INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target,\n" +
                "                              rule_type, rule_def, rule_content, inner_share)\n" +
                "    VALUES (2, '', '限制库的字符集',\n" +
                "            '对于字符集选项建库语句必须设置为 \\'character set #{character_set}\\'，对于修改库语句若出现该选项也必须设置为 \\'character set  #{character_set}\\'',\n" +
                "            '[\"MySQL\"]', 'Catalog', 'DetectRules',\n" +
                "            '[{\"name\":\"require\",\"type\":\"bool\",\"defaultValue\":\"true\",\"range\":[\"true\",\"false\"],\"hint\":\"强制要求设置字符集\"},{\"name\":\"character_set\",\"type\":\"string\",\"defaultValue\":\"utf8mb4\",\"range\":[\"utf8\",\"utf8mb4\"],\"hint\":\"允许使用的字符集\"}]', '#define \"require\" as bool\n" +
                "        default \"true\"\n" +
                "        enum [\"true\", \"false\"]\n" +
                "        hint \"强制要求设置字符集\"\n" +
                "#define \"character_set\" as string\n" +
                "        default \"utf8mb4\"\n" +
                "        enum [\"utf8\", \"utf8mb4\"]\n" +
                "        hint \"允许使用的字符集\"\n" +
                "\n" +
                "// 对于 alert 语句，只有当设置了 character set 选项时候才进行校验\n" +
                "if\n" +
                "  @domain.sqlType in [\\'ALERT_CATALOG\\'] and\n" +
                "  @func.string.isNotBlank(@domain.characterSet)\n" +
                "then\n" +
                "\n" +
                "    if @func.string.isBlank(#{character_set}) then\n" +
                "      return false //规则配置错误\n" +
                "    else\n" +
                "      return @func.string.equalsIgnoreCase(@domain.characterSet, #{character_set})\n" +
                "    end\n" +
                "\n" +
                "end\n" +
                "\n" +
                "// 对于 create 语句，检测是否必须设置 character set 选项\n" +
                "if @domain.sqlType == \\'CREATE_CATALOG\\' then\n" +
                "\n" +
                "  if cast(#{require} as bool) then\n" +
                "\n" +
                "    if @func.string.isBlank(#{character_set}) then\n" +
                "      return false //规则配置错误\n" +
                "    else\n" +
                "      return @func.string.equalsIgnoreCase(@domain.characterSet, #{character_set})\n" +
                "    end\n" +
                "\n" +
                "  else\n" +
                "\n" +
                "    if @func.string.isBlank(@domain.characterSet) then\n" +
                "      return true\n" +
                "    else\n" +
                "      return @func.string.equalsIgnoreCase(@domain.characterSet, #{character_set})\n" +
                "    end\n" +
                "\n" +
                "  end\n" +
                "\n" +
                "end', 1)");

        sqls.add("INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content,\n" +
                "                              inner_share)\n" +
                "    VALUES (3, '', '限制建表自增初始值', '在建表语句中使用了 auto_increment 选项，自增属性需要设置为 #{number}',\n" +
                "            '[\"MySQL\"]',\n" +
                "            'Table', 'DetectRules',\n" +
                "            '[{\"name\":\"require\",\"type\":\"bool\",\"defaultValue\":\"false\",\"range\":[\"true\",\"false\"],\"hint\":\"强制要求设置自增初始值\"},{\"name\":\"number\",\"type\":\"int\",\"defaultValue\":\"1\",\"range\":null,\"hint\":\"建表自增初始值\"}]', '#define \"require\" as bool\n" +
                "        default \"false\"\n" +
                "        enum [\"true\", \"false\"]\n" +
                "        hint \"强制要求设置自增初始值\"\n" +
                "#define \"number\" as int\n" +
                "        default \"1\"\n" +
                "        hint \"建表自增初始值\"\n" +
                "\n" +
                "if @domain.sqlType != \\'CREATE_TABLE\\' then\n" +
                "  return true\n" +
                "end\n" +
                "\n" +
                "if @func.string.isBlank(@domain.options.auto_increment) then\n" +
                "\n" +
                "  return !cast(#{require} as bool)\n" +
                "\n" +
                "else\n" +
                "\n" +
                "  if @func.string.isBlank(#{number}) then\n" +
                "    return false // 规则配置错误\n" +
                "  else\n" +
                "    return cast(@domain.options.auto_increment as int) == cast(#{number} as int)\n" +
                "  end\n" +
                "\n" +
                "end', 1)");

        sqls.add("INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content,\n" +
                "                              inner_share)\n" +
                "    VALUES (4, '', '限制列的数量', '在 create table 语句中限制列的数量最多为 #{maxCount} 个', '[\"MySQL\"]', 'Table',\n" +
                "            'DetectRules',\n" +
                "            '[{\"name\":\"maxCount\",\"type\":\"int\",\"defaultValue\":\"50\",\"range\":null,\"hint\":\"表最大自段数量\"}]', '#define \"maxCount\" as int\n" +
                "        default \"50\"\n" +
                "        hint \"表最大自段数量\"\n" +
                "\n" +
                "if\n" +
                "  @domain.sqlType != \\'CREATE_TABLE\\'\n" +
                "then\n" +
                "  return true\n" +
                "end\n" +
                "return @func.array.size(@domain.columns) <= cast(#{maxCount} as int)', 1)");

        sqls.add("INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content,\n" +
                "                              inner_share)\n" +
                "    VALUES (5, '', '表名拼写规则', '表名需要满足 #{caseType} 拼写规则', '[\"MySQL\"]', 'Table', 'DetectRules',\n" +
                "            '[{\"name\":\"caseType\",\"type\":\"string\",\"defaultValue\":\"Lower case\",\"range\":[\"Lower case\",\"Upper case\",\"Lower camel case\",\"Upper camel case\"],\"hint\":\"表名拼写规则\"}]', '#define \"caseType\" as string\n" +
                "        default \"Lower case\"\n" +
                "        enum [\"Lower case\", \"Upper case\", \"Lower camel case\", \"Upper camel case\"]\n" +
                "        hint \"表名拼写规则\"\n" +
                "\n" +
                "if @domain.sqlType in [\\'CREATE_TABLE\\', \\'CREATE_TABLE_SELECT\\', \\'CREATE_TABLE_LIKE\\'] then\n" +
                "  checkName = @domain.table\n" +
                "elseif @domain.sqlType in [\\'RENAME_TABLE\\', \\'ALERT_TABLE_RENAME\\'] then\n" +
                "  checkName = @domain.newName\n" +
                "else\n" +
                "  return true\n" +
                "end\n" +
                "\n" +
                "// ref document https://newbedev.com/regex-for-pascalcased-words-aka-camelcased-with-leading-uppercase-letter\n" +
                "\n" +
                "if #{caseType} == \\'Lower case\\' then\n" +
                "  return @func.string.lowerCase(checkName) == checkName\n" +
                "\n" +
                "elseif #{caseType} == \\'Upper case\\' then\n" +
                "\n" +
                "  return @func.string.upperCase(checkName) == checkName\n" +
                "\n" +
                "elseif #{caseType} == \\'Lower camel case\\' then\n" +
                "  return checkName matches \\'[a-z]+((\\\\d)|([A-Z0-9][a-z0-9]+))*([A-Z])?\\'\n" +
                "\n" +
                "elseif #{caseType} == \\'Upper camel case\\' then\n" +
                "  return checkName matches \\'([A-Z][a-z0-9]+)((\\\\d)|([A-Z0-9][a-z0-9]+))*([A-Z])?\\'\n" +
                "\n" +
                "else\n" +
                "\n" +
                "  return false\n" +
                "end', 1)");

        sqls.add("INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content,\n" +
                "                              inner_share)\n" +
                "    VALUES (6, '', '表必须有注释',\n" +
                "            '在 create table 语句中要求必须有注释，对于 alert table 语句如果存在设置注释选项必须不能为空', '[\"MySQL\"]',\n" +
                "            'Table', 'DetectRules', '[]', 'if\n" +
                "  @domain.sqlType == \\'CREATE_TABLE\\'\n" +
                "then\n" +
                "  return @func.string.isNotBlank(@domain.comment)\n" +
                "end\n" +
                "\n" +
                "if\n" +
                "  @domain.sqlType == \\'ALERT_TABLE\\' and\n" +
                "  @domain.comment != null\n" +
                "then\n" +
                "  return @func.string.isNotBlank(@domain.comment)\n" +
                "end', 1)");

        sqls.add("INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content,\n" +
                "                              inner_share)\n" +
                "    VALUES (7, '', '表必须有主键', '在 create table 建表语句中必须指定主键列', '[\"MySQL\"]', 'Table', 'DetectRules',\n" +
                "            '[]', 'if\n" +
                "  @domain.sqlType == \\'CREATE_TABLE\\'\n" +
                "then\n" +
                "  return @domain.hasPrimary\n" +
                "end', 1)");

        sqls.add("INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content,\n" +
                "                              inner_share)\n" +
                "    VALUES (8, '', '表名不能是关键字',\n" +
                "            '不能使用关键字作为表名，关键字清单可参考官方文档：https://dev.mysql.com/doc/refman/8.4/en/keywords.html',\n" +
                "            '[\"MySQL\"]', 'Table', 'DetectRules', '[]', 'if\n" +
                "  @domain.sqlType not in [\\'CREATE_TABLE\\', \\'RENAME_TABLE\\', \\'ALERT_TABLE_RENAME\\']\n" +
                "then\n" +
                "  return true\n" +
                "end\n" +
                "\n" +
                "return @func.string.upperCase((@domain.sqlType == \\'CREATE_TABLE\\'? @domain.table : @domain.newName)) not in\n" +
                "    [\\'ACCESSIBLE\\',\\'ADD\\',\\'ALL\\',\\'ALTER\\',\\'ANALYZE\\',\\'AND\\',\\'AS\\',\\'ASC\\',\\'ASENSITIVE\\',\n" +
                "     \\'BEFORE\\',\\'BETWEEN\\',\\'BIGINT\\',\\'BINARY\\',\\'BLOB\\',\\'BOTH\\',\\'BY\\',\n" +
                "     \\'CALL\\',\\'CASCADE\\',\\'CASE\\',\\'CHANGE\\',\\'CHAR\\',\\'CHARACTER\\',\\'CHECK\\',\\'COLLATE\\',\\'COLUMN\\',\\'CONDITION\\',\\'CONSTRAINT\\',\\'CONTINUE\\',\n" +
                "     \\'CONVERT\\',\\'CREATE\\',\\'CROSS\\',\\'CURRENT_DATE\\',\\'CURRENT_TIME\\',\\'CURRENT_TIMESTAMP\\',\\'CURRENT_USER\\',\\'CURSOR\\',\n" +
                "     \\'DATABASE\\',\\'DATABASES\\',\\'DAY_HOUR\\',\\'DAY_MICROSECOND\\',\\'DAY_MINUTE\\',\\'DAY_SECOND\\',\\'DEC\\',\\'DECIMAL\\',\\'DECLARE\\',\\'DEFAULT\\',\n" +
                "     \\'DELAYED\\',\\'DELETE\\',\\'DESC\\',\\'DESCRIBE\\',\\'DETERMINISTIC\\',\\'DISTINCT\\',\\'DISTINCTROW\\',\\'DIV\\',\\'DOUBLE\\',\\'DROP\\',\\'DUAL\\',\n" +
                "     \\'EACH\\',\\'ELSE\\',\\'ELSEIF\\',\\'ENCLOSED\\',\\'ESCAPED\\',\\'EXISTS\\',\\'EXIT\\',\\'EXPLAIN\\',\n" +
                "     \\'FALSE\\',\\'FETCH\\',\\'FLOAT\\',\\'FLOAT4\\',\\'FLOAT8\\',\\'FOR\\',\\'FORCE\\',\\'FOREIGN\\',\\'FROM\\',\\'FULLTEXT\\',\\'GENERATED\\',\\'GET\\',\\'GRANTint\\',\\'GROUP\\',\n" +
                "     \\'HAVING\\',\\'HIGH_PRIORITY\\',\\'HOUR_MICROSECOND\\',\\'HOUR_MINUTE\\',\\'HOUR_SECOND\\',\\'IF\\',\\'IGNORE\\',\\'IN\\',\\'INDEX\\',\\'INFILE\\',\n" +
                "     \\'INNER\\',\\'INOUT\\',\\'INSENSITIVE\\',\\'INSERT\\',\\'INT\\',\\'INT1\\',\\'INT2\\',\\'INT3\\',\\'INT4\\',\\'INT8\\',\\'INTEGER\\',\\'INTERVALint\\',\\'INTO\\',\n" +
                "     \\'IO_AFTER_GTIDS\\',\\'IO_BEFORE_GTIDS\\',\\'IS\\',\\'ITERATE\\',\\'JOIN\\',\\'KEY\\',\\'KEYS\\',\\'KILL\\',\\'LEADING\\',\\'LEAVE\\',\\'LEFT\\',\\'LIKE\\',\n" +
                "     \\'LIMIT\\',\\'LINEAR\\',\\'LINES\\',\\'LOAD\\',\\'LOCALTIME\\',\\'LOCALTIMESTAMP\\',\\'LOCK\\',\\'LONG\\',\\'LONGBLOB\\',\\'LONGTEXT\\',\\'LOOP\\',\\'LOW_PRIORITY\\',\n" +
                "     \\'MASTER_BIND\\',\\'MASTER_SSL_VERIFY_SERVER_CERT\\',\\'MATCH\\',\\'MAXVALUE\\',\\'MEDIUMBLOB\\',\\'MEDIUMINT\\',\\'MEDIUMTEXT\\',\\'MIDDLEINT\\',\n" +
                "     \\'MINUTE_MICROSECOND\\',\\'MINUTE_SECOND\\',\\'MOD\\',\\'MODIFIES\\',\\'NATURAL\\',\\'NOT\\',\\'NO_WRITE_TO_BINLOG\\',\\'NULL\\',\\'NUMERIC\\',\n" +
                "     \\'ON\\',\\'OPTIMIZE\\',\\'OPTIMIZER_COSTS\\',\\'OPTION\\',\\'OPTIONALLY\\',\\'OR\\',\\'ORDER\\',\\'OUT\\',\\'OUTER\\',\\'OUTFILE\\',\\'PARTITION\\',\\'PRECISION\\',\n" +
                "     \\'PRIMARY\\',\\'PROCEDURE\\',\\'PURGE\\',\\'RANGE\\',\\'READ\\',\\'READS\\',\\'READ_WRITE\\',\\'REAL\\',\\'REFERENCES\\',\\'REGEXP\\',\\'RELEASE\\',\\'RENAME\\',\n" +
                "     \\'REPEAT\\',\\'REPLACE\\',\\'REQUIRE\\',\\'RESIGNAL\\',\\'RESTRICT\\',\\'RETURN\\',\\'REVOKE\\',\\'RIGHT\\',\\'RLIKE\\',\\'SCHEMA\\',\\'SCHEMAS\\',\n" +
                "     \\'SECOND_MICROSECOND\\',\\'SELECT\\',\\'SENSITIVE\\',\\'SEPARATOR\\',\\'SET\\',\\'SHOW\\',\\'SIGNAL\\',\\'SMALLINT\\',\\'SPATIAL\\',\\'SPECIFIC\\',\n" +
                "     \\'SQL\\',\\'SQLEXCEPTION\\',\\'SQLSTATE\\',\\'SQLWARNING\\',\\'SQL_BIG_RESULT\\',\\'SQL_CALC_FOUND_ROWS\\',\\'SQL_SMALL_RESULT\\',\\'SSL\\',\n" +
                "     \\'STARTING\\',\\'STORED\\',\\'STRAIGHT_JOIN\\',\\'TABLE\\',\\'TERMINATED\\',\\'THEN\\',\\'TINYBLOB\\',\\'TINYINT\\',\\'TINYTEXT\\',\\'TO\\',\\'TRAILING\\',\n" +
                "     \\'TRIGGER\\',\\'TRUE\\',\\'UNDO\\',\\'UNION\\',\\'UNIQUE\\',\\'UNLOCK\\',\\'UNSIGNED\\',\\'UPDATE\\',\\'USAGE\\',\\'USE\\',\\'USING\\',\\'UTC_DATE\\',\\'UTC_TIME\\',\n" +
                "     \\'UTC_TIMESTAMP\\',\\'VALUES\\',\\'VARBINARY\\',\\'VARCHAR\\',\\'VARCHARACTER\\',\\'VARYING\\',\\'VIRTUAL\\',\\'WHEN\\',\\'WHERE\\',\\'WHILE\\',\\'WITH\\',\n" +
                "     \\'WRITE\\',\\'XOR\\',\\'YEAR_MONTH\\',\\'ZEROFILL\\']', 1)");

        sqls.add("INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content,\n" +
                "                              inner_share)\n" +
                "    VALUES (9, '', '限制表的字符集',\n" +
                "            '对于字符集选项 create table 语句必须设置为 \\'character set #{character_set}\\'，对于 alter table 语句若出现该选项也必须设置为 \\'character set #{character_set}\\'',\n" +
                "            '[\"MySQL\"]', 'Table', 'DetectRules',\n" +
                "            '[{\"name\":\"require\",\"type\":\"bool\",\"defaultValue\":\"true\",\"range\":[\"true\",\"false\"],\"hint\":\"强制要求设置字符集\"},{\"name\":\"character_set\",\"type\":\"string\",\"defaultValue\":\"utf8mb4\",\"range\":[\"utf8\",\"utf8mb4\"],\"hint\":\"允许使用的字符集\"}]', '#define \"require\" as bool\n" +
                "        default \"true\"\n" +
                "        enum [\"true\", \"false\"]\n" +
                "        hint \"强制要求设置字符集\"\n" +
                "#define \"character_set\" as string\n" +
                "        default \"utf8mb4\"\n" +
                "        enum [\"utf8\", \"utf8mb4\"]\n" +
                "        hint \"允许使用的字符集\"\n" +
                "\n" +
                "// 对于 alert 语句，只有当设置了 character set 选项时候才进行校验\n" +
                "if\n" +
                "  @domain.sqlType in [\\'ALERT_TABLE\\', \\'ALERT_TABLE_CONVERT\\'] and\n" +
                "  @func.string.isNotBlank(@domain.characterSet)\n" +
                "then\n" +
                "\n" +
                "    if @func.string.isBlank(#{character_set}) then\n" +
                "      return false //规则配置错误\n" +
                "    else\n" +
                "      return @func.string.equalsIgnoreCase(@domain.characterSet, #{character_set})\n" +
                "    end\n" +
                "\n" +
                "end\n" +
                "\n" +
                "\n" +
                "// 对于 create 语句，检测是否必须设置 character set 选项\n" +
                "if @domain.sqlType == \\'CREATE_TABLE\\' then\n" +
                "\n" +
                "  if cast(#{require} as bool) then\n" +
                "\n" +
                "    if @func.string.isBlank(#{character_set}) then\n" +
                "      return false //规则配置错误\n" +
                "    else\n" +
                "      return @func.string.equalsIgnoreCase(@domain.characterSet, #{character_set})\n" +
                "    end\n" +
                "\n" +
                "  else\n" +
                "\n" +
                "    if @func.string.isBlank(@domain.characterSet) then\n" +
                "      return true\n" +
                "    else\n" +
                "      return @func.string.equalsIgnoreCase(@domain.characterSet, #{character_set})\n" +
                "    end\n" +
                "\n" +
                "  end\n" +
                "\n" +
                "end', 1)");

        sqls.add("INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content,\n" +
                "                              inner_share)\n" +
                "    VALUES (10, '', '限制表的排序规则',\n" +
                "            '对于排序规则选项 create table 语句必须设置为 \\'collate #{collate}\\'，对于 alter table 语句若出现该选项也必须设置为 \\'collate #{collate}\\'',\n" +
                "            '[\"MySQL\"]', 'Table', 'DetectRules',\n" +
                "            '[{\"name\":\"require\",\"type\":\"bool\",\"defaultValue\":\"true\",\"range\":[\"true\",\"false\"],\"hint\":\"强制要求设置排序规则\"},{\"name\":\"collate\",\"type\":\"string\",\"defaultValue\":\"utf8mb4_unicode_ci\",\"range\":[\"utf8_unicode_ci\",\"utf8mb4_unicode_ci\"],\"hint\":\"允许使用的排序规则\"}]', '#define \"require\" as bool\n" +
                "        default \"true\"\n" +
                "        enum [\"true\", \"false\"]\n" +
                "        hint \"强制要求设置排序规则\"\n" +
                "#define \"collate\" as string\n" +
                "        default \"utf8mb4_unicode_ci\"\n" +
                "        enum [\"utf8_unicode_ci\", \"utf8mb4_unicode_ci\"]\n" +
                "        hint \"允许使用的排序规则\"\n" +
                "\n" +
                "// 对于 alert 语句，只有当设置了 collate 选项时候才进行校验\n" +
                "if\n" +
                "  @domain.sqlType in [\\'ALERT_TABLE\\', \\'ALERT_TABLE_CONVERT\\'] and\n" +
                "  @func.string.isNotBlank(@domain.collate)\n" +
                "then\n" +
                "\n" +
                "    if @func.string.isBlank(#{collate}) then\n" +
                "      return false //规则配置错误\n" +
                "    else\n" +
                "      return @func.string.equalsIgnoreCase(@domain.collate, #{collate})\n" +
                "    end\n" +
                "\n" +
                "end\n" +
                "\n" +
                "\n" +
                "// 对于 create 语句，检测是否必须设置 collate 选项\n" +
                "if @domain.sqlType == \\'CREATE_TABLE\\' then\n" +
                "\n" +
                "  if cast(#{require} as bool) then\n" +
                "\n" +
                "    if @func.string.isBlank(#{collate}) then\n" +
                "      return false //规则配置错误\n" +
                "    else\n" +
                "      return @func.string.equalsIgnoreCase(@domain.collate, #{collate})\n" +
                "    end\n" +
                "\n" +
                "  else\n" +
                "\n" +
                "    if @func.string.isBlank(@domain.collate) then\n" +
                "      return true\n" +
                "    else\n" +
                "      return @func.string.equalsIgnoreCase(@domain.collate, #{collate})\n" +
                "    end\n" +
                "\n" +
                "  end\n" +
                "\n" +
                "end', 1)");

        sqls.add("INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content,\n" +
                "                              inner_share)\n" +
                "    VALUES (11, '', '限制表的存储引擎',\n" +
                "            '对于存储引擎选项 create table 语句必须设置为 \\'engine = #{engine}\\'，对于 alter table 语句若出现该选项也必须设置为 \\'engine = #{engine}\\'',\n" +
                "            '[\"MySQL\"]', 'Table', 'DetectRules',\n" +
                "            '[{\"name\":\"require\",\"type\":\"bool\",\"defaultValue\":\"true\",\"range\":[\"true\",\"false\"],\"hint\":\"强制要求指定存储引擎\"},{\"name\":\"engine\",\"type\":\"string\",\"defaultValue\":\"InnoDB\",\"range\":[\"InnoDB\",\"MyISAM\",\"Memory\"],\"hint\":\"允许使用的存储引擎\"}]', '#define \"require\" as bool\n" +
                "        default \"true\"\n" +
                "        enum [\"true\", \"false\"]\n" +
                "        hint \"强制要求指定存储引擎\"\n" +
                "#define \"engine\" as string\n" +
                "        default \"InnoDB\"\n" +
                "        enum [\"InnoDB\", \"MyISAM\", \"Memory\"]\n" +
                "        hint \"允许使用的存储引擎\"\n" +
                "\n" +
                "// 对于 alert 语句，只有当设置了 engine 选项时候才进行校验\n" +
                "if\n" +
                "  @domain.sqlType in [\\'ALERT_TABLE\\', \\'ALERT_TABLE_CONVERT\\'] and\n" +
                "  @func.string.isNotBlank(@domain.options.engine)\n" +
                "then\n" +
                "\n" +
                "    if @func.string.isBlank(#{engine}) then\n" +
                "      return false //规则配置错误\n" +
                "    else\n" +
                "      return @func.string.equalsIgnoreCase(@domain.options.engine, #{engine})\n" +
                "    end\n" +
                "\n" +
                "end\n" +
                "\n" +
                "// 对于 create 语句，检测是否必须设置 engine 选项\n" +
                "if @domain.sqlType == \\'CREATE_TABLE\\' then\n" +
                "\n" +
                "  if cast(#{require} as bool) then\n" +
                "\n" +
                "    if @func.string.isBlank(#{engine}) then\n" +
                "      return false //规则配置错误\n" +
                "    else\n" +
                "      return @func.string.equalsIgnoreCase(@domain.options.engine, #{engine})\n" +
                "    end\n" +
                "\n" +
                "  else\n" +
                "\n" +
                "    if @func.string.isBlank(@domain.options.engine) then\n" +
                "      return true\n" +
                "    else\n" +
                "      return @func.string.equalsIgnoreCase(@domain.options.engine, #{engine})\n" +
                "    end\n" +
                "\n" +
                "  end\n" +
                "\n" +
                "end\n" +
                "', 1)");

        sqls.add("INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content,\n" +
                "                              inner_share)\n" +
                "    VALUES (12, '', '表需要包含某些列', '在 create table 建表语句中必须表需要包含这些列 #{columns}', '[\"MySQL\"]',\n" +
                "            'Table',\n" +
                "            'DetectRules',\n" +
                "            '[{\"name\":\"columns\",\"type\":\"string\",\"defaultValue\":\"id,create_gmt,modify_gmt\",\"range\":null,\"hint\":\"需要包含的列名称，多个列使用逗号区分\"}]', '#define \"columns\" as string\n" +
                "        default \"id,create_gmt,modify_gmt\"\n" +
                "        hint \"需要包含的列名称，多个列使用逗号区分\"\n" +
                "\n" +
                "if\n" +
                "  @domain.sqlType != \\'CREATE_TABLE\\'\n" +
                "then\n" +
                "  return true\n" +
                "end\n" +
                "\n" +
                "return @func.string.split(#{columns}, \\', \\') in @domain.columns', 1)");

        sqls.add("INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content,\n" +
                "                              inner_share)\n" +
                "    VALUES (13, '', '限制表名最大长度', '根据规范要求某个表的名称超过了最大 #{length} 长度限制', '[\"MySQL\"]', 'Table',\n" +
                "            'DetectRules', '[{\"name\":\"length\",\"type\":\"int\",\"defaultValue\":\"30\",\"range\":null,\"hint\":\"表名的最大长度\"}]', '#define \"length\" as int\n" +
                "        default \"30\"\n" +
                "        hint \"表名的最大长度\"\n" +
                "\n" +
                "if @domain.sqlType in [\\'CREATE_TABLE\\', \\'CREATE_TABLE_SELECT\\', \\'CREATE_TABLE_LIKE\\'] then\n" +
                "  checkName = @domain.table\n" +
                "elseif @domain.sqlType in [\\'RENAME_TABLE\\', \\'ALERT_TABLE_RENAME\\'] then\n" +
                "  checkName = @domain.newName\n" +
                "else\n" +
                "  return true\n" +
                "end\n" +
                "\n" +
                "return @func.string.length(checkName) <= cast(#{length} as int)', 1)");

        sqls.add("INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content,\n" +
                "                              inner_share)\n" +
                "    VALUES (14, '', '修改表的字符集或排序规则', '在修改表的字符集和排序规则时请使用 alert table convert 语法',\n" +
                "            '[\"MySQL\"]',\n" +
                "            'Table', 'DetectRules', '[]', 'if\n" +
                "  @domain.sqlType != \\'ALERT_TABLE\\'\n" +
                "then\n" +
                "  return true\n" +
                "end\n" +
                "\n" +
                "if\n" +
                "  @func.string.isNotBlank(@domain.collate) or\n" +
                "  @func.string.isNotBlank(@domain.characterSet)\n" +
                "then\n" +
                "  return false\n" +
                "end', 1)");

        sqls.add("INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content,\n" +
                "                              inner_share)\n" +
                "    VALUES (15, '', '限制列的字符集',\n" +
                "            '对于字符集选项 create table 语句中列中必须设置为 \\'character set #{character_set}\\'，对于 alter table modify/change 语句若出现该选项也必须设置为 \\'character set #{character_set}\\'',\n" +
                "            '[\"MySQL\"]', 'Column', 'DetectRules',\n" +
                "            '[{\"name\":\"require\",\"type\":\"bool\",\"defaultValue\":\"false\",\"range\":[\"true\",\"false\"],\"hint\":\"强制要求设置字符集\"},{\"name\":\"character_set\",\"type\":\"string\",\"defaultValue\":\"utf8mb4\",\"range\":[\"utf8\",\"utf8mb4\"],\"hint\":\"允许使用的字符集\"}]', '#define \"require\" as bool\n" +
                "        default \"false\"\n" +
                "        enum [\"true\", \"false\"]\n" +
                "        hint \"强制要求设置字符集\"\n" +
                "#define \"character_set\" as string\n" +
                "        default \"utf8mb4\"\n" +
                "        enum [\"utf8\", \"utf8mb4\"]\n" +
                "        hint \"允许使用的字符集\"\n" +
                "\n" +
                "// 对于 modify/change 语句，只有当设置了 character set 选项时候才进行校验\n" +
                "if\n" +
                "  @domain.sqlType == \\'ALERT_TABLE_ALERT_COLUMN\\' and\n" +
                "  @func.string.isNotBlank(@domain.characterSet)\n" +
                "then\n" +
                "\n" +
                "    if @func.string.isBlank(#{character_set}) then\n" +
                "      return false //规则配置错误\n" +
                "    else\n" +
                "      return @func.string.equalsIgnoreCase(@domain.characterSet, #{character_set})\n" +
                "    end\n" +
                "\n" +
                "end\n" +
                "\n" +
                "// 对于 add column or create table 语句，检测是否必须设置 character set 选项\n" +
                "if @domain.sqlType in [\\'CREATE_TABLE_ADD_COLUMN\\', \\'ALERT_TABLE_ADD_COLUMN\\'] then\n" +
                "\n" +
                "  if cast(#{require} as bool) then\n" +
                "\n" +
                "    if @func.string.isBlank(#{character_set}) then\n" +
                "      return false //规则配置错误\n" +
                "    else\n" +
                "      return @func.string.equalsIgnoreCase(@domain.characterSet, #{character_set})\n" +
                "    end\n" +
                "\n" +
                "  else\n" +
                "\n" +
                "    if @func.string.isBlank(@domain.characterSet) then\n" +
                "      return true\n" +
                "    else\n" +
                "      return @func.string.equalsIgnoreCase(@domain.characterSet, #{character_set})\n" +
                "    end\n" +
                "\n" +
                "  end\n" +
                "\n" +
                "end', 1)");

        sqls.add("INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content,\n" +
                "                              inner_share)\n" +
                "    VALUES (16, '', '限制列的排序规则',\n" +
                "            '对于排序规则选项 create table 语句中列中必须设置为 \\'collate #{collate}\\'，对于 alter table modify/change 语句若出现该选项也必须设置为 \\'collate #{collate}\\'',\n" +
                "            '[\"MySQL\"]', 'Column', 'DetectRules',\n" +
                "            '[{\"name\":\"require\",\"type\":\"bool\",\"defaultValue\":\"false\",\"range\":[\"true\",\"false\"],\"hint\":\"强制要求设置排序规则\"},{\"name\":\"collate\",\"type\":\"string\",\"defaultValue\":\"utf8mb4_unicode_ci\",\"range\":[\"utf8_unicode_ci\",\"utf8mb4_unicode_ci\"],\"hint\":\"允许使用的排序规则\"}]', '#define \"require\" as bool\n" +
                "        default \"false\"\n" +
                "        enum [\"true\", \"false\"]\n" +
                "        hint \"强制要求设置排序规则\"\n" +
                "#define \"collate\" as string\n" +
                "        default \"utf8mb4_unicode_ci\"\n" +
                "        enum [\"utf8_unicode_ci\", \"utf8mb4_unicode_ci\"]\n" +
                "        hint \"允许使用的排序规则\"\n" +
                "\n" +
                "// 对于 modify/change 语句，只有当设置了 collate 选项时候才进行校验\n" +
                "if\n" +
                "  @domain.sqlType == \\'ALERT_TABLE_ALERT_COLUMN\\' and\n" +
                "  @func.string.isNotBlank(@domain.collate)\n" +
                "then\n" +
                "\n" +
                "    if @func.string.isBlank(#{collate}) then\n" +
                "      return false //规则配置错误\n" +
                "    else\n" +
                "      return @func.string.equalsIgnoreCase(@domain.collate, #{collate})\n" +
                "    end\n" +
                "\n" +
                "end\n" +
                "\n" +
                "// 对于 add column or create table 语句，检测是否必须设置 collate 选项\n" +
                "if @domain.sqlType in [\\'CREATE_TABLE_ADD_COLUMN\\', \\'ALERT_TABLE_ADD_COLUMN\\'] then\n" +
                "\n" +
                "  if cast(#{require} as bool) then\n" +
                "\n" +
                "    if @func.string.isBlank(#{collate}) then\n" +
                "      return false //规则配置错误\n" +
                "    else\n" +
                "      return @func.string.equalsIgnoreCase(@domain.collate, #{collate})\n" +
                "    end\n" +
                "\n" +
                "  else\n" +
                "\n" +
                "    if @func.string.isBlank(@domain.collate) then\n" +
                "      return true\n" +
                "    else\n" +
                "      return @func.string.equalsIgnoreCase(@domain.collate, #{collate})\n" +
                "    end\n" +
                "\n" +
                "  end\n" +
                "\n" +
                "end', 1)");

        sqls.add("INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content,\n" +
                "                              inner_share)\n" +
                "    VALUES (17, '', '列上的字符集与排序规则',\n" +
                "            '列上的字符集或排序规则根据配置可能被部分禁止，建议您根据需要在表级别或者库级别中指定。当前规范的限制为 #{allow}',\n" +
                "            '[\"MySQL\"]', 'Column', 'DetectRules',\n" +
                "            '[{\"name\":\"allow\",\"type\":\"string\",\"defaultValue\":\"Both\",\"range\":[\"Nothing\",\"Both\",\"Character set\",\"Collate\"],\"hint\":\"是否允许字符集和排序规则出现在列上\"}]', '#define \"allow\" as string\n" +
                "        default \"Both\"\n" +
                "        enum [\"Nothing\", \"Both\", \"Character set\", \"Collate\"]\n" +
                "        hint \"是否允许字符集和排序规则出现在列上\"\n" +
                "\n" +
                "if\n" +
                "  @domain.sqlType in [\\'CREATE_TABLE_ADD_COLUMN\\', \\'ALERT_TABLE_ADD_COLUMN\\', \\'ALERT_TABLE_ALERT_COLUMN\\']\n" +
                "then\n" +
                "\n" +
                "  if @func.string.isNotBlank(@domain.characterSet) then\n" +
                "    if #{allow} not in [\\'Both\\', \\'Character set\\'] then\n" +
                "      return false\n" +
                "    end\n" +
                "  end\n" +
                "\n" +
                "  if @func.string.isNotBlank(@domain.collate) then\n" +
                "    if #{allow} not in [\\'Both\\', \\'Collate\\'] then\n" +
                "      return false\n" +
                "    end\n" +
                "  end\n" +
                "\n" +
                "end', 1)");

        sqls.add("INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content,\n" +
                "                              inner_share)\n" +
                "    VALUES (18, '', '列禁用 Zerofill 属性', '规范禁止使用 zerofill 属性', '[\"MySQL\"]', 'Column', 'DetectRules', '[]', 'if\n" +
                "  @domain.sqlType not in [\\'CREATE_TABLE_ADD_COLUMN\\', \\'ALERT_TABLE_ADD_COLUMN\\', \\'ALERT_TABLE_ALERT_COLUMN\\']\n" +
                "then\n" +
                "  return true\n" +
                "end\n" +
                "\n" +
                "return !cast(@domain.options.zerofill as bool)', 1)");

        sqls.add("INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content,\n" +
                "                              inner_share)\n" +
                "    VALUES (19, '', '列必须有注释', '新增的列必须有注释，修改列时若指定了注释则必须不能为空', '[\"MySQL\"]', 'Column',\n" +
                "            'DetectRules', '[]', 'if\n" +
                "  @domain.sqlType in [\\'CREATE_TABLE_ADD_COLUMN\\', \\'ALERT_TABLE_ADD_COLUMN\\']\n" +
                "then\n" +
                "  return @func.string.isNotBlank(@domain.comment)\n" +
                "end\n" +
                "\n" +
                "if\n" +
                "  @domain.sqlType == \\'ALERT_TABLE_ALERT_COLUMN\\' and\n" +
                "  @domain.comment != null\n" +
                "then\n" +
                "  return @func.string.isNotBlank(@domain.comment)\n" +
                "end', 1)");

        sqls.add("INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content,\n" +
                "                              inner_share)\n" +
                "    VALUES (20, '', '列必须有默认值', '新增的列必须有默认值', '[\"MySQL\"]', 'Column',\n" +
                "            'DetectRules', '[]', 'if\n" +
                "  @domain.sqlType in [\\'CREATE_TABLE_ADD_COLUMN\\', \\'ALERT_TABLE_ADD_COLUMN\\']\n" +
                "then\n" +
                "  return @domain.defaultValue != null\n" +
                "end\n" +
                "\n" +
                "if\n" +
                "  @domain.sqlType == \\'ALERT_TABLE_ALERT_COLUMN\\' and\n" +
                "  @domain.defaultValue != null\n" +
                "then\n" +
                "  return @func.string.isNotBlank(@domain.defaultValue)\n" +
                "end', 1)");

        sqls.add("INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content,\n" +
                "                              inner_share)\n" +
                "    VALUES (21, '', '列名不能是关键字',\n" +
                "            '不能使用关键字作为列名，关键字清单可参考官方文档： https://dev.mysql.com/doc/refman/8.4/en/keywords.html',\n" +
                "            '[\"MySQL\"]', 'Column', 'DetectRules', '[]',\n" +
                "            'if @domain.sqlType in [\\'CREATE_TABLE_ADD_COLUMN\\', \\'ALERT_TABLE_ADD_COLUMN\\'] then\n" +
                "      checkName = @domain.column\n" +
                "    elseif @domain.sqlType == \\'ALERT_TABLE_ALERT_COLUMN\\' then\n" +
                "      if @func.string.isBlank(@domain.newName) then\n" +
                "        return true\n" +
                "      else\n" +
                "        checkName = @domain.newName\n" +
                "      end\n" +
                "    else\n" +
                "      return true\n" +
                "    end\n" +
                "\n" +
                "    return @func.string.upperCase(checkName) not in [\\'ACCESSIBLE\\',\\'ADD\\',\\'ALL\\',\\'ALTER\\',\\'ANALYZE\\',\\'AND\\',\\'AS\\',\\'ASC\\',\\'ASENSITIVE\\',\n" +
                "         \\'BEFORE\\',\\'BETWEEN\\',\\'BIGINT\\',\\'BINARY\\',\\'BLOB\\',\\'BOTH\\',\\'BY\\',\n" +
                "         \\'CALL\\',\\'CASCADE\\',\\'CASE\\',\\'CHANGE\\',\\'CHAR\\',\\'CHARACTER\\',\\'CHECK\\',\\'COLLATE\\',\\'COLUMN\\',\\'CONDITION\\',\\'CONSTRAINT\\',\\'CONTINUE\\',\n" +
                "         \\'CONVERT\\',\\'CREATE\\',\\'CROSS\\',\\'CURRENT_DATE\\',\\'CURRENT_TIME\\',\\'CURRENT_TIMESTAMP\\',\\'CURRENT_USER\\',\\'CURSOR\\',\n" +
                "         \\'DATABASE\\',\\'DATABASES\\',\\'DAY_HOUR\\',\\'DAY_MICROSECOND\\',\\'DAY_MINUTE\\',\\'DAY_SECOND\\',\\'DEC\\',\\'DECIMAL\\',\\'DECLARE\\',\\'DEFAULT\\',\n" +
                "         \\'DELAYED\\',\\'DELETE\\',\\'DESC\\',\\'DESCRIBE\\',\\'DETERMINISTIC\\',\\'DISTINCT\\',\\'DISTINCTROW\\',\\'DIV\\',\\'DOUBLE\\',\\'DROP\\',\\'DUAL\\',\n" +
                "         \\'EACH\\',\\'ELSE\\',\\'ELSEIF\\',\\'ENCLOSED\\',\\'ESCAPED\\',\\'EXISTS\\',\\'EXIT\\',\\'EXPLAIN\\',\n" +
                "         \\'FALSE\\',\\'FETCH\\',\\'FLOAT\\',\\'FLOAT4\\',\\'FLOAT8\\',\\'FOR\\',\\'FORCE\\',\\'FOREIGN\\',\\'FROM\\',\\'FULLTEXT\\',\\'GENERATED\\',\\'GET\\',\\'GRANTint\\',\\'GROUP\\',\n" +
                "         \\'HAVING\\',\\'HIGH_PRIORITY\\',\\'HOUR_MICROSECOND\\',\\'HOUR_MINUTE\\',\\'HOUR_SECOND\\',\\'IF\\',\\'IGNORE\\',\\'IN\\',\\'INDEX\\',\\'INFILE\\',\n" +
                "         \\'INNER\\',\\'INOUT\\',\\'INSENSITIVE\\',\\'INSERT\\',\\'INT\\',\\'INT1\\',\\'INT2\\',\\'INT3\\',\\'INT4\\',\\'INT8\\',\\'INTEGER\\',\\'INTERVALint\\',\\'INTO\\',\n" +
                "         \\'IO_AFTER_GTIDS\\',\\'IO_BEFORE_GTIDS\\',\\'IS\\',\\'ITERATE\\',\\'JOIN\\',\\'KEY\\',\\'KEYS\\',\\'KILL\\',\\'LEADING\\',\\'LEAVE\\',\\'LEFT\\',\\'LIKE\\',\n" +
                "         \\'LIMIT\\',\\'LINEAR\\',\\'LINES\\',\\'LOAD\\',\\'LOCALTIME\\',\\'LOCALTIMESTAMP\\',\\'LOCK\\',\\'LONG\\',\\'LONGBLOB\\',\\'LONGTEXT\\',\\'LOOP\\',\\'LOW_PRIORITY\\',\n" +
                "         \\'MASTER_BIND\\',\\'MASTER_SSL_VERIFY_SERVER_CERT\\',\\'MATCH\\',\\'MAXVALUE\\',\\'MEDIUMBLOB\\',\\'MEDIUMINT\\',\\'MEDIUMTEXT\\',\\'MIDDLEINT\\',\n" +
                "         \\'MINUTE_MICROSECOND\\',\\'MINUTE_SECOND\\',\\'MOD\\',\\'MODIFIES\\',\\'NATURAL\\',\\'NOT\\',\\'NO_WRITE_TO_BINLOG\\',\\'NULL\\',\\'NUMERIC\\',\n" +
                "         \\'ON\\',\\'OPTIMIZE\\',\\'OPTIMIZER_COSTS\\',\\'OPTION\\',\\'OPTIONALLY\\',\\'OR\\',\\'ORDER\\',\\'OUT\\',\\'OUTER\\',\\'OUTFILE\\',\\'PARTITION\\',\\'PRECISION\\',\n" +
                "         \\'PRIMARY\\',\\'PROCEDURE\\',\\'PURGE\\',\\'RANGE\\',\\'READ\\',\\'READS\\',\\'READ_WRITE\\',\\'REAL\\',\\'REFERENCES\\',\\'REGEXP\\',\\'RELEASE\\',\\'RENAME\\',\n" +
                "         \\'REPEAT\\',\\'REPLACE\\',\\'REQUIRE\\',\\'RESIGNAL\\',\\'RESTRICT\\',\\'RETURN\\',\\'REVOKE\\',\\'RIGHT\\',\\'RLIKE\\',\\'SCHEMA\\',\\'SCHEMAS\\',\n" +
                "         \\'SECOND_MICROSECOND\\',\\'SELECT\\',\\'SENSITIVE\\',\\'SEPARATOR\\',\\'SET\\',\\'SHOW\\',\\'SIGNAL\\',\\'SMALLINT\\',\\'SPATIAL\\',\\'SPECIFIC\\',\n" +
                "         \\'SQL\\',\\'SQLEXCEPTION\\',\\'SQLSTATE\\',\\'SQLWARNING\\',\\'SQL_BIG_RESULT\\',\\'SQL_CALC_FOUND_ROWS\\',\\'SQL_SMALL_RESULT\\',\\'SSL\\',\n" +
                "         \\'STARTING\\',\\'STORED\\',\\'STRAIGHT_JOIN\\',\\'TABLE\\',\\'TERMINATED\\',\\'THEN\\',\\'TINYBLOB\\',\\'TINYINT\\',\\'TINYTEXT\\',\\'TO\\',\\'TRAILING\\',\n" +
                "         \\'TRIGGER\\',\\'TRUE\\',\\'UNDO\\',\\'UNION\\',\\'UNIQUE\\',\\'UNLOCK\\',\\'UNSIGNED\\',\\'UPDATE\\',\\'USAGE\\',\\'USE\\',\\'USING\\',\\'UTC_DATE\\',\\'UTC_TIME\\',\n" +
                "         \\'UTC_TIMESTAMP\\',\\'VALUES\\',\\'VARBINARY\\',\\'VARCHAR\\',\\'VARCHARACTER\\',\\'VARYING\\',\\'VIRTUAL\\',\\'WHEN\\',\\'WHERE\\',\\'WHILE\\',\\'WITH\\',\n" +
                "         \\'WRITE\\',\\'XOR\\',\\'YEAR_MONTH\\',\\'ZEROFILL\\']', 1)");

        sqls.add("INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content,\n" +
                "                              inner_share)\n" +
                "    VALUES (22, '', '列名拼写规则', '列名需要满足 #{caseType} 拼写规则', '[\"MySQL\"]', 'Column', 'DetectRules',\n" +
                "            '[{\"name\":\"caseType\",\"type\":\"string\",\"defaultValue\":\"Lower case\",\"range\":[\"Lower case\",\"Upper case\",\"Lower camel case\",\"Upper camel case\"],\"hint\":\"表名拼写规则\"}]', '#define \"caseType\" as string\n" +
                "        default \"Lower case\"\n" +
                "        enum [\"Lower case\", \"Upper case\", \"Lower camel case\", \"Upper camel case\"]\n" +
                "        hint \"表名拼写规则\"\n" +
                "\n" +
                "if @domain.sqlType in [\\'CREATE_TABLE_ADD_COLUMN\\', \\'ALERT_TABLE_ADD_COLUMN\\'] then\n" +
                "  checkName = @domain.column\n" +
                "elseif @domain.sqlType == \\'ALERT_TABLE_ALERT_COLUMN\\' then\n" +
                "  if @func.string.isBlank(@domain.newName) then\n" +
                "    return true\n" +
                "  else\n" +
                "    checkName = @domain.newName\n" +
                "  end\n" +
                "else\n" +
                "  return true\n" +
                "end\n" +
                "\n" +
                "// ref document https://newbedev.com/regex-for-pascalcased-words-aka-camelcased-with-leading-uppercase-letter\n" +
                "\n" +
                "if #{caseType} == \\'Lower case\\' then\n" +
                "  return @func.string.lowerCase(checkName) == checkName\n" +
                "\n" +
                "elseif #{caseType} == \\'Upper case\\' then\n" +
                "\n" +
                "  return @func.string.upperCase(checkName) == checkName\n" +
                "\n" +
                "elseif #{caseType} == \\'Lower camel case\\' then\n" +
                "  return checkName matches \\'[a-z]+((\\\\d)|([A-Z0-9][a-z0-9]+))*([A-Z])?\\'\n" +
                "\n" +
                "elseif #{caseType} == \\'Upper camel case\\' then\n" +
                "  return checkName matches \\'([A-Z][a-z0-9]+)((\\\\d)|([A-Z0-9][a-z0-9]+))*([A-Z])?\\'\n" +
                "\n" +
                "else\n" +
                "\n" +
                "  return false\n" +
                "end', 1)");

        sqls.add("INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content,\n" +
                "                              inner_share)\n" +
                "    VALUES (23, '', '限制列名最大长度',\n" +
                "            '根据规范要求某个列的名称超过了最大 #{length} 长度限制，请检查 crate table 语句以及 alert table xxx change 语句',\n" +
                "            '[\"MySQL\"]', 'Column', 'DetectRules',\n" +
                "            '[{\"name\":\"length\",\"type\":\"int\",\"defaultValue\":\"30\",\"range\":null,\"hint\":\"列名的最大长度\"}]', '#define \"length\" as int\n" +
                "        default \"30\"\n" +
                "        hint \"列名的最大长度\"\n" +
                "\n" +
                "if @domain.sqlType in [\\'CREATE_TABLE_ADD_COLUMN\\', \\'ALERT_TABLE_ADD_COLUMN\\'] then\n" +
                "  checkName = @domain.column\n" +
                "elseif @domain.sqlType == \\'ALERT_TABLE_ALERT_COLUMN\\' then\n" +
                "  if @func.string.isBlank(@domain.newName) then\n" +
                "    return true\n" +
                "  else\n" +
                "    checkName = @domain.newName\n" +
                "  end\n" +
                "else\n" +
                "  return true\n" +
                "end\n" +
                "\n" +
                "return @func.string.length(checkName) <= cast(#{length} as int)', 1)");

        sqls.add("INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content,\n" +
                "                              inner_share)\n" +
                "    VALUES (24, '', '限制CHAR/NCHAR类型最大长度', '根据规范要求某个列的  CHAR/NCHAR类型长度超过了最大 #{length} 限制',\n" +
                "            '[\"MySQL\"]', 'Column', 'DetectRules',\n" +
                "            '[{\"name\":\"length\",\"type\":\"int\",\"defaultValue\":\"120\",\"range\":null,\"hint\":\"字段最大长度\"}]', '#define \"length\" as int\n" +
                "        default \"120\"\n" +
                "        hint \"字段最大长度\"\n" +
                "\n" +
                "if\n" +
                "  @domain.sqlType not in [\\'CREATE_TABLE_ADD_COLUMN\\', \\'ALERT_TABLE_ADD_COLUMN\\', \\'ALERT_TABLE_ALERT_COLUMN\\']\n" +
                "then\n" +
                "  return true\n" +
                "end\n" +
                "\n" +
                "if\n" +
                "  @func.string.startsWith(@domain.typeDesc, \"char\") or\n" +
                "  @func.string.startsWith(@domain.typeDesc, \"nchar\")\n" +
                "then\n" +
                "\n" +
                "  if @func.string.isBlank(@domain.length) then\n" +
                "    return false // 语句中长度为空，例如 create table abc(name char);\n" +
                "  else\n" +
                "    return cast(@domain.length as int) <= cast(#{length} as int)\n" +
                "  end\n" +
                "\n" +
                "end', 1)");

        sqls.add("INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content,\n" +
                "                              inner_share)\n" +
                "    VALUES (25, '', '限制VARCHAR/NVARCHAR类型最大长度',\n" +
                "            '根据规范要求某个列的 VARCHAR/NVARCHAR 类型长度超过了最大 #{length} 限制', '[\"MySQL\"]', 'Column',\n" +
                "            'DetectRules',\n" +
                "            '[{\"name\":\"length\",\"type\":\"int\",\"defaultValue\":\"500\",\"range\":null,\"hint\":\"字段最大长度\"}]', '#define \"length\" as int\n" +
                "        default \"500\"\n" +
                "        hint \"字段最大长度\"\n" +
                "\n" +
                "if\n" +
                "  @domain.sqlType not in [\\'CREATE_TABLE_ADD_COLUMN\\', \\'ALERT_TABLE_ADD_COLUMN\\', \\'ALERT_TABLE_ALERT_COLUMN\\']\n" +
                "then\n" +
                "  return true\n" +
                "end\n" +
                "\n" +
                "if\n" +
                "  @func.string.startsWith(@domain.typeDesc, \"varchar\") or\n" +
                "  @func.string.startsWith(@domain.typeDesc, \"nvarchar\")\n" +
                "then\n" +
                "\n" +
                "  if @func.string.isBlank(@domain.length) then\n" +
                "    return false // 语句中长度为空，例如 create table abc(name char);\n" +
                "  else\n" +
                "    return cast(@domain.length as int) <= cast(#{length} as int)\n" +
                "  end\n" +
                "\n" +
                "end', 1)");

        sqls.add(" INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content,\n" +
                "                              inner_share)\n" +
                "    VALUES (26, '', '限制列删除', '规则描述：规范要求列不能被删除', '[\"MySQL\"]', 'Column', 'DetectRules',\n" +
                "            '[{\"name\":\"allow\",\"type\":\"string\",\"defaultValue\":\"true\",\"range\":[\"true\",\"false\"],\"hint\":\"是否允许删除列\"}]', '#define \"allow\" as string\n" +
                "        default \"true\"\n" +
                "        enum [\"true\", \"false\"]\n" +
                "        hint \"是否允许删除列\"\n" +
                "\n" +
                "if\n" +
                "  @domain.sqlType == \\'ALERT_TABLE_DROP_COLUMN\\' and\n" +
                "  !cast(#{allow} as bool)\n" +
                "then\n" +
                "  return false\n" +
                "end', 1)");

        sqls.add("INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content,\n" +
                "                              inner_share)\n" +
                "    VALUES (27, '', '限制列的类型',\n" +
                "            '规范要求列类型需要满足 #{ruleType} (AllowList 允许, BlockList 禁止的)名单要求，具体类型清单为：#{typeList}',\n" +
                "            '[\"MySQL\"]', 'Column', 'DetectRules',\n" +
                "            '[{\"name\":\"ruleType\",\"type\":\"string\",\"defaultValue\":\"AllowList\",\"range\":[\"AllowList\",\"BlockList\"],\"hint\":\"名单工作模式\"},{\"name\":\"typeList\",\"type\":\"string\",\"defaultValue\":\"bit,tinyint,smallint,mediumint,int,integer,bigint,decimal,numeric,float,double,date,datetime,timestamp,time,char,varchar,binary,tinyblob,blob,mediumblob,longblob,tinytext,text,mediumtext,longtext,enum,set,json\",\"range\":null,\"hint\":\"允许或禁止的类型列表\"}]', '#define \"ruleType\" as string\n" +
                "        default \"AllowList\"\n" +
                "        enum [\"AllowList\", \"BlockList\"]\n" +
                "        hint \"名单工作模式\"\n" +
                "#define \"typeList\" as string\n" +
                "        default \"bit,tinyint,smallint,mediumint,int,integer,bigint,decimal,numeric,float,double,date,datetime,timestamp,time,char,varchar,binary,tinyblob,blob,mediumblob,longblob,tinytext,text,mediumtext,longtext,enum,set,json\"\n" +
                "        hint \"允许或禁止的类型列表\"\n" +
                "\n" +
                "if\n" +
                "  @domain.sqlType not in [\\'CREATE_TABLE_ADD_COLUMN\\', \\'ALERT_TABLE_ADD_COLUMN\\', \\'ALERT_TABLE_ALERT_COLUMN\\']\n" +
                "then\n" +
                "  return true\n" +
                "end\n" +
                "\n" +
                "if\n" +
                "  @func.string.isBlank(#{typeList})\n" +
                "then\n" +
                "  return true\n" +
                "end\n" +
                "\n" +
                "if\n" +
                "  #{ruleType} == \\'AllowList\\'\n" +
                "then\n" +
                "  return @domain.typeName in @func.string.split(#{typeList}, \\', \\')\n" +
                "end\n" +
                "\n" +
                "if\n" +
                "  #{ruleType} == \\' BlockList\\'\n" +
                "then\n" +
                "  return @domain.typeName not in @func.string.split(#{typeList}, \\', \\')\n" +
                "end\n" +
                "\n" +
                "return false', 1)");

        sqls.add("INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content,\n" +
                "                              inner_share)\n" +
                "    VALUES (28, '', '限制列不允许为空', '根据规范要求列必须具有 \\'NOT NULL\\' 选项', '[\"MySQL\"]', 'Column',\n" +
                "            'DetectRules',\n" +
                "            '[]', 'if\n" +
                "  @domain.sqlType in [\\'CREATE_TABLE_ADD_COLUMN\\', \\'ALERT_TABLE_ADD_COLUMN\\', \\'ALERT_TABLE_ALERT_COLUMN\\']\n" +
                "then\n" +
                "  return !@domain.nullable\n" +
                "end', 1)");

        sqls.add(" INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,\n" +
                "                              rule_content,\n" +
                "                              inner_share)\n" +
                "    VALUES (29, '', '限制表外键', '创建或修改表结构时不允许使用表外键', '[\"MySQL\"]', 'Constraint', 'DetectRules',\n" +
                "            '[{\"name\":\"allow\",\"type\":\"bool\",\"defaultValue\":\"false\",\"range\":[\"true\",\"false\"],\"hint\":\"是否允许使用外键约束\"}]', '#define \"allow\" as bool\n" +
                "        default \"false\"\n" +
                "        enum [\"true\", \"false\"]\n" +
                "        hint \"是否允许使用外键约束\"\n" +
                "\n" +
                "if\n" +
                "  @domain.sqlType in [\\'CREATE_TABLE_ADD_CONSTRAINT\\', \\'ALERT_TABLE_ADD_CONSTRAINT\\']\n" +
                "then\n" +
                "\n" +
                "  if @domain.type == \\'ForeignKey\\' then\n" +
                "    return cast(#{allow} as bool)\n" +
                "  else\n" +
                "    return true\n" +
                "  end\n" +
                "\n" +
                "end', 1)");
    }
}