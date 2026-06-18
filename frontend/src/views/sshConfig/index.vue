<template>
  <div class="ssh-config-page">
    <aside class="ssh-config-list">
      <div class="ssh-config-list__searchbar">
        <Input v-model="search" clearable size="small" @on-enter="loadList" />
        <Tooltip :content="$t('shua-xin')" transfer>
          <Button type="text" :loading="loading" @click="loadList">
            <CustomIcon v-if="!loading" type="icon-v2-Refresh" size="16px" />
          </Button>
        </Tooltip>
      </div>
      <div class="ssh-config-list__body">
        <div v-if="!loading && rows.length === 0" class="ssh-config-empty-list">{{ $t('nothing-to-show') }}</div>
        <button
          v-for="row in rows"
          :key="row.id"
          type="button"
          class="ssh-config-item"
          :class="{ 'ssh-config-item--active': row.id === selectedId }"
          @click="selectRow(row)"
        >
          <span class="ssh-config-item__title">{{ row.name }}</span>
          <span class="ssh-config-item__meta">{{ formatEndpoint(row) }}</span>
        </button>
      </div>
    </aside>

    <main class="ssh-config-detail">
      <div class="ssh-config-detail__toolbar">
        <div class="ssh-config-detail__toolbar-title"></div>
        <div class="ssh-config-detail__actions">
          <Tooltip :content="$t('xin-jian')" transfer>
            <Button :disabled="!canWrite" @click="handleCreate">
              <CustomIcon type="icon-v2-add" size="16px" />
              <span>{{ $t('xin-jian') }}</span>
            </Button>
          </Tooltip>
          <Poptip confirm transfer :title="$t('que-ding-yao-shan-chu-gai-pei-zhi-ma')" @on-ok="handleDeleteSelected">
            <Button :disabled="!canWrite || !selectedId || isEditing">
              <Icon type="ios-remove" />
              <span>{{ $t('shan-chu') }}</span>
            </Button>
          </Poptip>
          <Tooltip :content="$t('fu-zhi')" transfer>
            <Button :disabled="!canWrite || !selectedId || isEditing" @click="handleCopySelected">
              <Icon type="ios-copy-outline" />
              <span>{{ $t('fu-zhi') }}</span>
            </Button>
          </Tooltip>
          <Tooltip :content="$t('bian-ji')" transfer>
            <Button :disabled="!canWrite || !selectedId || isEditing" @click="handleEditSelected">
              <CustomIcon type="icon-v2-EditSimple" size="16px" />
              <span>{{ $t('bian-ji') }}</span>
            </Button>
          </Tooltip>
          <Button v-if="isEditing" @click="handleCancelEdit">{{ $t('qu-xiao') }}</Button>
          <Button type="primary" v-if="isEditing" :loading="saving" @click="handleSave">{{ $t('bao-cun') }}</Button>
        </div>
      </div>

      <div v-if="!isEditing && !selectedId" class="ssh-config-empty-detail" @click="canWrite && handleCreate()">
        {{ $t('add-ssh-configuration') }}
      </div>

      <template v-else>
        <Form :model="form" :label-width="150" class="ssh-config-form">
          <div class="ssh-basic-settings">
            <div class="ssh-basic-settings__title">{{ $t('ji-chu-pei-zhi') }}</div>
            <FormItem :label="$t('ming-cheng')">
              <Input v-model="form.name" :disabled="formReadonly" />
            </FormItem>
            <div class="ssh-host-port-row">
              <FormItem label="Host">
                <Input v-model="form.host" :disabled="formReadonly" />
              </FormItem>
              <FormItem :label="$t('duan-kou')">
                <InputNumber v-model="form.port" :min="1" :max="65535" :disabled="formReadonly" style="width: 100%" />
              </FormItem>
            </div>
            <FormItem :label="$t('yong-hu-ming')">
              <Input v-model="form.username" :disabled="formReadonly" />
            </FormItem>
            <FormItem :label="$t('ren-zheng-fang-shi')">
              <Select v-model="form.authType" :disabled="formReadonly" @on-change="handleAuthTypeChange">
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
                :disabled="formReadonly"
                @input="markSecretTouched('password')"
              />
            </FormItem>
            <template v-if="form.authType === 'PRIVATE_KEY'">
              <FormItem :label="$t('private-key-data')">
                <textarea
                  class="ssh-private-key-textarea"
                  v-model="form.privateKeyData"
                  :placeholder="secretPlaceholder(detail.privateKeyDataConfigured)"
                  :disabled="formReadonly"
                  @input="markSecretTouched('privateKeyData')"
                ></textarea>
              </FormItem>
              <FormItem :label="$t('private-key-passphrase')">
                <Input
                  v-model="form.privateKeyPassphrase"
                  type="password"
                  password
                  :placeholder="secretPlaceholder(detail.privateKeyPassphraseConfigured)"
                  :disabled="formReadonly"
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
                <Checkbox v-model="form.keepAliveEnabled" :disabled="formReadonly">{{ $t('send-keep-alive-messages-every') }}</Checkbox>
                <InputNumber
                  v-model="serverAliveIntervalSeconds"
                  :min="1"
                  :disabled="formReadonly || !form.keepAliveEnabled"
                  class="ssh-feature-row__number"
                />
                <span class="ssh-feature-row__suffix">{{ $t('seconds') }}</span>
              </div>
              <div class="ssh-feature-row" :class="{ 'ssh-feature-row--error': showKnownHostsUpdateAction }">
                <Checkbox v-model="form.strictChecking" :disabled="formReadonly">{{ $t('strict-host-key-checking') }}</Checkbox>
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
                <Checkbox v-model="form.connectTimeoutEnabled" :disabled="formReadonly">{{ $t('connect-timeout') }}</Checkbox>
                <InputNumber
                  v-model="form.connectTimeoutMs"
                  :min="1000"
                  :disabled="formReadonly || !form.connectTimeoutEnabled"
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
                  <Radio :label="'NO_PROXY'" :disabled="formReadonly">{{ $t('no-proxy') }}</Radio>
                  <Radio v-for="item in proxyOptions" :key="item" :label="item" :disabled="formReadonly">{{ item }}</Radio>
                </RadioGroup>
              </FormItem>
              <template v-if="form.proxyType !== 'NO_PROXY'">
                <div class="ssh-proxy-host-port-row">
                  <FormItem :label="$t('proxy-host')">
                    <Input v-model="form.proxyHost" :disabled="formReadonly" />
                  </FormItem>
                  <FormItem :label="$t('proxy-port')">
                    <InputNumber v-model="form.proxyPort" :min="1" :max="65535" :disabled="formReadonly" style="width: 100%" />
                  </FormItem>
                </div>
                <FormItem :label="$t('ren-zheng-fang-shi')">
                  <Select v-model="form.proxySecurityType" :disabled="formReadonly">
                    <Option value="NONE">{{ $t('none-auth') }}</Option>
                    <Option value="USER_PASSWD">{{ $t('user-password') }}</Option>
                  </Select>
                </FormItem>
                <FormItem v-if="form.proxySecurityType === 'USER_PASSWD'" :label="$t('proxy-user')">
                  <Input v-model="form.proxyUsername" :disabled="formReadonly" />
                </FormItem>
                <FormItem v-if="form.proxySecurityType === 'USER_PASSWD'" :label="$t('proxy-password')">
                  <Input
                    v-model="form.proxyPassword"
                    type="password"
                    password
                    :placeholder="secretPlaceholder(detail.proxyPasswordConfigured)"
                    :disabled="formReadonly"
                    @input="markSecretTouched('proxyPassword')"
                  />
                </FormItem>
              </template>
            </div>
          </div>
          <div class="ssh-form-actions">
            <Button @click="handleModalTest" :loading="testing" :disabled="!selectedId && !isEditing">{{ $t('ce-shi-lian-jie') }}</Button>
            <span v-if="testErrorMessage" class="ssh-test-error">
              <Icon type="ios-close-circle" />
              <span>{{ testErrorMessage }}</span>
            </span>
          </div>
        </Form>
      </template>
    </main>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import { encryptMixin } from '@/mixins/encryptMixin';

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
      selectedId: null,
      isEditing: false,
      saving: false,
      testing: false,
      updatingKnownHosts: false,
      showKnownHostsUpdateAction: false,
      testErrorMessage: '',
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
    formReadonly() {
      return !this.isEditing;
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
    async loadList() {
      this.loading = true;
      try {
        const res = await this.$services.dmSshConfigList({ data: { search: this.search } });
        if (res.success) {
          this.rows = res.data || [];
          if (this.selectedId && !this.rows.some((row) => row.id === this.selectedId)) {
            this.resetSelection();
          }
        }
      } finally {
        this.loading = false;
      }
    },
    async selectRow(row) {
      if (this.isEditing && this.form.id !== row.id) {
        this.isEditing = false;
      }
      await this.loadDetail(row.id, false);
    },
    async loadDetail(id, editable) {
      const res = await this.$services.dmSshConfigDetail({ data: { id } });
      if (!res.success) {
        return;
      }
      this.selectedId = id;
      this.detail = {
        ...res.data,
        proxyPasswordConfigured: !!res.data?.proxyPasswordConfigured
      };
      this.form = this.detailToForm(res.data);
      this.resetSecretTouched(false);
      this.showKnownHostsUpdateAction = false;
      this.testErrorMessage = '';
      this.isEditing = editable;
    },
    handleCreate() {
      this.selectedId = null;
      this.detail = {};
      this.form = emptyForm();
      this.resetSecretTouched(true);
      this.showKnownHostsUpdateAction = false;
      this.testErrorMessage = '';
      this.conExpanded = false;
      this.proxyExpanded = false;
      this.isEditing = true;
    },
    handleEditSelected() {
      if (this.selectedId) {
        this.isEditing = true;
      }
    },
    async handleCopySelected() {
      if (!this.selectedId) {
        return;
      }
      const source = this.form;
      this.selectedId = null;
      this.detail = {};
      this.form = {
        ...source,
        id: null,
        name: source.name ? `${source.name} copy` : '',
        password: '',
        privateKeyData: '',
        privateKeyPassphrase: '',
        proxyPassword: ''
      };
      this.resetSecretTouched(false);
      this.showKnownHostsUpdateAction = false;
      this.testErrorMessage = '';
      this.isEditing = true;
    },
    async handleDeleteSelected() {
      if (!this.selectedId) {
        return;
      }
      const res = await this.$services.dmSshConfigDelete({ data: { id: this.selectedId } });
      if (res.success) {
        this.$Message.success(this.$t('shan-chu-cheng-gong'));
        this.resetSelection();
        await this.loadList();
      }
    },
    resetSelection() {
      this.selectedId = null;
      this.detail = {};
      this.form = emptyForm();
      this.resetSecretTouched(false);
      this.showKnownHostsUpdateAction = false;
      this.testErrorMessage = '';
      this.isEditing = false;
    },
    handleCancelEdit() {
      if (!this.form.id) {
        this.resetSelection();
        return;
      }
      this.loadDetail(this.form.id, false);
    },
    async handleModalTest() {
      this.testing = true;
      this.testErrorMessage = '';
      try {
        const data = {
          sshConfigId: this.form.id || this.selectedId,
          config: this.isEditing ? this.buildPayload() : null
        };
        const res = await this.$services.dmSshConfigTestConnection({ data });
        this.showTestResult(res);
      } finally {
        this.testing = false;
      }
    },
    async handleUpdateKnownHostsAndRetry() {
      this.updatingKnownHosts = true;
      try {
        const data = {
          sshConfigId: this.form.id || this.selectedId,
          config: this.buildPayload()
        };
        const res = await this.$services.dmSshConfigProbeKnownHosts({ data });
        if (!res.success) {
          this.testErrorMessage = res.message || this.$t('update-host-key-cache-failed');
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
          await this.loadList();
          const id = typeof res.data === 'object' ? res.data?.id || this.form.id : res.data || this.form.id;
          if (id) {
            await this.loadDetail(id, false);
          } else {
            this.resetSelection();
          }
        }
      } finally {
        this.saving = false;
      }
    },
    showTestResult(res) {
      if (res.success && res.data?.success) {
        this.showKnownHostsUpdateAction = false;
        this.testErrorMessage = '';
        this.$Message.success(res.data.message || this.$t('ce-shi-lian-jie-cheng-gong'));
        return;
      }
      const message = res.data?.message || res.message || this.$t('ce-shi-lian-jie-shi-bai');
      this.showKnownHostsUpdateAction = this.isEditing && this.canWrite && this.form.strictChecking && this.isHostKeyCheckError(message);
      this.testErrorMessage = message;
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
.ssh-config-page {
  height: calc(100vh - 116px);
  min-height: 620px;
  display: grid;
  grid-template-columns: 300px minmax(0, 1fr);
  border: 1px solid #e8eaec;
  background: #fff;
  overflow: hidden;
}

.ssh-config-list {
  min-width: 0;
  min-height: 0;
  border-right: 1px solid #e8eaec;
  display: flex;
  flex-direction: column;
  background: #fafafa;
  overflow: hidden;
}

.ssh-config-list__searchbar {
  height: 48px;
  padding: 8px 10px;
  display: flex;
  align-items: center;
  gap: 8px;
  border-bottom: 1px solid #e8eaec;
}

.ssh-config-list__searchbar :deep(.ivu-input-wrapper) {
  flex: 1;
  min-width: 0;
}

.ssh-config-list__body {
  position: relative;
  flex: 1;
  min-height: 0;
  overflow: auto;
  padding: 6px 8px 16px;
}

.ssh-config-empty-list,
.ssh-config-empty-detail {
  color: #8c8c8c;
  font-size: 18px;
  font-weight: 500;
}

.ssh-config-empty-list {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.ssh-config-item {
  width: 100%;
  border: 0;
  border-radius: 4px;
  background: transparent;
  color: #515a6e;
  cursor: pointer;
  display: block;
  text-align: left;
  padding: 8px 10px;
  margin-bottom: 4px;
  overflow: hidden;
}

.ssh-config-item:hover,
.ssh-config-item--active {
  background: #e8f2ff;
  color: #2d8cf0;
}

.ssh-config-item__title,
.ssh-config-item__meta {
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.ssh-config-item__title {
  font-size: 14px;
  font-weight: 500;
}

.ssh-config-item__meta {
  margin-top: 2px;
  font-size: 12px;
  color: #8c8c8c;
}

.ssh-config-detail {
  min-width: 0;
  min-height: 0;
  display: flex;
  flex-direction: column;
  background: #fff;
  overflow: hidden;
}

.ssh-config-detail__toolbar {
  height: 48px;
  flex: 0 0 48px;
  padding: 8px 24px;
  border-bottom: 1px solid #e8eaec;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.ssh-config-detail__toolbar-title {
  min-width: 0;
  flex: 1;
}

.ssh-config-detail__actions {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 8px;
  white-space: nowrap;
}

.ssh-config-empty-detail {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.ssh-config-form {
  flex: 1 1 auto;
  min-height: 0;
  overflow-y: auto;
  overflow-x: hidden;
  padding: 24px 28px 16px;
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

.ssh-private-key-textarea:disabled {
  color: #c5c8ce;
  background: #f3f3f3;
  cursor: not-allowed;
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

.ssh-form-actions {
  max-width: 900px;
  margin-top: 24px;
  padding-top: 16px;
  border-top: 1px solid #e8eaec;
  display: flex;
  align-items: center;
  gap: 12px;
  justify-content: flex-start;
}

.ssh-test-error {
  min-width: 0;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  color: #ed4014;
  line-height: 1.4;
}

@media (max-width: 980px) {
  .ssh-config-page {
    grid-template-columns: 240px minmax(0, 1fr);
  }

  .ssh-host-port-row,
  .ssh-proxy-host-port-row {
    grid-template-columns: 1fr;
  }
}
</style>
