import AfterUploadBox from '../blocks/AfterUploadBox'
import { faAngleLeft, faCopy } from '@fortawesome/free-solid-svg-icons'
import React, { useEffect, useState } from 'react'
import { setPage, setResponse } from '../../redux/actions'
import { PAGE_ID } from '../../redux/constants'
import { useDispatch, useSelector } from 'react-redux'
import copy from 'copy-to-clipboard'

const AfterPageBox = () => {
  const [showCopied, setShowCopied] = useState(false)
  const [showSelected, setShowSelected] = useState(true)
  const dispatch = useDispatch()
  const responseDataKey = useSelector(state => state.response.data.key)
  const displayLink = (process.env.GATSBY_API_URL ? process.env.GATSBY_API_URL : window.location.origin) + '/u/'

  const handleOnClick = (event) => {
    event.preventDefault()
    setShowCopied(true)
    copy(displayLink + responseDataKey)
    setTimeout(() => setShowCopied(false), 1000)
  }
  useEffect(() => {
    const selectLink = (event) => {
      setShowSelected(event.path[0].classList && event.path[1].classList
        && (event.path[0].classList.contains('focusable') || event.path[1].classList.contains('focusable')))
    }
    window.addEventListener('click', selectLink, false)
    return () => window.removeEventListener('click', selectLink)
  }, [])

  useEffect(() => {
    const copyText = (event) => {
      event.preventDefault()
      event.clipboardData.setData('text/plain', showSelected ? displayLink + responseDataKey : document.getSelection())
    }

    window.addEventListener('copy', copyText, false)
    return () => window.removeEventListener('copy', copyText)
  }, [displayLink, responseDataKey, showSelected])

  return (
    <AfterUploadBox>
      <AfterUploadBox.Center>
        <AfterUploadBox.TextBox active={showCopied}>
          <AfterUploadBox.Link
            onClick={handleOnClick}
            href={displayLink + responseDataKey}
            className='focusable'
          >
            <AfterUploadBox.Text
              className='focusable'
              focused={showSelected}
            >
              {displayLink + responseDataKey}
            </AfterUploadBox.Text>
          </AfterUploadBox.Link>
          <AfterUploadBox.CopyButton
            className='focusable'
            onClick={handleOnClick}
          >
            <AfterUploadBox.Icon
              icon={faCopy}
              className='focusable'
              style={{ margin: '0 10px' }}
            />
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
