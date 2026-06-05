<template>
  <a-modal :visible="visible" @cancel="handleCloseModal" :title="$t('tuo-min-gui-ze-yu-lan')" :width="480">
    <a-form-model :label-col="{ span: 4 }" :wrapper-col="{ span: 20 }">
      <a-form-model-item :label="$t('yuan-shi-shu-ju')">
        <div style="display: flex">
          <a-input v-model="form.value" :placeholder="$t('qing-shu-ru-yuan-shi-shu-ju')" style="width: 300px" />
          <a-button style="width: 60px; margin-left: 10px" @click="preview">
            {{ $t('ce-shi') }}
          </a-button>
        </div>
      </a-form-model-item>
      <a-form-model-item :label="$t('tuo-min-shu-ju')">
        <a-input placeholder="" v-model="form.ruledValue" type="textarea" style="width: 374px" />
      </a-form-model-item>
    </a-form-model>
    <div class="footer">
      <a-button style="width: 120px" @click="handleCloseModal">{{ $t('guan-bi') }}</a-button>
    </div>
  </a-modal>
</template>

<script>
import * as Vue from 'vue';

export default {
  name: 'PreviewModal',
  props: {
    visible: Boolean,
    column: {},
    ruleData: {},
    handleCloseModal: Function
  },
  data() {
    return {
      form: {
        value: '',
        ruledValue: ''
      }
    };
  },
  methods: {
    async preview() {
      const res = await this.$services.dmDesensitizationRuleDesensitizeRuleSample({
        data: {
          expr: this.ruleData.ruleExpr || '',
          ruleType: this.ruleData.ruleType || '',
          sampleStr: this.form.value
        }
      });

      if (res.success) {
        this.form.ruledValue = res.data;
      }
    }
  }
};
</script>

<style scoped></style>
