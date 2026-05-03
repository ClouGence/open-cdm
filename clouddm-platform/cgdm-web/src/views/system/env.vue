<template>
  <div class="env">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="header">
          <Breadcrumb>
            <BreadcrumbItem>{{ $t('huan-jing-guan-li') }}</BreadcrumbItem>
          </Breadcrumb>
        </div>
        <div class="content">
          <div class="option border-radius-card">
            <div class="left">
              <Input
                v-model="envSearch"
                style="width: 280px; margin-right: 10px"
                clearable
                :placeholder="$t('qing-shu-ru-huan-jing-ming-cheng-cha-xun')"
                @on-keydown="handleEnterSearch"
              ></Input>
              <Button :loading="loading" @click="listEnv('init')" type="primary">
                {{ $t('cha-xun') }}
              </Button>
            </div>
            <div class="right">
              <Button
                @click="handleShowAddEnv"
                type="primary"
                ghost
                style="margin-right: 10px"
                icon="md-add"
                v-if="myAuth.includes('RDP_ENV_MANAGE')"
              >
                {{ $t('xin-jian-huan-jing') }}
              </Button>
              <Button @click="listEnv('init')">
                <CustomIcon type="icon-v2-Refresh" v-if="!loading" />
              </Button>
            </div>
          </div>
          <div class="table-container">
            <Table :columns="getEnvColumns" :data="showEnvList" size="small" :loading="loading" border stripe>
              <template #envDesc="{ row }">
                <a-tooltip :title="row.envDesc" placement="topLeft">
                  <div class="env-desc-cell">
                    {{ row.envDesc }}
                  </div>
                </a-tooltip>
              </template>

              <template #securitySpec="{ row }">
                <div class="ticket-text-container">
                  <div v-if="!row.secDesVO.openSec" class="ticket-info">
                    <span class="ticket-status-text-error">{{ $t('wei-qi-yong') }}</span>
                    <a-button type="link" size="small" class="ticket-config-btn" @click="handleSwitchSpecChange(row)">
                      {{ $t('pei-zhi') }}
                    </a-button>
                  </div>
                  <div v-else class="ticket-info">
                    <a-button type="link" size="small" class="ticket-config-btn" @click="handleDisableSecuritySpec(row)">
                      {{ $t('jin-yong') }}
                    </a-button>
                    <a-popover placement="top" trigger="hover" :overlay-style="{ maxWidth: '300px' }">
                      <template #content>
                        <div class="ticket-popover-content">
                          <div class="ticket-popover-item">
                            <span class="ticket-popover-label">{{ $t('gui-fan-ming-cheng') }}：</span>
                            <span class="ticket-popover-value">{{ row.secDesVO.name }}</span>
                          </div>
                          <div v-if="row.secDesVO.desc" class="ticket-popover-item">
                            <span class="ticket-popover-label">{{ $t('gui-fan-miao-shu') }}：</span>
                            <span class="ticket-popover-value">{{ row.secDesVO.desc }}</span>
                          </div>
                        </div>
                      </template>
                      <span class="ticket-status-text cursor-pointer">{{ $t('yi-qi-yong') }}</span>
                    </a-popover>
                  </div>
                </div>
              </template>

              <template #sqlTicket="{ row }">
                <div class="ticket-text-container">
                  <div class="ticket-info">
                    <a-button
                      type="link"
                      size="small"
                      class="ticket-config-btn"
                      @click="handleShowTicketChange(row, row.sqlTicketInfo, 'ticket_info')"
                    >
                      {{ $t('pei-zhi') }}
                    </a-button>
                    <span v-if="!row.sqlTicketInfo.openTicket" class="ticket-status-text-error">{{ $t('wei-qi-yong') }}</span>
                    <a-popover v-else placement="top" trigger="hover" :overlay-style="{ maxWidth: '300px' }">
                      <template #content>
                        <div class="ticket-popover-content">
                          <div class="ticket-popover-item">
                            <span class="ticket-popover-label">{{ $t('liu-cheng-lei-xing') }}：</span>
                            <span class="ticket-popover-value">{{ row.sqlTicketInfo.typeI18n }}</span>
                          </div>
                          <div v-if="row.sqlTicketInfo.templateName" class="ticket-popover-item">
                            <span class="ticket-popover-label">{{ $t('mo-ban-ming-cheng') }}：</span>
                            <span class="ticket-popover-value" :class="{ deleted: row.sqlTicketInfo.delete }">
                              {{ row.sqlTicketInfo.templateName }}
                              <span v-if="row.sqlTicketInfo.delete" class="delete-tag">({{ $t('yi-shan-chu') }})</span>
                            </span>
                          </div>
                        </div>
                      </template>
                      <span class="ticket-status-text cursor-pointer">{{ $t('yi-qi-yong') }}</span>
                    </a-popover>
                  </div>
                </div>
              </template>

              <template #changeTicket="{ row }">
                <div class="ticket-text-container">
                  <div class="ticket-info">
                    <a-button
                      type="link"
                      size="small"
                      class="ticket-config-btn"
                      @click="handleShowTicketChange(row, row.changeTicketInfo, 'ticket_info_of_change')"
                    >
                      {{ $t('pei-zhi') }}
                    </a-button>
                    <span v-if="!row.changeTicketInfo.openTicket" class="ticket-status-text-error">{{ $t('wei-qi-yong') }}</span>
                    <a-popover v-else placement="top" trigger="hover" :overlay-style="{ maxWidth: '300px' }">
                      <template #content>
                        <div class="ticket-popover-content">
                          <div class="ticket-popover-item">
                            <span class="ticket-popover-label">{{ $t('liu-cheng-lei-xing') }}：</span>
                            <span class="ticket-popover-value">{{ row.changeTicketInfo.typeI18n }}</span>
                          </div>
                          <div v-if="row.changeTicketInfo.templateName" class="ticket-popover-item">
                            <span class="ticket-popover-label">{{ $t('mo-ban-ming-cheng') }}：</span>
                            <span class="ticket-popover-value" :class="{ deleted: row.changeTicketInfo.delete }">
                              {{ row.changeTicketInfo.templateName }}
                              <span v-if="row.changeTicketInfo.delete" class="delete-tag">({{ $t('yi-shan-chu') }})</span>
                            </span>
                          </div>
                        </div>
                      </template>
                      <span class="ticket-status-text cursor-pointer">{{ $t('yi-qi-yong') }}</span>
                    </a-popover>
                  </div>
                </div>
              </template>

              <template #authTicket="{ row }">
                <div class="ticket-text-container">
                  <div v-if="!row.authTicketInfo.openTicket" class="ticket-button-only">
                    <a-button type="link" size="small" @click="handleShowTicketChange(row, row.authTicketInfo, 'ticket_info_of_auth')">
                      {{ $t('pei-zhi') }}
                    </a-button>
                  </div>
                  <div v-else class="ticket-info">
                    <a-button
                      type="link"
                      size="small"
                      class="ticket-config-btn"
                      @click="handleShowTicketChange(row, row.authTicketInfo, 'ticket_info_of_auth')"
                    >
                      {{ $t('pei-zhi') }}
                    </a-button>
                    <a-popover placement="top" trigger="hover" :overlay-style="{ maxWidth: '300px' }">
                      <template #content>
                        <div class="ticket-popover-content">
                          <div class="ticket-popover-item">
                            <span class="ticket-popover-label">{{ $t('liu-cheng-lei-xing') }}：</span>
                            <span class="ticket-popover-value">{{ row.authTicketInfo.typeI18n }}</span>
                          </div>
                          <div v-if="row.authTicketInfo.templateName" class="ticket-popover-item">
                            <span class="ticket-popover-label">{{ $t('mo-ban-ming-cheng') }}：</span>
                            <span class="ticket-popover-value" :class="{ deleted: row.authTicketInfo.delete }">
                              {{ row.authTicketInfo.templateName }}
                              <span v-if="row.authTicketInfo.delete" class="delete-tag">({{ $t('yi-shan-chu') }})</span>
                            </span>
                          </div>
                        </div>
                      </template>
                      <span class="ticket-status-text cursor-pointer">{{ $t('yi-qi-yong') }}</span>
                    </a-popover>
                  </div>
                </div>
              </template>

              <template #sqlLimit="{ row }">
                <div class="sql-limit-container">
                  <a-switch v-model:checked="row.allowAllStatements" @change="handleAllowAllStatementsChange(row)" />
                </div>
              </template>

              <template #action="{ row }">
                <Button v-if="myAuth.includes('RDP_ENV_MANAGE')" size="small" type="text" @click="handleEdit(row)">
                  {{ $t('bian-ji') }}
                </Button>
                <Poptip
                  confirm
                  transfer
                  :title="$t('que-ding-shan-chu-gai-huan-jing-ma')"
                  :ok-text="$t('que-ding')"
                  :cancel-text="$t('qu-xiao')"
                  v-if="myAuth.includes('RDP_ENV_MANAGE')"
                  @on-ok="handleDeleteEnv(row)"
                >
                  <Button type="text" size="small">{{ $t('shan-chu') }}</Button>
                </Poptip>
              </template>
            </Table>
          </div>
        </div>
      </div>
      <div class="footer">
        <Page
          :total="total"
          show-total
          show-elevator
          @on-change="handlePageChange"
          show-sizer
          :page-size="pageSize"
          @on-page-size-change="handlePageSizeChange"
          :model-value="pageNum"
        />
      </div>
    </div>

    <CCModal :title="isEditEnv ? $t('bian-ji-huan-jing') : $t('chuang-jian-huan-jing')" v-model="showAddEnvModal" @on-cancel="handleCloseModal">
      <Form :model="addEnv" ref="addEnv" :rules="envRules" :label-width="100">
        <FormItem :label="$t('huan-jing-ming-cheng')" prop="envName">
          <Input v-model="addEnv.envName" />
        </FormItem>
        <FormItem :label="$t('bei-zhu')">
          <Input v-model="addEnv.description" />
        </FormItem>
      </Form>
      <template #footer>
        <Button type="default" @click="handleCloseModal">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" :loading="envActionLoading" @click="handleSubmitEnv">
          {{ isEditEnv ? $t('que-ren') : $t('tian-jia') }}
        </Button>
      </template>
    </CCModal>

    <CCModal
      v-model="showTicketModal"
      :title="$t('qi-yong-gong-dan-qing-pei-zhi') + ` ${APPROVAL_TYPE_I18N[showTicketType]}`"
      @onOk="handleBindTicket"
      @onCancel="handleCloseTicketChange"
    >
      <div class="ticket-list">
        <div v-for="def in ticketTypeList" :key="def.approvalType">
          <div v-if="def.enable">
            <div
              :class="`ticket ticket-enable ${ticketDataSelected.approvalType === def.approvalType ? 'ticket-selected' : ''}`"
              @click="handleApprovalTypeChange(def)"
            >
              <CustomIcon :type="`icon-v2-${def.approvalType}`" />
              <div>{{ def.i18n }}</div>
            </div>
          </div>
          <div v-else>
            <Tooltip :content="def.desc" placement="bottom">
              <div :class="`ticket ticket-disabled ${ticketDataSelected.approvalType === def.approvalType ? 'ticket-selected' : ''}`">
                <CustomIcon :type="`icon-v2-${def.approvalType}`" />
                <div>{{ def.i18n }}</div>
              </div>
            </Tooltip>
          </div>
        </div>
      </div>
      <div style="display: flex; justify-content: center">
        <div style="display: flex; width: 400px" v-if="!addTemplateTemplateDialog">
          <Select
            v-model="ticketDataSelected.templateId"
            :disabled="ticketDataSelected.approvalType === 'Disable'"
            :placeholder="$t('qing-xuan-ze-liu-cheng-mo-ban')"
            :not-found-text="$t('zan-wu-shu-ju')"
            filterable
            class="select-wrap"
          >
            <template #prefix>
              <CustomIcon :type="ticketDataSelected.approvalType" rightMargin />
            </template>
            <Option v-for="(temp, index) in templateList" :key="temp.templateIdentity" :value="temp.templateIdentity" :label="temp.approTemplateName">
              <span>{{ temp.approTemplateName }}</span>
              <CustomIcon v-if="isFeishuWechat" type="icon-v2-Delete2" class="option-icon" @click.native.stop="removeTemplate(index)" />
              <CustomIcon
                v-if="ticketDataSelected.approvalType === 'DingTalk'"
                type="icon-v2-CopyOutline"
                class="option-icon"
                @click.native.stop="handleTempCopy(`${temp?.approTemplateName}:${temp?.templateIdentity}`)"
              />
            </Option>
          </Select>
          <Button
            style="margin-left: 16px"
            v-if="ticketDataSelected.approvalType !== 'Feishu' && ticketDataSelected.approvalType !== 'Wechat'"
            :disabled="ticketDataSelected.approvalType === 'Disable'"
            :loading="refreshTemplatesLoading"
            @click="handleRefreshTemplates"
            type="primary"
          >
            {{ $t('shua-xin-mo-ban') }}
          </Button>
          <Button style="margin-left: 16px" v-if="isFeishuWechat" @click="handleAddTemplateDialog" type="primary">
            {{ $t('tian-jia-mo-ban') }}
          </Button>
          <CustomIcon type="icon-v2-help" @click="jumpToImDoc" v-if="isFeishuWechat" leftMargin="10px" hoverStyle />
        </div>
        <div style="display: flex; width: 400px" v-if="addTemplateTemplateDialog">
          <Input v-model="ticketDataSelected.templateUrl" style="width: 300px" />
          <Button style="margin-left: 16px" @click="handleAddTemplate" type="primary">
            {{ $t('tian-jia-mo-ban') }}
          </Button>
        </div>
      </div>
      <template #footer>
        <Button @click="handleCloseTicketChange">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" @click="handleBindTicket">{{ $t('que-ding') }}</Button>
      </template>
    </CCModal>

    <CCModal
      v-model="showEnableSpecModal"
      :title="$t('qi-yong-an-quan-gui-fan-qing-xuan-ze')"
      @onOk="handleBindSpec"
      @onCancel="handleCloseEnableSpecModal"
    >
      <Alert type="warning" v-if="!specList.length">
        {{ $t('dang-qian-mei-you-ke-yong-de-an-quan-gui-fan-qing-xian-qu') }}
        <router-link to="/system/dmspeclist?type=create">{{ $t('an-quan-gui-fan-ye-mian') }}</router-link>
        {{ $t('chuang-jian') }}
      </Alert>
      <Form ref="specForm" :model="specData" :rules="specDataValidate">
        <FormItem prop="check_spec_id" label="">
          <Select v-model="specData.check_spec_id" filterable transfer>
            <Option v-for="spec in specList" :value="spec.specId" :key="spec.specId" :disabled="!spec.enable">
              {{ spec.name }}({{ spec.description }})
            </Option>
          </Select>
        </FormItem>
      </Form>
      <template #footer>
        <Button @click="handleCloseEnableSpecModal">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" @click="handleBindSpec">{{ $t('que-ding') }}</Button>
      </template>
    </CCModal>

    <CCModal
      v-model="showDisableSpecModal"
      :title="$t('an-quan-gui-fan-guan-bi-ti-shi')"
      @onOk="handleDisableSpec"
      @onCancel="handleCloseDisableSpecModal"
    >
      {{ $t('que-ren-ting-yong') }}
      <b>{{ currentEnv?.envName }}</b>
      {{ $t('shang-de-an-quan-gui-fan') }}
    </CCModal>
  </div>
</template>

<script>
import { h, resolveComponent } from 'vue';
import { mapState } from 'vuex';
import deepClone from 'lodash.clonedeep';
import { Tooltip } from 'view-ui-plus';
import { handleCopy } from '@/utils/clipboard';
import { APPROVAL_TYPE_I18N } from '@/const';
import CCModal from '@/components/ui/CCModal';

const EMPTY_ENV = {
  description: '',
  envName: ''
};

const EMPTY_TICKET_DATA = {
  approvalType: '',
  templateId: '',
  templateName: ''
};

const EMPTY_TICKET_SELECTED = {
  approvalType: '',
  templateId: '',
  templateUrl: ''
};

export default {
  name: 'Env',
  components: {
    CCModal,
    Tooltip
  },
  computed: {
    ...mapState(['userInfo', 'myAuth']),
    isFeishuWechat() {
      return this.ticketDataSelected.approvalType === 'Feishu' || this.ticketDataSelected.approvalType === 'Wechat';
    },
    getEnvColumns() {
      if (!this.myAuth.includes('RDP_ENV_MANAGE')) {
        return this.envColumns;
      }
      return [
        ...this.envColumns,
        {
          title: this.$t('cao-zuo'),
          fixed: 'right',
          width: 160,
          slot: 'action'
        }
      ];
    }
  },
  data() {
    return {
      showAddEnvModal: false,
      isEditEnv: false,
      envActionLoading: false,
      loading: false,
      envSearch: '',
      envList: [],
      showEnvList: [],
      total: 0,
      pageSize: 20,
      pageNum: 1,
      addEnv: deepClone(EMPTY_ENV),
      currentEnv: {},
      specData: {
        check_spec_id: ''
      },
      specDataValidate: {
        check_spec_id: [{ required: true, message: '安全规范不能为空' }]
      },
      specList: [],
      showEnableSpecModal: false,
      showDisableSpecModal: false,
      ticketTypeList: [],
      templateList: [],
      ticketData: { ...EMPTY_TICKET_DATA },
      ticketDataSelected: { ...EMPTY_TICKET_SELECTED },
      refreshTemplatesLoading: false,
      addTemplateTemplateDialog: false,
      showTicketModal: false,
      showTicketType: '',
      envRules: {
        envName: [
          {
            required: true,
            message: this.$t('qing-tian-xie-huan-jing-ming-cheng'),
            trigger: 'blur'
          }
        ]
      },
      envColumns: [
        {
          title: this.$t('huan-jing-ming-cheng'),
          key: 'envName',
          width: 200
        },
        {
          title: this.$t('huan-jing-miao-shu'),
          key: 'envDesc',
          minWidth: 150,
          slot: 'envDesc'
        },
        {
          title: this.$t('an-quan-gui-fan'),
          slot: 'securitySpec',
          width: 140,
          align: 'left'
        },
        {
          title: this.$t('sql-gong-dan-config'),
          slot: 'sqlTicket',
          width: 140,
          align: 'left'
        },
        {
          title: this.$t('bian-geng-gong-dan-config'),
          slot: 'changeTicket',
          width: 140,
          align: 'left'
        },
        {
          title: this.$t('quan-xian-gong-dan-config'),
          slot: 'authTicket',
          width: 140,
          align: 'left'
        },
        {
          renderHeader: () => {
            return h(
              'div',
              {
                style: { display: 'flex', alignItems: 'center', gap: '4px' }
              },
              [
                this.$t('yu-ju-xian-zhi'),
                h(
                  resolveComponent('a-tooltip'),
                  {
                    placement: 'top'
                  },
                  {
                    title: () => this.$t('shi-fou-yun-xu-cha-xun-yi-wai-qi-ta-lei-xing-yu-ju-tong-guo-kong-zhi-tai-shang-zhi-xing'),
                    default: () =>
                      h(resolveComponent('CustomIcon'), {
                        type: 'icon-v2-help',
                        class: 'help-icon'
                      })
                  }
                )
              ]
            );
          },
          slot: 'sqlLimit',
          width: 150,
          align: 'left'
        }
      ],
      APPROVAL_TYPE_I18N
    };
  },
  mounted() {
    this.listEnv();
  },
  methods: {
    handleEnterSearch(e) {
      if (e.code === 'Enter') {
        e.preventDefault();
        this.listEnv('init');
      }
    },
    handlePageChange(pageNum) {
      this.pageNum = pageNum;
      this.setTableShowData();
    },
    handlePageSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.handlePageChange(1);
    },
    setTableShowData(type) {
      if (type === 'init') {
        this.pageNum = 1;
      }
      this.showEnvList = this.envList.slice((this.pageNum - 1) * this.pageSize, this.pageNum * this.pageSize);
    },
    async listEnv(type) {
      this.loading = true;
      const res = await this.$services.dmEnvParamListEnvParamForSec({
        envName: this.envSearch
      });
      this.loading = false;
      if (res.success) {
        this.envList = res.data;
        this.total = res.data.length;
        this.setTableShowData(type);
      }
    },
    handleShowAddEnv() {
      this.addEnv = deepClone(EMPTY_ENV);
      this.isEditEnv = false;
      this.showAddEnvModal = true;
    },
    handleEdit(row) {
      this.addEnv = {
        id: row.envId,
        envName: row.envName,
        description: row.envDesc || ''
      };
      this.isEditEnv = true;
      this.showAddEnvModal = true;
    },
    async addEnvFunc() {
      const res = await this.$services.rdpDsEnvAdd({
        data: {
          authKinds: [],
          description: this.addEnv.description,
          envName: this.addEnv.envName,
          queryLimit: this.addEnv.queryLimit
        }
      });
      if (res.success) {
        this.$Message.success(this.$t('tian-jia-cheng-gong'));
        this.handleCloseModal();
        await this.listEnv('init');
      }
    },
    async editEnvFunc() {
      const res = await this.$services.rdpDsEnvUpdate({
        data: {
          authKinds: [],
          description: this.addEnv.description,
          envName: this.addEnv.envName,
          queryLimit: this.addEnv.queryLimit,
          dsEnvId: this.addEnv.id
        }
      });
      if (res.success) {
        this.$Message.success(this.$t('bian-ji-cheng-gong'));
        this.handleCloseModal();
        await this.listEnv('init');
      }
    },
    handleSubmitEnv() {
      this.$refs.addEnv.validate(async (valid) => {
        if (!valid) {
          return;
        }
        this.envActionLoading = true;
        try {
          if (this.isEditEnv) {
            await this.editEnvFunc();
          } else {
            await this.addEnvFunc();
          }
        } finally {
          this.envActionLoading = false;
        }
      });
    },
    async handleDeleteEnv(row) {
      const res = await this.$services.rdpDsEnvDelete({
        data: {
          dsEnvId: row.envId
        }
      });
      if (res.success) {
        this.$Message.success(this.$t('shan-chu-cheng-gong'));
        await this.listEnv('init');
      }
    },
    handleCloseModal() {
      this.showAddEnvModal = false;
      this.isEditEnv = false;
      this.envActionLoading = false;
      this.showEnableSpecModal = false;
      this.showDisableSpecModal = false;
      this.showTicketModal = false;
      this.addTemplateTemplateDialog = false;
      this.currentEnv = {};
      this.addEnv = deepClone(EMPTY_ENV);
      this.ticketData = { ...EMPTY_TICKET_DATA };
      this.ticketDataSelected = { ...EMPTY_TICKET_SELECTED };
      this.specData = {
        check_spec_id: ''
      };
      this.$nextTick(() => {
        this.$refs.addEnv?.resetFields();
      });
    },
    async handleAllowAllStatementsChange(row) {
      if (!row || !row.envId) {
        return;
      }

      try {
        if (row.allowAllStatements) {
          const res = await this.$services.dmEnvParamBindEnvParam({
            data: {
              envId: row.envId,
              paramKey: 'dm_allow_all_statements',
              paramValue: 'true'
            }
          });

          if (res.success) {
            this.$Message.success(this.$t('huan-jing-ce-lue-sql-zhi-xing-false', [row.envName]));
          } else {
            this.$Message.error(this.$t('huan-jing-ce-lue-sql-zhi-xing-failed', [row.envName]));
            row.allowAllStatements = false;
          }
        } else {
          const res = await this.$services.dmEnvParamUnbindEnvParam({
            data: {
              envId: row.envId,
              paramKey: 'dm_allow_all_statements'
            }
          });

          if (res.success) {
            this.$Message.success(this.$t('huan-jing-ce-lue-sql-zhi-xing-true', [row.envName]));
          } else {
            this.$Message.error(this.$t('huan-jing-ce-lue-sql-zhi-xing-failed', [row.envName]));
            row.allowAllStatements = true;
          }
        }
      } catch (error) {
        row.allowAllStatements = !row.allowAllStatements;
      }
    },
    async handleSwitchSpecChange(row) {
      this.currentEnv = row;
      if (!row.secDesVO.openSec) {
        const res = await this.$services.dmDataSourceListSpec({
          data: {}
        });
        if (res.success) {
          this.specList = res.data;
          this.showEnableSpecModal = true;
          if (this.specList && this.specList.length) {
            for (let i = 0; i < this.specList.length; i++) {
              const spec = this.specList[i];
              if (spec.enable) {
                this.specData.check_spec_id = spec.specId;
                break;
              }
            }
          }
        }
      } else {
        this.handleDisableSpec();
      }
    },
    async handleDisableSpec() {
      if (!this.currentEnv || !this.currentEnv.envId) {
        this.$Message.error('环境信息不存在');
        return;
      }
      const res = await this.$services.dmEnvParamUnbindEnvParam({
        data: {
          envId: this.currentEnv.envId,
          paramKey: 'check_spec_id'
        }
      });

      if (res.success) {
        this.$Message.success(`环境 ${this.currentEnv.envName} 上的安全规范已停用`);
        this.handleCloseModal();
        await this.listEnv();
      } else {
        this.handleCloseDisableSpecModal();
      }
    },
    async handleDisableSecuritySpec(row) {
      this.currentEnv = row;
      await this.handleDisableSpec();
    },
    handleCloseEnableSpecModal() {
      if (this.currentEnv && this.currentEnv.secDesVO) {
        this.currentEnv.secDesVO.openSec = false;
      }
      this.handleCloseModal();
    },
    handleCloseDisableSpecModal() {
      if (this.currentEnv && this.currentEnv.secDesVO) {
        this.currentEnv.secDesVO.openSec = true;
      }
      this.handleCloseModal();
    },
    async handleBindSpec() {
      if (!this.currentEnv || !this.currentEnv.envId) {
        this.$Message.error('环境信息不存在');
        return;
      }
      this.$refs.specForm.validate(async (valid) => {
        if (valid) {
          const res = await this.$services.dmEnvParamBindEnvParam({
            data: {
              envId: this.currentEnv.envId,
              paramKey: 'check_spec_id',
              paramValue: this.specData.check_spec_id
            }
          });

          if (res.success) {
            let specName = '';
            this.specList.forEach((spec) => {
              if (spec.specId === this.specData.check_spec_id) {
                specName = spec.name;
              }
            });
            this.$Message.success(`环境 ${this.currentEnv.envName} 上已启用 ${specName} 作为安全规范`);
            this.handleCloseModal();
            await this.listEnv();
          } else {
            this.handleCloseEnableSpecModal();
          }
        }
      });
    },
    jumpToImDoc() {
      const type = this.ticketDataSelected.approvalType;
      let url = '';

      switch (type) {
        case 'Feishu':
          url = 'https://www.clougence.com/dm-doc/approval/engine/feishu_approval#use';
          break;
        case 'Wechat':
          url = 'https://www.clougence.com/dm-doc/approval/engine/wechat_approval#use';
          break;
        default:
          return;
      }
      window.open(url, '_blank');
    },
    removeTemplate(index) {
      this.$Modal.confirm({
        title: this.$t('ti-shi'),
        content: this.$t('que-ren-yao-shan-chu-ci-mo-ban-ma'),
        onOk: () => {
          this.templateList.splice(index, 1);
          this.handleRemoveTemplate();
        }
      });
    },
    handleTempCopy(item) {
      handleCopy(item);
      this.$Message.success(this.$t('fu-zhi-cheng-gong'));
    },
    async handleShowTicketChange(env, target, type) {
      this.showTicketType = type;
      this.currentEnv = env;
      this.ticketData = {
        approvalType: target.openTicket ? target.type : 'Disable',
        templateId: target.templateId,
        templateName: target.templateName
      };
      this.ticketDataSelected = { ...EMPTY_TICKET_SELECTED, ...this.ticketData };

      if (this.ticketTypeList === null || this.ticketTypeList.length === 0) {
        const res = await this.$services.dmTicketTicketType();
        if (res.success) {
          this.ticketTypeList = res.data;
          this.ticketTypeList.unshift({
            approvalType: 'Disable',
            i18n: '禁用',
            enable: type === 'ticket_info',
            desc: '不可用'
          });
          this.showTicketModal = true;
        } else {
          this.$Message.error(this.$t('cao-zuo-shi-bai'));
          this.showTicketModal = false;
          return;
        }
      } else {
        this.ticketTypeList[0].enable = type === 'ticket_info';
        this.showTicketModal = true;
      }

      await this.handleApprovalTypeChange(this.ticketTypeList.find((d) => d.approvalType === target.type) || this.ticketTypeList[0]);
    },
    handleCloseTicketChange() {
      this.showTicketModal = false;
      this.ticketData = { ...EMPTY_TICKET_DATA };
      this.ticketDataSelected = { ...EMPTY_TICKET_SELECTED };
      this.showTicketType = '';
      this.currentEnv = {};
      this.addTemplateTemplateDialog = false;
    },
    async handleApprovalTypeChange(def) {
      this.addTemplateTemplateDialog = false;

      if (def.approvalType === 'Disable') {
        this.templateList = [];
        this.ticketDataSelected.approvalType = def.approvalType;
        this.ticketDataSelected.templateId = '';
        return;
      }

      const res = await this.$services.dmTicketListTemplates({
        data: {
          uid: this.userInfo.uid,
          approvalType: def.approvalType
        }
      });
      if (res.success) {
        this.templateList = res.data;
      } else {
        this.$Message.error(this.$t('huo-qu-mo-ban-shi-bai'));
        return;
      }

      const selectOld = def.approvalType === this.ticketData.approvalType;
      if (selectOld) {
        this.ticketDataSelected.approvalType = def.approvalType;
        this.ticketDataSelected.templateId = this.ticketData.templateId;
      } else {
        this.ticketDataSelected.approvalType = def.approvalType;
        this.ticketDataSelected.templateId = this.templateList[0]?.templateIdentity || '';
      }
    },
    async handleRefreshTemplates() {
      this.refreshTemplatesLoading = true;
      const res = await this.$services.dmTicketRefreshTemplates({
        data: {
          approvalType: this.ticketDataSelected.approvalType
        }
      });
      this.templateList = res.data;
      this.refreshTemplatesLoading = false;
    },
    handleAddTemplateDialog() {
      this.addTemplateTemplateDialog = true;
      this.ticketDataSelected.templateUrl = '';
    },
    async handleAddTemplate() {
      const res = await this.$services.dmTicketAddTemplate({
        data: {
          approvalType: this.ticketDataSelected.approvalType,
          templateUrl: this.ticketDataSelected.templateUrl
        }
      });
      if (res.success) {
        this.addTemplateTemplateDialog = false;
        this.handleRefreshTemplates();
      } else {
        this.$Message.error(this.$t('cao-zuo-shi-bai'));
      }
    },
    async handleRemoveTemplate() {
      const res = await this.$services.dmTicketRemoveTemplate({
        data: {
          approvalType: this.ticketDataSelected.approvalType,
          templateId: this.ticketDataSelected.templateId
        }
      });
      if (res.success) {
        this.handleRefreshTemplates();
      } else {
        this.$Message.error(this.$t('cao-zuo-shi-bai'));
      }
    },
    async handleBindTicket() {
      const data = {
        approvalType: this.ticketDataSelected.approvalType,
        templateId: this.ticketDataSelected.templateId
      };

      if (this.ticketDataSelected.approvalType === 'Disable') {
        data.approvalType = '';
        data.templateId = '';
        data.templateName = '';
      } else {
        const found = this.templateList.find((d) => d.templateIdentity === this.ticketDataSelected.templateId);
        if (found) {
          data.templateName = found.approTemplateName;
        } else {
          this.$Message.error('模版不能为空');
          return;
        }
      }

      const approvalType = this.APPROVAL_TYPE_I18N[this.showTicketType];
      const title =
        this.ticketDataSelected.approvalType === 'Disable'
          ? `环境 <b>${this.currentEnv.envName}</b> 上禁止 <b>${approvalType}</b>。`
          : `环境 <b>${this.currentEnv.envName}</b> 中的 <b>${approvalType}</b> 使用<br/><b>${data.templateName}</b> 进行流程审批。`;

      this.$Modal.confirm({
        title: this.$t('cao-zuo-que-ren'),
        content: title,
        onOk: async () => {
          const res = await this.$services.dmEnvParamBindEnvParam({
            data: {
              envId: this.currentEnv.envId,
              paramKey: this.showTicketType,
              paramValue: JSON.stringify(data)
            }
          });

          if (res.success) {
            this.$Message.success(`环境 ${this.currentEnv.envName} 可以使用 ${data.templateName} 向工单系统发起审批流程`);
            this.handleCloseModal();
            await this.listEnv();
          } else {
            this.handleCloseModal();
          }
        }
      });
    }
  }
};
</script>

<style lang="less" scoped>
.env {
  height: 100%;

  .env-desc-cell {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 100%;
    cursor: help;
  }
}

.ticket-list {
  margin-top: 10px;
  margin-bottom: 20px;
  align-items: center;
  justify-content: center;
  display: flex;

  .ticket {
    width: 60px;
    height: 60px;
    border: 1px solid #ccc;
    border-radius: 6px;
    margin-right: 5px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }

  .ticket-enable {
    cursor: pointer;
  }

  .ticket-disabled {
    cursor: not-allowed;
    background: #efefef;
  }

  .ticket-selected {
    border: 2px solid #43cf7c;
  }
}

.select-wrap {
  :deep(.ivu-select-dropdown) {
    max-height: 200px !important;
  }
}

.option-icon {
  position: absolute;
  right: 10px;
  border-radius: 10px;
  padding: 1px;
}

.option-icon:hover {
  background: #ccc;
}

.sql-limit-container {
  display: flex;
  align-items: center;
  gap: 8px;
}

.help-icon {
  font-size: 14px;
  color: #999;
  cursor: pointer;

  &:hover {
    color: #1890ff;
  }
}

.ticket-text-container {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.ticket-button-only {
  padding: 4px 0;
}

.ticket-info {
  position: relative;
}

.ticket-config-btn {
  position: absolute;
  top: 0;
  right: 4px;
  z-index: 1;
  padding: 0 4px;
  height: 20px;
  line-height: 20px;
  font-size: 12px;
}

.ticket-status-text {
  color: var(--success-color);
}

.ticket-status-text-error {
  color: var(--text-disabled);
}

.delete-tag {
  color: #ff4d4f;
  font-size: 11px;
  margin-left: 4px;
}

.ticket-popover-content {
  display: flex;
  flex-direction: column;
  padding: 4px 0;
}

.ticket-popover-label {
  color: rgba(0, 0, 0, 0.65);
  margin-right: 2px;
  text-align: right;
}

.ticket-popover-value {
  color: rgba(0, 0, 0, 0.88);
  word-break: break-all;

  &.deleted {
    color: #ff4d4f;
  }
}

[data-theme='dark'] {
  .ticket-status-text {
    color: var(--success-color) !important;
  }

  .ticket-status-text-error {
    color: var(--text-disabled) !important;
  }
}
</style>
