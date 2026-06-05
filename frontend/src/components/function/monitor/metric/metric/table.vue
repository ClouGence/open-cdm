<template>
    <div>

        <div class="table-panel-container">
            <div class="table-panel-header-bg" v-show="rows.length"></div>
            <div class="table-panel-scroll" v-show="rows.length">
                <table class="table-panel-table">
                    <thead>
                    <tr>
                        <th v-for="col in columns" v-show="!col.hidden" :key="col.title">
                            <div class="table-panel-table-header-inner pointer" ng-click="ctrl.toggleColumnSort(col, $index)">
                                {{col.title}}
                                <span class="table-panel-table-header-controls" v-if="col.sort">
                <i class="fa fa-caret-down" v-show="col.desc"></i>
                <i class="fa fa-caret-up" v-show="!col.desc"></i>
              </span>
                            </div>
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="datapoints-warning" v-show="rows.length===0">
            <span class="small" >
                No data to show
            </span>
        </div>
        <div class="table-panel-footer">
        </div>

    </div>
</template>

<script>
import $ from 'jquery';
import { TablePanelCtrl } from '../../panel/table/module';
import Scope from '../../directives/scope';
import TableDirective from '../../directives/table';

export default {
  mounted() {
    this.ctrlScope = new Scope();
    this.ctrl = new TablePanelCtrl(this.ctrlScope, {
      panel: this.panel
    });

    this.tableScope = new Scope();
    this.tableScope.ctrl = this.ctrl;
    TableDirective(this.tableScope, $(this.$el));

    this.$bus?.on?.('render', (range, data) => {
      this.render(range, data);
    });
  },

  unmounted() {
    this.ctrlScope.$emit('$destroy');
    this.tableScope.$emit('$destroy');
  },

  props: {
    panel: {},
    time: {},
    emitter: {}
  },

  data() {
    return {
      columns: [],
      rows: []
    };
  },

  methods: {
    render(range, data) {
      this.ctrl.range = range;
      this.ctrl.events.emit('data-received', data);
      this.columns = this.ctrl.table.columns;
      this.rows = this.ctrl.table.rows;
    }
  }

};
</script>

<style lang="less">
</style>
