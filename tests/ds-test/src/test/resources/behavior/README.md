# Behavior Analysis Test Cases

`behavior` 用于验证 SQL 语句行为分析。测试会按数据源目录取得对应
`BehaviorAnalysisSpi`，并严格比较 `StatementBehavior`、`BehaviorRelation`
和 `BehaviorObject`。

## 目录组织

- 第一层目录是数据源，例如 `mysql`、`postgres`、`redis`、`sqlserver`。
- 有版本差异的方言继续按版本分目录；没有版本差异的方言从 `basic.txt` 开始。
- 每个文件可以包含多个 case，case 之间只使用一行 `----------` 分隔，分隔线前后不留空行。

## Case 格式

```text
[select_join]
sql:
SELECT * FROM db1.t1 JOIN db1.t2 ON t1.id = t2.id;
expect:
{
  "SELECT": [
    {
      "subject": "Table(1:14~1:20) /test/1/catalog1/db1/t1/",
      "action" : "READ"
    },
    {
      "subject": "Table(1:26~1:32) /test/1/catalog1/db1/t2/",
      "action" : "READ"
    }
  ]
}
```

`expect` 是按源码语句顺序书写的对象。每个顶层字段 occurrence 对应一个
`StatementBehavior`：字段名是 `SplitQueryType`，字段值是该语句按源码顺序排列的
行为关系列表。

多条语句具有相同 `SplitQueryType` 时重复输出同名字段，不能合并。重复 key 是
fixture 协议的一部分，测试读取层按字段 occurrence 流式读取，不会通过 Map 或
ObjectNode 覆盖同名语句。

无客体关系只输出 `subject`、`action`，不输出 `target`：

```json
{
  "subject": "Function(1:7~1:18) /test/1/catalog1/schema1/f_score/",
  "action" : "CALL"
}
```

存在客体时使用 `subject`、`action`、`target`：

```json
{
  "subject": "Table(1:13~1:23) /test/1/catalog1/schema1/target_tab/",
  "action" : "CREATE",
  "target" : "Table(1:41~1:51) /test/1/catalog1/schema1/source_tab/"
}
```

`subject` 和每个 `target` 客体都使用
`"<TargetType>(<codeLine>) <resourcePath>"`。`target` 只有一个客体时直接使用
字符串，两个及以上客体才使用字符串数组；没有客体时不输出 `target`，禁止输出
空数组，也禁止用单元素数组封装。同一个 case 内关系字段按值起始列竖向对齐：
`"subject":`、`"action" :`、`"target" :`。多语句的顶层类型 key 也按本 case
最长 key 对齐。对齐不跨 case，紧凑对象字符串内部不增加填充空格。

`codeLine` 使用 `起始行:起始列~结束行:结束列`，结束位置采用开区间。
生产接口传入的 `baseLine`、`baseColumn` 必须计入最终绝对位置。

完整语义和实现边界以
`../_prompt/resource-analysis-prompt.md` 为准。

## 运行

```bash
./gradlew :s-test:test --tests 'com.clougence.clouddm.ds.behavior.BehaviorDialectTextTest'
./gradlew :s-test:test --tests 'com.clougence.clouddm.ds.behavior.mysql.MySql*BehaviorTextTest'
```
