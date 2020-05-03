import { useCallback } from 'react'
import axios from 'axios'

const useFileUpload = ({ loading, response, setLoading, setError, setResponse }) => {
  const handleFileUpload = useCallback(async ({ files }) => {
    try {
      setLoading({ isLoading: true, value: 0 })

      const data = new FormData()
      data.append('file', files[0])

      const response = await axios.post(`${process.env.GATSBY_API_URL ? process.env.GATSBY_API_URL : ''}/api/upload`, data, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
        onUploadProgress: (value) => {
          setLoading({ isLoading: true, value: Math.round(value.loaded / value.total * 100) })
        },
      })
      setResponse({ received: true, data: { ...response.data } })
    } catch (e) {
      setError({
        message: e.response ? e.response.data.message : e.toString(),
        status: e.response ? e.response.status : undefined,
      })
      setResponse({ received: false, data: {} })
    } finally {
      setLoading({ isLoading: false, value: 100 })
    }
  }, [setLoading, setError])

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
