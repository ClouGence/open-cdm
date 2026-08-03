# 数据库 SQL 语句行为分析接入与纠错提示词

你现在要为 open-cdm 的目标数据库方言接入或纠正 SQL 语句行为分析能力。本提示词适用于未来所有关系型数据库及具有 SQL 方言的数据库接入，不以 MySQL 的语法、对象层级或实现方式作为通用前提。

本任务只回答一个问题：

一条 SQL 表达了哪些行为关系，每个关系的行为主体是谁、执行了什么 BehaviorAction、指向哪些行为客体；每个主体和客体属于什么 TargetType、位于整个输入文本的什么代码范围，以及它的完整 CloudDM 资源路径是什么？

行为分析的唯一公共契约位于：

`/home/zyc/project/dm/open-cdm/backend/clouddm-platform/cgdm-plugin-sdk/src/main/java/com/clougence/clouddm/sdk/sql/analysis/behavior`

必须以该目录当前真实存在的以下类型为准：

- `BehaviorAnalysisSpi`：语句行为分析入口；
- `StatementBehavior`：聚合一条语句中的多个 `BehaviorRelation`；
- `BehaviorRelation`：一个行为主体、一个 `BehaviorAction`、零个或多个行为客体，以及主体是否跳过对象级权限的标记；
- `BehaviorObject`：主体或客体的 `TargetType`、`objectPath` 与绝对代码起止位置；
- `BehaviorAction`：行为动作及其关联的现有 `SecDataAuthKind`。

不要把本任务重新做成资源请求分析。不得让行为模型引用或返回 `ResourceRequest`，不得把 `BehaviorRelation` 退化为一组没有主客体关系的资源列表，也不得在行为 visitor 中直接计算权限计划、执行鉴权或映射数据库原生权限。

`StatementBehavior.statementType` 同时提供语句分类及该类语句的功能权限要求；
`BehaviorRelation` 则进一步表达具体资源上的主体、动作和客体，用于确定数据权限要求。
两层权限不能互相替代：statementType 不能裁决主体和客体关系，relations 为空也不表示
该语句免除功能权限。不要审计、纠正或扩展 split fixture 的 `[TYPE]`；不要为了行为分析
修改 `SplitQueryType` 或 `SecDataAuthKind`。

## 一、任务边界

### 必须完成

1. 使用目标数据库全部受支持版本的 SQL 语料，逐条人工分析其中表达的全部行为关系。
2. 为每个关系确定唯一行为主体、准确的 `BehaviorAction` 和完整的行为客体列表。
3. 为每个主体和客体确定最准确的 `TargetType`。
4. 为每个主体和客体生成符合 CloudDM 层级的完整 `objectPath`。
5. 为每个主体和客体生成精确的代码起止位置。
6. 完善 `behavior/<dialect>` 测试脚本；一个 `BehaviorObject` 独占一行。
7. 修复目标数据库行为分析中的漏行为、错行为、错误主体、错误客体、错误动作、错误 TargetType、错误路径、错误位置和错误关系合并。
8. 对名称无法识别但行为对象确实存在的场景，保留准确类型，并让路径停在能够确认的最近祖先层级。
9. 所有真实函数调用默认产生 Function 行为对象并使用 CALL，包括内置函数、聚合函数、窗口函数和用户自定义函数；系统内置函数仍保留行为但跳过对象级权限，UDF 必须保留对象级权限。只有经厂商官网确认会执行运维动作或改变、影响数据库运行状态的功能性函数，才通过可注册的方言规则映射为对应的非 CALL 行为，parser 不感知该规则。
10. 行为分析必须递归覆盖 SQL 的全部可执行子级；外层语句、routine/trigger/event body、控制块、handler 和嵌套查询中的行为统一汇总到当前 `StatementBehavior.relations`。
11. `BehaviorAction` 必须只关联已有 `SecDataAuthKind`；不得为接入行为分析增加、删除或改名 `SecDataAuthKind`。
12. 主体和客体必须来自 SQL 可以证明的真实行为语义；不得为了填满结构而伪造主体、客体或关系。

### 明确不做

1. 不修改现有 `ResAnalysisSpi`、`ResourceRequest`、资源 visitor 或 `resource/<dialect>` fixture。
2. 不把资源分析结果转换成 `BehaviorRelation` 作为生产实现。
3. 不审计、修改或扩展 `SplitQueryType`。
4. 不新增、修改或删除 `SecDataAuthKind`。
5. 不审计或修改 split fixture 中的 `[TYPE]`。
6. 不修改目标方言的 split/classification visitor 来迁就行为结果。
7. 不建立 DDL、DML、DCL、DQL、ADMIN 等语句分类台账。
8. 不把 `StatementBehavior.statementType` 当作资源级动作；真正的行为动作必须来自 `BehaviorRelation.action`。
9. 不在行为分析 SPI 中计算权限计划、权限 AND/OR 表达式或执行权限校验；允许根据明确的方言事实标记某个行为主体跳过对象级权限。
10. 不把行为分析变成列分析、安全规则 Domain 或 SQL 重写任务。

`split/<dialect>` 或目标数据库现有的等价 SQL fixture 在本任务中只有三个用途：

- 提供经过整理的 SQL 语料；
- 提供每条 SQL 的可靠边界；
- 提供 version、exact-version、方言模式和会话解析属性等上下文。

split fixture 中已有的 `[TYPE]` 对行为关系没有裁决作用。读取 SQL 原文后必须独立分析；即使 `[TYPE]` 错误，也不在本任务中修改。

行为分析是独立能力。生产运行时不得调用 split SPI、读取 `SplitScript`、依赖 split 类型树或把 split 结果转换为 `StatementBehavior`。行为分析必须直接基于自己的 AST/behavior visitor 递归遍历。

版本化系统函数、系统表/视图、系统过程等方言知识不属于 split 私有逻辑，也不应散落在 behavior visitor 中。需要共享时，应提炼到目标方言模块的公共注册器，由 split、resource、behavior 分别调用。MySQL 的共享方言事实统一放在 `com.clougence.sql.mysql.analysis.reference`。共享的是版本化事实和查询 API，不共享各分析能力的遍历与输出流程。

## 二、通用路径与参考实现

工作区：

`/home/zyc/project/dm`

仓库：

`/home/zyc/project/dm/open-cdm`

仓库规则：

`/home/zyc/project/dm/open-cdm/AGENTS.md`

目标数据库 SQL 语料约定：

`/home/zyc/project/dm/open-cdm/tests/ds-test/src/test/resources/split/<dialect>`

split fixture 格式说明：

`/home/zyc/project/dm/open-cdm/tests/ds-test/src/test/resources/split/README.md`

行为分析公共契约：

`/home/zyc/project/dm/open-cdm/backend/clouddm-platform/cgdm-plugin-sdk/src/main/java/com/clougence/clouddm/sdk/sql/analysis/behavior`

行为测试代码目标目录：

`/home/zyc/project/dm/open-cdm/tests/ds-test/src/test/java/com/clougence/clouddm/ds/behavior`

行为测试脚本目标目录：

`/home/zyc/project/dm/open-cdm/tests/ds-test/src/test/resources/behavior/<dialect>`

资源类型枚举：

`/home/zyc/project/dm/open-cdm/backend/clouddm-platform/cgdm-plugin-sdk/src/main/java/com/clougence/clouddm/sdk/model/analysis/TargetType.java`

执行任务时，从目标方言模块中建立或定位 `BehaviorAnalysisSpi`、behavior analysis package 和 parse-tree behavior visitor。不能假设类名带 `My`，不能假设模块名为 `sql-mysql`，也不能复用 resource visitor 作为行为 visitor。

MySQL 只能作为 SQL 语料和方言工具的参考：

- SQL 语料：`tests/ds-test/src/test/resources/split/mysql`
- 现有方言工具：`com.clougence.sql.mysql.utils`
- parser property 示例：`sqlMode`
- 厂商官方文档入口：`https://dev.mysql.com/doc/`

接入 Oracle、PostgreSQL、SQL Server、DB2、达梦或其他数据库时，必须使用其自己的方言模块、版本模型、对象层级、行为语义和官方文档；不得复制 MySQL 的类、sqlMode、catalog/schema 语义、权限规则或函数清单。

开始前完整阅读 AGENTS.md，并检查 git status 和相关 diff。保留工作区中已有未提交修改，不得覆盖、回滚、重新暂存或整理无关文件。

## 三、如何取得目标数据库 SQL

优先动态扫描 `split/<dialect>` 下实际存在的全部版本和子目录，不硬编码版本清单。如果目标数据库没有 split fixture，则使用其现有、经过验证的等价 SQL 语料，并建立能够可靠表达单条 SQL 边界、版本和方言配置的 fixture；不要为了套用参考数据库目录而伪造 split 结构。

不得按分号拆分 SQL。split fixture 的真实边界是：

- 长分隔线 `------------------------------------------------------------------------------------------` 左侧是原始脚本；
- 长分隔线右侧由 `----------` 分开的每个块对应一条 SQL；
- 每个块中 `[TYPE]` 后面的 SQL 原文是本任务使用的 occurrence；
- 只取得 SQL 原文，不把 `[TYPE]` 复制成行为期望；
- routine body、字符串、注释和可执行注释中可能包含分号；
- sidecar 只用于还原目标方言的 parser properties；
- exact-version 或等价版本标记只用于选择正确 parser version。

每条 SQL 使用稳定 occurrence ID：

`<相对目标 SQL fixture 根目录的路径>#<三位序号>`

台账只记录行为分析需要的字段：

- occurrence_id
- version
- exact_version
- parser_properties
- fixture_path
- sql_sha256
- sql_text
- expected_statement_type
- expected_relations
- behavior_status
- reason
- grammar_context
- behavior_visitor_method
- implementation_status
- verification_status

`expected_relations` 中每项必须包含：

- subject
- action
- target（零个、一个或多个；没有客体时为空）

其中 subject 和每个 target 在 fixture 中都压缩为
`"<TargetType>(<codeLine>) <resourcePath>"`，解码后仍是完整的 `BehaviorObject`。

禁止在台账中增加 current_type、proposed_type 或依赖 split `[TYPE]` 裁决的分类字段。

路径中包含 reject 的 SQL 仍要进入完整性台账，但要保持边界清楚：

- 如果相同 parser version/parser properties 下无法构造 AST，标记为 `PARSE_REJECTED`；
- `PARSE_REJECTED` 不是“没有行为”，不能写成空 relations；
- 不为完成行为分析任务而放宽 parser；
- 如果另一个受支持版本存在同一 SQL，可以记录其行为语义用于对照，但不能伪造当前版本结果。

## 四、行为模型与分析规则

### StatementBehavior

`BehaviorAnalysisSpi.analysisBehavior(...)` 对 query 中的每条语句返回一个 `StatementBehavior`，并保持源码顺序。

`StatementBehavior`：

- `statementType`：当前语句的 `SplitQueryType` 上下文；
- `relations`：当前语句的全部行为关系。

`statementType` 不能从 fixture 的 `[TYPE]` 机械复制，也不能代替关系分析。
它通过 `SplitQueryType.authKind` 表达语句级功能权限；relations 通过具体 BehaviorAction
和 BehaviorObject 表达资源级数据权限。relations 为空时仍必须执行 statementType
对应的功能权限检查。

多语句 query 必须返回多个 `StatementBehavior`，不能把不同语句的关系合并到同一个结果中。

### BehaviorRelation

每个 `BehaviorRelation` 必须表达：

- `subject`：唯一行为主体；
- `action`：唯一 `BehaviorAction`；
- `target`：零个或多个行为客体。

`BehaviorRelation` 不承载白名单或权限过滤标记。对象级权限豁免由 SQL 引擎按名称
注册唯一的 `SysObjectRegistrySpi`，analysis 在行为分析之后消费该注册表，
生成独立的权限请求。

运行时模型始终保持上述 `subject + action + target` 三部分结构。fixture 始终使用
`subject`、`action` 表达主体和动作，并根据行为客体数量投影 `target`：

- `target` 为空时，输出 `subject`、`action`，不输出 `target`；
- `target` 非空时，输出 `subject`、`action`、`target`；
- `target` 只有一个客体时，字段值直接使用一个紧凑字符串，不使用数组封装；
- `target` 有两个或更多客体时，字段值使用紧凑字符串数组；
- fixture 中的 subject 和 target 行为对象都压缩为
  `"<TargetType>(<codeLine>) <resourcePath>"`；
- 上述格式只是 fixture 表示法，读取后必须还原为
  `BehaviorRelation.subject`、`BehaviorRelation.action` 和完整的
  `BehaviorRelation.target`，不能改变生产模型。

主体是当前关系的核心资源，不等同于默认数据库用户，也不等同于 SQL 的第一个 token。隐式当前执行人由调用方鉴权上下文提供；SQL 没有显式命名执行人时，不得伪造 User/UserOrRole 主体。

只有 SQL 能证明真实方向或依赖时，才能把多个对象放进同一个关系。彼此无关系、只是同时出现的资源必须拆成多个关系。

基本关系规则：

- 单资源读取：Table/View 等资源作为 subject，action=`READ`，target 为空；
- 单资源创建、修改、删除、调用、锁定或配置：目标资源作为 subject，使用对应 action，target 为空；
- RENAME：旧资源作为 subject，action=`RENAME`，新资源进入 target；
- CREATE ... AS SELECT：新对象作为 subject，action=`CREATE`，查询源 Table/View/Function 等进入 target；嵌套的独立 CALL、配置访问等仍可形成自己的 relation；
- CREATE OR REPLACE：被创建或替换的对象作为 subject，action=`REPLACE`，依赖对象进入 target；
- INSERT ... SELECT：写入表作为 subject，action=`INSERT`，读取来源进入 target；
- MERGE：被合并目标作为 subject，action=`MERGE`，来源表/查询对象进入 target；
- 导出：导出 File 作为 subject，action=`EXPORT`，读取来源进入 target；
- 导入：被写入对象作为 subject，action=`IMPORT`，输入 File 等来源进入 target；
- GRANT 权限：权限作用域资源作为 subject，action=`GRANT`，接收 User/Role 进入 target；
- GRANT role：被授予 Role 作为 subject，action=`GRANT`，接收 User/Role 进入 target；
- GRANT PROXY：被代理身份作为 subject，action=`GRANT`，代理接收者进入 target；
- REVOKE 使用与 GRANT 相同的主体/客体方向，action=`REVOKE`；
- 外键：当前表或新 Constraint 作为 subject；被引用 Table 进入 target，具体选择必须与方言真实语义一致并在同类 SQL 中保持稳定；
- COPY/MOVE 仅在 SQL 明确表达复制或移动关系时使用，不能替代普通 READ、INSERT 或 RENAME。

当一条 SQL 同时包含多个独立动作时，必须输出多个 relation。例如 routine 创建动作、body 内的 INSERT、READ、CALL 必须分别表达，不能用外层 CREATE 把 body 行为全部包成一个关系。

`BehaviorRequest.requiredAuthKind` 是根据拆分后的单个资源行为解析出的 CloudDM 粗粒度鉴权类别，不是数据库原生权限。不得因为某数据库要求 ALTER+DROP 或 CREATE+INSERT，就在行为 visitor 中伪造权限计划；行为 visitor 只报告真实行为关系，权限解释属于后续鉴权层。

### BehaviorObject

每个 subject 和 target 都是 `BehaviorObject`，必须包含：

- `objectType`：准确的 `TargetType`；
- `objectPath`：完整 CloudDM 路径；
- `startLine`、`startColumn`：对象名称 token 的绝对起点；
- `endLine`、`endColumn`：对象名称 token 的绝对结束开区间。

不得向 `BehaviorObject` 增加 `ResourceRequest`、`SplitQueryType`、`require`、`skipPermission` 或权限计划字段。

行为对象是 SQL 明确引用，或虽无法取得具体名称但能够确认类型和所属范围的实体。至少覆盖：

- Environment、Instance；
- Catalog、Schema；
- Table、View、Materialized；
- Index、Constraint、Partition、Sequence；
- Function、Procedure、Trigger、Event、ProgramObject；
- User、Role、UserOrRole；
- Tablespace、ResourceGroup、Library、Policy、MaskingPolicy；
- ConfigKey、PrepareStatement；
- Replication、Publication、Subscription 等数据库能力对象；
- 导入文件、导出文件及 SQL 引用的其他外部对象。

若真实对象无法被已有 TargetType 准确表达，先按“TargetType 规则”评估新增类型，不能因为枚举缺失而丢掉行为对象。

### 一个行为对象一行

fixture 中每个行为对象以紧凑字符串表示：subject 独占一行；单个 target
直接位于字段值中，多个 target 组成数组且每个紧凑字符串独占一行：

- 查询两个无直接关系的表，输出两个 READ relation；
- JOIN 三个表，必须保留三个 Table 行为对象；只有 grammar 和行为模型能证明关系时才放在同一 relation；
- 调用四个函数，输出四个 Function 行为对象并使用 CALL；内置、聚合、窗口和用户自定义不能成为漏掉函数行为的理由；
- 查询两个系统变量，输出两个 ConfigKey 行为对象；
- 导出查询结果，至少包含导出 File 主体和全部来源 Table/View 客体；
- 导入文件写表，至少包含写入主体和输入 File 客体；
- 创建索引，必须包含 Index 与承载它的 Table；
- 创建视图或 `CREATE TABLE ... SELECT`，必须包含新对象和全部查询依赖；
- RENAME 必须在同一 relation 中保留旧资源 subject 和新资源 target；
- GRANT/REVOKE 必须保留授权范围或角色主体及全部接收者客体；
- 外键必须保留当前对象与被引用表；
- routine、trigger、event body 内的表、函数、过程等行为继续递归分析。

### 子级与 body 必须递归展开

行为分析应达到与递归 split 相同的完整子级覆盖范围，但二者仍是独立能力，不能把 split 类型树复制成行为结果。behavior visitor 应按 parse tree 的真实包含关系递归扫描：

- routine、trigger、event 的 body；
- BEGIN/END、IF、CASE、LOOP、WHILE、REPEAT、handler 等控制结构中的语句；
- CREATE VIEW、CREATE TABLE ... SELECT、INSERT ... SELECT、UPDATE/DELETE 条件和赋值表达式中的查询；
- CTE、派生表、标量子查询、EXISTS/IN 子查询和函数参数中的子查询；
- body 内继续嵌套的 CALL、表读写、变量/配置访问、导入导出和用户自定义函数调用。

所有层级发现的关系都平铺汇总到当前 `StatementBehavior.relations`；relations 不表达 split 树，也不增加 parent/children 字段。

不能依赖父 visitor 是否调用 `visitChildren` 才获得子级覆盖。即使父节点已完成自身关系并提前返回，其后代行为上下文仍必须被扫描。函数自身无论最终映射为 CALL 还是功能性行为，函数参数或子查询中的关系都必须继续递归识别。

### 关系去重

行为去重只发生在同一条 SQL 内，不能跨 `StatementBehavior` 去重。

关系身份至少包含：

`action + subject(targetType, resourcePath) + target(targetType, resourcePath)`

规则：

- 相同路径在不同 action 中必须分别保留；
- 相同对象分别充当 subject 和 target 时不能合并；
- target 集合不同的关系不能合并；
- 完全相同关系重复出现时可以合并，但保留第一次真实出现的代码位置；
- 不得把多个不连续 token 粗暴合成一段代码范围；
- 多语句脚本即使关系完全相同，也必须保留各自位置。

### BehaviorAction

优先使用当前已有动作：

- `READ`
- `CREATE`
- `ALTER`
- `DROP`
- `RENAME`
- `INSERT`
- `UPDATE`
- `DELETE`
- `MERGE`
- `REPLACE`
- `CALL`
- `GRANT`
- `REVOKE`
- `IMPORT`
- `EXPORT`
- `COPY`
- `MOVE`
- `LOCK`
- `CONFIGURE`
- `SWITCH`
- `ADMIN`
- `OTHER`

选择动作必须依据具体 AST 行为节点，不能只看 SQL 第一个关键字。

`OTHER` 只能用于完整分析后确实无法落入现有稳定动作的行为，不能作为漏分析兜底。确需新增动作时，必须满足：

1. 它是稳定、可跨多条真实 SQL 复用的行为；
2. 它不是数据库原生权限名、语句类别或单个语法变体；
3. 已有动作无法准确表达；
4. 关联到已有 `SecDataAuthKind`；
5. 不修改 `SecDataAuthKind`；
6. 检查所有 BehaviorAction 消费者和穷举 switch。

### 函数 CALL 默认规则与功能性函数例外

函数就是函数。只要 SQL 中存在真实函数调用，就必须生成 `TargetType.Function` 行为对象：

- 内置聚合函数、普通内置函数、窗口函数和用户自定义函数默认都使用 `CALL`；
- `COUNT`、`SUM`、`AVG`、`MIN`、`MAX` 等聚合函数不能因为是系统内置而从行为结果中删除，但应跳过对象级权限；
- `DISTINCT`、函数内 `ORDER BY`、`SEPARATOR`、`FILTER`、`WITHIN GROUP`、`OVER` 等调用形态不改变 CALL 语义；
- schema/catalog 限定函数和未限定函数都必须识别，限定名用于计算真实 resourcePath；
- 嵌套函数逐层生成 Function 行为，外层函数不能遮蔽内层函数；
- 无法确定函数身份或用途时默认保留 Function/CALL；
- `CASE ... END`、`DATE '...'`、`TIME '...'`、`TIMESTAMP '...'` 等语法结构即使 grammar 复用了 function context，也不是真实函数调用，不得生成 Function。

唯一例外是**功能性函数**：函数本身用于执行运维动作，或会改变、影响数据库运行状态，并且该语义已经由目标数据库对应版本的厂商官网文档确认。此类函数仍然是 `TargetType.Function`，但 action 应映射为它真实表达的现有 `BehaviorAction`，例如 `LOCK`、`CONFIGURE` 或 `ADMIN`，而不是同时再输出一个 CALL。

“功能性”不能按函数实现类别判断。聚合、标量、窗口、内置、UDF 都不是非 CALL 的充分条件；只有函数自身的运行语义满足上述条件时才是例外。功能性函数的参数、子查询及嵌套函数仍须继续分析。

例如：

`SELECT COUNT(*), SUM(amount), analytics.risk_score(amount) FROM sales.orders`

COUNT、SUM 和 `analytics.risk_score` 都必须形成独立 Function/CALL；`sales.orders` 必须形成 Table/READ。

#### 功能性函数注册机制

功能性函数例外必须由具体方言在其内部资源注册表中登记；公共层只提供可复用的注册与查询工具，不持有任何数据库资源清单。

注册机制必须满足：

1. 规则属于行为分析或共享方言事实层，不属于 lexer、grammar、parser 或生成 visitor 基类。
2. parser 只产出函数调用节点、函数原文和限定信息，不读取、不持有、不注册行为例外。
3. 未命中规则的真实函数调用无条件回落为 Function/CALL。
4. 注册项至少包含 dialect、versionRange、规范函数名和对应的现有 BehaviorAction；新增规则不修改 parser。
5. 注册幂等、线程安全，并在分析开始前完成；分析阶段使用稳定只读视图。
6. 函数名匹配大小写不敏感，统一处理引号、大小写和规范名。
7. 支持数据库版本差异，不能用最新版本规则覆盖旧版本。
8. 只注册经确认具有运维或运行状态影响的函数；普通函数和身份不明的函数不得进入例外表。
9. schema/catalog 限定、明确 UDF grammar context或元数据确认是用户函数时，用户函数判定优先，不能仅按末级同名误套系统函数例外。
10. 每个注册项都有目标数据库对应版本的厂商官网证据。
11. registry 与官网证据清单一一对应，测试拒绝“已注册但无证据”或“有证据但无明确 BehaviorAction”的函数。
12. 不得为功能性函数新增或修改 `SecDataAuthKind`；只允许选择已有 BehaviorAction。

已经存在的系统内置聚合函数和普通函数清单应作为跳过对象级权限的白名单来源，但 behavior visitor 不得据此删除 Function/CALL。

#### 官网文档准入规则

功能性函数例外的唯一权威来源是目标数据库厂商官方网站中与目标版本对应的 Reference Manual、SQL Reference、Database Reference 或等价官方文档。

一个函数进入例外表前必须同时满足：

1. 对应版本官方文档明确说明该函数执行的运维动作或对数据库运行状态的影响。
2. 文档语义足以确定唯一且已有的 BehaviorAction。
3. 文档函数名、别名和版本范围与注册内容一致。
4. 保存可直接打开的官方文档 URL，不能只保存搜索结果或官网首页。
5. 记录文档标题/章节、适用版本、核验日期和证据说明。
6. 每个别名单独取得官方依据。

grammar、lexer token、parser context、当前代码、已有测试、名称经验、第三方文章、其他数据库文档及非目标版本文档都不能单独作为准入证据。

官网证据清单至少包含：

- function_name
- normalized_name
- version_range
- behavior_action
- official_url
- document_title_or_section
- verified_at
- aliases
- note

证据不足时不得注册；该函数继续作为 Function/CALL，并在独立证据清单标记 `OFFICIAL_EVIDENCE_REQUIRED`。

#### 函数行为测试

至少覆盖：

- 内置聚合函数、普通内置函数、窗口函数和 UDF 都生成 Function/CALL；
- 大小写、合法引号及 schema/catalog 限定形式使用正确函数路径；
- DISTINCT、ORDER BY、SEPARATOR、FILTER、WITHIN GROUP、OVER 等形态仍生成 CALL；
- 嵌套函数逐层生成行为，参数中的 Table、View、变量和子查询仍被识别；
- CASE 和日期/时间 typed literal 不产生 Function；
- 注册一个有官网证据的功能性测试函数后，无需修改 parser 即可映射为指定非 CALL action；
- 功能性函数只输出真实 action，不额外重复输出 CALL；
- 不同数据库和版本使用各自规则；
- 未注册或身份不明的函数回落为 Function/CALL；
- registry 每个注册项都有唯一官网证据；
- 缺少 official_url、版本范围、行为动作或核验信息的候选不能通过完整性测试。

### 变量和配置也是行为对象

SQL 中读取、写入或管理的每个系统变量、用户变量和配置键都要独立识别：

- 读取使用 `READ`；
- 修改配置使用 `CONFIGURE`；
- 每个变量或配置键分别形成 BehaviorObject；
- global/session scope 是否进入路径，遵循 CloudDM 当前真实模型。

routine 参数、局部变量、字面量和表达式别名不是行为对象。

### 列不是行为对象

本任务不产出 `TargetType.Column`：

- 列名、`*`、表达式别名不进入 subject 或 target；
- `SHOW COLUMNS`、`DESCRIBE`、列级 ALTER 等场景识别所属 Table/View 等对象；
- 不能把列伪装成 Object、ConfigKey 或其他 TargetType；
- 因列引用发现的表、视图和函数仍然保留。

### 名称未知不等于没有行为对象

如果能确认对象类型但无法取得具体名称，仍然输出：

- `objectType` 使用最准确类型；
- `objectPath` 停在能够确认的最近祖先层级；
- 不制造假名字；
- 不用 Unknown 或 Object 掩盖准确类型；
- 不因名字未知而删除 relation。

例如能够确认 SQL 管理实例配置但无法识别具体配置键时，BehaviorObject 可以是：

`"ConfigKey(1:0~1:14) /test/1/"`

只有 SQL 确实没有任何可定位的主客体资源时，才允许当前
`StatementBehavior.relations` 为空。纯常量表达式、单纯事务控制或不引用资源的程序控制
可以为空；空 relations 只表示没有额外的数据资源权限要求，不能跳过 statementType
对应的功能权限检查。

### 不是行为对象的内容

- SQL 关键字和语法子句；
- 表别名、列别名、CTE 名和派生表别名；
- routine 参数和局部变量；
- 普通字面量、数字和字符串；
- 普通注释和 label；能够改变执行期配置的可执行 hint 不是普通注释，
  其中明确命名的配置项必须输出 ConfigKey/CONFIGURE；
- 仅用于语法组织、没有真实实体语义的临时名字。

## 五、CloudDM resourcePath

测试入口统一使用固定环境与数据源，并根据目标数据库真实层级集中提供默认上下文：

```text
environment = test
dataSourceId = 1
defaultDatabaseLevels = <目标数据库按 UmiTypes 定义的层级与测试值>
```

通用路径：

`/<环境>/<数据源ID>/<目标数据库实际层级...>/<对象名>/`

路径深度：

- Environment：`/test/`
- Instance：`/test/1/`
- Catalog、Database、Schema：按该数据库在 CloudDM 注册的 UmiTypes 顺序展开；
- Table/View/Function/Procedure 等：追加在真实所属层级之后；
- 实例级 ConfigKey：从 `/test/1/` 继续表达真实配置路径；
- 服务器文件：从 `/test/1/` 继续表达规范化文件路径；
- 名称未知：停在能够确认的最近祖先。

某参考实现可能形成 `/test/1/catalog1/schema1/<name>/`，但这不表示所有数据库必须有 Catalog + Schema。只有 Database、只有 Schema，或具有 Cluster/Database/Schema 等层级时，必须遵循其真实 CloudDM 定义。

路径规则：

1. `objectPath` 以 `/` 开头并以 `/` 结尾。
2. 环境和数据源 ID 位于数据源对象路径最前面。
3. SQL 明确限定层级时以 SQL 为准。
4. SQL 未限定时由测试入口补默认上下文。
5. 实例级对象不能伪造 Catalog、Database 或 Schema。
6. 文件路径、URI、引号标识符和特殊字符复用 CloudDM 规范化/转义规则。
7. 不得为了复用参考方言路径而错误增删或调换层级。

## 六、TargetType 规则

先完整阅读 `TargetType` 并搜索生产消费者。

选择顺序：

`已有精确类型 > 已有稳定上位类型 > 新增稳定对象类型 > Object > Unknown`

新增 TargetType 只允许用于稳定实体：

1. 已有类型确实无法准确表达。
2. 它不是动作、语句类别、关键字或单个语法变体。
3. 多条真实 SQL 或稳定数据库对象模型会复用。
4. 名称符合现有枚举风格。
5. 不删除、不改名、不合并已有枚举值。
6. 检查序列化、排序/评分、权限规则、穷举 switch、前后端契约和其他消费者。
7. 有对应 UmiTypes 时正确关联；没有时可为 null，但必须说明原因。

导入/导出文件是明确对象。如果 TargetType 没有 File，应评估新增 File，不能忽略或写成 Object。

## 七、代码位置与 fixture 格式

### 结构化位置

`BehaviorObject` 的位置事实来源是：

- `startLine`
- `startColumn`
- `endLine`
- `endColumn`

规则：

- 行号从 1 开始；
- 列号从 0 开始；
- 范围采用结束开区间 `[startLine:startColumn, endLine:endColumn)`；
- 位置必须叠加 `BehaviorAnalysisSpi.analysisBehavior` 的 `baseLine`、`baseColumn`；
- 精确覆盖用于命名对象的完整 token；
- 限定名覆盖完整限定名，例如 `db1.fn_name`、`db1.t1`；
- 文件对象覆盖文件名或 URI 字面量；
- 名称未知时才允许退化到能够确认的语法节点。

fixture 使用：

`<startLine>:<startColumn>~<endLine>:<endColumn>`

例如：

`"codeLine":"1:2~3:4"`

绝对位置换算：

- `absoluteLine = baseLine + localLine - 1`；
- `localLine == 1` 时，`absoluteColumn = baseColumn + localColumn`；
- `localLine > 1` 时，`absoluteColumn = localColumn`；
- 起点和结束端点分别应用；
- query 后续行不能继续叠加首行 baseColumn。

方言 visitor 只取得局部 token 范围；BehaviorAnalysisSpi/公共行为层统一换算绝对位置；fixture 适配层统一完成四元坐标与 codeLine 的双向转换。不能在 visitor 中拼 codeLine，也不能把 codeLine 存为 BehaviorObject 第二套状态。

独立偏移测试至少覆盖：

- 非零 baseLine；
- query 第一行非零 baseColumn；
- 后续行不叠加 baseColumn；
- 跨行对象；
- 起止端点位于不同文本行；
- 多个 query 子串在同一完整脚本中的位置。

普通行为 fixture 统一使用 `baseLine=1, baseColumn=0`，但 codeLine 必须是真实范围。

### 最终 fixture 投影协议

fixture 是 `StatementBehavior` 运行时模型的可读投影。生产接口仍返回
`List<StatementBehavior>`；下面的紧凑格式只能存在于测试输入、测试输出和 fixture
适配层中。

#### 顶层：按语句 occurrence 顺序输出

`expect` 直接使用一个有序对象。每个顶层字段 occurrence 表示一条语句：

- key 是该语句的 `SplitQueryType`；
- value 是该语句的 `BehaviorRelation` 数组；
- 字段出现顺序必须与 query 中的语句顺序一致；
- 相同 `SplitQueryType` 可以重复成为多个 key，每个 key 仍只代表一条语句；
- 同名 key 不能覆盖、去重或合并，读取层必须使用流式 token 逐个读取；
- 禁止先把 expect 反序列化为 Map 或 ObjectNode。

例如，两条连续的 SELECT 必须保留为两个 `SELECT` occurrence：

```text
[example__multi_statement_0__001]
sql:
SELECT * FROM db1.t1;
SELECT * FROM db1.t2;
levels:
/test/1/catalog1/schema1
expect:
{
  "SELECT": [
    {
      "subject": "Table(1:14~1:20) /test/1/catalog1/db1/t1/",
      "action" : "READ"
    }
  ],
  "SELECT": [
    {
      "subject": "Table(2:14~2:20) /test/1/catalog1/db1/t2/",
      "action" : "READ"
    }
  ]
}
```

这两个同名字段是协议要求，不是待消除的重复数据。

#### 关系：固定主体和动作，按客体数量投影

每条关系始终先输出 `subject`、`action`，字段顺序不能交换：

- 没有客体：只输出 `subject`、`action`，不输出 `target`；
- 一个客体：`target` 直接使用一个紧凑字符串，禁止单元素数组；
- 两个或更多客体：`target` 使用至少包含两个紧凑字符串的数组。

完整示例：

```text
[<稳定且唯一的 case_id>]
sql:
<完整 SQL 原文>
levels:
/<environment>/<datasourceId>/<catalog>/<schema>
expect:
{
  "<SplitQueryType>": [
    {
      "subject": "<TargetType>(1:2~1:8) /<完整路径>/",
      "action" : "<BehaviorAction>",
      "target" : "<TargetType>(1:12~1:18) /<完整路径>/"
    },
    {
      "subject": "<TargetType>(2:2~2:8) /<完整路径>/",
      "action" : "<BehaviorAction>"
    },
    {
      "subject": "<TargetType>(3:2~3:8) /<完整路径>/",
      "action" : "<BehaviorAction>",
      "target" : [
        "<TargetType>(3:12~3:18) /<完整路径>/",
        "<TargetType>(3:20~3:26) /<完整路径>/"
      ]
    }
  ]
}
```

#### 当前环境层级

每个 testcase 必须在 `sql` 与 `expect` 之间声明 `levels`：

```text
levels:
/test/1/catalog1/schema0
```

`levels` 表示执行 SQL 时调用方提供的当前环境层级，固定使用
`/<environment>/<datasourceId>/<catalog>/<schema>` 格式。fixture 适配层必须将其解析为：

- `UmiTypes.Instance = <environment>/<datasourceId>`；
- `UmiTypes.Catalog = <catalog>`；
- `UmiTypes.Schema = <schema>`。

测试框架必须逐 testcase 读取并传给 `BehaviorAnalysisSpi.analysisBehavior(...)`，禁止由方言测试类
暗中统一注入默认 catalog/schema。SQL 显式限定的层级覆盖对应的上下文层级；SQL 未限定的对象才使用
`levels` 补全路径。这样审计者可以直接判断 `objectPath` 中哪些层级来自 SQL，哪些来自执行上下文。

#### 行为对象：统一紧凑字符串

subject、单个 target 和 target 数组元素统一使用：

```text
<TargetType>(<codeLine>) <resourcePath>
```

其中：

- TargetType 与左括号之间不加空格；
- codeLine 严格使用 `起始行:起始列~结束行:结束列`；
- 右括号与 resourcePath 之间只保留一个普通空格；
- codeLine 必须与 BehaviorObject 的四个位置整数完全一致；
- resourcePath 必须是完整 CloudDM 路径；
- 禁止重新展开为包含 `objectType`、`codeLine`、`objectPath` 三个字段的对象。

#### 单个 case 内的对齐

对齐只服务于阅读，不能改变字符串内容：

- 同一 case 的顶层 SplitQueryType key 按最长 key 补普通空格，使冒号和值起始列对齐；
- 每条关系按 `"subject":`、`"action" :`、`"target" :` 对齐；
- target 数组中的每个行为对象独占一行；
- 紧凑行为对象字符串内部不做列对齐，不添加填充空格；
- 对齐范围不跨 case、文件或目录，只能使用普通空格，不能使用 Tab。

#### 其他硬性要求

- 每个 testcase 必须且只能有一个 `levels`，并且位于 SQL 原文之后、`expect` 之前；
- 不允许 `requests`、`sqlType`、`require`、`skipPermission`；
- 顶层不再输出 `statementType`、`relations`，也不增加 `behaviors` 包装字段；
- 无客体关系禁止输出 `target:null`、空字符串或空数组；
- action 必须是 `BehaviorAction` 枚举名，不能用 statementType 代替；
- 没有行为时仍输出当前语句类型 key，value 明确写为 `[]`；
- 使用严格完整关系比较，不允许 contains、allowExtra 或只校验 size；
- 一个 case 可以包含多条语句，但每条语句必须对应一个独立顶层字段 occurrence；
- case 只使用一行 `----------` 分隔，分隔线前后不允许多余空行或空白回车；
- SQL 原文不得改写来迁就行为实现。

### 示例 1：单表读取

```text
[example__select_0__001]
sql:
SELECT * FROM db1.t1;
levels:
/test/1/catalog1/schema1
expect:
{
  "SELECT": [
    {
      "subject": "Table(1:14~1:20) /test/1/catalog1/db1/t1/",
      "action" : "READ"
    }
  ]
}
```

### 示例 2：JOIN、内置聚合函数和 UDF 都使用 CALL

```text
[example__join_0__007]
sql:
SELECT COUNT(*), SUM(a.score), db1.f_score(a.score), db2.f_label(b.kind)
FROM db1.account a JOIN db2.profile b ON a.id = b.account_id;
levels:
/test/1/catalog1/schema1
expect:
{
  "SELECT": [
    {
      "subject": "Function(1:7~1:12) /test/1/catalog1/schema1/COUNT/",
      "action" : "CALL"
    },
    {
      "subject": "Function(1:17~1:20) /test/1/catalog1/schema1/SUM/",
      "action" : "CALL"
    },
    {
      "subject": "Function(1:31~1:42) /test/1/catalog1/db1/f_score/",
      "action" : "CALL"
    },
    {
      "subject": "Function(1:53~1:64) /test/1/catalog1/db2/f_label/",
      "action" : "CALL"
    },
    {
      "subject": "Table(2:5~2:16) /test/1/catalog1/db1/account/",
      "action" : "READ"
    },
    {
      "subject": "Table(2:24~2:35) /test/1/catalog1/db2/profile/",
      "action" : "READ"
    }
  ]
}
```

COUNT、SUM 和两个 UDF 都是函数调用，因此都产生 Function/CALL。列不产生 BehaviorObject。

同名冲突：

```text
SELECT COUNT(amount), analytics.count(amount) FROM sales.orders;
```

未限定 COUNT 和 `analytics.count` 都形成 Function/CALL；二者按各自限定信息生成路径，`sales.orders` 形成 Table/READ。

### 示例 3：多个变量

```text
[example__variables_0__001]
sql:
SELECT @@global.time_zone, @@session.sql_mode;
levels:
/test/1/catalog1/schema1
expect:
{
  "SELECT": [
    {
      "subject": "ConfigKey(1:7~1:25) /test/1/time_zone/",
      "action" : "READ"
    },
    {
      "subject": "ConfigKey(1:27~1:45) /test/1/sql_mode/",
      "action" : "READ"
    }
  ]
}
```

### 示例 4：导出

```text
[example__export_0__001]
sql:
SELECT * FROM sales.orders INTO OUTFILE '/tmp/orders.csv';
levels:
/test/1/catalog1/schema1
expect:
{
  "DATA_EXPORT": [
    {
      "subject": "File(1:40~1:57) /test/1/tmp/orders.csv/",
      "action" : "EXPORT",
      "target" : "Table(1:14~1:26) /test/1/catalog1/sales/orders/"
    }
  ]
}
```

### 示例 5：名称未知的实例配置

```text
[example__instance_config_0__004]
sql:
ALTER INSTANCE RELOAD TLS;
levels:
/test/1/catalog1/schema1
expect:
{
  "SYSTEM_SETTING_WRITE": [
    {
      "subject": "ConfigKey(1:22~1:25) /test/1/",
      "action" : "CONFIGURE"
    }
  ]
}
```

若完整分析证明 Instance 更准确，应使用 Instance；不能因名称未知而删除行为。

### 示例 6：CREATE OR REPLACE 与查询依赖

```text
[example__view_replace_0__001]
sql:
CREATE OR REPLACE VIEW sales.active_orders AS
SELECT normalize_status(status) FROM sales.orders;
levels:
/test/1/catalog1/schema1
expect:
{
  "CREATE_VIEW": [
    {
      "subject": "View(1:23~1:42) /test/1/catalog1/sales/active_orders/",
      "action" : "REPLACE",
      "target" : "Table(2:37~2:49) /test/1/catalog1/sales/orders/"
    },
    {
      "subject": "Function(2:7~2:23) /test/1/catalog1/schema1/normalize_status/",
      "action" : "CALL"
    }
  ]
}
```

### 示例 7：RENAME

```text
[example__rename_0__001]
sql:
RENAME TABLE sales.orders TO archive.orders_2025;
levels:
/test/1/catalog1/schema1
expect:
{
  "RENAME_TABLE": [
    {
      "subject": "Table(1:13~1:25) /test/1/catalog1/sales/orders/",
      "action" : "RENAME",
      "target" : "Table(1:29~1:48) /test/1/catalog1/archive/orders_2025/"
    }
  ]
}
```

必须保留真实旧对象到新对象的方向，不能拆成两个互不相关的资源请求。

### 示例 8：GRANT

```text
[example__grant_0__001]
sql:
GRANT SELECT ON sales.orders TO 'reader'@'%';
levels:
/test/1/catalog1/schema1
expect:
{
  "GRANT": [
    {
      "subject": "Table(1:16~1:28) /test/1/catalog1/sales/orders/",
      "action" : "GRANT",
      "target" : "UserOrRole(1:32~1:44) /test/1/reader@%/"
    }
  ]
}
```

角色授权以 Role 为 subject、接收人或接收角色为 target；PROXY 授权以被代理身份为 subject、代理接收者为 target。

### 示例 9：routine body

```text
[example__routine_body_0__001]
sql:
CREATE PROCEDURE sales.refresh_orders()
BEGIN
  INSERT INTO sales.order_archive
  SELECT * FROM sales.orders WHERE status = normalize_status('ready');
  CALL sales.after_refresh();
END;
levels:
/test/1/catalog1/schema1
expect:
{
  "CREATE_PROG_OBJ": [
    {
      "subject": "Procedure(1:17~1:37) /test/1/catalog1/sales/refresh_orders/",
      "action" : "CREATE"
    },
    {
      "subject": "Table(3:14~3:33) /test/1/catalog1/sales/order_archive/",
      "action" : "INSERT",
      "target" : "Table(4:16~4:28) /test/1/catalog1/sales/orders/"
    },
    {
      "subject": "Function(4:44~4:60) /test/1/catalog1/schema1/normalize_status/",
      "action" : "CALL"
    },
    {
      "subject": "Procedure(5:7~5:26) /test/1/catalog1/sales/after_refresh/",
      "action" : "CALL"
    }
  ]
}
```

BEGIN/END、局部变量和 split 子级不是行为对象。

### 独立偏移测试

普通 fixture 使用 `(baseLine=1, baseColumn=0)`。单独 Java 测试直接调用：

```java
String query = "SELECT * FROM db1.t1 a\n"
        + "JOIN db2.t2 b ON a.id = b.id;";
List<StatementBehavior> behaviors = spi.analysisBehavior(query, levels, 10, 8);
```

第一个对象位于 query 第一行，列坐标叠加 8；第二个对象位于后续行，只叠加行偏移。测试必须直接断言 BehaviorObject 四个整数，不能只比较 codeLine。

### 示例 10：确实没有行为

```text
[example__no_behavior_0__003]
sql:
DO 1 + 1;
levels:
/test/1/catalog1/schema1
expect:
{
  "BLOCK": []
}
```

## 八、实现范围

优先修改或新增语句行为分析链路：

- `sdk.sql.analysis.behavior` 下的公共契约，只在真实行为契约需要时调整；
- 目标方言的 behavior parse-tree visitor；
- 目标方言的 BehaviorAnalysisSpi 实现；
- 行为分析层或共享方言层的功能性函数例外 registry；
- behavior fixture 测试框架；
- 目标方言多版本行为测试入口；
- `behavior/<dialect>` fixture。

不得修改：

- `ResAnalysisSpi`、`ResourceRequest` 和现有 resource visitor；
- `resource/<dialect>` fixture；
- split/classification visitor；
- split fixture `[TYPE]`；
- `SplitQueryType`；
- `SecDataAuthKind`；
- 与行为分析无关的安全规则 visitor；
- SQL 原文。

BehaviorObject 必须真实携带：

- targetType
- resourcePath
- startLine
- startColumn
- endLine
- endColumn

同步审计构造器、序列化、equals/去重和消费者：

- targetType 和 resourcePath 不能为空；
- 代码范围必须有效且结束不早于起点；
- BehaviorRelation.subject 不能为空；
- BehaviorRelation.action 不能为空；
- BehaviorRelation.target 不能为空，允许为空数组；
- StatementBehavior.relations 不能为空，允许为空数组；
- 同一对象在不同关系角色或不同 action 中不能错误合并；
- fixture 读取层必须把无 `target` 字段的关系还原为 subject、action 和空 target，
  并把 subject 字符串、单个 target 字符串或多 target 字符串数组还原为
  BehaviorObject 及 BehaviorObject 列表，
  不能让生产模型感知 fixture 格式；
- fixture 读取层必须按出现顺序逐个读取顶层 `SplitQueryType` 字段并构造
  StatementBehavior；同名字段不能覆盖、去重或合并；
- fixture 读取层必须解析每个 testcase 的 `levels`，并将得到的 Instance、Catalog、Schema
  上下文传给 `BehaviorAnalysisSpi`；不得使用测试类中的隐式统一默认值；
- fixture 写出层必须根据 target 数量选择唯一格式：零个省略 `target` 字段，
  一个使用字符串，两个及以上使用数组；
- fixture 解码后严格比较 statementType、subject、action、target、targetType、codeLine、resourcePath；
- codeLine 只由四个位置整数投影；
- 普通 fixture 固定偏移 `(1, 0)`，独立测试覆盖非零偏移。

函数行为边界：

- behavior visitor 对每个真实函数调用默认生成 Function/CALL；
- 方言 BehaviorAnalysisSpi 或行为初始化组件只装配功能性函数例外 registry；
- 每个方言只注册本数据库、对应版本且有官网证据的功能性函数及其非 CALL action；
- behavior visitor 只提交函数限定信息并查询例外；未命中必须回落 CALL；
- parser、grammar、lexer、split visitor 和 resource visitor 都不参与 behavior 的 CALL 删除；方言函数注册器只决定对象级权限是否跳过；
- 禁止在 behavior visitor 中堆积函数名 if/switch；
- 注册 API 能在测试中添加功能性函数并证明 parser 无需修改；
- 已注册例外不能同时产生指定 action 和重复的 CALL。

生产代码不能增加只为测试服务的 public API。测试使用的 environment、dataSourceId 和默认层级
必须显式写在每个 testcase 的 `levels` 中，由 fixture 适配层解析，不污染 parser 或行为语义。

## 九、工作流程

1. 动态生成全量 occurrence 台账，只记录 SQL 和行为字段。
2. 逐条阅读完整 SQL，独立写出正确 StatementBehavior 和 BehaviorRelation，不参考当前实际输出作为答案。
3. 优先处理行为丰富或易错 SQL：RENAME、GRANT/REVOKE、角色、PROXY、CREATE AS、INSERT SELECT、MERGE、JOIN、子查询、CTE、导入导出、routine body、外键、多函数和变量配置。
4. 对每个功能性函数例外候选访问目标数据库对应版本的厂商官网，建立证据记录并确定现有 BehaviorAction；未确认不得注册，默认保留 CALL。
5. 将严格期望写入 `behavior/<dialect>`。
6. 运行行为测试和功能性函数例外证据完整性测试。
7. 失败时判断是期望关系错误、visitor 漏节点、主体/客体方向错误、action 错误、TargetType 错误、路径错误、位置错误、函数例外误判还是测试框架错误。
8. 只有重新阅读 SQL 或获得更准确的官网证据才能修改期望/功能性函数规则；不能把期望改成当前输出以让测试通过。
9. 同一 grammar context 和同一行为语义跨版本联动检查。
10. 更新台账和进度，直到每个 occurrence 状态明确。

行为状态只允许：

- `VERIFIED_BEHAVIORS`：全部关系人工确认且测试通过；
- `VERIFIED_EMPTY`：确认没有可定位的主客体资源且严格空 relations 通过；
  statementType 对应的功能权限仍然有效；
- `PARSE_REJECTED`：对应 parser 配置无法产生 AST；
- `BLOCKED`：有具体可复现的行为分析阻塞。

不允许使用“语句类型不确定”作为 BLOCKED 理由；本任务重点是行为关系。

## 十、验证与完成标准

从以下目录运行：

`/home/zyc/project/dm/open-cdm/backend`

至少执行目标行为测试，例如：

```text
./gradlew :s-test:test --tests 'com.clougence.clouddm.ds.behavior.*'
```

根据生产改动补充目标方言模块、TargetType 和 BehaviorAction 消费者相关测试。不要并发运行共享同一测试结果目录的 Gradle 命令。

最终报告必须包含：

1. SQL occurrence 总数及按版本/parser properties/reject 的分布。
2. VERIFIED_BEHAVIORS、VERIFIED_EMPTY、PARSE_REJECTED、BLOCKED 数量。
3. `behavior/<dialect>` 与可分析 occurrence 的一一对应证明。
4. 按 BehaviorAction 汇总的关系数量。
5. 按 TargetType 汇总的 subject 和 target 数量。
6. resourcePath 落在 Environment、Instance、Catalog、Schema、具体对象层级的数量。
7. RENAME、GRANT、REVOKE、角色、PROXY、CREATE AS、INSERT SELECT、MERGE、导入导出等代表关系。
8. 功能性函数例外的来源、版本范围、函数数量、非 CALL action 和 occurrence 数；每项附厂商官网直达链接、章节和核验日期，同时报告聚合、普通内置、窗口和 UDF 的 CALL 覆盖。
9. 修复的漏关系、错关系、错误主体、错误客体、错误动作、错误类型、错误路径、错误代码位置和重复关系。
10. 新增 TargetType 或 BehaviorAction 的必要性及消费者影响；确认没有新增 SecDataAuthKind。
11. statementType 的 UNKNOWN 数量及其原因，但不得把它当作关系分析替代品。
12. codeLine 合法性统计、普通 fixture 固定偏移及独立非零偏移/跨行测试结果。
13. 执行命令、退出码和测试结果。
14. git status、diff --stat 和 diff --check。

只有满足以下条件才能完成：

- 没有修改现有资源分析接口、模型、visitor 和 fixture；
- 没有进行 split `[TYPE]` 分类纠错；
- 每个可分析 SQL 都有严格行为期望；
- expect 顶层按源码顺序保留每条语句；相同 SplitQueryType 使用重复字段且没有覆盖或合并；
- 每个 StatementBehavior 聚合当前语句的全部关系；
- 每个 BehaviorRelation 都有唯一 subject、唯一 action 和明确的运行时 target 列表；
- target 为空的关系只输出 `subject + action`，target 非空的关系只使用
  `subject + action + target` 完整格式；
- 完整格式中单个 target 没有数组封装，多个 target 使用至少含两个元素的数组；
- RENAME、GRANT、REVOKE 等方向性关系没有被拆成无关系资源；
- 主体和客体都使用 BehaviorObject，不引用 ResourceRequest；
- 每个 BehaviorObject 都有准确 TargetType、完整 resourcePath 和精确结束开区间；
- BehaviorAnalysisSpi 的 baseLine/baseColumn 正确叠加到起止端点，后续行没有错误叠加首行 baseColumn；
- 每个 subject 和 target 紧凑字符串独占一行，字符串内部顺序严格为
  `TargetType`、`codeLine`、`objectPath`；
- 同一对象在不同 action 或关系角色中没有错误合并；
- JOIN、多表、多函数、多变量、导入导出没有遗漏；
- routine、trigger、event 及嵌套控制块、handler、子查询和函数参数中的行为全部进入当前 StatementBehavior.relations；
- 内置聚合函数、普通内置函数、窗口函数和 UDF 都生成 Function/CALL；
- 内置函数和系统过程保留行为记录但跳过对象级权限，UDF 和普通用户过程仍执行对象级权限；
- CASE 和 typed literal 等非函数语法没有误生成 Function；
- 功能性函数例外可注册、按方言和版本隔离，parser 不感知；
- 功能性函数例外每项都有对应版本厂商官网证据和明确的现有 BehaviorAction；
- 未注册或无法确认的函数回落为 Function/CALL；
- 功能性函数只生成真实非 CALL action，不重复生成 CALL；
- 函数参数内行为不因函数动作映射而丢失；
- 列没有作为 BehaviorObject 输出；
- 名称未知但类型可知的对象没有丢失；
- resourcePath 完整且层级正确；
- 没有用 Object、Unknown、OTHER、contains、allowExtra 或修改 SQL 原文掩盖缺陷；
- BehaviorAction 只关联已有 SecDataAuthKind，没有修改 SecDataAuthKind；
- 所有非 BLOCKED 行为测试通过；
- 未覆盖或破坏用户已有修改。
