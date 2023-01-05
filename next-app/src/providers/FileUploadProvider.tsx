import { AxiosError, AxiosResponse } from 'axios'
import React, { useContext, useEffect, useState } from 'react'
import { DropzoneState, useDropzone } from 'react-dropzone'
import { toast } from 'react-toastify'
import { useMutation, UseMutationResult } from 'react-query'
import APIClient from '../api-client'
import { IUploadFileRequest, IUploadFileResponse } from '../../@types/api-client/uploadFile'
import { TOnDropFunction } from '../../@types/react-dropzone'
import { UploadBoxContentContext } from './UploadBoxContentProvider'
import { Box } from '../../@types/box'
import handleErrorMessage from '../utils/handleErrorMessage'
import { IOnUploadProgress } from '../../@types/axios'
import { IUpAppUniversalError } from '../../@types/api-client/common'
import { IDeleteFileRequest, IDeleteFileResponse } from '../../@types/api-client/deleteFile'

export type TFileUploadContext = {
  uploadFileMutation: UseMutationResult<
    AxiosResponse<IUploadFileResponse>,
    unknown,
    IUploadFileRequest
  >
  deleteFileMutation: UseMutationResult<
    AxiosResponse<IDeleteFileResponse>,
    unknown,
    IDeleteFileRequest
  >
  fileUploadDropzone: DropzoneState
  loadingValue: number | null
}

const defaultValue: TFileUploadContext = {
  uploadFileMutation: null,
  deleteFileMutation: null,
  fileUploadDropzone: null,
  loadingValue: null
}

export const FileUploadContext = React.createContext<TFileUploadContext>(defaultValue)

const FileUploadProvider: React.FC = (props) => {
  const uploadBoxContext = useContext(UploadBoxContentContext)
  const [loadingValue, setLoadingValue] = useState<number | null>(null)

  const getConfigurationMutation = useMutation(APIClient.getConfiguration, {
    onError: (error: AxiosError<IUpAppUniversalError> | Error) => {
      handleErrorMessage(error, { autoClose: false })
      uploadBoxContext.setCurrentBox(Box.DefaultUploadBox)
    }
  })

  const uploadFileMutation = useMutation(APIClient.uploadFile, {
    onMutate: () => {
      uploadBoxContext.setCurrentBox(Box.UploadingBox)
    },
    onError: (error: AxiosError<IUpAppUniversalError> | Error) => {
      handleErrorMessage(error, { autoClose: false })
      uploadBoxContext.setCurrentBox(Box.DefaultUploadBox)
    }
  })

  const uploadFile = async (file: File) => {
    try {
      let configuration: typeof getConfigurationMutation.data
      if (getConfigurationMutation.isSuccess) {
        configuration = getConfigurationMutation.data
      } else {
        configuration = await getConfigurationMutation.mutateAsync({})
      }

      if (file.size <= configuration.data.maxTemporaryFileSize) {
        await uploadFileMutation.mutateAsync({
          data: { file, expires: '24h' },
          config: {
            onUploadProgress: (value: IOnUploadProgress) =>
              setLoadingValue(Math.round((value.loaded / value.total) * 100))
          }
        })
        uploadBoxContext.setCurrentBox(Box.FileUploadedBox)
        setLoadingValue(null)
      } else {
        toast('File too large', { type: 'error' })
      }
    } catch (e) {
      uploadFileMutation.reset()
    }
  }

  const deleteFileMutation = useMutation(APIClient.deleteFile, {
    onSuccess: uploadFileMutation.reset,
    onError: (error: AxiosError<IUpAppUniversalError> | Error) => {
      handleErrorMessage(error)
    }
  })

  const onDrop: TOnDropFunction = (acceptedFiles) => {
    // Dragging files to upload should be only allowed on DefaultUploadBoxView
    if (uploadBoxContext.currentBox !== Box.DefaultUploadBox) {
      return
    }

    uploadFile(acceptedFiles[0])
  }

  const fileUploadDropzone = useDropzone({
    multiple: false,
    noClick: true,
    noKeyboard: true,
    onDrop
  })

  useEffect(() => {
    const handleOnPaste = async (event: ClipboardEvent) => {
      const items = event.clipboardData?.items
      if (!items) return

      let file: File | null
      const clipboardText: string | undefined = event.clipboardData?.getData('text')

      if (clipboardText === '') {
        file = items[0].getAsFile()
      } else if (clipboardText?.length) {
        file = new File([clipboardText], 'paste.txt', {
          type: 'text/plain'
        })
      } else {
        return
      }

      if (!file) return

      await uploadFile(file)
    }

    window.addEventListener('paste', handleOnPaste, false)
    return () => window.removeEventListener('paste', handleOnPaste)
  }, [])

  return (
    <FileUploadContext.Provider
      value={{
        uploadFileMutation,
        deleteFileMutation,
        fileUploadDropzone,
        loadingValue
      }}
    >
      <input {...fileUploadDropzone.getInputProps()} />
      {props.children}
    </FileUploadContext.Provider>
  )
}

export default FileUploadProvider
