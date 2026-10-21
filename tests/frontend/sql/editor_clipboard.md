# 编辑器 HTTP 剪贴板

## Purpose

验证全部 Monaco 编辑器在局域网 HTTP 下可初始化、输入与复制，防止 Safari/WebKit 的剪贴板兼容监听器直接访问不存在的 Async Clipboard API。

## Scope

- `/#/sql` 的可编辑 SQL、AI 只读 SQL；工单编辑器、只读编辑器、SQL Diff 和 CI/CD 变更 Diff。
- 统一入口 `frontend/src/components/editor/monaco.js`，在任何编辑器初始化前注册共享剪贴板服务。
- 不修改依赖文件，不改变浏览器安全设置，不执行 SQL；AI 普通文本复制使用 `ai_sql_generation.md` 的对应场景。

## Preconditions

- IDEA Alone 服务健康，@Browser 已登录测试账号。
- 使用本机局域网 HTTP 地址与 localhost 对照；WebKit 分支需要可用的 Safari/WebKit 浏览器工具，不能通过脚本伪造 userAgent 代替真实浏览器。
- 只读和 Diff 使用已有非敏感测试工单/变更记录，权限不足时不创建或更改业务权限。

## Test Data

- 新建临时查询，输入 `-- 剪贴板 α`、`SELECT 417;`、`SELECT 418;`，实际换行。
- 只读 SQL 使用已有常量 SELECT 会话；Diff 使用已有测试变更，不提交或执行。
- 原查询正文、字号、选中页签和连接在测试前记录，仅清理本轮临时查询。

## Suites

### EDITOR-CLIP-HTTP-01 HTTP 可编辑 SQL

- 目的/优先级：P0，防止每次点击和按键触发剪贴板异常。
- 初始状态：`/#/sql`，通过 LAN HTTP 打开独立临时查询。
- 准备：输入上述多行测试 SQL。
- @Browser 操作：点击正文、方向键移动、选择多行，用系统复制/粘贴快捷键在空临时查询核对；撤销、重做、切换查询、刷新后重复。localhost 重复同样操作。
- 预期：编辑器正常初始化，输入和选区保持，粘贴内容逐字一致，无自动执行；没有 `navigator.clipboard`、`ClipboardItem` 或 `write` 不存在的异常。HTTP 使用已有文本复制回退，键盘粘贴仍由浏览器原生处理。
- 清理：关闭临时查询，恢复原页签和连接。

### EDITOR-CLIP-WEBKIT-01 Safari/WebKit 与安全上下文

- 目的/优先级：P0，验证第三方 WebKit 兼容监听器仅在相应 API 可用时启用。
- 初始状态：Safari/WebKit 中以 LAN HTTP 打开查询，另以 localhost 或 HTTPS 打开对照。
- 准备：与 EDITOR-CLIP-HTTP-01 相同的独立临时数据。
- 操作：重复点击、按键、复制粘贴与刷新；安全上下文中复制后立即粘贴核对，不修改浏览器权限。
- 预期：HTTP 缺少异步图片剪贴板能力时不会注册有问题的 WebKit 监听器；安全上下文保留原有用户激活兼容实现，复制仍可用，没有新增异常。
- 清理：关闭临时查询并恢复原窗口。

### EDITOR-CLIP-READONLY-01 只读和 Diff

- 目的/优先级：P0，防止第一个打开的编辑器绕过统一注册，或共享服务重复初始化。
- 初始状态：全新 @Browser 测试标签，先打开只读 SQL 或 Diff，再进入可编辑 SQL；另一个标签先打开可编辑 SQL，再打开只读/Diff。
- 准备：已有常量 SQL 会话及非敏感测试工单/变更记录。
- 操作：读取、选择和复制两侧 SQL；关闭重开、切换正文并刷新，核对字体、滚动和只读性。
- 预期：各入口均正常创建，Diff 两侧内容正确，禁止编辑的区域保持只读；初始化顺序不影响剪贴板服务，无重复事件或 HTTP 剪贴板异常。
- 清理：关闭只读/Diff 弹窗与测试标签，不修改原记录。

## Cleanup

- 关闭本轮临时查询与测试标签，恢复原正文、页签、字号和浏览器视口。
- 不执行测试 SQL，不改变浏览器安全来源、HTTPS 证书信任或权限配置。

## Skip Conditions

- 缺少 WebKit 工具时，Safari/WebKit 运行态标记 SKIP；用实际依赖类和项目子类的隔离验证检查 HTTP 分支和安全上下文分支，不能宣称 Safari 实测通过。
- 无只读/Diff 测试记录或相应权限时，对应业务入口 SKIP，检查所有 Monaco 导入均使用统一入口。
- 工具无法读回原生复制内容时，实际剪贴板逐字一致性标记 BLOCKED，不以按钮反馈替代。
- 登录、Browser 或服务不可用时，依赖场景 BLOCKED。
