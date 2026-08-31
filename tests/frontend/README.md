# Open CDM 前端 Chrome 复测矩阵

本目录保存长期有效、可由 Codex 在 Chrome 中重复执行的用户流程规程。它不是单次测试报告：不记录执行日期、执行人或历史
PASS/FAIL，只维护当前真实入口、数据准备、操作、断言和清理方法。

## 执行 Runbook

1. 根据本次 diff 涉及的页面、路由、接口、权限和状态机，在下方流程索引中选择文档。
2. 完整阅读对应流程文档；步骤与当前页面不一致时，先核对代码并更新文档，不猜测点击。
3. 使用 `chrome:control-chrome` 和用户当前 Chrome 登录状态；所有任务标签页放入已有或新建的 `codex` 标签页组。
4. 仅在本地或用户明确指定的测试环境准备数据。数据名称使用 `codex_<业务>_<本次唯一标识>`，不得依赖上次执行遗留的固定 ID。
5. 先执行 P0 的 Smoke、Main Flow、Permission、State Consistency，再按改动风险选择其余矩阵。
6. 页面导航、刷新或局部重渲染后重新读取页面状态；按可见文案、角色、标签和所属区域定位控件。
7. 执行清理并确认没有遗留运行中任务、临时权限、网络模拟或测试下载文件。
8. 只在最终回复中汇总本次 PASS、FAIL、SKIP；仓库内不生成日期报告。

## 全局测试矩阵

| 套件                     | 优先级 | 触发条件                  | 最低可观察断言                                             | 允许跳过的条件                     |
|------------------------|-----|-----------------------|-----------------------------------------------------|-----------------------------|
| Smoke                  | P0  | 任何用户可见改动              | 入口与路由正确；核心数据、标题和主要操作可见；无相关 Console 错误               | 页面或登录客观不可用，且必须报告 BLOCKED    |
| Main Flow              | P0  | 交互、表单、状态流或接口契约改动      | 标准流程完成；按钮 loading/禁用时机正确；刷新后业务结果保持                  | 没有安全测试数据时可跳过写入步骤，但只读检查不能替代  |
| Boundaries             | P1  | 输入、分页、文件、数值、日期或内容展示改动 | 覆盖真实最小/最大值及外一档；空值、Unicode、特殊字符有明确结果                 | 代码和接口没有该类边界时标记不适用           |
| Extreme                | P1  | 列表、树、长文本、响应式或性能敏感改动   | 0/1/分页边界/大量数据；1440×900、1024×768、390×844 无遮挡或文档级横向溢出 | 大量数据没有安全 fixture 时跳过并保留覆盖缺口 |
| Repeat And Concurrency | P1  | 提交、刷新、轮询、重试或跨标签状态改动   | 双击不重复提交；旧响应不覆盖新状态；多标签最终一致                           | 操作纯只读且没有异步状态时标记不适用          |
| Failure And Recovery   | P1  | 网络请求、异步任务或外部依赖改动      | 400/401/403/500、超时或断网不误报成功；恢复后可重试或刷新                | 无法安全注入指定错误时至少验证断网，并说明未覆盖状态码 |
| Lifecycle              | P1  | 计时器、轮询、弹窗、路由或资源管理改动   | 刷新、前进后退、切页、重新进入后状态正确；离开后资源释放                        | 无页面生命周期资源时标记不适用             |
| Permission             | P0  | 页面或按钮权限、敏感数据、登录处理改动   | 无权限入口不可见或被拒绝；只读角色不能执行写操作；登录失效走统一流程                  | 没有对应测试账号时报告 SKIP，不得用管理员结果替代 |
| State Consistency      | P0  | 终态、重试、部分失败、级联或缓存改动    | 页面各区域与服务端最终事实一致；刷新后不回退；终态释放锁、轮询和 loading            | 无法构造状态时只读历史数据可作补充，不能替代完整推进  |

## 流程索引

| 业务域   | 流程文档                                                                                       | 入口/路由                    | 核心风险                     | 基础必跑套件                                                                        |
|-------|--------------------------------------------------------------------------------------------|--------------------------|--------------------------|-------------------------------------------------------------------------------|
| 工单    | [`ticket/ticket_detail.md`](ticket/ticket_detail.md)                                       | 工单列表 → `/#/ticket/:id`   | 进度、分析明细、SQL 只读展示、权限与自动刷新 | Smoke、Main Flow、Extreme、Lifecycle、Permission、State Consistency                |
| CI/CD | [`cicd/release_flow_change_record_refresh.md`](cicd/release_flow_change_record_refresh.md) | CI/CD 列表 → `/#/cicd/:id` | 变更记录轮询、级联进度、关闭工单后的终态回收   | Smoke、Main Flow、Repeat And Concurrency、Lifecycle、Permission、State Consistency |
| 数据源   | [`datasource/driver_default_selection.md`](datasource/driver_default_selection.md)           | 实例列表 → `/#/datasource/add` | 后端默认驱动契约、家族和版本默认选择        | Smoke、Main Flow、Boundaries、Lifecycle、Permission、State Consistency              |
| 数据源   | [`datasource/kingbasees_datasource.md`](datasource/kingbasees_datasource.md)                 | 实例列表 → SQL 工作台             | 四兼容模式、层级契约、单产品前端与真实执行链路   | Smoke、Main Flow、Failure And Recovery、Lifecycle、Permission、State Consistency     |
| SQL   | [`sql/table_editor_dameng_defaults.md`](sql/table_editor_dameng_defaults.md)                 | SQL 工作台 → 达梦新建表            | 默认值、虚拟列、标识符与插件契约       | Smoke、Main Flow、Boundaries、Lifecycle、Permission、State Consistency               |

新增用户流程时，在对应业务目录创建一份文档，并同步更新本索引。文档结构以 [`TEMPLATE.md`](TEMPLATE.md) 为准。

## 安全边界

- 不读取或记录 Cookie、Token、浏览器密码、Authorization 头或包含凭证的请求体。
- 不在生产环境执行创建、删除、批量修改、文件上传、断网注入或压力类极限测试。
- SQL 写入与级联执行只使用已确认的隔离数据源；优先使用 `SELECT 1;` 等无副作用语句。
- 无法安全构造的数据必须标记 SKIP/BLOCKED，不降低环境安全要求。
