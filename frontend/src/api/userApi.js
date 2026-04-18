import request from '../utils/request'

export const userApi = {
  getAllUsers() {
    return request.get('/')
  },
  
  getUserById(id) {
    return request.get(`/${id}`)
  },
  
  createUser(user) {
    return request.post('/', user)
  },
  
  updateUser(id, user) {
    return request.put(`/${id}`, user)
  },
  
  deleteUser(id) {
    return request.delete(`/${id}`)
  }
}

export default userApi
