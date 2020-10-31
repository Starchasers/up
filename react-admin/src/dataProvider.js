import { checkAuth } from './authProvider'

const API_URL = `${process.env.REACT_APP_API_URL}/admin`

const dataProvider = {
  getList: async (resource, params) => {
    await checkAuth()
    const { page, perPage } = params.pagination
    const { field, order } = params.sort
    const token = localStorage.getItem('access_token')
    const request = new Request(`${API_URL}/${resource}?page=${page - 1}&size=${perPage}&sort=${field},${order}`, {
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
    const token = localStorage.getItem('access_token')
    const request = new Request(`${API_URL}/${resource}/${params.id}`, {
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
  // NO VISIBLE USE CASE
  getMany: (resource, params) => Promise,
  // NO VISIBLE USE CASE
  getManyReference: (resource, params) => Promise,
  create: async (resource, params) => {
    await checkAuth()
    const token = localStorage.getItem('access_token')
    const request = new Request(`${API_URL}/${resource}`, {
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
          return Promise.reject(response)
        }
        return response.json()
      })
    return {
      data: response
    }
  },
  update: async (resource, params) => {
    await checkAuth()
    const token = localStorage.getItem('access_token')
    const request = new Request(`${API_URL}/${resource}/${params.id}`, {
      method: 'PATCH',
      headers: new Headers({
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }),
      body: JSON.stringify(params.data)
    })
    const response = await fetch(request)
      .then(response => {
        if (response.status < 200 || response.status >= 300) {
          return Promise.reject(response)
        }
        return { data: { id: params.id }}
      })
    return {
      data: response
    }
  },
  // NO VISIBLE USE CASE
  updateMany: (resource, params) => Promise,
  delete: async (resource, params) => {
    await checkAuth()
    const token = localStorage.getItem('access_token')
    const request = new Request(`${API_URL}/${resource}/${params.id}`, {
      method: 'DELETE',
      headers: new Headers({
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      })
    })
    const response = await fetch(request)
      .then(response => {
        if (response.status < 200 || response.status >= 300) {
          return Promise.reject(response)
        }
        return { data: { id: params.id }}
      })
    return {
      data: response
    }
  },
  // NO VISIBLE USE CASE
  deleteMany: (resource, params) => Promise,
}

export default dataProvider
