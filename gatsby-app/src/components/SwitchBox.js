import { PAGE_ID } from '../redux/constants'
import React from 'react'
import { useSelector } from 'react-redux'
import Loader from './elements/Loader'
import ErrorBox from './ErrorBox'
import MainPageBox from './MainPageBox'
import AfterPageBox from './AfterPageBox'

const SwitchBox = () => {
  const pageId = useSelector(state => state.page.pageId)
  switch (pageId) {
    case PAGE_ID.MAIN_PAGE:
      return <MainPageBox/>
    case PAGE_ID.AFTER_UPLOAD_PAGE:
      return <AfterPageBox/>
    case PAGE_ID.LOADING_PAGE:
      return <Loader/>
    case PAGE_ID.ERROR_PAGE:
      return <ErrorBox/>
    default:
      return <MainPageBox/>
  }
}

export default SwitchBox
