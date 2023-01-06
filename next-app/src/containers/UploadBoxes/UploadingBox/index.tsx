import useUploadingBox from './useUploadingBox'
import UploadingBoxView from './UploadingBoxView'

const UploadingBox = () => {
  const state = useUploadingBox()

  return <UploadingBoxView {...state} />
}

export default UploadingBox
