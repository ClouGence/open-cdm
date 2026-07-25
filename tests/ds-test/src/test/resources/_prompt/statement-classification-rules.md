# SQL 语句分类规则

版本：2026-07-23

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

下表覆盖当前全部 150 个枚举项。表中的“授权”“目标”“审计”分别对应代码里的 `SecDataAuthKind`、`TargetType`、`SecQueryKind`。

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

`SYSTEM_SETTING_WRITE` 和 `ADMIN` 可以共享 `SecDataAuthKind.ADMIN`，但判定依据不同：前者回答“修改了哪项系统配置或全局功能状态”，后者回答“对当前运行中的系统执行了什么即时管理动作”。不能因为插件、组件或全局功能不是字面参数键，就把其配置动作降级为 `ADMIN`。

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

### 9.1 `reject` fixture 判定

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
