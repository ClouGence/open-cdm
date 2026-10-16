# Kafka Topic 对象浏览

## Purpose

验证 Kafka 无 Catalog / Schema 的 Topic 浏览、独立授权、刷新和命令模板，防止已移除的 Consumer Group 功能再次暴露。

## Scope

- 路由：`/#/sql`，数据源树、对象列表、对象详情、编辑器与查询执行链路。
- 接口：`/browse/listLeaf`、`/browse/rdbObjectDetail`、`/browse/actions/requestScript`。
- 常规浏览场景仅生成管理模板；KB-AUTH-02 只允许在隔离环境对可丢弃对象验证删除拒绝。

## Preconditions

- @Browser 可访问本地 CloudDM（默认 `http://127.0.0.1:8222/#/sql`），由用户完成登录。
- Console 与 Worker 加载本次 Kafka 插件和共享 UMI / SDK；存在 Kafka 测试数据源。添加入口未开放时由环境预先配置，不绕过浏览器入口或权限。
- 准备查询控制台账号：实例读取账号、仅有一个 Topic 读取／管理权限的账号、仅实例管理账号。
- 测试对象由环境维护者建立；使用隔离 Kafka。

## Test Data

- 使用 `cdm_browser_<唯一标识>` 前缀，准备至少两个 Topic，其中 `<前缀>.orders` 包含点号、至少两个分区，并准备长名称对象。
- 准备空测试实例、不可连接实例；记录对象名称和原授权，不记录凭据。

## Suites

### KAFKA-BROWSE-01 — 无 Schema 的列表与详情（P0）

- 初始状态：`/#/sql`，实例读取账号，目标实例尚未打开。
- 操作：双击实例，选择 Topics，展开点号 Topic 和元数据分组；关闭后重新打开、刷新页面。
- 预期：只展示 Topics，不出现 Consumer Groups、Catalog、Schema 分类或 Schema 切换框；详情显示 ID、内部标记、分区数和 leader／replicas／ISR；重新打开后对象与详情仍对应正确 Topic。
- 恢复：关闭查询窗口。

### KAFKA-BROWSE-02 — 刷新、空态与失败恢复（P0）

- 初始状态：`/#/sql`，正常查询窗口中已展开目标对象。
- 操作：维护者增加测试 Topic 分区；右键刷新对象、刷新列表，快速重复刷新并切换另一测试实例。打开空实例和不可连接实例，恢复连接后重试。
- 预期：对象刷新后分区数更新；列表请求重新发出；空实例显示空列表；失败显示错误并结束加载；恢复后可重试。旧响应不会覆盖另一实例的数据。
- 恢复：恢复连接配置，关闭窗口，维护者清理测试 Topic。

### KAFKA-BROWSE-03 — 快捷命令、模板和语言服务（P0）

- 初始状态：`/#/sql`，读取账号，编辑器为空，点号 Topic 已加载。
- 操作：双击 Topic，右键选择“命令模板”，检查编辑器内容和命令补全；英文界面重复菜单检查。输入并尝试运行 `kafka-consumer-groups --list`、`kafka-console-consumer --topic <测试Topic> --group g`。
- 预期：快捷命令带确切 Topic 和 `--from-beginning --max-messages 10`；模板包含 list、describe、消费和注释状态的创建／删除；describe/delete 使用字面量正则。补全只有 kafka-topics、kafka-console-consumer；旧 Group 命令和 --group 参数报错且不执行。中英文菜单完整，生成模板不会自动执行。
- 恢复：清空编辑器，关闭窗口。

### KAFKA-BROWSE-04 — 权限边界（P0）

- 初始状态：`/#/sql`，由用户分别切换实例读取、Topic 读取和仅管理账号。
- 操作：浏览列表、展开详情、生成模板，尝试消费允许和未允许的 Topic。
- 预期：实例读取可查看全部 Topic；Topic 读取只显示获准对象，其他详情／模板／消费被拒绝；管理不隐含读取。
- 恢复：恢复原账号和授权。

### KAFKA-BROWSE-05 — 长名称与布局（P1）

- 初始状态：`/#/sql`，使用长名称 Topic 和多个分区。
- 操作：在桌面和工具支持的窄视口查看列表、详情和右键菜单，悬停长文本。
- 预期：刷新可点击，长名称可通过省略／提示查看；详情不遮挡编辑器或控件；无额外卡片或固定页脚。
- 恢复：恢复视口，关闭窗口。

### KB-AUTH-02 — Topic 范围与正则管理命令（P0）

- 初始状态：`/#/sql`，隔离实例，子账号仅有测试 Topic a 的读取／管理权限。
- 数据准备：维护者建立可丢弃的 `codex_<唯一标识>_a`、`codex_<唯一标识>_b`；b 无授权，删除试验只能匹配这两个对象。
- 操作：执行 list，查看对象树和补全；分别读取 a、b。执行匹配两者的 `kafka-topics --delete --topic 'codex_<唯一标识>_[ab]'`，管理员刷新确认两者存在。尝试创建 Topic；改为实例管理授权后再验证创建。
- 预期：仅 a 可见且可读取；批量删除整条拒绝，a、b 均未删除；Topic 管理不能创建，实例管理可以创建；审计记录实际 Topic 路径，实例管理不隐含读取。
- 清理：恢复授权，维护者确认实际对象状态后清理本次 Topic 和消息。

## Cleanup

关闭窗口、清空模板，由维护者清理本次唯一前缀的 Topic，恢复连接和权限；不删除其他对象。

## Skip Conditions

- 服务不可达、未登录、未配置 Kafka 或缺少测试账号时，依赖场景标记 BLOCKED，不切换其他浏览器绕过登录。
- 工具不支持视口或网络模拟时，相应项标记 SKIP。
- 模块编译、现有测试、临时 Admin 模拟、前端构建和 ESLint 只能补充验证，不能替代真实浏览器或 Kafka 联调。
