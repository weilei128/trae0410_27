import { ref } from 'vue'
import userApi from '../api/userApi'

export function useUsers() {
  const users = ref([])
  const loading = ref(false)
  const error = ref(null)

  const loadUsers = async () => {
    loading.value = true
    error.value = null
    try {
      const response = await userApi.getAllUsers()
      users.value = response.data.data
    } catch (e) {
      error.value = e.response?.data?.message || '加载用户列表失败'
      throw e
    } finally {
      loading.value = false
    }
  }

  const createUser = async (user) => {
    const response = await userApi.createUser(user)
    await loadUsers()
    return response.data.data
  }

  const updateUser = async (id, user) => {
    const response = await userApi.updateUser(id, user)
    await loadUsers()
    return response.data.data
  }

  const deleteUser = async (id) => {
    await userApi.deleteUser(id)
    await loadUsers()
  }

  return {
    users,
    loading,
    error,
    loadUsers,
    createUser,
    updateUser,
    deleteUser
  }
}
