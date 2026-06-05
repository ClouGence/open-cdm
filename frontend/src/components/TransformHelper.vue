<template>
  <div class="transform-icon">
    <Poptip trigger="click" placement="bottom" transfer :width="650" :title="$t('dang-qian-zhi-chi-de-jiao-ben')">
      <template #content>
        <Table :columns="transformHelpColumns" :data="transformHelpList || transformScripts" border stripe size="small">
          <template #scriptExpression="{ row }">
            <div @click="copyText(row.scriptExpression)" style="cursor: pointer" :title="$t('dian-ji-fu-zhi')">
              {{ row.scriptExpression }}
              <Icon class="transform-copy-icon" type="md-copy" />
            </div>
          </template>
        </Table>
      </template>
      <Icon type="ios-help-circle-outline" />
    </Poptip>
  </div>
</template>
<script>
import copyMixin from '@/mixins/copyMixin';

export default {
  name: 'TransformHelper',
  mixins: [copyMixin],
  props: {
    transformHelpList: Array
  },
  mounted() {
    this.listDataTransformScripts();
  },
  data() {
    return {
      transformHelpColumns: [
        {
          title: this.$t('jiao-ben'),
          slot: 'scriptExpression',
          width: 350
        },
        {
          title: this.$t('shuo-ming'),
          key: 'scriptDescription',
          width: 300
        }
      ],
      transformScripts: []
    };
  },
  methods: {
    async listDataTransformScripts() {
      const res = await this.$services.listDataTransformScripts();
      if (res.success) {
        this.transformScripts = res.data;
      }
    }
  }
};
</script>
<style scoped lang="less">
.transform-icon {
  display: inline-block;
  margin-left: 4px;
  i {
    font-size: 14px;
  }
  &:hover {
    .transform-copy-icon {
      display: inline-block;
    }
  }
}

.transform-copy-icon {
  display: none;
  color: #0bb9f8;
  margin-left: 4px;
  font-size: 16px;
}

.ivu-table-cell-slot:hover {
  .transform-copy-icon {
    display: inline-block;
  }
}
</style>
