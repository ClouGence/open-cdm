<template>
  <div class="content-wrapper">
    <second-confirm-modal
      :title="$t('shan-chu-shu-ju-yuan')"
      :event="SECOND_CONFIRM_EVENT_LIST.DELETE_DATASOURCE"
      :text="$t('yi-wan-cheng-shan-chu-selectedrowid-shu-ju-yuan-de-cao-zuo', [selectedRow.instanceId])"
      :visible="showDeleteDataSourceConfirm"
      ref="second-confirm-modal"
      :handle-confirm="deleteDataSource"
      :handle-close="handleCancelEdit"
    />
    <DataSourceHeader
      :handleSearch="getDataSourceList"
      :searchKey="searchKey"
      :supportAdd="canManageDataSource"
      :handleShowAddDataSource="handleShowAddDataSource"
      :handleChangeSearchType="handleChangeSearchType"
      :refreshLoading="refreshLoading"
      @update-search-key="handleUpdateSearchKey"
    ></DataSourceHeader>
    <div class="data-source-container">
      <div style="margin-top: 16px">
        <Table border :columns="dataSourceColumn" :data="showData" :loading="refreshLoading">
          <template #instanceId="{ row }">
            <div style="padding: 14px 10px">
              <div style="display: flex; align-items: center">
                <span>{{ row.instanceId }}</span>
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
              <div class="data-job-desc" style="position: relative; padding-right: 20px; margin-top: 4px">
                <Tooltip :content="row.instanceDesc || $t('zan-wu-miao-shu')" placement="bottom" transfer>
                  <span class="datasource-desc-content">
                    {{ row.instanceDesc || $t('zan-wu-miao-shu') }}
                  </span>
                  <CustomIcon
                    class="iconfont icon"
                    v-if="myAuth.includes('RDP_DS_MANAGE') || myAuth.includes('RDP_ALL_DATASOURCE_MANAGE')"
                    @click="handleEditDataSourceDesc(row)"
                    style="margin-left: 5px; cursor: pointer"
                    type="icon-v2-EditingPen"
                    size="16px"
                  />
                </Tooltip>
              </div>
              <Poptip placement="right" width="680" class="show-job-info-icon" transfer>
                <CustomIcon type="icon-v2-Info" @click="getDataSourceDetail(row)" size="14px" />
                <template #content>
                  <DataSourceInDetail :dataSourceDetail="sourceDetail"></DataSourceInDetail>
                </template>
              </Poptip>
            </div>
          </template>
          <template #action="{ row }">
            {{ '' }}
            <div v-if="canManageDataSource">
              <Dropdown transfer trigger="click">
                <a>
                  {{ $t('geng-duo') }}
                  <CustomIcon type="icon-v2-ArrowDown" size="12x" />
                </a>
                <template #list>
                  <DropdownMenu>
                    <DropdownItem class="datasource-operation-dropdown" :disabled="row.lifeCycleState !== 'CREATED'">
                      <a
                        class="dropdown-content"
                        v-if="row.lifeCycleState === 'CREATED'"
                        style="width: 100%; display: inline-block"
                        @click="handleShowTestConnectionModal(row)"
                      >
                        {{ $t('ce-shi-lian-jie') }}
                      </a>
                      <span class="dropdown-content" v-if="row.lifeCycleState !== 'CREATED'" style="cursor: not-allowed; color: #babdc5">
                        {{ $t('ce-shi-lian-jie') }}
                      </span>
                    </DropdownItem>
                    <DropdownItem class="datasource-operation-dropdown" :disabled="row.lifeCycleState !== 'CREATED'">
                      <span class="dropdown-content" v-if="row.lifeCycleState !== 'CREATED'" style="cursor: not-allowed; color: #babdc5">
                        {{ $t('xiu-gai-shu-ju-yuan-di-zhi') }}
                      </span>
                      <a
                        class="dropdown-content"
                        v-if="row.lifeCycleState === 'CREATED'"
                        style="width: 100%; display: inline-block"
                        @click="handleUpdatePublicHost(row)"
                      >
                        {{ $t('xiu-gai-shu-ju-yuan-di-zhi') }}
                      </a>
                    </DropdownItem>
                    <DropdownItem class="datasource-operation-dropdown" :disabled="row.lifeCycleState !== 'CREATED'">
                      <a
                        class="dropdown-content"
                        v-if="row.lifeCycleState === 'CREATED'"
                        style="width: 100%; display: inline-block"
                        @click="handleEditAccount(row)"
                      >
                        {{ $t('xiu-gai-zhang-hao') }}
                      </a>
                      <span class="dropdown-content" v-if="row.lifeCycleState !== 'CREATED'" style="cursor: not-allowed; color: #babdc5">
                        {{ $t('xiu-gai-zhang-hao') }}
                      </span>
                    </DropdownItem>
                    <DropdownItem class="datasource-operation-dropdown">
                      <a class="dropdown-content" @click="handleKvConfigs(row)" style="width: 100%; display: inline-block">
                        {{ $t('xiu-gai-shu-ju-yuan-can-shu') }}
                      </a>
                    </DropdownItem>
                    <DropdownItem class="datasource-operation-dropdown" :disabled="row.lifeCycleState !== 'CREATED'">
                      <a
                        class="dropdown-content"
                        v-if="row.lifeCycleState === 'CREATED'"
                        style="width: 100%; display: inline-block"
                        @click="handleShowDeleteAccountModal(row)"
                      >
                        {{ $t('shan-chu-zhang-hao') }}
                      </a>
                      <span class="dropdown-content" v-if="row.lifeCycleState !== 'CREATED'" style="cursor: not-allowed; color: #babdc5">
                        {{ $t('shan-chu-zhang-hao') }}
                      </span>
                    </DropdownItem>
                    <DropdownItem class="datasource-operation-dropdown">
                      <a class="dropdown-content" @click="handleDeleteConfirm(row)" style="width: 100%; display: inline-block">
                        {{ $t('shan-chu') }}
                      </a>
                    </DropdownItem>
                  </DropdownMenu>
                </template>
              </Dropdown>
            </div>
          </template>
          <template #host="{ row }">
            <div class="host-type">
              <p>{{ row.publicHost || row.privateHost || row.host || '-' }}</p>
            </div>
          </template>
          <template #dataSourceType="{ row }">
            <div>
              <Tooltip
                style="margin-left: 10px; font-size: 24px; cursor: pointer"
                placement="right"
                class="dataSource-icon mid-icon"
                transfer
                :content="`${
                  row.deployType === 'ALIBABA_CLOUD_HOSTED' ? Mapping.aliyunType[row.dataSourceType] || row.dataSourceType : row.dataSourceType
                }`"
              >
                <DataSourceIcon size="20px" :type="row.dataSourceType" :instanceType="row.deployType"></DataSourceIcon>
              </Tooltip>
            </div>
          </template>
          <template #deployType="{ row }">
            <div>
              <span>{{ DATASOURCE_DEPLOY_TYPE_I18N[row.deployType] }}</span>
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
    </div>
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
    <!--    <Page class="page-container" :total="total" show-total show-elevator @on-change="handlePageChange" show-sizer-->
    <!--          :page-size="size"-->
    <!--          @on-page-size-change="handlePageSizeChange"/>-->
    <CCModal v-model="showEditDesc" :title="$t('xiu-gai-shu-ju-yuan-miao-shu')" width="400px">
      <div>
        <p>
          {{ $t('xiu-gai-id-wei-selectedrowinstanceid-de-shu-ju-yuan-de-miao-shu-wei', [selectedRow.instanceId]) }}
        </p>
        <Input v-model="instanceDesc" style="width: 280px; margin-top: 20px" />
      </div>
      <template #footer>
        <div>
          <Button @click="handleCancelEdit">{{ $t('guan-bi') }}</Button>
          <Button @click="handleConfirmEditDesc" type="primary">{{ $t('que-ding') }}</Button>
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

          <!-- mysql ssl相关 start -->
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
          <!-- mysql ssl相关 end -->
        </Form>
      </div>
      <template #footer>
        <Button @click="handleCancelEdit">{{ $t('guan-bi') }}</Button>
        <Button @click="confirmEditAccount" type="primary">{{ $t('que-ding') }}</Button>
      </template>
    </CCModal>
    <CCModal v-model="showEditPublicHost" :title="$t('xiu-gai-shu-ju-yuan-di-zhi')" width="620px">
      <div>
        <p>
          {{
            showUpdateHttpHost
              ? $t('xiu-gai-id-wei-selectedrowinstanceid-de-shu-ju-yuan-de-client-di-zhi-wei', [selectedRow.instanceId])
              : $t('xiu-gai-id-wei-selectedrowinstanceid-de-shu-ju-yuan-de-di-zhi-wei', [selectedRow.instanceId])
          }}
        </p>
        <div style="margin-top: 20px">
          <Input v-model="publicHost" :placeholder="$t('ip-huo-yu-ming-duan-kou')" style="width: 460px" />
        </div>
      </div>
      <template #footer>
        <Button @click="handleCancelEdit">{{ $t('guan-bi') }}</Button>
        <Button @click="handleConfirmEditPublicHost" type="primary">{{ $t('que-ding') }}</Button>
      </template>
    </CCModal>
    <test-connection-modal
      ref="testConnectionModal"
      v-model:visible="showTestConnectionModal"
      :datasource="selectedRow"
      :handle-close-modal="handleCloseTestConnectionModal"
      type="dataSourceList"
    />
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
import _ from 'lodash';
import DataSourceHeader from '@/components/function/addDataSource/DataSourceHeader';
import DataSourceInDetail from '@/components/function/addDataSource/DataSourceInDetail';
import { isOracle } from '@/utils';
import fecha from 'fecha';
import { DATASOURCE_DEPLOY_TYPE_I18N, SECOND_CONFIRM_EVENT_LIST } from '@/const';
import SecondConfirmModal from '@/components/modal/SecondConfirmModal';
import DataSourceIcon from '@/components/function/DataSourceIcon';
import Mapping from '../util';
import TestConnectionModal from '../../components/function/addDataSource/TestConnectionModal';
import { resolveComponent } from 'vue';

export default {
  name: 'DataSource',
  components: {
    SecondConfirmModal,
    TestConnectionModal,
    DataSourceHeader,
    DataSourceInDetail,
    DataSourceIcon
  },
  mounted() {
    // this.getDataSourceList();
    this.listEnv();
  },
  data() {
    return {
      showDeleteAccountModal: false,
      dsKvConfigs: [],
      showTestConnectionModal: false,
      securitySetting: [],
      securitySettingObj: {},
      publicHost: '',
      publicHttpHost: '',
      sid: '',
      publicSid: '',
      showEditPublicHost: false,
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
      Mapping,
      envData: [],
      showEditDesc: false,
      instanceDesc: '',
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
          title: this.$t('shu-ju-yuan-id'),
          key: 'instanceId',
          slot: 'instanceId',
          width: 280
        },
        {
          title: this.$t('lei-xing'),
          key: 'dataSourceType',
          width: 80,
          slot: 'dataSourceType'
        },
        {
          title: this.$t('host'),
          key: 'host',
          minWidth: 500,
          slot: 'host'
        },
        {
          title: this.$t('huan-jing'),
          key: 'dsEnvName',
          width: 500
        },
        {
          title: this.$t('cao-zuo'),
          key: '',
          slot: 'action',
          width: 160,
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
    DATASOURCE_DEPLOY_TYPE_I18N() {
      return DATASOURCE_DEPLOY_TYPE_I18N;
    },
    showUpdateHttpHost() {
      return ['StarRocks', 'Doris', 'SelectDB'].includes(this.selectedRow.dataSourceType);
    },
    getEnvById() {
      return (id) => {
        let envName = '';
        this.envData.forEach((env) => {
          if (env.id === id) {
            envName = env.envName;
          }
        });
        return envName;
      };
    }
  },
  methods: {
    handleUpdateSearchKey(params) {
      // 更新searchKey对象的属性
      Object.assign(this.searchKey, params);
    },
    isOracle,
    async listEnv() {
      const data = {
        envName: ''
      };
      const res = await this.$services.rdpDsEnvList({ data });
      if (res.success) {
        this.envData = res.data;
      }
    },
    async handleKvConfigs(row) {
      this.selectedRow = row;
      // const res = await queryDsConfig({
      //   dataSourceId: row.id
      // });
      //
      // if (res.data.success) {
      //   this.dsKvConfigs = res.data.data;
      //   this.showKvConfigsModal = true;
      // }
      this.$router.push({ path: `/system/ccdatasource/params/${row.id}/${row.instanceId}` });
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
      this.$router.push({ path: '/system/ccdatasource/add' });
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
    handlePageChange(page) {
      this.page = page;
      this.showData = this.pagingData.slice((this.page - 1) * this.size, this.page * this.size);
      this.showData.map((item) => {
        item.showEditDesc = false;
        return null;
      });
    },
    handleConfirmEditDesc() {
      this.showEditDesc = false;
      this.$services
        .rdpDataSourceUpdateDataSourceDesc({
          data: {
            dataSourceId: this.selectedRow.id,
            instanceDesc: this.instanceDesc
          }
        })
        .then((res) => {
          if (res.success) {
            this.getDataSourceList();
            this.$Message.success(this.$t('xiu-gai-cheng-gong'));
          }
        });
    },
    handleCancelEdit() {
      this.publicHost = '';
      this.publicHttpHost = '';
      this.showEditDesc = false;
      this.showEditAccount = false;
      this.showEditPublicHost = false;
      this.showDeleteDataSourceConfirm = false;
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
      this.instanceDesc = row.instanceDesc;
      this.selectedRow = row;
      this.showEditDesc = true;
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
            const { deployType, dataSourceType } = res.data;
            this.$services
              .rdpConstantDsSecurityOption({
                data: {
                  deployEnvType: deployType,
                  dataSourceType,
                  deployFetchType: 'MANUALLY_FILL'
                }
              })
              .then((res2) => {
                if (res2.success) {
                  this.securitySetting = res2.data.securityOptions;
                  const obj = {};
                  res2.data.securityOptions.forEach((s) => {
                    obj[s.securityType] = s;
                  });
                  this.securitySettingObj = obj;
                }
              });
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

          // 处理不同类型源端字段映射
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
    handleCloseTestConnectionModal() {
      this.showTestConnectionModal = false;
    },
    handleShowTestConnectionModal(row) {
      this.showTestConnectionModal = true;
      this.selectedRow = row;
      // 清除上一次测试连接的状态
      this.$nextTick(() => {
        if (this.$refs.testConnectionModal) {
          this.$refs.testConnectionModal.hasTest = false;
          this.$refs.testConnectionModal.testSuccess = false;
          this.$refs.testConnectionModal.loading = false;
        }
      });
    },
    handleUpdatePublicHost(row) {
      this.showEditPublicHost = true;
      this.selectedRow = row;
      this.publicHost = row.publicHost || row.privateHost || row.host;
      if (row.extraVO) {
        this.publicHttpHost = row.extraVO.publicStarRocksHttpHost;
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
    handleConfirmEditPublicHost() {
      this.$services
        .rdpDataSourceUpdatePublicHost({
          data: {
            dataSourceId: this.selectedRow.id,
            publicHost: this.publicHost,
            publicHttpHost: this.publicHttpHost
          }
        })
        .then((res) => {
          if (res.success) {
            this.publicHost = '';
            this.publicHttpHost = '';
            this.showEditPublicHost = false;
            this.getDataSourceList();
            this.$Message.success(this.$t('xiu-gai-cheng-gong'));
          }
        });
    },
    handleGoConsoleJob(row) {
      this.$router.push({ path: `/ccsystem/state/task/${row.consoleJobId}` });
    },
    handleChangeSearchType() {
      // 切换查询类型的时候，重置所有搜索的值
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
  margin-top: 16px;
  margin-bottom: 60px;

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

  .mid-icon {
    display: flex;
  }
}

.add-white-list-container {
  width: 280px;
  border: 1px solid #dadada;
  padding: 0 12px;
  border-radius: 4px;
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

.datasource-operation-dropdown {
  padding: 0 !important;
}

.dropdown-content {
  padding: 7px 16px;
  display: block;
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

.show-job-info-icon {
  color: #0bb9f8;
  font-size: 20px;
  position: absolute;
  right: 10px;
  top: 20px;
  cursor: pointer;

  .iconfont {
    color: #8d95a6;
    cursor: pointer;
  }
}
[data-theme='dark'] {
  .host-type-label {
    color: var(--text-primary);
    background-color: var(--bg-select);
  }
}
</style>
