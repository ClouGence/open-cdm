import moment from 'moment/moment';
import { Emitter } from '../utils/emitter';
import { DataProcessor } from './data_processor';
import { MetricsPanelCtrl } from '../sdk/ctrls';

export function getDefaults() {
  return {
    // datasource name, null = default datasource
    datasource: null,
    // sets client side (flot) or native graphite png renderer (png)
    renderer: 'flot',
    yaxes: [
      {
        label: null,
        show: true,
        logBase: 1,
        min: null,
        max: null,
        format: 'short'
      },
      {
        label: null,
        show: true,
        logBase: 1,
        min: null,
        max: null,
        format: 'short'
      }
    ],
    xaxis: {
      show: true,
      mode: 'time',
      name: null,
      values: [],
      buckets: null
    },
    yaxis: {
      align: false,
      alignLevel: null
    },
    // show/hide lines
    lines: true,
    // fill factor
    fill: 1,
    // line width in pixels
    linewidth: 1,
    // show/hide dashed line
    dashes: false,
    // length of a dash
    dashLength: 10,
    // length of space between two dashes
    spaceLength: 10,
    // show hide points
    points: false,
    // point radius in pixels
    pointradius: 5,
    // show hide bars
    bars: false,
    // enable/disable stacking
    stack: false,
    // stack percentage mode
    percentage: false,
    // legend options
    legend: {
      show: true, // disable/enable legend
      values: true, // disable/enable legend values
      min: false,
      max: false,
      current: false,
      total: false,
      avg: false
    },
    // how null points should be handled
    nullPointMode: 'null',
    // staircase line mode
    steppedLine: false,
    // tooltip options
    tooltip: {
      value_type: 'individual',
      shared: true,
      sort: 2
    },
    // time overrides
    timeFrom: null,
    timeShift: null,
    // metric queries
    targets: [{}],
    // series color overrides
    aliasColors: {},
    // other style overrides
    seriesOverrides: [],
    thresholds: []
  };
}

class Dashboard {
  constructor() {
    this.graphTooltip = 1;
  }

  sharedTooltipModeEnabled() {
    return this.graphTooltip > 0;
  }

  sharedCrosshairModeOnly() {
    return this.graphTooltip === 1;
  }

  formatDate(date, format) {
    date = moment.isMoment(date) ? date : moment(date);
    format = format || 'YYYY-MM-DD HH:mm:ss';
    const timezone = this.getTimezone();

    return timezone === 'browser' ? moment(date).format(format) : moment.utc(date).format(format);
  }

  getTimezone() {
    return 'browser';
  }
}

export default class Ctrl extends MetricsPanelCtrl {
  constructor($scope, attributes) {
    super($scope, attributes);

    this.hiddenSeries = {};
    this.seriesList = [];
    this.dataList = [];
    this.annotations = [];
    this.colors = [];

    this.events = new Emitter();
    this.timing = {};

    this.panelDefaults = getDefaults();

    this.dashboard = new Dashboard();

    _.defaults(this.panel, this.panelDefaults);
    _.defaults(this.panel.tooltip, this.panelDefaults.tooltip);
    _.defaults(this.panel.legend, this.panelDefaults.legend);
    _.defaults(this.panel.xaxis, this.panelDefaults.xaxis);

    this.processor = new DataProcessor(this.panel);

    this.events.on('render', this.onRender.bind(this));
    this.events.on('data-received', this.onDataReceived.bind(this));
    this.events.on('data-error', this.onDataError.bind(this));
    this.events.on('data-snapshot-load', this.onDataSnapshotLoad.bind(this));
  }

  onDataReceived(dataList = []) {
    this.dataList = dataList;
    this.seriesList = this.processor.getSeriesList({
      dataList,
      range: this.range
    });

    this.dataWarning = null;
    const datapointsCount = this.seriesList.reduce((prev, series) => prev + series.datapoints.length, 0);

    if (datapointsCount === 0) {
      this.dataWarning = {
        title: 'No data points',
        tip: 'No datapoints returned from data query'
      };
    } else {
      for (const series of this.seriesList) {
        if (series.isOutsideRange) {
          this.dataWarning = {
            title: 'Data points outside time range',
            tip: 'Can be caused by timezone mismatch or missing time filter in query'
          };
          break;
        }
      }
    }

    this.loading = false;
    // this.alertState = result.alertState;
    // this.annotations = result.annotations;
    this.render(this.seriesList);
  }

  onRender() {
    if (!this.seriesList) {
      return;
    }

    for (const series of this.seriesList) {
      series.applySeriesOverrides(this.panel.seriesOverrides);

      if (series.unit) {
        this.panel.yaxes[series.yaxis - 1].format = series.unit;
      }
    }
  }

  onDataError(err) {
    this.seriesList = [];
    this.annotations = [];
    this.render([]);
  }

  onDataSnapshotLoad(snapshotData) {
    this.onDataReceived(snapshotData);
  }

  toggleSeries(serie, event) {
    if (event.ctrlKey || event.metaKey || event.shiftKey) {
      if (this.hiddenSeries[serie.alias]) {
        delete this.hiddenSeries[serie.alias];
      } else {
        this.hiddenSeries[serie.alias] = true;
      }
    } else {
      this.toggleSeriesExclusiveMode(serie);
    }
    this.render();
  }

  toggleSeriesExclusiveMode(serie) {
    const hidden = this.hiddenSeries;

    if (hidden[serie.alias]) {
      delete hidden[serie.alias];
    }

    // check if every other series is hidden
    const alreadyExclusive = _.every(this.seriesList, (value) => {
      if (value.alias === serie.alias) {
        return true;
      }

      return hidden[value.alias];
    });

    if (alreadyExclusive) {
      // remove all hidden series
      _.each(this.seriesList, (value) => {
        delete this.hiddenSeries[value.alias];
      });
    } else {
      // hide all but this serie
      _.each(this.seriesList, (value) => {
        if (value.alias === serie.alias) {
          return;
        }

        this.hiddenSeries[value.alias] = true;
      });
    }
  }
}
