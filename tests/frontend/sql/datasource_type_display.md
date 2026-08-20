# 数据源全类型展示

## Purpose

验证同一批数据库类型在 CloudDM 的对象元数据、SQL 工作台结果和只读表数据浏览三条链路中采用一致、可读且无精度损失的展示，并防止未知类型、长内容、二进制和空间值导致结果集错误或会话异常。

## Scope

- 页面与路由：SQL 工作台 /#/sql、数据源对象树、表结构和“浏览表数据”入口。
- 关联接口与状态：对象元数据加载、SQL 执行 WebSocket、结果分页、单元格详情与完整复制。
- 关联源码：
  - backend/clouddm-utils/cg-schema/src/main/java/com/clougence/adapter/mysql/MySQLTypes.java
  - backend/clouddm-plugins/clouddm-ds/dsc-common-mysql/src/main/java/com/clougence/clouddm/dsfamily/mysql/execute/MyColReader.java
  - backend/clouddm-platform/cgdm-sidecar/src/main/java/com/clougence/clouddm/worker/component/session/storage/RowStorage.java
  - backend/clouddm-platform/cgdm-components/cg-com-results-file/src/main/java/com/clougence/clouddm/component/resultfile/ResultFileReader.java
  - frontend/src/views/sql/components/Result.vue
- 数据资产：tests/dbs/data-types/mysql/。
- 首轮不覆盖：导出文件、生成 INSERT SQL、数据修改、保存、删除、排序和筛选。

## Preconditions

- 目标为本地或明确授权的非生产测试环境。
- DmAloneLauncher 已通过 IDEA 启动并完成健康检查。
- Chrome 已登录具有数据源查看、SQL 查询和只读表浏览权限的测试账号。
- 已将 tests/dbs/data-types/mysql/setup.sql 执行到待测 MySQL 数据源。
- 已执行 verify.sql，并确认长度、HEX、WKT、SRID 和精确数值与 fixture 一致。
- CloudDM 中的数据源连接默认 schema 可选择 cdm_data_types。
- Chrome 中本任务使用或打开的全部标签页位于 codex 标签页组。

## Test Data

| 编号 | 数据说明 | 构造方式 | 稳定标识 | 清理方式 |
|---|---|---|---|---|
| DTD01 | 数值、位和别名 | setup.sql 创建 cdm_dt_numeric、cdm_dt_alias | case_id | cleanup.sql |
| DTD02 | 日期时间 | setup.sql 创建 cdm_dt_temporal，事实查询固定为 UTC | case_id | cleanup.sql |
| DTD03 | 字符、Unicode 和特殊字符 | setup.sql 创建 cdm_dt_character | case_id | cleanup.sql |
| DTD04 | 255/256/257 与 4 MiB 边界文本 | setup.sql 创建 cdm_dt_character_long | case_id | cleanup.sql |
| DTD05 | 二进制和 BLOB | setup.sql 创建 cdm_dt_binary_lob、cdm_dt_binary_long | case_id | cleanup.sql |
| DTD06 | JSON | MySQL 5.7+ 条件创建 cdm_dt_json | case_id | cleanup.sql |
| DTD07 | 全部可实例化空间类型 | setup.sql 创建 cdm_dt_spatial | case_id | cleanup.sql |
| DTD08 | VECTOR | MySQL 9.0+ 条件创建 cdm_dt_vector；仓库必测目标为 9.7 | case_id | cleanup.sql |

默认预览字符数 256、在线单列上限 4 MiB 来自 DmDsUtils.fetchResultLimit 和 fillRequestConfig。超出 4 MiB 一档用于验证受控限制，不要求读取超过配置上限的完整内容。

## Suites

### DTD-SMOKE-01 对象树和表结构加载

- 风险/目的：P0，确认合法类型不会使 schema、表或字段元数据整体加载失败。
- 初始路由与状态：进入 /#/sql，目标 MySQL 数据源在线，尚未展开 cdm_data_types。
- 测试数据：DTD01 至 DTD08 中当前版本适用的表。
- 准备方法：执行 setup.sql 后在对象树刷新目标数据源。
- Chrome 操作：
  1. 通过可见对象树展开目标数据源和 cdm_data_types。
  2. 逐个展开所有 cdm_dt_ 前缀表并进入表结构。
  3. 对照 manifest.yaml 和 verify.sql 的 METADATA 结果检查字段。
  4. 检查相关元数据 HTTP 请求、Console 和后端日志。
- 预期结果：
  1. schema 和每张适用表均可展开，没有整树加载失败或缺表。
  2. 字段名、数据库归一化类型、长度、精度、标度、UNSIGNED、字符集和排序规则与数据库事实一致。
  3. JSON 在 5.6 不出现，VECTOR 在 9.0 前不出现；这是适用性差异而非失败。
  4. 请求业务结果成功，没有相关 4xx/5xx、Console 错误或后端转换异常。
- 恢复/清理：保持 fixture，供后续场景复用。

### DTD-MAIN-01 精确数值、时间与字符查询

- 风险/目的：P0，防止大整数、定点数、零日期、负 TIME、Unicode 和空值被截断、舍入或混淆。
- 初始路由与状态：在 /#/sql 打开目标数据源查询页签，schema 为 cdm_data_types。
- 测试数据：DTD01、DTD02、DTD03。
- 准备方法：确认 verify.sql 的 NUMERIC、ALIASES、TEMPORAL_UTC 和 CHARACTER 结果已通过数据库侧校验。
- Chrome 操作：
  1. 依次执行 SELECT * FROM cdm_dt_numeric ORDER BY FIELD(case_id,'NULL','ZERO','NORMAL','MIN','MAX')。
  2. 执行 SELECT * FROM cdm_dt_alias ORDER BY serial_value。
  3. 先执行 SET time_zone = '+00:00'，再查询 cdm_dt_temporal。
  4. 查询 cdm_dt_character，并查看 NORMAL、UNICODE 和 BOUNDARY 行。
  5. 检查每次执行的 WebSocket 正常终态、结果行列数、相关请求、Console 和后端日志。
- 预期结果：
  1. BIGINT UNSIGNED 18446744073709551615、负 BIGINT 超过 JavaScript 安全整数的值和 DECIMAL(65,30) 与 verify.sql 文本完全一致。
  2. FLOAT/DOUBLE 按数据库返回的近似值展示，不使用定点数预期强行比较。
  3. 日期时间包含 fsp 0/6，负 TIME 和 UTC TIMESTAMP 均与数据库事实一致。
  4. NULL 显示为专用 NULL 样式，空串显示为空单元格，数字 0 显示为 0，三者不混淆。
  5. 中文、emoji、组合字符、引号和换行无乱码；CHAR 尾随空格按数据库归一化事实验收。
  6. 不出现 Unsupported、undefined、[object Object] 或相关异常。
- 恢复/清理：关闭本场景新增结果页签。

### DTD-MAIN-02 二进制、JSON、空间与向量查询

- 风险/目的：P0/P1，确认非普通字符串类型具有稳定可读表示。
- 初始路由与状态：同 DTD-MAIN-01。
- 测试数据：DTD05、DTD06、DTD07、DTD08。
- 准备方法：从 manifest.yaml 确认当前 MySQL 版本的适用类型。
- Chrome 操作：
  1. 查询 cdm_dt_binary_lob 的 NULL、EMPTY、BYTES、BOUNDARY_256 和 BOUNDARY_257 行。
  2. 若适用，查询 cdm_dt_json 全部行。
  3. 查询 cdm_dt_spatial 全部行，不在 SELECT 中主动调用 ST_AsText。
  4. 若适用，直接执行 SELECT * FROM cdm_dt_vector ORDER BY case_id。
  5. 对关键单元格打开详情并使用复制按钮；与 verify.sql 的 HEX、JSON、WKT、SRID 或 VECTOR_TO_STRING 数值事实比较。
  6. 检查 WebSocket 终态、请求、Console 和后端日志。
- 预期结果：
  1. 二进制以稳定十六进制文本展示；X'' 与 NULL 可区分，00/FF 字节不产生乱码。
  2. JSON 的 SQL NULL、JSON null、标量、对象、数组、深层对象、Unicode 和长对象可区分且为有效 JSON 文本。
  3. GEOMETRY、POINT、LINESTRING、POLYGON、MULTIPOINT、MULTILINESTRING、MULTIPOLYGON 和两种集合声明均显示合法 WKT；非零地理 SRS 的坐标轴顺序与默认 ST_AsText 一致，不出现原始二进制、替换字符或 WKB Error。
  4. VECTOR 显示为与 VECTOR_TO_STRING 数值等价的数组文本；Unsupported 不能作为通过结果。
  5. 任一列问题不得中断同一结果集的其他列或 SQL 会话。
- 恢复/清理：关闭本场景新增结果页签。

### DTD-BOUNDARY-01 NULL、空值、零值和别名

- 风险/目的：P1，防止视觉相同但语义不同的数据被混为一类。
- 初始路由与状态：DTD-MAIN-01 查询结果可重新执行。
- 测试数据：DTD01、DTD02、DTD03、DTD05。
- 准备方法：无额外准备。
- Chrome 操作：
  1. 在同一结果中逐列比较 NULL、ZERO 或 EMPTY 行。
  2. 比较 BOOL/BOOLEAN、INT/INTEGER、DEC/NUMERIC/FIXED、DOUBLE PRECISION/REAL 和 SERIAL 的元数据及值。
  3. 打开空字符串、空字节串和 NULL 的单元格详情。
- 预期结果：
  1. NULL 使用专用 NULL 样式；空串、空 SET、空字节串和数值 0 不显示为 NULL。
  2. 别名按数据库规范化后的元数据展示，值与对应规范类型一致。
  3. BINARY(8) 的空输入按数据库零字节填充事实展示，不误判为空 VARBINARY。
- 恢复/清理：关闭详情弹窗。

### DTD-BOUNDARY-02 255、256、257 预览边界

- 风险/目的：P1，确认默认 256 字符预览的边界标记、详情和完整复制正确。
- 初始路由与状态：同 DTD-MAIN-01。
- 测试数据：DTD04、DTD05。
- 准备方法：查询 cdm_dt_character_long 中前三行和 cdm_dt_binary_long 中前三行。
- Chrome 操作：
  1. 比较 255、256、257 三行的单元格内容与截断角标。
  2. 双击 257 单元格打开详情。
  3. 使用单元格复制按钮复制 257 内容，并在非敏感临时编辑器中仅核对长度、首字符和末字符。
  4. 对文本和二进制各执行一次。
- 预期结果：
  1. 255 和 256 不显示不完整角标；257 显示角标。
  2. 257 的详情可取得完整内容，复制长度与数据库事实一致。
  3. 二进制预览和完整内容都保持十六进制契约，字节数与十六进制字符数的换算一致。
  4. 没有空 chunk、重复 chunk、错行或错列。
- 恢复/清理：关闭详情弹窗，清空临时编辑器内容。

### DTD-EXTREME-01 1 MiB 与在线单列上限

- 风险/目的：P0/P1，验证流式读取阈值和 4 MiB 配置上限两侧的受控行为。
- 初始路由与状态：同 DTD-MAIN-01，浏览器和后端日志均已记录测试前基线。
- 测试数据：DTD04、DTD05 的 ONE_MIB、LIMIT_MINUS_ONE、LIMIT_EXACT、LIMIT_PLUS_ONE。
- 准备方法：每次只查询一行一列，避免同一结果集累计大小干扰。
- Chrome 操作：
  1. 分别查询 1 MiB、4194303 和 4194304 字节的文本与二进制单元格。
  2. 对每个结果打开详情并获取完整内容，核对长度或 SHA-256，不把完整内容粘贴到聊天或日志。
  3. 单独查询 4194305 字节一档。
  4. 检查页面消息、WebSocket 终态、相关请求、Console 和后端日志。
- 预期结果：
  1. 1 MiB、4194303 和 4194304 字节在配置允许范围内可完成查询，预览有截断标记且详情可读取完整内容。
  2. 4194305 字节触发明确、受控的单列上限结果或错误，不使会话断开、不误报成功、不留下永久 loading。
  3. 后续执行 SELECT 1 AS recovery_probe 成功，证明会话可恢复。
- 恢复/清理：关闭长内容详情和结果页签。

### DTD-REPEAT-01 重复执行与结果页签切换

- 风险/目的：P1，防止异步结果错位或旧响应覆盖新结果。
- 初始路由与状态：保留一个数值结果和一个空间结果。
- 测试数据：DTD01、DTD07。
- 准备方法：无额外准备。
- Chrome 操作：
  1. 连续重新执行同一只读查询两次，等待每次正常终态。
  2. 在数值和空间结果页签间往返切换。
  3. 快速切换后打开固定 case_id 的单元格详情。
- 预期结果：
  1. 每次只生成一个完整终态结果，不出现永久 loading 或重复行。
  2. 页签列定义和值不串台；详情 resultId、行和列对应当前可见单元格。
  3. 无旧响应覆盖新结果或 WebSocket 重连风暴。
- 恢复/清理：关闭新增结果页签。

### DTD-FAILURE-01 不支持类型和限制后的恢复

- 风险/目的：P0，确保类型读取失败被定位到单元格且不破坏执行通道。
- 初始路由与状态：存在当前版本适用但 CloudDM 暂未支持的 fixture，或已执行 DTD-EXTREME-01 超限一档。
- 测试数据：使用未来 manifest 新增但尚未支持的类型；当前基线没有此类类型时，用 LIMIT_PLUS_ONE 验证受控失败。
- 准备方法：不得伪造生产类型或修改线上数据。
- Chrome 操作：
  1. 执行风险查询并等待明确终态。
  2. 记录可见错误，不记录认证头、请求体或含 token 的完整 WebSocket URL。
  3. 紧接着执行 SELECT 1 AS recovery_probe。
- 预期结果：
  1. 风险查询不导致 SQL 会话、页面或后端进程崩溃。
  2. 错误可关联具体类型或配置上限，不显示 undefined 或无限 loading。
  3. recovery_probe 返回 1；相关资源正常释放。
- 恢复/清理：关闭失败结果与恢复探针结果。

### DTD-LIFECYCLE-01 分页、刷新与重新进入

- 风险/目的：P1，确认同一 fixture 在结果生命周期变化后仍保持一致。
- 初始路由与状态：已完成 DTD-MAIN-01 和 DTD-MAIN-02。
- 测试数据：所有当前版本适用 fixture。
- 准备方法：选择一张有五行以上的表。
- Chrome 操作：
  1. 切换结果分页、结果页签和执行信息页签后返回。
  2. 刷新页面，重新从对象树进入同一表并执行同一查询。
  3. 从对象树使用“浏览表数据”进入只读浏览，再退出并重新进入。
- 预期结果：
  1. 切换和重新执行后的行列、类型和值一致。
  2. 表数据浏览与 SQL 工作台对同一 case_id 的展示契约一致。
  3. 刷新后无过期 resultId 请求、额外失败重试或相关 Console 错误。
- 恢复/清理：关闭测试查询页签和表浏览页签。

### DTD-PERMISSION-01 只读账号边界

- 风险/目的：P0，确认首轮验收不依赖写权限且只读入口不会暴露可执行写操作。
- 初始路由与状态：Chrome 使用只有 schema 查看和 SELECT 权限的测试账号。
- 测试数据：现有 fixture。
- 准备方法：由具备授权的准备账号预先执行 setup.sql；浏览器账号不执行 setup 或 cleanup。
- Chrome 操作：
  1. 执行 DTD-SMOKE-01 与一条 SELECT 查询。
  2. 从对象树进入只读表浏览。
  3. 观察写入、保存和删除入口的权限状态，但不尝试绕过 UI。
- 预期结果：
  1. 元数据、SELECT 和只读浏览可用。
  2. 无权限写入口隐藏或禁用；页面不要求授予额外写权限。
  3. 相关请求无意外 403；不存在越权写请求。
- 恢复/清理：退出只读浏览。

### DTD-STATE-01 三链路与数据库事实一致

- 风险/目的：P0，防止某一页面单独看似正确但与数据库或另一条产品链路不一致。
- 初始路由与状态：数据库 verify.sql、对象元数据、SQL 结果和表浏览均已取得当前状态。
- 测试数据：每个适用类型至少一个 NORMAL 或边界 case_id。
- 准备方法：使用 manifest.yaml 逐项勾选，不按肉眼印象抽样。
- Chrome 操作：
  1. 对照 verify.sql 检查对象元数据。
  2. 对照相同 case_id 检查 SQL 结果与表浏览。
  3. 检查最终 HTTP、WebSocket、Console 和后端日志。
- 预期结果：
  1. 每个适用类型在三条链路都有结论，无未说明缺项。
  2. 数据库事实、SQL 结果和表浏览语义一致。
  3. P0/P1 全部关闭后才能给出整体 PASS；任何必测版本 BLOCKED 时不得整体 PASS。
- 恢复/清理：进入 Cleanup。

## Cleanup

1. 关闭本流程创建的结果页签、查询页签、详情弹窗和表浏览页签。
2. 由具备授权的准备账号执行 tests/dbs/data-types/mysql/cleanup.sql。
3. 刷新对象树，确认只移除了 cdm_data_types。
4. 恢复 Chrome 原视口、缩放、网络条件和测试账号。
5. 确认没有遗留执行中 SQL、轮询或 WebSocket 重连。

## Skip Conditions

- Chrome 未登录、账号无查询权限或目标数据源未配置时，浏览器验收为 BLOCKED；不得用 curl 或其他浏览器替代。
- MySQL 5.6 跳过 JSON，MySQL 9.0 前跳过 VECTOR；manifest 已定义的版本差异不计为失败。
- max_allowed_packet 不大于 4194305 时跳过 LIMIT_PLUS_ONE；verify.sql 必须明确报告该环境约束，且 4 MiB 整数边界仍需执行。
- 只有仓库明确停止支持的版本才可标为不支持；没有环境时标为 BLOCKED，不得标为 PASS 或无说明 SKIP。
- 无只读账号时 DTD-PERMISSION-01 可标记 SKIP，但管理员账号结果不能替代权限覆盖。
- 无法安全获取超过 1 MiB 内容的摘要时可跳过粘贴或导出，只保留详情长度与数据库摘要核对；不得把敏感或超长内容写入报告。
- Repeat And Concurrency 只覆盖只读重复执行和页签切换；写入双击与多用户并发不在首轮范围。
