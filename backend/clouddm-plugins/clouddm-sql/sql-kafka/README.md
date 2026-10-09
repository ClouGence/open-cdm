# Kafka 命令语言

本模块通过 ANTLR 解析 Kafka CLI 风格的命令，不调用 Shell 或 Kafka CLI 进程。

- `KafkaLexer.g4` 定义命令名、选项、引号、转义、注释、续行和语句分隔符；`KafkaParser.g4` 定义命令及参数语法。
- `KafkaDslProvider` 接入通用 DSL 解析、拆句和 Visitor 入口，返回 `KafkaCommandSet` / `KafkaCommand` AST。
- `KafkaSplitAnalysisSpi` 复用 `AbstractSplitAnalysisSpi` 的流式拆句、位置计算与关闭机制。
- Java 层负责 AST 转换、参数值解码及必填、互斥、数值范围等语义校验。
- `ds-kafka` 注册编辑器语言服务；编辑器拆句和补全复用同一 ANTLR Lexer，并保留不完整 Token，以便逐句诊断、在输入过程中提供补全。

## 当前支持

| 命令（均兼容 `.sh` 后缀） | 操作与参数 |
| --- | --- |
| `kafka-topics` | `--list`、`--describe`、`--create`、`--delete`；`--topic`；创建时可指定 `--partitions`、`--replication-factor`、可重复的 `--config key=value`、`--if-not-exists`；删除时支持 `--if-exists` |
| `kafka-consumer-groups` | `--list`；`--describe --group <id>`（默认 offsets/lag，也支持 `--offsets`、`--members [--verbose]`、`--state`）；`--delete --group <id>` |
| `kafka-console-consumer` | `--topic`、`--partition`、`--from-beginning`、`--offset latest`、`--max-messages`、`--timeout-ms` |

每类命令支持 `--help`，编辑器通过信息提示展示用法。Consumer Group 不提供创建命令，每条命令最多操作一个 group。

参数名大小写敏感，支持 `--name value` 和 `--name=value`。操作参数互斥；重复参数报错，只有 topic 创建的 `--config` 可重复且配置键不能重复。不接受未实现的原生选项，也不允许命令覆盖当前数据源的 Broker、认证或客户端配置。

## 拆句与位置

- 分号或换行结束一条命令，单/双引号中的分隔符不拆句。
- 支持单引号字面值、双引号转义、引号外反斜杠转义，以及反斜杠加 LF/CRLF 续行。
- `#` 在词边界开始行注释；词内或引号中的 `#` 保留为字面值。
- 不执行变量展开、命令替换、管道、重定向或文件读取；不支持 SQL 绑定参数。
- 行号从 1 开始，列号从 0 开始，结束位置不包含分隔符；应用调用方传入的基准行列。流式拆句按需读取，关闭流不会关闭调用方的 Reader。

## 与原生命令的边界

选项参照 Apache Kafka 4.1 的 [TopicCommand](https://github.com/apache/kafka/blob/4.1/tools/src/main/java/org/apache/kafka/tools/TopicCommand.java)、[ConsumerGroupCommandOptions](https://github.com/apache/kafka/blob/4.1/tools/src/main/java/org/apache/kafka/tools/consumer/group/ConsumerGroupCommandOptions.java) 和 [ConsoleConsumerOptions](https://github.com/apache/kafka/blob/4.1/tools/src/main/java/org/apache/kafka/tools/consumer/ConsoleConsumerOptions.java)，当前只实现上表列出的子集。

- Topic 的 list/describe/delete 使用正则筛选，create 与 consumer 使用确切的 Topic 名称。
- `latest` 表示从末尾等待新消息，不代表历史最后 N 条。控制台允许省略 `--partition` 使用 `--offset latest`，应用于所有分区；原生 CLI 的显式 `--offset` 要求同时指定分区。
- `--from-beginning` 与 `--offset` 互斥；暂不接受数值 offset 或 `--offset earliest`。
- 未指定 `--max-messages` / `--timeout-ms` 时解析仍成功，并显示系统默认值提示。执行阶段负责补齐系统最大获取条数和系统查询超时（毫秒），将用户条数限制在系统上限内；显式用户超时优先。
- 解析阶段保留缺省参数，不访问系统配置，不订阅 Topic，不提交 Consumer Group offset。

## 会话、授权和审计接入

- `KafkaBehaviorAnalysisSpi` 复用 ANTLR 拆句与参数校验，每条命令都产生资源行为；语法错误直接失败。
- 消费消息、查询 Topic / Consumer Group 和帮助命令要求读取权限；创建 Topic、删除 Topic / Consumer Group 要求管理权限。两项权限独立，管理权限不隐含读取消息权限。
- 当前授权粒度为 Kafka 数据源实例（平台资源路径 `/`）；若分析上下文显式提供 Instance 层级，则使用该实例路径。Topic / Group 名称及正则保存在审计对象名称中，不拼入授权路径。Topic 正则删除与无名称的列表查询同样检查实例权限，暂不提供 Topic / Group 单独授权。
- `KafkaSessionFactory` 通过平台资源管理器获取客户端并解析 TLS 文件；`KafkaSessionSpi` 和 `KafkaSupportSpi` 接入查询上下文、只读和中断能力。会话固定自动提交，不支持 JDBC 回调、SQL 执行计划、事务、Catalog 或 Schema 切换。
- `KafkaSession` 通过统一 `ResultBuilder` 输出帮助和失败消息；平台既有链路负责记录命令、行为、执行人、执行结果与耗时。错误消息显式通知审计，以失败状态结束；会话关闭释放客户端并通知关闭监听器。
- 第 4 步仅接通上述链路：`--help` 可执行，其他命令明确返回未实现错误；元数据浏览、实际管理／消费、获取条数和超时重写仍在后续步骤实现。中断当前可取消等待中的连接检查；消费时的 `Consumer.wakeup()` 随消费执行实现。数据源继续保持隐藏。
