<template>
  <a-modal :visible="visible" :title="$t('xuan-ze-shen-pi-liu-cheng')" @cancel="handleCloseModal" :width="378" :maskClosable="false">
    <a-button :loading="refreshTemplateLoading" class="refresh-template-btn" @click="handleRefreshTemplate" size="small">
      {{ $t('shua-xin-shen-pi-mo-ban') }}
    </a-button>
    <a-select
      v-model="checkedTemplates"
      :placeholder="$t('qing-xuan-ze-shen-pi-liu-cheng')"
      mode="multiple"
      style="width: 338px"
      :filter-option="filterOption"
      show-search
    >
      <a-select-option v-for="template in templates" :value="template.templateIdentity" :key="template.templateIdentity">
        {{ template.approTemplateName }}
      </a-select-option>
    </a-select>
    <div class="footer">
      <a-button type="primary" @click="handleSave">{{ $t('bao-cun') }}</a-button>
      <a-button @click="handleCloseModal">{{ $t('qu-xiao') }}</a-button>
    </div>
  </a-modal>
</template>

<script>
export default {
  name: 'ProcessModal',
  props: {
    data: {
      type: Object,
      default: () => ({
        datasource: {
          ticket: {
            templates: []
          }
        }
      })
    },
    visible: Boolean,
    handleCloseModal: Function,
    handleTemplatesChange: Function
  },
  data() {
    return {
      templates: [],
      templatesObj: {},
      checkedTemplates: [],
      refreshTemplateLoading: false
    };
  },
  methods: {
    filterOption(input, option) {
      return option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0;
    },
    handleSave() {
      this.handleTemplatesChange(this.checkedTemplates, this.templatesObj);
      this.handleCloseModal();
    },
    async getTicketTemplates() {
      const res = await this.$services.dmTicketApproListTemplates({
        data: { approvalType: this.data.datasource.ticket.platform }
      });
      if (res.success) {
        this.templates = res.data;
        const obj = {};
        res.data.forEach((t) => {
          obj[t.templateIdentity] = t;
        });
        this.templatesObj = obj;
      }
    },
    async handleRefreshTemplate() {
      this.refreshTemplateLoading = true;
      const res = await this.$services.dmTicketApproRefreshTemplates({
        data: {
          approvalType: this.data.datasource.ticket.platform
        }
      });

      if (res.success) {
        this.templates = res.data;
        const obj = {};
        res.data.forEach((t) => {
          obj[t.templateIdentity] = t;
        });
        this.templatesObj = obj;
        this.refreshTemplateLoading = false;
      }
    }
  },
  created() {
    this.checkedTemplates = this.data.datasource.ticket.templates;
    this.getTicketTemplates();
  },
  watch: {
    'data.datasource': {
      handler(newValue) {
        console.log(newValue);
      },
      deep: true
    }
  }
};
</script>

<style scoped>
.refresh-template-btn {
  float: right;
  margin-bottom: 10px;
}
</style>
