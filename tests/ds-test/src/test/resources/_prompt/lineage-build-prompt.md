# 数据库查询列血缘分析接入与纠错提示词

版本：2026-07-29

你现在要为 open-cdm 的目标数据库方言接入、完善或纠正查询列血缘分析能力。本提示词与
`behavior-analysis-prompt.md`、`statement-classification-rules.md` 的用途相同：它不是只服务于
某一次 MySQL 重建，而是用于后续逐个数据源补全实现、建立 fixture、发现缺口并验证结果。

本提示词适用于未来所有关系型数据库及具有 SQL 查询能力的数据源。MySQL 是已经按新架构
落地的参考实现，但不能把 MySQL 的 grammar context、版本号、标识符规则、Catalog/Schema
层级或方言语义当作其他数据源的通用前提。

本任务只回答一个问题：

一条查询返回哪些有序结果列；每个结果列由哪些物理表字段产生；每个物理字段在完整 SQL
文本中的真实引用位置是什么？

唯一公共输出契约是：

```text
LineageAnalysisSpi
  -> List<LineageColumn>
       -> column
       -> List<SourceName>
            -> catalog
            -> schema
            -> table
            -> column
            -> startLine
            -> startColumn
            -> endLine
            -> endColumn
```

血缘分析采用三层结构：SDK 定义输入输出边界，`sqlc-common` 持有与方言无关的查询模型、
作用域和解析算法，各数据源模块使用自己的 parser CST 构建公共模型。任何数据源都不得先
生成安全规则 Domain，再从安全规则结果反推血缘。

参考设计文档：

`/home/zyc/project/dm/document/Query 语句真实字段解析.pdf`

该文档中的图是核心算法的语义来源。应复用其中“查询项、子节点、虚拟表、向上替换和
WITH 作用域”的思想，但不得复用旧有 `RdbSelectDomain`、`QueryItem`、`RuleDomain`
等安全规则承载类型。

## 一、任务边界

### 必须完成

1. 动态扫描目标数据源全部受支持版本和 parser properties 对应的 SQL 语料。
2. 逐条人工判断查询的有序结果列，以及每个结果列的全部物理字段来源。
3. 为每个来源保留准确的 Catalog、Schema、Table、Column 和源码位置。
4. 识别目标方言已有 `LineageAnalysisSpi`、parser、CST visitor、元数据层和 lineage fixture。
5. 将仍依赖安全规则 Domain 的血缘实现迁移到公共血缘模型和公共解析器。
6. 为目标方言建立独立的 CST 到公共血缘模型适配层。
7. 补全或纠正直接字段、表达式、星号、派生表、CTE、集合运算、标量子查询和方言特有查询结构。
8. 完善 `lineage/<dialect>` fixture，严格比较输出顺序、来源顺序、资源路径和代码位置。
9. 覆盖目标数据源全部受支持版本；存在 exact-version、兼容模式或方言开关时分别验证。
10. 对无法产生 AST、缺少必要元数据或公共模型暂不支持的 case，记录明确状态和可复现原因。
11. 发现公共模型或核心算法缺口时，在 `sqlc-common` 修复一次，并联动回归已接入数据源。
12. 保持输入 SQL 原文不变；实现必须适配真实 SQL，不能修改语料来迁就 visitor。

### 明确不做

1. 不修改或扩展安全规则 Domain 来承载血缘。
2. 不把安全分析、行为分析、资源分析或 split 结果转换成血缘结果。
3. 不让生产血缘实现调用 split SPI、读取 `SplitScript` 或依赖 fixture 中的 `[TYPE]`。
4. 不审计、纠正或扩展 `SplitQueryType` 和 split fixture 分类。
5. 不为了血缘修改 `SecDataAuthKind`、`TargetType`、权限模型或行为关系。
6. 不通过字符串搜索、正则或第二套 tokenizer 恢复字段位置。
7. 不在方言 visitor 中复制 `LineageResolver` 已负责的作用域、元数据展开和向上替换算法。
8. 不把 WHERE、JOIN ON、普通 GROUP BY、HAVING 或查询级 ORDER BY 中出现的字段全部收集为结果列血缘。
9. 不以“当前实现就是这样输出”为依据批量生成期望。
10. 不同时重构与目标数据源无关的 parser、resource、behavior、security 或 split 链路。

`split/<dialect>` 或目标数据库现有的等价 SQL fixture 在本任务中只有三个用途：

- 提供经过整理的真实 SQL 语料；
- 提供每条 SQL 的可靠边界；
- 提供 version、exact-version、兼容模式和 parser properties 等上下文。

split fixture 中已有的 `[TYPE]` 对列血缘没有裁决作用。读取 SQL 原文后必须独立分析；
即使分类错误，也不在本任务中修改。

## 二、通用路径与参考实现

工作区：

`/home/zyc/project/dm`

仓库：

`/home/zyc/project/dm/open-cdm`

仓库规则：

`/home/zyc/project/dm/open-cdm/AGENTS.md`

SQL 语料：

`/home/zyc/project/dm/open-cdm/tests/ds-test/src/test/resources/split/<dialect>`

split fixture 格式：

`/home/zyc/project/dm/open-cdm/tests/ds-test/src/test/resources/split/README.md`

血缘公共契约：

`/home/zyc/project/dm/open-cdm/backend/clouddm-platform/cgdm-plugin-sdk/src/main/java/com/clougence/clouddm/sdk/sql/analysis/lineage`

公共血缘模型和算法：

`/home/zyc/project/dm/open-cdm/backend/clouddm-plugins/clouddm-sql/sqlc-common/src/main/java/com/clougence/sql/common/analysis/lineage`

MySQL 新架构参考实现：

```text
backend/clouddm-plugins/clouddm-sql/sql-mysql/src/main/java/
└── com/clougence/sql/mysql/analysis/lineage
    ├── MyLineageAnalysisSpi.java
    └── antlr
        ├── MyLineageCstVisitor.java
        └── MyLineageTokenRangeFactory.java
```

通用血缘测试框架：

`/home/zyc/project/dm/open-cdm/tests/ds-test/src/test/java/com/clougence/clouddm/ds/lineage`

血缘 fixture：

`/home/zyc/project/dm/open-cdm/tests/ds-test/src/test/resources/lineage/<dialect>`

fixture 说明：

`/home/zyc/project/dm/open-cdm/tests/ds-test/src/test/resources/lineage/README.md`

开始前必须：

1. 完整阅读 `AGENTS.md`。
2. 检查 git status 和相关 diff，保留已有未提交修改。
3. 定位目标数据源实际使用的 SQL engine、`LineageAnalysisSpi`、parser provider 和版本模型。
4. 检查目标实现是否仍继承 `AbstractLineageAnalysisSpi`，或引用 security builder/visitor。
5. 检查 `lineage/<dialect>`、`split/<dialect>` 和对应测试入口的实际覆盖范围。
6. 检查目标数据源是独立 parser，还是复用 MySQL、PostgreSQL、Oracle 等 parser 家族。

不能假设类名前缀、模块名、版本枚举、目录名或对象层级。派生数据源如果复用同一 parser
家族，可以复用公共方言适配器，但必须分别验证其 parser properties、元数据层级和 fixture。

## 三、如何取得目标数据库 SQL

优先动态扫描 `split/<dialect>` 下实际存在的全部版本和子目录，不硬编码版本清单。如果
目标数据源没有 split fixture，则使用现有、经过验证的等价 SQL 语料，并建立能够可靠表达
单条 SQL 边界、版本和 parser properties 的 fixture；不要为了套用参考数据源而伪造目录。

不得按分号拆分 SQL。split fixture 的真实边界是：

- 长分隔线左侧是原始脚本；
- 长分隔线右侧由短分隔线分开的每个块对应一条 SQL occurrence；
- 每个块中 `[TYPE]` 后面的 SQL 原文是本任务使用的 SQL；
- routine body、字符串、注释和方言特殊块中可能包含分号；
- sidecar 只用于恢复目标方言的 parser properties；
- exact-version 或等价版本标记只用于选择正确 parser version。

每条 SQL 使用稳定 occurrence ID：

```text
<相对目标 SQL fixture 根目录的路径>#<三位序号>
```

完整性台账至少记录：

- occurrence_id
- version
- exact_version
- parser_properties
- fixture_path
- sql_sha256
- sql_text
- query_status
- expected_columns
- lineage_status
- reason
- grammar_context
- lineage_visitor_method
- implementation_status
- verification_status

`expected_columns` 必须是有序列表。每个结果列记录：

- column：最终输出名称；
- sources：有序物理字段来源；
- 每个 source 的 catalog、schema、table、column；
- 每个 source 的 startLine、startColumn、endLine、endColumn。

路径中包含 `reject` 的 SQL 仍进入完整性台账，但必须保持状态边界：

- 同一 parser version 和 parser properties 下无法产生 CST，标记 `PARSE_REJECTED`；
- `PARSE_REJECTED` 不是空血缘；
- 不为完成血缘任务而擅自放宽 parser；
- 若另一个受支持版本可以解析同一 SQL，可以作为语义对照，但不能伪造当前版本结果。

不是每条 split SQL 都产生查询结果列。DDL、DCL、事务或没有结果集的 DML 可以标记
`NOT_A_QUERY`；带 `RETURNING`、`OUTPUT` 或方言等价返回子句的语句，必须根据当前
`LineageAnalysisSpi` 公共职责和目标方言真实行为明确处理，不能只看首关键字。

## 四、三层生产架构

### 4.1 SDK 契约层

包：

`com.clougence.clouddm.sdk.sql.analysis.lineage`

保留并使用：

- `LineageAnalysisSpi`
- `LineageContext`
- `LineageColumn`
- `SourceName`

SDK 只定义公共输入输出，不依赖 `sqlc-common`，不依赖任何方言 parser，也不承载 CST、
作用域或解析过程模型。

`SourceName` 的位置约定：

- 行号从 1 开始；
- 列号从 0 开始；
- 结束位置为开区间；
- 坐标相对于传入 `LineageAnalysisSpi.analyze` 的完整 SQL；
- 新接入或迁移后的实现不得用全零位置表示“以后再补”。

### 4.2 公共模型与核心算法层

根包：

`com.clougence.sql.common.analysis.lineage`

该层持有：

- `model`：查询、查询块、查询项、关系、字段引用、表函数和源码范围；
- `scope`：CTE 词法作用域、FROM 关系作用域和已解析关系；
- `resolve`：元数据接口、表名模型、已解析列和 `LineageResolver`。

公共层负责：

- 查询块和集合运算递归；
- CTE 作用域与遮蔽；
- FROM 关系绑定；
- 物理表元数据展开；
- 未限定字段消歧；
- `*` 与 `table.*` 展开；
- 派生表和 CTE 的向上替换；
- 表达式值依赖合并；
- 输出列顺序与来源顺序；
- 集合运算按位置合并；
- 最终 `LineageColumn` 和 `SourceName` 生成。

公共层不得引用：

- 任意方言的 lexer、parser、ANTLR context 或 visitor；
- security、resource、behavior、split 的 Domain 或输出模型；
- 具体数据源配置类；
- 平台服务定位和插件装配细节。

依赖方向必须保持：

```text
目标方言模块
  -> sqlc-common
       -> cgdm-plugin-sdk
```

### 4.3 数据源 CST 适配层

每个 parser 家族建立自己的 lineage 包和 CST adapter。该层负责：

- 使用目标方言现有 parser provider 创建 CST；
- 识别查询块、select list、FROM、CTE、集合运算和方言表达式；
- 从 ANTLR token 提取准确 `SourceRange`；
- 将 CST 无损转换为公共血缘模型；
- 将方言特有结构归一化为公共语义；
- 调用公共 `LineageResolver`。

建议结构：

```text
com.clougence.sql.<dialect>.analysis.lineage
├── <Dialect>LineageAnalysisSpi
└── antlr
    ├── <Dialect>LineageCstVisitor
    └── <Dialect>LineageTokenRangeFactory
```

CST adapter 不能：

- 访问安全规则 Domain；
- 调用 security/resource/behavior/split visitor；
- 直接完成公共作用域解析；
- 自行查询物理表元数据；
- 直接拼装最终 `LineageColumn`；
- 通过 SQL 文本搜索恢复 token 位置。

MySQL 参考链路是：

```text
MySQL CST
  -> MyLineageCstVisitor
  -> common lineage model
  -> LineageResolver
  -> List<LineageColumn>
```

后续数据源应形成相同的边界，而不是复制 MySQL 的 context 方法：

```text
MySQL CST     -> MySQL adapter     ┐
Postgres CST  -> Postgres adapter  ├-> common lineage model -> common resolver
Oracle CST    -> Oracle adapter    ┤
Dameng CST    -> Dameng adapter    ┘
```

### 4.4 禁止保留的旧链路

目标数据源的新生产实现不得引用：

```text
com.clougence.clouddm.sdk.service.secrules
com.clougence.clouddm.sdk.sql.analysis.security
com.clougence.sql.<dialect>.analysis.security
com.clougence.sql.common.analysis.secrules
```

不得继续使用：

- `RuleDomain`
- `RdbSelectDomain`
- `RdbTableDomain`
- `RdbColumnDomain`
- `RdbCallDomain`
- 安全分析使用的 `QueryItem`
- 方言 security builder
- 方言 security visitor
- `AbstractLineageAnalysisSpi`

旧链路可以在迁移前作为缺口定位线索，但不能作为新 fixture 的正确性来源，也不能继续
承载目标数据源的生产血缘：

```text
方言 CST
  -> SecRuleDomain
  -> AbstractLineageAnalysisSpi
  -> 反推血缘
```

## 五、公共血缘模型与实现思想

模型名称以仓库当前真实代码为准。若实现演进导致名称变化，应保持本节描述的职责边界，
不要为匹配文档重复创建同义类型。

### 5.1 查询与查询块

每个查询块同时包含：

- 有序查询项；
- FROM 关系；
- 当前层可见 CTE；
- 外层查询作用域；
- 集合运算分支；
- 必要的方言属性。

例如：

```sql
select table1.id e,
       table2.*,
       table1.id + table2.id
from table1
left join table2 on table1.id = table2.id
```

公共模型必须保留三个有序查询项和两个关系。表达式查询项必须保存其中所有参与结果值计算
的字段引用，不能只保存表达式文本。

### 5.2 查询项

一个查询项至少表达：

- 输出名称或显式别名；
- 有序值依赖；
- 是否为 `*`；
- `table.*` 的限定名；
- 查询项自身的代码范围。

```sql
select id + name as display_name
```

这是一个结果列，包含两个字段来源，不是两个结果列。

### 5.3 关系

FROM 中可被字段引用的关系至少区分：

- 物理表；
- 派生表；
- CTE；
- JOIN 组合；
- 表函数；
- 方言支持时的 LATERAL 或等价关系。

每个关系应提供可查找名称或别名、有序输出字段、可见父作用域和关系类型。

### 5.4 字段引用与源码范围

一次真实字段引用至少包含：

- catalog；
- schema；
- table 或 qualifier；
- column；
- `SourceRange`。

字段位置必须在 CST 转换阶段直接从 token 获取。典型 ANTLR 位置计算为：

```java
startLine   = start.getLine();
startColumn = start.getCharPositionInLine();
endLine     = stop.getLine();
endColumn   = stop.getCharPositionInLine() + stop.getText().length();
```

多 token、转义标识符、带引号名称和跨行表达式必须由目标方言的 token range factory 按
真实 token 范围处理，不能机械假设所有引用都只有一个 token。

### 5.5 已解析列

关系对外公开的每个已解析字段至少包含：

- 当前输出名称；
- 当前关系别名；
- 有序 `SourceName`；
- 必要的内部解析状态。

派生关系对外公开的是子查询的有序输出，不是它内部物理表字段的平铺 Map。外层引用先按
派生关系输出名称或位置命中，再沿已解析来源向上替换。

## 六、核心解析算法

### 6.1 两阶段实现

第一阶段由方言 adapter 完成：

```text
方言 CST
  -> 方言 LineageCstVisitor
       -> LineageQuery
       -> LineageQueryBlock
       -> LineageSelectItem
       -> LineageRelation
       -> LineageValue
       -> SourceRange
```

第一阶段只做无损语义翻译，不做物理字段解析。

第二阶段由公共 `LineageResolver` 自底向上完成：

1. 注册当前层可见 CTE。
2. 解析 FROM 中的物理表。
3. 递归解析派生表、CTE、表函数和 LATERAL 子查询。
4. 构造当前查询块的关系作用域。
5. 按 select list 顺序解析查询项。
6. 展开 `*` 和 `table.*`。
7. 解析表达式中的值依赖。
8. 合并集合运算分支。
9. 输出有序 `LineageColumn`。

SQL 书写顺序通常是 SELECT 在 FROM 之前，但解析字段前必须先准备 FROM 关系。不能依赖
visitor 的自然进入顺序完成作用域绑定。

### 6.2 物理表字段解析

1. 使用 `LineageMetadataResolver` 获取物理表的有序字段。
2. 用查询上下文和 SQL 中的限定名补全 Catalog/Schema/Table。
3. 为物理表生成有序已解析字段。
4. 建立当前查询块的字段查找索引。
5. 按顺序解析查询项。
6. 将字段引用绑定到物理字段。
7. 将星号展开为对应关系的有序字段。

查找索引必须属于当前作用域，并支持目标方言实际提供的：

- 表名和表别名；
- Catalog/Schema 限定名；
- 未限定字段；
- 外层相关引用；
- 标识符大小写和引用规则；
- `NATURAL JOIN`；
- `JOIN ... USING`；
- 方言特有的名称可见性。

### 6.3 派生表向上替换

```sql
select x.e
from (
    select table1.id as e
    from table1
) x
```

先解析子查询：

```text
e -> table1.id
```

再绑定虚拟关系：

```text
x.e -> 子查询输出 e -> table1.id
```

外层不能重新猜测 `x.e` 属于哪张物理表。派生表显式列别名：

```sql
from (select a, b from t) x(c, d)
```

必须按位置把输出重命名为 `c`、`d`，不能按名称匹配。

### 6.4 WITH 作用域

CTE 应按词法作用域处理：

```text
CteScope
├── 当前层 CTE 定义
└── parent scope
```

名称从当前层向父层查找。同名内层 CTE 遮蔽外层 CTE。一个 CTE 只能看到目标方言允许
可见的定义；递归 CTE 必须按方言语义单独处理，不能依靠普通递归无限展开。

括号化集合运算操作数可以拥有局部 WITH。这类 CTE 不能被错误提升到其他分支。

### 6.5 集合运算

`UNION`、`UNION ALL`、`INTERSECT`、`EXCEPT` 及方言等价结构按输出位置合并：

```text
branch1.column[0].sources
    +
branch2.column[0].sources
    =
result.column[0].sources
```

最终输出名称通常取第一个分支，但必须以目标方言语义为准。分支列数不一致时给出明确错误，
不能截断或补空。各来源保留各自分支中的真实位置。

### 6.6 表达式递归与查询递归

普通函数和表达式只在当前查询项内递归收集值依赖：

```sql
select concat(trim(a.name), upper(b.code))
from table1 a
join table2 b
```

结果是一个输出列，来源包括 `a.name` 和 `b.code`。

表达式中出现子查询时，递归调用完整查询分析：

```sql
select coalesce(
    (select max(t2.value) from table2 t2),
    t1.value
)
from table1 t1
```

子查询建立自己的 CTE scope、FROM scope 和有序输出；解析完成后，把输出来源合并到父级
表达式。普通函数不能凭空建立新的查询作用域。

## 七、结果值依赖边界

列血缘表达结果值来源，不是 SQL 中出现的所有字段。

### 应进入结果列血缘

- select item 中直接引用的字段；
- 算术、字符串、条件和其他表达式中的字段；
- 函数参数中的字段；
- CASE 返回值及真正影响输出值的分支表达式；
- 窗口函数内部 `PARTITION BY`、`ORDER BY` 和窗口参数中的字段；
- 标量子查询返回列的真实来源；
- JSON、数组、结构体或其他结构化值构造中的字段；
- 集合运算各分支对应位置的来源；
- 方言返回子句中实际构造结果值的字段。

### 不得进入结果列血缘

- 仅用于 WHERE 过滤的字段；
- 仅用于 JOIN ON 匹配的字段；
- 仅用于普通 GROUP BY 的字段；
- 仅用于 HAVING 过滤的字段；
- 仅用于查询级 ORDER BY 排序的字段；
- LIMIT、OFFSET、锁定、优化器 hint 或访问路径中的字段/对象；
- 只定义数据类型、约束、权限或存储属性的名称。

窗口函数内部的排序与分区表达式会影响窗口结果，因此属于该查询项的值依赖；查询块末尾
独立的 ORDER BY 只改变结果顺序，不属于结果列血缘。

父查询关系作用域只用于解析子查询输出表达式中直接引用的外层字段。相关引用如果只出现在
子查询 WHERE 或 JOIN 条件中，不应进入结果列来源。

对于 CASE 条件、布尔表达式、聚合 FILTER 子句等可能影响“选取哪个值”但不直接提供值的
结构，必须以公共模型既定语义和已有高价值 fixture 为准；若需调整，应在公共层形成统一
规则并回归所有已接入方言，不能由单个 visitor 临时决定。

## 八、SourceName 位置传播规则

### 8.1 直接字段

```sql
select t.id from table1 t
```

来源是 `table1.id`，位置是 `t.id` 的完整范围。

### 8.2 未限定字段

```sql
select id from table1
```

来源是 `table1.id`，位置是 `id` 的范围。

### 8.3 星号展开

```sql
select * from table1
```

每个展开字段使用该 `*` 的位置。

```sql
select t.* from table1 t
```

每个展开字段使用 `t.*` 的完整位置。

### 8.4 派生表与 CTE

```sql
select x.e
from (
    select t.id as e
    from table1 t
) x
```

向上替换后最终来源保留内部 `t.id` 的位置，不使用外层 `x.e` 的位置。内部来源若由星号
展开，则保留内部星号的位置。

### 8.5 集合运算

```sql
select id from table1
union all
select id from table2
```

两个 `SourceName` 分别保留两个分支中 `id` 的位置。

### 8.6 重复引用

```sql
select id + id from table1
```

同一物理字段出现两次时，保留两个位置不同的 `SourceName`。不能只按资源路径去重。需要
资源去重的消费者应在消费端按 `toDsResPath()` 处理。

### 8.7 偏移与完整文本

所有位置相对于传入 SPI 的完整 SQL，而不是某个截取后的查询块。若公共调用链未来支持
显式行列偏移，fixture 必须增加非零偏移测试；不能只测试从 `(1, 0)` 开始的 SQL。

## 九、方言适配规则

目标数据源必须使用其现有 grammar、lexer、parser provider 和版本配置，不能创建第二套
SQL tokenizer。至少审视：

- 普通 SELECT 和无 FROM 查询；
- 多层限定名、引用标识符和大小写规则；
- 表别名、列别名及别名可见性；
- JOIN、NATURAL JOIN 和 USING；
- 派生表及派生列别名；
- WITH、嵌套 WITH 和递归 CTE；
- 星号、限定星号和方言星号修饰；
- 函数、CASE、CAST 和嵌套表达式；
- 聚合、窗口定义和命名窗口继承；
- 标量子查询和相关子查询；
- UNION、INTERSECT、EXCEPT 及方言变体；
- 表函数、JSON/数组展开和 LATERAL；
- QUALIFY、PIVOT、UNPIVOT、MODEL、CONNECT BY 等方言查询结构；
- SELECT INTO、RETURNING、OUTPUT 或方言等价返回形式；
- 受支持版本之间的 grammar 差异；
- parser properties、兼容模式和会话语法开关。

只实现目标数据库真实支持的结构。不能为了覆盖公共模型而编造 SQL，也不能把其他方言的
语义复制过来。

派生数据源处理原则：

1. 真正复用同一 parser 且语义相同的能力应复用 adapter。
2. parser properties 或兼容模式不同的，测试入口必须显式传递。
3. 对象层级不同的，在元数据补全边界处理，不能在公共 resolver 中写数据源名称判断。
4. 方言存在额外 grammar 分支时，在自己的 adapter 扩展公共模型。
5. 若多个数据源共同需要一种通用模型能力，先抽象真实公共语义，再修改 `sqlc-common`。

## 十、元数据解析

`LineageResolver` 通过窄接口 `LineageMetadataResolver` 获取物理表有序字段。方言 SPI
负责把 `LineageContext`、SQL 中的限定名和数据源对象层级转换为元数据查询参数。

必须保证：

- 星号展开使用真实有序列；
- SQL 中显式 Catalog/Schema 覆盖上下文默认值；
- 元数据结果缺失 Catalog/Schema/Table 时按已确认上下文补全；
- 不用安全规则 Domain 推测元数据；
- 测试元数据独立于待验证的旧血缘实现；
- fixture 新增表或列时同步补充顶层 `_meta`；
- 元数据无法确认时明确失败，不伪造物理字段；
- 同名表、临时表、系统表、表函数等优先级符合目标方言真实规则。

版本化语料规模较大时，可用独立 `VirtualMetaService` 或等价测试服务提供确定性元数据，
但不能从当前血缘输出反向构造期望。

## 十一、fixture 格式

目标目录：

`tests/ds-test/src/test/resources/lineage/<dialect>`

基础 case：

```text
[single_column]
context:
{
  "levels": {
    "Catalog": "catalog1",
    "Schema": "schema1"
  }
}
sql:
select t.id from table1 t
expect:
{
  "id": [
    "(1:7~1:11) /catalog1/schema1/table1/id/"
  ]
}
```

每个来源统一使用：

```text
(startLine:startColumn~endLine:endColumn) /catalog/schema/table/column/
```

示例坐标必须以真实 parser token 为准。不能照抄本文示意值。

当输出列名不重复时，可以使用保持字段顺序的 object：

```json
{
  "column1": [
    "(1:7~1:9) /catalog1/schema1/table1/column1/"
  ]
}
```

有重名结果列时必须使用有序数组，避免 Map 聚合丢失：

```json
[
  {
    "column": "column1",
    "sources": [
      "(1:7~1:21) /catalog1/schema1/table1/column1/"
    ]
  },
  {
    "column": "column1",
    "sources": [
      "(1:23~1:37) /catalog1/schema1/table2/column1/"
    ]
  }
]
```

期望异常时：

```json
{
  "exception": "ExceptionSimpleName"
}
```

fixture 要求：

- 保持结果列顺序；
- 保持每个结果列的来源顺序；
- 每个来源必须带位置；
- 不用 object 表示有重名输出列的场景；
- `context.levels` 显式表达非默认对象层级；
- parser version 和 properties 由测试入口或 sidecar 明确传递；
- 新增 SQL 涉及的新表或列先补充 `_meta`；
- 不把 split `[TYPE]` 写入 lineage expect；
- 不把当前实际输出直接复制为期望；
- fixture runner 必须比较完整位置字符串，不能只比较资源路径或只断言位置非零。

如果通用 `LineageTextTest` 当前仍使用 `SourceName.toDsResPath()`，接入位置断言时应让目标
测试使用 `toLocatedDsResPath()`，并最终统一通用 fixture 协议；不能因为旧测试未比较位置
而让新数据源继续输出零位置。

## 十二、实现流程

### 阶段 1：盘点和固定语义

1. 动态生成目标数据源全量 occurrence 台账。
2. 盘点受支持版本、exact-version、parser properties 和派生数据源。
3. 盘点当前 SPI、旧安全 Domain 依赖、fixture 和测试入口。
4. 选取一批高价值 case，人工写出有序结果列、来源路径和位置。
5. 先固定直接字段、表达式、星号、派生表、CTE 和集合运算语义。

### 阶段 2：建立或接入方言 adapter

1. 保持公共 SDK 契约不变，除非发现真实跨数据源契约缺口。
2. 目标 SPI 不再继承 `AbstractLineageAnalysisSpi`。
3. 移除目标生产血缘对 security builder 和 visitor 的使用。
4. 建立方言 CST visitor 和 token range factory。
5. 从 CST 构建公共查询模型。
6. 通过 `LineageMetadataResolver` 隔离平台元数据服务。
7. 调用公共 `LineageResolver` 生成结果。

### 阶段 3：基础查询和向上替换

1. 物理表元数据展开。
2. 直接字段和限定字段。
3. 未限定字段唯一解析和歧义错误。
4. `*` 与 `table.*`。
5. 派生表和多层派生表。
6. 派生列别名按位置替换。
7. CTE 作用域、遮蔽和递归边界。
8. 集合运算按位置合并。

### 阶段 4：表达式和方言高级语法

1. 普通函数及嵌套函数。
2. 算术、条件、CAST 和结构化表达式。
3. 标量子查询和相关子查询。
4. 聚合与窗口函数。
5. 表函数和 LATERAL。
6. 方言专属查询结构。
7. 版本和 parser properties 差异。

### 阶段 5：全量语料

1. 按语法类别和版本分批迁移。
2. 每批先人工确定血缘语义，再写或校对 expect。
3. 失败时区分 adapter 漏节点、公共模型缺口、resolver 错误、元数据错误、位置错误和期望错误。
4. 只有重新阅读 SQL、grammar 或数据库官方文档后才能修改期望。
5. 同一 grammar context 和同一语义跨版本联动检查。
6. 公共层改动后回归 MySQL 及其他已迁移数据源。
7. 更新台账，直到每个 occurrence 状态明确。

lineage 状态只允许：

- `VERIFIED_LINEAGE`：全部结果列、来源、顺序和位置人工确认且测试通过；
- `VERIFIED_EMPTY`：确认查询返回列不依赖物理字段，空 sources 或空结果符合公共契约且测试通过；
- `NOT_A_QUERY`：该 SQL 不属于血缘 SPI 处理范围；
- `PARSE_REJECTED`：对应 parser 配置无法产生 CST；
- `METADATA_BLOCKED`：缺少可复现且确实必要的元数据；
- `BLOCKED`：存在具体、可复现的公共模型或实现阻塞。

不能使用“旧实现没有结果”“fixture 没写”“语句太复杂”作为完成状态。

## 十三、最低测试矩阵

每个目标数据源至少覆盖：

1. 单表直接字段。
2. 字段别名。
3. 多字段表达式。
4. 多表限定字段。
5. 未限定字段唯一解析。
6. 未限定字段歧义错误。
7. `*`。
8. `table.*`。
9. 多关系星号输出顺序。
10. 派生表。
11. 多层派生表。
12. 派生列别名。
13. CTE。
14. 嵌套 CTE 和同名遮蔽。
15. 递归 CTE 的明确边界。
16. 集合运算分支来源合并。
17. 标量子查询。
18. 相关子查询。
19. 函数和嵌套函数参数。
20. CASE、CAST 和目标方言常用表达式。
21. 聚合与窗口函数。
22. JOIN USING 和 NATURAL JOIN。
23. 表函数或目标方言等价能力。
24. 同一物理字段的多个引用位置。
25. 重名结果列的有序输出。
26. 多行 SQL 的行列位置。
27. 引用标识符、转义名称和多 token 名称的位置。
28. 无 FROM 常量查询。
29. 空 sources 的合法结果列。
30. 每个受支持版本和关键 parser properties。

不支持某项语法的数据源应明确记为“不适用”，不能编造 fixture。

## 十四、验证与完成标准

从以下目录运行：

`/home/zyc/project/dm/open-cdm/backend`

先确认 Gradle 项目名，再执行目标方言编译和血缘测试。典型命令：

```bash
./gradlew projects

./gradlew \
  :<target-sql-module>:compileJava \
  :s-test:compileTestJava

./gradlew \
  :s-test:test \
  --tests 'com.clougence.clouddm.ds.lineage.<dialect>.*'
```

公共层修改后至少回归：

```bash
./gradlew \
  :sqlc-common:compileJava \
  :sql-mysql:compileJava \
  :s-test:compileTestJava

./gradlew \
  :s-test:test \
  --tests 'com.clougence.clouddm.ds.lineage.mysql.*'
```

不要并发运行共享同一 `tests/ds-test/build/test-results/test` 目录的 Gradle 测试。

全部满足以下条件才算目标数据源完成：

1. 目标 `LineageAnalysisSpi` 直接实现公共接口，不继承 `AbstractLineageAnalysisSpi`。
2. 目标生产血缘包不引用安全规则 Domain、builder 或 visitor。
3. 方言 adapter 只负责 CST 到公共模型的转换。
4. 公共核心代码不引用目标方言 parser 或 ANTLR context。
5. 所有物理来源具有真实、非占位源码范围。
6. 物理字段、表达式、星号、派生表、CTE 和集合运算遵守公共向上替换规则。
7. 输出列顺序与 select list 或星号元数据顺序一致。
8. 来源顺序稳定，重复引用不会因路径去重而丢失。
9. 重名结果列不会因 Map 聚合而丢失。
10. 目标数据源全部受支持版本和 parser properties 已进入台账。
11. 每个 occurrence 都有允许的最终状态。
12. 高价值 fixture 和目标方言全量测试通过。
13. 公共层变更已回归 MySQL 及其他受影响数据源。
14. `git diff --check` 通过。
15. 未修改 split 分类、资源分析、行为分析、安全规则或权限模型来迁就血缘。

最终报告必须包含：

1. occurrence 总数及按版本、parser properties、语法类别和状态统计；
2. 新增或修复的 lineage adapter、公共模型和 resolver 能力；
3. 仍为 `PARSE_REJECTED`、`METADATA_BLOCKED` 或 `BLOCKED` 的具体清单；
4. fixture 和 `_meta` 变更数量；
5. 执行的编译、测试、回归命令及结果；
6. 未覆盖项及原因。

## 十五、目标架构

每个完成迁移的数据源都必须形成：

```text
SDK 契约层
  LineageAnalysisSpi(LineageContext)
    |
    v
目标方言 CST 适配层
  DslProvider -> CST -> DialectLineageCstVisitor
    |
    v
sqlc-common 核心层
  公共血缘查询模型
    -> CTE 词法作用域与 FROM 关系作用域
    -> 表达式递归与查询递归
    -> 元数据驱动的有序字段展开
    -> 自底向上的真实字段替换
    -> List<LineageColumn>
         -> SourceName（资源名 + 精确位置）
```

不得再出现：

```text
目标方言 CST
  -> SecRuleDomain
  -> 反推血缘
```
