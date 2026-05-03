<template>
  <div class="spec-list-container">
    <table-list-layout :breadcrumb-list="breadcrumbList" :tableData="specList" :set-show-data="setShowData" ref="layout">
      <template v-slot:left>
        <Input v-model="search" style="width: 280px; margin-right: 10px"></Input>
        <Button @click="getSpecList" type="primary">{{ $t('cha-xun') }}</Button>
      </template>
      <template v-slot:right>
        <Button @click="handleAddSpec" type="primary" ghost style="margin-right: 10px" icon="md-add">
          {{ $t('xin-jian-gui-fan') }}
        </Button>
        <Button @click="getSpecList">
          <CustomIcon type="icon-v2-Refresh" />
        </Button>
      </template>
      <template v-slot:table>
        <Table border :columns="specColumns" :data="showData" size="small" :loading="specListLoading">
          <template #name="{ row }">
            <div>
              {{ row.name }}
              <Icon type="ios-create-outline" style="margin-left: 5px; cursor: pointer" @click="handleEditSpecName(row)" />
            </div>
          </template>
          <template #description="{ row }">
            <div>
              {{ row.description }}
              <Icon type="ios-create-outline" style="margin-left: 5px; cursor: pointer" @click="handleEditSpecDesc(row)" />
            </div>
          </template>
          <template #enable="{ row }">
            <div>
              <i-switch v-model="row.enable" true-color="#52C41A" @on-change="handleEnableChange({ spec: row, enable: $event })" />
            </div>
          </template>
          <template #action="{ row }">
            <div>
              <router-link :to="`/system/dmspec/${row.specId}`" style="margin-right: 10px">
                {{ $t('xiang-qing') }}
              </router-link>
              <Poptip
                transfer
                confirm
                :title="$t('que-ding-yao-shan-chu-gai-gui-fan-ma')"
                :ok-text="$t('que-ding')"
                :cancel-text="$t('qu-xiao')"
                @on-ok="handleDeleteSpec({ specId: row.specId })"
              >
                <a v-if="myAuth.includes('DM_SECRULES_MANAGE')">{{ $t('shan-chu') }}</a>
              </Poptip>
            </div>
          </template>
        </Table>
      </template>
    </table-list-layout>
    <CCModal v-model="showCreateSpecModal" :title="$t('xin-jian-gui-fan')" @cancel="handleCloseModal" @ok="handleCreateSpec">
      <Form ref="specForm" :model="newSpec" :rules="specRules" v-if="showCreateSpecModal">
        <FormItem :label="$t('gui-fan-ming-cheng')" prop="specName">
          <Input v-model="newSpec.specName" />
        </FormItem>
        <FormItem :label="$t('gui-fan-miao-shu')">
          <Input v-model="newSpec.specDesc" />
        </FormItem>
      </Form>
    </CCModal>
    <CCModal v-model="showForceSpecModal" :title="forceSpecModalTitle" @cancel="handleCloseModal" @ok="handleForce">
      <Alert type="warning">
        <div v-html="forceSpecModalText"></div>
      </Alert>
      <Table :columns="forceSpecRefererColumns" :data="forceSpecRefererList" size="small">
        <template #envDesc="{ row }">
          <Tooltip :content="row.envDesc" placement="top" transfer>
            <span class="force-spec-env-desc-cell">
              {{ row.envDesc }}
            </span>
          </Tooltip>
        </template>
      </Table>
    </CCModal>
    <CCModal v-model="showEditSpecNameModal" :title="$t('xiu-gai-gui-fan-ming-cheng')" @ok="handleEditSpecConfig" @cancel="handleCloseModal">
      <Input v-model="newSpecName" />
    </CCModal>
    <CCModal v-model="showEditSpecDescModal" :title="$t('xiu-gai-gui-fan-miao-shu')" @ok="handleEditSpecConfig" @cancel="handleCloseModal">
      <Input v-model="newSpecDesc" />
    </CCModal>
  </div>
</template>
<script>
import TableListLayout from '@/layout/TableListLayout';
import { mapState } from 'vuex';

export default {
  name: 'SpecList',
  components: { TableListLayout },
  created() {
    this.getSpecList();
    if (this.$route.query.type === 'create') {
      this.showCreateSpecModal = true;
    }
  },
  data() {
    return {
      showEditSpecNameModal: false,
      showEditSpecDescModal: false,
      breadcrumbList: [{ label: this.$t('gui-fan-lie-biao') }],
      forceEvent: {
        type: '',
        data: {}
      },
      originalEnableState: null,
      showForceSpecModal: false,
      forceSpecModalTitle: '',
      forceSpecModalText: '',
      forceSpecRefererList: [],
      forceSpecRefererColumns: [
        {
          title: this.$t('huan-jing-ming-cheng'),
          key: 'envName'
        },
        {
          title: this.$t('huan-jing-miao-shu'),
          key: 'envDesc',
          slot: 'envDesc'
        }
      ],
      selectedSpec: {},
      specRules: {
        specName: [
          {
            required: true,
            message: this.$t('qing-shu-ru-gui-fan-ming-cheng')
          }
        ],
        specDesc: [
          {
            required: true,
            message: this.$t('qing-shu-ru-gui-fan-miao-shu')
          }
        ]
      },
      newSpec: {
        specName: '',
        specDesc: ''
      },
      showCreateSpecModal: false,
      search: '',
      total: 0,
      specList: [],
      specListLoading: false,
      showData: [],
      specColumns: [
        {
          title: this.$t('gui-fan-ming-cheng'),
          slot: 'name',
          width: 200
        },
        {
          title: this.$t('gui-fan-miao-shu'),
          slot: 'description'
        },
        {
          title: this.$t('xiu-gai-shi-jian'),
          key: 'lastModified',
          width: 150
        },
        {
          title: this.$t('qi-yong'),
          slot: 'enable',
          width: 80
        },
        {
          title: this.$t('cao-zuo'),
          slot: 'action',
          width: 110,
          fixed: 'right'
        }
      ],
      newSpecName: '',
      newSpecDesc: ''
    };
  },
  computed: {
    ...mapState(['myAuth'])
  },
  methods: {
    handleEditSpecName(row) {
      this.selectedSpec = row;
      this.newSpecName = row.name;
      this.newSpecDesc = row.description;
      this.showEditSpecNameModal = true;
    },
    handleEditSpecDesc(row) {
      this.selectedSpec = row;
      this.newSpecName = row.name;
      this.newSpecDesc = row.description;
      this.showEditSpecDescModal = true;
    },
    async handleEditSpecConfig() {
      const { specId } = this.selectedSpec;
      const res = await this.$services.dmSecurityRulesSpecUpdateInfo({
        data: {
          specId,
          newName: this.newSpecName,
          newDesc: this.newSpecDesc
        }
      });

      if (res.success) {
        this.$Message.success(this.$t('xiu-gai-cheng-gong'));
        await this.getSpecList();
      }
      this.handleCloseModal();
    },
    setShowData(data) {
      this.showData = data;
    },
    handleForce() {
      this.forceEvent.data.force = true;
      console.warn(123, this.forceEvent);

      if (this.forceEvent.type === 'delete') {
        this.handleDeleteSpec({ ...this.forceEvent.data });
      }
      if (this.forceEvent.type === 'enable') {
        this.handleEnableChange({ ...this.forceEvent.data });
      }
      this.handleCloseModal();
    },
    async handleEnableChange({ spec, specId, enable, force = false }) {
      console.warn(321, specId, force);

      this.selectedSpec = spec;
      this.originalEnableState = !enable;
      const data = {
        specId: spec?.specId || specId,
        enable,
        force
      };
      const res = await this.$services.dmSecurityRulesSpecConfig({
        data
      });

      if (res.success) {
        if (res.data) {
          if (res.data.success) {
            this.$Message.success(res.data.message);
            this.handleCloseModal();
            await this.getSpecList();
          } else {
            this.selectedSpec.enable = !enable;
            this.forceEvent = {
              type: 'enable',
              data
            };
            this.showForceSpecModal = true;
            this.forceSpecModalTitle = this.$t('qiang-zhi-guan-bi');
            this.forceSpecModalText = res.data.message;
            this.forceSpecRefererList = res.data.referer;
          }
        }
      } else {
        this.selectedSpec.enable = !enable;
      }
    },
    async handleDeleteSpec({ specId, force = false }) {
      this.selectedSpec = { specId };
      const data = {
        specId,
        force
      };
      const res = await this.$services.dmSecurityRulesSpecDelete({
        data
      });

      if (res.success) {
        if (res.data) {
          if (res.data.success) {
            this.showForceSpecModal = false;
            this.$Message.success(res.data.message);
            await this.getSpecList();
          } else {
            this.forceEvent = {
              type: 'delete',
              data
            };
            this.showForceSpecModal = true;
            this.forceSpecModalTitle = this.$t('qiang-zhi-shan-chu');
            this.forceSpecModalText = res.data.message;
            this.forceSpecRefererList = res.data.referer;
          }
        }
      }
    },
    async handleCreateSpec() {
      const res = await this.$services.dmSecurityRulesSpecCreate({
        data: this.newSpec
      });

      if (res.success) {
        this.$Message.success(this.$t('xin-jian-gui-fan-cheng-gong'));
        this.handleCloseModal();
        await this.getSpecList();
      }
      this.handleCloseModal();
    },
    handleCloseModal() {
      this.newSpec = {
        specName: '',
        specDesc: ''
      };
      if (this.showForceSpecModal && this.originalEnableState !== null && this.selectedSpec) {
        this.selectedSpec.enable = this.originalEnableState;
      }
      this.forceEvent = {
        type: '',
        data: {}
      };
      this.originalEnableState = null;
      this.showCreateSpecModal = false;
      this.showForceSpecModal = false;
      this.showEditSpecNameModal = false;
      this.showEditSpecDescModal = false;
      this.newSpecDesc = '';
      this.newSpecName = '';
    },
    handleAddSpec() {
      this.showCreateSpecModal = true;
    },
    async getSpecList() {
      this.specListLoading = true;
      const res = await this.$services.dmSecurityRulesSpecList({
        data: {
          search: this.search
        }
      });

      this.specListLoading = false;
      if (res.success) {
        this.specList = res.data;
        const { pageNum, pageSize } = this.$refs.layout;
        this.showData = this.specList.slice((pageNum - 1) * pageSize, pageNum * pageSize);
      }
    }
  }
};
</script>
<style lang="less" scoped>
.spec-list-container {
  height: 100%;
  display: flex;
  flex-direction: column;

  .spec-list {
    padding: 16px;
    flex: 1;

    .header {
      margin-top: 14px;
      margin-bottom: 14px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      border: 1px solid #ededed;
      height: 60px;
      padding: 0 20px;
    }
  }

  .footer {
    box-shadow: 6px 2px 23px 0 rgba(197, 197, 197, 0.5);
    display: flex;
    align-items: center;
    justify-content: center;
    height: 60px;
  }
}
.force-spec-env-desc-cell {
  display: inline-block;
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
