const { defineConfig } = require('@vue/cli-service')
const path = require('path')

function resolve(dir) {
  return path.join(__dirname, dir)
}

module.exports = defineConfig({
  transpileDependencies: true,
  publicPath: process.env.NODE_ENV === 'production' ? '/' : '/',
  filenameHashing: true, // 哈希

  chainWebpack(config) {
    // 设置 svg-sprite-loader
    config.module
      .rule('svg')
      .exclude.add(resolve('src/icons'))
      .end()
    config.module
      .rule('icons')
      .test(/\.svg$/)
      .include.add(resolve('src/icons'))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]'
      })
      .end()

    // 使文件名带有哈希
    config.output
      .filename('js/[name].[contenthash:8].js')
      .chunkFilename('js/[name].[contenthash:8].js')

    // 启用代码分割，拆分第三方库和应用代码
    config.optimization.splitChunks({
      chunks: 'all',
      minSize: 20000,
      maxSize: 2000000,
      cacheGroups: {
        vendor: {
          test: /[\\/]node_modules[\\/]/,
          name: 'vendor',
          chunks: 'all',
          priority: -10
        },
        // 将公共模块提取到一个独立的 chunk 中
        common: {
          name: 'common',
          chunks: 'initial',
          minChunks: 2,
          priority: -20
        }
      }
    })

    config.module
      .rule('fonts')
      .test(/\.(woff|woff2|eot|ttf|otf)$/)
      .use('file-loader')
      .loader('file-loader')
      .options({
        name: 'fonts/[name].[hash:8].[ext]',
      });

    // 配置输出的文件大小限制
    config.performance
      .maxAssetSize(5000000)  // 5MB
      .maxEntrypointSize(5000000);  // 5MB
  }
})
