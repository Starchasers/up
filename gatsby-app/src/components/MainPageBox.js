import useFileUpload from './useFileUpload'
import Dropzone from 'react-dropzone'
import CustomFileUpload from './blocks/CustomFileUpload'
import { faFolderOpen } from '@fortawesome/free-solid-svg-icons'
import React from 'react'
import { getLoadingState, getResponseState } from '../redux/selectors'
import { connect } from 'react-redux'

const MainPageBox = (props) => {
  const fileUpload = useFileUpload(props)
  return (
    <Dropzone onDrop={files => fileUpload.handleFileUpload({ files })}>
      {({ getRootProps, getInputProps }) => (
        <CustomFileUpload onPaste={(event) => fileUpload.handleOnPaste(event)}>
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
      )}
    </Dropzone>
  )
}

const mapStateToProps = store => {
  const loading = getLoadingState(store)
  const response = getResponseState(store)
  return { loading, response }
}

export default connect(mapStateToProps)(MainPageBox)
