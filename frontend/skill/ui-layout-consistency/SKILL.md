---
name: ui-layout-consistency
description: >-
  CloudDM Web 产品 UI 一致性规范：扁平化编辑型 B 端、壳层布局、少线框分区、组件与 token。
  新增或改版任何用户可见 UI 时必须优先阅读；无法满足则中断改动。
---

# CloudDM 产品 UI 一致性规范

**优先级：最高。** 本规范优先于组件库默认样式、个人习惯、以及零散页面旧实现。

**说明：** 本文件是目标设计标准；存量页面尚未全量对齐，统一改版将另行推进。新增或改版 UI 时按本规范执行，勿复制旧页面的嵌套卡片、fixed 页脚等写法。

**定位：** CloudDM 是 **扁平化、留白充足的编辑型 B 端 SaaS**，不是卡片堆叠风、不是拟物/玻璃态、不是运营落地页。

涉及以下任一场景时，**必须先阅读本 skill**：
- 新增/改版 `views/`、`components/layout/`、弹窗、表单、列表、向导页
- 调整颜色、字号、间距、阴影、描边、Tab、页脚、面包屑
- 后端或协作者提交的前端 UI 改动

**若改动无法在不破坏规范的前提下完成，停止提交，向用户说明具体违反项与建议改法。**

Token 细节见 `DESIGN.md`；布局反例与代码片段见 [reference.md](./reference.md)。

---

## 0. 设计基调（必须先理解）

### 我们是什么风格

| 是 | 不是 |
|----|------|
| 扁平化产品 UI：白底 + 墨字 + 大量留白 | 每层内容都套 `border + shadow` 的「卡片套卡片」 |
| 深度靠 **对比与间距**，阴影极少 | 重投影、渐变背景、光晕 hero |
| 一个工作区外框（`app-main-card`）承载页面 | 页面内再铺 `#f5f8fb` 第二层背景 + 独立浮卡 |
| 区块用 **标题 + gap + 浅底** 分区 | 每个区块再画一圈线框 |
| 主按钮近黑实心；链接才用链接蓝 | 把链接蓝当主 CTA |
| 展示标题靠字号/字重 400–500，不靠 700  Bold 堆叠 | 满屏粗体标题 |
| 签名色（coral/forest 等）用于 **整幅色块**（登录/营销/阶段性强调） | 把签名色拆成小 badge、icon 底色 |

### 层次怎么建（优先级从高到低）

1. **壳层**：侧栏 + 唯一浮层工作区卡片  
2. **间距**：4px 网格 — 8 / 16 / 24 / 32  
3. **标题**：`page-section__title`、面包屑、表头  
4. **浅表面**：`page-aside`、`surface-soft`（`#f8fafc`），**无 border**  
5. **发丝线**：仅输入框、表格、页脚顶线、可选 chip 激活态  
6. **轻阴影**：仅 `app-main-card` 外层 `0 1px 3px` 级别  

**禁止用第 5、6 项去补本可用第 2、3、4 项表达的层次。**

### 一句话自检

> 拿掉所有 `border` 和 `box-shadow` 后，页面是否仍能通过留白和标题读懂结构？  
> 若不能，先加间距/标题/浅底，而不是加线框。

---

## 1. 应用壳层（App Shell）

```
左侧 AppSidebar（全局导航）
└─ 右侧 app-main
   └─ app-main-card（唯一浮层卡片 — 全页唯一「容器感」）
      ├─ AppContentHeader
      │    左：页面标题 / 面包屑（业务上下文）
      │    右：系统级操作（数据查询、文档、联系我们、语言、用户）
      └─ app-main-card__body（router-view）
```

**规则：**
- 业务内容只在 `app-main-card__body` 内渲染。
- **禁止**在 body 内再套「第二个大卡片」（白底 + 圆角 + 阴影 + 外边距 + 独立背景色）。
- 系统级操作不得与页面保存/提交按钮出现在同一视觉带。
- SQL 全屏模式是例外：独立 compact header，仍遵循扁平、少 chrome。

**参考：** `src/views/home/index.vue`、`src/views/dataSource/DataSource.vue`

---

## 2. 页面内结构（Page Shell）

```
page-shell（flex 列，height 100%，min-height 0）
├─ page-shell__body（唯一滚动区，padding 16px 24px）
└─ page-shell__footer（可选，页内主操作）
```

| 规则 | 说明 |
|------|------|
| 滚动 | 只发生在 `page-shell__body`，不要整页 + 内层双滚动 |
| 页脚位置 | 必须是 card 内 flex 子项 |
| 禁止 fixed 全屏底栏 | 不得 `position: fixed; left: 0; right: 0` 压过侧栏 |
| 页脚样式 | `border-top` 一条 + 按钮组；无外扩阴影 |

**样式：** `src/styles/app-shell.less` → `.page-shell`、`.page-shell__footer`

---

## 3. 内容分区（少线框）

`app-main-card` 已是唯一容器线框；**内部默认无 border 分区**。

```
page-shell__body
├─ 主列（flex column，gap: 32px）
│   ├─ page-section + page-section__title
│   └─ page-section
└─ page-aside（可选，浅底摘要/说明）
```

| 类名 | 用途 | border | 背景 |
|------|------|--------|------|
| `page-section` | 表单块、配置块、列表工具区上部的筛选区 | 无 | 透明（继承 card 白底） |
| `page-section__title` | **同级区块**标题（基本信息、发布流程配置），左 3px 色条 + 16px/500 | 无 | — |
| `panel-subheading` | 区块**内部**列标题（发布源、发布数据库），14px/500 + 可选图标，无左色条 | 无 | — |
| `page-aside` | 配置摘要、辅助只读信息 | 无 | `var(--bg-secondary)` |
| `page-panel-body` | Tab 面板内容区 padding | 无 | — |

**允许 border 的元素：** 输入/Select、表格、分页、可选 type-card/chip、页脚顶线。  
**禁止：** 每个 section 再包 `1px solid` 白底圆角盒；摘要区做成第二个线框卡片。

**参考：** `src/views/cicd/ReleaseFlowPage.vue`、`src/views/dataSource/AddDataSource.vue`

---

## 4. 页面类型模板

改页面前先判断类型，**复用对应模板**，不要发明新布局。

### A. 列表页（实例、工单、角色…）

```
page-shell
└─ body: table-list-layout
   ├─ AppPageTabs（若有子视图）
   ├─ 工具栏（筛选左 + 主操作右，同一行）
   ├─ Table（border 由表格组件承担）
   └─ 分页 footer（表格下方，非 fixed）
```

- 不要给表格外再套带 shadow 的 card。
- 参考：`src/views/ticket/index.vue`、`src/views/dataSource/DataSource.vue`

### B. 单页表单（新增数据源、编辑配置…）

```
page-shell
├─ body
│   ├─ AppPageTabs（多 panel 时）
│   ├─ page-panel-body 或 page-section
│   └─ page-aside（可选）
└─ footer: [次按钮] [主按钮]
```

- Tab 下无满宽灰线；仅激活 Tab 短下划线。
- 参考：`src/views/dataSource/AddDataSource.vue`

### C. 多区块向导 / 创建流（CI/CD 创建…）

```
page-shell
├─ body: grid(主列 minmax(0,1fr) + page-aside 280–320px)
│   ├─ 主列：多个 page-section 纵向 gap 32px
│   └─ 侧栏：page-aside 只读摘要
└─ footer: [上一步] [下一步/提交]
```

- 主列区块之间 **不要** 再画线框；双列发布源/库配置用 grid + 标题，不用两个 bordered panel。
- 参考：`src/views/cicd/ReleaseFlowPage.vue`

### D. 详情页（工单详情、变更流详情…）

- 头部信息用 **描述列表 / 标题 + meta 行**，不用大 shadow 卡片包一行字段。
- 长内容 Tab 化时用 `AppPageTabs`。
- 行内操作为 text-link 或 ghost 按钮，不与页级主 CTA 并列抢焦点。

### E. SQL 全屏工作台

- 脱离 `app-main-card` 布局；header 仅品牌 + 返回工作台 + 系统操作。
- 仍保持扁平：无渐变 header、无 heavy shadow。

---

## 5. 间距与尺寸

基于 **4px 网格**，禁止随意 `28px 32px 36px` 堆叠。

| 场景 | 值 |
|------|-----|
| `app-main-body` 外圈 | 已有 ~16px，不再叠加 |
| `page-shell__body` padding | `16px 24px` |
| 区块之间 gap | `24px` 或 `32px` |
| 标题下间距 | `16px` |
| 表单项之间 | `16px`（View UI 默认 margin 需收敛，避免 26px+） |
| 圆角 | 输入 6px · 内容区 10px · 主按钮 12px（见 DESIGN.md） |

**禁止：** body 内 `margin: 20px` 造第二容器；多层 padding 相加 > 48px。

---

## 6.  typography 与标题

| 层级 | 规格 | 用途 |
|------|------|------|
| 页面标题 | AppContentHeader 面包屑末级 · ~18px · 600 | 当前页上下文 |
| 区块标题 | `page-section__title` · 16px · 500 | 基本信息、发布配置 |
| 正文 | 14px · 400 | 表单 label、表格、说明 |
| 弱化 | 13px · muted 色 | 辅助说明、placeholder |
| 展示 h1/h2（营销/登录） | 400–500，不加粗到 700 | 非工作台主流程 |

- 面包屑用 `/` 分隔；末级为 current，不可点。
- **禁止**页面内再做一个 22px/700 的「假页面标题」与 header 重复。

---

## 7. 色彩与按钮

| 角色 | 规则 |
|------|------|
| 主 CTA | 近黑 `primary` 实心；每视口 **一个** |
| 次按钮 | 白底 + 发丝线描边 `button-secondary` |
| 链接 | 链接蓝，仅 inline 导航/表格「查看」 |
| 危险 | 删除等用 error 语义，不滥用 |
| 成功/状态 | 语义色文字或 tag，不大面积铺底 |

View UI / Ant Design 默认色 **必须覆盖到 token**，不要直接沿用组件库绿/蓝主色 if 与 DESIGN 冲突。

---

## 8. Tab

**唯一标准：** `AppPageTabs`（`src/components/layout/AppPageTabs.vue`）

- 文字 Tab + **仅激活项** 底部 2px 指示线（primary 色）
- Tab 容器 **无** `border-bottom` 满宽灰线
- 禁止页面内 `<Tabs>` + 大量 `:deep(.ivu-tabs-*)` 重皮肤
- 面板内容放 `page-panel-body`，padding 与 body 对齐

---

## 9. 表格与空态

- 列表优先 `table-list-layout` + 现有 Table 封装。
- 表格 border 由组件承担，表格外不再套 card。
- 空态：简洁文案 + 一个主 CTA，不用插图堆满卡片。
- 加载：`Spin` 在 `page-shell__body` 内居中或表格 loading，不用全屏遮罩除非阻塞提交。

---

## 10. 弹窗与抽屉

- 标题 + 内容 + footer 按钮；footer 右对齐 [取消] [确定]。
- 弹窗内 **不再套** 带 shadow 的大 card；表单项间距与页面一致。
- 危险操作二次确认走现有 `CCModal` / `second-confirm-modal` 模式。

---

## 11. 组件与样式来源优先级

1. `src/components/layout/` — AppSidebar、AppContentHeader、AppPageTabs  
2. 同类型已有页面（工单、实例、数据源、CI/CD 创建）  
3. `src/styles/app-shell.less` + `styles/variables.less`  
4. `DESIGN.md` token  
5. View UI Plus / Ant Design Vue — **仅行为**，样式对齐 token  

**不得**因赶进度新建：自定义 Tab 皮肤、fixed 全屏 footer、页面级第二背景层、独立 accent-title 体系。

---

## 12. 改 UI 前的决策流程

```
1. 属于哪种页面类型？（列表 / 表单 / 向导 / 详情 / SQL 全屏）
   → 套用 §4 模板

2. 内容是否已在 app-main-card 内？
   → 否：先修正壳层

3. 是否需要多个视觉区块？
   → 是：page-section + gap，不用 border 卡

4. 是否需要侧栏摘要？
   → 是：page-aside 浅底，不用 border 卡

5. 主操作放哪？
   → page-shell__footer 或列表工具栏；禁止 fixed 全屏

6. 是否需要 Tab？
   → AppPageTabs

7. 拿掉 border/shadow 后结构是否仍清晰？
   → 否：加间距/标题/浅底，不加线框
```

---

## 13. 提交前自检清单

**风格**
- [ ] 扁平：无渐变 hero、无多层 shadow 卡片
- [ ] 层次靠间距/标题/浅底，不靠堆线框
- [ ] 主 CTA 只有一个，近黑 primary

**壳层与布局**
- [ ] 无第二个大卡片/第二背景层
- [ ] page-shell 结构正确，滚动单一路径
- [ ] 页脚在 card 内，非 fixed 全屏

**组件**
- [ ] Tab 用 AppPageTabs，无满宽 Tab 底边
- [ ] 区块用 page-section / page-aside
- [ ] padding/gap 在规范档位内

**工程**
- [ ] 文案 i18n
- [ ] 未引入新 UI 框架
- [ ] 对照参考页目视检查

**任一未通过 → 中断改动**，先对齐规范再继续。
