# Rule Test Cases

`rules` 用于验证安全规则脚本在各数据源 `SecDomainResolveSpi` 输出上的执行结果。测试入口会扫描本目录下所有 `*.txt`，再读取顶层 `_rules` 中的规则脚本。

## 目录组织

- 第一层目录是数据源或数据源家族，例如 `mysql`、`postgres`、`gauss`、`doris`。
- 同一数据源内按规则目标分类，例如 `query.txt`、`table.txt`、`column.txt`、`constraint.txt`。
- 派生数据源放在同一家族目录时，文件名使用前缀，例如 `por4pg_query.txt`、`selectdb_table.txt`。

公共规则脚本统一放在：

```text
_rules/<target>/<rule-name>.rule
```

不要在 `rules` 数据源目录下再建私有 `_rules` 或 `rules` 子目录。

## Case 格式

每个 case 使用 `----------` 分隔：

```text
[MyRuleQueryAllowAsTest#selectAndWithTest_1#001]
rule: _rules/query/query-allow-as.rule
expect: false
vars:
  allow: false
sql:
select id as a, name from table_1
```

字段说明：

- `[case_name]` 必填，建议包含原测试名、场景名和序号。
- `rule` 必填，指向顶层 `_rules` 下的规则文件。
- `expect` 必填，`true` 表示规则通过，`false` 表示规则拒绝。
- `vars` 可选，按 `key: value` 每行一个变量；没有变量可省略或写 `{}`。
- `sql` 必填，写待解析并执行规则的 SQL。

## 编写建议

- 一个文件内聚合相同目标类别的 case，避免一个规则一个文件。
- 同一规则建议同时覆盖通过和拒绝场景。
- `vars` 只写本规则需要的变量，不要复制无关配置。
- 如果新增规则脚本，先放到顶层 `_rules` 对应 target 目录，再在各数据源 case 中引用。

## 运行

```bash
./gradlew :s-test:test --tests 'com.clougence.clouddm.ds.rules.RuleTextTest' --no-daemon
```
