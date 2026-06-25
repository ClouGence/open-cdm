<template>
  <section class="flow-section-card release-config-card">
    <div class="release-grid">
      <div class="release-panel">
        <div class="panel-heading">
          <CustomIcon type="icon-v2-Gitee" size="20px" />
          <span>{{ $t('fa-bu-yuan') }}</span>
        </div>

        <Form ref="releaseSourceForm" :model="flowGitOpsForm" :rules="releaseRules" label-position="left" :label-width="112">
          <FormItem :label="$t('fa-bu-yuan-lei-xing')" class="source-type-form-item force-required" required>
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
                <CustomIcon v-if="sourceType.iconResource" :resource="sourceType.iconResource" :alt="sourceType.label" size="18px" />
                <CustomIcon v-else :type="sourceType.iconType" size="18px" />
                <span>{{ sourceType.label }}</span>
              </button>
            </div>
          </FormItem>

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
                <CustomIcon :type="item.scmType" rightMargin />
                {{ item.scmDisplay }}
              </Option>
            </Select>
            <Button v-else type="text" @click="$emit('add-scm')">{{ $t('qu-pei-zhi') }}</Button>
          </FormItem>

          <FormItem :label="$t('cang-ku')" prop="repoName">
            <div class="inline-control repo-control">
              <Select
                ref="repoSelect"
                v-model="flowGitOpsForm.repoName"
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
                  <Option v-for="repo in repoGroup" :value="repo.repoName" :key="repo.repoUrl" :label="repo.repoName">
                    <span>{{ repo.repoName }}</span>
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
        <div class="panel-heading target-heading">
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
          <FormItem :label="$t('shu-ju-ku-lei-xing')" class="target-select-form-item database-type-form-item force-required" required>
            <div class="type-card-group database-type-card-group">
              <button
                v-for="type in databaseTypeCardList"
                :key="type"
                type="button"
                class="type-card database-type-card"
                :class="{ active: flowGitOpsForm.databaseType === type }"
                :aria-pressed="flowGitOpsForm.databaseType === type"
                @click="$emit('database-type-select', type)"
              >
                <CustomIcon :type="type" size="18px" />
                <span>{{ type }}</span>
              </button>
            </div>
          </FormItem>

          <FormItem :label="$t('shi-li-1')" prop="instanceId" class="target-select-form-item">
            <Select
              ref="instanceSelect"
              v-if="devopsInsList.length"
              v-model="flowGitOpsForm.instanceId"
              :placeholder="$t('qing-xuan-ze-shu-ju-ku-shi-li')"
              placement="bottom-start"
              transfer
              transfer-class-name="release-flow-select-dropdown"
              events-enabled
              @on-change="$emit('devops-ins-change')"
              @on-open-change="$emit('select-open-change', $event, $refs.instanceSelect)"
              filterable
              :not-found-text="$t('zan-wu-shu-ju')"
            >
              <Option v-for="ins in filteredDevopsInsList" :value="ins.objId" :key="ins.objId" :label="ins.objName">
                <CustomIcon :type="ins.objAttr.dsType" rightMargin />
                {{ ins.objName }}
              </Option>
            </Select>
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

          <FormItem prop="initScript" class="init-script-form-item">
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
                <div class="field-hint init-radio-hint">{{ flowGitOpsDescription(flowGitOpsForm.initScript) }}</div>
              </div>
            </div>
          </FormItem>
        </Form>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  name: 'ReleaseFlowPipelineConfig',
  props: {
    flowGitOpsForm: { type: Object, required: true },
    releaseRules: { type: Object, required: true },
    sourceScmType: { type: String, required: true },
    sourceTypeCardList: { type: Array, required: true },
    filteredDevopsScmList: { type: Array, required: true },
    devopsScmSelected: { type: Object, default: null },
    devopsRepoListByGroup: { type: Object, required: true },
    repoLoading: { type: Boolean, required: true },
    devopsTo: { type: String, required: true },
    databaseTypeCardList: { type: Array, required: true },
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
