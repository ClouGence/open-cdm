# SQL 语句分类规则

版本：2026-08-04

## 1. 唯一分类来源

`com.clougence.clouddm.sdk.sql.parser.SplitQueryType` 是分类 Code 的唯一来源。分类器必须遵守以下约束：

1. 方括号中的每个 Code 必须是当前 `SplitQueryType` 中真实存在的枚举项。
2. 不得为了贴合某个数据源或方言关键字临时创造分类，也不得恢复已经从枚举删除的分类。
3. 语句与现有枚举没有准确对应关系时使用 `UNKNOWN`，不能使用语义近似但实际错误的分类。
4. `SplitQueryType` 的 `SecDataAuthKind`、`TargetType`、`SecQueryKind` 以 Java 枚举定义为准；本文只解释其适用范围，不能覆盖代码定义。
5. 当前数据源没有可靠原生语法时不得虚构支持，不能为了覆盖公共枚举而编造 SQL。
6. 既有夹具标签只是待校验数据，不能反过来作为分类依据。

正文描述跨数据源通用的语义边界和判定方法。方言关键字、对象模型映射、版本差异、
内置函数清单及其他不能泛化的事实统一放在文末“特定数据源说明”，不得反向改变通用
分类含义。

### 1.1 目标数据库权威性与方言隔离

2026-08-03 至 2026-08-04 的 TiDB 实施证明，兼容性声明、复制 grammar 和已有 fixture 都不能
替代目标数据库自身结论。后续数据源分类必须遵守：

1. 分类事实以目标数据库对应版本的正式 parser、运行时注册表、源码测试、真实数据库和官方文档
   为证据；结论名称必须明确属于目标数据库，不能混用 Oracle、MySQL 或其他方言名词。
2. 即使目标数据库宣称兼容另一数据库，也不得直接继承兼容方言的 parser util、内置函数表、系统
   表、管理函数映射或权限豁免清单。参考方言只能提供候选语料，所有候选必须重新碰撞。
3. 每个数据源维护独立 grammar、生成基类、版本配置、DSL Provider、statement parser、split
   visitor 和事实注册表；生产模块不得依赖兼容方言 parser 才能工作。
4. 不建立含义不明的 `v2` parser。只维护一个当前 parser 实现；主版本差异通过明确版本配置和
   grammar 谓词表达。若碰撞证明语法向上兼容，可以维护最新 grammar 超集，但新语法仍保留最早
   支持版本谓词。
5. 厂商缩写保持官方大小写，例如 `TiDB`，不能写成 `TiDb`。Java 包名和资源目录才按规范使用
   小写 `tidb`。
6. 源码解析器证明语法接受和 AST 结构，不自动证明执行成功、对象存在、权限满足或执行器副作用；
   不同证据层的结论不能混写。

## 2. 记录与多分类规则

分类头使用以下形式：

```text
[ALTER_TABLE|ADD_COLUMN] ALTER TABLE t ADD COLUMN c INT;
```

代码块或程序对象存在子语句时，递归收集所有后代类型，并在分类头中使用一层括号汇总：

```text
[CREATE_PROG_OBJ(BLOCK,SELECT,UPDATE,PROGRAM_CONTROL,INSERT)]
```

分类头和 SQL 也可以分行保存；无论采用哪种排版，都必须满足：

- 第一项是最外层语句的主分类。
- 后续项只记录该语句直接执行的附加动作或实际嵌套查询。
- 附加分类按语法中的动作顺序排列，相同 Code 去重。
- 字符串、注释、对象名和权限列表中的关键词不能触发分类。
- 一条 SQL 内部的分号不一定代表新记录，例如 `BEGIN ... END` 必须作为完整语句处理。

后代分类摘要遵守以下规则：

1. `|` 只连接根 `SplitScript` 节点自身的完整类型集合。
2. 分类器递归遍历所有 `children`，将每个后代节点的全部类型统一放入根节点的一层 `(...)`。
3. 后代类型按深度优先的首次发现顺序排列，相同 Code 全局去重；分类头不得嵌套括号。
4. 子节点不能扁平追加到根节点类型集合。`CREATE_PROG_OBJ` 内的 `UPDATE` 表示为
   `CREATE_PROG_OBJ(...,UPDATE,...)`，不能写成 `CREATE_PROG_OBJ|UPDATE`。
5. fixture 分类头只记录扁平的后代类型摘要；每个实际 `SplitScript` 子节点仍须保存自己的
   SQL、位置和真实递归 children。
6. 没有后代类型时不输出括号。空括号不合法，不能使用 `TYPE()` 表示未知或尚未校对。

示例：

```text
[CREATE_TRIGGER(INSERT)]
CREATE TRIGGER trg_ai AFTER INSERT ON src
FOR EACH ROW INSERT INTO audit_log VALUES (NEW.id);
```

```text
[CREATE_PROG_OBJ|COMMENT_PROG_OBJ(BLOCK,UPDATE,PROGRAM_CONTROL,INSERT)]
CREATE PROCEDURE p()
  COMMENT 'procedure comment'
BEGIN
  UPDATE t SET c = 1;
  IF c > 0 THEN
    INSERT INTO audit_log VALUES (c);
  END IF;
  SIGNAL SQLSTATE '45000';
END;
```

其中 `BLOCK` 对应 `BEGIN ... END` 代码段；`IF ... END IF` 和叶子 `SIGNAL` 都是独立的
`PROGRAM_CONTROL` 子节点。`IF` 内的 `INSERT` 继续位于该控制节点的 children 中。

示例：

```text
[ALTER_TABLE|DROP_COLUMN|ADD_COLUMN|ADD_CONSTRAINT]
ALTER TABLE t
  DROP COLUMN c1,
  ADD COLUMN c2 INT,
  ADD CONSTRAINT ck CHECK (c2 > 0);
```

### 2.1 外层动作与附加动作共存原则

一条 SQL 同时表达多个实际执行动作时，专属能力不能替换承载它的外层动作。分类器应先记录最外层语句的主分类，再遍历该语句中实际执行的查询、函数、诊断、变量访问、锁、设置和其他附加机制，并按语法动作顺序追加分类。

适用时遵守以下方法：

1. 外层语句确实执行时必须保留主分类，例如 `SELECT`、`INSERT`、`BLOCK`、`TRANSACTION`。
2. 内部机制具有独立安全、审计或资源语义时追加专属分类，不能用专属分类覆盖外层类型。
3. 附加动作无论出现在投影、条件、赋值、`VALUES`、来源查询或其他实际求值表达式中，都应被识别。
4. 同一动作出现多次只保留一个分类，并保持第一次出现时的动作顺序。
5. 未执行的定义体、执行体、字符串、注释和仅用于展示的内层 SQL 不适用本原则。
6. 专属语句本身没有独立外层动作时，仍只使用最准确的专属分类。例如普通 `EXPLAIN` 只生成计划，不能追加其未执行的内层语句类型。

```text
[SELECT|PERFORMANCE] <query invoking a performance diagnostic>;
[BLOCK|PERFORMANCE] <anonymous execution invoking a performance diagnostic>;
[PERFORMANCE|SESSION_VARIABLE_RW] <read diagnostics into a session variable>;
[TRANSACTION|SYSTEM_SETTING_WRITE] <change system-wide transaction characteristics>;
[TRANSACTION|SESSION_SETTING_WRITE] <change session transaction characteristics>;
```

## 3. 总体判定顺序

分类器必须按以下顺序判断：

1. 识别最外层语法结构和主要目标对象。
2. 确定定义头、查询体、执行体、字符串和注释的边界。
3. 优先匹配复制、发布订阅、日志、资源组、系统设置、性能、导入导出等专属能力域。
4. 再匹配 Catalog、Schema、Tablespace、Table、Column、Constraint、Index、Partition、View、编程对象等对象生命周期。
5. 再判断普通 `SELECT / INSERT / UPDATE / DELETE / MERGE`。
6. 再判断匿名执行、事务、动态 SQL 和其他不透明语义。
7. 无法由静态语法准确确定时归 `UNKNOWN`。

不得仅按首关键字分类。例如 `SHOW`、`ALTER`、`SET`、`EXECUTE` 都可能根据完整语法落入不同枚举。

## 4. `SplitQueryType` 完整适用范围

下表覆盖当前全部枚举项。表中的“授权”“目标”“审计”分别对应代码里的 `SecDataAuthKind`、`TargetType`、`SecQueryKind`。

### 4.1 Catalog

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `CREATE_CATALOG` | `SPACE` | `Catalog` | `CREATE` | 创建 Catalog。Catalog、Database、Schema 的对象层级必须根据当前数据源的对象模型确定，不能仅凭关键字使用本项。 |
| `ALTER_CATALOG` | `SPACE` | `Catalog` | `ALTER` | 修改 Catalog 自身定义或属性，不含重命名。 |
| `DROP_CATALOG` | `SPACE` | `Catalog` | `DROP` | 删除 Catalog 对象本身。 |
| `RENAME_CATALOG` | `SPACE` | `Catalog` | `ALTER` | 重命名或迁移 Catalog 身份；没有对应语法时不得模拟。 |
| `COMMENT_CATALOG` | `SPACE` | `Catalog` | `ALTER` | 新增、修改或删除 Catalog/Database 的持久化备注。 |

### 4.2 Schema

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `CREATE_SCHEMA` | `SPACE` | `Schema` | `CREATE` | 创建 Schema；方言中的 Database 是否等同 Schema 由数据源对象模型决定。 |
| `ALTER_SCHEMA` | `SPACE` | `Schema` | `ALTER` | 修改 Schema/Database 的定义、默认字符集、排序规则等属性。 |
| `DROP_SCHEMA` | `SPACE` | `Schema` | `DROP` | 删除 Schema/Database 对象本身。 |
| `RENAME_SCHEMA` | `SPACE` | `Schema` | `ALTER` | 重命名 Schema。当前数据源没有可靠原生语法时不得虚构支持。 |
| `COMMENT_SCHEMA` | `SPACE` | `Schema` | `ALTER` | 新增、修改或删除 Schema 的持久化备注。 |

### 4.3 Tablespace

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `CREATE_TABLESPACE` | `SPACE` | `Tablespace` | `CREATE` | 创建表空间。`USE LOGFILE GROUP` 只是引用日志设施，不增加日志分类。 |
| `ALTER_TABLESPACE` | `SPACE` | `Tablespace` | `ALTER` | 修改表空间定义、文件或状态。 |
| `DROP_TABLESPACE` | `SPACE` | `Tablespace` | `DROP` | 删除表空间对象本身。 |
| `RENAME_TABLESPACE` | `SPACE` | `Tablespace` | `ALTER` | 重命名表空间。 |
| `COMMENT_TABLESPACE` | `SPACE` | `Tablespace` | `ALTER` | 新增、修改或删除表空间备注。仅当备注直接属于表空间时使用。 |

### 4.4 Table

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `CREATE_TABLE` | `DDL` | `Table` | `CREATE` | 创建普通表、临时表或同类表对象。建表定义中的列、索引和约束不是被本类型吸收的细节，必须分别追加 `ADD_COLUMN`、`ADD_INDEX`、`ADD_CONSTRAINT`。CTAS 的定义查询不增加 `SELECT`。 |
| `ALTER_TABLE` | `DDL` | `Table` | `ALTER` | 修改表定义或表级属性，也是 `ALTER TABLE` 复合动作的主分类。 |
| `DROP_TABLE` | `DDL` | `Table` | `DROP` | 删除表对象本身。PostgreSQL `DISCARD TEMP/TEMPORARY` 会删除当前 Session 的全部临时表，也仅使用本分类，不追加 Session 设置分类。 |
| `RENAME_TABLE` | `DDL` | `Table` | `ALTER` | `RENAME TABLE` 或 `ALTER TABLE ... RENAME` 对表身份的修改。 |
| `COMMENT_TABLE` | `DDL` | `Table` | `ALTER` | 表备注变更；索引、列等子对象备注不能使用本项。 |
| `TRUNCATE_TABLE` | `DDL` | `Table` | `ALTER` | 清空整张表但保留表定义。它按 SQL 定义属于 DDL，不归普通 `DELETE`。 |
| `ADMIN_TABLE` | `ADMIN` | `Table` | `ADMIN` | 表级运维：`ANALYZE TABLE`、`OPTIMIZE TABLE`、`CHECK TABLE`、`CHECKSUM TABLE`、`REPAIR TABLE`、表级表空间导入/丢弃、闪回等；不表示逻辑表定义变更。物化视图刷新若已有解析器明确映射为通用 `ADMIN`，不得擅自改成本项。 |

外部表仍是表对象，使用普通表生命周期分类。PostgreSQL `IMPORT FOREIGN SCHEMA`
读取远端对象定义并在本地创建一组 Foreign Table，因此使用 `CREATE_TABLE`，不因语句名
包含 `IMPORT` 就追加表示业务行数据流入的 `DATA_IMPORT`。

### 4.5 Column

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `ADD_COLUMN` | `DDL` | `Column` | `ALTER` | 新增列。既用于 `ALTER TABLE ... ADD COLUMN`，也用于 `CREATE TABLE` 中显式定义的列，并分别与外层 `ALTER_TABLE` 或 `CREATE_TABLE` 共存。 |
| `ALTER_COLUMN` | `DDL` | `Column` | `ALTER` | 修改列类型、默认值、可空性、生成表达式、可见性、遮蔽属性等。 |
| `DROP_COLUMN` | `DDL` | `Column` | `ALTER` | 从表定义中删除列。 |
| `RENAME_COLUMN` | `DDL` | `Column` | `ALTER` | 修改列名；同一方言动作同时改名和改定义时可与 `ALTER_COLUMN` 共存。 |
| `COMMENT_COLUMN` | `DDL` | `Column` | `ALTER` | 修改列备注。 |
| `TRUNCATE_COLUMN` | `DDL` | `Column` | `ALTER` | 当前数据源明确提供列级截断/清空语法时使用；普通类型缩短、删除列或字符串函数不能使用。没有可靠原生语法时不得虚构支持。 |

### 4.6 Constraint

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `ADD_CONSTRAINT` | `DDL` | `Constraint` | `ALTER` | 向表定义新增主键、唯一键、外键、检查约束等约束对象，包括 `CREATE TABLE` 中的表级约束和列级约束；无论具体语法写作 `ADD` 还是 `CREATE`，统一使用本类型，不另设 `CREATE_CONSTRAINT`。 |
| `ALTER_CONSTRAINT` | `DDL` | `Constraint` | `ALTER` | 修改、启用、禁用或验证约束，不含单纯重命名和备注变更。 |
| `DROP_CONSTRAINT` | `DDL` | `Constraint` | `ALTER` | 删除约束；方言即使按主键、外键等约束种类提供不同关键字，也按真实对象语义使用本项。 |
| `RENAME_CONSTRAINT` | `DDL` | `Constraint` | `ALTER` | 修改约束名称。 |
| `COMMENT_CONSTRAINT` | `DDL` | `Constraint` | `ALTER` | 新增、修改或删除约束的持久化备注。 |

### 4.7 Index

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `ADD_INDEX` | `DDL` | `Index` | `CREATE` | 新增索引或 KEY，包括 `CREATE TABLE` 中显式定义的普通、全文和空间索引；无论具体语法写作独立 `CREATE INDEX`、`ALTER TABLE ... ADD INDEX` 还是内联定义，统一使用本类型，不另设 `CREATE_INDEX`。外层语句仍同时保留 `CREATE_TABLE` 或 `ALTER_TABLE`。 |
| `ALTER_INDEX` | `DDL` | `Index` | `ALTER` | 修改索引定义、属性、可见性或状态，不含单纯重命名。 |
| `DROP_INDEX` | `DDL` | `Index` | `DROP` | 独立删除索引，或数据库将索引作为独立对象删除。 |
| `RENAME_INDEX` | `DDL` | `Index` | `ALTER` | 重命名索引或 KEY。 |
| `COMMENT_INDEX` | `DDL` | `Index` | `ALTER` | 索引备注变更；`ADD INDEX ... COMMENT` 使用本项而不是 `COMMENT_TABLE`。 |

### 4.8 Partition

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `ADD_PARTITION` | `DDL` | `Partition` | `ALTER` | 新增分区。 |
| `DROP_PARTITION` | `DDL` | `Partition` | `ALTER` | 从表中删除分区及其数据。 |
| `ALTER_PARTITION` | `DDL` | `Partition` | `ALTER` | `COALESCE`、`REORGANIZE`、`EXCHANGE`、`REMOVE PARTITIONING`、重新定义分区方式等逻辑定义变化。 |
| `TRUNCATE_PARTITION` | `DDL` | `Partition` | `ALTER` | 清空指定分区但保留分区定义。 |
| `ADMIN_PARTITION` | `ADMIN` | `Partition` | `ADMIN` | 分区运维：`ANALYZE`、`CHECK`、`OPTIMIZE`、`REBUILD`、`REPAIR`、表空间导入/丢弃、辅助引擎装卸等。 |
| `COMMENT_PARTITION` | `DDL` | `Partition` | `ALTER` | 分区备注变更。当前数据源没有可靠原生语法时不得虚构支持。 |

### 4.9 View

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `CREATE_VIEW` | `OBJECT` | `View` | `CREATE` | 创建普通视图、物化视图和 JSON Duality View。View 的 `AS` 查询是其查询定义体，必须作为 `SELECT` 子节点递归收集。 |
| `ALTER_VIEW` | `OBJECT` | `View` | `ALTER` | 修改、替换或编译视图定义；物化视图和 JSON Duality View 不拆新分类。仅刷新物化数据而不修改定义时属于运维，不使用本项。 |
| `DROP_VIEW` | `OBJECT` | `View` | `DROP` | 删除普通视图、物化视图或 JSON Duality View。 |
| `RENAME_VIEW` | `OBJECT` | `View` | `ALTER` | 重命名普通视图、物化视图或 JSON Duality View；重命名其列仍使用 `RENAME_COLUMN`。 |
| `COMMENT_VIEW` | `OBJECT` | `View` | `ALTER` | 新增、修改或删除视图对象的持久化备注。 |

### 4.10 Sequence

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `CREATE_SEQUENCE` | `OBJECT` | `Sequence` | `CREATE` | 创建独立序列对象。当前数据源没有可靠原生语法时不得虚构支持。 |
| `ALTER_SEQUENCE` | `OBJECT` | `Sequence` | `ALTER` | 修改或重启序列，不含单纯重命名和备注变更。 |
| `DROP_SEQUENCE` | `OBJECT` | `Sequence` | `DROP` | 删除序列对象。 |
| `RENAME_SEQUENCE` | `OBJECT` | `Sequence` | `ALTER` | 修改序列名称。 |
| `COMMENT_SEQUENCE` | `OBJECT` | `Sequence` | `ALTER` | 新增、修改或删除序列的持久化备注。 |

### 4.11 Type

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `CREATE_TYPE` | `OBJECT` | `Type` | `CREATE` | 创建独立命名类型、域或同类类型对象，不指列上的内置数据类型声明。 |
| `ALTER_TYPE` | `OBJECT` | `Type` | `ALTER` | 修改独立类型定义、成员或所有者，不含单纯重命名和备注变更。 |
| `DROP_TYPE` | `OBJECT` | `Type` | `DROP` | 删除独立类型对象。 |
| `RENAME_TYPE` | `OBJECT` | `Type` | `ALTER` | 修改独立类型或 Domain 对象本身的名称；类型成员、属性和 Domain 约束重命名使用其真实子对象分类。 |
| `COMMENT_TYPE` | `OBJECT` | `Type` | `ALTER` | 新增、修改或删除独立类型或 Domain 的持久化备注。 |
| `ADMIN_TYPE` | `ADMIN` | `Type` | `ADMIN` | 编译、验证、刷新等不改变类型逻辑定义的运维操作。 |

### 4.12 Programming Object

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `CREATE_PROG_OBJ` | `OBJECT` | `ProgramObject` | `CREATE` | 创建 Procedure、Function、Routine、Aggregate、Operator、Operator Class、Operator Family、Cast、Package 等编程对象。PostgreSQL Operator Class/Family 中登记的运算符和支持函数，以及 `CREATE CAST ... WITH FUNCTION` 中登记的转换函数，都只是定义引用，不追加 `CALL_PROG_OBJ`。 |
| `ALTER_PROG_OBJ` | `OBJECT` | `ProgramObject` | `ALTER` | 修改编程对象定义、属性、名称或所属 schema。Operator Class/Family 的重命名和 schema 迁移也使用本分类；单独的 Owner 变更只使用 `TRANSFER_PRIVILEGE`。 |
| `DROP_PROG_OBJ` | `OBJECT` | `ProgramObject` | `DROP` | 删除上述编程对象，包括 Operator Class 和 Operator Family。 |
| `RENAME_PROG_OBJ` | `OBJECT` | `ProgramObject` | `ALTER` | 修改 Procedure、Function、Routine、Aggregate、Operator、Package 等编程对象的名称。 |
| `COMMENT_PROG_OBJ` | `OBJECT` | `ProgramObject` | `ALTER` | 编程对象备注变更，包括 Operator Class 和 Operator Family。定义头部的 `COMMENT` 可与创建或修改分类共存。 |
| `CALL_PROG_OBJ` | `CALL` | `ProgramObject` | `CALL` | 调用语法中名称明确的已存在过程、用户自定义函数、用户提供的可加载 UDF、用户自定义聚合函数或其他编程对象，如 `CALL p()`、`SELECT app_func(c)`。本类型与外层 `SELECT/INSERT/UPDATE/DELETE/MERGE/BLOCK` 共存并在同一语句内去重。数据源随附的存储过程仍保留本类型；只有已按数据源及版本显式登记到专属能力域时，才追加对应类型。普通执行表达式中的系统内置标量函数、内置聚合函数以及数据源随附的普通系统函数不增加本类型。schema/catalog 限定函数及显式引用符包裹的函数名按用户函数处理，即使末级名称与内置函数同名。执行 Cast 时，只有结合类型和数据库元数据确认命中了用户自定义 Cast，才追加本类型；内置 Cast 不追加。不用于动态字符串执行。 |
| `ADMIN_PROG_OBJ` | `ADMIN` | `ProgramObject` | `ADMIN` | 编译、验证、终止运行、刷新状态等不改变逻辑定义的编程对象运维。 |

`ProgramObject` 表示数据库中能够被调用、执行，或参与运行期行为分派的独立能力单元，不要求它必须使用
`CALL` 关键字直接调用。Procedure、Function、Routine、Aggregate、Operator、Operator Class/Family、
Cast 和 Package 均属于这一抽象。Operator 和 Cast 可以通过表达式间接触发，Operator Class/Family
可以参与索引访问方法分派，但它们的生命周期仍属于编程对象。

分类应跟随当前 SQL 直接创建、修改、删除或调用的对象，不能因为定义中引用了另一个编程对象就追加
其生命周期或调用分类。`CREATE CAST ... WITH FUNCTION`、`CREATE OPERATOR ... FUNCTION` 和
`CREATE OPERATOR CLASS ... FUNCTION` 都只登记未来使用的实现函数，执行这些 DDL 时不产生
`CALL_PROG_OBJ`。

Package 是编程对象容器，不承载 Table、View、Index 等普通模式对象。包内可以声明过程、函数、类型、变量和游标；包生命周期仍使用编程对象分类。

### 4.13 Trigger

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `CREATE_TRIGGER` | `OBJECT` | `Trigger` | `CREATE` | 创建触发器。触发事件和触发器执行体中的 DML 不增加分类。 |
| `ALTER_TRIGGER` | `OBJECT` | `Trigger` | `ALTER` | 修改、启停或编译触发器；应按数据库真实语义区分定义修改和运维。 |
| `DROP_TRIGGER` | `OBJECT` | `Trigger` | `DROP` | 删除触发器对象。 |
| `RENAME_TRIGGER` | `OBJECT` | `Trigger` | `ALTER` | 修改普通触发器或 Event Trigger 的名称。 |
| `COMMENT_TRIGGER` | `OBJECT` | `Trigger` | `ALTER` | 触发器备注变更。当前数据源没有可靠原生语法时不得虚构支持。 |

### 4.14 Synonym

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `CREATE_SYNONYM` | `OBJECT` | `Synonym` | `CREATE` | 创建同义词。当前数据源没有可靠原生语法时不得虚构支持。 |
| `ALTER_SYNONYM` | `OBJECT` | `Synonym` | `ALTER` | 修改或替换同义词定义。 |
| `DROP_SYNONYM` | `OBJECT` | `Synonym` | `DROP` | 删除同义词。 |
| `RENAME_SYNONYM` | `OBJECT` | `Synonym` | `ALTER` | 修改同义词名称。 |
| `COMMENT_SYNONYM` | `OBJECT` | `Synonym` | `ALTER` | 同义词备注变更。 |

### 4.15 Event

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `CREATE_EVENT` | `OBJECT` | `Event` | `CREATE` | 创建当前数据源定义的独立调度事件。事件动作属于执行体，应按第 5 节进入 children，不扁平追加到根类型集合。 |
| `ALTER_EVENT` | `OBJECT` | `Event` | `ALTER` | 修改事件调度、状态、执行定义或其他属性。 |
| `DROP_EVENT` | `OBJECT` | `Event` | `DROP` | 删除事件对象。 |
| `RENAME_EVENT` | `OBJECT` | `Event` | `ALTER` | 修改事件名称。 |
| `COMMENT_EVENT` | `OBJECT` | `Event` | `ALTER` | 修改事件定义头部的备注。 |

### 4.16 Resource Group

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `CREATE_RESOURCE_GROUP` | `ADMIN` | `ResourceGroup` | `CREATE` | 创建资源组。 |
| `ALTER_RESOURCE_GROUP` | `ADMIN` | `ResourceGroup` | `ALTER` | 修改资源组定义和资源限制。 |
| `DROP_RESOURCE_GROUP` | `ADMIN` | `ResourceGroup` | `DROP` | 删除资源组对象。 |
| `ADMIN_RESOURCE_GROUP` | `ADMIN` | `ResourceGroup` | `ADMIN` | 线程绑定、运行时分配或其他资源组运维，具体语法根据数据源能力判断。 |

### 4.17 Job

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `CREATE_JOB` | `ADMIN` | `Job` | `CREATE` | 创建当前数据源中的独立 Job/Scheduler Job。若数据源把调度对象定义为 Event，应使用 Event 分类，不能按近似概念重复归 Job。 |
| `ALTER_JOB` | `ADMIN` | `Job` | `ALTER` | 修改 Job 定义、计划、动作或属性。 |
| `DROP_JOB` | `ADMIN` | `Job` | `DROP` | 删除 Job 对象。 |
| `RENAME_JOB` | `ADMIN` | `Job` | `ALTER` | 修改 Job 名称。 |
| `COMMENT_JOB` | `ADMIN` | `Job` | `ALTER` | 新增、修改或删除 Job 的持久化备注。 |
| `ADMIN_JOB` | `ADMIN` | `Job` | `ADMIN` | 启用、停用、立即运行、停止、刷新等 Job 运维。 |

### 4.18 User、Role 与授权

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `CREATE_USER` | `ADMIN` | `User` | `CREATE` | 创建数据库用户或登录主体。 |
| `DROP_USER` | `ADMIN` | `User` | `DROP` | 删除用户。 |
| `RENAME_USER` | `ADMIN` | `User` | `ALTER` | 修改用户身份名称。 |
| `ALTER_USER` | `ADMIN` | `User` | `ALTER` | 修改密码、认证方式、锁定状态、默认角色、资源限制等用户属性。 |
| `COMMENT_USER` | `ADMIN` | `User` | `ALTER` | 用户备注变更。 |
| `CREATE_ROLE` | `ADMIN` | `Role` | `CREATE` | 创建角色。 |
| `DROP_ROLE` | `ADMIN` | `Role` | `DROP` | 删除角色。 |
| `ALTER_ROLE` | `ADMIN` | `Role` | `ALTER` | 修改角色属性或定义。 |
| `RENAME_ROLE` | `ADMIN` | `Role` | `ALTER` | 修改角色名称。 |
| `COMMENT_ROLE` | `ADMIN` | `Role` | `ALTER` | 角色备注变更。 |
| `GRANT` | `ADMIN` | `UserOrRole` | `ALTER` | 实际授予权限或角色。权限列表中的 `SELECT/INSERT/UPDATE` 只是授权项，不产生 DML 分类。 |
| `REVOKE` | `ADMIN` | `UserOrRole` | `ALTER` | 实际撤销权限或角色。目标能解析时应在资源层区分 User 与 Role。批量删除角色拥有对象并撤销其权限的 `DROP OWNED` 也使用本分类。 |
| `TRANSFER_PRIVILEGE` | `ADMIN` | `UserOrRole` | `ALTER` | 在主体之间整体转移所有权或权限归属，不展开成 `GRANT` 与 `REVOKE`，也不套用两者的权限资源清单语义。 |

权限转移与授权、回收是三个平级动作，不使用 `ADMIN` 作为兜底分类。例如 PostgreSQL
`REASSIGN OWNED BY old_role TO new_role` 应分类为 `[TRANSFER_PRIVILEGE]`。该语句转移
旧角色所拥有对象的所有权，但不包含 `GRANT/REVOKE` 的对象权限清单，因此不能拆成
`[GRANT|REVOKE]`。显式修改单个对象所有者时，`ALTER` 只是所有权转移语法的外壳，
没有同时修改对象定义，因此 `ALTER TABLE t OWNER TO new_owner` 及其他
`ALTER ... OWNER TO ...` 均只使用 `[TRANSFER_PRIVILEGE]`，不叠加对象的 `ALTER_*`、
`SYSTEM_SETTING_WRITE`、`ADMIN_PERFORMANCE` 或 `UNKNOWN`。

PostgreSQL User Mapping 是本地数据库主体到 Foreign Server 远端身份的授权绑定，不是
用户对象生命周期，也没有把已有权限从一个主体转移给另一个主体。创建映射或修改映射
使用 `GRANT`，删除映射使用 `REVOKE`；`ALTER USER MAPPING ... OPTIONS (DROP ...)`
只是修改仍然存在的绑定，仍使用 `GRANT`。Foreign Server 的实际使用资格由独立的
`GRANT/REVOKE USAGE ON FOREIGN SERVER` 控制，不能与 User Mapping 身份绑定混为同一条权限。

Redis `ACL SETUSER` 是对用户命令、Key、Channel 和认证规则的授权调整，不按用户是否
已存在推断 `CREATE_USER` 或 `ALTER_USER`。增加权限规则、访问模式、密码或启用用户使用
`GRANT`；删除规则、关闭用户或 `reset` 使用 `REVOKE`；同一命令同时包含增加和删除规则时
使用 `GRANT|REVOKE`。该命令没有在两个主体之间迁移既有权限，因此不使用
`TRANSFER_PRIVILEGE`。`ACL DELUSER` 删除的是用户主体本身，仍使用 `DROP_USER`；ACL
目录、用户和规则展示仍按 `METADATA` 处理。

### 4.19 Library

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `CREATE_LIBRARY` | `OBJECT` | `Library` | `CREATE` | 创建或安装数据库可管理的外部代码库、Library、Extension、Plugin 或 Component 对象。源码中的关键词不参与分类。 |
| `ALTER_LIBRARY` | `OBJECT` | `Library` | `ALTER` | 修改 Library 或 Extension 的定义、版本、位置、凭证、属性、名称、所属 schema 或成员关系。 |
| `DROP_LIBRARY` | `OBJECT` | `Library` | `DROP` | 删除或卸载 Library、Extension、Plugin 或 Component 对象，不表示删除操作系统文件。 |
| `COMMENT_LIBRARY` | `OBJECT` | `Library` | `ALTER` | Library 备注变更。 |

`Library` 是可执行代码、模块、Extension 或函数集合的部署与承载载体，不是一个可直接调用的能力单元，因此不使用
`*_PROG_OBJ`。Library 中暴露的 Function、Routine 等仍是独立的 `ProgramObject`。管理 Library
本身使用 `*_LIBRARY`；创建引用既有 Library 的 Function 时管理的是 Function，使用
`CREATE_PROG_OBJ`，不能因为引用了 Library 文件就追加 `CREATE_LIBRARY`。

Library 生命周期已经完整表达外部代码载体的创建、安装、修改、替换、删除和卸载，不再叠加
`UNSAFE`。不能仅因为 Library 可能包含黑盒代码、卸载后可能影响依赖对象，或安装过程可能执行初始化
逻辑就追加 `UNSAFE`；否则普通对象生命周期也会因潜在后果被无边界扩大。MySQL
`INSTALL/UNINSTALL PLUGIN`、`INSTALL/UNINSTALL COMPONENT` 和 PostgreSQL
`CREATE/ALTER/DROP EXTENSION` 均只使用对应的 `*_LIBRARY`。Library 创建或安装语法内部的源码、
选项、初始化参数和变量引用均属于该 Library 生命周期的组成部分，不再拆出 `SESSION_VARIABLE_RW`、
`SYSTEM_SETTING_WRITE` 或 `UNSAFE`；只有 Library 语句之外真正独立的同级动作才按多动作共存规则
追加其他分类。

### 4.20 Replication

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `CREATE_REPLICATION` | `ADMIN` | `Replication` | `CREATE` | 数据库存在明确创建复制关系/槽/通道的语法时使用。 |
| `ALTER_REPLICATION` | `ADMIN` | `Replication` | `ALTER` | 修改复制配置、过滤条件、连接关系或运行状态，具体控制语法由数据源决定。 |
| `DROP_REPLICATION` | `ADMIN` | `Replication` | `DROP` | 数据库明确删除复制对象或关系时使用；不能凭 `RESET` 猜测删除。 |
| `ADMIN_REPLICATION` | `ADMIN` | `Replication` | `ADMIN` | 不属于复制关系定义修改的复制运维，包括复制事件回放和同步位置等待。复制等待函数可能出现在任意可执行表达式中，应保留外层语句类型并追加本类型；具体函数或语法必须按数据源及版本确认。 |

复制分类表达具体的创建、配置修改、运行控制、同步等待、删除和事件回放动作。只读查询复制状态或拓扑不属于复制操作，使用 `METADATA`。同步等待会阻塞当前 Session 直到复制位置条件满足、超时或失败，因此不是普通元信息读取、性能诊断或日志内容读取。

### 4.21 Publication / Subscription

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `CREATE_PUB_SUB` | `ADMIN` | `PublicationSubscription` | `CREATE` | 创建 Publication 或 Subscription。它们是发布/订阅模型两端，共用分类但资源身份仍可区分。 |
| `ALTER_PUB_SUB` | `ADMIN` | `PublicationSubscription` | `ALTER` | 修改成员、连接、所有者、名称或定义属性。`ALTER ... DROP ...` 删除成员不等于删除发布/订阅对象。 |
| `DROP_PUB_SUB` | `ADMIN` | `PublicationSubscription` | `DROP` | 删除 Publication 或 Subscription 对象本身。 |
| `ADMIN_PUB_SUB` | `ADMIN` | `PublicationSubscription` | `ADMIN` | 启停、刷新、跳过事务、发布消息或订阅运行控制等运维，按当前数据源的发布订阅模型判断。PostgreSQL `LISTEN`、`UNLISTEN`、`NOTIFY` 均属于该领域。 |

### 4.22 Log

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `CREATE_LOG` | `ADMIN` | `Log` | `CREATE` | 创建当前数据源可独立管理的日志基础设施对象。 |
| `ALTER_LOG` | `ADMIN` | `Log` | `ALTER` | 修改日志基础设施对象的定义。 |
| `DROP_LOG` | `ADMIN` | `Log` | `DROP` | 删除日志基础设施对象本身。 |
| `LOG_READ` | `READ` | `Log` | `READ` | 读取事务日志、复制中继日志、归档日志、日志目录、日志运行坐标或日志事件；不包括事件回放。专属日志读取已经完整表达该动作，不附加通用 `METADATA`。 |
| `ADMIN_LOG` | `ADMIN` | `Log` | `ADMIN` | 日志运行状态管理、开关、归档、访问控制以及直接写入审计事件等高阶操作。通过可执行表达式触发时保留外层动作并追加本类型。 |
| `MAINTAIN_LOG` | `ADMIN` | `Log` | `ADMIN` | 日志轮转、清理、刷新、重置等维护操作。 |

数据源中用于承载日志的独立存储基础设施归日志对象，不应因名称特殊而降级为
`UNKNOWN`。创建语句中的备注属于日志对象属性；当前枚举没有日志备注分类，不得
虚构新 Code。

### 4.23 Settings、Variables 与通用 Admin

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `SESSION_VARIABLE_RW` | `ADMIN` | `ConfigKey` | `OTHER` | 显式读取、写入或以方向不分离方式访问当前 Session 的变量或配置值。过程参数方向无法由静态语法确认时，不依赖运行期元数据猜测读写方向；变量列举属于元信息读取，不使用本项；明确的全局或持久化配置也不使用本项。 |
| `SESSION_SETTING_WRITE` | `ADMIN` | `ConfigKey` | `OTHER` | 修改、清理或重置会跨语句持续、但只影响当前连接的会话设置、SQL 模式、事务特征、客户端运行模式或连接状态。通过函数或其他可执行表达式修改时与外层动作共存。PostgreSQL `DISCARD SEQUENCES` 和 `DISCARD ALL` 均按当前 Session 状态重置归入本项；`DISCARD PLANS` 与 `DISCARD TEMP/TEMPORARY` 仍使用其更准确的专属分类。仅在当前语句执行期间临时生效并在结束后恢复的 Hint 不使用本项。 |
| `SYSTEM_SETTING_WRITE` | `ADMIN` | `ConfigKey` | `OTHER` | 修改数据库系统控制面的配置状态。系统配置不局限于标量参数键，还包括全局或持久化设置、实例默认值、系统级对象定义、插件或组件注册、安全基础设施、规则字典及其他全局功能状态。`ConfigKey` 表示逻辑配置项，不要求 SQL 中必须出现设置关键字、变量名或字面参数键。只要动作改变了系统后续运行所依据的全局配置或功能状态，就应考虑本类型；专属复制、日志、资源组等分类仍然优先。PostgreSQL `CREATE/DROP ACCESS METHOD` 注册或注销数据库级存储、索引实现能力，`COMMENT ON ACCESS METHOD` 写入或清除该系统对象的持久化说明，均使用本分类。此注释规则仅适用于 Access Method，不覆盖表、列及其他拥有专属 `COMMENT_*` 分类的对象。具体表执行 `ALTER TABLE ... SET ACCESS METHOD ...` 只是修改表的存储属性，仅使用 `ALTER_TABLE`。 |
| `ADMIN` | `ADMIN` | `Unknown` | `ADMIN` | 对当前实例、进程、连接、查询或任务执行即时运行管理，但不建立系统后续运行所依据的配置状态，例如 `KILL`、实例备份锁的获取和释放。它也是已确认属于数据库管理操作、但当前没有更准确专属枚举时的最后管理兜底，包括读取或直接返回真实密码、密钥、私钥等秘密材料。是否使用必须根据当前数据源解析器和对象模型决定；例如：某些数据源将 `REFRESH MATERIALIZED VIEW` 的运行刷新归入本项。PostgreSQL `REINDEX` 重建既有索引的物理内容但不创建、删除或修改索引逻辑定义，无论目标为 `INDEX`、`TABLE`、`SCHEMA`、`DATABASE` 还是 `SYSTEM`，均统一使用本项。不能用于普通未知语句。 |

PostgreSQL Foreign Data Wrapper 和 Foreign Server 是数据库外部访问能力的控制面定义，
其创建、修改、重命名和删除统一使用 `SYSTEM_SETTING_WRITE`，不归入数据复制。
修改这两类对象的 Owner 时只发生所有权转移，使用 `TRANSFER_PRIVILEGE`。Foreign Table
与 User Mapping 分别按表对象生命周期和身份授权绑定规则处理，不随 FDW/Server 一并
归入系统配置。

读取上一条语句结果、影响行数等瞬时连接诊断状态的内置能力，不因返回 Session
相关值就自动增加 `SESSION_VARIABLE_RW` 或 `CALL_PROG_OBJ`；按承载它的外层语句
归类。若同名能力的带参形式会修改跨语句持续的连接状态，则应额外增加
`SESSION_SETTING_WRITE`。

`SYSTEM_SETTING_WRITE` 和 `ADMIN` 可以共享 `SecDataAuthKind.MANAGE`，但判定依据不同：前者回答“修改了哪项系统配置或全局功能状态”，后者回答“对当前运行中的系统执行了什么即时管理动作”。不能因为插件、组件或全局功能不是字面参数键，就把其配置动作降级为 `ADMIN`。

修改安全、审计、密钥等系统能力的全局配置时使用 `SYSTEM_SETTING_WRITE`。配置生效
过程中由数据库内部条件性触发的日志轮转、缓存刷新或清理不是 SQL 直接表达的动作，
不追加相应运维分类。

读取或直接返回真实密码、密钥、私钥等秘密材料的能力使用 `ADMIN`，并保留承载它的
外层动作。只返回类型、长度等非秘密属性不追加 `ADMIN`；普通加解密、签名、随机数和
公钥运算也不因处理调用方提供的数据而自动归入本项。

### 4.24 Context Switch

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `SWITCH_CATALOG` | `READ` | `Catalog` | `QUERY` | 切换当前 Catalog 上下文，不创建或修改 Catalog。 |
| `SWITCH_SCHEMA` | `READ` | `Schema` | `QUERY` | 切换当前 Schema/Database 上下文，具体语法和对象层级由数据源决定。 |
| `SWITCH_USER` | `READ` | `User` | `OTHER` | 切换或模拟当前执行用户/安全主体；认证登录协议本身不一定是 SQL 分类。 |
| `SWITCH_ROLE` | `READ` | `Role` | `OTHER` | 激活、停用或切换当前会话角色，如 `SET ROLE`。它不是角色定义修改。 |

### 4.25 Metadata

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `METADATA` | `READ` | `Unknown` | `QUERY` | 读取数据库元信息，包括系统元信息、对象定义或结构元信息、用户/角色/授权等权限元信息，以及数据库内置帮助系统中的 SQL 语法和参考信息。具体资源目标继续由资源分析和安全域分析表达，不能为了统一元信息动作而伪造单一对象类型。 |

元信息读取按实际动作归类，不按返回内容中的 DDL/DCL 关键字归类。例如：`SHOW CREATE TABLE` 读取表定义，不使用 `CREATE_TABLE`；`SHOW GRANTS` 读取授权结果，不使用 `GRANT`。直接查询系统 Catalog 表时必须由数据源维护的全限定对象清单判定，不能只根据 schema 名字符串、对象末级名称或通配规则判断。

数据源可以维护版本化的系统资源清单，用于识别系统元信息表、系统 View、系统存储
过程和系统存储函数。清单必须使用“版本、对象种类、全限定对象名”精确登记，禁止
任意版本范围和 schema 级通配；升级目标版本时必须在真实干净实例上重新探测并复核
差异。

当目标数据库源码可获得时，系统资源清单还必须通过源码注册点反向生成或校验：

1. `INFORMATION_SCHEMA`、`PERFORMANCE_SCHEMA`、metrics 等虚拟 schema 从实际表 map 和
   virtual-table 注册入口提取，不能从兼容数据库文档复制。
2. 内部 `mysql`、`sys` 或等价 schema 的对象从 bootstrap 表、升级后仍存在的表、运行时初始化
   表和实际创建的 View 提取；源码中仅保留但最新干净实例不再创建的废弃 DDL 不能自动加入。
3. package init 或插件在静态 map 之外动态追加的系统表必须单独追踪；只有可确定启用条件和版本
   时才能登记。任意第三方扩展动态注册的对象不能冒充数据库静态内置对象。
4. 清单项必须是 `schema.object` 精确名称。schema 名本身、对象末级名称、前缀、正则和通配符
   都不能成为 `METADATA` 判定依据。
5. 生成文件记录目标数据库源码 revision、来源文件和注册变量，并提供可重复的生成/校验命令；
   手工修改后无法通过反向校验的清单不得合入。
6. 最新版本清单只能证明对象属于最新版本，不能自动证明旧主版本也存在。分类 API 已接收 version
   时不得忽略该参数；必须通过分版本源码/实例差异补齐最早支持版本。
7. 元数据特殊映射、权限豁免和行为对象识别都先命中同一版本化事实表，不能由遗留 switch 绕过
   清单继续把已删除对象当系统对象。

TiDB 实施中，清单从其 infoschema 注册、performance schema map、`MetricTableMap`、system
bootstrap/DDL 表、`sys` View 和 workload repository 表生成。由 MySQL 8 复制的 InnoDB、复制
Performance Schema 等对象被删除，TiDB 自己的 `CLUSTER_*`、`METRICS_SCHEMA` 和内部表被补齐。
该提取路线不能原样复制给其他数据库；其他数据库必须重新定位自己的注册入口。

直接查询已登记的系统元信息对象时按实际来源组合分类：

```text
[METADATA] SELECT * FROM system_catalog.columns;
[SELECT|METADATA] SELECT * FROM app.orders o JOIN system_catalog.columns c ON ...;
[DATA_EXPORT|METADATA] <export query reading only system_catalog.columns>;
[CREATE_TABLE|METADATA] CREATE TABLE snapshot AS SELECT * FROM system_catalog.users;
```

- 只读取清单内元信息对象的查询使用 `METADATA`，不再重复增加普通 `SELECT`。
- 同一条实际查询同时读取普通表和清单内元信息对象时使用 `SELECT|METADATA`。
- 导出、CTAS、DML 等外层动作继续保留自身分类；其查询来源只有元信息对象时不额外增加 `SELECT`，同时读取普通表时才增加 `SELECT`。
- CTE、派生表和子查询不能遮蔽实际来源；最终只读取元信息对象时仍使用 `METADATA`。
- 清单匹配要求 SQL 中出现完整 `schema.object`。同名普通表、未限定对象、字符串、注释和对象定义体中的查询都不命中。
- 系统内置函数仅返回运行值时不因函数本身增加 `METADATA`；系统存储函数或存储过程仍按 `CALL_PROG_OBJ` 表达调用动作。
- 数据源随附 Routine 只有已按当前版本显式登记到现有能力域时，才在保留 `CALL_PROG_OBJ` 的同时追加对应领域类型；不得根据名称、系统 schema、文档描述或可能产生的间接副作用临时推断。

当数据源把系统资源清单同时用于查询权限豁免时，分类与权限豁免不能混为一谈：

- `TABLE/VIEW` 仅在读取权限校验时豁免；对系统表的写入、DDL 或管理操作不豁免。
- `PROCEDURE/FUNCTION` 仅在 `CALL_PROG_OBJ` 调用权限校验时豁免。
- 豁免资源仍必须保留在资源分析、安全域分析和审计结果中，只跳过对象级访问权限校验；脱敏、安全规则和审计继续按各自规则处理。
- 工单中的资源范围检查同样忽略豁免资源，避免系统对象因不属于业务 Schema 而被误判越界。
- 不在清单中的用户存储过程、用户函数和普通表继续正常参与权限校验；对象名称相同但 schema 不同不能命中。

### 4.26 Policy

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `CREATE_POLICY` | `ADMIN` | `Policy` | `CREATE` | 创建具名策略对象，包括行级访问、脱敏、遮蔽、PostgreSQL 查询重写 Rule、独立 Collation 比较与排序策略、字符编码 Conversion 转换策略，以及 Text Search Configuration、Dictionary、Parser、Template 全文检索策略对象。不能把 Policy 固定解释为安全策略或脱敏策略。`CREATE RULE` 只记录规则对象的创建，不递归收集未来匹配时才执行的 Rule Action；`CREATE CONVERSION ... FROM function` 只引用转换函数，不追加 `CALL_PROG_OBJ`。 |
| `ALTER_POLICY` | `ADMIN` | `Policy` | `ALTER` | 修改具名策略定义、绑定、表达式、名称、所属 schema 或持久化备注。PostgreSQL `ALTER/COMMENT ON RULE`、`ALTER/COMMENT ON COLLATION`、`ALTER/COMMENT ON CONVERSION` 以及 Text Search 策略的配置、映射、重命名、schema 迁移和备注变更使用本分类；单独的 Owner 变更只使用 `TRANSFER_PRIVILEGE`。 |
| `DROP_POLICY` | `ADMIN` | `Policy` | `DROP` | 删除策略对象本身。PostgreSQL `DROP RULE`、`DROP COLLATION`、`DROP CONVERSION` 和 `DROP TEXT SEARCH ...` 使用本分类。列上的 `ADD/DROP MASKED` 是列属性修改，不是删除策略。数据库、表、列或表达式通过 `COLLATE` 或全文检索函数选择既有策略时，仍按所属数据库、表、列或查询语句分类，不视为策略对象生命周期操作。 |

对象生命周期和备注遵循以下统一规则：

- `Constraint`、`View`、`Sequence`、`Type`、`ProgramObject`、`Schema`、`Catalog`、
  `Index`、`Trigger`、`Synonym`、`Event`、`Job`、`User`、`Role` 等已建立公共对象模型
  的类型，应区分定义修改、对象重命名和备注变更，分别使用 `ALTER_*`、`RENAME_*`
  和 `COMMENT_*`，不能再因为语句以 `ALTER` 开头就全部压入 `ALTER_*`。
- Constraint 和 Index 是所属表的子对象，新增动作分别统一使用 `ADD_CONSTRAINT` 和
  `ADD_INDEX`。具体语法写作独立 `CREATE` 或表级 `ADD` 不产生第二套同义分类。
- `CREATE TABLE` 必须继续收集其结构定义动作：只要显式定义了列就追加 `ADD_COLUMN`，
  定义普通索引就追加 `ADD_INDEX`，定义列级或表级 PK、UK、FK、CHECK 就追加
  `ADD_CONSTRAINT`。这些类型与 `CREATE_TABLE` 并列并按首次发现顺序去重。
- 没有对应 `COMMENT_*` 分类的对象，设置、修改或清除持久化备注才默认使用该对象的
  `ALTER_*` 分类。
- 该兜底规则只解决备注动作如何表达，不反向决定尚未明确归属领域的对象生命周期；
  对象本身的 `CREATE/ALTER/DROP` 分类仍未确定时，其 `COMMENT ON` 应随该裁决一起处理。

### 4.27 DQL 与 DML

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `SELECT` | `READ` | `Query` | `QUERY` | 实际执行并返回/读取数据的查询，包括 `SELECT`、顶层 `TABLE`、顶层 `VALUES` 和可确认的只读键查询。方言中只提供表读取能力的专属语句也使用本类型。Redis `ECHO` 类比返回字面量的查询；`LOLWUT` 只生成并返回展示文本；`CLUSTER KEYSLOT` 只计算并返回 Key 的哈希槽；`TIME` 只读取并返回服务器时间；不含子操作的合法 `BITFIELD key` 返回空结果且没有副作用，这些命令均使用本类型。对象定义体中的查询不使用。 |
| `INSERT` | `WRITE` | `Insert` | `DML` | 明确只新增数据的操作。带实际执行的来源查询时可增加 `SELECT`。 |
| `UPDATE` | `WRITE` | `Update` | `DML` | 明确修改已有数据的操作。 |
| `DELETE` | `WRITE` | `Delete` | `DML` | 明确删除已有数据的操作。 |
| `MERGE` | `WRITE` | `Update` | `DML` | 写入结果由匹配、冲突、运行时状态或目标整体替换机制决定，且无法准确拆成独立 `INSERT/UPDATE/DELETE` 动作的复合写入，包括标准 `MERGE`、Upsert、原子 `INSERT OVERWRITE` 及各数据源等价语法。split、资源分析和安全域必须统一使用本类型；安全域可以继续使用单一 Insert Domain，但其 `sqlType` 必须设置为 `MERGE`。它不表示同名字符串函数。 |

普通 DML 中真实执行的嵌套查询可以增加 `SELECT`：

```text
[INSERT|SELECT] INSERT INTO dst SELECT * FROM src;
[UPDATE|SELECT] UPDATE t SET c = (SELECT MAX(c) FROM s);
[DELETE|SELECT] DELETE FROM t WHERE id IN (SELECT id FROM s);
[MERGE|SELECT] MERGE INTO dst USING (SELECT * FROM src) s ON (...);
```

管理型内置函数按“外层动作与附加动作共存原则”处理，不替代承载它的外层语句类型。管理函数无论出现在投影、`WHERE`、`HAVING`、赋值表达式、`INSERT VALUES` 或来源查询中，都按语法出现顺序追加对应的专属分类。

例如，一个已登记为策略修改能力的管理函数出现在查询投影中时使用
`SELECT|ALTER_POLICY`；出现在 `INSERT VALUES` 中时使用
`INSERT|ALTER_POLICY`；出现在 `INSERT ... SELECT` 的来源查询中时使用
`INSERT|SELECT|ALTER_POLICY`。

同一管理函数在 `UPDATE`、`DELETE`、`MERGE` 或其他可执行表达式中也采用相同追加规则。系统内置普通聚合函数和无管理副作用的普通内置函数不增加专属类型；无法确认是系统函数的用户函数仍按 `CALL_PROG_OBJ` 规则处理。

数据源内置函数和随附的可加载函数必须按当前版本语法族识别，不能使用所有版本名称的
并集。名称在当前语法族中未登记时，即使它在其他版本是系统函数，也按可能的用户函数
处理并追加 `CALL_PROG_OBJ`。功能型函数根据真实动作追加专属分类；普通延时函数的
阻塞时长和资源风险属于函数级执行策略，不因“耗时”自动追加 `PERFORMANCE` 或
`UNSAFE`。

系统函数事实表必须由目标数据库自身注册链证明：

1. 普通标量函数以执行层实际 builtin registry 为核心，不能只扫描 AST 函数常量或 grammar
   token；常量存在不代表运行时可调用。
2. parser 特殊函数、planner 重写函数、聚合函数和窗口函数可能不进入普通 registry，应从真实
   parser/planner/aggregation/window 入口补齐。
3. extension/plugin 动态函数与静态内置函数分开保存；只有目标版本默认注册或明确加载的函数才能
   进入对应系统函数集合。
4. 兼容数据库的 Audit、Group Replication、Keyring、Firewall、空间扩展等函数不能按名称复制；
   目标源码未注册时必须按 UDF 候选处理。
5. 功能型函数的专属分类 map 只对已命中当前版本系统函数事实表的名称生效。名称从事实表删除后，
   遗留 action set/switch 不得继续把它识别为管理函数并绕过 `CALL_PROG_OBJ`。
6. schema/catalog 限定或显式引用符包裹的函数优先按用户对象处理；不能仅比较末级名称命中未限定
   内置函数。
7. 事实表记录源码 revision 和注册点，提供生成器一致性测试，并同时覆盖目标数据库特有正例和
   兼容数据库独有负例。

查询修饰符或读取上一条查询瞬时诊断结果的内置函数不构成显式变量访问或配置修改，
除非当前数据源的语义明确表明它会修改跨语句持续的 Session 状态。

### 4.28 Procedural、Transaction、Lock、Import/Export

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `BLOCK` | `OTHER` | `Unknown` | `OTHER` | 不创建持久对象而直接执行的匿名程序块或无结果表达式执行，具体形式由数据源决定。 |
| `PROGRAM_CONTROL` | `OTHER` | `Unknown` | `OTHER` | 可独立成立并直接控制程序执行路径或 SQL condition 的单点语句。只分类当前语句节点，不扫描程序对象定义体，也不因代码块容器而扁平收集其内部语句。控制结构仅在具体数据库允许其作为独立语句且存在对应单点 fixture 时使用本类型；表达式中的条件函数或条件表达式不属于本类型。 |
| `TRANSACTION` | `OTHER` | `Unknown` | `OTHER` | 事务开始、提交、回滚、Savepoint、事务特征控制及分布式事务状态与恢复协议。只读的事务恢复查询仍优先归事务域，不叠加 `METADATA` 或 `PERFORMANCE`。匿名过程块的开始关键字不能误判为事务。 |
| `QUERY_LOCK` | `OTHER` | `Query` | `OTHER` | 查询主动请求并随当前查询生命周期持有的数据锁。它是外层 `SELECT/INSERT/UPDATE/MERGE` 等类型的附加分类；CTAS 查询实际携带锁子句时可与 `CREATE_TABLE` 共存。仅展示计划的语句和对象定义体中的锁子句不增加本项。 |
| `SESSION_LOCK` | `OTHER` | `Unknown` | `OTHER` | 当前 Session 主动获取、释放或跨语句维持生命周期的命名锁、对象锁或实例锁。与管理或外层语句组合时保留所有实际动作。只观察锁状态不增加本项；只在当前语句执行期间生效的并发选项或数据库自动产生的锁也不增加本项。 |
| `DATA_IMPORT` | `WRITE` | `Unknown` | `ADMIN` | 数据从当前数据库实例外部流入当前实例，包括文件、对象存储、远端实例或外部表空间等来源。若导入是表、分区等对象运维的一部分，应同时保留外层对象及运维分类；丢弃本地对象或表空间不等于导出。导入选项中的冲突处理关键字不改变主分类。 |
| `DATA_EXPORT` | `READ` | `Unknown` | `ADMIN` | 数据从当前数据库实例流向文件、对象存储、目录、远端实例或其他外部目标。普通客户端查询结果返回不算导出。查询式导出仍保留真实执行的查询类型；非查询式导出不增加 `SELECT`。若导出动作同时显式建立跨语句 Session 锁，应追加 `SESSION_LOCK`。 |

服务器文件读取函数会把数据库实例外部的数据带入 SQL 执行域，应追加
`DATA_IMPORT`；当文件目标无法由 SQL 文本静态限定或防御时还应追加 `UNSAFE`。
承载函数的外层动作继续保留。相反，对象定义中的目录或文件路径若只描述对象的存储
位置，并未执行数据流动，则不增加 `DATA_IMPORT`、`DATA_EXPORT` 或 `UNSAFE`。

只有 SQL 显式表达“获取、释放或维持锁”的动作才增加锁分类。普通 `INSERT/UPDATE/DELETE`、外键检查、DDL 元数据锁、账户 `LOCK/UNLOCK` 属性以及存储引擎自动产生的行锁、间隙锁、意向锁，都不因运行期可能加锁而增加锁分类。

### 4.29 Performance

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `PERFORMANCE` | `READ` | `Query` | `QUERY` | 不产生业务数据副作用的执行计划、解析树、性能诊断、运行状态、Session 诊断区、Profile、Processlist 以及优化器专属统计视图读取。以 `SELECT` 查询 `pg_stats*`、`pg_statistic*` 等 PostgreSQL 优化器统计关系时与 `SELECT` 共存。 |
| `ADMIN_PERFORMANCE` | `ADMIN` | `Unknown` | `ADMIN` | 创建、修改或删除优化器统计等性能对象，或者改变缓存、统计、优化器成本及其他性能运行状态。PostgreSQL `CREATE/ALTER/DROP STATISTICS` 统一使用本分类；`ALTER STATISTICS ... OWNER TO ...` 只使用 `TRANSFER_PRIVILEGE`。 |

普通 `EXPLAIN` 只生成估算计划，即使内部写着 DML 也只归 `PERFORMANCE`：

```text
[PERFORMANCE] EXPLAIN UPDATE t SET c = 1 WHERE id = 10;
```

只展示解析树而不执行内层语句的方言能力同样只使用 `PERFORMANCE`，不附加内层类型。

实际执行计划必须按内部真实操作分类，不再附加 `PERFORMANCE`：

```text
[SELECT] EXPLAIN ANALYZE SELECT * FROM t;
[UPDATE] EXPLAIN ANALYZE UPDATE t SET c = 1 WHERE id = 10;
```

是否真正执行内部语句必须根据当前数据源及版本的执行计划语义判断。不能仅凭
`ANALYZE` 等选项名称猜测：有的方言支持显式关闭实际执行，有的方言出现该选项即
执行内部语句。

### 4.30 Unsafe 与 Unknown

| Code | 授权 | 目标 | 审计 | 适用范围 |
|---|---|---|---|---|
| `UNSAFE` | `OTHER` | `Unknown` | `OTHER` | 对灾难级后果或静态不可防御执行能力的特别标记。适用于可能直接导致数据库服务器不稳定、崩溃、整体不可用、不可恢复的数据或恢复链损坏、重大事故，以及因 SQL 文本装载、动态解释或二阶注入而无法从当前语句静态判断真实动作的操作。实例停机或重启、无可静态限定目标的远端克隆、动态 SQL 和外部代码生命周期操作都应按真实机制评估。停止单个连接、查询或任务不等于整个实例失去可用性，不能仅因出现 `STOP/KILL` 等字面动作使用本项，但其具体参数或组合确实可能达到灾难级后果时仍应追加本类型。 |
| `UNKNOWN` | `OTHER` | `Unknown` | `OTHER` | 静态语义无法准确映射到任何现有枚举，或命令同时包含多种无法拆分的动态动作。它是语义未知兜底，不是管理命令兜底。 |

`UNSAFE` 是与精确动作类型正交的风险标记，不替代已经能够确定的语句类型。
例如远端实例导入仍保留 `DATA_IMPORT`，原始复制事件回放仍保留
`ADMIN_REPLICATION`，日志或复制状态重置仍保留 `MAINTAIN_LOG` 或
`ALTER_REPLICATION`。判断重点是该语句的真实运行机制和最坏后果，不能只根据
`DROP/RESET/STOP` 等关键字，也不能因为已经存在精确分类就省略 `UNSAFE`。

以下判断可以跨数据源复用：

- 外部插件、组件、共享库或不可静态审计代码的安装和卸载，保留其系统设置或对象
  生命周期分类，并追加 `UNSAFE`。卸载不能因为是“移除”动作而降低风险：它可能
  移除既有对象依赖的存储引擎、编解码、索引、安全组件或其他运行能力，使数据库
  不稳定、对象不可访问，甚至让完好数据呈现为损坏。不能根据目标名称猜测它是否
  重要。
- 普通数据库对象的 `DROP` 不自动等同于插件卸载，仍需判断它是否会卸载、注销或
  破坏数据库运行期依赖的可插拔能力；不能只凭 `DROP` 关键字追加 `UNSAFE`。
- 外部语言代码块、二进制逻辑或远端 payload 只要会进入数据库执行环境且分类器无法
  静态审计内部行为，就追加 `UNSAFE`，不能因运行时声称存在沙箱而降低分类。
- 禁用保障恢复链完整性的日志能力、回放不透明原始事件、清除复制连接或恢复位点等
  动作，在保留日志或复制专属分类的同时追加 `UNSAFE`。
- 反向启用保护能力、普通状态读取和可静态分析的 SQL 语言程序体不因与危险能力相邻
  而自动追加 `UNSAFE`。

Redis `SWAPDB` 原子交换两个逻辑 Database 的完整 Keyspace 映射并立即影响所有连接，
统一只使用 `UNSAFE`。`FUNCTION RESTORE payload REPLACE` 会从不可静态审计的序列化
载荷恢复并覆盖函数代码，也只使用 `UNSAFE`，不根据运行前函数库状态猜测创建或修改
生命周期。

`PREPARE` 即使来源是字符串字面量也归 `UNSAFE`；内部 SQL 只能作为潜在语义记录，不能替代主分类：

```text
[UNSAFE] PREPARE stmt FROM 'DROP TABLE users';
[UNSAFE] PREPARE stmt FROM @sql;
[UNSAFE] EXECUTE stmt;
[UNSAFE] DEALLOCATE PREPARE stmt;
```

名称明确的程序对象调用使用 `CALL_PROG_OBJ`；运行时字符串执行使用 `UNSAFE`。
不能仅凭执行关键字判断，必须根据当前数据源语法结构区分静态对象名与动态表达式。

## 5. 定义体与执行体分层规则

对象定义的根节点只分类定义本身和定义头部的直接动作，不能把执行体类型扁平追加到根节点。
能够被当前数据库语法解析为 SQL 语句的程序执行体必须进入 `SplitScript.children`：

- Trigger 的单语句执行体或 `BEGIN ... END` 执行体。
- Event 的 `DO` 单语句执行体或代码块；调度表达式不是子语句。
- Procedure、Function、Routine 的单语句实现体或代码块。
- `BEGIN ... END`、嵌套 `BEGIN ... END` 以及控制语句内部的直接语句。
- `IF/CASE/LOOP/WHILE/REPEAT` 等控制结构作为 `PROGRAM_CONTROL` 子节点，并递归保存各自直接子语句。
- `IF/ELSEIF/CASE/WHILE/REPEAT` 的条件表达式以及 `RETURN` 的返回表达式会在运行时真实求值。表达式中的查询、变量和函数动作必须按既有规则进入该 `PROGRAM_CONTROL` 节点的 children；例如 `IF EXISTS (SELECT ... @v ...) THEN` 收集 `SELECT` 和 `SESSION_VARIABLE_RW`。普通内置函数不额外产生函数分类，用户自定义函数使用 `CALL_PROG_OBJ`，管理型内置函数使用其所属能力类型。
- `SIGNAL/RESIGNAL/RETURN/LEAVE/ITERATE` 等没有语句体的程序控制操作作为叶子子节点。
- `DECLARE` 变量、Condition 和 Handler 属于程序结构，使用 `PROGRAM_CONTROL` 子节点。
- Cursor 是有名字的运行期查询控制对象，不是 Session 变量或系统配置。`DECLARE CURSOR`
  以及 `OPEN/FETCH/MOVE/CLOSE` 等游标生命周期操作统一使用
  `SELECT|PROGRAM_CONTROL`：`SELECT` 表达其查询读取能力，`PROGRAM_CONTROL` 表达游标
  句柄、位置和生命周期控制。不得使用 `SESSION_VARIABLE_RW` 或
  `SESSION_SETTING_WRITE`。
- `DECLARE ... HANDLER` 自带的单语句或 `BEGIN ... END` 执行体继续放入该
  `PROGRAM_CONTROL` 节点的 children。
- PostgreSQL `EXPLAIN ... DECLARE CURSOR` 只生成执行计划，并未创建真实 Cursor，仍只使用
  `PERFORMANCE`。MySQL `HANDLER ... OPEN/READ/CLOSE` 是直接表访问接口，不是存储程序
  Cursor，继续使用 `SELECT`。
- Trigger 执行体中凡是对 `NEW.<column>` 赋值，无论位于单语句执行体、代码块、
  Handler 或任意层级的控制结构中，均产生 `UPDATE` 子分类。识别必须依据
  `SET NEW.<column> = ...` 的语法节点，不得依赖大小写、空白或字符串匹配。
- 程序体内的 `PREPARE/EXECUTE/DEALLOCATE PREPARE` 仍使用 `UNSAFE`，但
  `PREPARE` 的字符串或变量内容保持不透明，不能作为更深层 SQL 递归解析。

当前阶段仍不递归分类以下内容：

- Library 和外部语言函数中的源码文本。
- `PREPARE`、动态 `EXECUTE` 等运行时字符串中的潜在 SQL。
- 注释、字符串、对象名以及 `GRANT/REVOKE` 权限列表中的操作名。

示例：

```text
[CREATE_TRIGGER(INSERT)]
CREATE TRIGGER trg_ai AFTER INSERT ON src
FOR EACH ROW INSERT INTO audit_log VALUES (NEW.id);
```

```text
[CREATE_PROG_OBJ|COMMENT_PROG_OBJ(BLOCK,UPDATE)]
CREATE PROCEDURE p()
  COMMENT 'procedure comment'
BEGIN
  UPDATE t SET c = 1;
END;
```

```text
[CREATE_VIEW(SELECT)]
CREATE MATERIALIZED VIEW mv AS SELECT * FROM t;
```

三个示例的执行体或查询定义体都进入 children，但不改变根节点的类型集合。
View 查询定义中的用户变量访问继续按查询定义体递归收集，例如
`CREATE VIEW v AS SELECT 1 INTO @v` 使用 `CREATE_VIEW(SELECT,SESSION_VARIABLE_RW)`。
安全模型在尚未接入递归分类树时继续使用根节点的第一个类型，不得因新增 children 改变既有授权行为。

列定义表达式中的子查询同样不追加 `SELECT`。如果该表达式明确调用函数，则函数调用仍作为直接安全动作记录为 `CALL_PROG_OBJ`，并与外层对象定义类型共存；例如 `CREATE TABLE ... DEFAULT ((SELECT DATABASE()))` 至少归为 `CREATE_TABLE|ADD_COLUMN|CALL_PROG_OBJ`。

## 6. 元数据与诊断读取

`SHOW`、`DESC`、数据字典查询及各方言的同类读取语句都不是独立分类，必须按实际
读取对象和用途判断：

- 事务日志、复制中继日志、归档日志、日志目录、日志运行坐标和日志事件读取使用 `LOG_READ`，不附加 `METADATA`。
- Status、Processlist、Profile、引擎运行状态、对象打开或锁定诊断等使用 `PERFORMANCE`，不附加 `METADATA`。
- 变量表达式中的当前会话变量读取或写入使用 `SESSION_VARIABLE_RW`。
- 系统变量或配置项的列举属于元信息读取，使用 `METADATA`；表达式中显式访问 Session 变量才使用 `SESSION_VARIABLE_RW`。
- 字符集、排序规则、存储引擎、插件等系统元信息读取使用 `METADATA`。
- 对象定义展示使用 `METADATA`，不能因返回 DDL 文本而使用对象创建分类。
- 授权、用户定义和权限能力的展示使用 `METADATA`，不能使用 `GRANT` 或 `CREATE_USER`。
- 数据空间、表、列、索引、Routine、Trigger、Event、Library 等对象元信息及结构描述使用 `METADATA`。
- `HELP` 读取数据库内置的 SQL 帮助元信息，使用 `METADATA`。
- Warnings、Errors 读取当前 Session 诊断区，使用 `PERFORMANCE`。
- 复制状态或拓扑元信息读取使用 `METADATA`。
- 解析树展示与查看估算执行计划一样属于只读 introspection，使用 `PERFORMANCE`。

## 7. 专属能力域优先级

以下场景必须优先使用专属分类，不能降级成普通设置或 DML：

1. 复制配置修改和运行控制使用 `ALTER_REPLICATION`，复制事件回放和同步等待使用 `ADMIN_REPLICATION`，复制状态与拓扑读取使用 `METADATA`。
2. Publication/Subscription 生命周期使用 `CREATE_PUB_SUB / ALTER_PUB_SUB / DROP_PUB_SUB`，运行控制使用 `ADMIN_PUB_SUB`。
3. 日志对象生命周期使用 `CREATE_LOG / ALTER_LOG / DROP_LOG`，读取、管理、维护分别使用 `LOG_READ / ADMIN_LOG / MAINTAIN_LOG`。
4. 资源组生命周期使用对应的创建、修改、删除分类，运行时线程分配使用 `ADMIN_RESOURCE_GROUP`。
5. 表和分区运维分别使用 `ADMIN_TABLE`、`ADMIN_PARTITION`。
6. 性能只读诊断使用 `PERFORMANCE`，改变缓存或性能状态使用 `ADMIN_PERFORMANCE`。
7. 数据装载和导出分别使用 `DATA_IMPORT`、`DATA_EXPORT`。
8. 只有不属于以上专属能力域的服务端配置变更才使用 `SYSTEM_SETTING_WRITE`。

## 8. 禁止的分类策略

以下做法一律禁止：

- 使用不在当前 `SplitQueryType` 中的 Code。
- 用笼统的 Object、Show、Read、Write 或 Execute 概念代替真实枚举。
- 根据字符串、注释、对象名称或源码中的关键词分类。
- 把 Trigger、Event、编程对象或 Library 的定义体关键字直接追加到根类型集合；View 查询体
  必须作为 children 收集，Library 外部源码保持不透明。
- 把 `GRANT/REVOKE` 权限列表当成真正执行的 DML。
- 把表或分区运维归为普通 `ALTER_TABLE / ALTER_PARTITION`。
- 把复制、日志、资源组、性能管理或导入导出错误归入 `SYSTEM_SETTING_WRITE`。
- 把普通 `EXPLAIN` 内部未执行的语句加入分类。
- 把实际执行的 `EXPLAIN ANALYZE` 弱化为 `PERFORMANCE`。
- 把数据源提供的插入或更新语法随意归入单一 `INSERT` 或 `UPDATE`；其静态分支无法确定时应使用 `MERGE`。
- 把 `INSERT OVERWRITE` 推测成 `INSERT|DELETE`；Doris、StarRocks 等实现通常写入
  临时 Table/Partition 后原子替换目标，不能据此断言执行了独立 `DELETE`。这种无法
  准确拆分底层 DML 动作的覆盖写统一使用 `MERGE`。
- 把不透明动态 SQL 归入 `CALL_PROG_OBJ` 或 `UNKNOWN`；应使用 `UNSAFE`。
- 为了减少 `UNKNOWN` 而套用语义近似但不准确的分类。

## 9. AI 校验流程

对一个可获得源码的数据源开始批量校验前，必须先按第 9.1 节构建分版本源码解析器并提取
源码测试语料；以下逐条分类步骤在该语料和版本碰撞结果之上执行。只有确认源码不可获得、
解析器无法构建或正式解析入口无法合理抽离时，才按第 9.1 节的后续优先级降级，并记录原因。

对每条记录执行以下步骤：

1. 定位完整 SQL，不能按执行体内部分号拆分。
2. 解析最外层语句和目标对象。
3. 屏蔽字符串、注释、对象名、权限列表和源码文本。
4. 划定定义体、查询体和执行体边界。
5. 按第 3 节优先级选择主分类。
6. 识别直接附加动作，如列、索引、约束、分区、重命名和备注。
7. 判断嵌套查询是否在本次语句中真实执行。
8. 判断执行计划是估算还是实际执行。
9. 判断动态 SQL 是否必须归 `UNSAFE`。
10. 对照第 4 节确认每个 Code 都存在且适用范围准确。
11. 按主分类优先、动作顺序、去重规则生成最终分类头。
12. 无法准确判断时使用 `UNKNOWN` 并保留待后续处理，不得猜测。

### 9.1 源码解析器优先的语料挖掘与兼容验证

当目标数据库源码可获得时，**按版本从源码构建可独立运行的解析器，是语料挖掘、语法
确认和 open-cdm 兼容验证的默认第一优先级方案**。不得因为已有第三方 grammar、少量文档
示例或人工整理 fixture，就跳过源码中的解析器和测试语料。

默认优先顺序如下：

1. 目标数据库源码中的正式 lexer/parser、解析入口及其原生语法测试。
2. 对应版本真实数据库，主要用于确认源码解析器未覆盖的预处理、会话开关、语义阶段和拒绝行为。
3. 厂商正式文档、发布说明及兼容性说明，用于补充源码中难以发现的用法和版本背景。
4. 既有 open-cdm fixture、第三方 grammar 和其他数据库改造来的解析器，只能作为待碰撞语料
   或缺口线索，不能优先于目标数据库自己的源码解析器。

#### 9.1.1 按版本构建独立源码解析器

应对 open-cdm 实际支持的每个主版本语法族分别执行以下工作：

1. 定位该版本真正使用的 lexer/parser、入口类、语法生成步骤、大小写处理、预处理和必要特性开关。
2. 从对应 Git tag、branch 或 commit 中抽离最小可编译集合，构建不依赖数据库部署的本地命令行程序。
3. 命令行程序至少接收完整 SQL，并输出“语法接受/拒绝”、错误位置和关键错误信息；不得只靠进程
   退出码表达结果。
4. 将构建脚本、版本映射、源码引用、必要补丁和可执行产物统一放入该数据源的 `tools` 目录，
   使用 `source-parser` / “源码解析器”命名。
5. 同一主版本的多个 tag 只有在 grammar 或正式解析调用链确有差异时才继续比较；不能因为 tag
   数量多就制造 open-cdm 并不存在的补丁版本语法族。
6. open-cdm 生产 parser 必须属于目标方言，不依赖兼容数据库的 parser util、token 类型、版本
   枚举或事实注册表；从参考 grammar 起步时也要把所有运行时边界独立出来。
7. 生产实现只保留一个当前 parser 包，不创建 `v2` 等平行最新版。主版本通过 parser config
   激活 grammar 谓词；源码解析器二进制可以按主版本分别构建，它们是验证工具，不等于生产包分叉。
8. 用版本接受矩阵验证是否向上兼容。即使所有旧语法都被最新版本接受，新语法的最早支持主版本
   仍要由谓词限制；只有矩阵证明无差异的规则才能放入 common。
9. 类名、grammar 名和版本配置保持数据库官方缩写，例如 `TiDBParser`、`TiDBVersion`；不要擅自
   改写成 `TiDbParser`、`TiDbVersion`。

源码解析器应尽量恢复厂商真实解析调用链，而不是只编译一个裸 grammar。若完整入口无法抽离，
必须记录被省略的预处理、会话变量、特性开关和解析后校验；这些缺失决定后续哪些结果需要真实
数据库复核，但不降低源码解析器作为默认语法挖掘工具的优先级。

#### 9.1.2 从源码测试中提取大规模真实语料

源码中的 SQL 测试是默认首选语料来源。提取时必须：

1. 扫描 parser、planner、regression、integration、DDL/DML、兼容性及升级测试，不能只搜索
   `.sql` 文件。
2. 测试语句由 Java、Go、Python、Groovy、模板、数据驱动器或生成器动态拼装时，应改造或运行
   原程序，在最终送入解析器的位置捕获完整 SQL；不得只摘取不完整字符串片段。
3. 保留语句来源、源码版本、文件位置和生成方式，使失败可以回溯到原测试。
4. 使用同版本源码解析器重新验证提取结果，剔除日志片段、期望错误文本、残缺示例及并非 SQL
   输入的字符串。
5. 将有效 SQL 转换为本文规定的 fixture 格式；先按语句边界记录，再分类、去重和组织版本目录。
6. 语料规模应与目标数据库源码测试规模相称。源码中存在上万条可恢复 SQL 时，不得只挑选几十条
   示例宣称覆盖完成。

转换和归档还必须满足：

1. AST dump、JSON/JSONL、执行 trace、SQL hash 清单只是中间证据，不是正式 fixture；最终每条
   SQL 都使用本项目统一的 split 文本格式。
2. 尽量保留源码原测试文件名、测试组和场景名称。SQL hash 只用于身份、碰撞和去重，不能把正式
   文件全部改成 `by-sql-hash` 一类无语义名称。
3. `common` 通过把同一 SQL hash 送入全部受支持主版本解析器得出；只有全部版本接受且分类/结构
   语义一致的 occurrence 才能抽入 common。
4. 版本专属 SQL 直接放入 `split/<dialect>/<major>`。`docs`、`upstream` 等来源目录不继续成为
   fixture 层级；来源信息写入 case ID、注释或独立 provenance 索引。
5. 完全相同 SQL 的重复 occurrence 可以去重，但必须保留全部来源映射。仅空白、无意义空行或
   等价拼装不同的同一结构场景，人工样例保留 1~2 条即可，同时全量自动覆盖统计不能丢失。
6. 删除旧 JSON 或中间采集格式时，必须先把其中每条有效 SQL 转为统一 fixture；不能因为格式
   不合规而把数万条已验证语料一起删除。

#### 9.1.3 碰撞实验与 open-cdm 修复闭环

语料提取后必须进行双向碰撞：

1. 将各版本源码语料送入对应版本源码解析器，形成版本接受矩阵。
2. 将同一批 SQL 送入 open-cdm 对应版本配置，记录语句切分、分类、语法解析和 visitor 恢复结果。
3. 将既有 open-cdm fixture 反向送入各版本源码解析器，发现既有语料中的真实兼容性问题。
4. 对“源码解析器接受、open-cdm 拒绝或恢复不完整”的 SQL，优先完善 grammar、版本谓词和 visitor；
   不得通过删除有效语料、改成 `UNKNOWN` 或放宽成吞 token 的兜底规则来消除失败。
5. 对“open-cdm 接受、源码解析器拒绝”的 SQL，检查版本目录、来源、预处理和源码解析器抽离范围；
   只有确认目标版本确实拒绝后，才按第 9.2 节进入 `reject`。
6. 每次扩展 grammar 后重新运行全部有效语料和既有 fixture，报告语句总量、接受数、拒绝数及剩余
   差异，不能只报告新增的少量样例。

#### 9.1.4 适用范围与版本组织

源码解析器直接用于确认语句边界、grammar 接受情况、主版本语法差异和 open-cdm 解析兼容性。
权限、对象存在、catalog 解析、执行成功和运行期语义不属于语法解析器的职责，应在对应测试阶段
分别验证，不要把不同测试目标混为一谈。

当抽离程序覆盖了该版本正式解析入口及必要预处理时，其版本接受矩阵可以作为 `common` 与主版本
专属 fixture 归属的主要依据。出现以下情况时必须再用真实数据库或厂商等价完整调用链复核：

1. 判断 SQL 应进入 `reject`。
2. 解析结果依赖会话配置、特性开关、预处理器或 catalog/语义分析。
3. 抽离程序省略了影响该语句的正式解析步骤。
4. 不同源码 tag 的结果与正式发布说明或真实数据库行为冲突。

fixture 最终仍按 open-cdm 对外提供的主版本语法族组织。源码 tag、branch 或 commit 用于保证构建
可复现，不能直接替代生产解析器的版本模型。

### 9.2 `reject` fixture 判定

`reject` 目录记录目标数据库版本明确拒绝的 SQL，用于验证解析器不能把该 SQL
当作目标版本支持的正常语法。放入 `reject` 前必须在对应真实数据库版本上执行，
记录错误码和关键错误信息，不能只根据其他版本文档、解析器结果或命令退出码判断。

数据库拒绝需要区分两类：

1. 目标版本不存在该语法、关键字或语句变体，数据库返回语法错误或明确的版本不支持。
   这类 SQL 的目标版本行为可能与支持版本不同，分类不得为了跨版本整齐而机械复制。
2. 数据库已经识别完整语法，只因对象、文件、权限、配置、存储引擎或运行状态无法完成
   操作。这类结果证明 SQL 在语法层面成立；即使 fixture 因测试目的保留在 `reject`，
   其分类也必须与普通 SQL 完全一致，不得因执行失败删减主动作或附加动作。

具体版本的已验证结果放在文末对应数据源说明中，不能把一个数据源的错误码或版本边界
写成通用规则。

### 9.3 独立测试框架接入

split、lineage、behavior 的正式资源位于独立项目：

`/home/zyc/project/dm/open-cdm-test/src/test/resources`

新增数据源不再建立大量 JUnit 分片类。应在
`src/test/resources/config/test-plan.json` 中声明 SqlEngine、数据源类型、主版本、common/版本资源
和 reject 条件，由统一资源加载器、有限队列、执行器和结果收集器运行：

```text
./gradlew runTests -PtestArgs='--domain=split --datasource=<dialect> --version=<major>'
./gradlew test -Dtest.domain=split -Dtest.datasource=<dialect> -Dtest.version=<major>
```

main 与 JUnit 必须调用同一测试机制；JUnit 只有一个统一入口。生产者、消费者、队列容量和 ANTLR
cache slot 可以按机器配置调整，但不得改变 testcase 语义。统计要求：

- `cases`、`passed`、`failed` 和 rate 都以实际 fixture occurrence 数计数；
- `source` 是资源/variant 进度，`queue` 是批任务容量，不能与 cases 混为一谈；
- source 仍在懒加载时，总数显示“已发现总数+”，不能为取得总数先阻塞扫描全部语料；
- 失败立即输出脚本、case 名、SQL 和完整堆栈，并同步写独立失败日志；
- 结束后按数据源以及 behavior/lineage/split 三个维度汇总 total/passed/failed。

## 10. 特定数据源说明

本节只记录通用规则无法单独推出的方言事实。新增数据源时应增加同级小节，不得把
某个数据源的关键字、版本号、资源文件名或例外条件写回正文。

### 10.1 MySQL

#### 10.1.1 对象模型与专属语法

- MySQL 通常把 Database 映射为 Schema，因此 `CREATE DATABASE/SCHEMA` 使用
  `CREATE_SCHEMA`，不能仅凭 `DATABASE` 关键字使用 Catalog 分类。
- MySQL 没有可靠的原生 Schema 重命名、列级截断、分区备注、触发器备注、独立
  Sequence 或 Synonym 语法，不得为了覆盖公共枚举编造 fixture。
- MySQL Event 使用 Event 分类，不能重复归 Job；Event 的 `DO` 内容属于执行体。
- `DROP PRIMARY KEY`、`DROP FOREIGN KEY` 归 `DROP_CONSTRAINT`。
- `SET RESOURCE GROUP` 使用 `ADMIN_RESOURCE_GROUP`。
- `HANDLER ... OPEN/READ/CLOSE` 是表读取能力，使用 `SELECT`。
- `INSERT ... ON DUPLICATE KEY UPDATE` 和 `REPLACE` 的静态分支不能确定，使用
  `MERGE`；字符串 `REPLACE()` 函数不适用。
- `LOGFILE GROUP` 属于日志存储基础设施，生命周期分别使用
  `CREATE_LOG / ALTER_LOG / DROP_LOG`。创建语句中的 `COMMENT` 是日志对象属性，
  不得虚构日志备注 Code。

#### 10.1.2 复制、日志、锁与数据流

- `CHANGE REPLICATION SOURCE/MASTER`、复制过滤器修改、
  `START/STOP REPLICA/SLAVE`、组复制启停和 `RESET REPLICA/SLAVE` 使用
  `ALTER_REPLICATION`。
- `BINLOG 'base64-event'` 是复制事件回放；`MASTER_POS_WAIT()`、
  `SOURCE_POS_WAIT()`、`WAIT_FOR_EXECUTED_GTID_SET()` 和
  `WAIT_UNTIL_SQL_THREAD_AFTER_GTIDS()` 是复制进度等待，使用
  `ADMIN_REPLICATION` 并保留外层动作。函数名必须按当前版本确认。
- `SHOW REPLICA/SLAVE STATUS`、`SHOW REPLICAS`、`SHOW SLAVE HOSTS` 只读取复制
  状态或拓扑，使用 `METADATA`。
- `SHOW BINARY/MASTER LOGS`、`SHOW MASTER/BINARY LOG STATUS` 和
  `SHOW BINLOG EVENTS` 使用 `LOG_READ`；`PURGE BINARY LOGS`、`RESET BINARY LOGS`
  使用 `MAINTAIN_LOG`。
- `audit_api_message_emit_udf(...)` 直接写入审计事件，在保留外层 `SELECT` 的同时
  使用 `ADMIN_LOG`，不新增 `LOG_WRITE`。
- `SELECT ... FOR UPDATE`、`FOR SHARE` 和 `LOCK IN SHARE MODE` 追加
  `QUERY_LOCK`。
- `LOCK/UNLOCK TABLES`、`LOCK/UNLOCK INSTANCE`、
  `FLUSH TABLES ... WITH READ LOCK`、`GET_LOCK/RELEASE_LOCK/RELEASE_ALL_LOCKS`
  以及已登记的锁服务和 Version Token 锁函数使用 `SESSION_LOCK`。
  `IS_FREE_LOCK/IS_USED_LOCK` 只观察锁状态，不增加本项。
- `FLUSH TABLE ... FOR EXPORT` 使用 `DATA_EXPORT|SESSION_LOCK`；它持有锁直到当前
  Session 执行 `UNLOCK TABLES`。DDL 的 `LOCK=NONE/SHARED/EXCLUSIVE` 只控制当前
  语句，不增加锁分类。
- `CLONE INSTANCE FROM` 表示数据从远端 donor 流入当前 recipient，使用
  `DATA_IMPORT`；无 `DATA DIRECTORY` 的远端 Clone 还追加 `UNSAFE`。
  `CLONE LOCAL DATA DIRECTORY` 把当前实例数据复制到外部目录，使用
  `DATA_EXPORT`。
- `ALTER TABLE ... IMPORT TABLESPACE` 和 `IMPORT PARTITION ... TABLESPACE` 在保留
  `ALTER_TABLE` 及表或分区管理类型的同时追加 `DATA_IMPORT`；对应
  `DISCARD TABLESPACE` 是丢弃而不是导出。
- `SELECT ... INTO OUTFILE/DUMPFILE` 使用 `DATA_EXPORT|SELECT`。
- `LOAD_FILE(...)` 从服务器文件系统读取内容进入 SQL 执行域，追加
  `DATA_IMPORT|UNSAFE`，并保留外层动作。例如：
  `SELECT LOAD_FILE(...)` 使用 `SELECT|DATA_IMPORT|UNSAFE`。
- `CREATE TABLE ... DATA DIRECTORY/INDEX DIRECTORY` 中的路径只是定义属性，只使用
  `CREATE_TABLE`；表空间数据文件路径同理。

#### 10.1.3 Session、系统设置与秘密材料

- `CALL p(@v)` 不依赖过程参数元数据猜测 `IN/OUT/INOUT`，统一使用
  `CALL_PROG_OBJ|SESSION_VARIABLE_RW`。
- `SET TIMESTAMP=value`、`SET LAST_INSERT_ID=value`、`SET INSERT_ID=value` 和
  `LAST_INSERT_ID(expr)` 使用 `SESSION_SETTING_WRITE`；函数形式保留外层动作。
- `LAST_INSERT_ID()`、`FOUND_ROWS()`、`ROW_COUNT()` 读取瞬时连接状态时只按外层
  语句归类，不增加 `SESSION_VARIABLE_RW` 或 `CALL_PROG_OBJ`。
- `SQL_CALC_FOUND_ROWS` 是查询修饰符；它与读取结果的 `FOUND_ROWS()` 都只使用
  `SELECT`，不追加 Session 或 System Setting 类型。
- `option_tracker_usage_get(...)` 读取持久化的功能使用遥测，使用
  `SELECT|METADATA`；`option_tracker_usage_set(...)` 写入同一持久化信息，使用
  `SELECT|SYSTEM_SETTING_WRITE`，不得归入只影响当前连接的
  `SESSION_SETTING_WRITE`。
- `SET_VAR(...)` Hint 只在当前语句执行期间生效，结束后恢复，因此不增加
  `SESSION_SETTING_WRITE`。
- `audit_log_encryption_password_set(...)` 修改审计日志加密配置，使用
  `SELECT|SYSTEM_SETTING_WRITE`。配置生效时可能发生的日志轮转和旧密码清理是条件性
  副作用，不追加 `ADMIN_LOG` 或 `MAINTAIN_LOG`。
- `audit_log_encryption_password_get()`、`keyring_key_fetch(...)` 和
  `CREATE_ASYMMETRIC_PRIV_KEY(...)` 会读取或直接返回秘密材料，使用
  `SELECT|ADMIN`。`keyring_key_type_fetch(...)`、
  `keyring_key_length_fetch(...)` 只返回非秘密属性，不追加 `ADMIN`；普通加解密、
  签名、随机数和公钥函数也不自动归入本项。

#### 10.1.4 内置函数、系统 Routine 与元信息资源

- 内置函数和 MySQL 随附的可加载函数必须按当前 `MySqlVersion` 语法族识别。
  `mysql-built-in-functions.json` 与 `mysql-system-loadable-functions.json` 中每个
  名称都必须显式声明版本集合，不能使用所有版本名称的并集。名称在当前语法族不存在
  时按可能的用户函数处理并追加 `CALL_PROG_OBJ`。
- 系统存储过程默认保留 `CALL_PROG_OBJ`。只有 Routine 已按 `MySqlVersion` 显式
  登记到现有能力域时才追加对应类型；不能根据过程名称、系统 schema、文档描述或
  间接副作用临时推断。`CALL query_rewrite.flush_rewrite_rules()` 当前未登记专属
  领域，只使用 `CALL_PROG_OBJ`。
- `mysql_firewall_flush_status()` 清零 Firewall 的全局运行统计，是安全组件管理
  动作而不是性能诊断或策略修改；保留外层 `SELECT` 并追加 `ADMIN`，不使用
  `ADMIN_PERFORMANCE` 或 `ALTER_POLICY`。
- `META-INF/clougence/mysql-skip-permission-resources.json` 维护跳过对象权限检查的
  系统元信息表、系统 View、系统存储过程和系统存储函数。每项使用结构化的
  `versions`、`type`、`name` 字段；`versions` 是 `MySqlVersion` 语法族数组，
  `name` 是严格的 `[schema, object]` 两段数组。禁止省略版本、使用任意版本范围
  或通配资源名。
- 当前清单由 MySQL 5.6.51、5.7.44、8.0.46、8.4.10、9.7.1 干净实例分别探测后
  合并。匹配必须使用当前解析器的 `MySqlVersion`，升级目标版本时必须重新探测差异。
- `SHOW VARIABLES` 是 `METADATA`，不是 `SESSION_VARIABLE_RW`；
  `SHOW OPEN TABLES`、`SHOW WARNINGS/ERRORS`、`SHOW PARSE_TREE` 分别按运行诊断、
  Session 诊断区和解析树使用 `PERFORMANCE`。
- `SHOW PARSE_TREE <statement>` 不执行内层语句，无论内层是查询、DML 还是 DDL 都
  只使用 `PERFORMANCE`。

#### 10.1.5 性能与执行语义

- `PROCEDURE ANALYSE()` 会实际执行输入查询并生成诊断结果，使用
  `SELECT|PERFORMANCE`；位于 DML 来源查询或导出查询中分别使用
  `INSERT|SELECT|PERFORMANCE`、`DATA_EXPORT|SELECT|PERFORMANCE`。
- `BENCHMARK()` 是表达式性能测量函数，保留承载它的 `SELECT/BLOCK` 并追加
  `PERFORMANCE`。
- `SLEEP()` 按普通内置函数处理，不追加 `PERFORMANCE`、`UNSAFE` 或 Session 类型；
  阻塞时长和资源风险属于函数级执行策略。
- MySQL 的 `EXPLAIN ANALYZE` 会执行内部语句，按内部真实操作分类，不附加
  `PERFORMANCE`。
- 只读的 `XA RECOVER [CONVERT XID]` 使用 `TRANSACTION`，不叠加 `METADATA` 或
  `PERFORMANCE`。

#### 10.1.6 `UNSAFE` 已确认边界

- MySQL 文档中的 Statement-Based Replication `unsafe` 只表示语句重放可能产生不同
  结果，不等同于本分类中的灾难级 `UNSAFE`。`UUID()`、`USER()`、`VERSION()`、
  `RAND()` 等函数即使出现在写语句中，也不追加 `UNSAFE` 或另设复制风险分类；
  只保留外层语句及函数本身已有的分类。`LOAD_FILE()` 因读取服务器文件而独立使用
  `DATA_IMPORT|UNSAFE`，不是因为 MySQL 将其列为 SBR unsafe。
- `SHUTDOWN`、`RESTART` 使用 `UNSAFE`。
- `INSTALL/UNINSTALL PLUGIN`、`INSTALL/UNINSTALL COMPONENT` 使用对应的
  `CREATE_LIBRARY` 或 `DROP_LIBRARY`，不追加 `UNSAFE`。`CREATE FUNCTION ... SONAME`
  使用 `CREATE_PROG_OBJ|UNSAFE`。
- `CREATE LIBRARY ... LANGUAGE JAVASCRIPT/WASM` 使用 `CREATE_LIBRARY`。使用非 SQL
  `LANGUAGE` 或 `USING (...)` 外部 Library 的
  Procedure/Function，在保留 `CREATE_PROG_OBJ` 或 `ALTER_PROG_OBJ` 的同时追加
  `UNSAFE`；普通 `LANGUAGE SQL` 程序体按可递归分析的 SQL 分类。
- `ALTER INSTANCE DISABLE INNODB REDO_LOG` 使用 `ADMIN_LOG|UNSAFE`；重新启用
  Redo Log 使用 `ADMIN_LOG`。
- `BINLOG 'base64-event'` 使用 `ADMIN_REPLICATION|UNSAFE`。
- `RESET REPLICA/SLAVE ALL`、`RESET BINARY LOGS AND GTIDS` 及同时执行两类动作的
  复合 `RESET`，分别使用 `ALTER_REPLICATION|UNSAFE`、
  `MAINTAIN_LOG|UNSAFE` 或 `ALTER_REPLICATION|MAINTAIN_LOG|UNSAFE`。

#### 10.1.7 已验证版本边界

- MySQL 5.6/5.7 接受 `SELECT ... PROCEDURE ANALYSE()`；8.0/8.4/9.7 对同一结构
  返回 1064，属于被移除的版本语法。
- MySQL 5.6/5.7 对 `SELECT ... FOR SHARE` 返回 1064；8.0/8.4/9.7 接受。旧版本
  reject 不能按新版本语义追加 `QUERY_LOCK`。
- MySQL 8.0 对 `IMPORT TABLE` 返回 `secure-file-priv` 环境限制；8.4/9.7 对不存在
  文件返回 3608。这些错误均说明服务器已接受语法，因此分类仍为 `DATA_IMPORT`。
- `asynchronous_connection_failover_reset()` 在 5.6/5.7 使用
  `SELECT|CALL_PROG_OBJ`，在 8.0 及以后使用 `SELECT|ALTER_REPLICATION`。同一
  `MySqlVersion` 语法族内不再细分补丁版本。

### 10.2 TiDB

#### 10.2.1 方言边界与生产 parser

- 产品、类名、grammar 和版本类型统一写作 `TiDB`，例如 `TiDBParser`、`TiDBVersion`、
  `TiDBDslProvider`；包名和资源目录使用小写 `tidb`。
- TiDB 声明兼容 MySQL 不表示其 parser、系统函数和系统 schema 等同 MySQL。TiDB 生产 parser
  不依赖 MySQL parser 工具、版本枚举或资源清单。
- 生产模块只维护一个最新版 grammar/parser 实现，不保留 `v2` 包。TiDB 5、6、7、8、9 通过
  `TiDBParserConfig` 和 grammar 谓词激活对应主版本能力。
- 是否只维护最新 grammar 超集由五个主版本源码解析器的接受矩阵决定；新语法仍必须标注最早支持
  主版本，不能因为当前抽样表现向上兼容就删除谓词。
- TiDB Database/Schema、系统对象层级和默认上下文必须以 TiDB 数据源自身的 CloudDM levels
  定义与 TiDB 源码为准，不能因兼容 MySQL 直接复制 MySQL 层级结论。

#### 10.2.2 源码解析器与语料实证快照

2026-08-04 的验证工具分别从以下 TiDB 源码构建独立 parser/AST 采集器：

| 主版本 | 源码 ref | commit | 接受 | 拒绝 |
|---|---|---|---:|---:|
| 5 | `v5.4.3` | `f9b13b76505429b48cc479020e1b4b260a689175` | 10,423 | 33 |
| 6 | `v6.5.12` | `2ec3e3e04536ee5dafa11663e32632378a1a1be8` | 10,640 | 60 |
| 7 | `v7.5.7` | `972e642ba15f36f6a96c0b9858a6572aba6a8a19` | 11,072 | 50 |
| 8 | `v8.5.7` | `202b7f47286a1109b5c957401d34c9358d130ae0` | 11,433 | 46 |
| 9 | `v9.0.0-beta.2.pre` | `f52d19273b447c08061632fde70e288f24b324ba` | 11,572 | 54 |

- 共碰撞 55,383 个“版本 × occurrence”组合，55,140 个接受、243 个拒绝。
- common 语料通过五个采集器共同接受得出，版本差异进入 `tidb/<major>`；最终源接受 SQL
  转为统一 fixture，没有保留 collector JSONL 作为测试资源。
- parser/AST 结果权威证明 TiDB 语法接受、statement/node 类型和对象结构；它没有被描述成真实
  executor 行为。任意 corpus 名称缺少 InfoSchema、统计、文件和外部服务时，不用 executor 环境
  错误否定 parser 接受结论。
- 上述数字是本次可复现实证，不是后续版本永久常量。更换 tag/commit 后必须重新生成矩阵。

#### 10.2.3 TiDB 内置函数事实表

- TiDB 标量函数核心来源是 `pkg/expression/builtin.go` 的真实 `funcs` 注册表；
  `pkg/parser/misc.go` 的 parser 特殊函数、聚合/窗口 token，以及 parser/planner 特殊函数用于补齐
  不进入普通标量 map 的 SQL surface。
- 仅存在于 `pkg/parser/ast/functions.go` 的常量不能单独证明函数可执行；extension 动态函数也不
  无条件进入静态内置表。
- 最新源码 revision `4c0e2c062449` 的已核验快照包含 347 个 TiDB 系统函数。从 MySQL 8 清单
  复制的 Audit、Group Replication、Keyring、Firewall 等 286 个 TiDB 未注册名称已删除，另补充
  77 个 TiDB 注册函数。
- 管理函数和 statement-type 映射先检查当前 TiDB 系统函数成员资格。已删除名称不得通过遗留
  action map 绕过 UDF 判定。
- `schema.fn()`、显式引用符函数和未命中当前版本清单的名称按用户函数候选处理，使用外层动作并
  追加 `CALL_PROG_OBJ`；不能仅按末级同名套用 TiDB 系统函数例外。

#### 10.2.4 TiDB 元数据对象事实表

- TiDB 元数据对象从 `pkg/infoschema` 的 information/performance/metrics 注册入口、cluster 动态
  表映射、`pkg/session` bootstrap 与 DDL 内部表、`sys` View 和 workload repository 表生成。
- 最新源码 revision `4c0e2c062449` 的已核验快照包含 844 个全限定元数据对象，其中包含 642 个
  `METRICS_SCHEMA` 表。由 MySQL 8 复制的 266 个 InnoDB、复制 Performance Schema 等 TiDB
  未注册对象已删除，补齐 781 个 TiDB 对象。
- 只有清单中的完整 `schema.object` 才触发 `METADATA`。仅写 `INFORMATION_SCHEMA`、使用未限定
  同名表或命中对象名后缀都不成立。
- 当前 844 项是最新源码快照，不代表全部对象在 5/6/7/8 都存在。完成分版本资源差异前，旧版本
  不能无条件共享最新版清单。
- 资源文件由校验工具从源码反向生成；升级 TiDB 源码时必须先运行一致性检查并审阅新增/删除差异。

#### 10.2.5 行为 fixture 与分类关系

- TiDB 源接受语料投影出 14,576 个 behavior occurrence，叠加 8 个既有精选用例后为 14,584；
  common 与各主版本增量总计回放 55,140 case，全部通过。
- behavior 中的 Function/CALL、系统对象权限豁免与 split 的 `CALL_PROG_OBJ/METADATA` 使用同一
  版本化方言事实，但各自 visitor 和输出模型保持独立，不能互相转换结果。
- collector AST 用于确认对象身份和嵌套，CloudDM ANTLR token 用于代码范围；不能把 TiDB AST
  中少量可用 offset 扩大解释为全部精确位置。
- parser 接受、split 分类、behavior 关系和真实执行副作用分别报告。不能用 55,140 个 parser/行为
  fixture 通过宣称任意 SQL 已在真实 TiDB executor 上成功执行。
