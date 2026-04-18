<template>
  <Transition name="message-fade">
    <div v-if="visible" :class="['message', type]">
      {{ message }}
    </div>
  </Transition>
</template>

<script>
import { ref, watch } from 'vue'

export default {
  name: 'Message',
  props: {
    message: {
      type: String,
      default: ''
    },
    type: {
      type: String,
      default: 'info',
      validator: (value) => ['success', 'error', 'warning', 'info'].includes(value)
    },
    duration: {
      type: Number,
      default: 3000
    }
  },
  setup(props) {
    const visible = ref(false)

    watch(() => props.message, (newVal) => {
      if (newVal) {
        visible.value = true
        setTimeout(() => {
          visible.value = false
        }, props.duration)
      }
    })

    return {
      visible
    }
  }
}
</script>

<style scoped>
.message {
  position: fixed;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  padding: 12px 24px;
  border-radius: 4px;
  color: white;
  font-size: 14px;
  z-index: 2000;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.message.success {
  background-color: #67c23a;
}

.message.error {
  background-color: #f56c6c;
}

.message.warning {
  background-color: #e6a23c;
}

.message.info {
  background-color: #409eff;
}

.message-fade-enter-active,
.message-fade-leave-active {
  transition: opacity 0.3s ease;
}

.message-fade-enter-from,
.message-fade-leave-to {
  opacity: 0;
}
</style>
