<template>
    <div class="graph-panel" :class="{ 'graph-panel--legend-right': rightSide }">
        <div ref="chart" class="graph-panel__chart"></div>

        <div ref="legend" class="graph-legend"></div>
    </div>
</template>

<script>
import $ from 'jquery';
import GraphCtrl from '../../panel/graph/graph';
import Scope from '../../directives/scope';
import GraphDirective from '../../directives/graph';
import LegendDirective from '../../directives/legend';

export default {
  mounted() {
    this.graphCtrlScope = new Scope();
    this.graphCtrl = new GraphCtrl(this.graphCtrlScope, {
      panel: this.panel,
      time: this.time
    });

    this.graphScope = new Scope();
    this.graphScope.ctrl = this.graphCtrl;
    GraphDirective(this.graphScope, $(this.$refs.chart));

    this.legendScope = new Scope();
    this.legendScope.ctrl = this.graphCtrl;
    LegendDirective(this.legendScope, $(this.$refs.legend));

    this.emitter.on('render', (range, data) => {
      this.render(range, data);
    });

    this.emitter.emit('ready', this.graphCtrl);
  },

  unmounted() {
    this.graphCtrlScope.$emit('$destroy');
    this.graphScope.$emit('$destroy');
    this.legendScope.$emit('$destroy');
  },

  props: {
    panel: {},
    time: {},
    emitter: {}
  },

  data() {
    return {
      rightSide: false
    };
  },

  methods: {

    render(range, data) {
      if (this.graphCtrl.panel.legend.rightSide) {
        this.rightSide = true;
      } else {
        this.rightSide = false;
      }

      setTimeout(() => {
        this.graphCtrl.range = range;
        this.graphCtrl.events.emit('data-received', data);
      }, 100);
    }
  }

};
</script>

<style lang="less">
</style>
