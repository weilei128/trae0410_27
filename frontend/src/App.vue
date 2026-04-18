<template>
  <div id="app">
    <div class="container">
      <h1>用户信息管理</h1>
      
      <Toolbar @add="handleAdd" />
      
      <UserTable 
        :users="users" 
        @edit="handleEdit" 
        @delete="handleDelete" 
      />
      
      <UserModal
        v-if="showModal"
        :visible="showModal"
        :is-edit="isEdit"
        :user-data="formData"
        @close="closeModal"
        @submit="handleSubmit"
      />
    </div>
  </div>
</template>

<script>
import Toolbar from './components/Toolbar.vue'
import UserTable from './components/UserTable.vue'
import UserModal from './components/UserModal.vue'
import { userApi } from './api/userApi'
import { MESSAGES } from './constants'

export default {
  name: 'App',
  components: {
    Toolbar,
    UserTable,
    UserModal
  },
  data() {
    return {
      users: [],
      showModal: false,
      isEdit: false,
      formData: {
        id: null,
        name: '',
        age: null,
        email: ''
      }
    }
  },
  mounted() {
    this.loadUsers()
  },
  methods: {
    async loadUsers() {
      try {
        const res = await userApi.getAllUsers()
        this.users = res.data || []
      } catch (error) {
        console.error(MESSAGES.LOAD_ERROR, error)
      }
    },
    
    handleAdd() {
      this.isEdit = false
      this.formData = { id: null, name: '', age: null, email: '' }
      this.showModal = true
    },
    
    handleEdit(user) {
      this.isEdit = true
      this.formData = { ...user }
      this.showModal = true
    },
    
    closeModal() {
      this.showModal = false
    },
    
    async handleSubmit(userData) {
      try {
        if (this.isEdit) {
          await userApi.updateUser(userData.id, userData)
        } else {
          await userApi.createUser(userData)
        }
        this.closeModal()
        this.loadUsers()
      } catch (error) {
        console.error(MESSAGES.SAVE_ERROR, error)
        alert(error.message || MESSAGES.SAVE_ERROR)
      }
    },
    
    async handleDelete(id) {
      if (confirm(MESSAGES.CONFIRM_DELETE)) {
        try {
          await userApi.deleteUser(id)
          this.loadUsers()
        } catch (error) {
          console.error(MESSAGES.DELETE_ERROR, error)
          alert(error.message || MESSAGES.DELETE_ERROR)
        }
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
</style>
