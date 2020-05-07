import { SET_ERROR } from '../actionTypes'

const initialState = {
  message: '',
  status: 0,
}

export default function(state = initialState, action) {
  switch (action.type) {
    case SET_ERROR: {
      return {
        ...state,
        ...action.payload,
      }
    }
    default:
      return state
  }
}
