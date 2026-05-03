<template>
  <a-modal :visible="visible" @cancel="handleCloseModal" title="Insert SQL" :width="800" :mask-closable="false">
    <div>
      <a-input v-model="tableName" :placeholder="$t('qing-shu-ru-biao-ming')" />
      <a-button @click="generateInsertSql(tableName)">
        {{ $t('sheng-cheng-insert-yu-ju') }}
      </a-button>
    </div>
    <div class="insert-sql-content">
      <div v-for="(sql, index) in sqls" :key="index">
        {{ sql }}
      </div>
    </div>
    <div class="footer">
      <a-button type="primary" ghost @click="copy">{{ $t('fu-zhi') }}</a-button>
      <a-button @click="handleCloseModal">{{ $t('guan-bi') }}</a-button>
    </div>
  </a-modal>
</template>

<script>
import copyMixin from '../../../../mixins/copyMixin';

export default {
  name: 'InsertSqlModal',
  mixins: [copyMixin],
  props: {
    visible: Boolean,
    sqls: Array,
    handleCloseModal: Function,
    generateInsertSql: Function
  },
  data() {
    return {
      tableName: ''
    };
  },
  methods: {
    copy() {
      this.copyText(this.sqls.join('\n'));
    }
  }
};
</script>

<style scoped lang="less">
.insert-sql-content {
  border: 1px solid rgba(218, 218, 218, 1);
  padding: 10px;
  max-height: 540px;
  overflow: scroll;
}
</style>
