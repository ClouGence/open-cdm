const prodPlugins = [];
if (process.env.NODE_ENV !== 'development') {
  prodPlugins.push(['transform-remove-console', { exclude: ['warn', 'error'] }]);
}

module.exports = {
  presets: ['@vue/cli-plugin-babel/preset'],
  plugins: [
    // ['import', {
    //   libraryName: 'view-ui-plus',
    //   libraryDirectory: 'src/components'
    // }],
    '@vue/babel-plugin-jsx',
    ...prodPlugins
  ]
};
