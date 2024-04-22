const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
	transpileDependencies: true,
	devServer: {
		port: 9000,
		proxy: {
			'/auth': {
				target: 'http://10.99.15.144:8080',
				changeOrigin: true,
				pathRewrite: {
					'^/auth': '/auth'
				}
			},
			'/chat': {
				target: 'http://127.0.0.1:3000',
				changeOrigin: true,
				pathRewrite: {
					'^/chat': '/chat'
				}
			},
		}
	}
})