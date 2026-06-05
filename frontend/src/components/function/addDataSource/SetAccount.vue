<template>
  <div class="add-datasource-step2" :class="{ 'is-dark': currentTheme === 'dark' }">
    <p class="add-datasouce-title">{{ $t('shu-ju-yuan-xin-xi') }}</p>
    <p class="add-datasource-basic">
      <span class="add-datasource-basic-label">{{ $t('bu-shu-lei-xing') }}</span>
      <span class="add-datasource-basic-value">
        {{ Mapping.cloudOrIdcName[addDataSourceForm.instanceType] }}
      </span>
      <span class="add-datasource-basic-label">{{ $t('yuan-ku-lei-xing') }}</span>
      <span class="add-datasource-basic-value">
        {{ addDataSourceForm.instanceType === 'ALIBABA_CLOUD_HOSTED' ? Mapping.aliyunType[addDataSourceForm.type] : addDataSourceForm.type }}
      </span>
    </p>
    <p class="add-datasouce-title">{{ $t('chuang-jian-zhang-hao-mi-ma') }}</p>
    <Table border :columns="getColumn" :data="addDataSourceForm.rdsList">
      <template #securityType="{ index }">
        <a-select v-model:value="addDataSourceForm.rdsList[index].securityType" style="width: 160px" @change="handleUpdateAddForm">
          <a-select-option v-for="security in securitySetting" :key="security.securityType" :value="security.securityType">
            {{ Mapping.securityType[security.securityType] }}
          </a-select-option>
        </a-select>
      </template>
      <template #TrustStore="{ index }">
        <div class="file-item" v-if="getSecurity(addDataSourceForm.rdsList[index].securityType).needTlsTrustStoreFile">
          <span>{{ $t('truststore-wen-jian') }}</span>
          <Upload :before-upload="(file) => handleUploadFile(index, file, 'tlsTrustStoreFile')" :show-upload-list="false" action="#">
            <Button size="small" icon="ios-cloud-upload-outline">
              {{
                addDataSourceForm.rdsList[index].tlsTrustStoreFile ? addDataSourceForm.rdsList[index].tlsTrustStoreFile.name : $t('xuan-ze-wen-jian')
              }}
            </Button>
          </Upload>
        </div>
        <div class="file-item" v-if="getSecurity(addDataSourceForm.rdsList[index].securityType).needTlsTrustStoreFilePassword">
          <span>{{ $t('truststore-wen-jian-mi-ma') }}</span>
          <Input size="small" v-model="addDataSourceForm.rdsList[index].tlsTrustStoreFilePassword" style="width: 200px" />
        </div>
        <div class="file-item" v-if="getSecurity(addDataSourceForm.rdsList[index].securityType).needTlsKeyStoreFile">
          <span>{{ $t('key-store-wen-jian') }}</span>
          <Upload :before-upload="(file) => handleUploadFile(index, file, 'tlsKeystoreFile')" :show-upload-list="false" action="#">
            <Button size="small" icon="ios-cloud-upload-outline">
              {{ addDataSourceForm.rdsList[index].tlsKeystoreFile ? addDataSourceForm.rdsList[index].tlsKeystoreFile.name : $t('xuan-ze-wen-jian') }}
            </Button>
          </Upload>
        </div>
        <div class="file-item" v-if="getSecurity(addDataSourceForm.rdsList[index].securityType).needTlsKeyStoreFilePassword">
          <span>{{ $t('key-store-mi-ma') }}</span>
          <Input size="small" v-model="addDataSourceForm.rdsList[index].tlsKeystoreFilePassword" style="width: 200px" />
        </div>
      </template>
      <template #aliyunAKSK="{ index }">
        <div class="file-item-aksk">
          <span>AK</span>
          <Input size="small" v-model="addDataSourceForm.rdsList[index].accessKey" style="width: 200px; margin-left: 6px" />
        </div>
        <div class="file-item-aksk">
          <span>SK</span>
          <Input size="small" v-model="addDataSourceForm.rdsList[index].secretKey" style="width: 200px; margin-left: 6px" password type="password" />
        </div>
      </template>
      <template #account="{ row, index }">
        <div class="account-form-container">
          <div
            v-if="
              getSecurity(addDataSourceForm.rdsList[index].securityType).needUserName &&
              !getSecurity(addDataSourceForm.rdsList[index].securityType).accountAliyunAkSk
            "
            class="form-item"
          >
            <span class="form-label">{{ $t('zhang-hao') }}</span>
            <div class="form-input">
              <Input :disabled="row.autoCreate" size="small" v-model="addDataSourceForm.rdsList[index].account" style="width: 160px" />
            </div>
          </div>
          <div
            v-if="
              getSecurity(addDataSourceForm.rdsList[index].securityType).needPassword &&
              !getSecurity(addDataSourceForm.rdsList[index].securityType).accountAliyunAkSk
            "
            class="form-item"
          >
            <span class="form-label">{{ $t('mi-ma') }}</span>
            <div class="form-input">
              <Input
                :disabled="row.autoCreate"
                size="small"
                v-model="addDataSourceForm.rdsList[index].password"
                style="width: 160px"
                password
                type="password"
              />
            </div>
          </div>
          <div v-if="getSecurity(addDataSourceForm.rdsList[index].securityType).accountAliyunAkSk">
            <div class="form-item">
              <span class="form-label">AK</span>
              <div class="form-input">
                <Input
                  :disabled="row.autoCreate"
                  size="small"
                  v-model="addDataSourceForm.rdsList[index].account"
                  style="width: 200px"
                  :placeholder="$t('qing-shu-ruali-yun-accesskey-id')"
                />
              </div>
            </div>
            <div class="form-item">
              <span class="form-label">SK</span>
              <div class="form-input">
                <Input
                  :disabled="row.autoCreate"
                  size="small"
                  v-model="addDataSourceForm.rdsList[index].password"
                  style="width: 200px"
                  password
                  type="password"
                  :placeholder="$t('qing-shu-ruali-yun-accesskey-secret')"
                />
              </div>
            </div>
          </div>
          <div v-if="getSecurity(addDataSourceForm.rdsList[index].securityType).needClientTrustStorePassword" class="form-item">
            <span class="form-label">{{ $t('ke-hu-duan-truststore-mi-ma') }}</span>
            <div class="form-input">
              <Input
                :disabled="row.autoCreate"
                size="small"
                v-model="addDataSourceForm.rdsList[index].clientTrustStorePassword"
                style="width: 200px"
                :placeholder="$t('qing-shu-ru-ke-hu-duan-truststore-mi-ma')"
              />
            </div>
          </div>
          <div v-if="getSecurity(addDataSourceForm.rdsList[index].securityType).needTlsFile" class="form-item">
            <span class="form-label">{{ $t('xuan-ze-wen-jian') }}</span>
            <div class="form-input">
              <Upload :before-upload="(file) => handleUploadTlsFile(index, file)" :show-upload-list="false" action="#">
                <Button size="small" icon="ios-cloud-upload-outline">
                  {{ addDataSourceForm.rdsList[index].securityFile ? addDataSourceForm.rdsList[index].securityFile.name : $t('xuan-ze-wen-jian') }}
                </Button>
              </Upload>
            </div>
          </div>
          <!--          <Checkbox-->
          <!--            v-if="getSecurity(addDataSourceForm.rdsList[index].securityType).hasAutoCreateAccountOption"-->
          <!--            style="margin-left: 4px" v-model="row.autoCreate"-->
          <!--            @on-change="handleChangeAuto(index,$event)">{{ $t('zi-dong-chuang-jian') }}-->
          <!--          </Checkbox>-->
        </div>
      </template>
      <template #File="{ row, index }">
        <div v-if="row.securityType === 'CA_CERTIFICATE'">
          <div class="file-item">
            <span>{{ $t('ca-zheng-shu') }}</span>
            <Upload :before-upload="(file) => handleUploadFile(index, file, 'caFile')" :show-upload-list="false" action="#">
              <Button size="small" icon="ios-cloud-upload-outline">
                {{ addDataSourceForm.rdsList[index].caFile ? addDataSourceForm.rdsList[index].caFile.name : $t('xuan-ze-wen-jian') }}
              </Button>
            </Upload>
          </div>
          <div class="file-item">
            <span>{{ $t('ke-hu-duan-ca-zheng-shu') }}</span>
            <Upload :before-upload="(file) => handleUploadFile(index, file, 'clientSecurityFile')" :show-upload-list="false" action="#">
              <Button size="small" icon="ios-cloud-upload-outline">
                {{
                  addDataSourceForm.rdsList[index].clientSecurityFile
                    ? addDataSourceForm.rdsList[index].clientSecurityFile.name
                    : $t('xuan-ze-wen-jian')
                }}
              </Button>
            </Upload>
          </div>
          <div class="file-item">
            <span>{{ $t('ke-hu-duan-si-yao-wen-jian') }}</span>
            <Upload :before-upload="(file) => handleUploadFile(index, file, 'clientSecretFile')" :show-upload-list="false" action="#">
              <Button size="small" icon="ios-cloud-upload-outline">
                {{
                  addDataSourceForm.rdsList[index].clientSecretFile ? addDataSourceForm.rdsList[index].clientSecretFile.name : $t('xuan-ze-wen-jian')
                }}
              </Button>
            </Upload>
          </div>
          <div class="file-item">
            <span>{{ $t('ssl-si-yao-mi-ma') }}</span>
            <Input size="small" v-model="addDataSourceForm.rdsList[index].secretFilePassword" style="width: 200px" password type="password" />
          </div>
        </div>
      </template>
    </Table>
  </div>
</template>
<script>
import Mapping from '@/views/util';
import DataSourceGroup from '@/views/dataSourceGroup.json';
import store from '@/store/index';
import { resolveComponent } from 'vue';

export default {
  props: {
    addDataSourceForm: Object,
    securitySetting: Array
  },
  created() {
    if (store.state.firstAddDataSource) {
      if (DataSourceGroup.mysql.indexOf(this.addDataSourceForm.type) > -1 || DataSourceGroup.drds.indexOf(this.addDataSourceForm.type) > -1) {
        this.addDataSourceForm.rdsList.map((rds) => {
          rds.autoCreate = false;
          return null;
        });
      } else if (DataSourceGroup.kafka.indexOf(this.addDataSourceForm.type) > -1) {
        this.addDataSourceForm.rdsList.map((rds) => {
          rds.noAccount = false;
          rds.securityType = this.addDataSourceForm.securityType;
          rds.clientTrustStorePassword = 'KafkaOnsClient';
          return null;
        });
      } else {
        this.addDataSourceForm.rdsList.map((rds) => {
          rds.noAccount = false;
          rds.securityType = this.addDataSourceForm.securityType;
          return null;
        });
      }
    }
    store.state.firstAddDataSource = false;
  },
  data() {
    return {
      store,
      DataSourceGroup,
      Mapping,
      dataSourceData: []
    };
  },
  computed: {
    getColumn() {
      const baseColumns = this.generateBaseColumns();
      if (this.isCaCertificateMode()) {
        return [...baseColumns, this.generateFileColumn(), this.generateAliyunAkSkColumn()];
      }
      if (this.isTlsMode()) {
        return [...baseColumns, this.generateAliyunAkSkColumn(), this.generateTlsColumn()];
      }
      if (this.getSecurity(this.addDataSourceForm.rdsList[0].securityType)?.needExtraAliyunAkSk) {
        return [...baseColumns, this.generateAliyunAkSkColumn()];
      }
      return baseColumns;
    },
    currentTheme() {
      return this.$store.state.theme || 'light';
    }
  },
  methods: {
    // base列
    generateBaseColumns() {
      return [
        {
          title: this.$t('shi-li-id'),
          key: 'instanceId',
          minWidth: 300,
          render: (h, params) => h('div', {}, `${params.row.instanceDesc}/${params.row.instanceId}`)
        },
        {
          title: this.$t('shu-ju-ku-ren-zheng-fang-shi'),
          slot: 'securityType',
          minWidth: 200
        },
        {
          title: this.$t('shu-ju-ku-zhang-hao-mi-ma'),
          key: '',
          slot: 'account',
          minWidth: 400,
          renderHeader: (h) =>
            h('div', [
              h(
                'span',
                {
                  style: {
                    fontFamily: 'PingFangSC-Medium',
                    fontWeight: '500'
                  }
                },
                this.$t('shu-ju-ku-zhang-hao-mi-ma')
              )
            ])
        }
      ];
    },

    // AK/SK列
    generateAliyunAkSkColumn() {
      return {
        title: this.$t('aksk-bi-tian-yong-yu-shu-ju-tong-bu-la-qu-bei-fen-de-ri-zhi-wen-jian'),
        slot: 'aliyunAKSK',
        minWidth: 330,
        renderHeader: (h) =>
          h('div', [
            h(
              'span',
              {
                style: {
                  fontFamily: 'PingFangSC-Medium',
                  fontWeight: '500'
                }
              },
              this.$t('a-li-yun-accesskey-idaccesskey-secret')
            ),
            h(
              resolveComponent('Tooltip'),
              {
                style: {
                  color: '#888888',
                  marginLeft: '8px',
                  fontWeight: 400
                },
                content: this.$t('bi-tian-yong-yu-shu-ju-tong-bu-la-qu-bei-fen-de-ri-zhi-wen-jian'),
                placement: 'right',
                transfer: true
              },
              [
                h(resolveComponent('Icon'), {
                  type: 'ios-help-circle-outline'
                })
              ]
            )
          ])
      };
    },

    // tls列
    generateTlsColumn() {
      return {
        title: 'Tls',
        key: '',
        slot: 'TrustStore',
        minWidth: 450
      };
    },

    // file列（CA证书）
    generateFileColumn() {
      return {
        title: this.$t('ca-zheng-shu'),
        key: '',
        slot: 'File',
        minWidth: 450
      };
    },

    isCaCertificateMode() {
      return this.addDataSourceForm.rdsList.length && this.addDataSourceForm.rdsList.some((item) => item.securityType === 'CA_CERTIFICATE');
    },
    isTlsMode() {
      return this.addDataSourceForm.rdsList.length && this.addDataSourceForm.rdsList.some((item) => item.securityType === 'USER_PASSWD_WITH_TLS');
    },
    handleChangeAuto(index, data) {
      this.addDataSourceForm.rdsList[index].autoCreate = data;
    },
    handleChangeNoAccount(index, data) {
      this.addDataSourceForm.rdsList[index].noAccount = data;
    },
    handleFileChange(index, e) {
      const files = e.target.files;

      if (files && files[0]) {
        const file = files[0];

        if (file.size > 1024 * 1024) {
          return false;
        }
        this.addDataSourceForm.rdsList[index].securityFile = file;
      }
    },
    handleCaFileChange(index, e, key) {
      const files = e.target.files;
      if (files && files[0]) {
        const file = files[0];
        if (file.size > 1024 * 1024) {
          return false;
        }
        this.addDataSourceForm.rdsList[index][key] = file;
      }
    },
    handleUploadFile(index, file, key) {
      if (file.size > 1024 * 1024) {
        this.$Message.error(this.$t('wen-jian-da-xiao-bu-neng-chao-guo-1mb'));
        return false;
      }
      this.addDataSourceForm.rdsList[index][key] = file;
      return false; // 阻止自动上传
    },
    handleUploadTlsFile(index, file) {
      if (file.size > 1024 * 1024) {
        this.$Message.error(this.$t('wen-jian-da-xiao-bu-neng-chao-guo-1mb'));
        return false;
      }
      this.addDataSourceForm.rdsList[index].securityFile = file;
      return false; // 阻止自动上传
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
    handleUpdateAddForm() {
      this.$emit('update:addDataSourceForm', { ...this.addDataSourceForm });
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
    }
  }
};
</script>
<style lang="less" scoped>
.add-datasource-step2 {
  .add-datasouce-title {
    height: 40px;
    line-height: 40px;
    background-color: #ececec;
    border-bottom: 1px solid #dadada;
    padding: 0 20px;
    font-weight: 500;
  }

  .add-datasource-basic {
    height: 40px;
    line-height: 40px;
    border-bottom: 1px solid #dadada;

    .add-datasource-basic-label {
      width: 10%;
      height: 39px;
      background-color: #f4f4f4;
      padding: 0 10px;
      text-align: right;
      display: inline-block;
      border-right: 1px solid #dadada;
    }

    .add-datasource-basic-value {
      width: 39.8%;
      padding: 0 10px;
      display: inline-block;
      border-right: 1px solid #dadada;
    }

    .add-datasource-basic-value:last-child {
      border-right: none;
    }
  }
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

.file-item {
  display: flex;
  margin: 8px 0;

  span {
    display: inline-block;
    text-align: right;
    margin-right: 15px;
    width: 140px;
  }
}

.file-item-aksk {
  margin: 10px 0;

  span {
    display: inline-block;
    text-align: right;
    margin-right: 15px;
    width: 20px;
  }
}

.account-form-container {
  .form-item {
    display: flex;
    align-items: center;
    margin: 8px 0;

    .form-label {
      display: inline-block;
      text-align: right;
      margin-right: 15px;
      width: 130px;
      flex-shrink: 0;
    }

    .form-input {
      flex: 1;
    }
  }
}

// 暗色模式覆盖
&.is-dark {
  .add-datasouce-title {
    background-color: var(--bg-tertiary);
  }

  .add-datasource-basic-label {
    background-color: var(--bg-secondary) !important;
  }
}
</style>
