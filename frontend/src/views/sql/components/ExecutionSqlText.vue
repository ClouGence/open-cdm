<template>
  <span class="execution-sql-text">
    <span v-for="(token, index) in tokens" :key="index" :class="token.type ? `execution-sql-text__${token.type}` : ''">
      {{ token.text }}
    </span>
  </span>
</template>

<script>
const SQL_KEYWORDS = new Set(
  [
    'ADD',
    'ALL',
    'ALTER',
    'AND',
    'AS',
    'ASC',
    'BEGIN',
    'BETWEEN',
    'BY',
    'CASE',
    'COMMIT',
    'CREATE',
    'CROSS',
    'DATABASE',
    'DELETE',
    'DESC',
    'DISTINCT',
    'DROP',
    'ELSE',
    'END',
    'EXISTS',
    'EXPLAIN',
    'FALSE',
    'FROM',
    'FULL',
    'GROUP',
    'HAVING',
    'IN',
    'INDEX',
    'INNER',
    'INSERT',
    'INTO',
    'IS',
    'JOIN',
    'LEFT',
    'LIKE',
    'LIMIT',
    'NOT',
    'NULL',
    'OFFSET',
    'ON',
    'OR',
    'ORDER',
    'OUTER',
    'REPLACE',
    'RIGHT',
    'ROLLBACK',
    'SELECT',
    'SET',
    'SHOW',
    'TABLE',
    'THEN',
    'TRUE',
    'TRUNCATE',
    'UNION',
    'UPDATE',
    'USE',
    'VALUES',
    'VIEW',
    'WHEN',
    'WHERE',
    'WITH'
  ].map((keyword) => keyword.toUpperCase())
);

const SQL_TOKEN_PATTERN =
  /(--[^\r\n]*|#[^\r\n]*|\/\*[\s\S]*?\*\/|'(?:''|\\.|[^'])*'|"(?:""|\\.|[^"])*"|`(?:``|[^`])*`|\b\d+(?:\.\d+)?\b|\b[A-Za-z_][A-Za-z0-9_$]*\b|\s+|.)/g;

function getTokenType(text) {
  if (text.startsWith('--') || text.startsWith('#') || text.startsWith('/*')) {
    return 'comment';
  }
  if (text.startsWith("'") || text.startsWith('"')) {
    return 'string';
  }
  if (/^\d/.test(text)) {
    return 'number';
  }
  if (SQL_KEYWORDS.has(text.toUpperCase())) {
    return 'keyword';
  }
  return '';
}

export default {
  name: 'ExecutionSqlText',
  props: {
    sql: {
      type: String,
      default: ''
    }
  },
  computed: {
    tokens() {
      return (this.sql.match(SQL_TOKEN_PATTERN) || []).map((text) => ({
        text,
        type: getTokenType(text)
      }));
    }
  }
};
</script>

<style scoped lang="less">
.execution-sql-text {
  color: var(--text-primary);
  white-space: pre-wrap;
  overflow-wrap: anywhere;
}

.execution-sql-text__keyword {
  color: #054cff;
}

.execution-sql-text__string {
  color: #a31515;
}

.execution-sql-text__number {
  color: #098658;
}

.execution-sql-text__comment {
  color: var(--text-tertiary);
  font-style: italic;
}

:global([data-theme='dark']) {
  .execution-sql-text__keyword {
    color: #569cd6;
  }

  .execution-sql-text__string {
    color: #ce9178;
  }

  .execution-sql-text__number {
    color: #b5cea8;
  }
}
</style>
