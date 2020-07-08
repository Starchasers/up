import jwt_decode from 'jwt-decode'

export default {
  login: ({ username, password }) => {
    const request = new Request(`${process.env.REACT_APP_API_URL}/auth/login`, {
      method: 'POST',
      body: JSON.stringify({ username, password }),
      headers: new Headers({ 'Content-Type': 'application/json' }),
    })
    return fetch(request)
      .then(response => {
        if (response.status < 200 || response.status >= 300) {
          throw new Error(response.statusText)
        }
        return response.json()
      })
      .then(({ token }) => {
        localStorage.removeItem('access_token')
        localStorage.setItem('refresh_token', token)
      })
  },
  logout: () => {
    if (typeof localStorage.getItem('refresh_token') === 'string') {
      const request = new Request(`${process.env.REACT_APP_API_URL}/auth/logout`, {
        method: 'POST',
        headers: new Headers({
          'Content-Type': 'application/json',
          'authorization': localStorage.getItem('refresh_token'),
        }),
      })
      localStorage.removeItem('access_token')
      localStorage.removeItem('refresh_token')
      return fetch(request)
        .then(response => {
          if (response.status < 200 || response.status >= 300) {
            throw new Error(response.statusText)
          }
        })
    }
    return Promise.resolve()
  },
  checkAuth: async params => {
    const refreshToken = localStorage.getItem('refresh_token')
    if (typeof refreshToken !== 'string') return Promise.reject({ redirectTo: '/login' })
    if (jwt_decode(refreshToken).exp * 1000 <= Date.now()) {
      localStorage.removeItem('refresh_token')
      localStorage.removeItem('access_token')
      return Promise.reject({ redirectTo: '/login' })
    }

    const accessToken = localStorage.getItem('access_token')

    if (typeof accessToken !== 'string' || jwt_decode(accessToken).exp * 1000 <= Date.now()) {
      const request = new Request(`${process.env.REACT_APP_API_URL}/auth/getAccessToken`, {
        method: 'POST',
        body: JSON.stringify({ token: refreshToken }),
        headers: new Headers({ 'Content-Type': 'application/json' }),
      })
      await fetch(request)
        .then(response => {
          if (response.status < 200 || response.status >= 300) {
            throw new Error(response.statusText)
          }
          return response.json()
        })
        .then(({ token }) => {
          localStorage.setItem('access_token', token)
        })
    }

    return Promise.resolve()
  },
  checkError: error => Promise.resolve(),
  getPermissions: params => Promise.resolve(),
}
