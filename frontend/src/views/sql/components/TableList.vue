<template>
  <div class="table-list-container">
    <div class="table-list-resize" />
    <div class="table-list-content">
      <div class="search-header">
        <div class="search-border">
          <a-dropdown trigger="click" placement="bottomLeft" style="margin-right: 10px">
            <div class="btn-hint">
              <CustomIcon
                v-if="currentTab.leafType === 'TABLE'"
                :type="`icon-v2-${currentTab.leafType}`"
                :color="`${isDark ? '#fff' : ''}`"
                hoverStyle
                leftMargin="4px"
                rightMargin="4px"
              />
              <CustomIcon v-else :type="`icon-v2-svg-${currentTab.leafType}`" hoverStyle leftMargin="4px" rightMargin="4px" />
            </div>
            <template #overlay>
              <a-menu :selected-keys="[currentTab.leafType]">
                <a-menu-item v-for="leaf in currentTab.leafGroup" :key="leaf.type" :value="leaf.type" @click="handleChangeTab(leaf.type)">
                  <!-- <CustomIcon :type="`icon-v2-svg-${leaf.type}`" /> -->
                  <cc-svg-icon :name="leaf.type" style="display: inline-block" />
                  <span style="margin-left: 5px">{{ leaf.i18n }}</span>
                </a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
          <Input
            size="small"
            allowClear
            v-model="currentTab[currentTab.leafType].searchKey"
            icon="ios-search"
            @on-click="handleSearch"
            @on-change="handleSearch"
            @on-enter="handleSearch"
          />
          <CustomIcon type="icon-v2-Refresh" hoverStyle @click.native="handleRefreshTree" leftMargin="4px" rightMargin="4px" />
        </div>
      </div>
      <div
        class="table-query-item-container"
        @contextmenu.prevent.stop="onContextmenu"
        :class="!['TABLE', 'VIEW', 'PROC', 'FUNC'].includes(currentTab.leafType) ? 'no-indent' : ''"
      >
        <loading :active="tableListLoading" :is-full-page="false"></loading>
        <v-tree
          :emptyText="$t('dian-ji-jia-zai-shu-ju')"
          class="table-list-tree"
          ref="tableTree"
          key-field="key"
          :load="handleExpandLoadNode"
          :render="renderNode"
          :nodeIndent="20"
          :renderNodeAmount="200"
          @click="handleVTreeClick"
          @node-right-click="handleNodeRightClick"
        >
          <template #empty>
            <div v-if="!tableListLoading">
              <span>{{ $t('zan-wu-shu-ju') }}</span>
              <br />
              <a-button type="text" ghost @click="handleRefreshTree">
                <CustomIcon type="icon-v2-Refresh" />
                <span style="padding-left: 5px">{{ $t('dian-ji-jia-zai-shu-ju') }}</span>
              </a-button>
            </div>
          </template>
        </v-tree>
      </div>
    </div>
    <CCModal :title="menuModal.title" v-model="menuModal.show" :mask-closable="false" :closable="false" :keyboard="false">
      <div style="margin-bottom: 5px; font-weight: bold">
        {{ menuModal.content }}
      </div>
      <a-collapse :bordered="false" size="small" style="margin: 5px 0" v-model:activeKey="menuModal.collapseKey" class="advanced-setting">
        <a-collapse-panel key="options" :header="$t('gao-ji-pei-zhi')">
          <div style="padding: 5px">
            <div v-for="setting in advancedSetting" :key="setting.value">
              <a-checkbox :value="menuModal.options[setting.value]" @change="handleMenuOptionChange(setting.value, $event)">
                {{ setting.label }}
              </a-checkbox>
            </div>
          </div>
        </a-collapse-panel>
      </a-collapse>
      <a-input
        v-if="menuModal.showNameInput"
        :value="menuModal.name"
        :placeholder="$t('qing-shu-ru-xin-de-ming-zi')"
        @change="handleMenuNameChange"
        allowClear
      />
      <div style="margin: 5px 0; font-weight: bold" v-if="menuModal.sql">{{ $t('sql-yu-ju') }}:</div>
      <read-only-editor :text="menuModal.sql" v-if="menuModal.sql" :ds-type="currentTab.dsType" />
      <template #footer>
        <a-button type="primary" @click="handleRightClickMenu(actionType)" v-if="!menuModal.sql" :disabled="!menuModal.name">
          {{ $t('sheng-cheng-sql-yu-ju') }}
        </a-button>
        <a-button
          v-if="menuModal.permission && menuModal.sql"
          type="primary"
          :danger="menuModal.danger"
          @click="handleDoAction"
          :loading="doActionLoading"
        >
          {{ $t('li-ji-zhi-hang') }}
        </a-button>
        <a-button
          v-if="!menuModal.permission && menuModal.sql"
          @click="
            rawSqlToSubmit = menuModal.sql;
            ticketData.ticketTitle = `${$t('gong-dan')}${new Date().getTime()}`;
            ticketData.description = '';
            showTicketModal = true;
          "
        >
          {{ $t('ti-jiao-gong-dan') }}
        </a-button>
        <a-button @click="handleCloseModal">{{ $t('guan-bi') }}</a-button>
      </template>
    </CCModal>
    <CCModal :title="sqlModal.title" v-model="sqlModal.show" :mask-closable="false" :closable="true" :keyboard="false" :width="800">
      <div class="content">
        <div class="header" style="margin-bottom: 10px" v-if="currentDdlList.length">
          <a-radio-group v-model:value="sqlModal.type" @change="handleStruct">
            <a-radio value="Request" v-if="currentDdlList.includes('Request')">
              {{ $t('huo-qu-ddl-yu-ju') }}
            </a-radio>
            <a-radio value="Convert" v-if="currentDdlList.includes('Convert')">
              {{ $t('sheng-cheng-ddl-yu-ju') }}
            </a-radio>
          </a-radio-group>
          <a-select
            v-if="currentTargetDsList.length && sqlModal.type === 'Convert'"
            v-model:value="sqlModal.dsType"
            @change="handleStruct"
            style="width: 200px"
          >
            <a-select-option v-for="ds in currentTargetDsList" :value="ds" :key="ds">{{ ds }}</a-select-option>
          </a-select>
        </div>
        <a-collapse
          :bordered="false"
          size="small"
          style="margin: 5px 0"
          v-model:activeKey="menuModal.collapseKey"
          v-if="sqlModal.type === 'Convert'"
          class="advanced-setting"
        >
          <a-collapse-panel key="options" :header="$t('gao-ji-pei-zhi')">
            <div style="padding: 5px">
              <div v-for="setting in structAdvancedSetting" :key="setting.value">
                <a-checkbox :value="menuModal.options[setting.value]" @change="handleMenuOptionChange(setting.value, $event)">
                  {{ setting.label }}
                </a-checkbox>
              </div>
            </div>
          </a-collapse-panel>
        </a-collapse>
        <div style="width: 100%; border: 1px solid #ccc; padding: 3px 10px; overflow: auto; max-height: 500px">
          <a-spin :spinning="sqlModal.loading">
            <pre>{{ sqlModal.sql }}</pre>
          </a-spin>
        </div>
      </div>
      <template #footer>
        <a-button @click="copyText(sqlModal.sql)" type="primary" :disabled="sqlModal.loading">{{ $t('fu-zhi-sql') }}</a-button>
        <a-button @click="handleCloseModal" :disabled="sqlModal.loading">{{ $t('guan-bi') }}</a-button>
      </template>
    </CCModal>
    <CCModal v-model="triggerModal.show" :title="triggerModal.title" :mask-closable="false" :keyboard="false" :width="800">
      <div class="trigger-modal">
        <div v-if="!triggerModal.sql">
          <div style="display: flex; margin-top: 5px">
            <div style="width: 115px">{{ $t('chu-fa-biao-ming') }}:</div>
            <a-input v-model:value="triggerModal.options.triggerTable" size="small" style="width: 100%" disabled />
          </div>
          <create-table-item
            node-type="params"
            :current-schema="component"
            :selected-index="triggerModal.selectedIndex"
            :selected-node="triggerModal.selectedNode"
            :form-data="triggerModal.options"
            v-for="component in triggerModal.selectedSchema"
            :key="component.field"
            :tab="currentTab"
          />
        </div>
        <div v-show="triggerModal.sql" style="width: 100%; border: 1px solid #ccc; padding: 3px 10px; overflow: auto; max-height: 500px">
          <loading :active.sync="sqlModal.loading" :is-full-page="false"></loading>
          <pre>{{ triggerModal.sql }}</pre>
        </div>
      </div>
      <template #footer>
        <a-button type="primary" @click="handleRightClickMenu(actionType)" v-if="!triggerModal.sql" :disabled="!triggerModal.options.name">
          {{ $t('sheng-cheng-sql-yu-ju') }}
        </a-button>
        <a-button v-if="triggerModal.sql" @click="handlePreCreateTrigger">
          {{ $t('fan-hui-xiu-gai') }}
        </a-button>
        <a-button
          v-if="triggerModal.permission && triggerModal.sql"
          type="primary"
          :danger="triggerModal.danger"
          @click="handleDoAction"
          :loading="doActionLoading"
          style="margin-right: 10px"
        >
          {{ $t('li-ji-zhi-hang') }}
        </a-button>
        <a-button
          v-if="!triggerModal.permission && triggerModal.sql"
          @click="
            rawSqlToSubmit = triggerModal.sql;
            ticketData.ticketTitle = `${$t('gong-dan')}${new Date().getTime()}`;
            ticketData.description = '';
            showTicketModal = true;
          "
        >
          {{ $t('ti-jiao-gong-dan') }}
        </a-button>
        <a-button @click="handleCloseModal">{{ $t('guan-bi') }}</a-button>
      </template>
    </CCModal>
    <CCModal v-model="viewModal.show" :title="viewModal.title" :mask-closable="false" :closable="false" :keyboard="false" :width="800">
      <div class="view-modal">
        <div v-if="!viewModal.sql">
          <create-table-item
            node-type="params"
            :current-schema="component"
            :selected-index="viewModal.selectedIndex"
            :selected-node="viewModal.selectedNode"
            :form-data="viewModal.options"
            v-for="component in viewModal.selectedSchema"
            :key="component.field"
            :tab="currentTab"
          />
        </div>
        <div v-show="viewModal.sql" style="width: 100%; border: 1px solid #ccc; padding: 3px 10px; overflow: auto; max-height: 500px">
          <loading :active.sync="viewModal.loading" :is-full-page="false"></loading>
          <pre>{{ viewModal.sql }}</pre>
        </div>
      </div>
      <template #footer>
        <a-button type="primary" @click="handleRightClickMenu(actionType)" v-if="!viewModal.sql" :disabled="!viewModal.options.name">
          {{ $t('sheng-cheng-sql-yu-ju') }}
        </a-button>
        <a-button v-if="viewModal.sql" @click="handlePreCreateTrigger">
          {{ $t('fan-hui-xiu-gai') }}
        </a-button>
        <a-button
          v-if="viewModal.permission && viewModal.sql"
          :type="primary"
          :danger="viewModal.danger"
          @click="handleDoAction"
          :loading="doActionLoading"
          style="margin-right: 10px"
        >
          {{ $t('li-ji-zhi-hang') }}
        </a-button>
        <a-button
          v-if="!viewModal.permission && viewModal.sql"
          @click="
            rawSqlToSubmit = viewModal.sql;
            ticketData.ticketTitle = `${$t('gong-dan')}${new Date().getTime()}`;
            ticketData.description = '';
            showTicketModal = true;
          "
        >
          {{ $t('ti-jiao-gong-dan') }}
        </a-button>
        <a-button @click="handleCloseModal">{{ $t('guan-bi') }}</a-button>
      </template>
    </CCModal>
    <CCModal v-model="functionModal.show" :title="functionModal.title" :mask-closable="false" :closable="false" :keyboard="false" :width="800">
      <div class="function-modal">
        <div v-if="!functionModal.sql">
          <create-table-item
            node-type="params"
            :current-schema="component"
            :selected-index="functionModal.selectedIndex"
            :selected-node="functionModal.selectedNode"
            :form-data="functionModal.options"
            v-for="component in functionModal.selectedSchema"
            :key="component.field"
            :tab="currentTab"
          />
        </div>
        <div v-show="functionModal.sql" style="width: 100%; border: 1px solid #ccc; padding: 3px 10px; overflow: auto; max-height: 500px">
          <loading :active.sync="functionModal.loading" :is-full-page="false"></loading>
          <pre>{{ functionModal.sql }}</pre>
        </div>
      </div>
      <template #footer>
        <a-button type="primary" @click="handleRightClickMenu(actionType)" v-if="!functionModal.sql" :disabled="!functionModal.options.name">
          {{ $t('sheng-cheng-sql-yu-ju') }}
        </a-button>
        <a-button v-if="functionModal.sql" @click="handlePreCreateTrigger">
          {{ $t('fan-hui-xiu-gai') }}
        </a-button>
        <a-button
          v-if="functionModal.permission && functionModal.sql"
          :type="primary"
          :danger="functionModal.danger"
          @click="handleDoAction"
          :loading="doActionLoading"
          style="margin-right: 10px"
        >
          {{ $t('li-ji-zhi-hang') }}
        </a-button>
        <a-button
          v-if="!functionModal.permission && functionModal.sql"
          @click="
            rawSqlToSubmit = functionModal.sql;
            ticketData.ticketTitle = `${$t('gong-dan')}${new Date().getTime()}`;
            ticketData.description = '';
            showTicketModal = true;
          "
        >
          {{ $t('ti-jiao-gong-dan') }}
        </a-button>
        <a-button @click="handleCloseModal">{{ $t('guan-bi') }}</a-button>
      </template>
    </CCModal>
    <CCModal v-model="procedureModal.show" :title="procedureModal.title" :mask-closable="false" :closable="false" :keyboard="false" :width="800">
      <div class="procedure-modal">
        <div v-if="!procedureModal.sql">
          <create-table-item
            node-type="params"
            :current-schema="component"
            :selected-index="procedureModal.selectedIndex"
            :selected-node="procedureModal.selectedNode"
            :form-data="procedureModal.options"
            v-for="component in procedureModal.selectedSchema"
            :key="component.field"
            :tab="currentTab"
          />
        </div>
        <div v-show="procedureModal.sql" style="width: 100%; border: 1px solid #ccc; padding: 3px 10px; overflow: auto; max-height: 500px">
          <loading :active.sync="procedureModal.loading" :is-full-page="false"></loading>
          <pre>{{ procedureModal.sql }}</pre>
        </div>
      </div>
      <template #footer>
        <a-button type="primary" @click="handleRightClickMenu(actionType)" v-if="!procedureModal.sql" :disabled="!procedureModal.options.name">
          {{ $t('sheng-cheng-sql-yu-ju') }}
        </a-button>
        <a-button v-if="procedureModal.sql" @click="handlePreCreateTrigger">
          {{ $t('fan-hui-xiu-gai') }}
        </a-button>
        <a-button
          v-if="procedureModal.permission && procedureModal.sql"
          type="primary"
          :danger="procedureModal.danger"
          @click="handleDoAction"
          :loading="doActionLoading"
          style="margin-right: 10px"
        >
          {{ $t('li-ji-zhi-hang') }}
        </a-button>
        <a-button
          v-if="!procedureModal.permission && procedureModal.sql"
          @click="
            rawSqlToSubmit = procedureModal.sql;
            ticketData.ticketTitle = `${$t('gong-dan')}${new Date().getTime()}`;
            ticketData.description = '';
            showTicketModal = true;
          "
        >
          {{ $t('ti-jiao-gong-dan') }}
        </a-button>
        <a-button @click="handleCloseModal">{{ $t('guan-bi') }}</a-button>
      </template>
    </CCModal>
    <CCModal v-model="dblinkModal.show" :title="dblinkModal.title" :mask-closable="false" :closable="false" :keyboard="false" :width="800">
      <div class="dblink-modal">
        <div v-if="!dblinkModal.sql">
          <create-table-item
            node-type="params"
            :current-schema="component"
            :selected-index="dblinkModal.selectedIndex"
            :selected-node="dblinkModal.selectedNode"
            :form-data="dblinkModal.options"
            v-for="component in dblinkModal.selectedSchema"
            :key="component.field"
            :tab="currentTab"
          />
        </div>
        <div v-show="dblinkModal.sql" style="width: 100%; border: 1px solid #ccc; padding: 3px 10px; overflow: auto; max-height: 500px">
          <loading :active.sync="dblinkModal.loading" :is-full-page="false"></loading>
          <pre>{{ dblinkModal.sql }}</pre>
        </div>
      </div>
      <template #footer>
        <a-button type="primary" @click="handleRightClickMenu(actionType)" v-if="!dblinkModal.sql" :disabled="!dblinkModal.options.name">
          {{ $t('sheng-cheng-sql-yu-ju') }}
        </a-button>
        <a-button v-if="dblinkModal.sql" @click="handlePreCreateTrigger">
          {{ $t('fan-hui-xiu-gai') }}
        </a-button>
        <a-button
          v-if="dblinkModal.permission && dblinkModal.sql"
          type="primary"
          :danger="dblinkModal.danger"
          @click="handleDoAction"
          :loading="doActionLoading"
          style="margin-right: 10px"
        >
          {{ $t('li-ji-zhi-hang') }}
        </a-button>
        <a-button
          v-if="!dblinkModal.permission && dblinkModal.sql"
          @click="
            rawSqlToSubmit = dblinkModal.sql;
            ticketData.ticketTitle = `${$t('gong-dan')}${new Date().getTime()}`;
            ticketData.description = '';
            showTicketModal = true;
          "
        >
          {{ $t('ti-jiao-gong-dan') }}
        </a-button>
        <a-button @click="handleCloseModal">{{ $t('guan-bi') }}</a-button>
      </template>
    </CCModal>
    <CCModal v-model="jobModal.show" :title="jobModal.title" :mask-closable="false" :closable="false" :keyboard="false" :width="800">
      <div class="dblink-modal">
        <div v-if="!jobModal.sql">
          <create-table-item
            node-type="params"
            :current-schema="component"
            :selected-index="jobModal.selectedIndex"
            :selected-node="jobModal.selectedNode"
            :form-data="jobModal.options"
            v-for="component in jobModal.selectedSchema"
            :key="component.field"
            :tab="currentTab"
          />
        </div>
        <div v-show="jobModal.sql" style="width: 100%; border: 1px solid #ccc; padding: 3px 10px; overflow: auto; max-height: 500px">
          <loading :active.sync="jobModal.loading" :is-full-page="false"></loading>
          <pre>{{ jobModal.sql }}</pre>
        </div>
      </div>
      <template #footer>
        <a-button type="primary" @click="handleRightClickMenu(actionType)" v-if="!jobModal.sql" :disabled="!jobModal.options.execSql">
          {{ $t('sheng-cheng-sql-yu-ju') }}
        </a-button>
        <a-button v-if="jobModal.sql" @click="handlePreCreateTrigger">
          {{ $t('fan-hui-xiu-gai') }}
        </a-button>
        <a-button
          v-if="jobModal.permission && jobModal.sql"
          type="primary"
          :danger="jobModal.danger"
          @click="handleDoAction"
          :loading="doActionLoading"
          style="margin-right: 10px"
        >
          {{ $t('li-ji-zhi-hang') }}
        </a-button>
        <a-button
          v-if="!jobModal.permission && jobModal.sql"
          @click="
            rawSqlToSubmit = jobModal.sql;
            ticketData.ticketTitle = `${$t('gong-dan')}${new Date().getTime()}`;
            ticketData.description = '';
            showTicketModal = true;
          "
        >
          {{ $t('ti-jiao-gong-dan') }}
        </a-button>
        <a-button @click="handleCloseModal">{{ $t('guan-bi') }}</a-button>
      </template>
    </CCModal>
    <CCModal v-model="scheduleJobModal.show" :title="scheduleJobModal.title" :mask-closable="false" :closable="false" :keyboard="false" :width="800">
      <div class="dblink-modal">
        <div v-if="!scheduleJobModal.sql">
          <create-table-item
            node-type="params"
            :current-schema="component"
            :selected-index="scheduleJobModal.selectedIndex"
            :selected-node="scheduleJobModal.selectedNode"
            :form-data="scheduleJobModal.options"
            v-for="component in scheduleJobModal.selectedSchema"
            :key="component.field"
            :tab="currentTab"
          />
        </div>
        <div v-show="scheduleJobModal.sql" style="width: 100%; border: 1px solid #ccc; padding: 3px 10px; overflow: auto; max-height: 500px">
          <loading :active.sync="scheduleJobModal.loading" :is-full-page="false"></loading>
          <pre>{{ scheduleJobModal.sql }}</pre>
        </div>
      </div>
      <template #footer>
        <a-button type="primary" @click="handleRightClickMenu(actionType)" v-if="!scheduleJobModal.sql" :disabled="!scheduleJobModal.options.name">
          {{ $t('sheng-cheng-sql-yu-ju') }}
        </a-button>
        <a-button v-if="scheduleJobModal.sql" @click="handlePreCreateTrigger">
          {{ $t('fan-hui-xiu-gai') }}
        </a-button>
        <a-button
          v-if="scheduleJobModal.permission && scheduleJobModal.sql"
          type="primary"
          :danger="scheduleJobModal.danger"
          @click="handleDoAction"
          :loading="doActionLoading"
          style="margin-right: 10px"
        >
          {{ $t('li-ji-zhi-hang') }}
        </a-button>
        <a-button
          v-if="!scheduleJobModal.permission && scheduleJobModal.sql"
          @click="
            rawSqlToSubmit = scheduleJobModal.sql;
            ticketData.ticketTitle = `${$t('gong-dan')}${new Date().getTime()}`;
            ticketData.description = '';
            showTicketModal = true;
          "
        >
          {{ $t('ti-jiao-gong-dan') }}
        </a-button>
        <a-button @click="handleCloseModal">{{ $t('guan-bi') }}</a-button>
      </template>
    </CCModal>
    <CCModal
      :title="genDataModal.title"
      v-model="genDataModal.show"
      :width="1000"
      v-if="genDataModal.show"
      :mask-closable="false"
      :keyboard="false"
      @cancel="handleCloseModal"
    >
      <div class="generate-modal">
        <div class="advanced" v-show="genDataModal.step === 1">
          <div class="title">{{ $t('gao-ji-xuan-xiang') }}</div>
          <div class="options">
            <div class="option">
              <a-input v-model:value="genDataModal.globalConfigs.producer" size="small" :addon-before="$t('sheng-chan-bing-fa-du')" />
              <a-tooltip placement="bottom" style="margin-left: 3px; margin-right: 5px">
                <QuestionCircleOutlined />
                <template #title>
                  {{ $t('zao-shu-ju-zhong-sheng-cheng-sui-ji-shu-ju-de-shu-liang') }}
                </template>
              </a-tooltip>
            </div>
            <div class="option">
              <a-input v-model:value="genDataModal.globalConfigs.writer" size="small" :addon-before="$t('xie-ru-bing-fa-du')" />
              <a-tooltip placement="bottom" style="margin-left: 3px; margin-right: 5px">
                <QuestionCircleOutlined />
                <template #title>
                  {{ $t('zao-shu-ju-zhong-jiang-shu-ju-xie-ru-shu-ju-ku-xian-cheng-de-shu-liang') }}
                </template>
              </a-tooltip>
            </div>
            <div class="option">
              <a-checkbox v-model="genDataModal.globalConfigs.ignoreErrors">
                {{ $t('yu-dao-cuo-wu-shi-ji-xu') }}
              </a-checkbox>
              <a-tooltip placement="bottom" style="margin-left: 3px; margin-right: 5px">
                <QuestionCircleOutlined />
                <template #title>
                  {{ $t('sheng-cheng-shu-ju-shi-ru-yu-dao-duplicate-key-deng-cuo-wu-hu-lve-bing-ji-xu-sheng-cheng-shu-ju') }}
                </template>
              </a-tooltip>
            </div>
            <!--            <div class="option">-->
            <!--              <a-checkbox v-model="genDataModal.globalConfigs.transaction">开启事务</a-checkbox>-->
            <!--              <a-tooltip placement="bottom" style="margin-left: 3px;margin-right: 5px;">-->
            <!--                <a-icon type="question-circle"/>-->
            <!--                <template slot="title">-->
            <!--                  如果数据库支持事务，勾选此选项将开启事务-->
            <!--                </template>-->
            <!--              </a-tooltip>-->
            <!--            </div>-->
          </div>
          <div class="options" style="margin-top: 5px" v-if="actionType === TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TABLE_FAKER_INCREMENT">
            <div class="option">
              <a-input
                v-model:value="genDataModal.globalConfigs.time"
                :addon-before="$t('yun-hang-shi-jian')"
                :addon-after="$t('miao')"
                size="small"
              />
              <a-tooltip placement="bottom" style="margin-left: 3px; margin-right: 5px">
                <QuestionCircleOutlined />
                <template #title>
                  {{ $t('zeng-liang-cao-zuo-yun-hang-shi-jian-dan-wei-miao') }}
                </template>
              </a-tooltip>
            </div>
            <div class="option">
              <a-input v-model:value="genDataModal.globalConfigs.insertRatio" addon-before="INSERT" size="small" addon-after="%" />
              <a-tooltip placement="bottom" style="margin-left: 3px; margin-right: 5px">
                <QuestionCircleOutlined />
                <template #title>
                  {{ $t('cha-ru-cao-zuo-de-bi-shuai') }}
                </template>
              </a-tooltip>
            </div>
            <div class="option">
              <a-input v-model:value="genDataModal.globalConfigs.updateRatio" addon-before="UPDATE" size="small" addon-after="%" />
              <a-tooltip placement="bottom" style="margin-left: 3px; margin-right: 5px">
                <QuestionCircleOutlined />
                <template #title>
                  {{ $t('geng-xin-cao-zuo-de-bi-shuai') }}
                </template>
              </a-tooltip>
            </div>
            <div class="option">
              <a-input v-model:value="genDataModal.globalConfigs.deleteRatio" addon-before="DELETE" size="small" addon-after="%" />
              <a-tooltip placement="bottom" style="margin-left: 3px; margin-right: 5px">
                <QuestionCircleOutlined />
                <template #title>
                  {{ $t('shan-chu-cao-zuo-de-bi-shuai') }}
                </template>
              </a-tooltip>
            </div>
          </div>
        </div>
        <div class="content" v-show="genDataModal.step === 1">
          <div class="title">{{ $t('biao-she-zhi') }}</div>
          <div class="table-setting">
            <div class="left">
              <div class="search">
                <a-input v-model:value="genDataModal.searchKey" size="small" @change="handleGenDataFilter(genDataModal.searchKey)">
                  <template #prefix>
                    <SearchOutlined />
                  </template>
                </a-input>
              </div>
              <div class="datasource-tree">
                <v-tree
                  :emptyText="$t('zan-wu-shu-ju')"
                  checkable
                  cascade
                  ref="genDataTree"
                  key-field="key"
                  :load="handleGenDataExpandLoadNode"
                  :expanded-keys="genDataModal.expandedKeys"
                  :render="renderGenDataNode"
                  :nodeIndent="20"
                  :auto-load="false"
                  @click="handleGenDataSelectTable"
                  @select="handleGenDataSelectTable"
                  @expand="handleGenDataSetExpandedKeys"
                  @check="handleGenDataCheck"
                >
                  <template #empty>
                    <div style="margin-bottom: 10px; font-weight: bold">
                      {{ $t('zan-wu-shu-ju') }}
                    </div>
                    <a-button @click="handleRefreshTree" type="primary">
                      <CustomIcon type="icon-v2-Refresh" />
                    </a-button>
                  </template>
                </v-tree>
              </div>
            </div>
            <div class="right">
              <div v-if="genDataModal.selectedNode">
                <div class="select-title" v-if="genDataModal.selectedNode.configType === 'tableConfig'">
                  <a-breadcrumb separator=">">
                    <a-breadcrumb-item>
                      <b>{{ $t('dang-qian-biao') }}</b>
                      {{ genDataModal.selectedNode.title }}
                    </a-breadcrumb-item>
                  </a-breadcrumb>
                </div>
                <div class="select-title" v-if="genDataModal.selectedNode.configType === 'columnConfig'">
                  <a-breadcrumb separator=">">
                    <a-breadcrumb-item href="">
                      <span @click="handleGoTableConfig">
                        <b>{{ $t('dang-qian-biao') }}</b>
                        {{ genDataModal.selectedNode.tableName }}
                      </span>
                    </a-breadcrumb-item>
                    <a-breadcrumb-item>
                      <b>{{ $t('dang-qian-lie') }}</b>
                      {{ genDataModal.selectedNode.title }}
                    </a-breadcrumb-item>
                  </a-breadcrumb>
                </div>
                <create-table-item
                  v-for="component in genDataModal.selectedSchema"
                  :current-schema="component"
                  :key="component.field"
                  :tab="currentTab"
                  :form-data="genDataModal.tableConfigs"
                  :node-type="genDataModal.selectedNode.configType"
                  :selected-node="genDataModal.selectedNode"
                />
                <div style="display: flex; margin-top: 5px" v-if="genDataModal.selectedNode.configType === 'tableConfig'">
                  <div style="width: 100px; line-height: 24px">{{ $t('pei-zhi-suan-fa') }}</div>
                  <div style="flex: 1">
                    <a-table
                      class="column-config-table"
                      bordered
                      size="small"
                      :columns="configColumns"
                      :data-source="getConfigData"
                      :pagination="false"
                    >
                      <template #genType="{ text, record }">
                        <a @click="handleShowColumnConfigDetail(record)">
                          {{ getGenDetail(record) }}
                        </a>
                      </template>
                    </a-table>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="preview" v-if="genDataModal.step === 2">
          <div class="left">
            <div
              v-for="config in genDataModal.selectedTaleConfigs"
              :style="`background: ${selectedTableConfig.name === config.name ? '#d4e7fc' : '#fff'};padding: 3px 5px;cursor:pointer;font-weight:500`"
              :key="config.name"
              @click="handleSelectTableConfig(config)"
            >
              {{ config.name }}
            </div>
          </div>
          <div class="right">
            <c-c-read-only-table :cell-data="cellData" :column-list="columnList" :selected-config="selectedTableConfig" :width="652" />
            <p class="tips">
              <span>*</span>
              {{ $t('biao-ge-nei-wei-shi-li-shu-ju') }}
            </p>
          </div>
        </div>
        <div class="result" v-if="genDataModal.step === 3">
          <div class="header">
            <div class="status">
              <b>{{ $t('ren-wu-zhuang-tai') }}： &nbsp;&nbsp;&nbsp;&nbsp;</b>
              <span :style="`color:${generateJobStatusColor[genDataModal.status]};font-size:14px`">
                {{ generateJobStatus[genDataModal.status] }}
              </span>
            </div>
            <div class="info-container">
              <div class="info">
                <div class="line">
                  <div>
                    <b>{{ $t('zong-cheng-gong-hang') }}： &nbsp;&nbsp;&nbsp;&nbsp;</b>
                    {{ genDataModal.successTotal }}
                  </div>
                  <div>
                    <b>{{ $t('zong-shi-bai-hang') }}： &nbsp;&nbsp;&nbsp;&nbsp;</b>
                    {{ genDataModal.failedTotal }}
                  </div>
                  <div>
                    <b>{{ $t('ping-jun-xie-ru') }}： &nbsp;&nbsp;&nbsp;&nbsp;</b>
                    {{ genDataModal.writeAvgTime }}
                  </div>
                </div>
                <div class="line">
                  <div>
                    <b>{{ $t('cheng-gong-cha-ru-hang') }}：</b>
                    {{ genDataModal.successInsertTotal }}
                  </div>
                  <div>
                    <b>{{ $t('cheng-gong-shan-chu-hang') }}：</b>
                    {{ genDataModal.successDeleteTotal }}
                  </div>
                  <div>
                    <b>{{ $t('cheng-gong-xiu-gai-hang') }}：</b>
                    {{ genDataModal.successUpdateTotal }}
                  </div>
                </div>
                <div class="line">
                  <div>
                    <b>{{ $t('shi-bai-cha-ru-hang') }}：</b>
                    {{ genDataModal.failedInsertTotal }}
                  </div>
                  <div>
                    <b>{{ $t('shi-bai-shan-chu-hang') }}：</b>
                    {{ genDataModal.failedDeleteTotal }}
                  </div>
                  <div>
                    <b>{{ $t('shi-bai-xiu-gai-hang') }}：</b>
                    {{ genDataModal.failedUpdateTotal }}
                  </div>
                </div>
                <div class="line">
                  <div>
                    <b>{{ $t('ri-zhi-wen-jian') }}：</b>
                    {{ genDataModal.logFile }}
                  </div>
                </div>
                <div class="line" v-if="!isDesktop">
                  <div>
                    <b>{{ $t('ri-zhi-wen-jian-suo-zai-fu-wu-qi') }}：</b>
                    {{ genDataModal.logHost }}
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="logs">
            <div v-if="!genDataModal.logArr.length">{{ $t('huo-qu-shu-ju-ri-zhi-zhong') }}</div>
            <div v-else id="log-list">
              <div class="log" v-for="log in genDataModal.logArr" :key="log.id">{{ log.text }}</div>
            </div>
          </div>
        </div>
      </div>
      <template #footer>
        <a-button
          style="margin-right: 10px"
          v-if="genDataModal.step === 3"
          @click="handleShowLog(-25)"
          :disabled="parseInt(genDataModal.endRow) <= 25 || genDataModal.logLoading || genDataModal.logNextLoading"
        >
          {{ $t('shang-yi-ye') }}
        </a-button>
        <a-button
          style="margin-right: 10px"
          v-if="genDataModal.step === 3"
          @click="handleShowLog(25)"
          :disabled="genDataModal.logLoading || genDataModal.logPreLoading"
        >
          {{ $t('xia-yi-ye') }}
        </a-button>
        <a-button
          style="margin-right: 10px"
          v-if="genDataModal.step === 3"
          @click="handleShowLog(0)"
          :disabled="genDataModal.logPreLoading || genDataModal.logNextLoading"
        >
          <CustomIcon type="icon-v2-Refresh" />
        </a-button>
        <a-button v-if="genDataModal.step === 2" @click="handlePreStep" style="margin-right: 10px">
          {{ $t('shang-yi-bu') }}
        </a-button>
        <a-button type="primary" v-if="genDataModal.step === 1" @click="handlePreview" :loading="genDataModal.loading" style="margin-right: 10px">
          {{ $t('yu-lan') }}
        </a-button>
        <a-button type="primary" v-if="genDataModal.step === 2" style="margin-right: 10px" @click="handleGenData" :loading="genDataModal.loading">
          {{ this.genDataModal.title }}
        </a-button>
        <a-button
          type="primary"
          danger
          v-if="genDataModal.step === 3 && showTaskPauseBtn"
          style="margin-right: 10px"
          :loading="genDataModal.loading"
          @click="handleAsyncEventAction('pause')"
        >
          {{ $t('zan-ting') }}
        </a-button>
        <a-button
          type="primary"
          v-if="genDataModal.step === 3 && showTaskResumeBtn"
          @click="handleAsyncEventAction('resume')"
          style="margin-right: 10px"
          :loading="genDataModal.loading"
        >
          {{ $t('hui-fu') }}
        </a-button>
        <a-button
          type="primary"
          danger
          @click="handleCloseFaker"
          v-if="genDataModal.step === 3 && showTaskCancelBtn"
          style="margin-right: 10px"
          :loading="genDataModal.loading"
        >
          {{ $t('zhong-zhi') }}
        </a-button>
        <a-button @click="handleCloseModal" v-if="genDataModal.step === 3" style="margin-right: 10px">
          {{ genDataModal.status === 'COMPLETE' ? this.$t('guan-bi') : this.$t('zui-xiao-hua') }}
        </a-button>
        <a-button @click="handleCloseModal" v-if="genDataModal.step !== 3">
          {{ $t('guan-bi') }}
        </a-button>
      </template>
    </CCModal>
    <CCModal v-model="showTicketModal" :title="$t('ti-jiao-gong-dan')" @cancel="showTicketModal = false" @ok="showTicketModal = false">
      <a-form :model="ticketData" :rules="ticketRuleValidate" ref="ticketContent" :label-col="{ span: 4 }" :wrapper-col="{ span: 20 }">
        <a-form-item :label="$t('biao-ti')" prop="ticketTitle">
          <a-input v-model="ticketData.ticketTitle" />
        </a-form-item>
        <a-form-item :label="$t('xu-qiu-miao-shu')" prop="description">
          <a-input type="textarea" v-model="ticketData.description" :rows="4" />
        </a-form-item>
      </a-form>
      <template #footer>
        <a-button @click="showTicketModal = false">{{ $t('qu-xiao') }}</a-button>
        <a-button type="primary" @click="submitTicket">{{ $t('que-ding') }}</a-button>
      </template>
    </CCModal>
    <CCModal v-model="propertiesModal.show" :title="$t('shu-xing')" :width="800">
      <div class="properties-modal">
        <div class="left">
          <div
            v-for="prop in properties"
            :key="prop.key"
            @click="handleSelectProperty(prop)"
            :class="`properties ${propertiesModal.selectedProperties.key === prop.key ? 'active' : ''}`"
          >
            <div class="property-title">
              {{ prop.titleI18N }}
            </div>
          </div>
        </div>
        <div class="right">
          <div v-for="child in propertiesModal.selectedProperties.children" :key="child.key" class="property-container">
            <div class="property-container-title">{{ child.titleI18N }}</div>
            <Table :columns="propertyColumns" :data="child.children" size="small" border stripe>
              <template #title="{ row }">
                <div class="flex align-center">
                  <div>{{ row.titleI18N }}</div>
                  <Poptip trigger="hover" placement="right" style="margin-left: 4px" transfer>
                    <CustomIcon type="icon-v2-HelpCircle" size="16px" hoverStyle />
                    <template #content>
                      {{ row.descI18N }}
                    </template>
                  </Poptip>
                </div>
              </template>
              <template #value="{ row }">
                <div v-if="row.value || row.value === ''">
                  {{ row.value }}
                  <Icon class="copy-icon" type="ios-photos-outline" @click="copyText(row.value)" />
                </div>
                <div v-else class="italic font-bold" style="color: #ccc">NULL</div>
              </template>
            </Table>
          </div>
        </div>
      </div>
      <template #footer>
        <a-button @click="handleCloseModal">{{ $t('qu-xiao') }}</a-button>
        <a-button type="primary" @click="handleCloseModal">{{ $t('que-ding') }}</a-button>
      </template>
    </CCModal>
  </div>
</template>

<script lang="jsx">
import { QuestionCircleOutlined, SearchOutlined } from '@ant-design/icons-vue';
import VTree from '@wsfe/vue-tree';
import Loading from 'vue-loading-overlay';
import { mapGetters, mapState } from 'vuex';
import deepClone from 'lodash.clonedeep';
import copyMixin from '@/mixins/copyMixin';
import { ACTION_TYPE, TAB_TYPE } from '@/const';
import datasourceMixin from '@/mixins/datasourceMixin';
import { isMySQL, isRedis } from '@/const/dataSource';
import browseMixin from '@/mixins/browseMixin';
import { FAKER_TASK_STATUS, isOracle, isPG, TABLE_RIGHT_CLICK_MENU_ITEM } from '@/utils';
import utilMixin from '@/mixins/utilMixin';
import { clearAllPending } from '@/services/http/cancelRequest';
import ReadOnlyEditor from '@/components/editor/ReadOnlyEditor';
import CreateTableItem from '@/components/modal/CreateTableItem';
import CCReadOnlyTable from '@/components/widgets/CCReadOnlyTable';
import * as monaco from 'monaco-editor';
import { Modal } from 'ant-design-vue';
import i18n from '@/i18n';
import { nanoid } from 'nanoid';
import ContextMenu from '@imengyu/vue3-context-menu';

const BG_COLOR = {
  Insert: 'rgb(236, 255, 220)',
  Delete: 'rgb(250, 128, 114)',
  Update: 'yellow'
};

const EMPTY_PROCEDURE_DATA = {
  collapseKey: '',
  show: false,
  permission: false,
  sql: '',
  actionData: {},
  selectedSchema: {},
  selectedIndex: -1,
  options: {}
};
const EMPTY_FUNCTION_DATA = {
  collapseKey: '',
  show: false,
  permission: false,
  sql: '',
  actionData: {},
  selectedSchema: [],
  selectedIndex: -1,
  returnParamsForm: [],
  options: {}
};
const EMPTY_DBLINK_DATA = {
  collapseKey: '',
  show: false,
  permission: false,
  sql: '',
  actionData: {},
  selectedSchema: [],
  selectedIndex: -1,
  returnParamsForm: [],
  options: {}
};
const EMPTY_JOB_DATA = {
  collapseKey: '',
  show: false,
  permission: false,
  sql: '',
  actionData: {},
  selectedSchema: [],
  selectedIndex: -1,
  returnParamsForm: [],
  options: {}
};
const EMPTY_SCHEDULE_JOB_DATA = {
  collapseKey: '',
  show: false,
  permission: false,
  sql: '',
  actionData: {},
  selectedSchema: [],
  selectedIndex: -1,
  returnParamsForm: [],
  options: {}
};
const EMPTY_VIEW_DATA = {
  collapseKey: '',
  show: false,
  permission: false,
  sql: '',
  title: '新建视图',
  actionData: {},
  selectedSchema: [],
  options: {}
};
const EMPTY_TRIGGER_DATA = {
  collapseKey: '',
  columnList: [],
  show: false,
  permission: false,
  sql: '',
  title: '',
  actionData: {},
  columns: [],
  selectedSchema: [],
  options: {}
};

const EMPTY_GEN_DATA = {
  loading: false,
  resume: false,
  sessionId: '',
  dataSourceId: '',
  tableConfigColumns: {},
  refresh: true,
  timer: null,
  selectedSchema: [],
  selectedIndex: -1,
  searchKey: '',
  tableConfigs: [],
  selectedTaleConfigs: [],
  treeData: [],
  show: false,
  step: 1,
  previewLoading: false,
  failedDeleteTotal: 0,
  failedInsertTotal: 0,
  failedTotal: 0,
  failedUpdateTotal: 0,
  successDeleteTotal: 0,
  successInsertTotal: 0,
  successTotal: 0,
  successUpdateTotal: 0,
  writeAvgTime: '0毫秒',
  endRow: 0,
  logArr: [],
  logLoading: false,
  logPreLoading: false,
  logNextLoading: false,
  status: '',
  logFile: '',
  logHost: '',
  globalConfigs: {
    producer: 2,
    writer: 4,
    ignoreErrors: true,
    transaction: false,
    insertRatio: '80',
    updateRatio: '10',
    deleteRatio: '10',
    time: '300'
  },
  selectedNode: null,
  expandedKeys: [],
  checkedKeys: []
};
const EMPTY_SQL_DATA = {
  loading: false,
  dsType: '',
  type: 'Request',
  title: i18n.global.t('huo-qu-ddl-yu-ju'),
  show: false,
  sql: '',
  permission: false,
  danger: false,
  collapseKey: '',
  options: {
    delimited: false,
    usingExists: false,
    cascade: false,
    restrict: false,
    purge: false,
    truncateUseDelete: false
  }
};
const EMPTY_MENU_DATA = {
  actionData: {},
  show: false,
  title: '',
  content: '',
  name: '',
  preName: '',
  showNameInput: false,
  sql: '',
  permission: false,
  danger: false,
  collapseKey: '',
  options: {
    delimited: false,
    usingExists: false,
    cascade: false,
    restrict: false,
    purge: false,
    truncateUseDelete: false
  }
};

export default {
  name: 'TableList',
  mixins: [copyMixin, datasourceMixin, browseMixin, utilMixin],
  components: {
    QuestionCircleOutlined,
    SearchOutlined,
    CCReadOnlyTable,
    CreateTableItem,
    ReadOnlyEditor,
    // GenDataModal,
    Loading,
    VTree
  },
  props: {
    rdbObjectDetail: Function,
    getNodeData: Function,
    handleAddTab: Function,
    tableListLoading: Boolean,
    currentTab: {
      type: Object,
      default: () => {}
    },
    listLeaf: Function,
    handleQueryTable: Function,
    listDbTables: Function,
    handleShowStruct: Function,
    handleNewDbQuery: Function,
    handleShowData: Function
  },
  data() {
    return {
      dbClickTimer: null,
      propertyColumns: [
        {
          title: '属性',
          width: 200,
          slot: 'title'
        },
        {
          title: '值',
          slot: 'value'
        }
      ],
      propertiesModal: {
        selectedProperties: [],
        show: false
      },
      propertiesDef: {},
      properties: [],
      procedureModal: deepClone(EMPTY_PROCEDURE_DATA),
      dblinkModal: deepClone(EMPTY_DBLINK_DATA),
      jobModal: deepClone(EMPTY_JOB_DATA),
      scheduleJobModal: deepClone(EMPTY_SCHEDULE_JOB_DATA),
      functionModal: deepClone(EMPTY_FUNCTION_DATA),
      viewModal: deepClone(EMPTY_VIEW_DATA),
      triggerModal: deepClone(EMPTY_TRIGGER_DATA),
      genDataModal: deepClone(EMPTY_GEN_DATA),
      sqlModal: deepClone(EMPTY_SQL_DATA),
      menuModal: deepClone(EMPTY_MENU_DATA),
      FAKER_TASK_STATUS,
      monacoEditor: null,
      defaultOpts: {
        value: '', // 编辑器的值
        language: 'mysql',
        fontSize: 14,
        fontWeight: 'bold',
        theme: 'vs', // 编辑器主题：vs, hc-black, or vs-dark，更多选择详见官网
        minimap: {
          enabled: false
        },
        automaticLayout: true,
        lineNumbers: 'off',
        autoIndent: true // 自动缩进
      },
      functionModalReturnParamsColumns: [
        {
          title: '数据类型',
          scopedSlots: { customRender: 'columnType' }
        },
        {
          title: '长度',
          scopedSlots: { customRender: 'length' }
        },
        {
          title: '小数点',
          scopedSlots: { customRender: 'scale' }
        }
      ],
      functionModalParamsColumns: [
        {
          title: '名称',
          scopedSlots: { customRender: 'name' }
        },
        {
          title: '数据类型',
          scopedSlots: { customRender: 'columnType' }
        },
        {
          title: '长度',
          scopedSlots: { customRender: 'length' }
        },
        {
          title: '小数点',
          scopedSlots: { customRender: 'scale' }
        },
        {
          title: '操作',
          scopedSlots: { customRender: 'action' }
        }
      ],
      procedureModalParamsColumns: [
        {
          title: '名称',
          scopedSlots: { customRender: 'name' }
        },
        {
          title: '模式',
          scopedSlots: { customRender: 'mode' }
        },
        {
          title: '数据类型',
          scopedSlots: { customRender: 'columnType' }
        },
        {
          title: '长度',
          scopedSlots: { customRender: 'length' }
        },
        {
          title: '小数点',
          scopedSlots: { customRender: 'scale' }
        },
        {
          title: '操作',
          scopedSlots: { customRender: 'action' }
        }
      ],
      currentDdlList: [],
      currentTargetDsList: [],
      TABLE_RIGHT_CLICK_MENU_ITEM,
      selectedTableConfig: {},
      schemaDef: {},
      scrollY: 0,
      top: 0,
      selectedNode: null,
      advancedSetting: [
        {
          value: 'delimited',
          label: this.$t('shi-yong-xian-ding-fu-bao-guo-shu-ju-ku-dui-xiang-ming')
        },
        {
          value: 'usingExists',
          label: this.$t('shi-yong-if-exists-zi-ju')
        },
        {
          value: 'cascade',
          label: this.$t('shi-yong-cascade-zi-ju-jin-hang-qiang-zhi-shan-chu')
        },
        {
          value: 'restrict',
          label: this.$t('shi-yong-restrict-zi-ju-zai-xian-zhi-tiao-jian-xia-shan-chu')
        },
        {
          value: 'purge',
          label: this.$t('shi-yong-purge-zi-ju-jin-hang-zi-yuan-hui-shou')
        },
        {
          value: 'truncateUseDelete',
          label: this.$t('shi-yong-delete-yu-ju-ti-dai-truncate-yu-ju')
        }
      ],
      structAdvancedSetting: [
        {
          value: 'delimited',
          label: this.$t('shi-yong-xian-ding-fu-bao-guo-shu-ju-ku-dui-xiang-ming')
        }
      ],
      showTableLoading: false,
      columnList: [],
      previewData: {},
      cellData: [],
      actionType: '',
      menuList: [],
      tableListItemLength: 0,
      doActionLoading: false,
      timeoutMs: 10000,
      timeoutS: 10,
      refreshLoading: false,
      configColumns: [
        {
          title: this.$t('lie-ming'),
          dataIndex: 'name'
        },
        {
          title: this.$t('sheng-cheng-qi'),
          dataIndex: 'seedType'
        },
        {
          title: this.$t('sheng-cheng-fang-shi'),
          dataIndex: 'genType',
          scopedSlots: { customRender: 'genType' }
        }
      ],
      generateJobStatus: {
        INIT: this.$t('chu-shi-hua'),
        RUNNING: this.$t('yun-hang-zhong'),
        PAUSE: this.$t('yi-zan-ting'),
        COMPLETE: this.$t('yi-wan-cheng'),
        WAITING_RESUME: this.$t('hui-fu-zhong'),
        WAITING_PAUSE: this.$t('zan-ting-zhong')
      },
      generateJobStatusColor: {
        INIT: '#ccc',
        RUNNING: '#0BB9F8',
        PAUSE: '#FF6E0D',
        COMPLETE: '#52c41a',
        WAITING_RESUME: '#FF6E0D',
        WAITING_PAUSE: '#FF6E0D'
      },
      showTicketModal: false,
      rawSqlToSubmit: '',
      ticketData: {
        ticketTitle: '',
        description: ''
      },
      ticketRuleValidate: {
        ticketTitle: [
          {
            required: true,
            message: this.$t('biao-ti-bu-neng-wei-kong'),
            trigger: 'blur'
          }
        ],
        description: [
          {
            required: true,
            message: this.$t('xu-qiu-miao-shu-bu-neng-wei-kong'),
            trigger: 'blur'
          }
        ]
      }
    };
  },
  mounted() {
    const currentLeaf = this.currentTab[this.currentTab.leafType];
    if (currentLeaf.treeData) {
      this.handleSetData(currentLeaf.treeData);
    }

    const tableListTreeList = $('.table-list-tree');
    if (tableListTreeList && tableListTreeList.length) {
      tableListTreeList[0].addEventListener('scroll', this.handleSetScrollTop, true);
    }

    this.$bus.on('showFakerModal', (task) => this.showFakerDetail(task));
  },
  beforeUnmount() {
    const tableListTreeList = $('.table-list-tree');
    if (tableListTreeList && tableListTreeList.length) {
      tableListTreeList[0].removeEventListener('scroll', this.handleSetScrollTop, true);
    }
    this.$bus.off('showFakerModal', this.showFakerDetail);
  },
  computed: {
    ...mapState(['globalDsSetting']),
    ...mapGetters([
      'isDesktop',
      'getMenus',
      'getBrowserMenus',
      'getQuickQuery',
      'getDsClassify',
      'genQualifierText',
      'targetDsList',
      'ddlList',
      'isDark'
    ]),
    // 根据当前节点类型返回正确的 leafGroup
    showTaskCancelBtn() {
      const { INIT, RUNNING, PAUSE, WAITING_RESUME, WAITING_PAUSE } = FAKER_TASK_STATUS;
      return [INIT, RUNNING, PAUSE, WAITING_RESUME, WAITING_PAUSE].includes(this.genDataModal.status);
    },
    showTaskPauseBtn() {
      const { RUNNING, WAITING_PAUSE } = FAKER_TASK_STATUS;
      return [RUNNING, WAITING_PAUSE].includes(this.genDataModal.status);
    },
    showTaskRetryBtn() {
      const { COMPLETE } = FAKER_TASK_STATUS;
      return [COMPLETE].includes(this.genDataModal.status);
    },
    showTaskResumeBtn() {
      const { PAUSE, WAITING_RESUME } = FAKER_TASK_STATUS;
      return [PAUSE, WAITING_RESUME].includes(this.genDataModal.status);
    },
    getConfigData() {
      let config = [{}];
      this.genDataModal.tableConfigs.forEach((table) => {
        if (table.key === this.genDataModal.selectedNode.key) {
          config = table.columnConfigs;
        }
      });
      return config;
    },
    getGenDetail() {
      return (record) => {
        let detail = '';
        if (record.genType) {
          detail += record.genType;
        } else {
          detail += this.$t('sui-ji-sheng-cheng');
        }
        if (record.max || record.min) {
          detail += `(${this.$t('qu-jian')}[${record.min}-${record.max}])`;
        }
        if (record.minLength || record.maxLength) {
          detail += `(${this.$t('chang-du-qu-jian')}[${record.minLength}-${record.maxLength}])`;
        }
        return detail;
      };
    }
  },
  methods: {
    handleSelectProperty(prop) {
      this.propertiesModal.selectedProperties = prop;
    },
    async submitTicket() {
      this.$refs.ticketContent.validate(async (valid) => {
        if (valid) {
          const { node } = this.currentTab;
          const dbLevels = this.browseGenLevelsData(node);
          if (this.actionType === TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TABLE_DROP) {
            dbLevels.splice(dbLevels.length - 1, 1, '');
          }
          const data = {
            dbLevels,
            rawSql: this.rawSqlToSubmit,
            description: this.ticketData.description,
            ticketTitle: this.ticketData.ticketTitle,
            force: true
          };
          const res = await this.$services.dmTicketCreate({ data });
          if (res.success) {
            const path = `/ticket/${res.data?.ticketId}`;

            this.$Message.success({
              duration: 2.5,
              render: (h) => {
                return h('div', [
                  this.$t('ti-jiao-cheng-gong'),
                  ', ',
                  h(
                    'a',
                    {
                      style: {
                        position: 'relative',
                        top: '1px',
                        color: '#2d8cf0',
                        textDecoration: 'underline',
                        cursor: 'pointer'
                      },
                      on: {
                        click: () => {
                          this.$router.push(path);
                        }
                      }
                    },
                    this.$t('dian-ji-tiao-zhuan-zhi-gong-dan')
                  )
                ]);
              }
            });
            this.handleCloseModal();
            this.showTicketModal = false;
          } else {
            this.$Message.error(res.msg);
          }
        } else {
          return false;
        }
      });
    },
    handleFunctionReturnParamTypeChange(e) {
      this.columnList.forEach((option) => {
        if (option.value === e) {
          this.functionModal.returnParamsForm = option.children;
          option.children.forEach((child) => {
            this.functionModal.options.returnParams[child.field] = child.defaultVal;
          });
        }
      });
    },
    isPG,
    isOracle,
    isMySQL,
    async handleAsyncEventAction(actionType) {
      try {
        let res;
        let msg;
        this.genDataModal.loading = true;
        switch (actionType) {
          case 'pause':
            res = await this.$services.dmFakerPause({
              data: {
                dataSourceId: this.currentTab.node.INSTANCE.id,
                toolSessionId: this.genDataModal.sessionId
              }
            });
            msg = this.$t('zan-ting-cheng-gong');
            break;
          case 'resume':
            res = await this.$services.dmFakerResume({
              data: {
                dataSourceId: this.currentTab.node.INSTANCE.id,
                toolSessionId: this.genDataModal.sessionId
              }
            });
            msg = this.$t('hui-fu-cheng-gong');
            break;
          default:
            break;
        }

        this.genDataModal.loading = false;
        if (res.success) {
          this.$message.success(msg);
          switch (actionType) {
            case 'pause':
              await this.handleShowLog();
              break;
            case 'resume':
              await this.handleShowLog();
              break;
            default:
              break;
          }
        }
      } catch (e) {
        this.genDataModal.loading = false;
      }
    },
    async showFakerDetail(task) {
      this.genDataModal.step = 3;
      this.genDataModal.show = true;
      const res = await this.$services.dmFakerFetchUiConfig({
        data: {
          toolSessionId: task.bizId
        }
      });

      if (res.success) {
        this.genDataModal = { ...this.genDataModal, ...res.data };
        this.genDataModal.preTableConfigs = deepClone(res.data.tableConfigs);
        this.genDataModal.sessionId = task.bizId;
        this.genDataModal.dataSourceId = res.data.levels[1];
        this.genDataModal.resume = true;
        this.actionType =
          res.data.type === 'FULL'
            ? TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TABLE_FAKER
            : TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TABLE_FAKER_INCREMENT;
        this.genDataModal.title =
          this.actionType === TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TABLE_FAKER ? this.$t('sheng-cheng-shu-ju') : this.$t('ya-li-ce-shi');
        this.genDataModal.endRow = 0;
        await this.handleShowLog();
      }
    },
    handleTriggerColumnsChange(value) {
      if (!value.includes('UPDATE')) {
        this.triggerModal.options.triggerColumns = [];
      }
    },
    handleAddFunctionParam() {
      const param = {
        name: 'param_name',
        key: nanoid()
      };
      if (this.functionModal.selectedSchema) {
        this.functionModal.selectedSchema.forEach((schema) => {
          param[schema.field] = schema.defaultVal;
          if (schema.options) {
            schema.options.forEach((option) => {
              if (option.value === schema.defaultVal) {
                if (option.children && option.children.length) {
                  schema.children = [option];
                  option.children.forEach((child) => {
                    param[child.field] = child.defaultVal;
                  });
                }
              }
            });
          }
        });
      }
      this.functionModal.options.params.unshift(param);
      this.functionModal.selectedIndex = this.functionModal.options.params.length - 1;
      this.functionModal.selectedNode = this.functionModal.options.params[this.functionModal.selectedIndex];
    },
    handleClickFunctionParam(index) {
      this.functionModal.selectedIndex = index;
      this.functionModal.selectedNode = this.functionModal.options.params[index];
    },
    handleRemoveFunctionParam() {
      if (this.functionModal.options.params.length) {
        this.functionModal.options.params.splice(this.functionModal.selectedIndex, 1);
        this.functionModal.selectedIndex = -1;
        this.functionModal.selectedNode = {};
      }
    },
    handleAddProcedureParam() {
      const param = {
        name: 'param_name',
        key: nanoid()
      };
      if (this.procedureModal.selectedSchema) {
        this.procedureModal.selectedSchema.forEach((schema) => {
          param[schema.field] = schema.defaultVal;
          if (schema.options) {
            schema.options.forEach((option) => {
              if (option.value === schema.defaultVal) {
                if (option.children && option.children.length) {
                  schema.children = [option];
                  option.children.forEach((child) => {
                    param[child.field] = child.defaultVal;
                  });
                }
              }
            });
          }
        });
      }
      this.procedureModal.options.params.unshift(param);
      this.procedureModal.selectedIndex = this.procedureModal.options.params.length - 1;
      this.procedureModal.selectedNode = this.procedureModal.options.params[this.procedureModal.selectedIndex];
    },
    handleClickProcedureParam(index) {
      this.procedureModal.selectedIndex = index;
      this.procedureModal.selectedNode = this.procedureModal.options.params[index];
    },
    handleRemoveProcedureParam() {
      if (this.procedureModal.options.params.length) {
        this.procedureModal.options.params.splice(this.procedureModal.selectedIndex, 1);
        this.procedureModal.selectedIndex = -1;
        this.procedureModal.selectedNode = {};
      }
    },
    async handleStruct() {
      this.sqlModal.loading = true;
      const { node, selectedTable: table, leafType } = this.currentTab;
      if (this.sqlModal.type === 'Request') {
        const res = await this.$services.dmBrowseActionsRequestScript({
          data: {
            levels: this.browseGenLevelsData(node),
            targetType: leafType,
            targetName: table.title
          }
        });

        if (res.success) {
          this.sqlModal.sql = res.data.sql;
        }
      } else if (this.sqlModal.type === 'Convert') {
        const res = await this.$services.dmBrowseActionsConvertDDL({
          data: {
            levels: this.browseGenLevelsData(node),
            leafType,
            sourceTableName: table.title,
            targetDsType: this.sqlModal.dsType,
            options: this.sqlModal.options
          }
        });

        if (res.success) {
          this.sqlModal.sql = res.data.sql;
        }
      }

      this.sqlModal.loading = false;
    },
    handleSelectTableConfig(tableConfig) {
      this.selectedTableConfig = tableConfig;
      const cellData = [];
      const currentTableData = this.previewData[tableConfig.name];
      if (currentTableData && currentTableData.length) {
        this.columnList = this.genDataModal.tableConfigColumns[tableConfig.name];
        this.columnList.forEach((column, columnIndex) => {
          currentTableData.forEach((row, rowIndex) => {
            const cell = {
              r: rowIndex,
              c: columnIndex,
              v: {
                ct: {
                  fa: '@',
                  t: 's'
                },
                v: row.type === 'Delete' ? row.oldValue[column] : row.newValue[column],
                m: row.type === 'Delete' ? row.oldValue[column] : row.newValue[column],
                bg: row.type === 'Update' ? (row.oldValue[column] !== row.newValue[column] ? BG_COLOR[row.type] : '#fff') : BG_COLOR[row.type]
              }
            };
            if (row.type === 'Update') {
              cell.v.ps = {
                value: row.oldValue[column]
              };
            }
            cellData.push(cell);
          });
        });
      }

      this.cellData = cellData;
    },
    handleShowColumnConfigDetail(record) {
      this.$refs.genDataTree.setSelected(record.key, true);
    },
    handleGoTableConfig() {
      console.log(11223344, this.genDataModal.selectedNode.key);
      const currentKey = this.genDataModal.selectedNode.key;
      const lastIndex = currentKey.lastIndexOf('.');
      const tableKey = currentKey.substring(0, lastIndex);

      console.log('tableKey', tableKey);
      this.$refs.genDataTree.setSelected(tableKey, true);
    },
    handlePreStep() {
      this.genDataModal.refresh = true;
      if (this.genDataModal.step === 2) {
        this.genDataModal.step -= 1;
      }
      if (this.genDataModal.step === 3) {
        if (this.genDataModal.status !== 'COMPLETE') {
          Modal.confirm({
            title: this.$t('ti-shi'),
            content: this.$t('dian-ji-shang-yi-bu-hui-guan-bi-thisgendatamodaltitle-que-ding-yao-jin-hang-shang-yi-bu-cao-zuo-ma', [
              this.genDataModal.title
            ]),
            onOk: () => {
              this.genDataModal.step -= 2;
              this.clearLogInterVal();
              this.handleCloseFaker();
              if (this.genDataModal.resume) {
                this.handleRightClickMenu(this.actionType);
              }
            }
          });
        } else {
          this.genDataModal.step -= 2;
        }
      }
    },
    highlightLog(log) {
      if (log.includes('[ERROR]')) {
        return this.highlightStrict(log, '[ERROR]', 'color: red', 'b');
      }
      if (log.includes('[TRACE]')) {
        return this.highlightStrict(log, '[TRACE]', 'color: gray', 'b');
      }
      if (log.includes('[DEBUG]')) {
        return this.highlightStrict(log, '[DEBUG]', 'color: gray', 'b');
      }
      if (log.includes('[INFO]')) {
        return this.highlightStrict(log, '[INFO]', 'color: green', 'b');
      }
      if (log.includes('[WARN]')) {
        return this.highlightStrict(log, '[WARN]', 'color: yellow', 'b');
      }

      return log;
    },
    async handlePreview() {
      const { node } = this.currentTab;
      const checkedKeys = this.$refs.genDataTree.getCheckedKeys();
      console.log('checkedKeys', checkedKeys);
      const tableConfigs = [];
      const tableConfigColumns = {};
      this.genDataModal.tableConfigs.forEach((tableConfig) => {
        const selectedTableConfig = deepClone(tableConfig);
        tableConfigColumns[selectedTableConfig.name] = [];
        selectedTableConfig.columnConfigs = [];
        delete selectedTableConfig.key;
        if (tableConfig.columnConfigs && tableConfig.columnConfigs.length) {
          tableConfig.columnConfigs.forEach((columnConfig) => {
            if (checkedKeys.includes(columnConfig.key)) {
              const selectedColumnConfig = {
                seedConfig: {}
              };
              Object.keys(columnConfig).forEach((key) => {
                if (['name', 'seedType', 'ignoreColsInsert', 'ignoreColsUpdate', 'ignoreColsUpdateWhere', 'ignoreColsDeleteWhere'].includes(key)) {
                  selectedColumnConfig[key] = columnConfig[key];
                } else {
                  if (key !== 'key') {
                    selectedColumnConfig.seedConfig[key] = columnConfig[key];
                  }
                }
              });
              delete selectedColumnConfig.key;
              selectedTableConfig.columnConfigs.push(selectedColumnConfig);
              tableConfigColumns[selectedTableConfig.name].push(selectedColumnConfig.name);
            }
          });
        }
        if (this.genDataModal.resume && checkedKeys.includes(tableConfig.key)) {
          this.genDataModal.preTableConfigs.forEach((preTableConfig) => {
            if (preTableConfig.name === tableConfig.name) {
              selectedTableConfig.columnConfigs = preTableConfig.columnConfigs;
              tableConfigColumns[selectedTableConfig.name] = [];
              preTableConfig.columnConfigs.forEach((preColumn) => {
                tableConfigColumns[selectedTableConfig.name].push(preColumn.name);
              });
            }
          });
        }

        if (selectedTableConfig.columnConfigs.length) {
          tableConfigs.push(selectedTableConfig);
        }
      });
      this.genDataModal.tableConfigColumns = tableConfigColumns;

      try {
        this.genDataModal.loading = true;
        const res = await this.$services.dmFakerPreview({
          data: {
            levels: this.browseGenLevelsData(node),
            ...this.genDataModal.globalConfigs,
            tableConfigs,
            type: this.actionType === TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TABLE_FAKER_INCREMENT ? 'INCREMENT' : 'FULL'
          }
        });
        this.genDataModal.loading = false;

        this.genDataModal.selectedTaleConfigs = tableConfigs;

        if (res.success) {
          this.genDataModal.step++;
          if (res.success) {
            if (res.data) {
              this.previewData = res.data;
              const cellData = [];
              const currentTableConfig = tableConfigs[0];
              this.selectedTableConfig = currentTableConfig;
              const currentTableData = this.previewData[currentTableConfig.name];
              if (currentTableData && currentTableData.length) {
                this.columnList = tableConfigColumns[currentTableConfig.name];
                this.columnList.forEach((column, columnIndex) => {
                  currentTableData.forEach((row, rowIndex) => {
                    const cell = {
                      r: rowIndex,
                      c: columnIndex,
                      v: {
                        ct: {
                          fa: '@',
                          t: 's'
                        },
                        v: row.type === 'Delete' ? row.oldValue[column] : row.newValue[column],
                        m: row.type === 'Delete' ? row.oldValue[column] : row.newValue[column],
                        bg: row.type === 'Update' ? (row.oldValue[column] !== row.newValue[column] ? BG_COLOR[row.type] : '#fff') : BG_COLOR[row.type]
                      }
                    };
                    if (row.type === 'Update') {
                      cell.v.ps = {
                        value: row.oldValue[column]
                      };
                    }
                    cellData.push(cell);
                  });
                });

                this.cellData = cellData;
              }
            }
          }
        }
      } catch (e) {
        this.genDataModal.loading = false;
      }
    },
    async handleGenData() {
      try {
        this.genDataModal.loading = true;
        const res = await this.$services.dmFakerExecute({
          data: {
            levels: this.browseGenLevelsData(this.currentTab.node),
            ...this.genDataModal.globalConfigs,
            tableConfigs: this.genDataModal.selectedTaleConfigs,
            type: this.actionType === TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TABLE_FAKER_INCREMENT ? 'INCREMENT' : 'FULL'
          }
        });
        this.genDataModal.loading = false;
        if (res.success) {
          this.genDataModal.sessionId = res.data;
          this.genDataModal.step++;
          await this.handleShowLog();
        }
      } catch (e) {
        this.genDataModal.loading = false;
      }
    },
    async handleObtainLog(loop = true) {
      this.refreshLoading = true;
      this.clearLogInterVal();
      const obtainLog = async () => {
        const res = await this.$services.dmFakerTailLog({
          data: {
            dataSourceId: this.genDataModal.dataSourceId,
            toolSessionId: this.genDataModal.sessionId,
            startLine: this.genDataModal.startLine
          }
        });

        if (res.success) {
          const {
            status,
            endLine,
            failedDeleteTotal,
            failedInsertTotal,
            failedTotal,
            failedUpdateTotal,
            successDeleteTotal,
            successInsertTotal,
            successTotal,
            successUpdateTotal,
            writeAvgTime,
            logArr,
            logFile,
            logHost
          } = res.data;
          this.genDataModal.status = status;
          if (status !== FAKER_TASK_STATUS.COMPLETE) {
            this.genDataModal.failedTotal = failedTotal;
            this.genDataModal.failedInsertTotal = failedInsertTotal;
            this.genDataModal.failedDeleteTotal = failedDeleteTotal;
            this.genDataModal.failedUpdateTotal = failedUpdateTotal;
            this.genDataModal.successTotal = successTotal;
            this.genDataModal.successInsertTotal = successInsertTotal;
            this.genDataModal.successDeleteTotal = successDeleteTotal;
            this.genDataModal.successUpdateTotal = successUpdateTotal;
            this.genDataModal.writeAvgTime = writeAvgTime;
            this.genDataModal.logFile = logFile;
            this.genDataModal.logHost = logHost;
          }
          if (logArr && logArr.length) {
            this.genDataModal.logArr = logArr.reverse().concat(this.genDataModal.logArr);
          }
          // if (status === FAKER_TASK_STATUS.COMPLETE || status === FAKER_TASK_STATUS.PAUSE) {
          //   this.clearLogInterVal();
          // }
        }
        this.refreshLoading = false;
      };
      await obtainLog();
      // if (loop || this.genDataModal.refresh) {
      //   this.genDataModal.timer = window.setInterval(obtainLog, this.timeoutMs);
      // }
    },
    async getFakerDef() {
      const { selectedTable, node } = this.currentTab;
      try {
        this.genDataModal.loading = true;
        const defRes = await this.$services.dmFakerFakerDef({
          data: {
            levels: this.browseGenLevelsData(node),
            table: selectedTable.title,
            type: this.actionType === TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TABLE_FAKER_INCREMENT ? 'INCREMENT' : 'FULL'
          }
        });
        this.genDataModal.loading = false;
        if (defRes.success) {
          this.schemaDef = defRes.data;
        }
      } catch (e) {
        this.genDataModal.loading = false;
        console.error(e);
      }
    },
    async getInitTableFaker(node, params = {}, resolve, reject) {
      console.log('init faker');
      const { checked, selected, expand, preTableConfig } = params;
      try {
        this.genDataModal.loading = true;
        const initRes = await this.$services.dmFakerInitFaker({
          data: {
            levels: this.browseGenLevelsData(this.currentTab.node),
            table: node.title,
            type: this.actionType === TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TABLE_FAKER_INCREMENT ? 'INCREMENT' : 'FULL'
          }
        });
        await this.$refs.genDataTree.setChecked(node.key, false);
        this.genDataModal.loading = false;
        if (initRes.success) {
          const children = [];
          initRes.data.columns.forEach((column) => {
            const { seedType, name, support, ignoreAct } = column;
            let preChecked = true;
            if (preTableConfig) {
              const preColIndex = preTableConfig.columnConfigs.findIndex((col) => col.name === name);
              if (preColIndex === -1) {
                preChecked = false;
              }
            }

            children.push({
              ignoreAct,
              title: name,
              seedType,
              name,
              disabled: !support,
              tableName: node.title,
              isLeaf: true,
              checked: preChecked && support,
              configType: 'columnConfig',
              key: `${node.key}.\`${name}\``
            });
          });

          node._loaded = true;
          if (resolve) {
            if (selected) {
              await this.$refs.genDataTree.setSelected(node.key, true);
            }
            if (expand) {
              await this.$refs.genDataTree.setExpand(node.key, true);
            }
            if (checked) {
              console.log('checked', node.key);
              await this.$refs.genDataTree.setChecked(node.key, true);
            }
            resolve(children);
          }
        } else {
          if (reject) {
            console.log('reject1');
            reject();
          }
        }
      } catch (e) {
        this.genDataModal.loading = false;
        if (reject) {
          console.error('reject2', e);
          reject(e);
        }
      }
    },
    async handleGenDataCheck(node) {
      console.log('check');
      if (!node.resume) {
        this.$refs.genDataTree.setSelected(node.key, true); // 设置选中状态
        await this.$refs.genDataTree.setExpand(node.key, true);
        setTimeout(() => {
          this.$refs.genDataTree.setChecked(node.key, true);
        });
      }
    },
    async handleGenDataSetCheckedKeys(keys, checked = true) {
      console.log('handleGenDataSetCheckedKeys', keys);
      this.genDataModal.checkedKeys = keys;
      await this.$refs.genDataTree.setCheckedKeys(keys, checked);
    },
    async handleGenDataGetCheckedKeys() {
      console.log('handleGenDataGetCheckedKeys');
      const checkedKeys = await this.$refs.genDataTree.getCheckedKeys();
      return checkedKeys;
    },
    handleSetScrollTop(e) {
      this.scrollY = e.target.scrollTop;
    },
    handleEleScroll(top) {
      const eleList = $('.table-list-tree .ctree-tree__scroll-area');
      if (eleList && eleList.length) {
        eleList[0].scrollTo({ top });
      }
    },
    async handleSearch() {
      await this.handleFilter(this.currentTab[this.currentTab.leafType].searchKey);
      if (this.currentTab[this.currentTab.leafType].treeData && this.currentTab[this.currentTab.leafType].treeData.length) {
        this.$refs.tableTree.scrollTo(this.currentTab[this.currentTab.leafType].treeData[0].key);
      }
    },
    handleDblClick(node) {
      console.log('dbl click');
      let sql = '';
      const dsQueryMap = this.getQuickQuery(this.currentTab.dsType);
      const dsClassify = this.getDsClassify(this.currentTab.dsType);

      const { selectedTable } = this.currentTab;
      const needQualifier = node.nodeType === 'TABLE' || node.nodeType === 'VIEW' || node.nodeType === 'COLUMN' || node.nodeType === 'KEY';

      if (dsQueryMap[node.nodeType]) {
        sql = dsQueryMap[node.nodeType];
        if (dsClassify === 'CACHE') {
          sql = sql.replace('%%KEY%%', needQualifier ? this.genQualifierText(this.currentTab.dsType, selectedTable.title) : selectedTable.title);
        } else {
          sql = sql.replace('%%TABLE%%', needQualifier ? this.genQualifierText(this.currentTab.dsType, selectedTable.title) : selectedTable.title);
          sql = sql.replace(
            '%%SCHEMA%%',
            needQualifier ? this.genQualifierText(this.currentTab.dsType, this.currentTab.node.SCHEMA.name) : this.currentTab.node.SCHEMA.name
          );
          sql = sql.replace('%%COLUMN%%', needQualifier ? this.genQualifierText(this.currentTab.dsType, node.title) : node.title);
          sql = sql.replace('%%PARAM%%', (node.objAttr && node.objAttr.rdb_param) || '');
        }
      }

      this.handleQueryTable(sql);
    },
    handleRefreshTree() {
      this.scrollY = 0;
      this.currentTab.expandedKeys = [];
      this.listLeaf(true);
    },
    handleFocus() {
      if (this.selectedNode) {
        this.handleScrollTo(this.selectedNode.key);
      }
    },
    handleScrollTo(key) {
      this.$refs.tableTree.scrollTo(key);
    },
    handleGenDataScrollTo(key) {
      this.$refs.genDataTree.scrollTo(key);
    },
    isExpandedKey(node) {
      return this.currentTab.expandedKeys.includes(node.key);
    },
    isGenDataExpandedKey(key) {
      return this.genDataModal.expandedKeys.includes(key);
    },
    // handleSetExpandedKeys(node) {
    //   console.log('handleSetExpandedKeys');
    //   const { key } = node;
    //   if (this.isExpandedKey(node)) {
    //     this.currentTab.expandedKeys = this.currentTab.expandedKeys.filter((k) => k !== key);
    //   } else {
    //     this.currentTab.expandedKeys.push(key);
    //   }
    // },
    handleGenDataSetExpandedKeys(node) {
      const { key } = node;
      if (this.isGenDataExpandedKey(key)) {
        this.genDataModal.expandedKeys = this.genDataModal.expandedKeys.filter((k) => k !== key);
      } else {
        this.genDataModal.expandedKeys.push(key);
      }
    },
    handleNodeClick(node) {
      this.handleSetSelected(node);
    },
    async handleExpandLoadNode(node, resolve) {
      console.log('handleExpandLoadNode');
      await this.getNodeData(node, {}, resolve);
    },
    async handleGenDataExpandLoadNode(node, resolve, reject) {
      if (node && node.title) {
        let preTableConfig = null;
        if (node.resume) {
          preTableConfig = this.genDataModal.preTableConfigs.find((tableConfig) => tableConfig.name === node.title);
        }
        await this.getInitTableFaker(
          node,
          {
            checked: false,
            selected: true,
            expand: false,
            preTableConfig
          },
          (children) => {
            this.genDataModal.tableConfigs.forEach((tableConfig) => {
              if (tableConfig.name === node.title) {
                children.forEach((child) => {
                  const columnConfig = {
                    name: child.title,
                    seedType: child.seedType,
                    key: child.key
                  };
                  this.schemaDef.uiPanels.columnConfig.children.forEach((child2) => {
                    console.log('child2', child2);
                    if (child2.type === 'Options') {
                      child2.options.forEach((option) => {
                        if (option.value === child.seedType) {
                          option.children.forEach((child3) => {
                            columnConfig[child3.field] = child3.defaultVal;
                          });
                        }
                      });
                    } else {
                      columnConfig[child2.field] = child2.defaultVal;
                      if (child2.type === 'Check') {
                        if (columnConfig[child2.field] === 'true') {
                          columnConfig[child2.field] = true;
                        }
                        if (columnConfig[child2.field] === 'false') {
                          columnConfig[child2.field] = false;
                        }
                      }
                    }
                  });
                  if (child.ignoreAct && child.ignoreAct.length) {
                    child.ignoreAct.forEach((ignoreConfig) => {
                      columnConfig[ignoreConfig] = true;
                    });
                  }
                  tableConfig.columnConfigs.push(columnConfig);
                });
              }
            });
            console.log('children', children);
            resolve(children);
          },
          reject
        );
        node.expand = false;
        console.log('node', node);
      } else {
        resolve();
      }
    },
    handleFilter(searchKey) {
      console.log('searchKey', searchKey);
      if (this.$refs.tableTree) {
        this.$refs.tableTree.filter(searchKey);
      }
    },
    handleGenDataFilter(searchKey) {
      if (this.$refs.genDataTree) {
        this.$refs.genDataTree.filter(searchKey);
      }
    },
    async handleSetData(data) {
      if (this.$refs.tableTree) {
        this.top = this.scrollY;
        await this.$refs.tableTree.setData(data);
        await this.handleSearch();
        this.$nextTick(() => {
          this.handleEleScroll(this.top);
        });
      }
    },
    handleGenDataSetData(data) {
      if (this.$refs.genDataTree) {
        this.$refs.genDataTree.setData(data);
      }
    },
    handleSetSelected(node, selected = true) {
      if (this.$refs.tableTree) {
        this.selectedNode = node;
        this.currentTab.selectedTable = {
          title: this.getRootNode(node).title
        };
        this.$refs.tableTree.setSelected(node.key, selected);
      }
    },
    async handleGenDataSetSelected(node, selected = true) {
      if (this.$refs.genDataTree) {
        this.genDataModal.selectedNode = node;
        this.genDataModal.selectedSchema = this.schemaDef.uiPanels[node.configType].children;
        let selectedData = {};
        if (node.configType === 'tableConfig') {
          this.genDataModal.tableConfigs.forEach((tableConfig) => {
            if (tableConfig.name === node.title) {
              selectedData = tableConfig;
            }
          });
        } else {
          this.genDataModal.tableConfigs.forEach((tableConfig) => {
            if (tableConfig.name === node.tableName) {
              tableConfig.columnConfigs.forEach((columnConfig) => {
                if (columnConfig.name === node.title) {
                  selectedData = columnConfig;
                }
              });
            }
          });
        }
        const generateOptionSchema = (item) => {
          if (item.type === 'Options' && selectedData) {
            const optionData = selectedData[item.field];
            item.options.forEach((option) => {
              if (option.value === optionData) {
                if (option.children && option.children.length) {
                  item.children = option.children;
                  option.children.forEach((child) => {
                    if (!(child.field in selectedData)) {
                      selectedData[child.field] = child.defaultVal;
                    }
                    generateOptionSchema(child);
                  });
                } else {
                  item.children = [];
                }
              }
            });
          }
          if (!(item.field in selectedData)) {
            selectedData[item.field] = item.defaultVal;
          }
        };
        this.genDataModal.selectedSchema.forEach((item) => {
          generateOptionSchema(item);
        });
        await this.$refs.genDataTree.setSelected(node.key, selected);
      }
    },
    getRootNode(node) {
      let rootNode = deepClone(node);
      if (rootNode.leafKey !== rootNode.key) {
        if (rootNode._parent) {
          if (rootNode._parent.key === rootNode.leafKey) {
            rootNode = rootNode._parent;
          } else {
            if (rootNode._parent._parent) {
              if (rootNode._parent._parent.key === rootNode.leafKey) {
                rootNode = rootNode._parent._parent;
              }
            }
          }
        }
      }
      return rootNode;
    },
    renderNode(node) {
      const { title, icon, tips, children, nodeType, objName, objAttr, isLeaf } = node;

      return (
        <div class={['node']} style={isLeaf ? 'position: relative; left: -20px;' : ''} key={title}>
          {icon && <cc-svg-icon name={icon} />}
          {objAttr && objAttr.rdb_disabled === 'true' && (
            <Tooltip content={'禁用'} transfer>
              <Icon type={'md-close-circle'} color='red' />
            </Tooltip>
          )}
          {objAttr && objAttr.rdb_invalid === 'true' && (
            <Tooltip content={'编译未通过'} transfer>
              <Icon type={'md-close-circle'} color='red' />
            </Tooltip>
          )}
          <div style={{ marginLeft: '3px' }} innerHTML={this.highlight(objName || title, this.currentTab[this.currentTab.leafType].searchKey)}></div>
          {children && ['GROUP', 'RDB_PARAM'].includes(nodeType) && <div style='font-weight: bold;color: #bbb;'>[{children.length}]</div>}
          {tips && <div style={{ marginLeft: '3px', color: '#ccc' }}>{tips}</div>}
          {objAttr && objAttr.rdb_tips && <div style='color: #bbb;'>&nbsp;{objAttr.rdb_tips}</div>}
        </div>
      );
    },
    renderGenDataNode(node) {
      const { title, icon, tips, children, nodeType } = node;
      return (
        <div class={['node']} key={title}>
          {icon && <cc-svg-icon name={icon} />}
          <div style={{ marginLeft: '3px' }} innerHTML={this.highlight(title, this.genDataModal.searchKey)}></div>
          {children && children.length > 0 && nodeType.includes('GROUP') && <div style='font-weight: bold;color: #bbb;'>[{children.length}]</div>}
          {tips && <div style={{ marginLeft: '3px', color: '#ccc' }}>{tips}</div>}
        </div>
      );
    },
    isRedis,
    handleSetExpandAll(expand = true) {
      this.$refs.tableTree.setExpandAll(expand);
    },
    handleSelectTable(table) {
      console.log('handle select table');
      this.handleSetSelected(table);
    },
    handleGenDataSelectTable(table) {
      console.log('handleGenDataSelectTable');
      this.handleGenDataSetSelected(table);
    },
    handleVTreeClick(table) {
      this.handleSelectTable(table);
      if (this.dbClickTimer) {
        clearTimeout(this.dbClickTimer);
        this.dbClickTimer = null;
        this.handleDblClick(table);
        return;
      }

      this.dbClickTimer = setTimeout(() => {
        this.dbClickTimer = null;
      }, 200);
    },
    handleNodeRightClick(table) {
      this.handleSetSelected(table);
    },
    handleChangeTab(leafType) {
      this.currentTab.leafType = leafType;
      const refreshCache = true;
      this.listLeaf(refreshCache);
    },
    onContextmenu(event) {
      const node = this.$refs.tableTree.getSelectedNode();
      const isNotNode = event.target.classList && event.target.classList.length && event.target.classList[0] === 'vtree-tree__block-area';
      const items = [];
      const menuList = this.getBrowserMenus(this.currentTab.dsType, isNotNode || !node ? this.currentTab.leafType : node.nodeType);
      console.log(event, node, menuList);
      if (menuList && menuList.length) {
        if (isNotNode || !node) {
          menuList.forEach((menu) => {
            if (menu.menuId !== 'MENU_SEPARATOR' && !menu.needTarget) {
              items.push({
                label: menu.i18n,
                svgProps: {
                  class: 'svg-icon'
                },
                svgIcon: `#icon-svg-${menu.menuId}`,
                onClick: () => this.handleRightClickMenu(menu.menuId)
              });
            }
          });
        } else {
          menuList.forEach((menu, menuIndex) => {
            if (menu.menuId !== 'MENU_SEPARATOR') {
              items.push({
                label: menu.i18n,
                svgProps: {
                  class: 'svg-icon'
                },
                svgIcon: `#icon-svg-${menu.menuId}`,
                divided: menuList[menuIndex + 1] && menuList[menuIndex + 1].menuId === 'MENU_SEPARATOR',
                onClick: () => this.handleRightClickMenu(menu.menuId)
              });
            }
          });
        }

        if (items.length) {
          ContextMenu.showContextMenu({
            x: event.x,
            y: event.y,
            theme: 'flat',
            items,
            event,
            customClass: 'custom-class',
            zIndex: 99,
            minWidth: 100
          });
        }
      }
    },
    clearLogInterVal() {
      if (this.genDataModal.timer) {
        window.clearInterval(this.genDataModal.timer);
        this.genDataModal.timer = null;
      }
    },
    async handleCloseFaker() {
      try {
        this.genDataModal.loading = true;
        this.clearLogInterVal();
        const res = await this.$services.dmFakerClose({
          data: {
            dataSourceId: this.currentTab.node.INSTANCE.id,
            toolSessionId: this.genDataModal.sessionId
          }
        });
        this.genDataModal.loading = false;
        if (res.success) {
          this.handleShowLog();
        }
      } catch (e) {
        this.genDataModal.loading = false;
      }
    },
    async handleShowLog(step = 0) {
      if (step === 25) {
        this.genDataModal.logNextLoading = true;
      } else if (step === -25) {
        this.genDataModal.logPreLoading = true;
      } else {
        this.genDataModal.logLoading = true;
      }

      const startLine = this.genDataModal.startLine + step < 0 || step === 0 ? 0 : this.genDataModal.startLine + step;
      this.genDataModal.startLine = startLine;

      const res = await this.$services.dmFakerTailLog({
        data: {
          dataSourceId: this.genDataModal.dataSourceId,
          toolSessionId: this.genDataModal.sessionId,
          startLine
        }
      });

      if (step === 25) {
        this.genDataModal.logNextLoading = false;
      } else if (step === -25) {
        this.genDataModal.logPreLoading = false;
      } else {
        this.genDataModal.logLoading = false;
      }

      if (res.success) {
        const {
          status,
          endLine,
          failedDeleteTotal,
          failedInsertTotal,
          failedTotal,
          failedUpdateTotal,
          successDeleteTotal,
          successInsertTotal,
          successTotal,
          successUpdateTotal,
          writeAvgTime,
          logArr,
          logFile,
          logHost
        } = res.data;
        this.genDataModal.status = status;
        this.genDataModal.endRow = endLine;
        this.genDataModal.failedTotal = failedTotal;
        this.genDataModal.failedInsertTotal = failedInsertTotal;
        this.genDataModal.failedDeleteTotal = failedDeleteTotal;
        this.genDataModal.failedUpdateTotal = failedUpdateTotal;
        this.genDataModal.successTotal = successTotal;
        this.genDataModal.successInsertTotal = successInsertTotal;
        this.genDataModal.successDeleteTotal = successDeleteTotal;
        this.genDataModal.successUpdateTotal = successUpdateTotal;
        this.genDataModal.writeAvgTime = writeAvgTime;
        this.genDataModal.logFile = logFile;
        this.genDataModal.logHost = logHost;
        if (logArr) {
          this.genDataModal.logArr = logArr.map((log) => ({
            text: log,
            id: nanoid()
          }));
        } else {
          this.genDataModal.logArr = [];
        }
        setTimeout(() => {
          const ele = document.getElementById('log-list');
          if (ele) {
            console.log(ele);
            ele.scrollTop = ele.scrollHeight;
          }
        }, 0);
      }
    },
    async handleCloseModal() {
      this.genDataModal = deepClone(EMPTY_GEN_DATA);
      this.sqlModal = deepClone(EMPTY_SQL_DATA);
      this.menuModal = deepClone(EMPTY_MENU_DATA);

      this.triggerModal = deepClone(EMPTY_TRIGGER_DATA);
      this.viewModal = deepClone(EMPTY_VIEW_DATA);
      this.functionModal = deepClone(EMPTY_FUNCTION_DATA);
      this.procedureModal = deepClone(EMPTY_PROCEDURE_DATA);
      this.dblinkModal = deepClone(EMPTY_DBLINK_DATA);
      this.jobModal = deepClone(EMPTY_JOB_DATA);
      this.scheduleJobModal = deepClone(EMPTY_SCHEDULE_JOB_DATA);
      this.propertiesModal = deepClone({
        selectedProperties: [],
        show: false
      });

      if (this.monacoEditor) {
        this.monacoEditor.dispose();
      }
      this.defaultOpts.value = '';
      this.currentDdlList = [];
      this.doActionLoading = false;
      clearAllPending();
    },
    handleMenuNameChange(e) {
      if (e.target.value !== this.menuModal.name) {
        this.menuModal.sql = '';
        this.menuModal.permission = false;
        this.menuModal.danger = false;
        this.menuModal.actionData = {};
      }
      this.menuModal.name = e.target.value;
    },
    generateDefaultOptions(selectedSchema = []) {
      const selectedData = {};
      const generateOptionSchema = (item) => {
        if (item.type === 'Options' && selectedData) {
          const optionData = selectedData[item.field];
          item.options.forEach((option) => {
            if (option.value === optionData) {
              if (option.children && option.children.length) {
                item.children = option.children;
                option.children.forEach((child) => {
                  if (!(child.field in selectedData)) {
                    selectedData[child.field] = child.defaultVal;
                  }
                  generateOptionSchema(child);
                });
              } else {
                item.children = [];
              }
            }
          });
        }
        if (!(item.field in selectedData)) {
          selectedData[item.field] = item.defaultVal;
        }
      };

      selectedSchema.forEach((item) => {
        generateOptionSchema(item);
      });

      return selectedData;
    },
    handleMenuOptionChange(key, e) {
      console.log(key, e);
      if (this.menuModal.show) {
        if (e !== this.menuModal.options[key]) {
          this.menuModal.sql = '';
          this.menuModal.permission = false;
          this.menuModal.danger = false;
          this.menuModal.actionData = {};
        }
        this.menuModal.options[key] = e.target.checked;
      }

      if (this.sqlModal.show) {
        if (e !== this.sqlModal.options[key]) {
          this.sqlModal.sql = '';
          this.sqlModal.permission = false;
          this.sqlModal.danger = false;
        }
        this.sqlModal.options[key] = e;
        this.handleStruct();
      }
    },
    async handleRightClickMenu(actionType) {
      const tableNode = this.$refs.tableTree.getSelectedNode();
      console.log(actionType, tableNode);
      this.actionType = actionType;
      const { node, selectedTable: table, leafType } = this.currentTab;
      const data = {
        node: this.currentTab.node,
        actionType,
        callback: null,
        other: {
          targetType: this.currentTab.leafType,
          targetName: tableNode && tableNode.objAttr && tableNode.objAttr.rdb_roi ? tableNode.objName : table ? table.title : '',
          targetNewName: this.menuModal.name,
          options: this.menuModal.options,
          targetExactName: tableNode ? tableNode.title : ''
        }
      };

      if (tableNode && tableNode.objAttr && tableNode.objAttr.rdb_tb) {
        data.other.options.triggerTable = tableNode.objAttr && tableNode.objAttr.rdb_tb;
      }

      let res = {};

      switch (actionType) {
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_PROPERTY:
          console.log(this.selectedNode);
          res = await this.$services.dmEditorPropertiesPropertiesDef({
            data: {
              levels: this.browseGenLevelsData(data.node),
              types: this.selectedNode.nodeType
            }
          });

          if (res.success) {
            this.propertiesDef = res.data;
          }

          const res2 = await this.$services.dmEditorPropertiesGetProperties({
            data: {
              levels: this.browseGenLevelsData(data.node),
              leafName: table.title,
              type: this.selectedNode.nodeType
            }
          });

          if (res.success && res2.success) {
            const properties = [];
            Object.keys(res.data.uiPanels).forEach((key) => {
              const item = res.data.uiPanels[key];
              const itemValue = res2.data[key];
              const property = {
                key,
                titleI18N: item.titleI18N,
                descI18N: item.descI18N,
                children: []
              };
              item.children.forEach((child) => {
                const propertyChild = {
                  key: child.field,
                  titleI18N: child.titleI18N,
                  descI18N: child.descI18N,
                  children: []
                };

                if (child.children && child.children.length) {
                  child.children.forEach((childChild) => {
                    propertyChild.children.push({
                      key: childChild.field,
                      titleI18N: childChild.titleI18N,
                      descI18N: childChild.descI18N,
                      value: itemValue[childChild.field]
                    });
                  });
                }
                property.children.push(propertyChild);
              });

              properties.push(property);
            });

            console.log(properties);
            this.properties = properties;
            if (properties && properties.length) {
              this.propertiesModal.selectedProperties = properties[0];
            }
            this.propertiesModal.show = true;
          }
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_COPY_NAME:
          this.copyText(this.selectedNode.objName || this.selectedNode.title);
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TABLE_FAKER:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TABLE_FAKER_INCREMENT:
          this.genDataModal.title =
            actionType === TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TABLE_FAKER ? this.$t('sheng-cheng-shu-ju') : this.$t('ya-li-ce-shi');
          // this.genDataModal.treeData = this.currentTab[this.currentTab.leafType].treeData;
          this.$nextTick(async () => {
            await this.getFakerDef();
            const treeData = [];
            const tableConfigs = [];
            let curNode = null;
            if (!this.genDataModal.show) {
              this.genDataModal.show = true;
            }
            if (!this.genDataModal.dataSourceId) {
              this.genDataModal.dataSourceId = this.currentTab.node.INSTANCE.id;
            }
            for await (const leaf of this.currentTab.TABLE.treeData) {
              const item = deepClone(leaf);
              item.configType = 'tableConfig';
              item.children = [];
              const tableConfig = {
                name: item.title,
                key: item.key,
                columnConfigs: []
              };
              this.schemaDef.uiPanels.tableConfig.children.forEach((child) => {
                tableConfig[child.field] = child.defaultVal;
              });

              if (this.selectedNode && leaf.key === this.selectedNode.key && !this.genDataModal.resume) {
                curNode = item;
                try {
                  await this.getInitTableFaker(
                    item,
                    {
                      checked: false,
                      selected: false,
                      expand: false
                    },
                    (children) => {
                      item.children = children;
                      children.forEach((child) => {
                        const columnConfig = {
                          name: child.title,
                          seedType: child.seedType,
                          key: child.key
                        };
                        this.schemaDef.uiPanels.columnConfig.children.forEach((child2) => {
                          if (child2.type === 'Options') {
                            child2.options.forEach((option) => {
                              if (option.value === child.seedType) {
                                option.children.forEach((child3) => {
                                  columnConfig[child3.field] = child3.defaultVal;
                                });
                              }
                            });
                          } else {
                            columnConfig[child2.field] = child2.defaultVal;
                            if (child2.type === 'Check') {
                              if (columnConfig[child2.field] === 'true') {
                                columnConfig[child2.field] = true;
                              }
                              if (columnConfig[child2.field] === 'false') {
                                columnConfig[child2.field] = false;
                              }
                            }
                          }
                        });

                        if (child.ignoreAct && child.ignoreAct.length) {
                          child.ignoreAct.forEach((ignoreConfig) => {
                            columnConfig[ignoreConfig] = true;
                          });
                        }
                        tableConfig.columnConfigs.push(columnConfig);
                      });
                    }
                  );
                } catch (e) {
                  console.log(e);
                }
              }
              tableConfigs.push(tableConfig);
              if (
                this.genDataModal.resume &&
                this.genDataModal.preTableConfigs.findIndex((preTableConfig) => preTableConfig.name === tableConfig.name) > -1
              ) {
                item.resume = true;
                tableConfig.resume = true;
              }
              treeData.push(item);
            }

            this.genDataModal.treeData = treeData;
            await this.$refs.genDataTree.setData(treeData);

            if (this.genDataModal.resume) {
              this.genDataModal.preTableConfigs.forEach((preTableConfig) => {
                tableConfigs.forEach((tableConfig) => {
                  if (preTableConfig.name === tableConfig.name) {
                    this.$refs.genDataTree.setChecked(tableConfig.key, true);
                  }
                });
              });
            }

            this.genDataModal.tableConfigs = tableConfigs;

            if (curNode && curNode._loaded && !this.genDataModal.resume) {
              await this.$refs.genDataTree.scrollTo(curNode.key);
              await this.$refs.genDataTree.setExpand(curNode.key, true);
              await this.$refs.genDataTree.setChecked(curNode.key, true);
              await this.handleGenDataSetSelected(curNode, true);
            }
          });
          break;
        default:
          break;
      }

      switch (actionType) {
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_VIEW_REFRESH:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_KEY_REFRESH:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_REFRESH_DICT:
          await this.listLeaf(true);
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_REFRESH:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TABLE_REFRESH:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_COLUMN_REFRESH:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_INDEX_REFRESH:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_PARTITION_REFRESH:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_PRIMARY_REFRESH:
          const refreshCache = true;
          this.rdbObjectDetail(
            this.currentTab.selectedTable.title,
            {
              expand: false,
              selected: true,
              type: actionType,
              selectedNode: this.selectedNode
            },
            () => {},
            refreshCache
          );
          break;
        default:
          break;
      }

      switch (actionType) {
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TABLE_DATA:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_VIEW_DATA:
          this.handleAddTab(TAB_TYPE.DATA, node, { table });
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TABLE_ALTER:
          this.handleAddTab(TAB_TYPE.STRUCT, node, { table, editorType: ACTION_TYPE.EDIT_TABLE });
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TABLE_CREATE:
          this.handleAddTab(TAB_TYPE.STRUCT, node, { editorType: ACTION_TYPE.CREATE_TABLE });
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_VIEW_DROP:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_KEY_DROP:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TABLE_DROP:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TRIGGER_DROP:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_PROCEDURE_DROP:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_DBLINK_DROP:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_JOB_DROP:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_FUNCTION_DROP:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_INDEX_DROP:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_PRIMARY_DROP:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_SCHEDULE_DROP:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_MATERIALIZED_DROP:
          data.callback = (permission, danger, sql, genActionData) => {
            this.menuModal.actionData = genActionData;
            this.menuModal.permission = permission;
            this.menuModal.danger = danger;
            this.menuModal.show = true;
            this.menuModal.title = this.$t('que-ren-shan-chu');
            this.menuModal.name = table.title;
            this.menuModal.preName = table.title;
            this.menuModal.content = `${this.$t('que-ding-yao-shan-chu')}${this.currentTab.popTip}.\`${table.title}\`？`;
            this.menuModal.sql = sql;
          };
          await this.browseGenAction(data.actionType, this.browseGenLevelsData(data.node), data.callback, data.other);
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_FUNCTION_COMPILE:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_PROCEDURE_COMPILE:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TRIGGER_COMPILE:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_VIEW_COMPILE:
          data.callback = (permission, danger, sql, genActionData) => {
            this.menuModal.actionData = genActionData;
            this.menuModal.permission = permission;
            this.menuModal.danger = danger;
            this.menuModal.show = true;
            this.menuModal.title = this.$t('bian-yi');
            this.menuModal.name = table.title;
            this.menuModal.preName = table.title;
            this.menuModal.content = `${this.$t('que-ding-yao-bian-yi')}${this.currentTab.popTip}.\`${table.title}\`？`;
            this.menuModal.sql = sql;
          };
          await this.browseGenAction(data.actionType, this.browseGenLevelsData(data.node), data.callback, data.other);
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_DBLINK_TEST:
          data.callback = (permission, danger, sql, genActionData) => {
            this.menuModal.actionData = genActionData;
            this.menuModal.permission = permission;
            this.menuModal.danger = danger;
            this.menuModal.show = true;
            this.menuModal.title = this.$t('ce-shi-lian-jie');
            this.menuModal.name = table.title;
            this.menuModal.preName = table.title;
            this.menuModal.content = `${this.$t('ce-shi-lian-jie')}${this.currentTab.popTip}.\`${table.title}\`？`;
            this.menuModal.sql = sql;
          };
          await this.browseGenAction(data.actionType, this.browseGenLevelsData(data.node), data.callback, data.other);
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_JOB_DISABLE:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_SCHEDULE_DISABLE:
          data.callback = (permission, danger, sql, genActionData) => {
            this.menuModal.actionData = genActionData;
            this.menuModal.permission = permission;
            this.menuModal.danger = danger;
            this.menuModal.show = true;
            this.menuModal.title = this.$t('ting-zhi');
            this.menuModal.name = table.title;
            this.menuModal.preName = table.title;
            this.menuModal.content = `${this.$t('ting-zhi')}${this.currentTab.popTip}.\`${table.title}\`？`;
            this.menuModal.sql = sql;
          };
          await this.browseGenAction(data.actionType, this.browseGenLevelsData(data.node), data.callback, data.other);
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_JOB_ENABLE:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_SCHEDULE_ENABLE:
          data.callback = (permission, danger, sql, genActionData) => {
            this.menuModal.actionData = genActionData;
            this.menuModal.permission = permission;
            this.menuModal.danger = danger;
            this.menuModal.show = true;
            this.menuModal.title = this.$t('hui-fu');
            this.menuModal.name = table.title;
            this.menuModal.preName = table.title;
            this.menuModal.content = `${this.$t('hui-fu')}${this.currentTab.popTip}.\`${table.title}\`？`;
            this.menuModal.sql = sql;
          };
          await this.browseGenAction(data.actionType, this.browseGenLevelsData(data.node), data.callback, data.other);
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_JOB_RUN:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_SCHEDULE_RUN:
          data.callback = (permission, danger, sql, genActionData) => {
            this.menuModal.actionData = genActionData;
            this.menuModal.permission = permission;
            this.menuModal.danger = danger;
            this.menuModal.show = true;
            this.menuModal.title = this.$t('yun-hang');
            this.menuModal.name = table.title;
            this.menuModal.preName = table.title;
            this.menuModal.content = `${this.$t('yun-hang')}${this.currentTab.popTip}.\`${table.title}\`？`;
            this.menuModal.sql = sql;
          };
          await this.browseGenAction(data.actionType, this.browseGenLevelsData(data.node), data.callback, data.other);
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TABLE_TRUNCATE:
          data.callback = (permission, danger, sql, genActionData) => {
            this.menuModal.actionData = genActionData;
            this.menuModal.permission = permission;
            this.menuModal.danger = danger;
            this.menuModal.show = true;
            this.menuModal.name = table.title;
            this.menuModal.preName = table.title;
            this.menuModal.title = this.$t('qing-kong');
            this.menuModal.content = `${this.$t('que-ding-yao-qing-kong')}${this.currentTab.popTip}.\`${table.title}\`？`;
            this.menuModal.sql = sql;
          };
          await this.browseGenAction(data.actionType, this.browseGenLevelsData(data.node), data.callback, data.other);
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_CONSTRAINT_ENABLE:
          data.callback = (permission, danger, sql, genActionData) => {
            this.menuModal.actionData = genActionData;
            this.menuModal.permission = permission;
            this.menuModal.danger = danger;
            this.menuModal.show = true;
            this.menuModal.name = table.title;
            this.menuModal.preName = table.title;
            this.menuModal.title = '启用约束';
            this.menuModal.content = `确定要启用${this.currentTab.popTip}.\`${table.title}\`.\`${tableNode.title}\`？`;
            this.menuModal.sql = sql;
          };
          await this.browseGenAction(data.actionType, this.browseGenLevelsData(data.node), data.callback, data.other);
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_USER_DROP:
          data.callback = (permission, danger, sql, genActionData) => {
            this.menuModal.actionData = genActionData;
            this.menuModal.permission = permission;
            this.menuModal.danger = danger;
            this.menuModal.show = true;
            this.menuModal.name = table.title;
            this.menuModal.preName = table.title;
            this.menuModal.title = '删除用户';
            this.menuModal.content = `确定要删除${this.currentTab.popTip}.\`${table.title}\`？`;
            this.menuModal.sql = sql;
          };
          await this.browseGenAction(data.actionType, this.browseGenLevelsData(data.node), data.callback, data.other);
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_CONSTRAINT_DISABLE:
          data.callback = (permission, danger, sql, genActionData) => {
            this.menuModal.actionData = genActionData;
            this.menuModal.permission = permission;
            this.menuModal.danger = danger;
            this.menuModal.show = true;
            this.menuModal.name = table.title;
            this.menuModal.preName = table.title;
            this.menuModal.title = '禁用约束';
            this.menuModal.content = `确定要禁用${this.currentTab.popTip}.\`${table.title}\`.\`${tableNode.title}\`？`;
            this.menuModal.sql = sql;
          };
          await this.browseGenAction(data.actionType, this.browseGenLevelsData(data.node), data.callback, data.other);
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TABLE_RENAME:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_VIEW_RENAME:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_KEY_RENAME:
          if (!this.menuModal.name) {
            this.menuModal.show = true;
            this.menuModal.title = this.$t('zhong-ming-ming');
            this.menuModal.showNameInput = true;
            this.menuModal.name = table.title;
            this.menuModal.preName = table.title;
            this.menuModal.content = this.$t('zhong-ming-ming-thiscurrenttabpoptiptabletitle-wei', [this.currentTab.popTip, table.title]);
          } else {
            data.callback = (permission, danger, sql, genActionData) => {
              this.menuModal.permission = permission;
              this.menuModal.danger = danger;
              this.menuModal.sql = sql;
              this.menuModal.actionData = genActionData;
            };
            await this.browseGenAction(data.actionType, this.browseGenLevelsData(data.node), data.callback, data.other);
          }
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_VIEW_REQUEST:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TABLE_REQUEST:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_FUNCTION_REQUEST:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_PROCEDURE_REQUEST:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TRIGGER_REQUEST:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_SYNONYM_REQUEST:
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_MATERIALIZED_REQUEST:
          res = await this.$services.dmBrowseActionsRequestScript({
            data: {
              levels: this.browseGenLevelsData(node),
              targetType: leafType,
              targetName: table.title
            }
          });

          if (res.success) {
            this.sqlModal.show = true;
            this.sqlModal.title = this.$t('huo-qu-ddl-yu-ju-tabletitle', [table.title]);
            this.sqlModal.sql = res.data.sql;
          }
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TABLE_GENERATE:
          res = await this.$services.dmBrowseActionsGenerateScript({
            data: {
              levels: this.browseGenLevelsData(node),
              targetType: leafType,
              targetName: table.title
            }
          });

          if (res.success) {
            this.sqlModal.show = true;
            this.sqlModal.title = this.$t('sheng-cheng-ddl-yu-ju-tabletitle', [table.title]);
            this.sqlModal.sql = res.data.sql;
          }
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TABLE_GET_DDL:
          this.currentTargetDsList = this.targetDsList(this.currentTab.dsType) || [];
          if (this.currentTargetDsList && this.currentTargetDsList.length) {
            this.sqlModal.dsType = this.currentTargetDsList[0];
            this.currentDdlList = this.ddlList(this.currentTab.dsType) || [];
            if (this.currentDdlList.length) {
              this.sqlModal.type = this.currentDdlList[0];
              this.sqlModal.show = true;
              await this.handleStruct();
            }
          }
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TRIGGER_CREATE:
          this.triggerModal.title = this.$t('xin-jian-chu-fa-qi');
          if (!this.triggerModal.options.name) {
            this.triggerModal.show = true;
            this.triggerModal.options.triggerTable = table.title;
            const editorDefRes = await this.$services.dmBrowseActionsEditorDef({
              data: {
                levels: this.browseGenLevelsData(node),
                targetType: 'TRIGGER',
                viewMode: 'CREATE',
                targetName: table.title
              }
            });

            if (editorDefRes.success) {
              this.triggerModal.selectedSchema = editorDefRes.data;
              this.triggerModal.options = this.generateDefaultOptions(editorDefRes.data);
              this.triggerModal.options.triggerTable = table.title;
            }

            const columnRes = await this.$services.dmBrowseRdbObjectDetail({
              data: {
                levels: this.browseGenLevelsData(this.currentTab.node),
                targetName: table.title,
                targetType: 'TABLE'
              }
            });

            if (columnRes.success) {
              if (columnRes.data && columnRes.data.group) {
                columnRes.data.group.forEach((item) => {
                  if (item.type === 'RDB_COLUMN_GROUP') {
                    this.currentTab.triggerColumnList = item.items;
                  }
                });
              }
            }
          } else {
            data.callback = (permission, danger, sql, genActionData) => {
              this.triggerModal.permission = permission;
              this.triggerModal.danger = danger;
              this.triggerModal.sql = sql;
              this.triggerModal.actionData = genActionData;
            };
            /* eslint-disable prettier/prettier */
            data.other.options = {
              ...this.triggerModal.options,
              triggerEvent:
                this.triggerModal.options.triggerEvent instanceof Array
                  ? this.triggerModal.options.triggerEvent
                  : this.triggerModal.options.triggerEvent
                    ? [this.triggerModal.options.triggerEvent]
                    : []
            };
            /* eslint-enable prettier/prettier */
            data.other.targetType = 'TRIGGER';
            await this.browseGenAction(data.actionType, this.browseGenLevelsData(data.node), data.callback, data.other);
          }
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TRIGGER_ALTER:
          this.triggerModal.title = this.$t('xiu-gai-chu-fa-qi');
          if (!this.triggerModal.options.name) {
            const editorDefRes = await this.$services.dmBrowseActionsEditorDef({
              data: {
                levels: this.browseGenLevelsData(node),
                targetType: 'TRIGGER',
                viewMode: 'CREATE',
                targetName: table.title
              }
            });

            if (editorDefRes.success) {
              this.triggerModal.selectedSchema = editorDefRes.data;
              // this.triggerModal.options = this.generateDefaultOptions(editorDefRes.data);
              if (isMySQL(this.currentTab.dsType)) {
                this.triggerModal.options.triggerEvent = '';
              } else {
                this.triggerModal.options.triggerEvent = [];
              }
              this.triggerModal.options.triggerTable = table.title;
            }
            res = await this.$services.dmBrowseActionsLoadObject({
              data: {
                levels: this.browseGenLevelsData(node),
                targetType: leafType,
                targetName: table.title
              }
            });

            if (res.success) {
              this.triggerModal.show = true;
              this.triggerModal.options = res.data;
              if (isMySQL(this.currentTab.dsType)) {
                this.triggerModal.options.triggerEvent = res.data.triggerEvent.join(',');
              }

              this.triggerModal.selectedSchema.forEach((schema) => {
                const optionData = this.triggerModal.options[schema.field];
                if (optionData && schema.options) {
                  schema.options.forEach((option) => {
                    if ((optionData === option.value || optionData.includes(option.value)) && option.children?.length) {
                      schema.children = option.children;
                    }
                  });
                }
              });
              const columnRes = await this.$services.dmBrowseRdbObjectDetail({
                data: {
                  levels: this.browseGenLevelsData(this.currentTab.node),
                  targetName: res.data.triggerTable,
                  targetType: 'TABLE'
                }
              });

              if (columnRes.success) {
                if (columnRes.data && columnRes.data.group) {
                  columnRes.data.group.forEach((item) => {
                    if (item.type === 'RDB_COLUMN_GROUP') {
                      this.currentTab.triggerColumnList = item.items;
                    }
                  });
                }
              }
            }
          } else {
            data.callback = (permission, danger, sql, genActionData) => {
              this.triggerModal.permission = permission;
              this.triggerModal.danger = danger;
              this.triggerModal.sql = sql;
              this.triggerModal.actionData = genActionData;
            };
            data.other.options = {
              ...this.triggerModal.options,
              triggerEvent: isMySQL(this.currentTab.dsType)
                ? this.triggerModal.options.triggerEvent
                  ? [this.triggerModal.options.triggerEvent]
                  : []
                : this.triggerModal.options.triggerEvent
            };
            data.other.targetType = 'TRIGGER';
            await this.browseGenAction(data.actionType, this.browseGenLevelsData(data.node), data.callback, data.other);
          }
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_VIEW_CREATE:
          this.viewModal.title = '新建视图';
          if (!this.viewModal.options.name) {
            this.viewModal.show = true;
            const editorDefRes = await this.$services.dmBrowseActionsEditorDef({
              data: {
                levels: this.browseGenLevelsData(node),
                targetType: 'VIEW',
                viewMode: 'CREATE',
                targetName: table?.title
              }
            });

            if (editorDefRes.success) {
              this.viewModal.selectedSchema = editorDefRes.data;
              this.viewModal.options = this.generateDefaultOptions(editorDefRes.data);
            }
          } else {
            data.callback = (permission, danger, sql, genActionData) => {
              this.viewModal.permission = permission;
              this.viewModal.danger = danger;
              this.viewModal.sql = sql;
              this.viewModal.actionData = genActionData;
            };
            data.other.options = this.viewModal.options;
            data.other.targetType = 'VIEW';
            await this.browseGenAction(data.actionType, this.browseGenLevelsData(data.node), data.callback, data.other);
          }
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_VIEW_ALTER:
          this.viewModal.title = '修改视图';
          if (!this.viewModal.options.name) {
            const editorDefRes = await this.$services.dmBrowseActionsEditorDef({
              data: {
                levels: this.browseGenLevelsData(node),
                targetType: 'VIEW',
                viewMode: 'ALTER',
                targetName: table.title
              }
            });

            if (editorDefRes.success) {
              this.viewModal.selectedSchema = editorDefRes.data;
            }
            res = await this.$services.dmBrowseActionsLoadObject({
              data: {
                levels: this.browseGenLevelsData(node),
                targetType: leafType,
                targetName: table.title
              }
            });

            if (res.success) {
              this.viewModal.options = res.data;
              this.viewModal.selectedNode = tableNode;
              this.viewModal.show = true;
            }
          } else {
            data.callback = (permission, danger, sql, genActionData) => {
              this.viewModal.permission = permission;
              this.viewModal.danger = danger;
              this.viewModal.sql = sql;
              this.viewModal.actionData = genActionData;
            };
            data.other.options = this.viewModal.options;
            data.other.targetType = 'VIEW';
            await this.browseGenAction(data.actionType, this.browseGenLevelsData(data.node), data.callback, data.other);
          }
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_JOB_ALTER:
          this.jobModal.title = '编辑任务';
          if (!this.jobModal.options.name) {
            const editorDefRes = await this.$services.dmBrowseActionsEditorDef({
              data: {
                levels: this.browseGenLevelsData(node),
                targetType: 'JOB',
                viewMode: 'ALTER',
                targetName: table.title
              }
            });

            if (editorDefRes.success) {
              this.jobModal.selectedSchema = editorDefRes.data;
            }
            res = await this.$services.dmBrowseActionsLoadObject({
              data: {
                levels: this.browseGenLevelsData(node),
                targetType: leafType,
                targetName: table.title
              }
            });

            if (res.success) {
              this.jobModal.options = res.data;
              this.jobModal.selectedNode = tableNode;
              this.jobModal.show = true;
            }
          } else {
            data.callback = (permission, danger, sql, genActionData) => {
              this.jobModal.permission = permission;
              this.jobModal.danger = danger;
              this.jobModal.sql = sql;
              this.jobModal.actionData = genActionData;
            };
            data.other.options = this.jobModal.options;
            data.other.targetType = 'JOB';
            await this.browseGenAction(data.actionType, this.browseGenLevelsData(data.node), data.callback, data.other);
          }
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_PROCEDURE_CREATE:
          this.procedureModal.title = '新建存储过程';
          if (!this.procedureModal.options.name) {
            this.procedureModal.show = true;
            const editorDefRes = await this.$services.dmBrowseActionsEditorDef({
              data: {
                levels: this.browseGenLevelsData(node),
                targetType: 'PROC',
                viewMode: 'CREATE',
                targetName: table?.title
              }
            });

            if (editorDefRes.success) {
              this.procedureModal.selectedSchema = editorDefRes.data;
              this.procedureModal.options = this.generateDefaultOptions(editorDefRes.data);
            }
          } else {
            data.callback = (permission, danger, sql, genActionData) => {
              this.procedureModal.permission = permission;
              this.procedureModal.danger = danger;
              this.procedureModal.sql = sql;
              this.procedureModal.actionData = genActionData;
            };
            data.other.options = this.procedureModal.options;
            data.other.targetType = 'PROC';
            await this.browseGenAction(data.actionType, this.browseGenLevelsData(data.node), data.callback, data.other);
          }
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_DBLINK_CREATE:
          this.dblinkModal.title = '新建外部链接';
          if (!this.dblinkModal.options.name) {
            this.dblinkModal.show = true;
            const editorDefRes = await this.$services.dmBrowseActionsEditorDef({
              data: {
                levels: this.browseGenLevelsData(node),
                targetType: 'DBLINK',
                viewMode: 'CREATE',
                targetName: table?.title
              }
            });

            if (editorDefRes.success) {
              this.dblinkModal.selectedSchema = editorDefRes.data;
              this.dblinkModal.options = this.generateDefaultOptions(editorDefRes.data);
            }
          } else {
            data.callback = (permission, danger, sql, genActionData) => {
              this.dblinkModal.permission = permission;
              this.dblinkModal.danger = danger;
              this.dblinkModal.sql = sql;
              this.dblinkModal.actionData = genActionData;
            };
            data.other.options = this.dblinkModal.options;
            data.other.targetType = 'DBLINK';
            await this.browseGenAction(data.actionType, this.browseGenLevelsData(data.node), data.callback, data.other);
          }
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_JOB_CREATE:
          this.jobModal.title = '新建任务';
          if (!this.jobModal.options.execSql) {
            this.jobModal.show = true;
            const editorDefRes = await this.$services.dmBrowseActionsEditorDef({
              data: {
                levels: this.browseGenLevelsData(node),
                targetType: 'JOB',
                viewMode: 'CREATE',
                targetName: table?.title
              }
            });

            if (editorDefRes.success) {
              this.jobModal.selectedSchema = editorDefRes.data;
              this.jobModal.options = this.generateDefaultOptions(editorDefRes.data);
            }
          } else {
            data.callback = (permission, danger, sql, genActionData) => {
              this.jobModal.permission = permission;
              this.jobModal.danger = danger;
              this.jobModal.sql = sql;
              this.jobModal.actionData = genActionData;
            };
            data.other.options = this.jobModal.options;
            data.other.targetType = 'JOB';
            await this.browseGenAction(data.actionType, this.browseGenLevelsData(data.node), data.callback, data.other);
          }
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_SCHEDULE_CREATE:
          this.scheduleJobModal.title = '新建任务';
          if (!this.scheduleJobModal.options.name) {
            this.scheduleJobModal.show = true;
            const editorDefRes = await this.$services.dmBrowseActionsEditorDef({
              data: {
                levels: this.browseGenLevelsData(node),
                targetType: 'SCHEDULE_JOB',
                viewMode: 'CREATE',
                targetName: table?.title
              }
            });

            if (editorDefRes.success) {
              this.scheduleJobModal.selectedSchema = editorDefRes.data;
              this.scheduleJobModal.options = this.generateDefaultOptions(editorDefRes.data);
            }
          } else {
            data.callback = (permission, danger, sql, genActionData) => {
              this.scheduleJobModal.permission = permission;
              this.scheduleJobModal.danger = danger;
              this.scheduleJobModal.sql = sql;
              this.scheduleJobModal.actionData = genActionData;
            };
            data.other.options = this.scheduleJobModal.options;
            data.other.targetType = 'SCHEDULE_JOB';
            await this.browseGenAction(data.actionType, this.browseGenLevelsData(data.node), data.callback, data.other);
          }
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_PROCEDURE_ALTER:
          this.procedureModal.title = '修改存储过程';
          if (!this.procedureModal.options.name) {
            const editorDefRes = await this.$services.dmBrowseActionsEditorDef({
              data: {
                levels: this.browseGenLevelsData(node),
                targetType: 'PROC',
                viewMode: 'ALTER',
                targetName: table.title
              }
            });

            if (editorDefRes.success) {
              this.procedureModal.selectedSchema = editorDefRes.data;
            }
            res = await this.$services.dmBrowseActionsLoadObject({
              data: {
                levels: this.browseGenLevelsData(node),
                targetType: leafType,
                targetName: table.title
              }
            });

            if (res.success) {
              res.data.params.forEach((param) => {
                param.key = nanoid();
              });
              this.procedureModal.options = res.data;
              this.procedureModal.selectedNode = tableNode;
              this.procedureModal.show = true;
            }
          } else {
            data.callback = (permission, danger, sql, genActionData) => {
              this.procedureModal.permission = permission;
              this.procedureModal.danger = danger;
              this.procedureModal.sql = sql;
              this.procedureModal.actionData = genActionData;
            };
            data.other.options = {
              ...this.procedureModal.options,
              sql: this.monacoEditor.getValue()
            };
            data.other.targetType = 'PROC';
            await this.browseGenAction(data.actionType, this.browseGenLevelsData(data.node), data.callback, data.other);
          }
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_FUNCTION_CREATE:
          this.functionModal.title = '新建函数';
          if (!this.functionModal.options.name) {
            this.functionModal.show = true;
            const editorDefRes = await this.$services.dmBrowseActionsEditorDef({
              data: {
                levels: this.browseGenLevelsData(node),
                targetType: 'FUNC',
                viewMode: 'CREATE',
                targetName: table?.title
              }
            });

            if (editorDefRes.success) {
              this.functionModal.selectedSchema = editorDefRes.data;
              this.functionModal.options = this.generateDefaultOptions(editorDefRes.data);
            }
          } else {
            data.callback = (permission, danger, sql, genActionData) => {
              this.functionModal.permission = permission;
              this.functionModal.danger = danger;
              this.functionModal.sql = sql;
              this.functionModal.actionData = genActionData;
            };
            data.other.options = this.functionModal.options;
            data.other.targetType = 'FUNC';
            await this.browseGenAction(data.actionType, this.browseGenLevelsData(data.node), data.callback, data.other);
          }
          break;
        case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_FUNCTION_ALTER:
          this.functionModal.title = '修改函数';
          if (!this.functionModal.options.name) {
            const editorDefRes = await this.$services.dmBrowseActionsEditorDef({
              data: {
                levels: this.browseGenLevelsData(node),
                targetType: 'FUNC',
                viewMode: 'ALTER',
                targetName: table.title
              }
            });

            if (editorDefRes.success) {
              this.functionModal.selectedSchema = editorDefRes.data;
            }
            res = await this.$services.dmBrowseActionsLoadObject({
              data: {
                levels: this.browseGenLevelsData(node),
                targetType: leafType,
                targetName: table.title
              }
            });

            if (res.success) {
              res.data.params.forEach((param) => {
                param.key = nanoid();
              });
              this.functionModal.options = res.data;
              this.functionModal.selectedNode = tableNode;
              this.functionModal.show = true;
            }
          } else {
            data.callback = (permission, danger, sql, genActionData) => {
              this.functionModal.permission = permission;
              this.functionModal.danger = danger;
              this.functionModal.sql = sql;
              this.functionModal.actionData = genActionData;
            };
            data.other.options = this.functionModal.options;
            data.other.targetType = 'FUNC';
            await this.browseGenAction(data.actionType, this.browseGenLevelsData(data.node), data.callback, data.other);
          }
          break;
        default:
          break;
      }
    },
    handlePreCreateTrigger() {
      this.triggerModal.sql = '';
      this.viewModal.sql = '';
      this.procedureModal.sql = '';
      this.functionModal.sql = '';
      this.dblinkModal.sql = '';
      this.jobModal.sql = '';
      this.scheduleJobModal.sql = '';
    },
    async handleDoAction() {
      const callback = () => {
        console.warn(1231232131, this.actionType);
        switch (this.actionType) {
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_MATERIALIZED_DROP:
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_USER_DROP:
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TABLE_DROP:
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_KEY_DROP:
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TRIGGER_DROP:
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_VIEW_DROP:
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_FUNCTION_DROP:
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_PROCEDURE_DROP:
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_DBLINK_DROP:
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_JOB_DROP:
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_JOB_DISABLE:
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_JOB_ENABLE:
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_SCHEDULE_ENABLE:
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_SCHEDULE_DISABLE:
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TABLE_RENAME:
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_KEY_RENAME:
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TRIGGER_ALTER:
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_VIEW_ALTER:
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_JOB_ALTER:
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_FUNCTION_ALTER:
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_PROCEDURE_ALTER:
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_FUNCTION_COMPILE:
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_PROCEDURE_COMPILE:
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TRIGGER_COMPILE:
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_VIEW_COMPILE:
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_SCHEDULE_DROP:
            const refreshCache = true;
            this.listLeaf(refreshCache);
            break;
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TRIGGER_CREATE:
            this.handleChangeTab('TRIGGER');
            break;
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_VIEW_CREATE:
            this.handleChangeTab('VIEW');
            break;
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_FUNCTION_CREATE:
            this.handleChangeTab('FUNC');
            break;
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_PROCEDURE_CREATE:
            this.handleChangeTab('PROC');
            break;
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_DBLINK_CREATE:
            this.handleChangeTab('DBLINK');
            break;
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_JOB_CREATE:
            this.handleChangeTab('JOB');
            break;
          case TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_SCHEDULE_CREATE:
            this.handleChangeTab('SCHEDULE_JOB');
            break;
          default:
            break;
        }
        this.handleCloseModal();
        this.doActionLoading = false;
        this.$message.success(this.$t('cao-zuo-cheng-gong'));
      };

      let actionData = this.menuModal.actionData;
      if ([TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TRIGGER_CREATE, TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_TRIGGER_ALTER].includes(this.actionType)) {
        actionData = this.triggerModal.actionData;
      }
      if ([TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_VIEW_CREATE, TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_VIEW_ALTER].includes(this.actionType)) {
        actionData = this.viewModal.actionData;
      }
      if (
        [TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_FUNCTION_CREATE, TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_FUNCTION_ALTER].includes(this.actionType)
      ) {
        actionData = this.functionModal.actionData;
      }
      if (
        [TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_PROCEDURE_CREATE, TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_PROCEDURE_ALTER].includes(this.actionType)
      ) {
        actionData = this.procedureModal.actionData;
      }
      if ([TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_DBLINK_CREATE].includes(this.actionType)) {
        actionData = this.dblinkModal.actionData;
      }
      if ([TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_JOB_CREATE, TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_JOB_ALTER].includes(this.actionType)) {
        actionData = this.jobModal.actionData;
      }
      if ([TABLE_RIGHT_CLICK_MENU_ITEM.MENU_BROWSE_SCHEDULE_CREATE].includes(this.actionType)) {
        actionData = this.scheduleJobModal.actionData;
      }
      const callbackFail = () => {
        this.doActionLoading = false;
      };
      this.doActionLoading = true;
      await this.browseDoAction(actionData, callback, callbackFail);
    }
  }
};
</script>
<style scoped lang="less">
.search-header {
  height: 31px;
  display: flex;
  align-items: center;
  padding: 2px;
  background: #fafafa;
  border-bottom: 1px solid #ccc;

  .search-border {
    width: 100%;
    display: flex;
    align-items: center;
    margin: 0;
    border: 1px solid #ccc;
    border-radius: 5px;

    :deep(.ivu-input) {
      border-radius: 0 !important;
      border-bottom: none !important;
      border-top: none !important;
    }
  }

  .btn-hint:before {
    content: '';
    width: 0;
    height: 0;
    position: absolute;
    top: 19px;
    left: 25px;
    border-top: solid 8px transparent;
    border-right: solid 8px #4b4b4b;
    border-bottom: solid 0 transparent;
  }
}

.table-list-tree {
  .ivu-btn-text:focus {
    box-shadow: none;
  }

  .ivu-btn:focus {
    box-shadow: none;
  }
}

.table-list-resize {
  height: 100%;
  width: 6px;
  background: rgba(0, 0, 0, 0);
  //background: red;
  position: absolute;
  right: -3px;
  cursor: col-resize;
  z-index: 3;
}

:deep(.ctree-tree-node__title) {
  padding-left: 0;
  margin-left: 0;
}

:deep(.node) {
  display: flex;
  align-items: center;
}

:deep(.no-indent) {
  .ctree-tree-node__square {
    display: none;
  }

  .node {
    padding-left: 5px;
  }
}

//:deep(.ant-collapse-header) {
//  padding: 5px 10px 5px 34px !important;
//}
//
//:deep(.ant-collapse-item) {
//  border: none;
//}
//
//:deep(.ant-collapse-content-box) {
//  padding: 8px 10px;
//}

:deep(.highlight) {
  background: orange !important;
  border-radius: 2px;
}

.generate-modal {
  height: 500px;
  display: flex;
  flex-direction: column;

  .advanced {
    margin-bottom: 10px;

    .title {
      font-size: 14px;
      font-weight: bold;
      margin-bottom: 5px;
    }

    .options {
      display: flex;

      .option {
        display: flex;
        align-items: center;
        margin-right: 10px;
      }
    }
  }

  .content {
    flex: 1;
    min-height: 0;
    display: flex;
    flex-direction: column;

    .title {
      font-size: 14px;
      font-weight: bold;
      margin-bottom: 5px;
    }

    .table-setting {
      display: flex;
      width: 100%;
      flex: 1;
      min-height: 0;
      border: 1px solid #ccc;

      .left {
        width: 300px;
        //min-height: 400px;
        //max-height: 400px;
        border-right: 1px solid #ccc;
        display: flex;
        flex-direction: column;

        .search {
          :deep(.ant-input) {
            border: none;
            border-bottom: 1px solid #ccc;
          }
        }

        .datasource-tree {
          flex: 1;
          min-height: 0;
        }
      }

      .right {
        flex: 1;
        overflow: auto;
        padding: 10px;
        min-height: 200px;
        max-height: 400px;

        .select-title {
          margin-bottom: 16px;
        }

        .column-config-table {
          margin-top: 16px;
        }
      }
    }
  }

  .preview {
    display: flex;
    height: 100%;

    .left {
      width: 300px;
      border: 1px solid #ccc;
      border-right: none;
      overflow: scroll;
    }

    .right {
      flex: 1;
      min-width: 0;
      border: 1px solid #ccc;
      position: relative;

      .tips {
        position: absolute;
        bottom: 0;
        left: 5px;
        color: red;
      }
    }
  }

  .result {
    display: flex;
    flex-direction: column;
    height: 100%;

    .refresh-container {
      position: absolute;
      right: 0;
      top: 0;

      .result-refresh-btn {
        margin-left: 8px;
      }
    }

    .header {
      margin-bottom: 5px;
      position: relative;

      .status {
        line-height: 22px;
      }

      .info-container {
        display: flex;
        justify-content: space-between;
        align-items: center;
      }

      .info {
        flex: 1;
        margin-bottom: 5px;
        margin-right: 80px;

        .line {
          display: flex;

          div {
            flex: 1;
            line-height: 22px;
          }
        }
      }
    }

    .logs {
      flex: 1;
      min-height: 0;
      border: 1px solid #ccc;
      padding: 3px;

      #log-list {
        display: flex;
        flex-direction: column;
        overflow: auto;
        height: 100%;

        .log {
          display: flex;
          width: 100%;
        }
      }
    }
  }
}

.trigger-modal,
.view-modal,
.procedure-modal,
.dblink-modal,
.function-modal {
  :deep(.ivu-form-item) {
    margin-bottom: 10px;
  }

  .advanced-setting {
    margin-left: 10px;
  }

  .param-container {
    border: 1px solid #ddd;
    max-height: 150px;
    min-height: 150px;
    display: flex;

    .left {
      width: 200px;
      border-right: 1px solid #ddd;
      display: flex;
      flex-direction: column;

      .op {
        display: flex;
        border-bottom: 1px solid #ddd;
      }

      .add,
      .remove {
        flex: 1;
        min-height: 20px;
        display: flex;
        justify-content: center;
        align-items: center;

        &.add {
          border-right: 1px solid #ddd;
        }
      }

      .list {
        flex: 1;
        overflow: auto;

        .param {
          height: 20px;
          line-height: 20px;
          padding: 0 10px;
          border-bottom: 1px solid #ddd;

          &.active {
            background: #ccc;
          }
        }
      }
    }

    .right {
      flex: 1;
      overflow: auto;
    }
  }

  padding-right: 10px;
  max-height: 500px;
  overflow: auto;
}

.properties-modal {
  height: 500px;
  border: 1px solid #ccc;
  display: flex;

  .left {
    width: 200px;
    height: 100%;
    border-right: 1px solid #ccc;

    .properties {
      cursor: pointer;
      padding: 2px 5px;

      .property-title {
        font-size: 14px;
        padding: 4px 6px;
      }

      &.active {
        background: #ccc;
        font-weight: bold;
      }
    }
  }

  .right {
    flex: 1;
    padding: 6px;
    overflow: auto;

    .property-container {
      .property-container-title {
        margin-bottom: 5px;
        font-weight: bold;
        font-size: 16px;
      }

      .property {
        display: flex;
      }
    }
  }
}

.trigger-monaco-editor,
.view-monaco-editor,
.procedure-monaco-editor,
.function-monaco-editor {
  height: 250px;
  width: 100%;
  border: 1px solid #ccc;
}

:deep(.ivu-collapse) {
  background: #fff;
}

:deep(.ivu-collapse-header) {
  background: #f7f7f7;
}

:deep(.ivu-collapse-content) {
  padding: 10px;
}

[dark-theme='dark'] {
}
</style>
