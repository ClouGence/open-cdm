import { format } from 'sql-formatter';
import { formatHanaSql } from './hanaSql';

const DIALECTS = {
  MySQL: 'mysql',
  MariaDB: 'mariadb',
  TiDB: 'tidb',
  OceanBase: 'mysql',
  PolarDbMySQL: 'mysql',
  PolarDbX: 'mysql',
  AdbForMySQL: 'mysql',
  KingbaseESMySQL: 'mysql',
  GoldenDBMySQL: 'mysql',
  Oracle: 'plsql',
  ObForOracle: 'plsql',
  Dameng: 'plsql',
  KingbaseESOracle: 'plsql',
  GoldenDBOracle: 'plsql',
  PostgreSQL: 'postgresql',
  CockroachDB: 'postgresql',
  Greenplum: 'postgresql',
  Cloudberry: 'postgresql',
  GaussDBForOpenGauss: 'postgresql',
  GaussDB: 'postgresql',
  KingbaseESPostgreSQL: 'postgresql',
  PolarDBPg: 'postgresql',
  Hologres: 'postgresql',
  Redshift: 'redshift',
  SQLServer: 'transactsql',
  KingbaseESSQLServer: 'transactsql',
  Db2: 'db2',
  Db2Fori: 'db2i',
  ClickHouse: 'clickhouse',
  StarRocks: 'mysql',
  Doris: 'mysql',
  SelectDB: 'mysql',
  MaxCompute: 'hive'
};

export function canFormatSql(tab) {
  return ['Allow', 'Hint'].includes(tab?.support?.format?.conf);
}

export function formatSqlDocument(sql, dsType) {
  if (dsType === 'Hana') return formatHanaSql(sql);
  const language = DIALECTS[dsType];
  if (!language) return null;
  try {
    return format(sql, { language, tabWidth: 4, keywordCase: 'preserve', linesBetweenQueries: 1 });
  } catch {
    return null;
  }
}
