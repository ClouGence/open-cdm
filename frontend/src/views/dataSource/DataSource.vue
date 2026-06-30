<template>
  <div class="content-wrapper">
    <second-confirm-modal
      :title="$t('shan-chu-shu-ju-yuan')"
      :event="SECOND_CONFIRM_EVENT_LIST.DELETE_DATASOURCE"
      :confirm-text="selectedRow.instanceId"
      :visible="showDeleteDataSourceConfirm"
      :confirm-button-text="$t('shan-chu-shu-ju-yuan')"
      confirm-button-type="text"
      confirm-button-danger
      hide-cancel-button
      disable-confirm-until-matched
      ref="second-confirm-modal"
      :handle-confirm="deleteDataSource"
      :handle-close="handleCancelEdit"
    >
      <Alert class="delete-datasource-confirm-tip">
        {{ $t('shan-chu-shu-ju-yuan') }}
        <span class="delete-datasource-confirm-id" @dblclick="selectConfirmText">
          {{ selectedRow.instanceId }}
        </span>
        {{ $t('qing-zai-xia-fang-zhong-fu-shu-ru-gai-id') }}
      </Alert>
    </second-confirm-modal>
    <DataSourceHeader
      :handleSearch="getDataSourceList"
      :searchKey="searchKey"
      :supportAdd="canManageDataSource"
      :handleShowAddDataSource="handleShowAddDataSource"
      :handleChangeSearchType="handleChangeSearchType"
      :refreshLoading="refreshLoading"
      @update-search-key="handleUpdateSearchKey"
    ></DataSourceHeader>
    <div class="data-source-container datasource-list-panel">
      <div class="datasource-table-wrap">
        <Table class="datasource-table" :columns="dataSourceColumn" :data="showData" :loading="refreshLoading">
          <template #instanceId="{ row }">
            <div class="datasource-identity">
              <DataSourceIcon class="datasource-type-icon" size="32px" :type="row.dataSourceType" :instanceType="row.deployType"></DataSourceIcon>
              <div class="datasource-info-text">
                <div class="datasource-main-info">
                  <Tooltip :content="row.instanceDesc || $t('zan-wu-miao-shu')" placement="bottom" transfer>
                    <span class="datasource-primary-content datasource-name">
                      {{ row.instanceDesc || $t('zan-wu-miao-shu') }}
                    </span>
                  </Tooltip>
                  <CustomIcon
                    class="iconfont icon datasource-edit-icon"
                    v-if="myAuth.includes('RDP_DS_MANAGE') || myAuth.includes('RDP_ALL_DATASOURCE_MANAGE')"
                    @click="handleEditDataSourceDesc(row)"
                    type="icon-v2-EditingPen"
                    size="16px"
                  />
                  <Tooltip v-if="row.lifeCycleState !== 'CREATED'" :content="$t('shu-ju-yuan-zheng-zai-chuang-jian-zhong')" placement="top" transfer>
                    <span class="datasource-creating-indicator"></span>
                  </Tooltip>
                  <div>
                    <Tooltip
                      placement="right"
                      class="alarm-icon"
                      transfer
                      :content="$t('cun-zai-yi-chang-de-hou-tai-ren-wu-qing-dian-ji-chu-li')"
                      v-if="row.consoleTaskState === 'FAILED'"
                    >
                      <span style="display: inline-block; margin-left: 6px" @click="handleGoConsoleJob(row)">
                        <i class="iconfont iconyibuforce"></i>
                      </span>
                    </Tooltip>
                  </div>
                </div>
                <div class="data-job-desc datasource-secondary-content datasource-id-text">
                  {{ row.instanceId }}
                </div>
              </div>
            </div>
          </template>
          <template #action="{ row }">
            <div v-if="canManageDataSource" class="datasource-action-group">
              <Button
                type="text"
                size="small"
                :loading="testingDataSourceId === row.id"
                :disabled="row.lifeCycleState !== 'CREATED' || testingDataSourceId !== null"
                @click="handleTestConnection(row)"
              >
                {{ $t('ce-shi') }}
              </Button>
              <Button type="text" size="small" @click="handleKvConfigs(row)">
                {{ $t('bian-ji') }}
              </Button>
              <Button type="text" size="small" class="datasource-action-danger" @click="handleDeleteConfirm(row)">
                {{ $t('shan-chu') }}
              </Button>
            </div>
          </template>
          <template #host="{ row }">
            <div class="host-type">
              <p class="datasource-primary-content">{{ row.publicHost || row.privateHost || row.host || '-' }}</p>
            </div>
          </template>
          <template #instanceDesc="{ row }">
            <div style="position: relative">
              <Tooltip :content="row.instanceDesc" placement="right" transfer>
                <span class="datasource-desc-content">{{ row.instanceDesc }}</span>
              </Tooltip>
              <CustomIcon
                type="icon-v2-EditSimple"
                size="13px"
                @click="handleEditDataSourceDesc(row)"
                hoverStyle
                style="position: absolute; right: 5px; top: 3px"
              />
            </div>
          </template>
        </Table>
      </div>
      <div class="page-footer-container datasource-list-footer">
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
    </div>
    <!--    <Page class="page-container" :total="total" show-total show-elevator @on-change="handlePageChange" show-sizer-->
    <!--          :page-size="size"-->
    <!--          @on-page-size-change="handlePageSizeChange"/>-->
    <CCModal v-model="showEditDesc" :title="$t('xiu-gai-shu-ju-yuan-miao-shu')" width="520px" :mask-closable="false">
      <div class="edit-desc-modal">
        <Form label-position="top">
          <FormItem>
            <Input
              ref="instanceDescInput"
              v-model="instanceDesc"
              type="textarea"
              :rows="4"
              :maxlength="128"
              show-word-limit
              clearable
              :placeholder="$t('qing-shu-ru-miao-shu-ming-cheng')"
            />
          </FormItem>
        </Form>
      </div>
      <template #footer>
        <div class="edit-desc-footer">
          <Button @click="handleConfirmEditDesc" type="primary" :loading="editDescLoading" :disabled="!canSubmitDesc">
            {{ $t('bao-cun') }}
          </Button>
        </div>
      </template>
    </CCModal>
    <CCModal v-model="showEditAccount" :title="$t('xiu-gai-zhang-hao')" width="600px">
      <div>
        <Form
          label-position="right"
          :label-width="145"
          style="margin-top: 10px"
          :model="accountInfo"
          :rules="accountInfoValidate"
          ref="account-info-form"
        >
          <FormItem :label="$t('ren-zheng-fang-shi')" key="securityType">
            <Select v-model="accountInfo.securityType" style="width: 280px">
              <Option v-for="security in securitySetting" :value="security.securityType" :key="security.securityType">
                {{ security.securityTypeI18nName }}
              </Option>
            </Select>
          </FormItem>
          <FormItem
            :label="$t('zhang-hao')"
            prop="account"
            key="account"
            v-if="securitySettingObj[accountInfo.securityType] && securitySettingObj[accountInfo.securityType].needUserName"
          >
            <Input v-model="accountInfo.account" style="width: 280px"></Input>
          </FormItem>
          <FormItem
            :label="$t('mi-ma')"
            prop="password"
            key="password"
            v-if="securitySettingObj[accountInfo.securityType] && securitySettingObj[accountInfo.securityType].needPassword"
          >
            <Input
              type="password"
              :placeholder="$t('mo-ren-bu-zhan-shi-dang-qian-mi-ma-qing-shu-ru-xin-mi-ma')"
              password
              autocomplete="new-password"
              v-model="accountInfo.password"
              style="width: 280px"
            ></Input>
          </FormItem>
          <FormItem
            :label="$t('api-key')"
            prop="password"
            key="password"
            v-if="securitySettingObj[accountInfo.securityType] && securitySettingObj[accountInfo.securityType].needApiKey"
          >
            <Input
              type="password"
              :placeholder="$t('mo-ren-bu-zhan-shi-dang-qian-mi-ma-qing-shu-ru-xin-mi-ma')"
              password
              autocomplete="new-password"
              v-model="accountInfo.password"
              style="width: 280px"
            ></Input>
          </FormItem>
          <FormItem
            :label="$t('ak')"
            prop="accessKey"
            key="accessKey"
            v-if="securitySettingObj[accountInfo.securityType] && securitySettingObj[accountInfo.securityType].needAkSk"
          >
            <Input v-model="accountInfo.accessKey" style="width: 280px"></Input>
          </FormItem>
          <FormItem
            :label="$t('sk')"
            prop="secretKey"
            key="secretKey"
            v-if="securitySettingObj[accountInfo.securityType] && securitySettingObj[accountInfo.securityType].needAkSk"
          >
            <Input v-model="accountInfo.secretKey" style="width: 280px"></Input>
          </FormItem>
          <FormItem
            :label="$t('ssl-pei-zhi-wen-jian')"
            prop="securityFile"
            key="securityFile"
            v-if="securitySettingObj[accountInfo.securityType] && securitySettingObj[accountInfo.securityType].needTlsFile"
          >
            <input @change="handleFileChange" type="file" name="uploadfile" id="uploadfile1" />
            <span style="margin-left: 10px; color: rgb(128, 134, 149)"></span>
          </FormItem>
          <FormItem
            :label="$t('ke-hu-duan-truststore-mi-ma')"
            v-if="securitySettingObj[accountInfo.securityType] && securitySettingObj[accountInfo.securityType].needClientTrustStorePassword"
            prop="clientTrustStorePassword"
            key="clientTrustStorePassword"
          >
            <Input v-model="addDataSourceForm.clientTrustStorePassword" style="width: 280px" type="password" password autocomplete="new-password" />
            <Tooltip placement="right-start">
              <CustomIcon type="icon-v2-HelpCircle" hoverStyle leftMargin size="16px" />
              <template #content>
                {{
                  $t('mi-ma-jing-guo-jia-mi-cun-chu-bao-zhang-an-quan-hou-xu-chuang-jian-shu-ju-ren-wu-ke-zhi-jie-lian-jie-wu-xu-zhong-xin-tian-xie')
                }}
              </template>
            </Tooltip>
          </FormItem>
          <FormItem
            :label="$t('ca-zheng-shu')"
            prop="caFile"
            key="caFile"
            v-if="securitySettingObj[accountInfo.securityType] && securitySettingObj[accountInfo.securityType].needCaFile"
          >
            <input ref="caFileInput" @change="handleCaFileChange" type="file" name="uploadfile" id="uploadfile1" />
            <span style="margin-left: 10px; color: rgb(128, 134, 149)"></span>
          </FormItem>
          <FormItem
            :label="$t('ke-hu-duan-truststore-mi-ma')"
            v-if="securitySettingObj[accountInfo.securityType] && securitySettingObj[accountInfo.securityType].needClientTrustStorePassword"
            prop="clientTrustStorePassword"
            key="clientTrustStorePassword"
          >
            <Input v-model="addDataSourceForm.clientTrustStorePassword" style="width: 280px" type="password" password autocomplete="new-password" />
            <Tooltip placement="right-start">
              <CustomIcon type="icon-v2-HelpCircle" hoverStyle leftMargin size="16px" />
              <template #content>
                {{
                  $t('mi-ma-jing-guo-jia-mi-cun-chu-bao-zhang-an-quan-hou-xu-chuang-jian-shu-ju-ren-wu-ke-zhi-jie-lian-jie-wu-xu-zhong-xin-tian-xie')
                }}
              </template>
            </Tooltip>
          </FormItem>
          <FormItem
            :label="$t('ke-hu-duan-ca-zheng-shu')"
            prop="clientSecurityFile"
            key="clientSecurityFile"
            v-if="securitySettingObj[accountInfo.securityType] && securitySettingObj[accountInfo.securityType].needClientCaFile"
          >
            <input ref="clientSecurityFileInput" @change="handleClientCaFileChange" type="file" name="clientSecurityFile" id="clientSecurityFile" />
            <span style="margin-left: 10px; color: rgb(128, 134, 149)"></span>
          </FormItem>
          <FormItem
            :label="$t('ke-hu-duan-si-yao-wen-jian')"
            prop="clientSecretFile"
            key="clientSecretFile"
            v-if="securitySettingObj[accountInfo.securityType] && securitySettingObj[accountInfo.securityType].needClientKeyFile"
          >
            <input ref="clientSecretFileInput" @change="handleClientKeyFileChange" type="file" name="clientSecretFile" id="clientSecretFile" />
            <span style="margin-left: 10px; color: rgb(128, 134, 149)"></span>
          </FormItem>
          <FormItem
            :label="$t('ssl-si-yao-mi-ma')"
            prop="secretFilePassword"
            key="secretFilePassword"
            v-if="securitySettingObj[accountInfo.securityType] && securitySettingObj[accountInfo.securityType].needSecretFilePassword"
          >
            <Input v-model="accountInfo.secretFilePassword" style="width: 280px" type="password" password autocomplete="new-password" />
          </FormItem>
          <FormItem
            :label="$t('kerberos-pei-zhi-wen-jian')"
            prop="securityFile"
            key="securityFile2"
            v-if="securitySettingObj[accountInfo.securityType] && securitySettingObj[accountInfo.securityType].needKrb5File"
          >
            <input @change="handleFileChange" type="file" name="uploadfile" id="uploadfile" />
          </FormItem>
          <FormItem
            :label="$t('keytab-wen-jian')"
            prop="secretFile"
            key="secretFile"
            v-if="securitySettingObj[accountInfo.securityType] && securitySettingObj[accountInfo.securityType].needKeyTabFile"
          >
            <input @change="handleKeyTabFileChange" type="file" name="uploadKeytabFile" id="uploadKeytabFile" />
          </FormItem>
          <FormItem
            :label="$t('keystore-mi-ma')"
            key="keystoreFilePassword"
            v-if="securitySettingObj[accountInfo.securityType] && securitySettingObj[accountInfo.securityType].needKeystoreFilePassword"
          >
            <Input v-model="accountInfo.clientTrustStorePassword" style="width: 280px"></Input>
          </FormItem>
          <FormItem
            :label="$t('keystore-wen-jian')"
            prop="keystoreFile"
            key="keystoreFile"
            v-if="securitySettingObj[accountInfo.securityType] && securitySettingObj[accountInfo.securityType].needKeystoreFile"
          >
            <input @change="handleKeystoreFileChange" type="file" name="uploadKeytabFile" id="uploadKeytabFile" />
          </FormItem>

          <!-- MySQL ssl related start -->
          <FormItem
            :label="$t('truststore-wen-jian')"
            prop="tlsTrustStoreFile"
            key="tlsTrustStoreFile"
            v-if="securitySettingObj[accountInfo.securityType] && securitySettingObj[accountInfo.securityType].needTlsTrustStoreFile"
          >
            <input @change="handleTlsTrustStoreFileChange" type="file" name="uploadfile" id="uploadfile1" />
            <span style="margin-left: 10px; color: rgb(128, 134, 149)"></span>
          </FormItem>
          <FormItem
            :label="$t('truststore-wen-jian-mi-ma')"
            prop="tlsTrustStoreFilePassword"
            key="tlsTrustStoreFilePassword"
            v-if="securitySettingObj[accountInfo.securityType] && securitySettingObj[accountInfo.securityType].needTlsTrustStoreFilePassword"
          >
            <Input v-model="accountInfo.tlsTrustStoreFilePassword" style="width: 280px" type="password" password autocomplete="new-password" />
          </FormItem>
          <FormItem
            :label="$t('key-store-wen-jian')"
            prop="tlsKeystoreFile"
            key="tlsKeystoreFile"
            v-if="securitySettingObj[accountInfo.securityType] && securitySettingObj[accountInfo.securityType].needTlsKeyStoreFile"
          >
            <input @change="handleTlsKeystoreFileChange" type="file" name="uploadfile" id="uploadfile1" />
            <span style="margin-left: 10px; color: rgb(128, 134, 149)"></span>
          </FormItem>
          <FormItem
            :label="$t('key-store-mi-ma')"
            v-if="securitySettingObj[accountInfo.securityType] && securitySettingObj[accountInfo.securityType].needTlsKeyStoreFilePassword"
            key="tlsKeystoreFilePassword"
            prop="tlsKeystoreFilePassword"
          >
            <Input v-model="accountInfo.tlsKeystoreFilePassword" style="width: 280px" type="password" password autocomplete="new-password" />
            <Tooltip placement="right-start">
              <CustomIcon type="icon-v2-HelpCircle" hoverStyle leftMargin size="16px" />
              <template #content>
                {{
                  $t('mi-ma-jing-guo-jia-mi-cun-chu-bao-zhang-an-quan-hou-xu-chuang-jian-shu-ju-ren-wu-ke-zhi-jie-lian-jie-wu-xu-zhong-xin-tian-xie')
                }}
              </template>
            </Tooltip>
          </FormItem>
          <!-- MySQL ssl related end -->
        </Form>
      </div>
      <template #footer>
        <Button @click="handleCancelEdit">{{ $t('guan-bi') }}</Button>
        <Button @click="confirmEditAccount" type="primary">{{ $t('que-ding') }}</Button>
      </template>
    </CCModal>
    <CCModal v-model="showDeleteAccountModal" :title="$t('shan-chu-shu-ju-yuan-zhang-hao-que-ren')" :mask-closable="false">
      <div>
        {{ $t('que-ren-yao-shan-chu-rowinstanceid-de-zhang-hao-ma', [sourceDetail.instanceId]) }}
      </div>
      <template #footer>
        <Button @click="handleCloseDeleteAccountModal">{{ $t('guan-bi') }}</Button>
        <Button type="primary" @click="handleDeleteAccount">{{ $t('que-ding') }}</Button>
      </template>
    </CCModal>
  </div>
</template>
<script>
import { mapGetters, mapState } from 'vuex';
import _ from '@/utils/lodash';
import DataSourceHeader from '@/components/function/addDataSource/DataSourceHeader';
import { isOracle } from '@/utils';
import { SECOND_CONFIRM_EVENT_LIST } from '@/const';
import SecondConfirmModal from '@/components/modal/SecondConfirmModal';
import DataSourceIcon from '@/components/function/DataSourceIcon';

export default {
  name: 'DataSource',
  components: {
    SecondConfirmModal,
    DataSourceHeader,
    DataSourceIcon
  },
  data() {
    return {
      showDeleteAccountModal: false,
      dsKvConfigs: [],
      testingDataSourceId: null,
      securitySetting: [],
      securitySettingObj: {},
      sid: '',
      publicSid: '',
      showEditAccount: false,
      accountInfo: {
        account: '',
        password: '',
        securityType: '',
        securityFile: '',
        caFile: '',
        jsonFile: '',
        secretFile: '',
        clientTrustStorePassword: '',
        keystoreFile: '',
        tlsTrustStoreFile: '',
        tlsTrustStoreFilePassword: ''
      },
      accountInfoValidate: {
        account: [
          {
            required: true,
            message: this.$t('zhang-hao-bu-neng-wei-kong')
          }
        ],
        password: [
          {
            required: true,
            message: this.$t('mi-ma-bu-neng-wei-kong')
          }
        ],
        securityFile: [
          {
            required: true,
            message: this.$t('ssl-pei-zhi-wen-jian-bu-neng-wei-kong')
          }
        ],
        // caFile: [
        //   {
        //     required: true,
        //     message: this.$t('ca-zheng-shu-bu-neng-wei-kong')
        //   }
        // ],
        keystoreFile: [
          {
            required: true,
            message: this.$t('keystore-wen-jian-bu-neng-wei-kong')
          }
        ],
        secretFile: [
          {
            required: true,
            message: this.$t('keytab-wen-jian-bu-neng-wei-kong')
          }
        ],
        clientTrustStorePassword: [
          {
            required: true,
            message: this.$t('ke-hu-duan-truststore-mi-ma-bu-neng-wei-kong'),
            trigger: 'change'
          }
        ],
        tlsTrustStoreFile: [
          {
            required: true,
            message: this.$t('truststore-wen-jian-bu-neng-wei-kong')
          }
        ],
        tlsTrustStoreFilePassword: [
          {
            required: true,
            message: this.$t('keystore-wen-jian-mi-ma-bu-neng-wei-kong')
          }
        ]
      },
      sourceDetail: {},
      showEditDesc: false,
      editDescLoading: false,
      instanceDesc: '',
      originalInstanceDesc: '',
      selectedRow: {},
      refreshLoading: false,
      showAddDataSource: false,
      showDeleteDataSourceConfirm: false,
      dataSourceTypes: [],
      workerClusterList: [],
      page: 1,
      size: 20,
      total: 0,
      addDataSourceForm: {
        host: '',
        type: 'MySQL',
        role: 'MASTER',
        instanceType: 'SELF_MAINTENANCE',
        sid: ''
      },
      searchKey: {
        host: '',
        region: '',
        dbType: 'all',
        deployType: 'all'
      },
      dataSourceColumn: [
        {
          title: this.$t('shu-ju-yuan'),
          key: 'instanceId',
          slot: 'instanceId',
          minWidth: 420
        },
        {
          title: this.$t('wang-luo-di-zhi'),
          key: 'host',
          minWidth: 320,
          slot: 'host'
        },
        {
          title: this.$t('cao-zuo'),
          key: '',
          slot: 'action',
          width: 120,
          fixed: 'right'
        }
      ],
      dataSourceData: [],
      showData: [],
      pagingData: [],
      addDataSourceRule: {
        host: [
          {
            required: true,
            message: 'The host cannot be empty',
            trigger: 'blur'
          }
        ],
        type: [
          {
            required: true,
            message: 'The type cannot be empty',
            trigger: 'change'
          }
        ],
        role: [
          {
            required: true,
            type: 'string',
            message: 'The role cannot be empty',
            trigger: 'change'
          }
        ],
        region: [
          {
            required: true,
            type: 'string',
            message: 'The region cannot be empty',
            trigger: 'change'
          }
        ],
        instanceType: [
          {
            required: true,
            message: 'Please select type',
            trigger: 'change'
          }
        ]
      }
    };
  },
  computed: {
    ...mapState(['productClusterList', 'myAuth']),
    ...mapState(['globalDsSetting', 'dmGlobalSetting']),
    ...mapGetters(['isDesktop']),
    SECOND_CONFIRM_EVENT_LIST() {
      return SECOND_CONFIRM_EVENT_LIST;
    },
    canManageDataSource() {
      return this.myAuth.includes('RDP_DS_MANAGE') || this.myAuth.includes('RDP_ALL_DATASOURCE_MANAGE');
    },
    canSubmitDesc() {
      return this.instanceDesc.trim().length > 0 && this.instanceDesc.trim() !== this.originalInstanceDesc.trim();
    }
  },
  methods: {
    handleUpdateSearchKey(params) {
      // Update properties of the searchkey object
      Object.assign(this.searchKey, params);
    },
    isOracle,
    async handleKvConfigs(row) {
      this.selectedRow = row;
      this.$router.push({
        path: '/datasource/add',
        query: {
          mode: 'edit',
          dsId: row.id,
          dsType: row.dataSourceType,
          instanceId: row.instanceId
        }
      });
    },
    handleKeyTabFileChange(e) {
      const files = e.target.files;

      if (files && files[0]) {
        const file = files[0];

        if (file.size > 1024 * 1024) {
          return false;
        }
        this.accountInfo.secretFile = file;
        this.$refs['account-info-form'].validateField('secretFile');
      }
    },
    handleFileChange(e) {
      const files = e.target.files;

      if (files && files[0]) {
        const file = files[0];

        if (file.size > 1024 * 1024) {
          return false;
        }
        this.accountInfo.securityFile = file;
        this.$refs['account-info-form'].validateField('securityFile');
      }
    },
    handleKeystoreFileChange(e) {
      const files = e.target.files;

      if (files && files[0]) {
        const file = files[0];

        if (file.size > 1024 * 1024) {
          return false;
        }
        this.accountInfo.securityFile = file;
        this.accountInfo.keystoreFile = file;
        this.$refs['account-info-form'].validateField('keystoreFile');
      }
    },
    handleTlsKeystoreFileChange(e) {
      const files = e.target.files;

      if (files && files[0]) {
        const file = files[0];

        if (file.size > 1024 * 1024) {
          return false;
        }
        this.accountInfo.tlsKeystoreFile = file;
        setTimeout(() => {
          this.$refs['account-info-form'].validateField('tlsKeystoreFile');
        }, 0);
      }
    },
    handleTlsTrustStoreFileChange(e) {
      const files = e.target.files;

      if (files && files[0]) {
        const file = files[0];

        if (file.size > 1024 * 1024) {
          return false;
        }
        this.accountInfo.tlsTrustStoreFile = file;
        setTimeout(() => {
          this.$refs['account-info-form'].validateField('tlsTrustStoreFile');
        }, 0);
      }
    },
    handleCaFileChange(e) {
      const files = e.target.files;

      if (!files.length) {
        this.accountInfo.caFile = '';
        this.$refs['account-info-form'].validateField('caFile');
      }

      if (files && files[0]) {
        const file = files[0];

        if (file.size > 1024 * 1024) {
          return false;
        }
        this.accountInfo.securityFile = file;
        this.accountInfo.caFile = file;
        setTimeout(() => {
          this.$refs['account-info-form'].validateField('caFile');
        }, 0);
      }
    },
    handleClientCaFileChange(e) {
      const files = e.target.files;

      if (files && files[0]) {
        const file = files[0];

        if (file.size > 1024 * 1024) {
          return false;
        }
        this.accountInfo.clientSecurityFile = file;
      }
    },
    handleClientKeyFileChange(e) {
      const files = e.target.files;

      if (files && files[0]) {
        const file = files[0];

        if (file.size > 1024 * 1024) {
          return false;
        }
        this.accountInfo.clientSecretFile = file;
        // this.$refs['account-info-form'].validateField('keyFile');
      }
    },
    handleRefresh() {
      this.getDataSourceList();
    },
    getDataSourceList(searchKey, searchType) {
      searchKey = this.searchKey;
      this.refreshLoading = true;
      let type = null;
      let deployType = null;

      if (searchKey && searchKey.dbType !== 'all') {
        type = searchKey.dbType;
      }
      if (searchKey && searchKey.deployType !== 'all') {
        deployType = searchKey.deployType;
      }
      this.$services
        .dmDataSourceListByCondition({
          data: {
            useVisibility: true,
            type,
            deployType,
            dataSourceDescLike: searchKey ? searchKey.dataSourceDescLike : null,
            dsHostLike: searchKey ? searchKey.dsHostLike : null,
            dataSourceId: searchKey ? searchKey.dataSourceId : null
          }
        })
        .then((res) => {
          if (res.success) {
            this.dataSourceData = res.data;
            this.pagingData = _.cloneDeep(this.dataSourceData);
            this.total = this.dataSourceData.length;
            if (searchType === 'init') {
              this.page = 1;
            }
            this.showData = this.dataSourceData.slice((this.page - 1) * this.size, this.page * this.size);
            this.showData.map((item) => {
              item.showEditDesc = false;
              return null;
            });
          }
          this.refreshLoading = false;
        })
        .catch(() => {
          this.refreshLoading = false;
        });
    },
    handleShowAddDataSource() {
      this.$router.push({ path: '/datasource/add' });
    },
    deleteDataSource() {
      this.$services.rdpDataSourceDelete({ data: { dataSourceId: this.selectedRow.id } }).then((res) => {
        if (res.success) {
          this.getDataSourceList();
          this.$Message.success(this.$t('shan-chu-cheng-gong'));
          this.handleCancelEdit();
        }
      });
    },
    handleDeleteConfirm(row) {
      this.selectedRow = row;
      this.showDeleteDataSourceConfirm = true;
    },
    selectConfirmText(event) {
      const range = document.createRange();
      const selection = window.getSelection();
      range.selectNodeContents(event.currentTarget);
      selection.removeAllRanges();
      selection.addRange(range);
    },
    handlePageChange(page) {
      this.page = page;
      this.showData = this.pagingData.slice((this.page - 1) * this.size, this.page * this.size);
      this.showData.map((item) => {
        item.showEditDesc = false;
        return null;
      });
    },
    async handleConfirmEditDesc() {
      if (!this.canSubmitDesc) {
        return;
      }
      this.editDescLoading = true;
      try {
        const res = await this.$services.rdpDataSourceUpdateDsDesc({
          data: {
            dataSourceId: this.selectedRow.id,
            instanceDesc: this.instanceDesc.trim()
          }
        });
        if (res.success) {
          this.showEditDesc = false;
          this.getDataSourceList();
          this.$Message.success(this.$t('xiu-gai-cheng-gong'));
        }
      } finally {
        this.editDescLoading = false;
      }
    },
    handleCancelEdit() {
      this.showEditDesc = false;
      this.showEditAccount = false;
      this.showDeleteDataSourceConfirm = false;
      this.editDescLoading = false;
      if (this.$refs.caFileInput) {
        this.$refs.caFileInput.value = '';
      }
      if (this.$refs.clientSecurityFileInput) {
        this.$refs.clientSecurityFileInput.value = '';
      }
      if (this.$refs.clientSecretFileInput) {
        this.$refs.clientSecretFileInput.value = '';
      }
    },
    handleEditDataSourceDesc(row) {
      this.instanceDesc = row.instanceDesc || '';
      this.originalInstanceDesc = row.instanceDesc || '';
      this.selectedRow = row;
      this.showEditDesc = true;
      this.$nextTick(() => {
        this.$refs.instanceDescInput?.focus?.();
      });
    },
    handlePageSizeChange(size) {
      this.size = size;
      this.handlePageChange(1);
    },
    getDataSourceDetail(row, security = false) {
      this.$services.rdpDataSourceQueryDs({ data: { dataSourceId: row.id } }).then((res) => {
        if (res.success) {
          this.sourceDetail = res.data;
          this.accountInfo.account = this.sourceDetail.accountName;
          this.accountInfo.securityType = this.sourceDetail.securityType;

          if (security) {
            const securityOptions = this.dmGlobalSetting?.dsSettingDef?.[res.data.dataSourceType]?.securityOptions || [];
            this.securitySetting = securityOptions;
            const obj = {};
            securityOptions.forEach((s) => {
              obj[s.securityType] = s;
            });
            this.securitySettingObj = obj;
          }
        }
      });
    },
    confirmEditAccount() {
      this.$refs['account-info-form'].validate((valid) => {
        if (valid) {
          const formData = new FormData();
          const datasourceUpdateData = {
            dataSourceId: this.sourceDetail.id,
            userName: this.accountInfo.account,
            password: this.accountInfo.password,
            securityType: this.accountInfo.securityType,
            accessKey: this.accountInfo.accessKey,
            secretKey: this.accountInfo.secretKey,
            secretFilePassword: this.accountInfo.secretFilePassword
          };

          // Process different types of source field map
          switch (this.sourceDetail?.dataSourceType) {
            case 'MySQL':
            case 'Kafka':
            case 'Tunnel':
            case 'AutoMQ':
              datasourceUpdateData.securityFilePassword = this.accountInfo?.tlsTrustStoreFilePassword || '';
              datasourceUpdateData.clientSecurityFilePassword = this.accountInfo?.tlsKeystoreFilePassword || '';
              this.accountInfo.securityFile = this.accountInfo.tlsTrustStoreFile;
              this.accountInfo.clientSecurityFile = this.accountInfo.tlsKeystoreFile;
              break;
            case 'PostgreSQL':
              datasourceUpdateData.secretFilePassword = this.accountInfo?.secretFilePassword || '';
              this.accountInfo.secretFile = this.accountInfo.clientSecretFile;
              break;
            default:
              break;
          }

          formData.append('DataSourceUpdateData', JSON.stringify(datasourceUpdateData));
          formData.append('securityFile', this.accountInfo?.securityFile || '');
          formData.append('clientSecurityFile', this.accountInfo?.clientSecurityFile || '');
          formData.append('secretFile', this.accountInfo?.secretFile || '');
          this.$services.rdpDataSourceUpdateAccountAndPassword({ data: formData }).then((res) => {
            if (res.success) {
              this.showEditAccount = false;
              this.getDataSourceList();
              this.resetAccountInfo();
              this.$Message.success(this.$t('xiu-gai-cheng-gong'));
            }
          });
        }
      });
    },
    handleEditAccount(row) {
      this.resetAccountInfo();
      this.showEditAccount = true;
      this.sourceDetail = row;
      this.getDataSourceDetail(row, true);
    },
    handleCloseDeleteAccountModal() {
      this.showDeleteAccountModal = false;
    },
    handleShowDeleteAccountModal(row) {
      this.showDeleteAccountModal = true;
      this.accountInfo.account = '';
      this.accountInfo.password = '';
      this.accountInfo.securityType = '';
      this.sourceDetail = row;
    },
    handleDeleteAccount() {
      this.$services.rdpDataSourceDeleteAccount({ data: { dataSourceId: this.sourceDetail.id } }).then((res) => {
        if (res.success) {
          this.$Message.success(this.$t('shan-chu-zhang-hao-cheng-gong'));
          this.getDataSourceList();
          this.handleCloseDeleteAccountModal();
        }
      });
    },
    async handleTestConnection(row) {
      this.testingDataSourceId = row.id;
      try {
        const res = await this.$services.dmDataSourceTestConnect({
          data: {
            dataSourceId: row.id
          }
        });
        if (res.success) {
          this.$Message.success(this.$t('ce-shi-lian-jie-cheng-gong'));
        } else {
          this.$Message.error(res.msg || this.$t('ce-shi-lian-jie-shi-bai'));
        }
      } catch (e) {
        this.$Message.error(e?.message || this.$t('ce-shi-lian-jie-shi-bai'));
      } finally {
        this.testingDataSourceId = null;
      }
    },
    resetAccountInfo() {
      this.accountInfo = {
        account: '',
        password: '',
        securityType: '',
        securityFile: '',
        caFile: '',
        jsonFile: '',
        secretFile: '',
        clientTrustStorePassword: '',
        keystoreFile: '',
        clientSecurityFile: '',
        clientSecretFile: '',
        secretFilePassword: '',
        tlsTrustStoreFile: '',
        tlsTrustStoreFilePassword: '',
        tlsKeystoreFile: '',
        tlsKeystoreFilePassword: '',
        accessKey: '',
        secretKey: ''
      };
    },
    handleGoConsoleJob(row) {
      this.$router.push({ path: `/ccsystem/state/task/${row.consoleJobId}` });
    },
    handleChangeSearchType() {
      // Reset all search values when switching query type
      this.searchKey = {
        host: '',
        region: '',
        dbType: 'all',
        deployType: 'all'
      };
    }
  }
};
</script>
<style lang="less" scoped>
.data-source-container {
  position: relative;
  margin-top: 0;
  margin-bottom: 0;

  .iconfont {
    color: #8d95a6;
    cursor: pointer;
    font-size: 12px;
  }

  .show-datasource-info-icon {
    color: #0bb9f8;
    font-size: 20px;
    position: absolute;
    right: 0;
    top: -10px;
    cursor: pointer;
  }

  .datasource-desc-content {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    display: inline-block;
    vertical-align: middle;
    max-width: 200px;
    font-size: 13px;
    opacity: 0.5;
    //margin-left: 24px;
  }

  .datasource-main-info {
    display: flex;
    align-items: center;
    min-width: 0;
    height: 22px;
  }

  .datasource-primary-content {
    color: var(--text-primary);
    font-size: 13px;
    line-height: 20px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    vertical-align: middle;
  }

  .datasource-secondary-content {
    color: var(--text-secondary);
    font-size: 12px;
    line-height: 16px;
    margin-top: 2px;
    opacity: 0.6;
  }

  .mid-icon {
    display: flex;
  }
}

.datasource-list-panel {
  display: flex;
  flex-direction: column;
  background: var(--bg-card);
  border: 1px solid var(--border-light);
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(15, 23, 42, 0.04);
  min-height: 0;
  overflow: hidden;
}

.datasource-table-wrap {
  flex: 1;
  min-height: 0;
  overflow: auto;

  :deep(.ivu-table-wrapper) {
    border: 0;
    border-radius: 0;
  }
}

.datasource-table {
  :deep(.ivu-table) {
    color: var(--text-primary);

    &::before,
    &::after {
      display: none;
    }
  }

  :deep(.ivu-table-header th) {
    height: 44px;
    background: var(--bg-secondary) !important;
    border-right: 0;
    border-bottom: 1px solid var(--border-light);
    color: var(--text-secondary) !important;
    font-size: 12px;
    font-weight: 600;
  }

  :deep(.ivu-table-cell) {
    padding-left: 16px;
    padding-right: 16px;
  }

  :deep(.ivu-table td) {
    height: 54px;
    border-right: 0;
    border-bottom: 1px solid var(--border-light);
  }

  :deep(.ivu-table-row-hover td),
  :deep(.ivu-table-row:hover td) {
    background: rgba(62, 207, 142, 0.04) !important;
  }
}

.datasource-identity {
  display: flex;
  min-width: 0;
  height: 40px;
  align-items: center;
  gap: 12px;
}

.datasource-type-icon {
  display: inline-flex;
  width: 36px;
  flex: 0 0 36px;
  align-items: center;
  justify-content: center;
}

.datasource-info-text {
  min-width: 0;
}

.datasource-name {
  font-size: 14px;
  font-weight: 500;
}

.datasource-id-text {
  max-width: 320px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.datasource-edit-icon {
  flex: 0 0 auto;
  margin-left: 6px;
  cursor: pointer;
}

.datasource-list-footer {
  border-top: 1px solid var(--border-light);
  flex-shrink: 0;

  .page-footer-paging {
    height: 56px;
    justify-content: flex-end;
    padding: 0 20px;
  }
}

.add-white-list-container {
  width: 280px;
  border: 1px solid #dadada;
  padding: 0 12px;
  border-radius: 4px;
}

.host-type {
  padding: 0;
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

.alarm-icon {
  width: 20px;
  height: 20px;
  display: inline-block;
  /*border-radius: 50%;*/
  /*background-color: #FF6E0D;*/
  color: #ff6e0d;
  text-align: center;
  line-height: 20px;
  cursor: pointer;
  margin-left: 4px;

  .iconyibuforce {
    font-size: 14px;
    color: #ff6e0d;
  }
}

.datasource-creating-indicator {
  display: inline-block;
  width: 6px;
  height: 6px;
  background-color: #ffd700;
  border-radius: 50%;
  margin-left: 6px;
  margin-top: 2px;
  cursor: pointer;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
  100% {
    opacity: 1;
  }
}

.job-list-datasource {
  position: absolute;
  width: 20px;
  height: 20px;
  line-height: 20px;
  border: 1px solid #c9c9c9;
  font-size: 14px;
  border-radius: 50%;
  top: -12px;
  background: #ffffff;
  text-align: center;
}

.job-list-source {
  left: -10px;
}

.data-job-desc {
  .iconfont {
    visibility: hidden;
  }

  &:hover .iconfont {
    visibility: visible;
  }
}

.datasource-action-group {
  display: flex;
  align-items: center;
  gap: 8px;
  justify-content: center;
  white-space: nowrap;

  :deep(.ivu-btn-text) {
    height: 28px;
    padding: 0;
    font-weight: 500;
  }
}

.datasource-action-danger {
  color: #ed4014;
}

.delete-datasource-confirm-tip {
  color: var(--text-secondary);
  font-size: 14px;
  line-height: 22px;
}

.delete-datasource-confirm-id {
  color: var(--text-primary);
  cursor: text;
  font-weight: 600;
  text-decoration: underline;
}

.edit-desc-modal {
  padding-top: 4px;
}

.edit-desc-footer {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

[data-theme='dark'] {
  .host-type-label {
    color: var(--text-primary);
    background-color: var(--bg-select);
  }
}
</style>
