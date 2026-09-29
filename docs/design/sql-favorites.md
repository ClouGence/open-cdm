# SQL 保存脚本（收藏）功能设计方案

## 1. 结论

建议第一期将“SQL 收藏”定义为：**用户主动保存、可跨设备访问、可重复打开编辑的个人命名 SQL 脚本**。

保存的脚本是独立资产，数据源只是运行时上下文。脚本不绑定数据源、Catalog 或 Schema，数据源删除、改名或权限变化都不影响脚本本身。

第一期包含：

- 收藏当前编辑器全部内容；
- 名称、SQL 内容和数据库类型持久化到服务端；
- 按名称搜索，列表展示名称和更新时间，按最近更新排序并分页；
- 在当前查询页签使用收藏，替换 SQL 而不新建页签或自动执行；
- 保存时可新建收藏，已载入收藏可保存修改；不提供选择其他收藏覆盖的功能；
- 无论收藏数据库类型如何，均保留当前查询连接、事务和历史结果；
- 更新、重命名、删除收藏；
- 使用版本号处理跨设备并发修改。

第一期不包含：

- 自动收藏执行历史；
- 团队共享、公开链接和协同编辑；
- 文件夹、标签、置顶和“收藏别人共享的 SQL”；
- 参数模板、定时执行、版本历史和回收站；
- 将本地已有查询页签自动上传到服务端。

这里的“收藏”是一个持久化的 SQL 副本，不是执行历史上的星标。后续如果建设团队 SQL 资产库，再把个人收藏升级为“SQL 脚本 + 可见范围 + 用户星标”的模型。

## 2. 为什么这样设计

### 2.1 仓库现状

当前查询页签属于本地临时工作区：

- `frontend/src/views/sql/index.vue` 使用 `clouddm_new_tabs_${uid}` 将页签元数据写入 `localStorage`；
- `frontend/src/views/sql/tabManager.js` 和 `frontend/src/utils/sql.js` 使用 IndexedDB 保存部分较大的页签数据；
- SQL 查询通过 `QueryEditorController` 创建会话并经 WebSocket 执行；
- `Operators.vue` 目前只有执行、执行计划、中断、事务和只读等操作，没有“保存 SQL”入口；
- 服务端现有执行审计用于安全审计，不是用户可维护的 SQL 资产，不能作为收藏表复用。

本地页签适合草稿恢复，但不满足跨浏览器、跨设备、检索、并发控制和后续共享。SQL 收藏应建立独立的服务端实体，不能只给本地页签增加 `favorite=true`。

### 2.2 市面工具调研

本次重点核对了脚本正文放在哪里、是否绑定数据源、列表是否加载全文以及大脚本如何处理。结论如下：

| 工具 | 正文存储 | 与数据源的关系 | 大内容和读取策略 | 对本方案的启发 |
| --- | --- | --- | --- | --- |
| DBeaver | 项目 `Scripts` 目录或用户指定的文件系统目录 | 文件可以在编辑器中选择连接，脚本本身是文件资产 | 官方文档未给保存上限，容量由文件系统和编辑器能力约束 | 临时 Console 与持久文件是两个概念；适合本地/VCS 工作流 |
| DataGrip | IDE 项目或外部目录中的 `.sql` 文件 | SQL 文件可以不固定数据源，每次执行时切换，也可以为单文件关联数据源 | 文件可被 IDE 索引并全文搜索；官方文档未给保存上限 | “脚本独立、执行时选择上下文”是成熟桌面工具的标准做法 |
| CloudBeaver | CloudBeaver 服务器文件系统中的 Private/Shared project | 可选关联连接；未关联时打开后再选连接 | Resource Manager 提供可配置单文件上限，当前官方默认值为 `500000` bytes；超限拒绝保存 | Web 产品即使使用文件系统，也会给在线编辑器设置明确的单文件边界 |
| Beekeeper Studio | 本地模式直接持久化到配置目录中的 SQLite；云工作区保存到云端 | Query 与 Connection 都是工作区资产，可分别进入 personal/team 文件夹 | 官方未公布单条大小限制 | SQL 正文落数据库并不罕见，存储介质应服从部署和同步方式 |
| Bytebase | `saved_query.statement` 为数据库 `text` 列 | 当前实现允许保存一个可空的数据库软引用；数据库失效不会阻断脚本正文后续保存 | Search 最多读取正文前 `2 MiB` 并同时返回真实字节数，Get/正式 List 按需读取全文 | 正文可存数据库，但列表/搜索与全文详情必须区分，字节数应是一等字段 |
| Redash | `queries.query` 为数据库 `Text` 列 | `data_source_id` 可空；删除数据源时把 Query 的数据源和最近结果置空，Query 本身保留 | 使用版本字段做并发更新；官方未公布单条大小限制 | 数据源失效后保留脚本并重新绑定上下文，是已有开源产品采用的行为 |

参考资料：

- [DBeaver Script management](https://dbeaver.com/docs/dbeaver/Script-Management/)
- [CloudBeaver Resource Manager](https://dbeaver.com/docs/cloudbeaver/Resource-Manager/)
- [CloudBeaver Resource quotas](https://dbeaver.com/docs/cloudbeaver/Server-configuration/#resource-quotas)
- [DataGrip File management](https://www.jetbrains.com/help/datagrip/working-with-files.html)
- [DataGrip Store your queries](https://www.jetbrains.com/help/datagrip/store-your-queries.html)
- [DataGrip Query history](https://www.jetbrains.com/help/datagrip/find-recent-queries-and-files.html)
- [Beekeeper Studio Saved Queries](https://docs.beekeeperstudio.io/user_guide/sql_editor/saving_queries/)
- [Beekeeper Studio Cloud Workspaces](https://docs.beekeeperstudio.io/user_guide/cloud-storage-team-workspaces/)
- [Bytebase Manage SQL Scripts](https://docs.bytebase.com/sql-editor/manage-sql-scripts)
- [Bytebase `saved_query` 表定义](https://github.com/bytebase/bytebase/blob/d9e69bf3ddb9028063aa38381641ac81479107e0/backend/migrator/migration/LATEST.sql#L424-L445)
- [Bytebase 列表截断与按需读取实现](https://github.com/bytebase/bytebase/blob/d9e69bf3ddb9028063aa38381641ac81479107e0/backend/store/saved_query.go#L119-L144)
- [Bytebase 2 MiB 展示阈值](https://github.com/bytebase/bytebase/blob/d9e69bf3ddb9028063aa38381641ac81479107e0/backend/common/util.go#L22-L26)
- [Bytebase 数据库软引用处理](https://github.com/bytebase/bytebase/blob/d9e69bf3ddb9028063aa38381641ac81479107e0/backend/api/v1/saved_query_service.go#L437-L454)
- [Redash Creating and Editing Queries](https://redash.io/help/user-guide/querying/writing-queries/)
- [Redash Favorites & Tagging](https://redash.io/help/user-guide/dashboards/favorites-tagging/)
- [Redash Query 模型及数据源解绑实现](https://github.com/getredash/redash/blob/8f6b15d30da433ae881dd6376a38df17bb6c2663/redash/models/__init__.py#L196-L198)

### 2.3 调研后的判断

市场上实际存在两条路线：

1. DBeaver、DataGrip、CloudBeaver 把 SQL 当文件或服务端资源管理，适合目录、导入导出、VCS 和大文件工作流；
2. Beekeeper Studio、Bytebase、Redash 把保存查询直接放数据库文本列，适合 Web 端检索、权限、同步和协作。

两条路线都会限制在线编辑器一次打开和传输的正文规模。没有发现主流产品为了“收藏 SQL”把一条脚本透明分片到多行；这样做不能降低浏览器打开全文、HTTP 传输和 SQL 编辑器渲染的成本，反而增加一致性、删除和版本控制复杂度。

open-cdm 的 `dm_exec_file` 是 worker 查询结果文件索引，`dm_sys_attachment` 是审批、自动执行和证书等附件存储。两者都有各自的状态和清理语义，不应承载用户 SQL 收藏。收藏因此使用独立的 `dm_exec_sql_script` 元数据索引，并把 UTF-8 正文作为不可变文件存入 Console 的持久数据目录 `app.data/userdata/sql-scripts`。数据库只保存所有者、名称、数据库类型、文件 URI、状态和版本，列表查询不会触碰正文。

文件写入采用临时文件、落盘、原子发布和读回校验；数据库写入失败时补偿删除新文件。更新先发布新文件，再通过版本号 CAS 切换索引，旧文件延迟一小时作为孤儿清理，保证并发详情读取不会因索引切换立即失效。删除先将索引标记为 `Deleting` 并释放名称，定时任务在保护期后删除文件和索引，失败会保留状态并重试。

早期文件索引实现曾使用 `app.data/userdata/sql-script`（单数）目录。升级和运行期读取、触碰、删除及孤儿清理同时兼容该目录；新写入统一进入 `sql-scripts`（复数），因此已有 `file_uri` 不会仅因目录更名而失效。

多 Console 实例必须挂载同一个共享、持久、支持原子重命名的 `app.data` 卷；数据库共享但 `app.data` 不共享的部署不受支持，否则请求切换实例后将无法读取正文。备份和恢复必须同时覆盖业务数据库与该共享目录，并保持同一时间点的一致性。

发布/迁移脚本可按需使用数据库 CI/CD 流程；SQL 收藏不设置业务层正文大小上限。

## 3. 产品交互

### 3.1 页面布局

查询页保持现有布局，不增加左侧一级页签，也不在执行工具栏增加常驻保存按钮。收藏入口放在 SQL 编辑器右上角，与现有字号 `T` 控件并列，默认只显示一个小书签图标：

```text
编辑器右上角                         [收藏] [T]
                                      │
                         ┌────────────┴────────────┐
                         │ SQL 收藏                  │
                         │ [搜索收藏名称]            │
                         │ 每日订单异常检查   MySQL   │
                         │ 用户权限核对       PostgreSQL│
                         │ 慢查询定位         MySQL   │
                         ├─────────────────────────┤
                         │      收藏当前 SQL         │
                         └─────────────────────────┘
```

点击图标后才展开轻量浮层；点击空白处或按 `Esc` 关闭。浮层包含带放大镜前缀的搜索框、同排独立刷新按钮和最近更新列表；标题“SQL 收藏”右侧仅显示保存图标，提供“收藏当前 SQL”悬停提示，底部不再显示收藏按钮，不增加数据库类型筛选控件。列表项只显示名称和更新时间，菜单提供打开、重命名和删除，不显示脚本总数。数据库类型保留为既有存储契约中的元数据，不用于选择、限制或切换查询连接，也不在收藏列表或保存弹窗中展示。

收藏图标承担状态反馈：

- 当前页签尚未关联收藏：点击图标只展开浮层，标题右侧保存图标可“收藏当前 SQL”；
- 当前页签已经关联收藏且内容有变化：图标显示小圆点，浮层顶部出现“保存当前修改”；
- 当前页签已经关联收藏且内容未变化：图标使用轻量选中态；
- 不注册 `Ctrl/Cmd + S`，保存只能由用户在收藏浮层中主动触发。

### 3.2 创建收藏

“收藏当前 SQL”仅打开新建弹窗并填写名称，不提供覆盖方式或目标选择。已载入收藏产生修改时，可通过“保存修改”更新该收藏。

字段规则：

| 字段 | 规则 |
| --- | --- |
| 名称 | 必填，去除首尾空白，1～128 字符；同一用户下不允许重名 |
| 数据库类型（内部） | 保留既有必填契约，优先沿用当前关联收藏的类型，否则取查询页签类型；仅作存储元数据，不展示或绑定数据源 |

点击保存时固定保存整个编辑器内容，不提供保存范围或 SQL 预览。

保存当前关联收藏的修改时必须使用页签加载时持有的 `scriptVersion`。提交前不得悄悄刷新版本，否则会掩盖并发冲突。成功后同步当前页签的 `scriptId`、`scriptVersion`、`scriptSavedText`、名称和数据库类型关联。

保存发起时同时捕获页签收藏关联代次。保存期间可以继续编辑并保持脏状态，但如果用户主动加载了另一收藏，旧保存响应只更新服务端和列表，不得把当前页签重新绑定回旧目标。弹窗保存与快捷保存共用页签级在途锁，锁在首次异步校验前建立并在 `finally` 释放。

SQL 内容只校验 `trim()` 后非空，保存时保留原始空格、换行、注释和多语句结构，不自动格式化，也不要求执行成功。

### 3.3 打开收藏

- 单击列表项或聚焦后按 Enter，把 SQL 加载到当前查询页签；页签数量和当前焦点不变；
- 详情异步返回后，只有目标页签仍存在、仍为当前页签、请求仍是最新且用户未继续编辑时才允许覆盖；
- 点击即替换已有正文，包括点击前尚未保存的内容，不增加确认或数据源选择步骤；
- 无论收藏 `dsType` 与当前查询是否相同，只更新正文及收藏关联/版本基线，保留查询页签标题、key、icon、active、连接、Catalog/Schema、事务、只读、会话、执行状态和历史结果；
- 收藏载入不创建、关闭或切换查询会话；运行中的查询继续使用已提交的 SQL；
- 加载收藏绝不自动执行；
- 普通查询工作台继续负责连接、数据源状态和执行权限；旧未连接页签仍可查看、编辑和保存收藏，不展示收藏专属绑定入口。

### 3.4 修改、冲突和关闭

页签保存以下状态：`scriptId`、`scriptVersion`、`scriptSavedText`、`scriptName`、`scriptDsType`。

- 编辑器内容与 `scriptSavedText` 不一致时，页签显示未保存圆点；
- 更新请求携带读取时的 `version`；服务端仅在版本相同时更新并递增版本；
- 版本冲突时不静默覆盖，弹窗提供“加载服务端版本”和“另存为副本”；第一期不做文本合并；
- 关闭有未保存修改的收藏页签时，只提供“取消”和“放弃修改”；取消保留正文并停止关闭队列，放弃关闭目标页签且不写服务端，正常保存仍在收藏浮层操作；
- 删除收藏前二次确认。删除已打开的收藏后，页签退化为普通本地草稿，不强制关闭。

## 4. 数据模型与存储位置

### 4.1 独立索引表

新增 `dm_exec_sql_script`，通过单个 Flyway Java migration 直接创建最终文件存储元数据表，不经过数据库正文列和后续搬迁步骤：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `id` | `bigint` | 自增主键，也是 API 的 `scriptId` |
| `gmt_create` | `datetime` | 创建时间 |
| `gmt_modified` | `datetime` | 最近更新时间 |
| `owner_uid` | `varchar(64)` | 脚本所有者，从登录态取得 |
| `name` | `varchar(128)` | 脚本名称；`Deleting` 状态可为空以立即释放同名约束 |
| `ds_type` | `varchar(64)` | 数据库类型 / SQL 方言，例如 MySQL、PostgreSQL；不保存数据源 ID |
| `file_uri` | `varchar(500)` | 正文文件索引，不向 API 暴露 |
| `status` | `varchar(16)` | `Ready` 或 `Deleting` |
| `version` | `bigint` | 乐观锁版本，默认 0 |

第一期不增加 `description`、`file_size`、`content_preview`、`file_type`、`retention_type` 或具体数据源 ID。它们都不是完成个人保存脚本闭环所必需的。`ds_type` 保留为历史契约元数据，不约束查询运行上下文。

索引：

```sql
PRIMARY KEY (id),
UNIQUE KEY uk_sql_script_owner_name (owner_uid, name),
UNIQUE KEY uk_sql_script_file_uri (file_uri),
KEY idx_sql_script_owner_status_modified (owner_uid, status, gmt_modified, id),
KEY idx_sql_script_status_modified (status, gmt_modified, id)
```

删除通过 CAS 将记录改为 `Deleting` 并清空名称，同名立即释放；后台清理文件成功后再删除索引。它不是用户可恢复的回收站。

### 4.2 正文读写

正文与元数据跨文件系统和数据库协作：

- 创建：持久发布文件后插入索引；插入失败补偿删除文件；
- 更新：发布新文件后按 `id + owner_uid + status + version` CAS 切换索引；失败删除新文件，成功后旧文件进入延迟孤儿清理；
- 详情：先读索引和文件，再复查索引；索引切换时重试，避免返回不一致版本；
- 删除：CAS 标记 `Deleting`，后台在保护期后删除文件与索引；删除失败保留记录供下轮重试；
- 巡检：只清理不被任何索引引用且超过保护期的 UUID 正文文件和过期临时文件。

覆盖时继续按既有 update 契约提交 `ds_type`，但该字段不改变查询页签的连接或运行状态。

列表只取摘要列；详情接口按 `id + owner_uid + Ready` 读取索引并从文件系统按 UTF-8 读取正文。API、异常和日志均不暴露物理路径或完整 SQL。

### 4.3 容量策略

- SQL 收藏不设置业务层单条正文大小上限，也不向列表接口暴露大小限制；
- 前后端只校验正文非空，不按 UTF-8 字节数拒绝保存；
- 第一期不预设每用户数量和总容量配额；
- 第一版只搜索 `name`，不扫描正文；
- 大型发布脚本仍可按用户需要使用数据库 CI/CD 的 SQL 文件流程，但不是 SQL 收藏的强制分流条件。

部署环境的 HTTP、内存、文件系统等基础设施仍可能形成客观边界；这些边界不属于 SQL 收藏业务契约，也不在本功能中额外收紧。列表不取正文，因此正文规模不影响浏览和搜索的响应体。

不建议把收藏序列化进 `dm_sys_user_conf.config_value`：收藏需要分页、唯一约束、并发更新和单条删除，JSON 大对象会把所有操作变成整包读写并产生覆盖风险。

## 5. 后端设计

### 5.1 模块与类

保存脚本属于查询编辑器能力，沿用现有模块边界：

- DAO：`cgdm-dao`
  - `DmExecSqlScriptDO`；
  - `DmExecSqlScriptMapper` 和对应 XML；
  - 在 `ExecutionDal` / `ExecutionDalImpl` 暴露 Mapper
- Console：`cgdm-console`
  - `SqlScriptController`
  - `SqlScriptService` / `SqlScriptServiceImpl`
  - create、update、list、detail、delete 对应的 FO/VO
- 初始化：`boot-initialization`
  - 新增时间戳版本的 Java migration 创建 `dm_exec_sql_script`

不要在 `QueryEditorController` 中继续堆叠 CRUD；它已经负责会话、状态、结果和下载。保存脚本使用独立 Controller，沿用 `DM_QUERY_CONSOLE` 权限。

### 5.2 API

遵循当前前端服务的驼峰接口命名和 POST 风格：

| 前端 service 名 | 路径 | 请求要点 | 响应 |
| --- | --- | --- | --- |
| `dmQueryScriptCreate` | `/api/entry/query/script/create` | name、dsType、sqlContent | scriptId、version、时间 |
| `dmQueryScriptUpdate` | `/api/entry/query/script/update` | scriptId、version、name、dsType、sqlContent | 新 version、时间 |
| `dmQueryScriptList` | `/api/entry/query/script/list` | keyword、page | 不含完整 SQL 的分页摘要 |
| `dmQueryScriptDetail` | `/api/entry/query/script/detail` | scriptId | 完整脚本详情 |
| `dmQueryScriptDelete` | `/api/entry/query/script/delete` | scriptId | 成功状态 |

列表固定按 `gmt_modified DESC, id DESC` 排序。`keyword` 只搜索名称；列表摘要返回内部使用的 `dsType`，但界面不展示该字段，也不返回 SQL 正文或预览，完整 SQL 仅由详情接口返回。

### 5.3 所有权与权限

所有接口都从登录态读取 `uid`，不接受前端传入所有者。Mapper 的详情、更新和删除条件必须同时包含脚本 ID 和所有者：

```sql
WHERE
    id        = #{scriptId}
    AND
    owner_uid = #{uid}
```

保存脚本 API 接收 `dsType`，但不接收数据源 ID、层级或连接配置。服务端校验类型值来自已注册的数据源类型集合。本轮保留该必填元数据及数据库结构，不修改历史 migration。查询执行继续使用普通工作台的 `createSession`、`ownDataSource` 和查询权限校验；收藏读取不绑定数据源，也不能绕过执行授权。

更新使用：

```sql
WHERE
    id        = #{scriptId}
    AND
    owner_uid = #{uid}
    AND
    version   = #{version}
```

受影响行数为 0 时，服务层先确认记录是否仍存在，再分别返回“脚本不存在”和“脚本已被其他会话修改”的国际化业务错误。

### 5.4 校验和日志

- FO 使用 Jakarta Bean Validation 默认消息，不在注解上设置 `message`；正文非空校验放在服务边界；
- 同名、版本冲突等业务错误使用带国际化 key 的 `ErrorMessageException`；
- 名称搜索必须使用 MyBatis 参数绑定，并正确转义 `LIKE` 中的 `%`、`_` 和转义字符；
- 操作审计可记录创建、修改、删除的脚本 ID 和名称，但日志、异常和审计内容都不得记录完整 SQL；
- SQL 在返回页面时作为纯文本进入 Monaco，不得通过 `v-html` 渲染；
- 保存脚本 CRUD 不触发查询审核、脱敏或工单，因为它不执行 SQL。

## 6. 前端设计

### 6.1 组件拆分

实现包括：

- `SqlScriptPopover.vue`：编辑器右上角轻量入口、搜索、分页、空态和列表操作；
- `SqlScriptPopover.vue` 内的保存弹窗：新建和重命名；已关联收藏通过“保存修改”更新；
- 在 `Editor/index.vue` 的字号控件旁增加收藏图标，并向 `SqlViewer.vue` 派发收藏动作；
- 在 `query.js` 注册收藏 API；
- 在 `frontend/src/locales/` 同步维护中英文文案。

第一期不把收藏放进 IndexedDB。服务端是收藏真源，本地页签只保存工作区状态。断网时允许继续编辑当前页签，但浮层中的保存动作明确提示失败，不建立容易产生冲突的离线写队列。

### 6.2 与现有页签的衔接

在现有查询 tab 对象上增加可选字段，不改变 DATA/STRUCT 页签：

```js
{
  scriptId,
  scriptVersion,
  scriptName,
  scriptSavedText,
  scriptDsType
}
```

使用收藏时对当前查询页签做快照并发起详情请求。响应只在页签仍为当前目标、请求序号仍最新且编辑器内容未变化时应用。所有类型都直接更新文本和收藏关联，当前查询的标题、连接及运行状态保持不变。`scriptName` 仅表示收藏名称，与查询页签 `title` 分离；载入、新建、覆盖、重命名或删除收藏均不自动改查询标题，刷新继续使用本地保存的原查询标题，用户主动命名沿用普通工作台功能。没有收藏专属数据源选择、兼容性校验或会话绑定，任何分支都不自动执行 SQL。

本地恢复页签后，保存脚本可能已经在其他设备被更新或删除。恢复阶段不阻塞整个查询页面：先恢复本地草稿，用户触发保存时再用 `version` 检测冲突；脚本列表加载成功后可异步标记已删除的关联。运行时连接只属于本地页签，不上传到脚本实体。

## 7. 关键流程

### 7.1 创建

```text
用户点击编辑器右上角收藏图标，在浮层中选择“收藏当前 SQL”
        ↓
用户填写名称，前端读取整个编辑器内容和当前 dsType
        ↓
POST /script/create
        ↓
服务端校验登录人、名称和 SQL 非空
        ↓
持久发布 UTF-8 文件并写入 dm_exec_sql_script 索引
        ↓
当前页签绑定 scriptId/version/savedText，刷新收藏列表
```

### 7.2 打开并执行

```text
用户在当前查询页签选择收藏
        ↓
POST /script/detail，读取 SQL 脚本和 dsType
        ↓
检查目标页签、请求序号和编辑快照
        ↓
保留当前连接、Catalog/Schema、事务、只读、会话和结果
        ↓
在当前页签填充 SQL，不自动执行
```

## 8. 分期计划

### Phase 1：个人收藏 MVP

- migration、DO、Mapper、Service、Controller、FO/VO；
- 编辑器右上角收藏浮层、名称与更新时间、搜索和分页；
- 新建、保存当前收藏修改、当前页签使用、重命名、删除；
- 收藏图标状态、浮层保存动作、未保存状态和关闭确认；
- 所有类型加载均保留查询运行态、乐观锁冲突和中英文文案。

预计 6～9 个开发人日，包括联调和浏览器回归，不包含产品视觉稿等待时间。

### Phase 2：易用性增强

- 标签、文件夹、置顶、最近使用；
- 从成功执行历史一键收藏；
- 导入/导出 `.sql`；
- 收藏版本历史与回收站。

### Phase 3：团队 SQL 资产

- 在现有 SQL Script 实体上增加 PERSONAL / TEAM 可见范围、只读分享和复制；
- 单独增加用户 Star 关系，不复用 Script 所有权；
- 团队目录、编辑权限、变更历史和审计；
- 根据真实需求再评估参数模板、审批发布和 Git/VCS 同步。

## 9. 验收标准

1. 用户在设备 A 收藏 SQL 后，可在设备 B 登录同一账号检索并打开。
2. 用户只能读取、更新和删除自己的收藏；修改请求中的 ID 不能越权访问其他用户数据。
3. SQL 的空格、换行、注释、多语句和非 ASCII 字符保存后保持一致。
4. 名称可搜索，分页和更新时间排序稳定；列表请求不读取或返回 SQL 正文。
5. 单击收藏直接替换当前页签完整 SQL，无二次确认、无数据源弹窗、不新建或切换页签、不自动执行；同/异类型都保留现有连接、Catalog/Schema、事务、只读和结果。
6. 保存脚本不包含具体数据源 ID；数据源删除、改名或权限变化不影响脚本读取。执行仍使用普通查询工作台既有连接和权限校验，不受收藏类型限制。
7. 两个设备同时编辑同一收藏时，后保存的一方收到冲突提示，不能静默覆盖。
8. 关闭有未保存修改的收藏页签时必须提示；保存成功后未保存标记消失。
9. 删除收藏后，其他已打开页签再次保存时得到“收藏不存在”，并可另存为。
10. 收藏浮层不提供类型筛选控件，直接展示全部脚本；列表和保存弹窗均不显示数据库类型。
11. 收藏列表接口不返回完整 SQL；服务端日志和操作审计不记录 SQL 正文。
12. 数据库不保存 SQL 正文；文件位于持久 `app.data/userdata/sql-scripts`，迁移、创建、更新和删除失败均可补偿或重试。
13. 多 Console 实例使用同一共享持久 `app.data` 卷，备份与数据库保持时间点一致。

## 10. 实施时的验证范围

- 后端：Mapper CRUD、所有权隔离、名称唯一、分页搜索、大正文读写和版本冲突；
- 前端：新建/覆盖/删除、收藏浮层、名称与时间展示、脏状态、当前页签替换、同/跨类型状态处理、异步防覆盖和双设备冲突；
- 国际化：执行 `npm run check-i18n`；
- 前端源码修改后，按仓库规则执行 `cd package && ./all_build.sh web`；
- 用户可见流程变更按 `frontend/AGENTS.md` 在 `tests/frontend/` 新增或更新唯一的长期复测流程文档，并使用真实 Chrome 完成回归；
- 后端至少执行 DAO/Console/Initialization 相关模块的定向构建与测试，数据库 migration 需要覆盖全新安装和从当前版本升级两条路径。

## 11. 已采用的产品约束

1. UI 使用“SQL 收藏”；
2. SQL 收藏不设置业务层正文大小上限，前后端只校验正文非空；
3. 不自动上传已有本地页签；
4. 使用收藏替换当前查询页签，不新建页签；
5. 保存弹窗仅支持新建收藏，不提供覆盖其他收藏；“保存修改”仅更新当前关联收藏。
