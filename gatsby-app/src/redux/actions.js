import { SET_ERROR, SET_LOADING, SET_RESPONSE } from './actionTypes'

export const setLoading = (state) => ({
  type: SET_LOADING,
  payload: {
    ...state,
  },
})

export const setError = (state) => ({
  type: SET_ERROR,
  payload: {
    ...state,
  },
})

export const setResponse = (state) => ({
  type: SET_RESPONSE,
  payload: {
    ...state,
  },
})
