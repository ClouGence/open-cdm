# SQL 错误场景当前语句选择 Design QA

## Source truth

- CloudDM 问题截图：`/var/folders/y4/v2d62ljn1ss_c6gbv7c10j9h0000gn/T/codex-clipboard-0b8b90c6-2197-4ca3-9554-29066bef2679.png`，1106×492。
- DataGrip 交互参考：`/var/folders/y4/v2d62ljn1ss_c6gbv7c10j9h0000gn/T/codex-clipboard-6e92c361-baef-401e-b583-969065e98c69.png`，784×416。
- 本次目标是交互一致性：错误诊断保留时，光标所在的其他 SQL 仍可选择和执行；不复制 DataGrip 的暗色主题、语法配色或工具栏视觉。

## Implementation evidence

- 完整实现截图：`/tmp/open-cdm-sql-statement-selection-1440x900.jpg`，Chrome 视口 1440×900，设备像素密度按浏览器截图 1× 记录。
- 同屏对比图：`/tmp/open-cdm-sql-statement-selection-comparison.png`，依次包含 CloudDM 问题状态、DataGrip 目标状态和 CloudDM 实现状态。
- 测试状态：编辑器同时包含 `SELECT * FROM ;` 和其后的 `SELECT version();`；错误红线可见，当前语句框只包围 `SELECT version();`，执行结果显示仅运行该语句并返回 `8.0.44`。
- 额外视口：1024×768 与 390×844 下均保持 1 个当前语句框和 1 个错误标记；视口只用于检查状态未被响应式重排破坏。

## Comparison findings

- P0：无。错误 SQL 不再清空整个文档的当前语句能力，错误后的有效 SQL 可直接执行。
- P1：无。错误语句本身仍可选择；纯空白行不显示语句框；引号内分号不误切；快速编辑后语句框仍对应当前模型。
- P2：无。本次沿用项目现有 Monaco 字体、语法配色和低对比度绿色语句框，没有引入新的视觉体系。
- 允许差异：DataGrip 和 CloudDM 的主题、字号、工具栏、行高及选择框颜色属于各自产品设计系统，不纳入本次一致性要求。

## Comparison history

1. 初始状态：后端分割因任一语法错误返回空列表，CloudDM 无法选择文档内任何语句。
2. 修改后：后端分割成功时继续使用精确范围；失败或结果过期时按本地分号、引号和注释边界选择光标所在语句。
3. Chrome 复测：错误标记与当前语句框同时存在，执行结果与语句框一致。

final result: passed
