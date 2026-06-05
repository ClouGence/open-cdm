export function compareQuery(a, b) {
  if (!compareGroupBy(a.group_by, b.group_by)) {
    return false;
  }

  if (a.mode === 'expert' && b.mode === 'expert') {
    if (a.metrics.length !== b.metrics.length) {
      return false;
    }

    let idx = 0;
    let matches = true;

    if (!a.metrics.length) {
      return false;
    }

    _.forEach(a.metrics, (m) => {
      if (m.metric !== b.metrics[idx].metric) {
        matches = false;
      }

      idx++;
    });

    return matches;
  }

  if (!a.metric || !b.metric) {
    return false;
  }

  if (a.source == 'prometheus') {
    return false;
  }

  if (a.metric !== b.metric) {
    return false;
  }

  return true;
}

function compareAggregators(a, b) {
  a = a || [];
  b = b || [];
  if (a.length !== b.length) {
    return false;
  }

  let idx = 0;
  let matches = true;

  _.forEach(a, (t) => {
    if (t.align != b[idx].align) {
      matches = false;
    }

    if (t.name != b[idx].name) {
      matches = false;
    }

    if (t.sampling != b[idx].sampling) {
      matches = false;
    }

    idx++;
  });

  return matches;
}

function compareTags(a, b) {
  a = a || [];
  b = b || [];
  if (a.length !== b.length) {
    return false;
  }

  let idx = 0;
  let matches = true;

  _.forEach(a, (t) => {
    if (t.type != b[idx].type) {
      matches = false;
    }

    if (t.key != b[idx].key) {
      matches = false;
    }

    if (t.value != b[idx].value) {
      matches = false;
    }

    idx++;
  });

  return matches;
}

function compareGroupBy(a, b) {
  a = a || [];
  b = b || [];
  if (a.length !== b.length) {
    return false;
  }

  let idx = 0;
  let matches = true;

  _.forEach(a, (t) => {
    if (t.name != b[idx].name) {
      matches = false;
    }

    if (t.value != b[idx].value) {
      matches = false;
    }

    idx++;
  });

  return matches;
}
