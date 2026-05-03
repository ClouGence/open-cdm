/**
 * 用于webstorm识别alias
 */

const resolve = (dir) => require('path').join(__dirname, dir);

module.exports = {
  resolve: {
    alias: {
      '@': 'src',
      '@/components': resolve('src/components'),
      '@/store': resolve('src/store'),
      '@/views': resolve('src/views'),
      '@/styles': resolve('src/styles'),
      '@/directives': resolve('src/directives'),
      '@/services': resolve('src/services'),
      '@/asset': resolve('src/assets'),
      '@/utils': resolve('src/utils'),
      '@/filters': resolve('src/filters'),
      '@/const': resolve('src/const'),
      '@/mixins': resolve('src/mixins')
    }
  }
};
