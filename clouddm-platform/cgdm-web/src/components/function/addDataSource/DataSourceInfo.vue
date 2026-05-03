<template>
  <div class="add-datasource-step1">
    <Form
      v-if="currentStep === 0"
      ref="selectDsTypeForm"
      :model="addDataSourceForm"
      label-position="right"
      :label-width="110"
      :rules="addDataSourceRule"
    >
      <FormItem :label="$t('shu-ju-ku-lei-xing')" prop="type">
        <RadioGroup
          v-model="addDataSourceForm.type"
          type="button"
          class="radio-group-radius-warp-datasource custom-radio-group"
          @on-change="handleDataSourceChange"
        >
          <div class="mb-6" v-for="(dataSourceGroup, index) of dataSourceTypes" :key="index">
            <Radio
              translate="no"
              class="custom-radio"
              v-for="type of dataSourceGroup"
              :label="type"
              :disabled="supportedDsType[type] === 'NOT_AUTHED'"
              :key="type"
              style="width: 160px; text-align: center; display: inline-flex; align-items: center; justify-content: center; border-radius: 4px"
            >
              <span>
                <span class="mid-text">
                  {{ getShowNameByDeployTypeAndDsName(getDeployTypeByDsName(type), type) }}
                </span>
                <DataSourceIcon class="ml-1" :type="type" :instanceType="getDeployTypeByDsName(type)"></DataSourceIcon>
              </span>
            </Radio>
          </div>
        </RadioGroup>
      </FormItem>
    </Form>
    <div v-if="currentStep === 1" class="add-datasource-form-stage">
      <div class="datasource-setting-title">
        {{ $t('huan-jing-she-zhi') }}
      </div>
      <Form ref="addLocalDs" :model="addDataSourceForm" label-position="right" :label-width="160" :rules="addDataSourceRule">
        <FormItem :label="$t('huan-jing')" prop="envId">
          <Select style="width: 280px" v-model="addDataSourceForm.envId" @on-change="handleEnvChange">
            <Option v-for="env in envData" :key="env.id" :value="env.id">{{ env.envName }}</Option>
          </Select>
        </FormItem>
        <FormItem v-if="showQueryConfig" :label="$t('bang-ding-ji-qun')" prop="queryClusterId">
          <Select v-model="addDataSourceForm.queryClusterId" style="width: 280px" filterable @on-change="handleChangeQueryCluster">
            <Option
              v-for="cluster in queryClusterList"
              :value="cluster.id"
              :key="cluster.id"
              :label="cluster.clusterDesc ? cluster.clusterDesc : cluster.clusterName"
              :style="`${cluster.runningCount ? '' : 'cursor: not-allowed'}`"
            >
              <p>{{ cluster.clusterName }}</p>
              <p style="color: #ccc; margin: 5px 0">
                {{ cluster.clusterDesc }}
                <span style="margin-left: 8px">{{ cluster.runningCount }}/{{ cluster.workerCount }}</span>
              </p>
            </Option>
          </Select>
        </FormItem>
        <FormItem label="" v-if="showQueryConfig && currentQueryCluster.runningCount === 0">
          <span>
            <span style="color: #ff6e0c">
              <i style="margin-left: 10px; margin-right: 8px" class="iconfont iconTIP"></i>
              {{ $t('gai-ji-qun-wu-cun-huo-ji-qi') }}
            </span>
            <a class="text-cc-primary" :href="`/#/system/dmmachine/list/${addDataSourceForm.queryClusterId}`">
              {{ $t('guan-li-ji-qi') }}
            </a>
          </span>
        </FormItem>
        <div class="datasource-setting-title datasource-setting-title-secondary">
          {{ $t('shu-ju-yuan-she-zhi') }}
        </div>
        <FormItem :label="$t('qu-dong')" key="driverSelection" v-if="currentDriverFamilies.length">
          <div class="driver-selection-row">
            <Select v-model="addDataSourceForm.driverFamily" style="width: 180px" @on-change="handleDriverFamilyChange">
              <Option v-for="family in currentDriverFamilies" :key="family.name" :value="family.name">
                {{ family.name }}
              </Option>
            </Select>
            <Select v-model="addDataSourceForm.driverVersion" style="width: 180px" @on-change="syncDriverValue">
              <Option v-for="version in currentDriverVersions" :key="version" :value="version">
                {{ version }}
              </Option>
            </Select>
          </div>
          <div v-if="showDriverStatusLine" class="driver-status-line" :class="driverStatusLineClass">
            <div class="driver-status-line-main">
              <span class="driver-status-icon-wrap" :class="{ 'is-clickable': canClickDriverStatusIcon }" @click="handleDriverStatusIconClick">
                <span v-if="showDriverDownloadProgress" class="driver-status-progress-circle" :style="driverProgressCircleStyle">
                  <span class="driver-status-progress-circle-text">{{ driverProgressCircleText }}</span>
                </span>
                <Icon v-else-if="driverUiState === 'checking'" type="ios-loading" class="driver-status-loading-icon" />
                <Icon v-else-if="driverUiState === 'ready'" type="md-checkmark-circle" class="driver-status-ready-icon" />
                <Icon v-else-if="driverUiState === 'unknown'" type="ios-help-circle-outline" class="driver-status-unknown-icon" />
                <Icon v-else-if="driverUiState === 'unprepared'" type="ios-warning-outline" class="driver-status-warning-icon" />
                <Icon v-else-if="driverUiState === 'error'" type="ios-alert-circle" class="driver-status-error-icon" />
                <span v-else class="driver-status-phase-dot"></span>
              </span>
              <span class="driver-status-resource" :title="driverResourceText || driverStatusTitleText">
                {{ driverResourceText || driverStatusTitleText }}
              </span>
              <span class="driver-status-progress-text" :title="driverProgressText">
                {{ driverProgressText }}
              </span>
              <div class="driver-status-actions">
                <Button v-if="showDriverActionButton" size="small" @click="handleDriverAction">
                  {{ driverActionLabel }}
                </Button>
              </div>
            </div>
            <div v-if="driverStatusMessageText" class="driver-status-line-detail driver-status-line-detail-message" :title="driverStatusMessageText">
              {{ driverStatusMessageText }}
            </div>
          </div>
        </FormItem>
        <FormItem :label="$t(chooseAddressLabelByDataSourceType(addDataSourceForm.type))">
          <div class="host-list-container">
            <div style="display: inline-block; vertical-align: middle">
              <div v-if="addDataSourceForm.hostList[0].display" style="display: flex; align-items: center">
                <FormItem prop="host" key="host">
                  <Input
                    v-model.trim="addDataSourceForm.hostList[0].host"
                    style="width: 280px"
                    :placeholder="chooseAddressPlaceholderByDataSourceType(addDataSourceForm.type)"
                  />
                </FormItem>
                <div style="margin: 0 5px" v-if="separatePort(addDataSourceForm.type)">:</div>
                <FormItem prop="port" v-if="separatePort(addDataSourceForm.type)" key="port">
                  <Input style="width: 120px" v-model.trim="addDataSourceForm.hostList[0].port" placeholder="port" />
                </FormItem>
                <Tooltip
                  placement="right-start"
                  transfer
                  v-if="addDataSourceForm.hostList[0].display && isBedrock(addDataSourceForm.type)"
                  class="ml-2"
                  style="margin-top: -4px"
                >
                  <CustomIcon type="icon-v2-HelpCircle" hoverStyle size="16px" />
                  <template #content>
                    <a href="https://docs.aws.amazon.com/general/latest/gr/bedrock.html" target="_blank">
                      {{ $t('amazon-bedrock-fu-wu-zhong-duan-jie-dian') }}
                    </a>
                  </template>
                </Tooltip>
              </div>
            </div>
            <a
              style="display: inline-block; margin-left: 10px"
              v-if="isStarRocks(addDataSourceForm.type)"
              :href="`${store.state.docUrlPrefix}/faq/solve_sr_dr_dst_writer_http_host`"
              target="_blank"
            >
              FAQ
            </a>
            <Tooltip placement="right-start" v-if="addDataSourceForm.type === 'OssFile'">
              <CustomIcon type="icon-v2-HelpCircle" hoverStyle leftMargin size="16px" />
              <template #content>
                {{ $t('bang-zhu-wen-dang-qing-cha-kan') }}
                <a href="https://help.aliyun.com/oss/user-guide/regions-and-endpoints" target="_blank">
                  {{ $t('aliyun-oss-link') }}
                </a>
              </template>
            </Tooltip>
            <Tooltip placement="right-start" v-if="addDataSourceForm.type === 'DataLakeFormation'">
              <CustomIcon type="icon-v2-HelpCircle" hoverStyle leftMargin size="16px" />
              <template #content>
                {{ $t('bang-zhu-wen-dang-qing-cha-kan') }}
                <a href="https://help.aliyun.com/dlf/dlf-2-0/developer-reference/service-access-point" target="_blank">
                  {{ $t('aliyun-dlf-link') }}
                </a>
              </template>
            </Tooltip>
            <Tooltip placement="right-start" v-if="addDataSourceForm.type === 'DynamoDB'">
              <CustomIcon type="icon-v2-HelpCircle" hoverStyle leftMargin size="16px" />
              <template #content>
                {{ $t('bang-zhu-wen-dang-qing-cha-kan') }}
                <a href="https://docs.aws.amazon.com/general/latest/gr/ddb.html" target="_blank">
                  {{ $t('aws-ddm-link') }}
                </a>
              </template>
            </Tooltip>
            <Tooltip placement="right-start" v-if="addDataSourceForm.type === 'S3File'">
              <CustomIcon type="icon-v2-HelpCircle" hoverStyle leftMargin size="16px" />
              <template #content>
                {{ $t('bang-zhu-wen-dang-qing-cha-kan') }}
                <a href="https://docs.aws.amazon.com/general/latest/gr/rande.html#s3_region" target="_blank">
                  {{ $t('aws-s3-link') }}
                </a>
              </template>
            </Tooltip>
          </div>
        </FormItem>
        <FormItem
          :label="$t('lian-jie-fang-shi')"
          v-if="(isOracle(addDataSourceForm.type) || isCk(addDataSourceForm.type)) && !isHana(addDataSourceForm.type)"
        >
          <Select v-model="addDataSourceForm.connectType" style="width: 280px">
            <Option v-for="type in oracleConnectTypeList" :value="type.connectType" :key="type.connectType">
              {{ type.i18nName }}
            </Option>
          </Select>
        </FormItem>
        <FormItem
          key="connectTypeValue"
          v-if="isOracle(addDataSourceForm.type) && !isHana(addDataSourceForm.type)"
          :label="addDataSourceForm.connectType"
          prop="connectTypeValue"
        >
          <Input v-model="addDataSourceForm.connectTypeValue" style="width: 150px" :placeholder="addDataSourceForm.connectType" />
        </FormItem>
        <FormItem key="securityType" :label="$t('ren-zheng-fang-shi')" prop="securityType" v-if="securitySetting.length > 1">
          <Select v-model="addDataSourceForm.securityType" style="width: 280px" @on-change="handleSecurityTypeChange">
            <Option v-for="security in securitySetting" :value="security.securityType" :key="security.securityType">
              {{ security.securityTypeI18nName }}
            </Option>
          </Select>
        </FormItem>
        <FormItem :label="$t('kerberos-pei-zhi-wen-jian')" prop="securityFile" v-if="getSecurity(addDataSourceForm.securityType).needKrb5File">
          <input @change="handleFileChange" type="file" name="uploadfile" id="uploadfile" />
          <span style="margin-left: 10px; color: rgb(128, 134, 149)">
            {{ $t('kerberos-ke-hu-duan-pei-zhi-yi-ban-wei-yu-yi-jia-ru-ren-zheng-ti-xi-ji-qi-de-etckrb5conf') }}
          </span>
        </FormItem>
        <FormItem :label="$t('keytab-wen-jian')" prop="securityFile" v-if="getSecurity(addDataSourceForm.securityType).needKeyTabFile">
          <input @change="handleKeyTabFileChange" type="file" name="uploadKeytabFile" id="uploadKeytabFile" />
          <span style="margin-left: 10px; color: rgb(128, 134, 149)">
            {{ $t('jian-yi-zhong-xin-sheng-cheng-he-cheng-hive-he-dui-ying-hdfs-principal-ren-zheng') }}
          </span>
        </FormItem>
        <FormItem
          :label="getSecurity(addDataSourceForm.securityType).dbNameLabel"
          porp="default"
          prop="dbName"
          key="dbName"
          :rules="[
            {
              required: true,
              message: this.$t('mo-ren-shu-ju-ku-bu-neng-wei-kong'),
              trigger: 'blur'
            }
          ]"
          v-if="
            getSecurity(addDataSourceForm.securityType).needDbName &&
            (isDb2(addDataSourceForm.type) ||
              isHana(addDataSourceForm.type) ||
              isGaussDB(addDataSourceForm.type) ||
              isMaxCompute(addDataSourceForm.type))
          "
        >
          <Input v-model="addDataSourceForm.dbName" style="width: 280px" />
          <a v-if="showFaq" style="margin-left: 10px" :href="urlForFaq" target="_blank">FAQ</a>
        </FormItem>
        <FormItem
          :label="getSecurity(addDataSourceForm.securityType).dbNameLabel"
          prop="noValidateDbName"
          key="noValidateDbName"
          v-if="
            getSecurity(addDataSourceForm.securityType).needDbName &&
            !(
              isDb2(addDataSourceForm.type) ||
              isHana(addDataSourceForm.type) ||
              isGaussDB(addDataSourceForm.type) ||
              isMaxCompute(addDataSourceForm.type)
            )
          "
        >
          <Input v-model="addDataSourceForm.noValidateDbName" style="width: 280px" />
        </FormItem>
        <FormItem
          :label="$t('zhang-hao')"
          v-if="getSecurity(addDataSourceForm.securityType).needUserName"
          :rules="[{ required: true, message: $t('zhang-hao-bu-neng-wei-kong'), trigger: 'blur' }]"
          prop="account"
          key="account"
        >
          <Input v-model="addDataSourceForm.account" style="width: 280px" autocomplete="new-password" />
          <Checkbox style="margin-left: 16px" v-if="DataSourceGroup.oracle.indexOf(addDataSourceForm.type) > -1" v-model="addDataSourceForm.asSysDba">
            {{ $t('yi-sysdba-shen-fen-deng-ru') }}
          </Checkbox>
          <a v-if="showPermissionPrepare" style="margin-left: 10px" :href="urlForAuthPrepare" target="_blank">
            {{ $t('quan-xian-zhun-bei') }}
          </a>
        </FormItem>
        <FormItem
          :label="$t('mi-ma')"
          v-if="getSecurity(addDataSourceForm.securityType).needPassword"
          :rules="[{ required: true, message: $t('mi-ma-bu-neng-wei-kong'), trigger: 'blur' }]"
          prop="password"
          key="password"
        >
          <Input v-model="addDataSourceForm.password" style="width: 280px" type="password" password autocomplete="new-password" />
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
          :label="$t('api-key')"
          v-if="getSecurity(addDataSourceForm.securityType).needApiKey"
          :rules="[{ required: true, message: $t('api-key-bu-neng-wei-kong'), trigger: 'blur' }]"
          prop="password"
          key="password"
        >
          <Input v-model="addDataSourceForm.password" style="width: 280px" type="password" password autocomplete="new-password" />
          <Tooltip placement="right-start">
            <CustomIcon type="icon-v2-HelpCircle" hoverStyle leftMargin size="16px" />
            <template #content>
              {{
                $t('api-key-jing-guo-jia-mi-cun-chu-bao-zhang-an-quan-hou-xu-chuang-jian-shu-ju-ren-wu-ke-zhi-jie-lian-jie-wu-xu-zhong-xin-tian-xie')
              }}
            </template>
          </Tooltip>
        </FormItem>
        <FormItem
          v-if="Mapping.testSecurityType.includes(addDataSourceForm.securityType) && canTestyDsList.includes(addDataSourceForm.type)"
          key="testConnection"
        >
          <Button :loading="testConnectionLoading" @click="handleTestConnection">{{ $t('ce-shi-lian-jie') }}</Button>
          <span v-if="hasTestConnectionResult" class="test-connection-result">
            <Icon :type="testConnectionSuccess ? 'ios-checkmark-circle' : 'ios-close-circle'" :color="testConnectionSuccess ? 'green' : 'red'" />
            {{ testConnectionMessage }}
          </span>
        </FormItem>
        <FormItem
          :label="$t('ak')"
          v-if="getSecurity(addDataSourceForm.securityType).needAkSk"
          :rules="[{ required: true, message: $t('ak-bu-neng-wei-kong'), trigger: 'blur' }]"
          prop="accessKey"
          key="accessKey"
        >
          <Input v-model="addDataSourceForm.accessKey" style="width: 280px" />
        </FormItem>
        <FormItem
          :label="$t('sk')"
          v-if="getSecurity(addDataSourceForm.securityType).needAkSk"
          :rules="[{ required: true, message: $t('sk-bu-neng-wei-kong'), trigger: 'blur' }]"
          prop="secretKey"
          key="secretKey"
        >
          <Input v-model="addDataSourceForm.secretKey" style="width: 280px" />
        </FormItem>
        <FormItem
          :label="$t('ke-hu-duan-truststore-mi-ma')"
          v-if="getSecurity(addDataSourceForm.securityType).needClientTrustStorePassword"
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
          :label="$t('ssl-pei-zhi-wen-jian')"
          prop="securityFile"
          key="securityFile"
          v-if="getSecurity(addDataSourceForm.securityType).needTlsFile"
        >
          <input @change="handleFileChange" type="file" name="uploadfile" id="uploadfile1" />
          <span style="margin-left: 10px; color: rgb(128, 134, 149)"></span>
        </FormItem>
        <FormItem
          :label="$t('keystore-wen-jian')"
          prop="keystoreFile"
          key="keystoreFile"
          v-if="getSecurity(addDataSourceForm.securityType).needKeyStoreFile"
        >
          <input @change="handleKeystoreFileChange" type="file" name="uploadfile" id="uploadfile1" />
          <span style="margin-left: 10px; color: rgb(128, 134, 149)"></span>
        </FormItem>
        <FormItem
          :label="$t('keystore-mi-ma')"
          v-if="getSecurity(addDataSourceForm.securityType).needKeyStoreFilePassword"
          key="keystoreFilePassword"
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
        <FormItem :label="$t('ca-zheng-shu')" prop="caFile" key="caFile" v-if="getSecurity(addDataSourceForm.securityType).needCaFile">
          <input @change="handleCaFileChange" type="file" name="uploadfile" id="uploadfile1" />
          <span style="margin-left: 10px; color: rgb(128, 134, 149)"></span>
        </FormItem>
        <FormItem :label="$t('json-zheng-shu')" prop="jsonFile" key="jsonFile" v-if="getSecurity(addDataSourceForm.securityType).needJsonFile">
          <input @change="handleJsonFileChange" type="file" name="uploadfile" id="uploadfile1" />
          <span style="margin-left: 10px; color: rgb(128, 134, 149)"></span>
        </FormItem>
        <!-- mysql ssl相关 start -->
        <FormItem
          :label="$t('truststore-wen-jian')"
          prop="tlsTrustStoreFile"
          key="tlsTrustStoreFile"
          v-if="getSecurity(addDataSourceForm.securityType).needTlsTrustStoreFile"
        >
          <input @change="handleTlsTrustStoreFileChange" type="file" name="uploadfile" id="uploadfile1" />
          <span style="margin-left: 10px; color: rgb(128, 134, 149)"></span>
        </FormItem>
        <FormItem
          :label="$t('truststore-wen-jian-mi-ma')"
          prop="tlsTrustStoreFilePassword"
          key="tlsTrustStoreFilePassword"
          v-if="getSecurity(addDataSourceForm.securityType).needTlsTrustStoreFilePassword"
        >
          <Input v-model="addDataSourceForm.tlsTrustStoreFilePassword" style="width: 280px" type="password" password autocomplete="new-password" />
        </FormItem>
        <FormItem
          :label="$t('key-store-wen-jian')"
          prop="tlsKeystoreFile"
          key="tlsKeystoreFile"
          v-if="getSecurity(addDataSourceForm.securityType).needTlsKeyStoreFile"
        >
          <input @change="handleTlsKeystoreFileChange" type="file" name="uploadfile" id="uploadfile1" />
          <span style="margin-left: 10px; color: rgb(128, 134, 149)"></span>
        </FormItem>
        <FormItem
          :label="$t('key-store-mi-ma')"
          v-if="getSecurity(addDataSourceForm.securityType).needTlsKeyStoreFilePassword"
          key="tlsKeystoreFilePassword"
          prop="tlsKeystoreFilePassword"
        >
          <Input v-model="addDataSourceForm.tlsKeystoreFilePassword" style="width: 280px" type="password" password autocomplete="new-password" />
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
        <FormItem
          :label="$t('ke-hu-duan-ca-zheng-shu')"
          prop="clientSecurityFile"
          key="clientSecurityFile"
          v-if="getSecurity(addDataSourceForm.securityType).needClientCaFile"
        >
          <input @change="handleClientCaFileChange" type="file" name="clientSecurityFile" id="clientSecurityFile" />
          <span style="margin-left: 10px; color: rgb(128, 134, 149)"></span>
        </FormItem>
        <FormItem
          :label="$t('ke-hu-duan-si-yao-wen-jian')"
          prop="clientSecretFile"
          key="clientSecretFile"
          v-if="getSecurity(addDataSourceForm.securityType).needClientKeyFile"
        >
          <input @change="handleClientKeyFileChange" type="file" name="clientSecretFile" id="clientSecretFile" />
          <span style="margin-left: 10px; color: rgb(128, 134, 149)"></span>
        </FormItem>
        <FormItem
          :label="$t('ssl-si-yao-mi-ma')"
          prop="secretFilePassword"
          key="secretFilePassword"
          v-if="getSecurity(addDataSourceForm.securityType).needSecretFilePassword"
        >
          <Input v-model="addDataSourceForm.secretFilePassword" style="width: 280px" type="password" password autocomplete="new-password" />
        </FormItem>
        <FormItem :label="$t('json-zheng-shu')" prop="jsonFile" key="jsonFile" v-if="getSecurity(addDataSourceForm.securityType).needJsonFile">
          <input @change="handleJsonFileChange" type="file" name="uploadfile" id="uploadfile1" />
          <span style="margin-left: 10px; color: rgb(128, 134, 149)"></span>
        </FormItem>
        <FormItem :label="$t('miao-shu')" key="desc">
          <Input v-model="addDataSourceForm.instanceDesc" style="width: 280px" />
          <Tooltip placement="right-start">
            <CustomIcon type="icon-v2-HelpCircle" hoverStyle leftMargin size="16px" />
            <template #content>
              {{ $t('bei-zhu-bian-yu-ji-yi-de-ming-zi-fang-bian-shi-yong-shi-shi-bie-ru-jiao-yi-ku-yong-hu-ku-ce-shi-ku-deng') }}
            </template>
          </Tooltip>
        </FormItem>
        <!--          <AddHive :getSecurity="getSecurity" :addDataSourceForm="addDataSourceForm"-->
        <!--                   :handleFileChange="handleFileChange"-->
        <!--                   :handleKeyTabFileChange="handleKeyTabFileChange"-->
        <!--                   v-if="addDataSourceForm.type==='Hive'"></AddHive>-->
        <FormItem :label="$t('e-wai-can-shu')" v-if="addDataSourceForm.dsKvConfigs.length && ifShowDsExtraConf">
          <config-params-edit :ds-kv-configs="addDataSourceForm.dsKvConfigs" @updateDsKvConfig="updateDsKvConfig" />
        </FormItem>
      </Form>
    </div>
    <test-connection-modal
      v-model:visible="showTestConnectionModal"
      :test-connection="testConnection"
      :datasource="addDataSourceForm"
      :handle-close-modal="hideTestConnectionModal"
    />
  </div>
</template>
<script>
import { isBedrock, isCk, isDb2, isDuckDB, isHana, isOracle, isRabbitMQ, isStarRocks, separatePort, isGaussDB, isMaxCompute } from '@/utils';
import { CONNECT_TYPE, ORACLE_CONTENT_TYPE } from '@/const/ccIndex';
import ConfigParamsEdit from '@/views/system/ConfigParamsEdit';
import Mapping from '@/views/util';
import utilMixin from '@/mixins/utilMixin';
import store from '@/store';
import { mapGetters } from 'vuex';
import { EVENT_BUS_NAME_LIST } from '@/utils/eventBusName';
// import AddHive from './AddHive';
import DataSourceIcon from '@/components/function/DataSourceIcon';
import DataSourceGroup from '../../../views/dataSourceGroup.json';
import TestConnectionModal from './TestConnectionModal';

const ALIBABA_CLOUD_HOSTED_DS_TYPES = new Set(['PolarDBPg', 'PolarDbMySQL', 'PolarDbX', 'AdbForMySQL', 'Hologres', 'MaxCompute']);

export default {
  name: 'DataSourceInfo',
  mixins: [utilMixin],
  components: {
    ConfigParamsEdit,
    TestConnectionModal,
    DataSourceIcon
  },
  props: {
    addDataSourceForm: Object,
    currentStep: {
      type: Number,
      default: 0
    },
    autoEnableFeatures: {
      type: Boolean,
      default: false
    },
    showQueryConfig: {
      type: Boolean,
      default: false
    },
    setSecuritySetting: Function,
    driverFamilyMap: {
      type: Object,
      default: () => ({})
    }
  },
  created() {
    this.$bus.on(EVENT_BUS_NAME_LIST.WS_RES_DRIVER_DOWNLOAD_EVENT, this.handleDriverDownloadEvent);
    this.listDataSourceTypes();
    // this.listRegions();
    // this.listDataSourceTypes();
    // this.getSecurityType();
    this.getDefaultKVConfig();
    if (this.includesCC) {
      this.needTestBeforeAddDsTypes();
    }
    this.listEnv();
    if (this.showQueryConfig || this.autoEnableFeatures) {
      this.listQueryBindCluster();
    }
  },
  beforeUnmount() {
    clearInterval(this.checkNetInfo);
    this.clearDriverStatusCheckTimeout();
    this.$bus.off(EVENT_BUS_NAME_LIST.WS_RES_DRIVER_DOWNLOAD_EVENT, this.handleDriverDownloadEvent);
  },
  data() {
    return {
      driverStatus: {
        checking: false,
        available: false,
        totalFileCount: 0,
        completedFileCount: 0,
        currentFilePercent: 0,
        status: 'IDLE',
        retryAction: 'CHECK',
        message: '',
        resourceCoordinate: '',
        currentFileName: ''
      },
      driverStatusRequestKey: '',
      driverStatusTimeoutId: null,
      clearData: {
        dbName: '',
        noValidateDbName: '',
        driver: '',
        driverFamily: '',
        driverVersion: '',
        dsKvConfigs: [],
        hostList: [
          {
            type: 'public',
            display: true,
            host: '',
            port: '3306'
          },
          {
            type: 'public',
            display: false,
            host: '',
            port: '3306'
          }
        ],
        connectType: null,
        connectTypeValue: '',
        host: '',
        rdsList: [],
        aliyunAk: '',
        aliyunSk: '',
        instanceDesc: '',
        ifAkSK: 'true',
        port: '',
        publicHost: '',
        publicPort: '',
        hdfsSecurityType: 'NONE',
        account: '',
        password: '',
        hdfsPort: '8020',
        securityType: 'KERBEROS',
        hdfsDwDir: '/user/hive/warehouse',
        sid: '',
        service: '',
        accountRole: '',
        asSysDba: false,
        securityFile: '',
        caFile: '',
        clientSecurityFile: '',
        clientSecretFile: '',
        secretFilePassword: '',
        jsonFile: '',
        clientTrustStorePassword: '',
        keystoreFile: '',
        accessKey: '',
        secretKey: ''
      },
      kvConfig: [],
      CONNECT_TYPE,
      oracleConnectTypeList: [],
      ORACLE_CONTENT_TYPE,
      showTestConnectionModal: false,
      testConnectionLoading: false,
      hasTestConnectionResult: false,
      testConnectionSuccess: false,
      testConnectionMessage: '',
      checkAll: false,
      securitySetting: [],
      store,
      DataSourceGroup,
      showNoData: false,
      hasChecked: false,
      needCancelList: [],
      envData: [],
      queryClusterList: [],
      currentQueryCluster: {},
      checkList: {},
      dataSourceTypes: [],
      regions: [],
      supportedDsType: {},
      supportedRegions: [],
      regionAreas: [],
      canTestyDsList: ['MySQL'],
      checkNetInfo: '',
      page: 1,
      size: 10,
      total: 0,
      noMoreData: true,
      searchKey: '',
      Mapping,
      showTips: false,
      showConfirmPublic: false,
      checkPermission: false,
      selectedRow: {},
      consoleIp: '',
      filterData: [],
      showData: [],
      selectedDataSourceColumn: [
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          title: this.$t('shi-li-id'),
          key: 'instanceId',
          minWidth: 120
        },
        {
          title: this.$t('miao-shu'),
          key: 'instanceDesc',
          minWidth: 120
        },
        {
          title: this.$t('host'),
          key: 'host',
          slot: 'host',
          width: 470
        },
        {
          title: this.$t('ban-ben-hao'),
          key: 'version',
          slot: 'version',
          minWidth: 120
        }
      ],
      loadingRdsList: false,
      addDataSourceRuleAkSk: {
        aliyunAk: [
          {
            required: true,
            message: this.$t('the-ak-cannot-be-empty'),
            trigger: 'blur'
          }
        ],
        aliyunSk: [
          {
            required: true,
            message: this.$t('the-sk-cannot-be-empty'),
            trigger: 'blur'
          }
        ]
      },
      addDataSourceRule: {
        host: [
          {
            validator: (rule, value, callback) => {
              if (!this.addDataSourceForm.hostList[0].host) {
                return callback(
                  new Error(
                    this.$t('isstarrocksthisadddatasourceformtype-client-wang-luo-di-zhi-bu-neng-wei-kong', [
                      isStarRocks(this.addDataSourceForm.type) ? this.$t('client') : this.$t('wang-luo')
                    ])
                  )
                );
              }
              // if ((this.addDataSourceForm.host && this.addDataSourceForm.host.indexOf(':') === -1)
              //       || (this.addDataSourceForm.publicHost && this.addDataSourceForm.publicHost.indexOf(':') === -1)) {
              //   return callback(new Error('缺少端口信息。请正确填写数据源信息。'));
              // }
              return callback();
            },
            trigger: 'blur'
          }
        ],
        port: [
          {
            validator: (rule, value, callback) => {
              if (!this.addDataSourceForm.hostList[0].port && this.addDataSourceForm.type !== 'Db2Fori') {
                return callback(
                  new Error(
                    this.$t('isstarrocksthisadddatasourceformtype-client-wang-luo-duan-kou-bu-neng-wei-kong', [
                      isStarRocks(this.addDataSourceForm.type) ? this.$t('client') : this.$t('wang-luo')
                    ])
                  )
                );
              }
              // if ((this.addDataSourceForm.host && this.addDataSourceForm.host.indexOf(':') === -1)
              //       || (this.addDataSourceForm.publicHost && this.addDataSourceForm.publicHost.indexOf(':') === -1)) {
              //   return callback(new Error('缺少端口信息。请正确填写数据源信息。'));
              // }
              return callback();
            },
            trigger: 'blur'
          }
        ],
        connectTypeValue: [
          {
            required: true,
            message: this.$t('thisadddatasourceformconnecttype-bu-neng-wei-kong', [this.addDataSourceForm.connectType]),
            trigger: 'change'
          }
        ],
        region: [
          {
            required: true,
            type: 'string',
            message: this.$t('the-region-cannot-be-empty'),
            trigger: 'change'
          }
        ],
        hdfsIp: [
          {
            required: true,
            message: this.$t('the-hdfsip-cannot-be-empty'),
            trigger: 'blur'
          }
        ],
        hdfsPort: [
          {
            required: true,
            message: this.$t('the-hdfsport-cannot-be-empty'),
            trigger: 'blur'
          }
        ],
        hdfsDwDir: [
          {
            required: true,
            message: this.$t('the-hdfsdwdir-cannot-be-empty'),
            trigger: 'blur'
          }
        ],
        account: [
          {
            required: true,
            message: this.$t('the-account-cannot-be-empty'),
            trigger: 'blur'
          }
        ],
        hdfsSecurityType: [
          {
            required: true,
            type: 'string',
            message: this.$t('the-hdfssecuritytype-cannot-be-empty'),
            trigger: 'change'
          }
        ],
        type: [
          {
            required: true,
            message: this.$t('the-type-cannot-be-empty'),
            trigger: 'change'
          }
        ],
        envId: [
          {
            validator: (rule, value, callback) => {
              if (this.addDataSourceForm.envId === '' || this.addDataSourceForm.envId === null || this.addDataSourceForm.envId === undefined) {
                return callback(new Error(this.$t('huan-jing-bu-neng-wei-kong')));
              }
              return callback();
            },
            trigger: 'change'
          }
        ],
        queryClusterId: [
          {
            validator: (rule, value, callback) => {
              if (this.showQueryConfig && !value) {
                return callback(new Error(this.$t('bang-ding-ji-qun-bu-neng-wei-kong')));
              }
              return callback();
            },
            trigger: 'change'
          }
        ],
        clientTrustStorePassword: [
          {
            required: true,
            message: this.$t('ke-hu-duan-truststore-mi-ma-bu-neng-wei-kong'),
            trigger: 'change'
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
        //     message: this.$t('ca-zheng-shu-bu-neng-wei-kong-0')
        //   }
        // ],
        clientSecurityFile: [
          {
            required: false,
            message: this.$t('ke-hu-duan-ca-zheng-shu-bu-neng-wei-kong')
          }
        ],
        clientSecretFile: [
          {
            required: false,
            message: this.$t('ke-hu-duan-si-yao-wen-jian-bu-neng-wei-kong')
          }
        ],
        secretFilePassword: [
          {
            required: false,
            message: this.$t('ssl-si-yao-mi-ma-bu-neng-wei-kong')
          }
        ],
        jsonFile: [
          {
            required: true,
            message: this.$t('json-wen-jian-bu-neng-wei-kong'),
            trigger: 'change',
            validator: (rule, value, callback) => {
              if (!this.addDataSourceForm.jsonFile) {
                return callback(new Error(this.$t('json-wen-jian-bu-neng-wei-kong')));
              } else {
                callback();
              }
            }
          }
        ],
        keystoreFile: [
          {
            required: true,
            message: this.$t('keystore-wen-jian-bu-neng-wei-kong')
          }
        ],
        driver: [
          {
            required: true,
            message: this.$t('qu-dong-bu-neng-wei-kong'),
            trigger: 'blur'
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
      }
    };
  },
  computed: {
    ...mapGetters(['includesCC', 'isDesktop', 'ifShowDsExtraConf']),
    currentDriverFamilies() {
      return this.driverFamilyMap[this.addDataSourceForm.type] || [];
    },
    currentDriverVersions() {
      const family = this.currentDriverFamilies.find((item) => item.name === this.addDataSourceForm.driverFamily);
      return Array.isArray(family?.versions) ? family.versions : [];
    },
    selectedDriverKey() {
      const { driverFamily, driverVersion } = this.addDataSourceForm;
      return driverFamily && driverVersion ? `${driverFamily}::${driverVersion}` : '';
    },
    selectedDriverStatusKey() {
      if (!this.selectedDriverKey) {
        return '';
      }

      const clusterId = this.normalizeDriverClusterId(this.addDataSourceForm.queryClusterId);
      return `${this.selectedDriverKey}::${clusterId || 'ALL'}`;
    },
    driverUiState() {
      switch (this.driverStatus.status) {
        case 'CHECKING':
          return 'checking';
        case 'UNKNOWN':
          return 'unknown';
        case 'AVAILABLE':
          return 'ready';
        case 'UNAVAILABLE':
          return 'unprepared';
        case 'ERROR':
        case 'FAILED':
          return 'error';
        case 'DOWNLOADING':
        case 'PREPARING':
        case 'SYNCING':
          return 'downloading';
        default:
          return 'idle';
      }
    },
    showDriverReadyState() {
      return this.driverUiState === 'ready';
    },
    showDriverWorkingState() {
      return this.driverUiState === 'checking' || this.showDriverDownloadProgress;
    },
    showDriverDownloadProgress() {
      return ['DOWNLOADING', 'PREPARING', 'SYNCING'].includes(this.driverStatus.status);
    },
    showDriverCheckAction() {
      return this.driverUiState === 'unknown';
    },
    showDriverDownloadAction() {
      return this.driverUiState === 'unprepared';
    },
    driverProgressLabel() {
      const { totalFileCount, completedFileCount } = this.driverStatus;
      return totalFileCount > 0 ? `${completedFileCount}/${totalFileCount}` : '';
    },
    driverProgressValue() {
      const { totalFileCount, completedFileCount } = this.driverStatus;
      if (!(totalFileCount > 0)) {
        return 0;
      }

      const currentStepPercent = Math.max(0, Math.min(100, Number(this.driverStatus.currentFilePercent) || 0));
      const completedRatio = Number(completedFileCount) / Number(totalFileCount);
      const currentStepRatio = currentStepPercent / 100 / Number(totalFileCount);

      return Math.max(0, Math.min(100, Math.round((completedRatio + currentStepRatio) * 100)));
    },
    driverProgressText() {
      return this.showDriverDownloadProgress ? this.driverProgressPercent : '';
    },
    driverProgressCircleText() {
      return this.showDriverDownloadProgress ? `${this.driverProgressValue}%` : '';
    },
    driverProgressCircleStyle() {
      return {
        '--driver-progress-percent': `${this.driverProgressValue}%`
      };
    },
    driverProgressPercent() {
      return `${Math.max(0, Math.min(100, Number(this.driverStatus.currentFilePercent) || 0))}%`;
    },
    showDriverStatusLine() {
      return !!this.selectedDriverKey && this.driverUiState !== 'idle';
    },
    driverStatusLineClass() {
      return `is-${this.driverUiState}`;
    },
    driverStatusTitleText() {
      return [this.addDataSourceForm.driverFamily, this.addDataSourceForm.driverVersion].filter(Boolean).join(' / ');
    },
    driverStatusTargetText() {
      const resourceText = `${this.driverStatus.resourceCoordinate || ''}`.trim();
      const fileText = `${this.driverStatus.currentFileName || ''}`.trim();
      const driverText = this.driverStatusTitleText;

      if (resourceText && fileText) {
        return `${resourceText}（${fileText}）`;
      }

      return resourceText || fileText || driverText;
    },
    driverResourceText() {
      if (this.showDriverDownloadProgress) {
        return `${this.driverStatus.currentFileName || ''}`.trim() || this.driverStatusTargetText;
      }

      return this.driverStatusTitleText;
    },
    driverStatusMessageText() {
      const message = `${this.driverStatus.message || ''}`.trim();
      if (!message || message === this.driverStatus.currentFileName || message === this.driverStatus.resourceCoordinate) {
        return '';
      }
      if (message === this.driverStatusTargetText || message === this.driverResourceText) {
        return '';
      }
      return message;
    },
    driverStatusErrorMessage() {
      const message = `${this.driverStatus.message || ''}`.trim();
      if (!message || this.driverUiState !== 'error') {
        return '';
      }
      return message;
    },
    showDriverStatusError() {
      return !!this.driverStatusErrorMessage;
    },
    showDriverActionButton() {
      return this.showDriverCheckAction || this.showDriverDownloadAction || this.driverUiState === 'error';
    },
    driverActionLabel() {
      if (this.showDriverCheckAction) {
        return this.$t('jian-cha');
      }
      if (this.showDriverDownloadAction) {
        return this.$t('xia-zai');
      }
      if (this.driverUiState === 'error') {
        return this.$t('zhong-shi');
      }
      return '';
    },
    canClickDriverStatusIcon() {
      return !['checking', 'downloading'].includes(this.driverUiState);
    },
    hasPrivateQueryHost() {
      return this.addDataSourceForm.hostList.some((item) => item.display && item.type === 'private');
    },
    hasPublicQueryHost() {
      return this.addDataSourceForm.hostList.some((item) => item.display && item.type === 'public');
    },
    useInlineTestConnection() {
      return !!this.normalizeDriverClusterId(this.addDataSourceForm.queryClusterId);
    },
    urlForAuthPrepare() {
      let url = store.state.docUrlPrefix;
      if (this.addDataSourceForm.type === 'MySQL') {
        url += '/dataMigrationAndSync/datasource_func/MySQL/privs_for_mysql';
      } else if (this.addDataSourceForm.type === 'Oracle') {
        url += '/dataMigrationAndSync/datasource_func/Oracle/privs_for_oracle';
      } else if (this.addDataSourceForm.type === 'Kafka') {
        url += 'dataMigrationAndSync/datasource_func/Kafka/privs_for_kafka';
      } else if (this.addDataSourceForm.type === 'Db2') {
        url += '/dataMigrationAndSync/datasource_func/Db2/prepare_for_db2';
      } else if (this.addDataSourceForm.type === 'SQLServer') {
        url += '/dataMigrationAndSync/datasource_func/SqlServer/privs_for_sqlserver';
      } else if (this.addDataSourceForm.type === 'Hana') {
        url += '/dataMigrationAndSync/datasource_func/Hana/privs_for_hana';
      } else if (this.addDataSourceForm.type === 'Dameng') {
        url += '/dataMigrationAndSync/datasource_func/Dameng/privs_for_dameng';
      } else if (this.addDataSourceForm.type === 'PostgreSQL') {
        url += '/dataMigrationAndSync/datasource_func/PostgreSQL/privs_for_pg';
      } else if (this.addDataSourceForm.type === 'GaussDB') {
        url += '/dataMigrationAndSync/datasource_func/GaussDB/privs_for_gaussdb';
      } else if (this.addDataSourceForm.type === 'TiDB') {
        url += '/dataMigrationAndSync/datasource_func/TiDB/privs_for_tidb';
      } else if (this.addDataSourceForm.type === 'MongoDB') {
        url += '/dataMigrationAndSync/datasource_func/MongoDB/privs_for_mongo';
      } else if (this.addDataSourceForm.type === 'TDengine') {
        url += '/dataMigrationAndSync/datasource_func/TDengine/privs_for_tdengine';
      } else if (this.addDataSourceForm.type === 'DynamoDB') {
        url += '/dataMigrationAndSync/datasource_func/DynamoDB/privs_for_dynamodb';
      }
      return url;
    },
    urlForFaq() {
      let url = store.state.docUrlPrefix;
      if (this.addDataSourceForm.type === 'Hana') {
        url += '/faq/solve_hana_test_connection_fail';
      }
      return url;
    },
    showPermissionPrepare() {
      if (this.isDesktop) {
        return false;
      }
      const showDsList = ['MySQL', 'Oracle', 'Kafka', 'Db2', 'SQLServer', 'Hana'];
      if (showDsList.includes(this.addDataSourceForm.type)) {
        return true;
      }
      return false;
    },
    showFaq() {
      const showDsList = ['Hana'];
      if (showDsList.includes(this.addDataSourceForm.type)) {
        return true;
      }
      return false;
    }
  },
  watch: {
    currentDriverFamilies: {
      handler() {
        this.applyDriverFamilySelection();
      },
      immediate: true
    },
    selectedDriverStatusKey() {
      if (!this.selectedDriverStatusKey) {
        this.resetDriverStatus();
        return;
      }

      if (this.currentStep === 1) {
        this.refreshDriverStatus();
      }
    },
    currentStep(step) {
      if (step === 1) {
        this.refreshDriverStatus();
      }
    }
  },
  methods: {
    isCk,
    isHana,
    isDb2,
    isMaxCompute,
    isOracle,
    isStarRocks,
    isGaussDB,
    normalizeDriverClusterId(clusterId) {
      const normalized = Number(clusterId);
      return Number.isFinite(normalized) && normalized > 0 ? normalized : null;
    },
    currentClusterHasRunningWorkers() {
      return Number(this.currentQueryCluster?.runningCount) > 0;
    },
    ensureDriverClusterHasRunningWorkers() {
      if (this.currentClusterHasRunningWorkers()) {
        return true;
      }

      this.$Message.warning(this.$t('gai-ji-qun-wu-cun-huo-ji-qi'));
      return false;
    },
    getDriverClusterId() {
      const clusterId = this.normalizeDriverClusterId(this.addDataSourceForm.queryClusterId);
      return clusterId || undefined;
    },
    getDeployTypeByDsName(dataSourceType) {
      return ALIBABA_CLOUD_HOSTED_DS_TYPES.has(dataSourceType) ? 'ALIBABA_CLOUD_HOSTED' : 'SELF_MAINTENANCE';
    },
    chooseAddressLabelByDataSourceType(dataSourceType) {
      if (isStarRocks(dataSourceType)) {
        return 'client-di-zhi';
      } else if (isDuckDB(dataSourceType)) {
        return 'wen-jian-di-zhi';
      } else {
        return 'wang-luo-di-zhi';
      }
    },
    chooseAddressPlaceholderByDataSourceType(dataSourceType) {
      if (isRabbitMQ(dataSourceType)) {
        return 'ip(or domain):amqp_port:http_port';
      } else if (isDuckDB(dataSourceType)) {
        return 'file path';
      } else if (!separatePort(dataSourceType)) {
        return 'ip:port,domain:port';
      } else {
        return 'ip,domain';
      }
    },
    isBedrock,
    syncDriverValue() {
      const { driverFamily, driverVersion } = this.addDataSourceForm;
      this.addDataSourceForm.driver = driverFamily && driverVersion ? JSON.stringify([driverFamily, `/${driverVersion}`]) : '';
    },
    applyDriverFamilySelection(forceReset = false) {
      const families = this.currentDriverFamilies;

      if (!families.length) {
        this.addDataSourceForm.driverFamily = '';
        this.addDataSourceForm.driverVersion = '';
        this.addDataSourceForm.driver = '';
        this.resetDriverStatus();
        return;
      }

      let currentFamily = families.find((item) => item.name === this.addDataSourceForm.driverFamily);

      if (!currentFamily || forceReset) {
        currentFamily = families[0];
        this.addDataSourceForm.driverFamily = currentFamily?.name || '';
      }

      const versions = Array.isArray(currentFamily?.versions) ? currentFamily.versions : [];
      if (!versions.length) {
        this.addDataSourceForm.driverVersion = '';
        this.addDataSourceForm.driver = '';
        this.resetDriverStatus();
        return;
      }

      if (forceReset || !versions.includes(this.addDataSourceForm.driverVersion)) {
        this.addDataSourceForm.driverVersion = versions[0];
      }

      this.syncDriverValue();
    },
    handleDriverFamilyChange(familyName) {
      const family = this.currentDriverFamilies.find((item) => item.name === familyName);
      const versions = Array.isArray(family?.versions) ? family.versions : [];

      this.addDataSourceForm.driverVersion = versions.length ? versions[0] : '';
      this.syncDriverValue();
    },
    resetDriverStatus() {
      this.clearDriverStatusCheckTimeout();
      this.driverStatusRequestKey = '';
      this.driverStatus = {
        checking: false,
        available: false,
        totalFileCount: 0,
        completedFileCount: 0,
        currentFilePercent: 0,
        status: 'IDLE',
        retryAction: 'CHECK',
        message: '',
        resourceCoordinate: '',
        currentFileName: ''
      };
    },
    clearDriverStatusCheckTimeout() {
      if (this.driverStatusTimeoutId) {
        clearTimeout(this.driverStatusTimeoutId);
        this.driverStatusTimeoutId = null;
      }
    },
    scheduleDriverStatusCheckTimeout(requestKey) {
      this.clearDriverStatusCheckTimeout();
      this.driverStatusTimeoutId = setTimeout(() => {
        if (this.driverStatusRequestKey !== requestKey || this.driverStatus.status !== 'CHECKING') {
          return;
        }

        this.driverStatusRequestKey = '';
        this.driverStatus = {
          ...this.driverStatus,
          checking: false,
          available: false,
          status: 'UNKNOWN',
          retryAction: 'CHECK',
          message: ''
        };
      }, 15000);
    },
    setDriverErrorStatus(message, retryAction = 'CHECK') {
      this.clearDriverStatusCheckTimeout();
      this.driverStatus = {
        ...this.driverStatus,
        checking: false,
        available: false,
        status: 'ERROR',
        retryAction,
        message: message || ''
      };
    },
    async refreshDriverStatus() {
      const driverKey = this.selectedDriverStatusKey;
      if (!driverKey) {
        this.resetDriverStatus();
        return;
      }

      const { driverFamily, driverVersion } = this.addDataSourceForm;
      const requestKey = `${driverKey}::${Date.now()}`;
      this.driverStatusRequestKey = requestKey;
      this.driverStatus = {
        ...this.driverStatus,
        checking: true,
        available: false,
        status: 'CHECKING',
        retryAction: 'CHECK',
        message: '',
        resourceCoordinate: '',
        currentFileName: '',
        totalFileCount: 0,
        completedFileCount: 0,
        currentFilePercent: 0
      };
      this.scheduleDriverStatusCheckTimeout(requestKey);

      try {
        const res = await this.$services.rdpDataSourceCheckDriverStatus({
          data: {
            clusterId: this.getDriverClusterId(),
            driverFamily,
            driverVersion
          }
        });

        if (this.driverStatusRequestKey !== requestKey || this.selectedDriverStatusKey !== driverKey) {
          return;
        }

        this.clearDriverStatusCheckTimeout();

        if (res.success) {
          const available = !!res.data?.available;
          this.driverStatus = {
            ...this.driverStatus,
            checking: false,
            available,
            status: available ? 'AVAILABLE' : 'UNAVAILABLE',
            retryAction: available ? 'CHECK' : 'DOWNLOAD',
            message: ''
          };
          return;
        }

        this.setDriverErrorStatus(res.msg || '', 'CHECK');
      } catch (error) {
        if (this.driverStatusRequestKey !== requestKey || this.selectedDriverStatusKey !== driverKey) {
          return;
        }

        this.setDriverErrorStatus(error?.message || '', 'CHECK');
      }
    },
    handleCheckDriverStatus() {
      if (!this.ensureDriverClusterHasRunningWorkers()) {
        return;
      }

      this.refreshDriverStatus();
    },
    handleDriverStatusIconClick() {
      if (this.canClickDriverStatusIcon) {
        this.handleCheckDriverStatus();
      }
    },
    handleDriverAction() {
      if (this.showDriverCheckAction) {
        this.handleCheckDriverStatus();
        return;
      }

      if (this.showDriverDownloadAction) {
        this.handleDownloadDriver();
        return;
      }

      if (this.driverUiState === 'error') {
        if (this.driverStatus.retryAction === 'DOWNLOAD') {
          this.handleDownloadDriver();
        } else {
          this.handleCheckDriverStatus();
        }
      }
    },
    async handleDownloadDriver() {
      const { driverFamily, driverVersion } = this.addDataSourceForm;
      if (!driverFamily || !driverVersion) {
        return;
      }

      if (!this.ensureDriverClusterHasRunningWorkers()) {
        return;
      }

      this.clearDriverStatusCheckTimeout();
      this.driverStatus = {
        ...this.driverStatus,
        checking: false,
        available: false,
        totalFileCount: 0,
        completedFileCount: 0,
        currentFilePercent: 0,
        status: 'DOWNLOADING',
        retryAction: 'DOWNLOAD',
        message: '',
        resourceCoordinate: '',
        currentFileName: ''
      };

      try {
        const res = await this.$services.rdpDataSourceDownloadDriver({
          data: {
            clusterId: this.getDriverClusterId(),
            driverFamily,
            driverVersion
          }
        });

        if (!res.success) {
          this.setDriverErrorStatus(res.msg || this.$t('xia-zai-shi-bai'), 'DOWNLOAD');
          this.$Message.error(res.msg || this.$t('xia-zai-shi-bai'));
        }
      } catch (error) {
        this.setDriverErrorStatus(error?.message || this.$t('xia-zai-shi-bai'), 'DOWNLOAD');
        this.$Message.error(error?.message || this.$t('xia-zai-shi-bai'));
      }
    },
    handleDriverDownloadEvent(payload) {
      const event = payload?.object || payload;
      if (!event) {
        return;
      }

      const isCurrentDriver =
        event.driverFamily === this.addDataSourceForm.driverFamily &&
        event.driverVersion === this.addDataSourceForm.driverVersion &&
        this.normalizeDriverClusterId(event.clusterId) === this.normalizeDriverClusterId(this.addDataSourceForm.queryClusterId);
      if (!isCurrentDriver) {
        return;
      }

      this.clearDriverStatusCheckTimeout();

      if (event.status === 'COMPLETED') {
        this.driverStatus = {
          ...this.driverStatus,
          checking: false,
          available: !!event.available,
          totalFileCount: Number.isFinite(event.totalFileCount) ? event.totalFileCount : this.driverStatus.totalFileCount,
          completedFileCount: Number.isFinite(event.completedFileCount) ? event.completedFileCount : this.driverStatus.completedFileCount,
          currentFilePercent: Number.isFinite(event.currentFilePercent) ? event.currentFilePercent : this.driverStatus.currentFilePercent,
          status: 'DOWNLOADING',
          retryAction: 'DOWNLOAD',
          message: event.message || '',
          resourceCoordinate: event.resourceCoordinate || this.driverStatus.resourceCoordinate,
          currentFileName: event.currentFileName || this.driverStatus.currentFileName
        };
        this.refreshDriverStatus();
        return;
      }

      if (event.status === 'FAILED') {
        this.setDriverErrorStatus(event.message || this.$t('xia-zai-shi-bai'), 'DOWNLOAD');
        this.driverStatus = {
          ...this.driverStatus,
          totalFileCount: Number.isFinite(event.totalFileCount) ? event.totalFileCount : this.driverStatus.totalFileCount,
          completedFileCount: Number.isFinite(event.completedFileCount) ? event.completedFileCount : this.driverStatus.completedFileCount,
          currentFilePercent: Number.isFinite(event.currentFilePercent) ? event.currentFilePercent : this.driverStatus.currentFilePercent,
          resourceCoordinate: event.resourceCoordinate || this.driverStatus.resourceCoordinate,
          currentFileName: event.currentFileName || this.driverStatus.currentFileName
        };
        this.$Message.error(event.message || this.$t('xia-zai-shi-bai'));
        return;
      }

      this.driverStatus = {
        ...this.driverStatus,
        checking: false,
        available: !!event.available,
        totalFileCount: Number.isFinite(event.totalFileCount) ? event.totalFileCount : this.driverStatus.totalFileCount,
        completedFileCount: Number.isFinite(event.completedFileCount) ? event.completedFileCount : this.driverStatus.completedFileCount,
        currentFilePercent: Number.isFinite(event.currentFilePercent) ? event.currentFilePercent : this.driverStatus.currentFilePercent,
        status: event.status || 'DOWNLOADING',
        retryAction: 'DOWNLOAD',
        message: event.message || '',
        resourceCoordinate: event.resourceCoordinate || this.driverStatus.resourceCoordinate,
        currentFileName: event.currentFileName || this.driverStatus.currentFileName
      };
    },
    updateDsKvConfig(dsKvConfigs) {
      this.addDataSourceForm.dsKvConfigs = [...dsKvConfigs];
    },
    async getDefaultKVConfig() {
      const res = await this.$services.rdpDataSourceDsKvConfigDef({
        data: {
          dataSourceType: this.addDataSourceForm.type,
          deployEnvType: this.addDataSourceForm.instanceType
        }
      });

      if (res.success) {
        this.addDataSourceForm.dsKvConfigs = res.data;
        this.addDataSourceForm.dsKvConfigs.forEach((config) => {
          if (config.defaultValue && config.confValType === 'BOOLEAN') {
            config.formatValue = JSON.parse(config.defaultValue);
          }
        });
      }
    },
    async getOracleConnectType() {
      const res = await this.$services.rdpConstantDsDsConnectType();
      this.oracleConnectTypeList = [];
      if (res.success) {
        res.data.forEach((type) => {
          if (type.dataSourceType === this.addDataSourceForm.type) {
            this.oracleConnectTypeList.push(type);
          }
        });
        for (let i = 0; i < this.oracleConnectTypeList.length; i++) {
          const connectType = this.oracleConnectTypeList[i];
          if (connectType.defaultCheck) {
            this.addDataSourceForm.connectType = connectType.connectType;
          }
        }
      }
    },
    handleHostTypeChange(index, type) {
      const beforeType = this.addDataSourceForm.hostList[index].type;
      this.addDataSourceForm.host = '';
      this.addDataSourceForm.port = '';
      this.addDataSourceForm.publicHost = '';
      this.addDataSourceForm.publicPort = '';
      if (beforeType === type) {
        // console.log(beforeType);
        // const zeroType = this.hostList[0].type;
        // this.hostList[0].type = this.hostList[1].type;
        // this.hostList[1].type = zeroType;
        if (index === 0) {
          this.addDataSourceForm.hostList[1].type = type === 'public' ? 'private' : 'public';
        } else {
          this.addDataSourceForm.hostList[0].type = type === 'public' ? 'private' : 'public';
        }
      }
      this.syncQueryHostType();
    },
    syncTestConnectionHosts() {
      const { hostList } = this.addDataSourceForm;

      if (hostList[0].type === 'public') {
        this.addDataSourceForm.publicHost = hostList[0].host;
        this.addDataSourceForm.publicPort = hostList[0].port;
        this.addDataSourceForm.host = hostList[1].host;
        this.addDataSourceForm.port = hostList[1].port;
      } else {
        this.addDataSourceForm.publicHost = hostList[1].host;
        this.addDataSourceForm.publicPort = hostList[1].port;
        this.addDataSourceForm.host = hostList[0].host;
        this.addDataSourceForm.port = hostList[0].port;
      }
    },
    buildConnectDsPayload(clusterId) {
      const { account, password, type, securityType, connectType, dbName, noValidateDbName, instanceType, region, instanceDesc, envId } =
        this.addDataSourceForm;
      let privateHost = '';
      let publicHost = '';
      let defaultHost = '';

      this.addDataSourceForm.hostList.forEach((hostItem) => {
        if (!hostItem.display || !hostItem.host) {
          return;
        }

        const resolvedHost = this.separatePort(type) ? `${hostItem.host}:${hostItem.port}` : hostItem.host;
        if (hostItem.type === 'private') {
          privateHost = resolvedHost;
        } else {
          publicHost = resolvedHost;
        }

        if (!defaultHost && hostItem.display) {
          defaultHost = resolvedHost;
        }
      });

      if (!defaultHost) {
        defaultHost = publicHost || privateHost;
      }

      return {
        bindClusterId: clusterId,
        dataSourceType: type,
        deployEnvType: instanceType,
        privateHost,
        publicHost,
        defaultHost,
        region,
        instanceDesc,
        securityType,
        connectType,
        envId,
        driver: this.addDataSourceForm.driver,
        dsKvConfigs: this.addDataSourceForm.dsKvConfigs.map((config) => ({
          configName: config.configName,
          configValue:
            config.currentCount !== undefined && config.currentCount !== null && config.currentCount !== ''
              ? config.currentCount
              : config.defaultValue
        })),
        dsPropsJson: JSON.stringify({
          database: isDb2(type) || isHana(type) ? dbName : noValidateDbName,
          userName: account,
          password
        })
      };
    },
    async testConnection() {
      const clusterId = this.normalizeDriverClusterId(this.addDataSourceForm.queryClusterId);
      if (!clusterId) {
        this.$Message.warning(this.$t('bang-ding-ji-qun-bu-neng-wei-kong'));
        return;
      }

      this.testConnectionLoading = true;
      this.hasTestConnectionResult = false;
      this.testConnectionSuccess = false;
      this.testConnectionMessage = '';

      try {
        const res = await this.$services.dmDataSourceConnectDs({
          data: this.buildConnectDsPayload(clusterId)
        });

        this.hasTestConnectionResult = true;
        this.testConnectionSuccess = !!res.success;
        this.testConnectionMessage = res.success
          ? this.$t('ce-shi-lian-jie-cheng-gong')
          : res.msg || this.$t('lian-jie-shi-bai-qing-jian-cha-shu-ju-yuan-deng-ru-xin-xi');

        if (res.success) {
          this.$Message.success(this.testConnectionMessage);
        } else {
          this.$Message.error(this.testConnectionMessage);
        }
      } catch (error) {
        this.hasTestConnectionResult = true;
        this.testConnectionSuccess = false;
        this.testConnectionMessage = error?.message || this.$t('ce-shi-lian-jie-shi-bai');
        this.$Message.error(this.testConnectionMessage);
      } finally {
        this.testConnectionLoading = false;
      }
    },
    handleTestConnection() {
      this.$refs.addLocalDs.validate((val) => {
        if (val) {
          this.syncTestConnectionHosts();

          if (this.useInlineTestConnection) {
            this.testConnection();
            return;
          }

          this.showTestConnectionModal = true;
        }
      });
    },
    hideTestConnectionModal() {
      this.showTestConnectionModal = false;
    },
    separatePort,
    handleFileChange(e) {
      const files = e.target.files;

      if (files && files[0]) {
        const file = files[0];

        if (file.size > 1024 * 1024) {
          return false;
        }
        this.addDataSourceForm.securityFile = file;
        setTimeout(() => {
          this.$refs.addLocalDs.validateField('securityFile');
        }, 0);
      }
    },
    handleCaFileChange(e) {
      const files = e.target.files;

      if (files && files[0]) {
        const file = files[0];

        if (file.size > 1024 * 1024) {
          return false;
        }
        this.addDataSourceForm.securityFile = file;
        this.addDataSourceForm.caFile = file;
        setTimeout(() => {
          this.$refs.addLocalDs.validateField('caFile');
        }, 0);
      }
    },
    handleJsonFileChange(e) {
      const files = e.target.files;

      if (files && files[0]) {
        const file = files[0];

        if (file.size > 1024 * 1024) {
          return false;
        }
        this.addDataSourceForm.securityFile = file;
        this.addDataSourceForm.jsonFile = file;
        setTimeout(() => {
          this.$refs.addLocalDs.validateField('jsonFile');
        }, 0);
      }
    },
    handleKeystoreFileChange(e) {
      const files = e.target.files;

      if (files && files[0]) {
        const file = files[0];

        if (file.size > 1024 * 1024) {
          return false;
        }
        this.addDataSourceForm.securityFile = file;
        this.addDataSourceForm.keystoreFile = file;
        setTimeout(() => {
          this.$refs.addLocalDs.validateField('keystoreFile');
        }, 0);
      }
    },
    handleTlsKeystoreFileChange(e) {
      const files = e.target.files;

      if (files && files[0]) {
        const file = files[0];

        if (file.size > 1024 * 1024) {
          return false;
        }
        this.addDataSourceForm.tlsKeystoreFile = file;
        setTimeout(() => {
          this.$refs.addLocalDs.validateField('tlsKeystoreFile');
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
        this.addDataSourceForm.tlsTrustStoreFile = file;
        setTimeout(() => {
          this.$refs.addLocalDs.validateField('tlsTrustStoreFile');
        }, 0);
      }
    },
    handleKeyTabFileChange(e) {
      const files = e.target.files;

      if (files && files[0]) {
        const file = files[0];

        if (file.size > 1024 * 1024) {
          return false;
        }
        this.addDataSourceForm.secretFile = file;
      }
    },
    async listDataSourceTypes() {
      const res = await this.$services.rdpConstantListDsTypesByDeployType({ data: {} });
      if (res.success) {
        this.dataSourceTypes = Array.isArray(res.data)
          ? res.data.map((group) => (Array.isArray(group) ? group.filter(Boolean) : [])).filter((group) => group.length > 0)
          : [];
        if (!this.dataSourceTypes.length) {
          return;
        }
        const flatArray = this.dataSourceTypes.reduce((result, group) => result.concat(group), []);
        if (!flatArray.includes(this.addDataSourceForm.type)) {
          this.addDataSourceForm.type = this.dataSourceTypes[0][0];
        }
        this.addDataSourceForm.instanceType = this.getDeployTypeByDsName(this.addDataSourceForm.type);
        this.getSecurityType();
        this.getDefaultKVConfig();
      } else {
        this.dataSourceTypes = [];
      }
    },
    async listEnv() {
      this.loading = true;
      const data = {
        envName: null
      };
      const res = await this.$services.rdpDsEnvList({ data });
      if (res.success) {
        this.envData = res.data;
        if (res.data[0]) {
          this.addDataSourceForm.envId = res.data[0].id;
          this.clearFieldValidate('envId');
        }
      }
    },
    async listQueryBindCluster() {
      const res = await this.$services.dmDataSourceListDsBindCluster();
      if (res.success || res.code === '1') {
        this.queryClusterList = res.data;
        const availableCluster = this.queryClusterList.find((cluster) => cluster.runningCount > 0) || this.queryClusterList[0];
        if (availableCluster) {
          this.addDataSourceForm.queryClusterId = availableCluster.id;
          this.currentQueryCluster = availableCluster;
          this.clearFieldValidate('queryClusterId');
        }
      }
    },
    clearFieldValidate(field) {
      this.$nextTick(() => {
        if (this.$refs.addLocalDs) {
          this.$refs.addLocalDs.clearValidate(field);
        }
      });
    },
    handleEnvChange(value) {
      this.addDataSourceForm.envId = value;
      this.clearFieldValidate('envId');
    },
    handleChangeQueryCluster() {
      this.currentQueryCluster = this.queryClusterList.find((cluster) => cluster.id === this.addDataSourceForm.queryClusterId) || {};
      this.clearFieldValidate('queryClusterId');
    },
    syncQueryHostType() {
      this.addDataSourceForm.queryHostType = 'PUBLIC';
    },
    validateSelectStep(callback) {
      this.$refs.selectDsTypeForm.validate((valid) => {
        callback(valid);
      });
    },
    handleSelectDataSource() {
      Object.keys(this.checkList).map((key) => {
        if (this.checkList[key]) {
          store.state.rdsData.map((item) => {
            if (item.instanceId === key) {
              // this.addDataSourceForm.rdsList.push(item);
              store.state.addedRdsList.push(key);
              this.checkList[key] = false;
              this.checkList = { ...this.checkList };
              let hasPublic = false;
              let privateHost = '';
              let publicHost = '';

              item.netInfo.map((net) => {
                if (net.netIpType === 'VPC_Public' || net.netIpType === 'Classical_Public') {
                  if (net.complexHost) {
                    publicHost = net.connectionString;
                  } else {
                    publicHost = `${net.connectionString}:${net.port}`;
                  }
                  hasPublic = true;
                } else if (net.netIpType === 'VPC_Private' || net.netIpType === 'Classical_Private') {
                  if (net.complexHost) {
                    privateHost = net.connectionString;
                  } else {
                    privateHost = `${net.connectionString}:${net.port}`;
                  }
                }
                return null;
              });
              this.addDataSourceForm.rdsList.push({
                instanceId: item.instanceId,
                host: privateHost,
                privateHost,
                publicHost,
                instanceDesc: item.instanceDesc,
                hostType: hasPublic ? 'PUBLIC' : 'PRIVATE',
                dataSourceType: item.dataSourceType,
                password: '',
                account: '',
                securityType: this.addDataSourceForm.securityType,
                version: item.version
              });
            }
            return null;
          });
        }
        return null;
      });
      this.hasChecked = false;
      this.checkAll = false;
    },
    handleCancelDataSource() {
      this.needCancelList.map((item) => {
        this.addDataSourceForm.rdsList.map((rds, index) => {
          if (item.instanceId === rds.instanceId) {
            this.addDataSourceForm.rdsList.splice(index, 1);
            store.state.addedRdsList.map((r, i) => {
              if (r === rds.instanceId) {
                store.state.addedRdsList.splice(i, 1);
              }
              return null;
            });
          }
          return null;
        });
        return null;
      });
      this.addDataSourceForm.rdsList.push('');
      this.addDataSourceForm.rdsList.pop();
      this.checkAll = false;
      this.needCancelList = [];
    },
    handleSelectCancelList(selection) {
      this.needCancelList = selection;
    },
    handleSelectAllDs(checked) {
      this.filterData.forEach((ds) => {
        if (!ds._disabled) {
          this.checkList[ds.instanceId] = checked;
        }
      });
    },
    handleSelectRds() {
      let hasChecked = false;

      Object.keys(this.checkList).map((key) => {
        if (this.checkList[key]) {
          hasChecked = true;
        }
        return null;
      });
      this.hasChecked = hasChecked;
    },
    handleFilter() {
      this.page = 1;
      this.filterData = [];
      store.state.rdsData.map((item) => {
        if (item.instanceDesc.indexOf(this.searchKey) > -1 || item.instanceId.indexOf(this.searchKey) > -1) {
          this.filterData.push(item);
        }
        return null;
      });
      this.total = this.filterData.length;
      this.showData = this.filterData.slice((this.page - 1) * this.size, this.page * this.size);
    },
    handlePageChange(page) {
      this.page = page;
      this.showData = this.filterData.slice((this.page - 1) * this.size, this.page * this.size);
    },
    handleDataSourceChange() {
      this.addDataSourceForm.instanceType = this.getDeployTypeByDsName(this.addDataSourceForm.type);
      Object.keys(this.clearData).forEach((key) => {
        this.addDataSourceForm[key] = this.clearData[key];
      });
      this.getDefaultKVConfig();
      if (this.$refs.addLocalDs) {
        this.$refs.addLocalDs.resetFields();
      }
      if (this.addDataSourceForm.fetchType === 'MANUALLY_FILL') {
        this.addDataSourceForm.port = '';
        this.addDataSourceForm.publicPort = '';
        this.addDataSourceForm.dsKvConfigs = [];
        this.addDataSourceForm.hostList = [
          {
            type: 'public',
            display: true,
            host: '',
            port: ''
          },
          {
            type: 'public',
            display: false,
            host: '',
            port: ''
          }
        ];
        if (this.addDataSourceForm.type === 'MySQL') {
          this.addDataSourceForm.port = '3306';
          this.addDataSourceForm.publicPort = '3306';
          this.addDataSourceForm.hostList[0].port = '3306';
          this.addDataSourceForm.hostList[1].port = '3306';
        } else if (this.addDataSourceForm.type === 'PostgreSQL') {
          this.addDataSourceForm.port = '5432';
          this.addDataSourceForm.publicPort = '5432';
          this.addDataSourceForm.hostList[0].port = '5432';
          this.addDataSourceForm.hostList[1].port = '5432';
        } else if (this.addDataSourceForm.type === 'Greenplum') {
          this.addDataSourceForm.port = '5432';
          this.addDataSourceForm.publicPort = '5432';
          this.addDataSourceForm.hostList[0].port = '5432';
          this.addDataSourceForm.hostList[1].port = '5432';
        } else if (this.addDataSourceForm.type === 'Hive') {
          this.addDataSourceForm.port = '10000';
          this.addDataSourceForm.publicPort = '10000';
          this.addDataSourceForm.hostList[0].port = '10000';
          this.addDataSourceForm.hostList[1].port = '10000';
        } else if (this.addDataSourceForm.type === 'TiDB') {
          this.addDataSourceForm.port = '4000';
          this.addDataSourceForm.publicPort = '4000';
          this.addDataSourceForm.hostList[0].port = '4000';
          this.addDataSourceForm.hostList[1].port = '4000';
        } else if (this.addDataSourceForm.type === 'TDengine') {
          this.addDataSourceForm.port = '6041';
          this.addDataSourceForm.publicPort = '6041';
          this.addDataSourceForm.hostList[0].port = '6041';
          this.addDataSourceForm.hostList[1].port = '6041';
        } else if (this.addDataSourceForm.type === 'Oracle') {
          this.getOracleConnectType();
          this.addDataSourceForm.port = '1521';
          this.addDataSourceForm.publicPort = '1521';
          this.addDataSourceForm.hostList[0].port = '1521';
          this.addDataSourceForm.hostList[1].port = '1521';
        } else if (this.addDataSourceForm.type === 'ClickHouse') {
          this.getOracleConnectType();
        } else if (this.addDataSourceForm.type === 'OceanBase') {
          this.addDataSourceForm.port = '2881';
          this.addDataSourceForm.publicPort = '2881';
          this.addDataSourceForm.hostList[0].port = '2881';
          this.addDataSourceForm.hostList[1].port = '2881';
        } else if (isDb2(this.addDataSourceForm.type)) {
          this.addDataSourceForm.port = '50000';
          this.addDataSourceForm.publicPort = '50000';
          this.addDataSourceForm.hostList[0].port = '50000';
          this.addDataSourceForm.hostList[1].port = '50000';
        } else if (this.addDataSourceForm.type === 'GreptimeDB') {
          this.addDataSourceForm.port = '4002';
          this.addDataSourceForm.publicPort = '4002';
          this.addDataSourceForm.hostList[0].port = '4002';
          this.addDataSourceForm.hostList[1].port = '4002';
        }
        this.applyDriverFamilySelection(true);
      } else if (this.addDataSourceForm.type === 'Kafka') {
        this.addDataSourceForm.securityType = 'USER_PASSWD_WITH_TLS';
      } else {
        this.addDataSourceForm.securityType = this.securitySetting[0].securityType;
      }
      this.addDataSourceForm.account = '';
      this.addDataSourceForm.password = '';
      this.addDataSourceForm.dbName = '';
      this.addDataSourceForm.noValidateDbNam = '';
      this.getSecurityType();
      this.syncQueryHostType();
    },
    getSecurityType() {
      this.$services
        .rdpConstantDsSecurityOption({
          data: {
            deployEnvType: this.addDataSourceForm.instanceType,
            dataSourceType: this.addDataSourceForm.type,
            deployFetchType: 'MANUALLY_FILL'
          }
        })
        .then((res) => {
          if (res.success) {
            const securityOptions = Array.isArray(res.data?.securityOptions) ? res.data.securityOptions : [];
            this.securitySetting = securityOptions;
            this.setSecuritySetting(securityOptions);
            if (securityOptions.length) {
              const matchedSecurity = securityOptions.find((securityOption) => securityOption.securityType === this.addDataSourceForm.securityType);
              const defaultSecurity = securityOptions.find((securityOption) => securityOption.defaultCheck) || securityOptions[0];

              if (!matchedSecurity && defaultSecurity) {
                this.addDataSourceForm.securityType = defaultSecurity.securityType;
              }

              securityOptions.forEach((securityOption) => {
                if (securityOption.defaultCheck && defaultSecurity) {
                  this.addDataSourceForm.securityType = defaultSecurity.securityType;
                }
                if (typeof securityOption.defaultHost === 'string' && securityOption.defaultHost.length > 0) {
                  this.addDataSourceForm.hostList[0].host = securityOption.defaultHost;
                }
              });
            }
          }
        });
    },
    handleSecurityTypeChange() {
      this.addDataSourceForm.account = '';
      this.addDataSourceForm.password = '';
    },
    handleAddHost() {
      this.addDataSourceForm.hostList[1].display = true;
      this.syncQueryHostType();
    },
    handleRemoveHost(index) {
      this.addDataSourceForm.hostList[index].display = false;
      this.addDataSourceForm.hostList[index].host = '';
      this.addDataSourceForm.hostList[index].port = '';

      if (index === 0) {
        [this.addDataSourceForm.hostList[0], this.addDataSourceForm.hostList[1]] = [
          this.addDataSourceForm.hostList[1],
          this.addDataSourceForm.hostList[0]
        ];
      }
      this.syncQueryHostType();
    },
    getSecurity(type) {
      let security = {};

      this.securitySetting.map((item) => {
        if (item.securityType === type) {
          security = item;
        }
        return null;
      });
      return security;
    },
    needTestBeforeAddDsTypes() {
      this.$services.ccConstantNeedTestBeforeAddDsTypes().then((res) => {
        if (res.success) {
          this.canTestyDsList = res.data;
        }
      });
    },
    // 客户端CA证书
    handleClientCaFileChange(e) {
      const files = e.target.files;
      if (files && files[0]) {
        const file = files[0];
        if (file.size > 1024 * 1024) {
          return false;
        }
        this.addDataSourceForm.clientSecurityFile = file;
        setTimeout(() => {
          this.$refs.addLocalDs.validateField('clientSecurityFile');
        }, 0);
      }
    },
    // 客户端私钥文件
    handleClientKeyFileChange(e) {
      const files = e.target.files;
      if (files && files[0]) {
        const file = files[0];
        if (file.size > 1024 * 1024) {
          return false;
        }
        this.addDataSourceForm.clientSecretFile = file;
        setTimeout(() => {
          this.$refs.addLocalDs.validateField('clientSecretFile');
        }, 0);
      }
    }
  }
};
</script>
<style lang="less" scoped>
.add-datasource-step1 {
  padding: 20px;

  .ivu-alert-with-desc.ivu-alert-with-icon {
    margin-bottom: 0;
  }
}

.transfer-title {
  font-weight: 500;
  margin-bottom: 7px;
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
    display: flex;
    align-items: center;

    button {
      position: absolute;
      right: 10px;
      top: 10px;
    }
  }

  .transfer-left-footer {
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 48px;
    text-align: center;
    line-height: 48px;
    border-top: 1px solid #dadada;
    background: #ffffff;
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

.transfer-btns {
  width: 100%;
  text-align: center;
  vertical-align: middle;
  margin-top: 200px;
  /*line-height: 500px;*/
}

.datasource-setting-title {
  font-weight: 500;
  margin-bottom: 20px;
}

.datasource-setting-title-secondary {
  margin-top: 8px;
  padding-top: 24px;
  border-top: 1px solid #f0f0f0;
}

.add-datasource-form-stage {
  position: relative;
}

.driver-selection-row {
  display: inline-flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.driver-status-loading-icon {
  color: #52c41a;
  font-size: 18px;
}

.driver-status-progress-circle {
  flex: 0 0 auto;
  position: relative;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 34px;
  height: 34px;
  border-radius: 50%;
  background: conic-gradient(#52c41a var(--driver-progress-percent, 0%), rgba(82, 196, 26, 0.18) 0);
}

.driver-status-progress-circle::before {
  content: '';
  position: absolute;
  inset: 5px;
  border-radius: 50%;
  background: #f6ffed;
}

.driver-status-progress-circle-text {
  position: relative;
  z-index: 1;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 10px;
  font-weight: 600;
  line-height: 1;
  color: #135200;
}

.driver-status-icon-wrap {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 34px;
  min-width: 34px;
  height: 34px;
}

.driver-status-icon-wrap.is-clickable {
  cursor: pointer;
}

.driver-status-ready-icon {
  color: #52c41a;
  font-size: 20px;
}

.driver-status-unknown-icon,
.driver-status-warning-icon {
  color: #faad14;
  font-size: 18px;
}

.driver-status-error-icon {
  color: #f5222d;
  font-size: 18px;
}

.driver-status-line {
  margin-top: 10px;
  padding: 10px 12px;
  border: 1px solid #d9f7be;
  background: #f6ffed;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  gap: 6px;
  max-width: 760px;
}

.driver-status-line.is-checking,
.driver-status-line.is-ready,
.driver-status-line.is-downloading {
  border-color: #d9f7be;
  background: #f6ffed;
}

.driver-status-line.is-unknown,
.driver-status-line.is-unprepared {
  border-color: #ffe58f;
  background: #fffbe6;
}

.driver-status-line.is-error {
  border-color: #ffccc7;
  background: #fff2f0;
}

.driver-status-line-main {
  display: flex;
  align-items: center;
  gap: 12px;
  min-width: 0;
}

.driver-status-phase-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #52c41a;
  flex: 0 0 auto;
}

.driver-status-resource {
  color: #262626;
  font-size: 13px;
  font-weight: 500;
  flex: 1 1 auto;
  min-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.driver-status-progress-text {
  flex: 0 0 64px;
  text-align: right;
  font-size: 13px;
  font-weight: 500;
  color: #135200;
}

.driver-status-actions {
  flex: 0 0 auto;
  margin-left: auto;
}

.driver-status-line-detail {
  color: #434343;
  font-size: 12px;
  line-height: 18px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.driver-status-line-detail-message {
  color: #595959;
}

.driver-status-line.is-unknown .driver-status-progress-text,
.driver-status-line.is-unprepared .driver-status-progress-text {
  color: #ad6800;
}

.driver-status-line.is-error .driver-status-resource,
.driver-status-line.is-error .driver-status-progress-text,
.driver-status-line.is-error .driver-status-line-detail-message {
  color: #cf1322;
}

.host-type {
  padding: 12px 0;
}

.host-type-label {
  font-size: 12px;
  color: #333;
  background-color: #deefff;
  display: inline-block;
  //width: 16px;
  height: 16px;
  border-radius: 4px;
  text-align: center;
  line-height: 16px;
  margin-right: 4px;
}

.second-host-item {
  margin-top: 20px;
}

.selected-region {
  color: #333333;
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
      margin-bottom: 4px;
      height: 36px;
      line-height: 34px;
    }
  }

  .region-btn {
    width: 100%;
  }
}

.datasource-warp {
  label::before {
    content: '';
    display: none !important;
  }
}

.datasource-warp {
  label::after {
    content: '';
    display: none !important;
  }
}
</style>
