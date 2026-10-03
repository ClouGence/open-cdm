# HANA 查询授权与 SQL 审核

## Purpose

验证 HANA 工作台在真正执行 SQL 前检查真实对象的读、写、程序权限和查询规则；重点防止 CTE、别名、子查询、MERGE、块内写入以及无法解析语句绕过检查。

## Scope

- `/#/sql` 的查询提交、执行信息、规则违规详情；安全规则/规范配置入口按当前用户菜单进入。
- 后端对应 `HanaBehaviorAnalysisSpi`、`HanaSqlAnalyzer`、`HanaSecDomainResolveSpi`、`HanaSecRulesSupportSpi` 及现有平台授权/规则链路。
- 不覆盖列血缘/脱敏、程序内部权限展开、跨数据库远程对象、DDL 编辑器生成、工单和 CI/CD。
- 语句选择及补全沿用 [SQL 语句选择流程](../sql/sql_statement_selection.md)，违规详情排版沿用 [规则违规提示流程](query_rule_violation_message.md)。

## Preconditions

- 本地 CloudDM 服务可用，@Browser 已登录；HANA Express 和第 6～8 步组合运行包已部署并重启加载。
- 使用隔离 tenant 的一次性 schema；服务账号、平台用户和查询规则仅用于本次验证。不要修改生产或共享规范。
- 准备平台 OWNER、仅 READ、无对象权限、具备 WRITE 但不具备 PROGRAM 的用户。数据库账号分别准备可写和只读身份，区分平台拦截与数据库拒绝。
- OWNER 创建下文样本；只读连接能够独立核对结果。用户无 SQL 页面权限时只检查菜单/路由拒绝，不绕过权限执行。
- 平台查询规则功能启用；准备 HANA 规范，将内置无 WHERE 的 UPDATE/DELETE 规则设为阻断，参数 `allow=false`。按当前页面核实关联实例、schema、表与规则等级。

## Test Data

使用唯一后缀 `<run>` 创建 `H8_A_<run>`、`H8_B_<run>`。由 OWNER 在原生客户端逐条准备并记录原始行数/值；下面是模板，替换 schema 后使用，不带真实凭据。

```sql
CREATE SCHEMA H8_A_<run>;
CREATE SCHEMA H8_B_<run>;
CREATE COLUMN TABLE H8_A_<run>.T (ID INTEGER PRIMARY KEY, V INTEGER);
CREATE COLUMN TABLE H8_B_<run>.S (ID INTEGER PRIMARY KEY, V INTEGER);
INSERT INTO H8_A_<run>.T VALUES (1, 10);
INSERT INTO H8_B_<run>.S VALUES (1, 20);
CREATE PROCEDURE H8_A_<run>.P() LANGUAGE SQLSCRIPT AS
BEGIN
    SELECT ID, V FROM H8_A_<run>.T;
END;
```

- 数据库可写账号具有两表读取/修改权限及 P 的调用权限；平台 READ 用户只授予 T 的 READ，不授予 S 的 READ、T 的 WRITE 和 P 的 PROGRAM。
- 另备平台具备 T/S READ、T WRITE、P PROGRAM 的用户进行正例；数据库只读账号用于反向对照。具体 GRANT 按已部署版本和角色契约核对，不能使用管理员账号代替权限正反例。
- 所有 SQL 使用替换后的显式 schema。稳定静态样本见 [08-analysis.sql](../../datasource/hana/sql/08-analysis.sql)，该文件只用于分析，不能整批执行。

## Suites

### H8-SMOKE-01 查询与程序权限

- 风险/目的：P0，READ 和 PROGRAM 必须分别检查。
- 初始路由与状态：`/#/sql`，选择隔离数据源及 A schema，数据库可写账号，平台 READ 用户。
- 测试数据与准备：上述 T、S、P，初始 T 为 `(1, 10)`。
- @Browser 操作：执行 `SELECT * FROM A.T`，再执行 `CALL A.P()`；切换具有 PROGRAM 的用户再调用 P（A 替换为完整 schema）。
- 预期结果：第一条显示 T 的一行；无 PROGRAM 时调用明确拒绝，有 PROGRAM 时只读过程返回结果。对照未授权 S 的普通 SELECT 也应拒绝。
- 恢复/清理：关闭结果，恢复 READ 用户；没有数据修改。

### H8-PERMISSION-01 跨 schema 与间接引用

- 风险/目的：P0，虚拟名称不能替代真实对象鉴权。
- 初始路由与状态：沿用 H8-SMOKE-01，平台无 S 权限。
- 测试数据与准备：T/S 已存在，数据库账号有两表 SELECT 权限。
- @Browser 操作：分别执行 `WITH C AS (SELECT * FROM B.S) SELECT * FROM C`、T JOIN B.S、`SELECT * FROM A.T WHERE EXISTS (SELECT 1 FROM B.S)`。
- 预期结果：三种写法均因 B.S 未授权而拒绝，不显示其结果；为独立正例用户授予 S 的 READ 后相同查询成功。错误中的资源应指向真实 S，不是 C 或别名。
- 恢复/清理：关闭结果，移除本次新增的临时授权。

### H8-PERMISSION-02 写操作与 SQLScript

- 风险/目的：P0，平台 READ 用户搭配 DB RW 时仍不能写入。
- 初始路由与状态：`/#/sql`，READ 用户，规则不作为此场景的唯一阻断来源。
- 测试数据与准备：独立只读连接确认 T.V=10；所有语句仅针对测试表。
- @Browser 操作：分别尝试 `UPDATE A.T SET V=99 WHERE ID=1`、`UPDATE X SET V=99 FROM A.T X WHERE X.ID=1`、MERGE A.T USING B.S 的 UPDATE 分支、`DO BEGIN SELECT * FROM A.T; UPDATE A.T SET V=99 WHERE ID=1; END;`。
- 预期结果：全部拒绝；DO 不出现先执行内部 SELECT 再尝试写入的拆分行为。独立连接确认 T.V 仍为 10。反向对照平台 WRITE + DB RO 仍由数据库拒绝，错误不能伪报成功。
- 恢复/清理：关闭结果；如测试失败导致修改，OWNER 恢复 T.V=10，记录失败，不重复尝试共享数据。

### H8-RULE-01 规则与真实表范围

- 风险/目的：P0，规则不能因空 resolver 或 CTE 名称匹配失败而跳过。
- 初始路由与状态：`/#/sql`，平台及数据库均有测试表写权限；隔离 HANA 规范启用阻断规则。
- 测试数据与准备：T 初始值已记录，内置无 WHERE UPDATE/DELETE 规则 `allow=false`；另配置仅匹配 B.S 的“查询不允许星号”阻断规则。
- @Browser 操作：执行无 WHERE 的 UPDATE/DELETE，打开违规详情；用 `WITH C AS (SELECT ID,V FROM B.S) SELECT * FROM C` 检查外层星号规则。再用带 WHERE 的 UPDATE（V 仍设置为原值）及显式列 SELECT 验证正例。
- 预期结果：前三项命中对应规则并阻止执行，显示规则名/行号/违规原因；符合规则的正例通过。未匹配该表的查询不应误命中 B.S 的规则；独立连接确认拒绝项没有改变数据。
- 恢复/清理：移除本次隔离规则/规范关联，保留原配置。

### H8-FAILURE-01 未支持语法与恢复

- 风险/目的：P0，动态或无法解析的 SQL 不得被当作只读或审核通过。
- 初始路由与状态：`/#/sql`，OWNER，当前编辑器 schema 为 A。
- 测试数据与准备：测试表 T，记录原值；仅提交下列隔离对象语句。
- @Browser 操作：逐条提交 `DO BEGIN EXECUTE IMMEDIATE 'UPDATE A.T SET V=99'; END;`、`SELECT * FROM REMOTE.S.T`、`SELECT * FROM :UNDECLARED`、`UPDATE A.T SET V=99 GARBAGE`、`SET SCHEMA B`；随后改成正常 SELECT 重新执行。
- 预期结果：失败项给出分析错误及原因/位置，不出现审核通过或执行成功；T 不变。正常 SELECT 可再次执行；SET SCHEMA 失败后编辑器上下文仍为 A。
- 恢复/清理：清空错误 SQL，关闭结果；不改变语法校验配置来绕过拒绝。

### H8-REPEAT-01 重复提交、刷新与权限变化

- 风险/目的：P1，失败和旧结果不能污染下一次请求的权限判断。
- 初始路由与状态：两个隔离 HANA 查询页签，一页有权限、一页引用无权对象。
- 测试数据与准备：只读 SELECT 和前述拒绝语句；不做并发写入。
- @Browser 操作：重复提交拒绝语句，在两页切换，刷新后重新进入；撤销独立测试用户的临时 READ 授权并在权限刷新后再次查询。可安全模拟网络中断时，仅重试只读 SQL。
- 预期结果：每次反馈对应当前 SQL 和资源；失败后正常 SELECT 可执行，权限撤销后不能沿用旧结果获取新数据；断网/恢复不显示虚假的审核/执行成功。
- 恢复/清理：恢复网络和临时权限，关闭页签。

边界覆盖包括跨 schema、别名、CTE 和不支持语法；引号/Unicode、长文档、视口和生命周期沿用 SQL 语句选择流程。新测试表只有一行，不进行大数据或并发写入压测；SQL 解析没有新增文档长度上限可作为边界。

## Cleanup

1. OWNER 独立回读 T/S 的行数和值，确认拒绝项未产生数据变更。
2. 移除仅为本流程建立的规则关联和平台授权，恢复原账号/网络/语言状态。
3. OWNER 按 P、T、S、两个空 schema 的顺序逐项删除本次唯一名称对象；不使用共享 schema 或未核对对象的级联删除。
4. 关闭测试页签与结果；不保存真实凭据、权限令牌或浏览器认证数据。

## Skip Conditions

- CloudDM 服务不可达或缺少真实 HANA：全部执行场景 BLOCKED；可进行语法/行为/规则引擎离线探针，不能替代端到端结果。
- @Browser 未登录：暂停页面验证，等待用户登录，不切换外部浏览器。
- 缺少隔离规则、权限用户或可靠清理条件：对应权限/写入场景 SKIP，不能用管理员成功查询替代。
- 网络模拟能力不可用时，网络恢复子场景 SKIP；其余套件继续。
