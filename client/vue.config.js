const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
	transpileDependencies: true,
	devServer: {
		port: 9000,
		proxy: {
			'/auth': {
				target: 'http://localhost:8080',
				changeOrigin: true,
				pathRewrite: {
					'^/auth': '/auth'
				}
			},
			'/profile': {
				target: 'http://localhost:8081',
				changeOrigin: true,
				pathRewrite: {
					'^/profile': '/profile'
				}
			},
			'/chat': {
				target: 'http://127.0.0.1:3000',
				changeOrigin: true,
				pathRewrite: {
					'^/chat': '/chat'
				}
			},
			'/message': {
				target: 'http://127.0.0.1:3000',
				changeOrigin: true,
				pathRewrite: {
					'^/message': '/message'
				}
			},
			'/post': {
				target: 'http://127.0.0.1:3001',
				changeOrigin: true,
				pathRewrite: {
					'^/post': '/post'
				}
			},
		}
	}
})