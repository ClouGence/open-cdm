# CockroachDB 数据源添加

## Purpose

验证 CockroachDB 作为独立数据源类型出现在类型选择器中，新增页使用 PostgreSQL JDBC 默认驱动、端口 26257、默认库 `defaultdb`，并展示 schema 对象树所需的分组能力。

## Scope

- 页面与路由：实例列表 `/#/datasource`、新增数据源 `/#/datasource/add?dsType=CockroachDB`。
- 入口：实例列表“新增”按钮和数据源类型选择弹窗。
- 关联接口与状态：全局设置 `dsSupportNames`、`dsSettingDef.CockroachDB`。
- 关联源码：`frontend/src/components/function/CustomIcon.vue`、`frontend/src/utils/index.js`、`backend/clouddm-plugins/clouddm-ds/ds-cockroachdb/`。
- 不覆盖：真实 CockroachDB 连接测试、保存实例、SQL 查询结果内容和分区表向导。

## Preconditions

- 本地 Alone 可通过 `http://localhost:8222` 访问，或前端 `npm run serve:dm` 代理到该后端。
- 使用已登录且拥有实例查看、新增权限的测试账号。
- 后端已加载 `ds-cockroachdb` 插件。
- 本流程默认不提交新增数据源。若执行了连接测试或保存，必须在 Cleanup 中删除。

## Test Data

| 编号 | 数据说明 | 构造方式 | 唯一标识 | 清理方式 |
|---|---|---|---|---|
| CRDB01 | CockroachDB 类型选择 | 类型选择弹窗中选择 CockroachDB | 不适用 | 无需清理 |
| CRDB02 | 默认连接字段 | 进入新增页后查看端口、Catalog、驱动家族 | 不适用 | 关闭新增页即可 |

## Suites

### CRDB-SMOKE-01 类型选择器可见

- 风险/目的：P0，确认 CockroachDB 出现在添加数据源类型列表并带有图标。
- 初始路由与状态：已登录，位于 `/#/datasource`。
- 测试数据：CRDB01。
- Chrome 操作：点击“新增”，在类型选择弹窗中找到 CockroachDB。
- 预期结果：类型名显示为 CockroachDB；图标可见且不是空白占位。
- 恢复/清理：关闭类型选择弹窗。

### CRDB-MAIN-01 新增页默认值

- 风险/目的：P0，确认默认端口、默认库和 PG JDBC 驱动家族。
- 初始路由与状态：从类型选择确认后进入 `/#/datasource/add?dsType=CockroachDB`。
- 测试数据：CRDB02。
- Chrome 操作：查看端口、Catalog/默认库、驱动家族与 SSL 选项。
- 预期结果：端口为 `26257`；默认库为 `defaultdb`；驱动家族为 PostgreSQL JDBC；SSL 模式可选 TRUST / CA / CLIENT_CERT。
- 恢复/清理：离开新增页，不保存。

## Cleanup

1. 不保存未完成的新增数据源。
2. 若误保存，删除该 CockroachDB 实例。

## Skip Conditions

- 后端未加载 `ds-cockroachdb` 插件时跳过，并记录覆盖缺口。
