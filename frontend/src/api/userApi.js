import axios from 'axios'
import { API_BASE_URL } from '../constants'

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000
})

apiClient.interceptors.response.use(
  response => response.data,
  error => {
    console.error('API 请求错误:', error)
    return Promise.reject(error)
  }
)

export default {
  getAllUsers() {
    return apiClient.get('')
  },

  getUserById(id) {
    return apiClient.get(`/${id}`)
  },

  createUser(user) {
    return apiClient.post('', user)
  },

  updateUser(id, user) {
    return apiClient.put(`/${id}`, user)
  },

  deleteUser(id) {
    return apiClient.delete(`/${id}`)
  }
}
