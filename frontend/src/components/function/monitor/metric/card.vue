<template>
    <div class="card-container">
        <div class="card-header">
            <div style="float: right">
            </div>
            {{model.title}}
            <Tooltip :content="model.description" placement="right-start" style="margin-left: 5px;cursor: pointer"
                     v-if="model.description">
                <Icon type="ios-information-circle-outline" style="font-size: 14px"/>
            </Tooltip>

        </div>
        <div class="card-body" style="height: 280px;">
            <Metric ref="metric" :model="model.options" :time="time" :filters="filters" :resourceType="resourceType" :dataJobId="dataJobId" :dataTaskId="dataTaskId" :workerId="workerId" :consoleId="consoleId"
                    :filterOthers="filterOthers"></Metric>
        </div>
    </div>
</template>

<script>
import _ from 'lodash';
import Metric from './metric';
import {Tooltip} from "view-ui-plus"

export default {
  components: { Metric,Tooltip },

  mounted() {
    this.render = _.debounce(() => {
      this.$refs.metric.$emit('render');
    }, 100);

    this.emitter.on('render', () => {
      this.render();
    });
  },

  props: {
    model: {},
    time: {},
    emitter: {},
    panel: {},
    filters: {},
    filterOthers: {},
    resourceType: String,
    dataJobId: String,
    dataTaskId: String,
    workerId: String,
    consoleId: String
  },

  data() {
    return {};
  },

  methods: {
    filter() {
      this.$refs.metric.$emit('filter');
    },

    updateConfig() {
      if (this.$route.name == '/template/dashboard') {
        this.$router.push({
          name: '/template/dashboard/update',
          params: {
            id: this.$route.params.id,
            panel_id: this.$route.params.panel_id,
            metric_id: this.model.id
          }
        });
        return;
      }
      if (this.panel.service) {
        this.$router.push({
          name: 'service/dashboard/update',
          params: {
            panel_id: this.$route.params.id,
            id: this.model.id
          }
        });
      } else if (
        this.panel.serviceability_id
                    && this.panel.serviceability_id > 0
      ) {
        this.$router.push({
          name: 'metric/page/update',
          params: {
            panel_id: this.model.panel_id,
            id: this.model.id
          }
        });
      } else if (
        this.model.service != undefined
                        && this.model.service != ''
                        && this.model.page_id == 0
      ) {
        this.$router.push({
          name: 'service/dashboard/update',
          params: {
            name: this.model.service,
            id: this.model.id,
            panel_id: this.model.panel_id
          }
        });
      } else if (this.model.page_id != 0 && this.model.panel_id != 0) {
        this.$router.push({
          name: 'metric/page/update',
          params: {
            panel_id: this.model.panel_id,
            id: this.model.id
          }
        });
      }
    },

    removeConfig() {
      const onOk = () => {
        this.$axios.delete(`metric/config/${this.model.id}`).then(() => {
          this.emitter.emit('refresh');
        });
      };

      this.$modal.confirm({
        title: this.$t('ti-shi'),
        content: this.$t('ci-cao-zuo-jiang-yong-jiu-shan-chu-ci-tu-biao-shi-fou-ji-xu'),
        onOk
      });
    },

    delConfig() {
      const onOk = () => {
        this.$axios.delete(`user/panel/${this.model.id}`).then(() => {
          this.emitter.emit('refresh');
        });
      };

      this.$modal.confirm({
        title: this.$t('ti-shi'),
        content: this.$t('que-ding-yi-chu-gai-tu-biao-mo'),
        onOk
      });
    },

    favoriteConfig() {
      const favorite = {};

      favorite.config_id = this.model.id;
      this.$axios.post('user/panel', favorite).then(() => {
        this.model.favorite = true;
      });
    },

    unfavoriteConfig() {
      this.$axios.delete(`user/panel/${this.model.id}`).then(() => {
        this.model.favorite = false;
      });
    },

    handleDetail(config) {
      if (this.filterOthers.panelType === 'basic') {
        this.$router.push({
          path: `/monitor/workerDetail/${this.filterOthers
            .panelType}`,
          query: {
            metric: config.id,
            instance: this.filters.instance
          }
        });
      } else {
        this.$router.push({
          path: `/monitor/detail/${this.filterOthers
            .panelType}`,
          query: {
            metric: config.id,
            jobId: this.filters.jobId,
            taskId: this.filters.taskId
          }
        });
      }
    },

    addAlarm() {
      const query = this.model.options.queries[0];

      if (query.mode == 'expert') {
        _.forEach(query.metrics, (metric) => {
          if (metric.source != 'druid') {
            metric.source = 'tsdb';
          }
        });
      } else {
        query.mode = 'normal';
      }

      this.$router.push({
        name: 'service/alarm/create',
        query: {
          q: JSON.stringify(query)
        }
      });
    },

    goAlarm() {
      this.$router.push({
        name: 'service/alarm/update',
        params: {
          id: this.model.alarm
        }
      });
    },

    gotoService() {
      if (this.model.panel_id == 0) {
        this.$router.push({
          name: 'service/dashboard-ccsystem',
          params: {
            name: this.model.service
          }
        });
      } else {
        this.$router.push({
          name: 'service/dashboard',
          params: {
            name: this.model.service,
            id: this.model.panel_id
          }
        });
      }
    },
    gotoPage() {
      this.$router.push({
        name: 'metric/page/view',
        params: {
          id: this.model.page_id
        }
      });
    }
  }
};
</script>

<style lang="less">

</style>
