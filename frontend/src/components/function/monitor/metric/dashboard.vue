<template>
    <div class='monitor-wraper'>
        <div style="padding: 15px 15px 15px 15px;margin-bottom:10px;border-bottom:1px solid rgba(0,0,0,0.3)" class="clearfix">
            <p style="float: left;font-size:18px;font-weigth:bold;margin-right:10px">{{ $t('mysql-jian-kong') }}</p>

            <Select size="small" style="width:150px;margin-top:2px;" value="basic" @on-change="handleType" v-model="filterOthers.panelType">
                <Option v-for="(val,key) in configFile" :value="key" :key="key">{{val.title}}</Option>
            </Select>
        </div>

        <div style="padding: 15px 15px 5px 15px" class="clearfix">

            <div style="float: right">

                <Timepicker ref="timepicker" :time="time"></Timepicker>

            </div>
            <div style="float: right;margin-right: 10px;line-height: 30px;">
                <Row>
                    <Col span="8">
                        <span>{{ $t('jiao-se') }}</span>
                    </Col>
                    <Col span="14">
                        <Select size="small" style="width:80px" v-model="filters.role" filterable @on-change="handleRender">
                            <Option value="all">{{ $t('quan-bu') }}</Option>
                            <Option value="master">master</Option>
                            <Option value="slave">slave</Option>
                        </Select>
                    </Col>
                </Row>
            </div>
            <div style="float: right;margin-right: 20px;line-height: 30px;">
                <Row>
                    <Col span="6">
                        <span>{{$t('shi-li-ming')}}:</span>
                    </Col>
                    <Col span="18">
                        <Select size="small" v-model="filters.instance_name" filterable @on-change="handleRender">
                            <Option value="all">{{ $t('quan-bu') }}</Option>
                            <Option v-for="(instance) in filterOthers.filterInstances" :key="instance.instance_name" :value="instance.instance_name">{{instance.instance_name}}</Option>
                        </Select>
                    </Col>
                </Row>
            </div>
            <div style="float: right;margin-right: 10px;line-height: 30px;">
                <Row>
                    <Col span="6">
                        <span>{{ $t('shu-ju-ku-0') }}</span>
                    </Col>
                    <Col span="18">
                        <Select size="small" v-model="filterOthers.database" filterable @on-change="changeDb">
                            <!-- <Option value="all">全部</Option> -->
                            <Option v-for="(item) in dbs" :key="item" :value="item">{{item}}</Option>
                        </Select>
                    </Col>
                </Row>
            </div>
        </div>

        <div ref="grid"></div>

    </div>
</template>

<script>
import _ from 'lodash';
import $ from 'jquery';
import { render } from 'react-dom';
import React from 'react';
import { EventEmitter } from 'eventemitter3';
import * as tools from '../utils/tools';
import Timepicker from './timepicker';
import Time from '../utils/time';

import Grid from './grid';
import { Config, PanelConfig } from '../config.js';
import { loadInstance } from '../../../services/api/dashboard';

export default {
  components: { Timepicker },

  created() {
    this.$Spin.show();
    this.dbs = ['db1'];
    this.dbs.unshift(this.$t('quan-bu'));
    const query = window.location.href.split('?')[1]
      ? window.location.href
        .split('?')[1]
        .split('&')[0]
        .split('=')[1]
      : 'all';
    const type = window.location.href.split('?')[1]
      ? window.location.href
        .split('?')[1]
        .split('&')[1]
        .split('=')[1]
      : 'basic';

    this.filterOthers.panelType = type;
    this.filterOthers.database = window.location.href.split('?')[1]
      ? window.location.href
        .split('?')[1]
        .split('&')[0]
        .split('=')[1]
      : this.$t('quan-bu');
    if (this.filterOthers.database != this.$t('quan-bu')) {
      this.filters.role = 'all';
    }
    this.filterOthers.filterInstances = [{ instance_name: '127.0.0.1' }];
    const dbs = [];
    const testData = { '127.0.0.1': ['db1'] };

    for (const key in testData) {
      testData[key].map((item) => {
        this.dbs.map((db) => {
          if (item == db) {
            dbs.push(db);
          }
        });
      });
      this.filterOthers.dbOnInstance[key] = dbs;
    }
    this.init();
    this.$Spin.hide();
    // loadDB("mysql", { env: this.env }).then(res => {
    //   this.dbs = res.data;
    //   this.dbs.unshift("全部");
    //   const query = window.location.href.split("?")[1]
    //     ? window.location.href
    //         .split("?")[1]
    //         .split("&")[0]
    //         .split("=")[1]
    //     : "all";
    //   const type = window.location.href.split("?")[1]
    //     ? window.location.href
    //         .split("?")[1]
    //         .split("&")[1]
    //         .split("=")[1]
    //     : "basic";
    //   this.filterOthers.panelType = type;
    //   this.filterOthers.database = window.location.href.split("?")[1]
    //     ? window.location.href
    //         .split("?")[1]
    //         .split("&")[0]
    //         .split("=")[1]
    //     : "全部";
    //   if (this.filterOthers.database != "全部") {
    //     this.filters.role = "all";
    //   }
    //   loadInstance({ env: this.env, dbname: query }).then(res => {
    //     this.filterOthers.filterInstances = res.data || [];
    //     getDbListByInstanceList({ env: this.env }).then(res => {
    //       let dbs = [];
    //       for (var key in res.data) {
    //         res.data[key].map(item => {
    //           this.dbs.map(db => {
    //             if (item == db) {
    //               dbs.push(db);
    //             }
    //           });
    //         });
    //         this.filterOthers.dbOnInstance[key] = dbs;
    //       }
    //       this.init();
    //       this.$Spin.hide();
    //     });
    //     // this.filterOthers.filterInstances.map((instance, index) => {
    //     //   getDbsByInstance({
    //     //     env: this.env,
    //     //     instance: instance.instance_name
    //     //   }).then(res => {
    //     //     let dbs = [];
    //     //     res.data.map(item => {
    //     //       this.dbs.map(db => {
    //     //         if (item == db) {
    //     //           dbs.push(db);
    //     //         }
    //     //       });
    //     //     });
    //     //     this.filterOthers.dbOnInstance[instance.instance_name] = dbs;
    //     //     if (index == this.filterOthers.filterInstances.length - 1) {
    //     //       this.init();
    //     //       this.$Spin.hide();
    //     //     }
    //     //   });
    //     // });
    //   });
    // });
  },

  mounted() {
    this.emitter = new EventEmitter();
    this.emitter.on('layout', (layout) => {
      this.panel.options = this.panel.options || {};
      this.panel.options.layout = layout;

      const model = _.cloneDeep(this.panel);

      model.options = JSON.stringify(model.options);

      // this.$axios.put('metric/panel/' + model.id, model).then(response => {
      // })
    });
    this.emitter.on('refresh', () => {
      this.updateConfigs();
    });
    this.$refs.timepicker.$on?.('refresh', () => {
      this.handleRender();
    });

    this.time.events.on('changed', () => {
      this.$refs.timepicker.$emit('to_refresh');
    });
  },

  data() {
    return {
      inited: false,
      model: {},
      panelType: 'basic',
      configFile: Config.mysql,
      servers: [],
      dcs: {},
      filters: {
        instance_name: 'all',
        role: 'master'
      },
      dbs: [],
      db: 'all',
      env: 'prod',

      time: new Time(),

      configs: [],
      panel: {},
      filterInstances: [],
      filterOthers: {
        database: '',
        filterInstances: [],
        panelType: 'basic',
        dbOnInstance: {}
      }
    };
  },

  watch: {
    $route() {
      this.init();
    }
  },

  methods: {
    init() {
      this.inited = false;

      // this.$axios.get('cmdb/servers', {
      //     params: {
      //         service: this.$route.params.name
      //     }
      // }).then(response => {
      //     this.servers = response.data

      //     _.forEach(response.data, s => {
      //         this.dcs[s.dc] = s.serverRoomId
      //     })
      // })

      // this.$axios.get('metric/panel/' + this.$route.params.id).then(response => {
      const model = PanelConfig.mysql.panel;

      if (model.options) {
        try {
          model.options = JSON.parse(model.options);
        } catch (e) {
          model.options = _.cloneDeep(model);
        }
      }

      model.options = model.options || {};
      model.options.layout = model.options.layout || [];

      this.panel = model;

      this.updateConfigs();
      // })
    },

    updateConfigs() {
      // let promises = [];

      // let p;

      // p = this.$axios.get('metric/config', {
      //     params: {
      //         panel_id: this.$route.params.id
      //     }
      // })
      // promises.push(p)

      // p = this.$axios.get('user/panel', {})
      // promises.push(p)

      // p = this.$axios.get('alarm', {
      //     params: {
      //         service: this.$route.params.name,
      //         page: 1,
      //         per: 1000
      //     }
      // })
      // promises.push(p)

      // Promise.all(promises).then(r => {

      // let configs = r[0].data
      const configs = Config.mysql[this.filterOthers.panelType].config;

      _.forEach(configs, (c) => {
        try {
          c.options = JSON.parse(c.options);
        } catch (e) {
          c.options = c.options;
        }
      });

      render(
        React.createElement(Grid, {
          emitter: this.emitter,
          time: this.time,
          panels: configs,
          width: $(this.$refs.grid).width(),
          panel: this.panel,
          filters: this.filters,
          filterOthers: this.filterOthers
        }),
        this.$refs.grid
      );

      this.configs = configs;
      this.inited = true;
      // })
    },

    handleRender() {
      if (this.emitter) {
        this.emitter.emit('render');
      }
    },
    handleType(value) {
      if (value == 'system') {
        // window.index_this.add(`/monitor/index`);
        this.$router.push({
          path: '/monitor/index'
        });
      } else {
        this.filterOthers.panelType = value;
        // this.updateConfigs();
        this.init();
        // this.handleRender();
      }
    },
    changeDb(value) {
      loadInstance({
        env: this.env,
        dbname:
          this.filterOthers.database == this.$t('quan-bu')
            ? 'all'
            : this.filterOthers.database
      }).then((res) => {
        this.filterOthers.filterInstances = res.data;
        this.filters.instance_name = 'all';
        // this.updateConfigs();
        this.handleRender();
      });
    }
  }
};
</script>

<style lang="less">

</style>
