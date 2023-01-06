import useDefaultUploadBox from './useDefaultUploadBox'
import DefaultUploadBoxView from './DefaultUploadBoxView'

const DefaultUploadBox = () => {
  const state = useDefaultUploadBox()

  return <DefaultUploadBoxView {...state} />
}

export default DefaultUploadBox
