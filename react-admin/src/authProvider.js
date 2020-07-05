export default {
  login: ({ username, password }) => {
    const request = new Request(process.env.REACT_APP_API_URL, {
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
        localStorage.setItem('token', token)
      })
  },
  logout: () => {
    localStorage.removeItem('token')
    return Promise.resolve()
  },
  checkAuth: params => localStorage.getItem('token')
    ? Promise.resolve()
    : Promise.reject({ redirectTo: '/login' }),
  checkError: error => Promise.resolve(),
  getPermissions: params => Promise.resolve(),
}
