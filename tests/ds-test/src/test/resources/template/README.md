# Template Test Cases

`template` 用于验证各数据源 `CmdTemplateSpi` 生成的 SQL 模板。测试入口会扫描本目录下所有 `*.txt`，按数据源目录选择 SPI。

## 目录组织

- 第一层目录是数据源，例如 `mysql`、`oracle`、`postgres`、`db2`。
- 数据源目录下按模板对象分类，例如 `view.txt`、`function.txt`、`procedure.txt`、`trigger.txt`。

## Case 格式

每个 case 使用 `----------` 分隔：

```text
[createView]
type: createView
option:
{
  "targetName": "target_name",
  "schema": "test_schema",
  "delimited": true,
  "data": {
    "name": "test_name",
    "sql": "test_sql"
  }
}
expect:
<<<
create view `test_schema`.`test_name`
as
test_sql;
>>>
```

字段说明：

- `[case_name]` 必填，使用生成动作或差异点命名。
- `spi` 可选，用于覆盖目录默认 SPI。通常不要写。
- `type` 必填，支持 `createView`、`alterView`、`createFunction`、`createProcedure`、`createTrigger`。
- `option` 必填，是 `CmdTemplateOption` 的 JSON。
- `expect` 必填，使用 `<<<` 和 `>>>` 包裹一个或多个期望输出块。

## 期望结果

- 每个 `<<<...>>>` 对应 `CmdTemplateSpi` 返回列表中的一个字符串。
- runner 会去掉期望块首尾换行，但不会调整内部缩进、空格或换行。
- 如果 SPI 返回多条 SQL，就写多个 `<<<...>>>`，顺序必须一致。

## 编写建议

- 同一对象类型的模板放在同一个文件中。
- 新增 option 字段时，至少覆盖默认值和非默认值各一个 case。
- 保持 SQL 输出完整，包括结束符、分隔符、注释和数据源特有 quoting。

## 运行

```bash
./gradlew :s-test:test --tests 'com.clougence.clouddm.ds.template.TemplateTextTest' --no-daemon
```
