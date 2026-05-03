import _ from 'lodash';

class Settings {
  constructor(options) {
    const defaults = {
      datasources: {},
      window_title_prefix: 'Grafana - ',
      panels: {},
      new_panel_title: 'Panel Title',
      playlist_timespan: '1m',
      unsaved_changes_warning: true,
      appSubUrl: ''
    };

    _.extend(this, defaults, options);
  }
}

const bootData = window.grafanaBootData || { settings: {} };
const options = bootData.settings;

options.bootData = bootData;
options.bootData.user = {};

const config = new Settings(options);

export default config;
