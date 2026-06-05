<template>
  <a-modal :visible="visible" @cancel="handleCloseModal" :title="$t('tuo-min-ce-lve-bian-ji')" :width="374">
    <div class="header">
      <cc-data-source-icon :instanceType="ds.deployEnvType" :size="18" :type="ds.dataSourceType" color="#4DBAEE" />
      {{ ds.instanceDesc }}{{ preRule.resourcePath }}
    </div>
    <div class="content">
      <a-form-model :label-col="{ span: 5 }" :wrapper-col="{ span: 16 }">
        <a-form-model-item :label="$t('suan-fa-lei-xing')">
          <a-select v-model="rule.ruleType" @change="handleRuleTypeChange">
            <a-select-option v-for="r in rulesObj" :key="r.ruleType" :value="r.ruleType">
              {{ r.name }}
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item :label="$t('can-shu-zhi')" v-if="rule.ruleType === 'PART_MASK'">
          <a-input v-model="rule.ruleExpr" />
        </a-form-model-item>
      </a-form-model>
    </div>
    <div class="footer">
      <a-button type="primary" @click="handleSave">{{ $t('bao-cun') }}</a-button>
      <a-button @click="handleCloseModal">{{ $t('qu-xiao') }}</a-button>
    </div>
  </a-modal>
</template>

<script>
import * as Vue from 'vue';

export default {
  name: 'EditRuleModal',
  props: {
    visible: Boolean,
    preRule: Object,
    rulesObj: {},
    handleCloseModal: Function
  },
  data() {
    return {
      rule: {},
      ds: {}
    };
  },
  watch: {
    preRule: {
      handler(newValue) {
        this.rule = newValue;
      },
      immediate: true
    }
  },
  methods: {
    handleRuleTypeChange() {
      this.rule.ruleExpr = '';
    },
    async handleSave() {
      const { ruleExpr, id, ruleType } = this.rule;
      console.log(this.rule);
      const res = await this.$services.dmDesensitizationRuleUpdateDesensitizeRule({
        data: {
          ruleExpr,
          ruleType,
          ruleId: id
        },
        msg: this.$t('xiu-gai-tuo-min-ce-lve-cheng-gong')
      });

      if (res.success) {
        this.handleCloseModal();
      }
    },
    async getDs() {
      const res = await this.$services.dmDataSourceQueryDs({ data: { dataSourceId: this.preRule.datasourceId } });
      if (res.success) {
        this.ds = res.data;
      }
    }
  },
  created() {
    this.getDs();
  }
};
</script>

<style scoped lang="less">
.header {
  margin-bottom: 16px;
}
</style>
