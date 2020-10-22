import React, { useContext } from 'react'
import Loader from '../elements/Loader'
import ErrorBox from './ErrorBox'
import MainPageBox from './MainPageBox'
import AfterPageBox from './AfterPageBox'
import { PAGE, PageContext } from '../../providers/page-provider'

const Boxes = () => {
  const { page } = useContext(PageContext)
  switch (page) {
    case PAGE.MAIN_PAGE:
      return <MainPageBox/>
    case PAGE.AFTER_UPLOAD_PAGE:
      return <AfterPageBox/>
    case PAGE.LOADING_PAGE:
      return <Loader/>
    case PAGE.ERROR_PAGE:
      return <ErrorBox/>
    default:
      return <MainPageBox/>
  }
}

export default Boxes
