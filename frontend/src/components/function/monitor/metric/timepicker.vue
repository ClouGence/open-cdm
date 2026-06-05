<template>
    <div>
        <Dropdown trigger="custom" :visible="timepickerVisible" placement="bottom-end">
            <Tooltip placement="bottom" transfer>
                <Button @click="handleTimepickerOpen">
                    <i class="fa fa-clock-o" style="margin-right: 6px"></i>

                    <span>{{ rangeString }}</span>

                    <span v-if="!!refreshString" class="text-primary">{{ $t('refreshstring-zi-dong-shua-xin', [refreshString]) }}</span>
                </Button>
                <template #content>
                    {{fromTooltip}} <br>to<br> {{toTooltip}}
                </template>
            </Tooltip>
            <template #list>
              <DropdownMenu>
                <div class="gf-timepicker-dropdown clearfix" style="width: 1000px;padding: 10px 20px;">

                    <div class="gf-timepicker-absolute-section">
                        <h3 class="section-heading">{{ $t('zi-ding-yi-xuan-ze') }}</h3>

                        <label class="small">{{ $t('cong') }}</label>
                        <div class="gf-form-inline">
                            <div class="gf-form max-width-28">
<!--                                <i-input v-model="editTimeRaw.from"></i-input>-->
                            </div>
                            <div class="gf-form" style="margin-left: 2px">
                                <DatePicker  type="datetime" v-model="fromDate" format="yyyy-MM-dd HH:mm" @on-change="handleFromChange" @on-ok="handleFromOk">
<!--                                    <Button type="primary" @click="handleFromOpen"><i class="fa fa-calendar"></i></Button>-->
                                </DatePicker>
                            </div>
                        </div>

                        <label class="small">{{ $t('dao') }}</label>
                        <div class="gf-form-inline">
                            <div class="gf-form max-width-28">
<!--                                <i-input v-model="editTimeRaw.to"></i-input>-->
                            </div>
                            <div class="gf-form" style="margin-left: 2px">
                                <DatePicker  type="datetime" v-model="toDate" format="yyyy-MM-dd HH:mm" @on-change="handleToChange" @on-ok="handleToOk">
<!--                                    <Button type="primary" @click="handleToOpen"><i class="fa fa-calendar"></i></Button>-->
                                </DatePicker>
                            </div>
                        </div>

                        <label class="small">{{ $t('zi-dong-shua-xin') }}</label>
                        <div class="gf-form-inline">
                            <div class="gf-form max-width-28">
                                <Select v-model="refreshValue" style="width: 150px">
                                    <Option :value="option.value" v-for="option in refreshOptions" :key="option.value">{{option.label}}</Option>
                                </Select>
                            </div>
                            <div class="gf-form" style="margin-left: 10px">
                                <Button type="primary" size="small" @click="applyCustom()">{{ $t('que-ren') }}</Button>
                            </div>
                        </div>
                    </div>

                    <div class="gf-timepicker-relative-section">
                        <h3 class="section-heading">{{ $t('kuai-su-xuan-ze') }}</h3>
                        <ul v-for="(group, key) in timeOptions" :key="key">
                            <li v-for="(option, key) in group" :key="key">
                                <a @click="setRelativeFilter(option)">{{option.display}}</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </DropdownMenu>
            </template>
        </Dropdown>
    </div>
</template>

<script>
import _ from 'lodash';
import moment from 'moment';
import * as rangeUtil from '../panel/utils/rangeutil';
import kbn from '../panel/utils/kbn';
import eventBus from '@/utils/eventBus';

export default {

  mounted() {
    this.refresh(false);

    this.$bus.on('to_refresh', () => {
      this.refresh(true);
    });

    this.refreshHandler(this.refreshValue);
  },

  beforeUnmount() {
    this.destroyed = true;
  },

  props: {
    time: {}
  },

  data() {
    return {
      editTimeRaw: {},
      timepickerVisible: false,
      rangeString: '',
      refreshString: '',
      fromTooltip: '',
      toTooltip: '',
      timeOptions: rangeUtil.getRelativeTimesList(),

      fromOpen: false,
      toOpen: false,
      refreshOptions: [
        { value: 'off', label: this.$t('guan-bi') },
        { value: '10s', label: this.$t('mei-10-miao') }
        // {value: '30s', label: '每30秒'},
        // {value: '1m', label: '每分钟'},
        // {value: '5m', label: '每5分钟'}
      ],
      refreshValue: 'off',

      fromDate: '',
      toDate: '',
      destroyed: false
    };
  },

  methods: {

    handleTimepickerOpen() {
      this.timepickerVisible = !this.timepickerVisible;
    },

    handleTimepickerClose() {
      this.timepickerVisible = false;
    },

    handleFromChange() {
      this.fromOpen = false;
      const from = moment(this.fromDate);

      this.editTimeRaw.from = from.format('YYYY-MM-DD HH:mm:ss');
    },

    handleFromOk() {
      this.fromOpen = false;
      const from = moment(this.fromDate);

      this.editTimeRaw.from = from.format('YYYY-MM-DD HH:mm:ss');
    },

    handleFromOpen() {
      this.fromOpen = !this.fromOpen;
    },

    handleToChange() {
      this.toOpen = false;
      const to = moment(this.toDate);

      this.editTimeRaw.to = to.format('YYYY-MM-DD HH:mm:ss');
    },

    handleToOk() {
      this.toOpen = false;
      const to = moment(this.toDate);

      this.editTimeRaw.to = to.format('YYYY-MM-DD HH:mm:ss');
    },

    handleToOpen() {
      this.toOpen = !this.toOpen;
    },

    setRelativeFilter(option) {
      const range = { from: option.from, to: option.to };
      this.time.setTime(range);
      this.timepickerVisible = false;
      eventBus.emit('update-monitor-time', range);
      this.refresh(true);
    },

    applyCustom() {
      const range = {
        from: this.editTimeRaw.from,
        to: this.editTimeRaw.to
      };

      if (range.from.indexOf('now') === -1) {
        range.from = moment(range.from);
      }

      if (range.to.indexOf('now') === -1) {
        range.to = moment(range.to);
      }

      this.time.setTime(range);
      this.timepickerVisible = false;
      eventBus.emit('update-monitor-time', range);

      this.refresh(true);

      this.refreshHandler(this.refreshValue);
    },

    refreshHandler(refreshValue) {
      if (this.refreshTimer) {
        clearTimeout(this.refreshTimer);
      }

      if (this.destroyed) {
        return;
      }

      if (refreshValue === '' || refreshValue === 'off') {
        return;
      }

      const intervalMs = kbn.interval_to_ms(refreshValue);

      this.refreshTimer = setTimeout(() => {
        this.refresh(true);

        this.refreshHandler(refreshValue);
      }, intervalMs);
    },

    refresh(e) {
      const time = _.cloneDeep(this.time.timeRange());

      const timeRaw = _.cloneDeep(time.raw);

      this.fromDate = time.from.toDate();
      this.toDate = time.to.toDate();

      const editTimeRaw = {};

      if (moment.isMoment(timeRaw.from)) {
        editTimeRaw.from = timeRaw.from.format('YYYY-MM-DD HH:mm:ss');
      } else {
        editTimeRaw.from = timeRaw.from;
      }

      if (moment.isMoment(timeRaw.to)) {
        editTimeRaw.to = timeRaw.to.format('YYYY-MM-DD HH:mm:ss');
      } else {
        editTimeRaw.to = timeRaw.to;
      }

      this.editTimeRaw = editTimeRaw;
      this.rangeString = rangeUtil.describeTimeRange(timeRaw);
      this.fromTooltip = moment(time.from).format('YYYY-MM-DD HH:mm:ss');
      this.toTooltip = moment(time.to).format('YYYY-MM-DD HH:mm:ss');

      if (this.refreshValue !== '' && this.refreshValue !== 'off') {
        const option = _.find(this.refreshOptions, (o) => o.value == this.refreshValue);

        if (option) {
          this.refreshString = option.label;
        }
      } else {
        this.refreshString = '';
      }

      if (e) {
        this.$emit('refresh');
      }
    }

  }
};
</script>

<style lang="less">

</style>
