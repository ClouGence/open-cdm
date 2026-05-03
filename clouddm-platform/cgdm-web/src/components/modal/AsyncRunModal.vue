<template>
  <CCModal :title="title" v-model="localShow">
    <Input v-model="asyncForm.confirmInfo" type="textarea" :placeholder="$t('qing-shu-ru-cao-zuo-li-you')" v-if="showReasonInput" />
    <template #footer>
      <Button type="primary" @click="handleSave">{{ $t('ti-jiao') }}</Button>
      <Button @click="handleCloseModal">{{ $t('qu-xiao') }}</Button>
    </template>
  </CCModal>
</template>

<script>
import { isMySQL } from '@/const/dataSource';
import { mapGetters } from 'vuex';

export default {
  name: 'AsyncRunModal',
  props: {
    confirmType: String,
    visible: Boolean,
    onRun: Function,
    handleCloseModal: Function,
    title: String,
    type: String,
    dataSourceType: String
  },
  data() {
    return {
      localShow: this.visible,
      asyncForm: {
        ddl: 'DIRECT',
        noneDdl: 'DIRECT',
        confirmInfo: ''
      },
      sqlExecType: {}
    };
  },
  computed: {
    ...mapGetters(['isDesktop']),
    showReasonInput() {
      return this.type === 'ticket';
    },
    showGhost() {
      return isMySQL(this.dataSourceType) && ((this.type === 'ticket' && this.confirmType === 'CONFIRM_AND_AUTO_EXEC') || this.type === 'console');
    }
  },
  // mounted() {
  //   this.getSQLExecType();
  // },
  methods: {
    async handleChooseFileSaveLocation(e) {
      console.log(e);
    },
    async getSQLExecType() {
      const res = await this.$services.dmConstantListExportSqExecType({
        data: {
          dataSourceType: this.dataSourceType
        }
      });

      if (res.success) {
        this.sqlExecType = res.data;
        this.sqlExecType.forEach((sql) => {
          if (sql.sqlTypeForView === 'EXPORT_SQL_TYPE_FOR_VIEW_DDL_ALTER') {
            sql.sqlType = 'ddl';
          } else {
            sql.sqlType = 'noneDdl';
          }
        });
      }
    },
    handleSave() {
      this.onRun(this.asyncForm);
    }
  },
  watch: {
    dataSourceType: {
      handler(newVal, oldVal) {
        if (newVal !== oldVal) {
          this.getSQLExecType();
        }
      },
      immediate: true
    },
    visible(newVal) {
      this.localShow = newVal;
    },
    localShow(newVal) {
      this.$emit('update:visible', newVal);
    }
  }
};
</script>

<style scoped></style>
