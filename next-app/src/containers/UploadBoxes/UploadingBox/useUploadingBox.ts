import { useContext } from 'react'
import { FileUploadContext, TFileUploadContext } from '../../../providers/FileUploadProvider'

export interface IUseUploadingBoxState {
  fileUpload: TFileUploadContext
}

const useUploadingBox = (): IUseUploadingBoxState => {
  const fileUpload = useContext(FileUploadContext)

  return {
    fileUpload
  }
}

export default useUploadingBox
