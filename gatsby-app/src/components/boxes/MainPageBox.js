import useFileUpload from '../useFileUpload'
import Dropzone from 'react-dropzone'
import CustomFileUpload from '../blocks/CustomFileUpload'
import { faFolderOpen } from '@fortawesome/free-solid-svg-icons'
import React from 'react'
import { useDispatch } from 'react-redux'
import { setError, setPage, setResponse } from '../../redux/actions'
import { PAGE_ID } from '../../redux/constants'

const MainPageBox = () => {
  const fileUpload = useFileUpload()
  const dispatch = useDispatch()

  return (
    <Dropzone
      onDrop={files => {
        if (files.length > 0) {
          const data = new FormData()
          data.append('file', files[0])
          fileUpload.handleFileUpload({ file: data })
        } else {
          dispatch(
            setError({
              message: 'Invalid input, please make sure to upload a valid file',
              status: undefined,
            }),
          )
          dispatch(setResponse({ received: false, data: {} }))
          dispatch(setPage({ pageId: PAGE_ID.ERROR_PAGE }))
        }
      }}
      multiple={false}
    >
      {({ getRootProps, getInputProps }) => (
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
      )}
    </Dropzone>
  )
}

export default MainPageBox
