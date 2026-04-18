<template>
  <table class="user-table">
    <thead>
      <tr>
        <th v-for="header in headers" :key="header.key">{{ header.label }}</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="user in users" :key="user.id">
        <td>{{ user.id }}</td>
        <td>{{ user.name }}</td>
        <td>{{ user.age }}</td>
        <td>{{ user.email }}</td>
        <td>
          <button @click="$emit('edit', user)" class="btn btn-edit">{{ buttonText.edit }}</button>
          <button @click="$emit('delete', user.id)" class="btn btn-danger">{{ buttonText.delete }}</button>
        </td>
      </tr>
      <tr v-if="users.length === 0">
        <td :colspan="headers.length" class="empty-message">{{ emptyMessage }}</td>
      </tr>
    </tbody>
  </table>
</template>

<script>
import { TABLE_HEADERS, BUTTON_TEXT, MESSAGES } from '../constants'

export default {
  name: 'UserTable',
  props: {
    users: {
      type: Array,
      required: true
    }
  },
  emits: ['edit', 'delete'],
  data() {
    return {
      headers: [
        { key: 'id', label: TABLE_HEADERS.ID },
        { key: 'name', label: TABLE_HEADERS.NAME },
        { key: 'age', label: TABLE_HEADERS.AGE },
        { key: 'email', label: TABLE_HEADERS.EMAIL },
        { key: 'actions', label: TABLE_HEADERS.ACTIONS }
      ],
      buttonText: {
        edit: BUTTON_TEXT.EDIT,
        delete: BUTTON_TEXT.DELETE
      },
      emptyMessage: MESSAGES.EMPTY_DATA
    }
  }
}
</script>

<style scoped>
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

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  margin-right: 8px;
}

.btn-edit {
  background-color: #67c23a;
  color: white;
}

.btn-edit:hover {
  background-color: #85ce61;
}

.btn-danger {
  background-color: #f56c6c;
  color: white;
}

.btn-danger:hover {
  background-color: #f78989;
}
</style>
