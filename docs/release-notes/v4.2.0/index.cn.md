## 新增

- 新增 AWS Valkey 数据源支持；当托管实例禁用 `CONFIG` 命令时，可通过 `SELECT` 探测实际数据库数量，由社区贡献者 [@BetaCat0](https://github.com/BetaCat0) 提交，感谢贡献（[#249](https://github.com/ClouGence/open-cdm/issues/249)）。
- 新增 CockroachDB 数据源，支持连接配置、SSL、Catalog 和 Schema 默认值、对象浏览及基础 SQL 查询，由社区贡献者 [@48N6E](https://github.com/48N6E) 提交，感谢贡献（[#296](https://github.com/ClouGence/open-cdm/issues/296)）。
- 新增四个独立的 KingbaseES 数据源模式，覆盖 PostgreSQL、MySQL、Oracle 和 SQLServer 兼容模式，并支持模式探测与不匹配拦截（[#285](https://github.com/ClouGence/open-cdm/issues/285)）。
- 新增 GoldenDB MySQL 和 GoldenDB Oracle 数据源，分别使用对应驱动与 SQL 体系，支持连接、对象浏览、查询、表结构管理及 GoldenDB 分布式表元数据（[#285](https://github.com/ClouGence/open-cdm/issues/285)）。
