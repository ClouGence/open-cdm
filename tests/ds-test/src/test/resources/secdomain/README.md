# Secdomain Test Cases

`secdomain` 用于验证 SQL 安全域解析结果，也就是 `SecDomainResolveSpi` 输出的 `RuleDomain` 结构。测试入口会扫描本目录下所有 `*.txt`。

## 目录组织

- 第一层目录是数据源，例如 `mysql`、`postgres`、`db2`、`sqlserver`。
- 数据源目录下按 SQL 场景拆分，例如 `dml_ddl.txt`、`alter_table.txt`、`create_table.txt`。
- 只放 secdomain 解析断言，不放规则脚本。规则脚本属于顶层 `_rules` 和 `rules` 测试集。

## Case 格式

每个 case 使用 `----------` 分隔：

```text
[alter_add_column]
context:
{
  "mcSchemaStyle": true
}
sql:
alter table `test`.`user_table` add column `address` varchar(255);
expect:
{
  "contains": [
    {
      "class": "MyColumnDomain",
      "sqlType": "ALTER_TABLE_ADD_COLUMN",
      "auditKind": "CREATE",
      "schema": "test",
      "table": "user_table",
      "column": "address",
      "typeDesc": "varchar(255)"
    }
  ]
}
```

字段说明：

- `[case_name]` 必填，使用明确场景名。
- `context` 可选，用于给解析器传入测试上下文；当前主要用于 MaxCompute 的 `mcSchemaStyle`。
- `sql` 必填，写待解析 SQL。
- `expect` 必填，JSON object 或 JSON array。

## 期望结果

推荐使用 `contains` 编写局部断言：

```json
{
  "contains": [
    {"class":"MyTableDomain", "sqlType": "CREATE_TABLE", "auditKind":"CREATE", "table":"t1"}
  ]
}
```

也可使用：

- `domains`: 按顺序严格比对。
- `contains`: 只要求包含指定 domain，顺序不敏感。
- `childrenSize`: 断言当前 domain 的直接子 domain 数量。
- `size`: 断言 domain 数量。
- `allowExtra: true`: 使用 `domains` 时允许实际结果有更多 domain。
- `exception`: 期望抛出的异常类名。

断言字段通过 Java Bean getter 读取。常用字段包括 `class`、`sqlType`、`auditKind`、`catalog`、`schema`、`table`、`column`、`name`、`newName`、`typeDesc`。

测试执行时会把所有 `RuleDomain.children` 递归铺平到同一层参与断言。父 domain 如果需要验证子节点数量，只写 `childrenSize`；子 domain 按普通 domain 写在同一个 `contains` 或 `domains` 列表中。

示例：

```json
{
  "contains": [
    {"class":"PgSelectDomain", "sqlType": "SELECT", "auditKind":"QUERY", "childrenSize":2},
    {"class":"RdbTableDomain", "schema":"test_schema", "table":"table1"},
    {"class":"RdbTableDomain", "schema":"test_schema", "table":"table2"}
  ]
}
```

## 编写建议

- secdomain 关注“解析出了什么安全域”，不要在这里写规则通过/拒绝逻辑。
- DDL 涉及多个动作时，写多个 expected domain，例如 rename old/new、create table as select、create index + alter table。
- 对解析顺序稳定且重要的场景用 `domains`；一般场景优先用 `contains` 降低无关顺序耦合。

## 运行

```bash
./gradlew :s-test:test --tests 'com.clougence.clouddm.ds.secdomain.SecDomainTextTest' --no-daemon
```
