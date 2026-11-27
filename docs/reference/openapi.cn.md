# CloudDM OpenAPI（V1）

## 接入与鉴权

### 子账号凭据

由具备凭据管理权限的管理员在 CloudDM 控制台为指定子账号分配 OpenAPI 凭据 `AccessKeyId`（AK）和 `SecretKey`（SK）。管理员可以重置其管理范围内子账号的 AK/SK。重置成功后旧凭据失效，调用方使用新凭据签名。子账号停用后，其凭据无法通过鉴权。

### 功能与资源权限

验签成功后，CloudDM 以 AK 绑定的子账号作为调用身份，按该子账号当前生效的功能权限和资源权限校验请求。

- **功能权限**。检查子账号的角色是否允许所请求的操作，例如数据源管理、执行数据操作或账号管理。
- **资源权限**。检查子账号对数据源实例、schema、表或列的访问权限。

列表按调用子账号的可见范围返回记录。管理员调整角色或资源授权后，后续请求按调整后的权限执行，调用方可继续使用该子账号的 AK/SK。

### 请求方式与公共请求头

所有接口使用 `POST`，路径前缀为 `/api/open/v1`。将业务参数放入 UTF-8 JSON 请求体，无业务参数时发送 `{}`。

| 公共请求头 | 必填 | 说明 |
| --- | --- | --- |
| `Content-Type` | 是 | `application/json` |
| `AccessKeyId` | 是 | 管理员为调用子账号分配的凭据标识 |
| `SignatureMethod` | 是 | 固定为 `HmacSHA256` |
| `Timestamp` | 是 | 发起请求时的 UTC 时间，格式为 `YYYY-MM-DDTHH:mm:ssZ` |
| `SignatureNonce` | 是 | 每次请求生成的唯一随机值，可使用 UUID |
| `Signature` | 是 | 按下文计算的 Base64 签名 |
| `Idempotency-Key` | 管理接口的写操作必填 | 客户端为每次管理写操作生成的唯一键，长度为 1 至 128 个可见 ASCII 字符。重试同一操作时沿用此键和原请求内容，更新时间戳、随机值和签名 |

### 计算签名

按顺序用换行符 LF（`\n`）连接以下各项，以请求体摘要的最后一位结束。使用 SK 的 UTF-8 字节对待签名字符串做 HMAC-SHA256，并将签名结果编码为标准 Base64。

1. HTTP 方法 `POST`。
2. 本文列出的完整接口路径，从 `/api/open/v1/` 开始。
3. URL 中 `?` 后的查询字符串，无查询字符串时填空字符串。
4. `AccessKeyId`。
5. `SignatureMethod`。
6. `Timestamp`。
7. `SignatureNonce`。
8. `Idempotency-Key`，请求未携带时填空字符串。
9. 实际发送的请求体字节的 SHA-256 摘要，使用小写十六进制。

签名与发送使用同一份请求体字节。JSON 中的空格、换行或字段顺序变化都会改变摘要。

### Python 调用示例

用实例访问地址和获授权子账号的 AK/SK 设置 `CLOUDDM_BASE_URL`、`CLOUDDM_ACCESS_KEY_ID` 和 `CLOUDDM_SECRET_KEY` 后，可调用数据源类型列表接口。

```python
import base64
import hashlib
import hmac
import os
import uuid
from datetime import datetime, timezone
from urllib.parse import urlsplit
from urllib.request import Request, urlopen

base_url = os.environ["CLOUDDM_BASE_URL"].rstrip("/")
access_key_id = os.environ["CLOUDDM_ACCESS_KEY_ID"]
secret_key = os.environ["CLOUDDM_SECRET_KEY"]
path = "/api/open/v1/datasource/listDataSourceTypes"
url = base_url + path
target = urlsplit(url)
body = b"{}"
timestamp = datetime.now(timezone.utc).strftime("%Y-%m-%dT%H:%M:%SZ")
nonce = str(uuid.uuid4())
method = "POST"
signature_method = "HmacSHA256"
idempotency_key = ""

string_to_sign = "\n".join([
    method,
    path,
    target.query,
    access_key_id,
    signature_method,
    timestamp,
    nonce,
    idempotency_key,
    hashlib.sha256(body).hexdigest(),
])
signature = base64.b64encode(
    hmac.digest(secret_key.encode("utf-8"), string_to_sign.encode("utf-8"), "sha256")
).decode("ascii")

headers = {
    "Content-Type": "application/json",
    "AccessKeyId": access_key_id,
    "SignatureMethod": signature_method,
    "Timestamp": timestamp,
    "SignatureNonce": nonce,
    "Signature": signature,
}
if idempotency_key:
    headers["Idempotency-Key"] = idempotency_key

request = Request(url, data=body, headers=headers, method=method)
with urlopen(request, timeout=30) as response:
    print(response.read().decode("utf-8"))
```

## 公共请求与响应

### 分页参数

分页接口在 JSON 请求体中传入以下字段。

| 参数 | 类型 | 必填 | 取值 | 示例值 |
| --- | --- | --- | --- | --- |
| `pageNumber` | 整数 | 是 | 从 `1` 开始 | `1` |
| `pageSize` | 整数 | 是 | `1` 至 `100` | `20` |

以下请求体用于 `POST /api/open/v1/environment/listEnvironments`，请求第一页，每页最多 20 条记录。

```json
{
  "pageNumber": 1,
  "pageSize": 20
}
```

请求缺少分页参数或取值超出范围时，返回 HTTP 400 和 `INVALID_ARGUMENT`。响应 `data.items` 为本页记录，`data.total` 为当前授权和筛选条件下的记录总数。超过最后一页时，`items` 为空数组。

### 响应格式

| 响应字段 | 类型 | 说明 |
| --- | --- | --- |
| `requestId` | 字符串 | 请求标识，用于定位调用记录 |
| `code` | 字符串 | 业务结果码，成功时为 `OK` |
| `msg` | 字符串 | 可读的结果说明 |
| `data` | 对象、数组或 `null` | 各接口的结果数据 |

客户端使用 HTTP 状态和 `code` 处理结果。数据源和账号管理的写操作成功后，幂等记录保留 24 小时。重试会重新校验子账号的身份和权限。同一子账号在同一接口使用原幂等键、原查询字符串和原请求体重试，返回首次成功的 HTTP 状态和响应。查询字符串或请求体发生变化时返回 HTTP 409 和 `STATE_CONFLICT`。

| HTTP 状态 | 业务码 | 含义 |
| --- | --- | --- |
| 200 | `OK` | 同步操作成功 |
| 201 | `OK` | 已创建数据源或子账号，返回其 ID |
| 400 | `INVALID_ARGUMENT` | 参数格式或必填项不符合接口约定 |
| 401 | `UNAUTHENTICATED` | 子账号已停用、凭据或签名无效、请求已过期或随机数重复 |
| 403 | `FORBIDDEN` | 子账号缺少所需的功能权限或资源权限 |
| 404 | `NOT_FOUND` | 目标不存在或对调用方不可见 |
| 409 | `STATE_CONFLICT` | 账号重复、数据源仍有业务依赖，或幂等键已用于不同请求 |
| 422 | `SQL_RULE_BLOCKED` | SQL 被 CloudDM 的规则阻止 |
| 422 | `SQL_EXECUTION_FAILED` | 目标数据库执行 SQL 失败 |
| 429 | `RATE_LIMITED` | 达到调用频率或并发限制 |
| 500 | `INTERNAL_ERROR` | 服务处理失败，可凭 `requestId` 定位 |
| 504 | `EXECUTION_TIMEOUT` | 超过 SQL 执行时限 |

## 接口目录

接口表中的路径相对于 `/api/open/v1`。标记为写操作的管理接口需携带 `Idempotency-Key`。

### 数据源管理

| 接口 | 请求参数 | 响应和用途 |
| --- | --- | --- |
| `/environment/listEnvironments` | `pageNumber`、`pageSize` | 返回可见环境的 `envId` 和 `envName` |
| `/cluster/listClusters` | `pageNumber`、`pageSize` | 返回调用方可用于登记数据源的执行集群，包含 `clusterId`、`clusterName`、`clusterDesc` 和运行中的 Worker 数 `runningCount` |
| `/datasource/listDataSourceTypes` | `{}` | 返回部署支持的数据源类型、可用操作及命名空间层级 |
| `/datasource/getDataSourceConfigSpec` | `dataSourceType`，可选 `clusterId` | 返回该类型支持的连接字段、必填项和认证方式。传入 `clusterId` 时，可查看该集群可用的驱动配置 |
| `/datasource/listDataSources` | `pageNumber`、`pageSize`，可选 `envId`、`dataSourceType` | 返回可见的数据源登记，包含 `dataSourceId`、`instanceId`、`instanceDesc`、`dataSourceType`、`envId` 和 `lifeCycleState` |
| `/datasource/getDataSource` | `dataSourceId` | 返回数据源摘要和可用的版本信息。具有管理权限的调用方可获取连接地址 |
| `/datasource/createDataSource` | `dataSourceType`、`envId`、`clusterId`、`instanceDesc`、`host`，以及适用的 `port`、`securityType`、`userName` 和 `password` | 写操作。登记数据源，返回 `dataSourceId`、`instanceId` 和 `lifeCycleState` |
| `/datasource/updateDataSource` | `dataSourceId`，以及要修改的 `instanceDesc`、`envId`、`clusterId`、`host`、`port`、`securityType`、`userName` 或 `password` | 写操作。更新获准的字段，返回 `dataSourceId`，登记时选择的数据源类型保持固定 |
| `/datasource/testConnection` | `dataSourceId` | 用已登记的配置测试连通性。返回布尔值 `success`，成功时返回 `version`，失败时返回原因码 `CONNECTION_FAILED` 或 `CLUSTER_UNAVAILABLE` |
| `/datasource/deleteDataSource` | `dataSourceId` | 写操作。注销 CloudDM 中的登记，返回 `dataSourceId` 和 `lifeCycleState`。存在活动业务依赖时返回 `STATE_CONFLICT` |

`listDataSourceTypes` 的 `operations` 列出各类型支持的管理操作。登记和连接字段更新支持 MySQL 和 PostgreSQL。其他已登记类型可更新显示名、环境和集群。

`getDataSourceConfigSpec` 返回连接字段和必填项。认证方式对应的必填字段见 `requiredFieldsBySecurityType`。传入 `clusterId` 时，`defaultDriver.available` 表示默认驱动在所选集群的可用状态。

| 响应字段 | 取值 |
| --- | --- |
| `lifeCycleState` | `CREATING`（登记中）、`CREATED`（已登记）、`DELETING`（注销中）、`DELETED`（已注销）、`LOCKED`（已锁定） |

### 数据源库表发现

| 接口 | 请求参数 | 响应和用途 |
| --- | --- | --- |
| `/metadata/listNamespaces` | `dataSourceId`，可选父级 `namespacePath` | 列出可见的数据库、Catalog 或 Schema，返回名称、层级类型和对应的 `namespacePath` |
| `/metadata/listTables` | `dataSourceId`、`namespacePath`、`pageNumber`、`pageSize`，可选 `objectType` | 列出有权浏览的表或视图，返回 `tableName`、`objectType` 和可用的注释。`objectType` 可取 `TABLE` 或 `VIEW` |
| `/metadata/getTable` | `dataSourceId`、`namespacePath`、`objectType`、`tableName` | 返回可见列的定义，以及仅由可见列组成的主键和索引 |

`namespacePath` 是相对数据源的有序名称数组。省略父级路径或传入 `[]` 可从根层级浏览。获取路径后原样传给库表浏览和执行数据操作的接口。MySQL 的路径是数据库名，例如 `["sales"]`。PostgreSQL 的路径依次为数据库名和 Schema 名，例如 `["sales", "public"]`。

### 数据操作

| 接口 | 请求参数 | 响应和用途 |
| --- | --- | --- |
| `/data/executeOperation` | `dataSourceId`、`namespacePath`、`sql`，可选 `maxRows` | 同步执行一条 SQL，支持数据查询、数据写入和结构变更。按数据库返回顺序在 `data.results` 中返回执行结果 |

CloudDM 在执行前校验子账号的功能权限，以及该 SQL 实际操作的资源及权限。查询、写入和结构变更分别校验所需的操作权限。任一权限不足时返回 HTTP 403 和 `FORBIDDEN`。执行同时遵循 CloudDM 的 SQL 规则和脱敏策略。

执行时传入完整的命名空间路径。路径各段支持字母、数字、下划线、连字符和 `$`。每次请求在独立的自动提交会话中执行一条 SQL。切换数据库、Schema 或身份以及控制事务的语句返回 `INVALID_ARGUMENT`。

| `results` 元素的 `type` | 字段 | 说明 |
| --- | --- | --- |
| `RESULT_SET` | `columns`、`rows`、`truncated` | 列定义、结果行和截断标记。每行的值按列顺序排列 |
| `UPDATE_COUNT` | `affectedRows` | 数据库报告的影响行数，可能为 `0` |

数据库执行成功且没有返回结果集或影响行数时，`results` 为空数组。每个结果集最多返回 1,000 行，平台配置和 `maxRows` 可进一步降低上限。响应体上限为 1 MiB。达到抓取上限或因体积限制省略结果行时，`truncated` 为 `true`。

执行时限最长为 25 秒，数据源允许的时限更短时按其配置执行。超过时限时，CloudDM 返回 `EXECUTION_TIMEOUT`，并发起取消和会话清理。写类 SQL 超时或连接中断时，语句可能已在目标数据库提交，重试前请确认数据库状态。

### 账号管理

| 接口 | 请求参数 | 响应和用途 |
| --- | --- | --- |
| `/account/listSubAccounts` | `pageNumber`、`pageSize`，可选 `userNameOrSubAccountPrefix`、`roleId`、`enabled` | 查询可见的子账号，返回 `uid`、`account`、`userName`、`roleId` 和 `enabled` |
| `/account/getSubAccount` | `uid` | 获取单个子账号的资料、角色和启停状态 |
| `/account/listAssignableRoles` | `{}` | 返回调用账号可分配角色的 `roleId` 和 `roleName` |
| `/account/createSubAccount` | `account`、`userName`、`roleId`、`initialPassword` | 写操作。建立本地子账号，返回 `uid`、登录名、角色和启停状态 |
| `/account/updateSubAccount` | `uid`，以及要修改的 `userName`、`email` 或 `phone` | 写操作。更新传入的资料字段，返回 `uid`。`email` 和 `phone` 可传空字符串清空 |
| `/account/setSubAccountRole` | `uid`、`roleId` | 写操作。将可分配角色绑定到子账号，返回 `uid` 和 `roleId` |
| `/account/setSubAccountEnabled` | `uid`、布尔值 `enabled` | 写操作。启用或停用子账号，返回 `uid` 和 `enabled` |

本组接口适用于与调用方同属一个主账号的普通本地子账号。写入目标的角色和可分配角色的功能权限须在调用方的角色权限范围内。通过 `listAssignableRoles` 获取可用的 `roleId`。角色绑定和启停适用于调用方管理的其他子账号。

## 接口样例

请求头按接入与鉴权章节设置。创建数据源和子账号时同时携带 `Idempotency-Key`，并在签名前替换请求体中的密码占位符。

### 查询执行集群

**接口地址** `POST /api/open/v1/cluster/listClusters`

使用有数据源管理权限的子账号查询可绑定的执行集群。请求体包含 `pageNumber` 和 `pageSize`。

```json
{
  "pageNumber": 1,
  "pageSize": 20
}
```

响应示例（HTTP 200）

```json
{
  "requestId": "req-007",
  "code": "OK",
  "msg": "请求成功",
  "data": {
    "items": [
      {
        "clusterId": 5,
        "clusterName": "cluster-sales",
        "clusterDesc": "销售数据库执行集群",
        "runningCount": 2
      }
    ],
    "total": 1
  }
}
```

### 登记数据源

**接口地址** `POST /api/open/v1/datasource/createDataSource`

| JSON 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `envId` | 整数 | 是 | 从 `listEnvironments` 返回结果选择的环境 ID |
| `clusterId` | 整数 | 是 | 从 `listClusters` 返回结果选择的数据源执行集群 ID |
| `dataSourceType` | 字符串 | 是 | 以 `listDataSourceTypes` 返回值为准 |
| `instanceDesc` | 字符串 | 是 | 资产的显示名称或描述 |
| `host` | 字符串 | 是 | 数据库地址。MySQL 填写主机名或 IP 地址 |
| `port` | 整数 | 依数据源类型 | 数据库端口，例如 `3306` |
| `securityType` | 字符串 | 依数据源类型 | 认证方式，例如 `USER_PASSWD` |
| `userName` | 字符串 | 依认证方式 | 数据库登录账号 |
| `password` | 字符串 | 依认证方式 | 数据库登录密码 |

以下示例使用集群上已配置的 MySQL 默认驱动。

```json
{
  "envId": 3,
  "clusterId": 5,
  "dataSourceType": "MySQL",
  "instanceDesc": "销售数据库",
  "host": "sales-db.example.test",
  "port": 3306,
  "securityType": "USER_PASSWD",
  "userName": "sales_reader",
  "password": "${DATABASE_PASSWORD}"
}
```

响应示例（HTTP 201）

```json
{
  "requestId": "req-001",
  "code": "OK",
  "msg": "数据源已登记",
  "data": {
    "dataSourceId": 42,
    "instanceId": "my-example42",
    "lifeCycleState": "CREATED"
  }
}
```

### 发现表字段

**接口地址** `POST /api/open/v1/metadata/getTable`

请求体包含 `dataSourceId`、`listNamespaces` 返回的 `namespacePath`，以及 `listTables` 返回的 `objectType` 和 `tableName`。

```json
{
  "dataSourceId": 42,
  "namespacePath": ["sales"],
  "objectType": "TABLE",
  "tableName": "items"
}
```

响应示例（HTTP 200）

```json
{
  "requestId": "req-002",
  "code": "OK",
  "msg": "请求成功",
  "data": {
    "tableName": "items",
    "objectType": "TABLE",
    "columns": [
      {"name": "item_id", "type": "BIGINT", "nullable": false},
      {"name": "item_name", "type": "VARCHAR", "nullable": false}
    ],
    "primaryKey": ["item_id"]
  }
}
```

### 执行数据操作

**接口地址** `POST /api/open/v1/data/executeOperation`

| JSON 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `dataSourceId` | 整数 | 是 | 目标数据源 ID |
| `namespacePath` | 字符串数组 | 是 | 目标数据库命名空间 |
| `sql` | 字符串 | 是 | 一条目标数据源支持的 SQL |
| `maxRows` | 正整数 | 否 | 每个结果集的返回行数上限，省略时最多返回 1,000 行 |

查询请求示例

```json
{
  "dataSourceId": 42,
  "namespacePath": ["sales"],
  "sql": "SELECT item_id, item_name FROM items ORDER BY item_id LIMIT 5",
  "maxRows": 5
}
```

查询响应示例（HTTP 200）

```json
{
  "requestId": "req-003",
  "code": "OK",
  "msg": "查询成功",
  "data": {
    "results": [
      {
        "type": "RESULT_SET",
        "columns": [
          {"name": "item_id", "type": "BIGINT"},
          {"name": "item_name", "type": "VARCHAR"}
        ],
        "rows": [[1001, "样例商品"]],
        "truncated": false
      }
    ]
  }
}
```

更新请求示例

```json
{
  "dataSourceId": 42,
  "namespacePath": ["sales"],
  "sql": "UPDATE items SET item_name = '新商品名' WHERE item_id = 1001"
}
```

更新响应示例（HTTP 200）

```json
{
  "requestId": "req-005",
  "code": "OK",
  "msg": "执行成功",
  "data": {
    "results": [
      {
        "type": "UPDATE_COUNT",
        "affectedRows": 1
      }
    ]
  }
}
```

权限不足响应示例（HTTP 403）

```json
{
  "requestId": "req-006",
  "code": "FORBIDDEN",
  "msg": "子账号缺少目标表的写入权限",
  "data": null
}
```

### 开通子账号

**接口地址** `POST /api/open/v1/account/createSubAccount`

请求示例中四个字段均必填。`account` 是 CloudDM 登录名，`roleId` 来自可分配角色列表，`initialPassword` 须符合本地账号密码策略。

```json
{
  "account": "employee-1042",
  "userName": "示例员工",
  "roleId": 7,
  "initialPassword": "${INITIAL_PASSWORD}"
}
```

响应示例（HTTP 201）

```json
{
  "requestId": "req-004",
  "code": "OK",
  "msg": "子账号已创建",
  "data": {
    "uid": "sample-user-1042",
    "account": "employee-1042",
    "roleId": 7,
    "enabled": true
  }
}
```

