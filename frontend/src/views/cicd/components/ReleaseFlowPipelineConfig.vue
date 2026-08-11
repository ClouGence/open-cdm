<template>
  <section class="page-section release-config-section">
    <div class="page-section__title">{{ $t('fa-bu-liu-cheng-pei-zhi') }}</div>
    <div class="release-grid">
      <div class="release-panel">
        <div class="panel-subheading">
          <img v-if="isBuiltIn" class="built-in-flow-icon built-in-flow-icon--heading" src="/dm.ico" alt="CloudDM" />
          <CustomIcon v-else :resource="getScmIconResource(sourceScmType)" size="20px" />
          <span>{{ $t('bian-geng-lei-xing') }}</span>
        </div>

        <Form ref="releaseSourceForm" :model="flowGitOpsForm" :rules="releaseRules" label-position="left" :label-width="112">
          <FormItem :label="$t('bian-geng-lei-xing')" class="source-type-form-item force-required" required>
            <div class="type-card-group source-type-card-group">
              <button
                v-for="sourceType in sourceTypeCardList"
                :key="sourceType.value"
                type="button"
                class="type-card source-type-card"
                :class="{ active: sourceScmType === sourceType.value }"
                :aria-pressed="sourceScmType === sourceType.value"
                @click="$emit('source-type-select', sourceType.value)"
              >
                <img v-if="sourceType.value === 'BUILT_IN'" class="built-in-flow-icon" src="/dm.ico" alt="CloudDM" />
                <CustomIcon v-else :resource="sourceType.iconResource || getScmIconResource(sourceType.value)" :alt="sourceType.label" size="18px" />
                <span>{{ sourceType.label }}</span>
              </button>
            </div>
          </FormItem>

          <FormItem v-if="isBuiltIn" :label="$t('shang-ji-bian-geng-liu')" prop="parentFlowId">
            <Select
              ref="parentFlowSelect"
              v-model="flowGitOpsForm.parentFlowId"
              :loading="parentFlowLoading"
              :placeholder="$t('qing-xuan-ze-shang-ji-bian-geng-liu')"
              placement="bottom-start"
              transfer
              transfer-class-name="release-flow-select-dropdown"
              events-enabled
              filterable
              clearable
              :not-found-text="$t('zan-wu-shu-ju')"
              @on-change="$emit('parent-flow-change', $event)"
              @on-open-change="$emit('select-open-change', $event, $refs.parentFlowSelect)"
            >
              <Option
                v-for="flow in parentFlowList"
                :value="flow.flowId"
                :key="flow.flowId"
                :label="flow.flowName"
                :disabled="flow.selectable === false"
              >
                <span>{{ flow.flowName }}</span>
                <span class="repo-empty-label">{{ parentFlowMeta(flow) }}</span>
                <span v-if="flow.unavailableReason" class="repo-empty-label">{{ flow.unavailableReason }}</span>
              </Option>
            </Select>
            <div class="field-hint">{{ $t('nei-zhi-bian-geng-liu-shang-ji-ke-xuan-shuo-ming') }}</div>
          </FormItem>

          <template v-else>
            <FormItem :label="$t('nav-git-ops')" prop="repoScmId" class="force-required gitops-select-form-item" required>
              <Select
                ref="gitOpsSelect"
                v-if="filteredDevopsScmList.length"
                v-model="flowGitOpsForm.repoScmId"
                :placeholder="$t('qing-xuan-ze')"
                placement="bottom-start"
                transfer
                transfer-class-name="release-flow-select-dropdown"
                events-enabled
                @on-change="$emit('devops-scm-change', $event)"
                @on-open-change="$emit('select-open-change', $event, $refs.gitOpsSelect)"
              >
                <Option v-for="item in filteredDevopsScmList" :value="item.scmId" :key="item.scmId" :label="item.scmDisplay">
                  <CustomIcon :resource="getScmIconResource(item.scmType)" :type="item.scmType" :alt="item.scmTypeI18n || ''" rightMargin />
                  {{ item.scmDisplay }}
                </Option>
              </Select>
              <Button v-else type="text" @click="$emit('add-scm')">{{ $t('qu-pei-zhi') }}</Button>
            </FormItem>

            <FormItem :label="$t('cang-ku')" prop="repoSelectionKey">
              <div class="inline-control repo-control">
                <Select
                  ref="repoSelect"
                  v-model="flowGitOpsForm.repoSelectionKey"
                  :disabled="!devopsScmSelected"
                  placement="bottom-start"
                  transfer
                  transfer-class-name="release-flow-select-dropdown"
                  events-enabled
                  @on-change="$emit('devops-repo-change')"
                  @on-open-change="$emit('select-open-change', $event, $refs.repoSelect)"
                  filterable
                  :not-found-text="$t('zan-wu-shu-ju')"
                >
                  <OptionGroup v-for="(repoGroup, namespace) in devopsRepoListByGroup" :label="namespace" :key="namespace">
                    <Option
                      v-for="repo in repoGroup"
                      :value="getRepoSelectionKey(repo)"
                      :key="getRepoSelectionKey(repo)"
                      :label="repo.repoPath || repo.repoName"
                      :disabled="repo.empty"
                    >
                      <span>{{ repo.repoName }}</span>
                      <span v-if="repo.empty" class="repo-empty-label">{{ $t('kong-cang-ku') }}</span>
                      <span class="repo-link">
                        <CustomIcon type="icon-v2-jicheng" @click.stop="$emit('repo-jump', repo.repoHome)" />
                      </span>
                    </Option>
                  </OptionGroup>
                </Select>
                <button
                  type="button"
                  class="repo-refresh-action"
                  :disabled="!devopsScmSelected || repoLoading"
                  @mousedown.stop
                  @click.stop.prevent="$emit('devops-scm-change')"
                >
                  <span v-if="repoLoading" class="mini-spinner"></span>
                  <CustomIcon v-else type="icon-v2-Refresh" />
                </button>
              </div>
            </FormItem>

            <FormItem :label="$t('mu-biao-fen-zhi')" prop="repoBranch">
              <Input v-model="flowGitOpsForm.repoBranch" :placeholder="$t('qing-shu-ru-mu-biao-fen-zhi')" />
            </FormItem>

            <FormItem :label="$t('jiao-ben-lu-jin')" prop="repoScriptPath">
              <Input v-model="flowGitOpsForm.repoScriptPath" :placeholder="$t('qing-shu-ru-jiao-ben-lu-jin-ke-xuan')" />
              <div class="field-hint">{{ $t('devops-script-hint') }}</div>
            </FormItem>

            <FormItem :label="$t('chu-fa-fang-shi')" prop="eventType">
              <RadioGroup v-model="flowGitOpsForm.eventType">
                <Radio label="Push">{{ eventTypeMap.push }}</Radio>
                <Radio label="PullRequest">{{ eventTypeMap.pr }}</Radio>
              </RadioGroup>
            </FormItem>
          </template>
        </Form>
      </div>

      <div class="link-divider">
        <span>
          <svg class="flow-link-arrows" viewBox="0 0 28 28" aria-hidden="true">
            <path d="M7 14h14" />
            <path d="m16.8 9.8 4.2 4.2-4.2 4.2" />
          </svg>
        </span>
      </div>

      <div class="release-panel">
        <div class="panel-subheading target-heading">
          <CustomIcon :type="devopsTo" size="20px" />
          <span>{{ $t('mu-biao-fa-bu-shu-ju-ku') }}</span>
        </div>

        <Form
          ref="releaseTargetForm"
          class="release-target-form"
          :model="flowGitOpsForm"
          :rules="releaseRules"
          label-position="left"
          :label-width="112"
        >
          <FormItem :label="$t('shu-ju-ku-lei-xing')" prop="databaseType" class="target-select-form-item database-type-form-item force-required">
            <ReleaseFlowDatabaseTypeSelect
              :value="flowGitOpsForm.databaseType"
              :options="databaseTypeOptions"
              :disabled="databaseTypeLocked"
              @change="handleDatabaseTypeSelect"
              @open-change="handleDatabaseTypeOpenChange"
            />
            <div v-if="databaseTypeLocked" class="field-hint">
              {{ $t('shu-ju-ku-lei-xing-yu-shang-ji-bian-geng-liu-yi-zhi') }}
            </div>
          </FormItem>

          <FormItem :label="$t('shi-li-1')" prop="instanceId" class="target-select-form-item">
            <ReleaseFlowInstanceSelect
              ref="instanceSelect"
              v-if="devopsInsList.length"
              v-model="flowGitOpsForm.instanceId"
              :options="filteredDevopsInsList"
              :placeholder="$t('qing-xuan-ze-shu-ju-ku-shi-li')"
              @change="$emit('devops-ins-change')"
              @open-change="handleInstanceOpenChange"
            />
            <Button v-else type="text" @click="$emit('ds-setting')">{{ $t('qu-pei-zhi') }}</Button>
          </FormItem>

          <FormItem v-if="flowGitOpsForm.devopsInsHasCatalog" :label="$t('shu-ju-ku')" prop="catalogName">
            <div class="inline-control">
              <Select
                ref="catalogSelect"
                v-model="flowGitOpsForm.catalogName"
                placement="bottom-start"
                transfer
                transfer-class-name="release-flow-select-dropdown"
                events-enabled
                @on-change="$emit('catalog-change')"
                @on-open-change="$emit('select-open-change', $event, $refs.catalogSelect)"
                filterable
              >
                <Option v-for="catalog in devopsInsCatalogList" :value="catalog.objName" :key="catalog.objName">
                  {{ catalog.objName }}
                </Option>
              </Select>
              <CustomIcon type="icon-v2-Refresh" @click="$emit('refresh-catalog-list', true)" leftMargin />
            </div>
          </FormItem>

          <FormItem :label="$t('schema')" prop="schemaName" class="schema-form-item target-select-form-item force-required" required>
            <div class="inline-control">
              <Select
                ref="schemaSelect"
                v-model="flowGitOpsForm.schemaName"
                :disabled="schemaSelectDisabled"
                :placeholder="$t('qing-xuan-ze')"
                placement="bottom-start"
                transfer
                transfer-class-name="release-flow-select-dropdown"
                events-enabled
                @on-open-change="$emit('select-open-change', $event, $refs.schemaSelect)"
                filterable
              >
                <Option v-for="schema in devopsInsSchemaList" :value="schema.objName" :key="schema.objName">
                  {{ schema.objName }}
                </Option>
              </Select>
              <button
                type="button"
                class="inline-refresh-slot"
                :disabled="schemaSelectDisabled || schemaLoading"
                @mousedown.stop
                @click.stop.prevent="$emit('refresh-schema-list')"
              >
                <span v-if="schemaLoading" class="mini-spinner"></span>
                <CustomIcon v-else type="icon-v2-Refresh" />
              </button>
            </div>
          </FormItem>

          <FormItem v-if="!isBuiltIn" prop="initScript" class="init-script-form-item">
            <div class="init-script-field">
              <div class="init-script-label">
                <span>*</span>
                {{ $t('chu-shi-hua-fang-shi') }}
              </div>
              <div class="init-script-control">
                <RadioGroup v-model="flowGitOpsForm.initScript" class="init-radio-row">
                  <Radio v-for="item in initOptions" :key="item.value" :label="item.value">
                    {{ item.label }}
                  </Radio>
                </RadioGroup>
                <div class="field-hint init-radio-hint">
                  {{ flowGitOpsDescription(flowGitOpsForm.initScript) }}
                </div>
              </div>
            </div>
          </FormItem>
        </Form>
      </div>
    </div>
  </section>
</template>

<script>
import { getRepoSelectionKey, getScmIconResource } from '../utils';
import ReleaseFlowDatabaseTypeSelect from './ReleaseFlowDatabaseTypeSelect.vue';
import ReleaseFlowInstanceSelect from './ReleaseFlowInstanceSelect.vue';

export default {
  name: 'ReleaseFlowPipelineConfig',
  components: { ReleaseFlowDatabaseTypeSelect, ReleaseFlowInstanceSelect },
  props: {
    flowGitOpsForm: { type: Object, required: true },
    releaseRules: { type: Object, required: true },
    sourceScmType: { type: String, required: true },
    sourceTypeCardList: { type: Array, required: true },
    isBuiltIn: { type: Boolean, default: false },
    parentFlowList: { type: Array, default: () => [] },
    parentFlowLoading: { type: Boolean, default: false },
    databaseTypeLocked: { type: Boolean, default: false },
    filteredDevopsScmList: { type: Array, required: true },
    devopsScmSelected: { type: Object, default: null },
    devopsRepoListByGroup: { type: Object, required: true },
    repoLoading: { type: Boolean, required: true },
    devopsTo: { type: String, required: true },
    databaseTypeOptions: { type: Array, required: true },
    devopsInsList: { type: Array, required: true },
    filteredDevopsInsList: { type: Array, required: true },
    devopsInsCatalogList: { type: Array, required: true },
    devopsInsSchemaList: { type: Array, required: true },
    schemaSelectDisabled: { type: Boolean, required: true },
    schemaLoading: { type: Boolean, required: true },
    initOptions: { type: Array, required: true },
    eventTypeMap: { type: Object, required: true },
    flowGitOpsDescription: { type: Function, required: true }
  },
  emits: [
    'source-type-select',
    'parent-flow-change',
    'devops-scm-change',
    'devops-repo-change',
    'repo-jump',
    'database-type-select',
    'devops-ins-change',
    'catalog-change',
    'refresh-catalog-list',
    'select-open-change',
    'refresh-schema-list',
    'add-scm',
    'ds-setting'
  ],
  methods: {
    parentFlowMeta(flow) {
      return [flow?.flowManagerName, flow?.dsType].filter(Boolean).join(' · ');
    },
    getRepoSelectionKey,
    getScmIconResource,
    handleDatabaseTypeSelect(type) {
      this.$emit('database-type-select', type);
    },
    handleInstanceOpenChange(open, selectRef) {
      this.$emit('select-open-change', open, selectRef);
    },
    handleDatabaseTypeOpenChange(open, selectRef) {
      this.$emit('select-open-change', open, selectRef);
    },
    async validate() {
      const result = await Promise.all([this.$refs.releaseSourceForm.validate(), this.$refs.releaseTargetForm.validate()]);
      return result.every(Boolean);
    },
    clearSourceValidate(prop) {
      this.$refs.releaseSourceForm?.clearValidate?.(prop);
    }
  }
};
</script>

<style lang="less" scoped>
.built-in-flow-icon {
  display: block;
  width: 18px;
  height: 18px;
  flex: 0 0 auto;
  object-fit: contain;
}

.built-in-flow-icon--heading {
  width: 20px;
  height: 20px;
}

.repo-empty-label {
  margin-left: 8px;
  color: var(--text-tertiary);
  font-size: 13px;
}
</style>
