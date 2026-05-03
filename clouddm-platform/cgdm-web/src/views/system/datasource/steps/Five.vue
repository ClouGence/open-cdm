<template>
  <div class="step step-five">
    <a-table size="small" :columns="datasourceListColumns" :data-source="datasourceList" :rowKey="(record) => record.instanceId">
      <!--      <template slot="ticket-title">-->
      <!--        <a-checkbox :indeterminate="selectedDatasourceNum > 0 && selectedDatasourceNum < datasourceList.length"-->
      <!--                  :checked="selectedDatasourceNum === datasourceList.length"-->
      <!--                  @change="handleAllChange"/>-->
      <!--        开启审批流程-->
      <!--        <a-popover>-->
      <!--          <cc-iconfont name="tip-black" size="14"/>-->
      <!--          <template slot="content">-->
      <!--            绑定的集群机器的ip会自动被添加到对应数据库实例的白名单列表中-->
      <!--          </template>-->
      <!--        </a-popover>-->
      <!--      </template>-->
      <!--      <template #ticket="record">-->
      <!--        <a-checkbox v-model="record.ticket.turn" @change="handleChange"/>-->
      <!--      </template>-->
      <template #name="record">{{ record.instanceDesc }}/{{ record.instanceId }}</template>
      <template #platform="record">
        <a-select v-model="record.ticket.platform" :filter-option="filterOption" show-search>
          <a-select-option v-for="platform in platforms" :value="platform.value" :key="platform.value">
            {{ platform.label }}
          </a-select-option>
        </a-select>
      </template>
      <template #process="record">
        {{ record.ticket.templates.length }}{{ $t('ge') }}
        <a-button type="link" @click="handleSelectProcess(record)">
          {{ $t('xuan-ze-shen-pi-liu-cheng') }}
        </a-button>
      </template>
    </a-table>
    <div class="footer">
      <a-button @click="handleBack">{{ $t('shang-yi-bu') }}</a-button>
      <a-button type="primary" @click="handleAddAllDs">
        {{ $t('xia-yi-bu-que-ren-tian-jia') }}
      </a-button>
    </div>
    <process-modal
      :visible="showProcessModal"
      :handle-close-modal="handleCloseModal"
      :data="{ datasource: selectedDatasource }"
      v-if="showProcessModal"
      :handle-templates-change="handleTemplatesChange"
    />
  </div>
</template>

<script>
import ProcessModal from '@/views/system/datasource/components/ProcessModal';

export default {
  name: 'Five',
  components: { ProcessModal },
  props: {
    stepData: Array,
    handleBack: Function,
    handleAddAllDs: Function
  },
  data() {
    return {
      selectedDatasourceNum: 0,
      selectedDatasource: {},
      datasourceList: [{}],
      datasourceListColumns: [
        {
          title: this.$t('shi-li-ming-cheng-id'),
          scopedSlots: { customRender: 'name' }
        },
        // {
        //   slots: { title: 'ticket-title' },
        //   scopedSlots: { customRender: 'ticket' }
        // },
        {
          title: this.$t('shen-pi-ping-tai'),
          scopedSlots: { customRender: 'platform' }
        },
        {
          title: this.$t('shen-pi-liu-cheng'),
          scopedSlots: { customRender: 'process' }
        }
      ],
      platforms: [
        {
          value: 'DINGDING',
          label: this.$t('ding-ding')
        }
      ],
      showProcessModal: false,
      templatesObj: {}
    };
  },
  methods: {
    filterOption(input, option) {
      return option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0;
    },
    handleSelectProcess(record) {
      this.showProcessModal = true;
      this.selectedDatasource = record;
    },
    handleCloseModal() {
      this.showProcessModal = false;
    },
    handleAllChange(value) {
      this.datasourceList.forEach((ds) => {
        ds.ticket.turn = value;
      });
      this.datasourceList = [...this.datasourceList];

      if (value) {
        this.selectedDatasourceNum = this.datasourceList.length;
      } else {
        this.selectedDatasourceNum = 0;
      }
    },
    handleChange(value) {
      let num = this.selectedDatasourceNum;
      if (value) {
        num++;
      } else {
        num--;
      }
      this.selectedDatasourceNum = num;
    },
    handleTemplatesChange(templates, templatesObj) {
      this.selectedDatasource.ticket.templates = templates;
      this.datasourceList.forEach((ds) => {
        if (ds.id === this.selectedDatasource.id) {
          ds = this.selectedDatasource;
        }
      });
      this.datasourceList = [...this.datasourceList];
      this.templatesObj = templatesObj;
    }
  }
};
</script>

<style scoped></style>
