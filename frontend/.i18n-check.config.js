module.exports = {
  // 需要检查的文件类型
  fileExtensions: ['.vue', '.js', '.ts'],

  // 需要排除的目录和文件
  excludePatterns: [
    'node_modules',
    'dist',
    '.git',
    'src/locales',
    'src/assets/iconfont',
    'public/luckysheet',
    '.husky',
    'scripts',
    'src/styles',
    'public/css',
    'i18n-check.config.js',
    'vue.config.js',
    '.eslintrc.js',
    '.prettierrc.js',
    '.editorconfig',
    '.husky',
    'check-services.js',
    'src/const',
    'src/views/home',
    'src/views/WebHome',
    'src/utils',
    'src/services/socket.js',
    'src/components/CCLogoHeader.vue',
    'src/services/socket.js',
    'src/utils/index.js',

    // dm相关代码暂不做检测
    'src/views/sql',
    'src/views/ticket',
    'src/views/project',
    'src/views/system/desensitization',
    'src/views/system/datasource',
    'src/views/devops',
    'src/views/im',
    'src/views/security',
    'src/views/system/subaccount',
    'src/components/editor'
  ],

  // 需要排除的行模式（注释、TODO等）
  excludeLinePatterns: [
    /^\s*\/\//, // 单行注释
    /^\s*\*/, // 多行注释
    /^\s*\/\*/, // 多行注释开始
    /^\s*\*\//, // 多行注释结束
    /console\.(log|warn|error|info)/, // 控制台输出
    /TODO/, // TODO标记
    /FIXME/, // FIXME标记
    /NOTE/, // NOTE标记
    /\/\*[\s\S]*?\*\//, // 多行注释块
    /^\s*<!--/, // HTML注释
    /^\s*-->/ // HTML注释结束
  ],

  // 不需要国际化的中文词汇（技术术语、常见词汇等）
  excludeTerms: [],

  // 是否在检测到问题时阻止提交
  failOnError: true,

  // 是否显示详细信息
  verbose: true
};
