<template>
  <Table border size="small" :columns="paramsColumn" :data="userConfigMap" class="border-radius-card-bottom" max-height="600" :loading="loading">
    <template #configValue="{ row }">
      <div class="config-value-cell" :class="{ 'is-edited': hasEditedValue(row) }">
        <div class="config-value-content">
          <template v-if="row.__editing">
            <i-switch
              v-if="row.confValType === 'BOOLEAN'"
              true-color="#52C41A"
              v-model="row.__editValue"
              @on-change="handleInlineBooleanChange(row, $event)"
            ></i-switch>
            <Input
              v-else
              size="small"
              :type="isJSON(currentDisplayValue(row)) ? 'textarea' : 'text'"
              :rows="3"
              v-model="row.__editValue"
              @on-enter="confirmInlineEdit(row)"
              @on-blur="confirmInlineEdit(row)"
            />
          </template>
          <template v-else>
            <span v-if="isJSON(currentDisplayValue(row))" class="config-value-text">
              {{ currentDisplayValue(row) }}
            </span>
            <span v-else class="config-value-text">{{ formatDisplayValue(currentDisplayValue(row)) }}</span>
          </template>
        </div>
        <div class="config-value-actions">
          <a v-if="!row.__editing && isJSON(currentDisplayValue(row))" class="config-value-action" @click="handleShowCurrentJson(row)">
            <Icon type="md-arrow-dropdown" />
          </a>
          <Tooltip transfer :content="$t('che-xiao')" placement="right" v-if="!row.readOnly && canEdit && showCancelAction(row)">
            <a class="config-value-action" @click="handleCancelEdit(row)">
              <CustomIcon type="icon-v2-Reset" />
            </a>
          </Tooltip>
          <a v-if="!row.readOnly && canEdit && !row.__editing" class="config-value-action" @click="startInlineEdit(row)">
            <CustomIcon type="icon-v2-EditSimple" />
          </a>
          <a
            v-if="!row.readOnly && canEdit && row.__editing && row.confValType !== 'BOOLEAN'"
            class="config-value-action"
            @mousedown.prevent
            @click="confirmInlineEdit(row)"
          >
            <Icon type="md-checkmark" />
          </a>
        </div>
      </div>
    </template>
    <template #paramName="{ row }">
      <p style="position: relative">
        <span :style="`font-style: italic;font-size: 14px;font-weight: 500;color: ${row.needCreated ? '#ff6e0d' : ''}`">
          {{ row.configName }}
          {{ row.needCreated ? $t('dai-chuang-jian') : '' }}
          <Tooltip transfer placement="right" style="position: absolute; right: 0; top: 0">
            <template #content>
              <div v-html="row.description"></div>
            </template>
            <a><CustomIcon size="14px" type="icon-v2-InfoColorful" /></a>
          </Tooltip>
        </span>
      </p>
    </template>
    <template #tag="{ row }">
      <!-- 映射对不上，暂时隐藏popover内容 -->
      <!-- <Tooltip placement="right-start" transfer class="ml-2"> -->
      <Button type="warning" size="small" ghost :style="getTagStyle(row)">
        {{ row.userConfigTagType }}
      </Button>
      <!-- <template #content>
          <div>
            {{ UtilJson?.tagInfo?.[row.userConfigTagType] }}
          </div>
        </template> -->
      <!-- </Tooltip> -->
    </template>
    <template #action="{ row }">
      <Button ghost type="primary" size="small" style="margin-right: 5px" @click="handleShowEditParams(row, 'serviceCoreParamsData')">
        {{ $t('xiu-gai') }}
      </Button>
    </template>
  </Table>
</template>
<script>
import UtilJson from '@/views/util';
import { pick } from '@/components/function/monitor/utils/colors';

export default {
  name: 'UserConfigTable',
  props: {
    userConfigMap: Array,
    handleShowJson: Function,
    isJSON: Function,
    canEdit: Boolean,
    handleShowEditParams: Function,
    handleEditCurrent: Function,
    showTagList: Array,
    loading: Boolean
  },
  data() {
    return {
      UtilJson,
      paramsColumn: [
        {
          title: this.$t('can-shu-ming-cheng'),
          key: 'configName',
          slot: 'paramName',
          width: 300,
          sortable: true
        },
        {
          title: this.$t('can-shu-dang-qian-yun-hang-zhi'),
          key: 'configValue',
          slot: 'configValue',
          minWidth: 260,
          filters: [
            {
              label: this.$t('ke-xiu-gai-can-shu'),
              value: 1
            },
            {
              label: this.$t('zhi-du-can-shu'),
              value: 2
            }
          ],
          filterMultiple: false,
          filterMethod(value, row) {
            if (value === 1) {
              return !row.readOnly;
            }
            if (value === 2) {
              return row.readOnly;
            }
          }
        },
        {
          title: this.$t('ke-xiu-gai-fan-wei'),
          key: 'valueRange',
          render: (h, params) => h('div', {}, params.row.valueRange || '-')
        },
        {
          title: this.$t('biao-qian'),
          width: 160,
          filters: [],
          filterMethod(value, row) {
            return row.userConfigTagType === value;
          },
          filterMultiple: false,
          slot: 'tag'
          // render: (h, params) =>
          //   h('div', [
          //     h(
          //       'Tooltip',
          //       {
          //         content: UtilJson.tagInfo[params.row.userConfigTagType],
          //         placement: 'left-start',
          //         transfer: true
          //       },
          //       [
          //         h(
          //           'pd-button',
          //           {
          //             type: 'warning',
          //             size: 'small',
          //             ghost: true,
          //             style: {
          //               color: pick(this.showTagList.indexOf(params.row.userConfigTagType)),
          //               borderColor: pick(this.showTagList.indexOf(params.row.userConfigTagType))
          //             }
          //           },
          //           params.row.userConfigTagType
          //         )
          //       ]
          //     )
          //   ])
        }
      ]
    };
  },
  methods: {
    currentDisplayValue(row) {
      return this.hasEditedValue(row) ? row.currentCount : row.configValue;
    },
    formatDisplayValue(value) {
      return value === undefined || value === null || value === '' ? '-' : value;
    },
    hasEditedValue(row) {
      if (row.currentCount === undefined || row.currentCount === null || row.currentCount === '') {
        return false;
      }
      return String(row.currentCount) !== String(row.configValue);
    },
    showCancelAction(row) {
      return this.hasEditedValue(row) || (row.__editing && row.confValType === 'BOOLEAN');
    },
    getTagStyle(row) {
      return {
        color: pick(this.showTagList.indexOf(row.userConfigTagType)),
        borderColor: pick(this.showTagList.indexOf(row.userConfigTagType))
      };
    },
    handleShowCurrentJson(row) {
      if (this.hasEditedValue(row)) {
        this.handleShowJson(row, 'read', 'currentCount');
        return;
      }
      this.handleShowJson(row);
    },
    startInlineEdit(row) {
      row.__editing = true;
      if (row.confValType === 'BOOLEAN') {
        const currentValue = this.currentDisplayValue(row);
        row.__editValue = typeof currentValue === 'boolean' ? currentValue : currentValue === 'true';
      } else {
        row.__editValue = this.currentDisplayValue(row);
      }
      this.userConfigMap = [...this.userConfigMap];
    },
    confirmInlineEdit(row) {
      if (!row.__editing || row.confValType === 'BOOLEAN') {
        return;
      }
      this.handleEditCurrent(row, row.__editValue);
      row.__editing = false;
      this.userConfigMap = [...this.userConfigMap];
    },
    handleInlineBooleanChange(row, value) {
      this.handleEditCurrent(row, value);
      row.__editing = false;
      this.userConfigMap = [...this.userConfigMap];
    },
    handleCancelEdit(row) {
      this.userConfigMap.forEach((item) => {
        if (item.configName === row.configName) {
          item.currentCount = '';
          item.__editing = false;
          item.__editValue = '';
          if (item.confValType === 'BOOLEAN') {
            item.formatValue = item.configValue === 'true';
          }
        }
      });
      this.userConfigMap = [...this.userConfigMap];
    }
  },
  watch: {
    userConfigMap: {
      handler(newVal) {
        const filters = [];
        newVal.forEach((userConfig) => {
          const currentTag = {
            label: userConfig.userConfigTagType,
            value: userConfig.userConfigTagType
          };
          const contains = filters.some((item) => item.label === userConfig.userConfigTagType);
          if (!contains) {
            filters.push(currentTag);
          }
        });
        this.paramsColumn[3].filters = filters;
      },
      deep: true
    }
  }
};
</script>
<style lang="less" scoped>
.config-value-cell {
  display: flex;
  align-items: center;
  min-height: 26px;
  gap: 8px;
}

.config-value-content {
  flex: 1;
  min-width: 0;
}

.config-value-text {
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  word-break: break-all;
}

.config-value-cell.is-edited .config-value-text {
  color: #d48806;
  font-weight: 600;
}

.config-value-actions {
  display: inline-flex;
  align-items: center;
  justify-content: flex-end;
  gap: 6px;
  min-width: 42px;
  margin-left: auto;
}

.config-value-action {
  display: inline-flex;
  align-items: center;
  color: #8d95a6;
  line-height: 1;
}
</style>
