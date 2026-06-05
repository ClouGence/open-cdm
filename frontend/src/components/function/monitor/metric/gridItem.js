import React from 'react';
import { createApp } from 'vue';
import GridItem from "@/components/function/monitor/metric/GridItem.vue";
import Card from './card';
import router from '../../../../router';

export default class Panel extends React.Component {
  render() {
    return React.createElement('div', {
      ref: 'main'
    });
  }

  componentDidMount() {
    const app = createApp(GridItem, {
      model: this.props.model,
      time: this.props.time,
      emitter: this.props.emitter,
      panel: this.props.panel,
      filters: this.props.filters,
      filterOthers: this.props.filterOthers,
      resourceType: this.props.resourceType,
      dataJobId: this.props.dataJobId,
      dataTaskId: this.props.dataTaskId,
      workerId: this.props.workerId,
      consoleId: this.props.consoleId
    });
    app.use(router);
    app.mount(this.refs.main);
  }
}
