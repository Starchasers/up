import React, { useCallback, useEffect, useMemo, useState } from 'react'
import { useDispatch } from 'react-redux'
import { setError, setLoading, setPage, setResponse } from '../redux/actions'
import { PAGE_ID } from '../redux/constants'
import axios from 'axios'
import DropZone, { useDropzone } from 'react-dropzone'
import Offset from '../components/elements/Offset'
import GlobalDropZone from '../components/blocks/GlobalDropZone'

const backendURL = process.env.GATSBY_API_URL ? process.env.GATSBY_API_URL : ''

export const FileUploadContext = React.createContext({})

const FileUploadProvider = ({ children }) => {
  const [dragActive, setDragActive] = useState(false)
  const dispatch = useDispatch()

  const handleFileUpload = useCallback(async ({ file }) => {
    try {
      dispatch(setLoading({ isLoading: true, value: 0 }))
      dispatch(setPage({ pageId: PAGE_ID.LOADING_PAGE }))

      const checkSize = await axios.post(`${backendURL}/api/verifyUpload`, { size: file.get('file').size })
      if (!checkSize.data.valid) {
        dispatch(setError({
          message: 'File is too large. Maximum size: ' + checkSize.data.maxUploadSize / 1000 + ' MB',
          status: 413
        }))
        dispatch(setResponse({ received: false, data: {} }))
        dispatch(setPage({ pageId: PAGE_ID.ERROR_PAGE }))
        dispatch(setLoading({ isLoading: true, value: 0 }))
        return
      }
      const response = await axios.post(`${backendURL}/api/upload`, file, {
        headers: {
          'Content-Type': 'multipart/form-data'
        },
        onUploadProgress: (value) => {
          dispatch(setLoading({ isLoading: true, value: Math.round(value.loaded / value.total * 100) }))
        }
      })
      dispatch(setResponse({ received: true, data: { ...response.data } }))
      dispatch(setPage({ pageId: PAGE_ID.AFTER_UPLOAD_PAGE }))
    } catch (e) {
      dispatch(
        setError({
          message: e.response ? e.response.data.message : e.toString(),
          status: e.response ? e.response.status : undefined
        })
      )
      dispatch(setResponse({ received: false, data: {} }))
      dispatch(setPage({ pageId: PAGE_ID.ERROR_PAGE }))
    } finally {
      dispatch(setLoading({ isLoading: false, value: 100 }))
    }
  }, [dispatch])

  const handleOnPaste = useCallback((event) => {
    const items = event.clipboardData.items
    const data = new FormData()
    if (event.clipboardData.getData('text') === '') {
      let blob = null

      for (let i = 0; i < items.length; i++) {
        if (items[i].type.indexOf('image') === 0) {
          blob = items[i].getAsFile()
        }
      }
      if (!blob) return
      data.append('file', blob)
    } else {
      const file = new File([event.clipboardData.getData('text')], 'paste.txt', {
        type: 'text/plain'
      })
      data.append('file', file)
    }
    handleFileUpload({
      file: data
    })
  }, [handleFileUpload])

  const onDrop = useCallback((acceptedFiles) => {
    if (acceptedFiles.length > 0) {
      const data = new FormData()
      data.append('file', acceptedFiles[0])
      handleFileUpload({ file: data })
    } else {
      dispatch(setError({
        message: 'Invalid input, please make sure to upload a valid file',
        status: undefined
      }))
      dispatch(setResponse({ received: false, data: {} }))
      dispatch(setPage({ pageId: PAGE_ID.ERROR_PAGE }))
    }
  }, [dispatch, handleFileUpload])

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop: onDrop,
    multiple: false
  })

  useEffect(() => {
    window.addEventListener('paste', handleOnPaste, false)
    return () => window.removeEventListener('paste', handleOnPaste)
  }, [handleOnPaste])

  const value = useMemo(() => ({
    handleFileUpload,
    getRootProps,
    getInputProps,
    isDragActive
  }), [
    handleFileUpload,
    getRootProps,
    getInputProps,
    isDragActive
  ])

  return (
    <FileUploadContext.Provider value={value}>
      <DropZone
        onDrop={data => {
          setDragActive(false)
          onDrop(data)
        }}
        multiple={false}
        onDragEnter={() => setDragActive(true)}
        onDragLeave={() => setDragActive(false)}
      >
        {({ getRootProps }) => (
          <>
            <Offset blur={dragActive} {...getRootProps()}>
              {children}
            </Offset>
            <GlobalDropZone active={dragActive} {...getRootProps()}>
              <GlobalDropZone.Container>
                <GlobalDropZone.Text>
                  Drop file here
                </GlobalDropZone.Text>
              </GlobalDropZone.Container>
            </GlobalDropZone>
          </>
        )}
      </DropZone>
    </FileUploadContext.Provider>
  )
}

export default FileUploadProvider
