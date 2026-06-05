import $ from 'jquery';
import { getColorForValue } from '../panel/singlestat/module';
import '../vendor/flot/jquery.flot';
import '../vendor/flot/jquery.flot.gauge';

import templateSrv from '../panel/sdk/template_srv';

export default function link(scope, elem) {
  const ctrl = scope.ctrl;
  // var linkSrv = this.linkSrv;
  const panel = ctrl.panel;
  let data; let linkInfo;
  const $panelContainer = elem.find('.panel-container');

  function applyColoringThresholds(value, valueString) {
    if (!panel.colorValue) {
      return valueString;
    }

    const color = getColorForValue(data, value);

    if (color) {
      return `<span style="color:${color}">${valueString}</span>`;
    }

    return valueString;
  }

  function getSpan(className, fontSize, value) {
    value = templateSrv.replace(value, data.scopedVars);
    return `<span class="${className}" style="font-size:${fontSize}">${value}</span>`;
  }

  function getBigValueHtml() {
    let body = '<div class="singlestat-panel-value-container">';

    if (panel.prefix) {
      const prefix = applyColoringThresholds(data.value, panel.prefix);

      body += getSpan('singlestat-panel-prefix', panel.prefixFontSize, prefix);
    }

    const value = applyColoringThresholds(data.value, data.valueFormatted);

    body += getSpan('singlestat-panel-value', panel.valueFontSize, value);

    if (panel.postfix) {
      const postfix = applyColoringThresholds(data.value, panel.postfix);

      body += getSpan('singlestat-panel-postfix', panel.postfixFontSize, postfix);
    }

    body += '</div>';

    return body;
  }

  function getValueText() {
    let result = panel.prefix ? templateSrv.replace(panel.prefix, data.scopedVars) : '';

    result += data.valueFormatted;
    result += panel.postfix ? templateSrv.replace(panel.postfix, data.scopedVars) : '';

    return result;
  }

  function addGauge() {
    const width = elem.width();
    const height = elem.height();
    // Allow to use a bit more space for wide gauges
    const dimension = Math.min(width, height * 1.3);

    ctrl.invalidGaugeRange = false;
    if (panel.gauge.minValue > panel.gauge.maxValue) {
      ctrl.invalidGaugeRange = true;
      return;
    }

    const plotCanvas = $('<div></div>');
    const plotCss = {
      top: '10px',
      margin: 'auto',
      position: 'relative',
      height: `${height * 0.9}px`,
      width: `${dimension}px`
    };

    plotCanvas.css(plotCss);

    const thresholds = [];

    for (let i = 0; i < data.thresholds.length; i++) {
      thresholds.push({
        value: data.thresholds[i],
        color: data.colorMap[i]
      });
    }
    thresholds.push({
      value: panel.gauge.maxValue,
      color: data.colorMap[data.colorMap.length - 1]
    });

    const bgColor = config.bootData.user.lightTheme ? 'rgb(230,230,230)' : 'rgb(38,38,38)';

    const fontScale = parseInt(panel.valueFontSize) / 100;
    const fontSize = Math.min(dimension / 5, 100) * fontScale;
    // Reduce gauge width if threshold labels enabled
    const gaugeWidthReduceRatio = panel.gauge.thresholdLabels ? 1.5 : 1;
    const gaugeWidth = Math.min(dimension / 6, 60) / gaugeWidthReduceRatio;
    const thresholdMarkersWidth = gaugeWidth / 5;
    const thresholdLabelFontSize = fontSize / 2.5;

    const options = {
      series: {
        gauges: {
          gauge: {
            min: panel.gauge.minValue,
            max: panel.gauge.maxValue,
            background: { color: bgColor },
            border: { color: null },
            shadow: { show: false },
            width: gaugeWidth
          },
          frame: { show: false },
          label: { show: false },
          layout: { margin: 0, thresholdWidth: 0 },
          cell: { border: { width: 0 } },
          threshold: {
            values: thresholds,
            label: {
              show: panel.gauge.thresholdLabels,
              margin: thresholdMarkersWidth + 1,
              font: { size: thresholdLabelFontSize }
            },
            show: panel.gauge.thresholdMarkers,
            width: thresholdMarkersWidth
          },
          value: {
            color: panel.colorValue ? getColorForValue(data, data.valueRounded) : null,
            formatter() {
              return getValueText();
            },
            font: {
              size: fontSize,
              family: '"Helvetica Neue", Helvetica, Arial, sans-serif'
            }
          },
          show: true
        }
      }
    };

    elem.append(plotCanvas);

    const plotSeries = {
      data: [[0, data.valueRounded]]
    };

    $.plot(plotCanvas, [plotSeries], options);
  }

  function addSparkline() {
    const width = elem.width() + 20;

    if (width < 30) {
      // element has not gotten it's width yet
      // delay sparkline render
      setTimeout(addSparkline, 30);
      return;
    }

    const height = ctrl.height;
    const plotCanvas = $('<div></div>');
    const plotCss = {};

    plotCss.position = 'absolute';

    if (panel.sparkline.full) {
      plotCss.bottom = '5px';
      plotCss.left = '-5px';
      plotCss.width = `${width - 10}px`;
      const dynamicHeightMargin = height <= 100 ? 5 : Math.round(height / 100) * 15 + 5;

      plotCss.height = `${height - dynamicHeightMargin}px`;
    } else {
      plotCss.bottom = '0px';
      plotCss.left = '-5px';
      plotCss.width = `${width - 10}px`;
      plotCss.height = `${Math.floor(height * 0.25)}px`;
    }

    plotCanvas.css(plotCss);

    const options = {
      legend: { show: false },
      series: {
        lines: {
          show: true,
          fill: 1,
          lineWidth: 1,
          fillColor: panel.sparkline.fillColor
        }
      },
      yaxes: { show: false },
      xaxis: {
        show: false,
        mode: 'time',
        min: ctrl.range.from.valueOf(),
        max: ctrl.range.to.valueOf()
      },
      grid: { hoverable: false, show: false }
    };

    elem.append(plotCanvas);

    const plotSeries = {
      data: data.flotpairs,
      color: panel.sparkline.lineColor
    };

    $.plot(plotCanvas, [plotSeries], options);
  }

  function render() {
    if (!ctrl.data) {
      return;
    }
    data = ctrl.data;

    // get thresholds
    data.thresholds = panel.thresholds.split(',').map((strVale) => Number(strVale.trim()));
    data.colorMap = panel.colors;

    const body = panel.gauge.show ? '' : getBigValueHtml();

    if (panel.colorBackground) {
      const color = getColorForValue(data, data.value);

      if (color) {
        $panelContainer.css('background-color', color);
        if (scope.fullscreen) {
          elem.css('background-color', color);
        } else {
          elem.css('background-color', '');
        }
      }
    } else {
      $panelContainer.css('background-color', '');
      elem.css('background-color', '');
    }

    elem.html(body);

    if (panel.sparkline.show) {
      addSparkline();
    }

    if (panel.gauge.show) {
      addGauge();
    }

    elem.toggleClass('pointer', panel.links.length > 0);

    if (panel.links.length > 0) {
      // linkInfo = linkSrv.getPanelLinkAnchorInfo(panel.links[0], data.scopedVars);
    } else {
      linkInfo = null;
    }
  }

  function hookupDrilldownLinkTooltip() {
    // drilldown link tooltip
    const drilldownTooltip = $('<div id="tooltip" class="">hello</div>"');

    elem.mouseleave(() => {
      if (panel.links.length === 0) {
        return;
      }
      // $timeout(function() {
      drilldownTooltip.detach();
      // });
    });

    elem.click((evt) => {
      if (!linkInfo) {
        return;
      }
      // ignore title clicks in title
      if ($(evt).parents('.panel-header').length > 0) {
        return;
      }

      if (linkInfo.target === '_blank') {
        window.open(linkInfo.href, '_blank');
        return;
      }

      if (linkInfo.href.indexOf('http') === 0) {
        window.location.href = linkInfo.href;
      } else {
        // $timeout(function() {
        // $location.url(linkInfo.href);
        // });
      }

      drilldownTooltip.detach();
    });

    elem.mousemove((e) => {
      if (!linkInfo) {
        return;
      }

      drilldownTooltip.text(`click to go to: ${linkInfo.title}`);
      drilldownTooltip.place_tt(e.pageX, e.pageY - 50);
    });
  }

  hookupDrilldownLinkTooltip();

  ctrl.events.on('render', () => {
    render();
    ctrl.renderingCompleted();
  });
}
