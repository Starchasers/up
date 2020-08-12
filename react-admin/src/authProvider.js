import jwt_decode from 'jwt-decode'

const getAccessToken = () => {
  const refreshToken = localStorage.getItem('refresh_token')
  const request = new Request(`${process.env.REACT_APP_API_URL}/auth/getAccessToken`, {
    method: 'POST',
    body: JSON.stringify({ token: refreshToken }),
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
      localStorage.setItem('access_token', token)
    })
}

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
        localStorage.setItem('refresh_token', token)
      })
      .then(getAccessToken)
  },
  logout: () => {
    const refreshToken = localStorage.getItem('refresh_token')
    if (refreshToken) {
      localStorage.removeItem('access_token')
      localStorage.removeItem('refresh_token')
    }
    return Promise.resolve()
  },
  checkAuth: () => {
    const refreshToken = localStorage.getItem('refresh_token')
    const accessToken = localStorage.getItem('access_token')

    if (!refreshToken) {
      return Promise.reject()
    }

    if (!accessToken || jwt_decode(accessToken).exp * 1000 <= Date.now()) {
      return getAccessToken()
    }

    return Promise.resolve()
  },
  checkError: ({ status }) => {
    return status === 401 || status === 403
      ? Promise.reject({ redirectTo: '/login' })
      : Promise.resolve()
  },
  getPermissions: () => {
    const accessToken = localStorage.getItem('access_token')
    return accessToken
      ? Promise.resolve(jwt_decode(accessToken).role)
      : Promise.reject()
  },
}
