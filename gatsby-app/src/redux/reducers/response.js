import { SET_RESPONSE } from '../actionTypes'

const initialState = {
  received: false,
  data: {},
}

export default function(state = initialState, action) {
  switch (action.type) {
    case SET_RESPONSE: {
      return {
        ...state,
        ...action.payload,
      }
    }
    default:
      return state
  }
}
