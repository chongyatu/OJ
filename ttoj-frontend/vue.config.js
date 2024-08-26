const path = require('path');
const webpack = require('webpack')
const CompressionWebpackPlugin = require('compression-webpack-plugin')
const productionGzipExtensions = ['js', 'css']
const isProduction = process.env.NODE_ENV === 'production'
// 代码压缩
const UglifyJsPlugin = require('uglifyjs-webpack-plugin')

function resolve(dir) {
    return path.join(__dirname, dir);
}

// cdn链接
const cdn = {
    // cdn：模块名称和模块作用域命名（对应window里面挂载的变量名称）
    externals: {
        vue: 'Vue',
        'vue-router': 'VueRouter',
        axios: 'axios',
        vuex: 'Vuex',
        'element-ui': 'ELEMENT',
        "mavon-editor": "MavonEditor",
    },
    // cdn的css链接
    css: [
        "https://unpkg.com/element-ui@2.15.12/lib/theme-chalk/index.css",
        "https://unpkg.com/element-ui@2.15.12/lib/theme-chalk/display.css",
        "https://unpkg.com/mavon-editor@2.10.4/dist/css/index.css"
    ],
    // cdn的js链接
    js: [
        "https://unpkg.com/vue@2.6.14/dist/vue.min.js",
        "https://unpkg.com/vue-router@3.5.3/dist/vue-router.min.js",
        "https://unpkg.com/vuex@3.6.2/dist/vuex.min.js",
        "https://unpkg.com/axios@0.21.0/dist/axios.min.js",
        "https://unpkg.com/element-ui@2.15.12/lib/index.js",
        "https://unpkg.com/mavon-editor@2.10.4/dist/mavon-editor.js",
    ]
}

module.exports = {
    publicPath: isProduction ? '/' : '/',
    productionSourceMap: false,
    devServer: {                //记住，别写错了devServer//设置本地默认端口  选填
        port: 8080,
        proxy: {                 //设置代理，必须填
            '/oj-api/oj': {              //设置拦截器  拦截器格式   斜杠+拦截器名字，名字可以自己定
                target: 'http://localhost:9991',     //代理的目标地址
                changeOrigin: true,              //是否设置同源，输入是的
                pathRewrite: {                   //路径重写
                    '/oj-api/oj': ''                     //选择忽略拦截器里面的单词
                }
            },
        }
    },
    chainWebpack: config => {
        // 开发环境
        config.when(!isProduction, config => {
            //改变入口
            config.entry('app').clear().add('./src/main-dev.js')
        })
        config.when(isProduction, config => {
            config.entry('app').clear().add('./src/main-prod.js')
            config.set('externals', cdn.externals)
            config.plugin('html').tap(args => {
                args[0].cdn = cdn
                return args
            })
        })
        config.resolve.alias.set('@', resolve('src'))
    },
    configureWebpack: (config) => {
        if (isProduction) {
            config.plugins.push(
                // 配置compression-webpack-plugin压缩
                new CompressionWebpackPlugin({
                    algorithm: 'gzip',
                    test: new RegExp('\\.(' + productionGzipExtensions.join('|') + ')$'),
                    threshold: 10240,
                    minRatio: 0.8
                })
            )
            config.plugins.push(
                new UglifyJsPlugin({
                    uglifyOptions: {
                        output: {
                            // 删除注释
                            comments: false
                        },
                        compress: {
                            drop_console: false,
                            // pure_funcs: ['console.log'] // 自定义去除函数
                        }
                    },
                    sourceMap: false
                })
            )
            config.plugins.push(
                new webpack.optimize.LimitChunkCountPlugin({
                    maxChunks: 5,
                    minChunkSize: 100
                })
            )
            config.optimization = {
                runtimeChunk: 'single',
                splitChunks: {
                    chunks: 'all',
                    maxInitialRequests: Infinity,
                    minSize: 20000,
                    cacheGroups: {
                        vendor: {
                            test: /[\\/]node_modules[\\/]/,
                            name(module) {
                                // get the name. E.g. node_modules/packageName/not/this/part.js
                                // or node_modules/packageName
                                const packageName = module.context.match(/[\\/]node_modules[\\/](.*?)([\\/]|$)/)[1]
                                // npm package names are URL-safe, but some servers don't like @ symbols
                                return `npm.${packageName.replace('@', '')}`
                            }
                        }
                    }
                }
            }
        }
    }
}
