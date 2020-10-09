import { checkAuth } from './authProvider'

const dataProvider = {
  getList: async (resource, params) => {
    await checkAuth()
    const { page, perPage } = params.pagination
    const { field, order } = params.sort
    const token = localStorage.getItem('access_token')
    const request = new Request(`${process.env.REACT_APP_API_URL}/admin/${resource}?page=${page - 1}&size=${perPage}&sort=${field},${order}`, {
      method: 'GET',
      headers: new Headers({ 'Authorization': `Bearer ${token}` }),
    })
    const response = await fetch(request)
      .then(response => {
        if (response.status < 200 || response.status >= 300) {
          throw new Error(response.statusText)
        }
        return response.json()
      })
    return {
      data: response.content,
      total: response.totalElements,
    }
  },
  getOne: async (resource, params) => {
    await checkAuth()
    const { id } = params
    const token = localStorage.getItem('access_token')
    const request = new Request(`${process.env.REACT_APP_API_URL}/admin/${resource}/${id}`, {
      method: 'GET',
      headers: new Headers({ 'Authorization': `Bearer ${token}` }),
    })
    const response = await fetch(request)
      .then(response => {
        if (response.status < 200 || response.status >= 300) {
          throw new Error(response.statusText)
        }
        return response.json()
      })
    return {
      data: response
    }
  },
  getMany: (resource, params) => Promise,
  getManyReference: (resource, params) => Promise,
  create: async (resource, params) => {
    await checkAuth()
    const token = localStorage.getItem('access_token')
    const request = new Request(`${process.env.REACT_APP_API_URL}/admin/${resource}`, {
      method: 'POST',
      headers: new Headers({
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }),
      body: JSON.stringify(params.data)
    })
    const response = await fetch(request)
      .then(response => {
        if (response.status < 200 || response.status >= 300) {
          throw new Error(response.statusText)
        }
        return response.json()
      })
    return {
      data: response
    }
  },
  update: (resource, params) => Promise,
  updateMany: (resource, params) => Promise,
  delete: (resource, params) => Promise,
  deleteMany: (resource, params) => Promise,
}

export default dataProvider
