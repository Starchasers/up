import { Box } from '../../../@types/box'

import DefaultUploadBoxView from './DefaultUploadBox'
import UploadingBox from './UploadingBox'
import FileUploadedBox from './FileUploadedBox'

const UploadBoxes: { [key in Box]: { Component: () => JSX.Element } } = {
  [Box.DefaultUploadBox]: {
    Component: DefaultUploadBoxView
  },
  [Box.UploadingBox]: {
    Component: UploadingBox
  },
  [Box.FileUploadedBox]: {
    Component: FileUploadedBox
  }
}

export default UploadBoxes
