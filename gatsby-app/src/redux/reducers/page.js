import { SET_PAGE } from '../actionTypes'
import { PAGE_ID } from '../constants'

const initialState = {
  pageId: PAGE_ID.MAIN_PAGE,
}

export default function(state = initialState, action) {
  switch (action.type) {
    case SET_PAGE: {
      return {
        ...state,
        ...action.payload,
      }
    }
    default:
      return state
  }
}
