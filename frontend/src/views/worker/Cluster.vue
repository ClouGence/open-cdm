<template>
  <div class="cluster-container" style="padding: 16px">
    <Breadcrumb style="margin-bottom: 14px">
      <BreadcrumbItem>
        {{ type === 'dm' ? $t('cha-xun-ji-qi') : $t('tong-bu-ji-qi') }}
      </BreadcrumbItem>
    </Breadcrumb>
    <ClusterHeader
      :handleSearch="handleRefresh"
      :refreshLoading="refreshLoading"
      :handleAddCluster="handleAddCluster"
      :params="searchData"
    ></ClusterHeader>
    <Table
      border
      :columns="displayResourceColumns"
      :data="showData"
      size="small"
      :loading="refreshLoading"
      style="margin-top: 12px; margin-bottom: 64px"
    >
      <template #cluster="{ row }">
        <div>
          <a @click="handleCluster(row)">{{ row.clusterName }}</a>
          <CustomIcon type="CopyOutline" leftMargin hoverStyle @click="handleCopy(row.clusterName)" />
        </div>
      </template>
      <template #action="{ row }">
        <a class="text-cc-primary" style="margin-right: 16px" @click="handleCluster(row)">
          {{ $t('cha-kan-bai-ming-dan') }}
        </a>
        <a class="text-cc-primary" v-if="hasManageAuth" style="margin-right: 16px" @click="handleDeleteCluster(row)">
          {{ $t('shan-chu') }}
        </a>
      </template>
      <template #clusterDesc="{ row }">
        <span style="padding-right: 30px; display: block">{{ row.clusterDesc }}</span>
        <CustomIcon
          type="icon-v2-EditSimple"
          @click="handleEditDClusterDesc(row)"
          size="13px"
          hoverStyle
          style="position: absolute; right: 20px; top: 0"
        />
      </template>
    </Table>
    <div class="page-footer-container">
      <div class="page-footer-paging">
        <Page
          :total="total"
          show-total
          show-elevator
          @on-change="handlePageChange"
          show-sizer
          :page-size="size"
          @on-page-size-change="handlePageSizeChange"
          :model-value="page"
        />
      </div>
    </div>
    <CCModal v-model="showConfirmDelete" :title="$t('shan-chu-ji-qun-que-ren')">
      <div>
        <p>
          {{ $t('que-ren-yao-shan-chu-ji-qun-ming-cheng-wei-selectedclusterclustername-de-ji-qun-ma', [selectedCluster.clusterName]) }}
        </p>
      </div>
      <template #footer>
        <Button @click="handleCancel">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" @click="handleConfirmDeleteCluster">{{ $t('que-ren') }}</Button>
      </template>
    </CCModal>
    <CCModal v-model="showAddCluster" :title="isEdit ? $t('bian-ji-ji-qun') : $t('xin-zeng-ji-qun')" width="800px">
      <div style="padding: 10px">
        <Form :model="addCluster" label-position="right" :label-width="120">
          <FormItem :label="$t('ji-qun-miao-shu')">
            <Input style="width: 280px" v-model="addCluster.clusterDesc" />
          </FormItem>
          <FormItem v-if="type !== 'dm'" :label="$t('yun-huo-ji-fang-ming-cheng')">
            <RadioGroup v-model="addCluster.cloudOrIdcName" type="button" class="radio-group-radius-cluster" @on-change="handleChangeCloudOrIdcName">
              <Radio v-for="cloudOrIdcName of cloudOrIdcNames" :label="cloudOrIdcName.cloudOrIdcName" :key="cloudOrIdcName.cloudOrIdcName">
                {{ cloudOrIdcName.i18nName }}
              </Radio>
            </RadioGroup>
          </FormItem>
          <FormItem :label="$t('di-yu-0')">
            <!--            <RadioGroup v-model="addCluster.region" type="button">-->
            <!--              <Radio v-for="(region) of regions" :label="region.region" :key="region.region"-->
            <!--                     :disabled="supportedRegions.indexOf(region.region)===-1">-->
            <!--                {{ region.i18nName }}-->
            <!--              </Radio>-->
            <!--            </RadioGroup>-->
            <Dropdown trigger="click" style="margin-left: 20px" placement="bottom-start" transfer>
              <a href="javascript:void(0)">
                <span class="selected-region">{{ getRegionI18n(addCluster.region) }}</span>
                <Icon type="ios-arrow-down"></Icon>
              </a>
              <template #list>
                <div class="region-container">
                  <div v-for="area in regionAreas" :key="area.regionArea">
                    <div class="region-group" v-if="getRegions(area.regionArea).length > 0">
                      <h3>{{ area.i18nName }}</h3>
                      <div>
                        <RadioGroup v-model="addCluster.region" type="button" class="radio-group-radius-area custom-radio-group">
                          <Radio class="custom-radio" v-for="region in getRegions(area.regionArea)" :key="region.region" :label="region.region">
                            {{ region.i18nName }}
                          </Radio>
                        </RadioGroup>
                      </div>
                    </div>
                  </div>
                </div>
              </template>
            </Dropdown>
          </FormItem>
        </Form>
      </div>
      <template #footer>
        <Button @click="handleCancel">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" @click="handleConfirmAddCluster">{{ $t('bao-cun') }}</Button>
      </template>
    </CCModal>
    <CCModal v-model="showEditDesc" :title="$t('xiu-gai-ji-qun-miao-shu')" width="400px">
      <div>
        <p>
          {{ $t('xiu-gai-ji-qun-ming-cheng-wei-selectedrowclustername-de-ji-qun-de-miao-shu-wei', [selectedRow.clusterName]) }}
        </p>
        <Input v-model="clusterDesc" style="width: 280px; margin-top: 20px" />
      </div>
      <template #footer>
        <Button @click="handleCancelEdit">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" @click="handleConfirmEditDesc">{{ $t('que-ding') }}</Button>
      </template>
    </CCModal>
  </div>
</template>
<script>
import _ from 'lodash';
import { handleCopy as handleCopyUtil } from '@/utils/clipboard';
import { mapGetters, mapState } from 'vuex';
import store from '@/store';
import ClusterHeader from '@/components/function/cluster/ClusterHeader';
import fecha from 'fecha';
import UtilMapping from '../util';

export default {
  components: {
    ClusterHeader
  },
  computed: {
    displayResourceColumns() {
      if (this.type === 'dm') {
        return this.resourceColumns.filter((column) => column.key !== 'cloudOrIdcName');
      }
      return this.resourceColumns;
    },
    getRegions() {
      return (area) => {
        const regionsByArea = [];
        this.supportedRegions.forEach((region) => {
          if (region.regionArea === area) {
            regionsByArea.push(region);
          }
        });
        return regionsByArea;
      };
    },
    getRegionI18n() {
      return (theRegion) => {
        let i18nName = '';
        this.supportedRegions.forEach((region) => {
          if (region.region === theRegion) {
            i18nName = region.i18nName;
          }
        });
        return i18nName;
      };
    },
    ...mapState(['myAuth', 'userInfo']),
    hasManageAuth() {
      if (this.$route.name === 'System_Machine') {
        return this.myAuth.includes('DM_WORKER_MANAGE');
      } else {
        return this.myAuth.includes('CC_WORKER_MANAGE');
      }
    }
  },
  created() {
    if (this.$route.name === 'System_Machine') {
      this.type = 'dm';
    }
  },
  mounted() {
    this.init();
  },
  data() {
    return {
      type: 'cc',
      page: 1,
      size: 20,
      total: 0,
      showEditDesc: false,
      selectedRow: '',
      clusterDesc: '',
      deployMode: '',
      supportedRegions: [],
      UtilMapping,
      store,
      refreshLoading: false,
      isEdit: true,
      showAddCluster: false,
      showConfirmDelete: false,
      selectedCluster: {},
      regions: [],
      regionAreas: [],
      cloudOrIdcNames: [],
      addCluster: {
        clusterName: '',
        region: 'customer',
        cloudOrIdcName: 'SELF_MAINTENANCE'
      },
      searchData: {
        cloudOrIdcName: '',
        clusterDesc: '',
        clusterName: ''
      },
      resourceColumns: [
        {
          title: this.$t('ji-qun-ming-cheng'),
          key: 'clusterName',
          slot: 'cluster',
          width: 300
        },
        {
          title: this.$t('ji-qun-miao-shu'),
          key: 'clusterDesc',
          slot: 'clusterDesc',
          minWidth: 200
        },
        {
          title: this.$t('ji-qi-shu-liang'),
          key: 'workerCount',
          width: 120
        },
        {
          title: this.$t('ke-yong-shu-liang'),
          key: 'runningCount',
          width: 120
        },
        {
          title: this.$t('bu-ke-yong-shu-liang'),
          key: 'abnormalCount',
          width: 120
        },
        {
          title: this.$t('bu-shu-lei-xing'),
          key: 'cloudOrIdcName',
          width: 160,
          render: (h, params) => h('div', {}, UtilMapping.cloudOrIdcName[params.row.cloudOrIdcName])
        },
        {
          title: this.$t('di-yu-0'),
          key: 'region',
          width: 200,
          render: (h, params) => h('div', {}, UtilMapping.region[params.row.region])
        },
        {
          title: this.$t('chuang-jian-ren'),
          key: 'ownerName',
          width: 120
        },
        {
          title: this.$t('chuang-jian-shi-jian'),
          key: 'ownerName',
          width: 200,
          render: (h, params) => h('div', {}, fecha.format(new Date(params.row.gmtCreate), 'YYYY-MM-DD HH:mm:ss'))
        },
        {
          title: this.$t('cao-zuo'),
          key: '',
          slot: 'action',
          width: 180,
          fixed: 'right'
        }
      ],
      resourceData: [],
      showData: [],
      pagingData: []
    };
  },
  methods: {
    handleCopyUtil,
    buildDmRegions() {
      return [
        {
          region: 'customer',
          regionArea: 'REGION_AREA_CUSTOMER',
          defaultCheck: true,
          i18nName: UtilMapping.region.customer
        }
      ];
    },
    buildDmRegionAreas() {
      return [
        {
          regionArea: 'REGION_AREA_CUSTOMER',
          i18nName: UtilMapping.region.customer
        }
      ];
    },
    init() {
      console.log('this.type', this.type);
      if (this.type === 'dm') {
        this.regions = this.buildDmRegions();
        this.supportedRegions = this.buildDmRegions();
        this.regionAreas = this.buildDmRegionAreas();
      } else {
        // queryDeployMode()
        //   .then((res) => {
        //     if (res.data.code === '1') {
        //       this.deployMode = res.data.data;
        //     }
        //   });
        this.listRegions();
        this.listRegionAreas();
        this.listCloudOrIdcNames();
      }
      this.getClusterList(this.searchData);
    },
    handleRefresh(data, type) {
      this.searchData = data;
      this.getClusterList(data, type);
    },
    handleCluster(row) {
      if (this.type === 'dm') {
        localStorage.setItem(`dmcluster-${row.id}`, JSON.stringify(row));
        this.$router.push({ path: `/system/dmmachine/list/${row.id}` });
      } else {
        localStorage.setItem(`cluster-${row.id}`, JSON.stringify(row));
        this.$router.push({ path: `/ccsystem/resource/${row.id}` });
      }
    },
    handleAddCluster() {
      this.isEdit = false;
      this.addCluster = this.getDefaultAddCluster();
      if (this.type !== 'dm' && store.getters.isProductTrail) {
        this.cloudOrIdcNames = [
          {
            cloudOrIdcName: 'SELF_MAINTENANCE',
            defaultCheck: true,
            i18nName: 'Self Maintenance'
          }
        ];
      }
      this.showAddCluster = true;
    },
    handleDeleteCluster(row) {
      this.selectedCluster = row;
      this.showConfirmDelete = true;
    },
    handleConfirmDeleteCluster() {
      this.showConfirmDelete = false;
      this.selectedCluster.clusterId = this.selectedCluster.id;
      if (this.type === 'dm') {
        this.$services.dmClusterDelete({ data: this.selectedCluster }).then((res) => {
          if (res.success) {
            this.getClusterList(this.searchData);
            this.$Message.success(this.$t('shan-chu-cheng-gong'));
          }
        });
      } else {
        this.$services.ccClusterDelete({ data: this.selectedCluster }).then((res) => {
          if (res.success) {
            this.getClusterList(this.searchData);
            this.$Message.success(this.$t('shan-chu-cheng-gong'));
          }
        });
      }
    },
    handleEditCluster(row) {
      this.addCluster = _.cloneDeep(row);
      if (this.type === 'dm' && !this.addCluster.cloudOrIdcName) {
        this.addCluster.cloudOrIdcName = 'SELF_MAINTENANCE';
      }
      if (this.type === 'dm' && !this.addCluster.region) {
        this.addCluster.region = 'customer';
      }
      this.isEdit = true;
      this.showAddCluster = true;
    },
    handleConfirmAddCluster() {
      this.showAddCluster = false;
      if (this.type === 'dm') {
        this.$services.dmClusterCreate({ data: this.addCluster }).then((res) => {
          if (res.success) {
            this.getClusterList(this.searchData);
            this.$Message.success(this.$t('tian-jia-cheng-gong'));
            this.addCluster = this.getDefaultAddCluster();
          }
        });
      } else {
        this.$services.ccClusterCreate({ data: this.addCluster }).then((res) => {
          if (res.success) {
            this.getClusterList(this.searchData);
            this.$Message.success(this.$t('tian-jia-cheng-gong'));
            this.addCluster = this.getDefaultAddCluster();
          }
        });
      }
    },
    getDefaultAddCluster() {
      if (this.type === 'dm') {
        return {
          clusterName: '',
          region: 'customer',
          cloudOrIdcName: 'SELF_MAINTENANCE'
        };
      }

      return {
        clusterName: '',
        region: 'hangzhou',
        cloudOrIdcName: 'ALIBABA_CLOUD'
      };
    },
    handleCopy(data) {
      this.handleCopyUtil(data);
      if (data) {
        this.$Message.success(this.$t('fu-zhi-cheng-gong'));
      }
    },
    getClusterList(data, searchType) {
      this.refreshLoading = true;
      if (this.type === 'dm') {
        this.$services
          .dmClusterListByCondition({ data })
          .then((res) => {
            this.resourceData = res.data;
            this.total = this.resourceData.length;
            this.pagingData = _.cloneDeep(this.resourceData);
            if (searchType) {
              this.page = 1;
            }
            this.showData = this.resourceData.slice((this.page - 1) * this.size, this.page * this.size);
            this.refreshLoading = false;
          })
          .catch(() => {
            this.refreshLoading = false;
          });
      } else {
        this.$services
          .ccClusterListByCondition({ data })
          .then((res) => {
            this.resourceData = res.data;
            this.total = this.resourceData.length;
            this.pagingData = _.cloneDeep(this.resourceData);
            if (searchType) {
              this.page = 1;
            }
            this.showData = this.resourceData.slice((this.page - 1) * this.size, this.page * this.size);
            this.refreshLoading = false;
          })
          .catch(() => {
            this.refreshLoading = false;
          });
      }
    },
    listRegions() {
      this.$services.ccConstantRegion().then((res) => {
        if (res.success) {
          this.regions = res.data;
          this.$services.ccConstantSupportedRegion({ data: { cloudOrIdcName: this.addCluster.cloudOrIdcName } }).then((res2) => {
            if (res2.success) {
              this.supportedRegions = res2.data;
            }
          });
        }
      });
    },
    listRegionAreas() {
      this.$services.rdpConstantListRegionAreas().then((res) => {
        if (res.success) {
          this.regionAreas = res.data;
        }
      });
    },
    listCloudOrIdcNames() {
      this.$services.ccConstantCloudOrIdcName().then((res) => {
        if (res.success) {
          this.cloudOrIdcNames = res.data;
          // if (store.state.globalConfig.product_trial) {
          //   this.cloudOrIdcNames = ['SELF_MAINTENANCE'];
          // }
        }
      });
    },
    handleCancel() {
      this.showAddCluster = false;
      this.addCluster = this.getDefaultAddCluster();
      this.showConfirmDelete = false;
    },
    handleChangeCloudOrIdcName(data) {
      console.log('data', data);
      if (this.type === 'dm') {
        this.regions = this.buildDmRegions();
        this.supportedRegions = this.buildDmRegions();
        this.regionAreas = this.buildDmRegionAreas();
      } else {
        this.listRegions();
      }
      if (data === 'SELF_MAINTENANCE') {
        this.addCluster.region = 'customer';
      } else if (data === 'ALIBABA_CLOUD') {
        this.addCluster.region = 'virginia';
      } else if (data === 'AWS_CLOUD_HOSTED') {
        this.addCluster.region = 'virginia';
      }
    },
    handlePageChange(page) {
      this.page = page;
      this.showData = this.resourceData.slice((this.page - 1) * this.size, this.page * this.size);
      this.showData.map((item) => {
        item.showEditDesc = false;
        return null;
      });
    },
    handlePageSizeChange(size) {
      this.size = size;
      this.handlePageChange(1);
    },
    handleEditDClusterDesc(row) {
      this.clusterDesc = row.clusterDesc;
      this.selectedRow = row;
      this.showEditDesc = true;
    },
    async handleConfirmEditDesc() {
      this.showEditDesc = false;
      if (this.type === 'dm') {
        const res = await this.$services.dmClusterUpdateDesc({
          data: {
            clusterId: this.selectedRow.id,
            clusterDesc: this.clusterDesc
          }
        });
        if (res.success) {
          this.getClusterList(this.searchData);
          this.$Message.success(this.$t('xiu-gai-cheng-gong'));
          this.addCluster = this.getDefaultAddCluster();
        }
      } else {
        this.$services
          .ccClusterUpdateDesc({
            data: {
              clusterId: this.selectedRow.id,
              clusterName: this.selectedRow.clusterName,
              region: this.selectedRow.region,
              cloudOrIdcName: this.selectedRow.cloudOrIdcName,
              clusterDesc: this.clusterDesc
            }
          })
          .then((res) => {
            if (res.success) {
              this.getClusterList(this.searchData);
              this.$Message.success(this.$t('xiu-gai-cheng-gong'));
              this.addCluster = this.getDefaultAddCluster();
            }
          });
      }
    },
    handleCancelEdit() {
      this.showEditDesc = false;
    }
  },
  watch: {
    $route(val) {
      if (val.name === 'System_Machine') {
        this.type = 'dm';
      } else {
        this.type = 'cc';
      }
      this.init();
    }
  }
};
</script>
<style lang="less">
.cluster-container {
  .iconfont {
    font-size: 12px;
  }
}

.resource-manager-wrapper {
  padding: 24px;
  background: #ffffff;
  margin-top: 16px;
}

.iconbianji {
  color: #8d95a6;
  cursor: pointer;
}

.selected-region {
  color: rgba(0, 0, 0, 0.88);
  padding-right: 16px;
}

.region-container {
  padding: 20px;
  max-height: 500px;
  width: 1000px;
  overflow: auto;

  .region-group {
    margin-bottom: 20px;

    h3 {
      margin-bottom: 6px;
    }

    .ivu-radio-group-item {
      width: 180px;
      text-align: center;
      margin-bottom: 1px;
      height: 36px;
      line-height: 34px;
    }
  }

  .region-btn {
    width: 100%;
  }
}

//.radio-group-radius-cluster {
//  .ivu-radio-wrapper:not(:first-child) {
//    border-left: 0 !important;
//  }
//
//  .ivu-radio-wrapper {
//    border: 1px solid #dcdee2;
//    border-radius: 0 !important;
//  }
//
//  .ivu-radio-wrapper:first-child {
//    border-radius: 4px 0 0 4px !important;
//  }
//
//  .ivu-radio-wrapper:last-child {
//    border-radius: 0 4px 4px 0 !important;
//  }
//
//  .ivu-radio-wrapper:only-child {
//    border-radius: 4px !important;
//  }
//}

//.radio-group-radius-area {
//  .ivu-radio-wrapper {
//    border-radius: 4 !important;
//  }
//  .ivu-radio-wrapper:first-child {
//    border-radius: 4px 4px 4px 4px !important;
//  }
//  .ivu-radio-wrapper:last-child {
//    border-radius: 4px 4px 4px 4px !important;
//  }
//  .ivu-radio-wrapper::before {
//    display: none !important;
//  }
//  .ivu-radio-wrapper::after {
//    display: none !important;
//  }
//}

.spin {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.ip-white-list {
  background: #f5f5f5;
}

[data-theme='dark'] {
  .ip-white-list {
    background: var(--bg-tertiary);
    color: var(--text-primary);
  }
}
</style>
