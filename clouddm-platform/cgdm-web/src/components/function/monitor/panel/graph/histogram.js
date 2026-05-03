import _ from 'lodash';

/**
 * Convert series into array of series values.
 * @param data Array of series
 */
export function getSeriesValues(dataList) {
  const VALUE_INDEX = 0;

  const values = [];

  // Count histogam stats
  for (let i = 0; i < dataList.length; i++) {
    const series = dataList[i];
    const datapoints = series.datapoints;

    for (let j = 0; j < datapoints.length; j++) {
      if (datapoints[j][VALUE_INDEX] !== null) {
        values.push(datapoints[j][VALUE_INDEX]);
      }
    }
  }

  return values;
}

/**
 * Convert array of values into timeseries-like histogram:
 * [[val_1, count_1], [val_2, count_2], ..., [val_n, count_n]]
 * @param values
 * @param bucketSize
 */
export function convertValuesToHistogram(values, bucketSize, min, max) {
  const histogram = {};

  const minBound = getBucketBound(min, bucketSize);
  const maxBound = getBucketBound(max, bucketSize);
  let bound = minBound;
  let n = 0;

  while (bound <= maxBound) {
    histogram[bound] = 0;
    bound = minBound + bucketSize * n;
    n++;
  }

  for (let i = 0; i < values.length; i++) {
    const bound = getBucketBound(values[i], bucketSize);

    histogram[bound] = histogram[bound] + 1;
  }

  const histogam_series = _.map(histogram, (count, bound) => [Number(bound), count]);

  // Sort by Y axis values
  return _.sortBy(histogam_series, (point) => point[0]);
}

/**
 * Convert series into array of histogram data.
 * @param data Array of series
 * @param bucketSize
 */
export function convertToHistogramData(
  data,
  bucketSize,
  hiddenSeries,
  min,
  max
) {
  return data.map((series) => {
    const values = getSeriesValues([series]);

    series.histogram = true;
    if (!hiddenSeries[series.alias]) {
      const histogram = convertValuesToHistogram(values, bucketSize, min, max);

      series.data = histogram;
    } else {
      series.data = [];
    }
    return series;
  });
}

function getBucketBound(value, bucketSize) {
  return Math.floor(value / bucketSize) * bucketSize;
}
