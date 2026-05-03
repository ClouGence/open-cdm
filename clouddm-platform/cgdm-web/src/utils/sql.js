import Dexie from 'dexie';

const db = new Dexie('clougence_clouddm');
db.version(1).stores({
  tab: 'id, createTime, updateTime, uid, data'
});

export const isSupportSql = !!window.indexedDB && indexedDB instanceof IDBFactory;
export default db;
