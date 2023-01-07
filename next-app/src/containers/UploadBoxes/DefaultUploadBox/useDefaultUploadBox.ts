import { useContext } from 'react'
import { FileUploadContext, TFileUploadContext } from '../../../providers/FileUploadProvider'

export interface IDefaultUploadBoxState {
  fileUpload: TFileUploadContext
}

const useDefaultUploadBox = (): IDefaultUploadBoxState => {
  const fileUpload = useContext(FileUploadContext)

  return {
    fileUpload
  }
}

export default useDefaultUploadBox
