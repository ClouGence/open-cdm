---
applyTo: "**"
description: "Use when working anywhere in CloudDM. Mandatory rule: check relevant files under .document before making architectural claims, startup/build guidance, datasource/plugin changes, or non-trivial code edits."
---

# Document-First Rule For CloudDM

在 CloudDM 仓库中，`.document` 是必须优先检查的项目知识源。

## Mandatory Workflow

- 在开始任何非琐碎任务前，先判断是否有对应的 `.document` 文档。
- 若存在相关文档，必须先读取，再继续代码搜索、实现修改、命令建议或架构判断。
- 如果任务横跨多个主题，必须组合读取多份文档，而不是只读一份。
- 如果没有找到相关文档，才能继续依赖代码搜索和局部实现推断。

## Required Document Mapping

- 启动、调试、构建、打包、运行入口、测试账号：`.document/dev/README.md`
- 产品定位、仓库范围、模块组成：`.document/horses/02-项目介绍.md`
- 总体边界、允许/禁止行为：`.document/horses/01-总体原则.md`
- 技术架构、平台分层、跨层边界：`.document/horses/03-项目技术架构.md`
- 插件体系、PluginLoader、Plugin SDK、Binder：`.document/horses/04-插件化体系.md`
- 数据源架构、Sidecar 访问链路、Driver family、DriverBinding、DriverLoader：`.document/horses/05-数据源架构.md`
- 代码风格、格式化、最小改动要求：`.document/horses/06-代码风格和格式化风格.md`

## Required Behavior

- 不要跳过相关 `.document` 文档直接回答。
- 不要给出与 `.document` 明显冲突的产品、架构或职责描述。
- 不要因为局部代码看起来可行，就越过文档定义的模块边界。
- 不要把局部实现细节上升成全局规范。
- 不要在风格问题上引入新的团队规范，应以 `.document/horses/06-代码风格和格式化风格.md` 和现有文件风格为准。

## If Docs And Code Differ

- 先明确指出文档与代码存在差异。
- 实施修改时以当前代码和用户要求为准。
- 不要静默忽略文档，也不要静默重写文档含义。
