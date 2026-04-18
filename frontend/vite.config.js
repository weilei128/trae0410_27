import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 10024,
    proxy: {
      '/api': {
        target: 'http://localhost:10023',
        changeOrigin: true
      }
    }
  }
})
