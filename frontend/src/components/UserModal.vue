<template>
  <div v-if="visible" class="modal-overlay" @click.self="$emit('close')">
    <div class="modal">
      <h2>{{ isEdit ? '编辑用户' : '添加用户' }}</h2>
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label>姓名:</label>
          <input v-model="formData.name" type="text" required />
        </div>
        <div class="form-group">
          <label>年龄:</label>
          <input v-model.number="formData.age" type="number" required />
        </div>
        <div class="form-group">
          <label>邮箱:</label>
          <input v-model="formData.email" type="email" required />
        </div>
        <div class="modal-actions">
          <button type="submit" class="btn btn-primary">{{ isEdit ? '保存' : '添加' }}</button>
          <button type="button" @click="$emit('close')" class="btn btn-secondary">取消</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { reactive, watch } from 'vue'

export default {
  name: 'UserModal',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    user: {
      type: Object,
      default: null
    }
  },
  emits: ['close', 'submit'],
  setup(props, { emit }) {
    const formData = reactive({
      id: null,
      name: '',
      age: null,
      email: ''
    })

    const isEdit = computed(() => !!props.user)

    watch(() => props.user, (newUser) => {
      if (newUser) {
        formData.id = newUser.id
        formData.name = newUser.name
        formData.age = newUser.age
        formData.email = newUser.email
      } else {
        formData.id = null
        formData.name = ''
        formData.age = null
        formData.email = ''
      }
    }, { immediate: true })

    const handleSubmit = () => {
      emit('submit', { ...formData })
    }

    return {
      formData,
      isEdit,
      handleSubmit
    }
  }
}
</script>

<script setup>
import { computed } from 'vue'
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal {
  background: white;
  padding: 24px;
  border-radius: 8px;
  width: 400px;
  max-width: 90%;
}

.modal h2 {
  margin-bottom: 20px;
  color: #333;
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #606266;
  font-weight: 500;
}

.form-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 14px;
}

.form-group input:focus {
  outline: none;
  border-color: #409eff;
}

.modal-actions {
  margin-top: 24px;
  text-align: right;
}

.modal-actions .btn {
  margin-left: 8px;
  margin-right: 0;
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

.btn-secondary {
  background-color: #909399;
  color: white;
}

.btn-secondary:hover {
  background-color: #a6a9ad;
}
</style>
