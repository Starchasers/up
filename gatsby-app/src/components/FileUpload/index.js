import CustomFileUpload from '../blocks/CustomFileUpload'
import React from 'react'
import Dropzone from 'react-dropzone'
import { faFolderOpen } from '@fortawesome/free-solid-svg-icons'
import { connect } from 'react-redux'
import useFileUpload from './useFileUpload'
import { setError, setLoading, setResponse } from '../../redux/actions'

const DropZoneInput = ({ getRootProps, getInputProps }) => (
  <CustomFileUpload.Container {...getRootProps()} style={{ width: '80%' }}>
    <CustomFileUpload.DropZone>
      <CustomFileUpload.Text bold>Drop file here</CustomFileUpload.Text>
    </CustomFileUpload.DropZone>
    <CustomFileUpload.Input {...getInputProps()}/>
  </CustomFileUpload.Container>
)

const ChooseFile = ({ getRootProps, getInputProps }) => (
  <CustomFileUpload.Container {...getRootProps()}>
    <CustomFileUpload.Button>
      <CustomFileUpload.Icon icon={faFolderOpen} style={{ width: '22.5px' }}/>
      <CustomFileUpload.Text>Choose File</CustomFileUpload.Text>
    </CustomFileUpload.Button>
    <CustomFileUpload.Input {...getInputProps()}/>
  </CustomFileUpload.Container>
)

const FileUpload = (props) => {
  const state = useFileUpload(props)
  return (
    <Dropzone onDrop={files => state.handleFileUpload({ files })}>
      {(d) => (
        <CustomFileUpload onPaste={(event) => state.handleOnPaste(event)}>
          <DropZoneInput {...d} />
          <CustomFileUpload.Or>OR</CustomFileUpload.Or>
          <ChooseFile {...d} />
        </CustomFileUpload>
      )}
    </Dropzone>
  )
}

export default connect(null, { setLoading, setError, setResponse })(FileUpload)
