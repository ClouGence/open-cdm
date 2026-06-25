const path = require('path');
const webpack = require('webpack');
const MonacoWebpackPlugin = require('monaco-editor-webpack-plugin');
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;
const { codeInspectorPlugin } = require('code-inspector-plugin');

const LOCAL_HOST_DM = 'http://localhost:8222';
// const LOCAL_HOST_DM = 'http://192.168.0.168:8222';
// const LOCAL_HOST = 'http://192.168.10.118:8222';
// const LOCAL_HOST = 'http://192.168.0.183:8222';
// const LOCAL_HOST = 'http://192.168.0.158:33228';
// const LOCAL_HOST = 'http://192.168.0.140:33228';
let HOST = LOCAL_HOST_DM;
let indexHtml = 'index.rdp.html';

const PRODUCT = (process.env.VUE_PRODUCT || process.env.VUE_APP_PRODUCT || 'DM').toUpperCase();
const APP_LOCALE = process.env.VUE_APP_I18N_LOCALE;
const isDevelopment = process.env.NODE_ENV === 'development';

console.log(PRODUCT, HOST, indexHtml);

// Take out the host so that it can be later injected into the world.
const getHostFromUrl = (url) => {
  try {
    const urlObj = new URL(url);
    return urlObj.host;
  } catch (e) {
    // If not complete URL, try to extract it directly
    return url.replace(/^https?:\/\//, '');
  }
};

const DM_HOST = getHostFromUrl(LOCAL_HOST_DM);

// const isDev = process.env.NODE_ENV === 'dev' || 'development';
// const isProd = process.env.NODE_ENV === 'prod' || 'production';

const resolve = (dir) => path.join(__dirname, dir);

module.exports = {
  pages: {
    index: {
      entry: `src/main.js`,
      template: `public/${indexHtml}`,
      filename: 'index.html'
    }
  },
  productionSourceMap: false,
  outputDir: 'dist/templates',
  css: {
    loaderOptions: {
      postcss: {
        postcssOptions: {
          // Modify to postcssOptions
          plugins: [require('tailwindcss'), require('autoprefixer')]
        }
      }
    }
  },
  devServer: {
    open: true,
    compress: true,
    client: {
      overlay: false
    },
    allowedHosts: 'all', // Replace Disable HostCheck
    proxy: {
      '/cloudcanal': {
        target: HOST
      },
      '/clouddm': {
        target: HOST
      },
      '/api': {
        target: HOST,
        changeOrigin: true,
        ws: true,
        pathRewrite: {
          '^/api': ''
        }
      },
      '/login': {
        target: HOST
      },
      '/logout': {
        target: HOST
      },
      '/globalSettings': {
        target: HOST
      },
      '/signin': {
        target: HOST
      },
      '/loginMfaValid': {
        target: HOST
      },
      '/list_org': {
        target: HOST
      },
      '/checkSupplement': {
        target: HOST
      },
      '/auth': {
        target: HOST
      },
      '/requestJumpUrl': {
        target: HOST
      }
    }
    // proxy: {
    //   '/api': {
    //     target: 'http://clouddm.clougence.com/',
    //     changeOrigin: true,
    //     ws: true,
    //     pathRewrite: {
    //       '^/api': ''
    //     }
    //   },
    //   '/login': {
    //     target: 'http://clouddm.clougence.com/'
    //   },
    //   '/register': {
    //     target: 'http://clouddm.clougence.com/'
    //   },
    //   '/logout': {
    //     target: 'http://clouddm.clougence.com/'
    //   }
    // }
  },
  chainWebpack: (config) => {
    config.resolve.symlinks(true);
    config.resolve.extensions.add('vue');
    config.resolve.alias
      .set('@', resolve('src'))
      .set('@/components', resolve('src/components'))
      .set('@/views', resolve('src/views'))
      .set('@/styles', resolve('src/styles'))
      .set('@/directives', resolve('src/directives'))
      .set('@/services', resolve('src/services'))
      .set('@/assets', resolve('src/assets'))
      .set('@/utils', resolve('src/utils'))
      .set('@/filters', resolve('src/filters'))
      .set('@/mixins', resolve('src/mixins'))
      .set('@/const', resolve('src/const'))
      .set('@/layout', resolve('src/layout'))
      .set('@/i18n', resolve('src/i18n'));

    if (isDevelopment) {
      config.plugin('code-inspector-plugin').use(
        codeInspectorPlugin({
          bundler: 'webpack'
        })
      );
    }
  },
  pluginOptions: {
    'style-resources-loader': {
      preProcessor: 'less',
      patterns: [path.resolve(__dirname, './src/styles/global.less')]
    }
  },
  configureWebpack: {
    optimization: {
      splitChunks: {
        chunks: 'all',
        cacheGroups: {
          // vxe-table related library (non-CC product only)
          ...(PRODUCT === 'CC'
            ? {
                // Monaco Editor
                monaco: {
                  name: 'chunk-monaco',
                  test: /[\\/]node_modules[\\/](monaco-editor)[\\/]/,
                  priority: 10,
                  reuseExistingChunk: true,
                  enforce: true
                }
              }
            : {})
        }
      }
    },
    plugins: [
      new MonacoWebpackPlugin({
        languages: ['mysql', 'sql', 'redis', 'pgsql', 'json']
      }),
      new webpack.ProvidePlugin({
        $: 'jquery',
        jQuery: 'jquery',
        'windows.jQuery': 'jquery'
      }),
      new webpack.DefinePlugin({
        'process.env.VUE_APP_DM_HOST': JSON.stringify(DM_HOST)
      })
    ],
    module: {
      rules: [
        {
          test: /monaco-editor(\/|\\).*\.js/,
          loader: 'babel-loader'
        }
      ]
    }
  },
  transpileDependencies: ['react-draggable', 'react-resizable', 'react-grid-layout']
};
