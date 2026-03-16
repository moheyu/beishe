import { defineConfig } from 'vite'
// 修正这里：改成默认导入，名字随便起，比如叫 vue
import vue from '@vitejs/plugin-vue2'
import path from 'path'

export default defineConfig({
  // 修正这里：直接调用 vue()
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    }
  },
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false
      }
    }
  }
})