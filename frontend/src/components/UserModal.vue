<template>
  <div class="modal-overlay" @click.self="handleClose">
    <div class="modal">
      <h2>{{ title }}</h2>
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label>{{ labels.name }}:</label>
          <input v-model="formData.name" type="text" required />
        </div>
        <div class="form-group">
          <label>{{ labels.age }}:</label>
          <input v-model.number="formData.age" type="number" required min="1" />
        </div>
        <div class="form-group">
          <label>{{ labels.email }}:</label>
          <input v-model="formData.email" type="email" required />
        </div>
        <div class="modal-actions">
          <button type="submit" class="btn btn-primary">{{ submitText }}</button>
          <button type="button" @click="handleClose" class="btn btn-secondary">{{ cancelText }}</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { MODAL_TITLES, BUTTON_TEXT, FORM_LABELS } from '../constants'

export default {
  name: 'UserModal',
  props: {
    visible: {
      type: Boolean,
      required: true
    },
    isEdit: {
      type: Boolean,
      default: false
    },
    userData: {
      type: Object,
      default: () => ({
        id: null,
        name: '',
        age: null,
        email: ''
      })
    }
  },
  emits: ['close', 'submit'],
  data() {
    return {
      formData: { ...this.userData },
      labels: {
        name: FORM_LABELS.NAME,
        age: FORM_LABELS.AGE,
        email: FORM_LABELS.EMAIL
      },
      cancelText: BUTTON_TEXT.CANCEL
    }
  },
  computed: {
    title() {
      return this.isEdit ? MODAL_TITLES.EDIT : MODAL_TITLES.ADD
    },
    submitText() {
      return this.isEdit ? BUTTON_TEXT.SAVE : BUTTON_TEXT.ADD_SUBMIT
    }
  },
  watch: {
    userData: {
      handler(newVal) {
        this.formData = { ...newVal }
      },
      immediate: true
    }
  },
  methods: {
    handleClose() {
      this.$emit('close')
    },
    handleSubmit() {
      this.$emit('submit', { ...this.formData })
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
