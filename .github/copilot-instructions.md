# CloudDM AI 指引

本仓库的 AI 协作必须优先服从 `.document` 下的项目文档。

## 强制规则

- 处理任何非琐碎任务前，必须先判断 `.document` 中是否存在相关文档；如果存在，必须先读再行动，不能跳过。
- 对项目边界、技术架构、插件体系、数据源体系、开发启动、调试、构建、打包、代码风格的判断，必须优先基于 `.document`，不能凭经验改写。
- 若用户请求涉及这些主题，但 AI 没有先读取对应 `.document` 文件，不应直接下结论或直接改代码。
- 若 `.document` 与当前代码实现存在差异，先指出差异，再以当前代码为实现事实来源；不要静默忽略文档，也不要用文档强行覆盖已存在实现。
- 用户显式要求优先级高于仓库文档，但在用户未明确要求突破边界时，`.document` 是本仓库第一优先的项目级上下文来源。

## 文档使用映射

### 通用原则

以下文档在大多数非琐碎任务中都应优先检查：

- `.document/horses/01-总体原则.md`
- `.document/horses/02-项目介绍.md`
- `.document/horses/06-代码风格和格式化风格.md`

### 按任务类型强制读取

- 启动、调试、构建、打包、测试账号、运行入口：必须先读 `.document/dev/README.md`
- 模块边界、产品形态、项目组成、仓库范围判断：必须先读 `.document/horses/02-项目介绍.md`
- 技术架构、平台层次、模块职责、是否能跨层改动：必须先读 `.document/horses/03-项目技术架构.md`
- 插件、Plugin SDK、PluginLoader、Binder、驱动与插件装配关系：必须先读 `.document/horses/04-插件化体系.md`
- 数据源、dsc-common、ds-xxx、Sidecar 访问链路、Driver family、DriverBinding、DriverLoader：必须先读 `.document/horses/05-数据源架构.md`
- 代码修改、格式调整、命名和排版判断：必须先读 `.document/horses/06-代码风格和格式化风格.md`

## 行为约束

- 不要把 CloudDM 描述成单一 Web 应用、单体后台或小型数据库工具。
- 不要擅自把 Console、Sidecar、Desktop、Plugin、Driver、DataSource 的职责合并或互换。
- 不要在没有明确授权时扩大改动范围、顺手重构、顺手清理无关问题。
- 不要在没有文档或代码依据时宣布某个模块可以删除、已经废弃或应当合并。
- 不要为了“统一”而忽略现有代码风格、格式化边界和模块分层。

## 推荐执行顺序

1. 先判断用户请求属于哪一类问题。
2. 读取 `.document` 下对应文档，必要时同时读取多份。
3. 用文档收敛边界后，再去搜索代码、定位入口或修改实现。
4. 回答时保持与文档一致；若发现文档与代码不一致，明确指出。

## 特别说明

- 前端单独改动时，构建方式优先参考 `.document/dev/README.md` 中记录的 `./all_build.sh web`。
- 需要访问真实数据源时，启动要求优先参考 `.document/dev/README.md` 中关于 Console 与 Sidecar 联动的说明。
- 涉及架构或边界争议时，优先回到 `.document/horses/` 下文档，而不是只凭局部代码猜测。