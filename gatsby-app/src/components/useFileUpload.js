import { useCallback } from 'react'
import axios from 'axios'
import { setError, setLoading, setPage, setResponse } from '../redux/actions'
import { PAGE_ID } from '../redux/constants'

const useFileUpload = ({ loading, response, dispatch }) => {
  const handleFileUpload = useCallback(async ({ files }) => {
    try {
      dispatch(setLoading({ isLoading: true, value: 0 }))
      dispatch(setPage({ pageId: PAGE_ID.LOADING_PAGE }))

      const data = new FormData()
      data.append('file', files[0])

      const response = await axios.post(`${process.env.GATSBY_API_URL ? process.env.GATSBY_API_URL : ''}/api/upload`, data, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
        onUploadProgress: (value) => {
          dispatch(setLoading({ isLoading: true, value: Math.round(value.loaded / value.total * 100) }))
        },
      })
      dispatch(setResponse({ received: true, data: { ...response.data } }))
      dispatch(setPage({ pageId: PAGE_ID.AFTER_UPLOAD_PAGE }))
    } catch (e) {
      setError({
        message: e.response ? e.response.data.message : e.toString(),
        status: e.response ? e.response.status : undefined,
      })
      dispatch(setResponse({ received: false, data: {} }))
      dispatch(setPage({ pageId: PAGE_ID.ERROR_PAGE }))
    } finally {
      dispatch(setLoading({ isLoading: false, value: 100 }))
    }
  }, [dispatch])

  const handleOnPaste = useCallback((event) => {
    if (loading.isLoading || response.received) return
    const items = event.clipboardData.items
    let blob = null

    for (let i = 0; i < items.length; i++) {
      if (items[i].type.indexOf('image') === 0) {
        blob = items[i].getAsFile()
      }
    }
    if (!blob) return
    handleFileUpload({
      files: [blob],
    })
  }, [loading, response, handleFileUpload])

  window.addEventListener('paste', (e) => {
    handleOnPaste(e)
  }, false)

  return {
    handleFileUpload,
    handleOnPaste,
  }
}

export default useFileUpload
