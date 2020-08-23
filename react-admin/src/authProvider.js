import jwt_decode from 'jwt-decode'

const getAccessToken = async () => {
  const refreshToken = localStorage.getItem('refresh_token')
  const request = new Request(`${process.env.REACT_APP_API_URL}/auth/getAccessToken`, {
    method: 'POST',
    body: JSON.stringify({ token: refreshToken }),
    headers: new Headers({ 'Content-Type': 'application/json' }),
  })
  return fetch(request)
    .then(response => {
      if (response.status < 200 || response.status >= 300) {
        console.log(response)
        throw new Error(response.statusText + ': ' + response.status)
      }
      return response.json()
    })
    .then(({ token }) => {
      localStorage.setItem('access_token', token)
    })
    .catch((e) => {
      return Promise.reject({ status: (e && e.length > 3 && e.slice(-3)) || 403 })
    })
}

const getRefreshToken = async () => {
  const refreshToken = localStorage.getItem('refresh_token')

  const request = new Request(`${process.env.REACT_APP_API_URL}/auth/refreshToken`, {
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
      localStorage.setItem('refresh_token', token)
    })
    .then(getAccessToken)
    .catch((e) => {
      return Promise.reject({ status: (e && e.length > 3 && e.slice(-3)) || 403 })
    })
}

export const checkAuth = async () => {
  const refreshToken = localStorage.getItem('refresh_token')
  const accessToken = localStorage.getItem('access_token')

  if (!refreshToken) {
    return getRefreshToken()
  }

  try {
    const dRefreshToken = jwt_decode(refreshToken)
    const duration = dRefreshToken.exp - dRefreshToken.iat

    if (((duration / 2) + dRefreshToken.iat) * 1000 < Date.now()) {
      return getRefreshToken()
    }
  } catch (e) {
    localStorage.removeItem('refreshToken')
    return Promise.reject()
  }

  try {
    if (!accessToken || jwt_decode(accessToken).exp * 1000 <= Date.now()) {
      return getAccessToken()
    }
  } catch (e) {
    localStorage.removeItem('accessToken')
    return Promise.reject()
  }
  return Promise.resolve()
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
    localStorage.removeItem('access_token')
    localStorage.removeItem('refresh_token')
    return Promise.resolve()
  },
  checkAuth: checkAuth,
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
