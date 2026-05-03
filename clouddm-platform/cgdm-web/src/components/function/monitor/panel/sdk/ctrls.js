import _ from 'lodash';
import { Emitter } from '../utils/emitter';

export class PanelCtrl {
  constructor($scope, attributes) {
    _.forEach(attributes, (value, key) => {
      this[key] = value;
    });

    this.events = new Emitter();
    this.timing = {};

    $scope.$on('$destroy', () => {
      this.events.emit('panel-teardown');
      this.events.removeAllListeners();
    });
  }

  render(payload) {
    this.timing.renderStart = new Date().getTime();
    this.events.emit('render', payload);
  }

  renderingCompleted() {

  }
}

export class MetricsPanelCtrl extends PanelCtrl {
  constructor($scope, attributes) {
    super($scope, attributes);

    this.scope = $scope;
    this.panel.datasource = this.panel.datasource || null;

    if (!this.panel.targets) {
      this.panel.targets = [{}];
    }
  }
}
