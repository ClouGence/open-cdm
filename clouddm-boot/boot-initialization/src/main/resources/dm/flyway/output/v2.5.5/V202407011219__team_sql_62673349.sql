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

    alter table `dm_ds_session`
        add session_type varchar(16) null;
    alter table `dm_ds_session`
        modify datasource_id varchar(255) null;
    alter table `dm_ds_session`
        add `attach` text null;
    alter table `dm_ds_session`
        add `last_query_time` datetime null;
    alter table `dm_ds_session`
        drop `tx`;

    CREATE TABLE IF NOT EXISTS `dm_async_task`
    (
        `id`                 bigint(20)   NOT NULL AUTO_INCREMENT,
        `gmt_create`         datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `gmt_modified`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `title`              varchar(256) NOT NULL,
        `description`        text         NULL,
        `biz_id`             varchar(128) NULL,
        `biz_type`           varchar(64)  NULL,
        `console_ip`         varchar(32)  NULL,
        `depend_on_biz_id`   varchar(128) NULL,
        `depend_on_biz_type` varchar(64)  NULL,
        `owner_uid`          varchar(255) NOT NULL,
        `handler_name`       text         NOT NULL,
        `handler_type`       text         NOT NULL,
        `config_data`        text         NULL,
        `show_in_dock`       tinyint      NOT NULL DEFAULT 0,
        `process_type`       varchar(64)  NOT NULL DEFAULT 'SCROLL',
        `process_value`      bigint       NOT NULL DEFAULT 0,
        `fast_fail`          tinyint      NOT NULL DEFAULT 0,
        `status`             varchar(32)  NOT NULL,
        `status_msg`         text         NULL,
        `time_of_start`      datetime     NULL,
        `time_of_last`       datetime     NULL,
        `time_of_finish`     datetime     NULL,
        PRIMARY KEY (`id`),
        index dm_async_task_owner_uid (`owner_uid`),
        unique dm_async_task_biz_idx (`biz_id`, `biz_type`)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    create table if not exists `dm_sec_spec`
    (
        `id`           bigint(20)   not null auto_increment,
        `gmt_create`   datetime     not null default current_timestamp,
        `gmt_modified` datetime     not null default current_timestamp,
        `owner_uid`    varchar(255) not null,
        `spec_name`    varchar(255) not null,
        `description`  text         null,
        `enable`       tinyint      not null default 0,
        primary key (`id`),
        index dm_sec_spec_owner_uid (`owner_uid`)
    ) engine = innodb
      default charset = utf8mb4
      collate = utf8mb4_general_ci;

    create table if not exists `dm_sec_rules`
    (
        `id`           bigint(20)   not null auto_increment,
        `gmt_create`   datetime     not null default current_timestamp,
        `gmt_modified` datetime     not null default current_timestamp,
        `owner_uid`    varchar(255) not null,
        `rule_name`    varchar(255) not null,
        `rule_desc`    text,
        `ds_range`     text         null,
        `rule_target`  varchar(64)  not null,
        `rule_type`    varchar(64)  not null,
        `rule_def`     text,
        `rule_content` text,
        `inner_share`  tinyint      not null default 0,
        primary key (`id`),
        index dm_sec_rules_owner_uid (`owner_uid`)
    ) engine = innodb
      default charset = utf8mb4
      collate = utf8mb4_general_ci;

    create table if not exists `dm_sec_referer`
    (
        `id`           bigint(20)   not null auto_increment,
        `gmt_create`   datetime     not null default current_timestamp,
        `gmt_modified` datetime     not null default current_timestamp,
        `owner_uid`    varchar(255) not null,
        `enable`       tinyint      not null,
        `ref_rule`     bigint       not null,
        `ref_spec`     bigint       not null,
        `warn_level`   varchar(64),
        `rule_param`   text,
        primary key (`id`),
        index dm_sec_referer_owner_uid (`owner_uid`),
        index dm_sec_referer_refs (`ref_rule`, `ref_spec`)
    ) engine = innodb
      default charset = utf8mb4
      collate = utf8mb4_general_ci;


    CREATE TABLE `dm_ticket_details_inst`
    (
        `id`                     bigint       NOT NULL AUTO_INCREMENT,
        `gmt_create`             datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `gmt_modified`           datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        `rdp_ticket_ins_id`      varchar(64)  NOT NULL COMMENT 'The corresponding ID in the RDP work order table',
        `session_id`             varchar(255)          DEFAULT NULL COMMENT 'Session will only be created after entering the automatic execution phase',
        `risk_sql_count`         int                   DEFAULT NULL COMMENT 'Risk SQL quantity',
        `raw_sql`                longtext COMMENT 'Unresolved original SQL content submitted by users',
        `explain_sql_data`       longtext,
        `total_count`            mediumtext COMMENT 'The total number of SQL statements in the execution process',
        `expected_affected_rows` bigint                DEFAULT NULL COMMENT 'Expected impact on the number of rows',
        `expected_exec_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'If executed immediately, it defaults to the time when the work order was generated. If it is found that the expected execution time is less than the current execution time after approval, execute immediately',
        `immediately`            tinyint(1)            DEFAULT NULL COMMENT 'Do you want to execute it immediately',
        `ddl_sql_exec_type`      varchar(128) NOT NULL DEFAULT 'DIRECT',
        `none_ddl_sql_exec_type` varchar(128) NOT NULL DEFAULT 'DIRECT',
        `roll_back_sql`          longtext,
        `deleted`                tinyint(1)   NOT NULL DEFAULT '0',
        PRIMARY KEY (`id`),
        UNIQUE KEY `idx_unique_biz_id` (`rdp_ticket_ins_id`)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    drop table `checker_parameter`;
    drop table `checker_template`;
    drop table `console_job`;
    drop table `console_task`;
    drop table `sql_process`;
    drop table `ticket_process`;
    drop table `dm_ticket_inst`;
    drop table `ds_appro_template`;


    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content, inner_share)
    VALUES (1, '', '限制库的排序规则',
            '对于排序规则选项建库语句必须设置为 \'collate #{collate}\'，对于修改库语句若出现该选项也必须设置为 \'collate #{collate}\'',
            '["MySQL"]', 'Catalog', 'DetectRules',
            '[{"name":"require","type":"bool","defaultValue":"true","range":["true","false"],"hint":"强制要求设置排序规则"},{"name":"collate","type":"string","defaultValue":"utf8mb4_general_ci","range":["utf8_general_ci","utf8_bin","utf8mb4_general_ci","utf8mb4_bin"],"hint":"允许使用的排序规则"}]', '#define "require" as bool
        default "true"
        enum ["true", "false"]
        hint "强制要求设置排序规则"
#define "collate" as string
        default "utf8mb4_unicode_ci"
        enum ["utf8_unicode_ci", "utf8mb4_unicode_ci"]
        hint "允许使用的排序规则"

// 对于 alert 语句，只有当设置了 collate 选项时候才进行校验
if
  @domain.sqlType in [\'ALERT_CATALOG\'] and
  @func.string.isNotBlank(@domain.collate)
then

    if @func.string.isBlank(#{collate}) then
      return false //规则配置错误
    else
      return @func.string.equalsIgnoreCase(@domain.collate, #{collate})
    end

end

// 对于 create 语句，检测是否必须设置 collate 选项
if @domain.sqlType == \'CREATE_CATALOG\' then

  if cast(#{require} as bool) then

    if @func.string.isBlank(#{collate}) then
      return false //规则配置错误
    else
      return @func.string.equalsIgnoreCase(@domain.collate, #{collate})
    end

  else

    if @func.string.isBlank(@domain.collate) then
      return true
    else
      return @func.string.equalsIgnoreCase(@domain.collate, #{collate})
    end

  end

end', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target,
                              rule_type, rule_def, rule_content, inner_share)
    VALUES (2, '', '限制库的字符集',
            '对于字符集选项建库语句必须设置为 \'character set #{character_set}\'，对于修改库语句若出现该选项也必须设置为 \'character set  #{character_set}\'',
            '["MySQL"]', 'Catalog', 'DetectRules',
            '[{"name":"require","type":"bool","defaultValue":"true","range":["true","false"],"hint":"强制要求设置字符集"},{"name":"character_set","type":"string","defaultValue":"utf8mb4","range":["utf8","utf8mb4"],"hint":"允许使用的字符集"}]', '#define "require" as bool
        default "true"
        enum ["true", "false"]
        hint "强制要求设置字符集"
#define "character_set" as string
        default "utf8mb4"
        enum ["utf8", "utf8mb4"]
        hint "允许使用的字符集"

// 对于 alert 语句，只有当设置了 character set 选项时候才进行校验
if
  @domain.sqlType in [\'ALERT_CATALOG\'] and
  @func.string.isNotBlank(@domain.characterSet)
then

    if @func.string.isBlank(#{character_set}) then
      return false //规则配置错误
    else
      return @func.string.equalsIgnoreCase(@domain.characterSet, #{character_set})
    end

end

// 对于 create 语句，检测是否必须设置 character set 选项
if @domain.sqlType == \'CREATE_CATALOG\' then

  if cast(#{require} as bool) then

    if @func.string.isBlank(#{character_set}) then
      return false //规则配置错误
    else
      return @func.string.equalsIgnoreCase(@domain.characterSet, #{character_set})
    end

  else

    if @func.string.isBlank(@domain.characterSet) then
      return true
    else
      return @func.string.equalsIgnoreCase(@domain.characterSet, #{character_set})
    end

  end

end', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content,
                              inner_share)
    VALUES (3, '', '限制建表自增初始值', '在建表语句中使用了 auto_increment 选项，自增属性需要设置为 #{number}',
            '["MySQL"]',
            'Table', 'DetectRules',
            '[{"name":"require","type":"bool","defaultValue":"false","range":["true","false"],"hint":"强制要求设置自增初始值"},{"name":"number","type":"int","defaultValue":"1","range":null,"hint":"建表自增初始值"}]', '#define "require" as bool
        default "false"
        enum ["true", "false"]
        hint "强制要求设置自增初始值"
#define "number" as int
        default "1"
        hint "建表自增初始值"

if @domain.sqlType != \'CREATE_TABLE\' then
  return true
end

if @func.string.isBlank(@domain.options.auto_increment) then

  return !cast(#{require} as bool)

else

  if @func.string.isBlank(#{number}) then
    return false // 规则配置错误
  else
    return cast(@domain.options.auto_increment as int) == cast(#{number} as int)
  end

end', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content,
                              inner_share)
    VALUES (4, '', '限制列的数量', '在 create table 语句中限制列的数量最多为 #{maxCount} 个', '["MySQL"]', 'Table',
            'DetectRules',
            '[{"name":"maxCount","type":"int","defaultValue":"50","range":null,"hint":"表最大自段数量"}]', '#define "maxCount" as int
        default "50"
        hint "表最大自段数量"

if
  @domain.sqlType != \'CREATE_TABLE\'
then
  return true
end

return @func.array.size(@domain.columns) <= cast(#{maxCount} as int)', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content,
                              inner_share)
    VALUES (5, '', '表名拼写规则', '表名需要满足 #{caseType} 拼写规则', '["MySQL"]', 'Table', 'DetectRules',
            '[{"name":"caseType","type":"string","defaultValue":"Lower case","range":["Lower case","Upper case","Lower camel case","Upper camel case"],"hint":"表名拼写规则"}]', '#define "caseType" as string
        default "Lower case"
        enum ["Lower case", "Upper case", "Lower camel case", "Upper camel case"]
        hint "表名拼写规则"

if @domain.sqlType in [\'CREATE_TABLE\', \'CREATE_TABLE_SELECT\', \'CREATE_TABLE_LIKE\'] then
  checkName = @domain.table
elseif @domain.sqlType in [\'RENAME_TABLE\', \'ALERT_TABLE_RENAME\'] then
  checkName = @domain.newName
else
  return true
end

// ref document https://newbedev.com/regex-for-pascalcased-words-aka-camelcased-with-leading-uppercase-letter

if #{caseType} == \'Lower case\' then
  return @func.string.lowerCase(checkName) == checkName

elseif #{caseType} == \'Upper case\' then

  return @func.string.upperCase(checkName) == checkName

elseif #{caseType} == \'Lower camel case\' then
  return checkName matches \'[a-z]+((\\d)|([A-Z0-9][a-z0-9]+))*([A-Z])?\'

elseif #{caseType} == \'Upper camel case\' then
  return checkName matches \'([A-Z][a-z0-9]+)((\\d)|([A-Z0-9][a-z0-9]+))*([A-Z])?\'

else

  return false
end', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content,
                              inner_share)
    VALUES (6, '', '表必须有注释',
            '在 create table 语句中要求必须有注释，对于 alert table 语句如果存在设置注释选项必须不能为空', '["MySQL"]',
            'Table', 'DetectRules', '[]', 'if
  @domain.sqlType == \'CREATE_TABLE\'
then
  return @func.string.isNotBlank(@domain.comment)
end

if
  @domain.sqlType == \'ALERT_TABLE\' and
  @domain.comment != null
then
  return @func.string.isNotBlank(@domain.comment)
end', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content,
                              inner_share)
    VALUES (7, '', '表必须有主键', '在 create table 建表语句中必须指定主键列', '["MySQL"]', 'Table', 'DetectRules',
            '[]', 'if
  @domain.sqlType == \'CREATE_TABLE\'
then
  return @domain.hasPrimary
end', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content,
                              inner_share)
    VALUES (8, '', '表名不能是关键字',
            '不能使用关键字作为表名，关键字清单可参考官方文档：https://dev.mysql.com/doc/refman/8.4/en/keywords.html',
            '["MySQL"]', 'Table', 'DetectRules', '[]', 'if
  @domain.sqlType not in [\'CREATE_TABLE\', \'RENAME_TABLE\', \'ALERT_TABLE_RENAME\']
then
  return true
end

return @func.string.upperCase((@domain.sqlType == \'CREATE_TABLE\'? @domain.table : @domain.newName)) not in
    [\'ACCESSIBLE\',\'ADD\',\'ALL\',\'ALTER\',\'ANALYZE\',\'AND\',\'AS\',\'ASC\',\'ASENSITIVE\',
     \'BEFORE\',\'BETWEEN\',\'BIGINT\',\'BINARY\',\'BLOB\',\'BOTH\',\'BY\',
     \'CALL\',\'CASCADE\',\'CASE\',\'CHANGE\',\'CHAR\',\'CHARACTER\',\'CHECK\',\'COLLATE\',\'COLUMN\',\'CONDITION\',\'CONSTRAINT\',\'CONTINUE\',
     \'CONVERT\',\'CREATE\',\'CROSS\',\'CURRENT_DATE\',\'CURRENT_TIME\',\'CURRENT_TIMESTAMP\',\'CURRENT_USER\',\'CURSOR\',
     \'DATABASE\',\'DATABASES\',\'DAY_HOUR\',\'DAY_MICROSECOND\',\'DAY_MINUTE\',\'DAY_SECOND\',\'DEC\',\'DECIMAL\',\'DECLARE\',\'DEFAULT\',
     \'DELAYED\',\'DELETE\',\'DESC\',\'DESCRIBE\',\'DETERMINISTIC\',\'DISTINCT\',\'DISTINCTROW\',\'DIV\',\'DOUBLE\',\'DROP\',\'DUAL\',
     \'EACH\',\'ELSE\',\'ELSEIF\',\'ENCLOSED\',\'ESCAPED\',\'EXISTS\',\'EXIT\',\'EXPLAIN\',
     \'FALSE\',\'FETCH\',\'FLOAT\',\'FLOAT4\',\'FLOAT8\',\'FOR\',\'FORCE\',\'FOREIGN\',\'FROM\',\'FULLTEXT\',\'GENERATED\',\'GET\',\'GRANTint\',\'GROUP\',
     \'HAVING\',\'HIGH_PRIORITY\',\'HOUR_MICROSECOND\',\'HOUR_MINUTE\',\'HOUR_SECOND\',\'IF\',\'IGNORE\',\'IN\',\'INDEX\',\'INFILE\',
     \'INNER\',\'INOUT\',\'INSENSITIVE\',\'INSERT\',\'INT\',\'INT1\',\'INT2\',\'INT3\',\'INT4\',\'INT8\',\'INTEGER\',\'INTERVALint\',\'INTO\',
     \'IO_AFTER_GTIDS\',\'IO_BEFORE_GTIDS\',\'IS\',\'ITERATE\',\'JOIN\',\'KEY\',\'KEYS\',\'KILL\',\'LEADING\',\'LEAVE\',\'LEFT\',\'LIKE\',
     \'LIMIT\',\'LINEAR\',\'LINES\',\'LOAD\',\'LOCALTIME\',\'LOCALTIMESTAMP\',\'LOCK\',\'LONG\',\'LONGBLOB\',\'LONGTEXT\',\'LOOP\',\'LOW_PRIORITY\',
     \'MASTER_BIND\',\'MASTER_SSL_VERIFY_SERVER_CERT\',\'MATCH\',\'MAXVALUE\',\'MEDIUMBLOB\',\'MEDIUMINT\',\'MEDIUMTEXT\',\'MIDDLEINT\',
     \'MINUTE_MICROSECOND\',\'MINUTE_SECOND\',\'MOD\',\'MODIFIES\',\'NATURAL\',\'NOT\',\'NO_WRITE_TO_BINLOG\',\'NULL\',\'NUMERIC\',
     \'ON\',\'OPTIMIZE\',\'OPTIMIZER_COSTS\',\'OPTION\',\'OPTIONALLY\',\'OR\',\'ORDER\',\'OUT\',\'OUTER\',\'OUTFILE\',\'PARTITION\',\'PRECISION\',
     \'PRIMARY\',\'PROCEDURE\',\'PURGE\',\'RANGE\',\'READ\',\'READS\',\'READ_WRITE\',\'REAL\',\'REFERENCES\',\'REGEXP\',\'RELEASE\',\'RENAME\',
     \'REPEAT\',\'REPLACE\',\'REQUIRE\',\'RESIGNAL\',\'RESTRICT\',\'RETURN\',\'REVOKE\',\'RIGHT\',\'RLIKE\',\'SCHEMA\',\'SCHEMAS\',
     \'SECOND_MICROSECOND\',\'SELECT\',\'SENSITIVE\',\'SEPARATOR\',\'SET\',\'SHOW\',\'SIGNAL\',\'SMALLINT\',\'SPATIAL\',\'SPECIFIC\',
     \'SQL\',\'SQLEXCEPTION\',\'SQLSTATE\',\'SQLWARNING\',\'SQL_BIG_RESULT\',\'SQL_CALC_FOUND_ROWS\',\'SQL_SMALL_RESULT\',\'SSL\',
     \'STARTING\',\'STORED\',\'STRAIGHT_JOIN\',\'TABLE\',\'TERMINATED\',\'THEN\',\'TINYBLOB\',\'TINYINT\',\'TINYTEXT\',\'TO\',\'TRAILING\',
     \'TRIGGER\',\'TRUE\',\'UNDO\',\'UNION\',\'UNIQUE\',\'UNLOCK\',\'UNSIGNED\',\'UPDATE\',\'USAGE\',\'USE\',\'USING\',\'UTC_DATE\',\'UTC_TIME\',
     \'UTC_TIMESTAMP\',\'VALUES\',\'VARBINARY\',\'VARCHAR\',\'VARCHARACTER\',\'VARYING\',\'VIRTUAL\',\'WHEN\',\'WHERE\',\'WHILE\',\'WITH\',
     \'WRITE\',\'XOR\',\'YEAR_MONTH\',\'ZEROFILL\']', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content,
                              inner_share)
    VALUES (9, '', '限制表的字符集',
            '对于字符集选项 create table 语句必须设置为 \'character set #{character_set}\'，对于 alter table 语句若出现该选项也必须设置为 \'character set #{character_set}\'',
            '["MySQL"]', 'Table', 'DetectRules',
            '[{"name":"require","type":"bool","defaultValue":"true","range":["true","false"],"hint":"强制要求设置字符集"},{"name":"character_set","type":"string","defaultValue":"utf8mb4","range":["utf8","utf8mb4"],"hint":"允许使用的字符集"}]', '#define "require" as bool
        default "true"
        enum ["true", "false"]
        hint "强制要求设置字符集"
#define "character_set" as string
        default "utf8mb4"
        enum ["utf8", "utf8mb4"]
        hint "允许使用的字符集"

// 对于 alert 语句，只有当设置了 character set 选项时候才进行校验
if
  @domain.sqlType in [\'ALERT_TABLE\', \'ALERT_TABLE_CONVERT\'] and
  @func.string.isNotBlank(@domain.characterSet)
then

    if @func.string.isBlank(#{character_set}) then
      return false //规则配置错误
    else
      return @func.string.equalsIgnoreCase(@domain.characterSet, #{character_set})
    end

end


// 对于 create 语句，检测是否必须设置 character set 选项
if @domain.sqlType == \'CREATE_TABLE\' then

  if cast(#{require} as bool) then

    if @func.string.isBlank(#{character_set}) then
      return false //规则配置错误
    else
      return @func.string.equalsIgnoreCase(@domain.characterSet, #{character_set})
    end

  else

    if @func.string.isBlank(@domain.characterSet) then
      return true
    else
      return @func.string.equalsIgnoreCase(@domain.characterSet, #{character_set})
    end

  end

end', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content,
                              inner_share)
    VALUES (10, '', '限制表的排序规则',
            '对于排序规则选项 create table 语句必须设置为 \'collate #{collate}\'，对于 alter table 语句若出现该选项也必须设置为 \'collate #{collate}\'',
            '["MySQL"]', 'Table', 'DetectRules',
            '[{"name":"require","type":"bool","defaultValue":"true","range":["true","false"],"hint":"强制要求设置排序规则"},{"name":"collate","type":"string","defaultValue":"utf8mb4_unicode_ci","range":["utf8_unicode_ci","utf8mb4_unicode_ci"],"hint":"允许使用的排序规则"}]', '#define "require" as bool
        default "true"
        enum ["true", "false"]
        hint "强制要求设置排序规则"
#define "collate" as string
        default "utf8mb4_unicode_ci"
        enum ["utf8_unicode_ci", "utf8mb4_unicode_ci"]
        hint "允许使用的排序规则"

// 对于 alert 语句，只有当设置了 collate 选项时候才进行校验
if
  @domain.sqlType in [\'ALERT_TABLE\', \'ALERT_TABLE_CONVERT\'] and
  @func.string.isNotBlank(@domain.collate)
then

    if @func.string.isBlank(#{collate}) then
      return false //规则配置错误
    else
      return @func.string.equalsIgnoreCase(@domain.collate, #{collate})
    end

end


// 对于 create 语句，检测是否必须设置 collate 选项
if @domain.sqlType == \'CREATE_TABLE\' then

  if cast(#{require} as bool) then

    if @func.string.isBlank(#{collate}) then
      return false //规则配置错误
    else
      return @func.string.equalsIgnoreCase(@domain.collate, #{collate})
    end

  else

    if @func.string.isBlank(@domain.collate) then
      return true
    else
      return @func.string.equalsIgnoreCase(@domain.collate, #{collate})
    end

  end

end', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content,
                              inner_share)
    VALUES (11, '', '限制表的存储引擎',
            '对于存储引擎选项 create table 语句必须设置为 \'engine = #{engine}\'，对于 alter table 语句若出现该选项也必须设置为 \'engine = #{engine}\'',
            '["MySQL"]', 'Table', 'DetectRules',
            '[{"name":"require","type":"bool","defaultValue":"true","range":["true","false"],"hint":"强制要求指定存储引擎"},{"name":"engine","type":"string","defaultValue":"InnoDB","range":["InnoDB","MyISAM","Memory"],"hint":"允许使用的存储引擎"}]', '#define "require" as bool
        default "true"
        enum ["true", "false"]
        hint "强制要求指定存储引擎"
#define "engine" as string
        default "InnoDB"
        enum ["InnoDB", "MyISAM", "Memory"]
        hint "允许使用的存储引擎"

// 对于 alert 语句，只有当设置了 engine 选项时候才进行校验
if
  @domain.sqlType in [\'ALERT_TABLE\', \'ALERT_TABLE_CONVERT\'] and
  @func.string.isNotBlank(@domain.options.engine)
then

    if @func.string.isBlank(#{engine}) then
      return false //规则配置错误
    else
      return @func.string.equalsIgnoreCase(@domain.options.engine, #{engine})
    end

end

// 对于 create 语句，检测是否必须设置 engine 选项
if @domain.sqlType == \'CREATE_TABLE\' then

  if cast(#{require} as bool) then

    if @func.string.isBlank(#{engine}) then
      return false //规则配置错误
    else
      return @func.string.equalsIgnoreCase(@domain.options.engine, #{engine})
    end

  else

    if @func.string.isBlank(@domain.options.engine) then
      return true
    else
      return @func.string.equalsIgnoreCase(@domain.options.engine, #{engine})
    end

  end

end
', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content,
                              inner_share)
    VALUES (12, '', '表需要包含某些列', '在 create table 建表语句中必须表需要包含这些列 #{columns}', '["MySQL"]',
            'Table',
            'DetectRules',
            '[{"name":"columns","type":"string","defaultValue":"id,create_gmt,modify_gmt","range":null,"hint":"需要包含的列名称，多个列使用逗号区分"}]', '#define "columns" as string
        default "id,create_gmt,modify_gmt"
        hint "需要包含的列名称，多个列使用逗号区分"

if
  @domain.sqlType != \'CREATE_TABLE\'
then
  return true
end

return @func.string.split(#{columns}, \', \') in @domain.columns', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content,
                              inner_share)
    VALUES (13, '', '限制表名最大长度', '根据规范要求某个表的名称超过了最大 #{length} 长度限制', '["MySQL"]', 'Table',
            'DetectRules', '[{"name":"length","type":"int","defaultValue":"30","range":null,"hint":"表名的最大长度"}]', '#define "length" as int
        default "30"
        hint "表名的最大长度"

if @domain.sqlType in [\'CREATE_TABLE\', \'CREATE_TABLE_SELECT\', \'CREATE_TABLE_LIKE\'] then
  checkName = @domain.table
elseif @domain.sqlType in [\'RENAME_TABLE\', \'ALERT_TABLE_RENAME\'] then
  checkName = @domain.newName
else
  return true
end

return @func.string.length(checkName) <= cast(#{length} as int)', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content,
                              inner_share)
    VALUES (14, '', '修改表的字符集或排序规则', '在修改表的字符集和排序规则时请使用 alert table convert 语法',
            '["MySQL"]',
            'Table', 'DetectRules', '[]', 'if
  @domain.sqlType != \'ALERT_TABLE\'
then
  return true
end

if
  @func.string.isNotBlank(@domain.collate) or
  @func.string.isNotBlank(@domain.characterSet)
then
  return false
end', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content,
                              inner_share)
    VALUES (15, '', '限制列的字符集',
            '对于字符集选项 create table 语句中列中必须设置为 \'character set #{character_set}\'，对于 alter table modify/change 语句若出现该选项也必须设置为 \'character set #{character_set}\'',
            '["MySQL"]', 'Column', 'DetectRules',
            '[{"name":"require","type":"bool","defaultValue":"false","range":["true","false"],"hint":"强制要求设置字符集"},{"name":"character_set","type":"string","defaultValue":"utf8mb4","range":["utf8","utf8mb4"],"hint":"允许使用的字符集"}]', '#define "require" as bool
        default "false"
        enum ["true", "false"]
        hint "强制要求设置字符集"
#define "character_set" as string
        default "utf8mb4"
        enum ["utf8", "utf8mb4"]
        hint "允许使用的字符集"

// 对于 modify/change 语句，只有当设置了 character set 选项时候才进行校验
if
  @domain.sqlType == \'ALERT_TABLE_ALERT_COLUMN\' and
  @func.string.isNotBlank(@domain.characterSet)
then

    if @func.string.isBlank(#{character_set}) then
      return false //规则配置错误
    else
      return @func.string.equalsIgnoreCase(@domain.characterSet, #{character_set})
    end

end

// 对于 add column or create table 语句，检测是否必须设置 character set 选项
if @domain.sqlType in [\'CREATE_TABLE_ADD_COLUMN\', \'ALERT_TABLE_ADD_COLUMN\'] then

  if cast(#{require} as bool) then

    if @func.string.isBlank(#{character_set}) then
      return false //规则配置错误
    else
      return @func.string.equalsIgnoreCase(@domain.characterSet, #{character_set})
    end

  else

    if @func.string.isBlank(@domain.characterSet) then
      return true
    else
      return @func.string.equalsIgnoreCase(@domain.characterSet, #{character_set})
    end

  end

end', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content,
                              inner_share)
    VALUES (16, '', '限制列的排序规则',
            '对于排序规则选项 create table 语句中列中必须设置为 \'collate #{collate}\'，对于 alter table modify/change 语句若出现该选项也必须设置为 \'collate #{collate}\'',
            '["MySQL"]', 'Column', 'DetectRules',
            '[{"name":"require","type":"bool","defaultValue":"false","range":["true","false"],"hint":"强制要求设置排序规则"},{"name":"collate","type":"string","defaultValue":"utf8mb4_unicode_ci","range":["utf8_unicode_ci","utf8mb4_unicode_ci"],"hint":"允许使用的排序规则"}]', '#define "require" as bool
        default "false"
        enum ["true", "false"]
        hint "强制要求设置排序规则"
#define "collate" as string
        default "utf8mb4_unicode_ci"
        enum ["utf8_unicode_ci", "utf8mb4_unicode_ci"]
        hint "允许使用的排序规则"

// 对于 modify/change 语句，只有当设置了 collate 选项时候才进行校验
if
  @domain.sqlType == \'ALERT_TABLE_ALERT_COLUMN\' and
  @func.string.isNotBlank(@domain.collate)
then

    if @func.string.isBlank(#{collate}) then
      return false //规则配置错误
    else
      return @func.string.equalsIgnoreCase(@domain.collate, #{collate})
    end

end

// 对于 add column or create table 语句，检测是否必须设置 collate 选项
if @domain.sqlType in [\'CREATE_TABLE_ADD_COLUMN\', \'ALERT_TABLE_ADD_COLUMN\'] then

  if cast(#{require} as bool) then

    if @func.string.isBlank(#{collate}) then
      return false //规则配置错误
    else
      return @func.string.equalsIgnoreCase(@domain.collate, #{collate})
    end

  else

    if @func.string.isBlank(@domain.collate) then
      return true
    else
      return @func.string.equalsIgnoreCase(@domain.collate, #{collate})
    end

  end

end', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content,
                              inner_share)
    VALUES (17, '', '列上的字符集与排序规则',
            '列上的字符集或排序规则根据配置可能被部分禁止，建议您根据需要在表级别或者库级别中指定。当前规范的限制为 #{allow}',
            '["MySQL"]', 'Column', 'DetectRules',
            '[{"name":"allow","type":"string","defaultValue":"Both","range":["Nothing","Both","Character set","Collate"],"hint":"是否允许字符集和排序规则出现在列上"}]', '#define "allow" as string
        default "Both"
        enum ["Nothing", "Both", "Character set", "Collate"]
        hint "是否允许字符集和排序规则出现在列上"

if
  @domain.sqlType in [\'CREATE_TABLE_ADD_COLUMN\', \'ALERT_TABLE_ADD_COLUMN\', \'ALERT_TABLE_ALERT_COLUMN\']
then

  if @func.string.isNotBlank(@domain.characterSet) then
    if #{allow} not in [\'Both\', \'Character set\'] then
      return false
    end
  end

  if @func.string.isNotBlank(@domain.collate) then
    if #{allow} not in [\'Both\', \'Collate\'] then
      return false
    end
  end

end', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content,
                              inner_share)
    VALUES (18, '', '列禁用 Zerofill 属性', '规范禁止使用 zerofill 属性', '["MySQL"]', 'Column', 'DetectRules', '[]', 'if
  @domain.sqlType not in [\'CREATE_TABLE_ADD_COLUMN\', \'ALERT_TABLE_ADD_COLUMN\', \'ALERT_TABLE_ALERT_COLUMN\']
then
  return true
end

return !cast(@domain.options.zerofill as bool)', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content,
                              inner_share)
    VALUES (19, '', '列必须有注释', '新增的列必须有注释，修改列时若指定了注释则必须不能为空', '["MySQL"]', 'Column',
            'DetectRules', '[]', 'if
  @domain.sqlType in [\'CREATE_TABLE_ADD_COLUMN\', \'ALERT_TABLE_ADD_COLUMN\']
then
  return @func.string.isNotBlank(@domain.comment)
end

if
  @domain.sqlType == \'ALERT_TABLE_ALERT_COLUMN\' and
  @domain.comment != null
then
  return @func.string.isNotBlank(@domain.comment)
end', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content,
                              inner_share)
    VALUES (20, '', '列必须有默认值', '新增的列必须有默认值', '["MySQL"]', 'Column',
            'DetectRules', '[]', 'if
  @domain.sqlType in [\'CREATE_TABLE_ADD_COLUMN\', \'ALERT_TABLE_ADD_COLUMN\']
then
  return @domain.defaultValue != null
end

if
  @domain.sqlType == \'ALERT_TABLE_ALERT_COLUMN\' and
  @domain.defaultValue != null
then
  return @func.string.isNotBlank(@domain.defaultValue)
end', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content,
                              inner_share)
    VALUES (21, '', '列名不能是关键字',
            '不能使用关键字作为列名，关键字清单可参考官方文档： https://dev.mysql.com/doc/refman/8.4/en/keywords.html',
            '["MySQL"]', 'Column', 'DetectRules', '[]',
            'if @domain.sqlType in [\'CREATE_TABLE_ADD_COLUMN\', \'ALERT_TABLE_ADD_COLUMN\'] then
      checkName = @domain.column
    elseif @domain.sqlType == \'ALERT_TABLE_ALERT_COLUMN\' then
      if @func.string.isBlank(@domain.newName) then
        return true
      else
        checkName = @domain.newName
      end
    else
      return true
    end

    return @func.string.upperCase(checkName) not in [\'ACCESSIBLE\',\'ADD\',\'ALL\',\'ALTER\',\'ANALYZE\',\'AND\',\'AS\',\'ASC\',\'ASENSITIVE\',
         \'BEFORE\',\'BETWEEN\',\'BIGINT\',\'BINARY\',\'BLOB\',\'BOTH\',\'BY\',
         \'CALL\',\'CASCADE\',\'CASE\',\'CHANGE\',\'CHAR\',\'CHARACTER\',\'CHECK\',\'COLLATE\',\'COLUMN\',\'CONDITION\',\'CONSTRAINT\',\'CONTINUE\',
         \'CONVERT\',\'CREATE\',\'CROSS\',\'CURRENT_DATE\',\'CURRENT_TIME\',\'CURRENT_TIMESTAMP\',\'CURRENT_USER\',\'CURSOR\',
         \'DATABASE\',\'DATABASES\',\'DAY_HOUR\',\'DAY_MICROSECOND\',\'DAY_MINUTE\',\'DAY_SECOND\',\'DEC\',\'DECIMAL\',\'DECLARE\',\'DEFAULT\',
         \'DELAYED\',\'DELETE\',\'DESC\',\'DESCRIBE\',\'DETERMINISTIC\',\'DISTINCT\',\'DISTINCTROW\',\'DIV\',\'DOUBLE\',\'DROP\',\'DUAL\',
         \'EACH\',\'ELSE\',\'ELSEIF\',\'ENCLOSED\',\'ESCAPED\',\'EXISTS\',\'EXIT\',\'EXPLAIN\',
         \'FALSE\',\'FETCH\',\'FLOAT\',\'FLOAT4\',\'FLOAT8\',\'FOR\',\'FORCE\',\'FOREIGN\',\'FROM\',\'FULLTEXT\',\'GENERATED\',\'GET\',\'GRANTint\',\'GROUP\',
         \'HAVING\',\'HIGH_PRIORITY\',\'HOUR_MICROSECOND\',\'HOUR_MINUTE\',\'HOUR_SECOND\',\'IF\',\'IGNORE\',\'IN\',\'INDEX\',\'INFILE\',
         \'INNER\',\'INOUT\',\'INSENSITIVE\',\'INSERT\',\'INT\',\'INT1\',\'INT2\',\'INT3\',\'INT4\',\'INT8\',\'INTEGER\',\'INTERVALint\',\'INTO\',
         \'IO_AFTER_GTIDS\',\'IO_BEFORE_GTIDS\',\'IS\',\'ITERATE\',\'JOIN\',\'KEY\',\'KEYS\',\'KILL\',\'LEADING\',\'LEAVE\',\'LEFT\',\'LIKE\',
         \'LIMIT\',\'LINEAR\',\'LINES\',\'LOAD\',\'LOCALTIME\',\'LOCALTIMESTAMP\',\'LOCK\',\'LONG\',\'LONGBLOB\',\'LONGTEXT\',\'LOOP\',\'LOW_PRIORITY\',
         \'MASTER_BIND\',\'MASTER_SSL_VERIFY_SERVER_CERT\',\'MATCH\',\'MAXVALUE\',\'MEDIUMBLOB\',\'MEDIUMINT\',\'MEDIUMTEXT\',\'MIDDLEINT\',
         \'MINUTE_MICROSECOND\',\'MINUTE_SECOND\',\'MOD\',\'MODIFIES\',\'NATURAL\',\'NOT\',\'NO_WRITE_TO_BINLOG\',\'NULL\',\'NUMERIC\',
         \'ON\',\'OPTIMIZE\',\'OPTIMIZER_COSTS\',\'OPTION\',\'OPTIONALLY\',\'OR\',\'ORDER\',\'OUT\',\'OUTER\',\'OUTFILE\',\'PARTITION\',\'PRECISION\',
         \'PRIMARY\',\'PROCEDURE\',\'PURGE\',\'RANGE\',\'READ\',\'READS\',\'READ_WRITE\',\'REAL\',\'REFERENCES\',\'REGEXP\',\'RELEASE\',\'RENAME\',
         \'REPEAT\',\'REPLACE\',\'REQUIRE\',\'RESIGNAL\',\'RESTRICT\',\'RETURN\',\'REVOKE\',\'RIGHT\',\'RLIKE\',\'SCHEMA\',\'SCHEMAS\',
         \'SECOND_MICROSECOND\',\'SELECT\',\'SENSITIVE\',\'SEPARATOR\',\'SET\',\'SHOW\',\'SIGNAL\',\'SMALLINT\',\'SPATIAL\',\'SPECIFIC\',
         \'SQL\',\'SQLEXCEPTION\',\'SQLSTATE\',\'SQLWARNING\',\'SQL_BIG_RESULT\',\'SQL_CALC_FOUND_ROWS\',\'SQL_SMALL_RESULT\',\'SSL\',
         \'STARTING\',\'STORED\',\'STRAIGHT_JOIN\',\'TABLE\',\'TERMINATED\',\'THEN\',\'TINYBLOB\',\'TINYINT\',\'TINYTEXT\',\'TO\',\'TRAILING\',
         \'TRIGGER\',\'TRUE\',\'UNDO\',\'UNION\',\'UNIQUE\',\'UNLOCK\',\'UNSIGNED\',\'UPDATE\',\'USAGE\',\'USE\',\'USING\',\'UTC_DATE\',\'UTC_TIME\',
         \'UTC_TIMESTAMP\',\'VALUES\',\'VARBINARY\',\'VARCHAR\',\'VARCHARACTER\',\'VARYING\',\'VIRTUAL\',\'WHEN\',\'WHERE\',\'WHILE\',\'WITH\',
         \'WRITE\',\'XOR\',\'YEAR_MONTH\',\'ZEROFILL\']', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content,
                              inner_share)
    VALUES (22, '', '列名拼写规则', '列名需要满足 #{caseType} 拼写规则', '["MySQL"]', 'Column', 'DetectRules',
            '[{"name":"caseType","type":"string","defaultValue":"Lower case","range":["Lower case","Upper case","Lower camel case","Upper camel case"],"hint":"表名拼写规则"}]', '#define "caseType" as string
        default "Lower case"
        enum ["Lower case", "Upper case", "Lower camel case", "Upper camel case"]
        hint "表名拼写规则"

if @domain.sqlType in [\'CREATE_TABLE_ADD_COLUMN\', \'ALERT_TABLE_ADD_COLUMN\'] then
  checkName = @domain.column
elseif @domain.sqlType == \'ALERT_TABLE_ALERT_COLUMN\' then
  if @func.string.isBlank(@domain.newName) then
    return true
  else
    checkName = @domain.newName
  end
else
  return true
end

// ref document https://newbedev.com/regex-for-pascalcased-words-aka-camelcased-with-leading-uppercase-letter

if #{caseType} == \'Lower case\' then
  return @func.string.lowerCase(checkName) == checkName

elseif #{caseType} == \'Upper case\' then

  return @func.string.upperCase(checkName) == checkName

elseif #{caseType} == \'Lower camel case\' then
  return checkName matches \'[a-z]+((\\d)|([A-Z0-9][a-z0-9]+))*([A-Z])?\'

elseif #{caseType} == \'Upper camel case\' then
  return checkName matches \'([A-Z][a-z0-9]+)((\\d)|([A-Z0-9][a-z0-9]+))*([A-Z])?\'

else

  return false
end', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content,
                              inner_share)
    VALUES (23, '', '限制列名最大长度',
            '根据规范要求某个列的名称超过了最大 #{length} 长度限制，请检查 crate table 语句以及 alert table xxx change 语句',
            '["MySQL"]', 'Column', 'DetectRules',
            '[{"name":"length","type":"int","defaultValue":"30","range":null,"hint":"列名的最大长度"}]', '#define "length" as int
        default "30"
        hint "列名的最大长度"

if @domain.sqlType in [\'CREATE_TABLE_ADD_COLUMN\', \'ALERT_TABLE_ADD_COLUMN\'] then
  checkName = @domain.column
elseif @domain.sqlType == \'ALERT_TABLE_ALERT_COLUMN\' then
  if @func.string.isBlank(@domain.newName) then
    return true
  else
    checkName = @domain.newName
  end
else
  return true
end

return @func.string.length(checkName) <= cast(#{length} as int)', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content,
                              inner_share)
    VALUES (24, '', '限制CHAR/NCHAR类型最大长度', '根据规范要求某个列的  CHAR/NCHAR类型长度超过了最大 #{length} 限制',
            '["MySQL"]', 'Column', 'DetectRules',
            '[{"name":"length","type":"int","defaultValue":"120","range":null,"hint":"字段最大长度"}]', '#define "length" as int
        default "120"
        hint "字段最大长度"

if
  @domain.sqlType not in [\'CREATE_TABLE_ADD_COLUMN\', \'ALERT_TABLE_ADD_COLUMN\', \'ALERT_TABLE_ALERT_COLUMN\']
then
  return true
end

if
  @func.string.startsWith(@domain.typeDesc, "char") or
  @func.string.startsWith(@domain.typeDesc, "nchar")
then

  if @func.string.isBlank(@domain.length) then
    return false // 语句中长度为空，例如 create table abc(name char);
  else
    return cast(@domain.length as int) <= cast(#{length} as int)
  end

end', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content,
                              inner_share)
    VALUES (25, '', '限制VARCHAR/NVARCHAR类型最大长度',
            '根据规范要求某个列的 VARCHAR/NVARCHAR 类型长度超过了最大 #{length} 限制', '["MySQL"]', 'Column',
            'DetectRules',
            '[{"name":"length","type":"int","defaultValue":"500","range":null,"hint":"字段最大长度"}]', '#define "length" as int
        default "500"
        hint "字段最大长度"

if
  @domain.sqlType not in [\'CREATE_TABLE_ADD_COLUMN\', \'ALERT_TABLE_ADD_COLUMN\', \'ALERT_TABLE_ALERT_COLUMN\']
then
  return true
end

if
  @func.string.startsWith(@domain.typeDesc, "varchar") or
  @func.string.startsWith(@domain.typeDesc, "nvarchar")
then

  if @func.string.isBlank(@domain.length) then
    return false // 语句中长度为空，例如 create table abc(name char);
  else
    return cast(@domain.length as int) <= cast(#{length} as int)
  end

end', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content,
                              inner_share)
    VALUES (26, '', '限制列删除', '规则描述：规范要求列不能被删除', '["MySQL"]', 'Column', 'DetectRules',
            '[{"name":"allow","type":"string","defaultValue":"true","range":["true","false"],"hint":"是否允许删除列"}]', '#define "allow" as string
        default "true"
        enum ["true", "false"]
        hint "是否允许删除列"

if
  @domain.sqlType == \'ALERT_TABLE_DROP_COLUMN\' and
  !cast(#{allow} as bool)
then
  return false
end', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content,
                              inner_share)
    VALUES (27, '', '限制列的类型',
            '规范要求列类型需要满足 #{ruleType} (AllowList 允许, BlockList 禁止的)名单要求，具体类型清单为：#{typeList}',
            '["MySQL"]', 'Column', 'DetectRules',
            '[{"name":"ruleType","type":"string","defaultValue":"AllowList","range":["AllowList","BlockList"],"hint":"名单工作模式"},{"name":"typeList","type":"string","defaultValue":"bit,tinyint,smallint,mediumint,int,integer,bigint,decimal,numeric,float,double,date,datetime,timestamp,time,char,varchar,binary,tinyblob,blob,mediumblob,longblob,tinytext,text,mediumtext,longtext,enum,set,json","range":null,"hint":"允许或禁止的类型列表"}]', '#define "ruleType" as string
        default "AllowList"
        enum ["AllowList", "BlockList"]
        hint "名单工作模式"
#define "typeList" as string
        default "bit,tinyint,smallint,mediumint,int,integer,bigint,decimal,numeric,float,double,date,datetime,timestamp,time,char,varchar,binary,tinyblob,blob,mediumblob,longblob,tinytext,text,mediumtext,longtext,enum,set,json"
        hint "允许或禁止的类型列表"

if
  @domain.sqlType not in [\'CREATE_TABLE_ADD_COLUMN\', \'ALERT_TABLE_ADD_COLUMN\', \'ALERT_TABLE_ALERT_COLUMN\']
then
  return true
end

if
  @func.string.isBlank(#{typeList})
then
  return true
end

if
  #{ruleType} == \'AllowList\'
then
  return @domain.typeName in @func.string.split(#{typeList}, \', \')
end

if
  #{ruleType} == \' BlockList\'
then
  return @domain.typeName not in @func.string.split(#{typeList}, \', \')
end

return false', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content,
                              inner_share)
    VALUES (28, '', '限制列不允许为空', '根据规范要求列必须具有 \'NOT NULL\' 选项', '["MySQL"]', 'Column',
            'DetectRules',
            '[]', 'if
  @domain.sqlType in [\'CREATE_TABLE_ADD_COLUMN\', \'ALERT_TABLE_ADD_COLUMN\', \'ALERT_TABLE_ALERT_COLUMN\']
then
  return !@domain.nullable
end', 1);
    INSERT INTO dm_sec_rules (id, owner_uid, rule_name, rule_desc, ds_range, rule_target, rule_type, rule_def,
                              rule_content,
                              inner_share)
    VALUES (29, '', '限制表外键', '创建或修改表结构时不允许使用表外键', '["MySQL"]', 'Constraint', 'DetectRules',
            '[{"name":"allow","type":"bool","defaultValue":"false","range":["true","false"],"hint":"是否允许使用外键约束"}]', '#define "allow" as bool
        default "false"
        enum ["true", "false"]
        hint "是否允许使用外键约束"

if
  @domain.sqlType in [\'CREATE_TABLE_ADD_CONSTRAINT\', \'ALERT_TABLE_ADD_CONSTRAINT\']
then

  if @domain.type == \'ForeignKey\' then
    return cast(#{allow} as bool)
  else
    return true
  end

end', 1);

-- sample user and worker (end) --------------------
end
$$

call proc_init();