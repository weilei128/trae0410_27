<template>
  <div>
    <div class="toolbar">
      <button @click="handleAdd" class="btn btn-primary">添加用户</button>
    </div>

    <table class="user-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>姓名</th>
          <th>年龄</th>
          <th>邮箱</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.id">
          <td>{{ user.id }}</td>
          <td>{{ user.name }}</td>
          <td>{{ user.age }}</td>
          <td>{{ user.email }}</td>
          <td>
            <button @click="handleEdit(user)" class="btn btn-edit" :disabled="loading">
              编辑
            </button>
            <button @click="handleDelete(user.id)" class="btn btn-danger" :disabled="loading">
              删除
            </button>
          </td>
        </tr>
        <tr v-if="users.length === 0">
          <td colspan="5" class="empty-message">暂无用户数据</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  name: 'UserTable',
  props: {
    users: {
      type: Array,
      default: () => []
    },
    loading: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    handleAdd() {
      this.$emit('add')
    },
    handleEdit(user) {
      this.$emit('edit', user)
    },
    handleDelete(id) {
      this.$emit('delete', id)
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
  margin-right: 8px;
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

.btn-edit {
  background-color: #67c23a;
  color: white;
}

.btn-edit:hover:not(:disabled) {
  background-color: #85ce61;
}

.btn-danger {
  background-color: #f56c6c;
  color: white;
}

.btn-danger:hover:not(:disabled) {
  background-color: #f78989;
}

.user-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  border-radius: 4px;
  overflow: hidden;
}

.user-table th,
.user-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ebeef5;
}

.user-table th {
  background-color: #f5f7fa;
  color: #606266;
  font-weight: 600;
}

.user-table tr:hover {
  background-color: #f5f7fa;
}

.empty-message {
  text-align: center;
  color: #909399;
  padding: 40px;
}
</style>
