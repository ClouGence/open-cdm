export const MySQL = ['MySQL'];
export const PostgreSQL = ['PostgreSQL'];
export const Oracle = ['Oracle'];
export const Redis = ['Redis'];

export const isMySQL = (type) => MySQL.includes(type);
export const isPostgreSQL = (type) => PostgreSQL.includes(type);
export const isOracle = (type) => Oracle.includes(type);
export const isRedis = (type) => Redis.includes(type);
