<template>
  <div id="app">
    <div class="container">
      <h1>用户信息管理</h1>

      <div v-if="notification.show" :class="['notification', notification.type]">
        {{ notification.message }}
      </div>

      <UserTable
        :users="users"
        :loading="loading"
        @add="showAddModal"
        @edit="showEditModal"
        @delete="deleteUser"
      />

      <UserForm
        :visible="showModal"
        :user="editingUser"
        @close="closeModal"
        @submit="submitForm"
      />
    </div>
  </div>
</template>

<script>
import UserTable from './components/UserTable.vue'
import UserForm from './components/UserForm.vue'
import userApi from './api/userApi'
import { MESSAGES } from './constants'

export default {
  name: 'App',
  components: {
    UserTable,
    UserForm
  },
  data() {
    return {
      users: [],
      loading: false,
      showModal: false,
      editingUser: null,
      notification: {
        show: false,
        message: '',
        type: 'success'
      }
    }
  },
  mounted() {
    this.loadUsers()
  },
  methods: {
    showNotification(message, type = 'success') {
      this.notification = {
        show: true,
        message,
        type
      }
      setTimeout(() => {
        this.notification.show = false
      }, 3000)
    },

    async loadUsers() {
      this.loading = true
      try {
        const result = await userApi.getAllUsers()
        this.users = result.data || []
      } catch (error) {
        console.error(MESSAGES.LOAD_USERS_ERROR, error)
        this.showNotification(MESSAGES.LOAD_USERS_ERROR, 'error')
      } finally {
        this.loading = false
      }
    },

    showAddModal() {
      this.editingUser = null
      this.showModal = true
    },

    showEditModal(user) {
      this.editingUser = { ...user }
      this.showModal = true
    },

    closeModal() {
      this.showModal = false
      this.editingUser = null
    },

    async submitForm(userData) {
      try {
        if (this.editingUser) {
          await userApi.updateUser(userData.id, userData)
          this.showNotification(MESSAGES.UPDATE_USER_SUCCESS)
        } else {
          await userApi.createUser(userData)
          this.showNotification(MESSAGES.CREATE_USER_SUCCESS)
        }
        await this.loadUsers()
      } catch (error) {
        const errorMessage = this.editingUser ? MESSAGES.UPDATE_USER_ERROR : MESSAGES.CREATE_USER_ERROR
        console.error(errorMessage, error)
        this.showNotification(errorMessage, 'error')
        throw error
      }
    },

    async deleteUser(id) {
      if (!confirm(MESSAGES.DELETE_CONFIRM)) {
        return
      }
      try {
        await userApi.deleteUser(id)
        this.showNotification(MESSAGES.DELETE_USER_SUCCESS)
        await this.loadUsers()
      } catch (error) {
        console.error(MESSAGES.DELETE_USER_ERROR, error)
        this.showNotification(MESSAGES.DELETE_USER_ERROR, 'error')
      }
    }
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, sans-serif;
  background-color: #f5f5f5;
}

.container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

h1 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.notification {
  position: fixed;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  padding: 12px 24px;
  border-radius: 4px;
  color: white;
  font-weight: 500;
  z-index: 2000;
  animation: slideDown 0.3s ease;
}

.notification.success {
  background-color: #67c23a;
}

.notification.error {
  background-color: #f56c6c;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateX(-50%) translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateX(-50%) translateY(0);
  }
}
</style>
