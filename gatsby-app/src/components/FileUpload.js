import CustomFileUpload from './blocks/CustomFileUpload'
import React from 'react'
import Dropzone from 'react-dropzone'
import { faFolderOpen } from '@fortawesome/free-solid-svg-icons'
import axios from 'axios'

export const handleOnPaste = (props) => {
  if (props.isLoading || props.uploaded) return

  const items = props.event.clipboardData.items
  let blob = null

  for (let i = 0; i < items.length; i++) {
    if (items[i].type.indexOf('image') === 0) {
      blob = items[i].getAsFile()
    }
  }

  handleFileUpload({
    setLoader: props.setLoader,
    setUpload: props.setUpload,
    files: [blob],
  })
}

const handleFileUpload = async (props) => {
  try {
    props.setLoader({ isLoading: true, value: 0 })

    const data = new FormData()
    data.append('file', props.files[0])

    const response = await axios.post(`${process.env.GATSBY_API_URL ? process.env.GATSBY_API_URL : ''}/api/upload`, data, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      onUploadProgress: (value) => {
        props.setLoader({ isLoading: true, value: Math.round(value.loaded / value.total * 100) })
      },
    })
    props.setUpload({ uploaded: true, data: { ...response.data } })
  } catch (e) {
    props.setError({
      active: true,
      message: e.response ? e.response.data.message : e.toString(),
      status: e.response ? e.response.status : undefined,
    })
    props.setUpload({ uploaded: false, data: {} })
  } finally {
    props.setLoader({ isLoading: false, value: 100 })
  }
}

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

const FileUpload = (props) => (
  <Dropzone onDrop={files => handleFileUpload({ files, ...props })}>
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
