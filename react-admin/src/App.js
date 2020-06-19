import React from 'react'
import { Admin } from 'react-admin'
import jsonServerProvider from 'ra-data-json-server'
import { createBrowserHistory } from 'history'

const history = createBrowserHistory({ basename: '/admin' })
const dataProvider = jsonServerProvider(process.env.REACT_APP_API_URL)
const App = () => <Admin history={history} dataProvider={dataProvider}/>

export default App
