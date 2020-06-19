import AfterUploadBox from '../blocks/AfterUploadBox'
import { faAngleLeft, faCopy } from '@fortawesome/free-solid-svg-icons'
import React, { useEffect, useMemo, useRef, useState } from 'react'
import { setPage, setResponse } from '../../redux/actions'
import { PAGE_ID } from '../../redux/constants'
import { useDispatch, useSelector } from 'react-redux'
import copy from 'copy-to-clipboard'

const AfterPageBox = () => {
  const [showTooltip, setShowTooltip] = useState(false)
  const dispatch = useDispatch()
  const responseDataKey = useSelector(state => state.response.data.key)
  const linkNode = useRef(null)

  const resourceLink = useMemo(() => {
    return (process.env.GATSBY_API_URL ? process.env.GATSBY_API_URL : window.location.origin) + '/u/' + responseDataKey
  }, [responseDataKey])

  const selectLink = () => {
    try {
      const range = new Range()
      range.setStart(linkNode.current, 0)
      range.setEndAfter(linkNode.current)
      const selection = window.getSelection()
      selection.removeAllRanges()
      selection.addRange(range)
    } catch (e) {
      console.log('Your browser is outdated', e)
    }
  }

  const handleCopyButton = (event) => {
    event.preventDefault()
    setShowTooltip(true)
    copy(resourceLink)
    setTimeout(() => setShowTooltip(false), 1000)
  }

  const handleLinkClick = (event) => {
    event.preventDefault()
    selectLink()
  }

  useEffect(() => {
    const copyText = (event) => {
      const select = window.getSelection()
      if (!select.isCollapsed) {
        return
      }
      selectLink()
      event.clipboardData.setData('text/plain', resourceLink)
      event.preventDefault()
    }

    selectLink()

    window.addEventListener('copy', copyText)
    return () => window.removeEventListener('copy', copyText)
  }, [resourceLink])

  return (
    <AfterUploadBox>
      <AfterUploadBox.Center>
        <AfterUploadBox.TextBox>
          <AfterUploadBox.Link
            onClick={handleLinkClick}
            href={resourceLink}
          >
            <AfterUploadBox.Text
              ref={linkNode}
            >
              {resourceLink}
            </AfterUploadBox.Text>
          </AfterUploadBox.Link>
          <AfterUploadBox.CopyButton
            onClick={handleCopyButton}
          >
            <AfterUploadBox.Icon
              icon={faCopy}
              style={{ margin: '0 10px' }}
            />
          </AfterUploadBox.CopyButton>
        </AfterUploadBox.TextBox>
        <AfterUploadBox.Tooltip active={showTooltip}>
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
