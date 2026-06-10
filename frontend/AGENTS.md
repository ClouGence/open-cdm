# AGENTS.md

本文件为 AI 编码代理在 open-cdm 前端工程（`frontend/`）内工作时的规则说明。内容按 CloudDM Web 前端的实际工程组织和团队规则调整。

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
  - 编辑器：Monaco Editor；图表/监控：Highcharts、自研 monitor 面板。
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
  - `cc/`：面向 RDP / CloudCanal 相关 API
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
- 品牌张力来自**全幅签名色块**（coral `#aa2d00`、forest `#0a2e0e`、dark navy `#181d26`、cream `#f5e9d4` 等），用于阶段性强调，不作为小元素点缀色。
- 深度优先靠**色块对比**，阴影极少；输入框、次级按钮用 1px 发丝线边框（`#dddddd`）。

**色彩角色**

| 角色 | Token | 用途 |
|------|-------|------|
| 主色 / 墨字 | `primary` / `ink` `#181d26` | 主按钮背景、标题、强调文字 |
| 正文 | `body` `#333840` | 默认段落 |
| 弱化 | `muted` `#41454d` | 页脚、面包屑、说明 |
| 画布 | `canvas` `#ffffff` | 页面默认背景 |
| 浅表面 | `surface-soft` `#f8fafc` | 卡片、选中层 |
| 链接 | `link` `#1b61c9` | 行内链接；**不是**主按钮色 |
| 语义 | `info` / `success` 等 | 提示、成功态 |

- 主按钮用近黑（`primary`），不是链接蓝。链接蓝仅用于 `text-link`。
- 签名色（coral、forest、peach、mint 等）只用于整块表面，不拆成小 badge 或 icon 底色。

**字体**

- 主系统：Haas / Haas Groot Disp；无授权字体时用 Inter Display 或 `system-ui` 替代。
- 展示标题（h1/h2）用 400–500 字重，**不靠加粗强调**；强调靠字号和色块对比。
- 正文固定 14px / 400；按钮与标签 16px / 500。
- 定价子系统单独用 Inter Display + `rounded.pill` 药丸按钮，不与主编辑系统混用。

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
- Don't：把链接蓝当主按钮色；展示标题加粗到 600/700；hero 加渐变/光晕背景；在定价子系统外使用 pill 圆角；连续两个相同表面模式（如两段纯白无变化）；自行发明签名色以外的 accent 色。
- 状态只文档化 Default 与 Active/Pressed，不额外设计 hover 变体。

**与现有代码的关系**

- 样式实现落在 `styles/variables.less`、`styles/themes/` 和组件 Less 中；新 UI 应以 `DESIGN.md` token 为目标，逐步对齐，不沿用与规范冲突的旧色值（如把链接绿当主色）。
- 暗色主题遵循 `styles/themes/dark-theme.less`，语义角色与亮色一致，色相按主题映射。

**响应式**

- 断点：Mobile `<768` · Tablet `768–1024` · Desktop `1024–1440` · Wide `>1440`（内容最大宽约 1280px 居中）。
- 触控目标：主按钮最小 48×48px；输入框高 44px。
- 窄屏优先减列数而非缩小卡片；表格改为横向滚动。

### 组件与样式

- 复用 `components/ui/`、`components/widgets/` 中已有基础组件；功能型复用看 `components/function/`。
- 只有现有组件无法表达真实交互或可访问性需求时，才新增组件。
- 样式优先用 Less，全局变量和 mixin 在 `styles/variables.less`、`styles/mixins.less`；主题通过 `store` 的 `initTheme` 和 `styles/themes/` 管理。
- 新增或改版 UI 前先读 `DESIGN.md`，颜色、字号、间距、圆角、按钮层级按设计 token 落地。
- 已混用 View UI Plus、Ant Design Vue、Tailwind；新增 UI 优先与当前页面所在模块保持一致，避免同一区域混用多套组件库；覆盖组件库默认样式时对齐 `DESIGN.md` 而非组件库原生色。
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
- DM Console API 通常走 `/console/api/` 或 `/rdp/console/api/` 前缀；具体路径以 `services/http/request.js` 和现有 api 文件为准。

## 实时连接与异步

- WebSocket 使用 `services/socket.js`（reconnecting-websocket）；关注重连、终态和资源释放。
- SQL 执行、结果导出、异步任务等长连接场景，明确 loading、取消、失败恢复和组件卸载时的清理。
- 不要把阻塞操作放在主线程导致页面卡顿；大列表注意分页或虚拟滚动。

## 测试要求

- 默认不要新增无用测试；除非用户明确要求补测试，否则不要新增测试类。
- 前端改动优先运行 `npm run lint`、`npm run check-i18n` 和相关单测（`npm run test:unit`）。
- 用户可见流程变更要做浏览器级检查。
- 测试覆盖真实风险路径：权限边界、接口异常、WebSocket 断开重连、表单校验与状态一致性。

## Review 规则

- Review 只提真实 bug 和明确 concern。
- 不要堆风格噪音，除非它确实影响正确性、可维护性或契约清晰度。
- 优先关注状态一致性、协议破坏、权限绕过、前后端字段不一致、i18n 遗漏、样式回归和偏离 `DESIGN.md` 的色彩/字重/圆角。

## PR 与提交

- PR 说明要清楚描述改了什么、为什么改、如何验证。
- 提交信息使用 Conventional Commits，例如 `feat(frontend): add SSO settings page` 或 `fix(sql): fix operator panel overflow`。
- 提交前确认没有把 `node_modules/`、`dist/`、日志、临时文件或无关格式化改动带入 diff。
