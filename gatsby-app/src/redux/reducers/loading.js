import { SET_LOADING } from '../actionTypes'

const initialState = {
  isLoading: false,
  value: 0,
}

export default function(state = initialState, action) {
  switch (action.type) {
    case SET_LOADING: {
      return {
        ...state,
        ...action.payload,
      }
    }
    default:
      return state
  }
}
