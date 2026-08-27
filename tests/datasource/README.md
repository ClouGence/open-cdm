# 数据源专项测试

`tests/datasource/` 统一管理单个数据源的测试矩阵和复测说明。

目录约定：

```text
tests/datasource/<datasource>/
├── README.md                 # 环境要求和复跑入口
├── *-test-matrix.md          # 能力矩阵、执行证据和未覆盖门禁
└── optional helper scripts   # 驱动准备等不绕过产品链路的辅助脚本
```

本目录不存放 Docker Compose 数据库环境、厂商二进制、数据库数据、License、真实凭据或生成日志：

- Docker Compose 测试数据库放在 `tests/dbs/`；
- Gradle/Java 公共数据源测试放在 `tests/ds-test/`；
- Chrome 数据源用户流程放在 `tests/frontend/datasource/`；
- 各数据源专项矩阵和辅助脚本放在本目录；产品验收必须从 CloudDM 页面或运行时链路执行。

当前已纳入：

- [`goldendb/`](goldendb/README.md)：GoldenDB MySQL/Oracle 兼容模式专项测试。
