import { MYSQL_FUNCTION } from '@/components/editor/snippets/functions/mysql';
import { PG_FUNCTION } from '@/components/editor/snippets/functions/pg';
import { MONGODB_FUNCTION } from '@/components/editor/snippets/functions/mongodb';

export const functions = {
  MySQL: MYSQL_FUNCTION,
  PostgreSQL: PG_FUNCTION,
  MongoDB: MONGODB_FUNCTION
};
export const getFunction = (dsType) => functions[dsType] || [];
