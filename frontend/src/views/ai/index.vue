<template>
  <div class="ai-settings table-list-layout">
    <div class="table-list">
      <div class="content">
        <div v-if="error" class="ai-notice" role="alert">
          <span>{{ error }}</span>
          <Button :loading="loading" @click="load">{{ $t('ai.retry') }}</Button>
        </div>
        <a-spin v-if="loading && !capabilities" />
        <p v-if="capabilities && !capabilities.canManageEngines">{{ $t('ai.errors.FORBIDDEN') }}</p>
        <template v-if="capabilities?.canManageEngines">
          <div class="option">
            <div class="left">
              <Input
                v-model="searchKeyword"
                class="ai-engine-search"
                clearable
                :maxlength="128"
                :placeholder="$t('ai.engineSearchPlaceholder')"
                :aria-label="$t('ai.engineSearchPlaceholder')"
                @on-enter="search"
                @on-clear="search"
              />
              <Button type="primary" ghost :loading="loading" @click="search">{{ $t('cha-xun') }}</Button>
            </div>
            <div class="right">
              <Button type="primary" @click="editEngine()">{{ $t('xin-zeng') }}</Button>
            </div>
          </div>
          <div class="table-container">
            <Table :columns="columns" :data="items" :loading="loading" row-key="id" :scroll="{ x: 900 }" size="small">
              <template #engineActions="{ row }">
                <div class="ai-actions">
                  <Button type="text" size="small" :disabled="!!busyId" @click="editEngine(row)">{{ $t('ai.edit') }}</Button>
                  <Button type="text" size="small" :loading="busyId === row.id && !confirming" :disabled="!!busyId" @click="testEngine(row)">
                    {{ $t('ai.test') }}
                  </Button>
                  <Button type="text" size="small" danger :disabled="!!busyId" @click="confirmDelete(row)">{{ $t('ai.delete') }}</Button>
                </div>
              </template>
              <template #empty>{{ $t('ai.empty') }}</template>
            </Table>
          </div>
        </template>
      </div>
    </div>
    <div v-if="capabilities?.canManageEngines" class="footer">
      <Page v-model="page" :total="total" :page-size="20" @on-change="load" />
    </div>
    <CCModal
      class="dm-modal-footer-center"
      v-model="editing"
      :title="editTitle"
      :width="680"
      :closable="!saving"
      :keyboard="!saving"
      @on-cancel="clearKey"
    >
      <a-form id="ai-engine-form" layout="vertical" :model="form" @finish="saveEngine">
        <a-form-item :label="$t('ai.name')" name="name" :rules="[{ required: true, whitespace: true, message: $t('ai.required') }]">
          <a-input v-model:value="form.name" :maxlength="80" :disabled="saving" />
        </a-form-item>
        <a-form-item :label="$t('ai.preset')">
          <a-select v-model:value="form.preset" :disabled="saving" @change="changePreset">
            <a-select-option value="DEEPSEEK">{{ $t('ai.deepseek') }}</a-select-option>
            <a-select-option value="CUSTOM">{{ $t('ai.custom') }}</a-select-option>
            <a-select-option value="KIMI">{{ $t('ai.kimi') }}</a-select-option>
            <a-select-option value="MINIMAX">{{ $t('ai.minimax') }}</a-select-option>
            <a-select-option value="DASHSCOPE">{{ $t('ai.dashscope') }}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item :label="$t('ai.model')" name="model" :rules="[{ required: true, whitespace: true, message: $t('ai.required') }]">
          <a-input v-model:value="form.model" :maxlength="128" :disabled="saving" />
        </a-form-item>
        <a-form-item :label="$t('ai.baseUrl')" name="baseUrl" :rules="[{ required: true, whitespace: true, message: $t('ai.required') }]">
          <a-input v-model:value="form.baseUrl" :maxlength="2048" :disabled="saving" />
        </a-form-item>
        <a-form-item :label="$t('ai.apiKey')">
          <a-input-password
            v-model:value="form.apiKey"
            :placeholder="form.id ? '********************************' : ''"
            :maxlength="8192"
            :disabled="saving"
            autocomplete="new-password"
          />
        </a-form-item>
        <p v-if="formError" role="alert" class="ai-error">{{ formError }}</p>
      </a-form>
      <template #footer>
        <Button
          :disabled="saving"
          @click="
            editing = false;
            clearKey();
          "
        >
          {{ $t('ai.cancel') }}
        </Button>
        <Button form="ai-engine-form" html-type="submit" type="primary" :loading="saving">{{ $t('ai.save') }}</Button>
      </template>
    </CCModal>
    <CCModal class="dm-modal-footer-center" v-model="confirming" :title="$t('ai.delete')" :closable="!busyId" :keyboard="!busyId">
      <p>{{ $t('ai.deleteHint', { name: target?.name }) }}</p>
      <p v-if="actionError" class="ai-error" role="alert">{{ actionError }}</p>
      <template #footer>
        <Button :disabled="!!busyId" @click="confirming = false">{{ $t('ai.cancel') }}</Button>
        <Button type="error" :loading="!!busyId" @click="runAction">{{ $t('ai.confirm') }}</Button>
      </template>
    </CCModal>
  </div>
</template>

<script>
import CCModal from '@/components/ui/CCModal.vue';
import { aiRequest, aiErrorMessage, AiRequestError } from '@/services/ai';

export default {
  name: 'AiSettings',
  components: { CCModal },
  data() {
    return {
      items: [],
      total: 0,
      page: 1,
      searchKeyword: '',
      appliedKeyword: '',
      loading: false,
      error: '',
      editing: false,
      saving: false,
      form: {},
      formError: '',
      confirming: false,
      target: null,
      busyId: '',
      actionError: '',
      controller: new AbortController(),
      loadSequence: 0
    };
  },
  computed: {
    capabilities() {
      return this.$store.state.aiCapabilities;
    },
    editTitle() {
      return this.$t(this.form.id ? 'ai.editEngine' : 'ai.addEngine');
    },
    columns() {
      return [
        { title: this.$t('ai.engine'), key: 'name', minWidth: 200, tooltip: true },
        { title: this.$t('ai.modelColumn'), key: 'model', minWidth: 200, tooltip: true },
        { title: this.$t('ai.baseUrl'), key: 'baseUrl', minWidth: 240, tooltip: true },
        { title: this.$t('ai.actions'), slot: 'engineActions', width: 230 }
      ];
    }
  },
  watch: {
    capabilities(value) {
      if (value?.canManageEngines) return;
      this.loadSequence++;
      this.loading = false;
      if (this.$store.state.aiCapabilitiesError) this.error = aiErrorMessage({ code: this.$store.state.aiCapabilitiesError });
      this.items = [];
      this.total = 0;
      this.clearKey();
      this.editing = false;
      this.confirming = false;
    }
  },
  mounted() {
    this.load();
  },
  beforeUnmount() {
    this.controller.abort();
    this.clearKey();
  },
  methods: {
    search() {
      this.appliedKeyword = this.searchKeyword.trim();
      this.page = 1;
      this.load();
    },
    async load() {
      const sequence = ++this.loadSequence;
      this.loading = true;
      this.error = '';
      try {
        const capabilities = await this.$store.dispatch('loadAiCapabilities');
        if (sequence !== this.loadSequence) return;
        if (!capabilities) {
          if (this.$store.state.aiCapabilitiesError) throw new AiRequestError(this.$store.state.aiCapabilitiesError);
          return;
        }
        if (!capabilities.canManageEngines) {
          this.items = [];
          return;
        }
        const data = { page: { pageNum: this.page, pageSize: 20 }, keyword: this.appliedKeyword };
        const result = await aiRequest('engine/listEngines', data, { signal: this.controller.signal });
        if (sequence !== this.loadSequence) return;
        this.items = result.records;
        this.total = result.total;
      } catch (error) {
        if (sequence === this.loadSequence && !this.controller.signal.aborted) {
          this.error = aiErrorMessage(error);
          this.items = [];
          if (['FORBIDDEN', 'HTTP_403'].includes(error.code)) {
            this.$store.commit('SET_AI_CAPABILITIES', { value: null, error: error.code });
          }
        }
      } finally {
        if (sequence === this.loadSequence) this.loading = false;
      }
    },
    async refreshPermissionsAfterDenial(error) {
      if (!['FORBIDDEN', 'HTTP_403'].includes(error.code) || this.controller.signal.aborted) return;
      this.loadSequence++;
      this.loading = false;
      this.$store.commit('SET_AI_CAPABILITIES', { value: null, error: '' });
      await this.$store.dispatch('loadAiCapabilities');
    },
    editEngine(row) {
      this.formError = '';
      this.form = {
        name: '',
        preset: 'DEEPSEEK',
        model: '',
        baseUrl: 'https://api.deepseek.com',
        apiKey: ''
      };
      if (row) {
        for (const key of ['id', 'version', 'name', 'preset', 'model', 'baseUrl']) this.form[key] = row[key];
      }
      this.editing = true;
    },
    changePreset(value) {
      const baseUrls = {
        DEEPSEEK: 'https://api.deepseek.com',
        CUSTOM: 'https://api.openai.com/v1',
        KIMI: 'https://api.moonshot.cn/v1',
        MINIMAX: 'https://api.minimax.cn/v1',
        DASHSCOPE: 'https://dashscope.aliyuncs.com/api/v1'
      };
      this.form.baseUrl = baseUrls[value];
    },
    clearKey() {
      this.form.apiKey = '';
    },
    async saveEngine() {
      if (this.saving) return;
      this.formError = '';
      if (!this.form.id && !this.form.apiKey) {
        this.formError = this.$t('ai.required');
        return;
      }
      this.saving = true;
      try {
        await aiRequest('engine/saveEngine', this.form, { signal: this.controller.signal });
        this.clearKey();
        this.editing = false;
        await this.load();
      } catch (error) {
        if (!this.controller.signal.aborted) this.formError = aiErrorMessage(error);
        await this.refreshPermissionsAfterDenial(error);
      } finally {
        this.saving = false;
      }
    },
    async testEngine(row) {
      if (this.busyId) return;
      this.busyId = row.id;
      try {
        const result = await aiRequest('engine/testEngine', { id: row.id, version: row.version }, { signal: this.controller.signal });
        if (this.controller.signal.aborted) return;
        if (result.verificationStatus === 'PASS') {
          this.$Message.success(this.$t('ai.verification.PASS'));
        } else {
          this.$Message.error(aiErrorMessage({ code: result.errorCode }));
        }
      } catch (error) {
        if (!this.controller.signal.aborted) this.$Message.error(aiErrorMessage(error));
      } finally {
        if (!this.controller.signal.aborted) await this.load();
        this.busyId = '';
      }
    },
    confirmDelete(row) {
      this.target = row;
      this.actionError = '';
      this.confirming = true;
    },
    async runAction() {
      if (this.busyId) return;
      this.busyId = this.target.id;
      this.actionError = '';
      try {
        await aiRequest('engine/deleteEngine', { id: this.target.id, version: this.target.version }, { signal: this.controller.signal });
        this.confirming = false;
        await this.load();
      } catch (error) {
        if (!this.controller.signal.aborted) this.actionError = aiErrorMessage(error);
        await this.refreshPermissionsAfterDenial(error);
      } finally {
        this.busyId = '';
      }
    }
  }
};
</script>

<style lang="less" scoped>
.ai-settings {
  min-width: 0;
  .table-list,
  .content,
  .table-container {
    min-width: 0;
  }
  .table-list {
    padding: 16px 24px 0;
  }
  .ai-engine-search {
    width: 320px;
    max-width: 100%;
  }
  .ai-actions {
    display: flex;
    align-items: center;
    gap: 4px;
    white-space: nowrap;
  }
  .ai-notice {
    display: flex;
    align-items: center;
    gap: 8px;
    padding-bottom: 16px;
  }
}

.ai-error {
  color: var(--error-color);
}
</style>
