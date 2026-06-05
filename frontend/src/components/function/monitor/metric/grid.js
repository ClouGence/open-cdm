import GridLayout from 'react-grid-layout';
import React from 'react';
import _ from 'lodash';
import {
  EventEmitter
} from 'eventemitter3';
import Panel from './gridItem';

export default class Grid extends React.Component {
  render() {
    const layout = this.props.panel.options.layout || [];
    const children = [];
    const emitters = {};

    let width = 0;
    let lines = 0;
    const cols = 24;

    _.forEach(this.props.panels, (panel) => {
      const key = panel.id.toString();
      const emitter = new EventEmitter();

      emitter.on('refresh', () => {
        this.props.emitter.emit('refresh');
      });

      children.push(React.createElement('div', {
        key
      }, React.createElement(Panel, {
        model: panel,
        time: this.props.time,
        emitter,
        panel: this.props.panel,
        filters: this.props.filters,
        filterOthers: this.props.filterOthers,
        resourceType: this.props.resourceType,
        dataJobId: this.props.dataJobId,
        dataTaskId: this.props.dataTaskId,
        workerId: this.props.workerId,
        consoleId: this.props.consoleId
      })));
      const l = _.find(layout, (l) => l.i === key);

      if (!l) {
        let w = 24;

        if (panel.options.width) {
          w = panel.options.width;
        }

        layout.push({
          i: key,
          w,
          h: 20,
          x: width,
          y: Infinity
        });

        if (w + width == cols) {
          lines += 1;
          width = 0;
        } else if (w + width > cols) {
          lines += 1;
          width = w;
        } else {
          width += w;
        }
      } else if (l.w + width == cols) {
        lines += 1;
        width = 0;
      } else if (l.w + width > cols) {
        lines += 1;
        width = l.w;
      } else {
        width += l.w;
      }

      emitters[key] = emitter;
    });

    const emitter = this.props.emitter;

    emitter.on('render', () => {
      _.forEach(emitters, (e) => {
        e.emit('render');
      });
    });

    const newLayout = [{
      w: 24,
      h: 20,
      x: 0,
      y: 0,
      i: '1',
      moved: false,
      static: false
    },
    {
      w: 24,
      h: 20,
      x: 0,
      y: 20,
      i: '2',
      moved: false,
      static: false
    },
    {
      w: 24,
      h: 20,
      x: 0,
      y: 40,
      i: '3',
      moved: false,
      static: false
    },
    {
      w: 24,
      h: 20,
      x: 0,
      y: 60,
      i: '4',
      moved: false,
      static: false
    },
    {
      w: 24,
      h: 20,
      x: 0,
      y: 80,
      i: '5',
      moved: false,
      static: false
    },
    {
      w: 24,
      h: 20,
      x: 0,
      y: 100,
      i: '6',
      moved: false,
      static: false
    },
    {
      w: 24,
      h: 20,
      x: 0,
      y: 120,
      i: '7',
      moved: false,
      static: false
    },
    {
      w: 24,
      h: 20,
      x: 0,
      y: 140,
      i: '8',
      moved: false,
      static: false
    },
    {
      w: 24,
      h: 20,
      x: 0,
      y: 160,
      i: '9',
      moved: false,
      static: false
    },
    {
      w: 24,
      h: 20,
      x: 0,
      y: 180,
      i: '10',
      moved: false,
      static: false
    },
    {
      w: 24,
      h: 20,
      x: 0,
      y: 200,
      i: '11',
      moved: false,
      static: false
    }
    ];

    return React.createElement(GridLayout, {
      onResize: (layout, oldItem, newItem, placeholder, e, element) => {
        if (emitters[newItem.i]) {
          emitters[newItem.i].emit('render');
        }
      },
      onLayoutChange: (layout) => {
        if (this.props.filterOthers.panelType == 'basic') {
          emitter.emit('layout', newLayout);
        } else {
          emitter.emit('layout', layout);
        }
      },
      draggableHandle: '.card-header-test',
      layout: this.props.filterOthers.panelType == 'basic' ? newLayout : layout,
      cols,
      rowHeight: 5,
      width: this.props.width
    },
    children);
  }
}
