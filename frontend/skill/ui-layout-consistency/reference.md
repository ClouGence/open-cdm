# UI 一致性 — 参考手册

本文件是 [SKILL.md](./SKILL.md) 的补充：示意图、token 速查、反例库、代码片段。

---

## 1. 扁平化风格速查

### 视觉关键词

编辑型 · 白底画布 · 墨字 · 留白 · 浅表面 · 发丝线 · 轻阴影 · 单主 CTA

### 深度来源（能用 A 就不用 B）

| A 优先                | B 谨慎              | C 禁止           |
| --------------------- | ------------------- | ---------------- |
| 32px 区块 gap         | 浅底 `page-aside`   | 区块 border 卡   |
| `page-section__title` | 表格/输入 border    | 多层 box-shadow  |
| 面包屑层级            | 页脚一条 top border | 渐变背景分区     |
| 近黑主按钮            | type-card 选中描边  | fixed 全屏操作条 |

### 与 DESIGN.md 的关系

- **SKILL** = 产品结构与「能不能这么画」的硬性规则
- **DESIGN.md** = 色值、字号、圆角 token 明细
- 冲突时：**SKILL 结构与少线框原则优先**；色值以 DESIGN token 为准

---

## 2. 壳层 ASCII

```
┌──────────┬──────────────────────────────────────────────┐
│ Sidebar  │ app-main-card                                │
│          │ ┌──────────────────────────────────────────┐ │
│ 实例     │ │ Header  面包屑 / 标题      [系统操作区]  │ │
│ CI/CD    │ ├──────────────────────────────────────────┤ │
│ …        │ │ page-shell__body                         │ │
│          │ │   page-section                           │ │
│          │ │   page-section                           │ │
│          │ │   page-aside (浅底，无线框)              │ │
│          │ ├──────────────────────────────────────────┤ │
│          │ │ page-shell__footer   [次] [主]           │ │
│          │ └──────────────────────────────────────────┘ │
└──────────┴──────────────────────────────────────────────┘
```

**系统操作区（Header 右）：** 数据查询、文档、联系我们、语言、用户 — 永远不放「保存/提交」。

---

## 3. 页面类型对照

| 类型            | 路由示例          | 参考文件                             |
| --------------- | ----------------- | ------------------------------------ |
| 列表 + Tab      | `/ticket`         | `views/ticket/index.vue`             |
| 列表无 Tab      | `/datasource`     | `views/dataSource/DataSource.vue`    |
| 单页表单 + Tab  | `/datasource/add` | `views/dataSource/AddDataSource.vue` |
| 向导 + 侧栏摘要 | `/cicd/create`    | `views/cicd/ReleaseFlowPage.vue`     |
| SQL 全屏        | `/sql`            | `views/home/index.vue`（sql-layout） |

---

## 4. Token 速查（工作台常用）

| Token                       | 值        | 用途                  |
| --------------------------- | --------- | --------------------- |
| canvas                      | `#ffffff` | 主画布、app-main-card |
| ink / primary               | `#181d26` | 主按钮、标题          |
| body                        | `#333840` | 正文                  |
| muted                       | `#41454d` | 辅助文案              |
| surface-soft / bg-secondary | `#f8fafc` | page-aside、浅表面    |
| hairline                    | `#dddddd` | 输入框、次按钮边框    |
| link                        | `#1b61c9` | 行内链接（非主按钮）  |

CSS 变量优先：`var(--text-primary)`、`var(--bg-secondary)`、`var(--border-primary)`、`var(--primary-color)`。

---

## 5. 标准类名与文件

| 类 / 组件                   | 文件                                         |
| --------------------------- | -------------------------------------------- |
| `.page-shell`               | `src/styles/app-shell.less`                  |
| `.page-section` / `__title` | `src/styles/app-shell.less`                  |
| `.page-aside`               | `src/styles/app-shell.less`                  |
| `.app-page-tabs`            | `src/styles/app-shell.less`                  |
| `AppPageTabs`               | `src/components/layout/AppPageTabs.vue`      |
| `AppContentHeader`          | `src/components/layout/AppContentHeader.vue` |
| `.table-list-layout`        | `src/styles/global.less`                     |

---

## 6. 代码片段

### page-shell 表单页

```vue
<div class="page-shell">
  <div class="page-shell__body">
    <AppPageTabs v-model="activeKey" :tabs="tabs" />
    <div class="page-panel-body">...</div>
  </div>
  <div class="page-shell__footer">
    <Button>{{ $t('qu-xiao') }}</Button>
    <Button type="primary">{{ $t('bao-cun') }}</Button>
  </div>
</div>
```

### page-section + 侧栏

```vue
<div class="release-flow-shell">
  <div class="release-flow-main">
    <section class="page-section">
      <div class="page-section__title">{{ $t('ji-ben-xin-xi') }}</div>
      <!-- 表单 -->
    </section>
  </div>
  <aside class="page-aside">...</aside>
</div>
```

```less
.release-flow-shell {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 320px;
  gap: 24px;
  padding: 20px 24px;
  align-items: start;
}
.release-flow-main {
  display: flex;
  flex-direction: column;
  gap: 32px;
}
```

---

## 7. 反例库（禁止复制）

### A. 卡片套卡片 + 第二背景

```less
/* 反例 */
.page {
  background: #f5f8fb;
}
.wrapper {
  margin: 20px;
  background: #fff;
  border: 1px solid...;
  box-shadow: ...;
}
```

**改法：** 去掉 `.page` 背景与 `.wrapper` 线框，直接用 `page-shell`。

---

### B. fixed 全屏页脚

```less
/* 反例 */
.page-footer {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
}
```

**改法：** `page-shell__footer`，作为 flex 子项。

---

### C. 自定义 Tabs 皮肤

View UI `<Tabs>` + 10 条 `:deep(.ivu-tabs-*)` + Tab 容器 `border-bottom`。

**改法：** `AppPageTabs` + `page-panel-body`。

---

### D. 区块全线框

基本信息、发布配置、摘要各包 `border: 1px solid; border-radius: 10px; box-shadow: ...`。

**改法：** `page-section`（无线框）+ `page-aside`（浅底无线框）。

---

### E. 重复大标题

Header 已有「CI/CD / 创建变更流」，内容区再来 `22px/700` accent-title。

**改法：** 区块仅用 `page-section__title` 16px/500。

---

### F. 多层 padding

`app-main-body 16px` + `wrapper margin 20px` + `section padding 28px 32px`。

**改法：** 单层 `page-shell__body padding 16px 24px`，区块 gap 32px。

---

### G. 链接蓝主按钮

`<Button type="primary">` 被覆盖成 `#1b61c9`。

**改法：** 主按钮近黑；蓝色仅 `text-link`。

---

### H. 渐变 / 重阴影 hero

内容区 `linear-gradient` 背景或 `box-shadow: 0 10px 28px` 分区。

**改法：** 白底 + 间距；阴影仅 card 外层轻量。

---

## 8. 协作者常见误区

| 误区                           | 正确做法                                |
| ------------------------------ | --------------------------------------- |
| 「每个模块一个 Card 组件」     | 一个 app-main-card，模块用 page-section |
| 「底部按钮要吸底才好用」       | page-shell\_\_footer 在 card 内即可     |
| 「Tab 用组件库默认就一致」     | 必须 AppPageTabs                        |
| 「多包一层 padding 更安全」    | 对齐 16/24/32，不叠加大 padding         |
| 「阴影让层次更清晰」           | 扁平产品靠留白，不靠 shadow             |
| 「每个字段区加 border 更清晰」 | 仅控件级 border                         |

---

## 9. 目视对照清单（改完必做）

1.  squint test：眯眼看是否「满屏格子」→ 若是，减 border
2.  侧栏与主区是否只有一个外框 + 一块浅底
3.  主按钮是否只有一个、近黑
4.  Tab 下是否无满宽灰线
5.  页脚是否不盖住左侧菜单
6.  与 `/ticket`、`/datasource/add`、`/cicd/create` 并排对比是否同「家族感」
