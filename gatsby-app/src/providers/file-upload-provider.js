import React, { createContext, useCallback, useContext, useEffect, useState } from 'react'
import DropZone, { useDropzone } from 'react-dropzone'
import Offset from '../components/elements/Offset'
import GlobalDropZone from '../components/blocks/GlobalDropZone'
import APIClient from '../api-client'
import { PAGE, PageContext } from './page-provider'
import { LoadingContext } from './loading-provider'

export const FileUploadContext = createContext({})

const FileUploadProvider = ({ children }) => {
  const [dragActive, setDragActive] = useState(false)
  const { setPage, setError, setResponse } = useContext(PageContext)
  const { setLoading } = useContext(LoadingContext)

  const handleFileUpload = useCallback(async ({ file }) => {
    try {
      const fileSize = file.get('file').size

      setLoading({ isLoading: true, value: 0 })
      setPage(PAGE.LOADING_PAGE)

      const configuration = await APIClient.getConfiguration()
      const maxTemporaryFileSize = configuration.data.maxTemporaryFileSize

      if (fileSize >= maxTemporaryFileSize) {
        setError({
          message: 'File is too large. Maximum size: ' + Math.floor(maxTemporaryFileSize / 1024 / 1024) + ' MiB',
          status: 413
        })
        setResponse({ data: {}})
        setPage(PAGE.ERROR_PAGE)
        setLoading({ isLoading: false, value: 0 })
        return
      }

      const response = await APIClient.postFile(file, {
        headers: {
          'Content-Type': 'multipart/form-data'
        },
        onUploadProgress: (value) => {
          setLoading({ isLoading: true, value: Math.round(value.loaded / value.total * 100) })
        }
      })
      setResponse({ data: { ...response.data } })
      setPage(PAGE.AFTER_UPLOAD_PAGE)
    } catch (e) {
      setError({
        message: e.response ? e.response.data.message : e.toString(),
        status: e.response ? e.response.status : undefined
      })
      setResponse({ data: {} })
      setPage(PAGE.ERROR_PAGE)
    } finally {
      setLoading({ isLoading: false, value: 100 })
    }
  }, [setError, setPage, setResponse, setLoading])

  const onDrop = useCallback((acceptedFiles) => {
    if (acceptedFiles.length > 0) {
      const data = new FormData()
      data.append('file', acceptedFiles[0])
      return handleFileUpload({ file: data })
    }
    setError({
      message: 'Invalid input, please make sure to upload a valid file',
      status: undefined
    })
    setResponse({ data: {} })
    setPage(PAGE.ERROR_PAGE)
  }, [handleFileUpload, setError, setPage, setResponse])

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop: onDrop,
    multiple: false
  })

  useEffect(() => {
    const handleOnPaste = (event) => {
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
      return handleFileUpload({
        file: data
      })
    }
    window.addEventListener('paste', handleOnPaste, false)
    return () => window.removeEventListener('paste', handleOnPaste)
  }, [handleFileUpload])

  const value = {
    handleFileUpload,
    getRootProps,
    getInputProps,
    isDragActive
  }

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
