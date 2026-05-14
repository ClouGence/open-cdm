export const initApi = {
  // 获取默认配置字段定义
  dmInitDefaultConfig: '/clouddm/console/api/v1/init/defaultConfig',
  // 测试数据库连接 + 空库检测 + 已安装检测
  dmInitTestDb: '/clouddm/console/api/v1/init/testDb',
  // 检查初始化程序 MySQL 驱动状态
  dmInitCheckMysqlDriverStatus: '/clouddm/console/api/v1/init/checkMysqlDriverStatus',
  // 下载初始化程序 MySQL 驱动
  dmInitDownloadMysqlDriver: '/clouddm/console/api/v1/init/downloadMysqlDriver',
  // 预览待执行脚本
  dmInitPreviewScripts: '/clouddm/console/api/v1/init/previewScripts',
  // 保存初始化配置（完整模式）
  dmInitApplyConfig: '/clouddm/console/api/v1/init/applyConfig',
  // 执行升级（升级模式）
  dmInitUpgrade: '/clouddm/console/api/v1/init/upgrade',
  // 仅更新数据库配置（dbOnly 模式）
  dmInitUpdateDbConfig: '/clouddm/console/api/v1/init/updateDbConfig',
  // 触发系统重启
  dmInitRestart: '/clouddm/console/api/v1/init/restart'
};
