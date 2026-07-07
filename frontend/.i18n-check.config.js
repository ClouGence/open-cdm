module.exports = {
  // File type to check
  fileExtensions: ['.vue', '.js', '.ts'],

  // Directory and file to exclude
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

    // dm-related codes are not tested
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

  // Line Mode to Exclude (Note, TODO, etc.)
  excludeLinePatterns: [
    /^\s*\/\//, // Single-line comment
    /^\s*\*/, // Multi-line comment
    /^\s*\/\*/, // Start of multi-line comment
    /^\s*\*\//, // End of multi-line comment
    /console\.(log|warn|error|info)/, // Console output
    /TODO/, // TODO marker
    /FIXME/, // FIXME marker
    /NOTE/, // NOTE marker
    /\/\*[\s\S]*?\*\//, // Multiline Comment Block
    /^\s*<!--/, // HTML comment
    /^\s*-->/ // End of HTML comment
  ],

  // No internationalized Chinese vocabulary (technical terms, common terminology, etc.)
  excludeTerms: [],

  // Whether or not to prevent submission when detected
  failOnError: true,

  // Whether to show details
  verbose: true
};
