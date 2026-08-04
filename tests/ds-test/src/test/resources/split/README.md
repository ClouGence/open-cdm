# Split Test Cases

`split` 用于验证脚本切分结果。测试入口会扫描本目录下所有 `*.txt`，每个期望切分片段都会生成一个 JUnit dynamic test。

## 目录组织

- 第一层目录是数据源，例如 `mysql`、`db2`、`sqlserver`、`redis`。
- 数据源目录下按语句类型和场景命名，例如 `dql_select_0.txt`、`ddl_table_0.txt`、`inline_basic_0_003.txt`。
- 文件名建议使用 `<category>_<subject>_<index>.txt`，必要时追加 `_sqltype` 等说明。

## 文件格式

一个 split fixture 分为输入 SQL 和期望结果两段，中间用长分隔线：

```text
select * from table1;
select * from table2;
------------------------------------------------------------------------------------------
[SELECT] select * from table1;
----------
[SELECT] select * from table2;
```

规则：

- 长分隔线必须是：
  `------------------------------------------------------------------------------------------`
- 期望结果中的每个 split 使用 `----------` 分隔。
- 每个期望块第一行使用 `[TYPE]` 声明主类型。复合语句可以使用 `[TYPE_A|TYPE_B]`，
  按顺序声明 `SplitScript.getType()` 中的完整类型集合。
- 代码块存在子语句时，递归收集所有后代 `SplitScript` 的类型，在父类型集合后使用一层
  括号声明：`[TYPE_A|TYPE_B(CHILD_A,CHILD_B,GRANDCHILD)]`。
- `|` 只连接根节点自身的复合类型；`,` 分隔递归收集到的后代类型。后代类型按深度优先
  的首次发现顺序排列并去重。
- 分类头的括号不再递归嵌套，也不允许空括号。没有后代类型时不输出括号。
- 分类头只表达扁平的后代类型摘要；实际 `SplitScript.children` 仍保存递归结构、各子节点
  的 SQL、位置和更深层 children。
- `[TYPE]` 后可以同一行写 SQL，也可以换行写多行 SQL。
- runner 会对实际脚本和期望脚本执行 `strip()` 后比对，内部空白和注释仍然需要保持一致。

## 编写建议

- 输入区写完整原始脚本，包含注释、空行、结束符和批处理分隔符。
- 期望区按切分后的顺序逐段写出。
- 对事务、过程调用、CDC/EXEC、复合 DDL 等容易误切的场景，应单独建文件。
- 如果一个输入脚本期望 N 段切分，测试数量也会是 N 个，失败名形如 `split/mysql/dql_select_0.txt#001 [SELECT] ...`。

## 运行

```bash
./gradlew :s-test:test --tests 'com.clougence.clouddm.ds.split.*SplitTextTest' --no-daemon
```
