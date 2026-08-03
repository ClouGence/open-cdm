# Column Lineage Test Cases

`lineage` 用于验证 SQL 查询列血缘分析。测试入口会扫描本目录下所有 `*.txt`，按数据源目录执行对应
`LineageAnalysisSpi`。

## 目录组织

- 第一层目录是数据源或数据源家族，例如 `mysql`、`postgres`、`doris`。
- 同一数据源内按场景合并脚本，例如 `simple.txt`、`join.txt`、`function.txt`、`with.txt`。
- 同家族派生数据源放在同一目录下时，文件名使用派生前缀，例如 `selectdb_simple.txt`、`por4pg_simple.txt`。

## Case 格式

每个 case 使用 `----------` 分隔：

```text
[case_name]
context:
{
  "levels": {
    "Catalog": "catalog1",
    "Schema": "schema1"
  }
}
sql:
select column1 as c1 from table1
expect:
{
  "c1": [ "/catalog1/schema1/table1/column1/" ]
}
```

字段说明：

- `[case_name]` 建议保留历史测试类名或场景名，方便失败时定位。
- `context` 可选，用于覆盖默认上下文。默认 `Catalog=catalog1`、`Schema=schema1`。
- `sql` 必填，写一条用于列血缘分析的 SQL。
- `expect` 必填，是 JSON object，key 是输出列名，value 是该输出列对应的真实列路径数组。

## 期望结果

期望结果只写输出列到真实列的映射：

```json
{
  "output_column": [
    "/catalog/schema/table/source_column/"
  ]
}
```

注意：

- 保持字段顺序，runner 会按顺序比对输出列。
- 路径格式使用 `/catalog/schema/table/column/`，不要再写 levels 包装。
- 一个输出列来源多个真实列时，在数组中按分析结果顺序列出。
- 如果期望异常，`expect` 可写：`{"exception":"ExceptionSimpleName"}`。

## 元数据

列元数据统一从顶层 `_meta` 读取：

- `_meta/1-layer`
- `_meta/2-layer`
- `_meta/3-layer`

不要在 `lineage` 目录下新增私有 meta。新增 SQL 涉及的新表或列，应先补充 `_meta` 中对应 layer 的资源文件。

## 运行

```bash
./gradlew :s-test:test --tests 'com.clougence.clouddm.ds.lineage.ColumnLineageTest' --no-daemon
```
