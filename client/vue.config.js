const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
	transpileDependencies: true,
	devServer: {
		port: 9000,
		proxy: {
			'/auth': {
				target: 'http://127.0.0.1:8080',
				changeOrigin: true,
				pathRewrite: {
					'^/auth': '/auth'
				}
			},
		}
	}
})