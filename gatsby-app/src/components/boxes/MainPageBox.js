import CustomFileUpload from '../blocks/CustomFileUpload'
import { faFolderOpen } from '@fortawesome/free-solid-svg-icons'
import React, { useContext } from 'react'
import { FileUploadContext } from '../../providers/file-upload-provider'

const MainPageBox = () => {
  const { getRootProps, getInputProps } = useContext(FileUploadContext)
  return (
    <CustomFileUpload>
      <CustomFileUpload.Container {...getRootProps()} style={{ width: '80%' }}>
        <CustomFileUpload.DropZone>
          <CustomFileUpload.Text bold>Drop file here</CustomFileUpload.Text>
        </CustomFileUpload.DropZone>
        <CustomFileUpload.Input {...getInputProps()}/>
      </CustomFileUpload.Container>
      <CustomFileUpload.Or>OR</CustomFileUpload.Or>
      <CustomFileUpload.Container {...getRootProps()}>
        <CustomFileUpload.Button>
          <CustomFileUpload.Icon icon={faFolderOpen} style={{ width: '22.5px' }}/>
          <CustomFileUpload.Text>Choose File</CustomFileUpload.Text>
        </CustomFileUpload.Button>
        <CustomFileUpload.Input {...getInputProps()}/>
      </CustomFileUpload.Container>
    </CustomFileUpload>
  )
}

export default MainPageBox
