import _ from 'lodash';
import { calculate, is } from './vql';

export function fetch($axios, metrics, start, end) {
  const requests = {};

  _.forEach(metrics, (m, i) => {
    let offset = '';

    if (m.offset) {
      offset = m.offset;
    }

    if (!requests[offset]) {
      requests[offset] = {
        offset,
        wrappers: []
      };
    }
    requests[offset].wrappers.push({
      index: i,
      metric: m
    });
  });

  const promises = [];
  const response = {
    data: {
      queries: []
    }
  };

  _.forEach(requests, (r) => {
    const metrics = [];

    _.forEach(r.wrappers, (w, i) => {
      metrics.push(w.metric);
    });

    let offset = 0;

    if (r.offset) {
      offset = parseDuration(r.offset);
    }

    promises.push($axios.post('tsdb/query', {
      start_absolute: start - offset,
      end_absolute: end - offset,
      metrics
    }).then((rr) => {
      const queries = rr.data.queries;

      _.forEach(r.wrappers, (w, i) => {
        response.data.queries[w.index] = queries[i];
      });
    }));
  });

  return new Promise((resolve, reject) => {
    Promise.all(promises).then(() => {
      resolve(response);
    });
  });
}

export function parseSampling(s) {
  if (!s) {
    return;
  }
  let unit = 'seconds';
  let value = 1;

  const u = s.substr(-1, 1);
  const v = s.substr(0, s.length - 1);

  if (u == 's') {
    unit = 'seconds';
  }
  if (u == 'm') {
    unit = 'minutes';
  }
  if (u == 'h') {
    unit = 'hours';
  }
  if (u == 'd') {
    unit = 'days';
  }

  value = parseInt(v, 10);

  if (value < 1) {
    return;
  }

  return {
    unit,
    value
  };
}

function buildMetric(name, tags, aggregators, group_by) {
  const metric = {
    name,
    filters: [],
    aggregators: [],
    group_by: []
  };

  _.forEach(tags, (t) => {
    metric.filters.push({
      type: t.type || 'wildcard',
      key: t.key,
      value: t.value
    });
  });

  _.forEach(aggregators || [], (aggregator) => {
    const a = {};

    a.name = aggregator.name;

    if (aggregator.name == 'percentile') {
      a.percentile = aggregator.percentile;
    }

    if (_.includes(['count', 'last', 'first', 'histogram', 'sum', 'avg', 'max', 'min', 'percentile'], aggregator.name)) {
      let unit = 'seconds';
      let value = 1;
      const u = aggregator.sampling.substr(-1, 1);
      const v = aggregator.sampling.substr(0, aggregator.sampling.length - 1);

      if (u == 's') {
        unit = 'seconds';
      }
      if (u == 'm') {
        unit = 'minutes';
      }
      if (u == 'h') {
        unit = 'hours';
      }
      if (u == 'd') {
        unit = 'days';
      }
      value = parseInt(v, 10);
      a.sampling = {
        unit,
        value
      };
      if (aggregator.align == 'start_time') {
        // weird, maybe an issue of kairosdb
        a.align_start_time = true;
        a.align_sampling = true;
      }

      if (aggregator.align == 'sampling') {
        a.align_sampling = true;
      }
    }

    if (aggregator.name == 'rate') {
      let unit = 'seconds';
      let value = 1;
      const u = aggregator.sampling.substr(-1, 1);
      const v = aggregator.sampling.substr(0, aggregator.sampling.length - 1);

      if (u == 's') {
        unit = 'seconds';
      }
      if (u == 'm') {
        unit = 'minutes';
      }
      if (u == 'h') {
        unit = 'hours';
      }
      if (u == 'd') {
        unit = 'days';
      }
      value = parseInt(v, 10);
      a.sampling = {
        unit,
        value
      };
    }

    metric.aggregators.push(a);
  });

  const g = {};

  _.forEach(group_by || [], (gg) => {
    if (g[gg.name]) {
      g[gg.name].push(gg.value);
    } else {
      g[gg.name] = [gg.value];
    }
  });

  _.forEach(g, (values, name) => {
    metric.group_by.push({ name, tags: values });
  });

  return metric;
}

export function build(query) {
  if (query.mode == 'expert') {
    const metrics = [];

    _.forEach(query.metrics, (m) => {
      const metric = buildMetric(m.metric, m.tags, m.aggregators, query.group_by);

      metric.offset = m.offset;
      metrics.push(metric);
    });

    return metrics;
  }

  return [buildMetric(query.metric, query.tags, query.aggregators, query.group_by)];
}

export function getTags(queries, group_by) {
  const group_by_tags = [];

  _.forEach(group_by || [], (g) => {
    group_by_tags.push(g.value);
  });
  group_by_tags.sort();

  const ts = {};

  _.forEach(queries, (q) => {
    _.forEach(q.results, (r) => {
      const tags = [];

      _.forEach(group_by_tags, (t) => {
        let tt = '';

        if (r.tags[t]) {
          tt = r.tags[t][0];
        }
        const tag = `${t}=${tt}`;

        tags.push(tag);
      });

      const key = tags.join(',');

      ts[key] = { name: r.name, tags: r.tags };
    });
  });

  return ts;
}

export function findResult(query, group_by, key) {
  if (!query) {
    return;
  }

  const group_by_tags = [];

  _.forEach(group_by || [], (g) => {
    group_by_tags.push(g.value);
  });
  group_by_tags.sort();

  let result;

  _.forEach(query.results, (r) => {
    if (result) {
      return;
    }

    const tags = [];

    _.forEach(group_by_tags, (t) => {
      let tt = '';

      if (r.tags[t]) {
        tt = r.tags[t][0];
      }
      const tag = `${t}=${tt}`;

      tags.push(tag);
    });

    const k = tags.join(',');

    if (k == key) {
      result = r;
    }
  });

  return result;
}

export function getDataByMetrics(queries, metrics, group_by, key) {
  const data = {};

  if (!queries.length) {
    return data;
  }

  let idx = 0;

  _.forEach(metrics || [], (m) => {
    const query = queries[idx];

    const result = findResult(query, group_by, key);

    if (!result) {
      return;
    }

    let offset = 0;

    if (m.offset) {
      offset = parseDuration(m.offset);
    }

    if (idx == 0) {
      _.forEach(result.values, (v) => {
        let t = parseInt(v[0], 10);

        t += offset;
        if (!data[t]) {
          data[t] = {};
        }

        data[t][m.name] = v[1];
      });
    } else {
      _.forEach(data, (vs, t) => {
        const v = findNearestValue(t, offset, result.values);

        if (typeof v !== 'undefined') {
          data[t][m.name] = v;
        }
      });
    }

    idx++;
  });

  const filtered = {};

  _.forEach(data, (v, t) => {
    if (_.keys(v).length === metrics.length) {
      filtered[t] = v;
    }
  });

  return filtered;
}

function findNearestValue(t, offset, values) {
  const max = 60000;
  let matched = false;
  let found = false;
  let value; let diff;
  const tt = parseInt(t, 10);

  _.forEach(values, (v) => {
    if (v.length !== 2) {
      return;
    }

    if (matched) {
      return;
    }

    const vt = parseInt(v[0], 10) + offset;

    if (vt == tt) {
      matched = true;
      found = true;
      value = v[1];
      return;
    }

    const d = Math.abs(vt - tt);

    if (d > max) {
      return;
    }

    if (!found) {
      diff = d;
      value = v[1];
      found = true;
      return;
    }

    if (d < diff) {
      diff = d;
      value = v[1];
    }
  });

  if (found) {
    return value;
  }
}

export function getCharts(equation, config) {
  is(equation, config);

  return config.results;
}

export function getValuesByLogic(equation, data, charts) {
  const values = [];

  _.forEach(charts, (chart, key) => {
    values[key] = [];
  });

  _.forEach(data, (d, t) => {
    t = parseInt(t, 10);
    const config = {
      data: d
    };

    is(equation, config);

    _.forEach(charts, (chart, key) => {
      const result = config.results[key];

      if (result) {
        chart.threshold = result.threshold;
        chart.expression = result.expression;
        values[key].push([t, result.value]);
      }
    });
  });

  return values;
}

export function pushResultsByLogic(charts, model, key, legend, ct, label, queries, lastQueries, options = {}) {
  const data = getDataByMetrics(queries, model.metrics, model.group_by, key);
  const values = getValuesByLogic(model.equation, data, charts);

  const lastData = getDataByMetrics(lastQueries, model.metrics, model.group_by, key);
  const lastValues = getValuesByLogic(model.equation, lastData, charts);

  _.forEach(charts, (chart, key) => {
    let l = legend;
    const m = chart.metric;

    l = l.replace(new RegExp('{{metric}}', 'g'), chart.metric);

    chart.results = ps(chart.results, model, l, ct, label, values[key], lastValues[key], options);
  });
}

export function getValuesByMetrics(equation, data) {
  const values = [];

  _.forEach(data, (d, t) => {
    t = parseInt(t, 10);
    values.push([t, calculate(equation, d)]);
  });

  return values;
}

export function pushResults(results, model, key, legend, ct, label, queries, lastQueries, options = {}) {
  if (model.mode == 'expert') {
    const data = getDataByMetrics(queries, model.metrics, model.group_by, key);
    const values = getValuesByMetrics(model.equation, data);

    const lastData = getDataByMetrics(lastQueries, model.metrics, model.group_by, key);
    const lastValues = getValuesByMetrics(model.equation, lastData);

    return ps(results, model, legend, ct, label, values, lastValues, options);
  }
  const result = findResult(queries[0], model.group_by, key);
  const lastResult = findResult(lastQueries[0], model.group_by, key);

  const values = result ? result.values : [];
  const lastValues = lastResult ? lastResult.values : [];

  return ps(results, model, legend, ct, label, values, lastValues, options);
}

function ps(results, model, legend, ct, label, values, lastValues, options = {}) {
  if (model.comparison_mode == 'compare') {
    results.push({
      data: values,
      type: options.type,
      tags: options.tags,
      name: legend
    });
    const vs = [];

    _.forEach(lastValues, (v) => {
      v[0] += ct;
      vs.push(v);
    });

    results.push({
      data: vs,
      type: options.type,
      tags: options.tags,
      name: `${legend}(${label})`
    });
  } else if (model.comparison_mode == 'absolute') {
    const vs = {};
    const vvs = [];

    _.forEach(lastValues, (v) => {
      vs[v[0] + ct] = v[1];
    });

    _.forEach(values, (v) => {
      const l = vs[v[0]] || 0;

      vvs.push([v[0], v[1] - l]);
    });

    results.push({
      data: vvs,
      type: options.type,
      tags: options.tags,
      name: `${legend}(${label}差值)`
    });
  } else if (model.comparison_mode == 'percent') {
    const vs = {};
    const vvs = [];

    _.forEach(lastValues, (v) => {
      vs[v[0] + ct] = v[1];
    });

    _.forEach(values, (v) => {
      const l = vs[v[0]] || 0;

      let div = l;

      if (div == 0) {
        div = 1;
      }

      vvs.push([v[0], (v[1] - l) / div * 100]);
    });

    results.push({
      data: vvs,
      type: options.type,
      tags: options.tags,
      name: `${legend}(${label}差值百分比)`
    });
  } else {
    results.push({
      data: values,
      type: options.type,
      tags: options.tags,
      name: legend
    });
  }

  return results;
}

export function parseDuration(d) {
  const u = d.substr(-1, 1);
  const v = d.substr(0, d.length - 1);
  let value = parseInt(v, 10);

  if (u == 's') {
    value *= 1000;
  } else if (u == 'm') {
    value = value * 1000 * 60;
  } else if (u == 'h') {
    value = value * 1000 * 60 * 60;
  } else if (u == 'd') {
    value = value * 1000 * 60 * 60 * 24;
  } else {
    return 0;
  }

  return value;
}
