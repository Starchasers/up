import AfterUploadBox from './blocks/AfterUploadBox'
import { faAngleLeft } from '@fortawesome/free-solid-svg-icons'
import React from 'react'
import { getResponseState } from '../redux/selectors'
import { connect } from 'react-redux'
import { setPage, setResponse } from '../redux/actions'
import { PAGE_ID } from '../redux/constants'

const AfterPageBox = ({ response, dispatch }) => {
  const hrefLink = (process.env.GATSBY_API_URL ? process.env.GATSBY_API_URL : '') + '/u/'
  const displayLink = (process.env.GATSBY_API_URL ? process.env.GATSBY_API_URL : window.location.origin) + '/u/'
  return (
    <AfterUploadBox>
      <AfterUploadBox.TextBox>
        <AfterUploadBox.Text
          target='_blank'
          href={hrefLink + response.data.key}
        >
          {displayLink + response.data.key}
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

const mapStateToProps = store => {
  const response = getResponseState(store)
  return { response }
}

export default connect(mapStateToProps)(AfterPageBox)
