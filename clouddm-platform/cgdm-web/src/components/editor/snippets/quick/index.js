import { REDIS_QUICK } from '@/components/editor/snippets/quick/redis';
import { MYSQL_QUICK } from '@/components/editor/snippets/quick/mysql';
import { MONGODB_QUICK } from '@/components/editor/snippets/quick/mongodb';

const quicks = {
  Redis: REDIS_QUICK,
  MySQL: MYSQL_QUICK,
  MongoDB: MONGODB_QUICK
};

export const getQuick = (dsType) => quicks[dsType] || [];
