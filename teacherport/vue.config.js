module.exports = {
  lintOnSave: false,
  devServer: {
    port: 8080,
    host: 'localhost',
    proxy: {
      '^/api': {
        target: 'http://localhost:8084',
        ws: true,
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        },
        logLevel: 'debug'
      }
    }
  }
}
