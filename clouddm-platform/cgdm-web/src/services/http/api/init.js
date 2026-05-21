export const initApi = {
  // 获取默认配置字段定义
  dmInitDefaultConfig: '/clouddm/console/api/v1/init/defaultConfig',
  // 测试数据库连接 + 空库检测 + 已安装检测
  dmInitTestDb: '/clouddm/console/api/v1/init/testDb',
  // 检查初始化程序驱动状态
  dmInitCheckDriverStatus: '/clouddm/console/api/v1/init/checkDriverStatus',
  // 下载初始化程序驱动
  dmInitDownloadDriver: '/clouddm/console/api/v1/init/downloadDriver',
  // 预览待执行脚本
  dmInitPreviewScripts: '/clouddm/console/api/v1/init/previewScripts',
  // 保存初始化配置（完整模式）
  dmInitApplyConfig: '/clouddm/console/api/v1/init/applyConfig',
  // 触发系统重启
  dmInitRestart: '/clouddm/console/api/v1/init/restart'
};
