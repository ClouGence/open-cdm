import Highcharts from 'highcharts';

export function byUnit(value, unit) {
  if (typeof value === 'undefined') {
    return '';
  }

  if (unit == 'percent') {
    return percent(value);
  } if (unit == 'byte') {
    return byte(value);
  } if (unit == 'bytePerSecond') {
    return bytePerSecond(value);
  } if (unit == 'millisecond') {
    return millisecond(value);
  } if (unit == 'second') {
    return second(value);
  }
  return number(value);
}

export function percent(value) {
  return `${Highcharts.numberFormat(value, 2)}%`;
}

export function byte(value) {
  let unit = 'B';

  if (value >= 1024) {
    unit = 'KB';
    value /= 1024;
  }

  if (value >= 1024) {
    unit = 'MB';
    value /= 1024;
  }

  if (value >= 1024) {
    unit = 'GB';
    value /= 1024;
  }

  if (value >= 1024) {
    unit = 'TB';
    value /= 1024;
  }

  if (value >= 1024) {
    unit = 'PB';
    value /= 1024;
  }

  if (value.toString().indexOf('.') === -1) {
    return value + unit;
  }

  return Highcharts.numberFormat(value, 2) + unit;
}

export function bytePerSecond(value) {
  let unit = 'B/s';

  if (value >= 1024) {
    unit = 'KB/s';
    value /= 1024;
  }

  if (value >= 1024) {
    unit = 'MB/s';
    value /= 1024;
  }

  if (value >= 1024) {
    unit = 'GB/s';
    value /= 1024;
  }

  if (value >= 1024) {
    unit = 'TB/s';
    value /= 1024;
  }

  if (value >= 1024) {
    unit = 'PB/s';
    value /= 1024;
  }

  if (value.toString().indexOf('.') === -1) {
    return value + unit;
  }

  return Highcharts.numberFormat(value, 2) + unit;
}

export function millisecond(value) {
  const unit = 'ms';

  if (value >= 1000) {
    value /= 1000;
    return second(value);
  }

  if (value.toString().indexOf('.') === -1) {
    return value + unit;
  }

  return Highcharts.numberFormat(value, 2) + unit;
}

export function second(value) {
  let unit = 's';

  if (value >= 60) {
    unit = 'm';
    value /= 60;

    if (value >= 60) {
      unit = 'h';
      value /= 60;

      if (value >= 24) {
        unit = 'd';
        value /= 24;
      }
    }
  }

  if (value.toString().indexOf('.') === -1) {
    return value + unit;
  }

  return Highcharts.numberFormat(value, 2) + unit;
}

export function number(value) {
  let unit = '';

  if (value >= 1000) {
    value /= 1000;
    unit = 'k';
  }

  if (value >= 1000) {
    value /= 1000;
    unit = 'm';
  }

  if (value.toString().indexOf('.') === -1) {
    return value + unit;
  }

  return Highcharts.numberFormat(value, 2) + unit;
}
