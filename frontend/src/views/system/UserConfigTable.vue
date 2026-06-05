<template>
  <Table border size="small" :columns="paramsColumn" :data="userConfigMap" class="border-radius-card-bottom" max-height="600" :loading="loading">
    <template #configValue="{ row }">
      <p style="position: relative; padding-right: 15px">
        <span
          v-if="isJSON(row.configValue)"
          style="white-space: nowrap; text-overflow: ellipsis; overflow: hidden; word-break: break-all; display: block"
        >
          {{ row.configValue }}
          <a style="position: absolute; right: 0; font-size: 14px" @click="handleShowJson(row)">
            <Icon type="md-arrow-dropdown" />
          </a>
        </span>
        <span v-if="!isJSON(row.configValue)">{{ row.configValue ? row.configValue : '-' }}</span>
      </p>
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
    <template #currentValue="{ row }">
      <div>
        <div v-if="!row.readOnly && canEdit">
          <div v-if="row.confValType === 'BOOLEAN'">
            <i-switch true-color="#52C41A" v-model="row.formatValue" @on-change="handleEditCurrent(row, row.formatValue)"></i-switch>
          </div>
          <div v-if="row.confValType !== 'BOOLEAN'">
            <span style="margin-right: 16px; display: inline-block; width: 60px">
              {{ row.currentCount }}
            </span>
            <span>
              <Poptip v-model="row.visible" transfer @on-popper-show="handlePopShow(row)" placement="right" width="250">
                <a><CustomIcon type="icon-v2-EditSimple" /></a>
                <template #content>
                  <p style="font-size: 12px; margin-bottom: 10px">{{ $t('xiu-gai-can-shu-wei') }}:</p>
                  <p style="margin-bottom: 10px">
                    <Input size="small" type="textarea" :rows="3" style="width: 200px" v-model="currentValue" />
                  </p>
                  <p>
                    <Button style="margin-right: 5px" type="primary" size="small" @click="handleEditCurrent(row, currentValue)">
                      {{ $t('que-ding') }}
                    </Button>
                  </p>
                </template>
              </Poptip>
              <Tooltip transfer style="margin-left: 5px" :content="$t('che-xiao')" placement="right" v-if="row.currentCount">
                <a style="font-size: 16px" @click="handleCancelEdit(row)">
                  <CustomIcon type="icon-v2-Reset" />
                </a>
              </Tooltip>
            </span>
          </div>
        </div>
        <div v-if="row.readOnly">
          {{ $t('zhi-du-can-shu') }}
        </div>
      </div>
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
      currentValue: '',
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
          width: 260,
          slot: 'configValue'
        },
        {
          title: this.$t('xiu-gai-hou-de-can-shu-zhi'),
          key: 'currentCount',
          slot: 'currentValue',
          width: 160,
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
    getTagStyle(row) {
      return {
        color: pick(this.showTagList.indexOf(row.userConfigTagType)),
        borderColor: pick(this.showTagList.indexOf(row.userConfigTagType))
      };
    },
    handleCancelEdit(row) {
      this.userConfigMap.forEach((item) => {
        if (item.configName === row.configName) {
          item.currentCount = '';
        }
      });
      this.userConfigMap = [...this.userConfigMap];
    },
    handlePopShow(row) {
      this.currentValue = row.configValue;
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
        this.paramsColumn[4].filters = filters;
      },
      deep: true
    }
  }
};
</script>
