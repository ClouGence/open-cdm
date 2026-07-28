<template>
  <div class="page-shell preference-page">
    <div class="page-shell__body">
      <AppPageTabs :model-value="activeTab" :tabs="tabs" @change="handleTabChange" />

      <div class="page-panel-body preference-panel">
        <Spin v-if="activeState.loading" fix />

        <Alert v-if="!canEdit" type="info" show-icon class="preference-readonly-alert">
          {{ $t('preference-readonly-help') }}
        </Alert>

        <Form
          v-if="activeState.loaded"
          ref="preferenceForm"
          :key="`${activeTab}-${activeState.revision}`"
          :model="activeState.draftValues"
          :rules="formRules"
          label-position="top"
          class="preference-form"
        >
          <section v-for="section in activeDefinition.sections" :key="section.name" class="page-section preference-section">
            <div class="page-section__title">{{ $t(section.titleKey) }}</div>

            <div class="preference-fields">
              <FormItem v-for="field in section.fields" :key="field.key" :label="$t(field.labelKey)" :prop="field.key">
                <PreferenceSettingField
                  :field="field"
                  :model-value="activeState.draftValues[field.key]"
                  :disabled="isFieldDisabled(field)"
                  @update:model-value="setDraftValue(field.key, $event)"
                />
                <p class="preference-field-help">{{ $t(field.helpKey) }}</p>
              </FormItem>
            </div>
          </section>
        </Form>

        <div v-else-if="!activeState.loading" class="preference-load-empty">
          {{ $t('preference-load-failed') }}
        </div>
      </div>
    </div>

    <div v-if="canEdit" class="page-shell__footer preference-footer">
      <Button :disabled="!activeDirty || activeState.loading || activeState.saving" @click="resetActiveDraft">
        {{ $t('qu-xiao') }}
      </Button>
      <Button type="primary" :loading="activeState.saving" :disabled="!activeDirty || activeState.loading" @click="saveActiveTab">
        {{ $t('bao-cun') }}
      </Button>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import AppPageTabs from '@/components/layout/AppPageTabs';
import PreferenceSettingField from '@/views/system/PreferenceSettingField';
import { getPreferenceTab, getPreferenceTabFields, PREFERENCE_TABS } from '@/views/system/preferenceConfig';

function createTabStates() {
  const states = {};
  PREFERENCE_TABS.forEach((tab) => {
    states[tab.name] = {
      loaded: false,
      loading: false,
      saving: false,
      revision: 0,
      configMeta: {},
      originalValues: {},
      draftValues: {}
    };
  });
  return states;
}

function normalizeTabName(tabName) {
  if (PREFERENCE_TABS.some((tab) => !tab.hidden && tab.name === tabName)) {
    return tabName;
  }
  return PREFERENCE_TABS.find((tab) => !tab.hidden).name;
}

export default {
  name: 'Preference',
  components: {
    AppPageTabs,
    PreferenceSettingField
  },
  data() {
    return {
      activeTab: normalizeTabName(this.$route.query.tab),
      tabStates: createTabStates()
    };
  },
  computed: {
    ...mapState(['myAuth']),
    tabs() {
      return PREFERENCE_TABS.filter((tab) => !tab.hidden).map((tab) => ({
        name: tab.name,
        label: this.$t(tab.labelKey),
        disabled: this.activeState.saving && tab.name !== this.activeTab
      }));
    },
    activeDefinition() {
      return getPreferenceTab(this.activeTab);
    },
    activeFields() {
      return getPreferenceTabFields(this.activeTab);
    },
    activeState() {
      return this.tabStates[this.activeTab];
    },
    activeDirty() {
      return this.getDirtyKeys(this.activeTab).length > 0;
    },
    canEdit() {
      return this.myAuth.includes('RDP_PRI_USER_KV_CONF_W');
    },
    formRules() {
      const rules = {};
      this.activeFields.forEach((field) => {
        if (field.widget === 'number') {
          rules[field.key] = [
            {
              trigger: 'change',
              validator: (rule, value, callback) => {
                if (typeof value !== 'number' || !Number.isInteger(value)) {
                  callback(new Error(this.$t('preference-validation-integer')));
                  return;
                }
                if (value < field.min || value > field.max) {
                  callback(new Error(this.$t('preference-validation-range', [field.min, field.max])));
                  return;
                }
                callback();
              }
            }
          ];
          return;
        }

        if (field.required) {
          rules[field.key] = [
            {
              trigger: 'change',
              validator: (rule, value, callback) => {
                if (value === undefined || value === null || String(value).trim() === '') {
                  callback(new Error(this.$t('preference-validation-required')));
                  return;
                }
                callback();
              }
            }
          ];
        }
      });
      return rules;
    }
  },
  mounted() {
    window.addEventListener('beforeunload', this.handleBeforeUnload);
    this.loadTab(this.activeTab).finally(() => {
      this.syncTabQuery();
    });
  },
  beforeUnmount() {
    window.removeEventListener('beforeunload', this.handleBeforeUnload);
  },
  beforeRouteUpdate(to, from, next) {
    const nextTab = this.normalizeTabName(to.query.tab);
    if (nextTab === this.activeTab) {
      next();
      return;
    }

    this.confirmDiscardIfNeeded().then((confirmed) => {
      if (!confirmed) {
        next(false);
        return;
      }
      this.resetTabDraft(this.activeTab);
      this.activeTab = nextTab;
      next();
      this.loadTab(nextTab);
    });
  },
  beforeRouteLeave(to, from, next) {
    this.confirmDiscardIfNeeded().then((confirmed) => {
      next(confirmed);
    });
  },
  methods: {
    normalizeTabName(tabName) {
      return normalizeTabName(tabName);
    },
    syncTabQuery() {
      if (this.$route.query.tab === this.activeTab) {
        return;
      }
      this.$router.replace({
        path: this.$route.path,
        query: {
          ...this.$route.query,
          tab: this.activeTab
        }
      });
    },
    handleTabChange(tabName) {
      const nextTab = this.normalizeTabName(tabName);
      if (nextTab === this.activeTab) {
        return;
      }

      this.confirmDiscardIfNeeded().then((confirmed) => {
        if (!confirmed) {
          return;
        }
        this.resetTabDraft(this.activeTab);
        this.activeTab = nextTab;
        this.syncTabQuery();
        this.loadTab(nextTab);
      });
    },
    confirmDiscardIfNeeded() {
      if (!this.activeDirty) {
        return Promise.resolve(true);
      }

      return new Promise((resolve) => {
        this.$Modal.confirm({
          title: this.$t('preference-unsaved-title'),
          content: this.$t('preference-unsaved-content'),
          okText: this.$t('preference-discard-and-continue'),
          cancelText: this.$t('qu-xiao'),
          onOk: () => resolve(true),
          onCancel: () => resolve(false)
        });
      });
    },
    handleBeforeUnload(event) {
      if (!this.activeDirty) {
        return;
      }
      event.preventDefault();
      event.returnValue = '';
    },
    getDirtyKeys(tabName) {
      const state = this.tabStates[tabName];
      if (!state.loaded) {
        return [];
      }
      return getPreferenceTabFields(tabName)
        .filter(
          (field) =>
            this.serializeFieldValue(field, state.draftValues[field.key]) !== this.serializeFieldValue(field, state.originalValues[field.key])
        )
        .map((field) => field.key);
    },
    parseConfigValue(field, config) {
      let value = config?.configValue;
      if (value === undefined || value === null) {
        value = field.defaultValue;
      }

      if (field.widget === 'switch') {
        if (typeof value === 'boolean') {
          return value;
        }
        return String(value).toLowerCase() === 'true';
      }
      if (field.widget === 'number') {
        if (value === '') {
          return null;
        }
        const numberValue = Number(value);
        if (!Number.isInteger(numberValue)) {
          return null;
        }
        return numberValue;
      }
      return value ?? '';
    },
    serializeFieldValue(field, value) {
      if (field.widget === 'switch') {
        return String(Boolean(value));
      }
      if (field.widget === 'number') {
        if (value === undefined || value === null || value === '') {
          return '';
        }
        return String(value);
      }
      return String(value ?? '').trim();
    },
    async loadTab(tabName, force = false) {
      const state = this.tabStates[tabName];
      if ((state.loaded && !force) || state.loading) {
        return;
      }

      state.loading = true;
      const fields = getPreferenceTabFields(tabName);
      try {
        const res = await this.$services.rdpUserConfigGetUserSpecifiedConfs({
          data: {
            configNames: fields.map((field) => field.key)
          }
        });
        if (!res.success) {
          return;
        }

        const configMap = res.data || {};
        const configMeta = {};
        const values = {};
        fields.forEach((field) => {
          const config = configMap[field.key];
          configMeta[field.key] = config || {
            configName: field.key,
            needCreated: true,
            readOnly: false
          };
          values[field.key] = this.parseConfigValue(field, config);
        });

        state.configMeta = configMeta;
        state.originalValues = { ...values };
        state.draftValues = { ...values };
        state.loaded = true;
        state.revision += 1;
        await this.$nextTick();
        state.originalValues = { ...state.draftValues };
      } finally {
        state.loading = false;
      }
    },
    setDraftValue(configKey, value) {
      this.activeState.draftValues[configKey] = value;
    },
    isFieldDisabled(field) {
      const config = this.activeState.configMeta[field.key];
      return !this.canEdit || Boolean(config?.readOnly);
    },
    resetTabDraft(tabName) {
      const state = this.tabStates[tabName];
      state.draftValues = { ...state.originalValues };
      state.revision += 1;
    },
    resetActiveDraft() {
      this.resetTabDraft(this.activeTab);
      this.$nextTick(() => {
        this.$refs.preferenceForm?.clearValidate?.();
      });
    },
    async saveActiveTab() {
      if (!this.canEdit || !this.activeDirty) {
        return;
      }

      const valid = await this.$refs.preferenceForm.validate();
      if (!valid) {
        return;
      }

      const updateConfigs = {};
      const needCreateConfigs = {};
      const state = this.activeState;
      const dirtyKeys = new Set(this.getDirtyKeys(this.activeTab));
      this.activeFields.forEach((field) => {
        if (!dirtyKeys.has(field.key)) {
          return;
        }

        const value = this.serializeFieldValue(field, state.draftValues[field.key]);
        const config = state.configMeta[field.key];
        if (config?.needCreated) {
          needCreateConfigs[field.key] = value;
        } else {
          updateConfigs[field.key] = value;
        }
      });

      state.saving = true;
      try {
        const res = await this.$services.rdpUserConfigUpsertUserConfigs({
          data: {
            updateConfigs,
            needCreateConfigs
          }
        });
        if (!res.success) {
          return;
        }
        this.$Message.success(this.$t('preference-save-success'));
        await this.loadTab(this.activeTab, true);
      } finally {
        state.saving = false;
      }
    }
  }
};
</script>

<style lang="less" scoped>
.page-shell {
  display: flex;
  flex: 1;
  flex-direction: column;
  min-height: 0;
  height: 100%;
}

.page-shell__body {
  position: relative;
  display: flex;
  flex: 1;
  flex-direction: column;
  min-height: 0;
  overflow: hidden;
}

.page-panel-body {
  padding: 16px 24px;
}

.preference-panel {
  position: relative;
  flex: 1;
  min-height: 0;
  overflow-y: auto;
}

.preference-readonly-alert {
  margin-bottom: 24px;
}

.preference-form {
  display: flex;
  flex-direction: column;
  gap: 32px;
  max-width: 1040px;
}

.preference-section {
  margin: 0;
}

.page-section__title {
  position: relative;
  margin-bottom: 16px;
  padding-left: 11px;
  color: var(--text-primary);
  font-size: 16px;
  font-weight: 500;
  line-height: 1.5;
}

.page-section__title::before {
  position: absolute;
  top: 5px;
  bottom: 5px;
  left: 0;
  width: 3px;
  border-radius: 2px;
  background: var(--primary-color);
  content: '';
}

.preference-fields {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 8px 32px;
  padding-left: 11px;
}

.preference-form :deep(.ivu-form-item) {
  min-width: 0;
  margin-bottom: 16px;
}

.preference-form :deep(.ivu-form-item-label) {
  padding-bottom: 8px;
  color: var(--text-primary);
  font-size: 14px;
  font-weight: 500;
}

.preference-field-help {
  margin-top: 7px;
  color: var(--text-tertiary);
  font-size: 12px;
  line-height: 1.5;
}

.preference-load-empty {
  padding: 48px 0;
  color: var(--text-tertiary);
  text-align: center;
}

.page-shell__footer {
  display: flex;
  flex: 0 0 auto;
  align-items: center;
  justify-content: center;
  gap: 8px;
  min-height: 64px;
  padding: 12px 24px;
  border-top: 1px solid var(--border-light);
  background: var(--bg-card);
}

@media (max-width: 767px) {
  .page-panel-body {
    padding: 16px;
  }

  .preference-fields {
    grid-template-columns: minmax(0, 1fr);
    gap: 4px;
  }

  .page-shell__footer {
    flex-wrap: wrap;
    padding: 12px 16px;
  }
}
</style>
