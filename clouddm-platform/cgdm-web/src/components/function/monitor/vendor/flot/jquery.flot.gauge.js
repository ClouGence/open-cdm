/*!
 * jquery.flot.gauge v1.1.0 *
 *
 * Flot plugin for rendering gauge charts.
 *
 * Copyright (c) 2015 @toyoty99.
 * Licensed under the MIT license.
 */

/**
 * @module flot.gauge
 */
(function ($) {
  /**
     * Gauge class
     *
     * @class Gauge
     */
  const Gauge = (function () {
    /**
         * context of canvas
         *
         * @property context
         * @type Object
         */
    let context;
    /**
         * placeholder of canvas
         *
         * @property placeholder
         * @type Object
         */
    let placeholder;
    /**
         * options of plot
         *
         * @property options
         * @type Object
         */
    let options;
    /**
         * options of gauge
         *
         * @property gaugeOptions
         * @type Object
         */
    let gaugeOptions;
    /**
         * data series
         *
         * @property series
         * @type Array
         */
    let series;
    /**
         * logger
         *
         * @property logger
         * @type Object
         */
    let logger;

    /**
         * constructor
         *
         * @class Gauge
         * @constructor
         * @param  {Object} gaugeOptions gauge options
         */
    const Gauge = function (plot, ctx) {
      context = ctx;
      placeholder = plot.getPlaceholder();
      options = plot.getOptions();
      gaugeOptions = options.series.gauges;
      series = plot.getData();
      logger = getLogger(gaugeOptions.debug);
    };

    /**
         * calculate layout
         *
         * @method calculateLayout
         * @return the calculated layout properties
         */
    Gauge.prototype.calculateLayout = function () {
      const canvasWidth = placeholder.width();
      const canvasHeight = placeholder.height();

      // calculate cell size
      const columns = Math.min(series.length, gaugeOptions.layout.columns);
      const rows = Math.ceil(series.length / columns);

      const margin = gaugeOptions.layout.margin;
      const hMargin = gaugeOptions.layout.hMargin;
      const vMargin = gaugeOptions.layout.vMargin;
      let cellWidth = (canvasWidth - margin * 2 - hMargin * (columns - 1)) / columns;
      let cellHeight = (canvasHeight - margin * 2 - vMargin * (rows - 1)) / rows;

      if (gaugeOptions.layout.square) {
        const cell = Math.min(cellWidth, cellHeight);

        cellWidth = cell;
        cellHeight = cell;
      }

      // calculate 'auto' values
      calculateAutoValues(gaugeOptions, cellWidth);

      // calculate maximum radius
      const cellMargin = gaugeOptions.cell.margin;
      let labelMargin = 0;
      let labelFontSize = 0;

      if (gaugeOptions.label.show) {
        labelMargin = gaugeOptions.label.margin;
        labelFontSize = gaugeOptions.label.font.size;
      }
      let valueMargin = 0;
      let valueFontSize = 0;

      if (gaugeOptions.value.show) {
        valueMargin = gaugeOptions.value.margin;
        valueFontSize = gaugeOptions.value.font.size;
      }
      let thresholdWidth = 0;

      if (gaugeOptions.threshold.show) {
        thresholdWidth = gaugeOptions.threshold.width;
      }
      let thresholdLabelMargin = 0;
      let thresholdLabelFontSize = 0;

      if (gaugeOptions.threshold.label.show) {
        thresholdLabelMargin = gaugeOptions.threshold.label.margin;
        thresholdLabelFontSize = gaugeOptions.threshold.label.font.size;
      }

      const maxRadiusH = cellWidth / 2 - cellMargin - thresholdWidth - thresholdLabelMargin * 2 - thresholdLabelFontSize;

      const startAngle = gaugeOptions.gauge.startAngle;
      const endAngle = gaugeOptions.gauge.endAngle;
      const dAngle = (endAngle - startAngle) / 100;
      let heightRatioV = -1;

      for (let a = startAngle; a < endAngle; a += dAngle) {
        heightRatioV = Math.max(heightRatioV, Math.sin(toRad(a)));
      }
      heightRatioV = Math.max(heightRatioV, Math.sin(toRad(endAngle)));
      let outerRadiusV = (cellHeight - cellMargin * 2 - labelMargin * 2 - labelFontSize) / (1 + heightRatioV);

      if (outerRadiusV * heightRatioV < valueMargin + valueFontSize / 2) {
        outerRadiusV = cellHeight - cellMargin * 2 - labelMargin * 2 - labelFontSize - valueMargin - valueFontSize / 2;
      }
      const maxRadiusV = outerRadiusV - thresholdLabelMargin * 2 - thresholdLabelFontSize - thresholdWidth;

      const radius = Math.min(maxRadiusH, maxRadiusV);

      let width = gaugeOptions.gauge.width;

      if (width >= radius) {
        width = Math.max(3, radius / 3);
      }

      const outerRadius = thresholdLabelMargin * 2 + thresholdLabelFontSize + thresholdWidth + radius;
      const gaugeOuterHeight = Math.max(outerRadius * (1 + heightRatioV), outerRadius + valueMargin + valueFontSize / 2);

      return {
        canvasWidth,
        canvasHeight,
        margin,
        hMargin,
        vMargin,
        columns,
        rows,
        cellWidth,
        cellHeight,
        cellMargin,
        labelMargin,
        labelFontSize,
        valueMargin,
        valueFontSize,
        width,
        radius,
        thresholdWidth,
        thresholdLabelMargin,
        thresholdLabelFontSize,
        gaugeOuterHeight
      };
    };

    /**
         * calculate the values which are set as 'auto'
         *
         * @method calculateAutoValues
         * @param  {Object} gaugeOptionsi the options of the gauge
         * @param  {Number} cellWidth the width of cell
         */
    function calculateAutoValues(gaugeOptionsi, cellWidth) {
      if (gaugeOptionsi.gauge.width === 'auto') {
        gaugeOptionsi.gauge.width = Math.max(5, cellWidth / 8);
      }
      if (gaugeOptionsi.label.margin === 'auto') {
        gaugeOptionsi.label.margin = Math.max(1, cellWidth / 20);
      }
      if (gaugeOptionsi.label.font.size === 'auto') {
        gaugeOptionsi.label.font.size = Math.max(5, cellWidth / 8);
      }
      if (gaugeOptionsi.value.margin === 'auto') {
        gaugeOptionsi.value.margin = Math.max(1, cellWidth / 30);
      }
      if (gaugeOptionsi.value.font.size === 'auto') {
        gaugeOptionsi.value.font.size = Math.max(5, cellWidth / 9);
      }
      if (gaugeOptionsi.threshold.width === 'auto') {
        gaugeOptionsi.threshold.width = Math.max(3, cellWidth / 100);
      }
      if (gaugeOptionsi.threshold.label.margin === 'auto') {
        gaugeOptionsi.threshold.label.margin = Math.max(3, cellWidth / 40);
      }
      if (gaugeOptionsi.threshold.label.font.size === 'auto') {
        gaugeOptionsi.threshold.label.font.size = Math.max(5, cellWidth / 15);
      }
    }
    Gauge.prototype.calculateAutoValues = calculateAutoValues;

    /**
         * calculate the layout of the cell inside
         *
         * @method calculateCellLayout
         * @param  {Object} gaugeOptionsi the options of the gauge
         * @param  {Number} cellWidth the width of cell
         * @param  {Number} i the index of the series
         * @return the calculated cell layout properties
         */
    Gauge.prototype.calculateCellLayout = function (gaugeOptionsi, layout, i) {
      // calculate top, left and center
      const c = col(layout.columns, i);
      const r = row(layout.columns, i);
      const x = layout.margin + (layout.cellWidth + layout.hMargin) * c;
      const y = layout.margin + (layout.cellHeight + layout.vMargin) * r;
      const cx = x + layout.cellWidth / 2;
      let cy = y + layout.cellMargin + layout.labelMargin * 2 + layout.labelFontSize + layout.thresholdWidth
                        + layout.thresholdLabelFontSize + layout.thresholdLabelMargin * 2 + layout.radius;
      const blank = layout.cellHeight - layout.cellMargin * 2 - layout.labelMargin * 2 - layout.labelFontSize - layout.gaugeOuterHeight;
      let offsetY = 0;

      if (gaugeOptionsi.cell.vAlign === 'middle') {
        offsetY = blank / 2;
      } else if (gaugeOptionsi.cell.vAlign === 'bottom') {
        offsetY = blank;
      }
      cy += offsetY;

      return {
        col: c,
        row: r,
        x,
        y,
        offsetY,
        cellWidth: layout.cellWidth,
        cellHeight: layout.cellHeight,
        cellMargin: layout.cellMargin,
        cx,
        cy
      };
    };

    /**
         * draw the background of chart
         *
         * @method drawBackground
         * @param  {Object} layout the layout properties
         */
    Gauge.prototype.drawBackground = function (layout) {
      if (!gaugeOptions.frame.show) {
        return;
      }
      context.save();
      context.strokeStyle = options.grid.borderColor;
      context.lineWidth = options.grid.borderWidth;
      context.strokeRect(0, 0, layout.canvasWidth, layout.canvasHeight);
      if (options.grid.backgroundColor) {
        context.fillStyle = options.grid.backgroundColor;
        context.fillRect(0, 0, layout.canvasWidth, layout.canvasHeight);
      }
      context.restore();
    };

    /**
         * draw the background of cell
         *
         * @method drawCellBackground
         * @param  {Object} gaugeOptionsi the options of the gauge
         * @param  {Object} cellLayout the cell layout properties
         */
    Gauge.prototype.drawCellBackground = function (gaugeOptionsi, cellLayout) {
      context.save();
      if (gaugeOptionsi.cell.border && gaugeOptionsi.cell.border.show && gaugeOptionsi.cell.border.color && gaugeOptionsi.cell.border.width) {
        context.strokeStyle = gaugeOptionsi.cell.border.color;
        context.lineWidth = gaugeOptionsi.cell.border.width;
        context.strokeRect(cellLayout.x, cellLayout.y, cellLayout.cellWidth, cellLayout.cellHeight);
      }
      if (gaugeOptionsi.cell.background && gaugeOptionsi.cell.background.color) {
        context.fillStyle = gaugeOptionsi.cell.background.color;
        context.fillRect(cellLayout.x, cellLayout.y, cellLayout.cellWidth, cellLayout.cellHeight);
      }
      context.restore();
    };

    /**
         * draw the gauge
         *
         * @method drawGauge
         * @param  {Object} gaugeOptionsi the options of the gauge
         * @param  {Object} layout the layout properties
         * @param  {Object} cellLayout the cell layout properties
         * @param  {String} label the label of data
         * @param  {Number} data the value of the gauge
         */
    Gauge.prototype.drawGauge = function (gaugeOptionsi, layout, cellLayout, label, data) {
      const blur = gaugeOptionsi.gauge.shadow.show ? gaugeOptionsi.gauge.shadow.blur : 0;

      // draw gauge frame
      drawArcWithShadow(
        cellLayout.cx, // center x
        cellLayout.cy, // center y
        layout.radius,
        layout.width,
        toRad(gaugeOptionsi.gauge.startAngle),
        toRad(gaugeOptionsi.gauge.endAngle),
        gaugeOptionsi.gauge.border.color, // line color
        gaugeOptionsi.gauge.border.width, // line width
        gaugeOptionsi.gauge.background.color, // fill color
        blur
      );

      // draw gauge
      const c1 = getColor(gaugeOptionsi, data);
      const a2 = calculateAngle(gaugeOptionsi, layout, data);

      drawArcWithShadow(
        cellLayout.cx, // center x
        cellLayout.cy, // center y
        layout.radius - 1,
        layout.width - 2,
        toRad(gaugeOptionsi.gauge.startAngle),
        toRad(a2),
        c1, // line color
        1, // line width
        c1, // fill color
        blur
      );
    };

    /**
         * decide the color of the data from the threshold options
         *
         * @method getColor
         * @private
         * @param  {Object} gaugeOptionsi the options of the gauge
         * @param  {Number} data the value of the gauge
         */
    function getColor(gaugeOptionsi, data) {
      let color;

      for (let i = 0; i < gaugeOptionsi.threshold.values.length; i++) {
        const threshold = gaugeOptionsi.threshold.values[i];

        color = threshold.color;
        if (data < threshold.value) {
          break;
        }
      }
      return color;
    }

    /**
         * calculate the angle of the data
         *
         * @method calculateAngle
         * @private
         * @param  {Object} gaugeOptionsi the options of the gauge
         * @param  {Object} layout the layout properties
         * @param  {Number} data the value of the gauge
         */
    function calculateAngle(gaugeOptionsi, layout, data) {
      let a = gaugeOptionsi.gauge.startAngle
                    + (gaugeOptionsi.gauge.endAngle - gaugeOptionsi.gauge.startAngle)
                        * ((data - gaugeOptionsi.gauge.min) / (gaugeOptionsi.gauge.max - gaugeOptionsi.gauge.min));

      if (a < gaugeOptionsi.gauge.startAngle) {
        a = gaugeOptionsi.gauge.startAngle;
      } else if (a > gaugeOptionsi.gauge.endAngle) {
        a = gaugeOptionsi.gauge.endAngle;
      }
      return a;
    }

    /**
         * draw the arc of the threshold
         *
         * @method drawThreshold
         * @param  {Object} gaugeOptionsi the options of the gauge
         * @param  {Object} layout the layout properties
         * @param  {Object} cellLayout the cell layout properties
         */
    Gauge.prototype.drawThreshold = function (gaugeOptionsi, layout, cellLayout) {
      let a1 = gaugeOptionsi.gauge.startAngle;

      for (let i = 0; i < gaugeOptionsi.threshold.values.length; i++) {
        const threshold = gaugeOptionsi.threshold.values[i];

        c1 = threshold.color;
        a2 = calculateAngle(gaugeOptionsi, layout, threshold.value);
        drawArc(
          context,
          cellLayout.cx, // center x
          cellLayout.cy, // center y
          layout.radius + layout.thresholdWidth,
          layout.thresholdWidth - 2,
          toRad(a1),
          toRad(a2),
          c1, // line color
          1, // line width
          c1
        ); // fill color
        a1 = a2;
      }
    };

    /**
         * draw an arc with a shadow
         *
         * @method drawArcWithShadow
         * @private
         * @param  {Number} cx the x position of the center
         * @param  {Number} cy the y position of the center
         * @param  {Number} r the radius of an arc
         * @param  {Number} w the width of an arc
         * @param  {Number} rd1 the start angle of an arc in radians
         * @param  {Number} rd2 the end angle of an arc in radians
         * @param  {String} lc the color of a line
         * @param  {Number} lw the widht of a line
         * @param  {String} fc the fill color  of an arc
         * @param  {Number} blur the shdow blur
         */
    function drawArcWithShadow(cx, cy, r, w, rd1, rd2, lc, lw, fc, blur) {
      if (rd1 === rd2) {
        return;
      }
      context.save();

      drawArc(context, cx, cy, r, w, rd1, rd2, lc, lw, fc);

      if (blur) {
        drawArc(context, cx, cy, r, w, rd1, rd2);
        context.clip();
        context.shadowOffsetX = 0;
        context.shadowOffsetY = 0;
        context.shadowBlur = 10;
        context.shadowColor = 'gray';
        drawArc(context, cx, cy, r + 1, w + 2, rd1, rd2, lc, 1);
      }
      context.restore();
    }

    /**
         * draw the label of the gauge
         *
         * @method drawLable
         * @param  {Object} gaugeOptionsi the options of the gauge
         * @param  {Object} layout the layout properties
         * @param  {Object} cellLayout the cell layout properties
         * @param  {Number} i the index of the series
         * @param  {Object} item the item of the series
         */
    Gauge.prototype.drawLable = function (gaugeOptionsi, layout, cellLayout, i, item) {
      drawText(
        cellLayout.cx,
        cellLayout.y + cellLayout.cellMargin + layout.labelMargin + cellLayout.offsetY,
        `flotGagueLabel${i}`,
        gaugeOptionsi.label.formatter ? gaugeOptionsi.label.formatter(item.label, item.data[0][1]) : text,
        gaugeOptionsi.label
      );
    };

    /**
         * draw the value of the gauge
         *
         * @method drawValue
         * @param  {Object} gaugeOptionsi the options of the gauge
         * @param  {Object} layout the layout properties
         * @param  {Object} cellLayout the cell layout properties
         * @param  {Number} i the index of the series
         * @param  {Object} item the item of the series
         */
    Gauge.prototype.drawValue = function (gaugeOptionsi, layout, cellLayout, i, item) {
      drawText(
        cellLayout.cx,
        cellLayout.cy - gaugeOptionsi.value.font.size / 2,
        `flotGagueValue${i}`,
        gaugeOptionsi.value.formatter ? gaugeOptionsi.value.formatter(item.label, item.data[0][1]) : text,
        gaugeOptionsi.value
      );
    };

    /**
         * draw the values of the threshold
         *
         * @method drawThresholdValues
         * @param  {Object} gaugeOptionsi the options of the gauge
         * @param  {Object} layout the layout properties
         * @param  {Object} cellLayout the cell layout properties
         * @param  {Number} i the index of the series
         */
    Gauge.prototype.drawThresholdValues = function (gaugeOptionsi, layout, cellLayout, i) {
      // min, max
      drawThresholdValue(gaugeOptionsi, layout, cellLayout, `Min${i}`, gaugeOptionsi.gauge.min, gaugeOptionsi.gauge.startAngle);
      drawThresholdValue(gaugeOptionsi, layout, cellLayout, `Max${i}`, gaugeOptionsi.gauge.max, gaugeOptionsi.gauge.endAngle);
      // threshold values
      for (let j = 0; j < gaugeOptionsi.threshold.values.length; j++) {
        const threshold = gaugeOptionsi.threshold.values[j];

        if (threshold.value > gaugeOptionsi.gauge.min && threshold.value < gaugeOptionsi.gauge.max) {
          const a = calculateAngle(gaugeOptionsi, layout, threshold.value);

          drawThresholdValue(gaugeOptionsi, layout, cellLayout, `${i}_${j}`, threshold.value, a);
        }
      }
    };

    /**
         * draw the value of the threshold
         *
         * @method drawThresholdValue
         * @param  {Object} gaugeOptionsi the options of the gauge
         * @param  {Object} layout the layout properties
         * @param  {Object} cellLayout the cell layout properties
         * @param  {Number} i the index of the series
         * @param  {Number} value the value of the threshold
         * @param  {Number} a the angle of the value drawn
         */
    function drawThresholdValue(gaugeOptionsi, layout, cellLayout, i, value, a) {
      drawText(
        cellLayout.cx
                    + (layout.thresholdLabelMargin + layout.thresholdLabelFontSize / 2 + layout.radius)
                        * Math.cos(toRad(a)),
        cellLayout.cy
                    + (layout.thresholdLabelMargin + layout.thresholdLabelFontSize / 2 + layout.radius)
                        * Math.sin(toRad(a)),
        `flotGagueThresholdValue${i}`,
        gaugeOptionsi.threshold.label.formatter ? gaugeOptionsi.threshold.label.formatter(value) : value,
        gaugeOptionsi.threshold.label,
        a
      );
    }

    /**
         * draw a text
         *
         * the textOptions is assumed as follows:
         *
         *   textOptions: {
         *       background: {
         *           color: null,
         *           opacity: 0
         *       },
         *       font: {
         *           size: "auto"
         *           family: "\"ＭＳ ゴシック\",sans-serif"
         *       },
         *       color: null
         *   }
         *
         * @method drawText
         * @private
         * @param  {Number} x the x position of the text drawn (left top)
         * @param  {Number} y the y position of the text drawn (left top)
         * @param  {String} id the id of the dom element
         * @param  {String} text the text drawn
         * @param  {Object} textOptions the option of the text
         * @param  {Number} [a] the angle of the value drawn
         */
    function drawText(x, y, id, text, textOptions, a) {
      let span = $(`.${id}`, placeholder);
      const exists = span.length;

      if (!exists) {
        span = $('<span></span>');
        span.attr('id', id);
        span.css('position', 'absolute');
        span.css('top', `${y}px`);
        if (textOptions.font.size) {
          span.css('font-size', `${textOptions.font.size}px`);
        }
        if (textOptions.font.family) {
          span.css('font-family', textOptions.font.family);
        }
        if (textOptions.color) {
          span.css('color', textOptions.color);
        }
        if (textOptions.background.color) {
          span.css('background-color', textOptions.background.color);
        }
        if (textOptions.background.opacity) {
          span.css('opacity', textOptions.background.opacity);
        }
        placeholder.append(span);
      }
      span.text(text);
      // after append, readjust the left position
      span.css('left', `${x}px`); // for redraw, resetting the left position is needed here
      span.css('left', `${parseInt(span.css('left')) - span.width() / 2}px`);

      // at last, set angle
      if (!exists && a) {
        span.css('top', `${parseInt(span.css('top')) - span.height() / 2}px`);
        span.css('transform', `rotate(${180 * a + 90}deg)`); // not supported for ie8
      }
    }

    return Gauge;
  }());
    /**
     * get a instance of Logger
     *
     * @method  getLogger
     * @for flot.gauge
     * @private
     * @param  {Object} debugOptions the options of debug
     */

  function getLogger(debugOptions) {
    return typeof Logger !== 'undefined' ? new Logger(debugOptions) : null;
  }

  /**
     * calculate the index of columns for the specified data
     *
     * @method col
     * @for flot.gauge
     * @param  {Number} columns the number of columns
     * @param  {Number} i       the index of the series
     * @return the index of columns
     */
  function col(columns, i) {
    return i % columns;
  }

  /**
     * calculate the index of rows for the specified data
     *
     * @method row
     * @for flot.gauge
     * @param  {Number} columns the number of rows
     * @param  {Number} i       the index of the series
     * @return the index of rows
     */
  function row(columns, i) {
    return Math.floor(i / columns);
  }

  /**
     * calculate the angle in radians
     *
     * internally, use a number without PI (0 - 2).
     * so, in this function, multiply PI
     *
     * @method toRad
     * @for flot.gauge
     * @param  {Number} a the number of angle without PI
     * @return the angle in radians
     */
  function toRad(a) {
    return a * Math.PI;
  }

  /**
     * draw an arc
     *
     * @method drawArc
     * @for flot.gauge
     * @param  {Object} context the context of canvas
     * @param  {Number} cx the x position of the center
     * @param  {Number} cy the y position of the center
     * @param  {Number} r the radius of an arc
     * @param  {Number} w the width of an arc
     * @param  {Number} rd1 the start angle of an arc in radians
     * @param  {Number} rd2 the end angle of an arc in radians
     * @param  {String} lc the color of a line
     * @param  {Number} lw the widht of a line
     * @param  {String} fc the fill color  of an arc
     */
  function drawArc(context, cx, cy, r, w, rd1, rd2, lc, lw, fc) {
    if (rd1 === rd2) {
      return;
    }
    const counterClockwise = false;

    context.save();
    context.beginPath();
    context.arc(cx, cy, r, rd1, rd2, counterClockwise);
    context.lineTo(cx + (r - w) * Math.cos(rd2),
      cy + (r - w) * Math.sin(rd2));
    context.arc(cx, cy, r - w, rd2, rd1, !counterClockwise);
    context.closePath();
    if (lw) {
      context.lineWidth = lw;
    }
    if (lc) {
      context.strokeStyle = lc;
      context.stroke();
    }
    if (fc) {
      context.fillStyle = fc;
      context.fill();
    }
    context.restore();
  }

  /**
     * initialize plugin
     *
     * @method init
     * @for flot.gauge
     * @private
     * @param  {Object} plot a instance of plot
     */
  function init(plot) {
    // add processOptions hook
    plot.hooks.processOptions.push((plot, options) => {
      const logger = getLogger(options.series.gauges.debug);

      // turn 'grid' and 'legend' off
      if (options.series.gauges.show) {
        options.grid.show = false;
        options.legend.show = false;
      }

      // sort threshold
      const thresholds = options.series.gauges.threshold.values;

      thresholds.sort((a, b) => {
        if (a.value < b.value) {
          return -1;
        } if (a.value > b.value) {
          return 1;
        }
        return 0;
      });
    });

    // add draw hook
    plot.hooks.draw.push((plot, context) => {
      const options = plot.getOptions();
      const gaugeOptions = options.series.gauges;

      const logger = getLogger(gaugeOptions.debug);

      if (!gaugeOptions.show) {
        return;
      }

      const series = plot.getData();

      if (!series || !series.length) {
        return; // if no series were passed
      }

      const gauge = new Gauge(plot, context);

      // calculate layout
      const layout = gauge.calculateLayout();

      // debug layout
      if (gaugeOptions.debug.layout) {

      }

      // draw background
      gauge.drawBackground(layout);

      // draw cells (label, gauge, value, threshold)
      for (let i = 0; i < series.length; i++) {
        const item = series[i];

        const gaugeOptionsi = $.extend({}, gaugeOptions, item.gauges);

        if (item.gauges) {
          // re-calculate 'auto' values
          gauge.calculateAutoValues(gaugeOptionsi, layout.cellWidth);
        }

        // calculate cell layout
        const cellLayout = gauge.calculateCellLayout(gaugeOptionsi, layout, i);

        // draw cell background
        gauge.drawCellBackground(gaugeOptionsi, cellLayout);
        // debug layout
        if (gaugeOptionsi.debug.layout) {

        }
        // draw label
        if (gaugeOptionsi.label.show) {
          gauge.drawLable(gaugeOptionsi, layout, cellLayout, i, item);
        }
        // draw gauge
        gauge.drawGauge(gaugeOptionsi, layout, cellLayout, item.label, item.data[0][1]);
        // draw threshold
        if (gaugeOptionsi.threshold.show) {
          gauge.drawThreshold(gaugeOptionsi, layout, cellLayout);
        }
        if (gaugeOptionsi.threshold.label.show) {
          gauge.drawThresholdValues(gaugeOptionsi, layout, cellLayout, i);
        }
        // draw value
        if (gaugeOptionsi.value.show) {
          gauge.drawValue(gaugeOptionsi, layout, cellLayout, i, item);
        }
      }
    });
  }

  /**
     * [defaults description]
     *
     * @property defaults
     * @type {Object}
     */
  const defaults = {
    series: {
      gauges: {
        debug: {
          log: false,
          layout: false,
          alert: false
        },
        show: false,
        layout: {
          margin: 5,
          columns: 3,
          hMargin: 5,
          vMargin: 5,
          square: false
        },
        frame: {
          show: true
        },
        cell: {
          background: {
            color: null
          },
          border: {
            show: true,
            color: 'black',
            width: 1
          },
          margin: 5,
          vAlign: 'middle' // 'top' or 'middle' or 'bottom'
        },
        gauge: {
          width: 'auto', // a specified number, or 'auto'
          startAngle: 0.9, // 0 - 2 factor of the radians
          endAngle: 2.1, // 0 - 2 factor of the radians
          min: 0,
          max: 100,
          background: {
            color: 'white'
          },
          border: {
            color: 'lightgray',
            width: 2
          },
          shadow: {
            show: true,
            blur: 5
          }
        },
        label: {
          show: true,
          margin: 'auto', // a specified number, or 'auto'
          background: {
            color: null,
            opacity: 0
          },
          font: {
            size: 'auto', // a specified number, or 'auto'
            family: 'sans-serif'
          },
          color: null,
          formatter(label, value) {
            return label;
          }
        },
        value: {
          show: true,
          margin: 'auto', // a specified number, or 'auto'
          background: {
            color: null,
            opacity: 0
          },
          font: {
            size: 'auto', // a specified number, or 'auto'
            family: 'sans-serif'
          },
          color: null,
          formatter(label, value) {
            return parseInt(value);
          }
        },
        threshold: {
          show: true,
          width: 'auto', // a specified number, or 'auto'
          label: {
            show: true,
            margin: 'auto', // a specified number, or 'auto'
            background: {
              color: null,
              opacity: 0
            },
            font: {
              size: 'auto', // a specified number, or 'auto'
              family: ',sans-serif'
            },
            color: null,
            formatter(value) {
              return value;
            }
          },
          values: [
            {
              value: 50,
              color: 'lightgreen'
            }, {
              value: 80,
              color: 'yellow'
            }, {
              value: 100,
              color: 'red'
            }
          ]
        }
      }
    }
  };

  // register the gauge plugin
  $.plot.plugins.push({
    init,
    options: defaults,
    name: 'gauge',
    version: '1.1.0'
  });
}(jQuery));
