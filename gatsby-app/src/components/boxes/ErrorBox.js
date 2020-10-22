import React, { useContext } from 'react'
import CustomErrorBox from '../blocks/CustomErrorBox'
import { faAngleLeft, faBug } from '@fortawesome/free-solid-svg-icons'
import { PAGE, PageContext } from '../../providers/page-provider'

const ErrorBox = () => {
  const { error, setError, setPage } = useContext(PageContext)
  return (
    <CustomErrorBox>
      {
        error?.status
          ? <CustomErrorBox.Status> {error.status}</CustomErrorBox.Status>
          : <CustomErrorBox.Icon icon={faBug}/>
      }
      <CustomErrorBox.Header>Oopsie woopsie,</CustomErrorBox.Header>
      <CustomErrorBox.Header>something went wrong</CustomErrorBox.Header>
      <CustomErrorBox.Pre>
        <CustomErrorBox.Code>
          {error?.message}
        </CustomErrorBox.Code>
      </CustomErrorBox.Pre>
      <CustomErrorBox.Back onClick={() => {
        setError({ status: 0, message: '' })
        setPage({ pageId: PAGE.MAIN_PAGE })
      }}
      >
        <CustomErrorBox.Icon icon={faAngleLeft}/>
        Go back
      </CustomErrorBox.Back>
    </CustomErrorBox>
  )
}

export default ErrorBox
