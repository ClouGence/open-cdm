import { MetricsPanelCtrl } from '../sdk/ctrls';

export function getDefaults() {
  return {
    datasource: null
  };
}

class HeatMapCtrl extends MetricsPanelCtrl {
  constructor($scope, attributes) {
    super($scope, attributes);

    _.defaults(this.panel, this.panelDefaults);

    this.events.on('data-received', this.onDataReceived.bind(this));
    this.events.on('data-error', this.onDataError.bind(this));
    this.events.on('data-snapshot-load', this.onDataReceived.bind(this));
  }

  onDataReceived(dataList) {
    this.data = dataList;
    this.render();
  }

  onDataError(err) {
    this.onDataReceived([]);
  }
}

export { HeatMapCtrl };
