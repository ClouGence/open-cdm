<template>
    <div class="metric-wrapper">
        <GraphMetric v-if="type === 'graph'" ref="metric" :panel="model.panel" :time="time" :emitter="emitter"></GraphMetric>

        <TableMetric v-if="type === 'table'" ref="metric" :panel="model.panel" :time="time" :emitter="emitter"></TableMetric>

        <TextMetric v-if="type === 'text'" ref="metric" :panel="model.panel" :time="time"></TextMetric>

        <SinglestatMetric v-if="type === 'singlestat'" ref="metric" :panel="model.panel" :time="time" :emitter="emitter"></SinglestatMetric>
    </div>
</template>

<script>
import _ from 'lodash';
import { EventEmitter } from 'eventemitter3';
import * as metricUtils from '../utils/metric';

import GraphMetric from './metric/graph';
import TableMetric from './metric/table';
import TextMetric from './metric/text';
import SinglestatMetric from './metric/singlestat';
import eventBus from '@/utils/eventBus';

import { services } from '@/services/http';

export default {
  name: 'Metric',
  components: {
    GraphMetric, TableMetric, TextMetric, SinglestatMetric
  },

  mounted() {
    this.emitter = new EventEmitter();
    this.emitter?.on?.('ready', (ctrl) => {
      this.ctrl = ctrl;
    });
    eventBus?.on?.('update-monitor-time', (range) => {
      this.time.time = range;
      this.render()
    })

    this.model.panel = this.model.panel || {};

    if (this.model.type === 'table') {
      this.type = 'table';
    } else if (this.model.type === 'text') {
      this.type = 'text';
    } else if (this.model.type === 'singlestat') {
      this.type = 'singlestat';
    } else {
      this.type = 'graph';
    }
    this.render();

    this.$bus?.on?.('render', () => {
      this.render();
    });

    this.$bus?.on?.('filter', () => {
      if (!this.ctrl) {f
        return;
      }

      const filterSeries = [];

      _.forEach(this.ctrl.seriesList, (l) => {
        let checked = true;

        if (this.ctrl.hiddenSeries[l.alias]) {
          checked = false;
        }

        filterSeries.push({
          icon: '\u25CF',
          color: l.color,
          name: l.alias,
          min: l.stats.min,
          min_label: l.formatValue(l.stats.min),
          max: l.stats.max,
          max_label: l.formatValue(l.stats.max),
          avg: l.stats.avg,
          avg_label: l.formatValue(l.stats.avg),
          current: l.stats.current,
          current_label: l.formatValue(l.stats.current),
          _checked: checked
        });
      });

      this.filterSeries = filterSeries;
      this.filterVisible = true;
    });
  },

  props: {
    model: {},
    time: {},
    filters: {},

    step: {},
    sample: {},

    theme: false,
    filterOthers: {},
    resourceType: String,
    dataJobId: String,
    dataTaskId: String,
    workerId: String,
    consoleId: String
  },

  data() {
    return {
      type: '',
      selected: {},

      filterVisible: false,
      filterSeries: []
    };
  },

  methods: {
    selectFilter() {
      this.ctrl.hiddenSeries = {};

      _.forEach(this.ctrl.seriesList, (l) => {
        if (!this.selected[l.alias]) {
          this.ctrl.hiddenSeries[l.alias] = true;
        }
      });

      this.ctrl.render();

      this.filterVisible = false;
    },

    filterChange(selection) {
      const selected = {};

      _.forEach(selection, (s) => {
        selected[s.name] = s.name;
      });
      this.selected = selected;
    },

    render() {
      const range = this.time.timeRange();

      this.loadData(range).then((response) => {
        const data = [];

        _.forEach(response, (results) => {
          _.forEach(results, (result) => {
            const datapoints = [];

            _.forEach(result.data, (v) => {
              datapoints.push([v[1], v[0]]);
            });

            data.push({
              target: result.name,
              datapoints
            });
          });
        });

        if (this.$refs.metric) {
          this.$refs.metric?.render(range, _.sortBy(data, ['target']));
        }
      });
    },

    loadData(range) {
      const promises = [];
      let queries = [];
      if (!this.model.queries && this.model.config) {
        queries = this.model.config[0].options.queries;
      } else {
        queries = this.model.queries;
      }

      _.forEach(queries, (query) => {
        const q = _.cloneDeep(query);

        q.resourceType = this.resourceType;
        if (q.resourceType === 'DATA_JOB') {
          q.dataJobId = this.dataJobId;
          q.dataTaskId = this.dataTaskId;
        }
        if (q.resourceType === 'CONSOLE') {
          q.consoleId = this.consoleId;
        }
        if (q.resourceType === 'WORKER') {
          q.workerId = this.workerId;
        }
        if (q.mode == 'expert') {
          _.forEach(q.metrics || [], (m) => {
            m.tags = m.tags || [];
            _.forEach(this.filters || {}, (v, k) => {
              if (v == 'all') {
                v = '';
                if (this.filterOthers.filterInstances.length > 0) {
                  this.filterOthers.filterInstances.map((item, index) => {
                    if (k != 'role') {
                      if (
                        index
                        != this.filterOthers.filterInstances.length - 1
                      ) {
                        if (k == 'instance') {
                          v += `${item.ip}|`;
                        } else {
                          v += `${item[k]}|`;
                        }
                      } else if (k == 'instance') {
                        v += item.ip;
                      } else {
                        v += item[k];
                      }
                    }
                  });
                } else {
                  m.tags.push({
                    type: 'equal',
                    key: k,
                    value: ''
                  });
                }
              }
              if (v) {
                m.tags.push({
                  type: 'regexp',
                  key: k,
                  value: v
                });
              }
            });

            m.aggregators = m.aggregators || [];

            if (this.step && this.sample && m.source != 'druid') {
              m.aggregators.push({
                name: this.sample,
                sampling: this.step,
                align: 'start_time'
              });
            }
          });
        } else {
          q.tags = q.tags || [];
          _.forEach(this.filters || {}, (v, k) => {
            if (v == 'all') {
              v = '';
              if (this.filterOthers.filterInstances.length > 0) {
                this.filterOthers.filterInstances.map((item, index) => {
                  if (k != 'role') {
                    if (index != this.filterOthers.filterInstances.length - 1) {
                      if (k == 'instance') {
                        v += `${item.ip}|`;
                      } else {
                        v += `${item[k]}|`;
                      }
                    } else if (k == 'instance') {
                      v += item.ip;
                    } else {
                      v += item[k];
                    }
                  }
                });
              } else {
                q.tags.push({
                  key: k,
                  value: ''
                });
              }
            }
            if (v) {
              q.tags.push({
                type: 'regexp',
                key: k,
                value: v
              });
            }
          });

          q.aggregators = q.aggregators || [];

          if (this.step && this.sample && q.source != 'druid') {
            q.aggregators.push({
              name: this.sample,
              sampling: this.step,
              align: 'start_time'
            });
          }
        }

        q.start = range.from.valueOf();
        q.end = range.to.valueOf();

        const ps = [];

        ps.push(services.ccMonitorQueryMetric({ data: q }));

        let ct; let
          label;

        if (query.comparison_mode && query.comparison_mode != 'none') {
          const lq = _.cloneDeep(q);

          label = query.comparison_duration;
          ct = metricUtils.parseDuration(query.comparison_duration);

          lq.start = range.from.valueOf() - ct;
          lq.end = range.to.valueOf() - ct;
          ps.push(services.ccMonitorQueryMetric({ data: lq }));
        }

        const group_by_tags = [];

        if (query.comparison_mode && query.comparison_mode != 'none') {
          _.forEach(query.group_by || [], (g) => {
            group_by_tags.push(g.value);
          });
          group_by_tags.sort();
        }

        promises.push(
          Promise.all(ps).then((responses) => {
            const results = [];
            let currentResults = [];
            let lastResults = [];

            if (responses[0].data && responses[0].data[0].values instanceof Array) {
              responses[0].data.map((data) => {
                data.values.map((item) => {
                  item[0] *= 1000;
                  item[1] = parseFloat(item[1]);
                });
              });
            }
            currentResults = responses[0].data;

            if (query.comparison_mode && query.comparison_mode != 'none') {
              if (responses[1].data[0].values instanceof Array) {
                responses[1].data.map((data) => {
                  data.values.map((item) => {
                    item[0] *= 1000;
                    item[1] = parseFloat(item[1]);
                  });
                });
              }
              lastResults = responses[1].data;

              _.forEach(lastResults, (r) => {
                const tags = [];

                _.forEach(group_by_tags, (t) => {
                  const tag = `${t}=${r.tags[t]}`;

                  tags.push(tag);
                });

                r.key = tags.join(',');
              });
            }

            _.forEach(currentResults, (result) => {
              let legend = query.legend || '{{metric}}';

              legend = legend.replace(
                new RegExp('{{metric}}', 'g'),
                result.metric
              );

              _.forEach(result.tags, (v, k) => {
                if (k == 'instance') {
                  //   getDbsByInstance({ instance: v, env: "stable" }).then(res => {
                  //     let alias = "";
                  //     if (res.data.length > 0) {
                  //       res.data.map((item, index) => {
                  //         alias += item + " ";
                  //       });
                  //       legend = legend.replace(
                  //         new RegExp("{{tags." + k + "}}", "g"),
                  //         v + "-" + alias
                  //       );
                  //     } else {
                  if (
                    this.filterOthers.database
                    && this.filterOthers.database != 'all'
                  ) {
                    legend = legend.replace(
                      new RegExp(`{{tags.${k}}}`, 'g'),
                      `${v}-${this.filterOthers.database}`
                    );
                  } else if (this.filterOthers.database == 'all') {
                    const dbOnInstance = this.filterOthers.dbOnInstance;

                    legend = legend.replace(
                      new RegExp(`{{tags.${k}}}`, 'g'),
                      `${v}-${dbOnInstance[v]}`
                    );
                  } else {
                    legend = legend.replace(
                      new RegExp(`{{tags.${k}}}`, 'g'),
                      v
                    );
                  }
                  // }
                  //   });
                } else {
                  legend = legend.replace(
                    new RegExp(`{{tags.${k}}}`, 'g'),
                    v
                  );
                }
              });

              const tags = [];

              _.forEach(group_by_tags, (t) => {
                const tag = `${t}=${result.tags[t]}`;

                tags.push(tag);
              });

              const key = tags.join(',');

              const findNearest = (values, begin, timestamp) => {
                let found = false;
                let diff = 0;
                let value = 0;
                let matches = 0;

                for (let i = begin; i < values.length; i++) {
                  let s = values[i][0] - timestamp;

                  if (s == 0) {
                    return { value: values[i], next: i + 1 };
                  }

                  if (s > 60000) {
                    break;
                  }

                  if (s < 0) {
                    s = -s;
                  }

                  if (s > 60000) {
                    continue;
                  }

                  if (!found) {
                    diff = s;
                    value = values[i][1];
                    found = true;
                    matches = i;
                    continue;
                  }

                  if (s < diff) {
                    diff = s;
                    value = values[i][1];
                    matches = i;
                  }
                }

                if (!found) {
                  return;
                }

                return { value: values[matches], next: matches + 1 };
              };

              if (query.comparison_mode == 'compare') {
                const r = _.find(lastResults, (r) => r.key === key);

                if (!r) {
                  return;
                }

                results.push({
                  type: query.type,
                  name: legend,
                  data: result.values
                });

                _.forEach(r.values, (v) => {
                  v[0] += ct;
                });

                results.push({
                  type: query.type,
                  name: `${legend} - ${label}`,
                  data: r.values
                });
              } else if (query.comparison_mode == 'absolute') {
                const r = _.find(lastResults, (r) => r.key === key);

                if (!r) {
                  return;
                }

                let begin = 0;
                const values = [];

                _.forEach(r.values, (v) => {
                  const t = v[0] + ct;

                  const o = findNearest(result.values, begin, t);

                  if (o) {
                    values.push([o.value[0], o.value[1] - v[1]]);
                    begin = o.next;
                  }
                });

                results.push({
                  type: query.type,
                  name: this.$t('legend-cha-zhi-label', [legend, label]),
                  data: values
                });
              } else if (query.comparison_mode == 'percent') {
                const r = _.find(lastResults, (r) => r.key === key);

                if (!r) {
                  return;
                }

                let begin = 0;
                const values = [];

                _.forEach(r.values, (v) => {
                  const t = v[0] + ct;

                  const o = findNearest(result.values, begin, t);

                  if (o) {
                    if (v[1] != 0) {
                      // console.log(o.value[1], v[1], (o.value[1] - v[1]) / v[1] * 100)
                      values.push([
                        o.value[0],
                        (o.value[1] - v[1]) / v[1] * 100
                      ]);
                    }
                    begin = o.next;
                  }
                });

                results.push({
                  type: query.type,
                  name: this.$t('legend-bai-fen-bi-label', [legend, label]),
                  data: values
                });
              } else {
                results.push({
                  type: query.type,
                  name: legend,
                  data: result.values
                });
              }
            });
            return results;
          })
        );
      });

      return Promise.all(promises);
    }
  }
};
</script>

<style lang="less">

</style>
