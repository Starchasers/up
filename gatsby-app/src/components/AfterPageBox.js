import AfterUploadBox from './blocks/AfterUploadBox'
import { faAngleLeft } from '@fortawesome/free-solid-svg-icons'
import React from 'react'
import { setPage, setResponse } from '../redux/actions'
import { PAGE_ID } from '../redux/constants'
import { useDispatch, useSelector } from 'react-redux'

const AfterPageBox = () => {
  const dispatch = useDispatch()
  const responseDataKey = useSelector(state => state.response.data.key)
  const hrefLink = (process.env.GATSBY_API_URL ? process.env.GATSBY_API_URL : '') + '/u/'
  const displayLink = (process.env.GATSBY_API_URL ? process.env.GATSBY_API_URL : window.location.origin) + '/u/'
  return (
    <AfterUploadBox>
      <AfterUploadBox.TextBox>
        <AfterUploadBox.Text
          target='_blank'
          href={hrefLink + responseDataKey}
        >
          {displayLink + responseDataKey}
        </AfterUploadBox.Text>
      </AfterUploadBox.TextBox>
      <AfterUploadBox.Back
        onClick={() => {
          dispatch(setResponse({ received: false, data: {} }))
          dispatch(setPage({ pageId: PAGE_ID.MAIN_PAGE }))
        }}>
        <AfterUploadBox.Icon icon={faAngleLeft}/>
        Go back
      </AfterUploadBox.Back>
    </AfterUploadBox>
  )
}

export default AfterPageBox
