import React from 'react'
import CustomErrorBox from './blocks/CustomErrorBox'
import { faAngleLeft, faBug } from '@fortawesome/free-solid-svg-icons'
import { useDispatch, useSelector } from 'react-redux'
import { setError, setPage } from '../redux/actions'
import { PAGE_ID } from '../redux/constants'

const ErrorBox = () => {
  const dispatch = useDispatch()
  const errorStatus = useSelector(state => state.error.status)
  const errorMessage = useSelector(state => state.error.message)
  return (
    <CustomErrorBox>
      {
        errorStatus
          ? <CustomErrorBox.Status> {errorStatus}</CustomErrorBox.Status>
          : <CustomErrorBox.Icon icon={faBug}/>
      }
      <CustomErrorBox.Header>Oopsie woopsie,</CustomErrorBox.Header>
      <CustomErrorBox.Header>something went wrong</CustomErrorBox.Header>
      <CustomErrorBox.Pre>
        <CustomErrorBox.Code>
          {errorMessage}
        </CustomErrorBox.Code>
      </CustomErrorBox.Pre>
      <CustomErrorBox.Back onClick={() => {
        dispatch(setError({ status: 0, message: '' }))
        dispatch(setPage({ pageId: PAGE_ID.MAIN_PAGE }))
      }}
      >
        <CustomErrorBox.Icon icon={faAngleLeft}/>
        Go back
      </CustomErrorBox.Back>
    </CustomErrorBox>
  )
}

export default ErrorBox
