<template>
  <div class="toolbar">
    <button @click="$emit('add')" class="btn btn-primary">{{ buttonText }}</button>
  </div>
</template>

<script>
import { BUTTON_TEXT } from '../constants'

export default {
  name: 'Toolbar',
  emits: ['add'],
  data() {
    return {
      buttonText: BUTTON_TEXT.ADD
    }
  }
}
</script>

<style scoped>
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
