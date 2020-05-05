import React from 'react'
import CustomErrorBox from './blocks/CustomErrorBox'
import { faAngleLeft, faBug } from '@fortawesome/free-solid-svg-icons'
import { getErrorState } from '../redux/selectors'
import { connect } from 'react-redux'
import { setError, setPage } from '../redux/actions'
import { PAGE_ID } from '../redux/constants'

const ErrorBox = ({ children, error, dispatch }) => (
  (error.message || !!(error.status))
    ? <CustomErrorBox>
      {
        error.status
          ? <CustomErrorBox.Status> {error.status}</CustomErrorBox.Status>
          : <CustomErrorBox.Icon icon={faBug}/>
      }
      <CustomErrorBox.Header>Oopsie woopsie,</CustomErrorBox.Header>
      <CustomErrorBox.Header>something went wrong</CustomErrorBox.Header>
      <CustomErrorBox.Pre>
        <CustomErrorBox.Code>
          {error.message}
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
    : children
)

const mapStateToProps = state => {
  const error = getErrorState(state)
  return { error }
}

export default connect(mapStateToProps)(ErrorBox)
