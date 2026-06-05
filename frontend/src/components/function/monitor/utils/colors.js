const colors = [
  '#56bc76',
  '#a88108',
  '#32adfa',
  '#c596fa',
  '#70e0e0',
  '#a1e55c',
  '#f2a38d',
  '#124e93',
  '#7aa9ff',
  '#e5603b',

  '#2f7ed8', '#0d233a', '#8bbc21', '#910000', '#1aadce',
  '#492970', '#f28f43', '#77a1e5', '#c42525', '#a6c96a',

  '#4572A7', '#AA4643', '#89A54E', '#80699B', '#3D96AE',
  '#DB843D', '#92A8CD', '#A47D7C', '#B5CA92'
];

export function pick(c) {
  const m = colors.length;

  if (c > m) {
    c %= m;
  }
  return colors[c];
}
