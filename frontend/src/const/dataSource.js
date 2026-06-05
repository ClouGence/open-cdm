// 该文件需废弃，替换成 /src/utils/index.js
export const MySQL = ['MySQL'];
export const PostgreSQL = ['PostgreSQL'];
export const Greenplum = ['Greenplum'];
export const Oracle = ['Oracle'];
export const Redis = ['Redis'];
export const TiDB = ['TiDB'];

export const SqlServer = ['SQLServer'];

export const Db2 = ['Db2'];

export const Dameng = ['Dameng'];

export const HasSchema = [...Oracle, ...PostgreSQL, ...Greenplum, ...SqlServer];

export const isMySQL = (type) => MySQL.includes(type);
export const isPostgreSQL = (type) => PostgreSQL.includes(type);
export const isGreenplum = (type) => Greenplum.includes(type);
export const isOracle = (type) => Oracle.includes(type);
export const isRedis = (type) => Redis.includes(type);
export const isTiDB = (type) => TiDB.includes(type);

export const isDb2 = (type) => Db2.includes(type);

export const isSqlServer = (type) => SqlServer.includes(type);

export const isDameng = (type) => Dameng.includes(type);

export const isHasSchema = (type) => HasSchema.includes(type);

export const NO_DDL = (type) => [...Redis].includes(type);

export const NO_CREATE_DATABASE = (type) => [...Oracle, ...Db2].includes(type);
