module.exports = {
  root: true,
  env: {
    jquery: true,
    node: true
  },
  extends: ['plugin:vue/essential', 'plugin:@intlify/vue-i18n/recommended', 'plugin:vue/recommended',
    'eslint:recommended',
    'plugin:import/recommended',
    'plugin:prettier/recommended'],
  parserOptions: {
    parser: '@babel/eslint-parser',
    requireConfigFile: false,
    ecmaVersion: 2021,
    sourceType: 'module',
    ecmaFeatures: {
      jsx: true // 启用 JSX 支持
    }
  },
  rules: {
    'prettier/prettier': [
      'error',
      {
        printWidth: 150
      }
    ],
    'import/no-cycle': 'off',
    'class-methods-use-this': 'off',
    'no-useless-concat': 'off',
    'no-prototype-builtins': 'off',
    camelcase: 'off',
    'no-else-return': 'off',
    // 'i18n-rule-name': 'off',
    // "@intlify/vue-i18n/no-raw-text": "off",
    '@intlify/vue-i18n/no-raw-text': [
      'error',
      {
        attributes: {
          '/.+/': ['title', 'label', 'content', 'placeholder', 'v-text', 'v-html', 'addon-before', 'addon-after', 'tab'],
          input: ['placeholder'],
          img: ['alt']
        },
        'ignoreText': [
          'EUR', 'HKD', 'USD', '=', '%', 'GB', 'MB', 0, 'KB)', '', 'AccessKey ID', 'AccessKey Secret', 'ID：', 'AK', 'SK', 'G',
          'sms', 'dingding', 'IM', 'phone', 'webhook', 'default', 'custom', 'cn-hangzhou', 'accessKeyId', 'accessSecret',
          '*******', 'sourceState:', 'targetState:', 'triggerClassName:', 'contextClassName:', 'triggerContext:',
          'AK/SK', 'accessKey：', 'secretKey：', 'x', 'TAOBAO', '中文', 'English', '&#xe662;', '&#xe662;', 'Home',
          'Copyright © 2023 ClouGence, Inc.', '浙ICP备20007605号-1', 'App', 'host', 'HadoopUser', '******', 'varchar', 'select', 'Redis KEY',
          'from', 'where', 'update', 'set ...wherepk = xxx and', 'select \* from table where', 'KERBEROS', '-', '(', ')', '（', '）', ':', '：',
          'advanced', 'easy', 'HOST', 'running', 'wait', 'finish', 'v', '1', '2', '3', '*', 'ADD_ALL', 'PRIVATE_IP_ONLY', 'PUBLIC_IP_ONLY',
          '/', 'PRIVATE', 'PUBLIC', '.', 'INSERT', 'UPDATE', 'DELETE', 'CREATE', 'ALTER', 'RENAME', 'TRUNCATE', 'DROP',
          'arrow', 'MIGRATION', 'SYNC', 'CHECK', 'STRUCT_MIGRATION', 'REVISE', '[table.sourceDb][table.sourceTable]', 'IncreEnhance',
          'FullEnhance', 'Balance', 'false', 'true', 'noCheck', 'checkOnce', 'checkPeriod', '/', ':{', '}', 'I', 'U', 'D',
          'C', 'A', 'R', 'T', 'DR', 'public', 'Exchange', 'cloudcanal', 'Vhost', 'Consumer Group', 'CloudCanal',
          'DB', 'Buckets', 'DDL Topic', 'NESTED', 'TEXT', 'OBJECT', 'KEYWORD', '_id', '0', 'update tables set ... where', 'set ... where pk = xxx and', 'manual',
          'auto', 'ip', 'IP', '）', '（', 'email', 'SCHEMA', 'host: ', 'ID: ', 'Operators', 'Insert SQL', '>', '[', ']', ':', 'ms',
          'Login', 'Home', 'Register', 'Reset', '~', '?', 'SID', 'SERVICE', 'Four', 'Self', '2021-01-07 11:00:00', 'Register', 'China', 'Login', '，', 'userAvatar', '(New)',
          'Two', 'Host', 'Three', 'Host', 'Three', 'Host', 'One', 'Five', '...', 'name', '@', '【', '】', '20', '50', '100', '200',
          'int', 'integer', 'float', 'decimal', 'bool', 'string', 'date', 'time', 'datetime',
          '500', '1000', '2000', '5000', '10000',
          'of', '0', 'host: ', 'ID: ', 'FAQ', 0, 'Copyright © 2025 ClouGence,Inc.', 'AgentId', 'AppKey', 'AppSecret', 'Clougence RDP', 'ClouGence RDP', 'CloudDM', 'AutoMQ',
          'WHERE', 'ORDER BY', 'PORT', 'FOLLOWS', 'PRECEDES', 'follows', 'precedes', 'DEFERRABLE', 'row', 'statement', 'NOT DEFERRABLE',
          'INITIALLY IMMEDIATE', 'INITIALLY DEFERRED', 'DEFERRABLE INITIALLY DEFERRED', 'DEFERRABLE INITIALLY IMMEDIATE', '$',
          'NOT DEFERRABLE INITIALLY IMMEDIATE', 'delete', 'insert', 'DETERMINISTIC', 'IN', 'OUT', 'INOUT', 'CONTAINS SQL', 'NO SQL',
          'READS SQL DATA', 'MODIFIES SQL DATA', 'security_barrier', 'MODIFY SQL DATA', 'Request', 'Convert', 'before', 'after', 'truncate', 'SQL', 'BladePipe', 'uid', '****',
          'NULL',
          'approTemplateName', 'port', 'P0', 'P1', 'P2', 'P3', 'P4', 'select month', 'select date', 'Select time', 'schema', 'Tenant', 'Namespace', '-$', '0', 'Namespace', 'BASE_TABLE', 'VIEW',
          'NONE', 'Skip', 'SKIP', 'RETRY', 'IMMEDIATE', 'SPECIFY_TIME', 'MANUAL_EXEC', '¥', 'Push', 'PullRequest', 'Snapshot', 'CreateChange', 'None', 'Always', 'Suggest', 'Enable', 'Disable', 'Auto',
          'Manual', 'Disabled', 'Enable', 'zh', 'en', 'Failure', 'Webhook', 'POST', 'GET', 'http://... or https://...','HTTP','wget','curl','http','HTTP(GET)','json','text','new','old',
          'SELECT','DDL','QUERY','DML','CALL','OTHER','CONSOLE','TICKET','CHANGE','RUNNING','SUCCESS','WAIT_CONFIRM','ROLLBACK','FAILURE','ERROR', '取消',
          'To', 'Addr', '6%', 'VAT_NORMAL_INVOICE', 'VAT_SPECIAL_INVOICE','NULL', 'MFA', "MANAGED", "BYOC", 'weixin', 'feishu', 'slack', 'discord', 'dingtalk', ''
        ]
      }
    ],
    // '@intlify/vue-i18n/no-dynamic-keys': 'error',
    // '@intlify/vue-i18n/no-unused-keys': [
    //   'error',
    //   {
    //     extensions: ['.js', '.vue']
    //   }
    // ],
    'linebreak-style': ['off', 'windows'],
    'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'no-unused-vars': 'off',
    'comma-dangle': [
      'error',
      {
        // 逗号处理
        arrays: 'never',
        objects: 'never',
        imports: 'never',
        exports: 'never',
        functions: 'ignore'
      }
    ],
    'global-require': 'off',
    'max-len': [
      'error',
      {
        code: 30000
      }
    ],
    'func-names': 'off',
    'no-plusplus': 'off',
    'no-param-reassign': 'off',
    'import/prefer-default-export': 'off',
    'no-underscore-dangle': 'off',
    'consistent-return': 'off',
    'vue/no-parsing-error': [2, { 'x-invalid-end-tag': false }],
    'no-return-assign': 'off',
    'no-restricted-syntax': 'off',
    'no-case-declarations': 'off',
    'no-nested-ternary': 'off',
    'guard-for-in': 'off',
    'no-use-before-define': 'off',
    'no-async-promise-executor': 'off',
    'no-mixed-operators': 'off',
    'prefer-destructuring': 'off',
    'no-lonely-if': 'off',
    'no-bitwise': 'off',
    'object-curly-newline': 'off',
    'vue/html-closing-bracket-spacing': 'off',
    'vue/max-attributes-per-line': 'off',
    'vue/attributes-order': 'off',
    'vue/order-in-components': 'off',
    'vue/multi-word-component-names': 'off',
    'vue/require-default-prop': 'off',
    'vue/attribute-hyphenation': 'off',
    'import/extensions':
      'off',
    'vue/no-mutating-props': 'off',
    'vue/no-template-shadow': 'off',
    'vue/no-v-html': 'off',
    'vue/prop-name-casing': 'off',
    'vue/comment-directive': 'off',
    'import/no-named-as-default-member': 'off',
    'vue/no-lone-template': 'off',
    'vue/no-unused-vars': 'off',
    'vue/this-in-template': 'off',
    'vue/require-prop-types': 'warn',
    'vue/component-definition-name-casing': ['warn', 'PascalCase'],
    'vue/require-prop-types': 'off',
    'vue/component-definition-name-casing': 'off',
    'vue/require-prop-types': 'off',
    'vue/v-slot-style': 'off',
    'import/no-named-as-default': 'off',
    'vue/no-v-model-argument': 'off'
  },
  overrides: [
    {
      files: [
        '**/__tests__/*.{j,t}s?(x)',
        '**/tests/unit/**/*.spec.{j,t}s?(x)'
      ],
      env: {
        jest: true
      }
    }
  ],
  settings: {
    'vue-i18n': {
      localeDir: '/src/locales/*.json'
    },
    'import/resolver': {
      node: {
        paths: ['src'],
        extensions: ['.js', '.jsx', '.ts', '.tsx', '.vue']
      },
      alias: {
        map: [
          ['@', './src']
        ],
        extensions: ['.js', '.jsx', '.ts', '.tsx', '.vue']
      }
    }
  }
};
