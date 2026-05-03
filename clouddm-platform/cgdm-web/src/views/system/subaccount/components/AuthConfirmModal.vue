<template>
  <a-modal :visible="visible" @cancel="handleCloseModal" :title="$t('shu-ju-ku-shou-quan-que-ren')" :width="1100">
    <a-table :columns="columns" size="small" :data-source="Object.values(authList)" :rowKey="(record) => record.tableName">
      <template #duration="record">
        {{ renderDurationTime(record.startTime || record.endTime ? record : { startTime, endTime }) }}
      </template>
      <template #name="record">{{ record.dsEnvName }}/{{ record.instanceDesc }}/{{ record.tableName }}</template>
      <template v-for="auth in dsAuthKind" #[`${auth.kindValue}-title`]>
        <div :key="auth.kindValue">
          {{ auth.kindNameI18n }}
        </div>
      </template>
      <template v-for="kind in dsAuthKind" v-slot:[kind.kindValue]="record">
        <div :key="`${record.dsEnvName}/${record.instanceDesc}/${record.tableName}/${kind.kindValue}`">
          <cc-iconfont
            :name="record[kind.kindValue] ? 'check' : record.preAuthArr.includes(kind.kindValue) ? 'minus' : ''"
            :color="
              record.preAuthArr.includes(kind.kindValue) && !record.intersection.includes(kind.kindValue)
                ? '#FF6E0D'
                : record.currentAuthArr.includes(kind.kindValue) && !record.intersection.includes(kind.kindValue)
                  ? '#52C41A'
                  : '#CCC'
            "
            :style="`border: ${
              record.preAuthArr.includes(kind.kindValue) || record.currentAuthArr.includes(kind.kindValue)
                ? '1px solid rgba(204, 204, 204, 1)'
                : 'none'
            }`"
          />
        </div>
      </template>
    </a-table>
    <div class="icons" v-if="Object.values(authList).length">
      <div class="icon">
        <cc-iconfont name="check" style="border: 1px solid rgba(204, 204, 204, 1)" color="#52C41A" />
        <span class="title">{{ $t('xin-zeng-de-quan-xian') }}</span>
      </div>
      <div class="icon">
        <cc-iconfont name="minus" style="border: 1px solid rgba(204, 204, 204, 1)" color="#FF6E0D" />
        <span class="title">{{ $t('qu-xiao-de-quan-xian') }}</span>
      </div>
      <div class="icon">
        <cc-iconfont name="check" style="border: 1px solid rgba(204, 204, 204, 1)" color="#CCCCCC" />
        <span class="title">{{ $t('yuan-you-de-quan-xian') }}</span>
      </div>
      <a-date-picker
        ref="start-time-picker"
        v-model="startTime"
        show-time
        format="YYYY-MM-DD HH:mm:ss"
        :placeholder="$t('kai-shi-shi-jian')"
        @openChange="handleStartOpenChange"
        renderExtraFooter
        size="small"
      >
        <template #renderExtraFooter>
          <div style="display: flex">
            <div
              v-for="r in ranges"
              :key="r.key"
              @click="handleRangeClick(r.key)"
              :style="`cursor: pointer;margin-right: 5px;color: ${range === r.key ? '#0BB9F8' : 'rgba(0, 0, 0, 0.65)'}`"
            >
              {{ r.label }}
            </div>
          </div>
        </template>
      </a-date-picker>
      ~
      <a-date-picker
        v-model="endTime"
        show-time
        format="YYYY-MM-DD HH:mm:ss"
        :placeholder="$t('jie-shu-shi-jian')"
        :open="endTimeOpen"
        @openChange="handleEndOpenChange"
        size="small"
      />
    </div>
    <div class="footer">
      <a-button type="primary" @click="handleConfirm(startTime, endTime)">
        {{ $t('que-ding') }}
      </a-button>
      <a-button @click="handleCloseModal">{{ $t('qu-xiao') }}</a-button>
    </div>
  </a-modal>
</template>

<script>
import moment from 'moment';
import { dateRange } from '../../../../utils';
import authMixin from '../../../../mixins/authMixin';

export default {
  name: 'AuthConfirmModal',
  mixins: [authMixin],
  props: {
    visible: Boolean,
    handleCloseModal: Function,
    authList: {
      type: Object,
      default: () => ({})
    },
    handleConfirm: Function,
    columns: Array,
    dsAuthKind: Array
  },
  data() {
    return {
      dateRange,
      ranges: [
        { key: 1, label: '1天' },
        { key: 7, label: '7天' },
        { key: 30, label: '30天' }
      ],
      startTime: null,
      endTime: null,
      endTimeOpen: false,
      range: -1
    };
  },
  methods: {
    handleRangeClick(key) {
      this.range = key;
      this.startTime = moment();
      this.endTime = moment().add(key, 'd');
      this.$refs['start-time-picker'].$refs.picker.sOpen = false;
    },
    handleStartOpenChange(open) {
      if (!open) {
        this.endOpen = true;
      }
    },
    handleEndOpenChange(open) {
      this.endTimeOpen = open;
    }
  }
};
</script>

<style scoped lang="less">
.icons {
  display: flex;
  margin-top: -42px;

  .icon {
    .title {
      margin-top: -5px;
      margin-left: 10px;
      margin-right: 20px;
    }
  }
}
</style>
