export const MySQL = ['MySQL', 'GoldenDBMySQL'];
export const PostgreSQL = ['PostgreSQL'];
export const Oracle = ['Oracle', 'GoldenDBOracle'];
export const Redis = ['Redis'];

export const isMySQL = (type) => MySQL.includes(type);
export const isPostgreSQL = (type) => PostgreSQL.includes(type);
export const isOracle = (type) => Oracle.includes(type);
export const isRedis = (type) => Redis.includes(type);
