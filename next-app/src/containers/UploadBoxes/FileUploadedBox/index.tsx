import useFileUploadedBox from './useFileUploadedBox'
import FileUploadedBoxView from './FileUploadedBoxView'

const FileUploadedBox = () => {
  const state = useFileUploadedBox()

  return <FileUploadedBoxView {...state} />
}

export default FileUploadedBox
