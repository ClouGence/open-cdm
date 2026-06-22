<template>
  <div class="ssh-config-account">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="content">
          <div class="option">
            <div class="left">
              <Input
                v-model="search"
                clearable
                style="width: 280px"
                :placeholder="$t('shu-ru-guan-jian-zi-jin-hang-mo-hu-sou-suo')"
                @on-enter="loadList"
                @on-clear="handleQueryClear"
              />
              <Button type="primary" ghost :loading="loading" @click="loadList">{{ $t('cha-xun') }}</Button>
            </div>
            <div class="right">
              <Button v-if="canWrite" type="primary" icon="md-add" @click="handleCreate">{{ $t('xin-jian') }}</Button>
              <Button @click="loadList" :loading="loading">
                <CustomIcon v-if="!loading" type="icon-v2-Refresh" />
              </Button>
            </div>
          </div>

          <div class="table-container">
            <Table
              border
              stripe
              size="small"
              :columns="sshConfigColumns"
              :data="rows"
              :loading="loading"
              :locale="{ emptyText: $t('zan-wu-shu-ju') }"
            >
              <template #authType="{ row }">
                {{ authTypeLabel(row.authType) }}
              </template>
              <template #endpoint="{ row }">
                {{ formatEndpoint(row) }}
              </template>
              <template #action="{ row }">
                <div class="ssh-config-actions" @click.stop>
                  <Button type="text" size="small" @click="handleEdit(row)">{{ $t('bian-ji') }}</Button>
                  <Button type="text" size="small" @click="handleCopy(row)">{{ $t('fu-zhi') }}</Button>
                  <Poptip confirm transfer :title="$t('que-ding-yao-shan-chu-gai-pei-zhi-ma')" @on-ok="handleDelete(row)">
                    <Button type="text" size="small">{{ $t('shan-chu') }}</Button>
                  </Poptip>
                </div>
              </template>
            </Table>
          </div>
        </div>
      </div>
    </div>

    <CCModal :title="modalTitle" v-model="showConfigModal" :width="760" @on-cancel="handleCloseModal">
      <Form :model="form" :label-width="150" class="ssh-config-form">
        <div class="ssh-basic-settings">
          <div class="ssh-basic-settings__title">{{ $t('ji-chu-pei-zhi') }}</div>
          <FormItem :label="$t('ming-cheng')">
            <Input v-model="form.name" />
          </FormItem>
          <div class="ssh-host-port-row">
            <FormItem label="Host">
              <Input v-model="form.host" />
            </FormItem>
            <FormItem :label="$t('duan-kou')">
              <InputNumber v-model="form.port" :min="1" :max="65535" style="width: 100%" />
            </FormItem>
          </div>
          <FormItem :label="$t('yong-hu-ming')">
            <Input v-model="form.username" />
          </FormItem>
          <FormItem :label="$t('ren-zheng-fang-shi')">
            <Select v-model="form.authType" @on-change="handleAuthTypeChange">
              <Option value="PASSWORD">{{ $t('mi-ma') }}</Option>
              <Option value="PRIVATE_KEY">{{ $t('key-pair') }}</Option>
            </Select>
          </FormItem>
          <FormItem v-if="form.authType === 'PASSWORD'" :label="$t('mi-ma')">
            <Input
              v-model="form.password"
              type="password"
              password
              :placeholder="secretPlaceholder(detail.passwordConfigured)"
              @input="markSecretTouched('password')"
            />
          </FormItem>
          <template v-if="form.authType === 'PRIVATE_KEY'">
            <FormItem :label="$t('private-key-data')">
              <textarea
                class="ssh-private-key-textarea"
                v-model="form.privateKeyData"
                :placeholder="secretPlaceholder(detail.privateKeyDataConfigured)"
                @input="markSecretTouched('privateKeyData')"
              ></textarea>
            </FormItem>
            <FormItem :label="$t('private-key-passphrase')">
              <Input
                v-model="form.privateKeyPassphrase"
                type="password"
                password
                :placeholder="secretPlaceholder(detail.privateKeyPassphraseConfigured)"
                @input="markSecretTouched('privateKeyPassphrase')"
              />
            </FormItem>
          </template>
        </div>

        <div class="ssh-section">
          <button type="button" class="ssh-section__head" @click="conExpanded = !conExpanded">
            <Icon :type="conExpanded ? 'ios-arrow-down' : 'ios-arrow-forward'" />
            <span>{{ $t('lian-jie-can-shu') }}</span>
          </button>
          <div v-if="conExpanded" class="ssh-section__body ssh-feature-rows">
            <div class="ssh-feature-row">
              <Checkbox v-model="form.keepAliveEnabled">{{ $t('send-keep-alive-messages-every') }}</Checkbox>
              <InputNumber
                v-model="serverAliveIntervalSeconds"
                :min="1"
                :disabled="!form.keepAliveEnabled"
                class="ssh-feature-row__number"
              />
              <span class="ssh-feature-row__suffix">{{ $t('seconds') }}</span>
            </div>
            <div class="ssh-feature-row" :class="{ 'ssh-feature-row--error': showKnownHostsUpdateAction }">
              <Checkbox v-model="form.strictChecking">{{ $t('strict-host-key-checking') }}</Checkbox>
              <Button
                v-if="showKnownHostsUpdateAction"
                size="small"
                @click="handleUpdateKnownHostsAndRetry"
                :loading="updatingKnownHosts"
                :disabled="testing"
              >
                {{ $t('update-host-key-cache-and-retry') }}
              </Button>
            </div>
            <div class="ssh-feature-row">
              <Checkbox v-model="form.connectTimeoutEnabled">{{ $t('connect-timeout') }}</Checkbox>
              <InputNumber
                v-model="form.connectTimeoutMs"
                :min="1000"
                :disabled="!form.connectTimeoutEnabled"
                class="ssh-feature-row__number"
              />
              <span class="ssh-feature-row__suffix">{{ $t('milliseconds') }}</span>
            </div>
          </div>
        </div>

        <div class="ssh-section">
          <button type="button" class="ssh-section__head" @click="proxyExpanded = !proxyExpanded">
            <Icon :type="proxyExpanded ? 'ios-arrow-down' : 'ios-arrow-forward'" />
            <span>{{ $t('http-socks-proxy') }}</span>
          </button>
          <div v-if="proxyExpanded" class="ssh-section__body ssh-proxy-form">
            <FormItem :label="$t('proxy')">
              <RadioGroup v-model="form.proxyType" type="button" class="ssh-proxy-form__radio">
                <Radio :label="'NO_PROXY'">{{ $t('no-proxy') }}</Radio>
                <Radio v-for="item in proxyOptions" :key="item" :label="item">{{ item }}</Radio>
              </RadioGroup>
            </FormItem>
            <template v-if="form.proxyType !== 'NO_PROXY'">
              <div class="ssh-proxy-host-port-row">
                <FormItem :label="$t('proxy-host')">
                  <Input v-model="form.proxyHost" />
                </FormItem>
                <FormItem :label="$t('proxy-port')">
                  <InputNumber v-model="form.proxyPort" :min="1" :max="65535" style="width: 100%" />
                </FormItem>
              </div>
              <FormItem :label="$t('ren-zheng-fang-shi')">
                <Select v-model="form.proxySecurityType">
                  <Option value="NONE">{{ $t('none-auth') }}</Option>
                  <Option value="USER_PASSWD">{{ $t('user-password') }}</Option>
                </Select>
              </FormItem>
              <FormItem v-if="form.proxySecurityType === 'USER_PASSWD'" :label="$t('proxy-user')">
                <Input v-model="form.proxyUsername" />
              </FormItem>
              <FormItem v-if="form.proxySecurityType === 'USER_PASSWD'" :label="$t('proxy-password')">
                <Input
                  v-model="form.proxyPassword"
                  type="password"
                  password
                  :placeholder="secretPlaceholder(detail.proxyPasswordConfigured)"
                  @input="markSecretTouched('proxyPassword')"
                />
              </FormItem>
            </template>
          </div>
        </div>
      </Form>
      <template #footer>
        <Button @click="handleCloseModal">{{ $t('guan-bi') }}</Button>
        <Button @click="handleModalTest" :loading="testing">{{ $t('ce-shi-lian-jie') }}</Button>
        <Button type="primary" :loading="saving" @click="handleSave">{{ $t('bao-cun') }}</Button>
      </template>
    </CCModal>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import { encryptMixin } from '@/mixins/encryptMixin';
import Toast from '@/utils/toast';

const emptyForm = () => ({
  id: null,
  name: '',
  host: '',
  port: 22,
  username: '',
  authType: 'PASSWORD',
  password: '',
  privateKeyData: '',
  privateKeyPassphrase: '',
  keepAliveEnabled: false,
  serverAliveIntervalMs: 300000,
  strictChecking: false,
  knownHosts: [],
  connectTimeoutEnabled: true,
  connectTimeoutMs: 30000,
  proxyType: 'NO_PROXY',
  proxyHost: '',
  proxyPort: null,
  proxySecurityType: 'NONE',
  proxyUsername: '',
  proxyPassword: ''
});

export default {
  name: 'SshConfigList',
  mixins: [encryptMixin],
  data() {
    return {
      search: '',
      rows: [],
      loading: false,
      showConfigModal: false,
      saving: false,
      testing: false,
      updatingKnownHosts: false,
      showKnownHostsUpdateAction: false,
      conExpanded: false,
      proxyExpanded: false,
      form: emptyForm(),
      detail: {},
      secretTouched: {
        password: false,
        privateKeyData: false,
        privateKeyPassphrase: false,
        proxyPassword: false
      },
      proxyOptions: ['HTTP', 'SOCKS4', 'SOCKS5']
    };
  },
  computed: {
    ...mapState(['myAuth']),
    canWrite() {
      return this.myAuth.includes('DM_SSH_CHANNEL_WRITE');
    },
    modalTitle() {
      if (this.form.id) {
        return this.$t('bian-ji-ssh-tong-dao');
      }
      return this.$t('xin-jian-ssh-tong-dao');
    },
    sshConfigColumns() {
      const columns = [
        { title: this.$t('ming-cheng'), key: 'name', minWidth: 160, ellipsis: true },
        { title: 'Host', key: 'host', minWidth: 180, ellipsis: true },
        { title: this.$t('duan-kou'), key: 'port', width: 90, align: 'center' },
        { title: this.$t('yong-hu-ming'), key: 'username', minWidth: 140, ellipsis: true },
        { title: this.$t('ren-zheng-fang-shi'), slot: 'authType', key: 'authType', width: 120 },
        { title: this.$t('lian-jie-di-zhi'), slot: 'endpoint', key: 'endpoint', minWidth: 220, ellipsis: true }
      ];
      if (this.canWrite) {
        columns.push({
          title: this.$t('cao-zuo'),
          slot: 'action',
          key: 'action',
          width: 180,
          fixed: 'right'
        });
      }
      return columns;
    },
    serverAliveIntervalSeconds: {
      get() {
        return Math.max(1, Math.floor((this.form.serverAliveIntervalMs || 300000) / 1000));
      },
      set(value) {
        this.form.serverAliveIntervalMs = (value || 1) * 1000;
      }
    }
  },
  mounted() {
    this.loadList();
  },
  methods: {
    handleQueryClear() {
      this.search = '';
      this.loadList();
    },
    authTypeLabel(authType) {
      if (authType === 'PRIVATE_KEY') {
        return this.$t('key-pair');
      }
      return this.$t('mi-ma');
    },
    async loadList() {
      this.loading = true;
      try {
        const res = await this.$services.dmSshConfigList({ data: { search: this.search } });
        if (res.success) {
          this.rows = res.data || [];
        }
      } finally {
        this.loading = false;
      }
    },
    resetModalState() {
      this.detail = {};
      this.form = emptyForm();
      this.resetSecretTouched(false);
      this.showKnownHostsUpdateAction = false;
      this.conExpanded = false;
      this.proxyExpanded = false;
    },
    handleCloseModal() {
      this.showConfigModal = false;
      this.resetModalState();
    },
    handleCreate() {
      this.resetModalState();
      this.resetSecretTouched(true);
      this.showConfigModal = true;
    },
    async openModalWithDetail(id) {
      const res = await this.$services.dmSshConfigDetail({ data: { id } });
      if (!res.success) {
        return false;
      }
      this.detail = {
        ...res.data,
        proxyPasswordConfigured: !!res.data?.proxyPasswordConfigured
      };
      this.form = this.detailToForm(res.data);
      this.resetSecretTouched(false);
      this.showKnownHostsUpdateAction = false;
      this.conExpanded = false;
      this.proxyExpanded = false;
      this.showConfigModal = true;
      return true;
    },
    async handleEdit(row) {
      await this.openModalWithDetail(row.id);
    },
    async handleCopy(row) {
      const opened = await this.openModalWithDetail(row.id);
      if (!opened) {
        return;
      }
      const source = this.form;
      this.form = {
        ...source,
        id: null,
        name: source.name ? `${source.name} copy` : '',
        password: '',
        privateKeyData: '',
        privateKeyPassphrase: '',
        proxyPassword: ''
      };
      this.detail = {};
      this.resetSecretTouched(false);
    },
    async handleDelete(row) {
      const res = await this.$services.dmSshConfigDelete({ data: { id: row.id } });
      if (res.success) {
        this.$Message.success(this.$t('shan-chu-cheng-gong'));
        if (this.showConfigModal && this.form.id === row.id) {
          this.handleCloseModal();
        }
        await this.loadList();
      }
    },
    async handleModalTest() {
      this.testing = true;
      try {
        const data = {
          sshConfigId: this.form.id,
          config: this.buildPayload()
        };
        const res = await this.$services.dmSshConfigTestConnection({ data, modal: false });
        this.showTestResult(res);
      } finally {
        this.testing = false;
      }
    },
    async handleUpdateKnownHostsAndRetry() {
      this.updatingKnownHosts = true;
      try {
        const data = {
          sshConfigId: this.form.id,
          config: this.buildPayload()
        };
        const res = await this.$services.dmSshConfigProbeKnownHosts({ data, modal: false });
        if (!res.success) {
          Toast.error(res.msg || res.message || this.$t('update-host-key-cache-failed'));
          return;
        }
        this.form.knownHosts = res.data || [];
        this.showKnownHostsUpdateAction = false;
        await this.handleModalTest();
      } finally {
        this.updatingKnownHosts = false;
      }
    },
    async handleSave() {
      this.saving = true;
      try {
        const payload = this.buildPayload();
        const apiName = this.form.id ? 'dmSshConfigUpdate' : 'dmSshConfigCreate';
        const res = await this.$services[apiName]({ data: payload });
        if (res.success) {
          this.$Message.success(this.$t('bao-cun-cheng-gong'));
          this.handleCloseModal();
          await this.loadList();
        }
      } finally {
        this.saving = false;
      }
    },
    showTestResult(res) {
      if (res.success && res.data?.success) {
        this.showKnownHostsUpdateAction = false;
        Toast.success(res.data.message || this.$t('ce-shi-lian-jie-cheng-gong'));
        return;
      }
      const message = this.resolveTestMessage(res);
      this.showKnownHostsUpdateAction = this.canWrite && this.form.strictChecking && this.isHostKeyCheckError(message);
      Toast.error(message);
    },
    resolveTestMessage(res) {
      return res?.data?.message || res?.msg || res?.message || this.$t('ce-shi-lian-jie-shi-bai');
    },
    isHostKeyCheckError(message) {
      return /reject HostKey|HostKey has been changed|UnknownHostKey|host key/i.test(message || '');
    },
    detailToForm(data) {
      const conFeatures = data.conFeatures || {};
      const proxyFeatures = data.proxyFeatures || {};
      const hostKey = conFeatures.hostKey || {};
      return {
        ...emptyForm(),
        id: data.id,
        name: data.name,
        host: data.host,
        port: data.port || 22,
        username: data.username,
        authType: data.authType || 'PASSWORD',
        keepAliveEnabled: !!conFeatures.keepAliveEnabled,
        serverAliveIntervalMs: conFeatures.serverAliveIntervalMs || 300000,
        strictChecking: !!(hostKey.strictChecking ?? conFeatures.strictHostKeyChecking),
        knownHosts: conFeatures.knownHosts || [],
        connectTimeoutEnabled: !!conFeatures.connectTimeoutMs,
        connectTimeoutMs: conFeatures.connectTimeoutMs || 30000,
        proxyType: data.proxyType || 'NO_PROXY',
        proxyHost: proxyFeatures.host || '',
        proxyPort: proxyFeatures.port || null,
        proxySecurityType: proxyFeatures.securityType === 'USER_PASSWD' ? 'USER_PASSWD' : 'NONE',
        proxyUsername: proxyFeatures.username || '',
        proxyPassword: ''
      };
    },
    buildPayload() {
      const payload = {
        id: this.form.id,
        name: this.form.name,
        host: this.form.host,
        port: this.form.port,
        username: this.form.username,
        authType: this.form.authType,
        conFeatures: {
          keepAliveEnabled: this.form.keepAliveEnabled,
          serverAliveIntervalMs: this.form.keepAliveEnabled ? this.form.serverAliveIntervalMs : 0,
          connectTimeoutMs: this.form.connectTimeoutEnabled ? this.form.connectTimeoutMs : 0,
          hostKey: {
            strictChecking: this.form.strictChecking
          },
          knownHosts: this.form.knownHosts || []
        },
        proxyType: this.form.proxyType,
        proxyFeatures: {}
      };
      if (this.form.authType === 'PASSWORD') {
        this.fillSecret(payload, 'password');
      }
      if (this.form.authType === 'PRIVATE_KEY') {
        if (this.secretTouched.privateKeyData) {
          payload.privateKeyData = this.form.privateKeyData;
        }
        this.fillSecret(payload, 'privateKeyPassphrase');
      }
      if (this.form.proxyType !== 'NO_PROXY') {
        payload.proxyFeatures = {
          host: this.form.proxyHost,
          port: this.form.proxyPort,
          securityType: this.form.proxySecurityType
        };
        if (this.form.proxySecurityType === 'USER_PASSWD') {
          payload.proxyFeatures.username = this.form.proxyUsername;
        }
        if (this.form.proxySecurityType === 'USER_PASSWD') {
          this.fillSecret(payload.proxyFeatures, 'proxyPassword', 'password');
        }
      }
      return payload;
    },
    handleAuthTypeChange() {
      this.form.password = '';
      this.form.privateKeyData = '';
      this.form.privateKeyPassphrase = '';
      this.secretTouched.password = true;
      this.secretTouched.privateKeyData = true;
      this.secretTouched.privateKeyPassphrase = true;
    },
    markSecretTouched(field) {
      this.secretTouched[field] = true;
    },
    resetSecretTouched(value) {
      this.secretTouched = {
        password: value,
        privateKeyData: value,
        privateKeyPassphrase: value,
        proxyPassword: value
      };
    },
    fillSecret(payload, formField, payloadField = formField) {
      if (!this.secretTouched[formField]) {
        return;
      }
      payload[payloadField] = this.encryptSecret(this.form[formField]);
    },
    encryptSecret(value) {
      return value === null || value === undefined ? null : this.passwordEncrypt(value);
    },
    secretPlaceholder(configured) {
      return configured ? this.$t('yi-pei-zhi-liu-kong-bao-chi-bu-bian') : '';
    },
    formatEndpoint(row) {
      const username = row.username || '<username>';
      const host = row.host || 'localhost';
      return `${username}@${host}:${row.port || 22}`;
    }
  }
};
</script>

<style lang="less" scoped>
.ssh-config-actions {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 4px;
}

.ssh-config-form {
  max-height: 62vh;
  overflow-y: auto;
  padding-right: 4px;
}

.ssh-basic-settings {
  max-width: 900px;
}

.ssh-private-key-textarea {
  width: 100%;
  min-height: 168px;
  padding: 6px 7px;
  border: 1px solid #dcdee2;
  border-radius: 4px;
  color: #515a6e;
  background: #fff;
  line-height: 1.5;
  resize: vertical;
  outline: none;
  transition: border-color 0.2s ease;
}

.ssh-private-key-textarea:focus {
  border-color: #57a3f3;
}

.ssh-basic-settings__title {
  height: 32px;
  color: #515a6e;
  font-size: 16px;
  font-weight: 500;
}

.ssh-host-port-row {
  display: grid;
  grid-template-columns: minmax(360px, 1fr) 220px;
  column-gap: 24px;
}

.ssh-section {
  margin-top: 12px;
}

.ssh-section__head {
  width: 100%;
  border: 0;
  border-bottom: 1px solid #e8eaec;
  background: transparent;
  display: flex;
  align-items: center;
  gap: 8px;
  height: 42px;
  color: #515a6e;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
}

.ssh-section__body {
  padding-top: 18px;
}

.ssh-feature-rows,
.ssh-proxy-form {
  padding-left: 38px;
}

.ssh-feature-row {
  min-height: 42px;
  display: flex;
  align-items: center;
  gap: 12px;
  color: #515a6e;
}

.ssh-feature-row--error,
.ssh-feature-row--error :deep(.ivu-checkbox-wrapper) {
  color: #ed4014;
}

.ssh-feature-row--error :deep(.ivu-btn) {
  color: #ed4014;
  border-color: #ed4014;
}

.ssh-feature-row__number {
  width: 92px;
}

.ssh-feature-row__suffix {
  color: #515a6e;
}

.ssh-proxy-form {
  max-width: 900px;
}

.ssh-proxy-form :deep(.ivu-form-item) {
  margin-bottom: 14px;
}

.ssh-proxy-form__radio {
  display: flex;
  flex-wrap: wrap;
  gap: 0;
}

.ssh-proxy-host-port-row {
  display: grid;
  grid-template-columns: minmax(360px, 1fr) 220px;
  column-gap: 24px;
}

@media (max-width: 980px) {
  .ssh-host-port-row,
  .ssh-proxy-host-port-row {
    grid-template-columns: 1fr;
  }
}
</style>
