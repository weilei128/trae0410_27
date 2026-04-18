<template>
  <div v-if="visible" class="modal-overlay" @click.self="handleClose">
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
          <button type="submit" class="btn btn-primary" :disabled="loading">
            {{ loading ? '保存中...' : (isEdit ? '保存' : '添加') }}
          </button>
          <button type="button" @click="handleClose" class="btn btn-secondary" :disabled="loading">
            取消
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'UserForm',
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
  data() {
    return {
      loading: false,
      formData: {
        id: null,
        name: '',
        age: null,
        email: ''
      }
    }
  },
  computed: {
    isEdit() {
      return this.user !== null
    }
  },
  watch: {
    visible(newVal) {
      if (newVal) {
        this.resetForm()
      }
    },
    user: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.formData = { ...newVal }
        }
      }
    }
  },
  methods: {
    resetForm() {
      if (this.isEdit && this.user) {
        this.formData = { ...this.user }
      } else {
        this.formData = { id: null, name: '', age: null, email: '' }
      }
    },
    handleClose() {
      if (!this.loading) {
        this.$emit('close')
      }
    },
    async handleSubmit() {
      this.loading = true
      try {
        await this.$emit('submit', this.formData)
        this.handleClose()
      } catch (error) {
        console.error('提交表单失败:', error)
      } finally {
        this.loading = false
      }
    }
  }
}
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

.form-group input:disabled {
  background-color: #f5f7fa;
  cursor: not-allowed;
}

.modal-actions {
  margin-top: 24px;
  text-align: right;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  margin-left: 8px;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-primary {
  background-color: #409eff;
  color: white;
}

.btn-primary:hover:not(:disabled) {
  background-color: #66b1ff;
}

.btn-secondary {
  background-color: #909399;
  color: white;
}

.btn-secondary:hover:not(:disabled) {
  background-color: #a6a9ad;
}
</style>
