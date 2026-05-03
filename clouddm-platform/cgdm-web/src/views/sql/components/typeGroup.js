export const mysqlInsert = {
  needQuote: [
    'DATE',
    'DATETIME',
    'TIMESTAMP',
    'TIME',
    'YEAR',
    'CHAR',
    'VARCHAR',
    'TINYTEXT',
    'TEXT',
    'MEDIUMTEXT',
    'LONGTEXT',
    'ENUM',
    'SET',
    'JSON'
  ],
  noNeedQuote: ['BIT', 'TINYINT', 'SMALLINT', 'MEDIUMINT', 'INT', 'BIGINT', 'DECIMAL', 'FLOAT', 'DOUBLE']
};

export const pgInsert = {
  needQuote: [
    'TIMESTAMP_WITHOUT_TIME_ZONE',
    'TIMESTAMP_WITH_TIME_ZONE',
    'TIME_WITHOUT_TIME_ZONE',
    'TIME_WITH_TIME_ZONE',
    'DATE',
    'CHARACTER',
    'BPCHAR',
    'CHARACTER_VARYING',
    'TEXT',
    'NAME',
    'BIT_VARYING',
    'POINT',
    'LINE',
    'LSEG',
    'BOX',
    'PATH',
    'POLYGON',
    'CIRCLE',
    'CIDR',
    'INET',
    'MACADDR',
    'MACADDR8',
    'JSON',
    'JSONB',
    'INT4RANGE',
    'INT8RANGE',
    'NUMRANGE',
    'BIT_ARRAY',
    'BIT_VARYING_ARRAY',
    'BYTEA_ARRAY',
    'POINT_ARRAY',
    'LINE_ARRAY',
    'LSEG_ARRAY',
    'BOX_ARRAY',
    'PATH_ARRAY',
    'POLYGON_ARRAY',
    'CIRCLE_ARRAY',
    'CIDR_ARRAY',
    'INET_ARRAY',
    'MACADDR_ARRAY',
    'MACADDR8_ARRAY',
    'JSON_ARRAY',
    'JSONB_ARRAY',
    'INT4RANGE_ARRAY',
    'INT8RANGE_ARRAY',
    'NUMRANGE_ARRAY'
  ],
  noNeedQuote: ['SMALLSERIAL', 'BIGSERIAL', 'SMALLINT', 'INTEGER', 'BIGINT', 'OID', 'NUMERIC', 'REAL', 'DOUBLE_PRECISION', 'MONEY', 'BIT', 'BOOLEAN']
};

export const mysqlTypeGroup = {
  date: ['DATE', 'DATETIME', 'TIMESTAMP', 'TIME', 'YEAR'],
  text: ['CHAR', 'VARCHAR', 'TINYTEXT', 'TEXT', 'MEDIUMTEXT', 'LONGTEXT', 'ENUM', 'SET', 'JSON'],
  number: ['BIT', 'TINYINT', 'SMALLINT', 'MEDIUMINT', 'INT', 'BIGINT', 'DECIMAL', 'FLOAT', 'DOUBLE', 'BIGINT UNSIGNED'],
  binary: ['BINARY', 'VARBINARY', 'TINYBLOB', 'BLOB', 'MEDIUMBLOB', 'LONGBLOB', 'GEOMETRY', 'POINT', 'LINESTRING', 'POLYGON', 'GEOMETRY_COLLECTION']
};

export const pgTypeGroup = {
  date: ['TIMESTAMP_WITHOUT_TIME_ZONE', 'TIMESTAMP_WITH_TIME_ZONE', 'TIME_WITHOUT_TIME_ZONE', 'TIME_WITH_TIME_ZONE', 'DATE'],
  text: ['CHARACTER', 'BPCHAR', 'CHARACTER_VARYING', 'TEXT', 'NAME', 'XML'],
  number: ['SMALLSERIAL', 'BIGSERIAL', 'SMALLINT', 'INTEGER', 'BIGINT', 'OID', 'NUMERIC', 'REAL', 'DOUBLE_PRECISION', 'MONEY', 'BIT', 'BOOLEAN'],
  binary: ['BYTEA']
};
