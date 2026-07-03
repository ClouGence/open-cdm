---
id: open_cdm_3_1_0
title: Open CDM 3.1.0
sidebar_label: v3.1.0
description: 优化初始化升级、登录与 SSO、驱动准备和 Docker 打包体验
date: 2026-05-30
tags: [版本更新, 初始化升级, 登录优化, SSO, Docker, 驱动]
---

- 发版时间: 2026 年 5 月 30 日
- 版本号: v3.1.0

## 产品介绍

CloudDM 是一款免费且开源的团队化数据库管理工具，提供统一 Web 数据库访问、权限控制、数据脱敏、SQL 审核、流程协同和数据库 CI/CD 等能力。

## 快速体验

```bash
# 默认镜像
docker run -d --name cgdm-alone -p 8222:8222 \
  -v cgdm_alone_conf:/root/cgdm/alone/conf \
  -v cgdm_alone_logs:/root/cgdm/alone/logs \
  -v cgdm_alone_data:/root/cgdm/alone/data \
  -v cgdm_mysql_data:/var/lib/mysql \
  bladepipe/cgdm-alone:3.1.0

# 中国区加速镜像
docker run -d --name cgdm-alone -p 8222:8222 \
  -v cgdm_alone_conf:/root/cgdm/alone/conf \
  -v cgdm_alone_logs:/root/cgdm/alone/logs \
  -v cgdm_alone_data:/root/cgdm/alone/data \
  -v cgdm_mysql_data:/var/lib/mysql \
  cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-alone:3.1.0
```

启动后访问：

```text
http://localhost:8222
```

首次部署访问会进入初始化向导；升级时会进入升级向导。

## 更新亮点

- 优化初始化和升级体验，补充 MySQL 运行时驱动准备流程、下载进度展示和失败详情定位。
- 优化登录和统一认证体验，区分子账号登录与管理登录，并改进 SSO、OIDC、LDAP、AD 登录链路。
- 优化驱动下载和驱动隔离能力，减少驱动缺失、下载失败和多版本驱动冲突带来的排障成本。
- 修复 Oracle 23ai 数据源兼容性问题，并补充 Oracle 系统维护 Schema 过滤配置。
- 构建 DAO 层并推进产品模块标准化命名，减少服务层直接注入 mapper，统一数据访问和领域命名。
- 优化 Docker 打包和文档示例，补充持久化卷、快速升级、跨平台构建和镜像源参数。

## 新增

- 新增初始化 MySQL 驱动准备流程，初始化阶段会先确认运行时 MySQL 驱动可用。
- 新增初始化 MySQL 驱动下载进度页面和 WebSocket 进度推送。
- 新增 SSO 首次登录页内补全模式，支持用户补全手机号、邮箱等信息后继续登录。
- 新增登录页 `defaultLogin=manage` 参数，可直接进入管理登录入口。
- 新增前端路由权限元信息，角色、数据源、偏好设置等入口支持 URL 级权限拦截。
- 新增 Oracle `excludeOraMaintainedSchemas` 配置，用于控制是否过滤 Oracle 系统维护 Schema（[#14](https://github.com/ClouGence/open-cdm/issues/14)）。
- 新增 `all_build.sh plugin <module>` 单插件构建入口。
- 新增 Docker 构建 `--mirrors` 参数，支持构建时使用内置 Ubuntu 镜像源。
- 新增 FAQ、README 和 DEPLOY 文档补充，完善快速启动、快速升级和部署说明。

## 优化

- 优化登录页默认入口和文案，默认进入子账号登录，管理账号通过独立管理登录入口进入。
- 优化 SSO / OIDC 回调处理，登录失败原因可直接展示在登录页，首次补全时可准确定位主账号。
- 优化 LDAP / AD 登录实现，使用 JNDI 原生上下文进行查询和认证，并补充特殊字符转义。
- 优化 LDAP / AD 错误信息，账号不存在、密码错误、账号禁用、账号锁定、密码过期、多用户匹配等场景会返回更明确的业务错误。
- 优化初始化执行失败展示，脚本失败或全局错误详情出现时自动展开并定位到错误位置。
- 优化驱动下载失败信息，优先展示根因和 Maven transfer 上下文。
- 优化普通数据源驱动下载进度展示，前端可按用户接收并展示当前文件、进度、摘要和详情。
- 优化 PG 等数据源驱动隔离和生命周期管理，降低不同驱动版本之间的类加载冲突。
- 优化权限服务代码边界，收敛资源权限、角色权限、数据源权限过滤等权限判断逻辑。
- 优化 Alone、Console、Sidecar 关闭流程，关闭 Spring 默认 shutdown hook，统一使用公共关闭等待机制。
- 优化产品模块标准化命名，将认证、审批、系统、数据源、执行、监控等领域归一到 Open CDM 产品语义。
- 优化构建 DAO 层，将 mapper、DO、枚举、类型处理器等收敛到统一数据访问模块，减轻服务注入。
- 优化 Docker 快速启动和快速升级示例，增加配置、日志、应用数据和 embedded MySQL 数据卷。
- 优化 Docker 构建脚本，使用 buildx 支持 x86_64 / arm64 平台构建，并支持内置镜像源参数。

## 修复

- 修复单机 Docker 初始化后重启或升级复用 embedded MySQL 数据卷时，启动脚本仍按无密码 root 连接导致 `ERROR 1045 (28000)` 的问题（[#21](https://github.com/ClouGence/open-cdm/issues/21)）。
- 修复 MySQL 驱动未下载时仍可能被内部依赖命中，导致驱动选择无效的问题（[#5](https://github.com/ClouGence/open-cdm/issues/5)）。
- 修复 PostgreSQL 驱动隔离异常导致配置或查询时报 `NoClassDefFoundError`、`ClassCastException` 的问题（[#29](https://github.com/ClouGence/open-cdm/issues/29)、[#31](https://github.com/ClouGence/open-cdm/issues/31)、[#41](https://github.com/ClouGence/open-cdm/issues/41)）。
- 修复导出空查询结果时可能出现除零 `ArithmeticException` 的问题（[#40](https://github.com/ClouGence/open-cdm/issues/40)）。
- 修复带查询参数的登录页地址 `#/login?...` 被前端误判为非登录页的问题（[#42](https://github.com/ClouGence/open-cdm/issues/42)）。
- 修复初始化完成后进入普通登录入口，导致用户还需手动切换到管理登录的问题（[#42](https://github.com/ClouGence/open-cdm/issues/42)）。
- 修复 SSO 首次登录补全、OIDC 回调账号展示和已绑定账号再次登录状态不准确的问题（[#42](https://github.com/ClouGence/open-cdm/issues/42)）。
- 修复 LDAP 对接后账号无法登录的问题（[#33](https://github.com/ClouGence/open-cdm/issues/33)）。
- 修复 WebSocket 在未登录或登录失效后仍可能创建、重连或发送消息的问题（[#42](https://github.com/ClouGence/open-cdm/issues/42)）。
- 修复 OIDC 用户退出时未主动关闭 WebSocket 的问题（[#42](https://github.com/ClouGence/open-cdm/issues/42)）。
- 修复系统偏好设置、角色、数据源等入口仅隐藏菜单但 URL 仍可能访问的问题（[#42](https://github.com/ClouGence/open-cdm/issues/42)）。
- 修复 Oracle 23ai 数据源兼容性问题（[#27](https://github.com/ClouGence/open-cdm/issues/27)）。
- 修复修改数据源参数时可能出现 `DataSource (0) not exist` 的问题（[#13](https://github.com/ClouGence/open-cdm/issues/13)）。
- 修复 Oracle 数据源配置修改后部分参数未正确回显或保存的问题。
- 修复 SQL 工单无法使用内置流程、无法配置外部流程审核的问题（[#18](https://github.com/ClouGence/open-cdm/issues/18)、[#22](https://github.com/ClouGence/open-cdm/issues/22)）。
- 修复初始化 MySQL 驱动、普通数据源驱动下载失败时错误信息过短，不便定位根因的问题（[#42](https://github.com/ClouGence/open-cdm/issues/42)）。
