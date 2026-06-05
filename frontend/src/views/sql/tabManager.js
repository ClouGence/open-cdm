import db, { isSupportSql } from '@/utils/sql';

class TabManager {
  constructor() {
    this.tabTable = db.table('tab');
  }

  getInstance() {
    return this.tabTable;
  }

  getTabDataList(ids) {
    if (!isSupportSql) {
      return Promise.resolve();
    }
    return this.tabTable.bulkGet(ids);
  }

  getTabData(id) {
    if (!isSupportSql) {
      return Promise.resolve();
    }
    return this.tabTable.get({ id });
  }

  setTabData({ id, data, uid }) {
    if (!isSupportSql) {
      return Promise.resolve();
    }

    return this.tabTable.get({ id }).then((tab) => {
      if (!tab) {
        return this.tabTable.add({
          id,
          data,
          uid,
          createTime: Date.now(),
          updateTime: Date.now()
        });
      }

      return this.tabTable.update(id, {
        updateTime: Date.now(),
        data
      });
    });
  }

  deleteTabData(id) {
    if (!isSupportSql) {
      return Promise.resolve();
    }
    console.log(id);
    return this.tabTable.delete(id);
  }
}

export { TabManager };
