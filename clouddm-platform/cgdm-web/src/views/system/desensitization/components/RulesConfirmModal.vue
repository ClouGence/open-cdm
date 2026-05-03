<template>
  <a-modal :visible="visible" :title="$t('tuo-min-ce-lve-que-ren')" @cancel="handleCloseModal" :width="800">
    <a-table :columns="columns" :data-source="datasource" size="small">
      <template #name="record">
        {{ record.confirmName.join('/') }}
      </template>
      <template #type="record">
        {{ types[record.ruleType] && types[record.ruleType].name }}
      </template>
      <template #value="record">
        {{ record.ruleExpr }}
      </template>
    </a-table>
    <div class="footer">
      <a-button type="primary" @click="handleConfirm">
        {{ $t('que-ding') }}
      </a-button>
      <a-button @click="handleCloseModal">
        {{ $t('qu-xiao') }}
      </a-button>
    </div>
  </a-modal>
</template>

<script>
export default {
  name: 'RulesConfirmModal',
  props: {
    visible: Boolean,
    rules: Object,
    datasource: {
      type: Array,
      default: () => [{}]
    },
    types: Object,
    handleCloseModal: Function
  },
  data() {
    return {
      columns: [
        {
          title: this.$t('shi-li-ming-ku-ming-biao-ming-lie-ming'),
          scopedSlots: { customRender: 'name' }
        },
        {
          title: this.$t('tuo-min-suan-fa'),
          scopedSlots: { customRender: 'type' },
          width: 100
        },
        {
          title: this.$t('can-shu-zhi'),
          scopedSlots: { customRender: 'value' },
          width: 120
        }
      ]
    };
  },
  methods: {
    async handleConfirm() {
      const { adds, updates, deletes } = this.rules;
      const res = await this.$services.dmDesensitizationRuleModifyDesensitizeRule({
        data: {
          addRules: Object.values(adds),
          updateRules: Object.values(updates),
          deleteRules: Object.values(deletes)
        },
        msg: this.$t('bian-ji-tuo-min-gui-ze-cheng-gong')
      });

      if (res.success) {
        this.handleCloseModal();
        this.$router.push({
          name: 'System_Desensitization'
        });
      }
    }
  }
};
</script>

<style scoped></style>
