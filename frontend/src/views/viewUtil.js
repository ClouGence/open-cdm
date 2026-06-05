import i18n from '../i18n';

const getExampleData = (type) => {
  const theType = type.split('(')[0].toUpperCase();

  switch (theType) {
    case 'DATE':
      return '2020-12-24';
    case 'DATETIME':
      return '2020-12-24 00:00:00';
    case 'TIMESTAMP':
      return '1608782968300';
    case 'TIME':
      return '12:13';
    case 'YEAR':
      return '2020';
    case 'BIGINT':
      return 13;
    case 'BIT':
      return 1;
    case 'TINYINT':
      return 1;
    case 'SMALLINT':
      return 1;
    case 'MEDIUMINT':
      return 100;
    case 'INIT':
      return 1;
    case 'DECIMAL':
      return 5.2;
    case 'FLOAT':
      return '3.45';
    case 'DOUBLE':
      return '1.23';
    case 'JSON':
      return { test: 1 };
    default:
      return 'string data';
  }
};

const handleMqSchemaTool = (schema, columns, db, schemaName, table, type) => {
  let mqSchema = schema;
  let dbValType = '';
  let data = '';
  let jdbcType = '';
  const pks = [];

  if (columns) {
    columns.map((column, index) => {
      if (type === 'detail') {
        dbValType += `
        "${column.column}":${i18n.global.t('mysql-lei-xing')}${index < columns.length - 1 ? ',' : ''}`;
        jdbcType += `
        "${column.column}":${i18n.global.t('jdbc-lei-xing')}${index < columns.length - 1 ? ',' : ''}`;
        data += `
        "${column.column}":${i18n.global.t('shu-ju')}${index < columns.length - 1 ? ',' : ''}`;
      } else if (column.selected) {
        if (type === 'mqTable') {
          dbValType += `
        "${column.sinkColumn}":"${column.sinkType}",`;
          jdbcType += `
        "${column.sinkColumn}":"0",`;
          data += `
        "${column.sinkColumn}":"${getExampleData(column.sinkType)}",`;
          if (column.isPk) {
            pks.push(column.sinkColumn);
          }
        } else {
          if (index >= columns.length - 1) {
            dbValType += `
        "${column.sourceColumn}":"${column.sourceType}",`;
            jdbcType += `
        "${column.sourceColumn}":"0",`;
            data += `"${column.sourceColumn}":"${getExampleData(column.sourceType)}"`;
          } else {
            dbValType += `
        "${column.sourceColumn}":"${column.sourceType}",`;
            jdbcType += `
        "${column.sourceColumn}":"0",`;
            data += `
        "${column.sourceColumn}":"${getExampleData(column.sourceType)}",`;
          }
          if (column.isPk) {
            pks.push(column.sourceColumn);
          }
        }
      }
      return null;
    });
  }
  if (type === 'cache') {
    mqSchema = mqSchema.replace(/\${data}/g, `${data}`);
  } else {
    mqSchema = mqSchema.replace(/\${dbValType}/g, `{${dbValType}}`);
    mqSchema = mqSchema.replace(/\${db}/g, `"${db}"`);
    mqSchema = mqSchema.replace(/\${table}/g, `"${table}"`);
    mqSchema = mqSchema.replace(/\${schema}/g, `"${schemaName}"`);
    mqSchema = mqSchema.replace(/\${data}/g, `[{${data}}]`);
    mqSchema = mqSchema.replace(/\${jdbcType}/g, `{${jdbcType}}`);
    mqSchema = mqSchema.replace(/\${pks}/g, JSON.stringify(pks));
    mqSchema = mqSchema.replace(/\/\*sql execute in db timestamp\*\//g, '');
    mqSchema = mqSchema.replace(/\/\*msgContent sent timestamp\*\//g, '');
  }
  return mqSchema;
};

export { getExampleData, handleMqSchemaTool };
