import React from 'react'
import { fetchUtils, Admin, Resource } from 'react-admin'
import simpleRestProvider from 'ra-data-simple-rest'
import { createBrowserHistory } from 'history'
import authProvider from './authProvider'

import { FileList, FileEdit } from './models/files'
import { UserList, UserEdit } from './models/users'

const history = createBrowserHistory({ basename: '/admin' })

const httpClient = (url, options = {}) => {
  if (!options.headers) {
    options.headers = new Headers({ Accept: 'application/json' })
  }
  const token = localStorage.getItem('token')
  options.headers.set('Authorization', `Bearer ${token}`)
  return fetchUtils.fetchJson(url, options)
}

const dataProvider = simpleRestProvider(process.env.REACT_APP_API_URL, httpClient)

const App = () => (
  <Admin
    history={history}
    dataProvider={dataProvider}
    authProvider={authProvider}
    locale='en'
  >
    <Resource
      name='users'
      list={UserList}
      edit={UserEdit}
    />
    <Resource
      name='files'
      list={FileList}
      edit={FileEdit}
    />
  </Admin>
)

export default App
