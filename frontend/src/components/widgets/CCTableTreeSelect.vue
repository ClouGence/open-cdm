<template>
  <div class="auth-tree-select">
    <div class="left">
      <cc-schema-tree-select :handle-data-processing="handleDataProcessing" style="width: 200px" :auth="false" />
    </div>
    <div class="right">
      <div class="search">
        <a-input-search :placeholder="$t('qing-shu-ru-biao-ming-cha-xun')" size="small" style="width: 220px" @change="handleQuery" v-model="query" />
      </div>
      <div class="list" v-if="showCurrentTables.length">
        <div
          v-for="table in showCurrentTables"
          :key="table.title"
          class="table-container"
          @click="handleSelectTable([table.key])"
          :style="`${table.key === currentTableKey ? 'background: #0BB9F8;color:#fff' : ''}`"
        >
          <cc-iconfont name="biao" size="12" style="margin-right: 8px" />
          <span class="title" :title="table.title">{{ table.title }}</span>
          <a-popover
            trigger="hover"
            v-if="table.key && nums[table.key] && (nums[table.key].adds || nums[table.key].updates || nums[table.key].deletes)"
          >
            <template #content>
              <span v-if="nums[table.key].adds">
                {{ $t('xin-zeng-numstablekeyadds-xiang', [nums[table.key].adds]) }}
              </span>
              <span v-if="nums[table.key].updates">
                {{ $t('bian-ji-numstablekeyupdates-xiang', [nums[table.key].updates]) }}
              </span>
              <span v-if="nums[table.key].deletes">
                {{ $t('shan-chu-numstablekeydeletes-xiang', [nums[table.key].deletes]) }}
              </span>
            </template>
            <cc-iconfont name="setting" size="12" style="position: absolute; left: 210px" />
          </a-popover>
        </div>
      </div>
      <div v-if="!showCurrentTables.length" style="margin-top: 20px; text-align: center; width: 240px">
        {{ $t('zan-wu-biao') }}
      </div>
      <div class="pagination" v-if="showCurrentTables.length">
        <div class="page-size">
          <a-select v-model="pageSize" size="small" @change="handlePagination">
            <!--            <a-select-option :value="10">10</a-select-option>-->
            <a-select-option :value="50">50</a-select-option>
            <a-select-option :value="100">100</a-select-option>
            <a-select-option :value="200">200</a-select-option>
          </a-select>
          {{ $t('gong-searchedcurrenttableslength-tiao', [searchedCurrentTables.length]) }}
        </div>

        <div class="jump">
          <cc-iconfont name="left" size="12" :color="startId < 1 ? '#ccc' : '#333'" @click.native="handleLeft" />
          {{ startId + 1 }}
          <cc-iconfont
            name="right"
            size="12"
            :color="startId >= parseInt(searchedCurrentTables.length / pageSize) ? '#ccc' : '#333'"
            @click.native="handleRight"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CCTableTreeSelect',
  props: {
    getColumns: Function,
    type: String,
    checkErrors: Function,
    nums: Object
  },
  data() {
    return {
      query: '',
      currentTables: [],
      searchedCurrentTables: [],
      showCurrentTables: [],
      preSelectedKey: '',
      currentTableKey: '',
      selectedDs: {},
      pageSize: 100,
      startId: 0
    };
  },
  methods: {
    handlePagination() {
      this.startId = 0;
      this.generateTables();
    },
    handleLeft() {
      if (this.startId > 0) {
        this.startId -= 1;
        this.generateTables();
      }
    },
    handleRight() {
      if (this.startId < parseInt(this.searchedCurrentTables.length / this.pageSize, 10)) {
        this.startId += 1;
        this.generateTables();
      }
    },
    generateTables() {
      this.showCurrentTables = this.searchedCurrentTables.slice(this.startId * this.pageSize, (this.startId + 1) * this.pageSize);
    },
    handleSelectTable(selectedKeys) {
      if (this.checkErrors()) {
        return;
      }
      const key = selectedKeys.length ? selectedKeys[0] : this.preSelectedKey;
      const keys = key.split('/');
      const len = keys.length;
      const tableName = keys[len - 1];
      const schemaName = keys[len - 2];
      this.selectedDs.tableName = tableName;
      this.selectedDs.schemaName = schemaName;
      this.preSelectedKey = key;
      this.currentTableKey = key;
      this.getColumns(this.selectedDs);
    },
    handleQuery() {
      this.searchedCurrentTables = this.currentTables.filter((t) => t.title.includes(this.query));
      this.startId = 0;
      this.generateTables();
    },
    handleDataProcessing(data, datasource) {
      const currentTables = [];
      this.selectedDs = datasource;
      data.forEach((table) => {
        const tableName = table.name;
        const key = `${datasource.key}/${tableName}`;
        const currentTable = {
          tableName,
          ...datasource,
          key,
          title: tableName,
          slots: { icon: 'custom' }
        };
        currentTables.push(currentTable);
      });
      this.currentTables = currentTables;
      this.searchedCurrentTables = currentTables;
      this.generateTables();
    }
  }
};
</script>

<style lang="less">
.auth-tree-select {
  display: flex;
  //overflow: scroll;

  .ant-tree-switcher-noop {
    display: none;
  }

  .left {
    width: 200px;
  }

  .right {
    position: relative;
    height: calc(~'100vh - 188px');
    width: 240px;
    border: 1px solid rgba(199, 199, 199, 1);
    border-left: none;

    .list {
      overflow: scroll;
      height: calc(~'100vh - 270px');
      margin-bottom: 38px;
    }

    .table {
      position: relative;
      display: flex;
      line-height: 24px;
      height: 24px;
      padding-left: 12px;
      cursor: pointer;
      margin-bottom: 4px;

      .title {
        width: 180px;
        text-overflow: ellipsis;
        white-space: nowrap;
        overflow: hidden;
      }
    }

    .search {
      height: 44px;
      background: #fafafa;
      border-bottom: 1px solid #eaeaea;
      line-height: 44px;
      text-align: center;
    }

    .pagination {
      height: 38px;
      width: 240px;
      background: #f3f3f3;
      position: absolute;
      bottom: 0;
      padding: 0 14px;
      line-height: 38px;
      display: flex;
      justify-content: space-between;
      border-right: 1px solid #c7c7c7;
    }
  }
}
</style>
