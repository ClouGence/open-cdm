<template>
  <div class="ssh-config-page">
    <div class="ssh-config-toolbar">
      <Input v-model="search" clearable style="width: 280px" @on-enter="loadList" />
      <Button type="primary" ghost @click="loadList">{{ $t('cha-xun') }}</Button>
      <div class="ssh-config-toolbar__right">
        <Button type="primary" icon="md-add" v-if="canWrite" @click="handleCreate">{{ $t('xin-jian') }}</Button>
        <Button @click="loadList">
          <CustomIcon type="icon-v2-Refresh" />
        </Button>
      </div>
    </div>

    <Table border stripe size="small" :columns="columns" :data="rows" :loading="loading">
      <template #endpoint="{ row }">{{ row.username }}@{{ row.host }}:{{ row.port || 22 }}</template>
      <template #action="{ row }">
        <Button type="text" size="small" @click="handleEdit(row)">{{ $t('xiang-qing') }}</Button>
        <Button type="text" size="small" @click="handleTest(row)">{{ $t('ce-shi-lian-jie') }}</Button>
        <Button type="text" size="small" v-if="canWrite" @click="handleEdit(row, true)">{{ $t('bian-ji') }}</Button>
        <Poptip confirm transfer :title="$t('que-ding-yao-shan-chu-gai-pei-zhi-ma')" @on-ok="handleDelete(row)">
          <Button type="text" size="small" v-if="canWrite">{{ $t('shan-chu') }}</Button>
        </Poptip>
      </template>
    </Table>

    <Modal v-model="showModal" :title="modalTitle" width="860" :mask-closable="false">
      <Form :model="form" :label-width="150" class="ssh-config-form">
        <div class="form-grid">
          <FormItem :label="$t('ming-cheng')">
            <Input v-model="form.name" :disabled="readonly" />
          </FormItem>
          <FormItem label="Host">
            <Input v-model="form.host" :disabled="readonly" />
          </FormItem>
          <FormItem :label="$t('duan-kou')">
            <InputNumber v-model="form.port" :min="1" :max="65535" :disabled="readonly" style="width: 100%" />
          </FormItem>
          <FormItem :label="$t('yong-hu-ming')">
            <Input v-model="form.username" :disabled="readonly" />
          </FormItem>
          <FormItem :label="$t('ren-zheng-fang-shi')">
            <Select v-model="form.authType" :disabled="readonly">
              <Option value="PASSWORD">{{ $t('mi-ma') }}</Option>
              <Option value="PRIVATE_KEY">{{ $t('private-key') }}</Option>
            </Select>
          </FormItem>
          <FormItem :label="$t('mi-ma')">
            <Input
              v-model="form.password"
              type="password"
              password
              :placeholder="secretPlaceholder(detail.passwordConfigured)"
              :disabled="readonly"
              @input="markSecretTouched('password')"
            />
          </FormItem>
          <FormItem v-if="form.authType === 'PRIVATE_KEY'" :label="$t('private-key-data')">
            <Input
              v-model="form.privateKeyData"
              type="textarea"
              :rows="5"
              :placeholder="secretPlaceholder(detail.privateKeyDataConfigured)"
              :disabled="readonly"
              @input="markSecretTouched('privateKeyData')"
            />
          </FormItem>
          <FormItem v-if="form.authType === 'PRIVATE_KEY'" :label="$t('private-key-passphrase')">
            <Input
              v-model="form.privateKeyPassphrase"
              type="password"
              password
              :placeholder="secretPlaceholder(detail.privateKeyPassphraseConfigured)"
              :disabled="readonly"
              @input="markSecretTouched('privateKeyPassphrase')"
            />
          </FormItem>
        </div>

        <Divider orientation="left">{{ $t('lian-jie-can-shu') }}</Divider>
        <div class="form-grid">
          <FormItem :label="$t('bao-chi-lian-jie')">
            <Checkbox v-model="form.keepAliveEnabled" :disabled="readonly">{{ $t('qi-yong') }}</Checkbox>
          </FormItem>
          <FormItem :label="$t('bao-huo-jian-ge-ms')">
            <InputNumber v-model="form.serverAliveIntervalMs" :min="1000" :disabled="readonly || !form.keepAliveEnabled" style="width: 100%" />
          </FormItem>
          <FormItem :label="$t('zhu-ji-mi-yao-xiao-yan')">
            <Select v-model="form.strictChecking" :disabled="readonly">
              <Option :value="true">{{ $t('shi') }}</Option>
              <Option :value="false">{{ $t('fou') }}</Option>
            </Select>
          </FormItem>
          <FormItem :label="$t('lian-jie-chao-shi-ms')">
            <InputNumber v-model="form.connectTimeoutMs" :min="1000" :disabled="readonly" style="width: 100%" />
          </FormItem>
        </div>

        <Divider orientation="left">{{ proxyLabel }}</Divider>
        <div class="form-grid">
          <FormItem :label="proxyLabel">
            <Select v-model="form.proxyType" :disabled="readonly">
              <Option value="NO_PROXY">{{ $t('no-proxy') }}</Option>
              <Option v-for="item in proxyOptions" :key="item" :value="item">{{ item }}</Option>
            </Select>
          </FormItem>
          <template v-if="form.proxyType !== 'NO_PROXY'">
            <FormItem :label="$t('proxy-host')">
              <Input v-model="form.proxyHost" :disabled="readonly" />
            </FormItem>
            <FormItem :label="$t('proxy-port')">
              <InputNumber v-model="form.proxyPort" :min="1" :max="65535" :disabled="readonly" style="width: 100%" />
            </FormItem>
            <FormItem :label="$t('proxy-user')">
              <Input v-model="form.proxyUsername" :disabled="readonly" />
            </FormItem>
            <FormItem :label="$t('ren-zheng-fang-shi')">
              <Select v-model="form.proxySecurityType" :disabled="readonly">
                <Option value="ONLY_USER">{{ $t('only-user') }}</Option>
                <Option value="USER_PASSWD">{{ $t('user-password') }}</Option>
              </Select>
            </FormItem>
            <FormItem :label="$t('proxy-password')">
              <Input
                v-model="form.proxyPassword"
                type="password"
                password
                :placeholder="secretPlaceholder(detail.proxyPasswordConfigured)"
                :disabled="readonly || form.proxySecurityType !== 'USER_PASSWD'"
                @input="markSecretTouched('proxyPassword')"
              />
            </FormItem>
          </template>
        </div>
      </Form>

      <template #footer>
        <Button @click="handleModalTest" :loading="testing">{{ $t('ce-shi-lian-jie') }}</Button>
        <Button @click="showModal = false">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" v-if="!readonly" :loading="saving" @click="handleSave">{{ $t('bao-cun') }}</Button>
      </template>
    </Modal>
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
  connectTimeoutMs: 30000,
  proxyType: 'NO_PROXY',
  proxyHost: '',
  proxyPort: null,
  proxySecurityType: 'ONLY_USER',
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
      showModal: false,
      readonly: false,
      saving: false,
      testing: false,
      form: emptyForm(),
      detail: {},
      secretTouched: {
        password: false,
        privateKeyData: false,
        privateKeyPassphrase: false,
        proxyPassword: false
      },
      proxyLabel: 'Proxy',
      proxyOptions: ['HTTP', 'SOCKS4', 'SOCKS5'],
      columns: [
        { title: this.$t('ming-cheng'), key: 'name', minWidth: 180 },
        { title: this.$t('lian-jie-di-zhi'), slot: 'endpoint', minWidth: 240 },
        { title: this.$t('ren-zheng-fang-shi'), key: 'authType', minWidth: 140 },
        { title: 'Proxy', key: 'proxyType', minWidth: 140 },
        { title: this.$t('cao-zuo'), slot: 'action', width: 260, align: 'center' }
      ]
    };
  },
  computed: {
    ...mapState(['myAuth']),
    canWrite() {
      return this.myAuth.includes('DM_SSH_CHANNEL_WRITE');
    },
    modalTitle() {
      if (this.readonly) {
        return this.$t('ssh-tong-dao-xiang-qing');
      }
      return this.form.id ? this.$t('bian-ji-ssh-tong-dao') : this.$t('xin-jian-ssh-tong-dao');
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
        }
      } finally {
        this.loading = false;
      }
    },
    handleCreate() {
      this.readonly = false;
      this.detail = {};
      this.form = emptyForm();
      this.resetSecretTouched(true);
      this.showModal = true;
    },
    async handleEdit(row, editable = false) {
      const res = await this.$services.dmSshConfigDetail({ data: { id: row.id } });
      if (!res.success) {
        return;
      }
      this.detail = {
        ...res.data,
        proxyPasswordConfigured: !!res.data?.proxyPasswordConfigured
      };
      this.form = this.detailToForm(res.data);
      this.resetSecretTouched(false);
      this.readonly = !editable;
      this.showModal = true;
    },
    async handleDelete(row) {
      const res = await this.$services.dmSshConfigDelete({ data: { id: row.id } });
      if (res.success) {
        this.$Message.success(this.$t('shan-chu-cheng-gong'));
        this.loadList();
      }
    },
    async handleTest(row) {
      const res = await this.$services.dmSshConfigTestConnection({ data: { sshConfigId: row.id } });
      this.showTestResult(res);
    },
    async handleModalTest() {
      this.testing = true;
      try {
        const res = await this.$services.dmSshConfigTestConnection({
          data: {
            sshConfigId: this.form.id,
            config: this.buildPayload()
          }
        });
        this.showTestResult(res);
      } finally {
        this.testing = false;
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
          this.showModal = false;
          this.loadList();
        }
      } finally {
        this.saving = false;
      }
    },
    showTestResult(res) {
      if (res.success && res.data?.success) {
        this.$Message.success(res.data.message || this.$t('ce-shi-lian-jie-cheng-gong'));
        return;
      }
      const message = res.data?.message || res.message || this.$t('ce-shi-lian-jie-shi-bai');
      this.$Message.error(message);
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
        connectTimeoutMs: conFeatures.connectTimeoutMs || 30000,
        proxyType: data.proxyType || 'NO_PROXY',
        proxyHost: proxyFeatures.host || '',
        proxyPort: proxyFeatures.port || null,
        proxySecurityType: proxyFeatures.securityType || 'ONLY_USER',
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
          connectTimeoutMs: this.form.connectTimeoutMs,
          hostKey: {
            strictChecking: this.form.strictChecking
          }
        },
        proxyType: this.form.proxyType,
        proxyFeatures: {}
      };
      this.fillSecret(payload, 'password');
      this.fillSecret(payload, 'privateKeyData');
      this.fillSecret(payload, 'privateKeyPassphrase');
      if (this.form.proxyType !== 'NO_PROXY') {
        payload.proxyFeatures = {
          host: this.form.proxyHost,
          port: this.form.proxyPort,
          securityType: this.form.proxySecurityType,
          username: this.form.proxyUsername
        };
        this.fillSecret(payload.proxyFeatures, 'proxyPassword', 'password');
      }
      return payload;
    },
    markSecretTouched(field) {
      this.$set(this.secretTouched, field, true);
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
    }
  }
};
</script>

<style lang="less" scoped>
.ssh-config-page {
  padding: 8px;
}

.ssh-config-toolbar {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 16px;
}

.ssh-config-toolbar__right {
  margin-left: auto;
  display: flex;
  gap: 10px;
}

.ssh-config-form {
  max-height: 62vh;
  overflow: auto;
  padding-right: 8px;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(260px, 1fr));
  column-gap: 20px;
}

@media (max-width: 900px) {
  .form-grid {
    grid-template-columns: 1fr;
  }
}
</style>
