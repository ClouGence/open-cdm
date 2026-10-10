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
| `kafka-console-consumer` | `--topic`、`--partition`、`--from-beginning`、`--offset latest`、`--max-messages`、`--timeout-ms` |

每类命令支持 `--help`，编辑器通过信息提示展示用法。不支持 Consumer Group 管理命令和消费参数 `--group`。

参数名大小写敏感，支持 `--name value` 和 `--name=value`。操作参数互斥；重复参数报错，只有 topic 创建的 `--config` 可重复且配置键不能重复。不接受未实现的原生选项，也不允许命令覆盖当前数据源的 Broker、认证或客户端配置。

## 拆句与位置

- 分号或换行结束一条命令，单/双引号中的分隔符不拆句。
- 支持单引号字面值、双引号转义、引号外反斜杠转义，以及反斜杠加 LF/CRLF 续行。
- `#` 在词边界开始行注释；词内或引号中的 `#` 保留为字面值。
- 不执行变量展开、命令替换、管道、重定向或文件读取；不支持 SQL 绑定参数。
- 行号从 1 开始，列号从 0 开始，结束位置不包含分隔符；应用调用方传入的基准行列。流式拆句按需读取，关闭流不会关闭调用方的 Reader。

## 与原生命令的边界

选项参照 Apache Kafka 4.1 的 [TopicCommand](https://github.com/apache/kafka/blob/4.1/tools/src/main/java/org/apache/kafka/tools/TopicCommand.java) 和 [ConsoleConsumerOptions](https://github.com/apache/kafka/blob/4.1/tools/src/main/java/org/apache/kafka/tools/consumer/ConsoleConsumerOptions.java)，当前只实现上表列出的子集。

- Topic 的 list/describe/delete 使用正则筛选，create 与 consumer 使用确切的 Topic 名称。
- `latest` 表示从末尾等待新消息，不代表历史最后 N 条。控制台允许省略 `--partition` 使用 `--offset latest`，应用于所有分区；原生 CLI 的显式 `--offset` 要求同时指定分区。
- `--from-beginning` 与 `--offset` 互斥；暂不接受数值 offset 或 `--offset earliest`。
- 未指定 `--max-messages` / `--timeout-ms` 时解析仍成功，并显示系统默认值提示。执行阶段负责补齐系统最大获取条数和系统查询超时（毫秒），将用户条数限制在系统上限内；显式用户超时优先。
- 解析阶段保留缺省参数，不访问系统配置，不订阅 Topic，不提交 Consumer Group offset。

## 会话、授权和审计接入

- `KafkaBehaviorAnalysisSpi` 复用 ANTLR 拆句与参数校验，每条命令都产生资源行为；语法错误直接失败。
- 消费消息、查询 Topic 和帮助命令要求读取权限；创建 Topic、删除 Topic 要求管理权限。两项权限独立，管理权限不隐含读取消息权限。
- Topic 复用 Table 的对象授权路径（`/topicName/`），可独立授予读取、管理权限并继承实例授权。读取消息和详情需要 Topic 读取权限，删除需要 Topic 管理权限；创建 Topic 始终需要实例管理权限。
- `kafka-topics --list` 仅返回有读取权限的 Topic。正则 describe/delete 在授权前解析实际 Topic；describe 校验所有目标的读取权限，delete 校验所有目标的管理权限，任一无权则整条拒绝。执行沿用授权时固定的 Topic 名单，审计记录相同的实际对象；无匹配目标也不会在执行时重新扩展。
- `KafkaSessionFactory` 通过平台资源管理器获取客户端并解析 TLS 文件；`KafkaSessionSpi` 和 `KafkaSupportSpi` 接入查询上下文、只读和中断能力。会话固定自动提交，不支持 JDBC 回调、SQL 执行计划、事务、Catalog 或 Schema 切换。
- `KafkaSession` 通过统一 `ResultBuilder` 输出表格、影响数量、帮助和失败消息；平台既有链路负责记录命令、行为、执行人、执行结果与耗时。错误消息显式通知审计，以失败状态结束；会话关闭释放客户端并通知关闭监听器。
- Topic 管理命令和 `--help` 已接入 Admin API。消息消费已实现下述有界读取；元数据树浏览已接入，数据源仍保持隐藏，待完整接入验收后开放。

## 管理命令执行

```sh
kafka-topics --list
kafka-topics --describe --topic 'orders.*'
kafka-topics --create --topic orders --partitions 3 --replication-factor 1 --config retention.ms=86400000
kafka-topics --delete --topic 'orders.*' --if-exists
```

- Topic 列表包含内部 Topic；详情分别输出 Topic 概况和分区信息，配置只展示非敏感的 Topic 动态配置。创建时未指定分区数、副本数则使用 Broker 默认值；`--if-not-exists` 只忽略 Topic 已存在错误。
- Topic 删除按正则匹配当前 Topic 列表，展示各 Topic 的 `DELETED`、`NOT_FOUND`、`FAILED` 或 `UNKNOWN` 状态。批量删除不具备原子性，部分失败仍按失败审计，并保留已知结果；`--if-exists` 只忽略 Topic 不存在错误。系统展示条数上限不缩小删除范围。
- 表格复用系统条数、字节、分页和流式输出规则；达到条数上限时停止并提示，超过字节上限时报错并清理结果缓存。管理命令使用系统查询超时作为总期限（未配置则使用客户端 API 超时），每个 Admin 请求同时受客户端 API 超时限制。
- 页面中断取消正在等待的 Admin future，结束当前执行后可复用会话；连接检查也支持中断。超时或中断无法撤销已发送到 Broker 的管理操作，应查询实际状态再决定是否重试。消费中断通过 `Consumer.wakeup()` 唤醒元数据请求或 poll。

## 有界消息消费

```sh
kafka-console-consumer --topic orders --from-beginning --max-messages 10
kafka-console-consumer --topic orders --partition 0 --offset latest --max-messages 10 --timeout-ms 5000
kafka-console-consumer --topic orders
```

- 每次执行手动分配指定分区，或执行开始时 Topic 的全部分区。不订阅、不加入 Consumer Group，不读取或提交组位点，不自动创建 Topic。执行期间新增的分区不会自动加入本次查询。
- `--from-beginning` 从各分区当前保留的最早 offset 开始；默认和 `--offset latest` 从初始化时取得的末尾 offset 等待新消息。跨分区消息按 poll 返回顺序输出，不保证全局时间顺序，也不表示“历史最后 N 条”。
- 未指定 `--max-messages` 时补齐系统最大获取条数，显式值超过系统上限时下调；未配置正数系统上限时必须显式指定正数条数。条数最多为 `Integer.MAX_VALUE`，达到上限立即结束，不额外 poll。
- `--timeout-ms` 沿用 [原生命令](https://github.com/apache/kafka/blob/4.1/tools/src/main/java/org/apache/kafka/tools/consumer/ConsoleConsumer.java) 的“等待下一条消息的空闲超时”，不是整条命令的总耗时。缺省使用系统查询超时（秒转毫秒），系统未配置正数超时时使用客户端 API 超时；显式用户值优先。元数据查询还受客户端 API 超时约束。
- 补齐或调整参数后保留原命令，通过统一重写标记与结果展示实际执行命令。空闲超时正常结束并提示，保留已读结果；认证、元数据超时、位点失效等错误按失败审计，不伪装为空结果。
- 消息结果列为 `TOPIC`、`PARTITION`、`OFFSET`、`TIMESTAMP`（epoch 毫秒，未知为空）、`TIMESTAMP-TYPE`、`KEY`、`VALUE`。Key / Value 按 UTF-8 文本显示，非法 UTF-8 使用替换字符；空字节串与 null（含 tombstone）保持区分。暂不提供自定义反序列化。
- 消费结果通过元数据 `refreshOnProgress` 开启接收期间的分页刷新，收到条数进度后补取当前页已缓存的数据；该能力默认关闭，Topic 管理及其他数据源的分页行为不变。
- 输出复用系统表格、分页、流式和字节限制，不在内存中累积整次消费结果。正常完成、空闲超时、异常、页面中断和会话关闭均释放本次 Consumer；关闭等待最多 5 秒，后续查询创建新 Consumer，避免继承缓冲消息、位置或 wakeup 状态。

## 元数据与对象浏览

- Kafka 没有 Catalog / Schema 层级，查询窗口直接在实例下展示 Topics，列表包含内部 Topic。
- 展开 Topic 展示 ID、内部标记、分区数及每个分区的 leader / replicas / ISR；完整配置通过 describe 命令查询。
- `MqValue` 使用共享 UMI 序列化；Topic 列表、详情使用独立缓存类型。列表刷新重新读取，对象刷新重新获取详情。
- 双击 Topic 填入从头最多 10 条的消费命令；右键“命令模板”包含列表、详情、消费和管理示例，创建、删除示例默认注释，需显式编辑后执行。
- Topic 管理模板使用正则字面量匹配，名称按命令语法转义；元数据和模板查询使用对象读取权限，实际执行进入授权与审计链路。
