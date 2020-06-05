import AfterUploadBox from '../blocks/AfterUploadBox'
import { faAngleLeft, faCopy } from '@fortawesome/free-solid-svg-icons'
import React, { useEffect, useState } from 'react'
import { setPage, setResponse } from '../../redux/actions'
import { PAGE_ID } from '../../redux/constants'
import { useDispatch, useSelector } from 'react-redux'

const AfterPageBox = () => {
  const [showCopied, setShowCopied] = useState(false)
  const dispatch = useDispatch()
  const responseDataKey = useSelector(state => state.response.data.key)
  const displayLink = (process.env.GATSBY_API_URL ? process.env.GATSBY_API_URL : window.location.origin) + '/u/'
  const textRef = React.createRef()

  useEffect(() => {
    textRef.current.focus()
  }, [textRef])

  const handleOnClick = () => {
    setShowCopied(true)
    textRef.current.focus()
    document.execCommand('copy')

    setTimeout(() => setShowCopied(false), 1000)
  }

  const handleOnFocus = (event) => {
    event.target.select()
  }

  return (
    <AfterUploadBox>
      <AfterUploadBox.Center>
        <AfterUploadBox.TextBox active={showCopied}>
          <AfterUploadBox.Link
            onClick={handleOnClick}
            value={displayLink + responseDataKey}
            spellCheck='false'
            ref={textRef}
            onFocus={handleOnFocus}
            onChange={() => {
              return false
            }}
          />
          <AfterUploadBox.CopyButton onClick={handleOnClick}>
            <AfterUploadBox.Icon icon={faCopy} style={{ margin: '0 10px' }}/>
          </AfterUploadBox.CopyButton>
        </AfterUploadBox.TextBox>
        <AfterUploadBox.Tooltip active={showCopied}>
          Copied!
        </AfterUploadBox.Tooltip>
      </AfterUploadBox.Center>
      <AfterUploadBox.Back
        onClick={() => {
          dispatch(setResponse({ received: false, data: {} }))
          dispatch(setPage({ pageId: PAGE_ID.MAIN_PAGE }))
        }}
      >
        <AfterUploadBox.Icon icon={faAngleLeft}/>
        Go back
      </AfterUploadBox.Back>
    </AfterUploadBox>
  )
}

export default AfterPageBox
