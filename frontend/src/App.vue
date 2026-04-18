<template>
  <div id="app">
    <div class="container">
      <h1>用户信息管理</h1>
      
      <div class="toolbar">
        <button @click="showAddModal" class="btn btn-primary">添加用户</button>
      </div>
      
      <UserTable 
        :users="users" 
        @edit="showEditModal" 
        @delete="handleDelete" 
      />
      
      <UserModal 
        :visible="modalVisible"
        :user="currentUser"
        @close="closeModal"
        @submit="handleSubmit"
      />
      
      <Message 
        :message="message.text" 
        :type="message.type" 
      />
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import UserTable from './components/UserTable.vue'
import UserModal from './components/UserModal.vue'
import Message from './components/Message.vue'
import { useUsers } from './composables/useUsers'

export default {
  name: 'App',
  components: {
    UserTable,
    UserModal,
    Message
  },
  setup() {
    const { users, loadUsers, createUser, updateUser, deleteUser } = useUsers()
    
    const modalVisible = ref(false)
    const currentUser = ref(null)
    const message = reactive({
      text: '',
      type: 'info'
    })

    const showMessage = (text, type = 'info') => {
      message.text = text
      message.type = type
    }

    const showAddModal = () => {
      currentUser.value = null
      modalVisible.value = true
    }

    const showEditModal = (user) => {
      currentUser.value = { ...user }
      modalVisible.value = true
    }

    const closeModal = () => {
      modalVisible.value = false
      currentUser.value = null
    }

    const handleSubmit = async (formData) => {
      try {
        if (formData.id) {
          await updateUser(formData.id, formData)
          showMessage('更新成功', 'success')
        } else {
          await createUser(formData)
          showMessage('创建成功', 'success')
        }
        closeModal()
      } catch (error) {
        showMessage(error.response?.data?.message || '操作失败', 'error')
      }
    }

    const handleDelete = async (id) => {
      if (confirm('确定要删除该用户吗？')) {
        try {
          await deleteUser(id)
          showMessage('删除成功', 'success')
        } catch (error) {
          showMessage(error.response?.data?.message || '删除失败', 'error')
        }
      }
    }

    onMounted(() => {
      loadUsers().catch(() => {
        showMessage('加载用户列表失败', 'error')
      })
    })

    return {
      users,
      modalVisible,
      currentUser,
      message,
      showAddModal,
      showEditModal,
      closeModal,
      handleSubmit,
      handleDelete
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

.toolbar {
  margin-bottom: 20px;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.btn-primary {
  background-color: #409eff;
  color: white;
}

.btn-primary:hover {
  background-color: #66b1ff;
}
</style>
