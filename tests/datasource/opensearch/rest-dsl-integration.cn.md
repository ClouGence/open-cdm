# OpenSearch REST DSL 集成验收

本文件是第 4 步的长期集成用例，开发完成后统一验收，不作为已通过的证据。不自动启动 CloudDM，不执行数据库升级。第 4 步已完成后端 DSL 接入及单元验证，以下集成用例均为 `NOT RUN`。第 5 步已接通单次查询输出，具体用例见[元数据与执行验收](metadata-execution.cn.md)；第 6 步已接入符合条件的自动分页，见[分页与导出验收](pagination-export.cn.md)；第 8 步已接入前端 REST 高亮映射和当前请求选择，页面流程见[OpenSearch 入口与 REST 工作台](../../frontend/datasource/opensearch_datasource.md)，完成后统一验收。

## 准备与记录

- 使用独立的 ES、OpenSearch 数据源，记录真实产品、服务端版本、插件版本及账号权限。版本组合沿用[能力矩阵](opensearch-test-matrix.md)，至少覆盖 OpenSearch 2.3、2.4、2.19、3.2、3.3、3.9，以及 ES 7.10、7.12、8.3、8.4、8.14、9.x 的相关边界。
- 使用普通账号和专用索引 `cdm_os_dsl_case`，另备 `cdm_os_dsl_lookup` 作为跨索引引用目标。测试账号、密码、证书和连接配置不写入仓库。
- 页面校验、补全、后端分析及实际执行分别记录结果。不能把编辑器校验成功记为执行成功。
- 拒绝用例须确认没有向数据源发送目标请求；若请求在更早的权限检查中被拒绝，须另行覆盖产品能力校验，不能以权限失败替代能力校验结果。

## 用例

| 编号 | 操作 | 预期 | 状态 |
| --- | --- | --- | --- |
| DSL-01 | 打开、重开及切换 ES / OpenSearch 编辑器，检查语言引擎、服务端版本与请求参数 | 使用对应产品身份和真实版本；切换后不残留另一产品的规则 | NOT RUN |
| DSL-02 | 输入多个 REST 请求，包含注释、空行、嵌套 JSON、中文、emoji、字符串中的 `GET /...` 和大整数；执行拆句及格式化 | 请求边界正确；字符串、大整数精度、注释保留；选择区域位置正确 | NOT RUN |
| DSL-03 | 在 OpenSearch 输入带 mapping type 的路由、`include_type_name`；在 ES 7 与 ES 8/9 对照 | OpenSearch 拒绝类型路由和参数；ES 延续原有版本规则；普通文档中同名字段不被误判 | NOT RUN |
| DSL-04 | 在索引管理请求输入 `cluster_manager_timeout` 和 `master_timeout`，检查补全与校验 | OpenSearch 按真实接口支持处理，补全优先推荐新名称；ES 不接受 OpenSearch 专用参数；不能仅凭弃用公告推断旧参数已删除 | NOT RUN |
| DSL-05 | 输入 ES `_knn_search`、搜索顶层 `knn`、`retriever`，以及 OpenSearch `knn` / Neural / Hybrid 查询及 `script_score` 的 `lang: knn` 脚本 | OpenSearch 首期拒绝向量和扩展查询，不提示为可执行能力；ES 原有版本边界与范围策略不变 | NOT RUN |
| DSL-06 | 在文档内容、term 查询值、聚合名称中使用 `knn`、`retriever`、`runtime`、`index` 等名字 | 不把普通数据、字段或聚合名称误识别为协议功能；合法请求仍能通过 | NOT RUN |
| DSL-07 | 在创建索引、更新 Mapping 和搜索请求中使用另一产品独有的字段 | 在对应 DSL 位置给出明确诊断；不以全文关键字匹配误伤文档数据 | NOT RUN |
| DSL-08 | 在 GET/POST 搜索、文档操作、索引管理请求的不同位置触发补全 | 按请求上下文和产品能力给出候选；不推荐当前范围明确拒绝的接口或参数 | NOT RUN |
| DSL-09 | 搜索内嵌 terms lookup、indexed shape、more_like_this 等引用，并把引用放进 bool、过滤聚合等嵌套位置 | 后端分析保留所有读取目标；权限检查与执行前资源校验使用同一范围 | NOT RUN |
| DSL-10 | 输入全局搜索、通配符/多索引、跨集群、编码路径分隔符、Bulk/MSearch、SQL/PPL、集群或 Security API | 保持首期范围限制；编辑器和后端不会通过产品切换绕过限制 | NOT RUN |
| DSL-11 | 输入 `filter_path`、重复参数、异步 by-query、Search Pipeline 参数 | 不掩盖失败状态，不接受重复歧义参数或超出治理范围的执行方式 | NOT RUN |
| DSL-12 | 模拟丢失产品参数、伪造另一产品参数、缺少/非法版本，并调用后端分析及执行入口 | 插件固定产品身份；执行使用探测到的版本；不能仅依赖浏览器传来的产品/版本放行 | NOT RUN |
| DSL-13 | 查询限制改写带 Unicode 和大整数的请求；对含显式 size 的请求执行限制改写 | 只修改查询限制，保留其余内容；产品/版本信息贯穿改写和后续校验 | NOT RUN |
| DSL-14 | 对照 OpenSearch 2.3/2.4 及 3.2/3.3 的能力选择 | 区分 PIT 是否存在与稳定分页排序是否可用；第 4 步的能力记录不能代替后续 Scroll/PIT 实际分页验收 | NOT RUN |
| DSL-15 | 在 ES 7.10/7.12、8.3/8.4、8.14、9.x 重跑已有编辑、分析、普通查询及版本相关拒绝场景 | OpenSearch 接入不改变现有 ES 的产品识别、版本规则、资源分析、查询与分页行为 | NOT RUN |

## 依据与清理

- [k-NN 脚本查询](https://docs.opensearch.org/latest/vector-search/vector-search-techniques/knn-score-script/)说明 `lang: knn` 也是向量查询入口，首期范围校验须覆盖该路径。

- [OpenSearch 版本变更说明](https://docs.opensearch.org/latest/breaking-changes/)说明 2.0 删除 mapping types；具体参数仍以对应版本接口实现为准。
- [OpenSearch 3.0 创建索引实现](https://github.com/opensearch-project/OpenSearch/blob/3.0/server/src/main/java/org/opensearch/rest/action/admin/indices/RestCreateIndexAction.java)仍处理弃用的 `master_timeout`；不要直接将其判定为 3.x 不支持。
- [PIT API](https://docs.opensearch.org/latest/api-reference/search-apis/point-in-time-api/)与[3.3 发布说明](https://github.com/opensearch-project/OpenSearch/blob/main/release-notes/opensearch.release-notes-3.3.0.md)分别用于 PIT 与 `_shard_doc` 能力边界。
- 验收完成后关闭测试会话，删除专用索引和临时权限，清理上传的测试证书及本地抓包记录；真实结果回填能力矩阵。保留本流程，不生成按日期命名的重复测试文档。
