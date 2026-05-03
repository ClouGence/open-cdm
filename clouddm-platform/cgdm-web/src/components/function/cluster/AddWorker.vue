<template>
  <div>
    <div class="add-worker-header">
      <span class="worker-header">
        <i class="iconfont iconClass"></i>
      </span>
      <span class="worker-header-name-main" style="margin-right: 10px">
        {{ clusterInfo.clusterName }}
      </span>
      <span v-if="clusterInfo.region" class="job-header-name-desc" style="margin-right: 20px">
        <i class="iconfont icondingwei"></i>
        {{ Mapping.region[clusterInfo.region] }}
      </span>
      <span>{{ $t('miao-shu-rdsinstancedesc', [clusterInfo.clusterDesc]) }}</span>
      <div style="margin-top: 10px">
        <Form ref="formInline" :model="clusterInfo" :rules="searchRule" label-position="right" :label-width="120">
          <FormItem :label="$t('yun-huo-ji-fang-ming-cheng')">
            <RadioGroup v-model="searchData.cloudOrIdcName" type="button">
              <Radio
                v-for="cloudOrIdcName of cloudOrIdcNames"
                :label="cloudOrIdcName.cloudOrIdcName"
                :disabled="clusterInfo.cloudOrIdcName && clusterInfo.cloudOrIdcName !== cloudOrIdcName.cloudOrIdcName"
                :key="cloudOrIdcName.cloudOrIdcName"
              >
                {{ cloudOrIdcName.i18nName }}
              </Radio>
            </RadioGroup>
          </FormItem>
          <FormItem :label="$t('di-qu-0')" style="margin-top: 4px" v-if="searchData.cloudOrIdcName === 'ALIBABA_CLOUD'">
            <!--            <RadioGroup v-model="searchData.region" type="button">-->
            <!--              <Radio v-for="(region) of regions" :label="region.region" :key="region.region"-->
            <!--                     :disabled="supportedRegions.indexOf(region.region)===-1||(clusterInfo.region&&clusterInfo.region!==region.region)">-->
            <!--                {{ region.i18nName }}-->
            <!--              </Radio>-->
            <!--            </RadioGroup>-->
            <Dropdown trigger="click" placement="bottom-start" transfer>
              <a href="javascript:void(0)">
                <span class="selected-region">{{ getRegionI18n(searchData.region) }}</span>
                <Icon type="ios-arrow-down"></Icon>
              </a>
              <template #list>
                <div class="region-container">
                  <div v-for="area in regionAreas" :key="area.regionArea">
                    <div class="region-group" v-if="getRegions(area.regionArea).length > 0">
                      <h3>{{ area.i18nName }}</h3>
                      <div>
                        <RadioGroup v-model="searchData.region" type="button">
                          <Radio
                            v-for="region in getRegions(area.regionArea)"
                            :key="region.region"
                            :disabled="clusterInfo.region && clusterInfo.region !== region.region"
                            :label="region.region"
                          >
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
          <!--          <FormItem style="margin-top: 4px">-->
          <!--            <Button type="primary" v-if="searchData.cloudOrIdcName==='ALIBABA_CLOUD'" ghost-->
          <!--                       @click="handleGetEcsList" :loading="loading">{{ $t('huo-qu-ji-qi-lie-biao') }}-->
          <!--            </Button>-->
          <!--            <span v-if="showNoData" style="margin-left: 10px"><i class="iconfont iconTIP"-->
          <!--                                                                 style="margin-right: 8px;color: #FFA30E;font-size: 14px"></i>{{ $t('gai-di-qu-mei-you-ji-qi') }}</span>-->
          <!--          </FormItem>-->
        </Form>
      </div>
    </div>
    <!--    <div v-if="searchData.cloudOrIdcName === 'ALIBABA_CLOUD'">-->
    <!--      <Row style="margin-top: 20px">-->
    <!--        <Col :span="15">-->
    <!--          <p class="transfer-title">-->
    <!--            <span style="margin-right: 42px">{{ $t('dai-xuan-ze-ji-qi') }}</span>-->
    <!--            <Input style="width: 280px;height: 28px" prefix="ios-search" v-model="searchKey"-->
    <!--                   :placeholder="$t('qing-shu-ru-miao-shu-shi-li-id-cha-xun')"/>-->
    <!--            <Button type="primary" style="margin-left: 10px" @click="handleFilter">{{ $t('shai-xuan') }}</Button>-->
    <!--          </p>-->
    <!--          <Table size="small" ref="selection" border stripe height="350px"-->
    <!--                 :columns="ecsColumn" :data="showData" @on-select="handleChooseWorker"-->
    <!--                 @on-select-cancel="handleChooseWorker" @on-select-all="handleChooseWorker"-->
    <!--                 @on-select-all-cancel="handleChooseWorker">-->
    <!--            <template #instanceId="{row}">-->
    <!--              <div style="padding: 10px 0;position: relative">-->
    <!--                <p>{{ row.instanceId }}</p>-->
    <!--                <p class="ecs-desc">{{ row.instanceName }}</p>-->
    <!--                <Tooltip v-if="!row.supported" transfer :content="row.unSupportedReasons.join(',')"-->
    <!--                         placement="right-start"-->
    <!--                         style="position: absolute;right: 0;top:20px;font-size: 16px;cursor: pointer">-->
    <!--                  <i class="iconfont icontip2" style="color: #FF6E0D"></i>-->
    <!--                </Tooltip>-->
    <!--                <span style="position: absolute;right: 0;top:24px;color: #FF6E0D"-->
    <!--                      v-if="row._disabled&&row.supported">{{ $t('yi-xuan-ze') }}</span>-->
    <!--              </div>-->
    <!--            </template>-->
    <!--            <template #ip="{row}">-->
    <!--              <div class="host-type">-->
    <!--                <p><span class="host-type-label">{{ $t('nei') }}</span>{{ row.privateIp.join(';') }}</p>-->
    <!--                <p v-if="row.allPublicIp&&row.allPublicIp.length>0" style="margin-top: 3px"><span-->
    <!--                  class="host-type-label">{{ $t('wai') }}</span>{{ row.allPublicIp.join(',') }}</p>-->
    <!--              </div>-->
    <!--            </template>-->
    <!--          </Table>-->
    <!--          <div class="transfer-left-footer">-->
    <!--            <Page @on-change="handlePageChange" size="small" :total="total" show-total-->
    <!--                  :page-size="size"/>-->
    <!--            &lt;!&ndash;                        <Button @click="handleListCluster('prev')" :disabled="pageData.pageNumber<=1"><Icon type="ios-arrow-back"/></Button>&ndash;&gt;-->
    <!--            &lt;!&ndash;                        <span style="margin: 0 10px">第{{pageData.pageNumber}}页</span>&ndash;&gt;-->
    <!--            &lt;!&ndash;                        <Button @click="handleListCluster('next')" :disabled="ecsData.length<pageData.pageSize"><Icon type="ios-arrow-forward"/></Button>&ndash;&gt;-->
    <!--          </div>-->
    <!--        </Col>-->
    <!--        <Col :span="2">-->
    <!--          <div class="transfer-btns">-->
    <!--            <div>-->
    <!--              <Button :type="hasChecked?'primary':'default'" @click="handleSelectWorker">-->
    <!--                <Icon type="ios-arrow-forward"/>-->
    <!--              </Button>-->
    <!--            </div>-->
    <!--            <div style="margin-top: 16px">-->
    <!--              <Button :type="needCancelList.length>0?'primary':'default'"-->
    <!--                         @click="handleCancelWorker">-->
    <!--                <Icon type="ios-arrow-back"/>-->
    <!--              </Button>-->
    <!--            </div>-->
    <!--          </div>-->
    <!--        </Col>-->
    <!--        <Col :span="7">-->
    <!--          <p class="transfer-title">{{ $t('yi-xuan-ze-ji-qi') }}</p>-->
    <!--          <div class="transfer-right">-->
    <!--            <Table size="small" border stripe height="400px"-->
    <!--                   :columns="selectedEcsColumn" :data="addWorkerList"-->
    <!--                   style="margin-bottom: 20px;margin-top: 10px"-->
    <!--                   @on-select="handleChooseCancelWorker"-->
    <!--                   @on-select-cancel="handleChooseCancelWorker"-->
    <!--                   @on-select-all="handleChooseCancelWorker"-->
    <!--                   @on-select-all-cancel="handleChooseCancelWorker"-->
    <!--            >-->
    <!--              <template #instanceId="{row}">-->
    <!--                <div style="padding: 10px 0">-->
    <!--                  <p>{{ row.instanceId }}</p>-->
    <!--                  <p class="ecs-desc">{{ row.instanceName }}</p>-->
    <!--                </div>-->
    <!--              </template>-->
    <!--            </Table>-->
    <!--          </div>-->
    <!--        </Col>-->
    <!--      </Row>-->
    <!--      <div slot="footer" class="modal-footer" style="margin-top: 20px">-->
    <!--        <Button type="primary" @click="saveWorker" :disabled="this.addWorkerList.length===0">{{ $t('que-ding') }}-->
    <!--        </Button>-->
    <!--        <Button @click="handleCancelAdd">{{ $t('qu-xiao') }}</Button>-->
    <!--      </div>-->
    <!--    </div>-->
    <div>
      <div class="add-worker-step-container">
        <RadioGroup v-model="deployType" style="margin-bottom: 16px">
          <Radio label="manual">{{ $t('shou-dong-bu-shu') }}</Radio>
          <Radio label="auto" v-if="showUpgradeSidecar">{{ $t('zi-dong-bu-shu') }}</Radio>
        </RadioGroup>
        <div class="add-worker-title">
          <div>
            {{ $t('zi-jian-ji-fang-tian-jia-ji-qi-de-bu-zhou-mu-qian-jin-zhi-chi-radhatcentosubuntumacos-xi-tong') }}
          </div>
          <div class="font-normal mt-2" style="font-size: 13px" v-if="deployType === 'auto'">
            *{{ $t('ru-guo-shi-yong-fei-root-qie-wu-sudo-quan-xian-zhang-hao-qing-shou-dong-chu-shi-hua-jdk-huan-jing') }}
          </div>
        </div>
        <div v-if="deployType === 'manual'">
          <div class="add-worker-step" style="margin-right: 80px">
            <span class="add-worker-step-count">1</span>
            <div class="add-worker-step-content" style="width: 200px">
              <i style="font-size: 120px" class="iconfont icontiaoma"></i>
            </div>
            <p class="add-worker-step-desc">
              <Button :disabled="hasCreated" type="primary" @click="handleCreateSelfMaintainWorker">
                {{ hasCreated ? $t('yi-sheng-cheng') : $t('sheng-cheng-ji-qi-wei-yi-biao-shi') }}
              </Button>
            </p>
          </div>
          <div class="add-worker-step" style="margin-right: 80px; max-width: 300px">
            <span class="add-worker-step-count">2</span>
            <div class="add-worker-step-content" style="background: none">
              <img
                v-if="$i18n.global.locale.value === 'en-US'"
                style="width: 277px"
                src="../../../assets/img001.png"
                :alt="$t('tian-jia-ji-qi-cao-zuo-shi-yi-tu')"
              />
              <img
                v-if="$i18n.global.locale.value === 'zh-CN'"
                style="width: 277px"
                src="../../../assets/img002.png"
                :alt="$t('tian-jia-ji-qi-cao-zuo-shi-yi-tu')"
              />
            </div>
            <p class="add-worker-step-desc" style="font-family: PingFangSC-Semibold, serif">
              {{ $t('jiang-ke-hu-duan-bao-fang-zhi-qian-yi-ji-qi-jie-ya-zhi-fei-root-yong-hu-zhu-mu-lu') }}
            </p>
          </div>
          <div class="add-worker-step" style="max-width: 400px">
            <span class="add-worker-step-count">3</span>
            <div class="add-worker-step-content" style="padding: 35px 14px; text-align: left">
              <p style="font-weight: 500">
                {{ $t('jiang-huo-qu-de-he-xin-pei-zhi-wen-jian-fu-zhi-dao') }}
              </p>
              <p>
                {{ $t('cloudcanalglobalconfconfproperties-zhong-ti-huan', ['cloudcanal']) }}
              </p>
              <p style="font-weight: 500; margin-top: 10px">
                {{ $t('shi-yong-fei-root-yong-hu-qi-dong-ke-hu-duan') }}
              </p>
              <p>{{ $t('ke-hu-duan-jie-ya-mu-lu-cloudcanalsidecarbinstartsidecarsh') }}</p>
            </div>
            <p class="add-worker-step-desc"></p>
          </div>
        </div>
        <div v-if="deployType !== 'manual'">
          <Form :model="autoDeploy" :rules="autoDeployValidate" :label-width="110" ref="auto-deploy-form">
            <FormItem label="ip" prop="remoteIp">
              <Input v-model="autoDeploy.remoteIp" style="width: 300px" />
            </FormItem>
            <FormItem :label="$t('duan-kou')" prop="sshPort">
              <Input v-model="autoDeploy.sshPort" style="width: 300px" autocomplete="new-password" />
            </FormItem>
            <FormItem :label="$t('yuan-cheng-an-zhuang-lu-jing')" prop="remoteInstallWorkerPath">
              <Input v-model="autoDeploy.remoteInstallWorkerPath" style="width: 300px" />
            </FormItem>
            <FormItem :label="$t('an-zhuang-bao-lu-jing')" prop="localInstallPackagePath">
              <Input v-model="autoDeploy.localInstallPackagePath" style="width: 300px" />
            </FormItem>
            <FormItem :label="$t('ssh-lei-xing')" prop="sshAuthType">
              <Select v-model="autoDeploy.sshAuthType" style="width: 300px">
                <Option value="password">{{ $t('mi-ma') }}</Option>
                <Option value="secret_key">{{ $t('mi-yue') }}</Option>
              </Select>
            </FormItem>
            <FormItem :label="$t('si-yue-lu-jing')" prop="privateKeyPath" v-if="autoDeploy.sshAuthType === 'secret_key'" key="privateKeyPath">
              <Input v-model="autoDeploy.privateKeyPath" style="width: 300px" />
            </FormItem>
            <FormItem :label="$t('zhang-hao')" prop="remoteUser" key="remoteUser">
              <Input v-model="autoDeploy.remoteUser" style="width: 300px" autocomplete="new-password" />
            </FormItem>
            <FormItem :label="$t('mi-ma')" prop="passphrase" v-if="autoDeploy.sshAuthType === 'secret_key'" key="passphrase">
              <Input v-model="autoDeploy.passphrase" style="width: 300px" />
            </FormItem>
            <FormItem :label="$t('mi-ma')" prop="remotePassword" v-if="autoDeploy.sshAuthType === 'password'" key="remotePassword">
              <Input v-model="autoDeploy.remotePassword" style="width: 300px" type="password" autocomplete="new-password" password />
            </FormItem>
            <FormItem :label="$t('ce-shi-lian-jie-0')" prop="connection">
              <Button @click="checkConnection" :loading="checkConnectionLoading">{{ $t('ce-shi-lian-jie') }}</Button>
              {{ this.connectionErrorMsg }}
            </FormItem>
            <FormItem :label="$t('shi-fou-chuan-shu-an-zhuang-bao')">
              <Checkbox v-model="autoDeploy.isDownloadPackage" />
              <Tooltip transfer>
                <Icon type="ios-help-circle-outline" size="16" style="margin-left: 4px" />
                <template #content>
                  {{ $t('qu-xiao-chuan-shu-an-zhuang-bao-xu-yao-jiang-an-zhuang-bao-fang-zhi-dao-yuan-cheng-an-zhuang-lu-jing-xia') }}
                </template>
              </Tooltip>
            </FormItem>
          </Form>
        </div>
      </div>
      <div style="margin-top: 20px">
        <div slot="footer" class="modal-footer" style="margin-top: 20px">
          <Button type="primary" @click="handleAutoDeploy" :disabled="checkConnectionLoading" v-if="deployType === 'auto'">
            {{ $t('zi-dong-bu-shu-0') }}
          </Button>
          <Button @click="handleCloseAddWorkerModal">{{ $t('guan-bi') }}</Button>
        </div>
      </div>
    </div>
    <StToken ref="stToken" :nextStep="handleGetEcsList"></StToken>
    <AliyunAKSK ref="aliyunAKSK"></AliyunAKSK>
  </div>
</template>
<script>
import fecha from 'fecha';
import _ from 'lodash';
import Mapping from '@/views/util';
import { mapGetters, mapState } from 'vuex';
import StToken from '../ApplyStToken';
import AliyunAKSK from '../ApplyAKSK';
import { Tooltip } from 'view-ui-plus';

export default {
  name: 'AddWorker',
  components: {
    Tooltip,
    StToken,
    AliyunAKSK
  },
  props: {
    clusterInfo: Object,
    handleCancel: Function,
    handleShowSighin: Function,
    getWorkList: Function
  },
  created() {
    this.autoDeploy.localInstallPackagePath = '/home/clougence/tar_gz/cloudcanal.tgz';
    this.autoDeploy.remoteInstallWorkerPath = '/home/clougence/';
    this.clusterId = this.$route.params.id;
    if (this.clusterInfo.cloudOrIdcName) {
      this.searchData.cloudOrIdcName = this.clusterInfo.cloudOrIdcName;
    } else {
      this.searchData.cloudOrIdcName = 'ALIBABA_CLOUD';
    }
    if (this.clusterInfo.region) {
      this.searchData.region = this.clusterInfo.region;
    } else if (this.searchData.cloudOrIdcName === 'ALIBABA_CLOUD') {
      this.searchData.region = 'hangzhou';
    } else {
      this.searchData.region = 'customer';
    }
  },
  computed: {
    ...mapState(['ccGlobalSetting', 'productClusterList', 'selectCcProductCluster']),
    ...mapGetters(['verifyType', 'upgradeSidecar']),
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
    showUpgradeSidecar() {
      if (this.ccGlobalSetting.productConsolePackageMode) {
        return this.ccGlobalSetting.productConsolePackageMode === 'TGZ';
      } else {
        let productConsolePackageMode = '';
        this.productClusterList.forEach((product) => {
          if (product.clusterCode === this.selectCcProductCluster) {
            productConsolePackageMode = product.config.productConsolePackageMode;
          }
        });
        return productConsolePackageMode === 'TGZ';
      }
    }
  },
  mounted() {
    this.listRegions();
    this.listRegionAreas();
    this.listCloudOrIdcNames();
  },
  data() {
    const validateConnection = (rule, value, callback) => {
      if (!this.hasConnection) {
        callback(new Error(this.$t('qing-xian-ce-shi-lian-jie')));
      } else if (!this.connectionSucceeded) {
        callback(new Error(this.$t('qing-xian-ce-shi-lian-jie-cheng-gong')));
      } else {
        callback();
      }
    };
    const validatePath = (rule, value, callback) => {
      if (value.endsWith('/')) {
        callback();
      } else {
        callback(new Error(this.$t('lu-jing-xu-yao-yi-xie-jie-wei-di-zhi')));
      }
    };
    return {
      hasConnection: false,
      connectionSucceeded: false,
      connectionErrorMsg: '',
      checkConnectionLoading: false,
      autoDeploy: {
        isDownloadPackage: true,
        sshPort: 22,
        executeTag: 'DEFAULT_REMOTE',
        remoteIp: '',
        remoteUser: '',
        remotePassword: '',
        localInstallPackagePath: '/home/bladepipe/tar_gz/bladepipe.tgz',
        remoteInstallWorkerPath: '/home/bladepipe/',
        sshAuthType: 'password',
        privateKeyPath: '',
        passphrase: ''
      },
      autoDeployValidate: {
        sshPort: [{ required: true, message: this.$t('duan-kou-bu-neng-wei-kong') }],
        privateKeyPath: [{ required: true, message: this.$t('si-yue-lu-jing-bu-neng-wei-kong') }],
        localInstallPackagePath: [{ required: true, message: this.$t('an-zhuang-bao-lu-jing-bu-neng-wei-kong') }],
        remoteInstallWorkerPath: [
          { required: true, message: this.$t('yuan-cheng-an-zhuang-lu-jing-bu-neng-wei-kong') },
          { validator: validatePath, trigger: 'blur' }
        ],
        remoteIp: [{ required: true, message: this.$t('ip-bu-neng-wei-kong') }],
        remoteUser: [
          {
            required: true,
            message: this.$t('zhang-hao-bu-neng-wei-kong')
          }
        ],
        remotePassword: [
          {
            required: true,
            message: this.$t('mi-ma-bu-neng-wei-kong')
          }
        ],
        connection: [
          {
            validator: validateConnection,
            trigger: 'blur'
          }
        ]
      },
      deployType: 'manual',
      hasCreated: false,
      showNoData: false,
      loading: false,
      cloudOrIdcNames: [],
      regions: [],
      regionAreas: [],
      supportedRegions: [],
      searchRule: {},
      searchData: {
        cloudOrIdcName: '',
        region: ''
      },
      hasChecked: false,
      needCancelList: [],
      needAddList: [],
      addedList: [],
      clusterId: 0,
      addWorkerList: [],
      searchKey: '',
      Mapping,
      searchEcs: {
        type: 'desc',
        name: ''
      },
      addWorkerForm: {},
      pageData: {
        startId: 0,
        pageSize: 5,
        pageNumber: 1
      },
      total: 0,
      page: 1,
      size: 4,
      addDataSourceRuleAkSk: {},
      ecsColumn: [
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          title: this.$t('shi-li-id'),
          key: 'instanceId',
          width: 280,
          slot: 'instanceId'
        },
        {
          title: 'IP',
          key: 'privateIp',
          width: 170,
          slot: 'ip'
        },
        {
          title: this.$t('qu-yu-0'),
          key: 'regionId',
          width: 140,
          render: (h, params) => h('div', {}, params.row.regionId)
        },
        {
          title: this.$t('cao-zuo-xi-tong'),
          key: 'osnameEn',
          width: 180
        },
        {
          title: 'cpu',
          key: 'cpuCount',
          width: 80
        },
        {
          title: this.$t('nei-cun-mb'),
          key: 'memoryMb',
          width: 120
        },
        {
          title: this.$t('guo-qi-shi-jian'),
          key: 'expiredTime',
          width: 180,
          render: (h, params) => h('div', {}, fecha.format(new Date(params.row.expiredTime), 'YYYY-MM-DD HH:mm:ss'))
        }
      ],
      ecsData: [],
      filteredData: [],
      showData: [],
      selectedEcsColumn: [
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          title: this.$t('shi-li-id'),
          key: 'instanceId',
          slot: 'instanceId'
        }
      ],
      addWorkerRule: {
        workerIp: [
          {
            required: true,
            message: 'The workerIp cannot be empty',
            trigger: 'blur'
          }
        ],
        publicIp: [
          {
            required: true,
            message: 'The publicIp cannot be empty',
            trigger: 'blur'
          }
        ]
      }
    };
  },
  methods: {
    handleCloseAddWorkerModal() {
      this.connectionErrorMsg = '';
      this.handleCancel();
    },
    checkConnection() {
      this.checkConnectionLoading = true;
      this.hasConnection = true;
      this.$services
        .ccAllWorkerCheckConnection({ data: { ...this.autoDeploy, manualFill: true } })
        .then((res) => {
          if (res.success) {
            this.connectionSucceeded = true;
            this.connectionErrorMsg = this.$t('ce-shi-lian-jie-cheng-gong');
            this.$refs['auto-deploy-form'] && this.$refs['auto-deploy-form'].validateField('connection');
          } else {
            this.connectionSucceeded = false;
            this.connectionErrorMsg = res.msg;
          }
        })
        .finally(() => {
          this.checkConnectionLoading = false;
        });
    },
    handleAutoDeploy() {
      this.$refs['auto-deploy-form'].validate((valid) => {
        if (valid) {
          this.$services
            .ccAllWorkerCreateAndInstall({
              data: {
                region: this.clusterInfo.region ? this.clusterInfo.region : this.searchData.region,
                cloudOrIdcName: this.searchData.cloudOrIdcName,
                clusterId: this.clusterId,
                ...this.autoDeploy,
                manualFill: true
              }
            })
            .then((res) => {
              if (res.success) {
                this.$Message.success(this.$t('kai-shi-zi-dong-bu-shu'));
                this.handleCloseAddWorkerModal();
                this.getWorkList();
              }
            });
        }
      });
    },
    listRegions() {
      this.$services.ccConstantRegion().then((res) => {
        if (res.success) {
          this.regions = res.data;
          this.$services.ccConstantSupportedRegion({ data: { cloudOrIdcName: this.searchData.cloudOrIdcName } }).then((res2) => {
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
        }
      });
    },
    handleGetEcsList() {
      this.loading = true;
      this.$services
        .ccAliyunEcsListEcs({
          data: {
            clusterId: this.clusterId,
            region: this.searchData.region
          }
        })
        .then((res) => {
          if (res.success) {
            this.ecsData = res.data;
            this.showNoData = this.ecsData.length === 0;
            this.ecsData.map((ecs) => {
              if (!ecs.supported) {
                ecs._disabled = true;
              }
              return null;
            });
            this.filteredData = _.cloneDeep(res.data);
            this.total = this.filteredData.length;
            this.showData = this.filteredData.slice((this.page - 1) * this.size, this.page * this.size);
          } else if (res.code === '6028') {
            this.$refs.stToken.handleShowAkSk();
          } else if (res.code === '2011') {
            this.$refs.aliyunAKSK.handleShowAkSk();
          }
          this.loading = false;
        })
        .catch(() => {
          this.loading = false;
        });
    },
    saveWorker() {
      const ecsInstanceIds = [];
      const addWorkerForm = {
        region: this.clusterInfo.region ? this.clusterInfo.region : this.searchData.region,
        ecsInstanceIds,
        clusterId: this.clusterId,
        pageData: this.pageData
      };

      this.addWorkerList.map((item) => {
        ecsInstanceIds.push(item.instanceId);
        return null;
      });

      this.addWorkerList = [];
      this.showData = [];
      this.$services.ccAliyunEcsAddAndInstall({ data: addWorkerForm }).then((res) => {
        if (res.success) {
          this.handleShowSighin(res.data);
        } else if (res.code === '6028') {
          this.$refs.stToken.handleShowAkSk();
        } else if (res.code === '2011') {
          this.$refs.aliyunAKSK.handleShowAkSk();
        }
      });
    },
    handleCancelAdd() {
      this.addWorkerList = [];
      this.showData = [];
      this.handleCancel();
    },
    handleAddWorkerInfo() {},
    handleListCluster(type) {
      if (type === 'next') {
        this.pageData.pageNumber++;
      } else if (type === 'prev') {
        this.pageData.pageNumber--;
      }
      this.handleGetEcsList();
    },
    handleSelectWorker() {
      this.filteredData = [];
      this.needAddList.map((item) => {
        this.ecsData.map((ecs) => {
          if (ecs.instanceId === item.instanceId) {
            this.addedList.push(item.instanceId);
            this.addWorkerList.push(item);
            ecs._disabled = true;
          }
          return null;
        });
        return null;
      });
      this.$refs.selection.selectAll(false);
      this.ecsData.push('');
      this.ecsData.pop();
      this.needCancelList = [];
      this.hasChecked = false;
      this.ecsData.map((item) => {
        if (item.instanceName.indexOf(this.searchKey) > -1 || item.instanceId.indexOf(this.searchKey) > -1) {
          this.filteredData.push(item);
        }
        return null;
      });
      this.total = this.filteredData.length;
      this.showData = this.filteredData.slice((this.page - 1) * this.size, this.page * this.size);
    },
    handleCancelWorker() {
      this.needCancelList.map((item) => {
        this.addWorkerList.map((ecs, index) => {
          if (item.instanceId === ecs.instanceId) {
            this.addWorkerList.splice(index, 1);
            this.addedList.map((r, i) => {
              if (r === ecs.instanceId) {
                this.addedList.splice(i, 1);
              }
              return null;
            });
          }
          return null;
        });
        this.ecsData.map((ecs) => {
          if (ecs.instanceId === item.instanceId) {
            ecs._disabled = false;
          }
          return null;
        });
        return null;
      });
      this.addWorkerList.push('');
      this.addWorkerList.pop();
      this.ecsData.push('');
      this.ecsData.pop();
      this.needCancelList = [];
      this.filteredData = [];
      this.ecsData.map((item) => {
        if (item.instanceName.indexOf(this.searchKey) > -1 || item.instanceId.indexOf(this.searchKey) > -1) {
          this.filteredData.push(item);
        }
        return null;
      });
      this.total = this.filteredData.length;
      this.showData = this.filteredData.slice((this.page - 1) * this.size, this.page * this.size);
    },
    handleChooseWorker(data) {
      this.needAddList = data;
      if (data.length > 0) {
        this.hasChecked = true;
      }
    },
    handleChooseCancelWorker(data) {
      this.needCancelList = data;
    },
    handleFilter() {
      this.page = 1;
      this.filteredData = [];
      this.ecsData.map((item) => {
        if (item.instanceName.indexOf(this.searchKey) > -1 || item.instanceId.indexOf(this.searchKey) > -1) {
          this.filteredData.push(item);
        }
        return null;
      });
      this.total = this.filteredData.length;
      this.showData = this.filteredData.slice((this.page - 1) * this.size, this.page * this.size);
    },
    handlePageChange(page) {
      this.page = page;
      this.showData = this.filteredData.slice((this.page - 1) * this.size, this.page * this.size);
    },
    handleCreateSelfMaintainWorker() {
      this.$services
        .ccWorkerCreateSelfMaintainWorker({
          data: {
            clusterId: this.clusterId,
            cloudOrIdcName: this.searchData.cloudOrIdcName,
            region: this.clusterInfo.region ? this.clusterInfo.region : this.searchData.region
          }
        })
        .then((res) => {
          if (res.success) {
            // this.hasCreated = true;
            this.getWorkList();
            this.handleCancel();
          }
        });
    }
  }
};
</script>
<style lang="less">
.add-worker-header {
  background-color: #deefff;
  padding: 8px 20px;
  border: 1px solid #dadada;

  .ivu-form-item {
    margin-bottom: 0;
  }
}

.worker-header {
  display: inline-block;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background-color: #f4c22d;
  color: #ffffff;
  text-align: center;
  line-height: 22px;
  box-shadow: 0 3px 12px 0 rgba(244, 194, 45, 0.79);
  margin-right: 16px;
  vertical-align: middle;

  .iconfont {
    font-size: 7px;
  }
}

.worker-header-name-main {
  font-family: PingFangSC-Medium, serif;
}

.transfer-title {
  font-weight: 500;
  margin-bottom: 7px;

  .ivu-input {
    height: 28px !important;
    line-height: 26px;
  }

  button {
    height: 28px !important;
    line-height: 26px;
  }
}

.transfer-left {
  width: 100%;
  height: 460px;
  border: 1px solid #dadada;
  position: relative;

  .transfer-left-search {
    padding: 10px;
    background-color: #fafafa;
    border-bottom: 1px solid #dadada;
    position: relative;

    button {
      /*position: absolute;*/
      /*right: 10px;*/
      /*top: 10px;*/
      margin-left: 6px;
    }
  }

  .transfer-left-item {
    padding: 16px 16px 15px 52px;
    border-bottom: 1px solid #dadada;
    position: relative;

    .ivu-checkbox-wrapper {
      position: absolute;
      left: 16px;
      top: 34px;
    }
  }
}

.transfer-left-footer {
  width: 100%;
  height: 48px;
  text-align: center;
  line-height: 48px;
  border-left: 1px solid #dadada;
  border-right: 1px solid #dadada;
  border-bottom: 1px solid #dadada;
}

.transfer-btns {
  width: 100%;
  text-align: center;
  vertical-align: middle;
  margin-top: 200px;
  /*line-height: 500px;*/
}

.ecs-desc {
  color: #8d95a6;
  font-family: PingFangSC-Regular, serif;
  margin-top: 4px;
}

.host-type {
  padding: 12px 0;
}

.host-type-label {
  font-size: 12px;
  color: rgba(0, 0, 0, 0.88);
  background-color: #deefff;
  display: inline-block;
  //width: 16px;
  height: 16px;
  border-radius: 4px;
  text-align: center;
  line-height: 16px;
  margin-right: 4px;
}

.add-worker-step-container {
  border: 1px solid #dadada;
  border-top: none;
  padding: 20px 52px;
  max-height: 500px;
  overflow: auto;

  .add-worker-title {
    font-family: PingFangSC-Medium, serif;
    font-weight: 500;
    margin-bottom: 33px;
  }
}

.add-worker-step {
  display: inline-block;
  text-align: center;
  vertical-align: top;

  .add-worker-step-count {
    display: inline-block;
    width: 42px;
    height: 42px;
    border-radius: 50%;
    background: #deefff;
    text-align: center;
    line-height: 42px;
    font-size: 24px;
    font-family: PingFangSC-Semibold, serif;
    margin-bottom: 26px;
  }

  .add-worker-step-content {
    background-color: #deefff;
  }

  .add-worker-step-desc {
    margin-top: 16px;
  }
}

.code-container {
  background: #f6f8fa;
  padding: 1rem;
  border-radius: 0.4rem;
}
</style>
