# MySQL DAL 执行与会话模式

## Purpose

验证 SQL 工作台能够执行 MySQL TABLE 执行计划，并在多次点击之间使用当前查询会话的 SQL_MODE。

## Scope

- 路由：`/#/sql`；入口：“执行”“执行计划”和事务模式菜单。
- 覆盖 EXPLAIN / DESC / DESCRIBE、ANALYZE、FORMAT=TREE、排序和分页，以及 ANSI_QUOTES、NO_BACKSLASH_ESCAPES 的会话切换。
- 不覆盖同一批 SQL 内先改变解析模式、后执行依赖新模式的语句；设置模式与验证语句分次执行。
- 不覆盖全局配置修改和账号权限配置。

## Preconditions

- 本地 CloudDM 已启动并完成插件加载，Chrome 已登录可查询测试数据源的账号。
- 使用隔离 MySQL 8.0.21+ 数据源。页面上的事务模式和 SQL_MODE 值必须实际确认。
- 每次编辑后确认选中的 SQL 与目标一致，再点击执行，防止误执行旧选区。

## Test Data

- 在隔离 schema 中使用带本次唯一后缀的测试表，例如 `codex_dal_<suffix>`，列为 `id INT PRIMARY KEY, amount DECIMAL(10,2)`，三行分别为 `(1,10.50)`、`(2,20.00)`、`(3,30.50)`。也可使用已确认结构和数据相同的专用测试表。
- 以下 `<table>` 替换为实际 schema 和表名；双引号场景应分别引用 schema 和表名。
- 在手动事务会话内执行 `SET @codex_old_mode=@@SESSION.sql_mode;` 保存模式；恢复时使用该变量。

## Suites

### DAL-TABLE-01 TABLE 计划与实际执行（P0）

- 初始状态：自动事务，查询页签已连接专用测试表。
- 操作：将 `EXPLAIN`、`DESC`、`DESCRIBE` 分别与 `ANALYZE`、`ANALYZE FORMAT=TREE`、`FORMAT=JSON` 组合；执行 `TABLE <table>`，并覆盖无后缀、`ORDER BY id DESC LIMIT 1,2`、`ORDER BY id LIMIT 2 OFFSET 1`。
- 预期：每条语句都有对应结果页签；ANALYZE 返回实际执行时间和行数，分页计划中的实际输出行数为 2；无 UNKNOWN、NoSuchElementException 或血缘分析异常。
- 清理：关闭新增结果页签，保留测试表供模式场景使用。

### DAL-MODE-01 跨次执行使用当前模式（P0）

- 初始状态：切换“手动事务”，保存原始模式。
- 操作：执行 `SET SESSION sql_mode=CONCAT(@@SESSION.sql_mode,',ANSI_QUOTES,NO_BACKSLASH_ESCAPES');`，通过单独查询 `@@SESSION.sql_mode` 确认生效。
- 再次点击执行：查询双引号引用的表和字段；执行带双引号的 TABLE 计划；执行 `SELECT 'trailing backslash\' AS value;`，并在带表查询的 WHERE 中使用同样的字符串。
- 对含双引号标识符和末尾反斜杠字符串的 SELECT 点击“执行计划”。
- 预期：SELECT 返回正确行值；字符串保留末尾反斜杠；所有 TABLE 计划和按钮生成的计划均返回结果，无解析或改写错误。
- 清理：继续 DAL-MODE-02，不在中途关闭会话。

### DAL-MODE-02 模式清空、错误恢复与会话结束（P0）

- 初始状态：沿用 DAL-MODE-01 的手动事务会话。
- 操作：单独执行 `SET SESSION sql_mode='';`；下一次执行 `SELECT "id" AS mode_probe FROM <table> ORDER BY id;`，表名使用普通标识符或反引号。
- 预期：三行 mode_probe 均为字符串 `id`；此时双引号限定表名的 TABLE 计划会出现定位明确的语法错误，不能沿用旧 ANSI_QUOTES 模式。
- 恢复：执行 `SET SESSION sql_mode=@codex_old_mode;`，查询模式确认与保存值一致；查询行数 3、amount 总和 61.00；点击“回滚”，切换“自动事务”。
- 再执行普通 SELECT，预期可以继续查询，原来的模式和事务状态不会泄漏到新会话。

## Cleanup

- 在原手动事务会话中恢复模式，结束事务并切回自动事务。
- 仅删除本次创建的专用测试表；复用的测试表不删除。
- 保留用户原有查询页签和内容。

## Skip Conditions

- 无隔离 MySQL 数据源时跳过实际执行，不能将解析器单测当作页面通过。
- 低于 MySQL 8.0.21 时跳过显式 ANALYZE FORMAT=TREE；报告实际版本与未覆盖项。
- 无法保持同一查询会话时，模式场景标记为未验证，不使用自动事务下的跨次点击代替。
