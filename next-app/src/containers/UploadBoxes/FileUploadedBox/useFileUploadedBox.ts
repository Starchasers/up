import { useCallback, useContext, useMemo } from 'react'
import { toast } from 'react-toastify'
import { FileUploadContext } from '../../../providers/FileUploadProvider'
import { UploadBoxContentContext } from '../../../providers/UploadBoxContentProvider'
import { Box } from '../../../../@types/box'
import { IUploadFileResponse } from '../../../../@types/api-client/uploadFile'

const fileServingPath = 'u'

export interface IUseFileUploadedBoxState {
  link: string
  handleGoBackButton: () => void
  handleDeleteFileClick: () => void
  fileUploadData: IUploadFileResponse | null
}

const useFileUploadedBox = (): IUseFileUploadedBoxState => {
  const fileUploadContext = useContext(FileUploadContext)
  const uploadBoxContent = useContext(UploadBoxContentContext)

  const link = useMemo((): string => {
    if (fileUploadContext.uploadFileMutation.data) {
      return `${window.location.origin}/${fileServingPath}/${fileUploadContext.uploadFileMutation.data?.data.key}`
    } else {
      return 'Oopsie woopsie'
    }
  }, [fileUploadContext.uploadFileMutation.data])

  const handleGoBackButton = useCallback(() => {
    fileUploadContext.uploadFileMutation.reset()
    uploadBoxContent.setCurrentBox(Box.DefaultUploadBox)
  }, [])

  const handleDeleteFileClick = useCallback(async () => {
    if (fileUploadContext.uploadFileMutation.data) {
      if (confirm('Are you sure you want to delete this file?')) {
        const response = await fileUploadContext.deleteFileMutation.mutateAsync({
          key: fileUploadContext.uploadFileMutation.data.data.key,
          data: { accessToken: fileUploadContext.uploadFileMutation.data.data.accessToken }
        })
        if (response.status === 200) {
          uploadBoxContent.setCurrentBox(Box.DefaultUploadBox)
          toast.success('Deleted successfully')
        }
      }
    }
  }, [fileUploadContext.deleteFileMutation])

  const fileUploadData = useMemo(() => {
    return fileUploadContext.uploadFileMutation.data?.data ?? null
  }, [fileUploadContext.uploadFileMutation.data])

  return {
    link,
    handleGoBackButton,
    handleDeleteFileClick,
    fileUploadData
  }
}

export default useFileUploadedBox
