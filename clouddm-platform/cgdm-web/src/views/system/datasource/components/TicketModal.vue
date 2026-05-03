<template>
  <a-modal :visible="visible" :title="$t('gong-dan-shen-pi-liu-cheng')" @cancel="handleCloseModal" :width="534" :mask-closable="false">
    <a-form-model :label-col="{ span: 3 }" :wrapper-col="{ span: 16 }">
      <!--      <a-form-model-item label="工单">-->
      <!--        <a-switch v-model="form.ticket" @change="handleTicketChange"/>-->
      <!--      </a-form-model-item>-->
      <a-form-model-item :label="$t('shen-pi-fang-shi')">
        <a-radio-group v-model="form.ticketType" @change="handleRefreshTemplate">
          <a-radio v-for="type of ticketTypes" :key="type.approvalType" :value="type.approvalType">
            {{ type.i18n }}
          </a-radio>
        </a-radio-group>
        <a-button :loading="refreshTemplateLoading" class="refresh-template-btn" @click="handleRefreshTemplate" size="small">
          {{ $t('shua-xin-shen-pi-mo-ban') }}
        </a-button>
      </a-form-model-item>
      <a-form-model-item :label="$t('shen-pi-liu-cheng')">
        <div>
          <div>{{ $t('quan-xian-shen-qing') }}</div>
          <a-select
            v-model="form.authTicket"
            :placeholder="$t('qing-xuan-ze-shen-pi-liu-cheng')"
            :filter-option="filterOption"
            show-search
            allowClear
          >
            <a-select-option v-for="template in ticketTemplates" :value="template.templateIdentity" :key="template.templateIdentity">
              {{ template.approTemplateName }}
            </a-select-option>
          </a-select>
        </div>
        <div>
          <div>{{ $t('shu-ju-bian-geng') }}</div>
          <a-select
            v-model="form.ticketArr"
            :placeholder="$t('qing-xuan-ze-shen-pi-liu-cheng')"
            mode="multiple"
            :filter-option="filterOption"
            show-search
          >
            <a-select-option v-for="template in ticketTemplates" :value="template.templateIdentity" :key="template.templateIdentity">
              {{ template.approTemplateName }}
            </a-select-option>
          </a-select>
        </div>
      </a-form-model-item>
    </a-form-model>
    <div class="footer">
      <a-button type="primary" @click="handleSubmit">{{ $t('bao-cun') }}</a-button>
      <a-button @click="handleCloseModal">{{ $t('qu-xiao') }}</a-button>
    </div>
  </a-modal>
</template>

<script>
import { APPROVAL_BIZ_TYPE } from '@/const';

export default {
  name: 'TicketModal',
  props: {
    ds: Object,
    visible: Boolean,
    handleCloseModal: Function
  },
  data() {
    return {
      templatesObj: {},
      authTemplateObj: {},
      form: {
        ticketType: 'DINGDING',
        ticketArr: [],
        authTicket: '',
        preAuthTicket: ''
      },
      ticketTemplates: [],
      ticketTemplatesObj: {},
      refreshTemplateLoading: false,
      ticketTypes: []
    };
  },
  async mounted() {
    const data = await this.$services.dmTicketApproTicketType();
    if (data.code === '1') {
      this.ticketTypes = data.data;
    }
  },
  methods: {
    filterOption(input, option) {
      return option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0;
    },
    async handleSubmit() {
      const templatesList = [];
      this.form.ticketArr.forEach((t) => {
        const add = {
          approvalType: this.form.ticketType,
          templateIdentity: this.ticketTemplatesObj[t].templateIdentity,
          templateName: this.ticketTemplatesObj[t].approTemplateName,
          approvalBiz: APPROVAL_BIZ_TYPE.EXECUTE
        };

        if (this.templatesObj[t] && this.templatesObj[t].approvalBiz === APPROVAL_BIZ_TYPE.EXECUTE) {
          add.approTemplateId = this.templatesObj[t].id;
        }

        templatesList.push(add);
      });

      const { authTicket } = this.form;
      if (authTicket) {
        const add = {
          approvalType: this.form.ticketType,
          templateIdentity: this.ticketTemplatesObj[authTicket].templateIdentity,
          templateName: this.ticketTemplatesObj[authTicket].approTemplateName,
          approvalBiz: APPROVAL_BIZ_TYPE.AUTH
        };

        if (this.templatesObj[authTicket] && this.templatesObj[authTicket].approvalBiz === APPROVAL_BIZ_TYPE.AUTH) {
          add.approTemplateId = this.templatesObj[authTicket].id;
        }

        templatesList.push(add);
      }

      const res = await this.$services.dmDsApproTemplateModifyDsApproTemplates({
        data: {
          dataSourceId: this.ds.id,
          templatesList
        },
        msg: this.$t('xiu-gai-gong-dan-cheng-gong')
      });

      if (res.success) {
        this.handleCloseModal();
      }
    },
    async handleTicketChange(checked) {
      if (checked) {
        const res = await this.$services.dmTicketApproListTemplates({
          data: { approvalType: this.form.ticketType }
        });

        if (res.success) {
          this.ticketTemplates = res.data;
          const ticketTemplatesObj = {};
          res.data.forEach((ticket) => {
            ticketTemplatesObj[ticket.templateIdentity] = ticket;
          });
          this.ticketTemplatesObj = ticketTemplatesObj;
        }
      }
    },
    async getDsTemplates() {
      const res = await this.$services.dmDsApproTemplateListDsApproTemplateByDsId({
        data: { dataSourceId: this.ds.id }
      });

      if (res.success) {
        if (res.data.length) {
          const templatesObj = {};
          const ticketArr = [];
          let authTicket = '';
          res.data.forEach((ticket) => {
            templatesObj[ticket.templateIdentity] = ticket;
            if (ticket.approvalBiz === APPROVAL_BIZ_TYPE.EXECUTE) {
              ticketArr.push(ticket.templateIdentity);
            } else {
              authTicket = ticket.templateIdentity;
            }
          });
          this.templatesObj = templatesObj;
          const form = {};
          form.ticketType = res.data[0].approvalType;
          form.ticketArr = ticketArr;
          form.authTicket = authTicket;
          this.form = form;
        }
        await this.handleTicketChange(true);
      }
    },
    async handleRefreshTemplate() {
      this.refreshTemplateLoading = true;
      const res = await this.$services.dmTicketApproRefreshTemplates({
        data: {
          approvalType: this.form.ticketType
        }
      });
      this.refreshTemplateLoading = false;

      if (res.success) {
        this.ticketTemplates = res.data;
        const ticketTemplatesObj = {};
        res.data.forEach((ticket) => {
          ticketTemplatesObj[ticket.templateIdentity] = ticket;
        });
        this.ticketTemplatesObj = ticketTemplatesObj;
      }
    }
  },
  created() {
    this.getDsTemplates();
  }
};
</script>

<style scoped>
.refresh-template-btn {
  float: right;
  margin-top: 4px;
}
</style>
