import i18n from '../i18n';

export function formatSeconds(value) {
  let theTime = parseInt(value, 10); // 秒
  let theTime1 = 0; // 分
  let theTime2 = 0; // 小时
  let theTime3 = 0; // 天

  if (theTime > 60) {
    theTime1 = parseInt(theTime / 60, 10);
    theTime = parseInt(theTime % 60, 10);
    if (theTime1 > 60) {
      theTime2 = parseInt(theTime1 / 60, 10);
      theTime1 = parseInt(theTime1 % 60, 10);
    }
    if (theTime2 > 24) {
      theTime3 = parseInt(theTime2 / 24, 10);
      theTime2 = parseInt(theTime2 % 24, 10);
    }
  }

  let result;
  if (theTime < 10 > 0) {
    result = i18n.global.t('0parseintthetime-10-miao', [parseInt(theTime, 10)]); // 秒
  } else {
    result = i18n.global.t('stringparseintthetime-10-miao', [String(parseInt(theTime, 10))]); // 秒
  }

  if (theTime1 < 10 > 0) {
    result = i18n.global.t('0parseintthetime1-10-fen-result', [parseInt(theTime1, 10), result]); // 分，不足两位数，首位补充0，
  } else {
    result = i18n.global.t('stringparseintthetime1-10-fen-result', [String(parseInt(theTime1, 10)), result]); // 分
  }
  if (theTime2 > 0) {
    result = i18n.global.t('stringparseintthetime2-10-xiao-shi-result', [String(parseInt(theTime2, 10)), result]); // 时
  }
  if (theTime3 > 0) {
    result = i18n.global.t('stringparseintthetime3-10-tian-result', [String(parseInt(theTime3, 10)), result]); // 天
  }

  return result;
}

export function formatCount(value) {
  let value1 = parseInt(value, 10);
  let value2 = 0; // 万
  let value3 = 0; // 亿

  let result = parseInt(value1, 10);

  if (value1 > 10000) {
    value2 = parseInt(value1 / 10000, 10);
    value1 = parseInt(value1 % 10000, 10);
    if (value2 > 10000) {
      value3 = parseInt(value2 / 10000, 10);
      value2 = parseInt(value2 % 10000, 10);
    }
  }
  if (value2 > 0) {
    result = i18n.global.t('stringparseintvalue2-10-wan-stringparseintvalue1-10-0-stringparseintvalue1-10', [
      String(parseInt(value2, 10)),
      String(parseInt(value1, 10)) > 0 ? String(parseInt(value1, 10)) : ''
    ]);
  }
  if (value3 > 0) {
    result = `${String(parseInt(value3, 10))}${i18n.global.t('yi-0')}${
      String(parseInt(value2, 10)) > 0 ? `${String(parseInt(value2, 10))}${i18n.global.t('wan')}` : ''
    }${String(parseInt(value1, 10)) > 0 ? String(parseInt(value1, 10)) : ''}`; //
  }
  return result;
}

export function formatEnCount(value) {
  let value1 = parseInt(value, 10);
  let value2 = 0; // million

  let result = parseInt(value1, 10);

  if (value1 > 1000000) {
    value2 = parseInt(value1 / 1000000, 10);
    value1 = parseInt(value1 % 1000000, 10);
  }
  if (value2 > 0) {
    result = i18n.global.t('stringparseintvalue2-10-million-stringparseintvalue1-10-0-stringparseintvalue1-10', [
      String(parseInt(value2, 10)),
      String(parseInt(value1, 10)) > 0 ? String(parseInt(value1, 10)) : ''
    ]);
  }
  return result;
}

export function formatHour(value) {
  let theTime = parseInt(value, 10); // 小时
  let theTime3 = 0; // 天

  if (theTime > 24) {
    theTime3 = parseInt(theTime / 24, 10);
    theTime = parseInt(theTime % 24, 10);
  }

  let result = ''; // 小时

  if (theTime > 0) {
    result = i18n.global.t('stringparseintthetime2-10-xiao-shi-result', [String(parseInt(theTime, 10)), result]); // 时
  }
  if (theTime3 > 0) {
    if (theTime3 > 100) {
      result = i18n.global.t('paramsrowamount-24-tian', [String(parseInt(theTime3, 10))]); // 天
    } else {
      result = i18n.global.t('stringparseintthetime3-10-tian-result', [String(parseInt(theTime3, 10)), result]); // 天
    }
  }

  return result;
}

export function parseCron(date) {
  let cron = '';

  if (date.dayType === 'DAY') {
    cron += `0 ${parseInt(date.time.split(':')[1], 10)} ${parseInt(date.time.split(':')[0], 10)} * * ? *`;
    return cron;
  }
  if (date.dayType === 'MONTH') {
    cron += `0 ${parseInt(date.time.split(':')[1], 10)} ${parseInt(date.time.split(':')[0], 10)} ${date.date} * ? *`;
    return cron;
  }
  if (date.dayType === 'YEAR') {
    cron += `0 ${parseInt(date.time.split(':')[1], 10)} ${parseInt(date.time.split(':')[0], 10)} ${date.date} ${date.month} ? *`;
    return cron;
  }
  if (date.dayType === 'HOUR') {
    cron += `0 ${date.hour} * * * ? *`;
    return cron;
  }
  if (date.dayType === 'WEEK') {
    cron += `0 ${parseInt(date.time.split(':')[1], 10)} ${parseInt(date.time.split(':')[0], 10)} ? * ${date.week}`;
    return cron;
  }
}

export function isNumber(data) {
  const num = Number(data);
  return Number.isFinite(num) && !Number.isNaN(num);
}
