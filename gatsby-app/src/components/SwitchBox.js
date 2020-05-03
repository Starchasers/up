import { PAGE_ID } from '../redux/constants'
import React from 'react'
import { getPageState } from '../redux/selectors'
import { connect } from 'react-redux'
import Loader from './elements/Loader'
import ErrorBox from './ErrorBox'
import MainPageBox from './MainPageBox'
import AfterPageBox from './AfterPageBox'

const SwitchBox = ({ page }) => {
  switch (page.pageId) {
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

const mapStateToProps = store => {
  const page = getPageState(store)
  return { page }
}

export default connect(mapStateToProps)(SwitchBox)
