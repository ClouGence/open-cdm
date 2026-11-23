# 机器列表复测

## Purpose

验证同一个机器列表入口能查看 Console 和 Sidecar，避免两类实例混在同一张表、旧工作实例链接失效或越权访问。

## Scope

覆盖 `/#/data-access/cluster`、工作实例分组链接和原有集群管理入口，涉及 `listConsoleInstances`、`listByCondition` 和 `listWorkers`。Sidecar 的安装和配置变更另行验证。

## Preconditions

使用已初始化、可从 @Browser 访问的隔离环境。准备有 `DM_WORKER_READ` 的账号和没有该权限的账号。有权限验证包含只读账号和有 `DM_WORKER_MANAGE` 的账号。

## Test Data

准备一台就绪 Console 和两个带本次唯一前缀的工作集群。第一个集群至少有一台 Sidecar，第二个集群无 Sidecar。离线实例验证可停止本次专用的另一台 Console，等待其租约过期。保留一台就绪实例供浏览器访问。

## Suites

### MACHINE-01 Smoke / Main Flow / Lifecycle

- P0。从已登录工作台点击“数据访问 → 机器列表”，确认 URL 为 `/#/data-access/cluster`，默认选中“工作集群”。切换到“控制集群”，确认 URL 带 `tab=console`，表格包含 Console 实例 ID、通信地址和状态。刷新后保持在“控制集群”。
- 切回“工作集群”，点击某个集群行的“机器列表”，确认原有 `/#/data-access/cluster/list/<clusterId>` 路径仍打开对应集群的 Sidecar 列表。返回后仍能切换两个页签。
- 恢复时关闭本轮测试标签页。

### MACHINE-02 Boundaries / Extreme / Repeat And Concurrency / State Consistency

- P1。在限速的隔离环境进入“控制集群”，分别在首次加载和主动点击“查询”时检查按钮。请求进行中按钮禁用、表格显示加载状态，按钮无加载图标，加载前后宽度相同；请求结束后恢复可点击。
- P1。从工作集群分别进入有 Sidecar 的集群和空集群，确认工作实例表格显示对应数据或空态。快速切换两个页签，确认最终表格属于最后选中的页签。
- 在 1440×900、1024×768、390×844 下检查页签和操作按钮可用，宽表格只在表格内容区横向滚动。恢复原视口。

### MACHINE-03 Failure And Recovery / Permission

- P0。用只读账号看两个页签，工作实例没有“添加机器”。无权限账号看不到入口，直接访问主路由和旧工作实例路由均离开目标页。恢复原登录。
- P1。仅停止本轮专用的 Console，等待租约过期后在“控制集群”点“查询”，确认该实例显示“离线”，其他实例正常。恢复被停节点，再次查询并看到同一个实例 ID 回到“就绪”。

## Cleanup

恢复临时停止的 Console、初始浏览器视口和登录身份，通过正常管理入口清理本轮专用集群与 Sidecar。

## Skip Conditions

环境待初始化、待升级或未能在 @Browser 登入时，页面流程受阻。只有一台 Console 时跳过停机状态验证。没有现成只读和无权限账号时，权限场景标记为未覆盖。
