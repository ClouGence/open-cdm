import dayjs from '@/utils/dayjsSetup';
import _ from '@/utils/lodash';
import * as dateMath from '../panel/utils/datemath';
import { Emitter } from '../panel/utils/emitter';

export default class Time {
  constructor() {
    this.events = new Emitter();

    this.time = {
      from: 'now-1h',
      to: 'now'
    };
  }

  setTime(time) {
    _.extend(this.time, time);
  }

  timeRange() {
    const raw = {
      from: dayjs.isDayjs(this.time.from) ? dayjs(this.time.from) : this.time.from,
      to: dayjs.isDayjs(this.time.to) ? dayjs(this.time.to) : this.time.to
    };

    const timezone = 'browser';

    return {
      from: dateMath.parse(raw.from, false, timezone),
      to: dateMath.parse(raw.to, true, timezone),
      raw
    };
  }
}
