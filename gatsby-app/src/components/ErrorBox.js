import React from 'react'
import CustomErrorBox from './blocks/CustomErrorBox'
import { faAngleLeft, faBug } from '@fortawesome/free-solid-svg-icons'

const ErrorBox = (props) => (
  props.active
    ? <CustomErrorBox>
      {
        props.status
          ? <CustomErrorBox.Status> {props.status}</CustomErrorBox.Status>
          : <CustomErrorBox.Icon icon={faBug}/>
      }
      <CustomErrorBox.Header>Oopsie woopsie,</CustomErrorBox.Header>
      <CustomErrorBox.Header>something went wrong</CustomErrorBox.Header>
      <CustomErrorBox.Pre>
        <CustomErrorBox.Code>
          {props.message}
        </CustomErrorBox.Code>
      </CustomErrorBox.Pre>
      <CustomErrorBox.Back onClick={() => props.setError({ active: false, message: '' })}>
        <CustomErrorBox.Icon icon={faAngleLeft}/>
        Go back
      </CustomErrorBox.Back>
    </CustomErrorBox>
    : props.children
)

export default ErrorBox
