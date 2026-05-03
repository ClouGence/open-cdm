/* eslint-disable no-template-curly-in-string */
export const REDIS_QUICK = [
  {
    label: 'GET key',
    insertText: 'GET ${1: key}',
    detail: '获取键值'
  },
  {
    label: 'RENAME key newKey',
    insertText: 'RENAME ${1: key} ${2: newKey}',
    detail: '更改键名称'
  },
  {
    label: 'SET key value',
    insertText: 'SET ${1: key} ${2: value}',
    detail: '存放键值'
  },
  {
    label: 'KEYS pattern',
    insertText: 'KEYS ${1: key}',
    detail: '获取符合该模式的键'
  },
  {
    label: 'EXISTS key [key ...]',
    insertText: 'EXISTS ${1: key}',
    detail: '查询键是否存在'
  },
  {
    label: 'DEL key [key ...]',
    insertText: 'DEL ${1: key}',
    detail: '删除键'
  },
  {
    label: 'TTL key',
    insertText: 'TTL ${1: key}',
    detail: '查询key的生命周期（秒）'
  },
  {
    label: 'EXPIRE key seconds [NX|XX|GT|LT]',
    insertText: 'EXPIRE ${1: key} ${2: seconds}',
    detail: '设置过期时间'
  }
];
