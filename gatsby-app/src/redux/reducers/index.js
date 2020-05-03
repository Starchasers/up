import { combineReducers } from 'redux'
import loading from './loading'
import error from './error'
import response from './response'

export default combineReducers({
  loading,
  error,
  response,
})
