import CustomFileUpload from './blocks/CustomFileUpload'
import React from 'react'
import Dropzone from 'react-dropzone'
import { faFolderOpen } from '@fortawesome/free-solid-svg-icons'

const DropZoneInput = ({ getRootProps, getInputProps }) => (
  <CustomFileUpload.Container {...getRootProps()}>
    <CustomFileUpload.DropZone>
      <CustomFileUpload.Text bold>Drop files here</CustomFileUpload.Text>
    </CustomFileUpload.DropZone>
    <CustomFileUpload.Input {...getInputProps()}/>
  </CustomFileUpload.Container>
)

const ChooseFile = ({ getRootProps, getInputProps }) => (
  <CustomFileUpload.Container {...getRootProps()}>
    <CustomFileUpload.Button>
      <CustomFileUpload.Icon icon={faFolderOpen}/>
      <CustomFileUpload.Text>Choose File</CustomFileUpload.Text>
    </CustomFileUpload.Button>
    <CustomFileUpload.Input {...getInputProps()}/>
  </CustomFileUpload.Container>
)

const FileUpload = () => (
  <Dropzone onDrop={acceptedFiles => console.log(acceptedFiles)}>
    {(d) => (
      <CustomFileUpload>
        <DropZoneInput {...d} />
        <CustomFileUpload.Or>OR</CustomFileUpload.Or>
        <ChooseFile {...d} />
      </CustomFileUpload>
    )}
  </Dropzone>
)

export default FileUpload