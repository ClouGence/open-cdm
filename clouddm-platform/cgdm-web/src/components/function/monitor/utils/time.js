import moment from 'moment';
import _ from 'lodash';
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
      from: moment.isMoment(this.time.from) ? moment(this.time.from) : this.time.from,
      to: moment.isMoment(this.time.to) ? moment(this.time.to) : this.time.to
    };

    const timezone = 'browser';

    return {
      from: dateMath.parse(raw.from, false, timezone),
      to: dateMath.parse(raw.to, true, timezone),
      raw
    };
  }
}
