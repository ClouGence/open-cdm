# AGENTS.md

本文件为 AI 编码代理在 open-cdm 前端工程（`frontend/`）内工作时的规则说明。内容按 CloudDM Web 前端的实际工程组织和团队规则调整。

## UI 布局一致性（最高优先级）

**任何用户可见 UI
新增或改版前，必须先阅读并遵守 [`skill/ui-layout-consistency/SKILL.md`](skill/ui-layout-consistency/SKILL.md)。**

- 该规范定义 **扁平化编辑型 B 端** 的产品风格、壳层布局、少线框分区、Tab/页脚/按钮层级与页面类型模板。
- 优先级高于组件库默认样式、个人习惯、以及本文件以下设计摘要与零散旧页面实现。
- 若无法满足（例如卡片套卡片、fixed 全屏页脚、多层线框、自定义 Tab 皮肤），**中断本次改动**，说明违反项，不得强行提交。
- Token 色值见 `DESIGN.md`；结构与反例见 `skill/ui-layout-consistency/reference.md`。
- 规范中的标准类名/组件（如 `AppPageTabs`、`page-shell`）以 skill 为准；**全量页面落地进行中**，改页面前先对照
  skill，勿沿用旧页面临时写法。

## 作用范围

- 本文件适用于以 `frontend/` 为工作根目录的前端开发任务。
- 仓库根目录的 `AGENTS.md` 提供全仓通用规则；本文件在其基础上补充前端专属约束。
- 若子目录存在更深层 `AGENTS.md`，则该子目录及其后代以内层文件为准。
- 本文件约束源码、配置、脚本和测试变更；默认不要修改生成产物和依赖目录，例如 `node_modules/`、`dist/`。

## 项目定位

- `frontend/` 是 CloudDM 的 Web 前端工程，npm 包名为 `clouddm-web`，同时作为后端 Gradle 模块 `cgdm-web` 被打包进安装包。
- 面向团队数据库管理，覆盖 SQL 查询、数据源管理、权限控制、脱敏、工单协作、系统配置等能力。
- 主要技术栈：
    - Vue 3、Vue CLI 5、Vue Router 4、Vuex 4。
    - JavaScript 为主，局部 TypeScript；Node.js 22.22.1。
    - UI：View UI Plus（主）、Ant Design Vue（局部）、Tailwind CSS、Less。
    - 编辑器：Monaco Editor。
    - 国际化：vue-i18n；HTTP：axios；实时：reconnecting-websocket。

## 项目结构

- `src/main.js`：应用入口，注册插件、全局组件、主题和 i18n。
- `src/App.vue`：根组件。
- `src/router/`：路由定义；`index.js` 为主路由，`system.js` 为系统管理子路由。
- `src/store/`：Vuex 全局状态（用户、权限、主题等）。
- `src/views/`：页面视图，按业务域划分：
    - `sql/`：SQL 工作台
    - `system/`：系统管理（数据源、权限、脱敏、子账号等）
    - `ticket/`：工单
    - `project/`：项目 / 变更
    - `login/`、`initialization/`：登录与初始化
    - `consoleJob/`、`worker/`、`devops/` 等运维相关页面
- `src/components/`：可复用组件：
    - `ui/`、`widgets/`：基础 UI 与业务小组件
    - `function/`：功能型组件（数据源、监控、编辑器等）
    - `layout/`：布局（侧边栏、品牌等）
    - `modal/`、`form/`、`editor/`：弹窗、表单、编辑器
- `src/services/`：接口与通信层：
    - `http/`：面向 DM Console 的 REST API（`request.js` 为 axios 封装）
    - `socket.js`：WebSocket 连接
- `src/locales/`：国际化文案（`en.json`、`zh.json`）。
- `src/i18n.js`：i18n 初始化与语言切换。
- `src/styles/`：全局样式、主题（`themes/`）、Less 变量与 mixin。
- `DESIGN.md`：UI 设计风格规范（色彩、字体、间距、圆角、组件形态）；新增或改版页面/组件时以此为准。
- `src/utils/`：通用工具函数。
- `src/mixins/`：Vue mixin（鉴权、数据源、弹窗等）。
- `src/directives/`：自定义指令。
- `src/const/`：常量与枚举。
- `public/`：静态资源与 HTML 模板。
- `vue.config.js`：Vue CLI / Webpack 配置；开发代理默认指向 `http://localhost:8222`。
- `scripts/check-i18n.js`：国际化 key 一致性检查脚本。

## 工作方式

- 以最小且正确的改动解决问题。
- 优先选择直接、清晰、可读的实现，不追求花哨抽象。
- 当多条规则冲突时，优先保证行为正确、状态一致、契约清晰。
- 不乱猜业务语义；拿不到明确数据时，不要擅自补业务对象、状态、内容或其他业务含义。
- 不胡乱兼容旧逻辑；已经明确删除的字段、分支、路径要清干净。
- 先阅读相关模块的现有实现，再决定改法；优先沿用本仓库已有模式、命名、工具类和基础设施。
- 工作区可能存在用户未提交改动；不要回滚、覆盖或重排与当前任务无关的改动。

## 构建与验证命令

### 环境要求

- Node.js 22.22.1
- 本地联调需后端单机模式运行于 `http://localhost:8222`（`DmAloneLauncher`）

### 常用命令

```bash
cd frontend && npm install
cd frontend && npm run serve:dm
cd frontend && npm run build:dm
cd frontend && npm run lint
cd frontend && npm run lint-fix
cd frontend && npm run test:unit
cd frontend && npm run check-i18n
```

- 使用 `package-lock.json`，默认使用 `npm`，不要擅自切换到其他包管理器。
- 构建产物输出到 `dist/templates/`，由后端 Gradle `cgdm-web` 模块打包。
- 全量构建前端资源也可通过 `cd package && ./all_build.sh web` 触发。
- 每次完成前端改造后，必须从仓库根目录运行 `cd package && ./all_build.sh web` 作为最终验证。

### 本地联调

- `npm run serve:dm` 启动开发服务器，`vue.config.js` 中 `devServer.proxy` 将 API 代理到后端。
- 后端未启动时，页面接口调用会失败；优先确认 `localhost:8222` 可访问。

## 编码规则

- 不过度防御，不为了极低概率场景写复杂兜底逻辑。
- 允许基于模块边界、配置约束和框架契约建立合理信任；不要不看上下文就把所有值都当成任意脏输入。
- 不要为实际不可能出现的 `null`、空值或非法状态写复杂防御分支；防御逻辑只放在真实边界和不可信输入处。
- 避免长条件和层层 `if` 堆叠导致代码难读；如果必须校验，优先让边界、契约和数据结构保持清晰。
- 代码要干净直接，好读优先，不要为了抽象而抽象。
- 尽量不写三元表达式，能用 `if` 表达清楚的逻辑，优先使用 `if`。
- 不写没必要的小 helper，只有复用明显且能降低理解成本时才抽。
- 没用到的字段、方法、分支、返回值要删掉。
- 注释只解释不明显的业务约束、协议边界或复杂流程，不写重复代码字面含义的注释。

## 前端规则

### 架构与目录约定

- 以 Vue 3 + Vue CLI 为主，沿用 `components/`、`services/`、`store/`、`router/`、`views/` 的既有分层。
- 新增页面：在 `views/` 对应业务目录下创建，并在 `router/` 注册路由与 `meta.requiredAuth` 权限。
- 新增接口：在 `services/http/api/` 或 `services/cc/api/` 下按现有模块拆分，通过 `services/http/index.js` 聚合导出。
- 全局状态放 Vuex；页面局部状态用组件 `data` / `setup`；跨组件通信用 `eventBus`（`utils/eventBus.js`）或 Vuex。
- 不要擅自引入新的 UI 框架、状态管理库或构建工具。

### 设计风格

完整规范见 `DESIGN.md`；以下为代理落地时必须遵守的摘要。

**设计基调**

- 编辑型工作流界面：白底画布（`#ffffff`）+ 深墨文字（`#181d26`），留白充足，不靠渐变或背景装饰抢注意力。
- 品牌张力来自**全幅签名色块**（coral `#aa2d00`、forest `#0a2e0e`、dark navy `#181d26`、cream `#f5e9d4`
  等），用于阶段性强调，不作为小元素点缀色。
- 深度优先靠**色块对比**，阴影极少；输入框、次级按钮用 1px 发丝线边框（`#dddddd`）。

**色彩角色**

| 角色      | Token                       | 用途              |
|---------|-----------------------------|-----------------|
| 主色 / 墨字 | `primary` / `ink` `#181d26` | 主按钮背景、标题、强调文字   |
| 正文      | `body` `#333840`            | 默认段落            |
| 弱化      | `muted` `#41454d`           | 页脚、面包屑、说明       |
| 画布      | `canvas` `#ffffff`          | 页面默认背景          |
| 浅表面     | `surface-soft` `#f8fafc`    | 卡片、选中层          |
| 链接      | `link` `#1b61c9`            | 行内链接；**不是**主按钮色 |
| 语义      | `info` / `success` 等        | 提示、成功态          |

- 主按钮用近黑（`primary`），不是链接蓝。链接蓝仅用于 `text-link`。
- 签名色（coral、forest、peach、mint 等）只用于整块表面，不拆成小 badge 或 icon 底色。

**字体**

- 主系统：Haas / Haas Groot Disp；无授权字体时用 Inter Display 或 `system-ui` 替代。
- 展示标题（h1/h2）用 400–500 字重，**不靠加粗强调**；强调靠字号和色块对比。
- 正文固定 14px / 400；按钮与标签 16px / 500。
- 定价子系统单独用 Inter Display + `rounded.pill` 药丸按钮，不与主编辑系统混用。

**SQL 编辑器字体**

- 所有可编辑、只读和 Diff SQL Monaco 编辑器统一复用
  `src/components/editor/sqlEditorTypography.js` 导出的 `SQL_EDITOR_TYPOGRAPHY`，页面和组件不得另行定义一套 SQL 字体。
- 默认规范固定为 `Menlo, Monaco, "Courier New", monospace`、14px、400 字重、21px 行高、0 字间距；SQL 工作台允许用户仅调整字号，
  但不得改变统一字体族和默认字重。
- JSON 等非 SQL 编辑器不套用该规范，按各自内容类型的编辑器约定处理。

**间距与圆角**

- 间距以 4px 为基准：`xs` 8 · `md` 16 · `lg` 24 · `xl` 32 · `xxl` 48 · `section` 96。
- 大区块上下内边距优先 `section`（96px）；卡片内边距 24–48px 按层级选用。
- 圆角层级：`sm` 6px 输入框 · `md` 10px 内容卡片 · `lg` 12px 主按钮与签名卡片 · `pill` 仅定价页。

**组件形态（对照 `DESIGN.md` components 节）**

- **主按钮** `button-primary`：近黑底、白字、12px 圆角、16×24px 内边距；每视口仅一个主 CTA。
- **次按钮** `button-secondary`：白底、墨字、发丝线描边；与主按钮成对出现。
- **输入框** `text-input`：高 44px、6px 圆角、发丝线边框；聚焦用 `info-border`。
- **签名卡片** `signature-coral-card` / `hero-card-dark`：全幅色块 + 48px 内边距 + 12px 圆角。
- **功能卡片** `feature-card-tabbed` / `demo-grid-card`：浅底或 pastel 底，网格内高度可刻意错落。

**Do / Don't（来自 DESIGN.md）**

- Do：主按钮保持近黑；hero 区信任留白、不加渐变；签名色块打断长页面单调节奏；间距对齐 4px 网格。
- Don't：把链接蓝当主按钮色；展示标题加粗到 600/700；hero 加渐变/光晕背景；在定价子系统外使用 pill
  圆角；连续两个相同表面模式（如两段纯白无变化）；自行发明签名色以外的 accent 色。
- 状态只文档化 Default 与 Active/Pressed，不额外设计 hover 变体。

**与现有代码的关系**

- 样式实现落在 `styles/variables.less`、`styles/themes/` 和组件 Less 中；新 UI 应以 `DESIGN.md` token
  为目标，逐步对齐，不沿用与规范冲突的旧色值（如把链接绿当主色）。
- 暗色主题遵循 `styles/themes/dark-theme.less`，语义角色与亮色一致，色相按主题映射。

**响应式**

- 断点：Mobile `<768` · Tablet `768–1024` · Desktop `1024–1440` · Wide `>1440`（内容最大宽约 1280px 居中）。
- 触控目标：主按钮最小 48×48px；输入框高 44px。
- 窄屏优先减列数而非缩小卡片；表格改为横向滚动。

### 组件与样式

- 复用 `components/ui/`、`components/widgets/` 中已有基础组件；功能型复用看 `components/function/`。
- 只有现有组件无法表达真实交互或可访问性需求时，才新增组件。
- 样式优先用 Less，全局变量和 mixin 在 `styles/variables.less`、`styles/mixins.less`；主题通过 `store` 的 `initTheme` 和
  `styles/themes/` 管理。
- 新增或改版 UI 前先读 `DESIGN.md`，颜色、字号、间距、圆角、按钮层级按设计 token 落地。
- 已混用 View UI Plus、Ant Design Vue、Tailwind；新增 UI 优先与当前页面所在模块保持一致，避免同一区域混用多套组件库；覆盖组件库默认样式时对齐
  `DESIGN.md` 而非组件库原生色。
- 修改样式时检查移动端和常见桌面宽度，避免文案溢出、控件遮挡和布局跳动。

### 国际化

- 用户可见文案必须维护在 `src/locales/`，不要在组件、服务或 store 中硬编码展示文案。
- 新增 key 时同步维护 `en.json` 和 `zh.json`；提交前运行 `npm run check-i18n`。
- 路由 `meta.title`、表格列名、按钮、提示、错误展示均走 i18n。

### API 与数据契约

- 修改接口字段、枚举、状态或错误码时，同步检查后端 VO / API、前端 service、页面逻辑和测试。
- 后端已删除的字段，前端不要继续保留 fallback 行为。
- 在增加兼容逻辑前，先确认后端真实协议。
- 接口响应、状态机、权限判断、国际化 key 和错误码以当前真实实现为准，不要凭命名猜业务含义。
- HTTP 错误统一走 `services/formatError.js` 和 `utils/errorQueueModal` 机制，不要各页面自行弹窗处理同类错误。

### 权限与路由

- 路由 `meta.requiredAuth` 声明页面所需权限码；侧边栏菜单通过 `utils/buildSidebarMenu.js` 按权限过滤。
- 按钮级权限参考现有页面的 `v-if` / mixin 模式（如 `authMixin`），保持与后端权限码一致。

## 前后端契约

- 前后端契约必须保持一致。
- 字段删除时，要同时删除后端、前端和测试中的对应逻辑。
- 前端不要为后端已删除字段继续保留 fallback 行为。
- DM Console API 通常走 `/console/api/` 或 `/rdp/console/api/` 前缀；具体路径以 `services/http/request.js` 和现有 api
  文件为准。

## 实时连接与异步

- WebSocket 使用 `services/socket.js`（reconnecting-websocket）；关注重连、终态和资源释放。
- SQL 执行、结果导出、异步任务等长连接场景，明确 loading、取消、失败恢复和组件卸载时的清理。
- 不要把阻塞操作放在主线程导致页面卡顿；大列表注意分页或虚拟滚动。

## 测试要求

- 默认不要新增无用测试；除非用户明确要求补测试，否则不要新增测试类。
- 前端改动优先运行 `npm run lint`、`npm run check-i18n` 和相关单测（`npm run test:unit`）。
- 用户可见流程变更要做浏览器级检查。
- 测试覆盖真实风险路径：权限边界、接口异常、WebSocket 断开重连、表单校验与状态一致性。

### 可复用的前端复测流程

**目标与触发条件**

- 可重复执行的前端浏览器测试流程统一存放在仓库根目录 `tests/frontend/`。
- 按业务域组织文档，路径使用 `tests/frontend/<业务域>/<流程名>.md`；文件名使用小写 snake_case，例如
  `tests/frontend/cicd/release_flow_creation.md`。
- 一个用户流程只维护一份长期有效的文档。重复测试同一流程时更新原文档，不按日期创建测试报告、`latest-report` 或单次结果文件。
- 前端改动涉及用户可见行为、交互、布局、路由、权限、表单、状态流、文件处理或接口契约时，测试前必须查找并阅读相关流程文档，再按文档进行复测。
- 完成浏览器端功能测试后，必须创建或更新对应流程文档；新增用户流程且没有对应文档时，前端任务不得视为完整结束。
- 仅运行 lint、国际化检查、单元测试或构建，且没有验证具体浏览器业务流程时，不要求创建流程文档。
- 流程文档只记录长期可复用的环境要求、数据构造、操作步骤和预期结果，不记录执行日期、执行人、本次 PASS/FAIL 或历史测试结论。

**Chrome 执行规则**

- 浏览器级前端复测必须使用 `chrome:control-chrome` skill，并在操作 Chrome 前完整阅读和遵守该 skill。
- 所有为测试打开或控制的 Chrome 标签页必须放入名为 `codex` 的标签页组；先检查并复用已有分组，仅在不存在时创建。
- 优先复用用户 Chrome 中已有的登录状态。不得读取或检查 Cookie、本地存储、浏览器密码、Token
  或其他认证数据；流程文档也不得记录这些敏感信息，只描述所需账号角色和权限。
- 如果 Chrome 尚未登录，必须暂停并请用户在 Chrome 中完成登录；不得为了绕过登录而切换到其他浏览器。
- 默认只在本地环境或用户明确指定的测试环境执行。不得在生产环境执行创建、删除、批量修改、文件上传、并发或极限数据测试。
- 必须根据当前改动涉及的页面、组件、路由、权限、接口和状态逻辑，选择并执行受影响的测试套件；不得仅自由浏览页面后直接认定测试通过。
- 页面导航、刷新或局部重渲染后，必须重新读取当前页面状态，不得继续使用已经失效的元素引用。
- 操作目标优先按可见文本、表单标签、按钮名称、控件角色和所属区域识别，不得依赖容易变化的 DOM 层级、临时 CSS 类名或坐标。
- 流程步骤与当前页面不一致时不得猜测点击。先结合当前代码和真实页面确认行为，更新原流程文档，再从稳定初始状态重新执行相关场景。
- 执行过程中可以区分 PASS、FAIL 和 SKIP，但只在最终回复中汇总，不在仓库中生成单次测试报告。SKIP 必须说明客观原因，不能把未执行项标记为通过。

**流程文档结构**

- 每份流程文档必须包含以下章节：
    - `Purpose`：功能目标以及主要防止的回归。
    - `Scope`：涉及页面、路由、接口、状态和明确不覆盖的范围。
    - `Preconditions`：服务地址、账号角色、权限、依赖服务和稳定初始状态。
    - `Test Data`：正常、边界、极端和异常数据的构造方式、唯一标识策略以及清理方式。
    - `Suites`：按风险组织的可重复测试场景。
    - `Cleanup`：删除测试数据、恢复权限和恢复环境的方法。
    - `Skip Conditions`：允许跳过的客观条件以及替代验证方式；没有替代方式时明确记录覆盖缺口。
- 每个测试场景必须包含：稳定的场景编号、风险或目的、优先级、初始路由与状态、使用的测试数据、数据准备方法、可由 Chrome
  直接执行的操作步骤、可观察的预期结果以及恢复/清理步骤。
- 预期结果必须具体说明可观察事实，例如 URL 变化、指定文案或数据行可见、按钮进入
  loading/禁用状态、弹窗开关、校验提示、刷新后的最终数据以及是否存在遮挡或溢出；不得只写“功能正常”或“结果正确”。
- 测试数据不得依赖上一次执行遗留的固定业务 ID。需要创建数据时使用带本次唯一标识的名称，并提供可靠的定位和清理方式。
- 大量数据、深层树、批量文件等无法通过 UI 稳定构造的极限场景，应提供可重复的数据准备脚本，并在流程文档中写明脚本路径、参数、安全限制和清理方法；不得要求
  AI 在 Chrome 中手工创建大量数据。

**最低测试套件**

- `Smoke`：页面可达、入口可见、核心数据加载和主要操作可进入。
- `Main Flow`：标准用户主流程及关键业务分支。
- `Boundaries`：空值、最小值、最大值、上下限外一档、特殊字符、Unicode、重复值、数值和日期边界。
- `Extreme`：空列表、单条、分页边界、大量数据、超长内容、深层结构、极窄/极宽视口和浏览器缩放。
- `Repeat And Concurrency`：双击提交、快速连续操作、重复重试、多标签页以及旧响应覆盖新状态。
- `Failure And Recovery`：400/401/403/500、超时、断网、部分失败、重试以及网络恢复。
- `Lifecycle`：刷新、前进后退、关闭弹窗、切换路由、重新进入和长时间停留。
- `Permission`：完整权限、只读、无页面权限、无按钮权限、登录失效和权限变化。
- `State Consistency`：失败后恢复、部分成功、幂等、刷新后状态以及前端展示与服务端最终事实一致。
- 不适用的套件或场景可以标记为“不适用”，但必须写明业务或技术原因。

**边界与安全要求**

- 字段长度、数量上限、文件大小、超时时间等边界必须来自当前前端代码、后端校验或真实接口契约，不得凭经验编造。
- 存在明确上下限时，原则上覆盖最小值以下、最小值、最小值以上、正常值、最大值以下、最大值和最大值以上。
- 极限、批量、并发和破坏性场景必须说明环境隔离、数据构造和清理方式；无法安全执行时标记 SKIP 并报告原因，不得降低环境安全要求。

## Review 规则

- Review 只提真实 bug 和明确 concern。
- 不要堆风格噪音，除非它确实影响正确性、可维护性或契约清晰度。
- 优先关注状态一致性、协议破坏、权限绕过、前后端字段不一致、i18n 遗漏、样式回归和偏离 `DESIGN.md` 的色彩/字重/圆角。

## PR 与提交

- PR 说明要清楚描述改了什么、为什么改、如何验证。
- 提交信息使用 Conventional Commits，例如 `feat(frontend): add SSO settings page` 或
  `fix(sql): fix operator panel overflow`。
- 提交前确认没有把 `node_modules/`、`dist/`、日志、临时文件或无关格式化改动带入 diff。
